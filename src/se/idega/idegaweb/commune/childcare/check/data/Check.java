package se.idega.idegaweb.commune.childcare.check.data;

import javax.ejb.*;

public interface Check extends com.idega.data.IDOEntity,com.idega.block.process.data.Case
{
 public int getCheckFee()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getMethod()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setChildCareType(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setCheckFee(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getAmount()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getWorkSituation2()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getWorkSituation1()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setMotherTongueFatherChild(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setWorkSituation2(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setMotherTongueMotherChild(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setChildId(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getMotherToungueParents()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setMotherTongueParents(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setWorkSituation1(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setMethod(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getChildCareType()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getMotherToungueFatherChild()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getMotherToungueMotherChild()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getChildId()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setAmount(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
