/*
 * $Id: ChildCareExportBusinessBean.java,v 1.1 2005/01/12 13:11:54 anders Exp $
 *
 * Copyright (C) 2005 Idega. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf & Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.childcare.export.business;

import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Iterator;

import javax.ejb.FinderException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import se.idega.idegaweb.commune.care.data.ChildCareContract;
import se.idega.idegaweb.commune.care.data.ChildCareContractHome;
import se.idega.idegaweb.commune.childcare.export.data.ChildCareExportTime;
import se.idega.idegaweb.commune.childcare.export.data.ChildCareExportTimeHome;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolClassMemberLog;
import com.idega.block.school.data.SchoolClassMemberLogHome;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOServiceBean;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileBMPBean;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookupException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * Service business bean for child care file export.
 * The first version of this class implements the business logic for
 * exporting text files for the IST Extens system.
 * <p>
 * Last modified: $Date: 2005/01/12 13:11:54 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.1 $
 */
public class ChildCareExportBusinessBean extends IBOServiceBean implements ChildCareExportBusiness {

	private final static String EXPORT_FOLDER_NAME = "Child Care Export Files";
	private final static String SEPARATOR_CHAR = ";";
	private final static String EMPTY_FIELD = "NULL";

	private final static int FILE_TYPE_PLACEMENT = 1;
	private final static int FILE_TYPE_TAXEKAT = 2; // IST Extens 'taxekategori' (child care time)
	
	private final static String FILE_NAME_PREFIX_PLACEMENT = "p_";
	private final static String FILE_NAME_PREFIX_TAXEKAT = "t_";

	private final static String KP = "cc_export."; // localization key prefix 

	public final static String KEY_EXPORT_FILE_TIMESTAMP_STORE_ERROR = KP + "file_timestamp_store_error";
	public final static String KEY_CREATE_FILE_ERROR = KP + "create_file_error";
	public final static String KEY_PLACEMENT_SEARCH_ERROR = KP + "placement_search_error";

	public final static String DEFAULT_EXPORT_FILE_TIMESTAMP_STORE_ERROR = "Could not store export file timestamp.";
	public final static String DEFAULT_CREATE_FILE_ERROR = KP + "Could not create export file.";
	public final static String DEFAULT_PLACEMENT_SEARCH_ERROR = KP + "Error while searching for placements.";

	/**
	 * Creates an export file with child care placements for the IST Extens system.
	 * @param userId the id for the user who is creating the export file 
	 * @return the name of the export file
	 * @throws ChildCareExportException
	 */
	public String exportPlacementFile(int userId) throws ChildCareExportException {
		String fileName = null;
		UserTransaction transaction = this.getSessionContext().getUserTransaction();		
		try {
			transaction.begin();

			fileName = storePlacementExportFileTimestamp(userId);
			String text = getPlacementFileContent();
			writeFile(text, fileName);
			
			transaction.commit();
		} catch (Exception e) {
			try {
				transaction.rollback();
			} catch (SystemException e2) {
				log(e2);
			}
			log(e);
			if (e instanceof ChildCareExportException) {
				throw (ChildCareExportException) e;
			}
			throw new ChildCareExportException(KEY_CREATE_FILE_ERROR, DEFAULT_CREATE_FILE_ERROR);
		}
		return fileName;
	}

	/**
	 * Creates an export file with child care placement with changes in taxekategori (child care time)
	 * for the IST Extens system.
	 * @param userId the id for the user who is creating the export file 
	 * @return the name of the export file
	 * @throws ChildCareExportException
	 */
	public String exportTaxekatFile(int userId) throws ChildCareExportException {
		String fileName = null;
		UserTransaction transaction = this.getSessionContext().getUserTransaction();		
		try {
			transaction.begin();
			
			Date lastExportDate = getLastExportDate(FILE_TYPE_TAXEKAT);
			fileName = storeTaxekatExportFileTimestamp(userId);
			String text = getTaxekatFileContent(lastExportDate);
			writeFile(text, fileName);
			
			transaction.commit();
		} catch (Exception e) {
			try {
				transaction.rollback();
			} catch (SystemException e2) {
				log(e2);
			}
			log(e);
			if (e instanceof ChildCareExportException) {
				throw (ChildCareExportException) e;
			}
			throw new ChildCareExportException(KEY_CREATE_FILE_ERROR, DEFAULT_CREATE_FILE_ERROR);
		}
		return fileName;
	}

