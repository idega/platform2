//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package is.idega.idegaweb.golf.entity;

import com.idega.data.GenericEntity;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.2
*/
public class TeeColorBMPBean extends GenericEntity implements TeeColor{

	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("tee_color_name","Nafn teigar",true,true,"java.lang.String");
		addAttribute("gender","Kyn",true,true,"java.lang.String");
	}
	

	public void setName(String name){
		setColumn("tee_color_name",name);
	}

	public String getName(){
		return (String) getColumnValue("tee_color_name");
	}
	
	public String getEntityName(){
		return "tee_color";
	}
	
	/*
	*Sets the gender:
	*M=male
	*F=female
	*/
	public void setGender(String gender){
		setColumn("gender",gender);
	}

	/*
	*Returns the gender:
	*M=male
	*F=female
	*/
	public String getGender(){
		return (String) getColumnValue("gender");
	}
	
	
}
