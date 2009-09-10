package com.idega.block.datareport.data;

import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHomeImpl;


public class MethodInvocationXMLFileHomeImpl extends ICFileHomeImpl implements MethodInvocationXMLFileHome
{
 protected Class getEntityInterfaceClass(){
  return MethodInvocationXMLFile.class;
 }


 public ICFile create() throws javax.ejb.CreateException{
  return (MethodInvocationXMLFile) super.createIDO();
 }


 public ICFile findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (MethodInvocationXMLFile) super.findByPrimaryKeyIDO(pk);
 }



}