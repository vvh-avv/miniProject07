package com.model2.mvc.web.user;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.UserService;

@Controller
public class UserController {

	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	public UserController(){
		System.out.println(this.getClass());
	}

	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
		
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	@RequestMapping("/addUserView.do")
	public String addUserView() throws Exception {
		System.out.println("/addUserView.do");
		
		return "redirect:/user/addUserView.jsp";
	}
	
	@RequestMapping("/addUser.do")
	public String addUser( @ModelAttribute("user") User user ) throws Exception {
		System.out.println("/addUser.do :: "+user);
		
		userService.addUser(user);
		
		return "redirect:/user/loginView.jsp";
	}
	
	@RequestMapping("/getUser.do")
	public String getUser( @RequestParam("userId") String userId , Model model ) throws Exception {
		System.out.println("/getUser.do");
		
		User user = userService.getUser(userId);
		
		model.addAttribute("user", user);
		
		return "forward:/user/getUser.jsp";
	}
	
	@RequestMapping("/updateUserView.do")
	public String updateUserView( @RequestParam("userId") String userId , Model model ) throws Exception{
		System.out.println("/updateUserView.do");
		
		User user = userService.getUser(userId);
		
		model.addAttribute("user", user);
		
		return "forward:/user/updateUser.jsp";
	}
	
	@RequestMapping("/updateUser.do")
	public String updateUser( @ModelAttribute("user") User user , Model model , HttpSession session) throws Exception{
		System.out.println("/updateUser.do");
		
		userService.updateUser(user);
		
		String sessionId=((User)session.getAttribute("user")).getUserId();
		if(sessionId.equals(user.getUserId())){
			session.setAttribute("user", user);
		}
		
		return "redirect:/getUser.do?userId="+user.getUserId();
	}
	
	@RequestMapping("/loginView.do")
	public String loginView() throws Exception{
		System.out.println("/loginView.do");

		return "redirect:/user/loginView.jsp";
	}
	
	@RequestMapping("/login.do")
	public String login( @ModelAttribute("user") User user , HttpSession session ) throws Exception{
		System.out.println("/login.do");
		
		User dbUser=userService.getUser(user.getUserId());
		
		//입력한 회원정보가 없을 때
		if(dbUser==null) {
			return "redirect:/user/loginView.jsp";
		}
		
		if( user.getPassword().equals(dbUser.getPassword()) ){
			session.setAttribute("user", dbUser);
		}
		
		return "redirect:/index.jsp";
	}
	
	@RequestMapping("/logout.do")
	public String logout(HttpSession session ) throws Exception{
		System.out.println("/logout.do");
		
		session.invalidate();
		
		return "redirect:/index.jsp";
	}
	
	@RequestMapping("/checkDuplication.do")
	public String checkDuplication( @RequestParam("userId") String userId , Model model ) throws Exception{
		System.out.println("/checkDuplication.do");
		
		boolean result=userService.checkDuplication(userId);
		
		model.addAttribute("result", new Boolean(result));
		model.addAttribute("userId", userId);

		return "forward:/user/checkDuplication.jsp";
	}
	
	@RequestMapping("/listUser.do")
	public String listUser( @ModelAttribute("search") Search search, @RequestParam(value="sort", required=false, defaultValue="asc") String sort,
								Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("/listUser.do");
		
		if(search.getCurrentPage()==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map=userService.getUserList(search, sort);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println("listUser :: resultPage :: "+resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		model.addAttribute("sort", sort);
		
		return "forward:/user/listUser.jsp";
	}
	
	@RequestMapping("/quitUser.do")
	public String quitUser( @RequestParam("reason") String reason, @RequestParam("userId") String userId ) throws Exception{
		System.out.println("/quitUser.do");
		
		if(reason.contains(",")) {
			reason = reason.split(",")[0];
		}
		userService.quitUser(userId, reason); //탈퇴DB에 insert
		userService.deleteUser(userId); //회원DB에 delete
		
		return "/logout.do";
	}
	
	@RequestMapping("/statsUser.do")
	public String statsUser(Model model) throws Exception{
		System.out.println("/statsUser.do");
		
		Map<String, Object> map = userService.getQuitUserList();
		//map에 두개가 담겨져 있음
		//List<QuitUser> user
		//Map<String, Integer> reason
		Map<String, Integer> reason = (Map<String, Integer>) map.get("reason");
		
		//데이터 가공 후 전달
		String result = "";
		Set<String> reasonKeys = reason.keySet();

		for(String key : reasonKeys) {
			if(result!="") {
				result += ",";
			}
			result += "['"+key+"', "+reason.get(key)+"]";
		}
		
		model.addAttribute("result", result);
		
		return "forward:/user/statsUser.jsp";
	}
}
