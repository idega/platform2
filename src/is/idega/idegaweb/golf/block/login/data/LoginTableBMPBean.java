// idega 2000 - Gimmi
/*
 * Copyright 2000 idega.is All Rights Reserved.
 */

package is.idega.idegaweb.golf.block.login.data;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import is.idega.idegaweb.golf.entity.Member;

import com.idega.data.GenericEntity;

public class LoginTableBMPBean extends GenericEntity implements LoginTable{

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		//addAttribute("member_id","Meðlimur",true,true,"java.lang.Integer","one-to-one","com.idega.projects.lv.entity.Member");
		//Breytt 13.03.2001 TL:
		addAttribute(getMemberIdColumnName(), "Notandi", true, true, "java.lang.Integer", "one-to-one", "is.idega.idegaweb.golf.entity.Member");
		//
		addAttribute("user_login", "Notandanafn", true, true, "java.lang.String");
		addAttribute("user_password", "Lykilorð", true, true, "java.lang.String");
	}

	public String getIDColumnName() {
		return "login_table_id";
	}

	public String getEntityName() {
		return "login_table";
	}

	public String getUserPassword() {
		return (String) getColumnValue("user_password");
	}

	public void setUserPassword(String userPassword) {
		setColumn("user_password", userPassword);
	}

	public void setUserLogin(String userLogin) {
		setColumn("user_login", userLogin);
	}

	public String getUserLogin() {
		return (String) getColumnValue("user_login");
	}

	public static String getMemberIdColumnName() {
		return "member_id";
	}

	public int getMemberId() {
		return getIntColumnValue(getMemberIdColumnName());
	}

	public void setMemberId(Integer memberId) {
		setColumn(getMemberIdColumnName(), memberId);
	}

	public void setMemberId(int memberId) {
		setMemberId((new Integer(memberId)));
	}
	
	public Member getMember() {
		return (Member)getColumnValue(getMemberIdColumnName());
	}
	
	public Object ejbFindByMember(Member member) throws EJBException, FinderException {
		return idoFindOnePKByQuery(idoQueryGetSelect().appendWhereEquals(getMemberIdColumnName(),member.getPrimaryKey()));
	}

}