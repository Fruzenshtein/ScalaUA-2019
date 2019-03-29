package ua.sample.free.compose

import scala.collection.mutable

object SampleC extends App {

  import cats.data.EitherK
  import cats.free.Free
  import cats.{Id, InjectK, ~>}

  /**
    * Describe algebras for User and Offer
    */
  sealed trait UserOps[A]
  final case class AddUser(user: User) extends UserOps[Unit]
  final case class GetUser(id: UserId) extends UserOps[Option[User]]

  sealed trait OfferOps[A]
  final case class AddOffer(offer: Offer) extends OfferOps[Unit]
  final case class GetOffer(id: OfferId) extends OfferOps[Option[Offer]]
  final case class DeleteOffer(id: OfferId) extends OfferOps[Unit]

  /**
    * Define a program
    */
  type UserOfferApp[A] = EitherK[UserOps, OfferOps, A]

  /**
    * Create Free[_] types for ADTs
    */
  class UserOperations[F[_]](implicit I: InjectK[UserOps, F]) {
    def addUser(user: User): Free[F, Unit] = Free.inject[UserOps, F](AddUser(user))
    def getUser(id: UserId): Free[F, Option[User]] = Free.inject[UserOps, F](GetUser(id))
  }

  object UserOperations {
    implicit def userOperations[F[_]](implicit I: InjectK[UserOps, F]): UserOperations[F] =
      new UserOperations[F]
  }

  class OfferOperations[F[_]](implicit I: InjectK[OfferOps, F]) {
    def addOffer(offer: Offer): Free[F, Unit] = Free.inject[OfferOps, F](AddOffer(offer))
    def getOffer(id: OfferId): Free[F, Option[Offer]] = Free.inject[OfferOps, F](GetOffer(id))
    def deleteOffer(id: OfferId): Free[F, Unit] = Free.inject[OfferOps, F](DeleteOffer(id))
  }

  object OfferOperations {
    implicit def offerOperations[F[_]](implicit I: InjectK[OfferOps, F]): OfferOperations[F] =
      new OfferOperations[F]
  }

  /**
    * Compose ADTs into one program
    */
  def program(implicit UO: UserOperations[UserOfferApp],
                       OO: OfferOperations[UserOfferApp]): Free[UserOfferApp, Unit] = {
    import  UO._, OO._
    for {
      _ <- addUser(User("uid01", "Alex"))
      _ <- addOffer(Offer(1, "uid01", "10% discount", true))
      addedOffer <- getOffer(1)
      _ <- deleteOffer(1)
      deletedOffer <- getOffer(1)
    } yield {
      println(addedOffer)
      println(deletedOffer)
    }
  }

  /**
    * Interpreters
    */
  object UserOpsInterpreter extends (UserOps ~> Id) {
    val userStorage = mutable.Map.empty[UserId, User]
    override def apply[A](fa: UserOps[A]) = fa match {
      case AddUser(user) => userStorage(user.id) = user
      case GetUser(id) => userStorage.get(id)
    }
  }

  object OfferOpsInterpreter extends (OfferOps ~> Id) {
    val offerStorage = mutable.Map.empty[OfferId, Offer]
    override def apply[A](fa: OfferOps[A]) = fa match {
      case AddOffer(offer) => offerStorage(offer.id) = offer
      case GetOffer(id) => offerStorage.get(id)
      case DeleteOffer(id) => offerStorage -= id
        ()
    }
  }

  val mainInterpreter: UserOfferApp ~> Id = UserOpsInterpreter or OfferOpsInterpreter

  /**
    * Run the program
    */
  import UserOperations._, OfferOperations._

  program.foldMap(mainInterpreter)

}
