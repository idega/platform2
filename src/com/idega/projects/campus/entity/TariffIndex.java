package com.idega.projects.campus.entity;

//import java.util.*;
import java.sql.*;
import com.idega.data.GenericEntity;



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
}
