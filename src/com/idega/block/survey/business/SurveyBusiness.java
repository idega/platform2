package com.idega.block.survey.business;


public interface SurveyBusiness extends com.idega.business.IBOService
{
 public com.idega.block.survey.data.SurveyEntity createSurvey(java.lang.String p0,java.lang.String p1,com.idega.util.IWTimestamp p2,com.idega.util.IWTimestamp p3)throws com.idega.data.IDOLookupException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyAnswer createSurveyAnswer(com.idega.block.survey.data.SurveyQuestion p0,java.lang.String p1,com.idega.core.localisation.data.ICLocale p2)throws com.idega.data.IDOLookupException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyAnswer createSurveyAnswer(com.idega.block.survey.data.SurveyQuestion p0,java.lang.String[] p1,com.idega.core.localisation.data.ICLocale[] p2)throws com.idega.data.IDOLookupException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyQuestion createSurveyQuestion(com.idega.block.survey.data.SurveyEntity p0,java.lang.String p1,com.idega.core.localisation.data.ICLocale p2,char p3)throws com.idega.data.IDOLookupException,com.idega.data.IDOAddRelationshipException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyQuestion createSurveyQuestion(com.idega.block.survey.data.SurveyEntity p0,java.lang.String[] p1,com.idega.core.localisation.data.ICLocale[] p2,char p3)throws com.idega.data.IDOLookupException,javax.ejb.CreateException,com.idega.data.IDOAddRelationshipException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyAnswerHome getAnswerHome()throws com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyQuestionHome getQuestionHome()throws com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyEntityHome getSurveyHome()throws com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyReplyHome getSurveyReplyHome()throws com.idega.data.IDOLookupException, java.rmi.RemoteException;
}
