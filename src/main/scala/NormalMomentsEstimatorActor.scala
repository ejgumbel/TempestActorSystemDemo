import akka.Done
import akka.actor.{Actor, ActorRef, PoisonPill}

class NormalMomentsEstimatorActor(private val downstream: ActorRef) extends Actor {

  def receive: PartialFunction[Any, Unit] = {
    case x: UnorderedContinuousSeries => downstream ! getParameters(x)
    case Done => {
      downstream ! Done
      self ! PoisonPill
    }
    case _ => println("Got unexpected message.")
  }

  private def computeMean(x: UnorderedContinuousSeries): Double = {
    x.values.sum / x.values.size
  }

  private def computeSD(x: UnorderedContinuousSeries): Double = {
    val mean = computeMean(x)
    x.values.map(a => math.pow(a - mean, 2)).sum / x.values.size
  }

  def getParameters(x: UnorderedContinuousSeries): NormalDistributionParameters = {
    NormalDistributionParameters(computeMean(x), computeSD(x))
  }

}
