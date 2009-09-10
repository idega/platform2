package com.idega.block.questions.business;

import java.rmi.RemoteException;

import com.idega.block.questions.data.Question;


public interface QuestionsService extends com.idega.business.IBOService
{
 public com.idega.block.questions.data.QuestionHome getQuestionHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.text.business.TextService getTextService()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void invalidateQuestion(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void removeQuestion(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void resetQuestionSequence(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.questions.data.Question storeQuestion(int p0,int p1,int p2,int p3)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.questions.data.Question storeQuestion(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7) throws java.rmi.RemoteException;
 public void swapSequences(int p0,int p1) throws java.rmi.RemoteException;
 public void validateQuestion(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
/**
 * @param categoryIds
 * @return
 */
public Question getRandomQuestion(int[] categoryIds)throws RemoteException;
}
