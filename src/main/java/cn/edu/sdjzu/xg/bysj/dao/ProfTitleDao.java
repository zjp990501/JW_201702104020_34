package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.ProfTitle;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class ProfTitleDao {
	private static ProfTitleDao profTitleDao = new ProfTitleDao();
	private ProfTitleDao(){}
	public static ProfTitleDao getInstance(){
		return profTitleDao;
	}
	private static Collection<ProfTitle> profTitles = new TreeSet<ProfTitle>();;

	public Collection<ProfTitle> findAll() throws SQLException {
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from proftitle");

		//将对象添加到集合中
		while(resultSet.next()){
			profTitles.add(new ProfTitle(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks")));
		}
		JdbcHelper.close(stmt,connection);

		return ProfTitleDao.profTitles;
	}

	public ProfTitle find(Integer id) throws SQLException {
		profTitleDao.findAll();
		ProfTitle profTitle = null;
		Connection connection = JdbcHelper.getConn();
		String findProfTitle_sql = "SELECT * FROM profTitle WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(findProfTitle_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			profTitle = new ProfTitle(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks")
			);
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);

		return profTitle;
	}

	public boolean update(ProfTitle profTitle) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String updateProfTitle_sql = "update proftitle " +
				"set description=?,no=?,remarks=? " +
				"where id=?";
		PreparedStatement pstmt = connection.prepareStatement(updateProfTitle_sql);

		pstmt.setString(1,profTitle.getDescription());
		pstmt.setString(3,profTitle.getRemarks());
		pstmt.setString(2,profTitle.getNo());

		//更改和自己id相同的记录
		pstmt.setInt(4,profTitle.getId());
		int affectedRowNum = pstmt.executeUpdate();
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum>0;
	}

	public boolean add(ProfTitle profTitle) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String addProfTitle_sql =
				"INSERT INTO Degree(no,description,remarks) VALUES" +
						" (?,?,?)";
		//在该链接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addProfTitle_sql);
		//为预编译参数赋值
		pstmt.setString(3,profTitle.getRemarks());
		pstmt.setString(1,profTitle.getNo());
		pstmt.setString(2,profTitle.getDescription());

		int affectedRowNum = pstmt.executeUpdate();

		//执行预编译对象的executeUpdate方法,获取添加的记录行数
		System.out.println("添加了" + affectedRowNum + "条记录... ");

		//关闭对象
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum > 0;
	}

	public boolean delete(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String deleteProTitle_sql =
				"delete from profTitle where id=?";
		//在该链接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(deleteProTitle_sql);
		//为预编译参数赋值
		int affectedRowNum = pstmt.executeUpdate();
		pstmt.setInt(1,id);

		//执行预编译对象的executeUpdate方法,获取删除的记录行数
		System.out.println("删除了" + affectedRowNum + "条记录...");
		JdbcHelper.close(pstmt,connection);

		return affectedRowNum > 0;
	}

	public boolean delete(ProfTitle profTitle) throws SQLException {
		return profTitleDao.delete(profTitle.getId());
	}

	public static void main(String[] args) throws SQLException {
		//创建一个ProfTitleDao对象
		ProfTitleDao profTitleDao = ProfTitleDao.getInstance();
		//通过find()获得对应的对象,由profTitle指向
		ProfTitle profTitle = profTitleDao.find(1);
		//观察属性值
		System.out.println(profTitle);

		//将对象的字段修改
		profTitle.setRemarks("包治百病");
		//使用update()，完成修改
		profTitleDao.update(profTitle);
		//观察修改后的属性值
		System.out.println(profTitleDao.find(1));
	}
}

