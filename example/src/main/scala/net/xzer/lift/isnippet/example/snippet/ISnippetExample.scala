package net.xzer.lift.isnippet.example.snippet

import java.util.concurrent.TimeUnit
import net.liftweb.http.S
import net.liftweb.util.Helpers._
import net.liftweb.common.Logger


object ISnippetExample extends Logger{

  private def doSleepWork(seconds: Int) = {
    val msg = "We spend %d second(s) to finish the job".format(seconds)
    debug(msg)
    TimeUnit.SECONDS.sleep(seconds)
    msg
  }
  
  def renderAfterSleep = {
    val sleepTime = S.attr("sleep").open_!.toInt
    "* *" #> doSleepWork(sleepTime)
  }
  
  def render = {
    "* *" #> "from render"
  }
  
}