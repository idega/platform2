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
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroupHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportMemberHome;
import is.idega.idegaweb.member.isi.block.reports.presentation.ClubSelector;
import is.idega.idegaweb.member.isi.block.reports.presentation.WorkReportWindowPlugin;
import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;
import is.idega.idegaweb.member.util.IWMemberConstants;
import java.rmi.RemoteException;
import java.sql.Date;
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
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.contact.data.PhoneType;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.IDOStoreException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.transaction.IdegaTransactionManager;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;

/**
 * Description: Use this business class to handle work report related business.
 * Copyright: Copyright (c) 2003 Company: Idega Software
 * 
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson </a>
 * @version 1.0
 */
public class WorkReportBusinessBean extends MemberUserBusinessBean implements MemberUserBusiness, WorkReportBusiness,UserGroupPlugInBusiness{

	private WorkReportGroupHome workReportGroupHome;

	private WorkReportClubAccountRecordHome workReportClubAccountRecordHome;

	private WorkReportAccountKeyHome workReportAccountKeyHome;

	private WorkReportHome workReportHome;

	private WorkReportMemberHome workReportMemberHome;

	private WorkReportBoardMemberHome workReportBoardMemberHome;

	private WorkReportDivisionBoardHome workReportDivisionBoardHome;

	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";

	public int getTotalCountOfMembersForWorkReportYear(int year) {
		int count = 0;
		try {
			Collection reports = this.getWorkReportHome().findAllWorkReportsByYearOrderedByGroupType(year);
			Iterator iter = reports.iterator();
			while (iter.hasNext()) {
				WorkReport report = (WorkReport) iter.next();
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
				WorkReport report = (WorkReport) iter.next();
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
				WorkReport report = (WorkReport) iter.next();
				int add = report.getNumberOfCompetitors();
				count += (add > 0) ? add : 0; //add to sum if more than 0
			}
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return count;
	}

	//any age
	public int getCountOfPlayersByWorkReportAndWorkReportGroup(WorkReport report, WorkReportGroup league) {
		return getWorkReportMemberHome().getCountOfPlayersByWorkReportAndWorkReportGroup(report, league);
	}

	public int getCountOfMalePlayersByWorkReportAndWorkReportGroup(WorkReport report, WorkReportGroup league) {
		return getWorkReportMemberHome().getCountOfMalePlayersByWorkReportAndWorkReportGroup(report, league);
	}

	public int getCountOfFemalePlayersByWorkReportAndWorkReportGroup(WorkReport report, WorkReportGroup league) {
		return getWorkReportMemberHome().getCountOfFemalePlayersByWorkReportAndWorkReportGroup(report, league);
	}

	public int getCountOfMembersByWorkReport(WorkReport report) {
		return getWorkReportMemberHome().getCountOfMembersByWorkReport(report);
	}

	public int getCountOfMaleMembersByWorkReport(WorkReport report) {
		return getWorkReportMemberHome().getCountOfMaleMembersByWorkReport(report);
	}

	public int getCountOfFemaleMembersByWorkReport(WorkReport report) {
		return getWorkReportMemberHome().getCountOfFemaleMembersByWorkReport(report);
	}

	public int getCountOfPlayersByWorkReport(WorkReport report) {
		//The main board connection that is neccesery for the member editor
		// makes the final count wrong, here we correct that
		int everyOneWithMainBoardStats = getWorkReportMemberHome().getCountOfPlayersByWorkReport(report);
		int mainBoardStats = getWorkReportMemberHome().getCountOfPlayersByWorkReportAndWorkReportGroup(report,
				getMainBoardWorkReportGroup(report.getYearOfReport().intValue()));
		return (everyOneWithMainBoardStats - mainBoardStats);
	}

	public int getCountOfMalePlayersByWorkReport(WorkReport report) {
		//The main board connection that is neccesery for the member editor
		// makes the final count wrong, here we correct that
		int everyOneWithMainBoardStats = getWorkReportMemberHome().getCountOfMalePlayersByWorkReport(report);
		int mainBoardStats = getWorkReportMemberHome().getCountOfMalePlayersByWorkReportAndWorkReportGroup(report,
				getMainBoardWorkReportGroup(report.getYearOfReport().intValue()));
		return (everyOneWithMainBoardStats - mainBoardStats);
	}

	public int getCountOfFemalePlayersByWorkReport(WorkReport report) {
		//The main board connection that is neccesery for the member editor
		// makes the final count wrong, here we correct that
		int everyOneWithMainBoardStats = getWorkReportMemberHome().getCountOfFemalePlayersByWorkReport(report);
		int mainBoardStats = getWorkReportMemberHome().getCountOfFemalePlayersByWorkReportAndWorkReportGroup(report,
				getMainBoardWorkReportGroup(report.getYearOfReport().intValue()));
		return (everyOneWithMainBoardStats - mainBoardStats);
	}

	//equal
	//just saving time
	public int getCountOfPlayersOfEqualAgeAndGenderByWorkReportAndWorkReportGroup(int age, String genderMorF,
			WorkReport report, WorkReportGroup league) {
		return getWorkReportMemberHome().getCountOfPlayersOfEqualAgeAndByWorkReportAndWorkReportGroup(age, genderMorF,
				report, league);
	}

	public int getCountOfPlayersOfEqualAgeAndByWorkReportAndWorkReportGroup(int age, WorkReport report,
			WorkReportGroup league) {
		return getWorkReportMemberHome().getCountOfPlayersOfEqualAgeAndByWorkReportAndWorkReportGroup(age, report,
				league);
	}

	public int getCountOfMalePlayersOfEqualAgeAndByWorkReportAndWorkReportGroup(int age, WorkReport report,
			WorkReportGroup league) {
		return getWorkReportMemberHome().getCountOfMalePlayersOfEqualAgeAndByWorkReportAndWorkReportGroup(age, report,
				league);
	}

	public int getCountOfFemalePlayersOfEqualAgeAndByWorkReportAndWorkReportGroup(int age, WorkReport report,
			WorkReportGroup league) {
		return getWorkReportMemberHome().getCountOfFemalePlayersOfEqualAgeAndByWorkReportAndWorkReportGroup(age,
				report, league);
	}

	//equal or older
	public int getCountOfPlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(int age, WorkReport report,
			WorkReportGroup league) {
		return getWorkReportMemberHome().getCountOfPlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age,
				report, league);
	}

	public int getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(int age, WorkReport report,
			WorkReportGroup league) {
		return getWorkReportMemberHome().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(age,
				report, league);
	}

	public int getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(int age, WorkReport report,
			WorkReportGroup league) {
		return getWorkReportMemberHome().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(
				age, report, league);
	}

	public int getCountOfMembersEqualOrOlderThanAgeAndByWorkReport(int age, WorkReport report) {
		return getWorkReportMemberHome().getCountOfMembersEqualOrOlderThanAgeByWorkReport(age, report);
	}

	public int getCountOfMaleMembersEqualOrOlderThanAgeAndByWorkReport(int age, WorkReport report) {
		return getWorkReportMemberHome().getCountOfMaleMembersEqualOrOlderThanAgeByWorkReport(age, report);
	}

	public int getCountOfFemaleMembersEqualOrOlderThanAgeAndByWorkReport(int age, WorkReport report) {
		return getWorkReportMemberHome().getCountOfFemaleMembersEqualOrOlderThanAgeByWorkReport(age, report);
	}

	public int getCountOfPlayersEqualOrOlderThanAgeAndByWorkReport(int age, WorkReport report) {
		//The main board connection that is neccesery for the member editor
		// makes the final count wrong, here we correct that
		int everyOneWithMainBoardStats = getWorkReportMemberHome().getCountOfPlayersEqualOrOlderThanAgeByWorkReport(
				age, report);
		int mainBoardStats = getWorkReportMemberHome().getCountOfPlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(
				age, report, getMainBoardWorkReportGroup(report.getYearOfReport().intValue()));
		return (everyOneWithMainBoardStats - mainBoardStats);
	}

	public int getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReport(int age, WorkReport report) {
		//The main board connection that is neccesery for the member editor
		// makes the final count wrong, here we correct that
		int everyOneWithMainBoardStats = getWorkReportMemberHome().getCountOfMalePlayersEqualOrOlderThanAgeByWorkReport(
				age, report);
		int mainBoardStats = getWorkReportMemberHome().getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(
				age, report, getMainBoardWorkReportGroup(report.getYearOfReport().intValue()));
		return (everyOneWithMainBoardStats - mainBoardStats);
	}

	public int getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReport(int age, WorkReport report) {
		//The main board connection that is neccesery for the member editor
		// makes the final count wrong, here we correct that
		int everyOneWithMainBoardStats = getWorkReportMemberHome().getCountOfFemalePlayersEqualOrOlderThanAgeByWorkReport(
				age, report);
		int mainBoardStats = getWorkReportMemberHome().getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(
				age, report, getMainBoardWorkReportGroup(report.getYearOfReport().intValue()));
		return (everyOneWithMainBoardStats - mainBoardStats);
	}

