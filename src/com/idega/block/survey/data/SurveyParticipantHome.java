package com.idega.block.survey.data;


public interface SurveyParticipantHome extends com.idega.data.IDOHome
{
 public SurveyParticipant create() throws javax.ejb.CreateException;
 public SurveyParticipant findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public int getNumberOfParticipations(com.idega.block.survey.data.SurveyEntity p0,java.lang.String p1)throws com.idega.data.IDOException;
 public java.util.Collection getRandomParticipants(com.idega.block.survey.data.SurveyEntity p0,int p1,boolean p2)throws javax.ejb.FinderException;

}