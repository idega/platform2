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

  public ResponseBMPBean(){
  }

  public ResponseBMPBean(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute("text","The text",true,true,String.class);
    this.addManyToManyRelationShip(Question.class);
  }

  public String getEntityName() {
    return "test_response";
  }


  public void setResponse(String text){
    setColumn("text",text);
  }

  public String getResponse(){
    return getStringColumnValue("text");
  }


  public void insertStartData()throws SQLException{
    try{
      Response quest = new ResponseBMPBean();
      quest.setResponse("My response");
      quest.store();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }


}