package ua.sample.classic.async

import scala.concurrent.{ExecutionContext, Future}

trait UserDAO {

  implicit val ec: ExecutionContext
  val storage: UserStorage

  def addUser(user: User): Future[Unit]
  def getUser(id: UserId): Future[Option[User]]
}

class UserDaoImpl(val storage: UserStorage)
                 (implicit val ec: ExecutionContext) extends UserDAO {

  def addUser(user: User): Future[Unit] = Future(storage(user.id) = user)
  def getUser(id: UserId): Future[Option[User]] = Future(storage.get(id))

}
