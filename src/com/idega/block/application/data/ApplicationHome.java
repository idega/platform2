package com.idega.block.application.data;


public interface ApplicationHome extends com.idega.data.IDOHome
{
 public Application create() throws javax.ejb.CreateException;
 public Application findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByApplicantAndStatus(java.lang.Integer p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findByApplicantID(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySQL(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findByStatus(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySubject(java.lang.Integer p0)throws javax.ejb.FinderException;
 public java.util.Collection findBySubjectAndStatus(java.lang.Integer p0,java.lang.String p1)throws javax.ejb.FinderException;

}