package com.idega.block.finance.business;


public interface AssessmentBusiness extends com.idega.business.IBOService
{
 public void assessTariffsToAccount(float p0,java.lang.String p1,java.lang.String p2,java.lang.Integer p3,java.lang.Integer p4,java.util.Date p5,java.lang.Integer p6,java.lang.Integer p7,java.lang.Integer p8,boolean p9,Integer assessmentRound) throws java.rmi.RemoteException;
 public void assessTariffsToAccount(java.lang.Integer[] p0,java.lang.Double[] p1,java.lang.Integer p2,java.util.Date p3,int p4,java.lang.Integer p5,java.lang.Integer p6,java.lang.Integer p7,Integer assessmentRound)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void assessTariffsToAccount(java.util.List p0,java.util.List p1,java.lang.Integer p2,java.util.Date p3,int p4,java.lang.Integer p5,java.lang.Integer p6,java.lang.Integer p7,Integer assessmentRound) throws java.rmi.RemoteException;
 public com.idega.block.finance.data.AccountEntry createAccountEntry(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,float p4,float p5,float p6,java.util.Date p7,java.lang.String p8,java.lang.String p9,java.lang.String p10,java.lang.Integer p11)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
 public int getGroupEntryCount(com.idega.block.finance.data.EntryGroup p0) throws java.rmi.RemoteException;
 public void groupEntries(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1)throws java.lang.Exception, java.rmi.RemoteException;
 public void groupEntriesWithSQL(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1)throws java.lang.Exception, java.rmi.RemoteException;
 public boolean rollBackAssessment(Integer p0) throws java.rmi.RemoteException;
/**
 * @param roundId
 */
public void publishAssessment(Integer roundId);
}
