//idega 2000 - idega team



package is.idega.idegaweb.golf.entity;



//import java.util.*;

import java.sql.*;



public class GolfMembersBMPBean extends is.idega.idegaweb.golf.entity.GolfEntityBMPBean implements is.idega.idegaweb.golf.entity.GolfMembers {



	public GolfMembersBMPBean(){

		super();

	}



	public GolfMembersBMPBean(int id)throws SQLException{

		super(id);

	}



	public void initializeAttributes(){

                addAttribute("member_id","Meðlimur",false,false,"java.lang.Integer");

                addAttribute("union_id","Klúbbur",false,false,"java.lang.Integer");

		addAttribute("first_name","Fornafn",true,true,"java.lang.String");

		addAttribute("middle_name","Miðnafn",true,true,"java.lang.String");

		addAttribute("last_name","Eftirnafn",true,true,"java.lang.String");

                addAttribute("image_id","MyndNúmer",false,false,"java.lang.Integer");



        }



	public String getEntityName(){

		return "golf_members";

	}



	public int getMemberID(){

		return getIntColumnValue("member_id");

	}



	public int getUnionID(){

		return getIntColumnValue("union_id");

	}



	public String getName(){

          StringBuffer nameBuffer = new StringBuffer();

		if ((getFirstName() != null) && (getMiddleName() != null) && (getLastName() != null)){

			 nameBuffer.append(getFirstName());

                          nameBuffer.append(" ");

                           nameBuffer.append(getMiddleName());

                            nameBuffer.append(" ");

                             nameBuffer.append(getLastName());

		}

		else if ((getFirstName() != null) && (getLastName() != null)){

                   nameBuffer.append(getFirstName());

                    nameBuffer.append(" ");

                     nameBuffer.append(getLastName());

		}

		else if(getLastName() != null){

                  nameBuffer.append(getLastName());

		}

		else if (getFirstName() != null){

                  nameBuffer.append(getFirstName());

		}

		return  nameBuffer.toString();

	}



	public String getFirstName(){

		return (String) getColumnValue("first_name");

	}



	public String getMiddleName(){

		return (String) getColumnValue("middle_name");

	}



	public String getLastName(){

		return (String) getColumnValue("last_name");

	}



	public int getImageID(){

		return getIntColumnValue("image_id");

	}



}

