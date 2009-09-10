package com.idega.block.survey.business;


public class SurveyBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements SurveyBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return SurveyBusiness.class;
 }


 public SurveyBusiness create() throws javax.ejb.CreateException{
  return (SurveyBusiness) super.createIBO();
 }



}