package com.idega.block.finance.business;

import com.idega.block.finance.data.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import com.idega.util.idegaCalendar;
import com.idega.util.idegaTimestamp;
import com.idega.data.EntityFinder;
import com.idega.data.IDOFinderException;
import java.sql.SQLException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class Finder  {

 public static Tariff[] findTariffs(int month,int year){
   Tariff[] tariffs = new Tariff[0];
    try{
      com.idega.util.idegaCalendar ic = new com.idega.util.idegaCalendar();
      int endofmonth = ic.getLengthOfMonth(month,year);
      String from = new idegaTimestamp(1,month,year).getTimestamp().toString();
      String to = new idegaTimestamp(endofmonth,month,year).getTimestamp().toString();
      tariffs = (Tariff[]) (((com.idega.block.finance.data.TariffHome)com.idega.data.IDOLookup.getHomeLegacy(Tariff.class)).createLegacy().findAllByColumnOrdered("usefrom_date",from,"useto_date",to,"tariff_id"));
    }
    catch(SQLException e){}
    return tariffs;
  }

  public static Tariff[] findTariffs(){
   Tariff[] tariffs = new Tariff[0];
    try{
      tariffs = (Tariff[]) ((com.idega.block.finance.data.TariffHome)com.idega.data.IDOLookup.getHomeLegacy(Tariff.class)).createLegacy().findAllOrdered(com.idega.block.finance.data.TariffBMPBean.getColumnAttribute());
    }
    catch(SQLException e){}
    return tariffs;
  }

  public static TariffKey[] findTariffKeys(){
   TariffKey[] keys = new TariffKey[0];
    try{
      keys = (TariffKey[]) ((com.idega.block.finance.data.TariffKeyHome)com.idega.data.IDOLookup.getHomeLegacy(TariffKey.class)).createLegacy().findAll();
    }
    catch(SQLException e){}
    return keys;
  }

  public static List listOfTariffs(){
    try{
      return EntityFinder.findAll(((com.idega.block.finance.data.TariffHome)com.idega.data.IDOLookup.getHomeLegacy(Tariff.class)).createLegacy());
    }
    catch(SQLException e){return null;}
  }


  public static List getAccountKeys(){
    List  L = null;
    try{
      L = EntityFinder.findAll(((com.idega.block.finance.data.AccountKeyHome)com.idega.data.IDOLookup.getHomeLegacy(AccountKey.class)).createLegacy());
    }
    catch(SQLException e){

    }
    return L;
  }

  public static AccountKey[] findAccountKeys(){
   AccountKey[] keys = new AccountKey[0];
    try{
    keys = (AccountKey[]) (((com.idega.block.finance.data.AccountKeyHome)com.idega.data.IDOLookup.getHomeLegacy(AccountKey.class)).createLegacy()).findAllOrdered(com.idega.block.finance.data.AccountKeyBMPBean.getNameColumnName());
    }
    catch(SQLException e){}
    return keys;
  }

  public static List listOfAccounts(){
    List L = null;
    try{
       L = EntityFinder.findAll(((com.idega.block.finance.data.AccountHome)com.idega.data.IDOLookup.getHomeLegacy(Account.class)).createLegacy());
    }
    catch(Exception e){e.printStackTrace();}
    return L;
  }

  public static int countAccounts(){
    String sql = "select count(*) from fin_account ";
    int count = 0;
    try{
      count = ((com.idega.block.finance.data.AccountHome)com.idega.data.IDOLookup.getHomeLegacy(Account.class)).createLegacy().getNumberOfRecords(sql.toString());
    }
    catch(SQLException ex){}
    if(count < 0)
      count = 0;
    return count;
  }

  public static List listOfAssessments(){
    try {
      return EntityFinder.findAllDescendingOrdered(((com.idega.block.finance.data.AssessmentRoundHome)com.idega.data.IDOLookup.getHomeLegacy(AssessmentRound.class)).createLegacy(),com.idega.block.finance.data.AssessmentRoundBMPBean.getRoundStampColumnName());
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static int countUnGroupedEntries(){
     String sql = "select count(*) from fin_acc_entry where FIN_ENTRY_GROUP_ID is not null or FIN_ENTRY_GROUP_ID > 0 ";
    int count = 0;
    try{
      count = ((com.idega.block.finance.data.AccountHome)com.idega.data.IDOLookup.getHomeLegacy(Account.class)).createLegacy().getNumberOfRecords(sql.toString());
    }
    catch(SQLException ex){}
    if(count < 0)
      count = 0;
    return count;
  }

  public static List listOfEntryGroups(){
    try {
      return EntityFinder.findAllDescendingOrdered(((com.idega.block.finance.data.EntryGroupHome)com.idega.data.IDOLookup.getHomeLegacy(EntryGroup.class)).createLegacy(),com.idega.block.finance.data.EntryGroupBMPBean.getColumnNameGroupDate());
    }
    catch (SQLException ex) {
      return null;
    }

  }

  public static List listOfEntriesInGroup(int id){
     try {

      EntityFinder.debug = true;
      List L =  EntityFinder.findAllByColumn(((com.idega.block.finance.data.AccountEntryHome)com.idega.data.IDOLookup.getHomeLegacy(AccountEntry.class)).createLegacy(),com.idega.block.finance.data.AccountEntryBMPBean.getEntryGroupIdColumnName(),id);
      EntityFinder.debug = false;
      return L;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public static Map mapOfIndicesByTypes(List listOfTariffIndices){
    List L = listOfTariffIndices;
    if(L!= null){
      int len = L.size();
      Hashtable T = new Hashtable(len);
      TariffIndex ti;
      for (int i = 0; i < len; i++) {
        ti = (TariffIndex) L.get(i);
        T.put(ti.getType(),ti);
      }
      return T;
    }
    else
     return null;
  }

  public static Map mapOfIndexIds(List listOfTariffIndices){
    List L = listOfTariffIndices;
    if(L!= null){
      int len = L.size();
      Hashtable T = new Hashtable(len);
      TariffIndex ti;
      for (int i = 0; i < len; i++) {
        ti = (TariffIndex) L.get(i);
        T.put(ti.getType(),Integer.toString(ti.getID()));
      }
      return T;
    }
    else
     return null;
  }

  public static List listOfTariffIndices(){
    try {
      return EntityFinder.findAll(((com.idega.block.finance.data.TariffIndexHome)com.idega.data.IDOLookup.getHomeLegacy(TariffIndex.class)).createLegacy());
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public static List listOfTypeGroupedIndices(){
    Vector V = new Vector();
    for (int i = 0; i < com.idega.block.finance.data.TariffIndexBMPBean.indexType.length(); i++) {
      TariffIndex ti= getTariffIndex(String.valueOf(com.idega.block.finance.data.TariffIndexBMPBean.indexType.charAt(i)));
      if(ti!= null)
        V.add(ti);
    }
    return V;
  }

  public static TariffIndex getTariffIndex(String type){
    TariffIndex ti = ((com.idega.block.finance.data.TariffIndexHome)com.idega.data.IDOLookup.getHomeLegacy(TariffIndex.class)).createLegacy();
    try {
      List L = EntityFinder.findAllByColumnDescendingOrdered(ti,com.idega.block.finance.data.TariffIndexBMPBean.getColumnNameType(),type,ti.getIDColumnName());
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

  public static List listOfFinanceEntriesWithoutGroup(idegaTimestamp from,idegaTimestamp to){
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append(com.idega.block.finance.data.AccountEntryBMPBean.getEntityTableName());
    sql.append(" where fin_entry_group_id is null ");
    if(from !=null){
      sql.append(" and last_updated >= ");
      sql.append(from.getSQLDate());
    }
    if(to !=null){
      sql.append(" and last_updated <= ");
      sql.append('\'');
      sql.append(to.getSQLDate());
      sql.append(" 23:59:59'");
    }
    //System.err.println(sql.toString());
    try {
      return EntityFinder.findAll(((com.idega.block.finance.data.AccountEntryHome)com.idega.data.IDOLookup.getHomeLegacy(AccountEntry.class)).createLegacy(),sql.toString());
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }
  }




}// class AccountKeyEditor
