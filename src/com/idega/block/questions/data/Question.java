package com.idega.block.questions.data;

import javax.ejb.*;

import com.idega.core.business.Category;
import com.idega.data.CategoryEntity;

public interface Question extends com.idega.data.IDOEntity,CategoryEntity
{
 public int getAnswerID() throws java.rmi.RemoteException;
 public int getQuestionID() throws java.rmi.RemoteException;
 public boolean getValid() throws java.rmi.RemoteException;
 public void initializeAttributes();
 public void setAnswerID(int p0) throws java.rmi.RemoteException;
 public void setQuestionID(int p0) throws java.rmi.RemoteException;
 public void setValid(boolean p0) throws java.rmi.RemoteException;
}
