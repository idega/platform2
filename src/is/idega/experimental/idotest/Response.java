package is.idega.experimental.idotest;


public interface Response extends com.idega.data.IDOEntity
{
 public void initializeAttributes() throws java.rmi.RemoteException;
 public boolean doInsertInCreate() throws java.rmi.RemoteException;
 public void setResponse(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getResponse() throws java.rmi.RemoteException;
}
