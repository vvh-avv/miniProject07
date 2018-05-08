package com.model2.mvc.service.purchase.test;

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
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.purchase.PurchaseService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/context-common.xml", "classpath:config/context-aspect.xml",
												"classpath:config/context-mybatis.xml", "classpath:config/context-transaction.xml" })
public class PurchaseServiceTest {

	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;


	//@Test
	public void testAddPurchase() throws Exception {
		
		User buyer = new User();
		buyer.setUserId("user01");
		Product purchaseProd = new Product();
		purchaseProd.setProdNo(10008);
		
		Purchase purchase = new Purchase();
		purchase.setBuyer(buyer);
		purchase.setDivyAddr("test서울");
		purchase.setDivyDate("19951017");
		purchase.setDivyRequest("test내일주세요");
		purchase.setPaymentOption("1");
		purchase.setPurchaseProd(purchaseProd);
		purchase.setReceiverName("test비트캠프");
		purchase.setReceiverPhone("010-1234-1234");
		purchase.setTranCode("1");
		purchase.setTranNo(10004);
		
		purchaseService.addPurchase(purchase);
		System.out.println("testAddPurchase() 성공!");
		
	}
	
	//@Test
	public void testGetPurchase() throws Exception {
		
		Purchase purchase = new Purchase();
		
		purchase = purchaseService.getPurchase(10004);

		System.out.println("testGetPurchase() purchase : "+purchase);
		
		Assert.assertEquals(10004, purchase.getTranNo());
		Assert.assertEquals("1", purchase.getPaymentOption());
		Assert.assertEquals("test비트캠프", purchase.getReceiverName());
		Assert.assertEquals("010-1234-1234", purchase.getReceiverPhone());
		Assert.assertEquals("test서울", purchase.getDivyAddr());
		Assert.assertEquals("1995-10-17 00:00:00.0", purchase.getDivyDate());
		Assert.assertEquals("test내일주세요", purchase.getDivyRequest());

		//Assert.assertNotNull(purchaseService.getPurchase(10000));
		//Assert.assertNotNull(purchaseService.getPurchase(10001));
	}
	
	//@Test
	public void testUpdatePurchase() throws Exception{
		 
		Purchase purchase = purchaseService.getPurchase(10004);
		Assert.assertNotNull(purchase);

		Assert.assertEquals(10004, purchase.getTranNo());
		Assert.assertEquals("1", purchase.getPaymentOption());
		Assert.assertEquals("test비트캠프", purchase.getReceiverName());
		Assert.assertEquals("010-1234-1234", purchase.getReceiverPhone());
		Assert.assertEquals("test서울", purchase.getDivyAddr());
		Assert.assertEquals("1995-10-17 00:00:00.0", purchase.getDivyDate());
		Assert.assertEquals("test내일주세요", purchase.getDivyRequest());

		purchase.setPaymentOption("2");
		purchase.setReceiverName("changeN");
		purchase.setReceiverPhone("changeP");
		purchase.setDivyAddr("changeA");
		purchase.setDivyDate("20180401");
		purchase.setDivyRequest("changeR");
		
		purchaseService.updatePurchase(purchase);
		
		purchase = purchaseService.getPurchase(10004);
		Assert.assertNotNull(purchase);
		
		System.out.println("testUpdatePurchase() purchase : "+purchase);
		
		Assert.assertEquals("2", purchase.getPaymentOption());
		Assert.assertEquals("changeN", purchase.getReceiverName());
		Assert.assertEquals("changeP", purchase.getReceiverPhone());
		Assert.assertEquals("changeA", purchase.getDivyAddr());
		Assert.assertEquals("2018-04-01 00:00:00.0", purchase.getDivyDate());
		Assert.assertEquals("changeR", purchase.getDivyRequest());
	 }
	
	 @Test
	 public void testGetPurchaseListAll() throws Exception{
	 	Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	
	 	String sort = "asc";
	 	Map<String,Object> map = purchaseService.getPurchaseList(search, "admin", sort);
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(3, list.size());
	 	
	 	for( Object consol : list ) {
	 		System.out.println("testGetPurchaseListAll() list : " + consol);
	 	}
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println("testGetPurchaseListAll() totalCount1 : "+totalCount);
	 }
	 
	 //@Test
	 public void testGetPurchaseListByTranNo() throws Exception{ //??getPurchase로 할 수 있는데 오 ㅐ했지
	 	Search search = new Search();
	 	search.setCurrentPage(1);
	 	search.setPageSize(3);
	 	search.setSearchCondition("1");
	 	search.setSearchKeyword("10124");
	 	
	 	String sort = "asc";
	 	Map<String,Object> map = purchaseService.getPurchaseList(search, "admin", sort);
	 	
	 	List<Object> list = (List<Object>)map.get("list");
	 	Assert.assertEquals(1, list.size());
	 	
	 	for( Object consol : list ) {
	 		System.out.println("testGetPurchaseListByTranNo() list : " + consol);
	 	}
	 	
	 	Integer totalCount = (Integer)map.get("totalCount");
	 	System.out.println("testGetPurchaseListByTranNo() totalCount1 : "+totalCount);
	 }
	 
	 	//@Test
		public void testUpdateTranCode() throws Exception{
			Purchase purchase = purchaseService.getPurchase(10124);
			Assert.assertNotNull(purchase);
			
			Assert.assertEquals("1", purchase.getTranCode());
			
			purchase.setTranCode("2");

			purchaseService.updateTranCode(purchase);
			
			purchase = purchaseService.getPurchase(10124);
			Assert.assertNotNull(purchase);
			
			System.out.println("testUpdateTranCode() purchase : "+purchase);
			
			Assert.assertEquals("2", purchase.getTranCode());
			
		 }
}