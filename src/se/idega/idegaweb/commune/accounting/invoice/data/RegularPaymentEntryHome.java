package se.idega.idegaweb.commune.accounting.invoice.data;


public interface RegularPaymentEntryHome extends com.idega.data.IDOHome
{
 public RegularPaymentEntry create() throws javax.ejb.CreateException;
 public RegularPaymentEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByPeriodeAndProvider(java.sql.Date p0,java.sql.Date p1,com.idega.block.school.data.School p2)throws javax.ejb.FinderException;
 public java.util.Collection findByPeriodeAndUser(java.sql.Date p0,java.sql.Date p1,int p2)throws javax.ejb.FinderException;
 public java.util.Collection findOngoingByUserAndProviderAndDate(com.idega.user.data.User p0,com.idega.block.school.data.School p1,java.sql.Date p2)throws javax.ejb.FinderException;
 public java.util.Collection findRegularPaymentForPeriodeCategory(java.sql.Date p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findRegularPaymentForPeriodeCategoryExcludingRegSpecType(java.sql.Date p0,java.lang.String p1,int p2)throws javax.ejb.FinderException;

}