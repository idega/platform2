package is.idega.experimental.idotest;


import com.idega.data.*;
import com.idega.util.database.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class IDOTest {
	private static boolean lookupThroughJNDI=true;

  public IDOTest(){
  }

  public static void main(String args[]){
    doIDOTest();
  }


  public static void doIDOTest(){
	if(!lookupThroughJNDI){
	    PoolManager.getInstance("/idega/webapps/ROOT/idegaweb/properties/db.properties");
	}
    EntityControl.setAutoCreationOfEntities(true);

    try{
        QuestionHome qhome = getQuestionHome();
        
        Question question = qhome.create();
        question.setText("crappson");
        question.store();

        question = qhome.create();
        question.setText("crappsrapps");
        question.store();


        ResponseHome rhome = getResponseHome();
        Response response = rhome.create();
        response.setResponse("response2");
        response.store();

		try{
	        Response response1 = rhome.findByPrimaryKey(new Integer(1));
	        response1.setResponse("response1changedNow");
	        response1.store();
		}
		catch(Exception e){
			System.out.println("No response found with pk=1");	
		}

        Collection questions = qhome.findAllQuestionsContaining("rappson");
        Iterator iter = questions.iterator();
        System.out.println("iter.getClass().getName()="+iter.getClass().getName());
        System.out.println("questions.getClass().getName()="+questions.getClass().getName());
          
        while (iter.hasNext()) {
          Question item = (Question)iter.next();
          System.out.println("Found: "+item.getText()+" with id="+item.getPrimaryKey());
        }

		System.out.println("IDOTest ran OK");
    }
    catch(Exception e){
    	System.out.println("Error running IDOTest");
      e.printStackTrace();
    }
    
  }
  
  
  public static QuestionHome getQuestionHome()throws Exception{
  	return (QuestionHome)IDOLookup.getHome(QuestionBMPBean.class);
  }
  
  public static ResponseHome getResponseHome()throws Exception{
  	return (ResponseHome)IDOLookup.getHome(ResponseBMPBean.class);
  }
  
  public static QuestionHome getQuestionHomeJNDI()throws Exception{
  	if(lookupThroughJNDI){
  		InitialContext jndiContext = getInitialContext();
  		QuestionHome home = null;
		Object homeObj = jndiContext.lookup(QuestionBMPBean.class.getName());
		//home = (QuestionHome) jndiContext.lookup("java:comp/env/"+QuestionBMPBean.class.getName());
		home = (QuestionHome)PortableRemoteObject.narrow(homeObj, QuestionHome.class);	
  		return home;
  	}
  	else{
  		return (QuestionHome)IDOLookup.getHome(Question.class);
  	}
  }
  
  
   public static ResponseHome getResponseHomeJNDI()throws Exception{
  	if(lookupThroughJNDI){
  		InitialContext jndiContext = getInitialContext();
		ResponseHome home = null;
		Object homeObj = jndiContext.lookup(ResponseBMPBean.class.getName());
		//home = (ResponseHome) jndiContext.lookup("java:comp/env/"+ResponseBMPBean.class.getName());
		home = (ResponseHome)PortableRemoteObject.narrow(homeObj, ResponseHome.class);	
  		return home;
  	}
  	else{
  		return (ResponseHome)IDOLookup.getHome(Response.class);
  	}
  }
  
  private static InitialContext getInitialContext() throws NamingException{
  	Properties properties = new Properties();
  	try {
		properties.load(new FileInputStream("/idega/jndi.properties"));
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
  	return new InitialContext(properties);
  }




}




