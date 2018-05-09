package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;

@Controller
@RequestMapping("/purchase/*")
public class PurchaseController {

	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	public PurchaseController() {
		System.out.println(this.getClass());
	}
	
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	int pageSize;

	
	@RequestMapping(value="/purchase/addPurchase", method=RequestMethod.GET)
	public ModelAndView addPurchase(@RequestParam("prod_no") int prodNo ) throws Exception {
		System.out.println("/purchase/addPurchase : GET");
		
		Product product = productService.getProduct(prodNo);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("product", product);
		modelAndView.setViewName("/purchase/addPurchaseView.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping(value="/purchase/addPurchase", method=RequestMethod.POST)
	public ModelAndView addPurchase(@ModelAttribute("purchase") Purchase purchase, HttpSession session,
													@RequestParam("prodNo") int prodNo) throws Exception{
		System.out.println("/purchase/addPurchase : POST");
		
		purchase.setBuyer((User)session.getAttribute("user"));
		purchase.setPurchaseProd(productService.getProduct(prodNo));
		purchaseService.addPurchase(purchase);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("purchase", purchase);
		modelAndView.setViewName("/purchase/addPurchase.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping(value="getPurchase", method=RequestMethod.GET)
	public ModelAndView getPurchase(@RequestParam("tranNo") int tranNo) throws Exception{
		System.out.println("/purchase/getPurchase : GET");
		
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("purchase", purchase);
		modelAndView.setViewName("/purchase/getPurchase.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping(value="listPurchase")
	public ModelAndView listPurchase(@ModelAttribute("search") Search search, HttpSession session,
												@RequestParam(value="sort", required=false, defaultValue="asc") String sort,
												@RequestParam(value="userId", required=false) String userId) throws Exception{
		System.out.println("/purchase/listPurchase : GET / POST");
		
		if(search.getCurrentPage()==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		//어드민이 각 유저 주문정보 확인
		if(userId==null) {
			userId = ((User)session.getAttribute("user")).getUserId();
		}
		
		Map<String,Object> map = purchaseService.getPurchaseList(search, userId, sort);
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize );
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("list", map.get("list"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);
		modelAndView.addObject("sort", sort);
		modelAndView.setViewName("/purchase/listPurchase.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping(value="updatePurchase", method=RequestMethod.GET)
	public ModelAndView updatePurchase(@RequestParam("tranNo") int tranNo) throws Exception{
		System.out.println("/purchase/updatePurchase : GET");
		
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("purchase", purchase);
		modelAndView.setViewName("/purchase/updatePurchaseView.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping(value="updatePurchase", method=RequestMethod.POST)
	public ModelAndView updatePurchase(@ModelAttribute("purchase") Purchase purchase) throws Exception{
		System.out.println("/purchase/updatePurchase : POST");
		
		purchaseService.updatePurchase(purchase);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/purchase/getPurchase.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping(value="updateTranCode", method=RequestMethod.POST)
	public ModelAndView updateTranCode(@RequestParam("tranNo") int tranNo, @RequestParam("tranCode") String tranCode) throws Exception{
		System.out.println("/purchase/updateTranCode : POST");
		
		Purchase purchase = purchaseService.getPurchase(tranNo);
		purchase.setTranCode(tranCode); //변경할 tranCode 덮어씌움
		purchaseService.updateTranCode(purchase);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/purchase/listPurchase");
		
		return modelAndView;
	}
	
	@RequestMapping(value="updateTranCodeByProd", method=RequestMethod.POST)
	public ModelAndView updateTranCodeByProd(@RequestParam("prodNo") String prodNo, @RequestParam("tranCode") String tranCode) throws Exception{
		System.out.println("/purchase/updateTranCodeByProd : POST");
		
		//일괄처리
		if(prodNo.contains(",")) {
			String[] prodList = prodNo.split(",");
			for(int i=0; i<prodList.length; i++) {
				Purchase purchase = purchaseService.getPurchase2(Integer.parseInt(prodList[i]));
				purchase.setTranCode(tranCode);
				purchaseService.updateTranCode(purchase);
			}
		}else {
			Purchase purchase = purchaseService.getPurchase2(Integer.parseInt(prodNo));
			purchase.setTranCode(tranCode);
			purchaseService.updateTranCode(purchase);
		}
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/product/listProduct?menu=manage");
		
		return modelAndView;
	}
	
}
