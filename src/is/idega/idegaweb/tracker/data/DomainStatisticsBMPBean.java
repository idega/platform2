package is.idega.idegaweb.tracker.data;

import java.sql.SQLException;
import java.sql.Timestamp;

import com.idega.core.builder.data.ICDomain;
import com.idega.core.localisation.data.ICLocale;


/**
 * Title:        com.idega.idegaweb.tracker.data.DomainStatistics
 * Description:  Keeps track of domain total hits and sessions
 * Copyright:    Copyright (c) 2002
 * Company:      idega
 * @author <a href="eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class DomainStatisticsBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.tracker.data.DomainStatistics {

  public DomainStatisticsBMPBean() {
    super();
  }

  public DomainStatisticsBMPBean(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameDomainId(),"Domain",true,true,Integer.class,"many-to-one",ICDomain.class);
    addAttribute(getColumnNameLocaleId(),"Locale",true,true, Integer.class,"many-to-one",ICLocale.class);
    addAttribute(getColumnNameHits(),"Number of hits on Domain",true,true,Integer.class);
    addAttribute(getColumnNameSessions(),"Number of unique hits",true,true,Integer.class);
    addAttribute(getColumnNameDate(),"Date of record",true,true,Timestamp.class);
  }

  public String getEntityName() {
    return getEntityTableName();
  }

  public static String getEntityTableName(){ return "TR_DOMAIN_STATISTICS";}
  public static String getColumnNameHits(){return "HITS";}
  public static String getColumnNameDomainId(){return "IB_DOMAIN_ID";}
  public static String getColumnNamePreviousPageId(){return "IB_PREVIOUS_PAGE_ID";}
  public static String getColumnNameSessions(){return "SESSIONS";}
  public static String getColumnNameDate(){return "MODIFICATION_DATE";}
  public static String getColumnNameLocaleId(){return "IC_LOCALE_ID";}

  public int getDomainId(){
    return getIntColumnValue(getColumnNameDomainId());
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

  public Timestamp getDate(){
    return (Timestamp) getColumnValue(getColumnNameDate());
  }

  public void setDomainId(int domainId){
    setColumn(getColumnNameDomainId(), domainId);
  }

  public void setLocale(int locale){
    setColumn(getColumnNameLocaleId(),locale);
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


}
