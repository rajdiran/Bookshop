package com.rajdiran.bookshop.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.rajdiran.bookshop.model.BookItem;
import com.rajdiran.bookshop.model.Offer;
import com.rajdiran.bookshop.model.OfferType;
import com.rajdiran.bookshop.repository.BookStore;
import com.rajdiran.bookshop.repository.BookStoreOffer;

public class CheckoutTest {

	private Checkout checkout;
	private BookStore bookStore;
	private BookStoreOffer bookStoreOffer;
	private Offer storeOffer;

	@Before
	public void prepareSuperMarket() {
		bookStore = new BookStore();
		bookStoreOffer = new BookStoreOffer();
		storeOffer = new Offer(OfferType.TOTAL_AMOUNT, 0, 0);
		checkout = new Checkout(bookStore, bookStoreOffer, storeOffer);
	}

	private HashMap<String, BookItem> storeTestData() {
		BookItem bookA = new BookItem("A", new BigDecimal("15.20"), "Moby Dick", 1851);
		BookItem bookB = new BookItem("B", new BigDecimal("13.14"), "The Terrible Privacy of Maxwell Sim", 2010);
		BookItem bookC = new BookItem("C", new BigDecimal("11.05"), "Still Life With Woodpecker", 1980);
		BookItem bookD = new BookItem("D", new BigDecimal("10.24"), "Sleeping Murder", 1976);
		BookItem bookE = new BookItem("E", new BigDecimal("12.87"), "Three Men in a Boat", 1889);
		BookItem bookF = new BookItem("F", new BigDecimal("10.43"), "The Time Machine", 1895);
		BookItem bookG = new BookItem("G", new BigDecimal("8.12"), "The Caves of Steel", 1954);
		BookItem bookH = new BookItem("H", new BigDecimal("7.32"), "Idle Thoughts of an Idle Fellow", 1886);
		BookItem bookI = new BookItem("I", new BigDecimal("4.23"), "A Christmas Carol", 1843);
		BookItem bookJ = new BookItem("J", new BigDecimal("6.32"), "A Tale of Two Cities", 1859);
		BookItem bookK = new BookItem("K", new BigDecimal("13.21"), "Great Expectations", 1861);
		HashMap<String, BookItem> itemsInStore = new HashMap<>();
		itemsInStore.put(bookA.getStockKeepingUnit(), bookA);
		itemsInStore.put(bookB.getStockKeepingUnit(), bookB);
		itemsInStore.put(bookC.getStockKeepingUnit(), bookC);
		itemsInStore.put(bookD.getStockKeepingUnit(), bookD);
		itemsInStore.put(bookE.getStockKeepingUnit(), bookE);
		itemsInStore.put(bookF.getStockKeepingUnit(), bookF);
		itemsInStore.put(bookG.getStockKeepingUnit(), bookG);
		itemsInStore.put(bookH.getStockKeepingUnit(), bookH);
		itemsInStore.put(bookI.getStockKeepingUnit(), bookI);
		itemsInStore.put(bookJ.getStockKeepingUnit(), bookJ);
		itemsInStore.put(bookK.getStockKeepingUnit(), bookK);

		bookStore.storeItem(bookA);
		bookStore.storeItem(bookB);
		bookStore.storeItem(bookC);
		bookStore.storeItem(bookD);
		bookStore.storeItem(bookE);
		bookStore.storeItem(bookF);
		bookStore.storeItem(bookG);
		bookStore.storeItem(bookH);
		bookStore.storeItem(bookI);
		bookStore.storeItem(bookJ);
		bookStore.storeItem(bookK);
		return itemsInStore;
	}

	@Test
	public void canAddItemsToCart() {
		checkout.addItemToCard(new BookItem("A", new BigDecimal("15.20"), "Moby Dick", 1851).getStockKeepingUnit());
		checkout.addItemToCard(new BookItem("B", new BigDecimal("13.14"), "The Terrible Privacy of Maxwell Sim", 2010)
				.getStockKeepingUnit());
		checkout.addItemToCard(
				new BookItem("C", new BigDecimal("11.05"), "Still Life With Woodpecker", 1980).getStockKeepingUnit());

		assertEquals(Integer.valueOf(3), checkout.countCartItems());
	}

