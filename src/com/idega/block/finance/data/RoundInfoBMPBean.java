package com.idega.block.finance.data;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.ejb.FinderException;

import com.idega.data.IDOQuery;

/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class RoundInfoBMPBean extends com.idega.data.GenericEntity implements com.idega.block.finance.data.RoundInfo {
 /*
 CREATE VIEW FIN_ROUND_INFO(
    ROUND_ID,
    GROUP_ID,
    NAME,
    ROUND_STAMP,
    CATEGORY_ID,
    NETTO,
    TOTALS,
    ACCOUNTS,
    STATUS)
AS

select
ass.fin_assessment_round_id round_id,
ass.fin_tariff_group_id group_id,
ass.name,
ass.round_stamp,
ass.ic_category_id category_id,
sum(e.netto ) netto,
sum(e.total ) totals,
count(distinct e.fin_account_id) accounts,
ass.status
from fin_assessment_round ass,fin_acc_entry e
where e.fin_assessment_round_id = ass.fin_assessment_round_id
group by
ass.fin_assessment_round_id,
ass.fin_tariff_group_id,
ass.name,
ass.round_stamp,
ass.ic_category_id,
ass.status
*/
  public static String getEntityTableName(){return "FIN_ROUND_INFO";}
  public static String getColumnRoundId(){return "ROUND_ID";}
  public static String getColumnGroupId(){return "GROUP_ID";}
  public static String getColumnName(){return "NAME";}
  public static String getColumnRoundStamp(){return "ROUND_STAMP";}
  public static String getColumnCategoryId(){return "CATEGORY_ID";}
  public static String getColumnNetto(){return "NETTO";}
  public static String getColumnTotals(){return "TOTALS";}
  public static String getColumnAccounts(){return "ACCOUNTS";}
  public static String getColumnStatus(){return "STATUS";}

  public RoundInfoBMPBean() {
  }
  public RoundInfoBMPBean(int id) throws SQLException {

  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnRoundId(),"Round id",true,true,java.lang.Integer.class);
    addAttribute(getColumnGroupId(),"Group id",true,true,java.lang.Integer.class);
    addAttribute(getColumnName(),"Name",true,true,java.lang.String.class);
    addAttribute(getColumnRoundStamp(),"Stamp",true,true,Timestamp.class);
    addAttribute(getColumnCategoryId(),"Category",true,true,java.lang.Integer.class);
    addAttribute(getColumnNetto(),"Netto",true,true,java.lang.Float.class);
    addAttribute(getColumnTotals(),"Totals",true,true,java.lang.Float.class);
    addAttribute(getColumnAccounts(),"Accounts",true,true,java.lang.Integer.class);
    addAttribute(getColumnStatus(),"Status",true,true,java.lang.String.class);
    setAsPrimaryKey(getIDColumnName(),true);

  }
  
  public String getIDColumnName(){
  	return getColumnRoundId();
  }
  public String getEntityName() {
    return(getEntityTableName());
  }
  public int getRoundId(){
   return getIntColumnValue(getColumnRoundId());
  }
  public int getGroupId(){
   return getIntColumnValue(getColumnGroupId());
  }
  public int getAccounts(){
    return getIntColumnValue(getColumnAccounts());
  }
  public float getTotals(){
    return getFloatColumnValue(getColumnTotals());
  }
  public float getNetto(){
    return getFloatColumnValue(getColumnNetto());
  }
  public String getName(){
    return getStringColumnValue(getColumnName());
  }
  public int getCategoryId(){
    return getIntColumnValue(getColumnCategoryId());
  }

  public Timestamp getRoundStamp() {
    return((Timestamp)getColumnValue(getColumnRoundStamp()));
  }

  public String getStatus(){
    return getStringColumnValue(getColumnStatus());
  }
  
  public java.util.Collection ejbFindByCategoryAndGroup(Integer categoryID,Integer groupID,Date from, Date to)throws FinderException{
  		IDOQuery query = idoQueryGetSelect();
  		query.appendWhereEquals(getColumnCategoryId(),categoryID);
  		query.appendAndEquals(getColumnGroupId(),groupID);
  		query.appendAnd();
  		query.appendWithinDates(getColumnRoundStamp(),from,to);
  		query.appendOrderBy(getColumnRoundStamp());
  		System.out.println(query.toString());
  		return super.idoFindPKsByQuery(query);
  }

  public void insert()throws SQLException{

  }
  public void delete()throws SQLException{

  }
}

