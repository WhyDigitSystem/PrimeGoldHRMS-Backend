package com.efit.hrms.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.efit.hrms.entity.TaskVO;

@Repository
public interface TaskRepo extends JpaRepository<TaskVO, Long>{

	
	@Query(nativeQuery = true,value = "select taskid,  assignedby, assignedto, category, department, duedate,  priority,  status, taskdescription, tasktitle,   remarks from task a\r\n"
			+ "where a.orgid=?2 and assignedto=?1 and status = 'New' ")
	Set<Object[]> findNewTaskByOrgIdAndAssignedto(String assignedto, Long orgid);

	@Query(nativeQuery = true,value = "select taskid,  assignedby, assignedto, category, department, duedate, priority,  status, taskdescription, tasktitle,   remarks from task a\r\n"
			+ "where a.orgid=?2 and assignedto=?1 and status = 'In Progress' ")
	Set<Object[]> findPendingTaskByOrgIdAndAssignedto(String assignedto, Long orgid);
	
	@Query(nativeQuery = true,value = "select count(*) as count from task a\r\n"
			+ "where a.orgid=?2 and assignedto=?1 and status = 'In Progress' ")
	Set<Object[]> findCountofPendingTaskByOrgIdAndAssignedto(String assignedto, Long orgid);
	
	@Query(nativeQuery = true,value = "select count(*) as count from task a\r\n"
			+ "where a.orgid=?2 and assignedto=?1 and status = 'New' ")
	Set<Object[]> findCountNewTaskByOrgIdAndAssignedto(String assignedto, Long orgid);

	
	@Query(nativeQuery = true,value = "select taskid,  assignedby, assignedto, category, department, duedate,  priority,  status, taskdescription, tasktitle,   remarks from task a\r\n"
			+ "where a.orgid=?2 and assignedby=?1 and status = 'New' ")
	Set<Object[]> findNewTaskByOrgIdAndAssignedby(String assignedby, Long orgid);

	@Query(nativeQuery = true,value = "select taskid,  assignedby, assignedto, category, department, duedate, priority,  status, taskdescription, tasktitle,   remarks from task a\r\n"
			+ "where a.orgid=?2 and assignedby=?1 and status = 'In Progress' ")
	Set<Object[]> findPendingTaskByOrgIdAndAssignedby(String assignedby, Long orgid);

	@Query(nativeQuery = true,value = "select count(*) as count from task a\r\n"
			+ "where a.orgid=?2 and assignedby=?1 and status = 'In Progress' ")
	Set<Object[]> findCountofPendingTaskByOrgIdAndAssignedby(String assignedby, Long orgid);

	@Query(nativeQuery = true,value = "select count(*) as count from task a\r\n"
			+ "where a.orgid=?2 and assignedby=?1 and status = 'New' ")
	Set<Object[]> findCountofNewTaskByOrgIdAndAssignedby( Long orgid,String assignedby);
	
	
}
