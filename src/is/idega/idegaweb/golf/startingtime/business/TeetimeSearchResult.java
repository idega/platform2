package is.idega.idegaweb.golf.startingtime.business;


public interface TeetimeSearchResult extends com.idega.business.IBOSession
{
 public void cachResult(java.util.List p0,is.idega.idegaweb.golf.GolfField p1,java.lang.String p2) throws java.rmi.RemoteException;
 public java.util.List current() throws java.rmi.RemoteException;
 public java.lang.String getDate() throws java.rmi.RemoteException;
 public is.idega.idegaweb.golf.GolfField getFieldInfo() throws java.rmi.RemoteException;
 public int getResultSize() throws java.rmi.RemoteException;
 public boolean hasNext() throws java.rmi.RemoteException;
 public boolean hasPrevious() throws java.rmi.RemoteException;
 public boolean isInitialized() throws java.rmi.RemoteException;
 public java.util.List next() throws java.rmi.RemoteException;
 public java.util.List prev() throws java.rmi.RemoteException;
 public void setSublistSize(int p0) throws java.rmi.RemoteException;
 public void throwCach() throws java.rmi.RemoteException;
}
