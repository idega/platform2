package is.idega.idegaweb.golf.legacy.business;


public interface GolfLegacyBusiness extends com.idega.business.IBOService
{
 public void copyAllFromUnionToGroup(boolean overwrite) throws java.rmi.RemoteException;
 public void copyFieldImagesForFieldOverview(com.idega.presentation.IWContext p0) throws java.rmi.RemoteException;
 public void copyHoleTextForFieldOverview(com.idega.presentation.IWContext p0) throws java.rmi.RemoteException;
 public void copyTeeImagesForFieldOverview(com.idega.presentation.IWContext p0) throws java.rmi.RemoteException;
 public is.idega.idegaweb.golf.block.login.data.LoginTable getGolfLogin(com.idega.core.accesscontrol.data.LoginTable p0)throws javax.ejb.EJBException,com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getLogin(is.idega.idegaweb.golf.block.login.data.LoginTable p0)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
}
