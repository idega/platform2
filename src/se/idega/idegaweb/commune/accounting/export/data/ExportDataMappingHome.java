package se.idega.idegaweb.commune.accounting.export.data;


public interface ExportDataMappingHome extends com.idega.data.IDOHome
{
 public ExportDataMapping create() throws javax.ejb.CreateException;
 public ExportDataMapping findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;

}