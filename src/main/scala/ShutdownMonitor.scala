import akka.actor.{Actor, ActorRef, ActorSystem, Terminated}

class ShutdownMonitor(op: ActorRef, as: ActorSystem) extends Actor {
  context.watch(op)

  def receive: PartialFunction[Any, Unit] = {
    case Terminated(op) => as.terminate()
    case _ => println("Unexpected message.")
  }

}
