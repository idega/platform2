package is.idega.idegaweb.member.isi.block.accounting.data;


public interface FinanceEntryHome extends com.idega.data.IDOHome
{
 public FinanceEntry create() throws javax.ejb.CreateException;
 public FinanceEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllAssessmentByUser(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.User p2)throws javax.ejb.FinderException;
 public java.util.Collection findAllAssessmentByUser(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.User p2,com.idega.util.IWTimestamp p3)throws javax.ejb.FinderException;
 public java.util.Collection findAllByAssessmentRound(is.idega.idegaweb.member.isi.block.accounting.data.AssessmentRound p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllByUser(com.idega.user.data.User p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllByUser(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.User p2)throws javax.ejb.FinderException;
 public java.util.Collection findAllFinanceEntriesByDateIntervalDivisionsAndGroupsOrderedByDivisionGroupAndDate(com.idega.user.data.Group p0,java.lang.String[] p1,java.sql.Date p2,java.sql.Date p3,java.util.Collection p4,java.util.Collection p5,java.lang.String p6)throws javax.ejb.FinderException;
 public java.util.Collection findAllFinanceEntriesByPaymentDateDivisionsAndGroupsOrderedByDivisionGroupAndDate(com.idega.user.data.Group p0,java.lang.String[] p1,java.util.Collection p2,java.util.Collection p3,java.lang.String p4)throws javax.ejb.FinderException;
 public java.util.Collection findAllOpenAssessmentByUser(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.User p2)throws javax.ejb.FinderException;
 public java.util.Collection findAllPaymentsByUser(com.idega.user.data.Group p0,com.idega.user.data.Group p1,com.idega.user.data.User p2)throws javax.ejb.FinderException;

}