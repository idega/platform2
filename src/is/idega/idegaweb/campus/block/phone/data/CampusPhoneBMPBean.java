package is.idega.idegaweb.campus.block.phone.data;



import java.sql.SQLException;





/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved

 * Company:      idega

  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>

 * @version 1.1

 */



public class CampusPhoneBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.campus.block.phone.data.CampusPhone {



  public CampusPhoneBMPBean() {

  }

  public CampusPhoneBMPBean(int id) throws SQLException{

    super(id);

  }

  public void initializeAttributes() {

    addAttribute(getIDColumnName());

    addAttribute(getColumnNamePhoneNumber(),"Phone number",true,true,String.class);

    addAttribute(getColumnNameApartmentId(),"Apartment",true,true,Integer.class,"one-to-one",com.idega.block.building.data.Apartment.class);

    addAttribute(getColumnNameDateInstalled(),"Installed",true,true,java.sql.Date.class);

    addAttribute(getColumnNameDateResigned(),"Resigned",true,true,java.sql.Date.class);

  }

  public String getEntityName() {

    return getEntityTableName();

  }



  public static String getEntityTableName(){return "CAM_PHONE";}

  public static String getColumnNamePhoneNumber(){return "PHONE_NUMBER";}

  public static String getColumnNameApartmentId(){return "BU_APARTMENT_ID";}

  public static String getColumnNameDateInstalled(){return "DATE_INSTALLED";}

  public static String getColumnNameDateResigned(){return "DATE_RESIGNED";}





  public void setPhoneNumber(String number){

    setColumn(getColumnNamePhoneNumber(),number);

  }

  public String getPhoneNumber(){

    return getStringColumnValue(getColumnNamePhoneNumber());

  }

  public int getApartmentId(){

    return getIntColumnValue(getColumnNameApartmentId());

  }

  public void setApartmentId(int id){

    setColumn(getColumnNameApartmentId(),id);

  }

  public void setDateInstalled(java.sql.Date date){

    setColumn(getColumnNameDateInstalled(),date);

  }

  public java.sql.Date getDateInstalled(){

    return((java.sql.Date)getColumnValue(getColumnNameDateInstalled()));

  }

  public void setDateResigned(java.sql.Date date){

    setColumn(getColumnNameDateResigned(),date);

  }

  public java.sql.Date getDateResigned(){

    return((java.sql.Date)getColumnValue(getColumnNameDateResigned()));

  }

}

