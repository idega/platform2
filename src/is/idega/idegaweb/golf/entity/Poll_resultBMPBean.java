//idega 2000 - Eiki



package is.idega.idegaweb.golf.entity;



//import java.util.*;

import java.sql.*;



public class Poll_resultBMPBean extends is.idega.idegaweb.golf.entity.GolfEntityBMPBean implements is.idega.idegaweb.golf.entity.Poll_result {



	public Poll_resultBMPBean(){

		super();	

	}

	

	public Poll_resultBMPBean(int id)throws SQLException{

		super(id);

	}



	public void initializeAttributes(){

		addAttribute(getIDColumnName());

		addAttribute("poll_option_id", "Svar", true, true, "java.lang.Integer","many-to-one","is.idega.idegaweb.golf.entity.Poll_option");

//		addAttribute("poll_result_id", "Spurning", true, true, "java.lang.Integer","many-to-one","is.idega.idegaweb.golf.entity.Poll");

		addAttribute("poll_user_address","IP-tala",true,true,"java.lang.String");

	}

	

	public String getAddress(){

		return getStringColumnValue("poll_user_address");

	}



	public Poll_option getOption(){

		return (Poll_option) getColumnValue("poll_option_id");

	}

	

	public int getOptionID(){

		return getIntColumnValue("poll_option_id");

	}



	public String getEntityName(){

		return "poll_result";

	}

	

	public String getName(){

		return getAddress();

	}

	

	public void setOption(int option_id){

		setColumn("poll_option_id",new Integer(option_id));

	}

	

	public void setOption(Poll_option option){

		setColumn("poll_option_id",option);

	}

	

	public void setUserIPAddress(String address){

		setColumn("poll_user_address",address);

	}



/*	public String getEntityName(){

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

*/	

}

