package se.idega.idegaweb.commune.childcare.check.data;

import com.idega.data.*;
import com.idega.block.process.data.*;

import javax.ejb.*;

import java.util.Collection;
import java.util.Iterator;
import java.rmi.RemoteException;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Anders Lindman
 * @version 1.0
 */

public class CheckBMPBean extends AbstractCaseBMPBean implements Check,Case{

  private static final String ENTITY_NAME="CC_CHECK";
  private static final String CASE_CODE_KEY="MBANCHK";
  private static final String CASE_CODE_DESCRIPTION="Request for child care check";

  private static final String COLUMN_CHILD_CARE_TYPE="CHILD_CARE_TYPE";
  private static final String COLUMN_WORK_SITUATION_1="WORK_SITUATION_1";
  private static final String COLUMN_WORK_SITUATION_2="WORK_SITUATION_2";
  private static final String COLUMN_MOTHER_TONGUE_MC="MOTHER_TONGUE_MC";
  private static final String COLUMN_MOTHER_TONGUE_FC="MOTHER_TONGUE_FC";
  private static final String COLUMN_MOTHER_TONGUE_P="MOTHER_TONGUE_P";
  private static final String COLUMN_CHILD_ID="CHILD_ID";
  private static final String COLUMN_METHOD="METHOD";
  private static final String COLUMN_AMOUNT="AMOUNT";
  private static final String COLUMN_CHECK_FEE="CHECK_FEE";

  public CheckBMPBean() {
  }

  public String getEntityName() {
    return ENTITY_NAME;
  }

  public void initializeAttributes(){
    //this.addAttribute(this.getIDColumnName());
    this.addManyToOneRelationship(getIDColumnName(),"Case ID",Case.class);
    this.getAttribute(getIDColumnName()).setAsPrimaryKey(true);

//    this.addAttribute(COLUMN_CHILD_CARE_TYPE,"Type of child care",Integer.class,MANY_TO_ONE,com.idega.block.school.data.SchoolType.class);
    this.addAttribute(COLUMN_CHILD_CARE_TYPE,"Type of child care",Integer.class);
    this.addAttribute(COLUMN_WORK_SITUATION_1,"Work situation for custodian 1",Integer.class);
    this.addAttribute(COLUMN_WORK_SITUATION_2,"Work situation for custodian 2",Integer.class);
    this.addAttribute(COLUMN_CHILD_ID,"Child id",Integer.class);
    this.addAttribute(COLUMN_MOTHER_TONGUE_MC,"Mother tongue Mother-Child",String.class);
    this.addAttribute(COLUMN_MOTHER_TONGUE_FC,"Mother tongue Father-Child",String.class);
    this.addAttribute(COLUMN_MOTHER_TONGUE_P,"Mother tongue Parents",String.class);
    this.addAttribute(COLUMN_METHOD,"Method used when applying for check (1 citizen, 2 quick)",Integer.class);
    this.addAttribute(COLUMN_AMOUNT,"Total check amount",Integer.class);
    this.addAttribute(COLUMN_CHECK_FEE,"The fee citizen pays",Integer.class);
//    this.addManyToManyRelationShip(SampleEntity.class);
  }

  public String getCaseCodeKey(){
    return CASE_CODE_KEY;
  }

  public String getCaseCodeDescription(){
    return CASE_CODE_DESCRIPTION;
  }

  /*
  public void insertStartData(){

    try{
      CheckHome home = (CheckHome)com.idega.data.IDOLookup.getHome(Check.class);
      Check check = home.create();
      check.setChildCareType(1);
      check.setWorkSituation1(2);
      check.setWorkSituation2(3);
      check.setChildId(4);
      check.setMethod(5);
      check.setAmount(2800);
      check.setAmount(1200);
      check.store();
    }
    catch(Exception e){
      e.printStackTrace(System.out);
    }
  }
  */

  public void setChildCareType(int type)throws java.rmi.RemoteException{
    this.setColumn(COLUMN_CHILD_CARE_TYPE,type);
  }

  public int getChildCareType()throws java.rmi.RemoteException{
    return this.getIntColumnValue(COLUMN_CHILD_CARE_TYPE);
  }

  public void setWorkSituation1(int type)throws java.rmi.RemoteException{
    this.setColumn(COLUMN_WORK_SITUATION_1,new Integer(type));
  }

  public int getWorkSituation1()throws java.rmi.RemoteException{
    return this.getIntColumnValue(COLUMN_WORK_SITUATION_1);
  }

  public void setWorkSituation2(int type)throws java.rmi.RemoteException{
    this.setColumn(COLUMN_WORK_SITUATION_2,new Integer(type));
  }

  public int getWorkSituation2()throws java.rmi.RemoteException{
    return this.getIntColumnValue(COLUMN_WORK_SITUATION_2);
  }

  public void setMotherTongueMotherChild(String s)throws java.rmi.RemoteException{
    this.setColumn(COLUMN_MOTHER_TONGUE_MC,s);
  }

  public String getMotherToungueMotherChild()throws java.rmi.RemoteException{
    return this.getStringColumnValue(COLUMN_MOTHER_TONGUE_MC);
  }

  public void setMotherTongueFatherChild(String s)throws java.rmi.RemoteException{
    this.setColumn(COLUMN_MOTHER_TONGUE_FC,s);
  }

  public String getMotherToungueFatherChild()throws java.rmi.RemoteException{
    return this.getStringColumnValue(COLUMN_MOTHER_TONGUE_FC);
  }

  public void setMotherTongueParents(String s)throws java.rmi.RemoteException{
    this.setColumn(COLUMN_MOTHER_TONGUE_P,s);
  }

  public String getMotherToungueParents()throws java.rmi.RemoteException{
    return this.getStringColumnValue(COLUMN_MOTHER_TONGUE_P);
  }

  public void setChildId(int id)throws java.rmi.RemoteException{
    this.setColumn(COLUMN_CHILD_ID,new Integer(id));
  }

  public int getChildId()throws java.rmi.RemoteException{
    return this.getIntColumnValue(COLUMN_CHILD_ID);
  }

  public void setMethod(int type)throws java.rmi.RemoteException{
    this.setColumn(COLUMN_METHOD,new Integer(type));
  }

  public int getMethod()throws java.rmi.RemoteException{
    return this.getIntColumnValue(COLUMN_METHOD);
  }

  public void setAmount(int amount)throws java.rmi.RemoteException{
    this.setColumn(COLUMN_AMOUNT,new Integer(amount));
  }

  public int getAmount()throws java.rmi.RemoteException{
    return this.getIntColumnValue(COLUMN_AMOUNT);
  }

  public void setCheckFee(int fee)throws java.rmi.RemoteException{
    this.setColumn(COLUMN_CHECK_FEE,new Integer(fee));
  }

  public int getCheckFee()throws java.rmi.RemoteException{
    return this.getIntColumnValue(COLUMN_CHECK_FEE);
  }

  public Collection ejbFindChecks()throws FinderException{
    return this.idoFindPKsBySQL("select * from "+this.getEntityName());
  }
}
