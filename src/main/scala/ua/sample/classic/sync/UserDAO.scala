package ua.sample.classic.sync

trait UserDAO {

  val storage: UserStorage

  def addUser(user: User): Unit
  def getUser(id: UserId): Option[User]
}

class UserDaoImpl(val storage: UserStorage) extends UserDAO {

  def addUser(user: User): Unit = storage(user.id) = user
  def getUser(id: UserId): Option[User] = storage.get(id)

}
