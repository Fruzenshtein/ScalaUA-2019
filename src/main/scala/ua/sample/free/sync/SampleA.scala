package ua.sample.free.sync

import cats.free.Free
import cats.free.Free.liftF

object SampleA extends App {

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
  import cats.{Id, ~>}
  import scala.collection.mutable

  def compiler(): UserOps ~> Id = new (UserOps ~> Id) {

    val userStorage = mutable.Map.empty[UserId, User]

    override def apply[A](fa: UserOps[A]): Id[A] = fa match {
      case Add(user) => userStorage(user.id) = user
      case Get(id) => userStorage.get(id)
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
  computations.foldMap(compiler)

}
