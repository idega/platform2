package is.idega.idegaweb.campus.data;

import javax.ejb.*;

public interface ContractAccounts extends com.idega.data.IDOLegacyEntity
{
 public void delete()throws java.sql.SQLException;
 public int getApartmentCategoryId();
 public int getApartmentId();
 public int getApartmentTypeId();
 public int getBuildingId();
 public int getComplexId();
 public java.lang.String getContracStatus();
 public int getContractId();
 public java.sql.Timestamp getDeliverTime();
 public int getFinanceAccountId();
 public float getFinanceBalance();
 public int getFloorId();
 public boolean getIsRented();
 public int getPhoneAccountId();
 public float getPhoneBalance();
 public java.sql.Timestamp getReturnTime();
 public java.lang.String getStatus();
 public int getUserId();
 public java.sql.Date getValidFrom();
 public java.sql.Date getValidTo();
}
