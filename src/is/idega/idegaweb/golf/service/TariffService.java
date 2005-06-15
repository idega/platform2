package is.idega.idegaweb.golf.service;



import com.idega.data.*;

import is.idega.idegaweb.golf.entity.*;
import com.idega.util.IWTimestamp;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import java.sql.*;
import java.util.Hashtable;
import java.util.Vector;
import java.util.List;

import javax.ejb.FinderException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class TariffService  {


  public TariffService(){

  }


  public DropdownMenu getExtraCatalogueDropdownMenu(String unionID)throws SQLException{
    DropdownMenu drp = new DropdownMenu();
    PriceCatalogue[] p = getExtraCatalogues(unionID);
    drp.addDisabledMenuElement("0","Engin flokkur valinn !");
    for(int i = 0; i < p.length; i++){
      drp.addMenuElement(p[i].getID(),p[i].getName());
    }
    return drp;
  }
  static public DropdownMenu getExtraCatalogueDropdownMenu(String name,String unionID)throws SQLException{
    DropdownMenu drp = new DropdownMenu(name);
    PriceCatalogue[] p = getExtraCatalogues(unionID);
    drp.addDisabledMenuElement("0","Engin flokkur valinn !");
    for(int i = 0; i < p.length; i++){
      drp.addMenuElement(p[i].getID(),p[i].getName());
    }
    return drp;
  }

  static public String getExtraCatalogueSQL(String union_id){
    return "select * from price_catalogue where union_id = '"+union_id+"' and in_use = 'Y' and is_independent = 'Y'";
  }

  static public void saveStringMatrixValues(IWContext modinfo , String[][] matrix, String ParameterName){
    modinfo.getSession().setAttribute(ParameterName, matrix);
  }
  static public String[][] getValues(IWContext modinfo, String ParameterName){
     if(modinfo.getSession().getAttribute(ParameterName)!= null){
      String S[][] = (String[][]) modinfo.getSession().getAttribute(ParameterName);
      return S;
      }
      else return null;
  }

  static public void saveIntArray(IWContext modinfo , int[] array, String ParameterName){
     modinfo.getSession().setAttribute(ParameterName, array );
  }
  static public int[] retrieveIntArray(IWContext modinfo, String ParameterName){
     if(modinfo.getSession().getAttribute(ParameterName)!= null){
      int S[] = (int[]) modinfo.getSession().getAttribute(ParameterName);
      return S;
      }
      else return null;
  }

  static public void saveCatalogArray(IWContext modinfo , PriceCatalogue[] array, String ParameterName){
     modinfo.getSession().setAttribute(ParameterName, array );
  }
  static public PriceCatalogue[] retrieveCatalogArray(IWContext modinfo, String ParameterName){
     if(modinfo.getSession().getAttribute(ParameterName)!= null){
      PriceCatalogue S[] = (PriceCatalogue[]) modinfo.getSession().getAttribute(ParameterName);
      return S;
      }
      else return null;
  }

  static public void saveMemberArray(IWContext modinfo,Member[][] memberarray , String ParameterName){
   modinfo.getSession().setAttribute(ParameterName, memberarray);
  }
  static public Member[][] getMemberArray(IWContext modinfo , String ParameterName){
   if(modinfo.getSession().getAttribute(ParameterName)!= null){
     return (Member[][])modinfo.getSession().getAttribute( ParameterName );
    }
    else return null;
  }

  static public void saveVectorArray(IWContext modinfo,Vector[] V, String ParameterName){
   modinfo.getSession().setAttribute(ParameterName, V);
  }
  static public Vector[] retrieveVectorArray(IWContext modinfo, String ParameterName){
   if(modinfo.getSession().getAttribute(ParameterName)!= null){
     return (Vector[]) modinfo.getSession().getAttribute(ParameterName );
    }
    else return null;
  }

  static public void saveIntegerMatrix(IWContext modinfo, Integer[][] matrix ,String ParameterName){
    modinfo.getSession().setAttribute(ParameterName, matrix);
  }
  static public Integer[][] retrieveIntegerMatrix(IWContext modinfo,String ParameterName){
    if(modinfo.getSession().getAttribute(ParameterName)!= null){
      Integer[][] T = (Integer[][])modinfo.getSession().getAttribute(ParameterName );
      return T;
    }
    else return null;
  }

  static public void saveInt(IWContext modinfo ,int i,String ParameterName){
    modinfo.getSession().setAttribute(ParameterName, new Integer(i));
  }
  static public int retrieveInt(IWContext modinfo ,String ParameterName){
    if(modinfo.getSession().getAttribute(ParameterName)!= null){
      Integer mc = (Integer)modinfo.getSession().getAttribute(ParameterName);
      return mc.intValue();
    }
    else return 0;
  }

  static public void saveHashtable(IWContext modinfo ,Hashtable H , String ParameterName){
    modinfo.getSession().setAttribute(ParameterName, H);
  }
  static public Hashtable retrieveHashtable(IWContext modinfo , String ParameterName){
    if(modinfo.getSession().getAttribute(ParameterName)!= null){
      Hashtable H = (Hashtable)modinfo.getSession().getAttribute(ParameterName);
      return H;
    }
    else return null;
  }

  static public void deleteElement(IWContext modinfo, String ParameterName){
    if(modinfo.getSession().getAttribute(ParameterName)!= null){
      modinfo.getSession().removeAttribute(ParameterName);
    }
  }

  static public List getCatalogList(String union_id) throws SQLException{
    List L = EntityFinder.findAllByColumnEquals((PriceCatalogue) IDOLookup.instanciateEntity(PriceCatalogue.class),"union_id",union_id,"in_use","Y","is_independent","N");
    return L;
  }

  static public PriceCatalogue[] getMainCatalogues(String sUnionId){
    try{
      List L = TariffService.getCatalogList(sUnionId);
      if(L != null){
        int len = L.size();
        PriceCatalogue[] P = new PriceCatalogue[len];
        for(int i = 0; i < len ; i++){
          P[i] = (PriceCatalogue) L.get(i);
        }
        return P;
      }
      return new PriceCatalogue[0];
    }
    catch(SQLException sql){
      return new PriceCatalogue[0];
    }
  }

  static public List getExtraCatalogList(String union_id) throws SQLException{
    return EntityFinder.findAllByColumnEquals((PriceCatalogue) IDOLookup.instanciateEntity(PriceCatalogue.class),"union_id",union_id,"in_use","Y","is_independent","Y");
  }

  static public PriceCatalogue[] getExtraCatalogues(String unionID) throws SQLException{
    PriceCatalogue[] PCs = (PriceCatalogue[])((PriceCatalogue) IDOLookup.instanciateEntity(PriceCatalogue.class)).findAll( getExtraCatalogueSQL(unionID));
    return PCs;
  }



  private void addMemberToMembersArray(Vector[] MemberArray,Vector[] MemInfoArray, Member eMember,UnionMemberInfo eUmi,int[] Membergroups){
    if(Membergroups != null){
      for(int i = 0; i < Membergroups.length; i++){
        MemberArray[Membergroups[i]].addElement(eMember);
        MemInfoArray[Membergroups[i]].addElement(eUmi);
      }
    }
    else{
        // Rest Members
        MemberArray[MemberArray.length-1].addElement(eMember);
        MemInfoArray[MemInfoArray.length-1].addElement(eUmi);
    }
  }

  static public String getActiveMembersSQL(int iUnionID){
    StringBuffer SQLstringbuff = new StringBuffer("select * from union_member_info where union_id = '");
    SQLstringbuff.append(iUnionID);
    SQLstringbuff.append("' and member_status = 'A' ");
    return SQLstringbuff.toString();
  }

  static public String getDependentMembersSQL(int iUnionID){
    return getActiveMembersSQL(iUnionID)+" and (price_catalogue_id is null or price_catalogue_id = '0')  ";
  }

  static public String getIndependentMembersSQL(int iUnionID){
    return getActiveMembersSQL(iUnionID)+" and (price_catalogue_id is not null and price_catalogue_id != '0') ";
  }

  static public int calculateBalance(AccountEntry[] eAccountEntries){
    int balance = 0;
    if(eAccountEntries.length > 0){
      for(int i = 0; i < eAccountEntries.length; i++){
        balance += eAccountEntries[i].getPrice();
      }
    }
    return balance;
  }

  static public Account[] getAccounts(int iMemberId, int iUnionId){
    try{
      Account[] A = (Account[]) ((Account) IDOLookup.instanciateEntity(Account.class)).findAllByColumnEquals("member_id",String.valueOf(iMemberId),"union_id",String.valueOf(iUnionId));
      return A;
    }
    catch(SQLException sql){
      return new Account[0];
    }
  }

  static public int findAccountID(int member_id, int union_id){
    int id = -1;
    try{
      Account[] A = (Account[]) ((Account) IDOLookup.instanciateEntity(Account.class)).findAllByColumnEquals("member_id",String.valueOf(member_id),"union_id",String.valueOf(union_id));
      if(A.length > 0)
        id = A[0].getID();
    }
    catch(SQLException e){}
    return id;
  }

  static public Account findAccount(int member_id, int union_id,int accountYearId){
    try{

      Account[] A = (Account[]) ((Account) IDOLookup.instanciateEntity(Account.class)).findAllByColumnEquals(
              "member_id",String.valueOf(member_id),
              "union_id",String.valueOf(union_id),
              "account_year_id",String.valueOf(accountYearId));
      if(A.length > 0)
        return A[0];
    }
    catch(SQLException e){}
    return null;
  }

  static public Account[] findAccounts(int member_id, int union_id){
    try{
      Account[] A = (Account[]) ((Account) IDOLookup.instanciateEntity(Account.class)).findAllByColumnEquals("member_id",String.valueOf(member_id),"union_id",String.valueOf(union_id));
      return A;
    }
    catch(SQLException e){}
    return new Account[0];
  }

  static public Payment[] getMemberPayments(int iMemberId,int iUnionId){
     Payment[] P;
    try{
      P = (Payment[]) ((Payment) IDOLookup.instanciateEntity(Payment.class)).findAllByColumnEqualsOrdered("member_id",String.valueOf(iMemberId),"union_id",String.valueOf(iUnionId),"last_updated");
    }
    catch(SQLException e){
      P = new Payment[0];
    }
    return P;
  }

   static public Payment[] getMemberPayments(int iAccountId,int iUnionId,IWTimestamp from,IWTimestamp to){
     Payment[] P;
    Payment entry = (Payment) IDOLookup.instanciateEntity(Payment.class);
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append(entry.getEntityName());
    sql.append(" where account_id = ");
    sql.append(iAccountId);
    sql.append(" and union_id = ");
    sql.append(iUnionId);
    if(from !=null){
      sql.append(" and payment_date >= '");
      sql.append(from.getSQLDate());
      sql.append("'");
    }
    if(to != null){
      sql.append(" and payment_date <= '");
      sql.append(to.getSQLDate());
      sql.append(" 23:59:59'");
    }
    sql.append(" order by payment_date ");
    //System.err.println(sql.toString());
    try{
      P = (Payment[]) ((Payment) IDOLookup.instanciateEntity(Payment.class)).findAll(sql.toString());
    }
    catch(SQLException e){
      P = new Payment[0];
    }
    return P;
  }

  static public AccountEntry[] getAccountEntrys(int iAccountId){
    AccountEntry[] E;
    try{
      E = (AccountEntry[]) ((AccountEntry) IDOLookup.instanciateEntity(AccountEntry.class)).findAllByColumnEqualsOrdered("account_id",String.valueOf(iAccountId),"last_updated");
    }
    catch(SQLException e){
      E = new AccountEntry[0];
    }
    return E;
  }

  static public AccountEntry[] getAccountEntrys(int iAccountId,IWTimestamp from,IWTimestamp to){
    AccountEntry[] E;
    AccountEntry entry = (AccountEntry) IDOLookup.instanciateEntity(AccountEntry.class);
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append(entry.getEntityName());
    sql.append(" where account_id = ");
    sql.append(iAccountId);
    if(from !=null){
      sql.append(" and last_updated >= '");
      sql.append(from.getSQLDate());
      sql.append("'");
    }
    if(to != null){
      sql.append(" and last_updated <= '");
      sql.append(to.getSQLDate());
      sql.append(" 23:59:59'");
    }
    sql.append(" order by last_updated ");
    //System.err.println(sql.toString());
    try{
      E = (AccountEntry[]) ((AccountEntry) IDOLookup.instanciateEntity(AccountEntry.class)).findAll(sql.toString());
    }
    catch(SQLException e){
      E = new AccountEntry[0];
    }
    return E;
  }

  static public AccountEntry[] getTariffEntrys(int iAccountId){
     AccountEntry[] E;
    try{
      E = (AccountEntry[]) ((AccountEntry) IDOLookup.instanciateEntity(AccountEntry.class)).findAllByColumnEqualsOrdered("account_id",String.valueOf(iAccountId),"info","Álagning","last_updated");
    }
    catch(SQLException e){
      E = new AccountEntry[0];
    }
    return E;
  }

  static public AccountEntry[] getTariffEntrys(int iAccountId,IWTimestamp from,IWTimestamp to){
    AccountEntry[] E;
    AccountEntry entry = (AccountEntry) IDOLookup.instanciateEntity(AccountEntry.class);
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append(entry.getEntityName());
    sql.append(" where account_id = ");
    sql.append(iAccountId);
    sql.append(" and info = 'Álagning'");
    if(from !=null){
      sql.append(" and last_updated >= '");
      sql.append(from.getSQLDate());
      sql.append("'");
    }
    if(to != null){
      sql.append(" and last_updated <= '");
      sql.append(to.getSQLDate());
      sql.append(" 23:59:59'");
    }
    sql.append(" order by last_updated ");
    //System.err.println(sql.toString());
    try{
      E = (AccountEntry[]) ((AccountEntry) IDOLookup.instanciateEntity(AccountEntry.class)).findAll(sql.toString());
    }
    catch(SQLException e){
      E = new AccountEntry[0];
    }
    return E;
  }


  static public void makePayment(int memberID,int iAccountId,int iUnionId ,int RoundId, int Price , boolean Status , String Name, String Info , int InstallmentNumber, int Totalinstallments, int PaymentTypeID, Timestamp PayDate ,Timestamp last_updated, int cashier_id) throws SQLException {
    Payment P = (Payment) IDOLookup.createLegacy(Payment.class);
    P.setAccountId(iAccountId);
    P.setMemberId(memberID);
    P.setRoundId( RoundId );
    P.setPrice(Price );
    P.setStatus( Status ); // False meaning; has not been paid
    P.setExtraInfo( Info );
    P.setName(Name);
    P.setInstallmentNr( InstallmentNumber );
    P.setTotalInstallment( Totalinstallments );
    P.setPaymentTypeID( PaymentTypeID );
    P.setPaymentDate( PayDate );
    P.setLastUpdated(last_updated);
    P.setCashierId(cashier_id);
    P.setUnionId(iUnionId);
    P.insert();
  }

  static public Account makeNewAccount(int MemberId,int UnionId,String Name,int CashierId,int accountYearId){
    Account A = (Account) IDOLookup.createLegacy(Account.class);
    A.setBalance(0);
    A.setName(String.valueOf(MemberId));
    A.setValid(true);
    A.setExtraInfo("");
    A.setCashierId(CashierId);
    A.setCreationDate(IWTimestamp.getTimestampRightNow());
    A.setLastUpdated(IWTimestamp.getTimestampRightNow());
    A.setMemberId(MemberId);
    A.setUnionId(UnionId);
    A.setAccountYear(accountYearId);
    int ret = -1;
    try{
      A.insert() ;
      //ret = A.getID();
      //System.err.print("st reikn "+ret);
      return A;
    }
    catch(SQLException sql){
      sql.printStackTrace() ;
      //ret = -1;
    }
    return null;
  }

   static public AccountEntry makeAnAccountEntry(int AccountId,int Price, String Name,String Info,String AccountKey,String EntryKey,String TariffKey,int CashierId,Timestamp PaymentDate,Timestamp LastUpdated) throws SQLException, FinderException{
    AccountEntry E = (AccountEntry) IDOLookup.createLegacy(AccountEntry.class);
    E.setAccountId(AccountId);
    E.setPrice(Price);
    E.setName(Name);
    E.setAccountKey(AccountKey);
    E.setEntryKey(EntryKey);
    E.setTariffKey(TariffKey);
    E.setInfo(Info);
    E.setCashierId(CashierId);
    E.setPaymentDate(PaymentDate);
    E.setLastUpdated(LastUpdated);
    E.insert();
    Account A = ((AccountHome) IDOLookup.getHomeLegacy(Account.class)).findByPrimaryKey(AccountId);
    A.addToBalance(Price);
    A.update();
    return E;
  }

  static public void makeAccountEntry(int AccountId,int Price, String Name,String Info,String AccountKey,String EntryKey,String TariffKey,int CashierId,Timestamp PaymentDate,Timestamp LastUpdated) throws SQLException, FinderException{
    AccountEntry E = (AccountEntry) IDOLookup.createLegacy(AccountEntry.class);
    E.setAccountId(AccountId);
    E.setPrice(Price);
    E.setName(Name);
    E.setAccountKey(AccountKey);
    E.setEntryKey(EntryKey);
    E.setTariffKey(TariffKey);
    E.setInfo(Info);
    E.setCashierId(CashierId);
    E.setPaymentDate(PaymentDate);
    E.setLastUpdated(LastUpdated);
    E.insert();
    Account A = ((AccountHome) IDOLookup.getHomeLegacy(Account.class)).findByPrimaryKey(AccountId);
    A.addToBalance(Price);
    A.update();
  }

  static public String getFamilySql(int iUnionId, int iFamilyId){
    StringBuffer sql = new StringBuffer();
    sql.append("select member_id,first_name,middle_name,last_name,date_of_birth,gender,image_id,social_security_number,email ");
    sql.append("from member,union_member_info ");
    sql.append("where member.member_id = union_member_info.member_id ");
    sql.append("and union_member_info.union_id = ");
    sql.append(iUnionId);
    sql.append(" and union_member_info.family_id = ");
    sql.append(iFamilyId);
    return sql.toString() ;
  }

  static public List getMemberFamily(int iMemberId,int iUnionId){
    try{
      UnionMemberInfo umi = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(iMemberId).getUnionMemberInfo(iUnionId);
      List M = EntityFinder.findAll((Member) IDOLookup.instanciateEntity(Member.class),getFamilySql(iUnionId,umi.getFamilyId()));
      return M;
    }
    catch(SQLException sql){
      sql.printStackTrace();
      return null;
    }
    catch(FinderException fe) {
    		fe.printStackTrace();
    		return null;
    }
  }


}// class Tariffer
