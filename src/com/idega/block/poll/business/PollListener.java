package com.idega.block.poll.business;

import com.idega.business.IWEventListener;
import com.idega.presentation.IWContext;

/**
 * Title:        Poll vote handling
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2001 - idega team - <a href="mailto:laddi@idega.is">Þórhallur Helgason</a>
 * @version 1.0
 */

public class PollListener implements IWEventListener{

  public PollListener() {
  }

  public void actionPerformed(IWContext iwc){
    if (PollBusiness.thisObjectSubmitted(iwc.getParameter(PollBusiness._PARAMETER_POLL_VOTER))){
      PollBusiness.handleInsert(iwc);
    }
  }
}