//idega 2000 - Ægir

package com.idega.projects.golf.entity;

//import java.util.*;
import java.sql.*;


public class Family extends GolfEntity{

	public Family(){
		super();

	}

	public Family(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("name","Fjölskylda",true,true,"java.lang.String");
                addAttribute("union_id","Félagsnúmer",true,true,"java.lang.Integer");
	}

        public void setDefaultValues(){
          setUnionId(1);
        }

	public String getEntityName(){
		return "family";
	}

	public String getName(){
		return getStringColumnValue("name");
	}

	public void setName(String name){
		setColumn("name", name);
	}

        public void setUnionId(int union_id) {
                setColumn("union_id", new Integer(union_id));
        }

        public int getUnionId() {
            return getIntColumnValue("union_id");
        }

}
