package se.idega.idegaweb.commune.childcare.data;


public interface ChildCarePrognosisHome extends com.idega.data.IDOHome
{
 public ChildCarePrognosis create() throws javax.ejb.CreateException;
 public ChildCarePrognosis findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 /**
 * Returns the prognosis for the given provider.
 * @param providerID
 * @return	The <code>ChildCarePrognosis</code> for the provider.
 * @throws javax.ejb.FinderException
 */
 public ChildCarePrognosis findPrognosis(int providerID)throws javax.ejb.FinderException;

}