//idega 2000 - eiki

package com.idega.projects.golf.entity;

//import java.util.*;
import java.sql.*;

public class PaymentType extends GolfEntity{

	public PaymentType(){
		super();
	}

	public PaymentType(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("name","Greiðslumiðill",true,true,"java.lang.String");
		addAttribute("extra_info","Athugasemd",true,true,"java.lang.String");
                addAttribute("default_installment_nr","Greiðslufjöldi",true,true,"java.lang.Integer");

	}

	public String getEntityName(){
		return "payment_type";
	}

	public String getName(){
		return getStringColumnValue("name");
	}

	public void setName(String name){
		setColumn("name", name);
	}

	public String getExtraInfo(){
		return getStringColumnValue("extra_info");
	}

	public void setExtraInfo(String extra_info){
		setColumn("extra_info", extra_info);
	}

        public int getDefaultInstallNr(){
		return getIntColumnValue("default_installment_nr");
	}

	public void setDefaultInstallNr(Integer default_installment_nr){
		setColumn("default_installment_nr",default_installment_nr);
	}




}
