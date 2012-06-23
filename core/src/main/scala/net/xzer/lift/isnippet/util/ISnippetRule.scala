package net.xzer.lift.isnippet.util

import net.liftweb.util.Helpers._
import scala.xml.NodeSeq
import scala.xml.Text

object ISnippetRule {
  
  /**
   * the default name of lazy load comet class
   * 
   * if we do not want to use the default name, a extended sub class can be used
   * and this type should be specified as well.
   */
  @volatile var defaultCometType: String = "ISnippetRenderComet"
  
  /**
   * the default waiting time for a lazy load judgment threshold
   * 
   * if a snippet executing over than this time, it will be replaced by a 
   * "on loading" message in output
   */
  @volatile var defaultWaitFor: TimeSpan = 5 seconds
  
  /**
   * the default shown message when a snippet has transcended the waiting time defined
   */
  @volatile var defaultLoadingMessage: NodeSeq = new Text("Loading...")
  
  /**
   * the default css class of "on loading" message
   * 
   * By default, the "on loading" message is wrapped by a div tag, this css definition
   * can be used to custormise the style the outer div tag.
   */
  @volatile var defaultLoadingMessageWrapperCssClassName: String = ""
}