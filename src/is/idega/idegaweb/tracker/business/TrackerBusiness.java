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
  private static Map pageSessions;
  private static Map pageHits;

  private static int totalHits = 0;
  private static int totalSessions = 0;

  private static String domainName;

  public TrackerBusiness() {
  }

  private static void init(IWContext iwc){
    if( cm == null ) cm = IWCacheManager.getInstance(iwc.getApplication());
    if( domainEntity == null ) domainEntity = BuilderLogic.getInstance().getCurrentDomain(iwc);/**@todo add multidomain support**/
    if( pages == null ){pages = new HashMap();}
    if( agents == null ){ agents = new Hashtable();}
  }

  private static void incrementPageHits(String pageId){
    if( pageHits == null ){ pageHits = new Hashtable();}
    Integer hits = (Integer) pageHits.get(pageId);
    pageHits.put(pageId,getIncrementedInteger(hits));
  }

  private static void incrementPageSessions(String pageId){
    if( pageSessions == null ){ pageSessions = new Hashtable();}
    Integer hits = (Integer) pageSessions.get(pageId);
    pageSessions.put(pageId,getIncrementedInteger(hits));
  }

  private static Integer getIncrementedInteger(Integer i){
    if( i == null ) i = new Integer(1);
    else {
      i = new Integer(i.intValue()+1);
    }
    return i;
  }

  public static void runThroughTheStatsMachine(IWContext iwc){
    init(iwc);
    handlePageStats(iwc);
    //handleDomainStats(iwc);
    handleReferrerStats(iwc);
    handleUserAgentStats(iwc);
  }

  public static void handlePageStats(IWContext iwc){
    int pageId = getCurrentPageId(iwc);
    String sPageId = String.valueOf(pageId);
    String sessionId = iwc.getSession().getId();


    if(pageId!=-1){
      PageStatistics page = new PageStatistics();/**@todo clone this?**/
      page.setPageId(pageId);
      page.setPreviousPageId(pageId);/**@todo this shit here**/
      page.setLocale(ICLocaleBusiness.getLocaleId(iwc.getCurrentLocale()));
      page.setUserId(iwc.getUserId());
      page.setModificationDate(idegaTimestamp.getTimestampRightNow());
      page.setGenerationTime(200);/**@todo this shit here**/

      ArrayList pageLog = (ArrayList) pages.get(sessionId);
      if( pageLog == null ){
        pageLog = new ArrayList();
        //session stuff
        totalSessions++;
        incrementPageSessions(sPageId);
      }

      pageLog.add(page);
      pages.put(sessionId,pageLog);
      //hit stuff
      totalHits++;
      incrementPageHits(sPageId);
    }


  }

  public static void handleDomainStats(IWContext iwc){
    // bara utreikningar og insert
  }

  public static void handleReferrerStats(IWContext iwc){
    String referer = iwc.getReferer();
    if( domainName == null ) domainName = iwc.getServerName();

    if( referers == null ){ referers = new Hashtable();}

    if( (referer!=null) && (referer.indexOf(domainName)==-1) ){
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
        agents.put(stats.getUserAgent(),stats);
      }
      else{
        stats.setSessions(stats.getSessions()+1);
      }
    }
  }

  public static int getCurrentPageId(IWContext iwc){
    int returner = -1;
    String pageId = iwc.getParameter(BuilderLogic.IB_PAGE_PARAMETER);
      if(pageId==null) pageId = String.valueOf(BuilderLogic.getStartPageId(iwc));
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

  public static int getCurrentPageHits(IWContext iwc){
    if( pageHits == null ){ pageHits = new Hashtable();}

    Integer hits = (Integer) pageHits.get(String.valueOf(getCurrentPageId(iwc)));
    if( hits == null ) return 0;
    else return hits.intValue();
  }

  public static int getCurrentPageSessions(IWContext iwc){
    if( pageSessions == null ){ pageSessions = new Hashtable();}

    Integer sessions = (Integer) pageSessions.get(String.valueOf(getCurrentPageId(iwc)));
    if( sessions == null ) return 0;
    else return sessions.intValue();
  }

  public static int getTotalHits(){
    return totalHits;
  }

  public static int getTotalSessions(){
    return totalSessions;
  }

  public static Map getReferers(){
    return referers;
  }

  public static Map getUserAgents(){
    return agents;
  }







}