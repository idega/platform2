package is.idega.idegaweb.member.business.plugins;


public interface AgeGenderPluginBusiness extends com.idega.business.IBOService,com.idega.user.business.UserGroupPlugInBusiness
{
 public void afterGroupCreateOrUpdate(com.idega.user.data.Group p0)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void afterUserCreateOrUpdate(com.idega.user.data.User p0)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void beforeGroupRemove(com.idega.user.data.Group p0)throws javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void beforeUserRemove(com.idega.user.data.User p0)throws javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findGroupsByFields(java.util.Collection p0,java.util.Collection p1,java.util.Collection p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getGroupPropertiesTabs(com.idega.user.data.Group p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getKeyDateForAge(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
 public java.util.Collection getListViewerFields()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getLowerAgeLimit(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
 public int getLowerAgeLimitDefault() throws java.rmi.RemoteException;
 public java.lang.Class getPresentationObjectClass()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getUpperAgeLimit(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
 public int getUpperAgeLimitDefault() throws java.rmi.RemoteException;
 public java.util.List getUserPropertiesTabs(com.idega.user.data.User p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.presentation.PresentationObject instanciateEditor(com.idega.user.data.Group p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.presentation.PresentationObject instanciateViewer(com.idega.user.data.Group p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean isAgeLimitStringentCondition(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
 public boolean isFemale(com.idega.user.data.Group p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public boolean isMale(com.idega.user.data.Group p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public boolean isNeutral(com.idega.user.data.Group p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public void setAgeLimitIsStringentCondition(com.idega.user.data.Group p0,boolean p1) throws java.rmi.RemoteException;
 public void setFemale(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
 public void setKeyDateForAge(com.idega.user.data.Group p0,java.lang.String p1) throws java.rmi.RemoteException;
 public void setLowerAgeLimit(com.idega.user.data.Group p0,int p1) throws java.rmi.RemoteException;
 public void setMale(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
 public void setNeutral(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
 public void setUpperAgeLimit(com.idega.user.data.Group p0,int p1) throws java.rmi.RemoteException;
 public java.util.GregorianCalendar getKeyDateForYearZero(com.idega.user.data.Group p0); 
}
