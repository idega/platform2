package is.idega.experimental.mm.presentation;

import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import is.idega.experimental.mm.business.*;
import is.idega.experimental.mm.data.*;
import java.util.*;


/**
 * Title:        IdegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Idega Software
 * @author Eirikur S. Hrafnsson
 * @version 1.0
 */

public class MmTest extends JModuleObject {


  int numberOfQuestions=2;

  public MmTest(){
  }

  public void main(ModuleInfo modinfo)throws Exception{
    Table t = new Table();
    add(t);
    List l = MMBusiness.getAllQuestions();
    if(l!=null){
      Iterator iter = l.iterator();
      int size=l.size();
      int count=1;
      while (iter.hasNext()) {
        if((size-count) < numberOfQuestions){
          Question quest = (Question)iter.next();
          Response first = quest.getFirstResponse();
          t.add(quest.getText(),1,count);
          if(first!=null){
            Link responseLink = new Link("Response");
            responseLink.setWindowToOpen(MmWindow.class);
            responseLink.addParameter(MmWindow.responseID,first.getID());
            t.add(responseLink,2,count);
          }
          count++;
        }
      }
    }
  }

  public void setNumberOfQuestions(int number){
      numberOfQuestions=number;
  }
}