package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.Department;
import cn.edu.sdjzu.xg.bysj.domain.School;
import cn.edu.sdjzu.xg.bysj.service.DepartmentService;
import cn.edu.sdjzu.xg.bysj.service.SchoolService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class DepartmentDao {
	private static Collection<Department> departments;

	private static DepartmentDao departmentDao=new DepartmentDao();
	private DepartmentDao(){}

	public static DepartmentDao getInstance(){
		return departmentDao;
	}


	public Collection<Department> findAll() throws SQLException {
		departments = new TreeSet<Department>();
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from department");

		//将对象添加到集合中
		while(resultSet.next()){
			departments.add(new Department(
					resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"),
					SchoolService.getInstance().find(resultSet.getInt("school_id"))
			));
		}
		JdbcHelper.close(stmt,connection);
		return DepartmentDao.departments;
	}

	public Collection<Department> findAllBySchool(Integer schoolId) throws SQLException {
		Collection findAllBySchool = new TreeSet<Department>();
		Connection connection = JdbcHelper.getConn();
		String findById_sql = "SELECT * FROM department WHERE school_id=?";
		PreparedStatement preparedStatement = connection.prepareStatement(findById_sql);
		preparedStatement.setInt(1,schoolId);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()){
			findAllBySchool.add(new Department(
					resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"),
					SchoolService.getInstance().find(resultSet.getInt("school_id"))
			));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return findAllBySchool;
	}

	public Department find(Integer id) throws SQLException {
		departmentDao.findAll();

		Department department = null;
		Connection connection = JdbcHelper.getConn();
		String update_sql = "SELECT * FROM department WHERE id=?";
		PreparedStatement preparedStatement = connection.prepareStatement(update_sql);
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()){
			department = new Department(
					resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"),
					SchoolService.getInstance().find(resultSet.getInt("school_id"))
			);
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return department;
	}

	public boolean update(Department department) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String updateDepartment_sql = "update department " +
				"set description=?,no=?,remarks=?,school_id=? " +
				"where id=?";
		PreparedStatement pstmt = connection.prepareStatement(updateDepartment_sql);

		pstmt.setString(1,department.getDescription());
		pstmt.setString(3,department.getRemarks());
		pstmt.setString(2,department.getNo());
		System.out.println(department.getSchool());
		pstmt.setInt(4,department.getSchool().getId());

		//更改和自己id相同的记录
		pstmt.setInt(5,department.getId());
		int affectedRowNum = pstmt.executeUpdate();
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum>0;
	}

	public boolean add(Department department) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String addDepartment_sql =
				"INSERT INTO Department(no,description,remarks,school_id) VALUES" +
						" (?,?,?,?)";
		//在该链接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addDepartment_sql);
		//为预编译参数赋值
		pstmt.setString(1,department.getNo());
		pstmt.setString(2,department.getDescription());
		pstmt.setString(3,department.getRemarks());
		pstmt.setInt(4,department.getSchool().getId());


		int affectedRowNum = pstmt.executeUpdate();

		//执行预编译对象的executeUpdate方法,获取添加的记录行数
		System.out.println("添加了" + affectedRowNum + "条记录...");
		//关闭对象
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum > 0;
	}

	public boolean delete(Integer id) throws SQLException {
		int affectedRowNum = 0;
		Connection connection = JdbcHelper.getConn();
		String sql =
				"delete from department where id=?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1,id);

		affectedRowNum = pstmt.executeUpdate();
		System.out.println("删除了" + affectedRowNum + "条记录...");
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum > 0;
	}

	public boolean delete(Department department){
		return departments.remove(department);
	}

	public static void main(String[] args) throws SQLException {
		DepartmentDao departmentDao = DepartmentDao.getInstance();
		Department department = departmentDao.find(1);
		//观察属性值
		System.out.println(department);

		//将对象的字段修改
		department.setRemarks("信息管理真香123!!!");
		//使用update()，完成修改
		departmentDao.update(department);
		//观察修改后的属性值
		System.out.println(departmentDao.find(1));
	}


//	alter table department add column id bigint not null auto_increment primary key;
//	ALTER  TABLE  department  DROP  PRIMARY  KEY;
}

