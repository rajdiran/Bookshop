package com.rajdiran.bookshop.model;

import java.math.BigDecimal;

public class BookItem extends Item {

	private String title;
	private int year;

	public BookItem(String stockKeepingUnit, BigDecimal unitPrice) {
		super(stockKeepingUnit, unitPrice);
	}

	public BookItem(String stockKeepingUnit, BigDecimal unitPrice, String title, int year) {
		super(stockKeepingUnit, unitPrice);
		this.title = title;
		this.year = year;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}
