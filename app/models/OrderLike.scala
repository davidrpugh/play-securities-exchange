package models

import akka.actor.ActorRef


/** Trait representing an order for a particular security.
  *
  * The OrderLike trait should be mixed in with each specific type of order
  * (i.e., ask orders, bid orders, limit orders, market orders, etc).
  *
  */
trait OrderLike {

  /** Whether the order is buy (true) or sell (false). */
  def buy: Boolean

  /** The unique identifier of the security. */
  def instrument: SecurityLike

  /** The quantity being bought or sold. */
  def quantity: Double

  /** Orders will often need to be split during the matching process. */
  def split(newQuantity: Double): OrderLike

  /** String representation of an order. */
  def toString: String

  /** The trading party for the order. */
  def tradingPartyRef: ActorRef

}
