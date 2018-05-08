package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseDao;

@Repository("purchaseDaoImpl")
public class PurchaseDaoImpl implements PurchaseDao{
	
	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public PurchaseDaoImpl() {
	}

	///Method
	public void addPurchase(Purchase purchase) throws Exception{
		sqlSession.insert("PurchaseMapper.insertPurchase", purchase);
	}
	
	public Purchase getPurchase(int prodNo) throws Exception{
		return sqlSession.selectOne("PurchaseMapper.findPurchase", prodNo);
	}

	public void updatePurchase(Purchase purchase) throws Exception{
		sqlSession.update("PurchaseMapper.updatePurchase", purchase);
	}
	
	public List<Purchase> getPurchaseList(Search search, String buyerId, String sort) throws Exception{		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("search", search);
		map.put("buyerId", buyerId);
		map.put("sort", sort);
		
		return sqlSession.selectList("PurchaseMapper.getPurchaseList", map);
	}
	
	public int getTotalCount(Search search, String buyerId) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("search", search);
		map.put("buyerId", buyerId);
		
		return sqlSession.selectOne("PurchaseMapper.getTotalCount", map);
	}

	public HashMap<String, Object> getSaleList(Search search) {
		return null;
	}

	public void updateTrancode(Purchase purchase) throws Exception {
		sqlSession.update("PurchaseMapper.updateTranCode", purchase);
	}


}