	/*
	 * Returns text with child care placement records for the IST Extens system.
	 */
	private String getPlacementFileContent() throws ChildCareExportException {
		String s = "";
		try {
			Iterator placements = getSchoolClassMemberHome().findAllByCategory(getSchoolBusiness().getCategoryChildcare()).iterator();
			while (placements.hasNext()) {
				SchoolClassMember placement = (SchoolClassMember) placements.next();
				User user = placement.getStudent();
				Date placementStartDate = null;
				int placementSchoolClassId = -1; 
				Iterator placementLogs = getSchoolClassMemberLogHome().findAllBySchoolClassMember(placement).iterator();
				while (placementLogs.hasNext()) {
					SchoolClassMemberLog log = (SchoolClassMemberLog) placementLogs.next();
					if (placementStartDate != null) {
						if (getISTDate(placementStartDate).equals(getISTDate(log.getStartDate())) && 
							(placementSchoolClassId == log.getSchoolClassID())) {
								continue; // Remove duplicates
						}
					}
					placementStartDate = log.getStartDate();
					placementSchoolClassId = log.getSchoolClassID();
					SchoolClass group = log.getSchoolClass();
					School school = group.getSchool();
					SchoolType schoolType = group.getSchoolType(); 
					
					s += getISTPersonalId(user) + SEPARATOR_CHAR;
					s += getISTSchoolId(school) + SEPARATOR_CHAR;
					s += getISTGroupId(group) + SEPARATOR_CHAR;
					s += getISTSchoolTypeId(schoolType) + SEPARATOR_CHAR;
					s += getISTDate(log.getStartDate()) + SEPARATOR_CHAR;
					s += getISTDate(log.getEndDate());
					
					if (placements.hasNext() || placementLogs.hasNext()) {
						s += "\r\n";						
					}
				}
			}
			
		} catch (Exception e) {
			log(e);
			throw new ChildCareExportException(KEY_PLACEMENT_SEARCH_ERROR, DEFAULT_PLACEMENT_SEARCH_ERROR);
		}
		return s;
	}

	/*
	 * Returns text with child care placement records with 'taxekategori' changes for the IST Extens system.
	 */
	private String getTaxekatFileContent(Date lastExportDate) throws ChildCareExportException {
		String s = "";
		try {
			Iterator contracts = getChildCareContractHome().findChangedSinceDate(lastExportDate).iterator();
			while (contracts.hasNext()) {
				ChildCareContract contract = (ChildCareContract) contracts.next();
				User user = contract.getChild();
				SchoolClassMember placement = contract.getSchoolClassMember();
				if (placement == null) {
					continue;
				}
				SchoolClass group = placement.getSchoolClass();
				School school = group.getSchool();
				SchoolType schoolType = group.getSchoolType(); 
					
				s += getISTPersonalId(user) + SEPARATOR_CHAR;
				s += getISTSchoolId(school) + SEPARATOR_CHAR;
				s += getISTGroupId(group) + SEPARATOR_CHAR;
				s += getISTSchoolTypeId(schoolType) + SEPARATOR_CHAR;
				s += getISTDate(placement.getRegisterDate()) + SEPARATOR_CHAR;
				s += getISTTaxekat(contract.getCareTime()) + SEPARATOR_CHAR;
				s += getISTDate(contract.getValidFromDate()) + SEPARATOR_CHAR;
				s += getISTDate(contract.getTerminatedDate());
					
				if (contracts.hasNext()) {
					s += "\r\n";						
				}				
			}
			
		} catch (Exception e) {
			log(e);
			throw new ChildCareExportException(KEY_PLACEMENT_SEARCH_ERROR, DEFAULT_PLACEMENT_SEARCH_ERROR);			
		}
		return s;
	}

	/*
	 * Returns the date for when the last export with the specified file type was created.
	 */
	private Date getLastExportDate(int fileType) {
		Date date = new Date(0L);
		try {
			ChildCareExportTime et = getChildCareExportTimeHome().findLatest(fileType);
			IWTimestamp t = new IWTimestamp(et.getCreated());
			t.setAsDate();
			date = t.getDate();
		} catch (Exception e) {}
		return date;
	}

	/*
	 * Returns a personal id formatted for the IST Extens system.
	 */
	private String getISTPersonalId(User user) {
		String personalId = EMPTY_FIELD;
		try {
			String pid = user.getPersonalID().trim();
			if (pid.length() == 12) {
				personalId = pid;
			}
		} catch (Exception e) {}
		return personalId;
	}

