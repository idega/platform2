package com.idega.block.application.data;


public interface ApplicantHome extends com.idega.data.IDOHome
{
 public Applicant create() throws javax.ejb.CreateException;
 public Applicant createLegacy();
 public Applicant findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Applicant findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Applicant findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public java.util.Collection findBySSN(java.lang.String p0)throws javax.ejb.FinderException;

}