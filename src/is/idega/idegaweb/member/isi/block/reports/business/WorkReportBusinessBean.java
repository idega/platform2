package is.idega.idegaweb.member.isi.block.reports.business;
import is.idega.idegaweb.member.business.MemberUserBusiness;
import is.idega.idegaweb.member.business.MemberUserBusinessBean;
import is.idega.idegaweb.member.business.NoFederationFoundException;
import is.idega.idegaweb.member.business.NoRegionalUnionFoundException;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportAccountKey;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportAccountKeyHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMember;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMemberHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportClubAccountRecord;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportClubAccountRecordHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportDivisionBoard;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportDivisionBoardHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportExportFile;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportExportFileHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroupHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportImportBoardMember;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportImportBoardMemberHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportImportClubAccountRecord;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportImportClubAccountRecordHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportImportDivisionBoard;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportImportDivisionBoardHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportImportMember;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportImportMemberHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportMemberHome;
import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.mail.MessagingException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.idega.block.media.business.MediaBusiness;
import com.idega.core.data.Address;
import com.idega.core.data.Email;
import com.idega.core.data.ICFile;
import com.idega.core.data.ICFileHome;
import com.idega.core.data.Phone;
import com.idega.core.data.PhoneType;
import com.idega.core.data.PostalCode;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.transaction.IdegaTransactionManager;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;
import com.idega.util.caching.Cache;
import com.idega.util.text.TextSoap;

/**
 * Description:	Use this business class to handle work report related business.
 * Copyright:    Copyright (c) 2003
 * Company:      Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */
public class WorkReportBusinessBean extends MemberUserBusinessBean implements MemberUserBusiness, WorkReportBusiness {

	private static int SHEET_BOARD_PART = 0;
	private static int SHEET_ACCOUNT_PART = 1;
	private static int SHEET_MEMBER_PART = 2;

	private WorkReportGroupHome workReportGroupHome;
	private WorkReportClubAccountRecordHome workReportClubAccountRecordHome;
	private WorkReportAccountKeyHome workReportAccountKeyHome;
	private WorkReportHome workReportHome;
	private WorkReportMemberHome workReportMemberHome;
	private WorkReportBoardMemberHome workReportBoardMemberHome;
	private WorkReportDivisionBoardHome workReportDivisionBoardHome;
	private WorkReportExportFileHome workReportExportFileHome;

	//Temporary import tables
	private WorkReportImportMemberHome workReportImportMemberHome;
	private WorkReportImportBoardMemberHome workReportImportBoardMemberHome;
	private WorkReportImportDivisionBoardHome workReportImportDivisionBoardHome;
	private WorkReportImportClubAccountRecordHome workReportImportClubAccountRecordHome;

	private static final short COLUMN_MEMBER_NAME = 0;
	private static final short COLUMN_MEMBER_SSN = 1;
	private static final short COLUMN_MEMBER_STREET_NAME = 2;
	private static final short COLUMN_MEMBER_POSTAL_CODE = 3;

	private static final short COLUMN_BOARD_MEMBER_LEAGUE = 0;
	private static final short COLUMN_BOARD_MEMBER_NAME = 2;
	private static final short COLUMN_BOARD_MEMBER_SSN = 3;
	private static final short COLUMN_BOARD_MEMBER_STREET_NAME = 4;
	private static final short COLUMN_BOARD_MEMBER_POSTAL_CODE = 5;

	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";

