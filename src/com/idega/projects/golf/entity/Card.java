//idega 2000 - \uFFFDgir

package com.idega.projects.golf.entity;

//import java.util.*;
import java.sql.*;


public class Card extends GolfEntity{

	public Card(){
		super();
	}

	public Card(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		//addAttribute("number_of_holes","Fjöldi hola",true,true,"java.lang.Integer");
		addAttribute("card_number","Kortanúmer",true,true,"java.lang.String");
		addAttribute("name","Korthafi",true,true,"java.lang.String");
                addAttribute("social_security_number","Kennitala",true,true,"java.lang.String");
		addAttribute("expire_date","Gildir til 00/00",true,true,"java.sql.Date");
		addAttribute("card_company","Kortafyrirtæki",true,true,"java.lang.String");
		addAttribute("card_type","Tegund korts",true,true,"java.lang.String");
	}

	public String getEntityName(){
		return "card";
	}

	public String getCardNumber(){
		return (String)getColumnValue("card_number");
	}

	public void setCardNumber(String cardNumber){
		setColumn("card_number",cardNumber);
	}

	public String getName(){
            return getStringColumnValue("name");
	}

        public void setName(String name){
            setColumn("name", name);
	}

        public String getSocialSecurityNumber(){
		return getStringColumnValue("social_security_number");
	}

	public void setSocialSecurityNumber(String socialSecurityNumber){
		setColumn("social_security_number", socialSecurityNumber);
	}

	public Date getExpireDate(){
		return (Date) getColumnValue("expire_date");
	}

	public void setExpireDate(Date expireDate){
		setColumn("expire_date",expireDate);
	}

	public String getCardCompany(){
		return (String) getColumnValue("card_company");
	}

	public void setCardCompany(String cardCompany){
		setColumn("card_company", cardCompany);
	}

	public String getCardType(){
		return (String) getColumnValue("card_type");
	}

	public void setCardType(String cardType){
		setColumn("card_type", cardType);
	}

}
