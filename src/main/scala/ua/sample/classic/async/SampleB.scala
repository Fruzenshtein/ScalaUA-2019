package ua.sample.classic.async

import scala.collection.mutable
import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._

object SampleB extends App {

  implicit val ec = ExecutionContext.global

  val userDB = mutable.Map.empty[UserId, User]
  val userDao = new UserDaoImpl(userDB)

  val offerDB = mutable.Map.empty[OfferId, Offer]
  val offerDao = new OfferDaoImpl(offerDB)

  val result = for {
    _ <- userDao.addUser(User("uid01", "Alex"))
    _ <- offerDao.addOffer(Offer(1, "uid01", "10% discount", false))
    offerAdded <- offerDao.getOffer(1)
    _ <- offerDao.deleteOffer(1)
    offerDeleted <- offerDao.getOffer(1)
  } yield {
    println(offerAdded)
    println(offerDeleted)
  }

  Await.result(result, 1.second)

}
