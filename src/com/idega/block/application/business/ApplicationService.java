package com.idega.block.application.business;

import java.rmi.RemoteException;

import com.idega.block.application.data.ApplicationSubject;


public interface ApplicationService extends com.idega.business.IBOService
{
 public com.idega.block.application.data.ApplicantHome getApplicantHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.application.data.ApplicationHome getApplicationHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.application.data.ApplicationSubjectHome getSubjectHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.application.data.ApplicationSubjectInfoHome getSubjectInfoHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean removeApplicationSubject(Integer id) ;
 public ApplicationSubject storeApplicationSubject(Integer ID, String sName, java.sql.Date expires, String extra)
	throws RemoteException ;
}
