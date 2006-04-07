/*
 * Created on Apr 7, 2006
 */
package is.idega.idegaweb.member.isi.block.groups.business;


public interface GroupInfoBusiness extends com.idega.business.IBOService
{
 public com.idega.user.data.Group getGroup(com.idega.presentation.IWContext p0,java.lang.String p1) throws java.rmi.RemoteException;
 public com.idega.core.location.data.Address getGroupAddress(com.idega.presentation.IWContext p0,com.idega.user.data.Group p1) throws java.rmi.RemoteException;
 public java.util.List getGroups(com.idega.presentation.IWContext p0,java.lang.String[] p1,java.lang.String[] p2,java.util.Comparator p3) throws java.rmi.RemoteException;
}