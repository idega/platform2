package com.idega.projects.golf.service;

import com.idega.presentation.*;
import com.idega.presentation.PresentationObject.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.projects.golf.entity.*;
import com.idega.projects.golf.templates.*;
import com.idega.projects.golf.*;
import com.idega.projects.*;
import com.idega.util.*;
import com.idega.util.text.*;
import com.idega.data.*;
import java.math.*;
import com.idega.jmodule.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.sql.*;
import java.io.*;
import java.util.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;


/**
*@author <a href="mailto:aron@idega.is">Aron Birkir</a>
*@version 1.0
*/

public class AccountViewer extends com.idega.presentation.PresentationObjectContainer {

  private String union_id,unionName,unionAbbrev,member_id;
  private int un_id,mem_id,cashier_id;
  private Union union;
  private String[][] Values;
  private Member thisMember,Cashier;
  private PriceCatalogue[] Catalogs;
  private String MenuColor,ItemColor,HeaderColor,LightColor,DarkColor,OtherColor,WhiteColor;
  private String KreditColor,DebetColor;
  private boolean isAdmin = false;
  private java.util.Locale currentLocale;
  private int cellspacing = 1, cellpadding = 2;
  private String sTablewidth = "650";
  private int  numOfCat, inputLines, saveCount,count,memberCount=0;
  private String sAction = "";
  private String prmString = "account_action";
  private Payment[] memberPayments;
  private String strMessage = "";
  private Account eAccount;
  private Member eMember;
  private Table Frame,MainFrame,Frame2;
  private NumberFormat NF ;
  private String styleAttribute = "font-size: 8pt";
  private String storage;

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.projects.golf.tariff";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public AccountViewer(){

    HeaderColor = "#336660";
    LightColor = "#CEDFD0";
    DarkColor = "#ADCAB1";
    OtherColor = "#6E9073";
    WhiteColor = "#FFFFFF";
    DebetColor = "0000FF";
    KreditColor = "#FF0000";

