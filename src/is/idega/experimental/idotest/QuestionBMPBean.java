package is.idega.experimental.idotest;

import com.idega.data.*;
import java.sql.SQLException;
import java.util.Collection;
import javax.ejb.FinderException;

/**
 * Title:        IdegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Idega Software
 * @author Eirikur S. Hrafnsson
 * @version 1.0
 */

public class QuestionBMPBean extends GenericEntity implements Question{

  public QuestionBMPBean(){
  }

  public QuestionBMPBean(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute("my_text","The text",true,true,String.class);
    this.addManyToManyRelationShip(Response.class);
  }

  public String getEntityName() {
    return "test_question";
  }


  public void setText(String text){
    setColumn("my_text",text);
  }

  public String getText(){
    return getStringColumnValue("my_text");
  }


  public void insertStartData()throws SQLException{
    try{
      //QuestionHome home = (QuestionHome)IDOLookup.getHome(this.getClass());
      QuestionHome home = (QuestionHome)getEJBLocalHome();
      Question quest = home.create();
      quest.setText("My First question");
      quest.store();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

/*
  public Response getFirstResponse()throws SQLException{
     Response[] responses = (Response[])this.findRelated(new ResponseBMPBean());
     if(responses!=null){
        if(responses.length>0){

          return  responses[0];
        }
      }
    return null;
  }
*/

	public boolean doInsertInCreate(){
		return true;
	}

  public Collection ejbFindAllQuestionsContaining(String string)throws FinderException{
    String sql = "select * from "+this.getTableName()+" where my_text like '%"+string+"%'";
    return super.idoFindIDsBySQL(sql);
  }

  public Collection ejbFindAllQuestionsNotContaining(String string)throws FinderException{
    String sql = "select * from "+this.getTableName()+" where my_text not like '%"+string+"%'";
    return super.idoFindIDsBySQL(sql);
  }


  public int ejbHomeGetNumberOfQuestions()throws javax.ejb.EJBException{
    return 1;
  }

}