//idega 2000 - Gimmi

package com.idega.projects.timereg.entity;
//import java.util.*;
import java.sql.*;
import com.idega.data.*;

public class MemberFordi extends GenericEntity{

	public MemberFordi(){
		super();
	}

	public MemberFordi(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		//addAttribute("first_name","Fornafn",true,true,"java.lang.String");
		addAttribute("member_id","Forðanúmer LH", true, true, "java.lang.Integer");
		addAttribute("forda_id","Forðanúmer LH", true, true, "java.lang.String");
                addAttribute("is_closed","er lokadur",true,true,"java.lang.Boolean");
	}

	public String getEntityName(){
		return "member_fordi";
	}


        public void setIsClosed( boolean is_closed) {
                setColumn("is_closed",new Boolean(is_closed));
        }

        public boolean getIsClosed() {
                return getBooleanColumnValue("is_closed");
        }

	public void setFordaId(String forda_id) {
		setColumn("forda_id",forda_id);
	}

	public String getFordaId() {
		return getStringColumnValue("forda_id");
	}

	public void setMemberId(int member_id) {
		setColumn("member_id",new Integer(member_id));
	}

	public int getMemberId() {
		return getIntColumnValue("member_id");
	}


}
