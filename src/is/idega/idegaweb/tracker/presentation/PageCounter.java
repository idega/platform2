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
  private boolean cph = true;
  private boolean cps = false;
  private boolean tph = false;
  private boolean tps = false;
  private boolean update = true;

  public PageCounter() {
  }

  public void init(IWContext iwc) throws Exception{

  }

  public void main(IWContext iwc) throws Exception{
    if(update){
      TrackerBusiness.runThroughTheStatsMachine(iwc);
    }

    if(cph){
      Text hits = new Text("Current page hits: "+TrackerBusiness.getCurrentPageHits(iwc));
      hits.setBold(true);
      add(hits);
      addBreak();
    }

    if(cps){
      Text hits2 = new Text("Current page sessions: "+TrackerBusiness.getCurrentPageSessions(iwc));
      hits2.setBold(true);
      add(hits2);
      addBreak();
    }

    if(tph){
      Text hits4 = new Text("Total website sessions: "+TrackerBusiness.getTotalSessions());
      hits4.setBold(true);
      add(hits4);
      addBreak();
    }

    if(tps){
      Text hits3 = new Text("Total website hits: "+TrackerBusiness.getTotalHits());
      hits3.setBold(true);
      add(hits3);
    }

  }


  public void setShowCurrentPageHits(boolean show){
    this.cph = show;
  }

  public void setShowCurrentPageSessions(boolean show){
    this.cps = show;
  }

  public void setShowTotalHits(boolean show){
    this.tph = show;
  }

  public void setShowTotalSessions(boolean show){
    this.tps = show;
  }

  public void setUpdateStats(boolean update){
    this.update = update;
  }


}