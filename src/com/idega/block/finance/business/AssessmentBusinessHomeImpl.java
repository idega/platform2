package com.idega.block.finance.business;


public class AssessmentBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements AssessmentBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return AssessmentBusiness.class;
 }


 public AssessmentBusiness create() throws javax.ejb.CreateException{
  return (AssessmentBusiness) super.createIBO();
 }



}