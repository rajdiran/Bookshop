package com.rajdiran.bookshop.repository;

import java.util.List;

import com.rajdiran.bookshop.model.Offer;

public interface OfferStore {

	void storeOffer(Offer offer);

	List<Offer> getOffers();

}
