/*
 * $Id: StudyPathBusinessBean.java,v 1.4 2003/09/29 13:40:34 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.school.business;

import java.util.Collection;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import com.idega.block.school.data.SchoolStudyPathHome;
import com.idega.block.school.data.SchoolStudyPath;

/** 
 * Business logic for age values and regulations for children in childcare.
 * <p>
 * Last modified: $Date: 2003/09/29 13:40:34 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.4 $
 */
public class StudyPathBusinessBean extends com.idega.business.IBOServiceBean implements StudyPathBusiness  {

	private final static int MAX_STUDY_PATH_CODE_LENGTH = 10;
	
	private final static String KP = "study_path_error."; // key prefix 

	public final static String KEY_STUDY_PATH_CODE_MISSING = KP + "study_path_code_missing";
	public final static String KEY_STUDY_PATH_CODE_TOO_LONG = KP + "study_path_code_too_long";
	public final static String KEY_DESCRIPTION_MISSING = KP + "description_missing";
	public final static String KEY_STUDY_PATH_CODE_ALREADY_EXISTS = KP + "study_path_code_already_exists";
	public final static String KEY_CANNOT_SAVE_STUDY_PATH = KP + "cannot_save_study_path";
	public final static String KEY_CANNOT_DELETE_STUDY_PATH = KP + "cannot_delete_study_path";
	public final static String KEY_CANNOT_FIND_STUDY_PATH = KP + "cannot_find_study_path";

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
	protected SchoolStudyPathHome getSchoolStudyPathHome() throws RemoteException {
		return (SchoolStudyPathHome) com.idega.data.IDOLookup.getHome(SchoolStudyPath.class);
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
		} catch (RemoteException e) {
			return null;
		} catch (FinderException e) {
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
			String studyPathCode,
			String description) throws StudyPathException {

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
				sp = home.findByCode(studyPathCode);
			} catch (FinderException e) {}
			if (sp != null) {
				if (!sp.getPrimaryKey().equals(new Integer(studyPathId))) {
					throw new StudyPathException(KEY_STUDY_PATH_CODE_ALREADY_EXISTS, DEFAULT_STUDY_PATH_CODE_ALREADY_EXISTS);
				}
			}
			try {
				sp = home.findByPrimaryKey(new Integer(studyPathId));
			} catch (FinderException e) {
				sp = home.create();
			}
			sp.setCode(studyPathCode);
			sp.setDescription(description);
			sp.store();
		} catch (RemoteException e) { 
			throw new StudyPathException(KEY_CANNOT_SAVE_STUDY_PATH, DEFAULT_CANNOT_SAVE_STUDY_PATH);
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
			SchoolStudyPathHome home = getSchoolStudyPathHome();
			SchoolStudyPath sp = home.findByPrimaryKey(new Integer(id));
			sp.remove();
		} catch (RemoteException e) { 
			throw new StudyPathException(KEY_CANNOT_DELETE_STUDY_PATH, DEFAULT_CANNOT_DELETE_STUDY_PATH);
		} catch (FinderException e) { 
			throw new StudyPathException(KEY_CANNOT_DELETE_STUDY_PATH, DEFAULT_CANNOT_DELETE_STUDY_PATH);
		} catch (RemoveException e) { 
			throw new StudyPathException(KEY_CANNOT_DELETE_STUDY_PATH, DEFAULT_CANNOT_DELETE_STUDY_PATH);
		}
	}
		
	/**
	 * Returns the study path with the specified code.
	 * @param code the study path code
	 * @throws StudyPathException if study path not found
	 */
	public SchoolStudyPath getStudyPath(String code) throws StudyPathException {
		SchoolStudyPath sp = null;
		try {
			SchoolStudyPathHome home = getSchoolStudyPathHome();
			sp = home.findByPrimaryKey(code);
		} catch (RemoteException e) { 
			throw new StudyPathException(KEY_CANNOT_FIND_STUDY_PATH, DEFAULT_CANNOT_FIND_STUDY_PATH);
		} catch (FinderException e) { 
			throw new StudyPathException(KEY_CANNOT_FIND_STUDY_PATH, DEFAULT_CANNOT_FIND_STUDY_PATH);
		}
		
		return sp;		
	}
}
