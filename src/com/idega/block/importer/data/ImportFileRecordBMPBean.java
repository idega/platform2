package com.idega.block.importer.data;

import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.core.file.data.ICFileBMPBean;
import com.idega.data.IDOQuery;



/**
 * Title:        com.idega.block.importer.data.ImportHandlerBMPBean
 * Description: A table of available Import handlers
 * Copyright:    Idega Software (c) 2002
 * Company:      Idega Software http://www.idega.com
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class ImportFileRecordBMPBean extends ICFileBMPBean implements ImportFileRecord{


  public ImportFileRecordBMPBean() {
    super();
  }

  public ImportFileRecordBMPBean(int id) throws SQLException {
    super(id);
  }
 
  public Integer ejbFindImportFileRecordFromNameAndSize(String name, Integer sizeInBytes)throws FinderException{
    IDOQuery sql = idoQuery();
    sql.appendSelectAllFrom(this.getEntityName())
    .appendWhere().appendEqualsQuoted(getColumnNameName(),name)
    .appendAndEqualsQuoted(getColumnNameFileSize(),sizeInBytes.toString());
    
    return (Integer) super.idoFindOnePKByQuery(sql);
  }
  
  public void setAsImported(){
  	this.setMetaData("imported","Y");
  }

  public void setAsNotImported(){
  	this.setMetaData("imported","N");
  }
    
  public boolean hasBeenImported(){
  	String imported = getMetaData("imported");
  	if(imported!=null && imported.equals("Y")){
  		 return true;
  	}	
  	return false;
  }
  
  
  
    

}
