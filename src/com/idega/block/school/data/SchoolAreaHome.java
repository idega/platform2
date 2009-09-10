package com.idega.block.school.data;


public interface SchoolAreaHome extends com.idega.data.IDOHome
{
 public SchoolArea create() throws javax.ejb.CreateException;
 public SchoolArea findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllBySchoolType(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllBySchoolTypeAndCity(int p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllBySchoolTypeCityAndManagementTypes(int p0,java.lang.String p1,java.util.Collection p2)throws javax.ejb.FinderException;
 public java.util.Collection findAllBySchoolTypes(java.util.Collection p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllSchoolAreas()throws javax.ejb.FinderException;
 public SchoolArea findSchoolAreaByAreaName(java.lang.String p0)throws javax.ejb.FinderException;

}