    setMenuColor("#ADCAB1");//,"#CEDFD0"
    setItemColor("#CEDFD0");//"#D0F0D0"
    currentLocale = java.util.Locale.getDefault();
    NF = java.text.NumberFormat.getInstance();
  }

  public void setMenuColor(String MenuColor){
    this.MenuColor = MenuColor;
  }
  public void setItemColor(String ItemColor){
    this.ItemColor = ItemColor;
  }

  private void control(IWContext iwc){

    try{
      if(iwc.getParameter("member_id") != null){
        member_id = iwc.getParameter("member_id");
        iwc.getSession().setAttribute("account_member_id",member_id);
      }

      union_id = (String)  iwc.getSession().getAttribute("golf_union_id");
      member_id = (String) iwc.getSession().getAttribute("account_member_id");

      if(iwc.getSession().getAttribute("member_login")!= null){
       Cashier = (Member) iwc.getSession().getAttribute("member_login");
        cashier_id = Cashier.getID();
      }

      un_id = Integer.parseInt(union_id)  ;
      union = new Union(un_id);
      unionName = union.getName();
      unionAbbrev = union.getAbbrevation() ;

      if( member_id != null){
        mem_id = Integer.parseInt(member_id);
        eMember = new Member(mem_id);
      }
      else
        mem_id = -1;

      int accountid = TariffService.findAccountID(mem_id,un_id);
      if(accountid != -1)
        eAccount = new Account(accountid);
      else if(mem_id != -1){
        eAccount = new Account(TariffService.makeNewAccount(mem_id,this.un_id,eMember.getName(),this.cashier_id));
      }

      strMessage = "";

      if(iwc.getRequest().getParameter(prmString) == null){
        doMain(iwc);
      }
      if(iwc.getParameter("deleteall.x")!=null){
        doDeleteAll(iwc);
      }
      else if(iwc.getParameter("payall.x")!=null){
        this.doPayAll(iwc);
      }
      else if(iwc.getParameter("updatepay.x")!=null){
        this.doUpdatePay(iwc);
      }
      else if(iwc.getParameter("makenew.x")!=null){
        this.doMakeNew(iwc);
      }
      else if(iwc.getRequest().getParameter(prmString) != null){
        sAction = iwc.getRequest().getParameter(prmString);
        if(sAction.equals("main"))	        { doMain(iwc);      }
        else if(sAction.equals("change"))	{ doChange(iwc); 	}
        else if(sAction.equals("clearaccount")) { doClearAccount(iwc);}
        else if(sAction.equals("update"))	{ doUpdate(iwc); 	}
        else if(sAction.equals("view"))	        { doView(iwc); 	}
        else if(sAction.equals("save"))	        { doSave(iwc); 	}
        else if(sAction.equals("calc"))	        { doCalc(iwc); 	}
        else if(sAction.equals("tariffs"))	{ doTariff(iwc); 	}
        else if(sAction.equals("new"))	        { 	}
        else if(sAction.equals("updatenew"))	{ doUpdateNew(iwc); }
        else if(sAction.equals("paychange"))	{ doChange(iwc);    }
      }
    }
    catch(SQLException S){	S.printStackTrace();	}
    catch(Exception s){ }
    }

    private void doMain(IWContext iwc) throws SQLException {
      makeMainFrame();
      makeFrame();
      makeFrame2();
      addLinks(makeLinkTable(2));
      addHead(makeViewTable());
      addFrames();
      add(MainFrame);
    }

    private void doTariff(IWContext iwc){
      makeMainFrame();
      makeFrame();
      makeFrame2();
      addLinks(makeLinkTable(3));
      addHead(this.makeHeaderTable());
      addMain(this.makeTarifViewTable());
      addRight(this.makeTariffTable());
      addFrames();
      add(MainFrame);
    }

    private void makeMainFrame(){
      MainFrame = new Table(4,3);
      MainFrame.setRowAlignment(2,"top");
      MainFrame.setWidth(1,"70");
      MainFrame.setWidth(2,"450");
      MainFrame.setWidth(3,"20");
      MainFrame.setWidth(4,"250");
      MainFrame.setAlignment(4,3,"top");
      MainFrame.setCellspacing(0);
      MainFrame.setCellpadding(0);
    }

    private void addFrames(){
      MainFrame.add(Frame,2,3);
      MainFrame.add(Frame2,4,3);
    }

    private void makeFrame(){
      Frame = new Table(1,3);
      Frame.setCellspacing(0);
      Frame.setCellpadding(0);
      Frame.setWidth("100%");
      Frame.setHeight("100%");
    }

    private void makeFrame2(){
      Frame2 = new Table(1,2);
      Frame2.setCellspacing(0);
      Frame2.setCellpadding(0);
      Frame2.setWidth("100%");
      Frame2.setHeight("100%");
    }

    private void addMain(PresentationObject T){
      Frame.add(T,1,3);
    }

    private void addHead(PresentationObject T){
      Frame.add(T,1,1);
    }

    private void addRight(PresentationObject T){
      Frame2.add(T,1,1);
    }

    private void addLinks(PresentationObject T){
      MainFrame.add(T,2,2);
    }

    private void doChange(IWContext iwc) throws SQLException{
      String sPaymId = iwc.getRequest().getParameter("payid");
      Payment P = null;
      if(sPaymId!=null)
      try{
        P = new Payment(Integer.parseInt(sPaymId));
      }
      catch(SQLException sql){}

      makeMainFrame();
      makeFrame();
      makeFrame2();
      addLinks(makeLinkTable(3));
      addHead(this.makeHeaderTable());
      if(P!=null)
        addMain(this.makeTarifViewTable(P));
      else
        addMain(this.makeTarifViewTable());
      addRight(this.makeTariffTable());
      addFrames();
      add(MainFrame);
    }

    private void doUpdate(IWContext iwc) throws SQLException{
      String strPaymID,strPrice,strPaytype,strDescr,strChkPaid,strChkUnPaid,strChkDel;
      strPaymID = iwc.getRequest().getParameter("payment_id");
      strPrice = iwc.getRequest().getParameter("payment_iprice");
      strDescr = iwc.getRequest().getParameter("payment_idesc");
      strPaytype = iwc.getRequest().getParameter("payment_ipaytype");
      strChkPaid = iwc.getRequest().getParameter("payment_ichkpaid");
      strChkUnPaid = iwc.getRequest().getParameter("payment_ichkunpaid");
      strChkDel = iwc.getRequest().getParameter("payment_ichkdel");

      int pm_id,price,pt_id;
      if(strChkDel != null && strChkDel.equalsIgnoreCase("true")){}
      if( strPrice != null && strPaymID  != null && strPaytype != null ){
        pm_id = Integer.parseInt(strPaymID);
        price = Integer.parseInt(strPrice);
        pt_id = Integer.parseInt(strPaytype);

        Payment P = new Payment(pm_id);
        if(strChkDel != null && strChkDel.equalsIgnoreCase("true")){
        try{
          P.delete();
        }
        catch(SQLException e){
          String av_msg1 = iwrb.getLocalizedString("av_msg1"," Payment could not be erased");
          strMessage = av_msg1;}
        }
        else{
        if(strChkPaid != null && strChkPaid.equalsIgnoreCase("true"))
          P.setStatus(true);
        if(strChkUnPaid != null && strChkUnPaid.equalsIgnoreCase("true"))
          P.setStatus(false);
        P.setPrice(price);
        P.setExtraInfo(strDescr);
        P.setPaymentTypeID(pt_id);
        try{
          P.update();
        }
        catch(SQLException e){
          String av_msg2 = iwrb.getLocalizedString("av_msg2"," Payment could not be changed");
          strMessage = av_msg2;}
      }
      }
      this.doMain(iwc);

    }
    private void doView(IWContext iwc) throws SQLException{

    }
    private void doSave(IWContext iwc) throws SQLException{

    }
    private void doUpdatePay(IWContext iwc) throws SQLException{
      String sPaymentId   = iwc.getParameter("account_oldpayid");
      String sPay         = iwc.getParameter("payment_ichkpaid");
      String sUpdate      = iwc.getParameter("payment_ichkupdate");
      String sDelete      = iwc.getParameter("payment_ichkdel");
      String sDescription = iwc.getParameter(this.getDscPrm());
      String sPrice       = iwc.getParameter(this.getPrcPrm());
      String sPayDate     = iwc.getParameter(this.getDtPrm());
      String sPayTypeId   = iwc.getParameter(this.getPTPrm());

      int iPaymentId = Integer.parseInt(sPaymentId);
      Payment ePayment;
      try{
        ePayment = new Payment(iPaymentId);
      }
      catch(SQLException sql){ePayment = null;}

      if(ePayment != null){
        idegaTimestamp itPayDate = this.parseStamp(sPayDate);
        idegaTimestamp itPaymentDate = new idegaTimestamp(ePayment.getPaymentDate());
        int iPrice = Integer.parseInt(sPrice);
        int iOldPrice = ePayment.getPrice();
        int iPayTypeId = Integer.parseInt(sPayTypeId);
        if(ePayment.getPrice() != iPrice)
          ePayment.setPrice(iPrice);
        if(ePayment.getName() != sDescription)
          ePayment.setName(sDescription);
        if(ePayment.getPaymentTypeID() != iPayTypeId)
          ePayment.setPaymentTypeID(iPayTypeId);
        if(itPaymentDate.getISLDate(".",true) != itPayDate.getISLDate(".",true))
          ePayment.setPaymentDate(itPayDate.getTimestamp());

        if(sPay != null && sPay.equalsIgnoreCase("true")){
          String sInfo = "Greiðsla";
          String sInfo2 = "Leiðrétting";
          int iPriceChange = iOldPrice - iPrice;
          ePayment.setStatus(true);
          try{
            TariffService.makeAccountEntry(this.eAccount.getID(),ePayment.getPrice(),ePayment.getName(),sInfo,"","","",this.cashier_id,ePayment.getPaymentDate(),idegaTimestamp.getTimestampRightNow());
            if(iPriceChange != 0)
              TariffService.makeAccountEntry(this.eAccount.getID(),iPriceChange,ePayment.getName(),sInfo2,"","","",this.cashier_id,ePayment.getPaymentDate(),idegaTimestamp.getTimestampRightNow());
            ePayment.update() ;
          }
          catch(SQLException sql){sql.printStackTrace();}
        }
        else if(sUpdate != null && sUpdate.equalsIgnoreCase("true")){
          String sInfo = "Leiðrétting";
          int iPriceChange = iOldPrice - iPrice;
          try{
            TariffService.makeAccountEntry(this.eAccount.getID(),iPriceChange,ePayment.getName(),sInfo,"","","",this.cashier_id,ePayment.getPaymentDate(),idegaTimestamp.getTimestampRightNow());
            ePayment.update();
          }
          catch(SQLException sql){sql.printStackTrace();}
        }
        else if(sDelete != null && sDelete.equalsIgnoreCase("true")){
          String sInfo = "NiðurFelling";
          try{
            ePayment.delete();
            TariffService.makeAccountEntry(this.eAccount.getID(),iOldPrice,ePayment.getName(),sInfo,"","","",this.cashier_id,ePayment.getPaymentDate(),idegaTimestamp.getTimestampRightNow());

          }
          catch(SQLException sql){sql.printStackTrace();}
        }
        this.doTariff(iwc);
      }
    }

    private void doMakeNew(IWContext iwc) throws SQLException{
      String sCatIds = iwc.getParameter(this.getIDsPrm());
      String sDescr = iwc.getParameter(this.getDscPrm());
      String sPrice = iwc.getParameter(this.getPrcPrm());
      String sDate = iwc.getParameter(this.getDtPrm());
      String sCost = iwc.getParameter(this.getCostprm());
      String sInterest = iwc.getParameter(this.getInterestprm());
      int totalprice = 0;
      int iCost = 0;
      double dInterest = 0.0;
      if( !sInterest.equalsIgnoreCase("")){
            dInterest = Double.parseDouble(sInterest);
      }
      if( !sCost.equalsIgnoreCase("")){
        iCost = Integer.parseInt(sCost);
      }

      int[] iCats;
      if(sCatIds.length() > 0){
        StringTokenizer ST = new StringTokenizer(sCatIds,"#");
        int count = ST.countTokens(), i = 0;
        iCats = new int[count];
        int itemp = 0;
        String stemp;
        while( ST.hasMoreTokens()){
          //iCats[i] = Integer.parseInt(ST.nextToken());
          itemp = Integer.parseInt(ST.nextToken());
          stemp = iwc.getParameter(this.getChkPrm()+itemp);
          if(stemp != null && stemp.equalsIgnoreCase("true")){
            try{
              PriceCatalogue P = new PriceCatalogue(itemp);
              TariffService.makeAccountEntry(this.eAccount.getID() ,-P.getPrice(),P.getName(),"Álagning","","","",0,idegaTimestamp.getTimestampRightNow(),idegaTimestamp.getTimestampRightNow());
              totalprice += P.getPrice();
             }
            catch(SQLException e){}
          }
          i++;
        }
      }
      int iPrice = 0;
      String sName;
      idegaTimestamp idPayDate = new idegaTimestamp();
      if(  sDescr != null && sDescr.length() > 0 ){
        if( sPrice != null && sPrice.length() > 0){
          if(sDate!= null && sDate.length() > 0){
            idPayDate = parseStamp(sDate);
          }
          try{
            iPrice = Integer.parseInt(sPrice);
            //add("<br>"+(-iPrice)+sDescr+idPayDate.toSQLString());
            TariffService.makeAccountEntry(this.eAccount.getID() ,-iPrice,sDescr,"Álagning","","","",0,idPayDate.getTimestamp(),idegaTimestamp.getTimestampRightNow());
            totalprice += iPrice;
          }
          catch(NumberFormatException nfe){ nfe.printStackTrace(); }
          //catch(SQLException sql){ sql.printStackTrace();}
        }
      }
      if(totalprice != 0){
        int iInst = Integer.parseInt( iwc.getParameter(this.getInstPrm()));
        int iType = Integer.parseInt( iwc.getParameter(this.getPTPrm()));
        String sdate = iwc.getParameter(this.getDtPrm());
        idPayDate = parseStamp(sdate);

        double Multi = dInterest/100 ;
        int totaladd = (int) Math.floor(totalprice * (Multi));
        totaladd += iCost;
        totalprice += totaladd;
        if(totaladd > 0){
          TariffService.makeAccountEntry(this.eAccount.getID() ,-totaladd,"Kostnaður","Álagning","","","",0,idegaTimestamp.getTimestampRightNow(),idegaTimestamp.getTimestampRightNow());
        }
        if(iInst > 0){
          try{
            PaymentRound payround = new PaymentRound();
            payround.setName("Auka");
            payround.setRoundDate(idegaTimestamp.getTimestampRightNow());
            payround.setTotals(totalprice);
            payround.setUnionId(this.un_id);
            payround.insert();
            int payRoundId = payround.getID();
            int tempPrice;
            for(int k = 0; k < iInst ; k++){
              tempPrice = totalprice/iInst;
              if(k == 0)
                tempPrice += totalprice%iInst;

              TariffService.makePayment(this.eMember.getID() , this.un_id,payRoundId,tempPrice,false,"","",k+1,iInst,iType,new idegaTimestamp(idPayDate.getDay(),idPayDate.getMonth() +k,idPayDate.getYear()).getTimestamp(), idegaTimestamp.getTimestampRightNow(),3);
            }
          }
          catch(SQLException sql){

          }
        }
      }

      this.doTariff(iwc);
    }

    private idegaTimestamp parseStamp(String sDate){
       idegaTimestamp it = new idegaTimestamp();
       try{
        StringTokenizer st = new StringTokenizer(sDate," .-/+");
        int day = 1,month = 1,year = 2001;
        if(st.hasMoreTokens()){
          day = Integer.parseInt(st.nextToken());
          month = Integer.parseInt(st.nextToken());
          year = Integer.parseInt(st.nextToken());

        }
        it = new idegaTimestamp(day,month,year);
      }
      catch(Exception pe){ it = new idegaTimestamp();}
      return it;
    }

    private void doUpdateNew(IWContext iwc) throws SQLException{
      DecimalFormat Formatter = new DecimalFormat("00");

      String strPrice,strPaytype,strDescr,strIfRoundRel,strRoundId,strInst;
      strPrice = iwc.getRequest().getParameter("payment_iprice");
      strDescr = iwc.getRequest().getParameter("payment_idesc");
      strPaytype = iwc.getRequest().getParameter("payment_ipaytype");
      strIfRoundRel = iwc.getRequest().getParameter("payment_roundrel");
      strRoundId = iwc.getRequest().getParameter("payment_irounds");
      strInst = iwc.getRequest().getParameter("payment_installments");
      int iday = Integer.parseInt(iwc.getRequest().getParameter("payment_day"));
      int imonth = Integer.parseInt(iwc.getRequest().getParameter("payment_month"));
      int iyear = Integer.parseInt(iwc.getRequest().getParameter("payment_year"));

      int inst = Integer.parseInt(strInst);
      int pm_id,price,pt_id;
      if( strPrice != null &&  strPaytype != null ){
        price = Integer.parseInt(strPrice);
        int payRoundId = -1;
        if(strIfRoundRel != null && strIfRoundRel.equalsIgnoreCase("true")){
          if(strIfRoundRel != null && strIfRoundRel.equalsIgnoreCase("true")){
            payRoundId = Integer.parseInt(strRoundId);
            PaymentRound pr = new PaymentRound(payRoundId);
            int prTotals = pr.getTotals();
            pr.setTotals(prTotals+price);
            pr.update();
          }
        }
        else{
          PaymentRound payround = new PaymentRound();
          payround.setName("Auka");
          payround.setRoundDate(idegaTimestamp.getTimestampRightNow());
          payround.setTotals(price);
          payround.setUnionId(this.un_id);
          payround.insert();
          payRoundId = payround.getID();
        }
        if(payRoundId != -1){
          pt_id = Integer.parseInt(strPaytype);
          for(int i = 0; i < inst ; i++){
          Payment P = new Payment();
            P.setMemberId(this.mem_id);
            P.setPriceCatalogueId(0);
            P.setPaymentDate(new idegaTimestamp(iday,imonth+i,iyear).getTimestamp());
            P.setLastUpdated(idegaTimestamp.getTimestampRightNow());
            P.setCashierId(cashier_id);
            P.setStatus(false);
            P.setExtraInfo(strDescr);
            P.setPaymentTypeID(pt_id);
            if(i==0)
              P.setPrice(price/inst + price%inst);
            else
              P.setPrice(price/inst);
            P.setInstallmentNr(i+1);
            P.setTotalInstallment(inst);
            P.setRoundId(payRoundId);
            try{
              P.insert();
            }
            catch(SQLException e){
              e.printStackTrace();
              String av_msg2 = iwrb.getLocalizedString("av_msg2"," Payment could not be changed");
              strMessage = av_msg2;
            }
          }
        }
      }
     this.doMain(iwc);
    }

    private void doClearAccount(IWContext iwc){
      if(this.eAccount.getBalance()==0){
      AccountEntry[] E = TariffService.getAccountEntrys(this.eAccount.getID());
      for (int i = 0; i < E.length; i++) {
        try {
          E[i].delete();
        }
        catch (SQLException ex) {

        }
      }
      }
      doTariff(iwc);
    }

    private void doCalc(IWContext iwc) throws SQLException{
      try{
        idegaTimestamp accountLastUpd = new idegaTimestamp(eAccount.getLastUpdated());
        AccountEntry[] E = TariffService.getAccountEntrys(this.eAccount.getID());
        if(this.eAccount.getBalance() == 0){
          eAccount.setBalance(TariffService.calculateBalance(E));
        }
        else{
          int len = E.length;
          int i = 0;
          for( i = 0;i < len;i++){
            idegaTimestamp entryLastUpd = new idegaTimestamp(E[0].getLastUpdated());
            if(entryLastUpd.isLaterThan(accountLastUpd))
              break;
          }
          if(i!=0){
            AccountEntry[] E2 = new AccountEntry[i];
            for(int j = 0; j < i ; j++){
              E2[j] = E[j];
            }
            eAccount.setBalance(TariffService.calculateBalance(E2));
          }
          else
            eAccount.setBalance(TariffService.calculateBalance(E));
        }
        eAccount.update();
      }
      catch(SQLException sql){ ;
      }
      this.doMain(iwc);
    }
    private void doDeleteAll(IWContext iwc){
      int pCount = Integer.parseInt(iwc.getParameter("payment_totalpaydel"));
      String sInfo = "NiðurFelling";
      int totalprice = 0;
      Timestamp today = idegaTimestamp.getTimestampRightNow();
      Timestamp lastpaydate = today;
      String name = "";
     for (int i = 0; i < pCount; i++) {
        if(iwc.getParameter("payment_delchk"+i)!=null){
          int id = Integer.parseInt(iwc.getParameter("payment_delchk"+i));
           Payment ePayment = null;
          try{
            ePayment = new Payment(id);
          }
          catch(SQLException sql){ePayment = null;}
          if(ePayment !=null){
            try{
              if(!ePayment.getStatus())
                totalprice += ePayment.getPrice();
              lastpaydate = ePayment.getPaymentDate();
              name = ePayment.getName();
              ePayment.delete();
            }
            catch(SQLException sql){sql.printStackTrace();}
          }
        }
      }
      if(totalprice > 0){
        try {
          TariffService.makeAccountEntry( this.eAccount.getID(),totalprice,name,
                          sInfo,"","","",this.cashier_id,lastpaydate,today);
        }
        catch (SQLException ex) {        }

      }
      this.doTariff(iwc);
    }

    private void doPayAll(IWContext iwc){
      int pCount = Integer.parseInt(iwc.getParameter("payment_totalpaydel"));
      String sInfo = "Greiðsla";
      int totalprice = 0;
      Timestamp today = idegaTimestamp.getTimestampRightNow();
      Timestamp lastpaydate = today;
      String name = "";
      for (int i = 0; i < pCount; i++) {
        if(iwc.getParameter("payment_delchk"+i)!=null){
          int id = Integer.parseInt(iwc.getParameter("payment_delchk"+i));
           Payment ePayment = null;
          try{
            ePayment = new Payment(id);
          }
          catch(SQLException sql){ePayment = null;}
          if(ePayment !=null){
            try{
              totalprice += ePayment.getPrice();
              lastpaydate = ePayment.getPaymentDate();
              name = ePayment.getName();
              ePayment.setStatus(true);
              ePayment.update();
            }
            catch(SQLException sql){sql.printStackTrace();}
          }
        }
      }
      if(totalprice > 0){
        try {
          TariffService.makeAccountEntry( this.eAccount.getID(),totalprice,name,
                          sInfo,"","","",this.cashier_id,lastpaydate,today);
        }
        catch (SQLException ex) {        }

      }
      this.doTariff(iwc);
    }

    private Table makeViewTable(){
      Table T = new Table(1,1);
      T.setColor(this.HeaderColor);
      //T.setBorder(1);
      //T.setColor(1,1,WhiteColor);
      T.setCellpadding(1);
      T.setCellspacing(1);
      Table T2 = new Table(1,3);
      T2.setColor(this.WhiteColor);
      T2.setCellpadding(4);
      T2.setCellspacing(0);
      T2.add(makeAccountTable(),1,1);
      T2.addBreak(1,2);
      T2.add(makeEntryTable(TariffService.getAccountEntrys(eAccount.getID())),1,3);
      T2.addBreak(1,3);
      Link L = new Link(new Image("/pics/tarif/small/uppfaera.gif"));
      L.addParameter(prmString,"calc");
      T2.add(L,1,3);
      T.add(T2);
      return T;
    }

    private Table makeTarifViewTable(){
      Table T = new Table(1,1);
      T.setWidth("100%");
      T.setColor(this.HeaderColor);
      //T.setBorder(1);
      //T.setColor(1,1,WhiteColor);
      T.setCellpadding(1);
      T.setCellspacing(1);
      T.setHeight("100%");
      Table T2 = new Table(1,2);
      T2.setWidth("100%");
      //T2.setBorder(1);
      T2.setColor(this.WhiteColor);
      T2.setCellpadding(4);
      T2.setCellspacing(0);
      T2.add(this.makeTariffEntriesTable(TariffService.getTariffEntrys(eAccount.getID())),1,1);
      T2.add(this.makePaymentsTable(TariffService.getMemberPayments(this.mem_id,this.un_id)),1,2);

      T.add(T2);
      return T;
    }

     private Table makeTarifViewTable(Payment P){
      Table T = new Table(1,1);
      T.setWidth("100%");
      T.setColor(this.HeaderColor);
      //T.setBorder(1);
      //T.setColor(1,1,WhiteColor);
      T.setCellpadding(1);
      T.setCellspacing(1);
      T.setHeight("100%");
      Table T2 = new Table(1,2);
      T2.setWidth("100%");
      //T2.setBorder(1);
      T2.setColor(this.WhiteColor);
      T2.setCellpadding(4);
      T2.setCellspacing(0);
      T2.add(this.makePayChangeTable(P),1,1);
      T2.add(this.makePaymentsTable(TariffService.getMemberPayments(this.mem_id,this.un_id)),1,2);
      T.add(T2);
      return T;
    }

    private Table makeTariffTable(){
      Table T = new Table(1,1);
      T.setColor(this.HeaderColor);
      //T.setColor(1,1,WhiteColor);
      T.setCellpadding(1);
      T.setCellspacing(1);
      T.setWidth("100%");
      T.setRowAlignment(1,"top");
      //T.setHeight("100%");
      Table T2 = new Table(1,4);
      T2.setWidth("100%");

      T2.setColor(this.WhiteColor);
      T2.setCellpadding(4);
      T2.setCellspacing(0);
      T2.add(this.makeTariffChangeTable(TariffService.getMainCatalogues(this.union_id)),1,1);
      T2.add(makeNewTarifTable(),1,2);
      T2.add(this.makeAdjustTable(),1,3);
      T2.add(this.makeSubmitTable(),1,4);
      Form myForm = new Form();
      myForm.maintainAllParameters();
      myForm.add(T2);
      //myForm.add(new HiddenInput( this.prmString,"makenew"));
      myForm.add(new HiddenInput( this.getIDsPrm(),this.storage));
      T.add(myForm);
      return T;
    }

    private Table makeTariffChangeTable(PriceCatalogue[] catalogs){
      int tableDepth = catalogs.length+1;
      Table T2 = new Table(1,2);
      T2.setCellspacing(1);
      T2.setCellpadding(2);
      T2.setWidth("100%");
      //T2.setHeight("100%");
      Table T = new Table(3,tableDepth);
      T.setWidth("100%");
      //T.setWidth(1,"65");
      T.setCellspacing(0);
      T.setCellpadding(2);
      T.setColumnAlignment(1,"center");
      T.setColumnAlignment(2,"left");
      T.setColumnAlignment(3,"right");


      T.setHorizontalZebraColored(LightColor,WhiteColor);
      T.setRowColor(1,HeaderColor);

      String fontColor = WhiteColor;
      int fontSize = 1;
      String sTariffList = iwrb.getLocalizedString("tarifflist","List of Tariffs");
      String sChoice = iwrb.getLocalizedString("choice","Choice");
      String sDesc = iwrb.getLocalizedString("description","Description");
      String sAmount = iwrb.getLocalizedString("amount","Amount");
      Text Title = new Text(sTariffList,true,false,false);
      Title.setFontColor(HeaderColor);
      T2.add(Title,1,1);

      Text[] TableTitles = new Text[3];
      TableTitles[0] = new Text(sChoice);
      TableTitles[1] = new Text(sDesc);
      TableTitles[2] = new Text(sAmount);

      for(int i = 0 ; i < TableTitles.length;i++){
        TableTitles[i].setFontSize(fontSize);
        TableTitles[i].setFontColor(WhiteColor);
        T.add(TableTitles[i],i+1,1);
      }
      Text[] TableTexts = new Text[4];
      boolean debet = false;
      int total = 0;
      int len = catalogs.length;
      StringBuffer cats = new StringBuffer();
      for(int j = 0; j < len; j++){
        int catId = catalogs[j].getID();
        cats.append(catId);
        cats.append("#");
        CheckBox chk = new CheckBox(getChkPrm()+catId,"true");
        Text Description = new Text(catalogs[j].getName());
        Description.setFontSize(fontSize);
        Description.setFontColor("#000000");
        Text Price = new Text(NF.format(catalogs[j].getPrice()));
        Price.setFontColor("#000000");
        Price.setFontSize(fontSize);

        T.add(chk,1,j+2);
        T.add(Description,2,j+2);
        T.add(Price,3,j+2);
      }
      this.storage = cats.toString();
      T2.add(T,1,2);
      return T2;
    }

    private Table makeNewTarifTable(){
      Table T2 = new Table(1,2);
      T2.setCellspacing(1);
      T2.setCellpadding(2);
      T2.setWidth("100%");
      Table T = new Table(3,2);
      T.setWidth("100%");
      //T.setWidth(1,"65");
      T.setCellspacing(0);
      T.setCellpadding(2);
      T.setColumnAlignment(1,"left");
      T.setColumnAlignment(2,"left");
      T.setRowColor(1,HeaderColor);

      String fontColor = WhiteColor;
      int fontSize = 1;
      String sNewTariff = iwrb.getLocalizedString("newtariff","New tariff");
      Text Title = new Text(" Nýtt Gjald",true,false,false);
      Title.setFontColor(HeaderColor);
      T2.add(Title,1,1);

      String sDesc = iwrb.getLocalizedString("description","Description");
      String sAmount = iwrb.getLocalizedString("amount","Amount");

      Text[] TableTitles = new Text[2];
      TableTitles[0] = new Text(sDesc);
      TableTitles[1] = new Text(sAmount);

      for(int i = 0 ; i < TableTitles.length;i++){
        TableTitles[i].setFontSize(fontSize);
        TableTitles[i].setFontColor(WhiteColor);
        T.add(TableTitles[i],i+1,1);
      }

      TextInput Description  = new TextInput(this.getDscPrm());
      Description.setLength(20);
      Description.setAttribute("style",this.styleAttribute);
      IntegerInput Price = new IntegerInput(this.getPrcPrm());
      Price.setLength(5);
      Price.setAttribute("style",this.styleAttribute);
      TextInput PayDate = new TextInput(this.getDtPrm());
      PayDate.setLength(10);
      PayDate.setAttribute("style",this.styleAttribute);

      T.add(Description,1,2);
      T.add(Price,2,2);

      //T.add(PayDate,3,2);
      T2.add(T,1,2);
      return T2;
    }

     private Table makePayChangeTable(Payment P){
      Form myForm = new Form();
      myForm.maintainAllParameters();
      //myForm.add(new HiddenInput( this.prmString,"updatepay"));
      myForm.add(new HiddenInput("account_oldpayid",String.valueOf(P.getID())));
      Table T2 = new Table(1,3);
      T2.setCellspacing(1);
      T2.setCellpadding(2);
      T2.setWidth("100%");
      T2.setRowAlignment(3,"right");
      Table T = new Table(6,2);
      T.setWidth("100%");
      T.setCellspacing(0);
      T.setCellpadding(2);
      T.setColumnAlignment(1,"left");
      T.setColumnAlignment(2,"left");
      T.setColumnAlignment(3,"center");
      T.setColumnAlignment(4,"left");
      T.setColumnAlignment(5,"right");


      T.setRowColor(1,HeaderColor);

      int fontSize = 1;

      String sDesc = iwrb.getLocalizedString("description","Description");
      String sAmount = iwrb.getLocalizedString("amount","Amount");
      String sPart = iwrb.getLocalizedString("part","Part");
      String sType = iwrb.getLocalizedString("paytype","Paytype");
      String sDueDate = iwrb.getLocalizedString("duedate","Duedate");
      String sPayment = iwrb.getLocalizedString("payment","Payment");
      String sPay = iwrb.getLocalizedString("pay","Pay");
      String sDelete = iwrb.getLocalizedString("delete","Delete");
      String sUpdate = iwrb.getLocalizedString("update","Update");

      Text Title = new Text(sPayment,true,false,false);
      Title.setFontColor(HeaderColor);
      T2.add(Title,1,1);

      Text[] TableTitles = new Text[5];
      TableTitles[0] = new Text(sDueDate);
      TableTitles[1] = new Text(sType);
      TableTitles[2] = new Text(sPart);
      TableTitles[3] = new Text(sDesc);
      TableTitles[4] = new Text(sAmount);

      for(int i = 0 ; i < TableTitles.length;i++){
        TableTitles[i].setFontSize(fontSize);
        TableTitles[i].setFontColor(WhiteColor);
        T.add(TableTitles[i],i+1,1);
      }

      String sPrice = String.valueOf(P.getPrice());

      String name = (P.getName()!=null?P.getName():"");
      TextInput Description  = new TextInput(this.getDscPrm(),name);
      Description.setLength(20);
      Description.setAttribute("style",this.styleAttribute);
      IntegerInput Price = new IntegerInput(this.getPrcPrm(),P.getPrice());
      Price.setLength(5);
      Price.setAttribute("style",this.styleAttribute);
      TextInput PayDate = new TextInput(this.getDtPrm(),new idegaTimestamp(P.getPaymentDate()).getISLDate(".",true));
      PayDate.setLength(10);
      PayDate.setAttribute("style",this.styleAttribute);

      DropdownMenu drdPaytypes = new DropdownMenu(this.getPTPrm());
      for(int i = 1; i < 5; i++){ drdPaytypes.addMenuElement( String.valueOf(i),this.getPaymentType(i));  }
      drdPaytypes.setSelectedElement(String.valueOf(P.getPaymentTypeID()));
      drdPaytypes.setAttribute("style",this.styleAttribute);

      Text tPart = new Text(P.getInstallmentNr()+"/"+P.getTotalInstallment());
      tPart.setFontSize(fontSize);
      tPart.setFontColor(HeaderColor);
      Text tPay = new Text(sPay);
      tPay.setFontSize(fontSize);
      tPay.setFontColor(HeaderColor);
      Text tDel = new Text(sDelete);
      tDel.setFontSize(fontSize);
      tDel.setFontColor(HeaderColor);
      Text tUpd = new Text(sUpdate);
      tUpd.setFontSize(fontSize);
      tUpd.setFontColor(HeaderColor);

      CheckBox chkPay = new CheckBox("payment_ichkpaid","true");
      CheckBox chkUpd = new CheckBox("payment_ichkupdate","true");
      CheckBox chkDel= new CheckBox("payment_ichkdel","true");

      SubmitButton B = new SubmitButton(iwrb.getImage("bill.gif"),"updatepay");

      Table T3 = new Table(8,1);
      T3.add(tPay,1,1);
      T3.add(chkPay,2,1);
      T3.add(tDel,3,1);
      T3.add(chkDel,4,1);
      T3.add(tUpd,5,1);
      T3.add(chkUpd,6,1);
      T3.add(B,8,1);

      T.add(PayDate,1,2);
      T.add(drdPaytypes,2,2);
      T.add(tPart,3,2);
      T.add(Description,4,2);
      T.add(Price,5,2);
      T2.add(T,1,2);
      T2.add(T3,1,3);
      myForm.add(T2);
      Table T4 = new Table();
      T4.setWidth("100%");
      T4.add(myForm);
      return T4;
    }


    private Table makeAdjustTable(){
      Table T2 = new Table(1,3);
      T2.setCellspacing(1);
      T2.setCellpadding(2);
      T2.setWidth("100%");
      Table T = new Table(3,2);
      T.setWidth("100%");
      T.setCellspacing(0);
      T.setCellpadding(2);
      T.setColumnAlignment(1,"left");
      T.setColumnAlignment(2,"left");
      T.setRowColor(1,HeaderColor);

      String fontColor = WhiteColor;
      int fontSize = 1;

      String sSettings = iwrb.getLocalizedString("settings","Settings");
      String sPaytype = iwrb.getLocalizedString("paytype","Paytype");
      String sFirstpaydate = iwrb.getLocalizedString("firstpaydate","1.Paydate");
      String sPayments = iwrb.getLocalizedString("payments","Payments");

      Text Title = new Text(sSettings,true,false,false);
      Title.setFontColor(HeaderColor);
      T2.add(Title,1,1);

      Text[] TableTitles = new Text[3];
      TableTitles[0] = new Text(sPayments);
      TableTitles[1] = new Text(sPaytype);
      TableTitles[2] = new Text(sFirstpaydate);

      for(int i = 0 ; i < TableTitles.length;i++){
        TableTitles[i].setFontSize(fontSize);
        TableTitles[i].setFontColor(WhiteColor);
        T.add(TableTitles[i],i+1,1);
      }

      DropdownMenu drdInst = new DropdownMenu(this.getInstPrm());
      for(int i = 0; i < 13; i++){ drdInst.addMenuElement( String.valueOf(i));  }
      drdInst.setSelectedElement("1");
      drdInst.setAttribute("style",this.styleAttribute);
      DropdownMenu drdPaytypes = new DropdownMenu(this.getPTPrm());
      for(int i = 1; i < 5; i++){ drdPaytypes.addMenuElement( String.valueOf(i),this.getPaymentType(i));  }
      drdPaytypes.setAttribute("style",this.styleAttribute);

      TextInput PayDate = new TextInput(this.getDtPrm());
      PayDate.setLength(10);
      PayDate.setAttribute("style",this.styleAttribute);

      IntegerInput Interest = new IntegerInput(this.getInterestprm());
      Interest.setLength(4);
      Interest.setAttribute("style",this.styleAttribute);

      IntegerInput Cost = new IntegerInput(this.getCostprm());
      Cost.setLength(4);
      Cost.setAttribute("style",this.styleAttribute);

      Table CostTable = new Table(4,1);
      CostTable.setWidth("100%");
      CostTable.setColumnAlignment(1,"left");
      CostTable.setColumnAlignment(2,"right");
      CostTable.setColumnAlignment(3,"left");
      CostTable.setColumnAlignment(4,"right");
      String sAmount = iwrb.getLocalizedString("amount","Amount");
      String sPercent = iwrb.getLocalizedString("percent","Percent");
      Text cost = new Text(sAmount);
      cost.setFontSize(fontSize);
      cost.setFontColor(HeaderColor);
      CostTable.add(cost,1,1);
      CostTable.add(Cost,2,1);
      Text interest = new Text(sPercent);
      interest.setFontSize(fontSize);
      interest.setFontColor(HeaderColor);
      CostTable.add(interest,3,1);
      CostTable.add(Interest,4,1);


      T.add(drdInst,1,2);
      T.add(drdPaytypes,2,2);
      T.add(PayDate,3,2);
      T2.add(T,1,2);
      T2.add(CostTable,1,3);
      return T2;
    }

    private Table makeSubmitTable(){
      Table T2 = new Table(1,1);
      T2.setCellspacing(1);
      T2.setCellpadding(2);
      T2.setAlignment("center");
      Table T = new Table(1,1);
      T.setWidth("100%");
      T.setCellspacing(0);
      T.setCellpadding(0);
      T.add(new SubmitButton(iwrb.getImage("bill.gif"),"makenew"),1,1);
      T2.add(T,1,1);
      return T2;
    }

    public String getChkPrm(){return "account_chkcat";}
    public String getDscPrm(){return "account_descrcat";}
    public String getPrcPrm(){return "account_price";}
    public String getDtPrm(){return "account_datecat";}
    public String getIDsPrm(){return "account_catids";}
    public String getInstPrm(){return "account_installdrd";}
    public String getPTPrm(){return "account_paytypesdrd";}
    public String getPCprm(){return "account_paychange";}
    public String getInterestprm(){return "account_bankinterest";}
    public String getCostprm(){return "account_bankcost";}


    private Table makeMainTable(){
      Table MainTable = new Table(1,6);
      //MainTable.setWidth(sTablewidth);
      //MainTable.setBorder(1);
      MainTable.setWidth(1,"100");
      MainTable.setCellspacing(0);
      MainTable.setCellpadding(0);
      //MainTable.add(this.makeHeaderTable(),1,2);
      MainTable.add(strMessage,1,6);
      return MainTable;
    }



    private Table makeAccountTable(){
      Table T = new Table(4,3);

      T.setWidth(sTablewidth);

      T.setCellspacing(0);
      T.setCellpadding(2);
      T.setColumnAlignment(1,"left");
      T.setColumnAlignment(2,"left");
      T.setColumnAlignment(3,"right");
      T.setColumnAlignment(4,"right");

      T.setHorizontalZebraColored(LightColor,WhiteColor);
      T.setRowColor(1,WhiteColor);
      T.setRowColor(2,HeaderColor);

      String fontColor = HeaderColor;
      int fontSize = 2;

      String sAccount = iwrb.getLocalizedString("account","Account");
      String sOwner = iwrb.getLocalizedString("owner","Owner");
      String sBalance= iwrb.getLocalizedString("balance","Balance");
      String sLastEntry = iwrb.getLocalizedString("lastentry","Last Entry");

      Text Title = new Text(sAccount,true,false,false);
      Title.setFontColor(HeaderColor);
      T.add(Title,1,1);


      Text[] TableTitles = new Text[4];
      TableTitles[0] = new Text(sAccount);
      TableTitles[1] = new Text(sOwner);
      TableTitles[2] = new Text(sLastEntry);
      TableTitles[3] = new Text(sBalance);

      Text[] TableTexts = new Text[4];
      TableTexts[0] = new Text(this.eAccount.getName());
      TableTexts[1] = new Text(this.eMember.getName());
      TableTexts[2] = new Text(new idegaTimestamp(this.eAccount.getLastUpdated()).getISLDate(".",true));
      int b = this.eAccount.getBalance();
      boolean debet = b > 0?true:false;
      TableTexts[3] = new Text(NF.format(b));

      for(int i = 0 ; i < 4;i++){
        TableTitles[i].setFontSize(fontSize);
        TableTitles[i].setFontColor(WhiteColor);
        T.add(TableTitles[i],i+1,2);
        TableTexts[i].setFontSize(fontSize);
        TableTexts[i].setFontColor("#000000");
        if(i == 3){
            if(debet) TableTexts[i].setFontColor(DebetColor);
            else TableTexts[i].setFontColor(KreditColor);
          }
          else
            TableTexts[i].setFontColor("#000000");
          T.add(TableTexts[i],i+1,3);
      }
      return T;
    }

    private Table makeHeaderTable(){
      Table T = new Table(1,1);
      T.setWidth("100%");
      T.setColor(this.HeaderColor);
      //T.setBorder(1);
      //T.setColor(1,1,WhiteColor);
      T.setCellpadding(1);
      T.setCellspacing(1);
      Table T2 = new Table(1,2);
      T2.setWidth("100%");
      T2.setColor(this.WhiteColor);
      T2.setCellpadding(2);
      T2.setCellspacing(0);
      T2.add(this.makeMemberTable(),1,1);
      T2.add(this.makeFamilyTable(),1,2);

      T.add(T2);
      return T;
    }

    private Table makeMemberTable(){

      Table T = new Table(3,2);
      T.setWidth("100%");
      T.setCellspacing(0);
      T.setCellpadding(2);
      T.setColumnAlignment(1,"left");
      T.setColumnAlignment(2,"center");
      T.setColumnAlignment(3,"right");

      String fontColor = WhiteColor;
      int fontSize = 1;

      Text Name = new Text(this.eMember.getName());
      Name.setFontColor(HeaderColor);
      Name.setBold();
      String sSsn = iwrb.getLocalizedString("ssn","Socialnumber");
      String sAccountBalance = iwrb.getLocalizedString("accountbalance","Account Balance");
      Text Kt = new Text(sSsn+": "+this.eMember.getSocialSecurityNumber());
      Kt.setFontColor(HeaderColor);

      Text AccountStatus = new Text(sAccountBalance);
      AccountStatus.setFontColor(HeaderColor);

      Account account = null;
      int balance = 0;
      try{
        account = new Account(this.eAccount.getID());
        balance = account.getBalance()  ;
      }
      catch(SQLException e){balance =  this.eAccount.getBalance();}

      Text Status = new Text(String.valueOf(balance));
      Status.setFontColor(HeaderColor);

      T.add(Name,1,1);
      T.add(Kt,3,1);
      T.add(AccountStatus,1,2);
      T.add(Status,3,2);

      return T;
    }

  private Table makeFamilyTable(){
    Table T = new Table();
    try{
    UnionMemberInfo umi = this.eMember.getUnionMemberInfo(this.un_id);
    int iFamilyId = umi.getFamilyId();

    StringBuffer sql = new StringBuffer();
    sql.append("select member_id,first_name,middle_name,last_name,date_of_birth,gender,image_id,social_security_number,email ");
    sql.append("from member,union_member_info ");
    sql.append("where member.member_id = union_member_info.member_id ");
    sql.append("and union_member_info.union_id = ");
    sql.append(this.un_id);
    sql.append(" and union_member_info.family_id = ");
    sql.append(iFamilyId);

    List L = EntityFinder.findAll(eMember,sql.toString());
    //List L = TariffService.getMemberFamily(this.eMember.getID() ,this.un_id);

      if(L!=null && !L.isEmpty()){

        int len = L.size();
        T = new Table(3,len+2);
        T.setWidth("100%");
        //T.setWidth(1,"65");
        T.setRowColor(1,HeaderColor);
        T.setCellspacing(0);
        T.setCellpadding(2);
        T.setColumnAlignment(1,"left");
        T.setColumnAlignment(2,"left");
        T.setColumnAlignment(3,"right");

        //T.setHorizontalZebraColored(LightColor,WhiteColor);
        String sFamily = iwrb.getLocalizedString("family","Family");
        String sSsn = iwrb.getLocalizedString("ssn","Socialnumber");
        Text header = new Text(sFamily+" :");
        header.setFontColor(WhiteColor);
        T.add(header,1,1);

        Text socialnr = new Text(sSsn+" :");
        socialnr.setFontColor(WhiteColor);
        T.add(socialnr,3,1);


        for(int i = 0; i < len;i++){
          Member m = (Member)L.get(i);
          int id = m.getID();
          if(id != eMember.getID()){
            Link link  = new Link(m.getName());
            link.addParameter("member_id",m.getID());
            link.addParameter(this.prmString,"tariffs");
            T.add(link,1,i+2);
            Text socid = new Text(m.getSocialSecurityNumber());
            socid.setFontColor(HeaderColor);
            T.add(socid,3,i+2);
          }
        }

      }
    }
    catch(Exception e){e.printStackTrace();}

    return T;
  }


  /**
   *
   */
  private Table makeEntryTable(AccountEntry[] entries){
    int tableDepth = entries.length+2;
    Table T = new Table(4,tableDepth);
    T.setWidth(sTablewidth);
    T.setWidth(1,"65");
    T.setCellspacing(0);
    T.setCellpadding(2);
    T.setColumnAlignment(1,"right");
    T.setColumnAlignment(2,"left");
    T.setColumnAlignment(3,"left");
    T.setColumnAlignment(4,"right");

    T.setHorizontalZebraColored(LightColor,WhiteColor);
    T.setRowColor(1,WhiteColor);
    T.setRowColor(2,HeaderColor);

    String fontColor = WhiteColor;
    int fontSize = 2;
    String sMovement = iwrb.getLocalizedString("movement","Movement");
    String sDate = iwrb.getLocalizedString("date","Date");
    String sDesc = iwrb.getLocalizedString("description","Description");
    String sText = iwrb.getLocalizedString("text","Text");
    String sAmount = iwrb.getLocalizedString("amount","Amount");

    Text Title = new Text(sMovement,true,false,false);
    Title.setFontColor(HeaderColor);
    T.add(Title,1,1);

    Text[] TableTitles = new Text[4];
    TableTitles[0] = new Text(sDate);
    TableTitles[1] = new Text(sDesc);
    TableTitles[2] = new Text(sText);
    TableTitles[3] = new Text(sAmount);

    for(int i = 0 ; i < TableTitles.length;i++){
      TableTitles[i].setFontSize(fontSize);
      TableTitles[i].setFontColor(WhiteColor);
      T.add(TableTitles[i],i+1,2);
    }

    Text[] TableTexts = new Text[4];
    boolean debet = false;
    for(int j = 0; j < entries.length; j++){
      TableTexts[0] = new Text(new idegaTimestamp(entries[j].getLastUpdated()).getISLDate(".",true));
      TableTexts[1] = new Text(entries[j].getName());
      TableTexts[2] = new Text(entries[j].getInfo());
      int p = entries[j].getPrice();
      debet = p > 0 ? true : false ;
      TableTexts[3] = new Text(NF.format(p));

      for(int i = 0 ; i < 4;i++){
        TableTexts[i].setFontSize(fontSize);
        TableTexts[i].setFontColor("#000000");
        if(i == 3){
          if(debet) TableTexts[i].setFontColor(DebetColor);
          else TableTexts[i].setFontColor(KreditColor);
        }
        else
          TableTexts[i].setFontColor("#000000");
        T.add(TableTexts[i],i+1,j+3);

      }
    }
      return T;
    }

  private Table makeTariffEntriesTable(AccountEntry[] entries){
    int tableDepth = entries.length+2;
    Table T2 = new Table(1,3);
    T2.setWidth("100%");
    T2.setCellspacing(1);
    T2.setCellpadding(2);
    Table T = new Table(4,tableDepth);
    T.setWidth("100%");
    T.setWidth(1,"65");
    T.setCellspacing(0);
    T.setCellpadding(2);
    T.setColumnAlignment(1,"right");
    T.setColumnAlignment(2,"left");
    T.setColumnAlignment(3,"left");
    T.setColumnAlignment(4,"right");

    T.setHorizontalZebraColored(LightColor,WhiteColor);
    T.setRowColor(1,HeaderColor);

    String fontColor = WhiteColor;
    int fontSize = 2;

    String sAssessment = iwrb.getLocalizedString("assessedtariffs","Assessed Tariffs");
    String sDate = iwrb.getLocalizedString("date","Date");
    String sDesc = iwrb.getLocalizedString("description","Description");
    String sText = iwrb.getLocalizedString("text","Text");
    String sAmount = iwrb.getLocalizedString("amount","Amount");

    Text Title = new Text(sAssessment,true,false,false);
    Title.setFontColor(HeaderColor);
    T2.add(Title,1,1);

    Text[] TableTitles = new Text[4];
    TableTitles[0] = new Text(sDate);
    TableTitles[1] = new Text(sDesc);
    TableTitles[2] = new Text(sText);
    TableTitles[3] = new Text(sAmount);

    for(int i = 0 ; i < TableTitles.length;i++){
      TableTitles[i].setFontSize(fontSize);
      TableTitles[i].setFontColor(WhiteColor);
      T.add(TableTitles[i],i+1,1);
    }

    Text[] TableTexts = new Text[4];
    boolean debet = false;
    int total = 0;
    for(int j = 0; j < entries.length; j++){
      TableTexts[0] = new Text(new idegaTimestamp(entries[j].getLastUpdated()).getISLDate(".",true));
      TableTexts[1] = new Text(entries[j].getName());
      TableTexts[2] = new Text(entries[j].getInfo());
      int p = -entries[j].getPrice();
      total += p;
      debet = p > 0 ? true : false ;
      TableTexts[3] = new Text(NF.format(p));

      for(int i = 0 ; i < 4;i++){
        TableTexts[i].setFontSize(fontSize);
        TableTexts[i].setFontColor("#000000");
        if(i == 3){
          if(debet) TableTexts[i].setFontColor(DebetColor);
          else TableTexts[i].setFontColor(KreditColor);
        }
        else
          TableTexts[i].setFontColor("#000000");
        T.add(TableTexts[i],i+1,j+2);
      }
    }
    Account account = null;
      int balance = 0;
    try{
      account = new Account(this.eAccount.getID());
      balance = account.getBalance()  ;
    }
    catch(SQLException e){balance =  this.eAccount.getBalance();}

    if(balance == 0){
    Link L = new Link(iwrb.getImage("clear.gif"));
    L.addParameter(this.prmString, "clearaccount");
    T2.setAlignment(1,3,"right");
    T2.add(L,1,3);
    }
    Text totalText = new Text(NF.format(total));
    totalText.setFontSize(fontSize);
    totalText.setFontColor(total < 0 ? KreditColor: DebetColor);
    T.add(totalText,4,tableDepth);
    T2.add(T,1,2);
    return T2;
  }


  /** Shows payments
   *
   */
  private Table makePaymentsTable(Payment[] payments){
    Form myForm = new Form();
    myForm.maintainAllParameters();
    int tableDepth = payments.length+1;
    Table T2 = new Table(1,3);
    T2.setCellspacing(1);
    T2.setCellpadding(2);
    T2.setWidth("100%");
    Table T = new Table(7,tableDepth);
    T.setWidth("100%");
    T.setWidth(1,"65");
    T.setWidth(6,"20");
    T.setCellspacing(0);
    T.setCellpadding(2);
    T.setColumnAlignment(1,"right");
    T.setColumnAlignment(2,"center");
    T.setColumnAlignment(3,"center");
    T.setColumnAlignment(4,"center");
    T.setColumnAlignment(5,"right");
    T.setColumnAlignment(6,"right");

    T.setHorizontalZebraColored(LightColor,WhiteColor);
    T.setRowColor(1,HeaderColor);

    String fontColor = WhiteColor;
    int fontSize = 2;

    String sPaymentview = iwrb.getLocalizedString("pamentview","Payment view");
    String sDuedate = iwrb.getLocalizedString("duedate","Duedate");
    String sType = iwrb.getLocalizedString("paytype","Paytype");
    String sPart = iwrb.getLocalizedString("part","Part");
    String sPaid = iwrb.getLocalizedString("paid","Paid");
    String sAmount = iwrb.getLocalizedString("amount","Amount");
    String sYes = iwrb.getLocalizedString("yes","Yes");
    String sNo = iwrb.getLocalizedString("no","No");

    Text Title = new Text(sPaymentview,true,false,false);
    Title.setFontColor(HeaderColor);
    T2.add(Title,1,1);

    Text[] TableTitles = new Text[5];
    TableTitles[0] = new Text(sDuedate);
    TableTitles[1] = new Text(sType);
    TableTitles[2] = new Text(sPart);
    TableTitles[3] = new Text(sPaid);
    TableTitles[4] = new Text(sAmount);

    for(int i = 0 ; i < 5;i++){
      TableTitles[i].setFontSize(fontSize);
      TableTitles[i].setFontColor(WhiteColor);
      T.add(TableTitles[i],i+1,1);
    }

    Text[] TableTexts = new Text[5];
    boolean debet = false;
    int delcount = 0;
    for(int j = 0; j < payments.length; j++){
      TableTexts[0] = new Text(new idegaTimestamp(payments[j].getPaymentDate()).getISLDate(".",true));
      TableTexts[1] = new Text(getPaymentType(payments[j].getPaymentTypeID()));
      TableTexts[2] = new Text(payments[j].getInstallmentNr()+"/"+payments[j].getTotalInstallment());
      boolean paid = payments[j].getStatus();
      String sOut = paid?sYes:sNo;
      TableTexts[3] = new Text(sOut);
      TableTexts[4] = new Text(NF.format(payments[j].getPrice()));

      for(int i = 0 ; i < 5;i++){
        TableTexts[i].setFontSize(fontSize);
        T.add(TableTexts[i],i+1,j+2);
      }
      if(true){
        CheckBox chkdel = new CheckBox("payment_delchk"+delcount,String.valueOf(payments[j].getID()));
        T.add(chkdel,6,j+2);
        Link L = new Link("B");
        L.addParameter(this.prmString,"paychange");
        L.addParameter("payid",payments[j].getID());
        L.setFontSize(fontSize);
        if(!paid)
          T.add(L,7,j+2);
        delcount++;
      }
    }
    T.add(new HiddenInput("payment_totalpaydel",String.valueOf(delcount)));
    T2.add(T,1,1);
    T2.setAlignment(1,2,"right");
    T2.setAlignment(1,3,"right");
    T2.add(new SubmitButton(iwrb.getImage("delete.gif"),"deleteall"),1,2);
    T2.add(new SubmitButton(iwrb.getImage("pay.gif"),"payall"),1,3);
    myForm.add(T2);
    Table T4 = new Table();
    T4.setWidth("100%");
    T4.add(myForm);
    return T4;
  }

  private String getPaymentType(int id){
    String s = "";
    switch(id){
      case 1 : s = "Giro";    break;
      case 2 : s = "Euro";    break;
      case 3 : s = "Visa";    break;
      case 4 : s = "Staðgr."; break;
    }
    return s;
  }

    private Table makeLinkTable(int menuNr){
      Table LinkTable = new Table(1,1);
      LinkTable.setBorder(0);
      LinkTable.setCellpadding(0);
      LinkTable.setCellspacing(0);

      Link BookLink = new Link(iwrb.getImage(menuNr == 1?"book.gif":"book1.gif"),"/tarif/paymentbook.jsp");
      BookLink.addParameter(prmString,"view");
      BookLink.addParameter("union_id",union_id);

      Link EntryLink = new Link(iwrb.getImage(menuNr == 2?"payments.gif":"payments1.gif"));
      EntryLink.addParameter(prmString,"main");

      Link TariffLink = new Link(iwrb.getImage(menuNr == 3?"createratelist.gif":"createratelist1.gif"));
      TariffLink.addParameter(prmString,"tariffs");

      if(isAdmin){
        LinkTable.add(BookLink,1,1);
        LinkTable.add(EntryLink,1,1);
        LinkTable.add(TariffLink,1,1);
      }
      return LinkTable;
    }

    private Link makeChangeLink(int iAccountEntryId){
      String sChange = iwrb.getLocalizedString("change","Change");
      Link L = new Link(sChange);
      L.addParameter("account_action","change");
      L.addParameter("account_id",iAccountEntryId);
      L.addParameter("account_member_id",member_id);
      return L;
    }

    private DropdownMenu drpDays(String name){
      DropdownMenu drp = new DropdownMenu(name);
      for(int i = 1; i < 32 ; i ++){
        drp.addMenuElement(i,String.valueOf(i));
      }
      return drp;
    }

    private DropdownMenu drpMonth(String name){
      idegaTimestamp Today = new idegaTimestamp();
      int iMonth = Today.getMonth();
      DropdownMenu drp = new DropdownMenu(name);
      for(int i = 1; i < 13 ; i ++){
        drp.addMenuElement(i,String.valueOf(i));
      }
      drp.setSelectedElement(String.valueOf(iMonth));
      return drp;
    }

    private DropdownMenu drpYear(String name){
      idegaTimestamp it = new idegaTimestamp();
      int a = it.getYear();
      DropdownMenu drp = new DropdownMenu(name);
      for(int i = a-10; i < a+10 ; i ++){
        drp.addMenuElement(i,String.valueOf(i));
      }
      drp.setSelectedElement(String.valueOf(a));
      return drp;
    }

  public void main(IWContext iwc) {
    try{
      isAdmin = com.idega.jmodule.login.business.AccessControl.isAdmin(iwc);
    }
    catch(SQLException sql){ isAdmin = false;}
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    /** @todo: fixa Admin*/
    //isAdmin = true;
    control(iwc);
  }
  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
}// class AccountViewer