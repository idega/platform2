package se.idega.idegaweb.commune.accounting.childcare.check.data;


public interface CheckHome extends com.idega.data.IDOHome
{
 public Check create() throws javax.ejb.CreateException;
 public Check findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByStatus(com.idega.block.process.data.CaseStatus p0)throws javax.ejb.FinderException;
 public java.util.Collection findApprovedChecksByUser(com.idega.user.data.User p0)throws javax.ejb.FinderException;
 public java.util.Collection findByStatusAndChild(java.lang.String p0,java.lang.Integer p1)throws javax.ejb.FinderException;
 public java.util.Collection findByStatusAndUserAndChild(java.lang.String p0,com.idega.user.data.User p1,java.lang.Integer p2)throws javax.ejb.FinderException;
 public java.util.Collection findByUserAndChild(com.idega.user.data.User p0,java.lang.Integer p1)throws javax.ejb.FinderException;
 public Check findCheckForChild(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findChecks()throws javax.ejb.FinderException;
 public java.util.Collection findChecksByUser(com.idega.user.data.User p0)throws javax.ejb.FinderException;
 public java.util.Collection findChecksByUserAndStatus(com.idega.user.data.User p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findNonApprovedChecks()throws javax.ejb.FinderException;

}