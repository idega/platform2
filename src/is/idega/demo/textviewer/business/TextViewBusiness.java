package is.idega.demo.textviewer.business;

import javax.ejb.*;

public interface TextViewBusiness extends com.idega.business.IBOService
{
 public java.lang.String getFileAsString(int p0)throws java.io.IOException,java.io.FileNotFoundException, java.rmi.RemoteException;
}
