/*
 * $Id: WorkReportBusiness.java,v 1.56 2004/09/07 23:04:37 eiki Exp $
 * Created on Sep 7, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.reports.business;

import is.idega.idegaweb.member.business.MemberUserBusiness;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportAccountKeyHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMember;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportBoardMemberHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportClubAccountRecordHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportDivisionBoard;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportDivisionBoardHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroupHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportHome;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportMemberHome;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.idega.business.IBOService;
import com.idega.data.IDOEntity;
import com.idega.data.IDOException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * 
 *  Last modified: $Date: 2004/09/07 23:04:37 $ by $Author: eiki $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.56 $
 */
public interface WorkReportBusiness extends IBOService,MemberUserBusiness{
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getTotalCountOfMembersForWorkReportYear
 */
public int getTotalCountOfMembersForWorkReportYear(int year) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getTotalCountOfPlayersForWorkReportYear
 */
public int getTotalCountOfPlayersForWorkReportYear(int year) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getTotalCountOfWorkReportsByStatusAndYear
 */
public int getTotalCountOfWorkReportsByStatusAndYear(String status, int year) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getTotalCountOfCompetitorsForWorkReportYear
 */
public int getTotalCountOfCompetitorsForWorkReportYear(int year) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfPlayersByWorkReportAndWorkReportGroup
 */
public int getCountOfPlayersByWorkReportAndWorkReportGroup(WorkReport report, WorkReportGroup league) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfMalePlayersByWorkReportAndWorkReportGroup
 */
public int getCountOfMalePlayersByWorkReportAndWorkReportGroup(WorkReport report, WorkReportGroup league) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfFemalePlayersByWorkReportAndWorkReportGroup
 */
public int getCountOfFemalePlayersByWorkReportAndWorkReportGroup(WorkReport report, WorkReportGroup league) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfMembersByWorkReport
 */
public int getCountOfMembersByWorkReport(WorkReport report) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfMaleMembersByWorkReport
 */
public int getCountOfMaleMembersByWorkReport(WorkReport report) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfFemaleMembersByWorkReport
 */
public int getCountOfFemaleMembersByWorkReport(WorkReport report) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfPlayersByWorkReport
 */
public int getCountOfPlayersByWorkReport(WorkReport report) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfMalePlayersByWorkReport
 */
public int getCountOfMalePlayersByWorkReport(WorkReport report) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfFemalePlayersByWorkReport
 */
public int getCountOfFemalePlayersByWorkReport(WorkReport report) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfPlayersOfEqualAgeAndGenderByWorkReportAndWorkReportGroup
 */
public int getCountOfPlayersOfEqualAgeAndGenderByWorkReportAndWorkReportGroup(int age, String genderMorF, WorkReport report, WorkReportGroup league) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfPlayersOfEqualAgeAndByWorkReportAndWorkReportGroup
 */
public int getCountOfPlayersOfEqualAgeAndByWorkReportAndWorkReportGroup(int age, WorkReport report, WorkReportGroup league) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfMalePlayersOfEqualAgeAndByWorkReportAndWorkReportGroup
 */
public int getCountOfMalePlayersOfEqualAgeAndByWorkReportAndWorkReportGroup(int age, WorkReport report, WorkReportGroup league) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfFemalePlayersOfEqualAgeAndByWorkReportAndWorkReportGroup
 */
public int getCountOfFemalePlayersOfEqualAgeAndByWorkReportAndWorkReportGroup(int age, WorkReport report, WorkReportGroup league) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfPlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup
 */
public int getCountOfPlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(int age, WorkReport report, WorkReportGroup league) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup
 */
public int getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(int age, WorkReport report, WorkReportGroup league) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup
 */
public int getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReportAndWorkReportGroup(int age, WorkReport report, WorkReportGroup league) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfMembersEqualOrOlderThanAgeAndByWorkReport
 */
public int getCountOfMembersEqualOrOlderThanAgeAndByWorkReport(int age, WorkReport report) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfMaleMembersEqualOrOlderThanAgeAndByWorkReport
 */
public int getCountOfMaleMembersEqualOrOlderThanAgeAndByWorkReport(int age, WorkReport report) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfFemaleMembersEqualOrOlderThanAgeAndByWorkReport
 */
public int getCountOfFemaleMembersEqualOrOlderThanAgeAndByWorkReport(int age, WorkReport report) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfPlayersEqualOrOlderThanAgeAndByWorkReport
 */
public int getCountOfPlayersEqualOrOlderThanAgeAndByWorkReport(int age, WorkReport report) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReport
 */
public int getCountOfMalePlayersEqualOrOlderThanAgeAndByWorkReport(int age, WorkReport report) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReport
 */
public int getCountOfFemalePlayersEqualOrOlderThanAgeAndByWorkReport(int age, WorkReport report) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfPlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup
 */
public int getCountOfPlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(int age, WorkReport report, WorkReportGroup league) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup
 */
public int getCountOfMalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(int age, WorkReport report, WorkReportGroup league) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup
 */
public int getCountOfFemalePlayersOfYoungerAgeAndByWorkReportAndWorkReportGroup(int age, WorkReport report, WorkReportGroup league) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfMembersOfYoungerAgeAndByWorkReport
 */
public int getCountOfMembersOfYoungerAgeAndByWorkReport(int age, WorkReport report) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfMaleMembersOfYoungerAgeAndByWorkReport
 */
public int getCountOfMaleMembersOfYoungerAgeAndByWorkReport(int age, WorkReport report) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfFemaleMembersOfYoungerAgeAndByWorkReport
 */
public int getCountOfFemaleMembersOfYoungerAgeAndByWorkReport(int age, WorkReport report) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfPlayersOfYoungerAgeAndByWorkReport
 */
public int getCountOfPlayersOfYoungerAgeAndByWorkReport(int age, WorkReport report) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfMalePlayersOfYoungerAgeAndByWorkReport
 */
public int getCountOfMalePlayersOfYoungerAgeAndByWorkReport(int age, WorkReport report) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getCountOfFemalePlayersOfYoungerAgeAndByWorkReport
 */
public int getCountOfFemalePlayersOfYoungerAgeAndByWorkReport(int age, WorkReport report) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getOrCreateWorkReportIdForGroupIdByYear
 */
public int getOrCreateWorkReportIdForGroupIdByYear(int groupId, int year, boolean createData) throws RemoteException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getWorkReportExpensesByWorkReportIdAndWorkReportGroupId
 */
public int getWorkReportExpensesByWorkReportIdAndWorkReportGroupId(int reportId, int wrGroupId) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getAmmountOfAccountRecordByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyId
 */
public int getAmmountOfAccountRecordByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyId(int reportId, int wrGroupId, int wrAccountKeyId) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getAmmountOfAccountRecordByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyName
 */
public int getAmmountOfAccountRecordByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyName(int reportId, int wrGroupId, String accountKeyName) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getTotalAmmountOfAccountRecordsByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyCollection
 */
public int getTotalAmmountOfAccountRecordsByWorkReportIdWorkReportGroupIdAndWorkReportAccountKeyCollection(int reportId, int wrGroupId, Collection accountKeys) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getWorkReportHome
 */
public WorkReportHome getWorkReportHome() throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getWorkReportMemberHome
 */
public WorkReportMemberHome getWorkReportMemberHome() throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getWorkReportGroupHome
 */
public WorkReportGroupHome getWorkReportGroupHome() throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getWorkReportAccountKeyHome
 */
public WorkReportAccountKeyHome getWorkReportAccountKeyHome() throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getWorkReportsForRegionalUnionCollection
 */
public Collection getWorkReportsForRegionalUnionCollection(int year, Collection regionalUnionsGroups) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#createWorkReportMember
 */
public WorkReportMember createWorkReportMember(int reportID, String personalID) throws CreateException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#createWorkReportMember
 */
public WorkReportMember createWorkReportMember(int reportID, Integer userId) throws CreateException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#createWorkReportMember
 */
public WorkReportMember createWorkReportMember(int reportID, User user) throws CreateException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#createWorkReportBoardMember
 */
public WorkReportBoardMember createWorkReportBoardMember(int reportID, String personalID, WorkReportGroup workReportGroup) throws CreateException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#createWorkReportBoardMember
 */
public WorkReportBoardMember createWorkReportBoardMember(int reportID, User user, WorkReportGroup workReportGroup) throws CreateException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#createWorkReportDivisionBoard
 */
public WorkReportDivisionBoard createWorkReportDivisionBoard(int reportId, Group clubDivision, WorkReportGroup league) throws CreateException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getWorkReportById
 */
public WorkReport getWorkReportById(int id) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getLeaguesOfWorkReportById
 */
public Collection getLeaguesOfWorkReportById(int id) throws IDOException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getFileName
 */
public String getFileName(int id) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#createOrUpdateLeagueWorkReportGroupsForYear
 */
public void createOrUpdateLeagueWorkReportGroupsForYear(int year) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#deleteWorkReportMembersForReport
 */
public void deleteWorkReportMembersForReport(int reportId) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#deleteWorkReportBoardMembersForReport
 */
public void deleteWorkReportBoardMembersForReport(int reportId) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#deleteWorkReportAccountRecordsForReport
 */
public void deleteWorkReportAccountRecordsForReport(int reportId) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getAllWorkReportGroupsForYearAndType
 */
public Collection getAllWorkReportGroupsForYearAndType(int year, String type) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getAllWorkReportsForYear
 */
public Collection getAllWorkReportsForYear(int year) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getAllLeagueWorkReportGroupsForYear
 */
public Collection getAllLeagueWorkReportGroupsForYear(int year) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getAllWorkReportMembersForWorkReportId
 */
public Collection getAllWorkReportMembersForWorkReportId(int workReportId) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getAllWorkReportMembersForWorkReportIdAndWorkReportGroupId
 */
public Collection getAllWorkReportMembersForWorkReportIdAndWorkReportGroupId(int workReportId, WorkReportGroup workReportGroup) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getAllWorkReportBoardMembersForWorkReportId
 */
public Collection getAllWorkReportBoardMembersForWorkReportId(int workReportId) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getAllWorkReportDivisionBoardForWorkReportId
 */
public Collection getAllWorkReportDivisionBoardForWorkReportId(int workReportId) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getAllWorkReportDivisionBoardForWorkReportIdAndYear
 */
public Collection getAllWorkReportDivisionBoardForWorkReportIdAndYear(int workReportId, int year) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#findWorkReportGroupByNameAndYear
 */
public WorkReportGroup findWorkReportGroupByNameAndYear(String workReportGroupName, int year) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#changeWorkReportGroupOfEntity
 */
public boolean changeWorkReportGroupOfEntity(int workReportID, String nameOldGroup, int yearOldGroup, String nameNewGroup, int yearNewGroup, IDOEntity entity) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#changeWorkReportGroupOfEntity
 */
public boolean changeWorkReportGroupOfEntity(int workReportID, WorkReportGroup oldGroup, WorkReportGroup newGroup, IDOEntity entity) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#addWorkReportGroupToEntity
 */
public boolean addWorkReportGroupToEntity(int workReportID, WorkReportGroup newGroup, IDOEntity entity) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#removeWorkReportGroupFromEntity
 */
public boolean removeWorkReportGroupFromEntity(int workReportID, WorkReportGroup oldGroup, IDOEntity entity) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#addWorkReportGroupToEntity
 */
public boolean addWorkReportGroupToEntity(int workReportID, String nameNewGroup, int year, IDOEntity entity) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#removeWorkReportGroupFromEntity
 */
public boolean removeWorkReportGroupFromEntity(int workReportID, String nameOldGroup, int year, IDOEntity entity) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getMainBoardWorkReportGroup
 */
public WorkReportGroup getMainBoardWorkReportGroup(int year) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#createWorkReportData
 */
public boolean createWorkReportData(int workReportId) throws RemoteException,FinderException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#updateWorkReportData
 */
public void updateWorkReportData(int workReportId) throws FinderException,IDOException,RemoteException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getYearDropdownMenu
 */
public DropdownMenu getYearDropdownMenu(int selectedYear) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#isWorkReportReadOnly
 */
public boolean isWorkReportReadOnly(int workReportId) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getWorkReportOpenFromDate
 */
public Date getWorkReportOpenFromDate() throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getWorkReportOpenToDate
 */
public Date getWorkReportOpenToDate() throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#setWorkReportOpenFromDateWithDateString
 */
public void setWorkReportOpenFromDateWithDateString(String dateString) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#setWorkReportOpenToDateWithDateString
 */
public void setWorkReportOpenToDateWithDateString(String dateString) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#setAllWorkReportsTemporarelyReadOnly
 */
public void setAllWorkReportsTemporarelyReadOnly() throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#setAllWorkReportsTemporarelyReadOnlyFlag
 */
public void setAllWorkReportsTemporarelyReadOnlyFlag(boolean setAllAsReadOnly) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#removeWorkReportsTemporarelyReadOnlyFlag
 */
public void removeWorkReportsTemporarelyReadOnlyFlag() throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#areAllWorkReportsTemporarelyReadOnly
 */
public boolean areAllWorkReportsTemporarelyReadOnly() throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#sendWorkReport
 */
public boolean sendWorkReport(int workReportId, String reportText, IWResourceBundle iwrb) throws RemoteException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#closeWorkReport
 */
public boolean closeWorkReport(int workReportId) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getWorkReportSentText
 */
public String getWorkReportSentText(int workReportId) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#unSendWorkReport
 */
public boolean unSendWorkReport(int workReportId) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#isThereAYearlyAccountForAnEmptyDivision
 */
public boolean isThereAYearlyAccountForAnEmptyDivision(int workReportId) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#isBoardMissingForDivisionWithMembersOrYearlyAccount
 */
public boolean isBoardMissingForDivisionWithMembersOrYearlyAccount(int workReportId) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getAllWorkReportGroupsPrimaryKeysWithNoMembers
 */
public List getAllWorkReportGroupsPrimaryKeysWithNoMembers(int workReportId) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getAllWorkReportGroupsPrimaryKeysThatHaveMembers
 */
public List getAllWorkReportGroupsPrimaryKeysThatHaveMembers(int workReportId) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#isYearlyAccountMissingForADivisionWithMembers
 */
public boolean isYearlyAccountMissingForADivisionWithMembers(int workReportId) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getWorkReportDivisionBoardHome
 */
public WorkReportDivisionBoardHome getWorkReportDivisionBoardHome() throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getWorkReportBoardMemberHome
 */
public WorkReportBoardMemberHome getWorkReportBoardMemberHome() throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getWorkReportClubAccountRecordHome
 */
public WorkReportClubAccountRecordHome getWorkReportClubAccountRecordHome() throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getWorkReportsByYearRegionalUnionsAndClubs
 */
public Collection getWorkReportsByYearRegionalUnionsAndClubs(int year, Collection regionalUnionsFilter, Collection clubsFilter) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#beforeUserRemove
 */
public void beforeUserRemove(User user) throws RemoveException,RemoteException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#afterUserCreate
 */
public void afterUserCreate(User user) throws CreateException,RemoteException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#beforeGroupRemove
 */
public void beforeGroupRemove(Group group) throws RemoveException,RemoteException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#afterGroupCreate
 */
public void afterGroupCreate(Group group) throws CreateException,RemoteException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getPresentationObjectClass
 */
public Class getPresentationObjectClass() throws RemoteException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#instanciateEditor
 */
public PresentationObject instanciateEditor(Group group) throws RemoteException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#instanciateViewer
 */
public PresentationObject instanciateViewer(Group group) throws RemoteException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getUserPropertiesTabs
 */
public List getUserPropertiesTabs(User user) throws RemoteException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getGroupPropertiesTabs
 */
public List getGroupPropertiesTabs(Group group) throws RemoteException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getMainToolbarElements
 */
public List getMainToolbarElements() throws RemoteException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getGroupToolbarElements
 */
public List getGroupToolbarElements(Group group) throws RemoteException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#getListViewerFields
 */
public Collection getListViewerFields() throws RemoteException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#findGroupsByFields
 */
public Collection findGroupsByFields(Collection listViewerFields, Collection finderOperators, Collection listViewerFieldValues) throws RemoteException,java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#isUserAssignableFromGroupToGroup
 */
public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup) throws java.rmi.RemoteException;
/**
 * @see is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusinessBean#isUserSuitedForGroup
 */
public String isUserSuitedForGroup(User user, Group targetGroup) throws java.rmi.RemoteException;

}
