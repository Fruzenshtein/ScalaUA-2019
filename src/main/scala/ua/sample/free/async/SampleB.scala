package ua.sample.free.async

import cats.free.Free
import cats.free.Free.liftF

import scala.concurrent.{Await, Future}

object SampleB extends App {

  // Create an ADT representing your grammar
  sealed trait UserOps[A]
  final case class Add(user: User) extends UserOps[Unit]
  final case class Get(id: UserId) extends UserOps[Option[User]]

  /**
    * 1. Create a Free type based on your ADT
    * Free[_] serves for creation of DSL
    */
  type UserStorage[A] = Free[UserOps, A]

  /**
    * 2. Create smart constructors using liftF
    */
  def add(user: User): UserStorage[Unit] = liftF[UserOps, Unit](Add(user))
  def get(id: UserId): UserStorage[Option[User]] = liftF[UserOps, Option[User]](Get(id))

  /**
   * 3. Compiler
   */
  import cats.~>
  import cats.implicits._

  import scala.collection.mutable
  import scala.concurrent.ExecutionContext.Implicits.global

  def compiler: UserOps ~> Future = new (UserOps ~> Future) {

    val userStorage = mutable.Map.empty[UserId, User]

    override def apply[A](fa: UserOps[A]): Future[A] = fa match {
      case Add(user) => Future(userStorage(user.id) = user)
      case Get(id) => Future(userStorage.get(id))
    }
  }

  /**
    * 4. Compose operations into a business logic
    */
  val computations =
    for {
      _ <- add(User("uid01", "Alex"))
      user <- get("uid01")
    } yield {
      println(user)
    }

  /**
    * 5. Run the computations
    */
  import scala.concurrent.duration._

  Await.result(computations.foldMap(compiler), 1.second)

}
