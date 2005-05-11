/*
 * $Id: StudyPathBusiness.java,v 1.6 2005/05/11 07:15:37 laddi Exp $
 * Created on 28.4.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.accounting.school.business;

import java.util.Collection;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathGroup;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOService;


/**
 * Last modified: $Date: 2005/05/11 07:15:37 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.6 $
 */
public interface StudyPathBusiness extends IBOService {

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findAllStudyPaths
	 */
	public Collection findAllStudyPaths() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPathsByOperation
	 */
	public Collection findStudyPathsByOperation(int operation) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPathsByType
	 */
	public Collection findStudyPathsByType(SchoolType type, SchoolStudyPathGroup group) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPathsBySchool
	 */
	public Collection findStudyPathsBySchool(School school) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPathsBySchool
	 */
	public Collection findStudyPathsBySchool(School school, SchoolStudyPathGroup group) throws java.rmi.RemoteException;

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
	public SchoolStudyPathGroup findStudyPathGroupByID(int groupId) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#findStudyPathGroup
	 */
	public SchoolStudyPathGroup findStudyPathGroup(Object groupPK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#saveStudyPath
	 */
	public void saveStudyPath(String studyPathId, String operation, String studyPathCode, String description,
			String points, String studypathgroup) throws StudyPathException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#deleteStudyPath
	 */
	public void deleteStudyPath(String id) throws StudyPathException, java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.accounting.school.business.StudyPathBusinessBean#getStudyPath
	 */
	public SchoolStudyPath getStudyPath(String id) throws StudyPathException, java.rmi.RemoteException;
}
