package com.idega.block.finance.business;

import com.idega.block.finance.data.*;
import com.idega.data.GenericEntity;
import com.idega.core.data.ICObjectInstance;
import java.sql.SQLException;
import java.util.*;
import com.idega.util.idegaCalendar;
import com.idega.util.idegaTimestamp;
import com.idega.data.EntityFinder;
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

  public static int getObjectInstanceCategoryId(int iObjectInstanceId,boolean CreateNew){
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

  public static int getObjectInstanceCategoryId(ICObjectInstance eObjectInstance){
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

  public static int getObjectInstanceCategoryId(int iObjectInstanceId){
    try {
      ICObjectInstance obj = new ICObjectInstance(iObjectInstanceId);
      return getObjectInstanceCategoryId(obj);
    }
    catch (Exception ex) {

    }
    return -1;
  }

  public static int getObjectInstanceIdFromCategoryId(int iCategoryId){
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


  public static FinanceCategory getFinanceCategory(int iCategoryId){
    if( iCategoryId > 0){
      try {
          return new FinanceCategory(iCategoryId );
      }
      catch (SQLException ex) {

      }
    }
    return null;
  }



  public static List listOfEntityForObjectInstanceId(int instanceid){
    try {
      ICObjectInstance obj = new ICObjectInstance(instanceid );
      return listOfEntityForObjectInstanceId(obj);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List listOfEntityForObjectInstanceId( ICObjectInstance obj){
    try {
      List L = EntityFinder.findRelated(obj,new FinanceCategory());
      return L;
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List listOfCategories(){
    try {
      return EntityFinder.findAll(new FinanceCategory());
    }
    catch (SQLException ex) {
    }
    return null;
  }

   public static List listOfTariffGroups(){
    try {
      return EntityFinder.findAll(new TariffGroup());
    }
    catch (SQLException ex) {
    }
    return null;
  }

  public static List listOfFinanceHandlers(){
    try {
      return EntityFinder.findAll(new FinanceHandlerInfo());
    }
    catch (SQLException ex) {
    }
    return null;
  }

  public static List listOfAccounts(){
    return null;
  }

  public static List listOfTariffKeys(int iCategoryId){
    try {
      return EntityFinder.findAllByColumn(new TariffKey(),TariffKey.getColumnCategoryId(),iCategoryId);
    }
    catch (SQLException ex) {
    }
    return null;
  }


  public static List listOfPaymentTypes(int iCategoryId){
    try {
      return EntityFinder.findAllByColumn(new PaymentType(),PaymentType.getColumnCategoryId(),iCategoryId);
    }
    catch (SQLException ex) {
    }
    return null;
  }

  public static List listOfAssessments(int iTariffGroupId){
    try {
      return EntityFinder.findAllByColumn(new AssessmentRound(),AssessmentRound.getColumnTariffGroupId(),iTariffGroupId);
    }
    catch (SQLException ex) {
    }
    return null;
  }


  public static Map mapOfTariffKeys(int iCategoryId){
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

    public static List listOfAccountKeys(int iCategoryId){

      try{
       return EntityFinder.findAllByColumn(new AccountKey(),AccountKey.getColumnCategoryId(),iCategoryId);
      }
      catch(SQLException e){

      }
      return null;
  }

  public static List listOfTariffs(int iGroupId){
     try{
       return EntityFinder.findAllByColumn(new Tariff(),Tariff.getColumnTariffGroup(),iGroupId);
      }
      catch(SQLException e){

      }
      return null;
  }

  public static List listOfTariffsByAttribute(String attribute){
     try{
      List L =  EntityFinder.findAllByColumn(new Tariff(),Tariff.getColumnAttribute(),attribute);
      return L;
      }
      catch(SQLException e){

      }
      return null;
  }

  public static List listOfTariffGroups(int iCategoryId){
     try{
       return EntityFinder.findAllByColumn(new TariffGroup(),TariffGroup.getColumnCategoryId(),iCategoryId);
      }
      catch(SQLException e){

      }
      return null;
  }

   public static List listOfTariffGroupsWithHandlers(int iCategoryId){
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

  public static FinanceHandler getFinanceHandler(int iHandlerId){
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

  public static TariffGroup getTariffGroup(int iGroupId){
    if(iGroupId > 0){
      try {
        return new TariffGroup(iGroupId);
      }
      catch (SQLException ex) {

      }
    }
    return null;
  }

  public static int countAccounts(int iCategory,String type){
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

  public static List searchAccounts(String id,String first,String middle,String last,String type,int iCategoryId){
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
    System.err.println(sql.toString());
    try {
      return EntityFinder.findAll(new Account(),sql.toString());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return null;

  }

   public static List searchAccountUsers(String first,String middle,String last){
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
      System.err.println(sql.toString());
      try {
        return EntityFinder.findAll(new User(),sql.toString());
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
    return null;

  }

  public static Account getAccount(int id){
    return (Account)Account.getEntityInstance(Account.class,id);
  }

  public static User getUser(int id){
    return (User) User.getEntityInstance(User.class,id);
  }

  public static TariffIndex getTariffIndex(int iTariffIndexId){
     if(iTariffIndexId > 0){
      try {
        return new TariffIndex(iTariffIndexId);
      }
      catch (SQLException ex) {

      }
    }
    return null;
  }

  public static TariffIndex getTariffIndex(String type,int iCategoryId){
    TariffIndex ti = new TariffIndex();
    try {
      StringBuffer sql = new StringBuffer(" select * from ");
      sql.append(ti.getEntityName());
      sql.append(" where ");
      sql.append(ti.getColumnNameType());
      sql.append( " = '");
      sql.append(type);
      sql.append("' and");
      sql.append(ti.getColumnCategoryId());
      sql.append(" = ");
      sql.append(iCategoryId);
      sql.append(" order by ");
      sql.append(ti.getIDColumnName());
      sql.append(" desc ");
      List L = EntityFinder.findAll(ti,sql.toString());
      if(L!= null)
        ti =  (TariffIndex) L.get(0);
      else
        ti =  null;
    }
    catch (SQLException ex) {
      ti = null;
    }
    return ti;
  }

  public static Collection getKeySortedTariffsByAttribute(String attribute){
    Hashtable tar = null;
    Map AccKeyMap = mapOfAccountKeys();
    Map TarKeyMap = mapOfTariffKeys();
    List tariffs = FinanceFinder.listOfTariffsByAttribute(attribute);
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

  public static Map mapOfAccountKeys(){
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

  public static Map mapOfTariffKeys(){
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

   public static List listOfAccountKeys(){
    try {
      return EntityFinder.findAll(new AccountKey());
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List listOfTariffKeys(){
    try {
      return EntityFinder.findAll(new TariffKey());
    }
    catch (SQLException ex) {
      return null;
    }
  }


}// class FinanceFinder