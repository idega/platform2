//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.projects.golf.entity;

//import java.util.*;
import java.sql.*;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class LoginTable extends GolfEntity{

	public LoginTable(){
		super();
	}

	public LoginTable(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("member_id","Meðlimur",true,true,"java.lang.Integer","one-to-one","com.idega.projects.golf.entity.Member");
		addAttribute("user_login","Notandanafn",true,true,"java.lang.String");
		addAttribute("user_password","Lykilorð",true,true,"java.lang.String");
	}

	public String getIDColumnName(){
		return "login_table_id";
	}

	public String getEntityName(){
		return "login_table";
	}

	public String getUserPassword(){
		return (String) getColumnValue("user_password");
	}

	public void setUserPassword(String userPassword){
		setColumn("user_password", userPassword);
	}
	public void setUserLogin(String userLogin) {
		setColumn("user_login", userLogin);
	}
	public String getUserLogin() {
		return (String) getColumnValue("user_login");
	}

	public int getMemberId(){
		return getIntColumnValue("member_id");
	}

	public void setMemberId(Integer memberId){
		setColumn("member_id", memberId);
	}

        public void setMemberId(int memberId) {
                setColumn("member_id", new Integer(memberId));
        }

        public void insertStartData()throws Exception{
          Member member = new Member();
          member.setFirstName("Administrator");
          member.insert();

          Group group = new Group();
          group.setName("administrator");
          group.setGroupType("accesscontrol");
          group.setDescription("Default IdegaWeb Golf Administrator");
          group.insert();

          member.addTo(group);

          LoginTable table = new LoginTable();
          table.setMemberId(member.getID());
          table.setUserLogin("admin");
          table.setUserPassword("golf4u");
          table.insert();
        }

}
