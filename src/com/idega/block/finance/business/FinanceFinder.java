package com.idega.block.finance.business;

import com.idega.block.finance.data.*;
import com.idega.data.GenericEntity;
import com.idega.core.data.ICObjectInstance;
import java.sql.SQLException;
import java.util.*;
import com.idega.util.idegaCalendar;
import com.idega.util.idegaTimestamp;
import com.idega.data.EntityFinder;
import com.idega.data.IDOFinderException;
import java.sql.SQLException;
import com.idega.core.user.data.User;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class FinanceFinder  {

  private static FinanceFinder instance;

  public static FinanceFinder getInstance(){
    if(instance==null){
        instance = new FinanceFinder();
    }
    return instance;
  }

  public int getObjectInstanceCategoryId(int iObjectInstanceId,boolean CreateNew){
    int id = -1;
    try {
      ICObjectInstance obj = new ICObjectInstance(iObjectInstanceId);
      id = getObjectInstanceCategoryId(obj);
      if(id <= 0 && CreateNew ){
        id = FinanceBusiness.createCategory(iObjectInstanceId );
      }
    }
    catch (Exception ex) {

    }
    return id;
  }

  public int getObjectInstanceCategoryId(ICObjectInstance eObjectInstance){
    try {
      List L = EntityFinder.findRelated(eObjectInstance ,new FinanceCategory());
      if(L!= null){
        return ((FinanceCategory) L.get(0)).getID();
      }
      else
        return -1;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return -2;
    }
  }

  public  int getObjectInstanceCategoryId(int iObjectInstanceId){
    try {
      ICObjectInstance obj = new ICObjectInstance(iObjectInstanceId);
      return getObjectInstanceCategoryId(obj);
    }
    catch (Exception ex) {

    }
    return -1;
  }

  public  int getObjectInstanceIdFromCategoryId(int iCategoryId){
    try {
      FinanceCategory cat = new FinanceCategory(iCategoryId);
      List L = EntityFinder.findRelated( cat,new ICObjectInstance());
      if(L!= null){
        return ((ICObjectInstance) L.get(0)).getID();
      }
      else
        return -1;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return -2;
    }
  }


  public  FinanceCategory getFinanceCategory(int iCategoryId){
    if( iCategoryId > 0){
      try {
          return new FinanceCategory(iCategoryId );
      }
      catch (SQLException ex) {

      }
    }
    return null;
  }



  public  List listOfEntityForObjectInstanceId(int instanceid){
    try {
      ICObjectInstance obj = new ICObjectInstance(instanceid );
      return listOfEntityForObjectInstanceId(obj);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public  List listOfEntityForObjectInstanceId( ICObjectInstance obj){
    try {
      List L = EntityFinder.findRelated(obj,new FinanceCategory());
      return L;
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public  List listOfCategories(){
    try {
      return EntityFinder.findAll(new FinanceCategory());
    }
    catch (SQLException ex) {
    }
    return null;
  }

   public  List listOfTariffGroups(){
    try {
      return EntityFinder.findAll(new TariffGroup());
    }
    catch (SQLException ex) {
    }
    return null;
  }

  public  List listOfFinanceHandlers(){
    try {
      return EntityFinder.findAll(new FinanceHandlerInfo());
    }
    catch (SQLException ex) {
    }
    return null;
  }

  public  List listOfAccounts(){
    return null;
  }

  public  List listOfTariffKeys(int iCategoryId){
    try {
      return EntityFinder.findAllByColumn(new TariffKey(),TariffKey.getColumnCategoryId(),iCategoryId);
    }
    catch (SQLException ex) {
    }
    return null;
  }


  public  List listOfPaymentTypes(int iCategoryId){
    try {
      return EntityFinder.findAllByColumn(new PaymentType(),PaymentType.getColumnCategoryId(),iCategoryId);
    }
    catch (SQLException ex) {
    }
    return null;
  }

  public  List listOfAssessments(int iTariffGroupId){
    try {
      return EntityFinder.findAllByColumn(new AssessmentRound(),AssessmentRound.getColumnTariffGroupId(),iTariffGroupId);
    }
    catch (SQLException ex) {
    }
    return null;
  }

  public  List listOfAssessmentInfo(int iCategory,int iTariffGroupId){
    try {
      StringBuffer sql = new StringBuffer("select * from ");
      sql.append(RoundInfo.getEntityTableName());
      if(iCategory > 0 || iTariffGroupId >0 )
      sql.append(" where ");
      if(iCategory > 0)
        sql.append(RoundInfo.getColumnCategoryId()).append(" = ").append(iCategory);
      if(iTariffGroupId >0)
        sql.append(" and ").append(RoundInfo.getColumnGroupId()).append(" = ").append(iTariffGroupId);

      //System.err.println(sql.toString());
      return EntityFinder.getInstance().findAll(RoundInfo.class,sql.toString());
    }
    catch (com.idega.data.IDOFinderException ex) {
      ex.printStackTrace();
    }
    return null;
  }


  public  Map mapOfTariffKeys(int iCategoryId){
    Hashtable h = new Hashtable();
    List TK = listOfTariffKeys(iCategoryId);
    if(TK != null){
      int len = TK.size();
      for (int i = 0; i < len; i++) {
        TariffKey T = (TariffKey) TK.get(i);
        h.put(new Integer(T.getID()),T.getName());
      }
    }
    return h;

  }

    public  List listOfAccountKeys(int iCategoryId){

      try{
       return EntityFinder.findAllByColumn(new AccountKey(),AccountKey.getColumnCategoryId(),iCategoryId);
      }
      catch(SQLException e){

      }
      return null;
  }

  public  List listOfTariffs(int iGroupId){
     try{
       return EntityFinder.findAllByColumn(new Tariff(),Tariff.getColumnTariffGroup(),iGroupId);
      }
      catch(SQLException e){

      }
      return null;
  }

  public  List listOfTariffsByAttribute(String attribute){
     try{
      List L =  EntityFinder.findAllByColumn(new Tariff(),Tariff.getColumnAttribute(),attribute);
      return L;
      }
      catch(SQLException e){

      }
      return null;
  }

  public  List listOfTariffGroups(int iCategoryId){
     try{
       return EntityFinder.findAllByColumn(new TariffGroup(),TariffGroup.getColumnCategoryId(),iCategoryId);
      }
      catch(SQLException e){

      }
      return null;
  }

   public  List listOfTariffGroupsWithHandlers(int iCategoryId){
     try{
        StringBuffer sql = new StringBuffer("select * from ");
        sql.append(TariffGroup.getEntityTableName());
        sql.append(" where ");
        sql.append(TariffGroup.getColumnHandlerId());
        sql.append(" > 0 ");
       return EntityFinder.findAll(new TariffGroup(),sql.toString());
      }
      catch(SQLException e){

      }
      return null;
  }

  public  FinanceHandler getFinanceHandler(int iHandlerId){
    try {
      FinanceHandlerInfo handler = new FinanceHandlerInfo(iHandlerId);
      if(handler.getClassName()!=null)
        try{
          return (FinanceHandler) Class.forName(handler.getClassName()).newInstance();
        }
        catch(Exception ex){
          ex.printStackTrace();
        }
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public  TariffGroup getTariffGroup(int iGroupId){
    if(iGroupId > 0){
      try {
        return new TariffGroup(iGroupId);
      }
      catch (SQLException ex) {

      }
    }
    return null;
  }

  public  int countAccounts(int iCategory,String type){
    try {
      StringBuffer sql = new StringBuffer("select count(*) from fin_account a");
      sql.append(" where a.");
      sql.append(Account.getTypeColumnName());
      sql.append(" = '");
      sql.append(type);
      sql.append("' and a.");
      sql.append(Account.getColumnCategoryId());
      sql.append(" = ");
      sql.append(iCategory);
      return new Account().getNumberOfRecords(sql.toString());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return 0;
  }

  public  List searchAccounts(String id,String first,String middle,String last,String type,int iCategoryId){
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append(Account.getEntityTableName());
    sql.append(" a,ic_user u ");
    sql.append(" where a.ic_user_id = u.ic_user_id");
    sql.append(" and a.");
    sql.append(Account.getColumnCategoryId());
    sql.append(" = ");
    sql.append(iCategoryId);
    if(id !=null && !"".equals(id)){
      sql.append(" and a.name like '");
      sql.append(id);
      sql.append("' ");
    }
    if(first !=null && !"".equals(first )){
      sql.append(" and u.first_name like '");
      sql.append(first);
      sql.append("' ");
    }
    if(middle !=null && !"".equals(middle)){
      sql.append(" and u.middle_name like '");
      sql.append(middle);
      sql.append("' ");
    }
    if(last !=null && !"".equals(last)){
      sql.append(" and u.last like '");
      sql.append(last);
      sql.append("' ");
    }
    if(type !=null && !"".equals(type)){
      sql.append(" and a.account_type like '");
      sql.append(type);
      sql.append("' ");
    }
    //System.err.println(sql.toString());
    try {
      return EntityFinder.findAll(new Account(),sql.toString());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return null;

  }

   public  List searchAccountUsers(String first,String middle,String last){
    //System.err.println("names: "+first+" ,"+middle+","+last);
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append("ic_user u ");
    boolean isfirst = true;
    if(first != null || middle !=null || last !=null){
      sql.append(" where ");
      if(first !=null && !"".equals(first)){
        if(!isfirst)
          sql.append(" and ");
        sql.append(" u.first_name like '");
        sql.append(first);
        sql.append("' ");
        isfirst = false;
      }
      if(middle !=null && !"".equals(middle)){
        if(!isfirst)
          sql.append(" and ");
        sql.append(" and u.middle_name like '");
        sql.append(middle);
        sql.append("' ");
        isfirst = false;
      }
      if(last !=null && !"".equals(last )){
        if(!isfirst)
          sql.append(" and ");
        sql.append(" u.last like '");
        sql.append(last);
        sql.append("' ");
        isfirst = false;
      }
      //System.err.println(sql.toString());
      try {
        return EntityFinder.findAll(new User(),sql.toString());
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
    return null;

  }

  public  Account getAccount(int id){
    return (Account)Account.getEntityInstance(Account.class,id);
  }

  public  AccountInfo getAccountInfo(int id){
    try {
      List accountInfo = EntityFinder.getInstance().findAllByColumn(AccountInfo.class,AccountInfo.getColumnAccountId(),id);
      if(accountInfo !=null && accountInfo.size() >0)
        return (AccountInfo) accountInfo.get(0);
    }
    catch (IDOFinderException ex) {

    }
    return null;
  }

  public  User getUser(int id){
    return (User) User.getEntityInstance(User.class,id);
  }

  public  TariffIndex getTariffIndex(int iTariffIndexId){
     if(iTariffIndexId > 0){
      try {
        return new TariffIndex(iTariffIndexId);
      }
      catch (SQLException ex) {

      }
    }
    return null;
  }

  public  TariffIndex getTariffIndex(String type,int iCategoryId){
    TariffIndex ti = new TariffIndex();
    try {
      StringBuffer sql = new StringBuffer(" select * from ");
      sql.append(ti.getEntityName());
      sql.append(" where ");
      sql.append(TariffIndex.getColumnNameType());
      sql.append( " = '");
      sql.append(type);
      sql.append("' and ");
      sql.append(TariffIndex.getColumnCategoryId());
      sql.append(" = ");
      sql.append(iCategoryId);
      sql.append(" order by ");
      sql.append(ti.getIDColumnName());
      sql.append(" desc ");
      //System.err.println(sql);
      List L = EntityFinder.findAll(ti,sql.toString());
      if(L!= null)
        ti =  (TariffIndex) L.get(0);
      else
        ti =  null;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      ti = null;
    }
    return ti;
  }

  public  Collection getKeySortedTariffsByAttribute(String attribute){
    Hashtable tar = null;
    Map AccKeyMap = mapOfAccountKeys();
    Map TarKeyMap = mapOfTariffKeys();
    List tariffs = this.listOfTariffsByAttribute(attribute);
    if(tariffs != null ){
      tar = new Hashtable();
      java.util.Iterator iter = tariffs.iterator();
      Tariff t;
      Integer acckey;
      Integer tarkey;
      while(iter.hasNext()){
        t = (Tariff) iter.next();
        acckey = new Integer(t.getAccountKeyId());
        if(AccKeyMap.containsKey(acckey)){
          AccountKey AK = (AccountKey) AccKeyMap.get(acckey);
          tarkey = new Integer(AK.getTariffKeyId());
          if(TarKeyMap.containsKey(tarkey)){
            TariffKey TK = (TariffKey) TarKeyMap.get(tarkey);
            if(tar.containsKey(tarkey)){
              Tariff a = (Tariff)tar.get(tarkey);
              a.setPrice(a.getPrice()+t.getPrice());
            }
            else{
              t.setName(TK.getName());
              t.setInfo(TK.getInfo());
              tar.put(tarkey,t)  ;
            }
          }
        }
      }
      return tar.values();
    }
    return null;
  }

  public  Map mapOfAccountKeys(){
    List L = listOfAccountKeys();
    if(L != null){
      int len = L.size();
      Hashtable H = new Hashtable(len);
      for (int i = 0; i < len; i++) {
        AccountKey AK = (AccountKey) L.get(i);
        H.put(new Integer(AK.getID()),AK);
      }
      return H;
    }
    else
      return null;
  }

  public  Map mapOfTariffKeys(){
    List L = listOfTariffKeys();
    if(L != null){
      int len = L.size();
      Hashtable H = new Hashtable(len);
      for (int i = 0; i < len; i++) {
        TariffKey AK = (TariffKey) L.get(i);
        H.put(new Integer(AK.getID()),AK);
      }
      return H;
    }
    else
      return null;
  }

   public  List listOfAccountKeys(){
    try {
      return EntityFinder.findAll(new AccountKey());
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public  List listOfTariffKeys(){
    try {
      return EntityFinder.findAll(new TariffKey());
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public  List listOfAssessmentAccountEntries(int iAccountId,int iAssessmentId){
    try {
      StringBuffer sql = new StringBuffer("select * from ");
      sql.append(AccountEntry.getEntityTableName());
      sql.append(" where ").append(AccountEntry.getAccountIdColumnName()).append(" = ").append(iAccountId);
      sql.append(" and ").append(AccountEntry.getRoundIdColumnName()).append( " = " ).append(iAssessmentId);
      return EntityFinder.getInstance().findAll(AccountEntry.class,sql.toString());

    }
    catch (IDOFinderException ex) {
      ex.printStackTrace();
    }
    return null;

  }

  public List listOfFinanceAccountsByUserId(int iUserId){
    try {
      List F = listOfFinanceAccountByUserId(iUserId);
      List P = listOfPhoneAccountByUserId(iUserId);
      if(F==null || F.size()==0){
        F = listOfAccountByUserId(iUserId);
        if(F== null)
          F = P;
      }
      else if(P!=null && F != null )
        F.addAll(P);
      return F;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;

  }

  public List listOfFinanceAccountByUserId(int iUserId){
    try {
      StringBuffer sql = new StringBuffer("select * from ");
      sql.append(AccountInfo.getEntityTableName());
      sql.append(" where ").append(AccountInfo.getColumnType()).append(" = '").append(Account.typeFinancial).append("'");
      sql.append(" and ").append(AccountInfo.getColumnUserId()).append(" = ").append(iUserId);
      //EntityFinder.debug = true;
      List L =  EntityFinder.getInstance().findAll(AccountInfo.class,sql.toString());
      //EntityFinder.debug = false;
      return L;
    }
    catch (IDOFinderException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public List listOfPhoneAccountByUserId(int iUserId){
    if(iUserId > 0){
      try {
        StringBuffer sql = new StringBuffer("select * from ");
        sql.append(Account.getEntityTableName());
        sql.append(" where ").append(Account.getTypeColumnName()).append(" = '").append(Account.typePhone).append("' ");
        sql.append(" and ").append(Account.getUserIdColumnName()).append(" = ").append(iUserId);

        //EntityFinder.debug = true;
        List L =  EntityFinder.getInstance().findAll(Account.class,sql.toString());
        //EntityFinder.debug = false;
        return L;
      }
      catch (IDOFinderException ex) {
        ex.printStackTrace();
      }
    }
    return null;
  }

   public List listOfAccountByUserId(int iUserId){
    if(iUserId > 0){
      try {
        return EntityFinder.getInstance().findAllByColumn(Account.class,Account.getUserIdColumnName(),iUserId);
      }
      catch (IDOFinderException ex) {
        ex.printStackTrace();
      }
    }
    return null;

  }

  public List listOfAccountInfoByCategoryId(int iCategoryId){
    if(iCategoryId > 0){
      try {
        return EntityFinder.getInstance().findAllByColumn(AccountInfo.class,AccountInfo.getColumnCategoryId(),iCategoryId);
      }
      catch (IDOFinderException ex) {
        ex.printStackTrace();
      }
    }
    return null;

  }

  public List listOfAccountInfo(){
    try {
      return EntityFinder.getInstance().findAll(AccountInfo.class);
    }
    catch (IDOFinderException ex) {
      ex.printStackTrace();
    }
    return null;

  }

   public List listOfUnBilledPhoneEntries(int iAccountId,idegaTimestamp from,idegaTimestamp to){
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append(AccountPhoneEntry.getEntityTableName());
    boolean where = false;
    if(iAccountId > 0){
      sql.append(" where ");
      where = true;
      sql.append(AccountPhoneEntry.getColumnNameAccountId());
      sql.append(" = ");
      sql.append(iAccountId);
    }
    if(from !=null){
      if(where)
        sql.append(" and ");
      else{
        sql.append(" where ");
        where = true;
      }
      sql.append(AccountPhoneEntry.getColumnNamePhonedStamp());
      sql.append(" >= '");
      sql.append(from.getSQLDate());
      sql.append("'");
    }
    if(to != null){
      if(where)
        sql.append(" and ");
      else{
        sql.append(" where ");
        where = true;
      }
      sql.append(AccountPhoneEntry.getColumnNamePhonedStamp());
      sql.append(" <= '");
      sql.append(to.getSQLDate());
      sql.append(" 23:59:59'");
    }
    sql.append(" and ");
    sql.append(AccountPhoneEntry.getColumnNameAccountEntryId());
    sql.append(" is null ");
    //System.err.println(sql.toString());
    List A = null;
    try{
        A = EntityFinder.findAll(new AccountPhoneEntry(),sql.toString());
    }
    catch(Exception e){A=null;}
    return A;
  }

  public float getPhoneAccountBalance(int iAccountId){
    String sql = "select sum(total_price) from fin_phone_entry where fin_account_id = "+iAccountId;
    try {
      String[] s = com.idega.data.SimpleQuerier.executeStringQuery(sql);
      if(s!=null && s.length > 0)
        return Float.parseFloat(s[0]);
    }
    catch (Exception ex) {

    }
    return 0;
  }

  public  List listOfAccountsInfoInAssessmentRound(int roundid){
    StringBuffer sql = new StringBuffer("select distinct a.* ");
    sql.append(" from fin_account_info a,fin_acc_entry e,fin_assessment_round r ");
    sql.append(" where a.account_id = e.fin_account_id ");
    sql.append(" and e.fin_assessment_round_id = r.fin_assessment_round_id ");
    sql.append(" and r.fin_assessment_round_id = ");
    sql.append(roundid);
    try {
      return EntityFinder.getInstance().findAll(AccountInfo.class,sql.toString());
    }
    catch (IDOFinderException ex) {
      ex.printStackTrace();
      return null;
    }

  }


  public List listOfAccountsInAssessmentRound(int roundid){
    StringBuffer sql = new StringBuffer("select distinct a.* ");
    sql.append(" from fin_account a,fin_acc_entry e,fin_assessment_round r ");
    sql.append(" where a.fin_account_id = e.fin_account_id ");
    sql.append(" and e.fin_assessment_round_id = r.fin_assessment_round_id ");
    sql.append(" and r.fin_assessment_round_id = ");
    sql.append(roundid);
    try {
      return EntityFinder.findAll(new Account(),sql.toString());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }

  }

  public List listOfAccountUsersByRoundId(int roundId){
    StringBuffer sql = new StringBuffer("select distinct u.* ");
    sql.append(" from fin_account a,fin_acc_entry e,fin_assessment_round r,ic_user u ");
    sql.append(" where a.fin_account_id = e.fin_account_id ");
    sql.append(" and e.fin_assessment_round_id = r.fin_assessment_round_id ");
    sql.append(" and a.ic_user_id = u.ic_user_id");
    sql.append(" and r.fin_assessment_round_id = ");
    sql.append(roundId);
    try {
      return EntityFinder.getInstance().findAll(User.class,sql.toString());
    }
    catch (IDOFinderException ex) {
      ex.printStackTrace();
      return null;
    }
  }
/*
  public List listOfPhoneEntriesInAssessment(){
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append(AccountPhoneEntry.getEntityTableName()).append(" e,");
    sql.append()
  }
*/

}// class FinanceFinder