package com.idega.block.survey.business;

import javax.ejb.CreateException;

import com.idega.block.survey.data.SurveyAnswer;
import com.idega.block.survey.data.SurveyEntity;
import com.idega.block.survey.data.SurveyQuestion;
import com.idega.core.localisation.data.ICLocale;
import com.idega.data.IDOLookupException;


public interface SurveyBusiness extends com.idega.business.IBOService
{
 public com.idega.block.survey.data.SurveyEntity createSurvey(com.idega.core.category.data.InformationFolder p0,java.lang.String p1,java.lang.String p2,com.idega.util.IWTimestamp p3,com.idega.util.IWTimestamp p4)throws com.idega.data.IDOLookupException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyAnswer createSurveyAnswer(com.idega.block.survey.data.SurveyQuestion p0,java.lang.String[] p1,com.idega.core.localisation.data.ICLocale[] p2)throws com.idega.data.IDOLookupException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyAnswer createSurveyAnswer(com.idega.block.survey.data.SurveyQuestion p0,java.lang.String p1,com.idega.core.localisation.data.ICLocale p2)throws com.idega.data.IDOLookupException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyQuestion createSurveyQuestion(com.idega.block.survey.data.SurveyEntity p0,java.lang.String p1,com.idega.core.localisation.data.ICLocale p2,char p3)throws com.idega.data.IDOLookupException,com.idega.data.IDOAddRelationshipException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyQuestion createSurveyQuestion(com.idega.block.survey.data.SurveyEntity p0,java.lang.String[] p1,com.idega.core.localisation.data.ICLocale[] p2,char p3)throws com.idega.data.IDOLookupException,javax.ejb.CreateException,com.idega.data.IDOAddRelationshipException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyReply createSurveyReply(com.idega.block.survey.data.SurveyEntity p0,com.idega.block.survey.data.SurveyQuestion p1,java.lang.String p2,com.idega.block.survey.data.SurveyAnswer p3,java.lang.String p4)throws com.idega.data.IDOLookupException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyAnswerHome getAnswerHome()throws com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyQuestionHome getQuestionHome()throws com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyEntityHome getSurveyHome()throws com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyReplyHome getSurveyReplyHome()throws com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public SurveyQuestion updateSurveyQuestion(SurveyEntity survey, SurveyQuestion question, String string, ICLocale locale, char type) throws IDOLookupException, CreateException ;
 public SurveyAnswer updateSurveyAnswer(SurveyAnswer ans, String string, ICLocale locale) throws IDOLookupException, CreateException ;
}
