package se.idega.idegaweb.commune.adulteducation.business;


import com.idega.block.process.business.CaseBusiness;
import com.idega.block.school.data.School;
import com.idega.block.process.data.Case;
import javax.ejb.CreateException;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoice;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationPersonalInfo;
import com.idega.block.school.data.SchoolClassMemberGrade;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationCourse;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import com.idega.business.IBOService;
import se.idega.idegaweb.commune.accounting.school.business.StudyPathBusiness;
import com.idega.block.school.data.SchoolStudyPathGroup;
import se.idega.idegaweb.commune.adulteducation.data.SchoolCoursePackage;
import se.idega.idegaweb.commune.adulteducation.data.CoursePackage;
import com.idega.user.data.User;
import java.sql.Date;
import com.idega.block.school.data.SchoolClassMember;
import java.rmi.RemoteException;
import com.idega.data.IDOCreateException;
import java.sql.Timestamp;
import java.util.Locale;
import java.util.Collection;
import com.idega.block.school.data.SchoolClass;
import javax.ejb.FinderException;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolCategory;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationPersonalInfoHome;
import javax.ejb.RemoveException;

public interface AdultEducationBusiness extends IBOService, CaseBusiness {
	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getBundleIdentifier
	 */
	public String getBundleIdentifier() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getLocalizedCaseDescription
	 */
	public String getLocalizedCaseDescription(Case theCase, Locale locale) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getSchoolForUser
	 */
	public School getSchoolForUser(User user) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getSchoolBusiness
	 */
	public SchoolBusiness getSchoolBusiness() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getUserBusiness
	 */
	public CommuneUserBusiness getUserBusiness() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getStudyPathBusiness
	 */
	public StudyPathBusiness getStudyPathBusiness() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getCourse
	 */
	public AdultEducationCourse getCourse(Object season, String code) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getCourse
	 */
	public AdultEducationCourse getCourse(Object coursePK) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getCourses
	 */
	public Collection getCourses(Object season, Object school, Object group) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#findAvailableCourses
	 */
	public Collection findAvailableCourses(SchoolType schoolType, SchoolSeason schoolSeason, SchoolStudyPathGroup studyPathGroup, SchoolStudyPath studyPath, School school) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getCoursesWithStudents
	 */
	public Collection getCoursesWithStudents(Object season, Object school, Object group) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getCourses
	 */
	public Collection getCourses(Object season, Object type, Object school, Object group) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getGroups
	 */
	public Collection getGroups(School school, SchoolSeason season, String code) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getGroups
	 */
	public Collection getGroups(School school, SchoolSeason season, SchoolStudyPathGroup group) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getStudents
	 */
	public Collection getStudents(SchoolClass group) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#storeGroup
	 */
	public void storeGroup(SchoolClass group, String name, School school, SchoolSeason season, SchoolType type, String code, User teacher, boolean update) throws CreateException, DuplicateValueException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#hasPlacement
	 */
	public boolean hasPlacement(String courseCode) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#hasPlacement
	 */
	public boolean hasPlacement(String courseCode, SchoolSeason season) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getNumberOfPlacementsForCourse
	 */
	public int getNumberOfPlacementsForCourse(String courseCode) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getNumberOfPlacementsForCourseAndSeason
	 */
	public int getNumberOfPlacementsForCourseAndSeason(String courseCode, SchoolSeason season) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#hasActiveChoices
	 */
	public boolean hasActiveChoices(SchoolSeason season, AdultEducationCourse course) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getNumberOfActiveChoices
	 */
	public int getNumberOfActiveChoices(SchoolSeason season, AdultEducationCourse course) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getChoices
	 */
	public Collection getChoices(SchoolSeason season, AdultEducationCourse course) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#createDefaultGroup
	 */
	public SchoolClass createDefaultGroup(SchoolSeason season, AdultEducationCourse course) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getChoice
	 */
	public AdultEducationChoice getChoice(User user, AdultEducationCourse course) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getChoice
	 */
	public AdultEducationChoice getChoice(Object choicePK) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getChoice
	 */
	public AdultEducationChoice getChoice(User user, Object seasonPK, Object studyPathPK, int choiceOrder) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getChoices
	 */
	public Collection getChoices(User user, SchoolSeason season) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getChoices
	 */
	public Collection getChoices(User user, SchoolSeason season, SchoolStudyPath path) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getChoices
	 */
	public Collection getChoices(User user) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getChoices
	 */
	public Collection getChoices(SchoolSeason season, SchoolType type, Date fromDate, Date toDate, User handler) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getUnhandledChoices
	 */
	public Collection getUnhandledChoices(SchoolSeason season, SchoolType type, Date fromDate, Date toDate, User handler) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getHandledChoices
	 */
	public Collection getHandledChoices(SchoolSeason season, SchoolType type, Date fromDate, Date toDate, User handler) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getSelectedStudyPaths
	 */
	public Collection getSelectedStudyPaths(User user, SchoolSeason season) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getAvailableSchools
	 */
	public Collection getAvailableSchools(Object studyPathPK, Object seasonPK) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getAvailableSchools
	 */
	public Collection getAvailableSchools(Object studyPathPK, Object seasonPK, Object studyPathGroupPK, Object schoolTypePK) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getAvailableCourses
	 */
	public Collection getAvailableCourses(Object seasonPK, Object schoolPK, Object studyPathPK) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getPendingSeasons
	 */
	public Collection getPendingSeasons() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getActiveReasons
	 */
	public Collection getActiveReasons() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getCategory
	 */
	public SchoolCategory getCategory() throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getSchoolTypes
	 */
	public Collection getSchoolTypes() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getSeasons
	 */
	public Collection getSeasons() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getCurrentSeasons
	 */
	public Collection getCurrentSeasons() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getNextSeasons
	 */
	public Collection getNextSeasons(SchoolSeason season) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getSchools
	 */
	public Collection getSchools(SchoolType type) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getSchools
	 */
	public Collection getSchools() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getStudyPathsGroups
	 */
	public Collection getStudyPathsGroups() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getStudyPaths
	 */
	public Collection getStudyPaths(SchoolType type, SchoolStudyPathGroup group) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getStydyPathsBySchoolTypeAndSchoolStudyPathGroup
	 */
	public Collection getStydyPathsBySchoolTypeAndSchoolStudyPathGroup(SchoolType type, SchoolStudyPathGroup group) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#storeCourse
	 */
	public AdultEducationCourse storeCourse(Object season, Object oldSeason, String code, String oldCode, Object school, Object studyPath, Date startDate, Date endDate, String comment, int length, boolean notActive, boolean update) throws CreateException, DuplicateValueException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#storeChoices
	 */
	public void storeChoices(User user, SchoolSeason season, Collection packagePKs, String comment, Object[] reasons, String otherReason) throws IDOCreateException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#storeChoices
	 */
	public void storeChoices(User user, Collection courses, Object[] oldCourses, String comment, Object[] reasons, String otherReason) throws IDOCreateException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#grantChoice
	 */
	public void grantChoice(AdultEducationChoice choice, boolean rule1, boolean rule2, boolean rule3, boolean rule4, String ruleNotes, String notes, int priority, User performer) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#denyChoice
	 */
	public void denyChoice(AdultEducationChoice choice, String rejectionMessage, User performer) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#saveChoiceChanges
	 */
	public void saveChoiceChanges(AdultEducationChoice choice, boolean rule1, boolean rule2, boolean rule3, boolean rule4, String ruleNotes, String notes, int priority, User performer) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#removeCourse
	 */
	public void removeCourse(Object coursePK) throws RemoveException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#removeChoices
	 */
	public void removeChoices(Object studyPathPK, Object seasonPK, User performer) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#removeChoices
	 */
	public void removeChoices(Object studyPathPK, Object seasonPK, Object userPK, User performer) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#placeChoices
	 */
	public void placeChoices(Object[] choicePKs, SchoolClass group, AdultEducationCourse course, Date date, User performer) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#rejectChoices
	 */
	public void rejectChoices(Object[] choicePKs, User performer) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#removeStudent
	 */
	public void removeStudent(Object schoolClassMemberPK, Object choicePK, User performer) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#sendPlacementMessage
	 */
	public void sendPlacementMessage(SchoolClass group, AdultEducationCourse course, String message) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#changeCourse
	 */
	public void changeCourse(AdultEducationChoice choice, Object coursePK) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#changeGroup
	 */
	public void changeGroup(SchoolClassMember member, Object groupPK) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#removeGroup
	 */
	public void removeGroup(SchoolClass group) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#storePersonalInfo
	 */
	public AdultEducationPersonalInfo storePersonalInfo(int icUserID, int nativecountryId, int languageID, int educationCountryID, boolean nativeThisCountry, boolean citizenThisCountry, boolean educationA, boolean educationB, boolean educationC, boolean educationD, boolean educationE, String educationF, String educationG, int eduGCountryID, int eduYears, boolean eduHA, boolean eduHB, boolean eduHC, String eduHCommune, boolean fulltime, boolean langSfi, boolean langSas, boolean langOther, boolean studySupport, boolean workUnEmpl, boolean workEmpl, boolean workKicked, String workOther) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#createOverviewPDF
	 */
	public void createOverviewPDF(User user, SchoolSeason season, String path, String fileName, Locale locale) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getAdultEducationPersonalHome
	 */
	public AdultEducationPersonalInfoHome getAdultEducationPersonalHome() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getGrades
	 */
	public Collection getGrades(SchoolType type) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getStudentGrade
	 */
	public SchoolClassMemberGrade getStudentGrade(SchoolClassMember student) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#updateGrades
	 */
	public void updateGrades(Object[] studentPKs, Object[] gradePKs, AdultEducationCourse course) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#terminatePlacement
	 */
	public void terminatePlacement(SchoolClassMember student, Timestamp terminated) throws RemoveException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#removePlacement
	 */
	public void removePlacement(SchoolClassMember student, AdultEducationChoice choice, User performer) throws RemoveException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#storePackage
	 */
	public void storePackage(Object packagePK, String name, String localizedKey) throws CreateException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#storeSchoolPackage
	 */
	public SchoolCoursePackage storeSchoolPackage(SchoolCoursePackage schoolPackage, CoursePackage coursePackage, School school, SchoolSeason season, String freeText, Object[] coursePKs) throws CreateException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#removeCourseFromPackage
	 */
	public void removeCourseFromPackage(SchoolCoursePackage schoolPackage, Object coursePK) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#activatePackage
	 */
	public void activatePackage(SchoolCoursePackage schoolPackage) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#deactivatePackage
	 */
	public void deactivatePackage(SchoolCoursePackage schoolPackage) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#removePackage
	 */
	public void removePackage(Object coursePackagePK) throws RemoveException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#removeSchoolPackage
	 */
	public void removeSchoolPackage(SchoolCoursePackage schoolPackage) throws RemoveException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#hasSchoolPackages
	 */
	public boolean hasSchoolPackages(CoursePackage coursePackage) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getCoursePackages
	 */
	public Collection getCoursePackages() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getCoursePackages
	 */
	public Collection getCoursePackages(School school, SchoolSeason season) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getSchoolCoursePackage
	 */
	public SchoolCoursePackage getSchoolCoursePackage(Object schoolCoursePackagePK) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getCoursePackage
	 */
	public CoursePackage getCoursePackage(Object coursePackagePK) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.business.AdultEducationBusinessBean#getSchoolCoursePackages
	 */
	public Collection getSchoolCoursePackages(School school, SchoolSeason season, CoursePackage coursePackage) throws RemoteException;
}