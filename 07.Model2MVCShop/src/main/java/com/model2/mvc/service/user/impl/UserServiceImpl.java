package com.model2.mvc.service.user.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.QuitUser;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.UserDao;
import com.model2.mvc.service.user.UserService;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService{
	
	@Autowired
	@Qualifier("userDaoImpl")
	private UserDao userDao;
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public UserServiceImpl() {
	}

	///Method
	public void addUser(User user) throws Exception {
		userDao.addUser(user);
	}

	public User getUser(String userId) throws Exception {
		return userDao.getUser(userId);
	}

	public Map<String, Object> getUserList(Search search, String sort) throws Exception {
		List<User> list= userDao.getUserList(search, sort);
		int totalCount = userDao.getTotalCount(search);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list );
		map.put("totalCount", new Integer(totalCount));
		
		return map;
	}

	public void updateUser(User user) throws Exception {
		userDao.updateUser(user);
	}

	public boolean checkDuplication(String userId) throws Exception {
		boolean result=true;
		User user=userDao.getUser(userId);
		if(user != null) {
			result=false;
		}
		return result;
	}

	public void quitUser(String userId, String reason) throws Exception {
		userDao.quitUser(userId, reason);
	}

	public void deleteUser(String userId) throws Exception {
		userDao.deleteUser(userId);
	}

	@Override
	public Map<String,Object> getQuitUserList() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<QuitUser> user = userDao.getQuitUserList();
		map.put("user", user);
		
		Map<String,Integer> reason = new HashMap<String,Integer>(); 
		for(int i=0; i<user.size(); i++) {
			String reasonKey = user.get(i).getReason();
			if(reason.containsKey(reasonKey)) { //map俊 历厘等 reason老 版快
				int reasonValue = reason.get(reasonKey);
				reasonValue++;
				reason.put(reasonKey, reasonValue);
			}else { //map俊 历厘救等 reason老 版快
				reason.put(reasonKey, 1);
			}
		}
		map.put("reason", reason);
		
		return map;
	}
	
}