package is.idega.experimental.idotest;

import javax.ejb.*;

public interface Question extends com.idega.data.IDOEntity
{
 public java.lang.String getText() throws java.rmi.RemoteException;
 public void setText(java.lang.String p0) throws java.rmi.RemoteException;
}
