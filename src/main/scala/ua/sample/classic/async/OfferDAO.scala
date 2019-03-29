package ua.sample.classic.async

import scala.concurrent.{ExecutionContext, Future}

trait OfferDAO {

  val storage: OfferStorage
  implicit val ec: ExecutionContext

  def addOffer(offer: Offer): Future[Unit]
  def getOffer(offerId: OfferId): Future[Option[Offer]]
  def deleteOffer(offerId: OfferId): Future[Unit]

}

class OfferDaoImpl(val storage: OfferStorage)
                  (implicit val ec: ExecutionContext) extends OfferDAO {

  override def addOffer(offer: Offer): Future[Unit] = Future {
    storage(offer.id) = offer
  }

  override def getOffer(offerId: OfferId): Future[Option[Offer]] = Future {
    storage.get(offerId)
  }

  override def deleteOffer(offerId: OfferId): Future[Unit] = Future {
    storage -= offerId
  }

}
