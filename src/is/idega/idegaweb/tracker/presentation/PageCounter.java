package is.idega.idegaweb.tracker.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;

import com.idega.presentation.IWContext;
import is.idega.idegaweb.tracker.business.TrackerBusiness;
import java.util.Map;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import is.idega.idegaweb.tracker.data.*;
/**
 * Title:        is.idega.idegaweb.tracker.presentation.PageCounter
 * Description:  A simple page counter that can display the number of visits/hits with text/images
 * Copyright:    Copyright (c) 2002
 * Company:      Idega software
 * @author Eirikur S. Hrafnsson
 * @version 1.0
 */

public class PageCounter extends Block {
  private boolean cph = true;
  private boolean cps = false;
  private boolean tph = false;
  private boolean tps = false;
  private boolean update = true;
  private Map ipFilter = new HashMap();/**clone**/

  public PageCounter() {
  }

  public void init(IWContext iwc) throws Exception{

  }

  public void main(IWContext iwc) throws Exception{
    if( (update) && (updateStats(iwc.getRemoteIpAddress())) ){
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



    if(cph){

    //referers
    Table refs = new Table();
    int y = 1;
    refs.add("Referer url",1,y);
    refs.add("Count",2,y);

    Hashtable referers = (Hashtable) TrackerBusiness.getReferers();
    if( referers != null ){
      Iterator iter = referers.keySet().iterator();
      while (iter.hasNext()) {
        ReferrerStatistics item = (ReferrerStatistics) referers.get((String)iter.next());
        refs.add(item.getReferrerUrl(),1,++y);
        refs.add(String.valueOf(item.getSessions()),1,y);
      }
    }
    add(refs);

addBreak();
    //agents
    Table agents = new Table();
    int y2 = 1;
    agents.add("User agents",1,y2);
    agents.add("Count",2,y2);

    Hashtable ua = (Hashtable) TrackerBusiness.getReferers();
    if( ua != null ){
      Iterator iter = ua.keySet().iterator();
      while (iter.hasNext()) {
        UserAgentStatistics item = (UserAgentStatistics) ua.get((String)iter.next());
        agents.add(item.getUserAgent(),1,++y2);
        agents.add(String.valueOf(item.getSessions()),1,y2);
      }
    }
    add(agents);

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

  public void setIpFilterNumber(String ipNumber){
   ipFilter.put(ipNumber,ipNumber);
  }

  public void removeIpFilterNumber(String ipNumber){
   ipFilter.remove(ipNumber);
  }

  private boolean updateStats(String ipNumber){
    String ip = (String)ipFilter.get(ipNumber);
    if(ip==null) return true;
    else return false;//dont update
  }

 public synchronized Object clone() {
    PageCounter obj = null;
    try {
      obj = (PageCounter)super.clone();
      obj.ipFilter = this.ipFilter;
    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }

    return obj;
  }


}