package is.idega.experimental.idotest;

import com.idega.data.*;
import java.sql.SQLException;

/**
 * Title:        IdegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Idega Software
 * @author Eirikur S. Hrafnsson
 * @version 1.0
 */

public class ResponseBMPBean extends GenericEntity implements Response{


  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute("my_text","The text",true,true,String.class);
    this.addManyToManyRelationShip(Question.class);
  }

  public String getEntityName() {
    return "test_response";
  }


  public void setResponse(String text){
    setColumn("my_text",text);
  }

  public String getResponse(){
    return getStringColumnValue("my_text");
  }


  public void insertStartData()throws SQLException{
    try{
      //ResponseHome rhome = (ResponseHome)IDOLookup.getHome(this.getClass());
      ResponseHome rhome = (ResponseHome)getEJBLocalHome();
      Response resp = rhome.create();
      resp.setResponse("My First response");
      resp.store();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }


	public boolean doInsertInCreate(){
		return true;
	}

}