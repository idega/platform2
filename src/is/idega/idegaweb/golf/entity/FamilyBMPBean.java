//idega 2001 -eiki

package is.idega.idegaweb.golf.entity;



//import java.util.*;

import java.sql.*;



public class FamilyBMPBean extends is.idega.idegaweb.golf.entity.GolfEntityBMPBean implements is.idega.idegaweb.golf.entity.Family {



  public FamilyBMPBean(){

    super();

  }

  public FamilyBMPBean(int id)throws SQLException{

    super(id);

  }

  public void initializeAttributes(){

    addAttribute(getIDColumnName());

    addAttribute("name","Fjölskylda",true,true,"java.lang.String");

  }

  public String getEntityName(){

    return "family";

  }

  public String getName(){

    return getStringColumnValue("name");

  }

  public void setName(String name){

    setColumn("name", name);

  }





}

