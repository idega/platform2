package is.idega.idegaweb.member.business;


public interface MemberFamilyLogic extends com.idega.business.IBOService
{
 public java.lang.String getChildRelationType() throws java.rmi.RemoteException;
 public java.util.Collection getChildrenFor(com.idega.user.data.User p0)throws is.idega.idegaweb.member.business.NoChildrenFound,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getChildrenInCustodyOf(com.idega.user.data.User p0)throws is.idega.idegaweb.member.business.NoChildrenFound,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.User getCohabitantFor(com.idega.user.data.User p0)throws is.idega.idegaweb.member.business.NoCohabitantFound, java.rmi.RemoteException;
 public java.lang.String getCohabitantRelationType() throws java.rmi.RemoteException;
 public java.lang.String getCustodianRelationType() throws java.rmi.RemoteException;
 public java.util.Collection getCustodiansFor(com.idega.user.data.User p0,boolean p1)throws is.idega.idegaweb.member.business.NoCustodianFound,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getCustodiansFor(com.idega.user.data.User p0)throws is.idega.idegaweb.member.business.NoCustodianFound,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getParentRelationType() throws java.rmi.RemoteException;
 public java.util.Collection getParentsFor(com.idega.user.data.User p0)throws is.idega.idegaweb.member.business.NoParentFound,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getSiblingRelationType() throws java.rmi.RemoteException;
 public java.util.Collection getSiblingsFor(com.idega.user.data.User p0)throws is.idega.idegaweb.member.business.NoSiblingFound,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.User getSpouseFor(com.idega.user.data.User p0)throws is.idega.idegaweb.member.business.NoSpouseFound, java.rmi.RemoteException;
 public java.lang.String getSpouseRelationType() throws java.rmi.RemoteException;
 public com.idega.user.business.UserBusiness getUserBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean hasPersonGotChildren(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public boolean hasPersonGotCohabitant(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public boolean hasPersonGotSiblings(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public boolean hasPersonGotSpouse(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public boolean isChildInCustodyOf(com.idega.user.data.User p0,com.idega.user.data.User p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean isChildOf(com.idega.user.data.User p0,com.idega.user.data.User p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean isCohabitantOf(com.idega.user.data.User p0,com.idega.user.data.User p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean isCustodianOf(com.idega.user.data.User p0,com.idega.user.data.User p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean isParentOf(com.idega.user.data.User p0,com.idega.user.data.User p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean isSiblingOf(com.idega.user.data.User p0,com.idega.user.data.User p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean isSpouseOf(com.idega.user.data.User p0,com.idega.user.data.User p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeAllFamilyRelationsForUser(com.idega.user.data.User p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeAsChildFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeAsCohabitantFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeAsCustodianFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeAsParentFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeAsSiblingFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeAsSpouseFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void setAsChildFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void setAsCohabitantFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void setAsCustodianFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void setAsParentFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void setAsSiblingFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void setAsSpouseFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
}
