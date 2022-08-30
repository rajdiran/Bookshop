package com.rajdiran.bookshop.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.rajdiran.bookshop.exceptions.ItemNotFoundException;
import com.rajdiran.bookshop.model.BookItem;
import com.rajdiran.bookshop.model.Offer;
import com.rajdiran.bookshop.repository.BookStore;
import com.rajdiran.bookshop.repository.BookStoreOffer;

public class Checkout {

	private HashMap<String, Integer> cartItems;
	private BookStore bookStore;
	private BookStoreOffer bookStoreOffer;
	private Offer storeOffer;

	public Checkout(BookStore bookStore, BookStoreOffer bookStoreOffer, Offer storeOffer) {
		this.cartItems = new HashMap<String, Integer>();
		this.bookStore = bookStore;
		this.bookStoreOffer = bookStoreOffer;
		this.storeOffer = storeOffer;
	}

	public void addBookStore(Offer offer) {
		this.bookStoreOffer.storeOffer(offer);
	}

	public void setStoreOffer(Offer storeOffer) {
		this.storeOffer = storeOffer;
	}

	public BigDecimal calculateTotal() {
		return cartItems.entrySet().stream().map(entry -> {
			try {
				// Multiply number of the same item we've added with their price
				return bookStore.retrieveItemPrice(entry.getKey()).multiply(new BigDecimal(entry.getValue()));
			} catch (ItemNotFoundException e) {
				return BigDecimal.ZERO;
			}
		}).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_EVEN);

	}

	public BigDecimal calculateAmountToPay() {
		BigDecimal amountPayable = BigDecimal.ZERO;
		List<BigDecimal> priceList = new ArrayList<BigDecimal>();
		bookStoreOffer.getOffers().forEach(offer -> {
			switch (offer.getOfferType()) {
			case PUBLISHED_BY_YEAR:
				cartItems.entrySet().forEach(entry -> {
					BookItem bookItem = (BookItem) bookStore.getItem(entry.getKey());
					if (bookItem.getYear() > offer.getOfferTypeValue()) {

						BigDecimal numberOfItemsPrice = bookItem.getUnitPrice()
								.multiply(BigDecimal.valueOf(entry.getValue()));
						BigDecimal discountValue = (numberOfItemsPrice
								.multiply(BigDecimal.valueOf(offer.getDiscount()))).divide(BigDecimal.valueOf(100));

						priceList.add(numberOfItemsPrice.subtract(discountValue));
					} else {
						priceList.add(bookItem.getUnitPrice().multiply(BigDecimal.valueOf(entry.getValue())));
					}
				});
				break;
			default:
				break;
			}
		});

		// Updating amountPayable after applying item specific offers
		amountPayable = amountPayable.add(priceList.stream().reduce(BigDecimal.ZERO, BigDecimal::add));

		if (null != storeOffer && storeOffer.getDiscount() > 0) {
			BigDecimal minimumAmount = BigDecimal.valueOf(storeOffer.getOfferTypeValue());
			if (amountPayable.compareTo(minimumAmount) > 0) {
				amountPayable = amountPayable.subtract(amountPayable
						.multiply(BigDecimal.valueOf(storeOffer.getDiscount())).divide(BigDecimal.valueOf(100)));
			}

		}
		return amountPayable.setScale(2, RoundingMode.HALF_EVEN);

	}

	public void addItemToCard(String stockKeepingUnit) {
		if (!cartItems.containsKey(stockKeepingUnit)) {
			cartItems.put(stockKeepingUnit, 1);
		} else {
			cartItems.put(stockKeepingUnit, cartItems.get(stockKeepingUnit) + 1);
		}
	}

	public Integer countCartItems() {
		return this.cartItems.values().stream().reduce(0, (accumulator, next) -> accumulator = accumulator + next);
	}

	public BigDecimal calculateSavings() {
		return calculateTotal().subtract(calculateAmountToPay());
	}

}
