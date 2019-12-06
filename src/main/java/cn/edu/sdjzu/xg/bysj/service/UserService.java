package cn.edu.sdjzu.xg.bysj.service;


import cn.edu.sdjzu.xg.bysj.dao.UserDao;
import cn.edu.sdjzu.xg.bysj.domain.User;

import java.sql.SQLException;
import java.util.Collection;

public final class UserService {
	private UserDao userDao = UserDao.getInstance();
	private static UserService userService = new UserService();
	
	public UserService() {
	}
	
	public static UserService getInstance(){
		return UserService.userService;
	}

	public Collection<User> findAll() throws SQLException {
		return userDao.findAll();
	}
	
	public User find(Integer id) throws SQLException {
		return userDao.find(id);
	}

	
	public boolean add(User user) throws SQLException {
		return userDao.add(user);
	}

	public boolean delete(Integer id) throws SQLException {
		User user = this.find(id);
		return this.delete(user);
	}
	
	public boolean delete(User user) throws SQLException {
		return userDao.delete(user.getId());
	}

	public boolean delete(String no) throws SQLException {
//		User user = this.find(no);
		return userDao.delete(no);
	}

	public boolean changePassword(String username, String newPassword) throws SQLException {
		return userDao.changePassword(username, newPassword);
	}
	
	public User login(String username, String password) throws SQLException {
//		Collection<User> users = this.findAll();
//		User desiredUser = null;
//		for(User user:users){
//			if(username.equals(user.getUsername()) && password.equals(user.getPassword())){
//				desiredUser = user;
//			}
//		}
		return userDao.login(username, password);
	}	
}
