package com.idega.block.survey.data;


public class SurveyParticipantHomeImpl extends com.idega.data.IDOFactory implements SurveyParticipantHome
{
 protected Class getEntityInterfaceClass(){
  return SurveyParticipant.class;
 }


 public SurveyParticipant create() throws javax.ejb.CreateException{
  return (SurveyParticipant) super.createIDO();
 }


public java.util.Collection findRandomParticipants(com.idega.block.survey.data.SurveyEntity p0,int p1,boolean p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SurveyParticipantBMPBean)entity).ejbFindRandomParticipants(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SurveyParticipant findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SurveyParticipant) super.findByPrimaryKeyIDO(pk);
 }


public int getNumberOfParticipations(com.idega.block.survey.data.SurveyEntity p0,java.lang.String p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((SurveyParticipantBMPBean)entity).ejbHomeGetNumberOfParticipations(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}