package net.xzer.lift.isnippet.util

import net.liftweb.util.Helpers._
import scala.xml.NodeSeq
import scala.xml.Text

object ISnippetRule {
  
  @volatile var defaultWaitFor: TimeSpan = 5 seconds
  
  @volatile var defaultLoadingMessage: NodeSeq = new Text("Loading...")
  
  @volatile var defaultLoadingMessageWrapperCssClassName: String = ""
}