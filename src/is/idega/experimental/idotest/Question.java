package is.idega.experimental.idotest;


public interface Question extends com.idega.data.IDOEntity
{
 public boolean doInsertInCreate() throws java.rmi.RemoteException;
 public void setText(java.lang.String p0) throws java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
 public java.lang.String getText() throws java.rmi.RemoteException;
}
