package is.idega.experimental.mm.presentation;

import is.idega.experimental.mm.data.Response;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;

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

  public void main(IWContext iwc)throws Exception{
    int questID = Integer.parseInt(iwc.getParameter(responseID));
    Response r = new Response(questID);
    add("My response is "+r.getResponse());
  }

}