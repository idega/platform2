package is.idega.experimental.idotest;


import com.idega.data.*;
import com.idega.util.database.*;
import java.util.*;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class IDOTest {

  public IDOTest(){
  }

  public static void main(String args[]){
    doIDOTest();
  }


  public static void doIDOTest(){

    PoolManager manager = PoolManager.getInstance("/idega/webapps/ROOT/idegaweb/properties/db.properties");
    EntityControl.setAutoCreationOfEntities(true);

    try{
        QuestionHome qhome = (QuestionHome)IDOLookup.getHome(Question.class);

        Question question = qhome.create();
        question.setText("crappson");
        question.store();

        question = qhome.create();
        question.setText("crappsrapps");
        question.store();


        ResponseHome rhome = (ResponseHome)IDOLookup.getHome(Response.class);
        Response response = rhome.create();
        response.setResponse("response2");
        response.store();

        Response response1 = rhome.findByPrimaryKey(new Integer(1));
        response1.setResponse("response1changedNow");
        response1.store();


        Collection questions = qhome.findAllQuestionsContaining("crapps");
        Iterator iter = questions.iterator();
        while (iter.hasNext()) {
          Question item = (Question)iter.next();
          System.out.println("Found: "+item.getText()+" with id="+item.getPrimaryKey());
        }

    }
    catch(Exception e){
      e.printStackTrace();
    }
    System.out.println("IDOTest ran OK");
  }




}




