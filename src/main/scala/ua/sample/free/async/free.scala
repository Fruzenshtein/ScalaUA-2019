package ua.sample.free

package object async {

  type UserId = String
  type Name = String

  final case class User(id: UserId, name: Name)

  type DataSource = Map[UserId, User]

}
