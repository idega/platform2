/*
 * $Id: Country.java,v 1.2 2001/11/08 15:40:39 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.entity;


import java.sql.*;
import com.idega.data.GenericEntity;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class Country extends GenericEntity{

	public Country(){
		super();
	}

	public Country(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("name", "Nafn", true, true, "java.lang.String");
		addAttribute("abbreviation", "Skammstöfun", true, true, "java.lang.String");
		addAttribute("internet_suffix", "internet skammstöfun", true, true, "java.lang.String");
		addAttribute("access_number", "Lykill", true, true, "java.lang.Integer");
	}

	public String getEntityName(){
		return "country";
	}

	public String getName(){
		return getStringColumnValue("name");
	}

	public void setAbbrevation(String abbrevation){
		setColumn("abbreviation", abbrevation);
	}

	public String getAbbrevation(){
		return getStringColumnValue("abbreviation");
	}

	public void setInternetSuffix(String internetSuffix){
		setColumn("internet_suffix", internetSuffix);
	}

	public String getInternetSuffix(){
		return getStringColumnValue("internet_suffix");
	}

	public void setName(String name){
		setColumn("name", name);
	}

	public int getAccessNumber(){
		return getIntColumnValue("access_number");
	}

	public void setAccessNumber(Integer access_number){
		setColumn("access_number", access_number);
	}
}
