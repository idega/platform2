/*
 * $Id: ZipCode.java,v 1.1 2001/06/06 11:29:36 palli Exp $
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
public class ZipCode extends GenericEntity{

	public ZipCode(){
		super();
	}

	public ZipCode(int id)throws SQLException{
		super(id);
	}

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("code", "Póstnúmer", true, true, "java.lang.String");
		addAttribute("city", "Borg", true, true, "java.lang.String");
		addAttribute("country_id", "Land", true, true, "java.lang.Integer","one-to-many","com.idega.projects.golf.entity.Country");
	}

	public String getEntityName(){
		return "zipcode";
	}

	public String getName(){
		return getCity();
	}

	public void setCode(String code){
		setColumn("code", code);
	}

	public String getCode(){
		return getStringColumnValue("code");
	}

	public void setCity(String city){
		setColumn("city", city);
	}

	public String getCity(){
		return getStringColumnValue("city");
	}

	public void setCountry(Country country){
		setColumn("country_id",country);
	}

	public Country getCountry(){
		return (Country)getColumnValue("country_id");
	}

	public void setCountryID(int country_id){
		setColumn("country_id",country_id);
	}

	public int getCountryID(){
		return getIntColumnValue("country_id");
	}

}
