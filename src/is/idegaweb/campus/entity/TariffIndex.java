/*
 * $Id: TariffIndex.java,v 1.2 2001/06/22 11:33:55 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.entity;

import java.sql.*;
import com.idega.data.GenericEntity;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class TariffIndex extends GenericEntity{

  public TariffIndex(){
          super();
  }

  public TariffIndex(int id)throws SQLException{
          super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getIndexColumnName(), "Vísitala", true, true, "java.lang.Float");
    addAttribute(getDateColumnName(), "Dags", true, true, "java.sql.Timestamp");
    addAttribute(getInfoColumnName(), "Upplýsingar", true, true, "java.lang.String");
  }

  public static String getTariffIndexEntityName(){ return "CAM_TARIFF_INDEX"; }
  public static String getIndexColumnName(){ return "RENT_INDEX";}
  public static String getInfoColumnName(){return "INFO";}
  public static String getDateColumnName(){return "FROM_DATE";}

  public String getEntityName(){
    return getTariffIndexEntityName();
  }
  public float getIndex(){
    return getFloatColumnValue(getIndexColumnName());
  }
  public void setIndex(float index){
    setColumn(getIndexColumnName(),index);
  }
  public void setIndex(Float index){
    setColumn(getIndexColumnName(),index);
  }
  public String getInfo(){
    return getStringColumnValue(getInfoColumnName());
  }
  public void setInfo(String info){
    setColumn(getInfoColumnName(), info);
  }
  public Timestamp getDate(){
    return (Timestamp) getColumnValue(getDateColumnName());
  }
  public void setDate(java.sql.Timestamp use_date){
    setColumn(getDateColumnName(),use_date);
  }
}
