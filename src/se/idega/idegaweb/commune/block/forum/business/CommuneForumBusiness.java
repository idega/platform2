package se.idega.idegaweb.commune.block.forum.business;


public interface CommuneForumBusiness extends com.idega.business.IBOService
{
 public java.util.Collection convertFilePKsToFileCollection(java.util.Collection p0)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
 public int getFileCount(com.idega.block.category.data.ICCategory p0) throws java.rmi.RemoteException;
 public com.idega.block.category.data.ICCategoryHome getICCategoryHome()throws com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public com.idega.core.file.data.ICFileHome getICFileHome()throws com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public com.idega.user.data.User getModerator(com.idega.block.category.data.ICCategory p0) throws java.rmi.RemoteException;
 public boolean isModerator(com.idega.block.category.data.ICCategory p0,com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public boolean isModerator(int topicID, com.idega.user.data.User currentUser) throws java.rmi.RemoteException;
}
