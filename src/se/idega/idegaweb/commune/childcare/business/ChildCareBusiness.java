package se.idega.idegaweb.commune.childcare.business;

import javax.ejb.*;

public interface ChildCareBusiness extends com.idega.business.IBOService
{
 public boolean insertApplications(com.idega.user.data.User p0,int p1,int[] p2,java.lang.String[] p3,int p4,int p5,boolean p6) throws java.rmi.RemoteException;
}
