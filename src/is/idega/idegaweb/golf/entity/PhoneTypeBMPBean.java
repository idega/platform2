package is.idega.idegaweb.golf.entity;
import com.idega.data.GenericEntity;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      Idega
 * @author       Ægir
 * @version 1.0
 */

public class PhoneTypeBMPBean extends GenericEntity implements PhoneType {

  public void initializeAttributes(){
      addAttribute(getIDColumnName());
      addAttribute("name", "Tegund", true, true, "java.lang.String");
  }

  public String getEntityName(){
      return "phone_type";
  }

  public String getIDColumnName() {
      return "phone_type_id";
  }

  public String getType(){
      return getStringColumnValue("name");
  }

  public void setType(String name){
      setColumn("name", name);
  }

  public String getName(){
      return getType();
  }

  public void setName(String name){
      setType(name);
  }

}