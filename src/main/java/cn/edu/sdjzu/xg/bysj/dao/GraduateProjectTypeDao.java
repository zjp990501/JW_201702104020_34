package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.GraduateProjectType;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.TreeSet;

public final class GraduateProjectTypeDao {
	private static GraduateProjectTypeDao graduateProjectTypeDao = new GraduateProjectTypeDao();
	
	private GraduateProjectTypeDao() {}
	public static GraduateProjectTypeDao getInstance(){
		return graduateProjectTypeDao;
	}
	
	private static Collection<GraduateProjectType> graduateProjectTypes;
	static{
		graduateProjectTypes = new TreeSet<GraduateProjectType>();
		GraduateProjectType design = new GraduateProjectType(1,"应用研究","01","");
		graduateProjectTypes.add(design);
		graduateProjectTypes.add(new GraduateProjectType(2,"理论研究","02",""));
		graduateProjectTypes.add(new GraduateProjectType(3,"软件开发","03",""));
		graduateProjectTypes.add(new GraduateProjectType(4,"社会调查","04",""));
		graduateProjectTypes.add(new GraduateProjectType(5,"XXX会调查","04",""));

	}
	public Collection<GraduateProjectType> findAll() throws SQLException {
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from graduateprojectcategory");

		//将对象添加到集合中
		while(resultSet.next()){
			graduateProjectTypes.add(new GraduateProjectType(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks")));
		}

		return GraduateProjectTypeDao.graduateProjectTypes;
	}
	
	public GraduateProjectType find(Integer id) throws SQLException {
		graduateProjectTypeDao.findAll();
		GraduateProjectType desiredGraduateProjectType = null;
		for (GraduateProjectType graduateProjectType : graduateProjectTypes) {
			if(id.equals(graduateProjectType.getId())){
				desiredGraduateProjectType = graduateProjectType;
			}
		}
		return desiredGraduateProjectType;
	}
	
	public boolean update(GraduateProjectType graduateProjectType){
		graduateProjectTypes.remove(graduateProjectType);
		return graduateProjectTypes.add(graduateProjectType);
	}
	
	public boolean add(GraduateProjectType graduateProjectType){
		return graduateProjectTypes.add(graduateProjectType);
	}

	public boolean delete(Integer id) throws SQLException {
		GraduateProjectType graduateProjectType = this.find(id);
		return this.delete(graduateProjectType);
	}
	
	public boolean delete(GraduateProjectType graduateProjectType){
		return graduateProjectTypes.remove(graduateProjectType);
	}
	
	public static void main(String[] args) throws SQLException {
		GraduateProjectTypeDao dao = new GraduateProjectTypeDao();
		Collection<GraduateProjectType> graduateProjectTypes = dao.findAll();
		display(graduateProjectTypes);
	}

	private static void display(Collection<GraduateProjectType> graduateProjectTypes) {
		for (GraduateProjectType graduateProjectType : graduateProjectTypes) {
			System.out.println(graduateProjectType);
		}
	}
}
