package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.Teacher;
import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.TeacherService;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import com.alibaba.fastjson.JSON;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;


public final class UserDao {
	private static UserDao userDao=new UserDao();
	private UserDao(){}
	public static UserDao getInstance(){
		return userDao;
	}
	
	private static Collection<User> users;

	public Collection<User> findAll() throws SQLException {
		users = new TreeSet<User>();
        Connection connection = JdbcHelper.getConn();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from user");

        while(resultSet.next()){
            users.add(new User(resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getDate("loginTime"),
					TeacherService.getInstance().findByNo(resultSet.getString("username")))
			);
        }
        //关闭
        JdbcHelper.close(stmt,connection);
		return UserDao.users;
	}
	/**
	 * @param id
	 * @return 查找到的user对象
	 */
	public User find(Integer id) throws SQLException {
//		userDao.findAll();//先放在这里
		User user = null;
		Connection connection = JdbcHelper.getConn();
		String findUser_sql = "SELECT * FROM user WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(findUser_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()){
			user = new User(id,
					resultSet.getString("username"),
					resultSet.getString("password"),
					resultSet.getDate("loginTime"),
                    TeacherService.getInstance().findByNo(resultSet.getString("username"))
            );
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return user;
	}

	public User findByUsername(String username) throws SQLException {
		User user = null;
		Connection connection = JdbcHelper.getConn();
		String findUserByUsername_sql = "SELECT * FROM USER WHERE USERNAME=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(findUserByUsername_sql);
		preparedStatement.setString(1,username);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next())
			user = new User(resultSet.getInt("id"),
					username,
					resultSet.getString("password"),
					resultSet.getDate("loginTime"),
                    TeacherService.getInstance().findByNo(resultSet.getString("username")));
		return user;
	}


	public boolean changePassword(String username, String newPassword) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String updateDegree_sql = "UPDATE user " +
				"SET password=?" + //按照道理，用户名和关联教师不能修改
				"WHERE username=?";
		PreparedStatement pstmt = connection.prepareStatement(updateDegree_sql);

		pstmt.setString(1,newPassword);
		pstmt.setString(2,username);

		int affectedRowNum = pstmt.executeUpdate();

		JdbcHelper.close(pstmt,connection);

		return affectedRowNum>0;
	}

	public boolean add(User user) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String addDegree_sql =
				"INSERT INTO USER (username,password,loginTime,teacher_id) VALUES" +
						" (?,?,?,?)";
		//在该链接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addDegree_sql);
		//为预编译参数赋值
		pstmt.setString(1,user.getUsername());
		pstmt.setString(2,user.getPassword());
		pstmt.setDate(3,user.getLoginTime());
		pstmt.setInt(4,user.getTeacher().getId());

		int affectedRowNum = pstmt.executeUpdate();

		//执行预编译对象的executeUpdate方法,获取添加的记录行数
		System.out.println("添加了" + affectedRowNum + "条记录...");
		//关闭对象
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum > 0;
	}

	public boolean delete(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String deleteUser_sql =
				"delete from Degree where id=?";
		//在该链接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(deleteUser_sql);
		//为预编译参数赋值
		pstmt.setInt(1,id);

		int affectedRowNum = pstmt.executeUpdate();
		//执行预编译对象的executeUpdate方法,获取删除的记录行数
		System.out.println("删除了" + affectedRowNum + "条记录... ");
		JdbcHelper.close(pstmt,connection);

		return affectedRowNum > 0;
	}

	public boolean delete(String username) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String deleteUser_sql =
				"delete from user where username=?";
		//在该链接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(deleteUser_sql);
		//为预编译参数赋值
		pstmt.setString(1,username);

		int affectedRowNum = pstmt.executeUpdate();
		//执行预编译对象的executeUpdate方法,获取删除的记录行数
		System.out.println("删除了" + affectedRowNum + "条记录...  ");
		JdbcHelper.close(pstmt,connection);

		return affectedRowNum > 0;
	}

    /**
     * 也可以使用多条件查询语句select * from user where username=? and password=?
     * 无需判断密码是否为空，后面有equals(使用equals必须判断是否为空)
     * @param username 用户名
     * @param password 密码
     * @return 返回查找对象
     * @throws SQLException
     */
	public User login(String username, String password) throws SQLException {
	    User user = userDao.findByUsername(username);
        if (username == null || user == null) return null;
        else if (user.getPassword().equals(password)) return user;
        else return null;

    }


//	public boolean delete(User user){
////		this.delete(user.getId());
////		return user.remove(user);
//		return false;//未作修改
//	}
	
	
	public static void main(String[] args) throws SQLException {
//		UserDao dao = new UserDao();
		User user = UserService.getInstance().find(4);
		Teacher teacher = TeacherService.getInstance().findByNo(user.getUsername());
		System.out.println("---------------------------");
		user.setTeacher(teacher);
		String user_json = JSON.toJSONString(user);
		System.out.println(user_json);
		System.out.println("=============================");
		Collection users = UserService.getInstance().findAll();
		String users_json = JSON.toJSONString(users);
		System.out.println(users_json);
//		System.out.println(dao.login("young","110"));
	}

	private static void display(Collection<User> users) {
		for (User user : users) {
			System.out.println(user);
		}
	}
	
}
