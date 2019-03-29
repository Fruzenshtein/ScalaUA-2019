package ua.sample.free

package object compose {

  type UserId = String
  type Name = String

  type OfferId = Int
  type Message = String

  final case class User(id: UserId, name: Name)
  final case class Offer(id: OfferId, userId: UserId, message: Message, active: Boolean)

  type UserStorage = collection.mutable.Map[UserId, User]
  type OfferStorage = collection.mutable.Map[OfferId, Offer]

}
