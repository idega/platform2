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
    Text hits = new Text(TrackerBusiness.getCurrentPageHits(iwc)+" page hits!");
    hits.setBold(true);
    add(hits);

    Text hits2 = new Text(TrackerBusiness.getTotalPageHits()+" total page hits!");
    hits2.setBold(true);
    add(hits2);

  }



}