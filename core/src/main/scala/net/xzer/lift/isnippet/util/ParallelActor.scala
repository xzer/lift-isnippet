package net.xzer.lift.isnippet.util

import net.liftweb.actor.LiftActor

case class Executor(f:()=>Unit)

class ParalleActor extends LiftActor{
  def messageHandler = {
    case Executor(f) => f()
  }
}