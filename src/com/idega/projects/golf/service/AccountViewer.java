package com.idega.projects.golf.service;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.ModuleObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
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
import java.text.NumberFormat.*;
import java.sql.*;
import java.io.*;
import java.util.*;

/**
*@author <a href="mailto:aron@idega.is">Aron Birkir</a>
*@version 1.0
*/

 public class AccountViewer extends com.idega.jmodule.object.ModuleObjectContainer {

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
  private Table Frame,MainFrame;
  private java.text.NumberFormat NF ;
  private String styleAttribute = "font-size: 8pt";
  private String storage;

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
    setInputLines(15);
    currentLocale = java.util.Locale.getDefault();
    NF = java.text.NumberFormat.getInstance();
  }

  public void setMenuColor(String MenuColor){
    this.MenuColor = MenuColor;
  }
  public void setItemColor(String ItemColor){
    this.ItemColor = ItemColor;
  }

  public void setInputLines(int inputlines){
    this.inputLines = inputlines;
  }
  private void control(ModuleInfo modinfo){

    try{
      if(modinfo.getParameter("member_id") != null){
        member_id = modinfo.getParameter("member_id");
        modinfo.getSession().setAttribute("account_member_id",member_id);
      }

      union_id = (String)  modinfo.getSession().getAttribute("golf_union_id");
      member_id = (String) modinfo.getSession().getAttribute("account_member_id");

      if(modinfo.getSession().getAttribute("member_login")!= null){
       Cashier = (Member) modinfo.getSession().getAttribute("member_login");
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

      eAccount = new Account(TariffService.findAccountID(mem_id,un_id));

      strMessage = "";

      if(modinfo.getRequest().getParameter(prmString) == null){
        doMain(modinfo);
      }
      if(modinfo.getRequest().getParameter(prmString) != null){
        sAction = modinfo.getRequest().getParameter(prmString);

        if(sAction.equals("main"))	        { doMain(modinfo);      }
        else if(sAction.equals("change"))	{ doChange(modinfo); 	}
        else if(sAction.equals("update"))	{ doUpdate(modinfo); 	}
        else if(sAction.equals("view"))	        { doView(modinfo); 	}
        else if(sAction.equals("save"))	        { doSave(modinfo); 	}
        else if(sAction.equals("calc"))	        { doCalc(modinfo); 	}
        else if(sAction.equals("tariffs"))	{ doTariff(modinfo); 	}
        else if(sAction.equals("new"))	        { 	}
        else if(sAction.equals("updatenew"))	{ doUpdateNew(modinfo); }
        else if(sAction.equals("makenew"))	{ doMakeNew(modinfo); }

      }
    }
    catch(SQLException S){	S.printStackTrace();	}
    }

    private void doMain(ModuleInfo modinfo) throws SQLException {
      makeMainFrame();
      makeFrame();
      addLinks(makeLinkTable(2));
      addHead(makeViewTable());
      addFrame(Frame);
      add(MainFrame);
    }

    private void doTariff(ModuleInfo modinfo){
      makeMainFrame();
      makeFrame();
      addLinks(makeLinkTable(3));
      addHead(this.makeHeaderTable());
      addMain(this.makeTarifViewTable());
      addRight(this.makeTariffTable());
      addFrame(Frame);
      add(MainFrame);
    }

    private void doChange(ModuleInfo modinfo) throws SQLException{
      String paym_id = modinfo.getRequest().getParameter("payment_id");
      PaymentType[] PT = (PaymentType[])(new PaymentType()).findAll();
      Form myForm = new Form();
      myForm.maintainAllParameters();
      if( paym_id != null){
        int pm_id = Integer.parseInt(paym_id);
        Payment P = new Payment(pm_id);
        String description = P.getExtraInfo();
        int price = P.getPrice();
        idegaTimestamp Paydate = new idegaTimestamp(P.getPaymentDate());
        idegaTimestamp Update = new idegaTimestamp(P.getLastUpdated());
        int pt_id = P.getPaymentTypeID();
        String part = P.getInstallmentNr()+"/"+P.getTotalInstallment();

        Table T =  new Table(9,2);

        T.setHorizontalZebraColored(DarkColor,LightColor);
        T.setRowColor(1,HeaderColor);
        String fontColor = "#FFFFFF";

        Text DESCR = new Text("LÝSING",true,false,false);
        DESCR.setFontColor(fontColor);
        Text PAYDATE = new Text("GJALDDAGI",true,false,false);
        PAYDATE.setFontColor(fontColor);
        Text PART = new Text("HLUTI",true,false,false);
        PART.setFontColor(fontColor);
        Text PRICE = new Text("UPPHÆÐ",true,false,false);
        PRICE.setFontColor(fontColor);
        Text PAYTYPE = new Text("GR.GERÐ",true,false,false);
        PAYTYPE.setFontColor(fontColor);
        Text UPDATED = new Text("UPPFÆRT",true,false,false);
        UPDATED.setFontColor(fontColor);
        Text PAID = new Text("GREITT",true,false,false);
        PAID.setFontColor(fontColor);
        Text UNPAID = new Text("GREITT",true,false,false);
        UNPAID.setFontColor(fontColor);
        Text DEL = new Text("EYÐA",true,false,false);
        DEL.setFontColor(fontColor);

        T.add(PAYDATE,1,1);
        T.add(DESCR,2,1);
        T.add(PRICE,3,1);
        T.add(PAYTYPE,4,1);
        T.add(PART,5,1);
        T.add(UPDATED,6,1);
        T.add(PAID,7,1);
        T.add(UNPAID,8,1);
        T.add(DEL,9,1);

        TextInput descInput = new TextInput("payment_idesc",description);
        descInput.setMaxlength(30);
        descInput.setSize(25);
        Text partText = new Text(part);
        IntegerInput priceInput = new IntegerInput("payment_iprice",price);
        priceInput.setSize(8);
        priceInput.setMaxlength(8);
        DropdownMenu drpPayType = new DropdownMenu(PT,"payment_ipaytype");
        drpPayType.setSelectedElement(String.valueOf(pt_id));
        Text payDateText = new Text(Paydate.toSQLDateString());
        Text lastUpdatedText = new Text(Update.toSQLDateString());
        CheckBox chkPaid = new CheckBox("payment_ichkpaid","true");
        CheckBox chkUnPaid = new CheckBox("payment_ichkunpaid","true");
        CheckBox chkDel = new CheckBox("payment_ichkdel","true");

        T.add(payDateText,1,2);
        T.add(descInput,2,2);
        T.add(priceInput,3,2);
        T.add(drpPayType,4,2);
        T.add(partText,5,2);
        T.add(lastUpdatedText,6,2);
        T.add(chkPaid,7,2);
        T.add(chkUnPaid,8,2);
        T.add(chkDel,9,2);

        myForm.add(T);
        myForm.add(new SubmitButton(new Image("/pics/tarif/uppfaera.gif")));
        myForm.add(new HiddenInput("payment_action","update" ));
        myForm.add(new HiddenInput("payment_id",paym_id ));

        Table MainTable = makeMainTable();
        MainTable.add(makeLinkTable(2),2,1);
        MainTable.add(myForm,2,3);
        MainTable.add("<br><br><br>",2,4);
        add(MainTable);
      }
    }

    private void doUpdate(ModuleInfo modinfo) throws SQLException{
      String strPaymID,strPrice,strPaytype,strDescr,strChkPaid,strChkUnPaid,strChkDel;
      strPaymID = modinfo.getRequest().getParameter("payment_id");
      strPrice = modinfo.getRequest().getParameter("payment_iprice");
      strDescr = modinfo.getRequest().getParameter("payment_idesc");
      strPaytype = modinfo.getRequest().getParameter("payment_ipaytype");
      strChkPaid = modinfo.getRequest().getParameter("payment_ichkpaid");
      strChkUnPaid = modinfo.getRequest().getParameter("payment_ichkunpaid");
      strChkDel = modinfo.getRequest().getParameter("payment_ichkdel");

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
        catch(SQLException e){strMessage = "Tókst ekki að eyða greiðslu";}
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
        catch(SQLException e){strMessage = "Tókst ekki að breyta greiðslu";}
      }
      }
      this.doMain(modinfo);

    }
    private void doView(ModuleInfo modinfo) throws SQLException{

    }
    private void doSave(ModuleInfo modinfo) throws SQLException{

    }
    private void doMakeNew(ModuleInfo modinfo) throws SQLException{
      String sCatIds = modinfo.getParameter(this.getIDsPrm());
      String sDescr = modinfo.getParameter(this.getDscPrm());
      String sPrice = modinfo.getParameter(this.getPrcPrm());
      String sDate = modinfo.getParameter(this.getDtPrm());
      int totalprice = 0;
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
          stemp = modinfo.getParameter(this.getChkPrm()+itemp);
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
        int iInst = Integer.parseInt( modinfo.getParameter(this.getInstPrm()));
        int iType = Integer.parseInt( modinfo.getParameter(this.getPTPrm()));
        String sdate = modinfo.getParameter(this.getDtPrm());
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
        catch(SQLException sql){add("villa");}
      }

      this.doTariff(modinfo);
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

    private void doUpdateNew(ModuleInfo modinfo) throws SQLException{
      DecimalFormat Formatter = new DecimalFormat("00");

      String strPrice,strPaytype,strDescr,strIfRoundRel,strRoundId,strInst;
      strPrice = modinfo.getRequest().getParameter("payment_iprice");
      strDescr = modinfo.getRequest().getParameter("payment_idesc");
      strPaytype = modinfo.getRequest().getParameter("payment_ipaytype");
      strIfRoundRel = modinfo.getRequest().getParameter("payment_roundrel");
      strRoundId = modinfo.getRequest().getParameter("payment_irounds");
      strInst = modinfo.getRequest().getParameter("payment_installments");
      int iday = Integer.parseInt(modinfo.getRequest().getParameter("payment_day"));
      int imonth = Integer.parseInt(modinfo.getRequest().getParameter("payment_month"));
      int iyear = Integer.parseInt(modinfo.getRequest().getParameter("payment_year"));

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
            catch(SQLException e){e.printStackTrace();  strMessage = "Tókst ekki að breyta greiðslu";}
          }
        }
      }
     this.doMain(modinfo);
    }

    private void doCalc(ModuleInfo modinfo) throws SQLException{
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
      catch(SQLException sql){ add("mistókst");
      }
      this.doMain(modinfo);
    }

    private void makeMainFrame(){
      MainFrame = new Table(4,3);
      MainFrame.setWidth(1,"70");
      MainFrame.setWidth(2,"450");
      MainFrame.setWidth(3,"20");
      MainFrame.setWidth(4,"250");
      MainFrame.setColumnColor(3,WhiteColor);
      MainFrame.setCellspacing(0);
      MainFrame.setCellpadding(0);
    }

    private void addFrame(ModuleObject T){
      MainFrame.add(T,2,3);
    }

    private void makeFrame(){
      Frame = new Table(1,3);
      Frame.setCellspacing(0);
      Frame.setCellpadding(0);
      Frame.setWidth("100%");
    }

    private void addMain(ModuleObject T){
      Frame.add(T,1,3);
    }

    private void addHead(ModuleObject T){
      Frame.add(T,1,1);
    }

    private void addRight(ModuleObject T){
      MainFrame.add(T,4,3);
    }

    private void addLinks(ModuleObject T){
      MainFrame.add(T,2,2);
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
      Table T2 = new Table(1,3);
      T2.setWidth("100%");
      T2.setColor(this.WhiteColor);
      T2.setCellpadding(4);
      T2.setCellspacing(0);
      T2.add(this.makeTariffEntriesTable(TariffService.getTariffEntrys(eAccount.getID())),1,1);
      T2.addBreak(1,2);
      T2.add(this.makePaymentsTable(TariffService.getMemberPayments(this.mem_id,this.un_id)),1,3);
      T2.addBreak(1,3);
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
      T.setHeight("100%");
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
      myForm.add(new HiddenInput( this.prmString,"makenew"));
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
      Table T = new Table(3,tableDepth);
      T.setWidth("100%");
      T.setWidth(1,"65");
      T.setCellspacing(0);
      T.setCellpadding(2);
      T.setColumnAlignment(1,"center");
      T.setColumnAlignment(2,"left");
      T.setColumnAlignment(3,"right");


      T.setHorizontalZebraColored(LightColor,WhiteColor);
      T.setRowColor(1,HeaderColor);

      String fontColor = WhiteColor;
      int fontSize = 1;

      Text Title = new Text(" Núverandi Gjaldskrá",true,false,false);
      Title.setFontColor(HeaderColor);
      T2.add(Title,1,1);

      Text[] TableTitles = new Text[3];
      TableTitles[0] = new Text("Val");
      TableTitles[1] = new Text("Lýsing");
      TableTitles[2] = new Text("Upphæð");

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

    public Table makeNewTarifTable(){
      Table T2 = new Table(1,2);
      T2.setCellspacing(1);
      T2.setCellpadding(2);
      Table T = new Table(3,3);
      T.setWidth("100%");
      //T.setWidth(1,"65");
      T.setCellspacing(0);
      T.setCellpadding(2);
      T.setColumnAlignment(1,"left");
      T.setColumnAlignment(2,"left");

      //T.setColumnAlignment(3,"right");
      T.setAlignment(1,3,"left");


      T.setRowColor(1,HeaderColor);

      String fontColor = WhiteColor;
      int fontSize = 1;

      Text Title = new Text(" Nýtt Gjald",true,false,false);
      Title.setFontColor(HeaderColor);
      T2.add(Title,1,1);

      Text[] TableTitles = new Text[3];
      TableTitles[0] = new Text("Lýsing");
      TableTitles[1] = new Text("Upphæð");
      TableTitles[2] = new Text("Gjalddagi");

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
      T.add(PayDate,3,2);
      T2.add(T,1,2);
      return T2;
    }

    public Table makeAdjustTable(){
      Table T2 = new Table(1,2);
      T2.setCellspacing(1);
      T2.setCellpadding(2);
      T2.setWidth("100%");
      Table T = new Table(3,3);
      T.setWidth("100%");
      T.setCellspacing(0);
      T.setCellpadding(2);
      T.setColumnAlignment(1,"left");
      T.setColumnAlignment(2,"left");
      T.setAlignment(1,3,"left");
      T.setRowColor(1,HeaderColor);

      String fontColor = WhiteColor;
      int fontSize = 1;

      Text Title = new Text("Stillingar",true,false,false);
      Title.setFontColor(HeaderColor);
      T2.add(Title,1,1);

      Text[] TableTitles = new Text[3];
      TableTitles[0] = new Text("Skipting");
      TableTitles[1] = new Text("Greiðslugerð");
      TableTitles[2] = new Text("1.Gjalddagi");

      for(int i = 0 ; i < TableTitles.length;i++){
        TableTitles[i].setFontSize(fontSize);
        TableTitles[i].setFontColor(WhiteColor);
        T.add(TableTitles[i],i+1,1);
      }

      DropdownMenu drdInst = new DropdownMenu(this.getInstPrm());
      for(int i = 1; i < 13; i++){ drdInst.addMenuElement( String.valueOf(i));  }
      drdInst.setAttribute("style",this.styleAttribute);
      DropdownMenu drdPaytypes = new DropdownMenu(this.getPTPrm());
      for(int i = 1; i < 5; i++){ drdPaytypes.addMenuElement( String.valueOf(i),this.getPaymentType(i));  }
      drdPaytypes.setAttribute("style",this.styleAttribute);

      TextInput PayDate = new TextInput(this.getDtPrm());
      PayDate.setLength(10);
      PayDate.setAttribute("style",this.styleAttribute);

      T.add(drdInst,1,2);
      T.add(drdPaytypes,2,2);
      T.add(PayDate,3,2);
      T2.add(T,1,2);
      return T2;
    }

    public Table makeSubmitTable(){
      Table T2 = new Table(1,1);
      T2.setCellspacing(1);
      T2.setCellpadding(2);
      T2.setAlignment("center");
      Table T = new Table(1,1);
      T.setWidth("100%");
      T.setCellspacing(0);
      T.setCellpadding(0);
      T.add(new SubmitButton(new Image("/pics/tarif/small/leggjaa.gif")),1,1);
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

      Text Title = new Text("Reikningur",true,false,false);
      Title.setFontColor(HeaderColor);
      T.add(Title,1,1);


      Text[] TableTitles = new Text[4];
      TableTitles[0] = new Text("Reikningur");
      TableTitles[1] = new Text("Eigandi");
      TableTitles[2] = new Text("Síðasta hreyfing");
      TableTitles[3] = new Text("Staða");

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
      T2.setCellpadding(4);
      T2.setCellspacing(0);
      T2.add(this.makeMemberTable(),1,1);
      T2.add(this.makeFamilyTable(),1,2);

      T.add(T2);
      return T;
    }

    private Table makeMemberTable(){

      Table T = new Table(3,1);
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
      Text Kt = new Text("Kt: "+this.eMember.getSocialSecurityNumber());
      Kt.setFontColor(HeaderColor);

      T.add(Name,1,1);
      T.add(Kt,3,1);

      return T;
    }

  private Table makeFamilyTable(){
    Table T = new Table();
    try{
    List L = TariffService.getMemberFamily(this.eMember.getID() ,this.un_id);

      if(!L.isEmpty()){
        int len = L.size();
        T = new Table(3,len+1);
        T.setWidth("100%");
        T.setWidth(1,"65");
        T.setCellspacing(0);
        T.setCellpadding(2);
        T.setColumnAlignment(1,"center");
        T.setColumnAlignment(2,"left");
        T.setColumnAlignment(3,"right");

        T.setHorizontalZebraColored(LightColor,WhiteColor);

        Text header = new Text("Fjölskylda :");
        header.setFontColor(HeaderColor);

        for(int i = 0; i < len;i++){
          Member m = (Member)L.get(i);
          Link link  = new Link(m.getName());
          link.addParameter("member_id",m.getID());
          link.addParameter(this.prmString,"tariffs");
          T.add(link,1,i+1);
        }
      }
    }
    catch(Exception e){e.printStackTrace();}

    return T;
  }



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

    Text Title = new Text("Hreyfingar",true,false,false);
    Title.setFontColor(HeaderColor);
    T.add(Title,1,1);

    Text[] TableTitles = new Text[4];
    TableTitles[0] = new Text("Dags");
    TableTitles[1] = new Text("Skýring");
    TableTitles[2] = new Text("Texti");
    TableTitles[3] = new Text("Hreyfing");

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
    Table T2 = new Table(1,2);
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

    Text Title = new Text("  Álögð gjöld",true,false,false);
    Title.setFontColor(HeaderColor);
    T2.add(Title,1,1);

    Text[] TableTitles = new Text[4];
    TableTitles[0] = new Text("Dags");
    TableTitles[1] = new Text("Skýring");
    TableTitles[2] = new Text("Texti");
    TableTitles[3] = new Text("Gjald");

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
    Text totalText = new Text(NF.format(total));
    totalText.setFontSize(fontSize);
    totalText.setFontColor(total < 0 ? KreditColor: DebetColor);
    T.add(totalText,4,tableDepth);
    T2.add(T,1,2);
    return T2;
  }

  private Table makePaymentsTable(Payment[] payments){
    int tableDepth = payments.length+2;
    Table T2 = new Table(1,2);
    T2.setCellspacing(1);
    T2.setCellpadding(2);
    T2.setWidth("100%");
    Table T = new Table(5,tableDepth);
    T.setWidth("100%");
    T.setWidth(1,"65");
    T.setCellspacing(0);
    T.setCellpadding(2);
    T.setColumnAlignment(1,"right");
    T.setColumnAlignment(2,"center");
    T.setColumnAlignment(3,"center");
    T.setColumnAlignment(4,"center");
    T.setColumnAlignment(5,"right");

    T.setHorizontalZebraColored(LightColor,WhiteColor);
    T.setRowColor(1,HeaderColor);

    String fontColor = WhiteColor;
    int fontSize = 2;

    Text Title = new Text("Skipting gjalda",true,false,false);
    Title.setFontColor(HeaderColor);
    T2.add(Title,1,1);

    Text[] TableTitles = new Text[5];
    TableTitles[0] = new Text("Gjalddagi");
    TableTitles[1] = new Text("Máti");
    TableTitles[2] = new Text("Hluti");
    TableTitles[3] = new Text("Útskrifað");
    TableTitles[4] = new Text("Upphæð");

    for(int i = 0 ; i < 5;i++){
      TableTitles[i].setFontSize(fontSize);
      TableTitles[i].setFontColor(WhiteColor);
      T.add(TableTitles[i],i+1,1);
    }

    Text[] TableTexts = new Text[5];
    boolean debet = false;
    for(int j = 0; j < payments.length; j++){
      TableTexts[0] = new Text(new idegaTimestamp(payments[j].getPaymentDate()).getISLDate(".",true));
      TableTexts[1] = new Text(getPaymentType(payments[j].getPaymentTypeID()));
      TableTexts[2] = new Text(payments[j].getInstallmentNr()+"/"+payments[j].getTotalInstallment());
      String sOut = payments[j].getStatus()? "Já":"Nei";
      TableTexts[3] = new Text(sOut);
      TableTexts[4] = new Text(NF.format(payments[j].getPrice()));

      for(int i = 0 ; i < 5;i++){
        TableTexts[i].setFontSize(fontSize);
        T.add(TableTexts[i],i+1,j+2);
      }
    }
    T2.add(T);
    return T2;
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

      //LinkTable.setWidth(sTablewidth);

      Link BookLink = new Link(new Image(menuNr == 1?"/pics/tarif/boka.gif":"/pics/tarif/boka1.gif"),"/tarif/paymentbook.jsp");
      BookLink.addParameter(prmString,"view");
      BookLink.addParameter("union_id",union_id);

      Link EntryLink = new Link(new Image(menuNr == 2?"/pics/tarif/greidslur.gif":"/pics/tarif/greidslur1.gif"));
      EntryLink.addParameter(prmString,"main");

      Link TariffLink = new Link(new Image(menuNr == 3?"/pics/tarif/alagning.gif":"/pics/tarif/alagning1.gif"));
      TariffLink.addParameter(prmString,"tariffs");
/*
      Link MakeLink = new Link(new Image(menuNr == 4?"/pics/tarif/nytt.gif":"/pics/tarif/nytt1.gif"));
      MakeLink.addParameter(prmString,"make");
*/

      if(isAdmin){
        LinkTable.add(BookLink,1,1);
        LinkTable.add(EntryLink,1,1);
        LinkTable.add(TariffLink,1,1);
        //LinkTable.add(MakeLink,1,1);
      }
      return LinkTable;
    }

    private Link makeChangeLink(int iAccountEntryId){
      Link L = new Link("Breyta");
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

  public void main(ModuleInfo modinfo) {
    try{
      isAdmin = com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException sql){ isAdmin = false;}
    /** @todo: fixa Admin*/
    //isAdmin = true;
    control(modinfo);
  }
}// class AccountViewer


