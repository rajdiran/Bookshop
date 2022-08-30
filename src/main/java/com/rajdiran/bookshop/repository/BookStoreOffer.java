package com.rajdiran.bookshop.repository;

import java.util.ArrayList;
import java.util.List;

import com.rajdiran.bookshop.model.Offer;

public class BookStoreOffer implements OfferStore {

	private List<Offer> offers;

	public BookStoreOffer() {
		this.offers = new ArrayList<Offer>();
	}

	@Override
	public void storeOffer(Offer offer) {
		offers.add(offer);
	}

	@Override
	public List<Offer> getOffers() {
		return offers;
	}

}
