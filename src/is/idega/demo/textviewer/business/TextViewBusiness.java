package is.idega.demo.textviewer.business;


public interface TextViewBusiness extends com.idega.business.IBOService
{
 public java.lang.String getFileAsString(int p0)throws java.io.FileNotFoundException,java.io.IOException, java.rmi.RemoteException;
}
