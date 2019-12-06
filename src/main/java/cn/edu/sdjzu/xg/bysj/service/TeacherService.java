package cn.edu.sdjzu.xg.bysj.service;


import cn.edu.sdjzu.xg.bysj.dao.TeacherDao;
import cn.edu.sdjzu.xg.bysj.domain.Teacher;

import java.sql.SQLException;
import java.util.Collection;

public final class TeacherService {
	private static TeacherDao teacherDao= TeacherDao.getInstance();
	private static TeacherService teacherService=new TeacherService();
	private TeacherService(){}
	
	public static TeacherService getInstance(){
		return teacherService;
	}
	
	public Collection<Teacher> findAll() throws SQLException {
		return teacherDao.findAll();
	}
	
	public Teacher find(Integer id) throws SQLException {
		return teacherDao.find(id);
	}

	public Teacher findByNo(String no) throws SQLException {
		return teacherDao.findByNo(no);
	}

	public boolean update(Teacher teacher) throws SQLException {
		return teacherDao.update(teacher);
	}
	
	public boolean add(Teacher teacher){
		return teacherDao.add(teacher);
	}

	public boolean delete(Integer id) throws SQLException {
		Teacher teacher = this.find(id);
		return teacherDao.delete(teacher);
	}
	
	public boolean delete(Teacher teacher) throws SQLException {
		//如果该教师有关联的课题，不能删除
//		if(teacher.getProjects().size()>0){
//			return false;  // 暂时有这个需要
//		}
		return teacherDao.delete(teacher.getId());
	}	
}
