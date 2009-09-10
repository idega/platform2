package com.idega.block.school.data;


public interface SchoolUserPaymentHistoryHome extends com.idega.data.IDOHome
{
 public SchoolUserPaymentHistory create() throws javax.ejb.CreateException;
 public SchoolUserPaymentHistory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllBySchoolUserPaymentID(int p0)throws javax.ejb.FinderException;
 public java.lang.Object makeHistoryFromSchoolUserPaymentID(int p0)throws com.idega.data.IDOLookupException,javax.ejb.FinderException,javax.ejb.CreateException;

}