	//younger
	public int getCountOfPlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(int age, WorkReport report,
			WorkReportGroup league) {
		return getWorkReportMemberHome().getCountOfPlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age, report,
				league);
	}

	public int getCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(int age, WorkReport report,
			WorkReportGroup league) {
		return getWorkReportMemberHome().getCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age,
				report, league);
	}

	public int getCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(int age, WorkReport report,
			WorkReportGroup league) {
		return getWorkReportMemberHome().getCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(age,
				report, league);
	}

	public int getCountOfMembersOfYoungerAgeAndByWorkReport(int age, WorkReport report) {
		return getWorkReportMemberHome().getCountOfMembersOfYoungerAgeByWorkReport(age, report);
	}

	public int getCountOfMaleMembersOfYoungerAgeAndByWorkReport(int age, WorkReport report) {
		return getWorkReportMemberHome().getCountOfMaleMembersOfYoungerAgeByWorkReport(age, report);
	}

	public int getCountOfFemaleMembersOfYoungerAgeAndByWorkReport(int age, WorkReport report) {
		return getWorkReportMemberHome().getCountOfFemaleMembersOfYoungerAgeByWorkReport(age, report);
	}

	public int getCountOfPlayersOfYoungerAgeAndByWorkReport(int age, WorkReport report) {
		//The main board connection that is neccesery for the member editor
		// makes the final count wrong, here we correct that
		int everyOneWithMainBoardStats = getWorkReportMemberHome().getCountOfPlayersOfYoungerAgeByWorkReport(age,
				report);
		int mainBoardStats = getWorkReportMemberHome().getCountOfPlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(
				age, report, getMainBoardWorkReportGroup(report.getYearOfReport().intValue()));
		return (everyOneWithMainBoardStats - mainBoardStats);
	}

	public int getCountOfMalePlayersOfYoungerAgeAndByWorkReport(int age, WorkReport report) {
		//The main board connection that is neccesery for the member editor
		// makes the final count wrong, here we correct that
		int everyOneWithMainBoardStats = getWorkReportMemberHome().getCountOfMalePlayersOfYoungerAgeByWorkReport(age,
				report);
		int mainBoardStats = getWorkReportMemberHome().getCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(
				age, report, getMainBoardWorkReportGroup(report.getYearOfReport().intValue()));
		return (everyOneWithMainBoardStats - mainBoardStats);
	}

	public int getCountOfFemalePlayersOfYoungerAgeAndByWorkReport(int age, WorkReport report) {
		//The main board connection that is neccesery for the member editor
		// makes the final count wrong, here we correct that
		int everyOneWithMainBoardStats = getWorkReportMemberHome().getCountOfFemalePlayersOfYoungerAgeByWorkReport(age,
				report);
		int mainBoardStats = getWorkReportMemberHome().getCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(
				age, report, getMainBoardWorkReportGroup(report.getYearOfReport().intValue()));
		return (everyOneWithMainBoardStats - mainBoardStats);
	}

	/**
	 * This method gets you the id of the workreport of the club and year
	 * specified. It will create a new report if it does not exist already.
	 * 
	 * @param clubId
	 * @param yearStamp
	 * @return The id of the WorkReport for this club and year.
	 */
	public int getOrCreateWorkReportIdForGroupIdByYear(int groupId, int year, boolean createData)
			throws RemoteException {
		WorkReport report = null;
		Group club = null;
		int wrId = -1;
		boolean justCreated = false;
		try {
			club = this.getGroupBusiness().getGroupByGroupID(groupId); //could
																	   // be
																	   // club,regional
																	   // union
																	   // or
																	   // league
			try {
				report = getWorkReportHome().findWorkReportByGroupIdAndYearOfReport(groupId, year);
			}
			catch (FinderException e) {
				System.out.println("[WorkReportBusinessBean] No report for groupId : " + groupId + " and year : "
						+ year + " creating a new one.");
				try {
					report = getWorkReportHome().create();
					report.setStatus(WorkReportConstants.WR_STATUS_NOT_DONE);
					report.setGroupId(groupId);
					report.setYearOfReport(year);
					report.setGroupType(club.getGroupType());
					updateAndStoreWorkReport(report);
					justCreated = true;
				}
				catch (CreateException e1) {
					e1.printStackTrace();
				}
			}
			//UPDATE ALWAYS UNLESS IS READ ONLY
			wrId = ((Integer) report.getPrimaryKey()).intValue();
			if (!isWorkReportReadOnly(wrId)) {
				createOrUpdateLeagueWorkReportGroupsForYear(year);
				if (!justCreated) {
					updateAndStoreWorkReport(report);
				}
				if (createData) {
					createWorkReportData(wrId);
				}
			}
		}
		catch (FinderException e1) {//second catch, could be a problem?
			e1.printStackTrace();
		}
		return wrId;
	}

	/**
	 * @param report
	 */
	private WorkReport updateAndStoreWorkReport(WorkReport report) throws RemoteException, FinderException {
		 //could be club,regional union or league
		Group club = this.getGroupBusiness().getGroupByGroupID(report.getGroupId().intValue());
		report.setGroupName(club.getName());
		report.setGroupNumber(club.getMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER));
		report.setGroupShortName(club.getShortName());
		String status = club.getMetaData(IWMemberConstants.META_DATA_CLUB_STATUS);
		if (IWMemberConstants.META_DATA_CLUB_STATE_INACTIVE.equals(status)) {
			report.setAsInactive();
		}
		else {
			report.setAsActive();
		}
		String club_type = club.getMetaData(IWMemberConstants.META_DATA_CLUB_MAKE);
		report.setType(club_type);
		String club_in_umfi = club.getMetaData(IWMemberConstants.META_DATA_CLUB_IN_UMFI);
		if (club_in_umfi != null && (club_in_umfi.equalsIgnoreCase("true") || club_in_umfi.equalsIgnoreCase("y") ) ) {
			report.setIsInUMFI(true);
		}
		else {
			report.setIsInUMFI(false);
		}
		try {
			if(club.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB)){
				Group regionalUnion = this.getRegionalUnionGroupForClubGroup(club);
				report.setRegionalUnionGroupId((Integer) regionalUnion.getPrimaryKey());
				report.setRegionalUnionNumber(regionalUnion.getMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER));
				String abbr = regionalUnion.getAbbrevation();
				if (abbr == null) {
					abbr = regionalUnion.getShortName();
					if (abbr == null) {
						abbr = regionalUnion.getName();
					}
				}
				report.setRegionalUnionAbbreviation(abbr);
				report.setRegionalUnionName(regionalUnion.getName());
			}
			else if(club.getGroupType().equals(IWMemberConstants.GROUP_TYPE_REGIONAL_UNION)){
				report.setRegionalUnionNumber(club.getMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER));
				String abbr = club.getAbbrevation();
				if (abbr == null) {
					abbr = club.getShortName();
				}
				report.setRegionalUnionAbbreviation(abbr);
				report.setRegionalUnionName(club.getName());
			}	
		}
		catch (NoRegionalUnionFoundException e3) {
			//no regional union, must be a league or a regional union itself
		}
		
		
		report.store();
		return report;
	}

	public int getWorkReportExpensesByWorkReportIdAndWorkReportGroupId(int reportId, int wrGroupId) {
		int count = 0;
		try {
			Collection recs = getWorkReportClubAccountRecordHome().findAllRecordsByWorkReportIdAndWorkReportGroupIdAndWorkReportAccountKeyCollection(
					reportId, wrGroupId, getWorkReportAccountKeyHome().findExpensesAccountKeysWithoutSubKeys());
			Iterator iter = recs.iterator();
			while (iter.hasNext()) {
				WorkReportClubAccountRecord rec = (WorkReportClubAccountRecord) iter.next();
				count += rec.getAmount();
			}
		}
		catch (FinderException e) {
			return 0;
		}
		return count;
	}

	public int getAmmountOfAccountRecordByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyId(int reportId,
			int wrGroupId, int wrAccountKeyId) {
		WorkReportClubAccountRecord record;
		try {
			record = getWorkReportClubAccountRecordHome().findRecordByWorkReportIdAndWorkReportGroupIdAndWorkReportAccountKeyId(
					reportId, wrGroupId, wrAccountKeyId);
			return (int) record.getAmount();
		}
		catch (FinderException e) {
			//no record returning 0
			return 0;
		}
	}

	public int getAmmountOfAccountRecordByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyName(int reportId,
			int wrGroupId, String accountKeyName) {
		WorkReportAccountKey key;
		try {
			key = getWorkReportAccountKeyHome().findAccountKeyByName(accountKeyName);
		}
		catch (FinderException e1) {
			e1.printStackTrace();
			//no such account key
			return 0;
		}
		int wrAccountKeyId = ((Integer) key.getPrimaryKey()).intValue();
		WorkReportClubAccountRecord record;
		try {
			record = getWorkReportClubAccountRecordHome().findRecordByWorkReportIdAndWorkReportGroupIdAndWorkReportAccountKeyId(
					reportId, wrGroupId, wrAccountKeyId);
			return (int) record.getAmount();
		}
		catch (FinderException e) {
			//no record returning 0
			return 0;
		}
	}

	public int getTotalAmmountOfAccountRecordsByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyCollection(
			int reportId, int wrGroupId, Collection accountKeys) {
		Collection records;
		int amount = 0;
		try {
			records = getWorkReportClubAccountRecordHome().findAllRecordsByWorkReportIdAndWorkReportGroupIdAndWorkReportAccountKeyCollection(
					reportId, wrGroupId, accountKeys);
			if (records != null && !records.isEmpty()) {
				Iterator iter = records.iterator();
				while (iter.hasNext()) {
					WorkReportClubAccountRecord rec = (WorkReportClubAccountRecord) iter.next();
					amount += (int) rec.getAmount();
				}
			}
			return amount;
		}
		catch (FinderException e) {
			//no record returning 0
			return amount;
		}
	}

	public WorkReportHome getWorkReportHome() {
		if (workReportHome == null) {
			try {
				workReportHome = (WorkReportHome) IDOLookup.getHome(WorkReport.class);
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
				workReportMemberHome = (WorkReportMemberHome) IDOLookup.getHome(WorkReportMember.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportMemberHome;
	}

	public WorkReportGroupHome getWorkReportGroupHome() {
		if (workReportGroupHome == null) {
			try {
				workReportGroupHome = (WorkReportGroupHome) IDOLookup.getHome(WorkReportGroup.class);
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
				workReportAccountKeyHome = (WorkReportAccountKeyHome) IDOLookup.getHome(WorkReportAccountKey.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportAccountKeyHome;
	}

	/**
	 * This method returns an ordered collection of workreports or an empty
	 * list.
	 * 
	 * @param year,
	 *            int for the work report year
	 * @param regionalUnionsGroups,
	 *            a collection of Group
	 * @return
	 */
	public Collection getWorkReportsForRegionalUnionCollection(int year, Collection regionalUnionsGroups) {
		try {
			return getWorkReportHome().findAllWorkReportsByYearAndRegionalUnionGroupsOrderedByRegionalUnionNameAndClubName(
					year, regionalUnionsGroups);
		}
		catch (FinderException e) {
			return ListUtil.getEmptyList();
		}
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
		member.setUserId(((Integer) user.getPrimaryKey()).intValue());
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
		String streetAddress = null;
		try {
			Address address = getUsersMainAddress(user);
			if (address != null) {
				streetAddress = address.getStreetAddress();
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
		try {
			member.store();
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
			System.err.println("WorkReportBusiness: Error creating workreport member! likely because some of the column in the table are too small, most likely the user has a too long ssn/personalid");
			return null;
		}
		return member;
	}

	public WorkReportBoardMember createWorkReportBoardMember(int reportID, String personalID,
			WorkReportGroup workReportGroup) throws CreateException {
		User user = null;
		try {
			user = getUser(personalID);
		}
		catch (FinderException e) {
			return null;
		}
		return createWorkReportBoardMember(reportID, user, workReportGroup);
	}

	public WorkReportBoardMember createWorkReportBoardMember(int reportID, User user, WorkReportGroup workReportGroup)
			throws CreateException {
		Date dob = user.getDateOfBirth();
		Age age = null;
		if (dob != null)
			age = new Age(user.getDateOfBirth());
		WorkReportBoardMember member = getWorkReportBoardMemberHome().create();
		member.setReportId(reportID);
		member.setName(user.getName());
		member.setPersonalId(user.getPersonalID());
		if (age != null)
			member.setAge(age.getYears());
		if (dob != null)
			member.setDateOfBirth((new IWTimestamp(user.getDateOfBirth())).getTimestamp());
		member.setUserId(((Integer) user.getPrimaryKey()).intValue());
		// league
		if (workReportGroup != null) {
			int pk = ((Integer) workReportGroup.getPrimaryKey()).intValue();
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

	public WorkReportDivisionBoard createWorkReportDivisionBoard(int reportId, Group clubDivision,
			WorkReportGroup league) throws CreateException {
		WorkReportDivisionBoard divisionBoard = null;
		// does the division board already exist?
		try {
			WorkReportDivisionBoardHome workReportDivisionBoardHomeTemp = getWorkReportDivisionBoardHome();
			int workReportGroupId = ((Integer) league.getPrimaryKey()).intValue();
			divisionBoard = workReportDivisionBoardHomeTemp.findWorkReportDivisionBoardByWorkReportIdAndWorkReportGroupId(
					reportId, workReportGroupId);
			if (divisionBoard != null) {
				// division board exist be sure that the league was added to the
				// work report
				WorkReport workReport = getWorkReportById(reportId);
				try {
					workReport.addLeague(league);
					workReport.store();
				}
				catch (IDORelationshipException ex) {
					String message = "[WorkReportBusiness]: Can't define realtion ship.";
					System.err.println(message + " Message is: " + ex.getMessage());
					//	ex.printStackTrace(System.err);
					// do nothing
				}
			}
		}
		catch (FinderException ex) {
			// work report division does not exist, go further, create it!
			divisionBoard = getWorkReportDivisionBoardHome().create();
			divisionBoard.setReportId(reportId);
			Integer id = (Integer) clubDivision.getPrimaryKey();
			divisionBoard.setGroupId(id.intValue());
			//			league
			if (league != null) {
				int pk = ((Integer) league.getPrimaryKey()).intValue();
				divisionBoard.setWorkReportGroupID(pk);
			}
			// +++++++++++++++++++++++++++++++
			// add league to work report group
			// +++++++++++++++++++++++++++++++
			WorkReport workReport = getWorkReportById(reportId);
			try {
				workReport.addLeague(league);
				workReport.store();
			}
			catch (IDORelationshipException error) {
				String message = "[WorkReportBusiness]: Can't define realtion ship.";
				System.err.println(message + " Message is: " + error.getMessage());
				//ex.printStackTrace(System.err);
				// do nothing
			}
		}
		// get group business
		GroupBusiness groupBusiness = null;
		try {
			groupBusiness = getGroupBusiness();
		}
		catch (RemoteException ex) {
			System.err.println("[WorkReportBusiness]: Can't retrieve GroupBusiness or an address. Message is: "
					+ ex.getMessage());
			ex.printStackTrace(System.err);
			throw new RuntimeException("[WorkReportBusiness]: Can't retrieve GroupBusiness or an address.");
		}
		// Update stuff
		if (clubDivision != null) {
			// home page
			String homePageURL = clubDivision.getHomePageURL();
			if (homePageURL != null) {
				divisionBoard.setHomePage(homePageURL);
			}
			// personal id
			String ssn = clubDivision.getMetaData(IWMemberConstants.META_DATA_CLUB_SSN);
			if (ssn != null) {
				divisionBoard.setPersonalId(ssn);
			}
			// address
			try {
				Address address = groupBusiness.getGroupMainAddress(clubDivision);
				if (address != null) {
					// street and number
					String streetAndNumber = address.getStreetAddress();
					if (streetAndNumber != null) {
						divisionBoard.setStreetName(streetAndNumber);
					}
					// postal code id
					PostalCode postalCode = address.getPostalCode();
					if (postalCode != null) {
						divisionBoard.setPostalCode(postalCode);
					}
				}
			}
			catch (RemoteException ex) {
				logError("[WorkReportBusiness]: Can't retrieve address.");
				log(ex);
				throw new RuntimeException("[WorkReportBusiness]: Can't retrieve Address.");
			}
			catch (IDOCompositePrimaryKeyException compEx) {
				logError("[WorkReportBusiness]: Can't retrieve address.");
				log(compEx);
				throw new RuntimeException("[WorkReportBusiness]: Can't retrieve Address.");
			}
			catch (IDORelationshipException relEx) {
				logError("[WorkReportBusiness]: Can't retrieve address.");
				log(relEx);
				throw new RuntimeException("[WorkReportBusiness]: Can't retrieve Address.");
			}
			// home phone
			try {
				Phone homePhone = groupBusiness.getGroupPhone(clubDivision, PhoneType.HOME_PHONE_ID);
				if (homePhone != null) {
					String number = homePhone.getNumber();
					if (number != null) {
						divisionBoard.setFirstPhone(number);
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
						divisionBoard.setSecondPhone(number);
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
						divisionBoard.setFax(number);
					}
				}
			}
			catch (RemoteException ex) {
				System.err.println("[WorkReportBusiness]: Can't retrieve fax phone. Message is: " + ex.getMessage());
				ex.printStackTrace(System.err);
				throw new RuntimeException("[WorkReportBusiness]: Can't retrieve fax phone.");
			}
			// email
			try {
				Email eMail = groupBusiness.getGroupEmail(clubDivision);
				if (eMail != null) {
					String eMailAddress = eMail.getEmailAddress();
					if (eMailAddress != null) {
						divisionBoard.setEmail(eMailAddress);
					}
				}
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		//store it!
		divisionBoard.store();
		return divisionBoard;
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

	/**
	 * Returns all leagues, that belong to the specified work report.
	 * 
	 * @param id -
	 *            id of the work report
	 * @return a collection of WorkReportGroups
	 */
	public Collection getLeaguesOfWorkReportById(int id) throws IDOException {
		WorkReport workReport = getWorkReportById(id);
		int year = workReport.getYearOfReport().intValue();
		WorkReportGroup mainBoard = getMainBoardWorkReportGroup(year);
		Integer mainBoardId = (Integer) mainBoard.getPrimaryKey();
		Collection leagues = workReport.getLeagues();
		Iterator leagueIterator = leagues.iterator();
		while (leagueIterator.hasNext()) {
			WorkReportGroup group = (WorkReportGroup) leagueIterator.next();
			Integer workReportId = (Integer) group.getPrimaryKey();
			if (mainBoardId.equals(workReportId)) {
				return leagues;
			}
		}
		try {
			workReport.addLeague(mainBoard);
		}
		catch (IDORelationshipException ex) {
			String message = "[WorkReportBusiness]: Can't add mainboard league";
			System.err.println(message + " Message is: " + ex.getMessage());
			ex.printStackTrace(System.err);
		}
		return workReport.getLeagues();
	}

	public String getFileName(int id) {
		ICFile icfile;
		try {
			icfile = ((ICFileHome) IDOLookup.getHome(ICFile.class)).findByPrimaryKey(new Integer(id));
			return icfile.getName();
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return "no_file";
	}

	protected MemberUserBusiness getMemberUserBusiness() {
		MemberUserBusiness mub = null;
		try {
			mub = (MemberUserBusiness) getServiceInstance(MemberUserBusiness.class);
		}
		catch (java.rmi.RemoteException rme) {
			throw new RuntimeException(rme.getMessage());
		}
		return mub;
	}

	/**
	 * @param year
	 *            of report
	 */
	public void createOrUpdateLeagueWorkReportGroupsForYear(int year) {
		createOrUpdateWorkReportGroupsForYearAndGroupType(year, IWMemberConstants.GROUP_TYPE_LEAGUE);
	}

	/**
	 * @param report
	 */
	public void deleteWorkReportMembersForReport(int reportId) {
		try {
			Collection members = getWorkReportMemberHome().findAllWorkReportMembersByWorkReportIdOrderedByMemberName(
					reportId);
			Iterator iter = members.iterator();
			while (iter.hasNext()) {
				WorkReportMember memb = (WorkReportMember) iter.next();
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
	public void deleteWorkReportBoardMembersForReport(int reportId) {
		try {
			Collection members = getWorkReportBoardMemberHome().findAllWorkReportBoardMembersByWorkReportIdOrderedByMemberName(
					reportId);
			Iterator iter = members.iterator();
			while (iter.hasNext()) {
				WorkReportBoardMember memb = (WorkReportBoardMember) iter.next();
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
			/*
			 * Collection records =
			 * getWorkReportDivisionBoardHome().findAllWorkReportDivisionBoardByWorkReportId(reportId);
			 * Iterator iter2 = records.iterator();
			 * 
			 * while (iter2.hasNext()) { WorkReportDivisionBoard record =
			 * (WorkReportDivisionBoard)iter2.next(); try { record.remove(); }
			 * catch (EJBException e1) { e1.printStackTrace(); } catch
			 * (RemoveException e1) { e1.printStackTrace(); } }
			 */
		}
		catch (FinderException e) {
			//do nothing because its empty
		}
	}

	/**
	 * @param report
	 */
	public void deleteWorkReportAccountRecordsForReport(int reportId) {
		try {
			Collection records = getWorkReportClubAccountRecordHome().findAllRecordsByWorkReportId(reportId);
			Iterator iter = records.iterator();
			while (iter.hasNext()) {
				WorkReportClubAccountRecord record = (WorkReportClubAccountRecord) iter.next();
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
	 * Returns a collection of WorkReportGroup or an empty List
	 * 
	 * @param year ,
	 *            the year of the report
	 * @param type ,
	 *            the group type
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
	 * 
	 * @param year ,
	 *            the year of the reports
	 * @return A collection of WorkReports or an empty List
	 */
	public Collection getAllWorkReportsForYear(int year) {
		try {
			return getWorkReportHome().findAllWorkReportsByYearOrderedByRegionalUnionNumberAndGroupNumber(year);
		}
		catch (FinderException e) {
			//no report available return empty list
			return ListUtil.getEmptyList();
		}
	}

	/**
	 * Returns a collection of WorkReportGroup of the type IWMemor an empty List
	 * 
	 * @param year ,
	 *            the year of the report
	 * @param type ,
	 *            the group type
	 * @return A collection of WorkReportGroup or an empty List
	 */
	public Collection getAllLeagueWorkReportGroupsForYear(int year) {
		try {
			return getWorkReportGroupHome().findAllWorkReportGroupsByGroupTypeAndYear(
					IWMemberConstants.GROUP_TYPE_LEAGUE, year);
		}
		catch (FinderException e) {
			//no group available return empty list
			return ListUtil.getEmptyList();
		}
	}

	private void createOrUpdateWorkReportGroupsForYearAndGroupType(int year, String groupType) {
		if (canWeUpdateWorkReportDataFromDatabase(year)) {
			GroupBusiness groupBiz;
			try {
				groupBiz = getGroupBusiness();
				WorkReportGroupHome grHome = getWorkReportGroupHome();
				Collection groups = groupBiz.getGroupHome().findGroupsByType(groupType);
				Iterator groupIter = groups.iterator();
				while (groupIter.hasNext()) {
					Group group = (Group) groupIter.next();
					int groupId = ((Integer) group.getPrimaryKey()).intValue();
					WorkReportGroup wGroup = null;
					try {
						wGroup = grHome.findWorkReportGroupByGroupIdAndYear(groupId, year);
					}
					catch (FinderException e1) {
						try {
							wGroup = grHome.create();
							wGroup.setGroupId(groupId);
							wGroup.setGroupType(group.getGroupType());
							wGroup.setYearOfReport(year);
						}
						catch (CreateException e) {
							e.printStackTrace();
						}
					}
					wGroup.setName(group.getName());
					String shortName = group.getAbbrevation();//abbrevation is
															  // better, change
															  // by eiki
					// should not happen but happens....
					// shortName must be set!
					if (shortName == null) {
						shortName = group.getShortName();
						if (shortName == null) {
							shortName = group.getName();
						}
					}
					wGroup.setShortName(shortName);
					wGroup.setNumber(group.getMetaData(IWMemberConstants.META_DATA_CLUB_NUMBER));
					wGroup.store();
				}
			}
			catch (RemoteException e1) {
				e1.printStackTrace();
			}
			catch (FinderException e1) {
				// do nothing return empty list
			}
		}
	}

	/**
	 * Gets all the WorkReportMembers for the supplied WorkReport id
	 * 
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
	 * Gets all the WorkReportMembers for the supplied WorkReport id and
	 * workreportgroup id
	 * 
	 * @param workReportId
	 * @return a collection of WorkReportMembers or an empty list
	 */
	public Collection getAllWorkReportMembersForWorkReportIdAndWorkReportGroupId(int workReportId,
			WorkReportGroup workReportGroup) {
		try {
			return getWorkReportMemberHome().findAllWorkReportMembersByWorkReportIdAndWorkReportGroup(workReportId,
					workReportGroup);
		}
		catch (FinderException e) {
			return ListUtil.getEmptyList();
		}
	}

	/**
	 * Gets all the WorkReportBoardMembers for the supplied WorkReport id
	 * 
	 * @param workReportId
	 * @return a collection of WorkReportBoardMember or an empty list
	 */
	public Collection getAllWorkReportBoardMembersForWorkReportId(int workReportId) {
		try {
			return getWorkReportBoardMemberHome().findAllWorkReportBoardMembersByWorkReportIdOrderedByMemberName(
					workReportId);
		}
		catch (FinderException e) {
			return ListUtil.getEmptyList();
		}
	}

	/**
	 * Gets all WorkReportDivisionBoard for the specified WorkReport id
	 * 
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
	 * Gets all WorkReportDivisionBoard for the specified WorkReport and year.
	 * Compares the year and if they don't match it gets the correct workreport.
	 * 
	 * @param workReportId
	 * @return a collection of WorkReportDivisionBoard
	 */
	public Collection getAllWorkReportDivisionBoardForWorkReportIdAndYear(int workReportId, int year) {
		try {
			if (year > 0) {
				WorkReport wReport = getWorkReportById(workReportId);
				if (wReport.getYearOfReport().intValue() != year) {
					wReport = getWorkReportHome().findWorkReportByGroupIdAndYearOfReport(
							getWorkReportById(workReportId).getGroupId().intValue(), year);
					workReportId = ((Integer) wReport.getPrimaryKey()).intValue();
				}
			}
			return getWorkReportDivisionBoardHome().findAllWorkReportDivisionBoardByWorkReportId(workReportId);
		}
		catch (FinderException e) {
			return ListUtil.getEmptyList();
		}
	}

	/**
	 * Gets work report group.
	 * 
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
				System.err.println("[WorkReportBusiness] Could not find old WorkReportGroup (name: "
						+ workReportGroupName + " , year: " + year + " ) Message is: " + ex.getMessage());
				ex.printStackTrace(System.err);
				return null;
			}
		}
		return workReportGroup;
	}

	/**
	 * Changes the WorkReportGroup of the specified entity, that is the entity
	 * is removed from the specified current group and added to the specified
	 * new group. If the name of the current group is null the entity will only
	 * be added to the specified new group. If the name of the new group is null
	 * the entity will only be removed from the specified current group. But if
	 * one of the groups could not be found nothing happens and false is
	 * returned. If both specified names are null, nothing happens and true is
	 * returned. If the complete operation was successful true is returned else
	 * false.
	 * 
	 * @param workReportID
	 * @param nameOldGroup
	 * @param yearOldGroup
	 * @param nameNewGroup
	 * @param yearNewGroup
	 * @param entity
	 * @return true if successful else false.
	 */
	public boolean changeWorkReportGroupOfEntity(int workReportID, String nameOldGroup, int yearOldGroup,
			String nameNewGroup, int yearNewGroup, IDOEntity entity) {
		WorkReportGroup oldGroup = null;
		WorkReportGroup newGroup = null;
		// try to find work groups
		WorkReportGroupHome home = getWorkReportGroupHome();
		if (nameOldGroup != null) {
			try {
				oldGroup = home.findWorkReportGroupByNameAndYear(nameOldGroup, yearOldGroup);
			}
			catch (FinderException ex) {
				System.err.println("[WorkReportBusiness] Could not find old WorkReportGroup (name: " + nameOldGroup
						+ " , year: " + yearOldGroup + " ) Message is: " + ex.getMessage());
				ex.printStackTrace(System.err);
				return false;
			}
		}
		if (nameNewGroup != null) {
			try {
				newGroup = home.findWorkReportGroupByNameAndYear(nameNewGroup, yearNewGroup);
			}
			catch (FinderException ex) {
				System.err.println("[WorkReportBusiness] Could not find new WorkReportGroup (name: " + nameNewGroup
						+ " , year: " + yearNewGroup + " ) Message is: " + ex.getMessage());
				ex.printStackTrace(System.err);
				return false;
			}
		}
		return changeWorkReportGroupOfEntity(workReportID, oldGroup, newGroup, entity);
	}

	/**
	 * Changes the WorkReportGroup of the specified entity, that is the entity
	 * is removed from the specified current group and added to the specified
	 * new group. If the specified current group is null the entity will only be
	 * added to the specified new group. If the specified new group is null the
	 * entity will only be removed from the specified current group. If both
	 * specified groups are null nothing happens and true is returned. If the
	 * complete operation was successful true is returned else false.
	 * 
	 * @param oldGroup
	 * @param newGroup
	 * @param entity
	 * @return true if successful else false.
	 */
	public boolean changeWorkReportGroupOfEntity(int workReportID, WorkReportGroup oldGroup, WorkReportGroup newGroup,
			IDOEntity entity) {
		TransactionManager manager = com.idega.transaction.IdegaTransactionManager.getInstance();
		try {
			manager.begin();
			// add work report group to work report
			if (newGroup != null) {
				WorkReport workReport = getWorkReportById(workReportID);
				try {
					workReport.addLeague(newGroup);
				}
				catch (IDORelationshipException e) {
					System.err.println("Error adding relation to workreportgroup, maybe relation is already there");
				}
				try {
					newGroup.addEntity(entity);
				}
				catch (IDOAddRelationshipException e) {
					System.err.println("Error adding relation to workreportgroup, maybe relation is already there");
					//	e.printStackTrace();
				}
			}
			if (oldGroup != null) {
				try {
					oldGroup.removeEntity(entity);
				}
				catch (IDORemoveRelationshipException e) {
					e.printStackTrace();
					System.err.println("Error removing relation to workreportgroup, maybe there was no relation");
				}
			}
			manager.commit();
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
		return true;
	}

	/**
	 * Adds the specified WorkReportGroup to the specified entity. If the
	 * secified WorkReportGroup is null nothing happens and true is returned. If
	 * the complete operation was successful true is returned else false.
	 * 
	 * @param workReportGroupID
	 * @param newGroup
	 * @param entity
	 * @return true if successful else false.
	 */
	public boolean addWorkReportGroupToEntity(int workReportID, WorkReportGroup newGroup, IDOEntity entity) {
		return changeWorkReportGroupOfEntity(workReportID, null, newGroup, entity);
	}

	/**
	 * Removes the specified WorkReportGroup from the specified entity. If the
	 * secified WorkReportGroup is null nothing happens and true is returned. If
	 * the complete operation was successful true is returned else false.
	 * 
	 * @param workReportGroupID
	 * @param newGroup
	 * @param entity
	 * @return true if successful else false.
	 */
	public boolean removeWorkReportGroupFromEntity(int workReportID, WorkReportGroup oldGroup, IDOEntity entity) {
		return changeWorkReportGroupOfEntity(workReportID, oldGroup, null, entity);
	}

	/**
	 * Adds the specified WorkReportGroup to the specified entity. The entity is
	 * specified by the name and the year. If the secified WorkReportGroup is
	 * null nothing happens and true is returned. If the complete operation was
	 * successful true is returned else false.
	 * 
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
	 * Removes the specified WorkReportGroup from the specified entity. The
	 * entity is specified by the name and the year. If the secified
	 * WorkReportGroup is null nothing happens and true is returned. If the
	 * complete operation was successful true is returned else false.
	 * 
	 * @param newGroup
	 * @param year
	 * @param entity
	 * @return true if successful else false.
	 */
	public boolean removeWorkReportGroupFromEntity(int workReportID, String nameOldGroup, int year, IDOEntity entity) {
		return changeWorkReportGroupOfEntity(workReportID, nameOldGroup, year, null, year, entity);
	}

	public WorkReportGroup getMainBoardWorkReportGroup(int year) {
		String mainBoardName = getIWApplicationContext().getIWMainApplication().getBundle(
				ClubSelector.IW_BUNDLE_IDENTIFIER).getProperty(WorkReportConstants.WR_MAIN_BOARD_NAME, "ADA");
		WorkReportGroup group = findWorkReportGroupByNameAndYear(mainBoardName, year);
		return group;
	}

	public boolean createWorkReportData(int workReportId) throws RemoteException,FinderException {
		// get year and group id from work report
		WorkReport workReport = getWorkReportById(workReportId);
		if (canWeUpdateWorkReportDataFromDatabase(workReport.getYearOfReport().intValue())) {
			
			// get the corresponding group
			int groupId = workReport.getGroupId().intValue();
			// get group business
			GroupBusiness groupBusiness = getGroupBusiness();
			
			// create different data depending on the group type
			boolean isLeague = false;
			boolean isRegionalUnion = false;
			int year = workReport.getYearOfReport().intValue();
			Group group = groupBusiness.getGroupByGroupID(groupId);
			String groupType = group.getGroupType();
			
			isLeague = IWMemberConstants.GROUP_TYPE_LEAGUE.equals(groupType);
			isRegionalUnion = IWMemberConstants.GROUP_TYPE_REGIONAL_UNION.equals(groupType);
		
			if (!isClubUsingTheMemberSystem(group)) {
				// the group does not use the member system. The data has to
				// be imported by a file.
				// returns true because this is not an error.
				return true;
			}

			//now for some real work
			// start transaction
			//
			TransactionManager tm = IdegaTransactionManager.getInstance();
			try {
				tm.begin();
				boolean boardDataCreated = createWorkReportBoardData(workReportId, year, groupId,groupBusiness);
				boolean memberDataCreated = (isLeague || isRegionalUnion) ? true : createWorkReportMemberData(workReportId, groupId, groupBusiness);
				if (boardDataCreated && memberDataCreated) {
					// mark the sucessfull creation
					workReport.setCreationFromDatabaseDone(true);
					workReport.store();
					tm.commit();
					
					//update the stats
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
				tm.rollback();
				return false;
			}
			catch (Exception ex) {
				System.err.println("[WorkReportBusiness]: Couldn't create work report data. Message is: "+ ex.getMessage());
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
		return true;
	}

	private boolean createWorkReportBoardData(int workReportId, int year, int groupId,GroupBusiness groupBusiness) throws RemoteException {
		// add ADA league to the work report
		// create corresponding division board
		WorkReportGroup mainBoardGroup = getMainBoardWorkReportGroup(year);
		Map idExistingMemberMap = new HashMap();
		
		try {
			//so we don't duplicate them!
			try {
				getWorkReportDivisionBoardHome().findWorkReportDivisionBoardByWorkReportIdAndWorkReportGroupId(workReportId, ((Integer) mainBoardGroup.getPrimaryKey()).intValue());
			}
			catch (FinderException e) {
				try {
					createWorkReportDivisionBoard(workReportId, getGroupBusiness().getGroupByGroupID(mainBoardGroup.getGroupId().intValue()), mainBoardGroup);
				}
				catch (RemoteException e1) {
					e1.printStackTrace();
				}
				catch (FinderException e1) {
					e1.printStackTrace();
				}
			}
		}
		catch (CreateException ex) {
			System.err.println("[WorkreportBusiness] WorkReportDivisionBoard could not be created. Message is: "
					+ ex.getMessage());
			ex.printStackTrace(System.err);
		}
		

		// find all existing work report members
		Collection existingWorkReportBoardMembers = getAllWorkReportBoardMembersForWorkReportId(workReportId);
		if (existingWorkReportBoardMembers != null && !existingWorkReportBoardMembers.isEmpty()) {
			// create a map with user ids as keys and leagues as values
			Iterator existingWorkReportBoardMembersIterator = existingWorkReportBoardMembers.iterator();
			while (existingWorkReportBoardMembersIterator.hasNext()) {
				WorkReportBoardMember member = (WorkReportBoardMember) existingWorkReportBoardMembersIterator.next();
				Integer userId = new Integer(member.getUserId());
				Collection memberLeagues = (Collection) idExistingMemberMap.get(userId);
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
		}
		
		//1. find all divisions under club and the main comittee group.
		//2. read the main comittee group and register the users to the main board (wrgroup)
		//3. for each division get the league (wrgroup) and find the committee for it (either a metadata or just the main board)
		Collection mainComm = null;
		Collection divisions = null;
		Collection mainBoardUsers = null;
		try {
			Group club = groupBusiness.getGroupByGroupID(groupId);
		}
		catch (FinderException e1) {
			e1.printStackTrace();
			return false;
		}
		
		List divisionType = new ArrayList();
		divisionType.add(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION);
		List mainCommittee = new ArrayList();
		mainCommittee.add(IWMemberConstants.GROUP_TYPE_CLUB_COMMITTEE_MAIN);
		
		mainComm = groupBusiness.getChildGroupsRecursiveResultFiltered(groupId,mainCommittee,true);
		divisions = groupBusiness.getChildGroupsRecursiveResultFiltered(groupId,divisionType,true);
		
		//
		// create work report board members
		//
		if(mainComm!=null && !mainComm.isEmpty()){
			Group committee = (Group)mainComm.iterator().next();
			try {
				mainBoardUsers = groupBusiness.getUsers(committee);
			}
			catch (FinderException e) {
				System.out.print("[WorkReportBusiness] NO USERS IN MAIN COMMITTEE (group id) : "+committee.getPrimaryKey());
			}
			
			if(mainBoardUsers!=null && !mainBoardUsers.isEmpty()){
				createWorkReportBoardMembers(mainBoardUsers, workReportId, mainBoardGroup, idExistingMemberMap);
			}
		}
		else{
			return false; //there must be a main board in the club!!
		}
		
		if(divisions!=null && !divisions.isEmpty()){
			Iterator divs = divisions.iterator();
			while(divs.hasNext()){
				
				//if there is only one division is that handled differently?
				Group divisionGroup = (Group)divs.next(); 

				WorkReportGroup league = getLeagueFromClubDivision(divisionGroup, year);
				// get users
				//The behaviour used to be that if the division has no board (and therefor users) we used the mainboard users.
//				Passing the mainboard users is no longer used but I did not remove it because they might want that behaviour back.
				if(league!=null){
					Collection users = getBoardUsersFromClubDivision(divisionGroup,mainBoardUsers);
					if(users!=null && !users.isEmpty()){
						//create the board members
						createWorkReportBoardMembers(users, workReportId, league, idExistingMemberMap);
					}
					// should this be done for the mainboard also??
					// should it be created if there are no users? I don't think so because it is just contact info and stuff?
					try {
						createWorkReportDivisionBoard(workReportId, divisionGroup, league);
					}
					catch (CreateException ex) {
						System.err.println("[WorkreportBusiness] WorkReportDivisionBoard could not be created. Message is: "
								+ ex.getMessage());
						ex.printStackTrace(System.err);
					}
				}else{
					System.err.println("[WorkReportBusiness] A league connection is missing for the division group: "+divisionGroup.getName()+" id:"+divisionGroup.getPrimaryKey());
				}
			}
		}
					
						
						
	
		return true;
	}

	/**
	 * update the statistics
	 */
	public void updateWorkReportData(int workReportId) throws FinderException, IDOException, RemoteException {
		/*
		 * Some rules: + members all always connected to the work report group
		 * ADA + members that are only connected to the work report group ADA
		 * aren't players + members that are connected to some other work report
		 * groups (leagues) are players
		 */
		// first update work report
		String mainBoardName = getIWApplicationContext().getIWMainApplication().getBundle(
				ClubSelector.IW_BUNDLE_IDENTIFIER).getProperty(WorkReportConstants.WR_MAIN_BOARD_NAME, "ADA");
		Collection members = getAllWorkReportMembersForWorkReportId(workReportId);
		// create map: member as key, leagues as value
		Map leagueCountMap = new HashMap();
		int playersCount = 0;
		int membersTotalSum = members.size();
		Iterator membersIterator = members.iterator();
		while (membersIterator.hasNext()) {
			WorkReportMember member = (WorkReportMember) membersIterator.next();
			Iterator leagues = member.getLeaguesForMember().iterator();
			while (leagues.hasNext()) {
				WorkReportGroup league = (WorkReportGroup) leagues.next();
				String leagueName = league.getName();
				if (!mainBoardName.equals(leagueName)) {
					// do not count members connected to the mainboard ADA
					playersCount++;
					Integer leagueId = (Integer) league.getPrimaryKey();
					Integer count = (Integer) leagueCountMap.get(leagueId);
					count = (count == null) ? new Integer(1) : new Integer((count.intValue()) + 1);
					leagueCountMap.put(leagueId, count);
				}
			}
		}
		WorkReport workReport = getWorkReportById(workReportId);
		workReport.setNumberOfMembers(membersTotalSum);
		workReport.setNumberOfPlayers(playersCount);
		workReport.store();
		// second update work report divisions
		WorkReportDivisionBoardHome home = getWorkReportDivisionBoardHome();
		Collection boards = home.findAllWorkReportDivisionBoardByWorkReportId(workReportId);
		Iterator iterator = boards.iterator();
		while (iterator.hasNext()) {
			WorkReportDivisionBoard board = (WorkReportDivisionBoard) iterator.next();
			WorkReportGroup workReportGroup = board.getLeague();
			Integer leagueId = (Integer) workReportGroup.getPrimaryKey();
			Integer number = (Integer) leagueCountMap.get(leagueId);
			if (number == null) {
				board.setNumberOfPlayers(0);
			}
			else {
				board.setNumberOfPlayers(number.intValue());
			}
			board.store();
		}
	}


	private WorkReportGroup getLeagueFromClubDivision(Group clubDivision, int year) {
		WorkReportGroup league = null;
		String leagueIdAsString = clubDivision.getMetaData(IWMemberConstants.META_DATA_DIVISION_LEAGUE_CONNECTION);
		if (leagueIdAsString != null && !"".equals(leagueIdAsString)) {
			try {
				Integer leagueId = new Integer(leagueIdAsString);
				league = getWorkReportGroupHome().findWorkReportGroupByGroupIdAndYear(leagueId.intValue(), year);
			}
			catch (NumberFormatException formatEx) {
				System.err.println("[workReportBusiness] league id ( " + leagueIdAsString+ " ) is not a number. Message is: " + formatEx.getMessage());
				formatEx.printStackTrace();
			}
			catch (FinderException ex) {
				System.err.println("[WorkreportBusiness] league with id " + leagueIdAsString+ " could not be found. Message is: " + ex.getMessage());
				ex.printStackTrace();
			}
		}
		return league;
	}

	private Collection getBoardUsersFromClubDivision(Group clubDivision, Collection mainBoardUsers) throws RemoteException {
		Collection users = null;
		//if the connection is null you should use the main board group of the club
		String divisionBoardIdAsString = clubDivision.getMetaData(IWMemberConstants.META_DATA_DIVISION_BOARD);
		if (divisionBoardIdAsString != null && !"".equals(divisionBoardIdAsString)) {
			divisionBoardIdAsString = divisionBoardIdAsString.substring(divisionBoardIdAsString.lastIndexOf("_") + 1);
			try {
				Integer divisionBoardId = new Integer(divisionBoardIdAsString);
				Group divisionBoard = getGroupHome().findByPrimaryKey(divisionBoardId);
				users = getGroupBusiness().getUsers(divisionBoard);
			
			}
			catch (NumberFormatException formatException) {
				System.err.println("[workReportBusiness] division board id ( " + divisionBoardIdAsString
						+ " ) is not a number. Message is: " + formatException.getMessage());
				formatException.printStackTrace(System.err);
			}
			catch (FinderException ex) {
				System.err.println("[WorkreportBusiness] Group with id " + divisionBoardIdAsString
						+ " could not be found. Message is: " + ex.getMessage());
				ex.printStackTrace(System.err);
			}
		}
		else{
			//ISI requested that if there is no board for the division it should show up empty or not at all.
			//users = mainBoardUsers;
			
		}
		
		return users;
	}

	public DropdownMenu getYearDropdownMenu(int selectedYear) {
		DropdownMenu dateSelector = new DropdownMenu(WorkReportConstants.WR_SESSION_PARAM_WORK_REPORT_YEAR);
		IWTimestamp stamp = IWTimestamp.RightNow();
		int currentYear = stamp.getYear();
		int beginningYear = 2001;//Because we have no older data, could also be
								 // an application setting
		for (int i = beginningYear; i <= currentYear; i++) {
			dateSelector.addMenuElement(i, Integer.toString(i));
		}
		if (selectedYear >= beginningYear) {
			dateSelector.setSelectedElement(selectedYear);
		}
		else {
			dateSelector.setSelectedElement(currentYear);
		}
		return dateSelector;
	}

	private boolean createWorkReportBoardMembers(Collection users, int workReportId, WorkReportGroup league,Map idExistingMemberMap) {
		if (users == null) {
			return false;
		}
		Iterator userIterator = users.iterator();
		while (userIterator.hasNext()) {
			User user = (User) userIterator.next();
			Integer primaryKeyUser = (Integer) user.getPrimaryKey();
			// create a member per league (that is one user can have one or many
			// members)
			Collection memberLeagues = (Collection) idExistingMemberMap.get(primaryKeyUser);
			// note: league can be null
			if (memberLeagues != null && (memberLeagues.contains(league))) {
				// nothing to do
				return true;
			}
			try {
				// create WorkReportBoardMember
				createWorkReportBoardMember(workReportId, user, league);
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
				System.err.println("[WorkReportBusiness] Couldn't create WorkreportBoardMember. Message is: "
						+ createEx.getMessage());
				createEx.printStackTrace(System.err);
			}
		}
		return true;
	}

	private boolean createWorkReportMemberData(int workReportId, int groupId,GroupBusiness groupBusiness) throws RemoteException {
		//  get the year of the work report
		int year = getWorkReportById(workReportId).getYearOfReport().intValue();
		// get the ADA work report group
		WorkReportGroup mainBoardGroup = getMainBoardWorkReportGroup(year);
		// find all existing work report members
		Collection existingWorkReportMembers = getAllWorkReportMembersForWorkReportId(workReportId);
		Map idExistingMember = new HashMap();
		// create a collection with user ids
		if (existingWorkReportMembers != null && !existingWorkReportMembers.isEmpty()) {
			Iterator existingWorkReportMembersIterator = existingWorkReportMembers.iterator();
			while (existingWorkReportMembersIterator.hasNext()) {
				WorkReportMember workReportMember = (WorkReportMember) existingWorkReportMembersIterator.next();
				Integer userId = new Integer(workReportMember.getUserId());
				idExistingMember.put(userId, workReportMember);
			}
		}
		//1. Get all users under the club and register them to the main board.
		//2. Get all division groups and their WR league
		//3. Get all player groups under each division
		//4. Get all users under player groups and register them to the
		// division
		Collection childGroups;
		List divisionType = new ArrayList();
		divisionType.add(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION);
		List playerType = new ArrayList();
		playerType.add(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER);
		//1.
		Collection allUsers;
		try {
			//TODO members in temporary and staff groups are not members!
			//do not register those people
			allUsers = groupBusiness.getUsersFromGroupRecursive(groupBusiness.getGroupByGroupID(groupId));
		}
		catch (FinderException e) {
			e.printStackTrace();
			return false;
		}
		Iterator userIterator = allUsers.iterator();
		while (userIterator.hasNext()) {
			User user = (User) userIterator.next();
			Integer userPrimaryKey = (Integer) user.getPrimaryKey();
			WorkReportMember existingMember = (WorkReportMember) idExistingMember.get(userPrimaryKey);
			//only add if not added before
			if (existingMember == null) {
				try {
					existingMember = createWorkReportMember(workReportId, userPrimaryKey);
					//sometimes saving the member failes, most likely do to a
					// too long personal id (can only be 10 digits as in
					// Iceland)
					if (existingMember == null)
						continue;
					// add ADA league to member
					addWorkReportGroupToEntity(workReportId, mainBoardGroup, existingMember);
					idExistingMember.put(userPrimaryKey, existingMember);
				}
				catch (CreateException ex) {
					System.err.println("[WorkReportBusiness]: Can't create member. Message is: " + ex.getMessage());
					ex.printStackTrace(System.err);
				}
			}
		}
		//2.
		Collection divisions = groupBusiness.getChildGroupsRecursiveResultFiltered(groupId, divisionType, true);
		if (divisions != null && !divisions.isEmpty()) {
			Iterator divs = divisions.iterator();
			while (divs.hasNext()) {
				Group group = (Group) divs.next();
				WorkReportGroup workReportGroup = getLeagueFromClubDivision(group, year);
				if(workReportGroup==null){
					System.err.println("[WorkReportBusiness] The club division "+group.getName()+" is not connected to a league!!");
					continue;
				}
				//3.
				Collection playerGroups = groupBusiness.getChildGroupsRecursiveResultFiltered(group, playerType, true);
				if (playerGroups != null && !playerGroups.isEmpty()) {
					Iterator players = playerGroups.iterator();
					while (players.hasNext()) {
						Group playerGroup = (Group) players.next();
						//4.
						Collection users = null;
						try {
							users = groupBusiness.getUsersRecursive(playerGroup);
						}
						catch (FinderException e1) {
							//no users found under group
						}
						if (users != null && !users.isEmpty()) {
							Iterator userIter = users.iterator();
							while (userIter.hasNext()) {
								User user = (User) userIter.next();
								//the player is also a member so it should be
								// in there
								WorkReportMember wrMember = (WorkReportMember) idExistingMember.get(user.getPrimaryKey());
								//do we need to check if it exists first?
								if (wrMember != null) {
									addWorkReportGroupToEntity(workReportId, workReportGroup, wrMember);
								}
								else {
									System.err.print("[WorkReportBusiness] error getting user with id "
											+ user.getPrimaryKey() + " from existing workreport members map");
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	public boolean isWorkReportReadOnly(int workReportId) {
		WorkReport report = getWorkReportById(workReportId);
		boolean isReadOnly = areAllWorkReportsTemporarelyReadOnly();
		if (isReadOnly) {
			return true;
		}
		//check if we are in the allowed timespan
		Date fromDate = this.getWorkReportOpenFromDate();
		Date toDate = this.getWorkReportOpenToDate();
		IWTimestamp now = IWTimestamp.RightNow();
		if (fromDate != null && toDate != null) {
			isReadOnly = !(now.isBetween(new IWTimestamp(fromDate), new IWTimestamp(toDate)));
			if (isReadOnly) {
				return true;
			}
		}
		return report.isSent();
	}

	private boolean canWeUpdateWorkReportDataFromDatabase(int year) {
		boolean update = true;
		//don't update if read only
		update = !areAllWorkReportsTemporarelyReadOnly();
		//check if is in allowed timespan
		update = isWorkReportYearWithinYearLimits(year);
		return update;
	}

	/**
	 * If there are no date limits set to the workreports this returns true
	 * otherwize the year must be within the other two.
	 * 
	 * @param year
	 * @return
	 */
	private boolean isWorkReportYearWithinYearLimits(int year) {
		Date fromDate = getWorkReportOpenFromDate();
		Date toDate = getWorkReportOpenToDate();
		if (fromDate != null && toDate != null) {
			//check if we are in the allowed timespan
			return ((year >= (new IWTimestamp(fromDate)).getYear()) && (year <= (new IWTimestamp(toDate)).getYear()));
		}
		return true;
	}

	public Date getWorkReportOpenFromDate() {
		String fDate = getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getProperty(
				WorkReportConstants.WR_BUNDLE_PARAM_FROM_DATE);
		try {
			if (fDate != null) {
				return (new IWTimestamp(fDate)).getDate();
			}
		}
		catch (java.lang.IllegalArgumentException ex) {
			ex.printStackTrace();
			return null;
		}
		return null;
	}

	public Date getWorkReportOpenToDate() {
		String tDate = getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getProperty(
				WorkReportConstants.WR_BUNDLE_PARAM_TO_DATE);
		try {
			if (tDate != null) {
				return (new IWTimestamp(tDate)).getDate();
			}
		}
		catch (java.lang.IllegalArgumentException ex) {
			ex.printStackTrace();
			return null;
		}
		return null;
	}

	public void setWorkReportOpenFromDateWithDateString(String dateString) {
		getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).setProperty(
				WorkReportConstants.WR_BUNDLE_PARAM_FROM_DATE, dateString);
	}

	public void setWorkReportOpenToDateWithDateString(String dateString) {
		getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).setProperty(
				WorkReportConstants.WR_BUNDLE_PARAM_TO_DATE, dateString);
	}

	public void setAllWorkReportsTemporarelyReadOnly() {
		setAllWorkReportsTemporarelyReadOnlyFlag(true);
	}

	public void setAllWorkReportsTemporarelyReadOnlyFlag(boolean setAllAsReadOnly) {
		if (setAllAsReadOnly) {
			getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).setProperty(
					WorkReportConstants.WR_BUNDLE_PARAM_TEMP_CLOSED, "TRUE");
		}
		else {
			getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).setProperty(
					WorkReportConstants.WR_BUNDLE_PARAM_TEMP_CLOSED, "FALSE");
		}
	}

	public void removeWorkReportsTemporarelyReadOnlyFlag() {
		setAllWorkReportsTemporarelyReadOnlyFlag(false);
	}

	public boolean areAllWorkReportsTemporarelyReadOnly() {
		return "TRUE".equals(getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getProperty(
				WorkReportConstants.WR_BUNDLE_PARAM_TEMP_CLOSED));
	}

	
	public boolean sendWorkReport(int workReportId, String reportText, IWResourceBundle iwrb) throws RemoteException {
		WorkReport report = getWorkReportById(workReportId);
		report.setAsSent(true);
		report.setSentReportText(reportText);
		//if some is done set status to partial else if it is not set yet to
		// not done
		if (report.isMembersPartDone() || report.isAccountPartDone() || report.isBoardPartDone()) {
			report.setStatus(WorkReportConstants.WR_STATUS_SOME_DONE);
		}
		else {
			report.setStatus(WorkReportConstants.WR_STATUS_NOT_DONE);
		}
		report.store();
		String subject = iwrb.getLocalizedString("work_report_send.email_subject",
				"IWMember workreport sent announcement");
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
					toEmailAddress = ((Email) toRegionalEmails.iterator().next()).getEmailAddress();
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
					String toEmailAddress = ((Email) fedEmails.iterator().next()).getEmailAddress();
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
		try {
			records = recHome.findAllRecordsByWorkReportId(workReportId);
		}
		catch (FinderException e1) {
			System.out.println("No account records for work report id : " + workReportId);
			return false; //no records
		}
		WorkReport report = this.getWorkReportById(workReportId);
		try {
			report.getLeagues();
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
		Iterator recs = records.iterator();
		//the real check happens here
		while (recs.hasNext()) {
			WorkReportClubAccountRecord rec = (WorkReportClubAccountRecord) recs.next();
			if (emptyLeagues.contains(new Integer(rec.getWorkReportGroupId()))) {
				System.out.println("Empty divisions with account record found! workreportgroupid : "
						+ rec.getWorkReportGroupId());
				return true;
			}
		}
		return false;
	}

	public boolean isBoardMissingForDivisionWithMembersOrYearlyAccount(int workReportId) {
		WorkReportBoardMemberHome boardHome = getWorkReportBoardMemberHome();
		WorkReportClubAccountRecordHome recHome = getWorkReportClubAccountRecordHome();
		Collection records = null;
		boolean checkForAccount = false;
		WorkReport report = this.getWorkReportById(workReportId);
		try {
			report.getLeagues();
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
			Integer workGroupID = (Integer) primaryKeys.next();
			try {
				boardHome.findAllWorkReportBoardMembersByWorkReportIdAndWorkReportGroupId(workReportId,
						workGroupID.intValue());
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
				WorkReportClubAccountRecord rec = (WorkReportClubAccountRecord) recs.next();
				leaguesMap.put(new Integer(rec.getWorkReportGroupId()), new Integer(rec.getWorkReportGroupId()));
			}
			Iterator iter = leaguesMap.keySet().iterator();
			while (iter.hasNext()) {
				Integer wrGroupId = (Integer) iter.next();
				try {
					boardHome.findAllWorkReportBoardMembersByWorkReportIdAndWorkReportGroupId(workReportId,
							wrGroupId.intValue());
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
			WorkReportGroup league = (WorkReportGroup) iter.next();
			int count = getCountOfPlayersByWorkReportAndWorkReportGroup(report, league);
			if (count < 1) {
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
			WorkReportGroup league = (WorkReportGroup) iter.next();
			int count = getCountOfPlayersByWorkReportAndWorkReportGroup(report, league);
			if (count > 0) {
				nonEmptyLeagues.add(league.getPrimaryKey());
			}
		}
		return nonEmptyLeagues;
	}

	public boolean isYearlyAccountMissingForADivisionWithMembers(int workReportId) {
		WorkReportClubAccountRecordHome recHome = getWorkReportClubAccountRecordHome();
		WorkReport report = this.getWorkReportById(workReportId);
		try {
			report.getLeagues();
		}
		catch (IDOException e) {
			System.out.println("No divisions for work report id : " + workReportId);
			return false; //no divisions
		}
		try {
			recHome.findAllRecordsByWorkReportId(workReportId);
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
		Iterator primaryKeys = nonEmptyLeagues.iterator();
		//the real check happens here
		while (primaryKeys.hasNext()) {
			Integer workGroupID = (Integer) primaryKeys.next();
			try {
				recHome.findAllRecordsByWorkReportIdAndWorkReportGroupId(workReportId, workGroupID.intValue());
			}
			catch (FinderException e2) {
				System.out.println("Account recs missing for a division");
				//e2.printStackTrace();
				return true;
			}
		}
		return false;
	}

	public WorkReportDivisionBoardHome getWorkReportDivisionBoardHome() {
		if (workReportDivisionBoardHome == null) {
			try {
				workReportDivisionBoardHome = (WorkReportDivisionBoardHome) IDOLookup.getHome(WorkReportDivisionBoard.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportDivisionBoardHome;
	}

	public WorkReportBoardMemberHome getWorkReportBoardMemberHome() {
		if (workReportBoardMemberHome == null) {
			try {
				workReportBoardMemberHome = (WorkReportBoardMemberHome) IDOLookup.getHome(WorkReportBoardMember.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportBoardMemberHome;
	}

	public WorkReportClubAccountRecordHome getWorkReportClubAccountRecordHome() {
		if (workReportClubAccountRecordHome == null) {
			try {
				workReportClubAccountRecordHome = (WorkReportClubAccountRecordHome) IDOLookup.getHome(WorkReportClubAccountRecord.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return workReportClubAccountRecordHome;
	}

	public Collection getWorkReportsByYearRegionalUnionsAndClubs(int year, Collection regionalUnionsFilter,
			Collection clubsFilter) {
		try {
			return getWorkReportHome().findAllWorkReportsByYearRegionalUnionsAndClubsOrderedByRegionalUnionNameAndClubName(
					year, regionalUnionsFilter, clubsFilter);
		}
		catch (FinderException e) {
			return ListUtil.getEmptyList();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeUserRemove(com.idega.user.data.User)
	 */
	public void beforeUserRemove(User user) throws RemoveException, RemoteException {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterUserCreate(com.idega.user.data.User)
	 */
	public void afterUserCreateOrUpdate(User user) throws CreateException, RemoteException {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeGroupRemove(com.idega.user.data.Group)
	 */
	public void beforeGroupRemove(Group group) throws RemoveException, RemoteException {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterGroupCreate(com.idega.user.data.Group)
	 */
	public void afterGroupCreateOrUpdate(Group group) throws CreateException, RemoteException {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getPresentationObjectClass()
	 */
	public Class getPresentationObjectClass() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateEditor(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateViewer(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getUserPropertiesTabs(com.idega.user.data.User)
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupPropertiesTabs(com.idega.user.data.Group)
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getMainToolbarElements()
	 */
	public List getMainToolbarElements() throws RemoteException {
		List list = new ArrayList(1);
		list.add(new WorkReportWindowPlugin());
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupToolbarElements(com.idega.user.data.Group)
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getListViewerFields()
	 */
	public Collection getListViewerFields() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#findGroupsByFields(java.util.Collection,
	 *      java.util.Collection, java.util.Collection)
	 */
	public Collection findGroupsByFields(Collection listViewerFields, Collection finderOperators,
			Collection listViewerFieldValues) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserAssignableFromGroupToGroup(com.idega.user.data.User,
	 *      com.idega.user.data.Group, com.idega.user.data.Group)
	 */
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserSuitedForGroup(com.idega.user.data.User,
	 *      com.idega.user.data.Group)
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup) {
		// TODO Auto-generated method stub
		return null;
	}
} //end of class
