package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.*;
import cn.edu.sdjzu.xg.bysj.service.*;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class TeacherDao {
	private static TeacherDao teacherDao=new TeacherDao();
	private TeacherDao(){}
	public static TeacherDao getInstance(){
		return teacherDao;
	}
	private static Collection<Teacher> teachers;

	public Collection<Teacher> findAll() throws SQLException {

		teachers = new TreeSet<Teacher>();

		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from teacher");

		while(resultSet.next()){
			teachers.add(new Teacher(resultSet.getInt("id"),
					resultSet.getString("no"),
					resultSet.getString("name"),
					ProfTitleService.getInstance().find(resultSet.getInt("profTitle_id")),
					DegreeService.getInstance().find(resultSet.getInt("degree_id")),
					DepartmentService.getInstance().find(resultSet.getInt("department_id"))
			));
		}
		//关闭对象
		JdbcHelper.close(stmt,connection);

		return TeacherDao.teachers;
	}

	public Teacher find(Integer id) throws SQLException {
//		teacherDao.findAll();
		Teacher teacher = null;
		Connection connection = JdbcHelper.getConn();
		String findTeacher_sql = "SELECT * FROM Teacher WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(findTeacher_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();

		if (resultSet.next()){
			teacher = new Teacher(resultSet.getInt("id"),
					resultSet.getString("no"),
					resultSet.getString("name"),
					ProfTitleService.getInstance().find(resultSet.getInt("profTitle_id")),
					DegreeService.getInstance().find(resultSet.getInt("degree_id")),
					DepartmentService.getInstance().find(resultSet.getInt("department_id"))
			);
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return teacher;
	}

	public boolean update(Teacher teacher) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String updateTeacher_sql = "update teacher " +
				"set name=?,degree_id=?,department_id=?,profTitle_id=? no=?" +
				"where id=?";
		PreparedStatement pstmt = connection.prepareStatement(updateTeacher_sql);

		pstmt.setString(1,teacher.getName());
		pstmt.setInt(2,teacher.getDegree().getId());
		pstmt.setInt(3,teacher.getDepartment().getId());
		pstmt.setInt(4,teacher.getTitle().getId());
		pstmt.setString(5,teacher.getNo());
		//更改和自己id相同的记录
		pstmt.setInt(6,teacher.getId());
		int affectedRowNum = pstmt.executeUpdate();

		JdbcHelper.close(pstmt,connection);

		return affectedRowNum>0;
	}
	
	public boolean add(Teacher teacher){

		//增加的记录条数
		boolean affectedRowNum = false;
		//创建连接对象
		Connection connection = null;
		PreparedStatement pstmt = null;
		try{
			connection = JdbcHelper.getConn();
			//自动提交设置为false
			connection.setAutoCommit(false);

			//添加教师
			String addTeacher_sql =
					"INSERT INTO Teacher(no,name,profTitle_id,degree_id,department_id) VALUES" +
							" (?,?,?,?,?)";
			//在该链接上创建预编译语句对象
			pstmt = connection.prepareStatement(addTeacher_sql);
			//为预编译参数赋值
			pstmt.setString(1,teacher.getNo());
			pstmt.setString(2,teacher.getName());
			pstmt.setInt(3,teacher.getTitle().getId());
			pstmt.setInt(4,teacher.getDegree().getId());
			pstmt.setInt(5,teacher.getDepartment().getId());


			pstmt.executeUpdate();
			//添加用户
			String addUser_sql =
					"INSERT INTO USER (username,password,loginTime,teacher_id) VALUES" +
							" (?,?,?,?)";
			pstmt = connection.prepareStatement(addUser_sql);
			//为预编译参数赋值
			pstmt.setString(1,teacher.getNo());
			pstmt.setString(2,teacher.getNo());
			pstmt.setDate(3,new Date(System.currentTimeMillis()));
			pstmt.setInt(4,teacher.getId());

			affectedRowNum = pstmt.executeUpdate()>0;
			connection.commit();
		}catch (SQLException e){
			System.out.println(e.getMessage() + "\nerrorCode=" + e.getErrorCode());
			try {
				//回滚当前连接所做的操作
				if (connection != null)
					connection.rollback();
			}catch (SQLException e1){
				e1.printStackTrace();
			}
		}finally {
			try {
				if (connection != null){
					//恢复自动提交
					connection.setAutoCommit(true);
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
		//关闭资源
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum;
	}

	public boolean delete(Integer id){
		//增加的记录条数
		boolean affectedRowNum = false;
		//创建连接对象
		Connection connection = null;
		PreparedStatement pstmt = null;
		try{
			connection = JdbcHelper.getConn();
			connection.setAutoCommit(false);

			String deleteUser_sql =
					"delete from user where username=?";
			//在该链接上创建预编译语句对象
			pstmt = connection.prepareStatement(deleteUser_sql);
			//为预编译参数赋值
			pstmt.setString(1,TeacherService.getInstance().find(id).getNo());

			pstmt.executeUpdate();

			String deleteTeacher =
					"delete from Teacher where id=?";
			pstmt = connection.prepareStatement(deleteTeacher);
			pstmt.setInt(1,id);

			affectedRowNum = pstmt.executeUpdate() > 0;
			connection.commit();
		}catch (SQLException e){
			System.out.println(e.getMessage() + "\nerrorCode=" + e.getErrorCode());
			try {
				//回滚当前连接所做的操作
				if (connection != null)
					connection.rollback();
			}catch (SQLException e1){
				e1.printStackTrace();
			}
		}finally {
			try {
				if (connection != null){
					//恢复自动提交
					connection.setAutoCommit(true);
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
		//关闭资源
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum;
	}

	public Teacher findByNo(String no) throws SQLException {
		Teacher teacher = null;
		Connection connection = JdbcHelper.getConn();
		String findTeacher_sql = "SELECT * FROM Teacher WHERE no=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(findTeacher_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,no);
		ResultSet resultSet = preparedStatement.executeQuery();

		if (resultSet.next()){
			teacher = new Teacher(resultSet.getInt("id"),
					resultSet.getString("no"),
					resultSet.getString("name"),
					ProfTitleService.getInstance().find(resultSet.getInt("profTitle_id")),
					DegreeService.getInstance().find(resultSet.getInt("degree_id")),
					DepartmentService.getInstance().find(resultSet.getInt("department_id"))
			);
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return teacher;
	}

	public boolean delete(Teacher teacher) throws SQLException {
		return this.delete(teacher.getId());
	}

	public static void main(String[] args) throws SQLException {
//		TeacherDao teacherDao = TeacherDao.getInstance();
//		Teacher teacher = teacherDao.find(1);
//		System.out.println(teacher);
//
//		//将对象的字段全部修改修改
//		teacher.setName("老师");
//		//使用update()，完成修改
//		teacherDao.update(teacher);
//		//观察修改后的属性值
//		System.out.println(teacherDao.find(1));
		Teacher teacher = TeacherService.getInstance().find(36);
		teacher.setName("斯123林");
//		teacher.setId(1111);
		TeacherService.getInstance().add(teacher);
	}
	
}
