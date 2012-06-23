package net.xzer.lift.isnippet.comet

import scala.xml.NodeSeq
import net.liftweb.http.CometActor
import net.liftweb.http.S
import scala.xml.Group
import net.liftweb.util.Schedule
import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmd
import net.liftweb.http.ShutDown
import net.liftweb.common.Box
import net.liftweb.common.Full
import net.liftweb.common.Logger
import java.util.concurrent.ConcurrentHashMap
import ISnippetRenderComet._

case class ISnippetRender(id:String, xml:NodeSeq)

object ISnippetRenderComet{
  val cometMap = new ConcurrentHashMap[String, ISnippetRenderComet]
}

/**
 * The Comet Actor for sending down the computed page fragments
 *
 */
class ISnippetRenderComet extends CometActor with Logger{
  
  override def lifespan: Box[TimeSpan] = Full(60 seconds)
  
  //private 
  
  override def localSetup = {
    super.localSetup()
    val id:String = name.open_!
    cometMap.put(id, this)
    debug("setuped:" + id)
  }
  
  override def localShutdown(): Unit = {
    val id:String = name.open_!
    cometMap.remove(id)
    debug("going to shutdown:" + id)
    super.localShutdown()
  }

  def render = NodeSeq.Empty

  override def lowPriority : PartialFunction[Any, Unit] = {
    case ISnippetRender(id, xml) => {
      partialUpdate(createUpdateJs(id, xml))
      //Schedule.schedule(() => this ! ShutDown, 10 seconds)
    }
  }
  
  private def createUpdateJs(id:String, in:NodeSeq) = {
    val w = new java.io.StringWriter
    val newNodes = Group(in)
    S.htmlProperties.htmlWriter(newNodes, w)
    val htmlStr = w.toString().encJs
    
    var ret =
      """
        try {
        var parent1 = document.getElementById(""" + id.encJs + """);
        parent1.innerHTML = """ + htmlStr + """;
        for (var i = 0; i < parent1.childNodes.length; i++) {
          var node = parent1.childNodes[i];
          parent1.parentNode.insertBefore(node.cloneNode(true), parent1);
        }
        parent1.parentNode.removeChild(parent1);
        } catch (e) {
          // if the node doesn't exist or something else bad happens
        }
      """
    new JsCmd{
      override val toJsCmd = ret
    }
  }
}
