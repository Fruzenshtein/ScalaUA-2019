package ua.sample.classic.sync

trait OfferDAO {

  val storage: OfferStorage

  def addOffer(offer: Offer): Unit
  def getOffer(offerId: OfferId): Option[Offer]
  def deleteOffer(offerId: OfferId): Unit

}

class OfferDaoImpl(val storage: OfferStorage) extends OfferDAO {

  override def addOffer(offer: Offer): Unit =
    storage(offer.id) = offer

  override def getOffer(offerId: OfferId): Option[Offer] =
    storage.get(offerId)

  override def deleteOffer(offerId: OfferId): Unit =
    storage -= offerId

}
