package is.idega.idegaweb.tracker.business;

import com.idega.presentation.IWContext;
import com.idega.idegaweb.IWCacheManager;
import is.idega.idegaweb.tracker.data.*;
import com.idega.builder.business.BuilderLogic;
import com.idega.builder.data.IBDomain;
import com.idega.builder.data.IBPage;
import com.idega.util.idegaTimestamp;
import com.idega.core.localisation.business.ICLocaleBusiness;
import java.util.Map;
import java.util.Hashtable; //synchronized
import java.util.HashMap;//unsynchronized
import java.util.ArrayList;//unsynchronized
import java.util.Iterator;

/**
 * Title:        is.idega.idegaweb.tracker.business.TrackerBusiness
 * Description:  The main business object for the idegaWeb Tracker statistics application
 * Copyright:    Copyright (c) 2002
 * Company:      Idega software
 * @author Eirikur S. Hrafnsson
 * @version 1.0
 */

public class TrackerBusiness {
  private static IWCacheManager cm;
  private static IBDomain domainEntity;
  private static String TR_USER_AGENT_KEY = "tr.ua";
  private static String TR_PAGE_CACHE_KEY = "tr.pg";
  private static String TR_PAGE_TOTAL_CACHE_KEY = "tr.pgt";
  private static String TR_REFERRER_CACHE_KEY = "tr.re";
  private static String TR_DOMAIN_CACHE_KEY = "tr.do";

  private static Map pages;
  private static Map referers;
  private static Map agents;
  private static Map domain;

  private static int totalHits = 0;

  public TrackerBusiness() {
  }

  public static void runThroughTheStatsMachine(IWContext iwc){
    if( cm == null ) cm = IWCacheManager.getInstance(iwc.getApplication());
    if( domainEntity == null ) domainEntity = BuilderLogic.getInstance().getCurrentDomain(iwc);/**@todo add multidomain support**/




    handlePageStats(iwc);
    //handleDomainStats(iwc);
    handleReferrerStats(iwc);
    handleUserAgentStats(iwc);
  }

  public static void handlePageStats(IWContext iwc){
    if( pages == null ){
      pages = new HashMap();
    }

    int pageId = getCurrentPageId(iwc);
    if(pageId!=-1){
      PageStatistics page = new PageStatistics();
      page.setPageId(pageId);
      page.setPreviousPageId(pageId);/**@todo this shit here**/
      page.setLocale(ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale()));
      page.setUserId(iwc.getUserId());
      page.setModificationDate(idegaTimestamp.getTimestampRightNow());
      page.setGenerationTime(200);

      ArrayList pageLog = (ArrayList) pages.get(String.valueOf(pageId));
      if( pageLog == null ){
        pageLog = new ArrayList();
      }

      pageLog.add(page);
      pages.put(iwc.getSession().getId(),pageLog);
      totalHits++;
    }


  }

  public static void handleDomainStats(IWContext iwc){
    if( domainEntity == null ) domainEntity = BuilderLogic.getInstance().getCurrentDomain(iwc);/**@todo add multidomain support**/
    //domain.getID();
    // bara utreikningar og insert
  }

  public static void handleReferrerStats(IWContext iwc){
    String referer = iwc.getReferer();
    if( referers == null ){ referers = new Hashtable();}

    if(referer!=null){
      ReferrerStatistics stats = (ReferrerStatistics) referers.get(referer);
      if( stats == null ){
       stats = new ReferrerStatistics();
       stats.setReferrerUrl(referer);
       stats.setSessions(1);
       stats.setModificationDate(idegaTimestamp.getTimestampRightNow());
       referers.put(stats.getReferrerUrl(),stats);
      }
      else{
        stats.setSessions(stats.getSessions()+1);
      }
    }
  }

  public static void handleUserAgentStats(IWContext iwc){
    String userAgent = iwc.getUserAgent();
    if( agents == null ){ agents = new Hashtable();}

    if(userAgent!=null){
      UserAgentStatistics stats = (UserAgentStatistics) agents.get(userAgent);
      if( stats == null ){
        stats = new UserAgentStatistics();
        stats.setUserAgent(userAgent);
        stats.setSessions(1);
        stats.setModificationDate(idegaTimestamp.getTimestampRightNow());
        referers.put(stats.getUserAgent(),stats);
      }
      else{
        stats.setSessions(stats.getSessions()+1);
      }
    }
  }

  public static int getCurrentPageHits(IWContext iwc){
    int pageId = getCurrentPageId(iwc);
    int hits = 0;
    if(pages!=null){
      ArrayList pageLog = (ArrayList) pages.get(String.valueOf(pageId));
      if(pageLog!=null) hits = pageLog.size();
    }
    return hits;
  }

  public static int getTotalHits(){
    return totalHits;
  }

  public static int getTotalSessions(){
    int hits = 0;
    if(pages!=null){
      hits = pages.size();
      Iterator iter = pages.entrySet().iterator();
      while (iter.hasNext()) {
        String item = (String) iter.next();
        hits+=((ArrayList)pages.get(item)).size();
      }
    }
    return hits;
  }

  public static int getCurrentPageId(IWContext iwc){
    int returner = -1;
    String pageId = iwc.getParameter(BuilderLogic.IB_PAGE_PARAMETER);
    if(pageId==null) pageId = iwc.getParameter(BuilderLogic.IB_PAGE_PARAMETER);
    try {
     if(pageId!=null){
      returner = Integer.parseInt(pageId);
     }

    }
    catch (Exception ex) {
    //do nothing
    }

    return returner;
  }

}