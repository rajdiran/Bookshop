package com.rajdiran.bookshop.services;

import com.rajdiran.bookshop.model.Offer;

public interface OffersService {

	Boolean hasOfferFor(String stockKeepingUnit);

	void registerOffer(Offer offer);

}
