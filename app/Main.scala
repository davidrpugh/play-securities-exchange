import akka.actor.{PoisonPill, Props, ActorSystem}
import models.Reaper.WatchMe
import models._

import scala.util.Random
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App
  with SecuritiesProvider {

  val prng = new Random(42)

  def generateRandomAssets(maxQuantity: Double = 1e6): List[Assets] = {
    securities.map(security => Assets(security, generateRandomQuantity(maxQuantity)))
  }

  def generateRandomCurrency(maxQuantity: Double = 1e6): Assets = {
    Assets(Currency, generateRandomQuantity(maxQuantity))
  }

  def generateRandomQuantity(maxQuantity: Double = 1e6): Double = {
    prng.nextDouble() * maxQuantity
  }

  val tradingPlatform = ActorSystem("Trading-Platform")
  val securitiesExchange = tradingPlatform.actorOf(Props[SecuritiesExchange])
  val noiseTrader1 = tradingPlatform.actorOf(Props(classOf[NoiseTrader], securitiesExchange, prng))

  // Initialize the reaper
  val reaper = tradingPlatform.actorOf(Props[ProductionReaper])
  reaper ! WatchMe(securitiesExchange)
  reaper ! WatchMe(noiseTrader1)

  // Initialize NoiseTrader
  val initialAssets = generateRandomAssets(Double.PositiveInfinity)
  initialAssets foreach(asset => noiseTrader1 ! asset)

  val initialCurrency = generateRandomCurrency(Double.PositiveInfinity)
  noiseTrader1 ! initialCurrency

  noiseTrader1 ! StartTrading


  tradingPlatform.scheduler.scheduleOnce(2.minute) {
    securitiesExchange ! PoisonPill
  }

  tradingPlatform.scheduler.scheduleOnce(4.minute) {
    noiseTrader1 ! PoisonPill
  }
}