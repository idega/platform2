package is.idega.idegaweb.tracker.presentation;

import is.idega.idegaweb.tracker.business.TrackerBusiness;
import is.idega.idegaweb.tracker.data.PageStatistics;
import is.idega.idegaweb.tracker.data.ReferrerStatistics;
import is.idega.idegaweb.tracker.data.UserAgentStatistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.idega.core.builder.data.ICPage;
import com.idega.core.builder.data.ICPageHome;
import com.idega.data.IDOLookup;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
/**
 * Title:        is.idega.idegaweb.tracker.presentation.PageCounter
 * Description:  A simple page counter that can display the number of visits/hits with text/images
 * Copyright:    Copyright (c) 2002
 * Company:      Idega software
 * @author Eirikur S. Hrafnsson
 * @version 1.0
 */

public class PageCounter extends Block {
  private boolean showCurrentPH = true;//show current page hits
  private boolean showCurrentPS = false;//show current page sessions
  private boolean showTotalPH = false;//show total page hits
  private boolean showTotalPS = false;//show total page session
  private boolean showReferers = false;//show referers
  private boolean showUserAgents = false;//show user agents
  private boolean showPageList= false;//show page list
  private boolean update = true;//update the stats(true) or just showing stats(false)
  private Map ipFilter = new HashMap();/**clone**/

  public PageCounter() {
  }

  public void main(IWContext iwc) throws Exception{
    if( (update) && (updateStats(iwc.getRemoteIpAddress())) ){
      TrackerBusiness.runThroughTheStatsMachine(iwc);
    }

    if(showCurrentPH){
      Text hits = new Text("Current page hits: "+TrackerBusiness.getCurrentPageHits(iwc));
      hits.setBold(true);
      add(hits);
      addBreak();
    }

    if(showCurrentPS){
      Text hits2 = new Text("Current page sessions: "+TrackerBusiness.getCurrentPageSessions(iwc));
      hits2.setBold(true);
      add(hits2);
      addBreak();
    }

    if(showTotalPS){
      Text hits4 = new Text("Total website sessions: "+TrackerBusiness.getTotalSessions());
      hits4.setBold(true);
      add(hits4);
      addBreak();
    }

    if(showTotalPH){
      Text hits3 = new Text("Total website hits: "+TrackerBusiness.getTotalHits());
      hits3.setBold(true);
      add(hits3);
    }

    if(showPageList){
      //referers
      Table page = new Table();
      int y = 1;
      page.add("Page name",1,y);
      page.add("Count",2,y);

      ArrayList pageHits =  TrackerBusiness.getPageHitsArrayListSortedByHits();

      Iterator pageList = pageHits.iterator();
      PageStatistics item;
      ICPageHome pHome = (ICPageHome)IDOLookup.getHome(ICPage.class);
      ICPage thePage;
      while (pageList.hasNext()) {
        item = (PageStatistics) pageList.next();
        thePage = pHome.findByPrimaryKey(new Integer(item.getPageId()));
        page.add(thePage.getName(),1,++y);
        page.add(String.valueOf(item.getHits()),2,y);
      }

      add(page);

      addBreak();
    }

    if(showReferers){
      //referers
      Table refs = new Table();
      int y = 1;
      refs.add("Referer url",1,y);
      refs.add("Count",2,y);

      ArrayList referers =  TrackerBusiness.getRefererArrayListSortedBySessions();

      Iterator referer = referers.iterator();
      ReferrerStatistics item;
      while (referer.hasNext()) {
        item = (ReferrerStatistics) referer.next();
        refs.add(item.getReferrerUrl(),1,++y);
        refs.add(String.valueOf(item.getSessions()),2,y);
      }

     add(refs);

     addBreak();
    }


    if(showUserAgents){
      //agents
      Table agents = new Table();
      int y2 = 1;
      agents.add("User agents",1,y2);
      agents.add("Count",2,y2);

      ArrayList agentsList = TrackerBusiness.getAgentArrayListSortedBySessions();
      Iterator ua = agentsList.iterator();
      UserAgentStatistics item;
      while (ua.hasNext()) {
        item = (UserAgentStatistics) ua.next();
        agents.add(item.getUserAgent(),1,++y2);
        agents.add(String.valueOf(item.getSessions()),2,y2);
      }

      add(agents);
    }

  }


  public void setShowCurrentPageHits(boolean show){
    showCurrentPH = show;
  }

  public void setShowCurrentPageSessions(boolean show){
    showCurrentPS = show;
  }

  public void setShowTotalHits(boolean show){
    showTotalPH = show;
  }

  public void setShowTotalSessions(boolean show){
    showTotalPS = show;
  }

  public void setUpdateStats(boolean update){
    this.update = update;
  }

  public void setShowReferers(boolean show){
    showReferers = show;
  }

  public void setShowAgents(boolean show){
    showUserAgents = show;
  }

  public void setShowPageList(boolean show){
    showPageList = show;
  }

  public void setIpFilterNumber(String ipNumber){
   ipFilter.put(ipNumber,ipNumber);
  }

  public void setIpFilterNumber(String name, String ipNumber){
   ipFilter.put(name,ipNumber);
  }

  public void removeIpFilterNumber(String nameOrIpNumber){
   ipFilter.remove(nameOrIpNumber);
  }

  private boolean updateStats(String ipNumber){
    return !(ipFilter.containsValue(ipNumber));
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
