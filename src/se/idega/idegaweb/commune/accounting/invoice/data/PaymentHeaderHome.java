package se.idega.idegaweb.commune.accounting.invoice.data;


public interface PaymentHeaderHome extends com.idega.data.IDOHome
{
 public PaymentHeader create() throws javax.ejb.CreateException;
 public PaymentHeader findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolAndSchoolCategoryPKAndStatus(java.lang.Object p0,java.lang.Object p1,java.lang.String p2)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolCategoryAndPeriod(java.lang.String p0,java.sql.Date p1)throws com.idega.data.IDOLookupException,javax.ejb.EJBException,javax.ejb.FinderException;
 public java.util.Collection findBySchoolCategoryAndPeriodForPrivate(com.idega.block.school.data.SchoolCategory p0,java.sql.Date p1)throws com.idega.data.IDOLookupException,javax.ejb.EJBException,javax.ejb.FinderException;
 public java.util.Collection findBySchoolCategoryAndSchoolAndPeriod(java.lang.String p0,java.lang.Integer p1,java.sql.Date p2,java.sql.Date p3)throws javax.ejb.FinderException;
 public PaymentHeader findBySchoolCategorySchoolPeriod(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolCategory p1,java.sql.Date p2)throws javax.ejb.FinderException;
 public PaymentHeader findBySchoolCategoryAndSchoolAndPeriodAndStatus(com.idega.block.school.data.School p0,com.idega.block.school.data.SchoolCategory p1,com.idega.util.TimePeriod p2, String status)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolCategoryStatusInCommuneWithCommunalManagement(java.lang.String p0,char p1)throws javax.ejb.FinderException;
 public java.util.Collection findBySchoolCategoryStatusOutsideCommuneOrWithoutCommunalManagement(java.lang.String p0,char p1)throws javax.ejb.FinderException;
 public java.util.Collection findByStatusAndSchoolId(char p0,int p1)throws com.idega.data.IDOLookupException,javax.ejb.EJBException,javax.ejb.FinderException;
 public int getPlacementCountForSchoolAndPeriod(int p0,java.sql.Date p1)throws javax.ejb.FinderException,com.idega.data.IDOException;
 public int getProviderCountForSchoolCategoryAndPeriod(java.lang.String p0,java.sql.Date p1)throws javax.ejb.FinderException,com.idega.data.IDOException;

}
