package is.idega.idegaweb.tracker.presentation;

import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.text.Text;
import com.idega.presentation.IWContext;
import is.idega.idegaweb.tracker.business.TrackerBusiness;

/**
 * Title:        is.idega.idegaweb.tracker.presentation.PageCounter
 * Description:  A simple page counter that can display the number of visits/hits with text/images
 * Copyright:    Copyright (c) 2002
 * Company:      Idega software
 * @author Eirikur S. Hrafnsson
 * @version 1.0
 */

public class PageCounter extends PresentationObjectContainer {

  public PageCounter() {
  }

  public void init(IWContext iwc) throws Exception{

  }

  public void main(IWContext iwc) throws Exception{
    TrackerBusiness.runThroughTheStatsMachine(iwc);

    Text hits = new Text("Current page hits: "+TrackerBusiness.getCurrentPageHits(iwc));
    hits.setBold(true);
    add(hits);

    addBreak();

    Text hits2 = new Text("Current page sessions: "+TrackerBusiness.getCurrentPageSessions(iwc));
    hits2.setBold(true);
    add(hits2);

    addBreak();

    Text hits4 = new Text("Total website sessions: "+TrackerBusiness.getTotalHits());
    hits4.setBold(true);
    add(hits4);

    addBreak();

    Text hits3 = new Text("Total website hits: "+TrackerBusiness.getTotalSessions());
    hits3.setBold(true);
    add(hits3);


  }



}