package is.idega.experimental.mm.presentation;

import com.idega.jmodule.object.interfaceobject.Window;
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

public class MmWindow extends Window {

  public static final String responseID="mm_response_id";

  public MmWindow() {
  }

  public void main(ModuleInfo modinfo)throws Exception{
    int questID = Integer.parseInt(modinfo.getParameter(responseID));
    Response r = new Response(questID);
    add("My response is "+r.getResponse());
  }

}