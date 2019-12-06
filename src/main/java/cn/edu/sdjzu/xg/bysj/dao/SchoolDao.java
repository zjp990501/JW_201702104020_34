package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.School;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class SchoolDao {
	private static SchoolDao schoolDao = new SchoolDao();
	private static Collection<School> schools;
	
	public SchoolDao(){}
	
	public static SchoolDao getInstance(){
		return schoolDao;
	}

	public Collection<School> findAll() throws SQLException {
		schools = new TreeSet<School>();
		//School和Degree本程序不同点：School抛出异常留给main方法处理,在degree中自己处理异常
		Connection connection = JdbcHelper.getConn();
		//在该链接上创建语句盒子对象
		Statement stmt = connection.createStatement();
		//执行sql查询结果并获得集对象
		ResultSet resultSet = stmt.executeQuery("select * from School");

		//将对象添加到集合中
		while(resultSet.next()){
			schools.add(new School(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks")));
		}

		JdbcHelper.close(stmt,connection);
		return SchoolDao.schools;
	}
	
	public School find(Integer id) throws SQLException {
		schoolDao.findAll();//先放到这里
		School desiredSchool = null;
		for (School school : schools) {
			if(id.equals(school.getId())){
				desiredSchool =  school;
			}
		}
		return desiredSchool;
	}
	
	public boolean update(School school) throws SQLException {
//		schools.remove(school);
//		return schools.add(school);//--原版
//		this.delete(school);
//		return this.add(school);//--想过这么改
		Connection connection = JdbcHelper.getConn();
		String updateSchool_sql = "update school " +
				"set description=?,no=?,remarks=? " +
				"where id=?";
		PreparedStatement pstmt = connection.prepareStatement(updateSchool_sql);

		pstmt.setString(1,school.getDescription());
		pstmt.setString(2,school.getNo());
		pstmt.setString(3,school.getRemarks());

		//更改和自己id相同的记录
		pstmt.setInt(4,school.getId());
		int affectedRowNum = pstmt.executeUpdate();
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum>0;
	}
	
	public boolean add(School school) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String addSchool_sql =
				"INSERT INTO School(no,description,remarks) VALUES" +
						" (?,?,?)";
		//在该链接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addSchool_sql);
		//为预编译参数赋值
		pstmt.setString(2,school.getDescription());
		pstmt.setString(1,school.getNo());
		pstmt.setString(3,school.getRemarks());

		int affectedRowNum = pstmt.executeUpdate();

		//执行预编译对象的executeUpdate方法,获取添加的记录行数
		System.out.println("添加了" + affectedRowNum + "条记录...");
		//关闭对象
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum > 0;
//		//增加的记录条数
//		int affectedRowNum = 0;
//		//创建连接对象
//		Connection connection = null;
//		PreparedStatement pstmt = null;
//		try{
//			connection = JdbcHelper.getConn();
//			//自动提交设置为false
//			connection.setAutoCommit(false);
//			String addSchool_sql =
//					"INSERT INTO School(no,description) VALUES" +
//							" (?,?)";
//			//在该链接上创建预编译语句对象
//			pstmt = connection.prepareStatement(addSchool_sql);
//			//为预编译参数赋值,如果no字段重复，则会违反唯一性约束，后面会抛出异常
//			pstmt.setString(1,school.getNo());
//			pstmt.setString(2,school.getDescription());
//
//			affectedRowNum = pstmt.executeUpdate();
//			//执行预编译对象的executeUpdate方法,获取添加的记录行数
////			System.out.println("添加了" + affectedRowNum + "条记录...");
//			//关闭对象
//			connection.commit();
//		}catch (Exception e){
//			System.out.println(e.getMessage());
////			System.out.println("\nerrorCode=" + e.getErrorCode());
//			try {
//				//回滚当前连接所做的操作
//				if (connection != null)
//					connection.rollback();
//			}catch (SQLException e1){
//				e1.printStackTrace();
//			}
//		}finally {
//			try {
//				if (connection != null){
//					//恢复自动提交
//					connection.setAutoCommit(true);
//					//提交操作
//				}
//			}catch (SQLException e){
//				e.printStackTrace();
//			}
//		}
	}

	public boolean delete(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String addSchool_sql =
				"delete from School where id=?";
		//在该链接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addSchool_sql);
		//为预编译参数赋值
		pstmt.setInt(1,id);
		int affectedRowNum = pstmt.executeUpdate();
		//执行预编译对象的executeUpdate方法,获取删除的记录行数
//		System.out.println("删除了" + pstmt.executeUpdate() + "条记录...");
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum>0;
	}

	//实验
	public boolean delete(School school) throws SQLException {
		return this.delete(school.getId());
	}

	/**
	 * 使用存储过程增加一个School对象
	 * @param school 需要存入的School对象(没有id)
	 * @return 存入的School对象，用数据库生成id为参数赋值,使之完整
	 * @throws SQLException
	 */
	public School addWithSP(School school) throws SQLException {
		//School和Degree本程序不同点：School抛出异常留给main方法处理,在degree中自己处理异常
		Connection connection = JdbcHelper.getConn();
		//根据连接对象准备可调用语句对象,sp_addSchool为存储过程名称
		CallableStatement callableStatement = connection.prepareCall("{call sp_addSchool (?,?,?,?)}");
		//将第五个参数设置为输出参数，参数类型为长整型，数据库的数据类型
		callableStatement.registerOutParameter(4,Types.BIGINT);
		callableStatement.setString(1,school.getDescription());
		callableStatement.setString(2,school.getNo());
		callableStatement.setString(3,school.getRemarks());
		//执行可调用语句callableStatement
		callableStatement.execute();

		//获得第四个参数的值:数据库为该记录自动生成的id,然后给school的id赋值
		school.setId(callableStatement.getInt(4));
		JdbcHelper.close(callableStatement,connection);
		return school;
	}

	public static void main(String[] args) throws SQLException {
		//创建一个SchoolDao对象
		SchoolDao schoolDao = SchoolDao.getInstance();
		//通过find()获得对应的对象,由school指向
		School school = schoolDao.find(26);
		//观察属性值
		System.out.println(school);

		//将对象的字段全部修改修改
		school.setDescription("宇宙大学");
		school.setNo("04");
		school.setRemarks("幼儿园毕业证");
		//使用update()，完成修改
		schoolDao.update(school);
		//观察修改后的属性值
		System.out.println(schoolDao.find(26));
	}
}
