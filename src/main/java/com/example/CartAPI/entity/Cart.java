package com.example.CartAPI.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = Cart.COLLECTION_NAME)
public class Cart {
	public static final String COLLECTION_NAME = "cart";

	@Id
	private String cartID;
	private String userId;
	private String productId;
	private int purchaseUnit;

	public static String getCollectionName() {
		return COLLECTION_NAME;
	}

	public String getCartID() {
		return cartID;
	}

	public void setCartID(String cartID) {
		this.cartID = cartID;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getPurchaseUnit() {
		return purchaseUnit;
	}

	public void setPurchaseUnit(int purchaseUnit) {
		this.purchaseUnit = purchaseUnit;
	}
}
