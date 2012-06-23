package net.xzer.lift.isnippet.snippet

import net.liftweb.util.Helpers._
import scala.xml.NodeSeq
import net.liftweb.http.S
import net.liftweb.common.Full
import scala.xml.Elem
import scala.xml.UnprefixedAttribute
import scala.xml.Null
import scala.xml.PrefixedAttribute
import scala.xml.TopScope
import net.liftweb.common.Box
import net.liftweb.common.Empty
import net.liftweb.http.RenderVersionHelper
import java.util.concurrent.CountDownLatch
import net.liftweb.http.LiftSession
import net.xzer.lift.isnippet.util.Executor
import net.xzer.lift.isnippet.util.ParalleActor
import java.util.concurrent.TimeUnit
import net.liftweb.util.Helpers
import net.xzer.lift.isnippet.comet.ISnippetRenderComet
import net.xzer.lift.isnippet.comet.ISnippetRender
import net.liftweb.common.Logger
import net.xzer.lift.isnippet.util.ISnippetRule

/**
 * we let the snippet's name can be compatible with HTML5 rendering
 */
object Isnippet extends ISnippetBase{}

trait ISnippetBase extends Logger{
  
  /*
  protected implicit def pimp(elem: Elem) = new {
    def %(attrs: collection.Map[String, String]) = {
      val seq = for ((n, v) <- attrs) yield createAttribute(n, v)
      (elem /: seq)(_ % _)
    }

    def %(attr: (String, String)) = {
      val a = createAttribute(attr._1, attr._2)
      elem % a
    }
    private def createAttribute(k: String, v: String) = {
      if ((k.indexOf(":")) < 0)
        new UnprefixedAttribute(k, v, Null)
      else {
        val kk = k.split(":")
        new PrefixedAttribute(kk(0), kk(1), v, Null)
      }
    }
  }
  
  private def createSnippetTag(snippetName:String) = {
    new Elem("lift", snippetName, Null, TopScope)
  }
  */
  
  private class ResultHolder{
      @volatile var result: Box[NodeSeq] = Empty
  }
  
  def render(in: NodeSeq):NodeSeq = {
    //it seems that it is difficult to pass the attributes safely
    //so commet it.
    /*
    val target:NodeSeq = S.attr("target") match {
      case Full(t) => createSnippetTag(t)
      case _ => in
    }
    */
    doRender(in)
  }
  
  private def doRender(in:NodeSeq) = {
    //debug("ready to render:" + in)
    val req = S.request.map(_.snapshot)
    val renderVersion = RenderVersionHelper.get
    
    val resultHolder = new ResultHolder
    
    val counter = new CountDownLatch(1)
    val ls: LiftSession = S.session.open_!
    val executor = Executor(()=>{
      val ret = ls.executeInScope(req, renderVersion){
        ls.processSurroundAndInclude("empty", in)
      }
      resultHolder.result = Full(ret)
      counter.countDown()
    })
    
    (new ParalleActor) ! executor
    
    val waitFor = S.attr("waitFor") match {
      case Full(wf) => wf.toInt
      case _ => ISnippetRule.defaultWaitFor.toMillis / 1000
    }
    
    counter.await(waitFor, TimeUnit.SECONDS)
    
    val ret = resultHolder.result
    ret match {
      case Full(r) => r
      case _ => doLazyLoad(counter, resultHolder)
    }
  }
  
  private def doLazyLoad(counter:CountDownLatch, resultHolder:ResultHolder):NodeSeq = {
    
    val id = Helpers.nextFuncName
    
    val executor = Executor(()=>{
      counter.await()
      val ret = resultHolder.result.open_!
      val map = ISnippetRenderComet.cometMap
      var actor:ISnippetRenderComet = null;
      while(actor == null){
        actor = map.get(id)
        if(actor == null) Thread.sleep(1000L)
      }
      map.remove(id)
      actor ! ISnippetRender(id, ret)
    })
    
    (new ParalleActor) ! executor
    
    val template = S.attr("message") openOr ("")
    
    val loadingMessage = S.attr("message") match {
      case Full(template) => <lift:embed what={template}/>
      case _ => ISnippetRule.defaultLoadingMessage
    }
    
    <xml:group>
      <div id={id} class={ISnippetRule.defaultLoadingMessageWrapperCssClassName}>{loadingMessage}</div>
      <lift:comet type={ISnippetRule.defaultCometType} name={id}/>
    </xml:group>
  }  

}