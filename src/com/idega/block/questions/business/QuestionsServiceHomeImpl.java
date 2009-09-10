package com.idega.block.questions.business;


public class QuestionsServiceHomeImpl extends com.idega.business.IBOHomeImpl implements QuestionsServiceHome
{
 protected Class getBeanInterfaceClass(){
  return QuestionsService.class;
 }


 public QuestionsService create() throws javax.ejb.CreateException{
  return (QuestionsService) super.createIBO();
 }



}