package is.idega.idegaweb.tracker.data;

import com.idega.core.data.ICLanguage;
import java.sql.Timestamp;
import java.sql.SQLException;
import com.idega.data.GenericEntity;


/**
 * Title:        com.idega.idegaweb.tracker.data.PageStatistics
 * Description:  Keeps track of page hits and sessions
 * Copyright:    Copyright (c) 2002
 * Company:      idega
 * @author <a href="eiki@idega.is">Eirikur S. Hrafnsson</a>Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class PageStatistics extends GenericEntity {

  public PageStatistics() {
    super();
  }

  public PageStatistics(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnNamePageId(),"Page",true,true,Integer.class,"many-to-one",com.idega.builder.data.IBPage.class);
    addAttribute(getColumnNamePreviousPageId(),"Previous page",true,true,Integer.class,"many-to-one",com.idega.builder.data.IBPage.class);
    addAttribute(getColumnNameLanguageId(),"Language",true,true, Integer.class,"many-to-one",ICLanguage.class);
    addAttribute(getColumnNameHits(),"Number of hits on page",true,true,java.lang.Integer.class);
    addAttribute(getColumnNameSessions(),"Number of unique hits",true,true,java.lang.Integer.class);
    addAttribute(getColumnNameDate(),"Date of record",true,true, java.sql.Timestamp.class);
    addAttribute(getColumnNameGenerationTime(),"Average time to generate xml",true,true,java.lang.Integer.class);
  }

  public String getEntityName() {
    return getEntityTableName();
  }

  public static String getEntityTableName(){ return "TR_PAGE_STATISTICS";}
  public static String getColumnNamePageId(){return "IB_PAGE_ID";}
  public static String getColumnNamePreviousPageId(){return "IB_PREVIOUS_PAGE_ID";}
  public static String getColumnNameLanguageId(){return "IC_LANGUAGE_ID";}
  public static String getColumnNameHits(){return "HITS";}
  public static String getColumnNameSessions(){return "SESSIONS";}
  public static String getColumnNameDate(){return "MODIFICATION_DATE";}
  public static String getColumnNameGenerationTime(){return "GENERATION_TIME";}

  public int getPageId(){
    return getIntColumnValue(getColumnNamePageId());
  }

  public int getPreviousPageId(){
    return getIntColumnValue(getColumnNamePreviousPageId());
  }

  public int getLanguage(){
    return getIntColumnValue(getColumnNameLanguageId());
  }

  public int getHits(){
    return getIntColumnValue(getColumnNameHits());
  }

  public int getSessions(){
    return getIntColumnValue(getColumnNameSessions());
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

  public void setLanguage(int language){
    setColumn(getColumnNameLanguageId(), new Integer(language));
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

}