	/*
	 * Returns a school id formatted for the IST Extens system.
	 */
	private String getISTSchoolId(School school) {
		String schoolId = EMPTY_FIELD;
		try {
			schoolId = school.getProviderStringId().trim();
			if (schoolId.length() > 10) {
				schoolId = schoolId.substring(0, 10);
			}
			if (schoolId.equals("null")) {
				schoolId = EMPTY_FIELD;
			}
		} catch (Exception e) {}
		return schoolId;
	}

	/*
	 * Returns a group (school class) id formatted for the IST Extens system.
	 */
	private String getISTGroupId(SchoolClass group) { 
		String groupId = EMPTY_FIELD;
		try {
			groupId = group.getGroupStringId().trim();
			if (groupId.length() > 10) {
				groupId = groupId.substring(0, 10);
			}
			if (groupId.equals("null")) {
				groupId = EMPTY_FIELD;
			}
		} catch (Exception e) {}
		return groupId;
	}

	/*
	 * Returns a school type id formatted for the IST Extens system.
	 */
	private String getISTSchoolTypeId(SchoolType schoolType) {
		String schoolTypeId = EMPTY_FIELD;
		try {
			schoolTypeId = schoolType.getTypeStringId().trim();
			if (schoolTypeId.length() > 10) {
				schoolTypeId = schoolTypeId.substring(0, 10);
			}
			if (schoolTypeId.equals("null")) {
				schoolTypeId = EMPTY_FIELD;
			}
		} catch (Exception e) {}
		return schoolTypeId;
	}

	/*
	 * Returns a date formatted for the IST Extens system.
	 */
	private String getISTDate(Date date) {
		String dateString = EMPTY_FIELD;
		try {
			dateString = date.toString();
		} catch (Exception e) {}
		return dateString;
	}

	/*
	 * Returns a date formatted for the IST Extens system.
	 */
	private String getISTDate(Timestamp t) {
		String dateString = EMPTY_FIELD;
		try {
			IWTimestamp ts = new IWTimestamp(t);
			ts.setAsDate();
			return getISTDate(ts.getDate());
		} catch (Exception e) {}
		return dateString;
	}


	/*
	 * Returns a taxekategori (child care time) formatted for the IST Extens system.
	 */
	private String getISTTaxekat(String s) {
		String taxekat = EMPTY_FIELD;
		try {
			s = s.trim();
			if (s.length() > 10) {
				taxekat = s.substring(0, 10);
			} else {
				taxekat = s;
			}
			if (taxekat.equals("null")) {
				taxekat = EMPTY_FIELD;
			}
		} catch (Exception e) {}
		return taxekat;
	}

	/*
	 * Writes the specified text to a file.
	 */
	private void writeFile(String text, String fileName) throws ChildCareExportException {
		ICFile exportFolder = null;
		ICFileHome fileHome = null;

		try {
			fileHome = (ICFileHome) com.idega.data.IDOLookup.getHome(ICFile.class);
			exportFolder = fileHome.findByFileName(EXPORT_FOLDER_NAME);
		} catch (FinderException e) {
			try {
				ICFile root = fileHome.findByFileName(ICFileBMPBean.IC_ROOT_FOLDER_NAME);
				exportFolder = fileHome.create();
				exportFolder.setName(EXPORT_FOLDER_NAME);
				exportFolder.setMimeType("application/vnd.iw-folder");
				exportFolder.store();
				root.addChild(exportFolder);
			} catch (Exception e2) {
				log(e2);
				throw new ChildCareExportException(KEY_CREATE_FILE_ERROR, DEFAULT_CREATE_FILE_ERROR);
			}
		} catch (IDOLookupException e) {
			log(e);
			throw new ChildCareExportException(KEY_CREATE_FILE_ERROR, DEFAULT_CREATE_FILE_ERROR);
		}

		ICFile exportFile = null;
		
		try {
			try {
				exportFile = fileHome.findByFileName(fileName);
				if (exportFile != null) {
					exportFile.remove();
				}
			} catch (FinderException e) {}
			exportFile = fileHome.create();
			byte[] bytes = text.getBytes();

			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
			exportFile.setFileValue(bais);
			exportFile.setMimeType("text/plain");
			exportFile.setName(fileName);
			exportFile.setFileSize(text.length());
			exportFile.store();
			
			exportFolder.addChild(exportFile);
		} catch (Exception e) {
			log(e);
			throw new ChildCareExportException(KEY_CREATE_FILE_ERROR, DEFAULT_CREATE_FILE_ERROR);
		}
	}
	
