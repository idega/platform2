package se.idega.idegaweb.commune.business;

import is.idega.block.family.business.FamilyLogic;


public interface CommuneFamilyService extends FamilyLogic
{
 public void setAsCohabitantFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void setAsSpouseFor(com.idega.user.data.User p0,com.idega.user.data.User p1)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
}