	@Test
	public void shouldReturnZeroIfThereAreNoItemsScanned() {
		assertEquals(Integer.valueOf(0), checkout.countCartItems());
	}

	@Test
	public void canRegisterCurrentGroceriesAndTheirPrices() {
		HashMap<String, BookItem> itemsInStore = storeTestData();
		assertTrue(itemsInStore.equals(bookStore.getItemsInStock()));
	}

	@Test
	public void canReturnTheTotalCheckoutPriceForAllItemsScanned() {

		bookStore.storeItem(new BookItem("A", new BigDecimal("15.20"), "Moby Dick", 1851));
		bookStore.storeItem(new BookItem("B", new BigDecimal("13.14"), "The Terrible Privacy of Maxwell Sim", 2010));
		bookStore.storeItem(new BookItem("C", new BigDecimal("11.05"), "Still Life With Woodpecker", 1980));
		bookStore.storeItem(new BookItem("D", new BigDecimal("10.24"), "Sleeping Murder", 1976));
		bookStore.storeItem(new BookItem("E", new BigDecimal("12.87"), "Three Men in a Boat", 1889));
		bookStore.storeItem(new BookItem("F", new BigDecimal("10.43"), "The Time Machine", 1895));
		bookStore.storeItem(new BookItem("G", new BigDecimal("8.12"), "The Caves of Steel", 1954));
		bookStore.storeItem(new BookItem("H", new BigDecimal("7.32"), "Idle Thoughts of an Idle Fellow", 1886));
		bookStore.storeItem(new BookItem("I", new BigDecimal("4.23"), "A Christmas Carol", 1843));
		bookStore.storeItem(new BookItem("J", new BigDecimal("6.32"), "A Tale of Two Cities", 1859));
		bookStore.storeItem(new BookItem("K", new BigDecimal("13.21"), "Great Expectations", 1861));

		checkout.addItemToCard("A");
		checkout.addItemToCard("B");
		checkout.addItemToCard("C");

		assertEquals(checkout.calculateTotal(), new BigDecimal("39.39"));
	}

	@Test
	public void shouldReturnTotalOfZeroIfWeAddItemThatDoesNotExist() {
		checkout.addItemToCard("D");
		checkout.addItemToCard("E");

		assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN), checkout.calculateTotal());
	}

	@Test
	public void shouldReturnZeroTotalIfThereAreNoItemsScanned() {
		assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN), checkout.calculateTotal());
	}

	@Test
	public void checkoutShouldCalculateSavingsIfThereIsAnOfferThatAppliesToTheItemsAdded() {
		storeTestData();

		checkout.addBookStore(new Offer(OfferType.PUBLISHED_BY_YEAR, 10, 2000));

		checkout.addItemToCard("B");
		checkout.addItemToCard("E");

		assertEquals(new BigDecimal("24.70"), checkout.calculateAmountToPay());
		assertEquals(new BigDecimal("1.31"), checkout.calculateSavings());
	}

	@Test
	public void checkoutShouldCalculateSavingsIfThereIsOfferForTotalValue() {
		storeTestData();

		checkout.addBookStore(new Offer(OfferType.PUBLISHED_BY_YEAR, 10, 2000));

		Offer storeOffer = new Offer(OfferType.TOTAL_AMOUNT, 5, 30);
		checkout.setStoreOffer(storeOffer);

		checkout.addItemToCard("C");
		checkout.addItemToCard("E");
		checkout.addItemToCard("K");

		assertEquals(new BigDecimal("35.27"), checkout.calculateAmountToPay());
		assertEquals(new BigDecimal("1.86"), checkout.calculateSavings());
	}

	@Test
	public void checkoutShouldCalculateSavingsIfThereIsAnOfferThatAppliesToItemAddedAndThereIsOfferForTotalValue() {
		storeTestData();

		checkout.addBookStore(new Offer(OfferType.PUBLISHED_BY_YEAR, 10, 2000));

		Offer storeOffer = new Offer(OfferType.TOTAL_AMOUNT, 5, 30);
		checkout.setStoreOffer(storeOffer);

		checkout.addItemToCard("B");
		checkout.addItemToCard("E");
		checkout.addItemToCard("K");

		assertEquals(new BigDecimal("36.01"), checkout.calculateAmountToPay());
		assertEquals(new BigDecimal("3.21"), checkout.calculateSavings());
	}

}
