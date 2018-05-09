package com.model2.mvc.web.product;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
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
	public String addProduct( @ModelAttribute("product") Product product, HttpServletRequest request,
										@RequestParam("file") MultipartFile file) throws Exception {
		System.out.println("/product/addProduct : POST");
		//String path = request.getSession().getServletContext().getRealPath("/");
		String path = request.getRealPath("/WebContent")+"\\images\\uploadFiles\\";		
		
		//File f = new File("C:\\Users\\Bit\\git\\miniProject07\\07.Model2MVCShop\\WebContent\\images\\uploadFiles\\"+file.getOriginalFilename());
		File f = new File(path+file.getOriginalFilename());
		file.transferTo(f); //위의 경로에 파일 저장
		product.setFileName(file.getOriginalFilename());
		
		
//		Iterator<String> iterator = mRequest.getFileNames();
//		//Iterator안에 input type='file'인 요소들의 name들을 모두 넣는다.
//		//나의 경우 iterator안에 file1,file2가 들어있다.
//		//이때 두 파일을 한꺼번에 생성하는데, 이때 파일이름을 날짜시간으로 정할거라서  
//		//두 파일 모두 이름이 똑같아 지기 때문에 같은이름으로 덮어써져서 파일이 하나만 생성될수있다.
//		//따라서 i인덱스를 만들어서 파일 이름 맨마지막에 i숫자를 붙여주면 모두 다른 이름으로 파일이 생성됨.
//		int i = 0;
//		
//		//iterator.hasNext를 써주면 iterator안에 다음 요소가 있을때 까지만 실행.
//		//iterator의 커서를 움직였을때 다음 요소가 없다면 while종료. 
//		while (iterator.hasNext()) {
//			i++;
//			String uploadFileName = iterator.next(); 
//			//next()를 해주면 커서가 움직여서 iterator안에 들어있는 요소. 즉 여기서는 name을 가져온다.
//			MultipartFile mFile = mRequest.getFile(uploadFileName);
//			//file생성
//			
//			String saveFileName = mFile.getOriginalFilename();
//			//파일이름
//			
//			//saveFileName이 존재할때만 실제 파일로 만들어줘야함.
//			if (saveFileName != null && !saveFileName.equals("")) {
//
//				//저장할 파일의 이름을 겹치지 않게 해주기위해
//				//날짜시간으로 이름을 만들어준다.
//				//Calendar객체에서 현재날짜시간을 받아서
//				//SimpleDateFormat를 이용해 원하는 모양으로 바꿈
//				//yyyy:년 MM:월 dd:일 HH:시 mm:분 ss:초
//				//extension은 확장자명이다. ex)png, jpg....
//				
//				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss-" + i);
//				Calendar now = Calendar.getInstance();
//				String extension = saveFileName.split("\\.")[1];
//				saveFileName = formatter.format(now.getTime()) + "." + extension;
//				//saveFileName에 날짜+.+확장자명으로 저장시킴.
//				
//				//transferTo: 실제 파일을 생성한다.
//				mFile.transferTo(new File(uploadPath + saveFileName));
//			}
//		}
		
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
