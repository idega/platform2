/*
 * $Id: AdultEducationBusinessBean.java,v 1.49.2.4 2006/04/05 15:20:35 dainis Exp $
 * Created on 27.4.2005
 * 
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package se.idega.idegaweb.commune.adulteducation.business;

import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import se.idega.idegaweb.commune.accounting.school.business.StudyPathBusiness;
import se.idega.idegaweb.commune.adulteducation.AdultEducationConstants;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoice;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceHome;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceReason;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoiceReasonHome;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourse;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseBMPBean;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourseHome;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationGroup;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationGroupHome;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationPersonalInfo;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationPersonalInfoHome;
import se.idega.idegaweb.commune.adulteducation.data.CoursePackage;
import se.idega.idegaweb.commune.adulteducation.data.CoursePackageHome;
import se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackage;
import se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackageHome;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.message.business.CommuneMessageBusiness;
import com.idega.block.pdf.business.PrintingContext;
import com.idega.block.pdf.business.PrintingService;
import com.idega.block.process.business.CaseBusiness;
import com.idega.block.process.business.CaseBusinessBean;
import com.idega.block.process.data.Case;
import com.idega.block.process.data.CaseCode;
import com.idega.block.process.data.CaseStatus;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolUserBusiness;
import com.idega.block.school.data.Grade;
import com.idega.block.school.data.GradeHome;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassHome;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberGrade;
import com.idega.block.school.data.SchoolClassMemberGradeHome;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolStudyPathGroup;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.FileUtil;
import com.idega.util.IWTimestamp;

/**
 * A collection of business methods associated with the Adult education block.
 * 
 * Last modified: $Date: 2006/04/05 15:20:35 $ by $Author: dainis $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.49.2.4 $
 */
public class AdultEducationBusinessBean extends CaseBusinessBean implements CaseBusiness, AdultEducationBusiness {

	public String getBundleIdentifier() {
		return AdultEducationConstants.IW_BUNDLE_IDENTIFIER;
	}

	public String getLocalizedCaseDescription(Case theCase, Locale locale) {
		AdultEducationChoice choice = getAdultEducationChoiceInstance(theCase);
		AdultEducationCourse course = choice.getCourse();
		SchoolStudyPath path = course.getStudyPath();
		School school = course.getSchool();

		Object[] arguments = { String.valueOf(choice.getChoiceOrder()), path.getDescription(),
				String.valueOf(path.getPoints()), school.getSchoolName() };

		String desc = super.getLocalizedCaseDescription(theCase, locale);
		return MessageFormat.format(desc, arguments);
	}

