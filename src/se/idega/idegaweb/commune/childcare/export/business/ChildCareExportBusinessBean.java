/*
 * $Id: ChildCareExportBusinessBean.java,v 1.10 2005/02/15 16:03:26 anders Exp $
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
import java.util.Collection;
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
 * Last modified: $Date: 2005/02/15 16:03:26 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.10 $
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
	 * @param to the latest date for placements included in the export 
	 * @return the name of the export file
	 * @throws ChildCareExportException
	 */
	public String exportPlacementFile(int userId, Date to) throws ChildCareExportException {
		String fileName = null;
		UserTransaction transaction = this.getSessionContext().getUserTransaction();		
		try {
			transaction.begin();

			fileName = storePlacementExportFileTimestamp(userId, to);
			String text = getPlacementFileContent(to);
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
	 * @param from the first date from which to include changes 
	 * @param to the last date to which to include changes 
	 * @return the name of the export file
	 * @throws ChildCareExportException
	 */
	public String exportTaxekatFile(int userId, Date from, Date to) throws ChildCareExportException {
		String fileName = null;
		UserTransaction transaction = this.getSessionContext().getUserTransaction();		
		try {
			transaction.begin();
			
			fileName = storeTaxekatExportFileTimestamp(userId, from, to);
			String text = getTaxekatFileContent(from, to);
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
	private String getPlacementFileContent(Date to) throws ChildCareExportException {
		String s = "";
		try {
			Iterator placements = getSchoolClassMemberHome().findAllByCategory(getSchoolBusiness().getCategoryChildcare()).iterator();
			while (placements.hasNext()) {
				SchoolClassMember placement = (SchoolClassMember) placements.next();

				Date placementDate = Date.valueOf(new Date(placement.getRegisterDate().getTime()).toString());
				if (placementDate.compareTo(to) > 0) {
					continue;
				}
				User user = placement.getStudent();
				Date placementStartDate = null;
				int placementSchoolClassId = -1; 
				Iterator placementLogs = getSchoolClassMemberLogHome().findAllBySchoolClassMember(placement).iterator();
				while (placementLogs.hasNext()) {
					SchoolClassMemberLog log = (SchoolClassMemberLog) placementLogs.next();
					if (log.getStartDate() != null && log.getStartDate().compareTo(to) > 0) {
						continue;
					}
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
	private String getTaxekatFileContent(Date from, Date to) throws ChildCareExportException {
		String s = "";
		try {
			Iterator contracts = getChildCareContractHome().findChangedBetween(from, to).iterator();
			while (contracts.hasNext()) {
				ChildCareContract contract = (ChildCareContract) contracts.next();
				User user = contract.getChild();
				SchoolClassMember placement = contract.getSchoolClassMember();
				if (placement == null) {
					continue;
				}
				SchoolClass group = placement.getSchoolClass();
				Date placementFromDate = new Date(placement.getRegisterDate().getTime());
				Date contractFromDate = contract.getValidFromDate();
				SchoolClassMemberLog log = null;
				SchoolClassMemberLogHome home = getSchoolBusiness().getSchoolClassMemberLogHome();
				if (contractFromDate != null) {
					try {
						log = home.findByPlacementAndDate(placement, contractFromDate);
					} catch (FinderException e) {}
					if (log != null) {
						placementFromDate = log.getStartDate();
						group = log.getSchoolClass();
					}
				} else {
					continue;
				}
				School school = group.getSchool();
				SchoolType schoolType = group.getSchoolType(); 

				if (isContractInInterval(contract, from, to)) {
					Date terminatedDate = contract.getTerminatedDate();
					if (log != null) {
						terminatedDate = log.getEndDate();
					}
					s += getTaxekatLine(user, school, group, schoolType, placementFromDate, contract.getCareTime(), contract.getValidFromDate(), terminatedDate);
					s += "\r\n";						
				} else {
					log = null;
				}
				
				Collection logs = null;
				try {
					logs = home.findAllBySchoolClassMember(placement);
				} catch (FinderException e) {}
				
				if (logs != null) {
					Iterator iter = logs.iterator();
					while (iter.hasNext()) {
						SchoolClassMemberLog l = (SchoolClassMemberLog) iter.next();
						if (isLogsEqual(l, log)) {
							continue;
						}
						if (isLogInInterval(l, from, to) && isLogInContract(l, contract)) {
							s += getTaxekatLine(user, school, l.getSchoolClass(), schoolType, l.getStartDate(), contract.getCareTime(), l.getStartDate(), l.getEndDate());
							s += "\r\n";						
						}
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
	 * Returns true if the specified contract has dates within specified date interval.
	 */
	private boolean isContractInInterval(ChildCareContract c, Date from, Date to) {
		Date cFrom = c.getValidFromDate();
		Date cTo = c.getTerminatedDate();
		
		if (cFrom.compareTo(from) >= 0 && cFrom.compareTo(to) <= 0) {
			return true;
		}
		
		if (cTo != null) {
			if (cTo.compareTo(from) >= 0 && cTo.compareTo(to) <= 0) {
				return true;
			}
		}
		
		return false;
	}

	/*
	 * Returns true if the specified log has dates within specified date interval.
	 */
	private boolean isLogInInterval(SchoolClassMemberLog l, Date from, Date to) {
		Date lFrom = l.getStartDate();
		Date lTo = l.getEndDate();
		
		if (lFrom.compareTo(from) >= 0 && lFrom.compareTo(to) <= 0) {
			return true;
		}
		
		if (lTo != null) {
			if (lTo.compareTo(from) >= 0 && lTo.compareTo(to) <= 0) {
				return true;
			}
		}
		
		return false;
	}
	
	/*
	 * Returns true if the specified logs are equal (only dates used in comparison).
	 */
	private boolean isLogsEqual(SchoolClassMemberLog l1, SchoolClassMemberLog l2) {
		if (l1 == null || l2 == null) {
			return false;
		}
		if (l1.getStartDate().compareTo(l2.getStartDate()) != 0) {
			return false;
		}
		if (l1.getEndDate() == null && l2.getEndDate() == null) {
			return true;
		}
		if (l1.getEndDate() != null && l2.getEndDate() != null) {
			if (l1.getEndDate().compareTo(l2.getEndDate()) == 0) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Returns true if the specified log is within dates of contract.
	 */
	private boolean isLogInContract(SchoolClassMemberLog l, ChildCareContract c) {
		Date lStart = l.getStartDate();
		Date lEnd = l.getEndDate();
		Date cStart = c.getValidFromDate();
		Date cEnd = c.getTerminatedDate();
		
		if (lStart.compareTo(cStart) >= 0) {
			if (cEnd != null) {
				if (lStart.compareTo(cEnd) <= 0) {
					return true;
				}
			} else {
				return true;
			}
		}

		if (lEnd != null && lEnd.compareTo(cStart) >= 0) {
			if (cEnd != null) {
				if (lEnd.compareTo(cEnd) <= 0) {
					return true;
				}
			} else {
				return true;
			}
		}
		
		return false;
	}
	
	/*
	 * Returns a taxekat export file line.
	 */
	private String getTaxekatLine(User user, School school, SchoolClass group, SchoolType schoolType, Date placementFromDate, String careTime, Date contractFrom, Date contractTo) {
		String s = "";
		s += getISTPersonalId(user) + SEPARATOR_CHAR;
		s += getISTSchoolId(school) + SEPARATOR_CHAR;
		s += getISTGroupId(group) + SEPARATOR_CHAR;
		s += getISTSchoolTypeId(schoolType) + SEPARATOR_CHAR;
		s += getISTDate(placementFromDate) + SEPARATOR_CHAR;
		s += getISTTaxekat(careTime) + SEPARATOR_CHAR;
		s += getISTDate(contractFrom) + SEPARATOR_CHAR;
		s += getISTDate(contractTo);
		return s;
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
	
	/**
	 * Returns the formated date interval for the specified export filename.
	 */
	public String getFileDateInterval(String fileName) {
		String s = "";
		try {
			ChildCareExportTime et = getChildCareExportTimeHome().findByFileName(fileName);
			IWTimestamp from = null;
			IWTimestamp to = null;
			if (et.getFromDate() != null) {
				from = new IWTimestamp(et.getFromDate());
				s += from.getDateString("yyyy-MM-dd");
			}
			if (et.getToDate() != null) {
				to = new IWTimestamp(et.getToDate());
				s += " - " + to.getDateString("yyyy-MM-dd");
			}
		} catch (Exception e) {}
		return s;
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
	 * @param userId the id for the user who created the export file
	 * @param to the latest date for placement to be included in the export
	 * @return the file name for the export file
	 * @throws ChildCareExportException
	 */
	public String storePlacementExportFileTimestamp(int userId, Date to) throws ChildCareExportException {
		return storeExportFileTimestamp(userId, FILE_TYPE_PLACEMENT, null, to);		
	}

	/**
	 * Stores a new entry in the export file timestamp log (ChildCareExportTime).
	 * The created timestamp is set to current time.
	 * The file type is set to IST Extens taxekat export file.
	 * @param userId the id for the user who created the export file
	 * @param from the first date from which to include changes 
	 * @param to the last date to which to include changes 
	 * @return the file name for the export file
	 * @throws ChildCareExportException
	 */
	public String storeTaxekatExportFileTimestamp(int userId, Date from, Date to) throws ChildCareExportException {
		return storeExportFileTimestamp(userId, FILE_TYPE_TAXEKAT, from, to);
	}
	
	/*
	 * Store a new entry in the export file timestamp log.
	 */
	private String storeExportFileTimestamp(int userId, int fileType, Date from, Date to) throws ChildCareExportException {		
		IWTimestamp now = IWTimestamp.RightNow();
		String fileName = getExportFileName(fileType, now);

		try {
			ChildCareExportTime entry = getChildCareExportTimeHome().create();
			entry.setCreated(now.getTimestamp());
			entry.setUserId(userId);
			entry.setFileName(fileName);
			entry.setFileType(fileType);
			entry.setFromDate(from);
			entry.setToDate(to);
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
		
		fileName += timestamp.getDateString("yyMMdd_HH_mm");
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
			exportFiles = exportFolder.getChildrenIterator("NAME");
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
