package com.rajdiran.bookshop.model;

public class Offer {

	private OfferType offerType;
	private int discount; // discount %
	private int offerTypeValue; // For Eg., offer applicable if year > 2000

	public Offer(OfferType offerType, int discount, int offerTypeValue) {
		this.offerType = offerType;
		this.discount = discount;
		this.offerTypeValue = offerTypeValue;
	}

	public int getDiscount() {
		return discount;
	}

	public OfferType getOfferType() {
		return offerType;
	}

	public int getOfferTypeValue() {
		return offerTypeValue;
	}

}
