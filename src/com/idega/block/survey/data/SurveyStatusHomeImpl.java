package com.idega.block.survey.data;


public class SurveyStatusHomeImpl extends com.idega.data.IDOFactory implements SurveyStatusHome
{
 protected Class getEntityInterfaceClass(){
  return SurveyStatus.class;
 }


 public SurveyStatus create() throws javax.ejb.CreateException{
  return (SurveyStatus) super.createIDO();
 }


public java.util.Collection findAllBySurvey(com.idega.block.survey.data.SurveyEntity p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SurveyStatusBMPBean)entity).ejbFindAllBySurvey(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public SurveyStatus findBySurvey(com.idega.block.survey.data.SurveyEntity p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SurveyStatusBMPBean)entity).ejbFindBySurvey(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public SurveyStatus findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SurveyStatus) super.findByPrimaryKeyIDO(pk);
 }



}