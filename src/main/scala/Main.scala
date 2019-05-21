import akka.Done
import akka.actor.{ActorSystem, Props}

object Main extends App {
  val system = ActorSystem("Main")

  val op = system.actorOf(Props[OutputActor], "Output")
  val generator2 = system.actorOf(Props(new NormalDistributionGeneratorActor(op)), name = "Generator2")
  val estimator1 = system.actorOf(Props(new NormalMomentsEstimatorActor(generator2)), "Estimator1")
  val generator1 = system.actorOf(Props(new NormalDistributionGeneratorActor(estimator1)), "Generator1")
  val monitor = system.actorOf(Props(new ShutdownMonitor(op, system)), name = "ShutdownMonitor")

  generator1 ! NormalDistributionGeneratorParameters(NormalDistributionParameters(0, 1), GeneratorSampleSizeParameter(1000))
  generator1 ! Done
}
