package com.idega.block.finance.data;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.SumColumn;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.util.IWTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class AccountEntryBMPBean extends com.idega.data.GenericEntity implements com.idega.block.finance.data.AccountEntry,com.idega.block.finance.data.Entry {
  public static final String statusCreated = "C";
  public static final String statusBilled = "B";
  public static final String statusPayed = "P";

  public AccountEntryBMPBean() {
    super();
  }
  public AccountEntryBMPBean(int id)throws SQLException{
    super(id);
  }
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getAccountIdColumnName(), "Account", true, true, java.lang.Integer.class,"many-to-one",com.idega.block.finance.data.Account.class);
    addAttribute(getNameColumnName(),"Name",true,true,java.lang.String.class);
    addAttribute(getInfoColumnName(),"Info",true,true,java.lang.String.class);
    addAttribute(getAccountKeyIdColumnName(),"Account key",true,true,java.lang.Integer.class,"many-to-one",com.idega.block.finance.data.AccountKey.class);
    addAttribute(getEntryGroupIdColumnName(),"Entry group",true,true,java.lang.Integer.class,"many-to-one",com.idega.block.finance.data.EntryGroup.class);
    addAttribute(getEntryTypeColumnName(),"Entry type",true,true,java.lang.String.class);
    addAttribute(getColumnNetto(), "Netto", true, true, java.lang.Float.class);
    addAttribute(getColumnVAT(), "VAT", true, true, java.lang.Float.class);
    addAttribute(getColumnTotal(), "Total", true, true, java.lang.Float.class);
    addAttribute(getPaymentDateColumnName(),"Payment date",true,true,java.sql.Timestamp.class);
    addAttribute(getLastUpdatedColumnName(),"Last updated",true,true,java.sql.Timestamp.class);
    addAttribute(getCashierIdColumnName(),"Cashier",true,true,java.lang.Integer.class,"many-to-one",com.idega.block.finance.data.Cashier.class);
    addAttribute(getRoundIdColumnName(),"Round",true,true,java.lang.Integer.class,"many-to-one",com.idega.block.finance.data.AssessmentRound.class);
    addAttribute(getColumnNameStatus(),"status",true,true,String.class);
  }

  public static String getEntityTableName(){ return "FIN_ACC_ENTRY"; }
  public static String getRoundIdColumnName(){ return "FIN_ASSESSMENT_ROUND_ID"; }
  public static String getEntryGroupIdColumnName(){ return "FIN_ENTRY_GROUP_ID"; }
  public static String getAccountIdColumnName(){ return "FIN_ACCOUNT_ID"; }
  public static String getCashierIdColumnName(){ return "FIN_CASHIER_ID"; }
  public static String getAccountKeyIdColumnName(){ return "FIN_ACC_KEY_ID"; }
  public static String getEntryTypeColumnName(){ return "ENTRY_TYPE"; }
  public static String getNameColumnName(){ return "NAME"; }
  public static String getInfoColumnName(){ return "INFO"; }
  public static String getColumnTotal(){ return "TOTAL"; }
  public static String getColumnVAT(){return "VAT";}
  public static String getColumnNetto() {return "NETTO";}
  public static String getPaymentDateColumnName(){ return "PAYMENT_DATE"; }
  public static String getLastUpdatedColumnName(){ return "LAST_UPDATED"; }
  public static String getColumnNameStatus(){ return "STATUS"; }


  public String getEntityName() {
    return getEntityTableName();
  }
  public int getAccountId(){
    return getIntColumnValue(getAccountIdColumnName());
  }
  public void setAccountId(Integer account_id){
    setColumn(getAccountIdColumnName(), account_id);
  }
  public void setAccountId(int account_id){
    setColumn(getAccountIdColumnName(), account_id);
  }
  public int getEntryGroupId(){
    return getIntColumnValue(getEntryGroupIdColumnName());
  }
  public void setEntryGroupId(int entry_group_id){
    setColumn(getEntryGroupIdColumnName(), entry_group_id);
  }
  public String getEntryType(){
    return getStringColumnValue(getEntryTypeColumnName());
  }
  public void setEntryType(String entryType){
    setColumn(getEntryTypeColumnName(), entryType);
  }
  public int getAccountKeyId(){
    return getIntColumnValue(getAccountKeyIdColumnName());
  }
  public void setAccountKeyId(Integer account_key_id){
    setColumn(getAccountKeyIdColumnName(), account_key_id);
  }
  public void setAccountKeyId(int account_key_id){
    setColumn(getAccountKeyIdColumnName(), account_key_id);
  }
  public Timestamp getPaymentDate(){
    return (Timestamp) getColumnValue(getPaymentDateColumnName());
  }
  public void setPaymentDate(Timestamp payment_date){
    setColumn(getPaymentDateColumnName(), payment_date);
  }
  public Timestamp getLastUpdated(){
    return (Timestamp) getColumnValue(getLastUpdatedColumnName());
  }
  public void setLastUpdated(Timestamp last_updated){
    setColumn(getLastUpdatedColumnName(), last_updated);
  }
  public int getCashierId(){
    return getIntColumnValue(getCashierIdColumnName());
  }
  public void setCashierId(Integer member_id){
    setColumn(getCashierIdColumnName(), member_id);
  }
  public void setCashierId(int member_id){
    setColumn(getCashierIdColumnName(), member_id);
  }
  public String getName(){
    return getStringColumnValue(getNameColumnName());
  }
  public void setName(String name){
    setColumn(getNameColumnName(), name );
  }
  public String getInfo(){
    return getStringColumnValue(getInfoColumnName());
  }
  public void setInfo(String info){
    setColumn(getInfoColumnName(), info);
  }

  public void setNetto(float netto){
    setColumn(getColumnNetto(), netto);
  }
  public float getNetto(){
    return getFloatColumnValue(getColumnNetto());
  }
  public void setPrice(Float netto){
    setColumn(getColumnNetto(), netto);
  }
  public void setPrice(float netto){
    setColumn(getColumnNetto(), netto);
  }
  public float getVAT(){
    return getFloatColumnValue(getColumnVAT());
  }
  public void setVAT(Float vat){
    setColumn(getColumnVAT(), vat);
  }
  public void setVAT(float vat){
    setColumn(getColumnVAT(), vat);
  }
  public float getTotal(){
    return getFloatColumnValue(getColumnTotal());
  }
  public void setTotal(Float total){
    setColumn(getColumnTotal(), total);
  }
  public void setTotal(float total){
    setColumn(getColumnTotal(), total);
  }
  public int getRoundId(){
    return getIntColumnValue(getRoundIdColumnName());
  }
  public void setRoundId(Integer round){
    setColumn(getRoundIdColumnName(), round);
  }
  public void setRoundId(int round){
    setColumn(getRoundIdColumnName(), round);
  }

  public String getStatus(){
    return getStringColumnValue( getColumnNameStatus());
  }

  public void setStatus(String status) throws IllegalStateException {
    if ((status.equalsIgnoreCase(statusCreated)) ||
        (status.equalsIgnoreCase(statusCreated)) ||
        (status.equalsIgnoreCase(statusBilled))){
      setColumn(getColumnNameStatus(),status);
      setLastUpdated(com.idega.util.IWTimestamp.getTimestampRightNow());
    }
    else
      throw new IllegalStateException("Undefined state : " + status);
  }

  // interface specific:
  public String getType(){
    return typeFinancial;
  }
  public String getFieldNameLastUpdated(){
    return getLastUpdatedColumnName();
  }
  public String getFieldNameAccountId(){
    return getAccountIdColumnName();
  }
  public String getTableName(){
    return getEntityTableName();
  }
  public String getFieldNameStatus(){
    return getColumnNameStatus();
  }
  
  public Collection ejbFindByAccountAndAssessmentRound(Integer accountID,Integer assessmentRoundID)throws FinderException{
  	return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getAccountIdColumnName(),accountID).appendAndEquals(getRoundIdColumnName(),assessmentRoundID));
  }
  
  public double ejbHomeGetTotalSumByAccountAndAssessmentRound(Integer accountID,Integer assessmentRoundID)throws SQLException{
  	StringBuffer sql = new StringBuffer();
  	sql.append("select sum(").append(getColumnTotal()).append(") from ");
  	sql.append(getEntityTableName()).append( " where ").append(getAccountIdColumnName()).append("=").append(accountID);
  	sql.append(" and ").append(getRoundIdColumnName()).append("=").append(assessmentRoundID);
  	//select sum(total) from fin_acc_entry where fin_account_id = 165 and fin_assessment_round_id = 3187
  	return (super.getDoubleTableValue(sql.toString()));
  }
  
  public double ejbHomeGetTotalSumByAccount(Integer accountID)throws SQLException{
  	 return ejbHomeGetTotalSumByAccount(accountID,null);
  	
  }
  
  public double ejbHomeGetTotalSumByAccount(Integer accountID,String roundStatus)throws SQLException{
  	try {
		Table entryTable = new Table(this);
		Table roundTable = new Table(AssessmentRound.class);
		SelectQuery query = new SelectQuery(entryTable);
		query.addColumn(new SumColumn(entryTable,getColumnTotal()));
		query.addCriteria(new MatchCriteria(entryTable.getColumn(getAccountIdColumnName()),MatchCriteria.EQUALS,accountID));
		if(roundStatus!=null){
			query.addJoin(entryTable,roundTable);
			query.addCriteria(new MatchCriteria(roundTable.getColumn(AssessmentRoundBMPBean.getStatusColumnName()),MatchCriteria.EQUALS,roundStatus,true));
		}
		
		/*
		
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(").append(getColumnTotal()).append(") from ");
		sql.append(getEntityTableName()).append( " where ").append(getAccountIdColumnName()).append("=").append(accountID);
		*/
		//select sum(total) from fin_acc_entry where fin_account_id = 165 and fin_assessment_round_id = 3187
		return (super.getDoubleTableValue(query.toString()));
	} catch (IDORelationshipException e) {
		throw new SQLException(e.getMessage());
	}
  }
  
  public double ejbHomeGetTotalSumByAssessmentRound(Integer roundID)throws SQLException{
  	StringBuffer sql = new StringBuffer();
  	sql.append("select sum(").append(getColumnTotal()).append(") from ");
  	sql.append(getEntityTableName()).append( " where ").append(getRoundIdColumnName()).append("=").append(roundID);  	
  	return (super.getDoubleTableValue(sql.toString()));
  }
  
  public Collection ejbFindByAssessmentRound(Integer assessmentRoundID)throws FinderException{
  	return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhereEquals(getRoundIdColumnName(),assessmentRoundID));
  }
  
  public Collection ejbFindByAccountAndStatus(Integer accountID,String status, Date fromDate,Date toDate,String assessmentStatus)throws FinderException{
		try {
			Table entryTable = new Table(this);
			Table roundTable = new Table(AssessmentRound.class);
			SelectQuery query = new SelectQuery(entryTable);
			query.addColumn(new WildCardColumn(entryTable));
			if(assessmentStatus!=null)
				query.addJoin(entryTable,roundTable);
			query.addCriteria(new MatchCriteria(entryTable,getFieldNameAccountId(),MatchCriteria.EQUALS,accountID));
			if(status!=null)
				query.addCriteria(new MatchCriteria(entryTable.getColumn(getColumnNameStatus()),MatchCriteria.EQUALS,status,true));
			
			if(fromDate!=null && toDate!=null){
				IWTimestamp from = new IWTimestamp(fromDate);
				IWTimestamp to = new IWTimestamp(toDate);
				to.setTime(23,59,59);
				query.addCriteria(new MatchCriteria(entryTable,getFieldNameLastUpdated(),MatchCriteria.GREATEREQUAL,from.getTimestamp()));
				query.addCriteria(new MatchCriteria(entryTable,getFieldNameLastUpdated(),MatchCriteria.LESSEQUAL,to.getTimestamp()));
			}
			if(assessmentStatus!=null)
				query.addCriteria(new MatchCriteria(roundTable.getColumn(AssessmentRoundBMPBean.getStatusColumnName()),MatchCriteria.EQUALS,assessmentStatus,true));
			return super.idoFindPKsBySQL(query.toString());
		} catch (IDORelationshipException e) {
			throw new FinderException(e.getMessage());
		}
}
  
  public int ejbHomeCountByGroup(Integer groupID)throws IDOException{
  	return super.idoGetNumberOfRecords(super.idoQueryGetSelectCount().appendWhereEquals(getEntryGroupIdColumnName(),groupID));
  }
  
  public Collection ejbFindUnGrouped(Date from,Date to)throws FinderException{
  	IDOQuery query = super.idoQueryGetSelect();
  	query.appendWhereIsNull(getEntryGroupIdColumnName());
  	query.appendAnd();
  	query.appendWithinDates(getLastUpdatedColumnName(),from,to);
    return super.idoFindPKsByQuery(query);
  }
  
  public Collection ejbFindByEntryGroup(Integer groupID)throws FinderException{
  		return super.idoFindPKsByQuery(super.idoQueryGetSelectCount().appendWhereEquals(getEntryGroupIdColumnName(),groupID));
  }
  
  public Date ejbHomeGetMaxDateByAccount(Integer accountID)throws IDOException{
  		IDOQuery query = super.idoQuery().appendSelect()
		.append( "max(").append(getLastUpdatedColumnName()).append(")");
  		query.appendFrom().append(getEntityName());
		query.appendWhereEquals(getAccountIdColumnName(),accountID);
  		try {
			return getDateTableValue(query.toString());
		} catch (SQLException e) {
			throw new IDOException(e.getMessage());
		}
  }

}

