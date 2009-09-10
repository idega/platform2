package com.idega.block.importer.data;


public interface ImportFileClassHome extends com.idega.data.IDOHome
{
 public ImportFileClass create() throws javax.ejb.CreateException;
 public ImportFileClass findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllImportFileClasses()throws javax.ejb.FinderException;
 public ImportFileClass findByClassName(java.lang.String p0)throws javax.ejb.FinderException;

}