
package is.idega.idegaweb.golf.entity;

import com.idega.data.GenericEntity;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class UserIdsBMPBean extends GenericEntity implements UserIds{

	public void initializeAttributes(){
          addAttribute(getIDColumnName());
          addAttribute("union_id", "Klúbbur", true, true, "java.lang.Integer","one-to-one","is.idega.idegaweb.golf.entity.Union");
          addAttribute("start","Upphafs",true,true,"java.lang.String");
          addAttribute("ending","Loka",true,true,"java.lang.String");
          addAttribute("last","Síðast",true,true,"java.lang.String");
          super.setMaxLength("start",16);
          super.setMaxLength("ending",16);
          super.setMaxLength("last",16);
        }
	public String getEntityName(){
          return "userids";
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
        public String getStart(){
          return getStringColumnValue("start");
        }
        public void setStart(String start){
          setColumn("start", start);
        }
        public String getEnding(){
          return getStringColumnValue("ending");
        }
        public void setEnding(String ending){
          setColumn("ending", ending);
        }
        public String getLast(){
          return getStringColumnValue("last");
        }
        public void setLast(String last){
          setColumn("last", last);
        }
}
