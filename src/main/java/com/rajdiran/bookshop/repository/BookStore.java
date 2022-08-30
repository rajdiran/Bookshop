package com.rajdiran.bookshop.repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

import com.rajdiran.bookshop.exceptions.ItemNotFoundException;
import com.rajdiran.bookshop.model.Item;

public class BookStore implements ItemStore {

	HashMap<String, Item> booksStock;

	public BookStore() {
		booksStock = new HashMap<String, Item>();
	}

	@Override
	public void storeItem(Item item) {
		booksStock.put(item.getStockKeepingUnit(), item);
	}

	@Override
	public HashMap<String, Item> getItemsInStock() {
		return booksStock;
	}

	@Override
	public Item getItem(String stockKeepingUnit) {
		Optional<Item> returnedPrice = Optional.ofNullable(booksStock.get(stockKeepingUnit));
		return returnedPrice.get();

	}

	@Override
	public BigDecimal retrieveItemPrice(String stockKeepingUnit) throws ItemNotFoundException {
		Optional<Item> returnedPrice = Optional.ofNullable(booksStock.get(stockKeepingUnit));
		return returnedPrice.orElseThrow(ItemNotFoundException::new).getUnitPrice();
	}

}
