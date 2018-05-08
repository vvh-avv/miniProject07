package com.model2.mvc.service.product.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductDao;

@Repository("productDaoImpl")
public class ProductDaoImpl implements ProductDao{
	
	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public ProductDaoImpl() {
	}

	///Method
	public void addProduct(Product product) throws Exception{
		sqlSession.insert("ProductMapper.insertProduct", product);
	}
	
	public Product getProduct(int prodNo) throws Exception{
		return sqlSession.selectOne("ProductMapper.findProduct", prodNo);
	}

	public void updateProduct(Product product) throws Exception{
		sqlSession.update("ProductMapper.updateProduct", product);
	}
	
	public List<Product> getProductList(Search search, String sort) throws Exception{
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("search", search);
		map.put("sort", sort);
		
		return sqlSession.selectList("ProductMapper.getProductList", map);
	}
	
	public int getTotalCount(Search search) throws Exception{
		return sqlSession.selectOne("ProductMapper.getTotalCount", search);
	}

}