package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.CookieGenerator;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;

@Controller
@RequestMapping("/product/*")
public class ProductController {

	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	public ProductController() {
		System.out.println(this.getClass());
	}
	
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	int pageSize;

	
	@RequestMapping(value="addProduct", method=RequestMethod.GET)
	public String addProduct() throws Exception {
		System.out.println("/product/addProduct : GET");
		
		return "redirect:/product/addProductView.jsp";
	}
	
	@RequestMapping(value="addProduct", method=RequestMethod.POST)
	public String addProduct(@ModelAttribute("product") Product product) throws Exception {
		System.out.println("/product/addProduct : POST");
		
		productService.addProduct(product);
		
		return "forward:/product/getProduct.jsp";
	}
	
	@RequestMapping(value="getProduct")
	public String getProduct(@RequestParam("prodNo") int prodNo, Model model,
									HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("/product/getProduct : GET / POST");
		
		Product product = productService.getProduct(prodNo);
		model.addAttribute("product", product);
		
		//쿠키 추가
		String history = null;
		Cookie[] c = request.getCookies();
		if(c!=null){ //쿠키가 존재하면
			for(int i=0; i<c.length; i++){
				Cookie cookie = c[i];
				if(cookie.getName().equals("history")){
					history = cookie.getValue();
				}
			}
		}
		history += "," + product.getProdNo();

		//Cookie cookie = new Cookie("history",history);
		//response.addCookie(cookie);
		CookieGenerator cg = new CookieGenerator();

		cg.setCookieName("history");
		cg.addCookie(response, history);
		
		return "forward:/product/detailProduct.jsp";
	}
	
	@RequestMapping(value="updateProduct", method=RequestMethod.GET)
	public String updateProduct(@ModelAttribute("product") Product product, Model model, HttpSession session,
										@RequestParam(value="status", required=false, defaultValue="") String status) throws Exception{
		System.out.println("/product/updateProduct : GET");
		
		Product productVO = productService.getProduct(product.getProdNo());

		model.addAttribute("product", productVO);
		
		if( ((User)session.getAttribute("user")).getRole().equals("admin") && status.equals("0") ) { //판매중 상품
			return "forward:/product/updateProduct.jsp";
		}else {
			return "forward:/product/detailProduct.jsp";
		}
	}
	
	@RequestMapping(value="updateProduct", method=RequestMethod.POST)
	public String updateProduct(@ModelAttribute("product") Product product) throws Exception{
		System.out.println("/product/updateProduct : POST");
		
		productService.updateProduct(product);
		
		//return "forward:/product/detailProduct.jsp";
		return "forward:/product/getProduct?prodNo="+product.getProdNo();
	}

	@RequestMapping(value="listProduct")
	public String listProduct(@ModelAttribute("search") Search search, @RequestParam(value="sort", required=false, defaultValue="prod_no asc") String sort,
									Model model) throws Exception{
		System.out.println("/product/listProduct : GET / POST");
		
		if(search.getCurrentPage()==0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		if(sort.indexOf("+")!=-1) {
			sort = sort.replace("+", " ");
		}
		
		Map<String,Object> map = productService.getProductList(search, sort);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize );
		
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		model.addAttribute("sort",sort);
		
		return "forward:/product/listProduct.jsp";
	}
}
