package com.idega.block.datareport.data;


public class ReportTranscriptionHomeImpl extends com.idega.data.IDOFactory implements ReportTranscriptionHome
{
 protected Class getEntityInterfaceClass(){
  return ReportTranscription.class;
 }


 public ReportTranscription create() throws javax.ejb.CreateException{
  return (ReportTranscription) super.createIDO();
 }


 public ReportTranscription findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ReportTranscription) super.findByPrimaryKeyIDO(pk);
 }


public com.idega.block.datareport.data.ReportTranscription getReportTranscriptionForObjectInstance(com.idega.core.component.data.ICObjectInstance p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	com.idega.block.datareport.data.ReportTranscription theReturn = ((ReportTranscriptionBMPBean)entity).ejbHomeGetReportTranscriptionForObjectInstance(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}