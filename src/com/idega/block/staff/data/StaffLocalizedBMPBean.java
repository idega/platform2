//idega 2000 - Laddi

package com.idega.block.staff.data;



import java.sql.SQLException;



public class StaffLocalizedBMPBean extends com.idega.data.GenericEntity implements com.idega.block.staff.data.StaffLocalized {



  public StaffLocalizedBMPBean(){

    super();

  }



  public StaffLocalizedBMPBean(int id)throws SQLException{

    super(id);

  }



  public void initializeAttributes(){

    addAttribute(getIDColumnName());

    addAttribute(getColumnNameLocaleId(), "Locale", true, true, java.lang.Integer.class,"many_to_one",com.idega.core.localisation.data.ICLocale.class);

    addAttribute(getColumnNameTitle(), "Title", true, true, java.lang.String.class);

    addAttribute(getColumnNameEducation(), "Education", true, true, java.lang.String.class,30000);

    addAttribute(getColumnNameArea(), "Area", true, true, java.lang.String.class,30000);

  }



  public static String getEntityTableName(){ return "ST_STAFF_LOCALIZED";}

  public static String getColumnNameLocaleId(){ return "IC_LOCALE_ID";}

  public static String getColumnNameTitle(){ return "TITLE";}

  public static String getColumnNameEducation(){ return "EDUCATION";}

  public static String getColumnNameArea(){ return "AREA";}





  public String getEntityName(){

    return getEntityTableName();

  }

  public int getLocaleId(){

    return getIntColumnValue(getColumnNameLocaleId());

  }

  public void setLocaleId(int id){

    setColumn(getColumnNameLocaleId(),id);

  }

  public void setLocaleId(Integer id){

    setColumn(getColumnNameLocaleId(),id);

  }

  public String getTitle(){

    return getStringColumnValue(getColumnNameTitle());

  }

  public void setTitle(String title){

    setColumn(getColumnNameTitle(), title);

  }

  public String getEducation(){

    return getStringColumnValue(getColumnNameEducation());

  }

  public void setEducation(String education){

    setColumn(getColumnNameEducation(), education);

  }

  public String getArea(){

    return getStringColumnValue(getColumnNameArea());

  }

  public void setArea(String area){

    setColumn(getColumnNameArea(), area);

  }

}

