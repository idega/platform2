package is.idega.idegaweb.tracker.data;

import java.sql.SQLException;
import java.sql.Timestamp;

import com.idega.core.builder.data.ICPage;
import com.idega.core.localisation.data.ICLocale;
import com.idega.core.user.data.User;


/**
 * Title:        com.idega.idegaweb.tracker.data.PageTotalStatistics
 * Description:  Keeps track of every user operation
 * Copyright:    Copyright (c) 2002
 * Company:      idega
 * @author <a href="eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class PageStatisticsBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.tracker.data.PageStatistics {

  public PageStatisticsBMPBean() {
    super();
  }

  public PageStatisticsBMPBean(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnNamePageId(),"Page",true,true,Integer.class,"many-to-one",ICPage.class);
    addAttribute(getColumnNamePreviousPageId(),"Previous page",true,true,Integer.class,"many-to-one",ICPage.class);
    addAttribute(getColumnNameLocaleId(),"Locale",true,true, Integer.class,"many-to-one",ICLocale.class);
    addAttribute(getColumnNameDate(),"Date of record",true,true,Timestamp.class);
    addAttribute(getColumnNameGenerationTime(),"time to generate xml",true,true,Integer.class);
    addAttribute(getColumnNameUserId(),"user id",true,true,Integer.class,"many-to-one",User.class);
  }

  public String getEntityName() {
    return getEntityTableName();
  }

  public static String getEntityTableName(){ return "TR_PAGE_STATISTICS";}
  public static String getColumnNamePageId(){return "IB_PAGE_ID";}
  public static String getColumnNamePreviousPageId(){return "IB_PREVIOUS_PAGE_ID";}
  public static String getColumnNameLocaleId(){return "IC_LOCALE_ID";}
  public static String getColumnNameHits(){return "HITS";}
  public static String getColumnNameSessions(){return "SESSIONS";}
  public static String getColumnNameDate(){return "MODIFICATION_DATE";}
  public static String getColumnNameGenerationTime(){return "GENERATION_TIME";}
  public static String getColumnNameUserId(){return "USER_ID";}

  public int getPageId(){
    return getIntColumnValue(getColumnNamePageId());
  }

  public int getPreviousPageId(){
    return getIntColumnValue(getColumnNamePreviousPageId());
  }

  public int getLocale(){
    return getIntColumnValue(getColumnNameLocaleId());
  }

  public int getHits(){
    return getIntColumnValue(getColumnNameHits());
  }

  public int getSessions(){
    return getIntColumnValue(getColumnNameSessions());
  }

  public int getUserId(){
    return getIntColumnValue(getColumnNameUserId());
  }

  public Timestamp getDate(){
    return (Timestamp) getColumnValue(getColumnNameDate());
  }

  public void setPageId(int pageId){
    setColumn(getColumnNamePageId(), pageId);
  }

  public void setPreviousPageId(int pageId){
    setColumn(getColumnNamePreviousPageId(), pageId);
  }

  public void setLocale(int locale){
    setColumn(getColumnNameLocaleId(), new Integer(locale));
  }

  public void setHits(int hits){
    setColumn(getColumnNameHits(), hits);
  }

  public void setSessions(int sessionHits){
    setColumn(getColumnNameSessions(), sessionHits);
  }

  public void setModificationDate(Timestamp date){
    setColumn(getColumnNameDate(), date);
  }

  public void setGenerationTime(int milliseconds){
    setColumn(getColumnNameGenerationTime(), milliseconds);
  }

  public void setUserId(int userId){
    setColumn(getColumnNameUserId(), userId);
  }

}
