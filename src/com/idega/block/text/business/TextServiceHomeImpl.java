package com.idega.block.text.business;


public class TextServiceHomeImpl extends com.idega.business.IBOHomeImpl implements TextServiceHome
{
 protected Class getBeanInterfaceClass(){
  return TextService.class;
 }


 public TextService create() throws javax.ejb.CreateException{
  return (TextService) super.createIBO();
 }



}