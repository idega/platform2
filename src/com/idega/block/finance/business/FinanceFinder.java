package com.idega.block.finance.business;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class FinanceFinder  {
/*
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
      ICObjectInstance obj = ((com.idega.core.component.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(iObjectInstanceId);
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
      List L = EntityFinder.findRelated(eObjectInstance ,((com.idega.block.finance.data.FinanceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(FinanceCategory.class)).createLegacy());
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
      ICObjectInstance obj = ((com.idega.core.component.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(iObjectInstanceId);
      return getObjectInstanceCategoryId(obj);
    }
    catch (Exception ex) {

    }
    return -1;
  }

  public  int getObjectInstanceIdFromCategoryId(int iCategoryId){
    try {
      FinanceCategory cat = ((com.idega.block.finance.data.FinanceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(FinanceCategory.class)).findByPrimaryKeyLegacy(iCategoryId);
      List L = EntityFinder.findRelated( cat,((com.idega.core.component.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).createLegacy());
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
          return ((com.idega.block.finance.data.FinanceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(FinanceCategory.class)).findByPrimaryKeyLegacy(iCategoryId );
      }
      catch (SQLException ex) {

      }
    }
    return null;
  }



  public  List listOfEntityForObjectInstanceId(int instanceid){
    try {
      ICObjectInstance obj = ((com.idega.core.component.data.ICObjectInstanceHome)com.idega.data.IDOLookup.getHomeLegacy(ICObjectInstance.class)).findByPrimaryKeyLegacy(instanceid );
      return listOfEntityForObjectInstanceId(obj);
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public  List listOfEntityForObjectInstanceId( ICObjectInstance obj){
    try {
      List L = EntityFinder.findRelated(obj,((com.idega.block.finance.data.FinanceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(FinanceCategory.class)).createLegacy());
      return L;
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public  List listOfCategories(){
    try {
      return EntityFinder.findAll(((com.idega.block.finance.data.FinanceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(FinanceCategory.class)).createLegacy());
    }
    catch (SQLException ex) {
    }
    return null;
  }

   public  List listOfTariffGroups(){
    try {
      return EntityFinder.findAll(((com.idega.block.finance.data.TariffGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TariffGroup.class)).createLegacy());
    }
    catch (SQLException ex) {
    }
    return null;
  }

  public  List listOfFinanceHandlers(){
    try {
      return EntityFinder.findAll(((com.idega.block.finance.data.FinanceHandlerInfoHome)com.idega.data.IDOLookup.getHomeLegacy(FinanceHandlerInfo.class)).createLegacy());
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
      return EntityFinder.findAllByColumn(((com.idega.block.finance.data.TariffKeyHome)com.idega.data.IDOLookup.getHomeLegacy(TariffKey.class)).createLegacy(),com.idega.block.finance.data.TariffKeyBMPBean.getColumnCategoryId(),iCategoryId);
    }
    catch (SQLException ex) {
    }
    return null;
  }


  public  List listOfPaymentTypes(int iCategoryId){
    try {
      return EntityFinder.findAllByColumn(((com.idega.block.finance.data.PaymentTypeHome)com.idega.data.IDOLookup.getHomeLegacy(PaymentType.class)).createLegacy(),com.idega.block.finance.data.PaymentTypeBMPBean.getColumnCategoryId(),iCategoryId);
    }
    catch (SQLException ex) {
    }
    return null;
  }

  public  List listOfAssessments(int iTariffGroupId){
    try {
      return EntityFinder.findAllByColumn(((com.idega.block.finance.data.AssessmentRoundHome)com.idega.data.IDOLookup.getHomeLegacy(AssessmentRound.class)).createLegacy(),com.idega.block.finance.data.AssessmentRoundBMPBean.getColumnTariffGroupId(),iTariffGroupId);
    }
    catch (SQLException ex) {
    }
    return null;
  }

  public  List listOfAssessmentInfo(int iCategory,int iTariffGroupId){
    try {
      StringBuffer sql = new StringBuffer("select * from ");
      sql.append(com.idega.block.finance.data.RoundInfoBMPBean.getEntityTableName());
      if(iCategory > 0 || iTariffGroupId >0 )
      sql.append(" where ");
      if(iCategory > 0)
        sql.append(com.idega.block.finance.data.RoundInfoBMPBean.getColumnCategoryId()).append(" = ").append(iCategory);
      if(iTariffGroupId >0)
        sql.append(" and ").append(com.idega.block.finance.data.RoundInfoBMPBean.getColumnGroupId()).append(" = ").append(iTariffGroupId);

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
       return EntityFinder.findAllByColumn(((com.idega.block.finance.data.AccountKeyHome)com.idega.data.IDOLookup.getHomeLegacy(AccountKey.class)).createLegacy(),com.idega.block.finance.data.AccountKeyBMPBean.getColumnCategoryId(),iCategoryId);
      }
      catch(SQLException e){

      }
      return null;
  }

  public  Collection listOfTariffs(int iGroupId){
     try{
       return ((TariffHome) com.idega.data.IDOLookup.getHome(Tariff.class)).findAllByColumn(TariffBMPBean.getColumnTariffGroup(),iGroupId);

      }
      catch(Exception e){

      }
      return null;
  }

  public  Collection listOfTariffsByAttribute(String attribute){
     try{
      return ((TariffHome) IDOLookup.getHome(Tariff.class)).findAllByColumn(TariffBMPBean.getColumnAttribute(),attribute);

      }
      catch(Exception e){

      }
      return null;
  }

  public  List listOfTariffGroups(int iCategoryId){
     try{
       return EntityFinder.findAllByColumn(((com.idega.block.finance.data.TariffGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TariffGroup.class)).createLegacy(),com.idega.block.finance.data.TariffGroupBMPBean.getColumnCategoryId(),iCategoryId);
      }
      catch(SQLException e){
        e.printStackTrace();
      }
      return null;
  }

   public  List listOfTariffGroupsWithHandlers(int iCategoryId){
     try{
        StringBuffer sql = new StringBuffer("select * from ");
        sql.append(com.idega.block.finance.data.TariffGroupBMPBean.getEntityTableName());
        sql.append(" where ");
        sql.append(com.idega.block.finance.data.TariffGroupBMPBean.getColumnHandlerId());
        sql.append(" > 0 ");
        sql.append(" and " );
        sql.append(com.idega.block.finance.data.TariffGroupBMPBean.getColumnCategoryId());
        sql.append(" = ");
        sql.append(iCategoryId);
       return EntityFinder.findAll(((com.idega.block.finance.data.TariffGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TariffGroup.class)).createLegacy(),sql.toString());
      }
      catch(SQLException e){

      }
      return null;
  }

  public  List listOfTariffGroupsWithOutHandlers(int iCategoryId){
     try{
        StringBuffer sql = new StringBuffer("select * from ");
        sql.append(com.idega.block.finance.data.TariffGroupBMPBean.getEntityTableName());
        sql.append(" where ");
        sql.append(com.idega.block.finance.data.TariffGroupBMPBean.getColumnHandlerId());
        sql.append(" is null ");
        sql.append(" and " );
        sql.append(com.idega.block.finance.data.TariffGroupBMPBean.getColumnCategoryId());
        sql.append(" = ");
        sql.append(iCategoryId);
       return EntityFinder.findAll(((com.idega.block.finance.data.TariffGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TariffGroup.class)).createLegacy(),sql.toString());
      }
      catch(SQLException e){

      }
      return null;
  }

  public  FinanceHandler getFinanceHandler(int iHandlerId){
    try {
      FinanceHandlerInfo handler = ((com.idega.block.finance.data.FinanceHandlerInfoHome)com.idega.data.IDOLookup.getHomeLegacy(FinanceHandlerInfo.class)).findByPrimaryKeyLegacy(iHandlerId);
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
        return ((com.idega.block.finance.data.TariffGroupHome)com.idega.data.IDOLookup.getHomeLegacy(TariffGroup.class)).findByPrimaryKeyLegacy(iGroupId);
      }
      catch (SQLException ex) {

      }
    }
    return null;
  }

  public int countAccounts(int iCategory,String type){
    /*try {
      AccountHome aHome = (AccountHome) IDOLookup.getHome(Account.class);
      //return aHome.
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return 0;
  }

  public  Collection searchAccounts(String id,String first,String middle,String last,String type,int iCategoryId){
    try{
      return getAccountHome().findBySearch(id,first,middle,last,type,iCategoryId);
    }
    catch(Exception ex){}
    return null;
  }

   public  List searchAccountUsers(String first,String middle,String last){
    System.err.println("names: "+first+" ,"+middle+","+last);
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append("ic_user u ");
    boolean isfirst = true;
    if(first != null || middle !=null || last !=null){
      sql.append(" where ");
      if(first !=null && !"".equals(first)){
        if(!isfirst)
          sql.append(" and ");
        sql.append(" u.first_name like '%");
        sql.append(first);
        sql.append("%' ");
        isfirst = false;
      }
      if(middle !=null && !"".equals(middle)){
        if(!isfirst)
          sql.append(" and ");
        sql.append(" u.middle_name like '%");
        sql.append(middle);
        sql.append("%' ");
        isfirst = false;
      }
      if(last !=null && !"".equals(last )){
        if(!isfirst)
          sql.append(" and ");
        sql.append(" u.last_name like '%");
        sql.append(last);
        sql.append("%' ");
        isfirst = false;
      }
      System.err.println(sql.toString());
      try {
				return EntityFinder.getInstance().findAll(User.class,sql.toString());
        //return EntityFinder.findAll(((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).createLegacy(),sql.toString());
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return null;

  }

  public  Account getAccount(int id){
    return (Account)com.idega.block.finance.data.AccountBMPBean.getEntityInstance(Account.class,id);
  }

  public FinanceAccount getFinanceAccount(int id){
    FinanceAccount account = getAccountInfo(id);
    if(account==null)
      account = getAccount(id);
    return account;
  }

  public  AccountInfo getAccountInfo(int id){
    try {
      List accountInfo = EntityFinder.getInstance().findAllByColumn(AccountInfo.class,com.idega.block.finance.data.AccountInfoBMPBean.getColumnAccountId(),id);
      if(accountInfo !=null && accountInfo.size() >0)
        return (AccountInfo) accountInfo.get(0);
    }
    catch (IDOFinderException ex) {

    }
    return null;
  }

  public  User getUser(int id){
    return (User) com.idega.core.user.data.UserBMPBean.getEntityInstance(User.class,id);
  }

  public  TariffIndex getTariffIndex(int iTariffIndexId){
     if(iTariffIndexId > 0){
      try {
        return ((com.idega.block.finance.data.TariffIndexHome)com.idega.data.IDOLookup.getHomeLegacy(TariffIndex.class)).findByPrimaryKeyLegacy(iTariffIndexId);
      }
      catch (SQLException ex) {

      }
    }
    return null;
  }

  public  TariffIndex getTariffIndex(String type,int iCategoryId){
    TariffIndex ti = ((com.idega.block.finance.data.TariffIndexHome)com.idega.data.IDOLookup.getHomeLegacy(TariffIndex.class)).createLegacy();
    try {
      StringBuffer sql = new StringBuffer(" select * from ");
      sql.append(ti.getEntityName());
      sql.append(" where ");
      sql.append(com.idega.block.finance.data.TariffIndexBMPBean.getColumnNameType());
      sql.append( " = '");
      sql.append(type);
      sql.append("' and ");
      sql.append(com.idega.block.finance.data.TariffIndexBMPBean.getColumnCategoryId());
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
    Collection tariffs = this.listOfTariffsByAttribute(attribute);
    if(tariffs != null ){
      tar = new Hashtable();
      java.util.Iterator iter = tariffs.iterator();
      Tariff t;
      Integer acckey;
      Integer tarkey;
      while(iter.hasNext()){
        t = (Tariff) iter.next();
        try{
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
        catch(java.rmi.RemoteException ex)
        {}
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
      return EntityFinder.findAll(((com.idega.block.finance.data.AccountKeyHome)com.idega.data.IDOLookup.getHomeLegacy(AccountKey.class)).createLegacy());
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public  List listOfTariffKeys(){
    try {
      return EntityFinder.findAll(((com.idega.block.finance.data.TariffKeyHome)com.idega.data.IDOLookup.getHomeLegacy(TariffKey.class)).createLegacy());
    }
    catch (SQLException ex) {
      return null;
    }
  }

  public  List listOfAssessmentAccountEntries(int iAccountId,int iAssessmentId){
    try {
      StringBuffer sql = new StringBuffer("select * from ");
      sql.append(com.idega.block.finance.data.AccountEntryBMPBean.getEntityTableName());
      sql.append(" where ").append(com.idega.block.finance.data.AccountEntryBMPBean.getAccountIdColumnName()).append(" = ").append(iAccountId);
      sql.append(" and ").append(com.idega.block.finance.data.AccountEntryBMPBean.getRoundIdColumnName()).append( " = " ).append(iAssessmentId);
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
      sql.append(com.idega.block.finance.data.AccountInfoBMPBean.getEntityTableName());
      sql.append(" where ").append(com.idega.block.finance.data.AccountInfoBMPBean.getColumnType()).append(" = '").append(com.idega.block.finance.data.AccountBMPBean.typeFinancial).append("'");
      sql.append(" and ").append(com.idega.block.finance.data.AccountInfoBMPBean.getColumnUserId()).append(" = ").append(iUserId);
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
        sql.append(com.idega.block.finance.data.AccountBMPBean.getEntityTableName());
        sql.append(" where ").append(com.idega.block.finance.data.AccountBMPBean.getTypeColumnName()).append(" = '").append(com.idega.block.finance.data.AccountBMPBean.typePhone).append("' ");
        sql.append(" and ").append(com.idega.block.finance.data.AccountBMPBean.getUserIdColumnName()).append(" = ").append(iUserId);

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
        return EntityFinder.getInstance().findAllByColumn(Account.class,com.idega.block.finance.data.AccountBMPBean.getUserIdColumnName(),iUserId);
      }
      catch (IDOFinderException ex) {
        ex.printStackTrace();
      }
    }
    return null;

  }

   public List listOfAccountByUserIdAndType(int iUserId,String type){
    if(iUserId > 0){
      try {
        StringBuffer sql = new StringBuffer("select * from ");
        sql.append(com.idega.block.finance.data.AccountBMPBean.getEntityTableName());
        sql.append(" where ");
        sql.append(com.idega.block.finance.data.AccountBMPBean.getUserIdColumnName());
        sql.append( " = ");
        sql.append(iUserId);
        sql.append(" and ");
        sql.append(com.idega.block.finance.data.AccountBMPBean.getTypeColumnName());
        sql.append(" = ");
        sql.append("'");
        sql.append(type);
        sql.append("'");
        return EntityFinder.getInstance().findAll(Account.class,sql.toString());
      }
      catch (IDOFinderException ex) {
        ex.printStackTrace();
      }
    }
    return new Vector();

  }

  public List listOfAccountInfoByCategoryId(int iCategoryId){
    if(iCategoryId > 0){
      try {
        return EntityFinder.getInstance().findAllByColumn(AccountInfo.class,com.idega.block.finance.data.AccountInfoBMPBean.getColumnCategoryId(),iCategoryId);
      }
      catch (IDOFinderException ex) {
        ex.printStackTrace();
      }
    }
    return null;

  }

 public List listOfAccountInfoByUserId(int iUserId){
    if(iUserId > 0){
      try {
        return EntityFinder.getInstance().findAllByColumn(AccountInfo.class,com.idega.block.finance.data.AccountInfoBMPBean.getColumnUserId(),iUserId);
      }
      catch (IDOFinderException ex) {
        ex.printStackTrace();
      }
    }
    return null;

  }

  public List listOfAccountInfoByUserIdAndType(int iUserId,String type){
    if(iUserId > 0){
      try {
        StringBuffer sql = new StringBuffer("select * from ");
        sql.append(com.idega.block.finance.data.AccountInfoBMPBean.getEntityTableName());
        sql.append(" where ");
        sql.append(com.idega.block.finance.data.AccountInfoBMPBean.getColumnUserId());
        sql.append( " = ");
        sql.append(iUserId);
        sql.append(" and ");
        sql.append(com.idega.block.finance.data.AccountInfoBMPBean.getColumnType());
        sql.append(" = ");
        sql.append("'");
        sql.append(type);
        sql.append("'");
        return EntityFinder.getInstance().findAll(AccountInfo.class,sql.toString());
      }
      catch (IDOFinderException ex) {
        ex.printStackTrace();
      }
    }
    return new Vector();

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

   public List listOfUnBilledPhoneEntries(int iAccountId,IWTimestamp from,IWTimestamp to){
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getEntityTableName());
    boolean where = false;
    if(iAccountId > 0){
      sql.append(" where ");
      where = true;
      sql.append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getColumnNameAccountId());
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
      sql.append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getColumnNamePhonedStamp());
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
      sql.append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getColumnNamePhonedStamp());
      sql.append(" <= '");
      sql.append(to.getSQLDate());
      sql.append(" 23:59:59'");
    }
    sql.append(" and ");
    sql.append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getColumnNameAccountEntryId());
    sql.append(" is null ");
    //System.err.println(sql.toString());
    List A = null;
    try{
        A = EntityFinder.findAll(((com.idega.block.finance.data.AccountPhoneEntryHome)com.idega.data.IDOLookup.getHomeLegacy(AccountPhoneEntry.class)).createLegacy(),sql.toString());
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


  public Collection listOfAccountsInAssessmentRound(int roundid){
    try{
      return getAccountHome().findByAssessmentRound(roundid);
    }
    catch(Exception ex){

    }
    return null;
  }

  public List listOfAccountUsersByRoundId(int roundId){
    StringBuffer sql = new StringBuffer("select distinct u.* ");
    sql.append(" from fin_account a,fin_acc_entry e,fin_assessment_round r ,ic_user u ");
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

  public AccountHome getAccountHome()throws java.rmi.RemoteException{
    return (AccountHome) IDOLookup.getHome(Account.class);
  }
  */
/*
  public List listOfPhoneEntriesInAssessment(){
    StringBuffer sql = new StringBuffer("select * from ");
    sql.append(com.idega.block.finance.data.AccountPhoneEntryBMPBean.getEntityTableName()).append(" e,");
    sql.append()
  }
*/

}// class FinanceFinder
