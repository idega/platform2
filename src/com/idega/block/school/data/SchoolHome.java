package com.idega.block.school.data;

import java.util.Collection;

public interface SchoolHome extends com.idega.data.IDOHome
{
 public School create() throws javax.ejb.CreateException;
 public School findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Collection findAllByPrimaryKeys(Collection p0) throws javax.ejb.FinderException;
 public Collection findAllByAreaAndType(int p0,int p1)throws javax.ejb.FinderException;
 public Collection findAllByAreaAndTypeAndYear(int p0,int p1,int p2)throws javax.ejb.FinderException;
 public Collection findAllByAreaAndTypes(int p0,Collection p1)throws javax.ejb.FinderException;
 public Collection findAllBySubAreaAndTypes(int subarea, Collection types)throws javax.ejb.FinderException;;
 public Collection findAllByAreaTypeCommune(int p0,int p1,int p2)throws javax.ejb.FinderException;
 public Collection findAllByAreaTypeManagement(int p0,int p1,java.lang.String p2)throws javax.ejb.FinderException;
 public Collection findAllByAreaTypeManagementCommune(int p0,int p1,Collection p2,int p3)throws javax.ejb.FinderException;
 public Collection findAllByAreaTypeManagementCommune(int p0,int p1,java.lang.String p2,int p3)throws javax.ejb.FinderException;
 public Collection findAllByAreaTypeManagementCommune(int p0,Collection p1,Collection p2,int p3)throws javax.ejb.FinderException;
 public Collection findAllByCategory(com.idega.block.school.data.SchoolCategory p0)throws javax.ejb.FinderException;
 public Collection findAllBySchoolArea(int p0)throws javax.ejb.FinderException;
 public Collection findAllBySchoolGroup(com.idega.user.data.Group p0)throws javax.ejb.FinderException;
 public Collection findAllBySchoolName(java.lang.String p0)throws javax.ejb.FinderException;
 public Collection findAllBySchoolType(Collection p0)throws javax.ejb.FinderException;
 public Collection findAllBySchoolType(int p0)throws javax.ejb.FinderException;
 public Collection findAllCentralizedAdministrated()throws javax.ejb.FinderException;
 public Collection findAllCentralizedAdministratedByType(Collection p0)throws javax.ejb.FinderException;
 public Collection findAllInHomeCommuneByCategory(com.idega.block.school.data.SchoolCategory p0)throws com.idega.data.IDOLookupException,javax.ejb.EJBException,javax.ejb.FinderException;
 public Collection findAllPrivate()throws com.idega.data.IDOLookupException,javax.ejb.EJBException,javax.ejb.FinderException;
 public Collection findAllSchools()throws javax.ejb.FinderException;
 public Collection findAllSchoolsByCategoryIncludingTerminated(java.lang.String p0)throws javax.ejb.FinderException;
 public Collection findAllSchoolsIncludingTerminated()throws javax.ejb.FinderException;
 public School findBySchoolName(java.lang.String p0)throws javax.ejb.FinderException;
 public int getNumberOfFreetimeTypes(int p0)throws com.idega.data.IDOException;
 public int getNumberOfRelations(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolYear p1)throws com.idega.data.IDOException;

}
