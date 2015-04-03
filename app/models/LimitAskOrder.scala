package models

import akka.actor.ActorRef


/** Represents a limit ask order.
  *
  * A LimitAskOrder represents an order to sell a certain quantity of shares of a
  * specific security at prices greater than or equal to some limit price.
  *
  * @param tradingPartyRef ActorRef for the trading party submitting the order.
  * @param instrument Security for which the order is being placed.
  * @param price Limit price for the order.
  * @param quantity Desired quantity of the security.
  */
case class LimitAskOrder(tradingPartyRef: ActorRef,
                         instrument: String,
                         price: Double,
                         quantity: Int) extends
  AskOrderLike with
  PricedOrderLike {

  /** Crossing logic for a limit ask order.
    *
    * @param bid some bid order.
    * @return true if the limit order ask cross the bid; false otherwise.
    */
  def crosses(bid: BidOrderLike): Boolean = bid match {
    case bid: PricedOrderLike => price <= bid.price
  }

  /** Price formation rules for limit ask orders.
    *
    * @param bid some bid order.
    * @return the trade price between a limit ask order and some bid order.
    */
  def formPrice(bid: BidOrderLike): Double = bid match {
    case bid: PricedOrderLike => bid.price
  }

  /** Split a limit ask order
    *
    * @param newQuantity Desired quantity for the new order.
    * @return new limit order ask.
    */
  def split(newQuantity: Int): OrderLike = {
    LimitAskOrder(tradingPartyRef, instrument, price, newQuantity)
  }

  /** String representation of a limit order. */
  override def toString: String = {
    s",${tradingPartyRef.path.name},$getClass,$instrument,$price,$quantity"
  }

}

