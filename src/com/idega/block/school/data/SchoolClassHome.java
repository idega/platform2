package com.idega.block.school.data;


import com.idega.data.IDOException;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import com.idega.user.data.User;

public interface SchoolClassHome extends IDOHome {
	public SchoolClass create() throws CreateException;

	public SchoolClass findByPrimaryKey(Object pk) throws FinderException;

	public Collection findBySchool(School school) throws FinderException;

	public Collection findBySchool(int schoolID) throws FinderException;

	public Collection findBySchoolAndSeason(School school, SchoolSeason schoolSeason) throws FinderException;

	public Collection findBySchoolAndSeason(int schoolID, int schoolSeasonID) throws FinderException;

	public Collection findBySchoolAndYear(School school, SchoolYear schoolYear) throws FinderException;

	public Collection findBySchoolAndYear(int schoolID, int schoolYearID) throws FinderException;

	public Collection findBySchoolAndInYear(int schoolID, int schoolYearID) throws FinderException;

	public Collection findBySchoolAndSeasonAndYear(School school, SchoolSeason schoolSeason, SchoolYear schoolYear) throws FinderException;

	public Collection findBySchoolAndSeasonAndYear(int schoolID, int schoolSeasonID, int schoolYearID, boolean showSubGroups) throws FinderException;

	public Collection findBySchoolAndSeasonAndInYear(int schoolID, int schoolSeasonID, int schoolYearID, boolean showSubGroups) throws FinderException;

	public Collection findBySchoolAndSeasonAndYearAndStudyPath(School school, SchoolSeason schoolSeason, SchoolYear schoolYear, SchoolStudyPath studyPath, boolean showSubGroups) throws FinderException;

	public Collection findBySchoolAndSeasonAndYear(int schoolID, int schoolSeasonID, int schoolYearID) throws FinderException;

	public Collection findBySchoolAndSeasonAndInYear(int schoolID, int schoolSeasonID, int schoolYearID) throws FinderException;

	public Collection findBySchoolAndSeasonAndInYear(int schoolID, int schoolSeasonID, int schoolYearID, int studyPathID) throws FinderException;

	public Collection findBySchoolAndSeasonAndYears(int schoolID, int schoolSeasonID, String[] schoolYearIDs) throws FinderException;

	public Collection findBySchoolAndSeasonAndYears(int schoolID, int schoolSeasonID, String[] schoolYearIDs, boolean showSubGroups) throws FinderException;

	public Collection findBySchoolAndSeasonAndCode(School school, SchoolSeason season, String code) throws FinderException;

	public Collection findBySeasonAndYear(SchoolSeason schoolSeason, SchoolYear schoolYear) throws FinderException;

	public Collection findBySchoolAndSchoolTypeAndSeason(int schoolID, int schoolTypeID, int seasonID, Boolean showSubGroups, Boolean showNonSeasonGroups) throws FinderException;

	public Collection findBySeasonAndYear(int schoolSeasonID, int schoolYearID) throws FinderException;

	public Collection findBySchoolAndCategory(int schoolID, String category) throws FinderException;

	public Collection findBySeason(SchoolSeason schoolSeason) throws FinderException;

	public Collection findBySeason(int schoolSeasonID) throws FinderException;

	public Collection findByTeacher(User teacher) throws FinderException;

	public Collection findByTeacher(int teacherID) throws FinderException;

	public Collection findBySchoolAndTeacher(School school, User teacher) throws FinderException;

	public Collection findBySchoolAndTeacher(int schoolID, int teacherID) throws FinderException;

	public Collection findBySchoolAndSeasonAndTeacher(School school, SchoolSeason schoolSeason, User teacher) throws FinderException;

	public Collection findBySchoolAndSeasonAndTeacher(int schoolID, int schoolSeasonID, int teacherID) throws FinderException;

	public SchoolClass findByNameAndSchool(String className, School school) throws FinderException;

	public SchoolClass findByNameAndSchool(String className, int schoolID) throws FinderException;

	public SchoolClass findBySchoolClassNameSchoolSchoolYearSchoolSeason(String className, School school, SchoolYear schoolYear, SchoolSeason schoolSeason) throws FinderException;

	public int getNumberOfStudentsInClass(int schoolClassID) throws IDOException;

	public Collection findAll() throws FinderException;

	public SchoolClass findOneBySchool(int schoolID) throws FinderException;

	public SchoolClass findOneByCode(String code) throws FinderException;

	public SchoolClass findOneByCodeAndSeason(String code, SchoolSeason season) throws FinderException;
}