package com.model2.mvc.service.user;

import java.util.List;
import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.QuitUser;
import com.model2.mvc.service.domain.User;

public interface UserService {
	
	public void addUser(User user) throws Exception;
	
	public User getUser(String userId) throws Exception;
	
	public Map<String, Object> getUserList(Search search, String sort) throws Exception;
	
	public void updateUser(User user) throws Exception;
	
	public boolean checkDuplication(String userId) throws Exception;

	public void quitUser(String userId, String reason) throws Exception;

	public void deleteUser(String userId) throws Exception;

	public Map<String, Object> getQuitUserList() throws Exception;
	
}