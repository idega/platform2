package com.idega.block.questions.business;

import javax.ejb.*;

public interface QuestionsService extends com.idega.business.IBOService
{
 public com.idega.block.questions.data.QuestionHome getQuestionHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void invalidateQueston(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.questions.data.Question storeQuestion(int p0,int p1,int p2,int p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
