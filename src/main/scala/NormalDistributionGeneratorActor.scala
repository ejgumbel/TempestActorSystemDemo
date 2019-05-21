import akka.Done
import akka.actor.{Actor, ActorRef, PoisonPill}
import org.apache.commons.math3.distribution.NormalDistribution

class NormalDistributionGeneratorActor(private val downstream: ActorRef) extends Actor {

  var defaultSampleSize: GeneratorSampleSizeParameter = GeneratorSampleSizeParameter(10)

  def receive: PartialFunction[Any, Unit] = {
    case generatorParameters: NormalDistributionGeneratorParameters => downstream ! performSample(generatorParameters)
    case normParam: NormalDistributionParameters => downstream !
      performSample(NormalDistributionGeneratorParameters(normParam, defaultSampleSize))
    case n: GeneratorSampleSizeParameter => updateDefaultSampleSize(n)
    case Done =>
      downstream ! Done
      self ! PoisonPill
    case _ => println("Got something unexpected.")
  }

  def performSample(generatorParameters: NormalDistributionGeneratorParameters): UnorderedContinuousSeries = {
    println(generatorParameters.parameters.location)
    println(generatorParameters.parameters.scale)
    val dist: NormalDistribution = new NormalDistribution(generatorParameters.parameters.location,
      generatorParameters.parameters.scale)
    UnorderedContinuousSeries(for (_ <- 1 to generatorParameters.sampleSize.n) yield dist.sample())
  }

  def validateParameters(generatorParameters: NormalDistributionGeneratorParameters): NormalDistributionGeneratorParameters = {
    generatorParameters
    //In the actual implementation some validation logic would go here. The parameters should pass through if they are
    //ok but if there's something wrong a message would bubble up to the top
  }

  private def updateDefaultSampleSize(n: GeneratorSampleSizeParameter): Unit = {
    defaultSampleSize = n
  }
}
