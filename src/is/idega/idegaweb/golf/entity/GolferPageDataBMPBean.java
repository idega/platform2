package is.idega.idegaweb.golf.entity;

import com.idega.data.GenericEntity;


/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class GolferPageDataBMPBean extends GenericEntity implements GolferPageData {
  public static final String MEMBER_ID = "MEMBER_ID";
  public static final String NEWS_READER_ID = "NEWS_READER_ID";
  public static final String PROFILE_ID = "PROFILE_ID";
  public static final String GOLFBAG_ID = "GOLFBAG_ID";
  public static final String RESULTS_ABROAD_ID = "RESULTS_ABROAD_ID";
  public static final String STATISTICS_ID = "STATISTICS_ID";
  public static final String SUPPORTERS_ID = "SUPPORTERS_ID";
  public static final String SUPPORTER_PRE_SIGNING_ID = "SUPPORTER_PRE_SIGNING_ID";

  public void initializeAttributes() {
    this.addAttribute( MEMBER_ID, "member_id", true, false, Integer.class, "one_to_many", is.idega.idegaweb.golf.entity.UnionMemberInfo.class);
    this.addAttribute( NEWS_READER_ID, "news_reader_id", true, false, Integer.class);
    this.addAttribute( PROFILE_ID, "profile_id", true, false, Integer.class);
    this.addAttribute( GOLFBAG_ID, "golfbag_id", true, false, Integer.class);
    this.addAttribute( RESULTS_ABROAD_ID, "results_abroad_id", true, false, Integer.class);
    this.addAttribute( STATISTICS_ID, "statistics_id", true, false, Integer.class);
    this.addAttribute( SUPPORTERS_ID, "supporters_id", true, false, Integer.class);
    this.addAttribute( SUPPORTER_PRE_SIGNING_ID, "supporter_pre_signing_id", true, false, Integer.class);
  }

  public String getEntityName() {
    return "golfer_page_data";
  }

  public void setMemberID (int memberID){
    this.setColumn(MEMBER_ID, new Integer(memberID));
  }

  public void setNewsReaderID (int newsReaderID){
    this.setColumn(NEWS_READER_ID, new Integer(newsReaderID));
  }

  public void setProfilerID (int profileID){
    this.setColumn(PROFILE_ID, new Integer(profileID));
  }

  public void setGolfbagID (int golfbagID){
    this.setColumn(GOLFBAG_ID, new Integer(golfbagID));
  }

  public void setResultsAbroadID (int resultsAbroadID){
    this.setColumn(RESULTS_ABROAD_ID, new Integer(resultsAbroadID));
  }

  public void setStatisticsID (int statisticsID){
    this.setColumn(STATISTICS_ID, new Integer(statisticsID));
  }

  public void setSupportesID (int supportesID){
    this.setColumn(SUPPORTERS_ID, new Integer(supportesID));
  }

  public void setSupportPreSigningID (int supportPreSigningID){
    this.setColumn(SUPPORTER_PRE_SIGNING_ID, new Integer(supportPreSigningID));
  }

  public int getMemberID (){
    return this.getIntColumnValue(MEMBER_ID);
  }

  public int getNewsReaderID (){
    return this.getIntColumnValue(NEWS_READER_ID);
  }

  public int getProfilerID (){
    return this.getIntColumnValue(PROFILE_ID);
  }

  public int getGolfbagID (){
    return this.getIntColumnValue(GOLFBAG_ID);
  }

  public int getResultsAbroadID (){
    return this.getIntColumnValue(RESULTS_ABROAD_ID);
  }

  public int getStatisticsID (){
    return this.getIntColumnValue(STATISTICS_ID);
  }

  public int getSupportesID (){
    return this.getIntColumnValue(SUPPORTERS_ID);
  }

  public int getSupportPreSigningID (){
    return this.getIntColumnValue(SUPPORTER_PRE_SIGNING_ID);
  }
}