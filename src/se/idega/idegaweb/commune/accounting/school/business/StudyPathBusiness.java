/*
 * Created on 2005-apr-22
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.accounting.school.business;

import java.util.Collection;


import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathGroup;
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
