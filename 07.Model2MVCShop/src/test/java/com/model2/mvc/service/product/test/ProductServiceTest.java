package com.model2.mvc.service.product.test;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/context-common.xml", "classpath:config/context-aspect.xml",
												"classpath:config/context-mybatis.xml", "classpath:config/context-transaction.xml" })
public class ProductServiceTest {

	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;

	//@Test
	public void testAddProduct() throws Exception {
		
		Product product = new Product();
		product.setProdNo(1004);
		product.setProdName("testProdName");
		product.setProdDetail("testDetail");
		product.setManuDate("19951017");
		product.setPrice(1004);
		product.setFileName("testFileName");
		
		productService.addProduct(product);
		
		product = productService.getProduct(1004);

		//==> console 확인
		System.out.println("testAddProduct() product : "+product);
		
		//==> API 확인
		Assert.assertEquals(1004, product.getProdNo());
		Assert.assertEquals("testProdName", product.getProdName());
		Assert.assertEquals("testDetail", product.getProdDetail());
		Assert.assertEquals("19951017", product.getManuDate());
		Assert.assertEquals(1004, product.getPrice());
		Assert.assertEquals("testFileName", product.getFileName());
	}
	
	//@Test
	public void testGetProduct() throws Exception {
		
		Product product = new Product();
//		==> 필요하다면...
//		product.setProdNo(1004);
//		product.setProdName("testProdName");
//		product.setProdDetail("testDetail");
//		product.setManuDate("19951017");
//		product.setPrice(1004);
//		product.setFileName("testFileName");
		
		product = productService.getProduct(1004);

		//==> console 확인
		System.out.println("testGetProduct() product : "+product);
		
		//==> API 확인
		Assert.assertEquals(1004, product.getProdNo());
		Assert.assertEquals("testProdName", product.getProdName());
		Assert.assertEquals("testDetail", product.getProdDetail());
		Assert.assertEquals("19951017", product.getManuDate());
		Assert.assertEquals(1004, product.getPrice());
		Assert.assertEquals("testFileName", product.getFileName());

		Assert.assertNotNull(productService.getProduct(10000));
		Assert.assertNotNull(productService.getProduct(10001));
	}
	
	//@Test
	 public void testUpdateProduct() throws Exception{
		 
		Product product = productService.getProduct(1004);
		Assert.assertNotNull(product);

		Assert.assertEquals("testProdName", product.getProdName());
		Assert.assertEquals("testDetail", product.getProdDetail());
		Assert.assertEquals("19951017", product.getManuDate());
		Assert.assertEquals(1004, product.getPrice());
		Assert.assertEquals("testFileName", product.getFileName());

		product.setProdName("changeN");
		product.setProdDetail("changeD");
		product.setManuDate("20180401");
		product.setPrice(10041004);
		product.setFileName("changeF");
		
		productService.updateProduct(product);
		
		product = productService.getProduct(1004);
		Assert.assertNotNull(product);
		
		//==> console 확인
		System.out.println("testUpdateProduct() product : "+product);
		
		//==> API 확인
		Assert.assertEquals("changeN", product.getProdName());
		Assert.assertEquals("changeD", product.getProdDetail());
		Assert.assertEquals("20180401", product.getManuDate());
		Assert.assertEquals(10041004, product.getPrice());
		Assert.assertEquals("changeF", product.getFileName());
	 }
	
	 //==>  주석을 풀고 실행하면....
	 @Test
	 public void testGetProductListAll() throws Exception{
		 
	 	Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	
	 	String sort = "p.price desc";
	 	Map<String,Object> map = productService.getProductList(search, sort);
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
		//==> console 확인
	 	for( Object consol : list ) {
	 		System.out.println("testGetProductListAll() list : " + consol);
	 	}
	 	//System.out.println("testGetProductListAll() list1 : "+list);
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println("testGetProductListAll() totalCount1 : "+totalCount);
	 	
	 	System.out.println("=======================================");
//	 	
//	 	search.setCurrentPage(1);
//	 	search.setPageSize(3);
//	 	search.setSearchCondition("0");
//	 	search.setSearchKeyword("");
//	 	map = productService.getProductList(search, sort);
//	 	
//	 	list = (List<Object>)map.get("list");
//	 	Assert.assertEquals(3, list.size());
//	 	
//	 	//==> console 확인
//	 	System.out.println("testGetProductListAll() list2 : "+list);
//	 	
//	 	totalCount = (Integer)map.get("totalCount");
//	 	System.out.println("testGetProductListAll() totalCount2 : "+totalCount);
	 }
	 
	 //@Test
	 public void testGetProductListByProductNo() throws Exception{
		 
	 	Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	search.setSearchCondition("0");
	 	search.setSearchKeyword("1004");
	 	
	 	String sort = "prod_no asc";
	 	Map<String,Object> map = productService.getProductList(search, sort);
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(1, list.size());
	 	
		//==> console 확인
	 	System.out.println("testGetProductListByProductId() list1 : "+list);
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println("testGetProductListByProductId() totalCount : "+totalCount);
	 	
	 	System.out.println("=======================================");
	 	
	 	search.setSearchCondition("0");
	 	search.setSearchKeyword(""+System.currentTimeMillis());
	 	map = productService.getProductList(search, sort);
	 	
	 	list = (List<Object>)map.get("list");
	 	Assert.assertEquals(0, list.size());
	 	
		//==> console 확인
	 	System.out.println("testGetProductListByProductId()2 list : "+list);
	 	
	 	totalCount = (Integer)map.get("totalCount");
	 	System.out.println(totalCount);
	 }
	 
	 //@Test
	 public void testGetProductListByProductName() throws Exception{
		 
	 	Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	search.setSearchCondition("1");
	 	search.setSearchKeyword("보르도");
	 	
	 	String sort = "prod_no asc";
	 	Map<String,Object> map = productService.getProductList(search, sort);
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(1, list.size());
	 	
		//==> console 확인
	 	System.out.println("testGetProductListByProductName() list1 : "+list);
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println("testGetProductListByProductName() totalCount1 : "+totalCount);
	 	
	 	System.out.println("=======================================");
	 	
	 	search.setSearchCondition("1");
	 	search.setSearchKeyword(""+System.currentTimeMillis());
	 	map = productService.getProductList(search, sort);
	 	
	 	list = (List<Object>)map.get("list");
	 	Assert.assertEquals(0, list.size());
	 	
		//==> console 확인
	 	System.out.println("testGetProductListByProductName() list2 : "+list);
	 	
	 	totalCount = (Integer)map.get("totalCount");
	 	System.out.println("testGetProductListByProductName() totalCount2 : "+totalCount);
	 }	 
}