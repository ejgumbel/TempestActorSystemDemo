import akka.Done
import akka.actor.{Actor, PoisonPill}

class OutputActor extends Actor {

  def receive: PartialFunction[Any, Unit] = {
    case x: UnorderedContinuousSeries => dumpToConsole(x)
    case Done => self ! PoisonPill
    case _ => println("Got something")
  }

  def dumpToConsole(x: UnorderedContinuousSeries): Unit = {
    x.values.toStream.foreach(xi => print(f"$xi%2.3f \n"))
  }

}
