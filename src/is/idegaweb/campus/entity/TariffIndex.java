/*
 * $Id: TariffIndex.java,v 1.1 2001/06/06 11:29:36 palli Exp $
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
          addAttribute("tindex", "Vísitala", true, true, "java.lang.Float");
          addAttribute("insert_date", "Dags", true, true, "java.sql.TimeStamp");
          addAttribute("info", "Upplýsingar", true, true, "java.lang.String");
          addAttribute("useindex","Gild",true,true,"java.lang.Boolean");
	}

	public String getEntityName(){
          return "tariff_index";
	}
        public float getIndex(){
          return getFloatColumnValue("tindex");
        }
        public void setIndex(float index){
          setColumn("tindex",index);
        }
        public void setIndex(Float index){
          setColumn("tindex",index);
        }
        public String getInfo(){
          return getStringColumnValue("info");
        }
        public void setInfo(String info){
          setColumn("info", info);
        }
	public Timestamp getDate(){
          return (Timestamp) getColumnValue("from_date");
        }
        public void setDate(Timestamp use_date){
          setColumn("from_date",use_date);
        }
        public void setUseIndex(boolean useindex){
          setColumn("useindex",useindex);
        }
        public boolean getUseIndex(){
          return getBooleanColumnValue("useindex");
        }

}
