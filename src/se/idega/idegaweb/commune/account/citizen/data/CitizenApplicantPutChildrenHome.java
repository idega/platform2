package se.idega.idegaweb.commune.account.citizen.data;

public interface CitizenApplicantPutChildrenHome extends com.idega.data.IDOHome
{
 public CitizenApplicantPutChildren create() throws javax.ejb.CreateException;
 public CitizenApplicantPutChildren findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public CitizenApplicantPutChildren findByApplicationId(int p0)throws javax.ejb.FinderException;
}
