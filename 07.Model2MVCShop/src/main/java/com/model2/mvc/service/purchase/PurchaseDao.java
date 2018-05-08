package com.model2.mvc.service.purchase;

import java.util.HashMap;
import java.util.List;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;

public interface PurchaseDao {
	
	public void addPurchase(Purchase purchase) throws Exception ;
	
	public Purchase getPurchase(int no) throws Exception ;	
	
	public List<Purchase> getPurchaseList(Search search, String buyerId, String sort) throws Exception ;
	
	public HashMap<String,Object> getSaleList(Search search) ;	
	
	public void updatePurchase(Purchase purchase) throws Exception ;
	
	public void updateTrancode(Purchase purchase) throws Exception ;	
	
	public int getTotalCount(Search search, String buyerId) throws Exception ;
}