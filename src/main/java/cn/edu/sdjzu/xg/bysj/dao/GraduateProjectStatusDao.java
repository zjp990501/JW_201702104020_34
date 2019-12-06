package cn.edu.sdjzu.xg.bysj.dao;


import cn.edu.sdjzu.xg.bysj.domain.GraduateProjectStatus;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.TreeSet;

public final class GraduateProjectStatusDao {
	private static GraduateProjectStatusDao graduateProjectStatusDao = new GraduateProjectStatusDao();
	private static Collection<GraduateProjectStatus> graduateProjectStatuses;

	private GraduateProjectStatusDao(){}

	public static GraduateProjectStatusDao getInstance(){
		try {
			Connection connection = JdbcHelper.getConn();
			Statement stmt = connection.createStatement();
			ResultSet resultSet = stmt.executeQuery("select * from graduateprojectcategory");

			//将对象添加到集合中
			while(resultSet.next()){
				graduateProjectStatuses.add(new GraduateProjectStatus(
						resultSet.getInt("id"),
						resultSet.getString("description"),
						resultSet.getString("no"),
						resultSet.getString("remarks")));
			}
		}catch (SQLException e){
			e.printStackTrace();
		}

		return graduateProjectStatusDao;
	}

	public Collection<GraduateProjectStatus> findAll(){
		return GraduateProjectStatusDao.graduateProjectStatuses;
	}

	public GraduateProjectStatus find(Integer id){
		GraduateProjectStatus desiredGraduateProjectStatus = null;
		for (GraduateProjectStatus graduateProjectStatus : graduateProjectStatuses) {
			if(id.equals(graduateProjectStatus.getId())){
				desiredGraduateProjectStatus =  graduateProjectStatus;
			}
		}
		return desiredGraduateProjectStatus;
	}

	public boolean update(GraduateProjectStatus graduateProjectStatus){
		graduateProjectStatuses.remove(graduateProjectStatus);
		return graduateProjectStatuses.add(graduateProjectStatus);
	}

	public boolean add(GraduateProjectStatus graduateProjectStatus){
		return graduateProjectStatuses.add(graduateProjectStatus);
	}

	public boolean delete(Integer id){
		GraduateProjectStatus graduateProjectStatus =this.find(id);
		return this.delete(graduateProjectStatus);
	}

	public boolean delete(GraduateProjectStatus graduateProjectStatus){
		return GraduateProjectStatusDao.graduateProjectStatuses.remove(graduateProjectStatus);
	}
}
