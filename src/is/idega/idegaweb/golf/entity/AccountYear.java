
package is.idega.idegaweb.golf.entity;

import java.sql.*;
import com.idega.data.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class AccountYear extends GolfEntity{

	public AccountYear(){
		super();
	}

	public AccountYear(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
                addAttribute("name","Heiti",true,true,"java.lang.String");
                addAttribute("main_year", "Ár", true, true, "java.lang.Integer");
                addAttribute("union_id", "Klúbbnúmer", true, true, "java.lang.Integer");
                addAttribute("from_date","Síðast Breytt",true,true,"java.sql.Timestamp");
                addAttribute("to_date","Gerandi",true,true,"java.sql.Timestamp");
                addAttribute("info","Athugasemd",true,true,"java.lang.String");
		addAttribute("creation_date","Greiðsludagur",true,true,"java.sql.Timestamp");
                addAttribute("active_year","í Gildi",true,true,"java.lang.Boolean");

	}

	public String getEntityName(){
		return "account_year";
	}

        public String getName(){
          return getStringColumnValue("name");
        }

        public void setName(String name){
          setColumn("name", name);
        }

        public int getUnionId(){
		return getIntColumnValue("union_id");
	}

	public void setUnionId(Integer union_id){
          setColumn("union_id", union_id);
	}

        public void setUnionId(int union_id){
          setColumn("union_id", union_id);
	}

         public int getMainYear(){
		return getIntColumnValue("main_year");
	}

	public void setMainYear(Integer year){
          setColumn("main_year", year);
	}

        public void setMainYear(int year){
          setColumn("main_year", year);
	}


        public Timestamp getFrom(){
          return (Timestamp) getColumnValue("from_date");
	}

	public void setFrom(Timestamp from){
          setColumn("from_date", from);
	}

         public Timestamp getTo(){
          return (Timestamp) getColumnValue("to_date");
	}

	public void setTo(Timestamp to){
          setColumn("to_date", to);
	}

	public Timestamp getCreationDate(){
          return (Timestamp) getColumnValue("creation_date");
	}

	public void setCreationDate(Timestamp creation_date){
          setColumn("creation_date", creation_date);
	}

	public String getInfo(){
          return getStringColumnValue("info");
	}

	public void setInfo(String extra_info){
          setColumn("info", extra_info);
	}

        public void setActive(boolean active){
          setColumn("active_year",active);
        }
        public boolean getActive(){
          return getBooleanColumnValue("active_year");
        }

}
