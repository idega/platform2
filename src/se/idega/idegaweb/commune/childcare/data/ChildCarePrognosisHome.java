package se.idega.idegaweb.commune.childcare.data;


public interface ChildCarePrognosisHome extends com.idega.data.IDOHome
{
 public ChildCarePrognosis create() throws javax.ejb.CreateException;
 public ChildCarePrognosis findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ChildCarePrognosis findPrognosis(int p0)throws javax.ejb.FinderException;

}