//idega 2000 - Gimmi

package com.idega.jmodule.days.data;


//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class Days extends GenericEntity{

	public Days(){
		super();
	}

	public Days(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
                addAttribute("days_name","Nafn dags",true,true,"java.lang.String");
                addAttribute("work_hours","Fjöldi vinnutima",true,true,"java.lang.Float");
        }

	public String getIDColumnName(){
		return "days_id";
	}

	public String getEntityName(){
		return "days";
	}

        public void setName(String name) {
          setColumn("days_name",name);
        }

        public String getName() {
          return getStringColumnValue("days_name");
        }

        public void setWorkHours(float hours) {
          setColumn("work_hours",hours);
        }

        public void setWorkHours(double hours) {
          setWorkHours(Float.parseFloat(Double.toString(hours)));
        }

        public float getWorkHours() {
          return getFloatColumnValue("work_hours");
        }

}
