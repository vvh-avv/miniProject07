package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseDao;
import com.model2.mvc.service.purchase.PurchaseService;

@Service("purchaseServiceImpl")
public class PurchaseServiceImpl implements PurchaseService{
	
	@Autowired
	@Qualifier("purchaseDaoImpl")
	private PurchaseDao purchaseDao;
	
	public void setPurchaseDao(PurchaseDao purchaseDao) {
		this.purchaseDao = purchaseDao;
	}
	
	public PurchaseServiceImpl() {
	}
	
	@Override
	public void addPurchase(Purchase purchase) throws Exception{
		purchaseDao.addPurchase(purchase);
	}
	
	@Override
	public Purchase getPurchase(int tranNo) throws Exception{
		return purchaseDao.getPurchase(tranNo);
	}
	
	@Override
	public Purchase getPurchase2(int ProdNo) throws Exception{
		return purchaseDao.getPurchase(ProdNo);
	}
	
	@Override
	public Map<String,Object> getPurchaseList(Search search, String buyerId, String sort) throws Exception{
		List<Purchase> list = purchaseDao.getPurchaseList(search, buyerId, sort);
		
		int totalCount = purchaseDao.getTotalCount(search, buyerId);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("totalCount", totalCount);
		
		return map;
	}
	
	@Override
	public Map<String,Object> getSaleList(Search search) throws Exception{
		return purchaseDao.getSaleList(search);
	}
	
	@Override
	public void updatePurchase(Purchase purchase) throws Exception{
		purchaseDao.updatePurchase(purchase);
	}

	/*
	@Override
	public void deletePurchase(Purchase purchaseVO) throws Exception{
		purchaseDao.deletePurchase(purchaseVO);
	}
	*/
	
	@Override
	public void updateTranCode(Purchase purchaseVO) throws Exception{
		purchaseDao.updateTrancode(purchaseVO);
	}
}
