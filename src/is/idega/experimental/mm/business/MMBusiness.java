package is.idega.experimental.mm.business;

import com.idega.data.*;
import java.util.List;
import is.idega.experimental.mm.data.*;

/**
 * Title:        IdegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Idega Software
 * @author Eirikur S. Hrafnsson
 * @version 1.0
 */

public class MMBusiness {

  public MMBusiness() {
  }


  public static List getAllQuestions()throws Exception{
    Question q = new Question();
    q.setText(Integer.toString(q.hashCode()));
    q.insert();

    Response r = new Response();
    r.setResponse("Resp: "+Integer.toString(q.hashCode()));
    r.insert();

    r.addTo(q);

    return EntityFinder.findAll(new Question());
  }

}