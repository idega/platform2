package com.idega.block.dataquery.data.sql;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Oct 8, 2003
 */
public interface Expression {
  
  public String toSQLString(); 
    
  public boolean isValid(); 


}
