package com.ecommerce.interfaces;

public interface PurchaseHistoryDAOInterface {
	    boolean purchaseItems(int userId);

		double calculateTotalBill(int userId);
}
