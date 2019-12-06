package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.Degree;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class DegreeDao {
	private static DegreeDao degreeDao=
			new DegreeDao();
	private DegreeDao(){}
	public static DegreeDao getInstance(){
		return degreeDao;
	}
	private static Collection<Degree> degrees;

	public Collection<Degree> findAll() throws SQLException {
		degrees = new TreeSet<Degree>();
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from Degree");

		while(resultSet.next()){
			degrees.add(new Degree(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks")));
		}
		//关闭对象
		JdbcHelper.close(stmt,connection);

		return DegreeDao.degrees;
	}

	/**
	 * @param id
	 * @return 查找到的degree对象
	 */
	public Degree find(Integer id) throws SQLException {
//		degreeDao.findAll();//先放在这里
		Degree degree = null;
		Connection connection = JdbcHelper.getConn();
		String findDegree_sql = "SELECT * FROM degree WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(findDegree_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			degree = new Degree(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return degree;
	}

	public boolean update(Degree degree) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String updateDegree_sql = "update degree " +
				"set description=?,no=?,remarks=? " +
				"where id=?";
		PreparedStatement pstmt = connection.prepareStatement(updateDegree_sql);

		pstmt.setString(3,degree.getRemarks());
		pstmt.setString(1,degree.getDescription());
		pstmt.setString(2,degree.getNo());

		//更改和自己id相同的记录
		pstmt.setInt(4,degree.getId());
		int affectedRowNum = pstmt.executeUpdate();

		JdbcHelper.close(pstmt,connection);

		return affectedRowNum>0;
	}

	public boolean add(Degree degree) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String addDegree_sql =
				"INSERT INTO Degree(no,description,remarks) VALUES" +
						" (?,?,?)";
		//在该链接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addDegree_sql);
		//为预编译参数赋值
		pstmt.setString(1,degree.getNo());
		pstmt.setString(2,degree.getDescription());
		pstmt.setString(3,degree.getRemarks());

		int affectedRowNum = pstmt.executeUpdate();

		//执行预编译对象的executeUpdate方法,获取添加的记录行数
		System.out.println("添加了" + affectedRowNum + "条记录...");
		//关闭对象
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum > 0;
	}

	public boolean delete(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String deleteDegree_sql =
				"delete from Degree where id=?";
		//在该链接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(deleteDegree_sql);
		//为预编译参数赋值
		pstmt.setInt(1,id);

		int affectedRowNum = pstmt.executeUpdate();
		//执行预编译对象的executeUpdate方法,获取删除的记录行数
		System.out.println("删除了" + affectedRowNum + "条记录...");
		JdbcHelper.close(pstmt,connection);
//		Degree degreeController = this.find(id);
//		return this.delete(degreeController);
		return affectedRowNum > 0;
	}

	//实验
	public boolean delete(Degree degree) throws SQLException {
		this.delete(degree.getId());
		return this.delete(degree);
	}

	public static void main(String[] args) throws SQLException {
		//创建一个DegreeDao对象
		DegreeDao degreeDao = DegreeDao.getInstance();
		//通过find()获得对应的对象,由degree指向
		Degree degree = degreeDao.find(18);
		//观察属性值
		System.out.println(degree);

		//将对象的字段全部修改修改
		degree.setDescription("杨志恒");
		degree.setNo("04");
		degree.setRemarks("幼儿园荣誉毕业哈");
		//使用update()，完成修改
		degreeDao.update(degree);
		//观察修改后的属性值
		System.out.println(degreeDao.find(18));
	}
}

