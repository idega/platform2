package com.idega.block.school.data;


public interface SchoolClassMemberHome extends com.idega.data.IDOHome
{
 public SchoolClassMember create() throws javax.ejb.CreateException;
 public SchoolClassMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findActiveByCategorySeasonAndSchools(com.idega.block.school.data.SchoolCategory p0,com.idega.block.school.data.SchoolSeason p1, java.lang.String[] p2, boolean p3)throws javax.ejb.FinderException;
 public java.util.Collection findAllBySchoolAndUsersWithSchoolYearAndNotRemoved(int p0,java.util.Collection p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllBySchoolStudyPath(com.idega.block.school.data.SchoolStudyPath p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllBySeasonAndSchoolYear(com.idega.block.school.data.SchoolSeason p0,com.idega.block.school.data.SchoolYear p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllByUserAndPeriodAndSchoolCategory(com.idega.user.data.User p0,java.sql.Date p1,com.idega.block.school.data.SchoolCategory p2)throws javax.ejb.FinderException;
 public java.util.Collection findAllByUserAndSchoolCategory(com.idega.user.data.User p0,com.idega.block.school.data.SchoolCategory p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllByUserAndSeason(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllByUserAndSeason(com.idega.user.data.User p0,com.idega.block.school.data.SchoolSeason p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCurrentInvoiceCompensationBySchoolTypeAndSchools(java.lang.String p0,java.util.Collection p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllLastYearStudentsBySeasonAndMaximumAge(com.idega.block.school.data.SchoolSeason p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllLastYearStudentsBySeasonAndYear(com.idega.block.school.data.SchoolSeason p0,com.idega.block.school.data.SchoolYear p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllLastYearStudentsBySeasonAndYearAndMaximumAge(com.idega.block.school.data.SchoolSeason p0,com.idega.block.school.data.SchoolYear p1,int p2)throws javax.ejb.FinderException;
 public java.util.Collection findAllOrderedByRegisterDate(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllOrderedByRegisterDate(com.idega.user.data.User p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllSubGroupPlacements(int p0,int p1,int p2)throws javax.ejb.FinderException;
 public java.util.Collection findByCategorydManagementCommune(java.lang.String p0,java.lang.String p1,int p2,int p3)throws javax.ejb.FinderException;
 public java.util.Collection findBySchool(int p0,int p1,java.sql.Date p2)throws javax.ejb.FinderException;
 public java.util.Collection findBySchool(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findBySchool(int p0,int p1,java.lang.String p2,java.sql.Date p3)throws javax.ejb.FinderException;
 public java.util.Collection findBySchool(int p0,int p1,java.lang.String p2,java.sql.Date p3,boolean p4)throws javax.ejb.FinderException;
 public java.util.Collection findBySchool(int p0,int p1,java.sql.Date p2,boolean p3)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndSeason(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndSeasonAndYear(int p0,int p1,int p2)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolClass(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolClass(com.idega.block.school.data.SchoolClass p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolClassAndYear(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolClassAndYearAndStudyPath(SchoolClass p0,SchoolYear p1,SchoolStudyPath p2)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolClasses(java.util.Collection p0)throws javax.ejb.FinderException;
 public java.util.Collection findByStudent(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findByStudent(com.idega.user.data.User p0)throws javax.ejb.FinderException;
 public java.util.Collection findByStudentAndSchool(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findByStudentAndSchoolAndTypes(int p0,int p1,java.util.Collection p2)throws javax.ejb.FinderException;
 public java.util.Collection findByStudentAndTypes(int p0,java.util.Collection p1)throws javax.ejb.FinderException;
 public SchoolClassMember findByUserAndSchool(int p0,int p1)throws javax.ejb.FinderException;
 public SchoolClassMember findByUserAndSchoolAndSeason(int p0,int p1,int p2)throws javax.ejb.FinderException;
 public SchoolClassMember findByUserAndSchoolAndSeason(int p0,int p1,int p2,java.util.Collection p3)throws javax.ejb.FinderException;
 public SchoolClassMember findByUserAndSchoolAndSeasonAndStudyPath(int p0, int p1, int p2, int p3)throws javax.ejb.FinderException;
 public int countByUserAndSchoolAndSeasonAndStudyPath(int p0, int p1, int p2, int p3)throws com.idega.data.IDOException;
 public int countByUserAndSchoolAndSeasonAndStudyPath(com.idega.user.data.User p0, School p1, SchoolSeason p2, SchoolStudyPath p3)throws com.idega.data.IDOException;
 public SchoolClassMember findByUserAndSchoolClass(int p0,int p1)throws javax.ejb.FinderException;
 public SchoolClassMember findByUserAndSchoolClass(com.idega.user.data.User p0,com.idega.block.school.data.SchoolClass p1)throws javax.ejb.FinderException;
 public SchoolClassMember findByUserAndSeason(int p0,int p1)throws javax.ejb.FinderException;
 public SchoolClassMember findByUserAndSeason(com.idega.user.data.User p0,com.idega.block.school.data.SchoolSeason p1)throws javax.ejb.FinderException;
 public SchoolClassMember findLatestByUser(com.idega.user.data.User p0)throws javax.ejb.FinderException;
 public SchoolClassMember findLatestByUserAndSchCategory(com.idega.user.data.User p0,com.idega.block.school.data.SchoolCategory p1)throws javax.ejb.FinderException;
 public SchoolClassMember findLatestByUserAndSchCategoryAndSeason(com.idega.user.data.User p0,com.idega.block.school.data.SchoolCategory p1,com.idega.block.school.data.SchoolSeason p2)throws javax.ejb.FinderException;
 public SchoolClassMember findLatestByUserAndSchool(int p0,int p1,java.util.Collection p2)throws javax.ejb.FinderException;
 public SchoolClassMember findLatestByUserAndSchool(int p0,int p1)throws javax.ejb.FinderException;
 public SchoolClassMember findLatestByUserAndSchoolType(com.idega.user.data.User p0,com.idega.block.school.data.SchoolType p1)throws javax.ejb.FinderException;
 public SchoolClassMember findLatestFromElemAndHighSchoolByUser(com.idega.user.data.User p0)throws javax.ejb.FinderException;
 public SchoolClassMember findLatestFromElemAndHighSchoolByUserAndSeason(com.idega.user.data.User p0,com.idega.block.school.data.SchoolSeason p1)throws javax.ejb.FinderException;
 public java.util.Collection getInvoiceIntervalTypes();
 public int getNumberOfPlacings(int p0,int p1)throws com.idega.data.IDOException;
 public int getNumberOfSubGroupPlacings(int p0,int p1)throws com.idega.data.IDOException;
 public int getNumberOfPlacings(int p0)throws com.idega.data.IDOException;
 public int getNumberOfPlacingsAtSchool(int p0,int p1)throws com.idega.data.IDOException;
 public int getNumberOfUsersNotAssignedToClassOnGivenDate(com.idega.user.data.Group p0,java.sql.Date p1,java.util.Collection p2,java.sql.Date p3,java.sql.Date p4)throws com.idega.data.IDOException,com.idega.data.IDOLookupException;
 public int getNumberOfUsersNotAssignedToClassOnGivenDateNew(com.idega.user.data.Group p0,java.sql.Date p1,com.idega.block.school.data.SchoolSeason p2,java.sql.Date p3,java.sql.Date p4)throws com.idega.data.IDOException,com.idega.data.IDOLookupException;
	public java.util.Collection findSubGroupPlacements() throws javax.ejb.FinderException;
 
}