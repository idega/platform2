package is.idega.experimental.mm.data;

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

public class Response extends GenericEntity implements IDOLegacyEntity {

  public Response(){
  }

  public Response(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute("text","The text",true,true,String.class);
    this.addManyToManyRelationShip(Question.class);
  }

  public String getEntityName() {
    return "mm3_response";
  }


  public void setResponse(String text){
    setColumn("text",text);
  }

  public String getResponse(){
    return getStringColumnValue("text");
  }


  public void insertStartData()throws SQLException{
    Response quest = new Response();
    quest.setResponse("My response");
    quest.insert();
  }


}