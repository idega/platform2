package com.idega.block.finance.business;





import com.idega.block.finance.business.*;

import com.idega.block.finance.data.*;

import com.idega.util.idegaTimestamp;

import com.idega.data.SimpleQuerier;

import com.idega.data.EntityBulkUpdater;

import com.idega.transaction.IdegaTransactionManager;

import javax.transaction.TransactionManager;

import java.sql.SQLException;

import java.util.List;

import java.util.Hashtable;

import java.util.Iterator;

import java.util.Vector;



/**

 * Title:   idegaclasses

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:

 * @author  <a href="mailto:aron@idega.is">aron@idega.is

 * @version 1.0

 */



public class AssessmentBusiness  {



  public static final char cComplex = 'x';

  public static final char cAll = 'a';

  public static final char cBuilding = 'b';

  public static final char cFloor = 'f';

  public static final char cCategory = 'c';

  public static final char cType = 't';

  public static final char cApartment = 'p';



  public static void groupEntriesWithSQL(idegaTimestamp from,

                                         idegaTimestamp to) throws Exception{



    TransactionManager t = IdegaTransactionManager.getInstance();

    try{





      t.begin();

      ///////////////////////////

      AccountEntry ae = (AccountEntry)AccountEntry.getStaticInstance(AccountEntry.class);

      EntryGroup EG = null;

      int gid = -1;

      try {

        EG = new EntryGroup();

        EG.setGroupDate(idegaTimestamp.RightNow().getSQLDate());

        EG.insert();

        gid = EG.getID();

      }

      catch (SQLException ex) {

        ex.printStackTrace();

        EG = null;

      }



      if(EG !=null){

        String dateColummn = AccountEntry.getPaymentDateColumnName();

        StringBuffer sql = new StringBuffer("update ");

        sql.append(AccountEntry.getEntityTableName());

        sql.append(" set ");

        sql.append(AccountEntry.getEntryGroupIdColumnName());

        sql.append(" = ");

        sql.append(gid);

        sql.append(" where ");

        sql.append(AccountEntry.getEntryGroupIdColumnName());

        sql.append(" is null ");

        if(from !=null){

          sql.append(" and ").append(dateColummn).append(" >= ").append(from.getSQLDate());

        }

        if(to !=null){

          sql.append(" and ").append(dateColummn).append(" <= ");

          sql.append('\'');

          sql.append(to.getSQLDate());

          sql.append(" 23:59:59'");

        }



        String where = " where "+AccountEntry.getEntryGroupIdColumnName()+" = "+gid;

        String sMinSql = "select min("+ae.getIDColumnName()+") from "+AccountEntry.getEntityTableName()+where;

        String sMaxSql = "select max("+ae.getIDColumnName()+") from "+AccountEntry.getEntityTableName()+where;

        //System.err.println(sql.toString());

        //System.err.println(sMinSql);

        //System.err.println(sMaxSql);



        SimpleQuerier.execute(sql.toString());

        String[] mi = SimpleQuerier.executeStringQuery(sMinSql);

        String[] ma = SimpleQuerier.executeStringQuery(sMaxSql);

        if(mi!=null && mi.length > 0){

          EG.setEntryIdFrom(new Integer(mi[0]).intValue());

        }

        if(ma!=null && ma.length > 0){

          EG.setEntryIdTo(new Integer(ma[0]).intValue());

        }

        EG.update();



      }

      t.commit();

      ///////////////////////////



    }

    catch(Exception e) {

      try {

        t.rollback();

      }

      catch(javax.transaction.SystemException ex) {

        ex.printStackTrace();

      }



      e.printStackTrace();

    }





  }



  public static void groupEntries(idegaTimestamp from,

                                  idegaTimestamp to) throws Exception{

    List L = Finder.listOfFinanceEntriesWithoutGroup(from,to);

    if(L!=null){

      int min = 0,max = 0;

      EntryGroup EG = null;

      try {

        EG = new EntryGroup();

        EG.setGroupDate(idegaTimestamp.RightNow().getSQLDate());

        EG.insert();

        int gid = EG.getID();

        //System.err.println(" gid "+gid);

      }

      catch (Exception ex) {

        ex.printStackTrace();

        try {

          EG.delete();

        }

        catch (SQLException ex2) {

          ex2.printStackTrace();

          EG = null;

        }



      }



      if(EG !=null){

        javax.transaction.TransactionManager t = com.idega.transaction.IdegaTransactionManager.getInstance();



        try{

          t.begin();

          ////////////////////////

          Iterator It = L.iterator();



          AccountEntry AE;

          int aeid = 0;

          AE = (AccountEntry) It.next();

          aeid = AE.getID();

          min = aeid;

          max = aeid;



          AE.setEntryGroupId(EG.getID());

          AE.update();



          while(It.hasNext()){

            AE = (AccountEntry) It.next();

            aeid = AE.getID();

            min = aeid < min ? aeid : min ;

            max = aeid > min ? aeid : max ;

            AE.setEntryGroupId(EG.getID());

            AE.update();



          }



          EG.setEntryIdFrom(min);

          EG.setEntryIdTo(max);

          EG.update();

          //////////////////////////////

          t.commit();



        }

        catch(Exception e) {

          try {

            t.rollback();



          }

          catch(javax.transaction.SystemException ex) {

            ex.printStackTrace();

          }

          try {

            EG.delete();

          }

          catch (SQLException ex) {



          }

          e.printStackTrace();



        }

      }//if EG null

    }

  }



  public static int getGroupEntryCount(EntryGroup entryGroup){

    int count = 0;

    if(entryGroup !=null ){

      StringBuffer sql = new StringBuffer("select count(*) from ");

      sql.append(AccountEntry.getEntityTableName());

      sql.append(" where ");

      sql.append(AccountEntry.getEntryGroupIdColumnName());

      sql.append(" = ");

      sql.append(entryGroup.getID());

      //System.err.println(sql.toString());

      try {

        count = entryGroup.getNumberOfRecords(sql.toString());

      }

      catch (SQLException ex) {

        ex.printStackTrace();

        count = 0;

      }

    }

    return count;

  }

  /*

    public static void updateAllAccounts(){

      String sql = "update fin_account f set f.balance = (select sum(price) from fin_acc_entry  f2 where f2.fin_account_id = f.fin_account_id)";

      try {



        SimpleQuerier.execute(sql);

      }

      catch (Exception ex) {

        ex.printStackTrace();

      }

    }

  */

}

