package com.idega.block.school.data;


import com.idega.data.IDOException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.user.data.User;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class SchoolClassHomeImpl extends IDOFactory implements SchoolClassHome {
	protected Class getEntityInterfaceClass() {
		return SchoolClass.class;
	}

	public SchoolClass create() throws CreateException {
		return (SchoolClass) super.createIDO();
	}

	public SchoolClass findByPrimaryKey(Object pk) throws FinderException {
		return (SchoolClass) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findBySchool(School school) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchool(school);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchool(int schoolID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchool(schoolID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndSeason(School school, SchoolSeason schoolSeason) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndSeason(school, schoolSeason);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndSeason(int schoolID, int schoolSeasonID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndSeason(schoolID, schoolSeasonID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndYear(School school, SchoolYear schoolYear) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndYear(school, schoolYear);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndYear(int schoolID, int schoolYearID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndYear(schoolID, schoolYearID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndInYear(int schoolID, int schoolYearID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndInYear(schoolID, schoolYearID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndSeasonAndYear(School school, SchoolSeason schoolSeason, SchoolYear schoolYear) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndSeasonAndYear(school, schoolSeason, schoolYear);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndSeasonAndYear(int schoolID, int schoolSeasonID, int schoolYearID, boolean showSubGroups) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndSeasonAndYear(schoolID, schoolSeasonID, schoolYearID, showSubGroups);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndSeasonAndInYear(int schoolID, int schoolSeasonID, int schoolYearID, boolean showSubGroups) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndSeasonAndInYear(schoolID, schoolSeasonID, schoolYearID, showSubGroups);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndSeasonAndYearAndStudyPath(School school, SchoolSeason schoolSeason, SchoolYear schoolYear, SchoolStudyPath studyPath, boolean showSubGroups) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndSeasonAndYearAndStudyPath(school, schoolSeason, schoolYear, studyPath, showSubGroups);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndSeasonAndYear(int schoolID, int schoolSeasonID, int schoolYearID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndSeasonAndYear(schoolID, schoolSeasonID, schoolYearID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndSeasonAndInYear(int schoolID, int schoolSeasonID, int schoolYearID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndSeasonAndInYear(schoolID, schoolSeasonID, schoolYearID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndSeasonAndInYear(int schoolID, int schoolSeasonID, int schoolYearID, int studyPathID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndSeasonAndInYear(schoolID, schoolSeasonID, schoolYearID, studyPathID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndSeasonAndYears(int schoolID, int schoolSeasonID, String[] schoolYearIDs) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndSeasonAndYears(schoolID, schoolSeasonID, schoolYearIDs);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndSeasonAndYears(int schoolID, int schoolSeasonID, String[] schoolYearIDs, boolean showSubGroups) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndSeasonAndYears(schoolID, schoolSeasonID, schoolYearIDs, showSubGroups);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndSeasonAndCode(School school, SchoolSeason season, String code) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndSeasonAndCode(school, season, code);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySeasonAndYear(SchoolSeason schoolSeason, SchoolYear schoolYear) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySeasonAndYear(schoolSeason, schoolYear);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndSchoolTypeAndSeason(int schoolID, int schoolTypeID, int seasonID, Boolean showSubGroups, Boolean showNonSeasonGroups) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndSchoolTypeAndSeason(schoolID, schoolTypeID, seasonID, showSubGroups, showNonSeasonGroups);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySeasonAndYear(int schoolSeasonID, int schoolYearID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySeasonAndYear(schoolSeasonID, schoolYearID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndCategory(int schoolID, String category) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndCategory(schoolID, category);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySeason(SchoolSeason schoolSeason) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySeason(schoolSeason);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySeason(int schoolSeasonID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySeason(schoolSeasonID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByTeacher(User teacher) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindByTeacher(teacher);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByTeacher(int teacherID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindByTeacher(teacherID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndTeacher(School school, User teacher) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndTeacher(school, teacher);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndTeacher(int schoolID, int teacherID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndTeacher(schoolID, teacherID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndSeasonAndTeacher(School school, SchoolSeason schoolSeason, User teacher) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndSeasonAndTeacher(school, schoolSeason, teacher);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findBySchoolAndSeasonAndTeacher(int schoolID, int schoolSeasonID, int teacherID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindBySchoolAndSeasonAndTeacher(schoolID, schoolSeasonID, teacherID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SchoolClass findByNameAndSchool(String className, School school) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassBMPBean) entity).ejbFindByNameAndSchool(className, school);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClass findByNameAndSchool(String className, int schoolID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassBMPBean) entity).ejbFindByNameAndSchool(className, schoolID);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClass findBySchoolClassNameSchoolSchoolYearSchoolSeason(String className, School school, SchoolYear schoolYear, SchoolSeason schoolSeason) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassBMPBean) entity).ejbFindBySchoolClassNameSchoolSchoolYearSchoolSeason(className, school, schoolYear, schoolSeason);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public int getNumberOfStudentsInClass(int schoolClassID) throws IDOException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int theReturn = ((SchoolClassBMPBean) entity).ejbHomeGetNumberOfStudentsInClass(schoolClassID);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((SchoolClassBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SchoolClass findOneBySchool(int schoolID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassBMPBean) entity).ejbFindOneBySchool(schoolID);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClass findOneByCode(String code) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassBMPBean) entity).ejbFindOneByCode(code);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public SchoolClass findOneByCodeAndSeason(String code, SchoolSeason season) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolClassBMPBean) entity).ejbFindOneByCodeAndSeason(code, season);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}