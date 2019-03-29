package ua.sample.free

package object sync {

  type UserId = String
  type Name = String

  final case class User(id: UserId, name: Name)

  type DataSource = Map[UserId, User]

}
