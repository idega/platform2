package com.idega.block.questions.business;

import java.rmi.RemoteException;

public interface QuestionsService extends com.idega.business.IBOService
{
 public com.idega.block.questions.data.QuestionHome getQuestionHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void invalidateQuestion(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.questions.data.Question storeQuestion(int p0,int p1,int p2,int p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void swapSequences(int p0,int p1) throws java.rmi.RemoteException;
 public void validateQuestion(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void resetQuestionSequence(int questionId) throws RemoteException;
 public void removeQuestion(int questionId)throws RemoteException;
}
