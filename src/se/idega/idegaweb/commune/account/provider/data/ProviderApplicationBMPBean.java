package se.idega.idegaweb.commune.account.provider.data;

import com.idega.data.*;
import com.idega.block.process.data.*;

/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      idega software
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class ProviderApplicationBMPBean extends AbstractCaseBMPBean implements ProviderApplication,Case{

  private static final String CASE_CODE_KEY="ANANSKO";
  private static final String TABLE_NAME="COMM_ACC_PROV";
  private static final String COLUMN_EMAIL="PROV_EMAIL";
  private static final String COLUMN_NAME="PROV_NAME";
  private static final String COLUMN_ADDRESS="PROV_ADDRESS";
  private static final String COLUMN_PLACES="PROV_PLACES";
  private static final String COLUMN_PHONE="PROV_PHONE";
  private static final String COLUMN_ADDITIONAL_INFO="PROV_ADD_INFO";
  private static final String COLUMN_MANAGER_NAME="PROV_MANAGER_NAME";

  public void initializeAttributes() {
    addGeneralCaseRelation();

    this.addAttribute(COLUMN_NAME,"Provider name",String.class);
    this.addAttribute(COLUMN_ADDRESS,"Provider address",String.class);
    this.addAttribute(COLUMN_PHONE,"Telephone",String.class,30);
    this.addAttribute(COLUMN_MANAGER_NAME,"Manager Name",String.class);
    this.addAttribute(COLUMN_EMAIL,"Manager email",String.class);
    this.addAttribute(COLUMN_PLACES,"Provider places",Integer.class);
    this.addAttribute(COLUMN_ADDITIONAL_INFO,"Additional info",String.class,2000);
  }
  public String getEntityName() {
    return this.TABLE_NAME;
  }
  public String getCaseCodeKey() {
    return CASE_CODE_KEY;
  }
  public String getCaseCodeDescription() {
    return "Application for School Administrators";
  }


  public String getEmailAddress(){
    return this.getStringColumnValue(COLUMN_EMAIL);
  }

  public void setEmailAddress(String emailAddress){
    this.setColumn(COLUMN_EMAIL,emailAddress);
  }


  public String getName(){
    return this.getStringColumnValue(COLUMN_NAME);
  }

  public void setName(String providerName){
    this.setColumn(COLUMN_NAME,providerName);
  }

  public String getAddress(){
    return this.getStringColumnValue(COLUMN_ADDRESS);
  }

  public void setAddress(String providerAddress){
    this.setColumn(COLUMN_ADDRESS,providerAddress);
  }

  public int getNumberOfPlaces(){
    return this.getIntColumnValue(COLUMN_PLACES);
  }

  public void setNumberOfPlaces(int places){
    this.setColumn(COLUMN_PLACES,places);
  }

  public String getManagerName(){
    return this.getStringColumnValue(COLUMN_MANAGER_NAME);
  }

  public void setManagerName(String name){
    this.setColumn(COLUMN_MANAGER_NAME,name);
  }

  public String getAdditionalInfo(){
    return this.getStringColumnValue(COLUMN_ADDITIONAL_INFO);
  }

  public void setAdditionalInfo(String info){
    this.setColumn(COLUMN_ADDITIONAL_INFO,info);
  }
}