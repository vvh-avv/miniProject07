package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

@Controller
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

	
	@RequestMapping("/addProductView.do")
	public String addProductView() throws Exception {
		System.out.println("/addProductView.do");
		
		return "redirect:/product/addProductView.jsp";
	}
	
	@RequestMapping("/addProduct.do")
	public String addProduct(@ModelAttribute("product") Product product) throws Exception {
		System.out.println("/addProduct.do");
		
		productService.addProduct(product);
		
		return "forward:/product/getProduct.jsp";
	}
	
	@RequestMapping("/getProduct.do")
	public String getProduct(@RequestParam("prodNo") int prodNo, Model model,
									HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("/getProduct.do");
		
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
		Cookie cookie = new Cookie("history",history);
		response.addCookie(cookie);
	
		
		return "forward:/product/detailProduct.jsp";
	}
	
	@RequestMapping("/updateProductView.do")
	public String updateProductView(@ModelAttribute("product") Product product, Model model,
												@RequestParam(value="status", required=false, defaultValue="") String status) throws Exception{
		System.out.println("/updateProductView.do");
		
		Product productVO = productService.getProduct(product.getProdNo());

		model.addAttribute("product", productVO);
		
		if(status.equals("0")) { //판매중 상품
			return "forward:/product/updateProduct.jsp";
		}else {
			return "forward:/product/detailProduct.jsp";
		}
	}
	
	@RequestMapping("/updateProduct.do")
	public String updateProduct(@ModelAttribute("product") Product product) throws Exception{
		System.out.println("/updateProduct.do");
		
		productService.updateProduct(product);
		
		return "forward:/product/detailProduct.jsp";
	}

	@RequestMapping("/listProduct.do")
	public String listProduct(@ModelAttribute("search") Search search, @RequestParam(value="sort", required=false, defaultValue="prod_no asc") String sort,
									Model model) throws Exception{
		System.out.println("/listProduct.do");
		
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
