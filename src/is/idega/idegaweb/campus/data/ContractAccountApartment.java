package is.idega.idegaweb.campus.data;

import javax.ejb.*;

public interface ContractAccountApartment extends com.idega.data.IDOLegacyEntity
{
 public void delete()throws java.sql.SQLException;
 public int getAccountId();
 public java.lang.String getAccountName();
 public int getAccountType();
 public int getApartmentCategoryId();
 public int getApartmentId();
 public int getApartmentTypeId();
 public int getBalance();
 public int getBuildingId();
 public int getComplexId();
 public int getContractId();
 public java.sql.Timestamp getDeliverTime();
 public int getFloorId();
 public boolean getIsRented();
 public java.sql.Timestamp getReturnTime();
 public java.lang.String getStatus();
 public int getUserId();
 public java.sql.Date getValidFrom();
 public java.sql.Date getValidTo();
}
