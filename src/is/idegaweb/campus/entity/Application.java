/*
 * $Id: Application.java,v 1.3 2001/06/21 16:21:18 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.entity;

import com.idega.data.GenericEntity;
import com.idega.data.EntityAttribute;
import java.sql.SQLException;
import java.sql.Date;

/**
 * A specific application for the campus system.
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class Application extends GenericEntity {
  public static final String name_ = "cam_application";
  public static final String applicationId_ = "app_application_id";
  public static final String currentResidenceId_ = "cam_curr_res_id";
  public static final String spouseOccupationId_ = "cam_spouse_occ_id";
  public static final String studyBeginMonth_ = "study_begin_mo";
  public static final String studyBeginYear_ = "study_begin_yr";
  public static final String studyEndMonth_ = "study_end_mo";
  public static final String studyEndYear_ = "study_end_yr";
  public static final String faculty_ = "faculty";
  public static final String studyTrack_ = "study_track";
  public static final String spouseName_ = "spouse_name";
  public static final String spouseSSN_ = "spouse_ssn";
  public static final String spouseSchool_ = "spouse_school";
  public static final String spouseStudyTrack_ = "spouse_study_track";
  public static final String spouseStudyBeginMonth_ = "spouse_study_begin_mo";
  public static final String spouseStudyBeginYear_ = "spouse_study_begin_yr";
  public static final String spouseStudyEndMonth_ = "spouse_study_end_mo";
  public static final String spouseStudyEndYear_ = "spouse_study_end_yr";
  public static final String children_ = "children";
  public static final String income_ = "income";
  public static final String spouseIncome_ = "spouse_income";
  public static final String housingFrom_ = "housing_from";
  public static final String onWaitinglist_ = "on_waitinglist";
  public static final String wantFurniture_ = "want_furniture";
  public static final String contactPhone_ = "contact_phone";
  public static final String otherInfo_ = "other_info";
  public static final String email_ = "email";

  public Application() {
    super();
  }

  public Application(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(applicationId_,"Application",true,true,"java.lang.Integer","one-to-one","com.idega.block.application.data.Application");
    addAttribute(currentResidenceId_,"Current residency",true,true,"java.lang.Integer","one-to-many","is.idegaweb.campus.entity.CurrentResidency");
    addAttribute(spouseOccupationId_,"Spouse occupation",true,true,"java.lang.Integer","one-to-many","is.idegaweb.campus.entity.SpouseOccupation");
    addAttribute(studyBeginMonth_,"Study begins (month)",true,true,"java.lang.Integer");
    addAttribute(studyBeginYear_,"Study begins (year)",true,true,"java.lang.Integer");
    addAttribute(studyEndMonth_,"Study ends (month)",true,true,"java.lang.Integer");
    addAttribute(studyEndYear_,"Study ends (year)",true,true,"java.lang.Integer");
    addAttribute(faculty_,"Faculty",true,true,"java.lang.String");
    addAttribute(studyTrack_,"Study track",true,true,"java.lang.String");
    addAttribute(spouseName_,"Spouses name",true,true,"java.lang.String");
    addAttribute(spouseSSN_,"Spouses SSN",true,true,"java.lang.String");
    addAttribute(spouseSchool_,"Spouses school",true,true,"java.lang.String");
    addAttribute(spouseStudyTrack_,"Spouses study track",true,true,"java.lang.String");
    addAttribute(spouseStudyBeginMonth_,"Spouses study begins (month)",true,true,"java.lang.Integer");
    addAttribute(spouseStudyBeginYear_,"Spouses study begins (year)",true,true,"java.lang.Integer");
    addAttribute(spouseStudyEndMonth_,"Spouses study ends (month)",true,true,"java.lang.Integer");
    addAttribute(spouseStudyEndYear_,"Spouses study ends (year)",true,true,"java.lang.Integer");
    addAttribute(children_,"Children info",true,true,"java.lang.String");
    addAttribute(income_,"Income",true,true,"java.lang.Integer");
    addAttribute(spouseIncome_,"Spouses income",true,true,"java.lang.Integer");
    addAttribute(housingFrom_,"Want housing from",true,true,"java.lang.Date");
    addAttribute(onWaitinglist_,"Want to be on waiting list",true,true,"java.lang.String");
    addAttribute(wantFurniture_,"Want to rent furniture",true,true,"java.lang.String");
    addAttribute(contactPhone_,"If not reachable, call",true,true,"java.lang.String");
    addAttribute(otherInfo_,"Other info",true,true,"java.lang.String");
    addAttribute(email_,"Email",true,true,"java.lang.String");
    setMaxLength(faculty_,255);
    setMaxLength(studyTrack_,255);
    setMaxLength(spouseName_,255);
    setMaxLength(spouseSSN_,20);
    setMaxLength(spouseSchool_,255);
    setMaxLength(spouseStudyTrack_,255);
    setMaxLength(children_,4000);
    setMaxLength(onWaitinglist_,1);
    setMaxLength(wantFurniture_,1);
    setMaxLength(contactPhone_,40);
    setMaxLength(otherInfo_,4000);
    setMaxLength(email_,255);
    setNullable(currentResidenceId_,true);
    setNullable(spouseOccupationId_,true);
  }

  public String getEntityName() {
    return(name_);
  }

  public void setAppApplicationId(int id) {
    setColumn(applicationId_,id);
  }

  public void setAppApplicationId(Integer id) {
    setColumn(applicationId_,id);
  }

  public Integer getAppApplicationId() {
    return((Integer)getColumnValue(applicationId_));
  }

  public Integer getCurrentResidenceId() {
    return((Integer)getColumnValue(currentResidenceId_));
  }

  public Integer getSpouseOccupationId() {
    return((Integer)getColumnValue(spouseOccupationId_));
  }

  public Integer getStudyBeginMonth() {
    return((Integer)getColumnValue(studyBeginMonth_));
  }

  public Integer getStudyBeginYear() {
    return((Integer)getColumnValue(studyBeginYear_));
  }

  public Integer getStudyEndMonth() {
    return((Integer)getColumnValue(studyEndMonth_));
  }

  public Integer getStudyEndYear() {
    return((Integer)getColumnValue(studyEndYear_));
  }

  public String getFaculty() {
    return((String)getColumnValue(faculty_));
  }

  public String getStudyTrack() {
    return((String)getColumnValue(studyTrack_));
  }

  public String getSpouseName() {
    return((String)getColumnValue(spouseName_));
  }

  public String getSpouseSSN() {
    return((String)getColumnValue(spouseSSN_));
  }

  public String getSpouseSchool() {
    return((String)getColumnValue(spouseSchool_));
  }

  public String getSpouseStudyTrack() {
    return((String)getColumnValue(spouseStudyTrack_));
  }

  public Integer getSpouseStudyBeginMonth() {
    return((Integer)getColumnValue(spouseStudyBeginMonth_));
  }

  public Integer getSpouseStudyBeginYear() {
    return((Integer)getColumnValue(spouseStudyBeginYear_));
  }

  public Integer getSpouseStudyEndMonth() {
    return((Integer)getColumnValue(spouseStudyEndMonth_));
  }

  public Integer getSpouseStudyEndYear() {
    return((Integer)getColumnValue(spouseStudyEndYear_));
  }

  public String getChildren() {
    return((String)getColumnValue(children_));
  }

  public Integer getIncome() {
    return((Integer)getColumnValue(income_));
  }

  public Integer getSpouseIncome() {
    return((Integer)getColumnValue(spouseIncome_));
  }

  public Date getHousingFrom() {
    return((Date)getColumnValue(housingFrom_));
  }

  public boolean getOnWaitinglist() {
    String tmp = (String)getColumnValue(onWaitinglist_);
    if (tmp.equalsIgnoreCase("y"))
      return(true);
    else
      return(false);
  }

  public boolean getWantFurniture() {
    String tmp = (String)getColumnValue(wantFurniture_);
    if (tmp.equalsIgnoreCase("y"))
      return(true);
    else
      return(false);
  }

  public String getContactPhone() {
    return((String)getColumnValue(contactPhone_));
  }

  public String getOtherInfo() {
    return((String)getColumnValue(otherInfo_));
  }

  public String getEmail() {
    return((String)getColumnValue(email_));
  }

  public void setCurrentResidenceId(Integer id) {
    setColumn(currentResidenceId_,id);
  }

  public void setCurrentResidenceId(int id) {
    setColumn(currentResidenceId_,id);
  }

  public void setSpouseOccupationId(Integer id) {
    setColumn(spouseOccupationId_,id);
  }

  public void setSpouseOccupationId(int id) {
    setColumn(spouseOccupationId_,id);
  }

  public void setStudyBeginMonth(Integer month) {
    setColumn(studyBeginMonth_,month);
  }

  public void setStudyBeginMonth(int month) {
    setColumn(studyBeginMonth_,month);
  }

  public void setStudyBeginYear(Integer year) {
    setColumn(studyBeginYear_,year);
  }

  public void setStudyBeginYear(int year) {
    setColumn(studyBeginYear_,year);
  }

  public void setStudyEndMonth(Integer month) {
    setColumn(studyEndMonth_,month);
  }

  public void setStudyEndMonth(int month) {
    setColumn(studyEndMonth_,month);
  }

  public void setStudyEndYear(Integer year) {
    setColumn(studyEndYear_,year);
  }

  public void setStudyEndYear(int year) {
    setColumn(studyEndYear_,year);
  }

  public void setFaculty(String faculty) {
    setColumn(faculty_,faculty);
  }

  public void setStudyTrack(String studyTrack) {
    setColumn(studyTrack_,studyTrack);
  }

  public void setSpouseName(String spouseName) {
    setColumn(spouseName_,spouseName);
  }

  public void setSpouseSSN(String ssn) {
    setColumn(spouseSSN_,ssn);
  }

  public void setSpouseSchool(String school) {
    setColumn(spouseSchool_,school);
  }

  public void setSpouseStudyTrack(String studyTrack) {
    setColumn(spouseStudyTrack_,studyTrack);
  }

  public void setSpouseStudyBeginMonth(Integer month) {
    setColumn(spouseStudyBeginMonth_,month);
  }

  public void setSpouseStudyBeginMonth(int month) {
    setColumn(spouseStudyBeginMonth_,month);
  }

  public void setSpouseStudyBeginYear(Integer year) {
    setColumn(spouseStudyBeginYear_,year);
  }

  public void setSpouseStudyBeginYear(int year) {
    setColumn(spouseStudyBeginYear_,year);
  }

  public void setSpouseStudyEndMonth(Integer month) {
    setColumn(spouseStudyEndMonth_,month);
  }

  public void setSpouseStudyEndMonth(int month) {
    setColumn(spouseStudyEndMonth_,month);
  }

  public void setSpouseStudyEndYear(Integer year) {
    setColumn(spouseStudyEndYear_,year);
  }

  public void setSpouseStudyEndYear(int year) {
    setColumn(spouseStudyEndYear_,year);
  }

  public void setChildren(String children) {
    setColumn(children_,children);
  }

  public void setIncome(Integer income) {
    setColumn(income_,income);
  }

  public void setIncome(int income) {
    setColumn(income_,income);
  }

  public void setSpouseIncome(Integer income) {
    setColumn(spouseIncome_,income);
  }

  public void setSpouseIncome(int income) {
    setColumn(spouseIncome_,income);
  }

  public void setHousingFrom(Date from) {
    setColumn(housingFrom_,from);
  }

  public void setOnWaitinglist(boolean putOnList) {
    if (putOnList)
      setColumn(onWaitinglist_,"Y");
    else
      setColumn(onWaitinglist_,"N");
  }

  public void setWantFurniture(boolean furniture) {
    if (furniture)
      setColumn(wantFurniture_,"Y");
    else
      setColumn(wantFurniture_,"N");
  }

  public void setContactPhone(String contactPhone) {
    setColumn(contactPhone_,contactPhone);
  }

  public void setOtherInfo(String info) {
    setColumn(otherInfo_,info);
  }

  public void setEmail(String email) {
    setColumn(email_,email);
  }
}