package is.idega.idegaweb.tracker.business;

import com.idega.presentation.IWContext;
import com.idega.idegaweb.IWCacheManager;
import is.idega.idegaweb.tracker.data.*;
import com.idega.builder.business.BuilderLogic;
import com.idega.builder.data.IBDomain;
import com.idega.builder.data.IBPage;
import java.util.Map;
import java.util.Hashtable; //synchronized
import java.util.HashMap;//unsynchronized

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
  private static Map referrers;
  private static Map agents;
  private static Map domain;

  public TrackerBusiness() {
  }

  public static void runThroughTheStatsMachine(IWContext iwc){
    if( cm == null ) cm = IWCacheManager.getInstance(iwc.getApplication());
    if( domainEntity == null ) domainEntity = BuilderLogic.getInstance().getCurrentDomain(iwc);/**@todo add multidomain support**/

    String sPageId = iwc.getParameter(BuilderLogic.IB_PAGE_PARAMETER);
    String sessionPageId = iwc.getParameter(BuilderLogic.IB_PAGE_PARAMETER);


    handlePageStats(iwc);
    //handleDomainStats(iwc);
    handleReferrerStats(iwc);
    handleUserAgentStats(iwc);
  }

  public static void handlePageStats(IWContext iwc){
  //  iwc.getApplicationAttribute()


  }

  public static void handleDomainStats(IWContext iwc){
    if( domainEntity == null ) domainEntity = BuilderLogic.getInstance().getCurrentDomain(iwc);/**@todo add multidomain support**/
    //domain.getID();
    // bara utreikningar og insert
  }

  public static void handleReferrerStats(IWContext iwc){
    String userAgent = iwc.getUserAgent();
    if( agents == null ){ agents = new Hashtable();}

    UserAgentStatistics stats = (UserAgentStatistics) agents.get(userAgent);
    if( stats == null ){
      stats = new UserAgentStatistics();
      stats.setSessions(1);
    }
    else{
      stats.setSessions(stats.getSessions()+1);
    }
  }

  public static void handleUserAgentStats(IWContext iwc){
    String userAgent = iwc.getUserAgent();
    if( agents == null ){ agents = new Hashtable();}

    UserAgentStatistics stats = (UserAgentStatistics) agents.get(userAgent);
    if( stats == null ){
      stats = new UserAgentStatistics();
      stats.setSessions(1);
    }
    else{
      stats.setSessions(stats.getSessions()+1);
    }
  }

  public static void getCurrentPageHits(int pageId){
   // cm.get
  }

}