package com.idega.block.application.data;


public interface ApplicantHome extends com.idega.data.IDOHome
{
 public Applicant create() throws javax.ejb.CreateException;
 public Applicant findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByApplicationStatusOrderedBy(java.lang.String p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findBySQL(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySSN(java.lang.String p0)throws javax.ejb.FinderException;

}