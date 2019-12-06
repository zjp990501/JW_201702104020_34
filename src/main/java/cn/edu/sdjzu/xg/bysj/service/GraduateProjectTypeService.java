package cn.edu.sdjzu.xg.bysj.service;


import cn.edu.sdjzu.xg.bysj.dao.GraduateProjectTypeDao;
import cn.edu.sdjzu.xg.bysj.domain.GraduateProjectType;

import java.sql.SQLException;
import java.util.Collection;

public final class GraduateProjectTypeService {
	private static GraduateProjectTypeService graduateProjectTypeService = new GraduateProjectTypeService();
	private GraduateProjectTypeDao graduateProjectTypeDao = GraduateProjectTypeDao.getInstance();
	
	private GraduateProjectTypeService(){}
	
	public static GraduateProjectTypeService getInstance(){
		return graduateProjectTypeService;
	}

	public Collection<GraduateProjectType> findAll() throws SQLException {
		return graduateProjectTypeDao.findAll();
	}

	public GraduateProjectType find(Integer id) throws SQLException {
		return graduateProjectTypeDao.find(id);
	}
	
	public boolean update(GraduateProjectType graduateProjectType){
		return graduateProjectTypeDao.update(graduateProjectType);
	}
	
	public boolean add(GraduateProjectType graduateProjectType){
		return graduateProjectTypeDao.add(graduateProjectType);
	}

	public boolean delete(Integer id) throws SQLException {
		GraduateProjectType graduateProjectType = this.find(id);
		return this.delete(graduateProjectType);
	}
	
	public boolean delete(GraduateProjectType graduateProjectType){
		return graduateProjectTypeDao.delete(graduateProjectType);
	}
}
