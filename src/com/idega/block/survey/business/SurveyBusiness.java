package com.idega.block.survey.business;


public interface SurveyBusiness extends com.idega.business.IBOService
{
 public com.idega.block.survey.data.SurveyEntity createSurvey(com.idega.core.category.data.InformationFolder p0,java.lang.String p1,java.lang.String p2,com.idega.util.IWTimestamp p3,com.idega.util.IWTimestamp p4)throws com.idega.data.IDOLookupException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyAnswer createSurveyAnswer(com.idega.block.survey.data.SurveyQuestion p0,java.lang.String[] p1,com.idega.core.localisation.data.ICLocale[] p2)throws com.idega.data.IDOLookupException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyAnswer createSurveyAnswer(com.idega.block.survey.data.SurveyQuestion p0,java.lang.String p1,com.idega.core.localisation.data.ICLocale p2)throws com.idega.data.IDOLookupException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyQuestion createSurveyQuestion(com.idega.block.survey.data.SurveyEntity p0,java.lang.String[] p1,com.idega.core.localisation.data.ICLocale[] p2,char p3)throws com.idega.data.IDOLookupException,javax.ejb.CreateException,com.idega.data.IDOAddRelationshipException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyQuestion createSurveyQuestion(com.idega.block.survey.data.SurveyEntity p0,java.lang.String p1,com.idega.core.localisation.data.ICLocale p2,char p3)throws com.idega.data.IDOLookupException,com.idega.data.IDOAddRelationshipException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyReply createSurveyReply(com.idega.block.survey.data.SurveyEntity p0,com.idega.block.survey.data.SurveyQuestion p1,java.lang.String p2,com.idega.block.survey.data.SurveyAnswer p3,java.lang.String p4)throws com.idega.data.IDOLookupException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyAnswerHome getAnswerHome()throws com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyQuestionHome getQuestionHome()throws com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyEntityHome getSurveyHome()throws com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyParticipantHome getSurveyParticipantHome()throws com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyReplyHome getSurveyReplyHome()throws com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyStatus getSurveyStatus(com.idega.block.survey.data.SurveyEntity p0) throws java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyStatusHome getSurveyStatusHome()throws com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public void removeAnswer(com.idega.block.survey.data.SurveyAnswer p0,com.idega.user.data.User p1)throws com.idega.data.IDORemoveRelationshipException, java.rmi.RemoteException;
 public void removeQuestionFromSurvey(com.idega.block.survey.data.SurveyEntity p0,com.idega.block.survey.data.SurveyQuestion p1,com.idega.user.data.User p2)throws com.idega.data.IDORemoveRelationshipException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyParticipant reportParticipation(com.idega.block.survey.data.SurveyEntity p0,java.lang.String p1)throws com.idega.data.IDOLookupException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyAnswer updateSurveyAnswer(com.idega.block.survey.data.SurveyAnswer p0,java.lang.String p1,com.idega.core.localisation.data.ICLocale p2)throws com.idega.data.IDOLookupException,javax.ejb.CreateException, java.rmi.RemoteException;
 public com.idega.block.survey.data.SurveyQuestion updateSurveyQuestion(com.idega.block.survey.data.SurveyEntity p0,com.idega.block.survey.data.SurveyQuestion p1,java.lang.String p2,com.idega.core.localisation.data.ICLocale p3,char p4)throws com.idega.data.IDOLookupException,javax.ejb.CreateException, java.rmi.RemoteException;
}