	public int getTotalCountOfMembersForWorkReportYear(int year) {
		int count = 0;
		try {
			Collection reports = this.getWorkReportHome().findAllWorkReportsByYearOrderedByGroupType(year);
			Iterator iter = reports.iterator();

			while (iter.hasNext()) {
				WorkReport report = (WorkReport)iter.next();
				int add = report.getNumberOfMembers();
				count += (add > 0) ? add : 0; //add to sum if more than 0
			}

		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return count;
	}

	public int getTotalCountOfPlayersForWorkReportYear(int year) {
		int count = 0;
		try {
			Collection reports = this.getWorkReportHome().findAllWorkReportsByYearOrderedByGroupType(year);
			Iterator iter = reports.iterator();

			while (iter.hasNext()) {
				WorkReport report = (WorkReport)iter.next();
				int add = report.getNumberOfPlayers();
				count += (add > 0) ? add : 0; //add to sum if more than 0
			}
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return count;
	}

	public int getTotalCountOfWorkReportsByStatusAndYear(String status, int year) {

		return getWorkReportHome().getCountOfWorkReportsByStatusAndYear(status, year);

	}

	public int getTotalCountOfCompetitorsForWorkReportYear(int year) {
		int count = 0;
		try {
			Collection reports = this.getWorkReportHome().findAllWorkReportsByYearOrderedByGroupType(year);
			Iterator iter = reports.iterator();

			while (iter.hasNext()) {
				WorkReport report = (WorkReport)iter.next();
				int add = report.getNumberOfCompetitors();
				count += (add > 0) ? add : 0; //add to sum if more than 0
			}
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return count;
	}

	/**
	 * This method gets you the id of the workreport of the club and year specified. It will create a new report if it does not exist already.
	 * @param clubId
	 * @param yearStamp
	 * @return The id of the WorkReport for this club and year.
	 */
	public int getOrCreateWorkReportIdForGroupIdByYear(int groupId, int year, boolean updateReport) throws RemoteException {
		WorkReport report = null;

		createOrUpdateLeagueWorkReportGroupsForYear(year);

		try {
			report = getWorkReportHome().findWorkReportByGroupIdAndYearOfReport(groupId, year);
		}
		catch (FinderException e) {
			System.out.println("[WorkReportBusinessBean] No report for groupId : " + groupId + " ann year : " + year + " creating a new one.");
			try {
				Group club;
				try {
					club = this.getGroupBusiness().getGroupByGroupID(groupId); //could be club,regional union or league
					report = getWorkReportHome().create();
					report.setGroupId(groupId);
					report.setYearOfReport(year);

					//THIS IS CRAP IT SHOULD JUST USE .getName() !! palli bitch
					report.setGroupName((club.getMetaData(IWMemberConstants.META_DATA_CLUB_NAME) != null) ? club.getMetaData(IWMemberConstants.META_DATA_CLUB_NAME) : club.getName());
					report.setGroupNumber(club.getMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER));
					report.setGroupShortName(club.getMetaData(IWMemberConstants.META_DATA_CLUB_SHORT_NAME));
					report.setStatus(WorkReportConstants.WR_STATUS_NOT_DONE);
					report.setGroupType(club.getGroupType());

					//tegund felags?
					//IWMemberConstants.META_DATA_CLUB_TYPE

					//status ovirkt?
					//META_DATA_CLUB_STATUS
					String status = club.getMetaData(IWMemberConstants.META_DATA_CLUB_STATUS);
					if (IWMemberConstants.META_DATA_CLUB_STATE_INACTIVE.equals(status)) {
						report.setAsInactive();
					}
					else {
						report.setAsActive();
					}

					try {
						Group regionalUnion = this.getRegionalUnionGroupForClubGroup(club);

						report.setRegionalUnionGroupId((Integer)regionalUnion.getPrimaryKey());
						report.setRegionalUnionNumber(regionalUnion.getMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER));
						report.setRegionalUnionAbbreviation(regionalUnion.getMetaData(IWMemberConstants.META_DATA_CLUB_ABRV));

					}
					catch (NoRegionalUnionFoundException e3) {
						//no regional union, must be a league or a regional union itself
					}

					report.store();
				}
				catch (FinderException e2) {
					e2.printStackTrace();
				}
			}
			catch (CreateException e1) {
				e1.printStackTrace();
			}

		}

		if (report != null) {
			createWorkReportData(((Integer)report.getPrimaryKey()).intValue());
			return ((Integer)report.getPrimaryKey()).intValue();
		}
		else {
			return -1;
		}

	}

	public WorkReportHome getWorkReportHome() {
		if (workReportHome == null) {
			try {
				workReportHome = (WorkReportHome)IDOLookup.getHome(WorkReport.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportHome;
	}

	public WorkReportMemberHome getWorkReportMemberHome() {
		if (workReportMemberHome == null) {
			try {
				workReportMemberHome = (WorkReportMemberHome)IDOLookup.getHome(WorkReportMember.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportMemberHome;
	}

	public WorkReportImportMemberHome getWorkReportImportMemberHome() {
		if (workReportImportMemberHome == null) {
			try {
				workReportImportMemberHome = (WorkReportImportMemberHome)IDOLookup.getHome(WorkReportImportMember.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportImportMemberHome;
	}

	public WorkReportBoardMemberHome getWorkReportBoardMemberHome() {
		if (workReportBoardMemberHome == null) {
			try {
				workReportBoardMemberHome = (WorkReportBoardMemberHome)IDOLookup.getHome(WorkReportBoardMember.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportBoardMemberHome;
	}

	public WorkReportImportBoardMemberHome getWorkReportImportBoardMemberHome() {
		if (workReportImportBoardMemberHome == null) {
			try {
				workReportImportBoardMemberHome = (WorkReportImportBoardMemberHome)IDOLookup.getHome(WorkReportImportBoardMember.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportImportBoardMemberHome;
	}

	public WorkReportDivisionBoardHome getWorkReportDivisionBoardHome() {
		if (workReportDivisionBoardHome == null) {
			try {
				workReportDivisionBoardHome = (WorkReportDivisionBoardHome)IDOLookup.getHome(WorkReportDivisionBoard.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportDivisionBoardHome;
	}

	public WorkReportImportDivisionBoardHome getWorkReportImportDivisionBoardHome() {
		if (workReportImportDivisionBoardHome == null) {
			try {
				workReportImportDivisionBoardHome = (WorkReportImportDivisionBoardHome)IDOLookup.getHome(WorkReportImportDivisionBoard.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportImportDivisionBoardHome;
	}

	public WorkReportGroupHome getWorkReportGroupHome() {
		if (workReportGroupHome == null) {
			try {
				workReportGroupHome = (WorkReportGroupHome)IDOLookup.getHome(WorkReportGroup.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportGroupHome;
	}

	public WorkReportAccountKeyHome getWorkReportAccountKeyHome() {
		if (workReportAccountKeyHome == null) {
			try {
				workReportAccountKeyHome = (WorkReportAccountKeyHome)IDOLookup.getHome(WorkReportAccountKey.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportAccountKeyHome;
	}

	public WorkReportClubAccountRecordHome getWorkReportClubAccountRecordHome() {
		if (workReportClubAccountRecordHome == null) {
			try {
				workReportClubAccountRecordHome = (WorkReportClubAccountRecordHome)IDOLookup.getHome(WorkReportClubAccountRecord.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportClubAccountRecordHome;
	}

	public WorkReportImportClubAccountRecordHome getWorkReportImportClubAccountRecordHome() {
		if (workReportImportClubAccountRecordHome == null) {
			try {
				workReportImportClubAccountRecordHome = (WorkReportImportClubAccountRecordHome)IDOLookup.getHome(WorkReportImportClubAccountRecord.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportImportClubAccountRecordHome;
	}

	public WorkReportExportFileHome getWorkReportExportFileHome() {
		if (workReportExportFileHome == null) {
			try {
				workReportExportFileHome = (WorkReportExportFileHome)IDOLookup.getHome(WorkReportExportFile.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportExportFileHome;
	}

	public WorkReportMember createWorkReportMember(int reportID, String personalID) throws CreateException {

		// there are some users in the system without any social security number
		if (personalID.length() == 0) {
			return null;
		}
		User user = null;
		try {
			user = getUser(personalID);
		}
		catch (FinderException e) {
			return null;
		}
		return createWorkReportMember(reportID, user);
	}

	public WorkReportMember createWorkReportMember(int reportID, Integer userId) throws CreateException {
		User user = null;
		try {
			user = getUser(userId);
		}
		catch (EJBException e) {
			System.err.println("[WorkReportBusiness]: User could not be found. Message is: " + e.getMessage());
			e.printStackTrace(System.err);
			return null;
		}
		return createWorkReportMember(reportID, user);
	}

	public WorkReportMember createWorkReportMember(int reportID, User user) throws CreateException {
		Age age = null;

		WorkReportMember member = getWorkReportMemberHome().create();

		if (user.getDateOfBirth() != null)
			age = new Age(user.getDateOfBirth());

		member.setReportId(reportID);
		member.setName(user.getName());
		member.setPersonalId(user.getPersonalID());
		if (age != null) {
			member.setAge(age.getYears());
			member.setDateOfBirth((new IWTimestamp(user.getDateOfBirth())).getTimestamp());
		}
		member.setUserId(((Integer)user.getPrimaryKey()).intValue());
		int gender = user.getGenderID();
		try {
			int male = getGenderId("male").intValue();
			if (gender == male) {
				member.setAsMale();
			}
			else {
				member.setAsFemale();
			}
		}
		catch (Exception ex) {
			String errorMessage = "[WorkreportBusiness] Gender can not be retrieved. Message is. " + ex.getMessage();
			System.err.println(errorMessage);
			ex.printStackTrace(System.err);
			throw new CreateException(errorMessage);
		}
		// address
		try {
			Address address = getUsersMainAddress(user);
			if (address != null) {
				String streetAddress = address.getStreetAddress();
				if (streetAddress != null) {
					member.setStreetName(streetAddress);
				}
				int postalCodeId = address.getPostalCodeID();
				if (postalCodeId > 0) {
					member.setPostalCodeID(postalCodeId);
				}
			}
		}
		catch (RemoteException ex) {
			String message = "[WorkReportBusiness]: Can't retrieve  user's main address.";
			System.err.println(message + " Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
			throw new RuntimeException(message);
		}
		member.store();
		return member;

	}

	public WorkReportBoardMember createWorkReportBoardMember(int reportID, String personalID, WorkReportGroup workReportGroup) throws CreateException {

		User user = null;
		try {
			user = getUser(personalID);
		}
		catch (FinderException e) {
			return null;
		}
		return createWorkReportBoardMember(reportID, user, workReportGroup);
	}

	public WorkReportBoardMember createWorkReportBoardMember(int reportID, User user, WorkReportGroup workReportGroup) throws CreateException {

		Age age = new Age(user.getDateOfBirth());

		WorkReportBoardMember member = getWorkReportBoardMemberHome().create();
		member.setReportId(reportID);
		member.setName(user.getName());
		member.setPersonalId(user.getPersonalID());
		member.setAge(age.getYears());
		member.setDateOfBirth((new IWTimestamp(user.getDateOfBirth())).getTimestamp());
		member.setUserId(((Integer)user.getPrimaryKey()).intValue());
		// league
		if (workReportGroup != null) {
			int pk = ((Integer)workReportGroup.getPrimaryKey()).intValue();
			member.setWorkReportGroupID(pk);
		}
		int gender = user.getGenderID();
		try {
			int male = getGenderId("male").intValue();
			if (gender == male) {
				member.setAsMale();
			}
			else {
				member.setAsFemale();
			}
		}
		catch (Exception ex) {
			String errorMessage = "[WorkreportBusiness] Gender can not be retrieved. Message is. " + ex.getMessage();
			System.err.println(errorMessage);
			ex.printStackTrace(System.err);
			throw new CreateException(errorMessage);
		}
		// address
		try {
			Address address = getUsersMainAddress(user);
			if (address != null) {
				String streetAddress = address.getStreetAddress();
				if (streetAddress != null) {
					member.setStreetName(streetAddress);
				}
				int postalCodeId = address.getPostalCodeID();
				if (postalCodeId > 0) {
					member.setPostalCodeID(postalCodeId);
				}
			}
		}
		catch (RemoteException ex) {
			String message = "[WorkReportBusiness]: Can't retrieve  user's main address.";
			System.err.println(message + " Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
			throw new RuntimeException(message);
		}
		member.store();
		return member;
	}

	public WorkReportDivisionBoard createWorkReportDivisionBoard(int reportId, Group clubDivision, WorkReportGroup league) throws CreateException {
		// does the division board already exist?
		try {
			WorkReportDivisionBoardHome workReportDivisionBoardHome = getWorkReportDivisionBoardHome();
			int workReportGroupId = ((Integer)league.getPrimaryKey()).intValue();
			WorkReportDivisionBoard divisionBoard = workReportDivisionBoardHome.findWorkReportDivisionBoardByWorkReportIdAndWorkReportGroupId(reportId, workReportGroupId);
			if (divisionBoard != null) {
				// division board exist be sure that the league was added to the work report
				WorkReport workReport = getWorkReportById(reportId);
				try {
					workReport.addLeague(league);
					workReport.store();
				}
				catch (IDORelationshipException ex) {
					String message = "[WorkReportBusiness]: Can't define realtion ship.";
					System.err.println(message + " Message is: " + ex.getMessage());
					ex.printStackTrace(System.err);
					// do nothing
				}
			}
		}
		catch (FinderException ex) {
			// work report division does not exist, go further, create it!
		}

		// get group business
		GroupBusiness groupBusiness = null;
		try {
			groupBusiness = getGroupBusiness();
		}
		catch (RemoteException ex) {
			System.err.println("[WorkReportBusiness]: Can't retrieve GroupBusiness or an address. Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
			throw new RuntimeException("[WorkReportBusiness]: Can't retrieve GroupBusiness or an address.");
		}
		WorkReportDivisionBoard workReportDivisionBoard = getWorkReportDivisionBoardHome().create();
		workReportDivisionBoard.setReportId(reportId);
		// corresponding division board group
		Integer id = (Integer)clubDivision.getPrimaryKey();
		workReportDivisionBoard.setGroupId(id.intValue());
		// home page 
		String homePageURL = clubDivision.getHomePageURL();
		if (homePageURL != null) {
			workReportDivisionBoard.setHomePage(homePageURL);
		}
		// personal id 
		String ssn = clubDivision.getMetaData(IWMemberConstants.META_DATA_CLUB_SSN);
		if (ssn != null) {
			workReportDivisionBoard.setPersonalId(ssn);
		}
		// address
		try {
			Address address = groupBusiness.getGroupMainAddress(clubDivision);
			if (address != null) {
				// street and number
				String streetAndNumber = address.getStreetAddress();
				if (streetAndNumber != null) {
					workReportDivisionBoard.setStreetName(streetAndNumber);
				}
				// postal code id
				PostalCode postalCode = address.getPostalCode();
				if (postalCode != null) {
					workReportDivisionBoard.setPostalCode(postalCode);
				}
			}
		}
		catch (RemoteException ex) {
			System.err.println("[WorkReportBusiness]: Can't retrieve Address. Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
			throw new RuntimeException("[WorkReportBusiness]: Can't retrieve Address.");
		}

		// home phone
		try {
			Phone homePhone = groupBusiness.getGroupPhone(clubDivision, PhoneType.HOME_PHONE_ID);
			if (homePhone != null) {
				String number = homePhone.getNumber();
				if (number != null) {
					workReportDivisionBoard.setFirstPhone(number);
				}
			}
		}
		catch (RemoteException ex) {
			System.err.println("[WorkReportBusiness]: Can't retrieve home phone. Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
			throw new RuntimeException("[WorkReportBusiness]: Can't retrieve home phone.");
		}
		// work phone
		try {
			Phone workPhone = groupBusiness.getGroupPhone(clubDivision, PhoneType.WORK_PHONE_ID);
			if (workPhone != null) {
				String number = workPhone.getNumber();
				if (number != null) {
					workReportDivisionBoard.setSecondPhone(number);
				}
			}
		}
		catch (RemoteException ex) {
			System.err.println("[WorkReportBusiness]: Can't retrieve work phone. Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
			throw new RuntimeException("[WorkReportBusiness]: Can't retrieve work phone.");
		}
		// fax
		try {
			Phone fax = groupBusiness.getGroupPhone(clubDivision, PhoneType.FAX_NUMBER_ID);
			if (fax != null) {
				String number = fax.getNumber();
				if (number != null) {
					workReportDivisionBoard.setFax(number);
				}
			}
		}
		catch (RemoteException ex) {
			System.err.println("[WorkReportBusiness]: Can't retrieve fax phone. Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
			throw new RuntimeException("[WorkReportBusiness]: Can't retrieve fax phone.");
		}
		// email
		String eMail = workReportDivisionBoard.getEmail();
		if (eMail != null) {
			workReportDivisionBoard.setEmail(eMail);
		}

		// league
		if (league != null) {
			int pk = ((Integer)league.getPrimaryKey()).intValue();
			workReportDivisionBoard.setWorKReportGroupID(pk);
		}
		// +++++++++++++++++++++++++++++++
		// add league to work report group
		// +++++++++++++++++++++++++++++++
		WorkReport workReport = getWorkReportById(reportId);
		try {
			workReport.addLeague(league);
			workReport.store();
		}
		catch (IDORelationshipException ex) {
			String message = "[WorkReportBusiness]: Can't define realtion ship.";
			System.err.println(message + " Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
			// do nothing
		}
		workReportDivisionBoard.store();
		return workReportDivisionBoard;
	}

	public WorkReport getWorkReportById(int id) {
		try {
			return getWorkReportHome().findByPrimaryKey(new Integer(id));
		}
		catch (FinderException e) {
			e.printStackTrace();
			return null;
		}
	}

	/** Returns all leagues, that  belong to the specified work report.
	 * @param  id - id of the work report
	 * @return a collection of WorkReportGroups  
	 */
	public Collection getLeaguesOfWorkReportById(int id) throws IDOException {
		WorkReport workReport = getWorkReportById(id);
		return workReport.getLeagues();
	}

	private File getFileObjectForFileId(int fileId) {
		Cache file = MediaBusiness.getCachedFileInfo(fileId, this.getIWApplicationContext().getApplication());

		return new File(file.getRealPathToFile());
	}

	private HSSFWorkbook getExcelWorkBookFromFileId(int fileId) throws WorkReportImportException {
		HSSFWorkbook excel = null;
		File file = getFileObjectForFileId(fileId);

		try {
			excel = new HSSFWorkbook(new FileInputStream(file));
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new WorkReportImportException("workreportimportexception.file_not_found");
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new WorkReportImportException("workreportimportexception.could_not_read_file");
		}

		return excel;
	}

	public boolean importMemberPart(int workReportFileId, int workReportId,String mainBoardName) throws WorkReportImportException {
		int memberCount = 0;
		int playerCount = 0;
		HashMap divPlayerCount = new HashMap();
		System.out.println("Starting member importing from excel file for workreportid: " + workReportId);

		//Check to see if the work report is read only
		if (isWorkReportReadOnly(workReportId))
			throw new WorkReportImportException("workreportimportexception.is_read_only");

		//clear the table first
		deleteWorkReportMembersForReport(workReportId);

		WorkReportMemberHome membHome = getWorkReportMemberHome();
		WorkReport report = getWorkReportById(workReportId);
		int year = report.getYearOfReport().intValue();

		//update or create the league groups so we can connect the users to them
		createOrUpdateLeagueWorkReportGroupsForYear(year);

		report.setMemberFileId(workReportFileId);
		report.store();

		HSSFWorkbook excel = getExcelWorkBookFromFileId(workReportFileId);
		int sheets = excel.getNumberOfSheets();
		if (sheets > 3)
			throw new WorkReportImportException("workreportimportexception.wrong_number_of_sheets");

		HSSFSheet members = excel.getSheetAt(SHEET_MEMBER_PART);
		int firstRow = 4;
		int lastRow = members.getLastRowNum();

		//System.out.println("First row is at: "+firstRow);
		//System.out.println("Last row is at: "+lastRow);

		//get the top row to get a list of leagues to use.
		HSSFRow headerRow = (HSSFRow)members.getRow(firstRow);
		Map leaguesMap = getLeaguesMapFromRow(headerRow, year, 5);

		//iterate through the rows that contain the actual data and create the records in the database
		for (int i = (firstRow + 1); i <= lastRow; i++) {
			HSSFRow row = (HSSFRow)members.getRow(i);

			if (row != null) {
				int firstCell = row.getFirstCellNum();
				int lastCell = row.getLastCellNum();

				//String name = HSSFCellUtil.translateUnicodeValues(row.getCell(COLUMN_MEMBER_NAME)).getStringCellValue();
				String name = row.getCell(COLUMN_MEMBER_NAME).getStringCellValue();
				String ssn = getStringValueFromExcelNumberOrStringCell(row, COLUMN_MEMBER_SSN);
				ssn = (ssn.length() < 10) ? "0" + ssn : ssn;
				String streetName = getStringValueFromExcelNumberOrStringCell(row, COLUMN_MEMBER_STREET_NAME);
				String postalCode = getStringValueFromExcelNumberOrStringCell(row, COLUMN_MEMBER_POSTAL_CODE);

				try {
					//the user must already exist in the database
					User user = this.getUser(ssn);
					try {
						membHome.findWorkReportMemberByUserIdAndWorkReportId(((Integer)user.getPrimaryKey()).intValue(), workReportId);
					}
					catch (FinderException e4) {
						//this should happen, we don't want them created twice	

						WorkReportMember member = createWorkReportMember(workReportId, ssn); //sets basic data
						if (streetName != null && !"".equals(streetName)) {
							member.setStreetName(streetName);

							try {
								PostalCode postal = getAddressBusiness().getPostalCodeHome().findByPostalCodeAndCountryId(postalCode, ((Integer)getAddressBusiness().getCountryHome().findByCountryName("Iceland").getPrimaryKey()).intValue());
								member.setPostalCode(postal);
							}
							catch (FinderException e3) {
								//e3.printStackTrace();
							}
							catch (RemoteException e) {
								e.printStackTrace();
							}

						}

						member.store();

						memberCount++;

						WorkReportGroup mainBoard = null;
						try {
							mainBoard = getWorkReportGroupHome().findWorkReportGroupByNameAndYear(mainBoardName, year);
						}
						catch (FinderException e1) {
							throw new WorkReportImportException("workreportimportexception.main_board_not_found");
						}

						//find which leagues the member belongs to
						//and create the many to many connections
						for (int j = 5; j < lastCell; j++) {
							HSSFCell leagueCell = row.getCell((short)j);

							if (leagueCell != null) {
								String check = leagueCell.getStringCellValue();
								//								boolean isChecked = (check != null && !"".equals(check) && "X".equals(check.toUpperCase()));
								boolean isChecked = (check != null && !"".equals(check));
								if (isChecked) {
									WorkReportGroup league = (WorkReportGroup)leaguesMap.get(new Integer(j));
									if (league != null) {
										try {
											league.addEntity(member);
											try {
												report.addLeague(league);
											}
											catch (Exception e) {
												//e.printStackTrace();
											}
											mainBoard.addEntity(member);
											playerCount++;
											Integer count = (Integer)divPlayerCount.get(new Integer(j));
											if (count == null)
												count = new Integer(1);
											else
												count = new Integer(count.intValue() + 1);

											divPlayerCount.put(new Integer(j), count);
										}
										catch (IDOAddRelationshipException e5) {
											e5.printStackTrace();
											throw new WorkReportImportException("workreportimportexception.database_error_could_not_add_member_to_group");
										}
									}
								}
							}

						}
					}
				}
				catch (EJBException e1) {
					e1.printStackTrace();
					throw new WorkReportImportException("workreportimportexception.database_error");
				}
				catch (CreateException e2) {
					//failed to create move on.
					e2.printStackTrace();
					System.err.println("Failed to create user for ssn : " + ssn);
					throw new WorkReportImportException("workreportimportexception.database_error_failed_to_create_user");
				}
				catch (FinderException e) {
					System.err.println("User not found for ssn : " + ssn + " skipping...");
				}
			}
		}

		report.setNumberOfMembers(memberCount);
		report.setNumberOfPlayers(playerCount);
		report.store();

		//Store work-report division thingie........ HOW!!!???!!!		
		Iterator it = leaguesMap.keySet().iterator();
		while (it.hasNext()) {
			Integer id = (Integer)it.next();
			Integer val = (Integer)divPlayerCount.get(id);

			WorkReportDivisionBoard board = null;
			try {
				board = (WorkReportDivisionBoard)getWorkReportDivisionBoardHome().findWorkReportDivisionBoardByWorkReportIdAndWorkReportGroupId(workReportId, id.intValue());
			}
			catch (FinderException e) {
				try {
					board = (WorkReportDivisionBoard)getWorkReportDivisionBoardHome().create();
				}
				catch (CreateException e1) {
					e1.printStackTrace();
					throw new WorkReportImportException("workreportimportexception.error_creating_division");
				}
			}

			board.setNumberOfPlayers(val.intValue());
			board.store();
		}

		return true;
	}

	public boolean importBoardPart(int workReportFileId, int workReportId) throws WorkReportImportException {

		UserTransaction transaction;

		System.out.println("Starting board and division importing from excel file...");

		//Check to see if the work report is read only
		if (isWorkReportReadOnly(workReportId))
			throw new WorkReportImportException("workreportimportexception.is_read_only");

		deleteWorkReportBoardMembersForReport(workReportId);

		WorkReportBoardMemberHome membHome = getWorkReportBoardMemberHome();
		WorkReport report = getWorkReportById(workReportId);
		int year = report.getYearOfReport().intValue();
		createOrUpdateLeagueWorkReportGroupsForYear(year);

		report.setBoardFileId(workReportFileId);
		report.store();

		HSSFWorkbook excel = getExcelWorkBookFromFileId(workReportFileId);
		int sheets = excel.getNumberOfSheets();
		if (sheets > 3)
			throw new WorkReportImportException("workreportimportexception.wrong_number_of_sheets");

		HSSFSheet members = excel.getSheetAt(SHEET_BOARD_PART);
		int firstRow = 6;
		int lastRow = members.getLastRowNum();

		System.out.println("First row is at: " + firstRow);
		System.out.println("Last row is at: " + lastRow);

		//iterate through the rows that contain the actual data and create the records in the database
		int i = firstRow;
		while (i <= lastRow) {

			HSSFRow row = (HSSFRow)members.getRow(i);

			if (row != null) {
				int firstCell = row.getFirstCellNum();
				int lastCell = row.getLastCellNum();

				HSSFCell nameCell = row.getCell(COLUMN_BOARD_MEMBER_NAME);
				if (nameCell == null) {
					break; //stop
				}

				HSSFCell leagueCell = row.getCell(COLUMN_BOARD_MEMBER_LEAGUE);
				String league = null;
				WorkReportGroup group = null;
				if (leagueCell != null) {
					league = leagueCell.getStringCellValue();
					if (league != null && !"".equals(league.trim())) {
						league = league.toUpperCase();
						try {
							group = getWorkReportGroupHome().findWorkReportGroupByShortNameAndYear(league, year);
						}
						catch (FinderException e) {
							e.printStackTrace();
							System.err.println("WorkReportGroup not found by short name : " + league + " trying group name");

							try {
								group = getWorkReportGroupHome().findWorkReportGroupByNameAndYear(league, year);
							}
							catch (FinderException e1) {
								throw new WorkReportImportException("workreportimportexception.league_not_found");
							}
						}
					}
				}

				String name = nameCell.getStringCellValue();
				if (name == null || name.indexOf("##") != -1) {
					break; //stop
				}

				String ssn = getStringValueFromExcelNumberOrStringCell(row, COLUMN_BOARD_MEMBER_SSN);
				ssn = (ssn.length() < 10) ? "0" + ssn : ssn;
				String streetName = getStringValueFromExcelNumberOrStringCell(row,COLUMN_BOARD_MEMBER_STREET_NAME);
				String postalCode = getStringValueFromExcelNumberOrStringCell(row, COLUMN_BOARD_MEMBER_POSTAL_CODE);

				WorkReportBoardMember member;

				try {
					//the user must already exist in the database
					User user = this.getUser(ssn);
					try {

						member = membHome.findWorkReportBoardMemberByUserIdAndWorkReportId(((Integer)user.getPrimaryKey()).intValue(), workReportId);
						member.store();
					}
					catch (FinderException e4) {
						//this should happen, we don't want them created twice	
						member = membHome.create(); //createWorkImportReportBoardMember(workReportId, ssn, group); //sets basic data
						member.setPersonalId(ssn);
						member.setReportId(workReportId);
						//Set group??

						if (streetName != null && !"".equals(streetName)) {
							member.setStreetName(streetName);

							try {
								PostalCode postal = getAddressBusiness().getPostalCodeHome().findByPostalCodeAndCountryId(postalCode, ((Integer)getAddressBusiness().getCountryHome().findByCountryName("Iceland").getPrimaryKey()).intValue());
								member.setPostalCode(postal);
							}
							catch (FinderException e3) {
								//e3.printStackTrace();
							}
							catch (RemoteException e) {
								e.printStackTrace();
							}

						}

						member.store();

					}
				}
				catch (EJBException e1) {
					e1.printStackTrace();
				}
				catch (CreateException e2) {
					//failed to create move on.
					e2.printStackTrace();
					System.err.println("Failed to create user for ssn : " + ssn);
				}
				catch (FinderException e) {
					System.err.println("User not found for ssn : " + ssn);
				}
			}

			i++;
		}

		//Division part
		i += 7;
		while (i <= lastRow) {
			HSSFRow row = (HSSFRow)members.getRow(i);

			WorkReportGroup group = null;
			if (row != null) {
				String league = getStringValueFromExcelNumberOrStringCell(row, (short)0);
				if (league != null && !"".equals(league)) {
					league = league.toUpperCase();
					try {
						group = getWorkReportGroupHome().findWorkReportGroupByShortNameAndYear(league, year);
					}
					catch (FinderException e) {
						e.printStackTrace();
						System.err.println("WorkReportGroup not found by short name : " + league + " trying group name");

						try {
							group = getWorkReportGroupHome().findWorkReportGroupByNameAndYear(league, year);
						}
						catch (FinderException e1) {
							throw new WorkReportImportException("workreportimportexception.league_not_found");
						}
					}
				}
				else {
					break;
				}

				String homePage = getStringValueFromExcelNumberOrStringCell(row, (short)2);
				String ssn = getStringValueFromExcelNumberOrStringCell(row, (short)3);
				String address = getStringValueFromExcelNumberOrStringCell(row, (short)4);
				String pnr = getStringValueFromExcelNumberOrStringCell(row, (short)5);
				String tel1 = getStringValueFromExcelNumberOrStringCell(row, (short)6);
				String tel2 = getStringValueFromExcelNumberOrStringCell(row, (short)7);
				String fax = getStringValueFromExcelNumberOrStringCell(row, (short)8);
				;
				String email = getStringValueFromExcelNumberOrStringCell(row, (short)9);
				String champ = getStringValueFromExcelNumberOrStringCell(row, (short)10);

				WorkReportDivisionBoard board = null;
				try {
					board = (WorkReportDivisionBoard)getWorkReportDivisionBoardHome().findWorkReportDivisionBoardByWorkReportIdAndWorkReportGroupId(workReportId, ((Integer)group.getPrimaryKey()).intValue());
				}
				catch (FinderException e) {
					try {
						board = (WorkReportDivisionBoard)getWorkReportDivisionBoardHome().create();
					}
					catch (CreateException e1) {
						e1.printStackTrace();
						throw new WorkReportImportException("workreportimportexception.error_creating_division");
					}
				}

				board.setHomePage(homePage);
				board.setPersonalId(ssn);
				board.setStreetName(address);
				try {
					PostalCode postal = getAddressBusiness().getPostalCodeHome().findByPostalCodeAndCountryId(pnr, ((Integer)getAddressBusiness().getCountryHome().findByCountryName("Iceland").getPrimaryKey()).intValue());
					board.setPostalCode(postal);
				}
				catch (FinderException e3) {
					//e3.printStackTrace();
				}
				catch (RemoteException e) {
					e.printStackTrace();
				}
				board.setFirstPhone(tel1);
				board.setSecondPhone(tel2);
				board.setFax(fax);
				board.setEmail(email);
				board.setWorKReportGroupID(((Integer)group.getPrimaryKey()).intValue());
				board.setReportId(workReportId);
				board.store();
			}
			i++;
		}

		return true;
	}

	/**
	 * A method to import the account part of the ISI workreports. A bit of a hack, since the format 
	 * of the Excel file is "constant", and so this import assumes that the positions of the keys won't 
	 * change. This could lead to problems, but we will look the other way for now :)
	 */
	public boolean importAccountPart(int workReportFileId, int workReportId) throws WorkReportImportException {
		System.out.println("Starting account importing from excel file...");

		//Check to see if the work report is read only
		if (isWorkReportReadOnly(workReportId))
			throw new WorkReportImportException("workreportimportexception.is_read_only");

		deleteWorkReportAccountRecordsForReport(workReportId);

		WorkReportAccountKeyHome accKeyHome = getWorkReportAccountKeyHome();
		WorkReportClubAccountRecordHome clubRecordHome = getWorkReportClubAccountRecordHome();
		WorkReport report = getWorkReportById(workReportId);
		int year = report.getYearOfReport().intValue();
		createOrUpdateLeagueWorkReportGroupsForYear(year);

		report.setAccountFileId(workReportFileId);
		report.store();

		HSSFWorkbook excel = getExcelWorkBookFromFileId(workReportFileId);
		int sheets = excel.getNumberOfSheets();
		if (sheets > 3)
			throw new WorkReportImportException("workreportimportexception.wrong_number_of_sheets");

		HSSFSheet accEntries = excel.getSheetAt(SHEET_ACCOUNT_PART);
		int currRow = 2;
		int leaguesStartColumn = 7;
		int lastRow = accEntries.getLastRowNum();

		System.out.println("Current row is at: " + currRow);
		System.out.println("Last row is at: " + lastRow);

		if (lastRow != 42) {
			System.err.println("Wrong number of lines in account sheet");
			throw new WorkReportImportException("workreportimportexception.wrong_number_lines");
		}

		//get the top row to get a list of leagues to use.
		HSSFRow headerRow = (HSSFRow)accEntries.getRow(currRow);
		Map leaguesMap = getLeaguesMapFromRow(headerRow, year, leaguesStartColumn);

		int numberOfLeagues = 1;
		if (leaguesMap != null)
			numberOfLeagues = leaguesMap.size();

		String accKey = null;

		/**
		 * @todo hehe, put this into one submethod and then call if for each set of rows. Silly silly silly
		 */

		//Get the revenue part
		for (currRow++; currRow < 8; currRow++) {
			HSSFRow row = (HSSFRow)accEntries.getRow(currRow);
			HSSFCell cell = row.getCell((short)1);
			if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
				accKey = Integer.toString((int)cell.getNumericCellValue());
			else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
				accKey = cell.getStringCellValue();
			else
				accKey = null;

			WorkReportAccountKey eAccKey = null;
			try {
				eAccKey = (WorkReportAccountKey)accKeyHome.findAccountKeyByNumber(accKey);
			}
			catch (FinderException e) {
				e.printStackTrace();
				throw new WorkReportImportException("workreportimportexception.wrong_keys_in_sheet");
			}

			for (int i = 0; i < numberOfLeagues; i++) {
				HSSFCell c = row.getCell((short) (leaguesStartColumn + i));
				double val = 0.0;
				if (c != null)
					val = c.getNumericCellValue();

				System.out.println("val = " + val);

				WorkReportGroup league = (WorkReportGroup)leaguesMap.get(new Integer(leaguesStartColumn + i));

				if (val != 0.0) {
					try {
						WorkReportClubAccountRecord rec = workReportClubAccountRecordHome.create();
						rec.setAccountKey(eAccKey);
						rec.setWorkReportGroup(league);
						rec.setReportId(workReportId);
						rec.setAmount((float)val);
						rec.store();

						try {
							getWorkReportById(workReportId).addLeague(league);
						}
						catch (Exception e) {
							//Do nothing
						}
					}
					catch (Exception e) {
						e.printStackTrace();
						throw new WorkReportImportException("workreportimportexception.unable_to_create_account_record");
					}
				}
			}
		}

		//Get the expenses part
		for (currRow = 11; currRow < 26; currRow++) {
			if (currRow != 15) {
				HSSFRow row = (HSSFRow)accEntries.getRow(currRow);
				HSSFCell cell = row.getCell((short)1);
				if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
					accKey = Integer.toString((int)cell.getNumericCellValue());
				else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
					accKey = cell.getStringCellValue();
				else
					accKey = null;

				WorkReportAccountKey eAccKey = null;
				try {
					eAccKey = (WorkReportAccountKey)accKeyHome.findAccountKeyByNumber(accKey);
				}
				catch (FinderException e) {
					e.printStackTrace();
					throw new WorkReportImportException("workreportimportexception.wrong_keys_in_sheet");
				}

				for (int i = 0; i < numberOfLeagues; i++) {
					HSSFCell c = row.getCell((short) (leaguesStartColumn + i));
					double val = 0.0;
					if (c != null)
						val = c.getNumericCellValue();

					System.out.println("val = " + val);
					WorkReportGroup league = (WorkReportGroup)leaguesMap.get(new Integer(leaguesStartColumn + i));
					if (val != 0.0) {
						try {
							WorkReportClubAccountRecord rec = workReportClubAccountRecordHome.create();
							rec.setAccountKey(eAccKey);
							rec.setWorkReportGroup(league);
							rec.setReportId(workReportId);
							rec.setAmount((float)val);
							rec.store();
							try {
								getWorkReportById(workReportId).addLeague(league);
							}
							catch (Exception e) {
								//Do nothing
							}
						}
						catch (Exception e) {
							e.printStackTrace();
							throw new WorkReportImportException("workreportimportexception.unable_to_create_account_record");
						}
					}
				}
			}
		}

		double totalAsset = 0;
		//Get the asset part
		for (currRow = 33; currRow < 35; currRow++) {
			HSSFRow row = (HSSFRow)accEntries.getRow(currRow);
			HSSFCell cell = row.getCell((short)1);
			if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
				accKey = Integer.toString((int)cell.getNumericCellValue());
			else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
				accKey = cell.getStringCellValue();
			else
				accKey = null;

			WorkReportAccountKey eAccKey = null;
			try {
				eAccKey = (WorkReportAccountKey)accKeyHome.findAccountKeyByNumber(accKey);
			}
			catch (FinderException e) {
				e.printStackTrace();
				throw new WorkReportImportException("workreportimportexception.wrong_keys_in_sheet");
			}

			for (int i = 0; i < numberOfLeagues; i++) {
				HSSFCell c = row.getCell((short) (leaguesStartColumn + i));
				double val = 0.0;
				if (c != null)
					val = c.getNumericCellValue();

				System.out.println("val = " + val);
				totalAsset += val;
				WorkReportGroup league = (WorkReportGroup)leaguesMap.get(new Integer(leaguesStartColumn + i));
				if (val != 0.0) {
					try {
						WorkReportClubAccountRecord rec = workReportClubAccountRecordHome.create();
						rec.setAccountKey(eAccKey);
						rec.setWorkReportGroup(league);
						rec.setReportId(workReportId);
						rec.setAmount((float)val);
						rec.store();
						try {
							getWorkReportById(workReportId).addLeague(league);
						}
						catch (Exception e) {
							//Do nothing
						}

					}
					catch (Exception e) {
						e.printStackTrace();
						throw new WorkReportImportException("workreportimportexception.unable_to_create_account_record");
					}
				}
			}
		}

		double totalDept = 0;
		//Get the dept part
		for (currRow = 38; currRow < 42; currRow++) {
			if (currRow != 39) {
				HSSFRow row = (HSSFRow)accEntries.getRow(currRow);
				HSSFCell cell = row.getCell((short)1);
				if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
					accKey = Integer.toString((int)cell.getNumericCellValue());
				else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
					accKey = cell.getStringCellValue();
				else
					accKey = null;

				WorkReportAccountKey eAccKey = null;
				try {
					eAccKey = (WorkReportAccountKey)accKeyHome.findAccountKeyByNumber(accKey);
				}
				catch (FinderException e) {
					e.printStackTrace();
					throw new WorkReportImportException("workreportimportexception.wrong_keys_in_sheet");
				}

				for (int i = 0; i < numberOfLeagues; i++) {
					HSSFCell c = row.getCell((short) (leaguesStartColumn + i));
					double val = 0.0;
					if (c != null)
						val = c.getNumericCellValue();

					System.out.println("val = " + val);
					totalDept += val;
					WorkReportGroup league = (WorkReportGroup)leaguesMap.get(new Integer(leaguesStartColumn + i));
					if (val != 0.0) {
						try {
							WorkReportClubAccountRecord rec = workReportClubAccountRecordHome.create();
							rec.setAccountKey(eAccKey);
							rec.setWorkReportGroup(league);
							rec.setReportId(workReportId);
							rec.setAmount((float)val);
							rec.store();
							try {
								getWorkReportById(workReportId).addLeague(league);
							}
							catch (Exception e) {
								//Do nothing
							}

						}
						catch (Exception e) {
							e.printStackTrace();
							throw new WorkReportImportException("workreportimportexception.unable_to_create_account_record");
						}
					}
				}
			}
		}

		if (totalAsset != totalDept)
			throw new WorkReportImportException("workreportimportexception.asset_and_dept_not_the_same");

		//Store work-report division thingie........ HOW!!!???!!!		
		Iterator it = leaguesMap.keySet().iterator();
		while (it.hasNext()) {
			Integer id = (Integer)it.next();

			WorkReportDivisionBoard board = null;
			try {
				board = (WorkReportDivisionBoard)getWorkReportDivisionBoardHome().findWorkReportDivisionBoardByWorkReportIdAndWorkReportGroupId(workReportId, id.intValue());
			}
			catch (FinderException e) {
				try {
					board = (WorkReportDivisionBoard)getWorkReportDivisionBoardHome().create();
				}
				catch (CreateException e1) {
					e1.printStackTrace();
					throw new WorkReportImportException("workreportimportexception.error_creating_division");
				}
			}

			board.store();
		}


		return true;
	}

	/**
	 * A method to export the work reports to excel for those who are not using the member system.
	 * 
	 * @param regionalUnionId The id of the regional union who is to receive the file
	 * @param year The year we are creating excel files for
	 * @param templateId The id for the template for the excel files in the IW file system
	 *  
	 * @return The primary key of the ICFile entry created for the zip file, or -1 if nothing was created.
	 */
	public int exportToExcel(int regionalUnionId, int year, int templateId) throws WorkReportImportException {
		WorkReportExportFile export = null;

		try {
			export = getWorkReportExportFileHome().findWorkReportExportFileByGroupIdAndYear(regionalUnionId, year);

			if (export.getFileId() > 0) {
				return export.getFileId();
			}
		}
		catch (FinderException e) {
			System.out.println("[WorkReportBusinessBean] No report for groupId : " + regionalUnionId + " ann year : " + year + " creating a new one.");

			try {
				export = getWorkReportExportFileHome().create();
			}
			catch (CreateException e1) {
				e1.printStackTrace();
				throw new WorkReportImportException("workreportimportexception.unable_to_create_excel_file");
			}
		}

		if (export != null) {
			HSSFWorkbook workbook = getExcelWorkBookFromFileId(templateId);
			String fileName = Integer.toString(regionalUnionId) + "_" + Integer.toString(year) + ".xls";
			try {
				FileOutputStream out = new FileOutputStream(fileName);
				workbook.write(out);
				out.close();

				ICFile icfile = ((ICFileHome)IDOLookup.getHome(ICFile.class)).create();
				icfile.setFileValue(new FileInputStream(fileName));
				icfile.setName(fileName);
				icfile.store();

				export.setGroupId(regionalUnionId);
				export.setYear(year);
				export.setFile(icfile);
				export.store();
			}
			catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			catch (IOException e1) {
				e1.printStackTrace();
			}
			catch (CreateException e1) {
				e1.printStackTrace();
			}

			//			try {
			//					// Create the ZIP file
			//					String outFilename = "outfile.zip";
			//					ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
			//
			//					// Compress the files
			//					for (int i=0; i<filenames.length; i++) {
			//							FileInputStream in = new FileInputStream(filenames[i]);
			//
			//							// Add ZIP entry to output stream.
			//							out.putNextEntry(new ZipEntry(filenames[i]));
			//
			//							// Transfer bytes from the file to the ZIP file
			//							int len;
			//							while ((len = in.read(buf)) > 0) {
			//									out.write(buf, 0, len);
			//							}
			//
			//							// Complete the entry
			//							out.closeEntry();
			//							in.close();
			//					}
			//
			//					// Complete the ZIP file
			//					out.close();
			//			} 
			//			catch (IOException e) {
			//			}

			return export.getFileId();
		}
		else {
			return -1;
		}
	}

	public String getFileName(int id) {
		ICFile icfile;
		try {
			icfile = ((ICFileHome)IDOLookup.getHome(ICFile.class)).findByPrimaryKey(new Integer(id));

			return icfile.getName();
		}
		catch (IDOLookupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "no_file";
	}

	protected MemberUserBusiness getMemberUserBusiness() {
		MemberUserBusiness mub = null;
		try {
			mub = (MemberUserBusiness)getServiceInstance(MemberUserBusiness.class);
		}
		catch (java.rmi.RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
		}

		return mub;
	}

	/**
	 * @param year of report
	 */
	private void createOrUpdateLeagueWorkReportGroupsForYear(int year) {
		createOrUpdateWorkReportGroupsForYearAndGroupType(year, IWMemberConstants.GROUP_TYPE_LEAGUE);
	}

	/**
	 * @param report
	 */
	private void deleteWorkReportMembersForReport(int reportId) {
		try {
			Collection members = getWorkReportMemberHome().findAllWorkReportMembersByWorkReportIdOrderedByMemberName(reportId);
			Iterator iter = members.iterator();

			while (iter.hasNext()) {
				WorkReportMember memb = (WorkReportMember)iter.next();
				try {
					memb.remove();
				}
				catch (EJBException e1) {
					e1.printStackTrace();
				}
				catch (RemoveException e1) {
					e1.printStackTrace();
				}
			}

		}
		catch (FinderException e) {
			//do nothing because its empty
		}
	}

	/**
	 * @param report
	 */
	private void deleteWorkReportBoardMembersForReport(int reportId) {
		try {
			Collection members = getWorkReportBoardMemberHome().findAllWorkReportBoardMembersByWorkReportIdOrderedByMemberName(reportId);
			Iterator iter = members.iterator();

			while (iter.hasNext()) {
				WorkReportMember memb = (WorkReportMember)iter.next();
				try {
					memb.remove();
				}
				catch (EJBException e1) {
					e1.printStackTrace();
				}
				catch (RemoveException e1) {
					e1.printStackTrace();
				}
			}

		}
		catch (FinderException e) {
			//do nothing because its empty
		}
	}

	/**
	 * @param report
	 */
	private void deleteWorkReportAccountRecordsForReport(int reportId) {
		try {
			Collection records = getWorkReportClubAccountRecordHome().findAllRecordsByWorkReportId(reportId);
			Iterator iter = records.iterator();

			while (iter.hasNext()) {
				WorkReportClubAccountRecord record = (WorkReportClubAccountRecord)iter.next();
				try {
					record.remove();
				}
				catch (EJBException e1) {
					e1.printStackTrace();
				}
				catch (RemoveException e1) {
					e1.printStackTrace();
				}
			}

		}
		catch (FinderException e) {
			//do nothing because its empty
		}
	}

	/**
	 * Gets the cell value as string even if it is a number and strips all ".", "-" and trailing exponent "098098809E8"
	 * @param row object
	 * @param column number
	 * @return
	 */
	private String getStringValueFromExcelNumberOrStringCell(HSSFRow row, short columnNumber) {
		String cell = "";
		HSSFCell myCell = row.getCell((short)columnNumber);
		if (myCell != null) {
			if (myCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
				//HSSFCell myCell2 = HSSFCellUtil.translateUnicodeValues(myCell);
				cell = myCell.getStringCellValue();
			}
			else {
				cell = Double.toString(myCell.getNumericCellValue());
			}

			cell = TextSoap.findAndCut(cell, "-");
			cell = TextSoap.findAndCut(cell, ".");

			int index = cell.indexOf("E");

			if (index != -1) {
				cell = cell.substring(0, index);
			}
		}

		return cell;
	}

	/**
	 * Returns a collection of WorkReportGroup or an empty List
	 * @param year , the year of the report
	 * @param type , the group type
	 * @return A collection of WorkReportGroup or an empty List
	 */
	public Collection getAllWorkReportGroupsForYearAndType(int year, String type) {
		try {
			return getWorkReportGroupHome().findAllWorkReportGroupsByGroupTypeAndYear(type, year);
		}
		catch (FinderException e) {
			//no group available return empty list
			return ListUtil.getEmptyList();
		}

	}

	/**
	 * Returns a collection of WorkReports or an empty List
	 * @param year , the year of the reports
	 * @return A collection of WorkReports or an empty List
	 */
	public Collection getAllWorkReportsForYear(int year) {
		try {
			return getWorkReportHome().findAllWorkReportsByYearOrderedByGroupType(year);
		}
		catch (FinderException e) {
			//no report available return empty list
			return ListUtil.getEmptyList();
		}

	}

	/**
	 * Returns a collection of WorkReportGroup of the type IWMemor an empty List
	 * @param year , the year of the report
	 * @param type , the group type
	 * @return A collection of WorkReportGroup or an empty List
	 */
	public Collection getAllLeagueWorkReportGroupsForYear(int year) {
		try {
			return getWorkReportGroupHome().findAllWorkReportGroupsByGroupTypeAndYear(IWMemberConstants.GROUP_TYPE_LEAGUE, year);
		}
		catch (FinderException e) {
			//no group available return empty list
			return ListUtil.getEmptyList();
		}

	}

	/**
	 * @param headerRow, the first row of the members-part worksheet
	 */
	private Map getLeaguesMapFromRow(HSSFRow headerRow, int year, int startColumn) throws WorkReportImportException {
		Map leagues = new HashMap();
		int lastCell = headerRow.getLastCellNum();
		WorkReportGroupHome home = getWorkReportGroupHome();

		for (int j = startColumn; j < lastCell; j++) {
			HSSFCell cell = headerRow.getCell((short)j);
			if (cell != null) {
				String leagueName = cell.getStringCellValue();
				//stupid framework returns "null" as a string
				if (leagueName != null && !"".equals(leagueName)) {
					String shortName = null;
					String name = null;
					int index = leagueName.indexOf('-');
					shortName = (index != -1) ? leagueName.substring(0, index) : leagueName;
					name = (index != -1) ? leagueName.substring(index, leagueName.length()) : leagueName;

					shortName = shortName.toUpperCase();
					name = name.toUpperCase();

					WorkReportGroup group = null;

					try {
						group = getWorkReportGroupHome().findWorkReportGroupByShortNameAndYear(shortName, year);
					}
					catch (FinderException e) {
						e.printStackTrace();
						System.err.println("WorkReportGroup not found by short name : " + shortName + " trying group name");

						try {
							group = getWorkReportGroupHome().findWorkReportGroupByNameAndYear(name, year);
						}
						catch (FinderException e1) {
							throw new WorkReportImportException("workreportimportexception.league_not_found");
						}

					}

					if (group != null) {
						leagues.put(new Integer(j), group);
					}
				}

			}

		}

		return leagues;
	}

	private Collection createOrUpdateWorkReportGroupsForYearAndGroupType(int year, String groupType) {
		GroupBusiness groupBiz;
		try {
			groupBiz = getGroupBusiness();
			WorkReportGroupHome grHome = getWorkReportGroupHome();

			Collection groups = groupBiz.getGroupHome().findGroupsByType(groupType);
			Iterator groupIter = groups.iterator();
			while (groupIter.hasNext()) {
				Group group = (Group)groupIter.next();
				int groupId = ((Integer)group.getPrimaryKey()).intValue();
				WorkReportGroup wGroup = null;
				try {

					wGroup = grHome.findWorkReportGroupByGroupIdAndYear(groupId, year);
				}
				catch (FinderException e1) {
					try {
						wGroup = grHome.create();
					}
					catch (CreateException e) {
						e.printStackTrace();
					}
				}

				wGroup.setGroupId(groupId);
				wGroup.setName(group.getName());
				wGroup.setShortName(group.getMetaData(IWMemberConstants.META_DATA_CLUB_SHORT_NAME));
				wGroup.setNumber(group.getMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER));
				wGroup.setGroupType(group.getGroupType());
				wGroup.setYearOfReport(year);
				
				wGroup.store();

			}

		}
		catch (RemoteException e1) {
			e1.printStackTrace();
		}
		catch (FinderException e1) {
			// do nothing return empty list
			return ListUtil.getEmptyList();
		}

		return null;
	}

	/**
	 * Gets all the WorkReportMembers for the supplied WorkReport id
	 * @param workReportId
	 * @return a collection of WorkReportMember or an empty list
	 */
	public Collection getAllWorkReportMembersForWorkReportId(int workReportId) {
		try {
			return getWorkReportMemberHome().findAllWorkReportMembersByWorkReportIdOrderedByMemberName(workReportId);
		}
		catch (FinderException e) {
			return ListUtil.getEmptyList();
		}
	}

	/**
	 * Gets all the WorkReportMembers for the supplied WorkReport id and workreportgroup id
	 * @param workReportId
	 * @return a collection of WorkReportMembers or an empty list
	 */
	public Collection getAllWorkReportMembersForWorkReportIdAndWorkReportGroupId(int workReportId, WorkReportGroup workReportGroup) {
		try {
			return getWorkReportMemberHome().findAllWorkReportMembersByWorkReportIdAndWorkReportGroup(workReportId, workReportGroup);
		}
		catch (FinderException e) {
			return ListUtil.getEmptyList();
		}
	}

	/**
	 * Gets all the WorkReportBoardMembers for the supplied WorkReport id
	 * @param workReportId
	 * @return a collection of WorkReportBoardMember or an empty list
	 */
	public Collection getAllWorkReportBoardMembersForWorkReportId(int workReportId) {
		try {
			return getWorkReportBoardMemberHome().findAllWorkReportBoardMembersByWorkReportIdOrderedByMemberName(workReportId);
		}
		catch (FinderException e) {
			return ListUtil.getEmptyList();
		}
	}

	/**
	 * Gets all WorkReportDivisionBoard for the specified WorkReport id
	 * @param workReportId
	 * @return a collection of WorkReportDivisionBoard
	 */
	public Collection getAllWorkReportDivisionBoardForWorkReportId(int workReportId) {
		try {
			return getWorkReportDivisionBoardHome().findAllWorkReportDivisionBoardByWorkReportId(workReportId);
		}
		catch (FinderException e) {
			return ListUtil.getEmptyList();
		}
	}

	/**
	 * Gets work report group.
	 * @param workReportGroupName
	 * @param year
	 * @return the desired work report group else null
	 */
	public WorkReportGroup findWorkReportGroupByNameAndYear(String workReportGroupName, int year) {
		WorkReportGroupHome home = getWorkReportGroupHome();
		WorkReportGroup workReportGroup = null;
		if (workReportGroupName != null) {
			try {
				workReportGroup = home.findWorkReportGroupByNameAndYear(workReportGroupName, year);
			}
			catch (FinderException ex) {
				System.err.println("[WorkReportBusiness] Could not find old WorkReportGroup (name: " + workReportGroupName + " , year: " + year + " ) Message is: " + ex.getMessage());
				ex.printStackTrace(System.err);
				return null;
			}
		}
		return workReportGroup;
	}

	/**
	 * Changes the WorkReportGroup of the specified entity, that is the entity is removed from the specified current group 
	 * and added to the specified new group.
	 * If the name of the current group is null the entity will only be added to the specified new group.
	 * If the name of the new group is null the entity will only be removed from the specified current group.
	 * But if one of the groups could not be found nothing happens and false is returned.
	 * If both specified names are null, nothing happens and true is returned.
	 * If the complete operation was successful true is returned else false.
	 * @param workReportID
	 * @param nameOldGroup
	 * @param yearOldGroup
	 * @param nameNewGroup
	 * @param yearNewGroup
	 * @param entity
	 * @return true if successful else false.
	 */

	public boolean changeWorkReportGroupOfEntity(int workReportID, String nameOldGroup, int yearOldGroup, String nameNewGroup, int yearNewGroup, IDOEntity entity) {
		WorkReportGroup oldGroup = null;
		WorkReportGroup newGroup = null;
		// try to find work groups
		WorkReportGroupHome home = getWorkReportGroupHome();
		if (nameOldGroup != null) {
			try {
				oldGroup = home.findWorkReportGroupByNameAndYear(nameOldGroup, yearOldGroup);
			}
			catch (FinderException ex) {
				System.err.println("[WorkReportBusiness] Could not find old WorkReportGroup (name: " + nameOldGroup + " , year: " + yearOldGroup + " ) Message is: " + ex.getMessage());
				ex.printStackTrace(System.err);
				return false;
			}
		}
		if (nameNewGroup != null) {
			try {
				newGroup = home.findWorkReportGroupByNameAndYear(nameNewGroup, yearNewGroup);
			}
			catch (FinderException ex) {
				System.err.println("[WorkReportBusiness] Could not find new WorkReportGroup (name: " + nameNewGroup + " , year: " + yearNewGroup + " ) Message is: " + ex.getMessage());
				ex.printStackTrace(System.err);
				return false;
			}
		}
		return changeWorkReportGroupOfEntity(workReportID, oldGroup, newGroup, entity);
	}

	/**
	 * Changes the WorkReportGroup of the specified entity, that is the entity is removed from the specified current group 
	 * and added to the specified new group.
	 * If the specified current group is null the entity will only be added to the specified new group.
	 * If the specified new group is null the entity will only be removed from the specified current group.
	 * If both specified groups are null nothing happens and true is returned.
	 * If the complete operation was successful true is returned else false.
	 * @param oldGroup
	 * @param newGroup
	 * @param entity
	 * @return true if successful else false.
	 */
	public boolean changeWorkReportGroupOfEntity(int workReportID, WorkReportGroup oldGroup, WorkReportGroup newGroup, IDOEntity entity) {
		TransactionManager manager = com.idega.transaction.IdegaTransactionManager.getInstance();
		try {
			manager.begin();
			// add work report group to work report 
			if (newGroup != null) {
				Collection coll = getLeaguesOfWorkReportById(workReportID);
				Integer pk = (Integer)newGroup.getPrimaryKey();
				Iterator iteratorLeagues = coll.iterator();
				boolean doesNotExist = true;
				while (iteratorLeagues.hasNext() && doesNotExist) {
					WorkReportGroup group = (WorkReportGroup)iteratorLeagues.next();
					Integer pkGroup = (Integer)group.getPrimaryKey();
					doesNotExist = !(pk.equals(pkGroup));
				}
				if (doesNotExist) {
					WorkReport workReport = getWorkReportById(workReportID);
					workReport.addLeague(newGroup);
				}
			}
			if (oldGroup != null) {
				oldGroup.removeEntity(entity);
				oldGroup.store();
			}
			if (newGroup != null) {
				newGroup.addEntity(entity);
				newGroup.store();
			}
			manager.commit();
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
			try {
				manager.rollback();
			}
			catch (javax.transaction.SystemException sysEx) {
				sysEx.printStackTrace(System.err);
				return false;
			}
			return false;
		}
	}

	/**
	* Adds the specified WorkReportGroup to the specified entity. 
	* If the secified WorkReportGroup is null nothing happens and true is returned.
	* If the complete operation was successful true is returned else false.
	* @param workReportGroupID
	* @param newGroup
	* @param entity
	* @return true if successful else false.
	*/
	public boolean addWorkReportGroupToEntity(int workReportID, WorkReportGroup newGroup, IDOEntity entity) {
		return changeWorkReportGroupOfEntity(workReportID, null, newGroup, entity);
	}

	/**
	* Removes the specified WorkReportGroup from the specified entity. 
	* If the secified WorkReportGroup is null nothing happens and true is returned.
	* If the complete operation was successful true is returned else false.
	* @param workReportGroupID
	* @param newGroup
	* @param entity
	* @return true if successful else false.
	*/
	public boolean removeWorkReportGroupFromEntity(int workReportID, WorkReportGroup oldGroup, IDOEntity entity) {
		return changeWorkReportGroupOfEntity(workReportID, oldGroup, null, entity);
	}

	/**
	* Adds the specified WorkReportGroup to the specified entity. 
	* The entity is specified by the name and the year.
	* If the secified WorkReportGroup is null nothing happens and true is returned.
	* If the complete operation was successful true is returned else false.
	* @param workReportGRoupID
	* @param newGroup
	* @param work
	* @param year
	* @param entity
	* @return true if successful else false.
	*/
	public boolean addWorkReportGroupToEntity(int workReportID, String nameNewGroup, int year, IDOEntity entity) {
		return changeWorkReportGroupOfEntity(workReportID, null, year, nameNewGroup, year, entity);
	}

	/**
	* Removes the specified WorkReportGroup from the specified entity. 
	* The entity is specified by the name and the year.
	* If the secified WorkReportGroup is null nothing happens and true is returned.
	* If the complete operation was successful true is returned else false.
	* @param newGroup
	* @param year
	* @param entity
	* @return true if successful else false.
	*/
	public boolean removeWorkReportGroupFromEntity(int workReportID, String nameOldGroup, int year, IDOEntity entity) {
		return changeWorkReportGroupOfEntity(workReportID, nameOldGroup, year, null, year, entity);
	}

	public boolean createWorkReportData(int workReportId) {
		// get year and group id from work report
		WorkReportBoardMemberHome membHome = getWorkReportBoardMemberHome();
		WorkReport workReport = getWorkReportById(workReportId);
		// has the data already been created?
		if (workReport.isCreationFromDatabaseDone()) {
			return true;
		}
		// get the corresponding group 
		int groupId = workReport.getGroupId().intValue();
		// get group business
		GroupBusiness groupBusiness;
		try {
			groupBusiness = getGroupBusiness();
		}
		catch (RemoteException ex) {
			System.err.println("[WorkReportBusiness]: Can't retrieve GroupBusiness. Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
			throw new RuntimeException("[WorkReportBusiness]: Can't retrieve GroupBusiness.");
		}

		// do we have to create the data at all?
		boolean isLeague;
		boolean isRegionalUnion;
		try {
			Group group = groupBusiness.getGroupByGroupID(groupId);
			String groupType = group.getGroupType();
			isLeague = IWMemberConstants.GROUP_TYPE_LEAGUE.equals(groupType);
			isRegionalUnion = IWMemberConstants.GROUP_TYPE_REGIONAL_UNION.equals(groupType);
			// !! assumption: leagues and regional unions use the member system !!
			if (!(isLeague || isRegionalUnion || isClubUsingTheMemberSystem(group))) {
				// the group does not use the member system. The data has to be imported by a file.
				// returns true because this is not an error.
				return true;
			}
		}
		catch (FinderException finderException) {
			System.err.println("[WorkReportBusiness]: Can't find group. Message is: " + finderException.getMessage());
			return false;
		}
		catch (RemoteException ex) {
			System.err.println("[WorkReportBusiness]: Can't retrieve WorkReportBusiness. Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
			throw new RuntimeException("[WorkReportBusiness]: Can't retrieve WorkReportBusiness.");
		}
		// update leagues
		int year = workReport.getYearOfReport().intValue();
		createOrUpdateLeagueWorkReportGroupsForYear(year);
		//
		// start transaction
		//
		TransactionManager tm = IdegaTransactionManager.getInstance();
		try {
			tm.begin();

			// add ADA league to the work report
			WorkReportGroup adaGroup = findWorkReportGroupByNameAndYear(WorkReportConstants.MAIN_BOARD_GROUP_NAME, year);
			try {
				workReport.addLeague(adaGroup);
				workReport.store();
			}
			catch (IDORelationshipException ex) {
				String message = "[WorkReportBusiness]: Can't define realtion ship.";
				System.err.println(message + " Message is: " + ex.getMessage());
				ex.printStackTrace(System.err);
				// do nothing
			}

			boolean boardDataCreated = createWorkReportBoardDataWithoutAnyChecks(workReportId, year, groupId, groupBusiness);
			boolean memberDataCreated = (isLeague || isRegionalUnion) ? true : createWorkReportMemberDataWithoutAnyChecks(workReportId, groupId, groupBusiness);
			if (boardDataCreated && memberDataCreated) {
				// mark the sucessfull creation
				workReport.setCreationFromDatabaseDone(true);
				workReport.store();
				//tm.commit();
				tm.rollback();
				return true;
			}
			else {
				tm.rollback();
				return false;
			}
		}
		catch (Exception ex) {
			System.err.println("[WorkReportBusiness]: Couldn't create work report data. Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
			try {
				tm.rollback();
				return false;
			}
			catch (SystemException sysEx) {
				System.err.println("[WorkReportBusiness]: Couldn't rollback. Message is: " + sysEx.getMessage());
				sysEx.printStackTrace(System.err);
				return false;
			}
		}
	}

	private boolean createWorkReportBoardDataWithoutAnyChecks(int workReportId, int year, int groupId, GroupBusiness groupBusiness) {
		Map idExistingMemberMap = new HashMap();
		// find all existing work report members
		Collection existingWorkReportBoardMembers = getAllWorkReportBoardMembersForWorkReportId(workReportId);
		// create a map with user ids as keys and leagues as values
		Iterator existingWorkReportBoardMembersIterator = existingWorkReportBoardMembers.iterator();
		while (existingWorkReportBoardMembersIterator.hasNext()) {
			WorkReportBoardMember member = (WorkReportBoardMember)existingWorkReportBoardMembersIterator.next();
			Integer userId = new Integer(member.getUserId());
			Collection memberLeagues = (Collection)idExistingMemberMap.get(userId);
			if (memberLeagues == null) {
				memberLeagues = new ArrayList();
				idExistingMemberMap.put(userId, memberLeagues);
			}
			WorkReportGroup league = null;
			try {
				league = member.getLeague();
				memberLeagues.add(league);
			}
			catch (IDOException ex) {
				System.err.println("[WorkreportBusiness]: Can't retrieve league. Message is: " + ex.getMessage());
				ex.printStackTrace(System.err);
			}
		}

		// get all children of the group group (not recursively)
		Collection childGroups;
		try {
			childGroups = groupBusiness.getChildGroups(groupId);
		}
		catch (RemoteException ex) {
			System.err.println("[WorkReportBoardBusiness]: Can't get child groups. Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
			throw new RuntimeException("[WorkReportBoardBusiness]: Can't child groups.");
		}
		catch (FinderException ex) {
			System.err.println("[WorkReportBoardBusiness]: Can't get child groups. Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
			childGroups = new ArrayList(0);
		}
		Iterator iterator = childGroups.iterator();
		while (iterator.hasNext()) {
			boolean isDivision = false;
			boolean isCommittee = false;
			Group group = (Group)iterator.next();
			String groupType = group.getGroupType();
			if (IWMemberConstants.GROUP_TYPE_CLUB_COMMITTEE.equals(groupType) || IWMemberConstants.GROUP_TYPE_LEAGUE_COMMITTEE.equals(groupType) || IWMemberConstants.GROUP_TYPE_REGIONAL_UNION_COMMITTEE.equals(groupType)) {
				// go further down, we are looking for the main committee
				try {
					Collection committeeChildren = group.getChildGroups();
					Iterator committeeChildrenIterator = committeeChildren.iterator();
					while (committeeChildrenIterator.hasNext()) {
						Group child = (Group)committeeChildrenIterator.next();
						String childGroupType = child.getGroupType();
						if (IWMemberConstants.GROUP_TYPE_CLUB_COMMITTEE_MAIN.equals(childGroupType)) {
							// change the value of the external loop variable group
							group = child;
							isCommittee = true;
						}
					}
				}
				catch (EJBException ex) {
					System.err.println("[WorkReportBusiness]: Can't retrieve children of group. Message is: " + ex.getMessage());
					ex.printStackTrace(System.err);
				}
			}
			else if (IWMemberConstants.GROUP_TYPE_CLUB_DIVISION.equals(groupType)) {
				isDivision = true;
			}
			//
			// create work report bord members 
			//
			if (isDivision || isCommittee) {
				Collection users = null;
				WorkReportGroup league = null;
				// division: 
				// fetch league 
				// and get users from that group (group type: division board)
				// that is referenced by the current group
				if (isDivision) {
					// get league
					league = getLeagueFromClubDivision(group, year);
					// get users
					users = getBoardUsersFromClubDivision(group, groupBusiness);
				}
				// committee:
				// there is no league.
				// get users directly.
				else {
					users = getBoardUsersFromCommittee(group, groupBusiness);
				}
				if (users != null) {
					// note: the following method adds the new created members to the idExistingMemberMap
					createWorkReportBoardMembers(users, workReportId, league, idExistingMemberMap);
				}
				//
				// create division boards
				//
				if (isDivision) {
					try {
						createWorkReportDivisionBoard(workReportId, group, league);
					}
					catch (CreateException ex) {
						System.err.println("[WorkreportBusiness] WorkReportDivisionBoard could not be created. Message is: " + ex.getMessage());
						ex.printStackTrace(System.err);
					}
				}
			}
		}
		try {
			updateWorkReportData(workReportId);
		}
		catch (Exception ex) {
			String message = "[WorkReportBusiness]: Can't update work report data.";
			System.err.println(message + " Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
		}
		return true;
	}

	private void updateWorkReportData(int workReportId) throws FinderException, IDOException {
		Collection members;
		members = getAllWorkReportMembersForWorkReportId(workReportId);
		// create map: member as key, leagues as value 
		Map leagueCountMap = new HashMap();
		int playersCount = 0;
		int membersTotalSum = members.size();
		Iterator membersIterator = members.iterator();
		while (membersIterator.hasNext()) {
			WorkReportMember member = (WorkReportMember)membersIterator.next();
			try {
				Iterator leagues = member.getLeaguesForMember().iterator();
				List leaguesList = new ArrayList();
				// if there is at least one league the member is a player
				if (leagues.hasNext()) {
					playersCount++;
				}
				while (leagues.hasNext()) {
					WorkReportGroup league = (WorkReportGroup)leagues.next();
					String leagueName = league.getName();
					leaguesList.add(leagueName);
					Integer count = (Integer)leagueCountMap.get(leagueName);
					count = (count == null) ? null : new Integer((count.intValue()) + 1);
					leagueCountMap.put(leagueName, count);
				}
			}
			catch (IDOException ex) {
				System.err.println("[WorkReportMemberEditor] Can't get leagues. Message is: " + ex.getMessage());
				ex.printStackTrace(System.err);
			}
		}
		WorkReport workReport = getWorkReportById(workReportId);

		workReport.setNumberOfMembers(membersTotalSum);
		workReport.setNumberOfPlayers(playersCount);
		workReport.store();

		WorkReportDivisionBoardHome home = getWorkReportDivisionBoardHome();
		Collection boards = home.findAllWorkReportDivisionBoardByWorkReportId(workReportId);

		Iterator iterator = boards.iterator();
		while (iterator.hasNext()) {
			WorkReportDivisionBoard board = (WorkReportDivisionBoard)iterator.next();
			WorkReportGroup workReportGroup = board.getLeague();
			String leagueName = workReportGroup.getName();
			Integer number = (Integer)leagueCountMap.get(leagueName);
			if (number == null) {
				board.setNumberOfPlayers(0);
			}
			else {
				board.setNumberOfPlayers(number.intValue());
			}
			board.store();
		}
	}

	private Collection getBoardUsersFromCommittee(Group committee, GroupBusiness groupBusiness) {
		try {
			return groupBusiness.getUsers(committee);
		}
		catch (FinderException ex) {
			System.err.println("[WorkreportBusiness] Users could not be found. Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
			return null;
		}
		catch (RemoteException ex) {
			System.err.println("[WorkReportBusiness]: Can't retrieve users. Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
			throw new RuntimeException("[WorkReportBusiness]: Can't retrieve users.");
		}
	}

	private WorkReportGroup getLeagueFromClubDivision(Group clubDivision, int year) {
		WorkReportGroup league = null;
		String leagueIdAsString = clubDivision.getMetaData(IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION);
		if (leagueIdAsString != null) {
			try {
				Integer leagueId = new Integer(leagueIdAsString);
				league = getWorkReportGroupHome().findWorkReportGroupByGroupIdAndYear(leagueId.intValue(), year);
			}
			catch (NumberFormatException formatEx) {
				System.err.println("[workReportBusiness] league id ( " + leagueIdAsString + " ) is not a number. Message is: " + formatEx.getMessage());
				formatEx.printStackTrace(System.err);
			}
			catch (FinderException ex) {
				System.err.println("[WorkreportBusiness] league with id " + leagueIdAsString + " could not be found. Message is: " + ex.getMessage());
				ex.printStackTrace(System.err);
			}

		}
		return league;
	}

	private Collection getBoardUsersFromClubDivision(Group clubDivision, GroupBusiness groupBusiness) {
		Collection users = null;
		String divisionBoardIdAsString = clubDivision.getMetaData(IWMemberConstants.META_DATA_DIVISION_BOARD);
		if (divisionBoardIdAsString != null) {
			try {
				Integer divisionBoardId = new Integer(divisionBoardIdAsString);
				Group divisionBoard = getGroupHome().findByPrimaryKey(divisionBoardId);
				users = groupBusiness.getUsers(divisionBoard);
			}
			catch (NumberFormatException formatException) {
				System.err.println("[workReportBusiness] division board id ( " + divisionBoardIdAsString + " ) is not a number. Message is: " + formatException.getMessage());
				formatException.printStackTrace(System.err);
			}
			catch (FinderException ex) {
				System.err.println("[WorkreportBusiness] Group with id " + divisionBoardIdAsString + " could not be found. Message is: " + ex.getMessage());
				ex.printStackTrace(System.err);
			}
			catch (RemoteException ex) {
				System.err.println("[WorkReportBusiness]: Can't retrieve users. Message is: " + ex.getMessage());
				ex.printStackTrace(System.err);
				throw new RuntimeException("[WorkReportBusiness]: Can't retrieve users.");
			}
		}
		return users;
	}

	private boolean createWorkReportBoardMembers(Collection users, int workReportId, WorkReportGroup league, Map idExistingMemberMap) {
		if (users == null) {
			return false;
		}
		Iterator userIterator = users.iterator();
		while (userIterator.hasNext()) {
			User user = (User)userIterator.next();
			Integer primaryKeyUser = (Integer)user.getPrimaryKey();
			// create a member per league (that is one user can have one or many members)
			Collection memberLeagues = (Collection)idExistingMemberMap.get(primaryKeyUser);
			// note: league can be null
			if (memberLeagues != null && (memberLeagues.contains(league))) {
				// nothing to do
				return true;
			}
			try {
				// create WorkReportBoardMember
				WorkReportBoardMember member = createWorkReportBoardMember(workReportId, user, league);
				// add the new one to the existing ones
				if (memberLeagues == null) {
					memberLeagues = new ArrayList();
					memberLeagues.add(league);
					idExistingMemberMap.put(primaryKeyUser, memberLeagues);
				}
				else {
					memberLeagues.add(league);
				}
			}
			catch (CreateException createEx) {
				System.err.println("[WorkReportBusiness] Couldn't create WorkreportBoardMember. Message is: " + createEx.getMessage());
				createEx.printStackTrace(System.err);
			}
		}
		return true;
	}

	private boolean createWorkReportMemberDataWithoutAnyChecks(int workReportId, int groupId, GroupBusiness groupBusiness) throws RemoteException {
		Map idExistingMember = new HashMap();
		// find all existing work report members
		Collection existingWorkReportMembers = getAllWorkReportMembersForWorkReportId(workReportId);
		// create a collection with user ids
		Iterator existingWorkReportMembersIterator = existingWorkReportMembers.iterator();
		while (existingWorkReportMembersIterator.hasNext()) {
			WorkReportMember workReportMember = (WorkReportMember)existingWorkReportMembersIterator.next();
			Integer userId = new Integer(workReportMember.getUserId());
			idExistingMember.put(userId, workReportMember);
		}
		// get the year of the work report
		int year = getWorkReportById(workReportId).getYearOfReport().intValue();
		// get the ADA work report group 
		WorkReportGroup adaGroup = findWorkReportGroupByNameAndYear(WorkReportConstants.MAIN_BOARD_GROUP_NAME, year);

		// get the first level under the club
		Collection childGroups;
		try {
			childGroups = groupBusiness.getChildGroups(groupId);
		}
		catch (FinderException ex) {
			System.err.println("[WorkReportBoardBusiness]: Can't get child groups. Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
			childGroups = new ArrayList(0);
		}
		// iterate over the first level
		Iterator childGroupsFirstLevelIterator = childGroups.iterator();
		while (childGroupsFirstLevelIterator.hasNext()) {
			Group childGroup = (Group)childGroupsFirstLevelIterator.next();
			WorkReportGroup workReportGroup = null;
			String groupType = childGroup.getGroupType();
			if (IWMemberConstants.GROUP_TYPE_CLUB_DIVISION.equals(groupType)) {
				workReportGroup = getLeagueFromClubDivision(childGroup, year);
			}
			Collection users;
			// iterate over all children
			users = groupBusiness.getUsersFromGroupRecursive(childGroup);
			Iterator userIterator = users.iterator();
			while (userIterator.hasNext()) {
				User user = (User)userIterator.next();
				Integer userPrimaryKey = (Integer)user.getPrimaryKey();
				WorkReportMember existingMember = (WorkReportMember)idExistingMember.get(userPrimaryKey);
				if (existingMember == null) {
					try {
						existingMember = createWorkReportMember(workReportId, userPrimaryKey);
						// add ADA league to member
						addWorkReportGroupToEntity(workReportId, adaGroup, existingMember);
						idExistingMember.put(userPrimaryKey, existingMember);
					}
					catch (CreateException ex) {
						System.err.println("[WorkReportBusiness]: Can't create member. Message is: " + ex.getMessage());
						ex.printStackTrace(System.err);
					}
				}
				// add league to member 
				if (workReportGroup != null) {
					addWorkReportGroupToEntity(workReportId, workReportGroup, existingMember);
				}
			}
		}
		return true;
	}

	public boolean isWorkReportReadOnly(int workReportId) {
		WorkReport report = getWorkReportById(workReportId);
		return report.isSent();
	}

	//=======
	//
	//	/**
	//	 * Gets all WorkReportDivisionBoard for the specified WorkReport id
	//	 * @param workReportId
	//	 * @return a collection of WorkReportDivisionBoard
	//	 */
	//	public Collection getAllWorkReportDivisionBoardForWorkReportId(int workReportId) {
	//		try {
	//			return getWorkReportDivisionBoardHome().findAllWorkReportDivisionBoardByWorkReportId(workReportId);
	//		}
	//		catch (FinderException e) {
	//			return ListUtil.getEmptyList();
	//		}
	//	}
	//
	//	/**
	//	 * Gets work report group.
	//	 * @param workReportGroupName
	//	 * @param year
	//	 * @return the desired work report group else null
	//	 */
	//	public WorkReportGroup findWorkReportGroupByNameAndYear(String workReportGroupName, int year) {
	//		WorkReportGroupHome home = getWorkReportGroupHome();
	//		WorkReportGroup workReportGroup = null;
	//		if (workReportGroupName != null) {
	//			try {
	//				workReportGroup = home.findWorkReportGroupByNameAndYear(workReportGroupName, year);
	//			}
	//			catch (FinderException ex) {
	//				System.err.println("[WorkReportBusiness] Could not find old WorkReportGroup (name: " + workReportGroupName + " , year: " + year + " ) Message is: " + ex.getMessage());
	//				ex.printStackTrace(System.err);
	//				return null;
	//			}
	//		}
	//		return workReportGroup;
	//	}
	//
	//	/**
	//	 * Changes the WorkReportGroup of the specified entity, that is the entity is removed from the specified current group 
	//	 * and added to the specified new group.
	//	 * If the name of the current group is null the entity will only be added to the specified new group.
	//	 * If the name of the new group is null the entity will only be removed from the specified current group.
	//	 * But if one of the groups could not be found nothing happens and false is returned.
	//	 * If both specified names are null, nothing happens and true is returned.
	//	 * If the complete operation was successful true is returned else false.
	//	 * @param workReportID
	//	 * @param nameOldGroup
	//	 * @param yearOldGroup
	//	 * @param nameNewGroup
	//	 * @param yearNewGroup
	//	 * @param entity
	//	 * @return true if successful else false.
	//	 */
	//
	//	public boolean changeWorkReportGroupOfEntity(int workReportID, String nameOldGroup, int yearOldGroup, String nameNewGroup, int yearNewGroup, IDOEntity entity) {
	//		WorkReportGroup oldGroup = null;
	//		WorkReportGroup newGroup = null;
	//		// try to find work groups
	//		WorkReportGroupHome home = getWorkReportGroupHome();
	//		if (nameOldGroup != null) {
	//			try {
	//				oldGroup = home.findWorkReportGroupByNameAndYear(nameOldGroup, yearOldGroup);
	//			}
	//			catch (FinderException ex) {
	//				System.err.println("[WorkReportBusiness] Could not find old WorkReportGroup (name: " + nameOldGroup + " , year: " + yearOldGroup + " ) Message is: " + ex.getMessage());
	//				ex.printStackTrace(System.err);
	//				return false;
	//			}
	//		}
	//		if (nameNewGroup != null) {
	//			try {
	//				newGroup = home.findWorkReportGroupByNameAndYear(nameNewGroup, yearNewGroup);
	//			}
	//			catch (FinderException ex) {
	//				System.err.println("[WorkReportBusiness] Could not find new WorkReportGroup (name: " + nameNewGroup + " , year: " + yearNewGroup + " ) Message is: " + ex.getMessage());
	//				ex.printStackTrace(System.err);
	//				return false;
	//			}
	//		}
	//		return changeWorkReportGroupOfEntity(workReportID, oldGroup, newGroup, entity);
	//	}
	//
	//	/**
	//	 * Changes the WorkReportGroup of the specified entity, that is the entity is removed from the specified current group 
	//	 * and added to the specified new group.
	//	 * If the specified current group is null the entity will only be added to the specified new group.
	//	 * If the specified new group is null the entity will only be removed from the specified current group.
	//	 * If both specified groups are null nothing happens and true is returned.
	//	 * If the complete operation was successful true is returned else false.
	//	 * @param oldGroup
	//	 * @param newGroup
	//	 * @param entity
	//	 * @return true if successful else false.
	//	 */
	//	public boolean changeWorkReportGroupOfEntity(int workReportID, WorkReportGroup oldGroup, WorkReportGroup newGroup, IDOEntity entity) {
	//		TransactionManager manager = com.idega.transaction.IdegaTransactionManager.getInstance();
	//		try {
	//			manager.begin();
	//			// add work report group to work report 
	//			if (newGroup != null) {
	//				Collection coll = getLeaguesOfWorkReportById(workReportID);
	//				Integer pk = (Integer)newGroup.getPrimaryKey();
	//				Iterator iteratorLeagues = coll.iterator();
	//				boolean doesNotExist = true;
	//				while (iteratorLeagues.hasNext() && doesNotExist) {
	//					WorkReportGroup group = (WorkReportGroup)iteratorLeagues.next();
	//					Integer pkGroup = (Integer)group.getPrimaryKey();
	//					doesNotExist = !(pk.equals(pkGroup));
	//				}
	//				if (doesNotExist) {
	//					WorkReport workReport = getWorkReportById(workReportID);
	//					workReport.addLeague(newGroup);
	//				}
	//			}
	//			if (oldGroup != null) {
	//				oldGroup.removeEntity(entity);
	//				oldGroup.store();
	//			}
	//			if (newGroup != null) {
	//				newGroup.addEntity(entity);
	//				newGroup.store();
	//			}
	//			manager.commit();
	//			return true;
	//		}
	//		catch (Exception ex) {
	//			ex.printStackTrace(System.err);
	//			try {
	//				manager.rollback();
	//			}
	//			catch (javax.transaction.SystemException sysEx) {
	//				sysEx.printStackTrace(System.err);
	//				return false;
	//			}
	//			return false;
	//		}
	//	}
	//
	//	/**
	//	* Adds the specified WorkReportGroup to the specified entity. 
	//	* If the secified WorkReportGroup is null nothing happens and true is returned.
	//	* If the complete operation was successful true is returned else false.
	//	* @param workReportGroupID
	//	* @param newGroup
	//	* @param entity
	//	* @return true if successful else false.
	//	*/
	//	public boolean addWorkReportGroupToEntity(int workReportID, WorkReportGroup newGroup, IDOEntity entity) {
	//		return changeWorkReportGroupOfEntity(workReportID, null, newGroup, entity);
	//	}
	//
	//	/**
	//	* Removes the specified WorkReportGroup from the specified entity. 
	//	* If the secified WorkReportGroup is null nothing happens and true is returned.
	//	* If the complete operation was successful true is returned else false.
	//	* @param workReportGroupID
	//	* @param newGroup
	//	* @param entity
	//	* @return true if successful else false.
	//	*/
	//	public boolean removeWorkReportGroupFromEntity(int workReportID, WorkReportGroup oldGroup, IDOEntity entity) {
	//		return changeWorkReportGroupOfEntity(workReportID, oldGroup, null, entity);
	//	}
	//
	//	/**
	//	* Adds the specified WorkReportGroup to the specified entity. 
	//	* The entity is specified by the name and the year.
	//	* If the secified WorkReportGroup is null nothing happens and true is returned.
	//	* If the complete operation was successful true is returned else false.
	//	* @param workReportGRoupID
	//	* @param newGroup
	//	* @param work
	//	* @param year
	//	* @param entity
	//	* @return true if successful else false.
	//	*/
	//	public boolean addWorkReportGroupToEntity(int workReportID, String nameNewGroup, int year, IDOEntity entity) {
	//		return changeWorkReportGroupOfEntity(workReportID, null, year, nameNewGroup, year, entity);
	//	}
	//
	//	/**
	//	* Removes the specified WorkReportGroup from the specified entity. 
	//	* The entity is specified by the name and the year.
	//	* If the secified WorkReportGroup is null nothing happens and true is returned.
	//	* If the complete operation was successful true is returned else false.
	//	* @param newGroup
	//	* @param year
	//	* @param entity
	//	* @return true if successful else false.
	//	*/
	//	public boolean removeWorkReportGroupFromEntity(int workReportID, String nameOldGroup, int year, IDOEntity entity) {
	//		return changeWorkReportGroupOfEntity(workReportID, nameOldGroup, year, null, year, entity);
	//	}
	//
	//	public boolean createWorkReportData(int workReportId) {
	//		// get year and group id from work report
	//		WorkReportBoardMemberHome membHome = getWorkReportBoardMemberHome();
	//		WorkReport workReport = getWorkReportById(workReportId);
	//		// has the data already been created?
	//		if (workReport.isCreationFromDatabaseDone()) {
	//			return true;
	//		}
	//		// get the corresponding group 
	//		int groupId = workReport.getGroupId().intValue();
	//		// get group business
	//		GroupBusiness groupBusiness;
	//		try {
	//			groupBusiness = getGroupBusiness();
	//		}
	//		catch (RemoteException ex) {
	//			System.err.println("[WorkReportBusiness]: Can't retrieve GroupBusiness. Message is: " + ex.getMessage());
	//			ex.printStackTrace(System.err);
	//			throw new RuntimeException("[WorkReportBusiness]: Can't retrieve GroupBusiness.");
	//		}
	//
	//		// do we have to create the data at all?
	//		boolean isLeague;
	//		boolean isRegionalUnion;
	//		try {
	//			Group group = groupBusiness.getGroupByGroupID(groupId);
	//			String groupType = group.getGroupType();
	//			isLeague = IWMemberConstants.GROUP_TYPE_LEAGUE.equals(groupType);
	//			isRegionalUnion = IWMemberConstants.GROUP_TYPE_REGIONAL_UNION.equals(groupType);
	//			// !! assumption: leagues and regional unions use the member system !!
	//			if (!(isLeague || isRegionalUnion || isClubUsingTheMemberSystem(group))) {
	//				// the group does not use the member system. The data has to be imported by a file.
	//				// returns true because this is not an error.
	//				return true;
	//			}
	//		}
	//		catch (FinderException finderException) {
	//			System.err.println("[WorkReportBusiness]: Can't find group. Message is: " + finderException.getMessage());
	//			return false;
	//		}
	//		catch (RemoteException ex) {
	//			System.err.println("[WorkReportBusiness]: Can't retrieve WorkReportBusiness. Message is: " + ex.getMessage());
	//			ex.printStackTrace(System.err);
	//			throw new RuntimeException("[WorkReportBusiness]: Can't retrieve WorkReportBusiness.");
	//		}
	//		// update leagues
	//		int year = workReport.getYearOfReport().intValue();
	//		createOrUpdateLeagueWorkReportGroupsForYear(year);
	//		//
	//		// start transaction
	//		//
	//		TransactionManager tm = IdegaTransactionManager.getInstance();
	//		try {
	//			tm.begin();
	//			boolean boardDataCreated = createWorkReportBoardDataWithoutAnyChecks(workReportId, year, groupId, groupBusiness);
	//			boolean memberDataCreated = (isLeague || isRegionalUnion) ? true : createWorkReportMemberDataWithoutAnyChecks(workReportId, groupId, groupBusiness);
	//			if (boardDataCreated && memberDataCreated) {
	//				// mark the sucessfull creation
	//				workReport.setCreationFromDatabaseDone(true);
	//				workReport.store();
	//				tm.commit();
	//				return true;
	//			}
	//			else {
	//				tm.rollback();
	//				return false;
	//			}
	//		}
	//		catch (Exception ex) {
	//			System.err.println("[WorkReportBusiness]: Couldn't create work report data. Message is: " + ex.getMessage());
	//			ex.printStackTrace(System.err);
	//			try {
	//				tm.rollback();
	//				return false;
	//			}
	//			catch (SystemException sysEx) {
	//				System.err.println("[WorkReportBusiness]: Couldn't rollback. Message is: " + sysEx.getMessage());
	//				sysEx.printStackTrace(System.err);
	//				return false;
	//			}
	//		}
	//	}
	//
	//	private boolean createWorkReportBoardDataWithoutAnyChecks(int workReportId, int year, int groupId, GroupBusiness groupBusiness) {
	//		Map idExistingMemberMap = new HashMap();
	//		// find all existing work report members
	//		Collection existingWorkReportBoardMembers = getAllWorkReportBoardMembersForWorkReportId(workReportId);
	//		// create a map with user ids as keys and leagues as values
	//		Iterator existingWorkReportBoardMembersIterator = existingWorkReportBoardMembers.iterator();
	//		while (existingWorkReportBoardMembersIterator.hasNext()) {
	//			WorkReportBoardMember member = (WorkReportBoardMember)existingWorkReportBoardMembersIterator.next();
	//			Integer userId = new Integer(member.getUserId());
	//			Collection memberLeagues = (Collection)idExistingMemberMap.get(userId);
	//			if (memberLeagues == null) {
	//				memberLeagues = new ArrayList();
	//				idExistingMemberMap.put(userId, memberLeagues);
	//			}
	//			WorkReportGroup league = null;
	//			try {
	//				league = member.getLeague();
	//				memberLeagues.add(league);
	//			}
	//			catch (IDOException ex) {
	//				System.err.println("[WorkreportBusiness]: Can't retrieve league. Message is: " + ex.getMessage());
	//				ex.printStackTrace(System.err);
	//			}
	//		}
	//
	//		// get all children of the group group (not recursively)
	//		Collection childGroups;
	//		try {
	//			childGroups = groupBusiness.getChildGroups(groupId);
	//		}
	//		catch (RemoteException ex) {
	//			System.err.println("[WorkReportBoardBusiness]: Can't get child groups. Message is: " + ex.getMessage());
	//			ex.printStackTrace(System.err);
	//			throw new RuntimeException("[WorkReportBoardBusiness]: Can't child groups.");
	//		}
	//		catch (FinderException ex) {
	//			System.err.println("[WorkReportBoardBusiness]: Can't get child groups. Message is: " + ex.getMessage());
	//			ex.printStackTrace(System.err);
	//			childGroups = new ArrayList(0);
	//		}
	//		Iterator iterator = childGroups.iterator();
	//		while (iterator.hasNext()) {
	//			boolean isDivision = false;
	//			boolean isCommittee = false;
	//			Group group = (Group)iterator.next();
	//			String groupType = group.getGroupType();
	//			if (IWMemberConstants.GROUP_TYPE_CLUB_COMMITTEE.equals(groupType) || IWMemberConstants.GROUP_TYPE_LEAGUE_COMMITTEE.equals(groupType) || IWMemberConstants.GROUP_TYPE_REGIONAL_UNION_COMMITTEE.equals(groupType)) {
	//				// go further down, we are looking for the main committee
	//				try {
	//					Collection committeeChildren = group.getChildGroups();
	//					Iterator committeeChildrenIterator = committeeChildren.iterator();
	//					while (committeeChildrenIterator.hasNext()) {
	//						Group child = (Group)committeeChildrenIterator.next();
	//						String childGroupType = child.getGroupType();
	//						if (IWMemberConstants.GROUP_TYPE_CLUB_COMMITTEE_MAIN.equals(childGroupType)) {
	//							// change the value of the external loop variable group
	//							group = child;
	//							isCommittee = true;
	//						}
	//					}
	//				}
	//				catch (EJBException ex) {
	//					System.err.println("[WorkReportBusiness]: Can't retrieve children of group. Message is: " + ex.getMessage());
	//					ex.printStackTrace(System.err);
	//				}
	//			}
	//			else if (IWMemberConstants.GROUP_TYPE_CLUB_DIVISION.equals(groupType)) {
	//				isDivision = true;
	//			}
	//			//
	//			// create work report bord members 
	//			//
	//			if (isDivision || isCommittee) {
	//				Collection users = null;
	//				WorkReportGroup league = null;
	//				// division: 
	//				// fetch league 
	//				// and get users from that group (group type: division board)
	//				// that is referenced by the current group
	//				if (isDivision) {
	//					// get league
	//					league = getLeagueFromClubDivision(group, year);
	//					// get users
	//					users = getBoardUsersFromClubDivision(group, groupBusiness);
	//				}
	//				// committee:
	//				// there is no league.
	//				// get users directly.
	//				else {
	//					users = getBoardUsersFromCommittee(group, groupBusiness);
	//				}
	//				if (users != null) {
	//					// note: the following method adds the new created members to the idExistingMemberMap
	//					createWorkReportBoardMembers(users, workReportId, league, idExistingMemberMap);
	//				}
	//				//
	//				// create division boards
	//				//
	//				if (isDivision) {
	//					try {
	//						createWorkReportDivisionBoard(workReportId, group, league);
	//					}
	//					catch (CreateException ex) {
	//						System.err.println("[WorkreportBusiness] WorkReportDivisionBoard could not be created. Message is: " + ex.getMessage());
	//						ex.printStackTrace(System.err);
	//					}
	//				}
	//			}
	//		}
	//		return true;
	//	}
	//
	//	private Collection getBoardUsersFromCommittee(Group committee, GroupBusiness groupBusiness) {
	//		try {
	//			return groupBusiness.getUsers(committee);
	//		}
	//		catch (FinderException ex) {
	//			System.err.println("[WorkreportBusiness] Users could not be found. Message is: " + ex.getMessage());
	//			ex.printStackTrace(System.err);
	//			return null;
	//		}
	//		catch (RemoteException ex) {
	//			System.err.println("[WorkReportBusiness]: Can't retrieve users. Message is: " + ex.getMessage());
	//			ex.printStackTrace(System.err);
	//			throw new RuntimeException("[WorkReportBusiness]: Can't retrieve users.");
	//		}
	//	}
	//
	//	private WorkReportGroup getLeagueFromClubDivision(Group clubDivision, int year) {
	//		WorkReportGroup league = null;
	//		String leagueIdAsString = clubDivision.getMetaData(IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION);
	//		if (leagueIdAsString != null) {
	//			try {
	//				Integer leagueId = new Integer(leagueIdAsString);
	//				league = getWorkReportGroupHome().findWorkReportGroupByGroupIdAndYear(leagueId.intValue(), year);
	//			}
	//			catch (NumberFormatException formatEx) {
	//				System.err.println("[workReportBusiness] league id ( " + leagueIdAsString + " ) is not a number. Message is: " + formatEx.getMessage());
	//				formatEx.printStackTrace(System.err);
	//			}
	//			catch (FinderException ex) {
	//				System.err.println("[WorkreportBusiness] league with id " + leagueIdAsString + " could not be found. Message is: " + ex.getMessage());
	//				ex.printStackTrace(System.err);
	//			}
	//
	//		}
	//		return league;
	//	}
	//
	//	private Collection getBoardUsersFromClubDivision(Group clubDivision, GroupBusiness groupBusiness) {
	//		Collection users = null;
	//		String divisionBoardIdAsString = clubDivision.getMetaData(IWMemberConstants.META_DATA_DIVISION_BOARD);
	//		if (divisionBoardIdAsString != null) {
	//			try {
	//				Integer divisionBoardId = new Integer(divisionBoardIdAsString);
	//				Group divisionBoard = getGroupHome().findByPrimaryKey(divisionBoardId);
	//				users = groupBusiness.getUsers(divisionBoard);
	//			}
	//			catch (NumberFormatException formatException) {
	//				System.err.println("[workReportBusiness] division board id ( " + divisionBoardIdAsString + " ) is not a number. Message is: " + formatException.getMessage());
	//				formatException.printStackTrace(System.err);
	//			}
	//			catch (FinderException ex) {
	//				System.err.println("[WorkreportBusiness] Group with id " + divisionBoardIdAsString + " could not be found. Message is: " + ex.getMessage());
	//				ex.printStackTrace(System.err);
	//			}
	//			catch (RemoteException ex) {
	//				System.err.println("[WorkReportBusiness]: Can't retrieve users. Message is: " + ex.getMessage());
	//				ex.printStackTrace(System.err);
	//				throw new RuntimeException("[WorkReportBusiness]: Can't retrieve users.");
	//			}
	//		}
	//		return users;
	//	}
	//
	//	private boolean createWorkReportBoardMembers(Collection users, int workReportId, WorkReportGroup league, Map idExistingMemberMap) {
	//		if (users == null) {
	//			return false;
	//		}
	//		Iterator userIterator = users.iterator();
	//		while (userIterator.hasNext()) {
	//			User user = (User)userIterator.next();
	//			Integer primaryKeyUser = (Integer)user.getPrimaryKey();
	//			// create a member per league (that is one user can have one or many members)
	//			Collection memberLeagues = (Collection)idExistingMemberMap.get(primaryKeyUser);
	//			// note: league can be null
	//			if (memberLeagues != null && (memberLeagues.contains(league))) {
	//				// nothing to do
	//				return true;
	//			}
	//			try {
	//				// create WorkReportBoardMember
	//				WorkReportBoardMember member = createWorkReportBoardMember(workReportId, user, league);
	//				// add the new one to the existing ones
	//				if (memberLeagues == null) {
	//					memberLeagues = new ArrayList();
	//					memberLeagues.add(league);
	//					idExistingMemberMap.put(primaryKeyUser, memberLeagues);
	//				}
	//				else {
	//					memberLeagues.add(league);
	//				}
	//			}
	//			catch (CreateException createEx) {
	//				System.err.println("[WorkReportBusiness] Couldn't create WorkreportBoardMember. Message is: " + createEx.getMessage());
	//				createEx.printStackTrace(System.err);
	//			}
	//		}
	//		return true;
	//	}
	//
	//	private boolean createWorkReportMemberDataWithoutAnyChecks(int workReportId, int groupId, GroupBusiness groupBusiness) {
	//		Collection idExistingMember = new ArrayList();
	//		// find all existing work report members
	//		Collection existingWorkReportMembers = getAllWorkReportMembersForWorkReportId(workReportId);
	//		// create a collection with user ids
	//		Iterator existingWorkReportMembersIterator = existingWorkReportMembers.iterator();
	//		while (existingWorkReportMembersIterator.hasNext()) {
	//			WorkReportMember workReportMember = (WorkReportMember)existingWorkReportMembersIterator.next();
	//			Integer userId = new Integer(workReportMember.getUserId());
	//			idExistingMember.add(userId);
	//		}
	//		Collection childGroups;
	//		try {
	//			String userGroupRepresentive = groupBusiness.getUserGroupRepresentativeHome().getGroupType();
	//			ArrayList groupTypes = new ArrayList();
	//			groupTypes.add(userGroupRepresentive);
	//			childGroups = groupBusiness.getChildGroupsRecursiveResultFiltered(groupId, groupTypes, false);
	//		}
	//		catch (RemoteException ex) {
	//			System.err.println("[WorkReportBoardBusiness]: Can't get child groups. Message is: " + ex.getMessage());
	//			ex.printStackTrace(System.err);
	//			throw new RuntimeException("[WorkReportBoardBusiness]: Can't child groups.");
	//		}
	//		Iterator childGroupsIterator = childGroups.iterator();
	//		while (childGroupsIterator.hasNext()) {
	//			Group child = (Group)childGroupsIterator.next();
	//			Integer primaryKey = (Integer)child.getPrimaryKey();
	//			if (!idExistingMember.contains(primaryKey)) {
	//				try {
	//					createWorkReportMember(workReportId, primaryKey);
	//					idExistingMember.add(primaryKey);
	//				}
	//				catch (CreateException ex) {
	//					System.err.println("[WorkReportBusiness]: Can't create member. Message is: " + ex.getMessage());
	//					ex.printStackTrace(System.err);
	//				}
	//			}
	//		}
	//		return true;
	//	}
	//
	//	public boolean isWorkReportReadOnly(int workReportId) {
	//		WorkReport report = getWorkReportById(workReportId);
	//		return report.isSent();
	//	}
	//>>>>>>> 1.70

	public boolean sendWorkReport(int workReportId, String reportText, IWResourceBundle iwrb) throws RemoteException {

		WorkReport report = getWorkReportById(workReportId);
		report.setAsSent(true);
		report.setSentReportText(reportText);

		//if some is done set status to partial else if it is not set yet to not done
		if (report.isMembersPartDone() || report.isAccountPartDone() || report.isBoardPartDone()) {
			report.setStatus(WorkReportConstants.WR_STATUS_SOME_DONE);
		}
		else {
			report.setStatus(WorkReportConstants.WR_STATUS_NOT_DONE);
		}

		report.store();

		String subject = iwrb.getLocalizedString("work_report_send.email_subject", "IWMember workreport sent announcement");
		String body = report.getGroupName() + "\n";
		body += iwrb.getLocalizedString("work_report_send.email_body", "has just sent a workreport.\n\n");
		body += iwrb.getLocalizedString("work_report_send.email_body_comments", "Comments: \n");
		body += reportText;

		//send email to regional union
		try {
			Integer regUniId = report.getRegionalUnionGroupId();
			if (regUniId != null) {

				Group regionalUnion = this.getGroupBusiness().getGroupByGroupID(regUniId.intValue());

				Collection toRegionalEmails = regionalUnion.getEmails();
				String toEmailAddress = null;
				if (toRegionalEmails != null && !toRegionalEmails.isEmpty()) {
					toEmailAddress = ((Email)toRegionalEmails.iterator().next()).getEmailAddress();
					try {
						sendEmailFromIWMemberSystemAdministrator(toEmailAddress, null, null, subject, body);
					}
					catch (MessagingException e) {
						e.printStackTrace();
					}
				}
			}
		}
		catch (FinderException e) {
			//either not found or this is a regional union / league
			e.printStackTrace();
		}

		//send email to federation
		Group federation;
		try {
			Integer groupID = report.getGroupId();

			if (groupID != null) {

				federation = getFederationGroupForClubGroup(getGroupBusiness().getGroupByGroupID(groupID.intValue()));

				Collection fedEmails = federation.getEmails();

				if (fedEmails != null && !fedEmails.isEmpty()) {
					String toEmailAddress = ((Email)fedEmails.iterator().next()).getEmailAddress();
					try {
						sendEmailFromIWMemberSystemAdministrator(toEmailAddress, null, null, subject, body);
					}
					catch (MessagingException e) {
						e.printStackTrace();
					}
				}
			}

		}
		catch (NoFederationFoundException e1) {
			e1.printStackTrace();
		}
		catch (FinderException e1) {
			e1.printStackTrace();
		}

		return true;
	}

	public boolean closeWorkReport(int workReportId) {
		WorkReport report = getWorkReportById(workReportId);
		report.setAsSent(true);

		report.store();

		return true;
	}

	public String getWorkReportSentText(int workReportId) {
		return getWorkReportById(workReportId).getSentReportText();
	}

	public boolean unSendWorkReport(int workReportId) {
		WorkReport report = getWorkReportById(workReportId);
		report.setAsSent(false);

		report.store();

		return true;
	}

	public boolean isThereAYearlyAccountForAnEmptyDivision(int workReportId) {
		WorkReportClubAccountRecordHome recHome = getWorkReportClubAccountRecordHome();
		Collection records = null;
		Collection leagues = null;

		try {
			records = recHome.findAllRecordsByWorkReportId(workReportId);
		}
		catch (FinderException e1) {
			System.out.println("No account records for work report id : " + workReportId);
			return false; //no records
		}

		WorkReport report = this.getWorkReportById(workReportId);

		try {
			leagues = report.getLeagues();
		}
		catch (IDOException e) {
			System.out.println("No divisions for work report id : " + workReportId);
			return false; //no divisions
		}

		List emptyLeagues = getAllWorkReportGroupsPrimaryKeysWithNoMembers(workReportId);

		if (emptyLeagues.isEmpty()) {
			System.out.println("No empty divisions for work report id : " + workReportId);
			return false;
		}
		else {
			Iterator recs = records.iterator();
			//the real check happens here
			while (recs.hasNext()) {
				WorkReportClubAccountRecord rec = (WorkReportClubAccountRecord)recs.next();
				if (emptyLeagues.contains(new Integer(rec.getWorkReportGroupId()))) {
					System.out.println("Empty divisions with account record found! workreportgroupid : " + rec.getWorkReportGroupId());
					return true;
				}
			}

			return false;

		}

	}

	public boolean isBoardMissingForDivisionWithMembersOrYearlyAccount(int workReportId) {
		WorkReportBoardMemberHome boardHome = getWorkReportBoardMemberHome();
		WorkReportClubAccountRecordHome recHome = getWorkReportClubAccountRecordHome();
		Collection records = null;
		Collection leagues = null;
		boolean checkForAccount = false;

		WorkReport report = this.getWorkReportById(workReportId);

		try {
			leagues = report.getLeagues();
		}
		catch (IDOException e) {
			System.out.println("No divisions for work report id : " + workReportId);
			return false; //no divisions
		}

		try {
			records = recHome.findAllRecordsByWorkReportId(workReportId);
			checkForAccount = true;
		}
		catch (FinderException e1) {
			System.out.println("No account records for work report id : " + workReportId);
		}

		List nonEmptyLeagues = getAllWorkReportGroupsPrimaryKeysThatHaveMembers(workReportId);

		Iterator primaryKeys = nonEmptyLeagues.iterator();

		//the real check happens here
		while (primaryKeys.hasNext()) {
			Integer workGroupID = (Integer)primaryKeys.next();
			Collection boardMembers;
			try {
				boardMembers = boardHome.findAllWorkReportBoardMembersByWorkReportIdAndWorkReportGroupId(workReportId, workGroupID.intValue());
			}
			catch (FinderException e2) {
				System.out.println("Board members missing for a division");
				//e2.printStackTrace();
				return true;
			}
		}

		if (checkForAccount) {
			Map leaguesMap = new HashMap();
			Iterator recs = records.iterator();
			//the real check happens here
			while (recs.hasNext()) {
				WorkReportClubAccountRecord rec = (WorkReportClubAccountRecord)recs.next();
				leaguesMap.put(new Integer(rec.getWorkReportGroupId()), new Integer(rec.getWorkReportGroupId()));
			}

			Iterator iter = leaguesMap.keySet().iterator();
			Collection boardMembers;

			while (iter.hasNext()) {
				Integer wrGroupId = (Integer)iter.next();

				try {
					boardMembers = boardHome.findAllWorkReportBoardMembersByWorkReportIdAndWorkReportGroupId(workReportId, wrGroupId.intValue());
				}
				catch (FinderException e2) {
					System.out.println("Board members missing for a division with account info");
					//e2.printStackTrace();
					return true;
				}
			}
		}

		return false;
	}

	public List getAllWorkReportGroupsPrimaryKeysWithNoMembers(int workReportId) {
		ArrayList emptyLeagues = new ArrayList();
		WorkReport report = this.getWorkReportById(workReportId);
		Collection leagues = null;
		try {
			leagues = report.getLeagues();
		}
		catch (IDOException e) {
			return ListUtil.getEmptyList();
		}

		Iterator iter = leagues.iterator();
		while (iter.hasNext()) {
			WorkReportGroup league = (WorkReportGroup)iter.next();
			Collection members = getAllWorkReportMembersForWorkReportIdAndWorkReportGroupId(workReportId, league);
			if (members.isEmpty()) {
				emptyLeagues.add(league.getPrimaryKey());
			}
		}

		return emptyLeagues;
	}

	public List getAllWorkReportGroupsPrimaryKeysThatHaveMembers(int workReportId) {
		ArrayList nonEmptyLeagues = new ArrayList();
		WorkReport report = this.getWorkReportById(workReportId);
		Collection leagues = null;

		try {
			leagues = report.getLeagues();
		}
		catch (IDOException e) {
			return ListUtil.getEmptyList();
		}

		Iterator iter = leagues.iterator();
		while (iter.hasNext()) {
			WorkReportGroup league = (WorkReportGroup)iter.next();
			Collection members = getAllWorkReportMembersForWorkReportIdAndWorkReportGroupId(workReportId, league);
			if (!members.isEmpty()) {
				nonEmptyLeagues.add(league.getPrimaryKey());
			}
		}

		return nonEmptyLeagues;
	}

	public boolean isYearlyAccountMissingForADivisionWithMembers(int workReportId) {

		WorkReportClubAccountRecordHome recHome = getWorkReportClubAccountRecordHome();
		Collection records = null;
		Collection leagues = null;

		WorkReport report = this.getWorkReportById(workReportId);

		try {
			leagues = report.getLeagues();
		}
		catch (IDOException e) {
			System.out.println("No divisions for work report id : " + workReportId);
			return false; //no divisions
		}

		try {
			records = recHome.findAllRecordsByWorkReportId(workReportId);
		}
		catch (FinderException e1) {
			System.out.println("No account records for work report id : " + workReportId);
			return true; //no records but leagues
		}

		List nonEmptyLeagues = getAllWorkReportGroupsPrimaryKeysThatHaveMembers(workReportId);

		if (nonEmptyLeagues.isEmpty()) {
			System.out.println("No divisions with members for work report id : " + workReportId);
			return false;
		}
		else {
			Iterator primaryKeys = nonEmptyLeagues.iterator();

			//the real check happens here
			while (primaryKeys.hasNext()) {
				Integer workGroupID = (Integer)primaryKeys.next();
				Collection recs;
				try {
					recs = recHome.findAllRecordsByWorkReportIdAndWorkReportGroupId(workReportId, workGroupID.intValue());
				}
				catch (FinderException e2) {

					System.out.println("Account recs missing for a division");
					//e2.printStackTrace();
					return true;
				}
			}

			return false;

		}
	}

} //end of class
