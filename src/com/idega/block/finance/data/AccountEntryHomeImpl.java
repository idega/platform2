package com.idega.block.finance.data;


public class AccountEntryHomeImpl extends com.idega.data.IDOFactory implements AccountEntryHome
{
 protected Class getEntityInterfaceClass(){
  return AccountEntry.class;
 }


 public AccountEntry create() throws javax.ejb.CreateException{
  return (AccountEntry) super.createIDO();
 }


public java.util.Collection findByAccountAndAssessmentRound(java.lang.Integer p0,java.lang.Integer p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AccountEntryBMPBean)entity).ejbFindByAccountAndAssessmentRound(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByAccountAndStatus(java.lang.Integer p0,java.lang.String p1,java.sql.Date p2,java.sql.Date p3,String assessmentStatus)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AccountEntryBMPBean)entity).ejbFindByAccountAndStatus(p0,p1,p2,p3,assessmentStatus);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByAssessmentRound(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AccountEntryBMPBean)entity).ejbFindByAssessmentRound(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByEntryGroup(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AccountEntryBMPBean)entity).ejbFindByEntryGroup(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findUnGrouped(java.sql.Date p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((AccountEntryBMPBean)entity).ejbFindUnGrouped(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public AccountEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AccountEntry) super.findByPrimaryKeyIDO(pk);
 }


public int countByGroup(java.lang.Integer p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((AccountEntryBMPBean)entity).ejbHomeCountByGroup(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public java.sql.Date getMaxDateByAccount(java.lang.Integer p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.sql.Date theReturn = ((AccountEntryBMPBean)entity).ejbHomeGetMaxDateByAccount(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public double getTotalSumByAccount(java.lang.Integer p0)throws java.sql.SQLException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	double theReturn = ((AccountEntryBMPBean)entity).ejbHomeGetTotalSumByAccount(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public double getTotalSumByAccount(java.lang.Integer p0,String roundStatus)throws java.sql.SQLException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	double theReturn = ((AccountEntryBMPBean)entity).ejbHomeGetTotalSumByAccount(p0,roundStatus);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public double getTotalSumByAccountAndAssessmentRound(java.lang.Integer p0,java.lang.Integer p1)throws java.sql.SQLException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	double theReturn = ((AccountEntryBMPBean)entity).ejbHomeGetTotalSumByAccountAndAssessmentRound(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public double getTotalSumByAssessmentRound(java.lang.Integer p0)throws java.sql.SQLException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	double theReturn = ((AccountEntryBMPBean)entity).ejbHomeGetTotalSumByAssessmentRound(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}