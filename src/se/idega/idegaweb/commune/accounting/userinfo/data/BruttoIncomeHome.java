package se.idega.idegaweb.commune.accounting.userinfo.data;


public interface BruttoIncomeHome extends com.idega.data.IDOHome
{
 public BruttoIncome create() throws javax.ejb.CreateException;
 public BruttoIncome findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findByUser(java.lang.Integer p0)throws javax.ejb.FinderException;
 public BruttoIncome findLatestByUser(java.lang.Integer p0)throws javax.ejb.FinderException;

}