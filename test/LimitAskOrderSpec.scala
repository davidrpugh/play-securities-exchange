import akka.actor.ActorSystem
import akka.testkit.TestKit
import models.LimitAskOrder
import org.scalatest.{GivenWhenThen, FeatureSpecLike, Matchers}

import scala.util.Random


class LimitAskOrderSpec extends TestKit(ActorSystem("TestSystem")) with
  FeatureSpecLike with
  GivenWhenThen with
  Matchers {

  /** Shutdown TestSystem after running tests. */
  def afterAll(): Unit = {
    system.shutdown()
  }

  /** Maximum share price for testing. */
  val maxPrice = 1000.0

  /** Maximum number of share for testing. */
  val maxQuantity = 1000000

  feature("LimitAskOrder should be able to split itself.") {

    scenario("A LimitAskOrder needs to be split.") {

      Given("some limit ask order")

      val price = Random.nextDouble() * maxPrice
      val quantity = Random.nextInt(maxQuantity)
      val limitAskOrder = LimitAskOrder(testActor, "GOOG", price, quantity)

      Then("that limit ask order should be able to split itself.")

      val newQuantity = quantity % Random.nextInt(quantity)
      val splitLimitAskOrder = LimitAskOrder(testActor, "GOOG", price, newQuantity)

      limitAskOrder.split(newQuantity) should be (splitLimitAskOrder)
    }

  }

}