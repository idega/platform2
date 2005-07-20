/*
 * Created on 2005-jun-01
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.accounting.school.business;

import java.util.Collection;


import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathGroup;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOService;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface StudyPathBusiness extends IBOService {
	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findAllStudyPaths
	 */
	public Collection findAllStudyPaths() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPathsByOperation
	 */
	public Collection findStudyPathsByOperation(int operation)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPathsByOperations
	 */
	public Collection findStudyPathsByOperations(Collection operations)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPathsByType
	 */
	public Collection findStudyPathsByType(SchoolType type,
			SchoolStudyPathGroup group) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPathsBySchool
	 */
	public Collection findStudyPathsBySchool(School school)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPathsBySchool
	 */
	public Collection findStudyPathsBySchool(School school,
			SchoolStudyPathGroup group) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findAllOperations
	 */
	public Collection findAllOperations() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findAllStudyPathGroups
	 */
	public Collection findAllStudyPathGroups() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPathGroupByID
	 */
	public SchoolStudyPathGroup findStudyPathGroupByID(int groupId)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPathGroup
	 */
	public SchoolStudyPathGroup findStudyPathGroup(Object groupPK)
			throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#saveStudyPath
	 */
	public void saveStudyPath(String studyPathId, String operation,
			String studyPathCode, String description, String points,
			String studypathgroup) throws StudyPathException,
			java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#deleteStudyPath
	 */
	public void deleteStudyPath(String id) throws StudyPathException,
			java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#getStudyPath
	 */
	public SchoolStudyPath getStudyPath(String id) throws StudyPathException,
			java.rmi.RemoteException;

}
