package com.idega.block.application.business;


public interface ApplicationService extends com.idega.business.IBOService
{
 public com.idega.block.application.data.ApplicantHome getApplicantHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.application.data.ApplicationHome getApplicationHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.application.data.ApplicationSubjectHome getSubjectHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.application.data.ApplicationSubjectInfoHome getSubjectInfoHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
}
