package se.idega.idegaweb.commune.childcare.check.data;

import javax.ejb.*;

public interface Check extends com.idega.data.IDOEntity,com.idega.block.process.data.Case
{
 public void setMotherTongueParents(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setManagerId(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getWorkSituation2()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getWorkSituation1()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getMotherToungueParents()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setChildCareType(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getCaseCodeKey() throws java.rmi.RemoteException;
 public java.lang.String[] getCaseStatusDescriptions() throws java.rmi.RemoteException;
 public int getChildId()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getMethod()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setMotherTongueMotherChild(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setMotherTongueFatherChild(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setWorkSituation2(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setWorkSituation1(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setMethod(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getManagerId()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setRule5(boolean p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setRule4(boolean p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getAmount()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setRule3(boolean p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getCheckFee()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setRule2(boolean p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setRule1(boolean p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setNotes(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean getRule5()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
 public boolean getRule4()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean getRule3()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean getRule2()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean getRule1()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getNotes()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getChildCareType()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setAmount(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getMotherToungueMotherChild()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getMotherToungueFatherChild()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setCheckFee(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setChildId(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String[] getCaseStatusKeys() throws java.rmi.RemoteException;
 public java.lang.String getCaseCodeDescription() throws java.rmi.RemoteException;
}
