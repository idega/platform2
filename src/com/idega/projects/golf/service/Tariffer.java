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
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class Tariffer extends ModuleObjectContainer {

  private String catal_action = "";
  private String union_id="1";
  private int un_id,cashier_id, numOfCat, inputLines, saveCount,count,memberCount=0;
  private String unionName;
  private String unionAbbrev;
  private PriceCatalogue pricecatalogue;
  private Union[] unions;
  private Member thisMember,Cashier;
  private Member[] unionMembers;
  private PriceCatalogue[] Catalogs;
  private String MenuColor,ItemColor,HeaderColor,LightColor,DarkColor,OtherColor;
  private boolean isAdmin = false;
  private java.util.Locale currentLocale;
  private Link sidan, UpdateLink, PriceLink;
  private String[][] Values = null;
  private Window window;
  private Member[][] mbsArray ;
  private Vector[] mbsVectorArray,mbsInfoVectorArray;
  private Vector[] TotalMembersVector;
  private int CatalogIds[];
  private Integer[][] totals = null;
  private int cellspacing = 1, cellpadding = 2;
  private Thread payThread = null;
  private String tablewidth = "539";
  private String filepath = null, fileSeparator = null;
  private Hashtable HeadsHash = null, AccHsh = null;
  private Table MainFrame;

  public Tariffer(){
    pricecatalogue = new PriceCatalogue();
    HeaderColor = "#336660";
    LightColor = "#CEDFD0";
    DarkColor = "#ADCAB1";
    OtherColor = "#6E9073";
    setMenuColor("#ADCAB1");//,"#CEDFD0"
    setItemColor("#CEDFD0");//"#D0F0D0"
    setInputLines(15);
    currentLocale = java.util.Locale.getDefault();
  }

  private void makeMainFrame(){
    MainFrame = new Table(2,3);
    MainFrame.setWidth(1,"70");
    MainFrame.setWidth(2,tablewidth);
    MainFrame.setCellspacing(0);
    MainFrame.setCellpadding(0);
  }

  private void addMain(ModuleObject T){
    MainFrame.add(T,2,3);
  }

  private void addRight(ModuleObject T){
    MainFrame.add(T,4,3);
  }

  private void addLinks(ModuleObject T){
    MainFrame.add(T,2,2);
  }


  private Table makeMainTable(int menuNr){
    Link PiLink = new Link( "_" ,"/tarif/pi.jsp");
    Text HeaderText = new Text("  F\u00e9lagsgj\u00f6ld  "+ unionAbbrev);
    HeaderText.setFontColor("#FFFFFF");
    Table HeaderTable = new Table(1,1);
    HeaderTable.setColor(HeaderColor );
    HeaderTable.setWidth(tablewidth);
    HeaderTable.add(HeaderText,1,1);

    Table MainTable = new Table(2,5);
    MainTable.setWidth(1,"50");
    MainTable.setBorder(0);
    MainTable.setCellpadding(0);
    MainTable.setCellspacing(0);
    MainTable.setWidth(tablewidth);
    MainTable.setRowAlignment(5, "left");

    MainTable.add(makeLinkTable(menuNr),2,1);
    MainTable.add(HeaderTable,2,2);
    //MainTable.add(PiLink,1,5);

    Link SaveLink = new Link(new Image("/pics/tarif/vista.gif"));
    SaveLink.addParameter("catal_action","save");
    SaveLink.addParameter("union_id",union_id);

    MainTable.add(new CloseButton(new Image("/pics/tarif/loka.gif")),2,4);
    if(menuNr == 3)
      MainTable.add(SaveLink,2,4);

    return MainTable;
  }

    public static Link getExtraCatalogueLink(String name,String union_id){
      Link sidan = new Link(name,"/tarif/pcmake.jsp");
      sidan.addParameter("catal_action","main");
      sidan.addParameter("union_id",union_id);
      return sidan;
    }

    public Link getMemberPaymentsLink(String name,String member_id){
      Link L2 = new Link( name, "/tarif/payments.jsp");
      L2.addParameter("catal_member_id",member_id);
      return L2;
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
    public DropdownMenu getExtraCatalogueDropdownMenu(String name,String unionID)throws SQLException{
      DropdownMenu drp = new DropdownMenu(name);
      PriceCatalogue[] p = getExtraCatalogues(unionID);
      drp.addDisabledMenuElement("0","Engin flokkur valinn !");
      for(int i = 0; i < p.length; i++){
        drp.addMenuElement(p[i].getID(),p[i].getName());
      }
      return drp;
    }
    public Link getTarifLink(Image image, String union_id){
      Link sidan = new Link(image);
      sidan.addParameter("catal_action","main");
      sidan.addParameter("union_id",union_id);
      return sidan;
    }
    public Link getTarifLink(String text, String union_id){
      Link sidan = new Link(text);
      sidan.addParameter("catal_action","main");
      sidan.addParameter("union_id",union_id);
      return sidan;
    }
    public Link getTarifLink(String union_id){
      Link sidan = new Link();
      sidan.addParameter("catal_action","main");
      sidan.addParameter("union_id",union_id);
      return sidan;
    }
    public String getExtraCatalogueSQL(String union_id){
      return "select * from price_catalogue where union_id = '"+union_id+"' and in_use = 'Y' and is_independent = 'Y'";
    }


   private Table makeLinkTable(int menuNr){
    Table LinkTable = new Table(1,1);
    LinkTable.setBorder(0);
    LinkTable.setCellpadding(0);
    LinkTable.setCellspacing(0);

    LinkTable.setWidth(tablewidth);

    Link sidan = new Link(new Image(menuNr == 1?"/pics/tarif/gjaldskra.gif":"/pics/tarif/gjaldskra1.gif"));
    sidan.addParameter("catal_action","main");
    sidan.addParameter("union_id",union_id);

    Link UpdateLink = new Link(new Image(menuNr == 2?"/pics/tarif/breyta.gif":"/pics/tarif/breyta1.gif"));
    UpdateLink.addParameter("catal_action","change");
    UpdateLink.addParameter("union_id",union_id);

    Link ViewLink = new Link(new Image(menuNr == 3?"/pics/tarif/skoda.gif":"/pics/tarif/skoda1.gif"));
    ViewLink.addParameter("catal_action","view");
    ViewLink.addParameter("union_id",union_id);
/**
    Link SaveLink = new Link(new Image(menuNr == 4?"/pics/tarif/vista.gif":"/pics/tarif/vista1.gif"));
    SaveLink.addParameter("catal_action","save");
    SaveLink.addParameter("union_id",union_id);
*/
    Link PriceLink = new Link(new Image(menuNr == 5?"/pics/tarif/alagning.gif":"/pics/tarif/alagning1.gif"));
    PriceLink.addParameter("catal_action","price");
    PriceLink.addParameter("union_id",union_id);

    //Link OutLink = new Link(new Image("/pics/tarif/utskraning.gif"),"/tarif/filepaym.jsp");
    Link OutLink = new Link(new Image(menuNr == 6?"/pics/tarif/utskrift.gif":"/pics/tarif/utskrift1.gif"));
    OutLink.addParameter("catal_action","file");
    OutLink.addParameter("union_id",union_id);

    Link BookingLink = new Link(new Image(menuNr == 7?"/pics/tarif/boka.gif":"/pics/tarif/boka1.gif"),"/tarif/paymentbook.jsp");
   //Rollback.addParameter("catal_action","rollback");
    BookingLink.addParameter("union_id",union_id);

    LinkTable.add(sidan,1,1);
    if(isAdmin){
      LinkTable.add(UpdateLink,1,1);
      LinkTable.add(ViewLink,1,1);
      //LinkTable.add(SaveLink,1,1);
      LinkTable.add(PriceLink,1,1);
      //LinkTable.add(OutLink,1,1);
      LinkTable.add(BookingLink,1,1);

    }
    return LinkTable;
  }

  private Table makeSubZeroTable(){
    int tablelines = 4;
    if(Values != null)
     tablelines += Values.length;
    Table T = new Table(7,tablelines);
    T.setBorder(0);
    T.setWidth(tablewidth);
    T.setColumnAlignment(1, "center");
    T.setColumnAlignment(3, "right");
    T.setColumnAlignment(4, "right");
    T.setColumnAlignment(5, "right");
    T.setColumnAlignment(6, "right");

    T.setHorizontalZebraColored(DarkColor,LightColor);
    T.setCellpadding(cellpadding);
    T.setCellspacing(cellspacing) ;

    T.add("Nr",1,1);
    T.add("L\u00fdsing",2,1);
    T.add("Upph\u00e6\u00f0",3,1);


    T.add("Fjöldi",4,1);
    T.add("%",5,1);
    T.add("Upphæð",6,1);
    T.add("Listi",7,1);

    if(Values != null){
      int sum = 0;
      int count = 0;
      String val;
      java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
      java.text.DecimalFormat df= new java.text.DecimalFormat("#0.0" );
      for (int i = 0; i < Values.length; i++) {
        for (int j = 0; j < (Values[i].length-5); j++) {
          val = Values[i][j];
          if(j == 2)
            val = nf.format(Integer.parseInt(val))+" Kr";
          T.add(val,j+1,i+2 );
        }
        if(totals != null){
          T.add(totals[i][0].toString() ,4,i+2);
          int c = (totals[i][0]).intValue();
          count = count + c;
          if(memberCount != 0) T.add(df.format((((float)c/memberCount)*100))+" %",5,i+2);
          Integer d = totals[i][1];
          if(d !=null){
            T.add(nf.format(d)+" Kr" ,6,i+2);
            sum = sum + totals[i][1].intValue();
          }
        }
        if(mbsVectorArray != null){
        Link L = new Link(" Listi "+(i+1));
        L.addParameter("catal_action","list");
        L.addParameter("catal_list_number",i);
        L.addParameter("union_id",union_id);
        T.add(L,7,i+2);
        }

      }
      if(totals != null){
        T.setRowColor(tablelines, "#FFFFFF");
        T.add("ALLS  : ",3,tablelines);
        if(memberCount != 0){
          //T.add(Integer.toString(count)+"/"+memberCount,4,tablelines);
          T.add(String.valueOf(memberCount),4,tablelines);
        }
        else{
          T.add(Integer.toString(count),5,tablelines);
        }
        T.add(nf.format(sum)+" Kr",6,tablelines);
        if(mbsVectorArray != null){
          if(mbsVectorArray[mbsVectorArray.length-2] != null && mbsVectorArray[mbsVectorArray.length-2].size() > 0){
            Link L = new Link(" Óháðir " );
            L.addParameter("catal_action","list");
            L.addParameter("catal_list_number",mbsVectorArray.length-2 );
            L.addParameter("union_id",union_id);
            L.addParameter("listextra","ups");
            T.add(totals[totals.length-2][0].toString() ,4,tablelines-2);
            //T.add(nf.format(totals[totals.length-2][1])+" Kr" ,6,tablelines-2);
            T.add(L,6,tablelines-2);

          }
          if(mbsVectorArray[mbsVectorArray.length-1] != null && mbsVectorArray[mbsVectorArray.length-1].size() > 0){
            Link L = new Link(" Afgangs " );
            L.addParameter("catal_action","list");
            L.addParameter("catal_list_number",mbsVectorArray.length-1 );
            L.addParameter("union_id",union_id);
            L.addParameter("listextra","not");
            T.add(totals[totals.length-1][0].toString() ,4,tablelines-1);
            //T.add(nf.format(totals[totals.length-1][1])+" Kr" ,6,tablelines-1);
            T.add(L,7,tablelines-1);
          }
        }
      }
    }
    return T;
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
  private void control(ModuleInfo modinfo) throws IOException{

    try{/*
      if(modinfo.getRequest().getParameter("union_id") != null){
         union_id = modinfo.getRequest().getParameter("union_id");
      }
      else{*/
         union_id = (String)  modinfo.getSession().getAttribute("golf_union_id");
      //}

      if(modinfo.getSession().getAttribute("member_login")!= null){
        Cashier = (Member) modinfo.getSession().getAttribute("member_login");
        cashier_id = Cashier.getID();
      }

      try{
        if(union_id == null) { add("Enginn Klúbbur");return;}
        un_id = Integer.parseInt(union_id)  ;
      }
      catch(NumberFormatException nfe){ add("Enginn klúbbur");}
      unions = (Union[]) (new Union()).findAllByColumn("union_id",un_id);
      unionName = unions[0].getName();
      unionAbbrev = unions[0].getAbbrevation();

      boolean hasSomeValues = false;
      fileSeparator = System.getProperty("file.separator");
      filepath = modinfo.getServletContext().getRealPath(fileSeparator+"files");

      if(modinfo.getRequest().getParameter("catal_action") == null){
        doMain(modinfo);
      }
      if(modinfo.getRequest().getParameter("catal_action") != null){
        catal_action = modinfo.getRequest().getParameter("catal_action");

        if(catal_action.equals("main")){ doMain(modinfo); }
        if(catal_action.equals("change")){ doChange(modinfo); }
        if(catal_action.equals("update")){ doUpdate(modinfo); }
        if(catal_action.equals("view")){ doView(modinfo); }
        if(catal_action.equals("save")){ doSave(modinfo); }
        if(catal_action.equals("list")){ doList(modinfo); }
        if(catal_action.equals("file")){ doFile(modinfo); }
        if(catal_action.equals("price")){ doNotPrice(modinfo); }
        if(catal_action.equals("skrifa")){ doSkrifa(modinfo); }
        if(catal_action.equals("gjalda")){ doPrice(modinfo); }
        if(catal_action.equals("updateextra")){ doUpdateExtra(modinfo); }
        if(catal_action.equals("seek")){ doSeek(modinfo); }

      }
    }
    catch(SQLException S){S.printStackTrace();}
    //catch(IOException IO){IO.printStackTrace();}
    //catch(Exception E){ E.printStackTrace(); add("<br> villa "+E.toString()  );}
}

    private void doMain(ModuleInfo modinfo) throws SQLException {
      PriceCatalogue pricecatalog = new PriceCatalogue();
      List CatalogList = this.getCatalogList(union_id);
      java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
      Table MainTable = makeMainTable(1);
      int count ;
      if(CatalogList != null)
        count = CatalogList.size();
      else
        count = 0;
      Table catalTable = new Table(3,count+1);
      catalTable.setWidth(tablewidth);
      catalTable.setHorizontalZebraColored(DarkColor,LightColor);
      catalTable.setCellpadding(cellpadding);
      catalTable.setCellspacing(cellspacing) ;
      catalTable.setColumnAlignment(3, "right");
      catalTable.add("Nr",1,1);
      catalTable.add("L\u00fdsing",2,1);
      catalTable.add("Upph\u00e6\u00f0",3,1);
      if(isAdmin){
        if(count > 0){
          PriceCatalogue PC;
          String activeCats[][] = new String[count][7];  // used to store in_use, extra_info strings
          for (int i = 0;i < count;i++){
            PC = (PriceCatalogue) CatalogList.get(i);
            catalTable.add(String.valueOf(i+1),1,i+2);
            catalTable.add(PC.getName(),2,i+2);
            catalTable.add(nf.format(PC.getPrice())+" Kr",3,i+2);
          }
          Values = this.fetchValues(union_id);
          this.setCatalogIds(modinfo,CatalogIds);
          setValues( modinfo , Values );
          setValuesCount( modinfo , count );
        }
      }
      MainTable.add(catalTable,2,3);
      add(MainTable);
    }



    private void doChange(ModuleInfo modinfo) throws SQLException{
      Form myForm = new Form();
      myForm.maintainAllParameters();
      Values = getValues( modinfo );

      int count = this.getValuesCount(modinfo) ;

      Table inputTable =  new Table(7,inputLines+1);
      inputTable.setWidth(tablewidth);
      inputTable.setCellpadding(2);
      inputTable.setCellspacing(1);
      inputTable.setColumnAlignment(1,"right");
      inputTable.setHorizontalZebraColored(DarkColor,LightColor);
      inputTable.add("Nr",1,1);
      inputTable.add("L\u00fdsing",2,1);
      inputTable.add("Upph\u00e6\u00f0",3,1);
      inputTable.add("Fr\u00e1",4,1);
      inputTable.add("Til",5,1);
      //inputTable.add("Stofnár",6,1);
      inputTable.add("Hverjir",6,1);
      inputTable.add("Tegund",7,1);

      for (int i = 1; i < inputLines+1 ;i++){
        String rownum = String.valueOf(i);
        String s = "";
        TextInput textInput, priceInput, ageFromInput, ageToInput, membForInput;
        DropdownMenu drpGender, drpExtra;
        String genderValue,extraValue;

        String tmp;

        if(Values != null && i <= Values.length ){
          textInput  = new TextInput("catal_text"+i,(Values[i-1][1].equalsIgnoreCase("null")?"":Values[i-1][1]));
          priceInput = new TextInput("catal_price"+i,(Values[i-1][2].equalsIgnoreCase("null")?"":Values[i-1][2]));
          ageFromInput = new TextInput("catal_agefrom"+i,(Values[i-1][3].equalsIgnoreCase("null")?"":Values[i-1][3]));
          ageToInput = new TextInput("catal_ageto"+i,(Values[i-1][4].equalsIgnoreCase("null")?"":Values[i-1][4]));
          membForInput = new TextInput("catal_memberfor"+i,(Values[i-1][5].equalsIgnoreCase("null")?"":Values[i-1][5]));
          genderValue = (Values[i-1][6].equalsIgnoreCase("null")?"A":Values[i-1][6]);
          extraValue = (Values[i-1][7].equalsIgnoreCase("null")?"I":Values[i-1][7]);
          drpGender = new DropdownMenu("catal_gender"+i);
          drpExtra = new DropdownMenu("catal_extra"+i);
          drpGender.addMenuElement("A","Allir");
          drpGender.addMenuElement("M","Karlar");
          drpGender.addMenuElement("F","Konur");
          drpGender.addMenuElement("P","Makar");
          drpGender.addMenuElement("C","Hjón");
          drpGender.addMenuElement("G","Fjölskylda");
          drpGender.addMenuElement("N","Nýliðar");
          drpGender.setSelectedElement(genderValue);
          drpExtra.addMenuElement("I","Aðal");
          drpExtra.addMenuElement("N","Auka");
          drpExtra.addMenuElement("L","Skáp");
          drpExtra.setSelectedElement(extraValue);

        }
        else{
          textInput  = new TextInput("catal_text"+i);
          priceInput = new TextInput("catal_price"+i);
          ageFromInput = new TextInput("catal_agefrom"+i);
          ageToInput = new TextInput("catal_ageto"+i);
          membForInput = new TextInput("catal_memberfor"+i);
          drpGender = new DropdownMenu("catal_gender"+i);
          drpExtra = new DropdownMenu("catal_extra"+i);
          drpGender.addMenuElement("A","Allir");
          drpGender.addMenuElement("M","Karlar");
          drpGender.addMenuElement("F","Konur");
          drpGender.addMenuElement("P","Makar");
          drpGender.addMenuElement("C","Hjón");
          drpGender.addMenuElement("G","Fjölskylda");
          drpGender.addMenuElement("N","Nýliðar");
          drpExtra.addMenuElement("I","Aðal");
          drpExtra.addMenuElement("N","Auka");
          drpExtra.addMenuElement("L","Skáp");
        }
        textInput.setSize(20);
        priceInput.setSize(4);
        priceInput.setAsIntegers();
        ageFromInput.setSize(1);ageFromInput.setMaxlength(2);
        ageToInput.setSize(1); ageToInput.setMaxlength(2) ;
        membForInput.setSize(2);membForInput.setMaxlength(4);
        membForInput.setAsIntegers();

        inputTable.add(rownum,1,i+1);
        inputTable.add(textInput,2,i+1);
        inputTable.add(priceInput,3,i+1);
        inputTable.add(ageFromInput,4,i+1);
        inputTable.add(ageToInput,5,i+1);

        inputTable.add(drpGender,6,i+1);
        inputTable.add(drpExtra,7,i+1);

      }
      myForm.add(new HiddenInput("numofcatal", String.valueOf(inputLines) ));
      myForm.add(new HiddenInput("catal_action","update" ));
      myForm.add(inputTable);
      myForm.add(new SubmitButton(new Image("/pics/tarif/uppfaera.gif")));

      Table MainTable = makeMainTable(2);
      MainTable.add(myForm,2,3);

      add(MainTable);
    }

    private void doUpdate(ModuleInfo modinfo) throws SQLException{
      int number = Integer.parseInt(modinfo.getRequest().getParameter("numofcatal"));
      int cols = 8;
      boolean hasNull = false;
      if (Values == null);
        Values = new String[number][cols];
      String text;
      String price;
      String agefrom;
      String ageto;
      String gender;
      String extra;

      for (int i = 1; i < number+1 ;i++){
        count = i;
        text = modinfo.getRequest().getParameter("catal_text"+i );
        price = modinfo.getRequest().getParameter("catal_price"+i);
        agefrom = modinfo.getRequest().getParameter("catal_agefrom"+i);
        ageto = modinfo.getRequest().getParameter("catal_ageto"+i);
        gender = modinfo.getRequest().getParameter("catal_gender"+i);
        extra = modinfo.getRequest().getParameter("catal_extra"+i);

        if(text.equalsIgnoreCase("")){
           text = "null";
           if(!hasNull){
            numOfCat = i-1;
            hasNull = true;
           }
        }

        if(price.equalsIgnoreCase("") ) price = "null";
        if(agefrom.equalsIgnoreCase("") ) agefrom = "null";
        if(ageto.equalsIgnoreCase("")) ageto = "null";

        Values[i-1][0] = String.valueOf(i);
        Values[i-1][1] = text;
        Values[i-1][2] = price;
        Values[i-1][3] = agefrom;
        Values[i-1][4] = ageto;
        Values[i-1][5] = "null";
        Values[i-1][6] = gender;
        Values[i-1][7] = extra;

      }// for lykkja
      String[][] parsedValues = new String[numOfCat][cols];
      for(int k = 0; k < numOfCat; k++){
        for(int m = 0; m < Values[k].length;m++){
          parsedValues[k][m] = Values[k][m];
        }
      }

      Values = parsedValues;
      this.setValues( modinfo , Values );
      this.setValuesCount(modinfo,numOfCat) ;
      this.removeMemberVectorArray(modinfo);
      Table MainTable = makeMainTable(3);
      MainTable.add(makeSubZeroTable(),2,3);
      Text T = new Text("<H3>Veldu SKOÐA til að fá útreikninga í töfluna<br> VISTA til að vista gjaldskrá í gagnagrunn</H3>");
      MainTable.add(T ,2,3);
      add(MainTable);
    }

    private void doView(ModuleInfo modinfo) {
      this.getEveryThing(modinfo);
      Link findLink = new Link(new Image("/pics/tarif/finna.gif"));
      findLink.addParameter("catal_action","seek");
      Table MainTable = makeMainTable(3);
      MainTable.add(findLink,2,4);
      MainTable.add(makeSubZeroTable(),2,3);
      add(MainTable);
    }

    private void doSeek(ModuleInfo modinfo){
      try{
      seekThem(modinfo);
      }
      catch(SQLException sql){sql.printStackTrace();}
      doView(modinfo);
    }

    private void doSave(ModuleInfo modinfo) throws SQLException{
      this.getEveryThing(modinfo);
      Text messageText;
      if(Values != null){
      	makeAllUnUsable();
      	saveCatalogs();
        removeMemberTotals(modinfo);
      	messageText = new Text("<H3> Gjaldskrá var vistuð ! </H3> ");
      }
      else{
      	messageText = new Text("<H3> Ekkert vistað !  </H3> ");
      }
      Table MainTable = makeMainTable(3);
      MainTable.add(makeSubZeroTable(),2,3);
      MainTable.add(messageText,2,3);
      add(MainTable);
    }

    private void doList(ModuleInfo modinfo) throws SQLException{
      this.getEveryThing(modinfo);

      int number = Integer.parseInt(modinfo.getRequest().getParameter("catal_list_number"));
      String listextra = modinfo.getRequest().getParameter("listextra");
      boolean ExtraCatalogs = false;
      boolean PcCheck = false;
      java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
      PriceCatalogue[] pcs = null;

      DropdownMenu drpXPCs = new DropdownMenu();

      Form myForm = new Form();
      myForm.maintainAllParameters();

      Table MainTable = makeMainTable(3);

      if(mbsVectorArray[number] != null){
      //Member[] mbs = mbsArray[number];
      Vector mbs = mbsVectorArray[number];
      Collections.sort(mbs);
      int len  =  mbs.size();

      int pclen = 0;
      String[] strDisplays = new String[1];
      int[] pcIds = new int[1];

      Table T = new Table(9,len+2);
      T.add("Nr",1,1);
      T.add("Nafn",2,1);
      T.add("Kennitala",4,1);
      if(listextra != null){
        ExtraCatalogs = true;
        T.add("Sérflokkur",8,1);
        List pcList = this.getExtraCatalogList(union_id);
        if(pcList != null){
        Object[] obs = pcList.toArray();
        int l = obs.length;
        pcs = new PriceCatalogue[l];
        for(int c = 0; c < l ; c++){
          pcs[c] = (PriceCatalogue) obs[c];
        }
        if(listextra.equalsIgnoreCase("not")){
          PcCheck = true;
          drpXPCs = new DropdownMenu("catal_extrapcdrop");
          drpXPCs.addMenuElement("0","Enginn");

          pclen = pcs.length;
          strDisplays = new String[pclen];
          pcIds = new int[pclen];
          for(int r = 0; r < pclen ; r++){
            strDisplays[r]  = pcs[r].getName() + "\t" + nf.format(pcs[r].getPrice()) + " Kr";
            pcIds[r] = pcs[r].getID();
          }
        }}
      }
      for(int b = 0; b < len ; b++){
        Member M = (Member) mbs.elementAt(b);
        T.add(String.valueOf(b+1),1,b+2);
        String name = M.getName();
        Link L = new Link(name,"/clubs/member.jsp");
        int mnr = M.getID();
        L.addParameter("member_id",mnr);
        T.add(L,2,b+2);
        T.add(M.getSocialSecurityNumber(),4,b+2);
        T.add("      ",5,b+2);
        Link L2 = new Link( "G", "/tarif/accountview.jsp");
        L2.addParameter("member_id",mnr);

        if(ExtraCatalogs){
          if(PcCheck ){
              drpXPCs = new DropdownMenu("catal_extrapcdrop"+b);
              drpXPCs.addMenuElement("0","Enginn");
              String strDisplay;
              for(int r = 0; r < pclen; r++){
                drpXPCs.addMenuElement(pcIds[r],strDisplays[r]);
              }
              myForm.add(new HiddenInput("membernumber"+b, String.valueOf(mnr) ));
              T.add(drpXPCs , 8 , b+2);
          }
          else{
            UnionMemberInfo umi = M.getUnionMemberInfo(union_id,String.valueOf(mnr));
            int pcnr = umi.getPriceCatalogueID();
            PriceCatalogue p = new PriceCatalogue(pcnr);
            T.add(p.getName(),8,b+2);
            T.add(nf.format(p.getPrice())+" Kr",9,b+2);
          }
        }
         T.add(L2,6,b+2);
      }

      if(ExtraCatalogs && PcCheck || pcs == null){
        myForm.add(new HiddenInput("catal_action","updateextra" ));
        myForm.add(new HiddenInput("numofextra", String.valueOf(len) ));
        T.add(new SubmitButton(new Image("/pics/tarif/uppfaera.gif")),8,len+2);
        T.add(new Link(new Image("/pics/tarif/skra.gif"),"/tarif/pcmake.jsp"),8,len+2);
      }

      myForm.add(T);
      MainTable.add(makeSubZeroTable(),2,3);
      MainTable.add(myForm,2,4);
      MainTable.add("<H2><BLINK>  Muna að vista breytingar !</BLINK></H2> ",2,3);
      }
      else
         MainTable.add("<H2><BLINK>  Enginn afgangur !</BLINK></H2> ",2,3);

      add(MainTable);
    }

    private void doUpdateExtra(ModuleInfo modinfo) throws SQLException{
      this.getEveryThing(modinfo);
      int number = Integer.parseInt(modinfo.getRequest().getParameter("numofextra"));

      int extraCatalNumber;
      String membernumber,strExtraCatalNumber;
      UnionMemberInfo umi;
      Member M;
      for (int i = 0; i < number ;i++){
        strExtraCatalNumber = modinfo.getRequest().getParameter("catal_extrapcdrop"+i);
        extraCatalNumber = Integer.parseInt(strExtraCatalNumber);
        membernumber = modinfo.getRequest().getParameter("membernumber"+i);
        if(extraCatalNumber != 0){
          M = new Member(Integer.parseInt(membernumber));
          umi = M.getUnionMemberInfo(union_id,membernumber);
          System.err.print(extraCatalNumber);
          umi.setPriceCatalogueID(extraCatalNumber);
          umi.update();
        }
      }
      this.removeMemberVectorArray(modinfo);
      Table MainTable = makeMainTable(3);
      MainTable.add(makeSubZeroTable(),2,3);
      add(MainTable);
    }

    private void doFile(ModuleInfo modinfo) throws SQLException{
      PaymentRound[] rounds = (PaymentRound[]) (new PaymentRound()).findAllByColumnDescendingOrdered("union_id", union_id,"round_date");

      Form myForm = new Form();
      myForm.maintainAllParameters();
      DropdownMenu drpRound = new DropdownMenu("catal_round");
      drpRound.addDisabledMenuElement("noround","Engin");
      if(rounds != null){
        for(int i = 0; i < rounds.length ; i++){
          drpRound.addMenuElement(rounds[i].getID() , rounds[i].getRoundDate().toString() );
        }
      }

      DropdownMenu drpBank = new DropdownMenu("catal_bank");
      drpBank.addMenuElement("nobnk", "Enginn");
      drpBank.addMenuElement("spsj", "Sparisjóður");
      drpBank.addMenuElement("lbnk","Landsbanki");

      TextInput bankofficeInput  = new TextInput("catal_bank_office");
      bankofficeInput.setAsIntegers();
      bankofficeInput.setLength(4);
      bankofficeInput.setSize(4);
      bankofficeInput.setMaxlength(4);

      DropdownMenu drdFinalPayDay = new DropdownMenu("catal_finalpayday");
      drdFinalPayDay.addDisabledMenuElement("0", "Enginn");
      for(int i = 1; i < 31;i++){ drdFinalPayDay.addMenuElement( String.valueOf(i));}

      TextInput B1input = new TextInput("catal_girotext1");B1input.setMaxlength(70);B1input.setSize(70);
      TextInput B2input = new TextInput("catal_girotext2");B2input.setMaxlength(70);B2input.setSize(70);
      TextInput B3input = new TextInput("catal_girotext3");B3input.setMaxlength(70);B3input.setSize(70);
      TextInput B4input = new TextInput("catal_girotext4");B4input.setMaxlength(70);B4input.setSize(70);

      Table T = new Table(3,10);
      //T.setHorizontalZebraColored(DarkColor,LightColor);
      T.setCellpadding(cellpadding);
      T.setCellspacing(cellspacing) ;
      T.setWidth(tablewidth) ;
      T.add("Álagning",1,1);
      T.add("Banki",1,3);
      T.add("Útibú banka",1,4);
      T.add("Eindagi ",1,5);
      T.add("Texti Gíróseðla :",1,6);
      T.add(" 1.Textalína",1,7);
      T.add(" 2.Textalína",1,8);
      T.add(" 3.Textalína",1,9);
      T.add(" 4.Textalína",1,10);

      T.add(drpRound ,3,1);
      T.add(drpBank,3,3);
      T.add(bankofficeInput,3,4);
      T.add(drdFinalPayDay,3,5);
      T.add(B1input,3,7);
      T.add(B2input,3,8);
      T.add(B3input,3,9);
      T.add(B4input,3,10);
      //T.add(new SubmitButton(new Image("/pics/tarif/skrifa.gif")),1,10);

      // KreditCard part
      DropdownMenu drpKredit = new DropdownMenu("catal_kreditcc");
      drpKredit.addMenuElement("nocr","Ekkert");
      drpKredit.addMenuElement("euro","Euro");
      drpKredit.addMenuElement("visa","Visa");

      TextInput kreditPrInput  = new TextInput("catal_kredit_pr");
      TextInput kreditKrInput  = new TextInput("catal_kredit_kr");
      kreditPrInput.setAsIntegers();
      kreditKrInput.setAsIntegers();
      kreditPrInput.setLength(4);
      kreditPrInput.setSize(4);
      kreditPrInput.setMaxlength(4);
      kreditKrInput.setLength(4);
      kreditKrInput.setSize(4);
      kreditKrInput.setMaxlength(4);

      TextInput kreditContractNr  = new TextInput("catal_kredit_contract");

      Table T2 = new Table(3,6);
      //T2.setHorizontalZebraColored(DarkColor,LightColor);
      T2.setCellpadding(cellpadding);
      T2.setCellspacing(cellspacing) ;
      T2.setWidth(tablewidth) ;
      T2.add("Kortafyrirtæki",1,2);
      T2.add("Færslugjald (%) ",1,3);
      T2.add("Færslugjald (kr)",1,4);
      T2.add("Samningsnúmer",1,5);

      T2.add( drpKredit,3,2 );
      T2.add( kreditPrInput,3,3 );
      T2.add( kreditKrInput,3,4 );
      T2.add( kreditContractNr,3,5 );
      T2.add(new SubmitButton(new Image("/pics/tarif/skrifa.gif")),1,6);

      Table MainTable = makeMainTable(6);
      MainTable.add(new HiddenInput("catal_action","skrifa" ));
      MainTable.add(T,2,3);
      MainTable.add(T2,2,3);
      myForm.add(MainTable);
      add(myForm);
    }

    private void doSkrifa(ModuleInfo modinfo) throws SQLException,IOException{
      String bank = modinfo.getRequest().getParameter("catal_bank");
      String bnkofc = modinfo.getRequest().getParameter("catal_bank_office");
      String kreditcc = modinfo.getRequest().getParameter("catal_kreditcc");
      String roundstring = modinfo.getRequest().getParameter("catal_round");
      String Message = "";
      double prosent = 0;
      int amount = 0;
      try{
        if(!roundstring.equalsIgnoreCase("noround")){
          int roundid = Integer.parseInt(roundstring);
          if(!bank.equalsIgnoreCase("nobnk")){
            if(bnkofc != null || !bnkofc.equals("")){
            int bankOffice = Integer.parseInt(bnkofc);
            int finalpayday = Integer.parseInt(modinfo.getRequest().getParameter("catal_finalpayday"));
            String B1input = modinfo.getRequest().getParameter("catal_girotext1");
            String B2input = modinfo.getRequest().getParameter("catal_girotext2");
            String B3input = modinfo.getRequest().getParameter("catal_girotext3");
            String B4input = modinfo.getRequest().getParameter("catal_girotext4");
            GiroFile GF = new GiroFile(roundid,bankOffice,finalpayday,B1input,B2input,B3input,B4input);
            GF.makeFile(modinfo);
            }
          }
          if(!kreditcc.equalsIgnoreCase("nocr")){
            String kreditPr = modinfo.getRequest().getParameter("catal_kredit_pr");
            String kreditKr = modinfo.getRequest().getParameter("catal_kredit_kr");
            String contractnr = modinfo.getRequest().getParameter("catal_kredit_contract");

            if(kreditPr.equalsIgnoreCase("")){
              add("prósenta tómt");
              prosent = 0.0;
              if(kreditKr != null)
              amount = Integer.parseInt(kreditKr);
            }
            else if(kreditKr.equalsIgnoreCase("")){
              amount = 0;
              if(kreditPr != null){
                prosent = Double.parseDouble(kreditPr);
                prosent = (prosent / 100.0 )+ 1.0;
              }
            }
            if(kreditcc.equalsIgnoreCase("euro")){
              EuroFile EF = new EuroFile(roundid,contractnr,prosent,amount);
              EF.makeFile(modinfo);
            }
            if(kreditcc.equalsIgnoreCase("visa")){
              VisaFile VF = new VisaFile(roundid,contractnr,prosent,amount);
              VF.makeFile(modinfo);

            }
          }
           Message =("<H3>Skrá var vistuð</H3>");
        }
        else
          Message = "<H3> Engin skrá var gerð - engin álagning</H3>";
      }
      catch(NumberFormatException e){
        Message = "<H3> Engin skrá var gerð</H3>";
      }
      finally{
        add(makeMainTable(6));
        add(Message);
        //add(modinfo.getRequest().getRequestURI());
      }
    }

    private void doNotPrice(ModuleInfo modinfo)throws SQLException{
      this.getEveryThing(modinfo);
      Form myForm = new Form();
      myForm.maintainAllParameters();

      IntegerInput GiroBankInterest = new IntegerInput("catal_giro_bankinterest");
      GiroBankInterest.setLength(4);
      IntegerInput EuroBankInterest = new IntegerInput("catal_euro_bankinterest");
      EuroBankInterest.setLength(4);
      IntegerInput VisaBankInterest = new IntegerInput("catal_visa_bankinterest");
      VisaBankInterest.setLength(4);

      IntegerInput GiroBankCost = new IntegerInput("catal_giro_bankcost");
      GiroBankCost.setLength(4);
      IntegerInput EuroBankCost = new IntegerInput("catal_euro_bankcost");
      EuroBankCost.setLength(4);
      IntegerInput VisaBankCost = new IntegerInput("catal_visa_bankcost");
      VisaBankCost.setLength(4);

      CheckBox GiroBox = new CheckBox("catal_giro_choice","true");
      CheckBox EuroBox = new CheckBox("catal_euro_choice","true");
      CheckBox VisaBox = new CheckBox("catal_visa_choice","true");

      DropdownMenu drdGiroDefaultInstallment = new DropdownMenu("catal_giro_installments");
      DropdownMenu drdGiroFirstInstMonth = new DropdownMenu("catal_giro_firstmonth");
      DropdownMenu drdGiroPaymentDay = new DropdownMenu("catal_giro_payday");
      for(int i = 1; i < 13; i++){ drdGiroDefaultInstallment.addMenuElement( String.valueOf(i));  }
      for(int i = 1; i < 31;i++){ drdGiroPaymentDay.addMenuElement( String.valueOf(i));}
      for(int i = 1; i < 13; i++){  drdGiroFirstInstMonth.addMenuElement( String.valueOf(i));}
      drdGiroDefaultInstallment.setSelectedElement("6");
      drdGiroFirstInstMonth.setSelectedElement("2");

      DropdownMenu drdEuroDefaultInstallment = new DropdownMenu("catal_euro_installments");
      DropdownMenu drdEuroFirstInstMonth = new DropdownMenu("catal_euro_firstmonth");
      DropdownMenu drdEuroPaymentDay = new DropdownMenu("catal_euro_payday");
      for(int i = 1; i < 13; i++){ drdEuroDefaultInstallment.addMenuElement( String.valueOf(i));  }
      for(int i = 1; i < 31;i++){ drdEuroPaymentDay.addMenuElement( String.valueOf(i));}
      for(int i = 1; i < 13; i++){  drdEuroFirstInstMonth.addMenuElement( String.valueOf(i));}
      drdEuroDefaultInstallment.setSelectedElement("6");
      drdEuroFirstInstMonth.setSelectedElement("2");

      DropdownMenu drdVisaDefaultInstallment = new DropdownMenu("catal_visa_installments");
      DropdownMenu drdVisaFirstInstMonth = new DropdownMenu("catal_visa_firstmonth");
      DropdownMenu drdVisaPaymentDay = new DropdownMenu("catal_visa_payday");
      for(int i = 1; i < 13; i++){ drdVisaDefaultInstallment.addMenuElement( String.valueOf(i));  }
      for(int i = 1; i < 31;i++){ drdVisaPaymentDay.addMenuElement( String.valueOf(i));}
      for(int i = 1; i < 13; i++){  drdVisaFirstInstMonth.addMenuElement( String.valueOf(i));}
      drdVisaDefaultInstallment.setSelectedElement("6");
      drdVisaFirstInstMonth.setSelectedElement("2");

      Table DefineTable = new Table(4,9);
      DefineTable.setWidth(tablewidth);
      DefineTable.setCellspacing(1);
      DefineTable.setCellpadding(2);
      DefineTable.setColumnColor(2, LightColor);
      DefineTable.setColumnColor(3, DarkColor);
      DefineTable.setColumnColor(4, LightColor);
      DefineTable.setColumnColor(1,LightColor);

      DefineTable.add(" Gíró",2,3);
      DefineTable.add(" Euro",3,3);
      DefineTable.add(" Visa",4,3);
      DefineTable.add(GiroBox,2,3);
      DefineTable.add(EuroBox,3,3);
      DefineTable.add(VisaBox,4,3);

      DefineTable.add("Prósenta kostnaðar (%)",1,5);
      DefineTable.add("Upphæð kostnaðar pr.seðil",1,6);
      DefineTable.add("Greiðslufjöldi",1,7);
      DefineTable.add("Mánaðardagur gjalddaga",1,8);
      DefineTable.add("Mánuður fyrsta gjalddaga",1,9);

      DefineTable.add(GiroBankInterest,2,5);
      DefineTable.add(GiroBankCost,2,6);
      DefineTable.add(drdGiroDefaultInstallment,2,7);
      DefineTable.add(drdGiroPaymentDay,2,8);
      DefineTable.add(drdGiroFirstInstMonth,2,9);

      DefineTable.add(EuroBankInterest,3,5);
      DefineTable.add(EuroBankCost,3,6);
      DefineTable.add(drdEuroDefaultInstallment,3,7);
      DefineTable.add(drdEuroPaymentDay,3,8);
      DefineTable.add(drdEuroFirstInstMonth,3,9);


      DefineTable.add(VisaBankInterest,4,5);
      DefineTable.add(VisaBankCost,4,6);
      DefineTable.add(drdVisaDefaultInstallment,4,7);
      DefineTable.add(drdVisaPaymentDay,4,8);
      DefineTable.add(drdVisaFirstInstMonth,4,9);

      Table MainTable = makeMainTable(5);
      MainTable.add(DefineTable,2,3);
      myForm.add(new HiddenInput("catal_action","gjalda" ));
      MainTable.add(new SubmitButton(new Image("/pics/tarif/leggjaa.gif")),2,3);
      myForm.add( MainTable );
      add(myForm);
    }

    private void doPrice(ModuleInfo modinfo) throws SQLException{
      this.getEveryThing(modinfo);

      String ifGiro = modinfo.getRequest().getParameter("catal_giro_choice");
      String ifEuro = modinfo.getRequest().getParameter("catal_euro_choice");
      String ifVisa = modinfo.getRequest().getParameter("catal_visa_choice");
      int defGiroInstlm=0,defGiroPayday=0,defGiroFirstMonth=0;
      int defEuroInstlm=0,defEuroPayday=0,defEuroFirstMonth=0;
      int defVisaInstlm=0,defVisaPayday=0,defVisaFirstMonth=0;
      int iGiroCost = 0,iEuroCost = 0, iVisaCost = 0;
      double  dGiroInterest = 0.0,dEuroInterest = 0.0, dVisaInterest = 0.0;

      if(mbsVectorArray == null){
        seekThem(modinfo);
      }

      if(mbsVectorArray != null){
      System.err.print("\nÁlagning hefst "+ new idegaTimestamp().toSQLTimeString());

        if(ifGiro != null &&ifGiro.equalsIgnoreCase("true")){
          defGiroInstlm =  Integer.parseInt( modinfo.getRequest().getParameter("catal_giro_installments"));
          defGiroPayday =  Integer.parseInt( modinfo.getRequest().getParameter("catal_giro_payday"));
          defGiroFirstMonth =  Integer.parseInt( modinfo.getRequest().getParameter("catal_giro_firstmonth"));
          String sGiroInterest =  modinfo.getRequest().getParameter("catal_giro_bankinterest");
          String sGiroCost =  modinfo.getRequest().getParameter("catal_giro_bankcost");
          if( !sGiroInterest.equalsIgnoreCase("")){
            dGiroInterest = Double.parseDouble(sGiroInterest);
          }
          else if( !sGiroCost.equalsIgnoreCase("")){
            iGiroCost = Integer.parseInt(sGiroCost);
          }
          letThemPay(modinfo , 1 , defGiroInstlm , defGiroFirstMonth , defGiroFirstMonth ,dGiroInterest,iGiroCost);

        }
        if(ifEuro != null && ifEuro.equalsIgnoreCase("true")){
          defEuroInstlm =  Integer.parseInt( modinfo.getRequest().getParameter("catal_euro_installments"));
          defEuroPayday =  Integer.parseInt( modinfo.getRequest().getParameter("catal_euro_payday"));
          defEuroFirstMonth =  Integer.parseInt( modinfo.getRequest().getParameter("catal_euro_firstmonth"));
          String sEuroInterest =  modinfo.getRequest().getParameter("catal_euro_bankinterest");
          String sEuroCost =  modinfo.getRequest().getParameter("catal_euro_bankcost");
          if( !sEuroInterest.equalsIgnoreCase("")){
            dEuroInterest = Double.parseDouble(sEuroInterest);
          }
          else if( !sEuroCost.equalsIgnoreCase("")){
            iEuroCost = Integer.parseInt(sEuroCost);
          }
          letThemPay(modinfo , 2 , defEuroInstlm , defEuroFirstMonth , defEuroFirstMonth , dEuroInterest,iEuroCost);

        }
        if(ifVisa != null && ifVisa.equalsIgnoreCase("true")){
          defVisaInstlm =  Integer.parseInt( modinfo.getRequest().getParameter("catal_visa_installments"));
          defVisaPayday =  Integer.parseInt( modinfo.getRequest().getParameter("catal_visa_payday"));
          defVisaFirstMonth =  Integer.parseInt( modinfo.getRequest().getParameter("catal_visa_firstmonth"));
          String sVisaInterest =  modinfo.getRequest().getParameter("catal_visa_bankinterest");
          String sVisaCost =  modinfo.getRequest().getParameter("catal_visa_bankcost");
          if( !sVisaInterest.equalsIgnoreCase("")){
            dVisaInterest = Double.parseDouble(sVisaInterest);
          }
          else if( !sVisaCost.equalsIgnoreCase("")){
            iVisaCost = Integer.parseInt(sVisaCost);
          }
          letThemPay(modinfo , 3 , defVisaInstlm , defVisaFirstMonth , defVisaFirstMonth,dVisaInterest, iVisaCost );

        }
      System.err.print("\nÁlagningu lýkur "+ new idegaTimestamp().toSQLTimeString());
      add("<H2><BLINK>  Álagningu er lokið ! </BLINK></H2> ");
      }
      else{
      add("<H2><BLINK>  Engin álagning var gerð ! </BLINK></H2> ");
      }
      add(makeMainTable(5));
    }

     private void setValues(ModuleInfo modinfo , String[][] values){
       modinfo.getSession().setAttribute("catalog_values", values);
    }
    private String[][] getValues(ModuleInfo modinfo){
       if(modinfo.getSession().getAttribute("catalog_values")!= null){
        String S[][] = (String[][]) modinfo.getSession().getAttribute("catalog_values");
        return S;
        }
        else return null;
    }
    private void removeValues(ModuleInfo modinfo){
      if(modinfo.getSession().getAttribute("catalog_values")!= null){
        modinfo.getSession().removeAttribute("catalog_values");
      }
    }

    private void setCatalogIds(ModuleInfo modinfo , int[] catalogids){
       modinfo.getSession().setAttribute("catalog_catalogids", catalogids);
    }
    private int[] getCatalogIds(ModuleInfo modinfo){
       if(modinfo.getSession().getAttribute("catalog_catalogids")!= null){
        int S[] = (int[]) modinfo.getSession().getAttribute("catalog_catalogids");
        return S;
        }
        else return null;
    }
    private void removeCatalogIds(ModuleInfo modinfo){
      if(modinfo.getSession().getAttribute("catalog_catalogids")!= null){
        modinfo.getSession().removeAttribute("catalog_catalogids");
      }
    }
    private void setValuesCount(ModuleInfo modinfo , int count){
      modinfo.getSession().setAttribute("catalog_count", new Integer(count));
    }
    private int getValuesCount(ModuleInfo modinfo){
      if(modinfo.getSession().getAttribute("catalog_count")!= null){
      Integer I = (Integer)modinfo.getSession().getAttribute("catalog_count");
      return I.intValue();
      }
      else return 0;
    }
    private void removeValuesCount(ModuleInfo modinfo){
      if(modinfo.getSession().getAttribute("catalog_count")!= null){
        modinfo.getSession().removeAttribute("catalog_count");
      }
    }

    private void setMemberArray(ModuleInfo modinfo,Member[][] memberarray){
     modinfo.getSession().setAttribute("catalog_victims", memberarray);
    }
    private Member[][] getMemberArray(ModuleInfo modinfo){
     if(modinfo.getSession().getAttribute("catalog_victims")!= null){
       return (Member[][])modinfo.getSession().getAttribute("catalog_victims" );
      }
      else return null;
    }
    private void setMemberVectorArray(ModuleInfo modinfo,Vector[] V){
     modinfo.getSession().setAttribute("catalog_victs", V);
    }
    private Vector[] getMemberVectorArray(ModuleInfo modinfo){
     if(modinfo.getSession().getAttribute("catalog_victs")!= null){
       return (Vector[]) modinfo.getSession().getAttribute("catalog_victs" );
      }
      else return null;
    }

    private void removeMemberVectorArray(ModuleInfo modinfo){
      if(modinfo.getSession().getAttribute("catalog_victs")!= null){
        modinfo.getSession().removeAttribute("catalog_victs");
      }
    }
    private void setMbsInfoVectorArray(ModuleInfo modinfo,Vector[] V){
     modinfo.getSession().setAttribute("catalog_totalinfo", V);
    }
    private Vector[] getMbsInfoVectorArray(ModuleInfo modinfo){
     if(modinfo.getSession().getAttribute("catalog_totalinfo")!= null){
       return (Vector[]) modinfo.getSession().getAttribute("catalog_totalinfo" );
      }
      else return null;
    }

    private void removeMbsInfoVectorArray(ModuleInfo modinfo){
      if(modinfo.getSession().getAttribute("catalog_totalinfo")!= null){
        modinfo.getSession().removeAttribute("catalog_totalinfo");
      }
    }

    private void setTotalMembersVector(ModuleInfo modinfo,Vector[] V){
     modinfo.getSession().setAttribute("catalog_totalmembers", V);
    }
    private Vector[] getTotalMembersVector(ModuleInfo modinfo){
     if(modinfo.getSession().getAttribute("catalog_totalmembers")!= null){
       return (Vector[]) modinfo.getSession().getAttribute("catalog_totalmembers" );
      }
      else return null;
    }

    private void removeTotalMembersVector(ModuleInfo modinfo){
      if(modinfo.getSession().getAttribute("catalog_totalmembers")!= null){
        modinfo.getSession().removeAttribute("catalog_totalmembers");
      }
    }

    private void setMemberTotals(ModuleInfo modinfo, Integer[][] totals){
      modinfo.getSession().setAttribute("catalog_totals", totals);
    }
    private Integer[][] getMemberTotals(ModuleInfo modinfo){
      if(modinfo.getSession().getAttribute("catalog_totals")!= null){
        Integer[][] T = (Integer[][])modinfo.getSession().getAttribute("catalog_totals" );
        return T;
      }
      else return null;
    }
    private void removeMemberTotals(ModuleInfo modinfo){
      if(modinfo.getSession().getAttribute("catalog_totals")!= null){
        modinfo.getSession().removeAttribute("catalog_totals");
      }
    }
    private void setMemberCount(ModuleInfo modinfo ,int membercount){
      modinfo.getSession().setAttribute("catalog_member_count", new Integer(membercount));
    }
    private int getMemberCount(ModuleInfo modinfo ){
      if(modinfo.getSession().getAttribute("catalog_member_count")!= null){
        Integer mc = (Integer)modinfo.getSession().getAttribute("catalog_member_count");
        return mc.intValue();
      }
      else return 0;
    }
     private void removeMemberCount(ModuleInfo modinfo){
      if(modinfo.getSession().getAttribute("catalog_member_count")!= null){
        modinfo.getSession().removeAttribute("catalog_member_count");
      }
    }

    private void setAccountsHash(ModuleInfo modinfo ,Hashtable H){
      modinfo.getSession().setAttribute("catalog_accounthsh", H);
    }
    private Hashtable getAccountsHash(ModuleInfo modinfo ){
      if(modinfo.getSession().getAttribute("catalog_accounthsh")!= null){
        Hashtable H = (Hashtable)modinfo.getSession().getAttribute("catalog_accounthsh");
        return H;
      }
      else return null;
    }
     private void removeAccountsHash(ModuleInfo modinfo){
      if(modinfo.getSession().getAttribute("catalog_accounthsh")!= null){
        modinfo.getSession().removeAttribute("catalog_accounthsh");
      }
    }

    private List getCatalogList(String union_id) throws SQLException{
      List L = EntityFinder.findAllByColumn(new PriceCatalogue(),"union_id",union_id,"in_use","Y","is_independent","N");
      return L;
    }

    public static List getExtraCatalogList(String union_id) throws SQLException{
      return EntityFinder.findAllByColumn(new PriceCatalogue(),"union_id",union_id,"in_use","Y","is_independent","Y");
    }

    public PriceCatalogue[] getExtraCatalogues(String unionID) throws SQLException{
      PriceCatalogue[] PCs = (PriceCatalogue[])(new PriceCatalogue()).findAll( getExtraCatalogueSQL(unionID));
      return PCs;
    }

    // Fetch the objects saved in the Session
    private void getEveryThing(ModuleInfo modinfo){
      Values = getValues(modinfo);
      mbsVectorArray = this.getMemberVectorArray( modinfo );
      mbsInfoVectorArray = this.getMbsInfoVectorArray(modinfo);
      totals = this.getMemberTotals( modinfo );
      TotalMembersVector = this.getTotalMembersVector(modinfo);
      memberCount = this.getMemberCount( modinfo );
      CatalogIds = this.getCatalogIds( modinfo );
      AccHsh = this.getAccountsHash();
    }


    private String[][] fetchValues(String union_id) throws SQLException{
      List CatalogList = this.getCatalogList(union_id);
      int count = 0;
      if(CatalogList != null){
        count = CatalogList.size();
      }
      String activeCats[][] = new String[count][8];
      CatalogIds = new int[count];
      if(count > 0){
        //String activeCats[][] = new String[count][8];  // used to store in_use, extra_info strings
        StringTokenizer st;
        for (int i = 0;i < count;i++){
          PriceCatalogue C = (PriceCatalogue) CatalogList.get(i);
          CatalogIds[i] = C.getID();
          st = new StringTokenizer(C.getExtraInfo(),"#");
          int a =0;
            while ((st.hasMoreTokens()) && a < 8) {
              activeCats[i][a]= st.nextToken();
              a++;
            }
          }
      }
     return activeCats;
  }

  private void saveCatalogs()throws SQLException{
    PriceCatalogue pcl;
    for(int i = 0; i < Values.length; i++){
      String text = Values[i][1];
      String price = Values[i][2];
      if(!(text.equalsIgnoreCase("null")) && !(price.equalsIgnoreCase("null"))){
        pcl = new PriceCatalogue();
        pcl.setUnion_id(this.un_id);
        pcl.setName(text);
        pcl.setPrice( Integer.valueOf(price));
        StringBuffer SB = new StringBuffer();
        for(int k = 0; k < Values[i].length ; k++){
          SB.append(Values[i][k]);
          SB.append("#");
        }
        pcl.setExtraInfo(SB.toString()) ;
        pcl.setInUse(true);
        pcl.setIndependent(false);
        pcl.insert();
      }
    }
  }

  private void makeAllUnUsable() throws SQLException{

    List CatalogList = this.getCatalogList(this.union_id);
    if(CatalogList != null){
      int count = CatalogList.size();
      PriceCatalogue PC;
      for(int i = 0; i < count; i++){
        PC = (PriceCatalogue) CatalogList.get(i);
        PC.setInUse(false);
        PC.setIndependent(false);
        PC.update();
      }
    }
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

  private String getActiveMembersSQL(int iUnionID){
    StringBuffer SQLstringbuff = new StringBuffer("select * from union_member_info where union_id = '");
    SQLstringbuff.append(iUnionID);
    SQLstringbuff.append("' and member_status = 'A' ");
    return SQLstringbuff.toString();
  }

  private String getDependentMembersSQL(int iUnionID){
    return this.getActiveMembersSQL(iUnionID)+" and (price_catalogue_id is null or price_catalogue_id = '0')  ";
  }

  private String getIndependentMembersSQL(int iUnionID){
    return this.getActiveMembersSQL(iUnionID)+" and (price_catalogue_id is not null and price_catalogue_id != '0') ";
  }

  private Hashtable getFamilyHash(List InfoList){
    Hashtable FamilyHash = new Hashtable();
    int iListLength = InfoList.size();
    UnionMemberInfo umi;
    Integer iFamilyId,iMemberId;
    Integer FamCount;
    Integer Temp;
    String sFamStat;
    for(int i = 0; i < iListLength; i++){
      umi = (UnionMemberInfo) InfoList.get(i);

      iFamilyId = new Integer(umi.getFamilyId());
      if(FamilyHash.containsKey(iFamilyId)){
        FamCount = (Integer)FamilyHash.get(iFamilyId);
        int c = FamCount.intValue()+1;
        Temp = (Integer)FamilyHash.put(iFamilyId,new Integer(c));
      }
      else{
        Temp = (Integer)FamilyHash.put(iFamilyId,new Integer(1));
      }
    }
    return FamilyHash;
  }

  private Hashtable getAccountsHash(){

    try{
      Account[] A = (Account[])new Account().findAllByColumn("union_id",this.union_id);
      if(A!=null && A.length > 0){
        int len = A.length;
        Hashtable H = new Hashtable();
        for(int i = 0; i < len;i++){
          H.put(new Integer(A[i].getMemberId()),new Integer(A[i].getID()));
        }
        return H;
      }
      return null;
    }
    catch (SQLException sql){
      return null;
    }

  }

   private int[] findPriceGroups(int iAge, int iRegYear, String strGender, boolean bFamily,boolean bCouple, String strFamilyStatus ,boolean isNew,boolean hasLocker){
    Vector V = new Vector();
    Vector V2 = new Vector();
    int lowestPrice = 0;
    int mainGroup = -1;
    boolean noMain = false;
    for(int i = 0; i < Values.length ; i++){
      int tempgr = -1;
      String text    = Values[i][1];        // Description
      String price   = Values[i][2];        // Price
      String agefrom = Values[i][3];        // Age From
      String ageto   = Values[i][4];        // Age To
      //String regyear = Values[i][5];        // registration year
      String gender  = Values[i][6];        // "A" all "M" male or "F" female or "P" partner "G" family "C" couple
      String extra   = Values[i][7];        // "I" main "N" extra "L" locker
      int priceAmount = Integer.parseInt(price);
      int ageFrom = 0, ageTo = 200, registrationYear = 0;
      String memberGender = "A", Extra = "I";
      String thegender, thefamstat;
      if(!(text.equalsIgnoreCase("null")) & !(price.equalsIgnoreCase("null"))){
        if(!(agefrom.equalsIgnoreCase("null"))){  ageFrom = Integer.parseInt(agefrom);}
        if(!(ageto.equalsIgnoreCase("null")))  {  ageTo = Integer.parseInt(ageto);       }
        //if(!(regyear.equalsIgnoreCase("null"))){  registrationYear = Integer.parseInt(regyear);   }
        if(!(gender.equalsIgnoreCase("null"))) {  memberGender = gender;       }
        if(!(extra.equalsIgnoreCase("null")))  {  Extra = extra;         }


          if( (iAge >= ageFrom) && (iAge <= ageTo)){
            if( gender.equalsIgnoreCase("G") ){
              if( bFamily && strFamilyStatus.equalsIgnoreCase("")){
                tempgr = i;
              }
              else
                noMain = true;
            }
            else if( gender.equalsIgnoreCase("C")){
              if(bCouple && !strFamilyStatus.equalsIgnoreCase(""))
                tempgr = i;
              else
                noMain = true;
            }
            else if(  gender.equalsIgnoreCase("P") && strFamilyStatus.equalsIgnoreCase("partner") ){
              tempgr = i;
            }
            else if( isNew && gender.equalsIgnoreCase("N")){
              tempgr = i;
            }
            else if( strGender.equalsIgnoreCase(gender) ){
              tempgr = i;
            }
            else if( gender.equalsIgnoreCase("A")){
              tempgr = i;
            }

          }
        }

        if(!noMain && tempgr != -1 && extra.equalsIgnoreCase("I")){
          if( lowestPrice == 0 ){
            mainGroup = tempgr;
            lowestPrice = priceAmount;
          }
          else if( priceAmount < lowestPrice ){
            mainGroup = tempgr;
          }
        }
        else if(tempgr != -1 && !extra.equalsIgnoreCase("I")){
          if(extra.equalsIgnoreCase("L") && hasLocker)
            V.addElement(new Integer(tempgr));
          else if( extra.equalsIgnoreCase("N"))
            V.addElement(new Integer(tempgr));
        }
      }// for loop

      int[] iReturnArray = null;
      if(!V.isEmpty()){
        iReturnArray = new int[V.size()+1];
        int iplus = 0;
        if(mainGroup != -1){
          iReturnArray[0] = mainGroup;
          iplus = 1;
        }
        for(int index = 0+iplus; index < V.size()+iplus; index++){
          iReturnArray[index] = ((Integer)V.elementAt(index-iplus)).intValue();
        }
      }
      else if(mainGroup != -1 ){
        iReturnArray = new int[1];
        iReturnArray[0] = mainGroup;
      }

      return iReturnArray;
  }

  private void seekThem(ModuleInfo modinfo) throws SQLException{
    this.Values = this.fetchValues(union_id);
    if(this.Values != null){
    idegaTimestamp dateToday = new idegaTimestamp(idegaTimestamp.getTimestampRightNow());
    int thisYear = dateToday.getYear();
    int linecount = Values.length;
    int colcount = Values[0].length;
    int listlen = linecount +2;
    System.err.println("Byrja Leit"+ idegaTimestamp.RightNow().toString());
    List IndependentMembersInfoList = EntityFinder.findAll( new UnionMemberInfo() ,  this.getIndependentMembersSQL(this.un_id) );
    List MembersInfoList = EntityFinder.findAll( new UnionMemberInfo() , this.getDependentMembersSQL(this.un_id) );
    System.err.println("Leit Lokið"+ idegaTimestamp.RightNow().toString());
    this.memberCount = MembersInfoList.size();

    Vector[] TotalMembersVector = new Vector[2];
    for(int j = 0; j < TotalMembersVector.length ; j++){
      TotalMembersVector[j] = new Vector(memberCount);
    }

    this.mbsVectorArray = new Vector[listlen];
    this.mbsInfoVectorArray = new Vector[listlen];
    for(int h = 0; h < listlen; h++){
      mbsVectorArray[h] = new Vector();
      mbsInfoVectorArray[h] = new Vector();
    }

    Member member;
    UnionMemberInfo umi;

    int age = 0, mbid, count = 0, pcid = 0, instlm = 1, pmid = 0, groupnr = 0,lockergrnr=0,extragrnr=0, familygroup = 100, famid = 0, regYear = 0;
    idegaTimestamp instDate = new idegaTimestamp();
    String strName, strKt, strGender, strFamStatus ="", Price = "0";
    String locker;
    int totprice;
    int iFamilyCount;
    int iAccountId;
    boolean isNew,hasLocker;
    Vector extragroupsV;
    Hashtable FamilyHash = getFamilyHash(MembersInfoList);
    System.err.println("Sortera " +memberCount + " " + idegaTimestamp.RightNow().toString());
    this.AccHsh = new Hashtable();
    for(int a = 0; a < memberCount; a++){
      isNew = false;
      hasLocker = false;
      totprice = 0;
      iFamilyCount = 1;
      umi = (UnionMemberInfo) MembersInfoList.get(a);
      mbid = umi.getMemberID();
      member = new Member(mbid);
      strName = member.getName();
      strKt = member.getSocialSecurityNumber();
      strGender = member.getGender();
      pcid = umi.getPriceCatalogueID();
      instlm = umi.getPreferredInstallmentNr();
      pmid = umi.getPaymentTypeID();

      famid = umi.getFamilyId();
      if(FamilyHash.containsKey(new Integer(famid)))
        iFamilyCount = ((Integer)FamilyHash.get(new Integer(famid))).intValue();
      boolean bCouple = (iFamilyCount == 2 ? true:false);
      boolean bFamily = (iFamilyCount >= 2 ? true:false);
      strFamStatus = umi.getFamilyStatus();
      if(strFamStatus == null || strFamStatus.length() == 0)
        strFamStatus = "";
      if(umi.getLockerNumber() != null && umi.getLockerNumber().length() > 0)
         hasLocker = true;
      if(umi.getFirstInstallmentDate() != null)
        instDate = new idegaTimestamp(umi.getFirstInstallmentDate());
      else
        instDate = new idegaTimestamp(1,1,2000);
      if(umi.getRegistrationDate() != null)
        regYear = new idegaTimestamp(umi.getRegistrationDate()).getYear();
      else
        regYear = 0;
      isNew = (regYear == thisYear)?true:false;
      int ktYear = 0;
      if(strKt.length() >= 6){
          ktYear = Integer.parseInt(strKt.substring(4,6));
        if(strKt.length() == 10){
          if( strKt.endsWith("9")) ktYear += 1900;
          if( strKt.endsWith("0")) ktYear += 2000;
        }
        else
          ktYear += 1900;
      }
      age = thisYear - ktYear ;
      //System.out.print(mbid+" \t"+strGender+" ");
      int[] iGroups = findPriceGroups(age, regYear, strGender, bFamily,bCouple, strFamStatus,isNew ,hasLocker);
      addMemberToMembersArray(mbsVectorArray,mbsInfoVectorArray,member,umi,iGroups);
      //System.out.println(age);
      if(iGroups != null){
        TotalMembersVector[0].addElement(umi);
        TotalMembersVector[1].addElement(iGroups);
      }
    }// for loop
    System.err.println("fjöldi reikninga "+AccHsh.size());

    System.err.println("Sortera búin " + idegaTimestamp.RightNow().toString());
    this.totals = new Integer[listlen][2];
    System.err.println("Óháðir " + idegaTimestamp.RightNow().toString());
    if(IndependentMembersInfoList!=null){
      mbsInfoVectorArray[mbsInfoVectorArray.length-2] = new Vector(IndependentMembersInfoList);
      UnionMemberInfo UMI;
      int[] grArray = new int[1];
      grArray[0] = -2;
      for(int i = 0; i < IndependentMembersInfoList.size();i++){
        UMI = (UnionMemberInfo) IndependentMembersInfoList.get(i);
        mbsVectorArray[mbsVectorArray.length-2].addElement(new Member(UMI.getMemberID()));
        TotalMembersVector[0].addElement(UMI);
        TotalMembersVector[1].addElement(grArray);
      }
    }
    System.err.println("Óháðir Búnir " + idegaTimestamp.RightNow().toString());
    System.err.println("Samtölur " + idegaTimestamp.RightNow().toString());
    for(int u = 0; u < listlen ; u++){
      int s = mbsVectorArray[u].size();
      totals[u][0] = new Integer(s);
      if(u < linecount)
        totals[u][1] = new Integer(s * Integer.parseInt(Values[u][2]));
    }
    System.err.println("Samtölur búnar" + idegaTimestamp.RightNow().toString());
    if(TotalMembersVector == null) System.err.println("Vector er tomur");
    else System.err.println(TotalMembersVector[0].size());
    this.setMemberTotals( modinfo , totals );
    this.setMemberVectorArray( modinfo , mbsVectorArray );
    this.setMbsInfoVectorArray( modinfo , mbsInfoVectorArray );
    this.setMemberCount( modinfo , memberCount ) ;
    this.setTotalMembersVector( modinfo , TotalMembersVector );
    }
    else
      add("<H2><BLINK>  Tókst ekki að leita! </BLINK></H2> ");
  }// seekThem

  private void setEverything( ModuleInfo modinfo ){
    this.setMemberTotals( modinfo , totals );
    this.setMemberVectorArray( modinfo , mbsVectorArray );
    this.setMbsInfoVectorArray( modinfo , mbsInfoVectorArray );
    this.setMemberCount( modinfo , memberCount) ;
    this.setTotalMembersVector( modinfo, TotalMembersVector );
  }

  private void letThemPay(ModuleInfo modinfo, int payTypeId, int defaultInstallmentNr, int defaultFirstMonth, int defaultPayday,double fBankInterest,int iBankCost) throws SQLException{
    //System.err.print("\nÁlagning hefst "+ new idegaTimestamp().toSQLTimeString());
    int giroID=1;
    int euroID=2;
    int visaID=3;
    StringBuffer RoundName = new StringBuffer("Félagsgjöld ");
    RoundName.append(this.unionAbbrev);
    switch(payTypeId){
      case 1: RoundName.append(" Gíro"); break;
      case 2: RoundName.append(" Euro"); break;
      case 3: RoundName.append(" Vísa"); break;
    }
    idegaTimestamp dateToday = idegaTimestamp.RightNow();
    Timestamp T = dateToday.getTimestamp();
    idegaTimestamp prefdate;

    PaymentRound round = new PaymentRound();
    round.setRoundDate( dateToday.getTimestamp() );
    round.setUnionId( new Integer( union_id ) );
    round.setName(RoundName.toString());
    round.insert();
    int roundnr =  round.getID();

    int defInstlm = defaultInstallmentNr;
    int defPayday = defaultPayday;
    int defFirstMonth = defaultFirstMonth;
    int pmId = payTypeId;

    int thisYear = dateToday.getYear();
    int thisMonth =  defFirstMonth;
    int thisDay = defPayday;

    double Multi = fBankInterest/100 + 1;

    int memberID=0,totalinst = 1,pmID=1,price=0,priceChange=0,firstprice=0;
    int totalinstallments,CatalogID=0,smallprice=0,instCount;
    String pcName="Félagsgjöld "+this.unionAbbrev,ifInst = "I";
    PriceCatalogue PC = new PriceCatalogue();
    boolean extra = false;
    int totalPrice = 0;

    CatalogIds = this.getCatalogIds(modinfo);

    UnionMemberInfo UMI;
    int[] mbgr;
    if(TotalMembersVector != null){
    int mlen = TotalMembersVector[0].size();
    //System.err.println("Byrja álagningu"+idegaTimestamp.RightNow().toString());
    //
    Hashtable AccountsHash = getAccountsHash();

    for(int i = 0; i < mlen ; i++){

      price = 0;
      StringBuffer sbExtra = new StringBuffer("");
      UMI = (UnionMemberInfo) TotalMembersVector[0].elementAt(i);
      pmID = UMI.getPaymentTypeID();
      if(pmID == 0) pmID = giroID;
      if(pmID == payTypeId){
        //System.err.print("\n"+i+" "+UMI.getMemberID()+" ");
        mbgr = (int[]) TotalMembersVector[1].elementAt(i);

        String gName = "";
        int gPrice = 0;

        memberID  = UMI.getMemberID();
        totalinst = UMI.getPreferredInstallmentNr();
        pmID = UMI.getPaymentTypeID();
        java.sql.Date D = UMI.getFirstInstallmentDate();
        if(D!=null){
          prefdate = new idegaTimestamp(D);
          thisMonth = prefdate.getMonth();
          thisDay = prefdate.getDay();
        }

        int iAccountId = -1;
        if(AccountsHash != null && AccountsHash.containsKey(new Integer(memberID)))
          iAccountId =((Integer)AccountsHash.get(new Integer(memberID))).intValue();
        else
          iAccountId = this.makeNewAccount(memberID,this.un_id,"",this.cashier_id);

        for(int j = 0; j < mbgr.length; j++){

          // Dependent catalogues
          if(mbgr[j] >= 0 ){
            //System.err.print(" "+mbgr[j]+" ");
            gPrice = Integer.parseInt(Values[mbgr[j]][2]);
            gPrice = (int) Math.floor(gPrice * Multi);
            price +=  gPrice;
            gName = Values[mbgr[j]][1];
            sbExtra.append(Values[mbgr[j]][1]);sbExtra.append( " " );
            sbExtra.append(String.valueOf(gPrice)) ;sbExtra.append( "," );
          }
          // inDependent catalogues
          else if(mbgr[j] == -2){
            PriceCatalogue Pr = new PriceCatalogue(UMI.getPriceCatalogueID());
            gPrice = Pr.getPrice();
            gPrice = (int) Math.floor(gPrice * Multi);
            price += gPrice;
            gName =  Pr.getName();
            sbExtra.append( Pr.getName() );sbExtra.append( " " );
            sbExtra.append( String.valueOf(gPrice));sbExtra.append( " , " );
          }

          if(iAccountId != -1)
          this.makeAccountEntry(iAccountId,-gPrice,gName,"Álagning","","","",this.cashier_id,T,T);

        }

        totalinstallments = (totalinst != 0 ? totalinst : defInstlm );
        instCount = totalinstallments;
        smallprice = price/totalinstallments;
        priceChange = price%totalinstallments;
        firstprice = smallprice + priceChange;

        int thePrice ;
        Timestamp Paydate;
        for(int k = 0 ; k < instCount ; k++){
          Paydate = new idegaTimestamp(thisYear,thisMonth+k,thisDay,0,0,0).getTimestamp();
          thePrice = smallprice;
          if (k==0)
           thePrice = firstprice;
          makePayment( memberID , roundnr , thePrice , false , pcName ,sbExtra.toString() , (k+1) , totalinstallments , pmID , Paydate, T, cashier_id);
          totalPrice += thePrice;
        }
      }
    } // i  loop
    round.setTotals(totalPrice);
    round.update();
    }
  }//letThemPay

  private void makePayment(int memberID, int RoundId, int Price , boolean Status , String Name, String Info , int InstallmentNumber, int Totalinstallments, int PaymentTypeID, Timestamp PayDate ,Timestamp last_updated, int cashier_id) throws SQLException {
    Payment P = new Payment();
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
    P.setUnionId(this.un_id);
    P.insert();
  }

  private int makeNewAccount(int MemberId,int UnionId,String Name,int CashierId){
    Account A = new Account();
    A.setBalance(0);
    A.setCashierId(CashierId);
    A.setCreationDate(idegaTimestamp.getTimestampRightNow());
    A.setLastUpdated(idegaTimestamp.getTimestampRightNow());
    A.setMemberId(MemberId);
    A.setUnionId(UnionId);
    int ret = -1;
    try{
      A.insert() ;
      ret = A.getID();
    }
    catch(SQLException sql){
      sql.printStackTrace() ;
      ret = -1;
    }
    return ret;
  }

  private void makeAccountEntry(int AccountId,int Price, String Name,String Info,String AccountKey,String EntryKey,String TariffKey,int CashierId,Timestamp PaymentDate,Timestamp LastUpdated) throws SQLException{
    AccountEntry E = new AccountEntry();
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
    Account A = new Account(AccountId);
    A.addToBalance(Price);
    A.update();
  }

  public void main(ModuleInfo modinfo) throws IOException {
    //isAdmin = com.idega.jmodule.object.ModuleObject.isAdministrator(this.modinfo);
    /** @todo: fixa Admin*/
    isAdmin = true;
    control(modinfo);
  }

}// class Tariffer

