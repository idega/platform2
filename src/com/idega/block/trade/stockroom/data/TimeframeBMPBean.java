package com.idega.block.trade.stockroom.data;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Locale;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;

/**
 * Title:        IW Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class TimeframeBMPBean extends com.idega.data.GenericEntity implements com.idega.block.trade.stockroom.data.Timeframe {
	
	private static Locale icelandicLocale = LocaleUtil.getIcelandicLocale();
	private static Locale englishLocale = LocaleUtil.getLocale("en");
	
  public TimeframeBMPBean(){
          super();
  }
  public TimeframeBMPBean(int id)throws SQLException{
          super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getNameColumnName(), "Name", true, true, String.class, 255);
    addAttribute(getTimeframeToColumnName(), "Til", true, true, java.sql.Timestamp.class);
    addAttribute(getTimeframeFromColumnName(), "Frá", true, true, java.sql.Timestamp.class);
    addAttribute(getYearlyColumnName(), "Árlegt", true, true, Boolean.class);

    this.addManyToManyRelationShip( Product.class, "SR_PRODUCT_TIMEFRAME" );
  }


  public void setDefaultValue() {
    setName("");
  }

  public String getEntityName(){
    return getTimeframeTableName();
  }
  public String getName(){
  	return getName(LocaleUtil.getIcelandicLocale());
  }
  
  public String getName(Locale locale) {
    String stampTxt1 = new IWTimestamp(this.getFrom()).getLocaleDate(locale);
    String stampTxt2 = new IWTimestamp(this.getTo()).getLocaleDate(locale);
	  try {
	    if (getIfYearly() ){
	    	if (locale.equals(icelandicLocale)) {
		    	stampTxt1 = stampTxt1.substring(0, stampTxt1.length() -4);
		    	stampTxt2 = stampTxt2.substring(0, stampTxt2.length() -4);
	    	}	else if (locale.equals(englishLocale)) {
		    	stampTxt1 = stampTxt1.substring(0, stampTxt1.length() -6);
		    	stampTxt2 = stampTxt2.substring(0, stampTxt2.length() -6);
	    	}

	    }
	  }catch (ArrayIndexOutOfBoundsException ai) {}
    return stampTxt1+" - "+stampTxt2;
  }

  public void setName(String name){
    setColumn(getNameColumnName(),name);
  }

  public Timestamp getFrom() {
    return (Timestamp) getColumnValue(getTimeframeFromColumnName());
  }

  public void setFrom(Timestamp timestamp) {
    setColumn(getTimeframeFromColumnName(), timestamp);
  }

  public Timestamp getTo() {
    return (Timestamp) getColumnValue(getTimeframeToColumnName());
  }

  public void setTo(Timestamp timestamp) {
    setColumn(getTimeframeToColumnName(), timestamp);
  }

  public boolean getIfYearly() {
    return getYearly();
  }

  public boolean getYearly() {
    return getBooleanColumnValue(getYearlyColumnName());
  }

  public void setIfYearly(boolean yearly) {
    setYearly(yearly);
  }

  public void setYearly(boolean yearly) {
    setColumn(getYearlyColumnName(),yearly);
  }

  public static String getTimeframeTableName(){return "TB_TIMEFRAME";}
  public static String getNameColumnName() {return "NAME";}
  public static String getTimeframeToColumnName() {return "TIMEFRAME_TO";}
  public static String getTimeframeFromColumnName() {return "TIMEFRAME_FROM";}
  public static String getYearlyColumnName() {return "YEARLY";}

}

