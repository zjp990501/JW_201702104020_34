package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.GraduateProjectCategory;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.TreeSet;

public final class GraduateProjectCategoryDao {
	//本类的一个对象引用，保存自身对象
	private static GraduateProjectCategoryDao graduateProjectCategoryDao = null;
	//私有的构造方法，防止其它类创建它的对象
	private GraduateProjectCategoryDao(){}
	//静态方法，返回本类的惟一对象
	public synchronized static GraduateProjectCategoryDao getInstance() {
		return graduateProjectCategoryDao == null ? new GraduateProjectCategoryDao()
				: graduateProjectCategoryDao;
	}

	private static Collection<GraduateProjectCategory> graduateProjectCategories;
	
	public Collection<GraduateProjectCategory> findAll() throws SQLException {
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from graduateprojectcategory");

		//将对象添加到集合中
		while(resultSet.next()){
			graduateProjectCategories.add(new GraduateProjectCategory(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks")));
		}

		JdbcHelper.close(stmt,connection);
		return GraduateProjectCategoryDao.graduateProjectCategories;
	}

	public GraduateProjectCategory find(Integer id) throws SQLException {
		graduateProjectCategoryDao.findAll();
		for (GraduateProjectCategory graduateProjectCategory : graduateProjectCategories) {
			if (id.equals(graduateProjectCategory.getId())) {
				return graduateProjectCategory;
			}
		}
		return null;
	}

	public boolean update(GraduateProjectCategory graduateProjectCategory) {
		graduateProjectCategories.remove(graduateProjectCategory);
		return graduateProjectCategories.add(graduateProjectCategory);
	}

	public boolean add(GraduateProjectCategory graduateProjectCategory) {
		return graduateProjectCategories.add(graduateProjectCategory);
	}

	public boolean delete(Integer id) throws SQLException {
		GraduateProjectCategory graduateProjectCategory = this.find(id);
		return this.delete(graduateProjectCategory);
	}

	public boolean delete(GraduateProjectCategory graduateProjectCategory) {
		return graduateProjectCategories.remove(graduateProjectCategory);
	}

	public static void main(String[] args) throws SQLException {
		GraduateProjectCategoryDao dao = new GraduateProjectCategoryDao();
		Collection<GraduateProjectCategory> graduateProjectCategories = dao
				.findAll();
	}
}
