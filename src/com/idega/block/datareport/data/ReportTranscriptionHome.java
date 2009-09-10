package com.idega.block.datareport.data;


public interface ReportTranscriptionHome extends com.idega.data.IDOHome
{
 public ReportTranscription create() throws javax.ejb.CreateException;
 public ReportTranscription findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public com.idega.block.datareport.data.ReportTranscription getReportTranscriptionForObjectInstance(com.idega.core.component.data.ICObjectInstance p0)throws javax.ejb.FinderException;

}