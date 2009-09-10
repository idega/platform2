package com.idega.block.finance.data;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.user.data.User;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class AccountInfoBMPBean extends com.idega.data.GenericEntity implements AccountInfo,FinanceAccount {

  /*
   
CREATE VIEW "FIN_ACCOUNT_INFO" (
  "ACCOUNT_ID",
  "CATEGORY_ID",
  "USER_ID",
  "NAME",
  "LAST_UPDATED",
  "BALANCE",
  "ACCOUNT_TYPE"
) AS
select
ac.fin_account_id account_id,
ac.ic_category_id category_id,
ac.ic_user_id user_id,
ac.name,
max(e.last_updated) last_updated,
sum(e.total ) balance,
ac.account_type
from fin_account ac,fin_acc_entry e
where e.fin_account_id = ac.fin_account_id
group by
ac.fin_account_id,
ac.ic_category_id ,
ac.ic_user_id,
ac.name,
ac.account_type
*/

  public AccountInfoBMPBean() {

  }

  public AccountInfoBMPBean(int id)throws SQLException{

  }

  public static String getEntityTableName(){ return "FIN_ACCOUNT_INFO";}
  public static String getColumnAccountId(){return  "ACCOUNT_ID";}
  public static String getColumnCategoryId(){return  "CATEGORY_ID";}
  public static String getColumnUserId(){ return "USER_ID";}
  public static String getColumnCashierId(){ return "CASHIER_ID";}
  public static String getColumnName(){ return "NAME";}
  public static String getColumnLastUpdated(){ return "LAST_UPDATED";}
  public static String getColumnBalance(){ return "BALANCE";}
  public static String getColumnType(){return "ACCOUNT_TYPE";}

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnAccountId(),"Accountid",true,true,Integer.class);
    addAttribute(getColumnCategoryId(),"Category",true,true,Integer.class);
    //addAttribute(getColumnUserId(),"UserId",true,true,Integer.class);
   // addAttribute(getColumnCashierId(),"Cashier",true,true,Integer.class);
    addAttribute(getColumnName(),"Name",true,true,String.class);
    addAttribute(getColumnLastUpdated(),"LastUpdated",true,true,Timestamp.class);
    addAttribute(getColumnBalance(),"Balance",true,true,Float.class);
    addAttribute(getColumnType(),"Type",true,true,String.class);
    setAsPrimaryKey(getIDColumnName(),true);
    
    addManyToOneRelationship(getColumnUserId(), User.class);
  }
  
  public String getIDColumnName(){
  	return  getColumnAccountId();
  }

  public String getEntityName(){
    return getEntityTableName();
  }

  public Integer getAccountId(){
    return getIntegerColumnValue( getColumnAccountId() );
  }
  public int getCategoryId(){
    return getIntColumnValue( getColumnCategoryId() );
  }
   public int getUserId(){
    return getIntColumnValue( getColumnUserId() );
  }

   public User getUser(){
	    return (User) getColumnValue( getColumnUserId() );
	  }

   
   public int getCashierId(){
    return getIntColumnValue( getColumnCashierId() );
  }
  public String getName(){
    return getStringColumnValue(getColumnName());
  }
  public String getAccountType(){
    return getStringColumnValue(getColumnType());
  }
  public Timestamp getLastUpdated(){
    return (Timestamp) getColumnValue(getColumnLastUpdated());
  }
  public float getBalance(){
    return getFloatColumnValue(getColumnBalance());
  }
  public String getAccountName(){
    return getName();
  }
  
  public Collection ejbFindByOwner(Integer ownerID)throws FinderException{
  	return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getColumnUserId(),ownerID));
  }
  
  
  public Collection ejbFindByOwnerAndType(Integer ownerID,String type)throws FinderException{
  	return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getColumnUserId(),ownerID).appendAndEquals(getColumnType(),type));
  }
  
  public Collection ejbFindByAssessmentRound(Integer roundID)throws FinderException{
  		return ejbFindByAssessmentRound(roundID,-1,-1);
  }
  
  public Collection ejbFindByAssessmentRound(Integer roundID,int resultSize,int startindex)throws FinderException{
  	return super.idoFindPKsBySQL(getByRoundSQL(roundID),resultSize,startindex);
  }
  
  private String getByRoundSQL(Integer roundID){
  	StringBuffer sql = new StringBuffer("select distinct a.* ");
    sql.append(" from fin_account_info a,fin_acc_entry e ");
    sql.append(" where a.account_id = e.fin_account_id ");
    sql.append(" and e.fin_assessment_round_id = ");
    sql.append(roundID.toString());
    return sql.toString();
  }

	/* (non-Javadoc)
	 * @see com.idega.data.CategoryEntity#setCategoryId(int)
	 */
	public void setCategoryId(int p0) {
		
	}
}
