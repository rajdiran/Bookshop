package com.rajdiran.bookshop.repository;

import java.math.BigDecimal;
import java.util.HashMap;

import com.rajdiran.bookshop.exceptions.ItemNotFoundException;
import com.rajdiran.bookshop.model.Item;

public interface ItemStore {

	void storeItem(Item item);

	HashMap<String, Item> getItemsInStock();

	Item getItem(String stockKeepingUnit) throws ItemNotFoundException;

	BigDecimal retrieveItemPrice(String stockKeepingUnit) throws ItemNotFoundException;

}
