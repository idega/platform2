package com.idega.block.beanshell.business;


public class BSHEngineHomeImpl extends com.idega.business.IBOHomeImpl implements BSHEngineHome
{
 protected Class getBeanInterfaceClass(){
  return BSHEngine.class;
 }


 public BSHEngine create() throws javax.ejb.CreateException{
  return (BSHEngine) super.createIBO();
 }



}