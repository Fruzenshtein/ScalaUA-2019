package ua.sample.classic

package object async {

  type UserId = String
  type Name = String

  type OfferId = Int
  type Message = String

  final case class User(id: UserId, name: Name)
  final case class Offer(id: OfferId, userId: UserId, message: Message, active: Boolean)

  type UserStorage = collection.mutable.Map[UserId, User]
  type OfferStorage = collection.mutable.Map[OfferId, Offer]

}
