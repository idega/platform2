package se.idega.idegaweb.commune.accounting.invoice.data;


public interface PaymentHeaderHome extends com.idega.data.IDOHome
{
 public PaymentHeader create() throws javax.ejb.CreateException;
 public PaymentHeader findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolCategoryAndPeriodForPrivate(com.idega.block.school.data.SchoolCategory p0,java.sql.Date p1)throws com.idega.data.IDOLookupException,javax.ejb.EJBException,javax.ejb.FinderException;
 public PaymentHeader findBySchoolCategorySchoolPeriod(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolCategory p1,java.sql.Date p2)throws javax.ejb.FinderException;

}