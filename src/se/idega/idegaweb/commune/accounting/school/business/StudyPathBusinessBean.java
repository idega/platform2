/*
 * $Id: StudyPathBusinessBean.java,v 1.8 2005/05/11 07:15:37 laddi Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.school.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathGroup;
import com.idega.block.school.data.SchoolStudyPathGroupHome;
import com.idega.block.school.data.SchoolStudyPathHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;

/** 
 * Business logic for age values and regulations for children in childcare.
 * <p>
 * Last modified: $Date: 2005/05/11 07:15:37 $ by $Author: laddi $
 *
 * @author Anders Lindman
 * @version $Revision: 1.8 $
 */
public class StudyPathBusinessBean extends com.idega.business.IBOServiceBean implements StudyPathBusiness  {

	private final static int MAX_STUDY_PATH_CODE_LENGTH = 10;
	
	private final static String KP = "study_path_error."; // key prefix 

	public final static String KEY_OPERATION_MISSING = KP + "operation_missing";
	public final static String KEY_STUDY_PATH_CODE_MISSING = KP + "study_path_code_missing";
	public final static String KEY_STUDY_PATH_CODE_TOO_LONG = KP + "study_path_code_too_long";
	public final static String KEY_DESCRIPTION_MISSING = KP + "description_missing";
	public final static String KEY_STUDY_PATH_CODE_ALREADY_EXISTS = KP + "study_path_code_already_exists";
	public final static String KEY_CANNOT_SAVE_STUDY_PATH = KP + "cannot_save_study_path";
	public final static String KEY_CANNOT_DELETE_STUDY_PATH = KP + "cannot_delete_study_path";
	public final static String KEY_CANNOT_FIND_STUDY_PATH = KP + "cannot_find_study_path";

	public final static String DEFAULT_OPERATION_MISSING = "Verksamhet mŒste v?ljas.";
	public final static String DEFAULT_STUDY_PATH_CODE_MISSING = "Koden fšr studievŠgen mŒste fyllas i.";
	public final static String DEFAULT_STUDY_PATH_CODE_TOO_LONG = "Koden fšr studievŠgen fŒr hšgst innehŒlla " + MAX_STUDY_PATH_CODE_LENGTH + " tecken.";
	public final static String DEFAULT_DESCRIPTION_MISSING = "Beskrivning av studievŠgen mŒste fyllas i.";
	public final static String DEFAULT_STUDY_PATH_CODE_ALREADY_EXISTS = "Det finns redan en studiev?g med denna kod.";
	public final static String DEFAULT_CANNOT_SAVE_STUDY_PATH = "StudievŠgen kunde inte sparas pŒ grund av tekniskt fel.";
	public final static String DEFAULT_CANNOT_DELETE_STUDY_PATH = "StudievŠgen kunde inte tas bort pŒ grund av tekniskt fel.";
	public final static String DEFAULT_CANNOT_FIND_STUDY_PATH = "Kan ej hitta studievŠgen.";