	public School getSchoolForUser(User user) throws FinderException {
		Group primaryGroup = user.getPrimaryGroup();
		SchoolBusiness schoolBuiz = getSchoolBusiness();
		try {
			if (primaryGroup.equals(schoolBuiz.getRootAdultEducationAdministratorGroup())) {
				SchoolUserBusiness sub = (SchoolUserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(),
						SchoolUserBusiness.class);
				Collection schoolIds = sub.getSchools(user);
				if (!schoolIds.isEmpty()) {
					Iterator iter = schoolIds.iterator();
					while (iter.hasNext()) {
						School school = sub.getSchoolHome().findByPrimaryKey(iter.next());
						return school;
					}
				}
			}
		}
		catch (CreateException ce) {
			ce.printStackTrace();
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
		catch (FinderException e) {
			Collection schools;
			try {
				schools = ((SchoolBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(),
						SchoolBusiness.class)).getSchoolHome().findAllBySchoolGroup(user);
			}
			catch (RemoteException e1) {
				throw new IBORuntimeException(e1.getMessage());
			}
			if (!schools.isEmpty()) {
				Iterator iter = schools.iterator();
				while (iter.hasNext()) {
					return (School) iter.next();
				}
			}
		}
		throw new FinderException("No school found for user: " + user.getPrimaryKey().toString());
	}

	protected AdultEducationChoice getAdultEducationChoiceInstance(Case theCase) throws RuntimeException {
		String caseCode = "unreachable";
		try {
			caseCode = theCase.getCode();
			if (AdultEducationConstants.ADULT_EDUCATION_CASE_CODE.equals(caseCode)) {
				return this.getChoice(theCase.getPrimaryKey());
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		throw new ClassCastException("Case with casecode: " + caseCode
				+ " cannot be converted to an adult education choice");
	}

	/**
	 * Fetches the home interface for AdultEducationCourse.
	 * 
	 * @return AdultEducationCourseHome
	 */
	private AdultEducationCourseHome getCourseHome() {
		try {
			return (AdultEducationCourseHome) IDOLookup.getHome(AdultEducationCourse.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	/**
	 * Fetches the home interface for AdultEducationChoice.
	 * 
	 * @return AdultEducationChoiceHome
	 */
	private AdultEducationChoiceHome getChoiceHome() {
		try {
			return (AdultEducationChoiceHome) IDOLookup.getHome(AdultEducationChoice.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	/**
	 * Fetches the home interface for AdultEducationChoiceReason.
	 * 
	 * @return AdultEducationChoiceReasonHome
	 */
	private AdultEducationChoiceReasonHome getChoiceReasonHome() {
		try {
			return (AdultEducationChoiceReasonHome) IDOLookup.getHome(AdultEducationChoiceReason.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	/**
	 * Fetches the home interface for Grade.
	 * 
	 * @return GradeHome
	 */
	private GradeHome getGradeHome() {
		try {
			return (GradeHome) IDOLookup.getHome(Grade.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	/**
	 * Fetches the home interface for SchoolClassMemberGrade.
	 * 
	 * @return SchoolClassMemberGradeHome
	 */
	private SchoolClassMemberGradeHome getStudentGradeHome() {
		try {
			return (SchoolClassMemberGradeHome) IDOLookup.getHome(SchoolClassMemberGrade.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private SchoolClassHome getSchoolClassHome() {
		try {
			return (SchoolClassHome) IDOLookup.getHome(SchoolClass.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private SchoolClassMemberHome getSchoolClassMemberHome() {
		try {
			return (SchoolClassMemberHome) IDOLookup.getHome(SchoolClassMember.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private CoursePackageHome getCoursePackageHome() {
		try {
			return (CoursePackageHome) IDOLookup.getHome(CoursePackage.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private SchoolCoursePackageHome getSchoolCoursePackageHome() {
		try {
			return (SchoolCoursePackageHome) IDOLookup.getHome(SchoolCoursePackage.class);
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	/**
	 * @return
	 */
	public SchoolBusiness getSchoolBusiness() {
		try {
			return (SchoolBusiness) getServiceInstance(SchoolBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public CommuneUserBusiness getUserBusiness() {
		try {
			return (CommuneUserBusiness) getServiceInstance(CommuneUserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private CommuneMessageBusiness getMessageBusiness() {
		try {
			return (CommuneMessageBusiness) this.getServiceInstance(CommuneMessageBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	/**
	 * @return
	 */
	public StudyPathBusiness getStudyPathBusiness() {
		try {
			return (StudyPathBusiness) getServiceInstance(StudyPathBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	/**
	 * Fetches and AdultEducationCourse instance for the season and code
	 * provided.
	 * 
	 * @param season
	 *            The season that the course is in (can be a SchoolSeason
	 *            instance or a primary key)
	 * @param code
	 *            String representing the course code
	 * @return Returns an AdultEducationCourse instance that matches the
	 *         criteria given.
	 * @throws FinderException
	 *             When no course exists with the code and season provided.
	 */
	public AdultEducationCourse getCourse(Object season, String code) throws FinderException {
		return getCourseHome().findBySeasonAndCode(season, code);
	}

	public AdultEducationCourse getCourse(Object coursePK) throws FinderException {
		return getCourseHome().findByPrimaryKey(coursePK);
	}

	public Collection getCourses(Object season, Object school, Object group) {
		try {
			String[] statuses = { getCaseStatusGranted().getStatus(), getCaseStatusPlaced().getStatus() };
			return getCourseHome().findAllBySchoolAndSeasonAndStudyPathGroupConnectedToChoices(school, season, group,
					statuses);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}
	
	public Collection findAvailableCourses(SchoolType schoolType, SchoolSeason schoolSeason, 
			SchoolStudyPathGroup studyPathGroup, SchoolStudyPath studyPath, School school ) {
		try {	
			return getCourseHome().findAllAvailableCoursesByParameters(schoolType, schoolSeason, studyPathGroup, studyPath, school);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}		
		
	}

	public Collection getCoursesWithStudents(Object season, Object school, Object group) {
		try {
			return getCourseHome().findAllBySchoolAndSeasonAndStudyPathGroupConnectedToStudents(school, season, group);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	public Collection getCourses(Object season, Object type, Object school, Object group) {
		try {
			return getCourseHome().findAllBySeasonAndTypeAndSchoolAndStudyPathGroup(season, type, school, group);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	public Collection getGroups(School school, SchoolSeason season, String code) {
		try {
			return getSchoolBusiness().getSchoolClassHome().findBySchoolAndSeasonAndCode(school, season, code);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public Collection getGroups(School school, SchoolSeason season, SchoolStudyPathGroup group) {
		try {
			AdultEducationGroupHome home = (AdultEducationGroupHome) IDOLookup.getHome(AdultEducationGroup.class);
			return home.findBySchoolAndSeasonAndStudyPathGroup(school, season, group);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public Collection getStudents(SchoolClass group) {
		try {
			return getSchoolBusiness().getSchoolClassMemberHome().findAllBySchoolClass(group);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public void storeGroup(SchoolClass group, String name, School school, SchoolSeason season, SchoolType type,
			String code, User teacher, boolean update) throws CreateException, DuplicateValueException {
		try {
			if (group == null) {
				group = getSchoolBusiness().getSchoolClassHome().create();
			}
			group.setSchool(school);
			group.setSchoolSeason(season);
			group.setSchoolClassName(name);
			group.setSchoolType(type);
			group.setCode(code);
			group.setValid(true);
			group.store();

			if (update) {
				try {
					group.removeFromUser();
				}
				catch (IDORemoveRelationshipException irre) {
					irre.printStackTrace();
				}
			}

			if (teacher != null) {
				try {
					group.addTeacher(teacher);
				}
				catch (IDOAddRelationshipException iare) {
					iare.printStackTrace();
				}
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public boolean hasPlacement(String courseCode) {
		return getNumberOfPlacementsForCourse(courseCode) > 0;
	}
	
	public int getNumberOfPlacementsForCourse(String courseCode) {
		try {
			SchoolClass schoolClass = getSchoolClassHome().findOneByCode(courseCode);
			return getSchoolClassMemberHome().getNumberOfPlacingsByClass(schoolClass);
		}
		catch (FinderException e) {
		}
		catch (IDOException e) {
		}
		
		return 0;
	}

	public boolean hasActiveChoices(SchoolSeason season, AdultEducationCourse course) {
		return getNumberOfActiveChoices(season, course) > 0;
	}

	public int getNumberOfActiveChoices(SchoolSeason season, AdultEducationCourse course) {
		try {
			String[] statuses = { getCaseStatusGranted().toString() };
			return getChoiceHome().getCountOfChoicesByCourse(season, course, statuses);
		}
		catch (IDOException ie) {
			ie.printStackTrace();
			return 0;
		}
	}

	public Collection getChoices(SchoolSeason season, AdultEducationCourse course) {
		try {
			String[] statuses = { getCaseStatusGranted().toString() };
			return getChoiceHome().findAllBySeasonAndCourse(season, course, statuses);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	public SchoolClass createDefaultGroup(SchoolSeason season, AdultEducationCourse course) {
		try {
			SchoolClass group = getSchoolBusiness().getSchoolClassHome().create();
			group.setSchool(course.getSchool());
			group.setSchoolSeason(season);
			group.setCode(course.getCode());
			group.setValid(true);
			group.setSchoolClassName(course.getCode());
			group.setSchoolType(course.getStudyPath().getSchoolType());
			group.store();

			return group;
		}
		catch (CreateException ce) {
			ce.printStackTrace();
			return null;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public AdultEducationChoice getChoice(User user, AdultEducationCourse course) throws FinderException {
		return getChoice(user, course.getPrimaryKey());
	}

	private AdultEducationChoice getChoice(User user, Object coursePK) throws FinderException {
		return getChoiceHome().findByUserAndCourse(user.getPrimaryKey(), coursePK);
	}

	public AdultEducationChoice getChoice(Object choicePK) throws FinderException {
		return getChoiceHome().findByPrimaryKey(choicePK);
	}

	public AdultEducationChoice getChoice(User user, Object seasonPK, Object studyPathPK, int choiceOrder)
			throws FinderException {
		String[] statuses = { getCaseStatusOpen().getStatus(), getCaseStatusInactive().getStatus() };
		return getChoiceHome().findByUserAndSeasonAndStudyPathAndChoiceOrder(user.getPrimaryKey(), seasonPK,
				studyPathPK, choiceOrder, statuses);
	}

	public Collection getChoices(User user, SchoolSeason season) {
		try {
			String[] statuses = { getCaseStatusOpen().getStatus(), getCaseStatusGranted().getStatus(),
					getCaseStatusReview().getStatus(), getCaseStatusPlaced().getStatus() };
			return getChoiceHome().findAllByUserAndSeasonAndStatuses(user, season, statuses);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	public Collection getChoices(User user, SchoolSeason season, SchoolStudyPath path) {
		try {
			String[] statuses = { getCaseStatusOpen().getStatus(), getCaseStatusInactive().getStatus(),
					getCaseStatusGranted().getStatus(), getCaseStatusReview().getStatus(),
					getCaseStatusPlaced().getStatus() };
			return getChoiceHome().findAllByUserAndSeasonAndStudyPath(user.getPrimaryKey(), season.getPrimaryKey(),
					path.getPrimaryKey(), statuses);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	public Collection getChoices(User user) {
		try {
			String[] statuses = { getCaseStatusOpen().getStatus(), getCaseStatusGranted().getStatus(),
					getCaseStatusReview().getStatus() };
			return getChoiceHome().findAllByUser(user, statuses);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	public Collection getChoices(SchoolSeason season, SchoolType type, Date fromDate, Date toDate, User handler) {
		try {
			String[] statuses = { getCaseStatusOpen().getStatus(), getCaseStatusReview().getStatus() };
			return getChoiceHome().findAllBySeasonAndTypeAndDateAndHandlerAndStatuses(season, type, fromDate, toDate,
					handler, statuses, 1);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	public Collection getUnhandledChoices(SchoolSeason season, SchoolType type, Date fromDate, Date toDate, User handler) {
		try {
			String[] statuses = { getCaseStatusOpen().getStatus() };
			return getChoiceHome().findAllBySeasonAndTypeAndDateAndHandlerAndStatuses(season, type, fromDate, toDate,
					handler, statuses, 1);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	public Collection getHandledChoices(SchoolSeason season, SchoolType type, Date fromDate, Date toDate, User handler) {
		try {
			String[] statuses = { getCaseStatusReview().getStatus() };
			return getChoiceHome().findAllBySeasonAndTypeAndDateAndHandlerAndStatuses(season, type, fromDate, toDate,
					handler, statuses, 1);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	private void updateHandlerForUserCases(User user, SchoolSeason season, User handler) {
		Collection choices = getChoices(user, season);
		Iterator iter = choices.iterator();
		while (iter.hasNext()) {
			AdultEducationChoice choice = (AdultEducationChoice) iter.next();
			choice.setHandler(handler);
			choice.store();
		}
	}

	public Collection getSelectedStudyPaths(User user, SchoolSeason season) {
		Collection studyPaths = new ArrayList();

		if (season != null) {
			try {
				Collection choices = getChoiceHome().findAllByUserAndSeason(user, season, 1);
				Iterator iter = choices.iterator();
				while (iter.hasNext()) {
					AdultEducationChoice choice = (AdultEducationChoice) iter.next();
					if (!hasAllChoicesForCodeDeniedOrDeleted(choice) && !hasPlacementTerminated(user, choice)) {
						studyPaths.add(choice.getCourse().getStudyPath());
					}
				}
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
		}

		return studyPaths;
	}

	private boolean hasPlacementTerminated(User user, AdultEducationChoice choice) {
		if (choice.getStatus().equals(getCaseStatusPlaced().getStatus())) {
			String courseCode = choice.getCourse().getCode();
			SchoolSeason season = choice.getCourse().getSchoolSeason();
			School school = choice.getCourse().getSchool();
			try {
				Collection classes = getSchoolClassHome().findBySchoolAndSeasonAndCode(school, season, courseCode);
				if (classes != null && !classes.isEmpty()) {
					Collection members = getSchoolClassMemberHome().findBySchoolClasses(classes);
					if (members != null && !members.isEmpty()) {
						Iterator it = members.iterator();
						while (it.hasNext()) {
							SchoolClassMember member = (SchoolClassMember) it.next();
							if (member.getStudent().equals(user)) {
								if (member.getRemovedDate() != null) {
									return true;
								}
							}

						}
					}
				}
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	/*
	 * private boolean hasAllChoicesForCodeDenied(AdultEducationChoice choice) {
	 * if (choice.getStatus().equals(getCaseStatusDenied().getStatus())) { if
	 * (choice.getChildCount() > 0) { Iterator iter =
	 * choice.getChildrenIterator(); while (iter.hasNext()) { Case element =
	 * (Case) iter.next(); if
	 * (element.getCaseCode().equals(choice.getCaseCode())) {
	 * AdultEducationChoice nextChoice =
	 * getAdultEducationChoiceInstance(element); return
	 * hasAllChoicesForCodeDenied(nextChoice); } } return true; } else { return
	 * true; } } else { return false; } }
	 */

	private boolean hasAllChoicesForCodeDeniedOrDeleted(AdultEducationChoice choice) {
		if (choice.getStatus().equals(getCaseStatusDenied().getStatus())
				|| choice.getStatus().equals(getCaseStatusDeleted().getStatus())) {
			if (choice.getChildCount() > 0) {
				Iterator iter = choice.getChildrenIterator();
				while (iter.hasNext()) {
					Case element = (Case) iter.next();
					if (element.getCaseCode().equals(choice.getCaseCode())) {
						AdultEducationChoice nextChoice = getAdultEducationChoiceInstance(element);
						return hasAllChoicesForCodeDeniedOrDeleted(nextChoice);
					}
				}
				return true;
			}
			else {
				return true;
			}
		}
		else {
			return false;
		}
	}

	public Collection getAvailableSchools(Object studyPathPK, Object seasonPK) {
		return getAvailableSchools(studyPathPK, seasonPK, null, null);
	}
	public Collection getAvailableSchools(Object studyPathPK, Object seasonPK, Object studyPathGroupPK, Object schoolTypePK) {
		try {
			return getSchoolBusiness().getSchoolHome().findAllByInQuery(
					AdultEducationCourseBMPBean.getFindAllBySeasonAndStudyPathSchoolQuery(seasonPK, studyPathPK, studyPathGroupPK, schoolTypePK));
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	public Collection getAvailableCourses(Object seasonPK, Object schoolPK, Object studyPathPK) {
		try {
			return getCourseHome().findAllBySeasonAndSchoolAndStudyPath(seasonPK, schoolPK, studyPathPK);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	public Collection getPendingSeasons() {
		try {
			List list = new ArrayList(getSchoolBusiness().getSchoolSeasonHome().findPendingSeasonsByDate(getCategory(),
					new IWTimestamp().getDate()));
			Collections.reverse(list);
			return list;
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	public Collection getActiveReasons() {
		try {
			return getChoiceReasonHome().findAll();
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	public SchoolCategory getCategory() throws FinderException {
		try {
			SchoolCategoryHome home = (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
			return home.findByPrimaryKey(AdultEducationConstants.ADULT_EDUCATION_CATEGORY);
		}
		catch (IDOLookupException ile) {
			throw new FinderException(ile.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusiness#getSchoolTypes()
	 */
	public Collection getSchoolTypes() {
		try {
			return getSchoolBusiness().getSchoolTypesForCategory(getCategory(), false);
		}
		catch (FinderException fe) {
			throw new IBORuntimeException(
					"Adult education block not properly set up, school category data entry is missing");
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	//
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusiness#getSeasons()
	 */
	public Collection getSeasons() {
		try {
			return getSchoolBusiness().findAllSchoolSeasons(getCategory());		
		}
		catch (FinderException fe) {
			throw new IBORuntimeException(
					"Adult education block not properly set up, school category data entry is missing");
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusiness#getSeasons()
	 */
	public Collection getCurrentSeasons() {
		try {
			return getSchoolBusiness().findAllCurrentSeasons(getCategory());
		}
		catch (FinderException fe) {
			throw new IBORuntimeException(
					"Adult education block not properly set up, school category data entry is missing");
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public Collection getNextSeasons(SchoolSeason season) {
		try {
			IWTimestamp stamp = new IWTimestamp(season.getSchoolSeasonStart());
			stamp.addDays(-1);
			return getSchoolBusiness().getSchoolSeasonHome().findPendingSeasonsByDate(season.getSchoolCategory(),
					stamp.getDate());
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public Collection getSchools(SchoolType type) {
		try {
			if (type != null) {
				return getSchoolBusiness().findAllSchoolsByType(type);
			}
			return new ArrayList();
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public Collection getSchools() {
		try {
			return getSchoolBusiness().findAllSchoolsByCategory(getCategory().getCategory());
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public Collection getStudyPathsGroups() {
		try {
			return getStudyPathBusiness().findAllStudyPathGroups();
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public Collection getStudyPaths(SchoolType type, SchoolStudyPathGroup group) {
		try {
			if (type == null || group == null) {
				return new ArrayList();
			}
			return getStudyPathBusiness().findStudyPathsByType(type, group);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	public Collection getStydyPathsBySchoolTypeAndSchoolStudyPathGroup(SchoolType type, SchoolStudyPathGroup group) {
		try {
			if (group == null || type == null) {
				return new ArrayList();			
			}
			return getStudyPathBusiness().findStudyPathsBySchoolTypeAndSchoolStudyPathGroup(type, group);
		} catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}		
	}
	

	/**
	 * Stores and returns an AdultEducationCourse instance with the values
	 * provided. If an entry already exists for the provided season and code, a
	 * DuplicateValueException in thrown.
	 * 
	 * @param season
	 *            The season that the course is in (can be a SchoolSeason
	 *            instance or a primary key)
	 * @param code
	 *            String representing the course code
	 * @param school
	 *            The school that holds the course (can be a School instance or
	 *            a primary key)
	 * @param studyPath
	 *            The study path that this course belongs to (can be a
	 *            SchoolStudyPath instance or a primary key)
	 * @param startDate
	 *            The date when the course starts
	 * @param endDate
	 *            The date when the course ends
	 * @param comment
	 *            Comments about the course
	 * @param length
	 *            The course length in weeks
	 * @param notActive
	 *            Whether the course should be visible or not
	 * @return Returns the newly stored AdultEducationCourse instance.
	 * @throws CreateException
	 * @throws DuplicateValueException
	 *             When trying to store a new course with the same code and
	 *             season as an already existing course.
	 */
	public AdultEducationCourse storeCourse(Object season, Object oldSeason, String code, String oldCode,
			Object school, Object studyPath, Date startDate, Date endDate, String comment, int length,
			boolean notActive, boolean update) throws CreateException, DuplicateValueException {
		AdultEducationCourse course = null;
		try {
			course = getCourse(update ? oldSeason : season, update ? oldCode : code);
			if (!update) {
				throw new DuplicateValueException("Season=" + season.toString() + "/Code=" + code);
			}
			else {
				if (!season.equals(oldSeason) || !code.equals(oldCode)) {
					try {
						getCourse(season, code);
						throw new DuplicateValueException("Season=" + season.toString() + "/Code=" + code);
					}
					catch (FinderException fe) {
						// Nothing found so we continue...
					}
				}
			}
		}
		catch (FinderException fe) {
			course = getCourseHome().create();
		}
		course.setCode(code);
		course.setComment(comment);
		course.setEndDate(endDate);
		course.setLength(length);
		course.setSchool(school);
		course.setSchoolSeason(season);
		course.setStartDate(startDate);
		course.setStudyPath(studyPath);
		course.setNotActive(notActive);
		course.store();
		return course;
	}

	public void storeChoices(User user, SchoolSeason season, Collection packagePKs, String comment, Object[] reasons,
			String otherReason) throws IDOCreateException {
		javax.transaction.UserTransaction trans = this.getSessionContext().getUserTransaction();
		try {
			trans.begin();

			CaseStatus first = getCaseStatusOpen();
			CaseStatus other = getCaseStatusInactive();
			CaseStatus status = null;

			IWTimestamp timeNow = new IWTimestamp();

			Iterator iter = packagePKs.iterator();
			int i = 0;
			while (iter.hasNext()) {
				SchoolCoursePackage schoolCoursePackage = getSchoolCoursePackage(iter.next());
				Collection courses = schoolCoursePackage.getCourses();

				Iterator iterator = courses.iterator();
				while (iterator.hasNext()) {
					AdultEducationCourse course = (AdultEducationCourse) iterator.next();

					timeNow.addSeconds(-i);
					if (i == 0) {
						status = first;
					}
					else {
						status = other;
					}

					storeChoice(user, course, course, comment, reasons, otherReason, i + 1, status, null,
							timeNow.getDate());
				}
				i++;
			}

			trans.commit();
		}
		catch (Exception ex) {
			try {
				trans.rollback();
			}
			catch (javax.transaction.SystemException e) {
				throw new IDOCreateException(e.getMessage());
			}
			ex.printStackTrace();
			throw new IDOCreateException(ex);
		}
	}

	public void storeChoices(User user, Collection courses, Object[] oldCourses, String comment, Object[] reasons,
			String otherReason) throws IDOCreateException {
		javax.transaction.UserTransaction trans = this.getSessionContext().getUserTransaction();
		try {
			trans.begin();

			CaseStatus first = getCaseStatusOpen();
			CaseStatus other = getCaseStatusInactive();
			CaseStatus status = null;

			AdultEducationChoice choice = null;
			IWTimestamp timeNow = new IWTimestamp();

			Collection choices = new ArrayList();
			Iterator iter = courses.iterator();
			int i = 0;
			while (iter.hasNext()) {
				Object course = iter.next();

				Object oldCourse = course;
				if (oldCourses != null && oldCourses.length > i) {
					oldCourse = oldCourses[i];
				}

				timeNow.addSeconds(-i);
				if (i == 0) {
					status = first;
				}
				else {
					status = other;
				}

				choice = storeChoice(user, course, oldCourse, comment, reasons, otherReason, i + 1, status, choice,
						timeNow.getDate());
				choices.add(choice);
				i++;
			}

			String subject = getLocalizedString("choices_sent_subject", "Adult education choices sent");
			String body = getLocalizedString(
					"choices_sent_body",
					"You have made a choice to course {0} for period {1}. The following alternatives where chosen:\n\n1. {2} - {3}\n2. {4} - {5}3. {6} - {7}");

			sendMessage(choices, subject, body);

			trans.commit();
		}
		catch (Exception ex) {
			try {
				trans.rollback();
			}
			catch (javax.transaction.SystemException e) {
				throw new IDOCreateException(e.getMessage());
			}
			ex.printStackTrace();
			throw new IDOCreateException(ex);
		}
	}

	private void sendMessage(AdultEducationChoice choice, String subject, String body) {
		AdultEducationCourse course = choice.getCourse();
		SchoolStudyPath path = course.getStudyPath();

		Object[] arguments = {
				getLocalizedString(path.getDescription(), path.getDescription()),
				course.getCode(),
				course.getSchool().getSchoolName(),
				new IWTimestamp(course.getStartDate()).getLocaleDate(
						getIWApplicationContext().getApplicationSettings().getDefaultLocale(), IWTimestamp.SHORT) };
		sendMessage(choice, choice.getUser(), arguments, subject, body);
	}

	private void sendMessage(Collection choices, String subject, String body) {
		Object[] arguments = { "-", "-", "-", "-", "-" };

		AdultEducationChoice parentCase = null;
		User user = null;
		boolean first = true;
		int i = 2;
		Iterator iter = choices.iterator();
		while (iter.hasNext()) {
			AdultEducationChoice choice = (AdultEducationChoice) iter.next();
			AdultEducationCourse course = choice.getCourse();
			School school = course.getSchool();
			if (first) {
				user = choice.getUser();
				parentCase = choice;
				SchoolStudyPath path = course.getStudyPath();
				SchoolSeason season = course.getSchoolSeason();
				arguments[0] = path.getDescription();
				arguments[1] = season.getSchoolSeasonName();
				first = false;
			}
			arguments[i] = school.getSchoolName() + " - " + course.getCode();
			i++;
		}

		sendMessage(parentCase, user, arguments, subject, body);
	}

	private void sendMessage(Case parentCase, User user, Object[] arguments, String subject, String body) {
		try {
			getMessageBusiness().createUserMessage(parentCase, user, subject, MessageFormat.format(body, arguments),
					true);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	private AdultEducationChoice storeChoice(User user, Object course, Object oldCourse, String comment,
			Object[] reasons, String otherReason, int choiceOrder, CaseStatus status, Case parentCase, Date choiceDate)
			throws CreateException {
		AdultEducationChoice choice = null;
		try {
			choice = getChoice(user, oldCourse);
			try {
				choice.removeAllReasons();
			}
			catch (IDORemoveRelationshipException irre) {
				log(irre);
			}
		}
		catch (FinderException fex) {
			choice = getChoiceHome().create();
		}

		choice.setUser(user);
		choice.setCourse(course);
		choice.setCaseStatus(status);
		choice.setChoiceOrder(choiceOrder);
		choice.setComment(comment);
		choice.setOwner(user);
		if (parentCase != null) {
			choice.setParentCase(parentCase);
		}
		choice.setChoiceDate(choiceDate);
		choice.setOtherReason(otherReason);
		choice.store();

		if (reasons != null) {
			for (int i = 0; i < reasons.length; i++) {
				try {
					choice.addReason(reasons[i]);
				}
				catch (IDOAddRelationshipException iare) {
					log(iare);
				}
			}
		}

		return choice;
	}

	public void grantChoice(AdultEducationChoice choice, boolean rule1, boolean rule2, boolean rule3, boolean rule4,
			String ruleNotes, String notes, int priority, User performer) {
		saveChoiceChanges(choice, rule1, rule2, rule3, rule4, ruleNotes, notes, priority, performer);
		choice.setAllGranted(true);
		choice.setGrantedDate(new IWTimestamp().getDate());

		changeCaseStatus(choice, getCaseStatusGranted().getStatus(), performer);
		updateHandlerForUserCases(choice.getUser(), choice.getCourse().getSchoolSeason(), performer);

		String subject = getLocalizedString("choice_granted_subject", "VUX application granted");
		String body = getLocalizedString("choice_granted_body",
				"Your choice to course in {0} has been granted. The choice will now be handled by the provider.");
		sendMessage(choice, subject, body);
	}

	public void denyChoice(AdultEducationChoice choice, String rejectionMessage, User performer) {
		choice.setRejectionComment(rejectionMessage);
		changeCaseStatus(choice, getCaseStatusReview().getStatus(), performer);
		updateHandlerForUserCases(choice.getUser(), choice.getCourse().getSchoolSeason(), performer);

		String subject = getLocalizedString("choice_denied_subject", "VUX application denied");
		sendMessage(choice, subject, rejectionMessage);
	}

	public void saveChoiceChanges(AdultEducationChoice choice, boolean rule1, boolean rule2, boolean rule3,
			boolean rule4, String ruleNotes, String notes, int priority, User performer) {
		choice.setGrantedRule1(rule1);
		choice.setGrantedRule2(rule2);
		choice.setGrantedRule3(rule3);
		choice.setGrantedRule4(rule4);
		choice.setGrantedRuleNotes(ruleNotes);
		choice.setPriority(priority);
		choice.setNotes(notes);
		choice.setHandler(performer);

		choice.store();

		updateHandlerForUserCases(choice.getUser(), choice.getCourse().getSchoolSeason(), performer);
	}

	public void removeCourse(Object coursePK) throws RemoveException {
		try {
			AdultEducationCourse course = getCourse(coursePK);
			course.remove();
		}
		catch (FinderException fe) {
			throw new RemoveException(fe.getMessage());
		}
	}

	public void removeChoices(Object studyPathPK, Object seasonPK, User performer) {
		removeChoices(studyPathPK, seasonPK, performer.getPrimaryKey(), performer);
	}

	public void removeChoices(Object studyPathPK, Object seasonPK, Object userPK, User performer) {
		try {
			String[] statuses = { getCaseStatusOpen().getStatus(), getCaseStatusInactive().getStatus(),
					getCaseStatusGranted().getStatus() };
			Collection choices = getChoiceHome().findAllByUserAndSeasonAndStudyPath(userPK, seasonPK, studyPathPK,
					statuses);
			Iterator iter = choices.iterator();
			while (iter.hasNext()) {
				AdultEducationChoice choice = (AdultEducationChoice) iter.next();
				changeCaseStatus(choice, getCaseStatusDeleted().getStatus(), performer);
			}
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
	}

	public void placeChoices(Object[] choicePKs, SchoolClass group, AdultEducationCourse course, Date date,
			User performer) {
		try {
			SchoolStudyPath path = course.getStudyPath();
			SchoolType type = path.getSchoolType();
			for (int i = 0; i < choicePKs.length; i++) {
				try {
					AdultEducationChoice choice = getChoice(choicePKs[i]);
					User user = choice.getUser();

					SchoolClassMember member = getSchoolBusiness().storeSchoolClassMember(group, user);
					member.setRegisterDate(new IWTimestamp(date).getTimestamp());
					member.setNotes(choice.getComment());
					member.setSchoolType(type);
					member.setRegistrator(performer);
					member.store();

					changeCaseStatus(choice, getCaseStatusPlaced().getStatus(), performer);
				}
				catch (FinderException fe) {
					fe.printStackTrace();
					continue;
				}
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public void rejectChoices(Object[] choicePKs, User performer) {
		for (int i = 0; i < choicePKs.length; i++) {
			try {
				AdultEducationChoice choice = getChoice(choicePKs[i]);
				if (choice.getPackagePK() != null) {
					rejectChoicesInPackage(choice, performer);
				}
				else {
					rejectChoice(choice, performer);
					activateNextChoice(choice, performer);
				}
			}
			catch (FinderException fe) {
				fe.printStackTrace();
				continue;
			}
		}
	}

	private void rejectChoicesInPackage(AdultEducationChoice choice, User performer) {
		try {
			int choiceOrder = choice.getChoiceOrder();
			int priority = choice.getPriority();
			AdultEducationCourse course = choice.getCourse();
			SchoolSeason season = course.getSchoolSeason();
			SchoolCoursePackage coursePackage = choice.getPackage();

			Collection choices = getChoiceHome().findAllByUserAndSeasonAndPackage(choice.getUser(), season,
					coursePackage, choiceOrder);
			Iterator iter = choices.iterator();
			while (iter.hasNext()) {
				AdultEducationChoice element = (AdultEducationChoice) iter.next();
				rejectChoice(element, performer);
			}

			choices = getChoiceHome().findAllByUserAndSeasonAndPackage(choice.getUser(), season, coursePackage,
					choiceOrder + 1);
			iter = choices.iterator();
			while (iter.hasNext()) {
				AdultEducationChoice element = (AdultEducationChoice) iter.next();
				element.setPriority(priority);
				changeCaseStatus(element, getCaseStatusGranted().getStatus(), performer);
			}
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
	}

	private void rejectChoice(AdultEducationChoice choice, User performer) {
		String subject = getLocalizedString("choice_rejected_subject", "VUX - Rejected by provider");
		String body = getLocalizedString(
				"choice_rejected_body",
				"Your choice to course {0} with course code {1} has been rejected by {2}. If you have chosen more alternatives that provider will now handle your application.");

		changeCaseStatus(choice, getCaseStatusDenied().getStatus(), performer);
		sendMessage(choice, subject, body);
	}

	private void activateNextChoice(AdultEducationChoice choice, User performer) {
		CaseCode code = choice.getCaseCode();
		if (choice.getChildCount() > 0) {
			Iterator iter = choice.getChildrenIterator();
			while (iter.hasNext()) {
				Case element = (Case) iter.next();
				if (element.getCaseCode().equals(code)) {
					AdultEducationChoice nextChoice = getAdultEducationChoiceInstance(element);
					nextChoice.setPriority(choice.getPriority());
					changeCaseStatus(nextChoice, getCaseStatusGranted().getStatus(), performer);
					break;
				}
			}
		}
	}

	public void removeStudent(Object schoolClassMemberPK, Object choicePK, User performer) {
		try {
			try {
				AdultEducationChoice choice = getChoice(choicePK);
				choice.setConfirmationMessageSent(false);
				choice.setPlacementMessageSent(false);
				changeCaseStatus(choice, getCaseStatusGranted().getStatus(), performer);

				SchoolClassMember member = getSchoolBusiness().getSchoolClassMemberHome().findByPrimaryKey(
						new Integer(schoolClassMemberPK.toString()));
				member.remove();
			}
			catch (RemoveException re) {
				re.printStackTrace();
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public void sendPlacementMessage(SchoolClass group, AdultEducationCourse course, String message) {
		try {
			String subject = getLocalizedString("choice_placed_subject", "VUX - Placement in course");

			Collection students = getSchoolBusiness().findStudentsInClass(((Integer) group.getPrimaryKey()).intValue());
			Iterator iter = students.iterator();
			while (iter.hasNext()) {
				SchoolClassMember member = (SchoolClassMember) iter.next();
				try {
					AdultEducationChoice choice = getChoice(member.getStudent(), course);
					if (!choice.isPlacementMessageSent()) {
						choice.setPlacementMessageSent(true);
						choice.store();

						sendMessage(choice, subject, message);
					}
				}
				catch (FinderException fe) {
					fe.printStackTrace();
				}
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public void changeCourse(AdultEducationChoice choice, Object coursePK) {
		choice.setCourse(coursePK);
		choice.store();
	}

	public void changeGroup(SchoolClassMember member, Object groupPK) {
		member.setSchoolClassId(new Integer(groupPK.toString()).intValue());
		member.store();
	}

	public void removeGroup(SchoolClass group) {
		group.setValid(false);
		group.store();
	}

	public AdultEducationPersonalInfo storePersonalInfo(int icUserID, int nativecountryId, int languageID,
			int educationCountryID, boolean nativeThisCountry, boolean citizenThisCountry, boolean educationA,
			boolean educationB, boolean educationC, boolean educationD, boolean educationE, String educationF,
			String educationG, int eduGCountryID, int eduYears, boolean eduHA, boolean eduHB, boolean eduHC,
			String eduHCommune, boolean fulltime, boolean langSfi, boolean langSas, boolean langOther,
			boolean studySupport, boolean workUnEmpl, boolean workEmpl, boolean workKicked, String workOther) {
		try {
			AdultEducationPersonalInfo personalInfo = null;
			if (icUserID != -1) {
				try {
					personalInfo = getAdultEducationPersonalHome().findByUserId(new Integer(icUserID));
				}
				catch (FinderException fe) {
					personalInfo = getAdultEducationPersonalHome().create();
					personalInfo.store(); // so it gets a primary key,
											// otherwise an exception
				}
			}

			if (personalInfo != null) {
				if (icUserID != -1)
					personalInfo.setIcUserID(icUserID);
				personalInfo.setNativeCountryID(nativecountryId);
				personalInfo.setIcLanguageID(languageID);

				personalInfo.setNativeThisCountry(nativeThisCountry);
				personalInfo.setCitizenThisCountry(citizenThisCountry);
				personalInfo.setEduA(educationA);
				personalInfo.setEduB(educationB);
				personalInfo.setEduC(educationC);
				personalInfo.setEduD(educationD);
				personalInfo.setEduE(educationE);
				personalInfo.setEduF(educationF);
				personalInfo.setEduG(educationG);
				personalInfo.setEducationCountryID(educationCountryID);
				personalInfo.setEduGYears(eduYears);
				personalInfo.setEduHA(eduHA);
				personalInfo.setEduHB(eduHB);
				personalInfo.setEduHC(eduHC);
				// if (eduHCommune != null && !eduHCommune.equals(""))
				personalInfo.setEduHCommune(eduHCommune);
				personalInfo.setFulltime(fulltime);
				personalInfo.setLangSFI(langSfi);
				personalInfo.setLangSAS(langSas);
				personalInfo.setLangOTHER(langOther);
				personalInfo.setStudySupport(studySupport);
				personalInfo.setWorkEmploy(workEmpl);
				personalInfo.setWorkUnEmploy(workUnEmpl);
				personalInfo.setWorkKicked(workKicked);
				// if (workOther != null && !workOther.equals(""))
				personalInfo.setWorkOther(workOther);

				personalInfo.store();

			}
			return personalInfo;
		}
		/*
		 * catch (FinderException fe) { fe.printStackTrace(System.err); return
		 * null; }
		 */
		catch (CreateException ce) {
			ce.printStackTrace(System.err);
			return null;
		}
	}

	public void createOverviewPDF(User user, SchoolSeason season, String path, String fileName, Locale locale) {
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		OutputStream mos = new MemoryOutputStream(buffer);
		InputStream mis = new MemoryInputStream(buffer);

		PrintingContext pcx = new ChoiceOverviewContext(getIWApplicationContext(), season, user, locale);
		pcx.setDocumentStream(mos);
		try {
			getPrintingService().printDocument(pcx);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		FileUtil.streamToFile(mis, path, fileName);
	}

	private PrintingService getPrintingService() {
		try {
			return (PrintingService) getServiceInstance(PrintingService.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public AdultEducationPersonalInfoHome getAdultEducationPersonalHome() {
		try {
			return (AdultEducationPersonalInfoHome) IDOLookup.getHome(AdultEducationPersonalInfo.class);
		}
		catch (IDOLookupException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	public Collection getGrades(SchoolType type) {
		try {
			return getGradeHome().findAllBySchoolType(type);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	public SchoolClassMemberGrade getStudentGrade(SchoolClassMember student) {
		try {
			return getStudentGradeHome().findByStudent(student);
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public void updateGrades(Object[] studentPKs, Object[] gradePKs, AdultEducationCourse course) {
		try {
			if (studentPKs != null) {
				for (int i = 0; i < studentPKs.length; i++) {
					try {
						if (((String) gradePKs[i]).length() == 0) {
							continue;
						}
						SchoolClassMember student = getSchoolBusiness().getSchoolClassMemberHome().findByPrimaryKey(
								studentPKs[i]);
						Grade grade = getGradeHome().findByPrimaryKey(gradePKs[i]);

						updateGrade(student, grade, course);
					}
					catch (FinderException fe) {
						fe.printStackTrace();
					}
					catch (CreateException ce) {
						ce.printStackTrace();
					}
				}
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	private void updateGrade(SchoolClassMember student, Grade grade, AdultEducationCourse course)
			throws CreateException {
		SchoolClassMemberGrade studentGrade = null;
		try {
			studentGrade = getStudentGradeHome().findByStudent(student);
			if (studentGrade.getGrade().getPrimaryKey().equals(grade.getPrimaryKey())) {
				return;
			}
			else {
				studentGrade = getStudentGradeHome().create();
			}
		}
		catch (FinderException fe) {
			studentGrade = getStudentGradeHome().create();
		}
		studentGrade.setStudent(student);
		studentGrade.setGrade(grade);
		studentGrade.setLocked(false);
		studentGrade.setCreated(new IWTimestamp().getTimestamp());
		studentGrade.store();

		IWTimestamp stamp = new IWTimestamp(course.getEndDate());
		student.setRemovedDate(stamp.getTimestamp());
		student.store();
	}

	public void terminatePlacement(SchoolClassMember student, Timestamp terminated) throws RemoveException {
		student.setRemovedDate(terminated);
		student.store();

		SchoolClass group = student.getSchoolClass();
		School school = group.getSchool();
		AdultEducationCourse course = null;
		SchoolStudyPath path = null;
		try {
			course = getCourse(new Integer(group.getSchoolSeasonId()), group.getCode());
		}
		catch (FinderException fe) {
			throw new RemoveException(fe.getMessage());
		}
		path = course.getStudyPath();
		IWTimestamp stamp = new IWTimestamp(terminated);

		String subject = getLocalizedString("placement_terminated_subject", "Placement terminated");
		String body = getLocalizedString("placement_terminated_body",
				"Your placement at {0} on course {1}, {2} has been been terminated from {3}.");
		Object[] arguments = {
				school.getSchoolName(),
				path.getDescription(),
				course.getCode(),
				stamp.getLocaleDate(getIWApplicationContext().getApplicationSettings().getDefaultLocale(),
						IWTimestamp.SHORT) };

		try {
			getMessageBusiness().createUserMessage(student.getStudent(), subject, MessageFormat.format(body, arguments));
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public void removePlacement(SchoolClassMember student, AdultEducationChoice choice, User performer)
			throws RemoveException {
		SchoolClass group = student.getSchoolClass();
		School school = group.getSchool();
		User user = student.getStudent();
		AdultEducationCourse course = null;
		SchoolStudyPath path = null;
		try {
			course = getCourse(new Integer(group.getSchoolSeasonId()), group.getCode());
		}
		catch (FinderException fe) {
			throw new RemoveException(fe.getMessage());
		}
		path = course.getStudyPath();
		student.remove();

		String subject = getLocalizedString("placement_removed_subject", "Placement removed");
		String body = getLocalizedString("placement_removed_body",
				"Your placement at {0} on course {1}, {2} has been been removed according to your wishes.");
		Object[] arguments = { school.getSchoolName(), path.getDescription(), course.getCode() };

		if (choice != null) {
			changeCaseStatus(choice, getCaseStatusDeleted().getStatus(), performer);
		}

		try {
			getMessageBusiness().createUserMessage(user, subject, MessageFormat.format(body, arguments));
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	public void storePackage(Object packagePK, String name, String localizedKey) throws CreateException {
		CoursePackage coursePackage = null;
		if (packagePK != null) {
			try {
				coursePackage = getCoursePackageHome().findByPrimaryKey(packagePK);
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
		}
		if (coursePackage == null) {
			coursePackage = getCoursePackageHome().create();
		}

		coursePackage.setName(name);
		coursePackage.setLocalizedKey(localizedKey);
		coursePackage.store();
	}

	public SchoolCoursePackage storeSchoolPackage(SchoolCoursePackage schoolPackage, CoursePackage coursePackage,
			School school, SchoolSeason season, String freeText, Object[] coursePKs) throws CreateException {
		if (schoolPackage == null) {
			schoolPackage = getSchoolCoursePackageHome().create();
			schoolPackage.setPackage(coursePackage);
			schoolPackage.setSchool(school);
			schoolPackage.setSeason(season);
		}

		schoolPackage.setFreeText(freeText);
		schoolPackage.store();

		if (coursePKs != null) {
			for (int i = 0; i < coursePKs.length; i++) {
				Object coursePK = coursePKs[i];
				try {
					AdultEducationCourse course = getCourseHome().findByPrimaryKey(coursePK);
					schoolPackage.addCourse(course);
				}
				catch (FinderException fe) {
					fe.printStackTrace();
				}
				catch (IDOAddRelationshipException iare) {
					log(iare);
				}
			}
		}
		return schoolPackage;
	}

	public void removeCourseFromPackage(SchoolCoursePackage schoolPackage, Object coursePK) {
		try {
			AdultEducationCourse course = getCourse(coursePK);
			schoolPackage.removeCourse(course);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
		catch (IDORemoveRelationshipException irre) {
			irre.printStackTrace();
		}
	}

	public void activatePackage(SchoolCoursePackage schoolPackage) {
		schoolPackage.setActive(true);
		schoolPackage.store();
	}

	public void deactivatePackage(SchoolCoursePackage schoolPackage) {
		schoolPackage.setActive(false);
		schoolPackage.store();
	}

	public void removePackage(Object coursePackagePK) throws RemoveException {
		try {
			CoursePackage coursePackage = getCoursePackageHome().findByPrimaryKey(coursePackagePK);
			coursePackage.remove();
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
	}

	public void removeSchoolPackage(SchoolCoursePackage schoolPackage) throws RemoveException {
		try {
			schoolPackage.removeCourses();
		}
		catch (IDORemoveRelationshipException irre) {
			irre.printStackTrace();
		}
		schoolPackage.remove();
	}

	public boolean hasSchoolPackages(CoursePackage coursePackage) {
		try {
			return getSchoolCoursePackageHome().getNumberOfSchoolPackages(coursePackage) > 0;
		}
		catch (IDOException ie) {
			ie.printStackTrace();
			return false;
		}
	}

	public Collection getCoursePackages() {
		try {
			return getCoursePackageHome().findAll();
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	public Collection getCoursePackages(School school, SchoolSeason season) {
		try {
			return getSchoolCoursePackageHome().findBySchoolAndSeason(school, season);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}

	public SchoolCoursePackage getSchoolCoursePackage(Object schoolCoursePackagePK) throws FinderException {
		return getSchoolCoursePackageHome().findByPrimaryKey(schoolCoursePackagePK);
	}

	public CoursePackage getCoursePackage(Object coursePackagePK) throws FinderException {
		return getCoursePackageHome().findByPrimaryKey(coursePackagePK);
	}

	public Collection getSchoolCoursePackages(School school, SchoolSeason season, CoursePackage coursePackage) {
		try {
			return getSchoolCoursePackageHome().findBySchoolAndSeasonAndPackage(school, season, coursePackage);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}
}