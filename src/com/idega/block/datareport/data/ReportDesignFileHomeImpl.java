package com.idega.block.datareport.data;


public class ReportDesignFileHomeImpl extends com.idega.data.IDOFactory implements ReportDesignFileHome
{
 protected Class getEntityInterfaceClass(){
  return ReportDesignFile.class;
 }


 public ReportDesignFile create() throws javax.ejb.CreateException{
  return (ReportDesignFile) super.createIDO();
 }


 public ReportDesignFile findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ReportDesignFile) super.findByPrimaryKeyIDO(pk);
 }



}