	/**
	 * Return study path home. 
	 */	
	protected SchoolStudyPathHome getSchoolStudyPathHome() {
		try {
			return (SchoolStudyPathHome) com.idega.data.IDOLookup.getHome(SchoolStudyPath.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}	
	
	/**
	 * Return study path home. 
	 */	
	protected SchoolStudyPathGroupHome getSchoolStudyPathGroupHome() {
		try {
			return (SchoolStudyPathGroupHome) com.idega.data.IDOLookup.getHome(SchoolStudyPathGroup.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	/**
	 * Return school type home. 
	 */	
	protected SchoolTypeHome getSchoolTypeHome() {
		try {
			return (SchoolTypeHome) com.idega.data.IDOLookup.getHome(SchoolType.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}	
	
	/**
	 * Return school class member home. 
	 */	
	protected SchoolClassMemberHome getSchoolClassMemberHome() {
		try {
			return (SchoolClassMemberHome) com.idega.data.IDOLookup.getHome(SchoolClassMember.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}	
	
	/**
	 * Finds all study paths.
	 * @return collection of study path objects
	 * @see se.idega.idegaweb.commune.accounting.school.data.StudyPath 
	 */
	public Collection findAllStudyPaths() {
		try {
			SchoolStudyPathHome home = getSchoolStudyPathHome();
			return home.findAllStudyPaths();				
		} catch (FinderException e) {
			return null;
		}
	}	
	
	/**
	 * Finds all study paths with the specified operation (school type).
	 * @param operation the operation (school type id)
	 * @return collection of study path objects
	 * @see se.idega.idegaweb.commune.accounting.school.data.StudyPath 
	 */
	public Collection findStudyPathsByOperation(int operation) {
		try {
			SchoolStudyPathHome home = getSchoolStudyPathHome();
			return home.findBySchoolType(operation);				
		} catch (Exception e) {
			return null;
		}
	}
	
	public Collection findStudyPathsByType(SchoolType type, SchoolStudyPathGroup group) {
		try {
			return getSchoolStudyPathHome().findBySchoolType(type, group);
		}
		catch (FinderException fe) {
			return new ArrayList();
		}
	}
	
	public Collection findStudyPathsBySchool(School school) {
		return findStudyPathsBySchool(school, null);
	}

	public Collection findStudyPathsBySchool(School school, SchoolStudyPathGroup group) {
		try {
			return getSchoolStudyPathHome().findStudyPaths(school, group);
		}
		catch (IDORelationshipException ire) {
			ire.printStackTrace();
			return null;
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Finds all oparations (school types).
	 * @return collection of operations
	 */
	public Collection findAllOperations() {
		try {
			SchoolTypeHome home = getSchoolTypeHome();
			return home.findAllSchoolTypes();				
		} catch (FinderException e) {
			return null;
		}
	}	
	
	/**
	 * Finds all study path groups .
	 * @return collection of study path groups
	 */
	public Collection findAllStudyPathGroups() {
		try {
			SchoolStudyPathGroupHome home = getSchoolStudyPathGroupHome();
			return home.findAllStudyPathGroups();				
		} catch (FinderException e) {
			return null;
		}
	}	
	
	/**
	 * Finds all study path groups .
	 * @return collection of study path groups
	 */
	public SchoolStudyPathGroup findStudyPathGroupByID(int groupId) {
		try {
			SchoolStudyPathGroupHome home = getSchoolStudyPathGroupHome();
			return home.findByPrimaryKey((new Integer (groupId)));			
		} catch (FinderException e) {
			return null;
		}
	}
	
	public SchoolStudyPathGroup findStudyPathGroup(Object groupPK) {
		try {
			return getSchoolStudyPathGroupHome().findByPrimaryKey(groupPK);			
		}
		catch (FinderException e) {
			return null;
		}
	}
	
	/**
	 * Saves a study path object.
	 * Creates a new persistent object if nescessary.
	 * @param code the study path code
	 * @param description the description of the age regulation
	 * @throws StudyPathException if invalid parameters
	 */
	public void saveStudyPath(
			String studyPathId,
			String operation,
			String studyPathCode,
			String description, String points, String studypathgroup) throws StudyPathException {

		// Operation
		Integer operationId = null;
		if (operation.length() == 0) {
			throw new StudyPathException(KEY_OPERATION_MISSING, DEFAULT_OPERATION_MISSING);
		} else {
			operationId = new Integer(operation);
		}
		
		//points
		Integer integerPoints = null;
		int intPoints = -1;
		if (points.equals(""))
			points = null;
		if (points != null)
			integerPoints = (new Integer (points));
		if (integerPoints != null)
			intPoints = integerPoints.intValue();
		//StudyPathGroup
		Integer integerStudyPathGroupId = null;
		int intStudyPathGroupId = -1;
		if (studypathgroup.equals(""))
			studypathgroup = null;
		if (studypathgroup != null)
			integerStudyPathGroupId = (new Integer (studypathgroup));
		if (integerStudyPathGroupId != null)
			intStudyPathGroupId= integerStudyPathGroupId.intValue();
		
		// Study path code
		String s = studyPathCode.trim().toUpperCase();
		if (s.equals("")) {
			throw new StudyPathException(KEY_STUDY_PATH_CODE_MISSING, DEFAULT_STUDY_PATH_CODE_MISSING);
		} else if (s.length() > MAX_STUDY_PATH_CODE_LENGTH) {
				throw new StudyPathException(KEY_STUDY_PATH_CODE_TOO_LONG, DEFAULT_STUDY_PATH_CODE_TOO_LONG);
		} else {
			studyPathCode = s;
		}

		// Description
		s = description.trim();
		if (s.equals("")) {
			throw new StudyPathException(KEY_DESCRIPTION_MISSING, DEFAULT_DESCRIPTION_MISSING);
		} else {
			description = s;
		}
		
		try {
			SchoolStudyPathHome home = getSchoolStudyPathHome();
			SchoolStudyPath sp = null;
			try {
				sp = home.findByCodeAndSchoolType(studyPathCode, operationId.intValue());
			} catch (FinderException e) {}
			if (sp != null) {
				if (!sp.getPrimaryKey().equals(new Integer(studyPathId))) {
					throw new StudyPathException(KEY_STUDY_PATH_CODE_ALREADY_EXISTS, DEFAULT_STUDY_PATH_CODE_ALREADY_EXISTS);
				}
			}
			try {
				Integer id = new Integer(studyPathId);
				if (id.intValue() > 0) { 
					sp = home.findByPrimaryKey(id);
				}
			} catch (Exception e) {}
			if (sp == null) {
				sp = home.create();
			}
			sp.setCode(studyPathCode);
			sp.setSchoolTypeId(operationId);
			sp.setDescription(description);
			sp.setPoints(intPoints);
			sp.setStudyPathGroupID(intStudyPathGroupId);
			sp.store();
		} catch (CreateException e) { 
			throw new StudyPathException(KEY_CANNOT_SAVE_STUDY_PATH, DEFAULT_CANNOT_SAVE_STUDY_PATH);
		}		
		
		
	}
	
	/**
	 * Deletes the study path object with the specified id.
	 * @param code the study path code
	 * @throws StudyPathException if the study path could not be deleted
	 */ 
	public void deleteStudyPath(String id) throws StudyPathException {
		try {
            // 1. set the study path to in valid
			SchoolStudyPathHome home = getSchoolStudyPathHome();
			SchoolStudyPath studyPath = home.findByPrimaryKey(new Integer(id));
			studyPath.remove();

            // 2. remove all associations between students and the invalidated
            // study path
            try {
                final Collection students = getSchoolClassMemberHome ()
                        .findAllBySchoolStudyPath (studyPath);
                if (null != students) {
                    for (Iterator i = students.iterator (); i.hasNext ();) {
                        final SchoolClassMember student
                                = (SchoolClassMember) i.next ();
                        student.setStudyPathToNull ();
                        student.store ();
                    }
                }
            } catch (final FinderException fe) {
                // no problem, no kids with this study path - do nothing
            }
		} catch (FinderException e) { 
			throw new StudyPathException(KEY_CANNOT_DELETE_STUDY_PATH, DEFAULT_CANNOT_DELETE_STUDY_PATH);
		} catch (RemoveException e) { 
			throw new StudyPathException(KEY_CANNOT_DELETE_STUDY_PATH, DEFAULT_CANNOT_DELETE_STUDY_PATH);
		}
	}
		
	/**
	 * Returns the study path with the specified id.
	 * @param code the study path code
	 * @throws StudyPathException if study path not found
	 */
	public SchoolStudyPath getStudyPath(String id) throws StudyPathException {
		SchoolStudyPath sp = null;
		try {
			SchoolStudyPathHome home = getSchoolStudyPathHome();
			sp = home.findByPrimaryKey(new Integer(id));
		} catch (FinderException e) { 
			throw new StudyPathException(KEY_CANNOT_FIND_STUDY_PATH, DEFAULT_CANNOT_FIND_STUDY_PATH);
		}
		
		return sp;		
	}
}
