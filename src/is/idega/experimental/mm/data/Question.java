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

public class Question extends GenericEntity implements IDOLegacyEntity{

  public Question(){
  }

  public Question(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute("text","The text",true,true,String.class);
    this.addManyToManyRelationShip(Response.class);
  }

  public String getEntityName() {
    return "mm3_question";
  }


  public void setText(String text){
    setColumn("text",text);
  }

  public String getText(){
    return getStringColumnValue("text");
  }


  public void insertStartData()throws SQLException{
    Question quest = new Question();
    quest.setText("My question");
    quest.insert();
  }


  public Response getFirstResponse()throws SQLException{
     Response[] responses = (Response[])this.findRelated(new Response());
     if(responses!=null){
        if(responses.length>0){

          return  responses[0];
        }
      }
    return null;
  }

}