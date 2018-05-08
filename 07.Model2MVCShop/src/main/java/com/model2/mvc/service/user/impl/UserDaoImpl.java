package com.model2.mvc.service.user.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.QuitUser;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.UserDao;

@Repository("userDaoImpl")
public class UserDaoImpl implements UserDao{
	
	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public UserDaoImpl() {
	}

	///Method
	public void addUser(User user) throws Exception {
		sqlSession.insert("UserMapper.insertUser", user);
	}

	public User getUser(String userId) throws Exception {
		return sqlSession.selectOne("UserMapper.findUser", userId);
	}
	
	public void updateUser(User user) throws Exception {
		sqlSession.update("UserMapper.updateUser", user);
	}

	public List<User> getUserList(Search search, String sort) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("search", search);
		map.put("sort", sort);
		
		return sqlSession.selectList("UserMapper.getUserList", map);
	}

	public int getTotalCount(Search search) throws Exception {
		return sqlSession.selectOne("UserMapper.getTotalCount", search);
	}

	public void deleteUser(String userId) throws Exception {
		sqlSession.delete("UserMapper.deleteUser", userId);
	}

	public void quitUser(String userId, String reason) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		
		map.put("userId", userId);
		map.put("reason", reason);
		
		sqlSession.insert("QuitUserMapper.insertUser", map);
	}

	public List<QuitUser> getQuitUserList() throws Exception {
		return sqlSession.selectList("QuitUserMapper.getUserList");
	}

}