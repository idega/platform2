package com.idega.block.questions.business;


public interface QuestionsServiceHome extends com.idega.business.IBOHome
{
 public QuestionsService create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}