	/**
	 * Stores a new entry in the export file timestamp log (ChildCareExportTime).
	 * The created timestamp is set to current time.
	 * The file type is set to IST Extens placement export file.
	 * @param userId the id for the user who created the export file.
	 * @return the file name for the export file
	 * @throws ChildCareExportException
	 */
	public String storePlacementExportFileTimestamp(int userId) throws ChildCareExportException {
		return storeExportFileTimestamp(userId, FILE_TYPE_PLACEMENT);		
	}

	/**
	 * Stores a new entry in the export file timestamp log (ChildCareExportTime).
	 * The created timestamp is set to current time.
	 * The file type is set to IST Extens taxekat export file.
	 * @param userId the id for the user who created the export file.
	 * @return the file name for the export file
	 * @throws ChildCareExportException
	 */
	public String storeTaxekatExportFileTimestamp(int userId) throws ChildCareExportException {
		return storeExportFileTimestamp(userId, FILE_TYPE_TAXEKAT);
	}
	
	/*
	 * Store a new entry in the export file timestamp log.
	 */
	private String storeExportFileTimestamp(int userId, int fileType) throws ChildCareExportException {		
		IWTimestamp now = IWTimestamp.RightNow();
		String fileName = getExportFileName(fileType, now);

		try {
			ChildCareExportTime entry = getChildCareExportTimeHome().create();
			entry.setCreated(now.getTimestamp());
			entry.setUserId(userId);
			entry.setFileName(fileName);
			entry.setFileType(fileType);
			entry.store();
		} catch (Exception e) {
			log(e);
			throw new ChildCareExportException(KEY_EXPORT_FILE_TIMESTAMP_STORE_ERROR,
					KEY_EXPORT_FILE_TIMESTAMP_STORE_ERROR);			
		}
		
		return fileName;
	}

	/*
	 * Returns a formatted export file name.
	 */
	private String getExportFileName(int fileType, IWTimestamp timestamp) {
		String fileName = "";
		
		switch (fileType) {
			case FILE_TYPE_PLACEMENT:
				fileName = FILE_NAME_PREFIX_PLACEMENT;
				break;
			case FILE_TYPE_TAXEKAT:
				fileName = FILE_NAME_PREFIX_TAXEKAT;
				break;
		}
		
		fileName += timestamp.getDateString("yyMMDD_HH_mm");
		fileName += ".txt";
		
		return fileName;
	}
	
	/**
	 * Returns all export files. 
	 */
	public Iterator getAllExportFiles() {
		Iterator exportFiles = null;
		try {
			ICFileHome fileHome = (ICFileHome) com.idega.data.IDOLookup.getHome(ICFile.class);
			ICFile exportFolder = fileHome.findByFileName(EXPORT_FOLDER_NAME);
			exportFiles = exportFolder.getChildrenIterator();
		} catch (Exception e) {
			log(e);
		}
		return exportFiles;
	}

	/**
	 * Returns the file name prefix for placement export files. 
	 */
	public String getPlacementExportFileNamePrefix() {
		return FILE_NAME_PREFIX_PLACEMENT;
	}

	/**
	 * Returns the file name prefix for taxekat export files. 
	 */
	public String getTaxekatExportFileNamePrefix() {
		return FILE_NAME_PREFIX_TAXEKAT;
	}
	
	/*
	 * Returns a SchoolBusiness instance.
	 */
	private SchoolBusiness getSchoolBusiness() throws RemoteException {
		return (SchoolBusiness) this.getServiceInstance(SchoolBusiness.class);
	}

	/*
	 * Returns the entity home for SchoolClassMember.
	 */
	private SchoolClassMemberHome getSchoolClassMemberHome() throws RemoteException {
		return (SchoolClassMemberHome) getIDOHome(SchoolClassMember.class);
	}

	/*
	 * Returns the entity home for SchoolClassMemberLog.
	 */
	private SchoolClassMemberLogHome getSchoolClassMemberLogHome() throws RemoteException {
		return (SchoolClassMemberLogHome) getIDOHome(SchoolClassMemberLog.class);
	}

	/*
	 * Returns the entity home for ChildCareContract.
	 */
	private ChildCareContractHome getChildCareContractHome() throws RemoteException {
		return (ChildCareContractHome) getIDOHome(ChildCareContract.class);
	}

	/*
	 * Returns the entity home for ChildCareExportTime.
	 */
	private ChildCareExportTimeHome getChildCareExportTimeHome() throws RemoteException {
		return (ChildCareExportTimeHome) getIDOHome(ChildCareExportTime.class);
	}
}
