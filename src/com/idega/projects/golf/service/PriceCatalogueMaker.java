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
public class PriceCatalogueMaker extends com.idega.presentation.PresentationObjectContainer {

  private String union_id,unionName,unionAbbrev;
  private int un_id;
  private Union union;
  private String[][] Values;
  private Member thisMember;
  private Member[] unionMembers;
  private PriceCatalogue[] Catalogs;
  private String MenuColor,ItemColor,HeaderColor,LightColor,DarkColor,OtherColor;
  private boolean isAdmin = false;
  private java.util.Locale currentLocale;
  private Link sidan, UpdateLink, PriceLink;
  private Window window;
  private Member[][] mbsArray;
  private Integer[][] totals;
  private int cellspacing = 1, cellpadding = 2;
  private Thread payThread = null;
  private String tablewidth = "308";
  private int  numOfCat, inputLines, saveCount,count,memberCount=0;
  private String extra_catal_action = "";


  public PriceCatalogueMaker(){

    HeaderColor = "#336660";
    LightColor = "#CEDFD0";
    DarkColor = "#ADCAB1";
    OtherColor = "#6E9073 ";

    setMenuColor("#ADCAB1");//,"#CEDFD0"
    setItemColor("#CEDFD0");//"#D0F0D0"
    setInputLines(15);
    currentLocale = java.util.Locale.getDefault();
  }

  private Table makeMainTable(int menuNr){
    Link PiLink = new Link(new Image("/pics/tarif/pi.gif"),"/tarif/pi.jsp");

    Text HeaderText = new Text("  F\u00e9lagsgj\u00f6ld  "+ unionAbbrev);
    HeaderText.setFontColor("#FFFFFF");
    Table HeaderTable = new Table(1,1);
    HeaderTable.setColor(HeaderColor );
    HeaderTable.setWidth(tablewidth);
    HeaderTable.add(HeaderText,1,1);

    Table MainTable = new Table(1,5);
    MainTable.setCellpadding(0);
    MainTable.setCellspacing(0);
    MainTable.setWidth(tablewidth);

    MainTable.add(makeLinkTable(menuNr),1,1);
    MainTable.add(HeaderTable,1,2);
    //MainTable.add(PiLink,1,5);
    return MainTable;
  }

   private Table makeLinkTable(int menuNr){
    Table LinkTable = new Table(1,1);

    LinkTable.setWidth(tablewidth);
    LinkTable.setCellpadding(0);
    LinkTable.setCellspacing(0);

    Link sidan = new Link(new Image(menuNr == 1?"/pics/tarif/gjaldskra.gif":"/pics/tarif/gjaldskra1.gif"));//,"/tarif/tarif.jsp");
    sidan.addParameter("extra_catal_action","main");
    sidan.addParameter("union_id",union_id);

    Link UpdateLink = new Link(new Image(menuNr == 2?"/pics/tarif/breyta.gif":"/pics/tarif/breyta1.gif"));
    UpdateLink.addParameter("extra_catal_action","change");
    UpdateLink.addParameter("union_id",union_id);

    Link ViewLink = new Link(new Image(menuNr == 3?"/pics/tarif/skoda.gif":"/pics/tarif/skoda1.gif"));
    ViewLink.addParameter("extra_catal_action","view");
    ViewLink.addParameter("union_id",union_id);

    Link SaveLink = new Link(new Image(menuNr == 4?"/pics/tarif/vista.gif":"/pics/tarif/vista1.gif"));
    SaveLink.addParameter("extra_catal_action","save");
    SaveLink.addParameter("union_id",union_id);

    LinkTable.add(sidan,1,1);
    if(isAdmin){
      LinkTable.add(UpdateLink,1,1);
      LinkTable.add(ViewLink,1,1);
      LinkTable.add(SaveLink,1,1);
      LinkTable.add(PriceLink,1,1);
    }
    return LinkTable;
  }

  private Table makeSubTable(){
   int tablelines = 1;
      if(Values != null)
       tablelines += Values.length;
      Table T = new Table(3,tablelines);
      T.setBorder(0);
      T.setWidth(tablewidth);
      T.setColumnAlignment(1, "center");
      T.setColumnAlignment(3, "right");

      T.setHorizontalZebraColored(DarkColor,LightColor);
      T.setCellpadding(cellpadding);
      T.setCellspacing(cellspacing) ;

      T.add("Nr",1,1);
      T.add("L\u00fdsing",2,1);
      T.add("Upph\u00e6\u00f0",3,1);

      if(Values != null){
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
        for (int i = 0; i < Values.length; i++) {
          for (int j = 0; j < 3; j++) {
            T.add(Values[i][j],j+1,i+2 );
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
  private void control(IWContext iwc) throws IOException{

    try{
      if(iwc.getRequest().getParameter("union_id") != null){
         union_id = iwc.getRequest().getParameter("union_id");
      }
      else{
         union_id = (String)  iwc.getSession().getAttribute("golf_union_id");
      }
      un_id = Integer.parseInt(union_id)  ;
      union = new Union(un_id);
      unionName = union.getName();
      unionAbbrev = union.getAbbrevation() ;

      boolean hasSomeValues = false;

      if(iwc.getRequest().getParameter("extra_catal_action") == null){
        doMain(iwc);
      }
      if(iwc.getRequest().getParameter("extra_catal_action") != null){
        extra_catal_action = iwc.getRequest().getParameter("extra_catal_action");

        if(extra_catal_action.equals("main")){ doMain(iwc); }
        if(extra_catal_action.equals("change")){ doChange(iwc); }
        if(extra_catal_action.equals("update")){ doUpdate(iwc); }
        if(extra_catal_action.equals("view")){ doView(iwc); }
        if(extra_catal_action.equals("save")){ doSave(iwc); }
        if(extra_catal_action.equals("list")){ doList(iwc); }

      }
    }
    catch(SQLException S){S.printStackTrace();}
    //catch(IOException IO){IO.printStackTrace();}
   // catch(Exception E){ E.printStackTrace(); add("<br> villa "+E.toString()  );}
}

    private void doMain(IWContext iwc) throws SQLException {

      PriceCatalogue[] Catalogs = (PriceCatalogue[]) (new PriceCatalogue()).findAll("select * from price_catalogue where union_id = '"+union_id+"' and in_use = 'Y' and is_independent = 'Y'");
      java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
      Table MainTable = makeMainTable(1);
      int count = Catalogs.length;
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

          for (int i = 0;i < count;i++){
            catalTable.add(String.valueOf(i+1),1,i+2);
            catalTable.add(Catalogs[i].getName(),2,i+2);
            //catalTable.add(String.valueOf(Catalogs[i].getPrice())+" kr",3,i+2);
            catalTable.add(nf.format(Catalogs[i].getPrice())+" Kr",3,i+2);
          }
          Values = fetchValues(union_id);
          setValues( iwc , fetchValues(union_id) );
          setValuesCount( iwc , count );
        }
      }

      MainTable.add(catalTable,1,3);
      MainTable.setWidth(tablewidth);
      add(MainTable);
    }

    private String[][] fetchValues(String unionID) throws SQLException{
   // PriceCatalogue[] Catalogs = (PriceCatalogue[])(new PriceCatalogue()).findAllByColumn("union_id",unionID,"in_use","Y");
      PriceCatalogue[] Catalogs = (PriceCatalogue[]) (new PriceCatalogue()).findAll("select * from price_catalogue where union_id = '"+union_id+"' and in_use = 'Y' and is_independent = 'Y'");
      int count = Catalogs.length;
      String activeCats[][] = new String[count][8];
      if(count > 0){
        //String activeCats[][] = new String[count][8];  // used to store in_use, extra_info strings
        StringTokenizer st;
        for (int i = 0;i < count;i++){
        st = new StringTokenizer(Catalogs[i].getExtraInfo(),"#");
            int a =0;
            while ((st.hasMoreTokens()) && a < 8) {
              activeCats[i][a]= st.nextToken();
              a++;
            }
          }

      }
       return activeCats;
    }

    private void doChange(IWContext iwc) throws SQLException{
      Form myForm = new Form();
      myForm.maintainAllParameters();
      Values = this.getValues(iwc)  ;
      int count = this.getValuesCount(iwc);

      Table inputTable =  new Table(3,inputLines+1);
      inputTable.setWidth(tablewidth);
      inputTable.setCellpadding(2);
      inputTable.setCellspacing(1);
      inputTable.setColumnAlignment(1,"right");
      inputTable.setHorizontalZebraColored(DarkColor,LightColor);
      inputTable.add("Nr",1,1);
      inputTable.add("L\u00fdsing",2,1);
      inputTable.add("Upph\u00e6\u00f0",3,1);


      for (int i = 1; i < inputLines+1 ;i++){
        String rownum = String.valueOf(i);
        String s = "";
        TextInput textInput, priceInput, ageFromInput, ageToInput, membForInput;
        DropdownMenu drpGender, drpExtra;
        String genderValue,extraValue;

        String tmp;

        if(Values != null && i <= Values.length ){
          textInput  = new TextInput("extra_catal_text"+i,(Values[i-1][1].equalsIgnoreCase("null")?"":Values[i-1][1]));
          priceInput = new TextInput("extra_catal_price"+i,(Values[i-1][2].equalsIgnoreCase("null")?"":Values[i-1][2]));
        }
        else{
          textInput  = new TextInput("extra_catal_text"+i);
          priceInput = new TextInput("extra_catal_price"+i);
        }
        textInput.setSize(20);
        priceInput.setSize(4);
        priceInput.setAsIntegers();

        inputTable.add(rownum,1,i+1);
        inputTable.add(textInput,2,i+1);
        inputTable.add(priceInput,3,i+1);
      }
      myForm.add(new HiddenInput("numofcatal", String.valueOf(inputLines) ));
      myForm.add(new HiddenInput("extra_catal_action","update" ));
      myForm.add(inputTable);
      myForm.add(new SubmitButton(new Image("/pics/tarif/uppfaera.gif")));

      Table MainTable = makeMainTable(2);
      MainTable.add(myForm,1,3);
      add(MainTable);
    }

    private void doUpdate(IWContext iwc) throws SQLException{
      int number = Integer.parseInt(iwc.getRequest().getParameter("numofcatal"));
      int cols = 3;
      boolean hasNull = false;
      if (Values == null);
        Values = new String[number][cols];
      String text;
      String price;

      for (int i = 1; i < number+1 ;i++){
        count = i;
        text = iwc.getRequest().getParameter("extra_catal_text"+i );
        price = iwc.getRequest().getParameter("extra_catal_price"+i);

        if(text.equalsIgnoreCase("")){
           text = "null";
           if(!hasNull){
            numOfCat = i-1;
            hasNull = true;
           }
        }

        if(price.equalsIgnoreCase("") ) price = "null";

        Values[i-1][0] = String.valueOf(i);
        Values[i-1][1] = text;
        Values[i-1][2] = price;

      }// for lykkja
      String[][] parsedValues = new String[numOfCat][cols];
      for(int k = 0; k < numOfCat; k++){
        for(int m = 0; m < Values[k].length;m++){
          parsedValues[k][m] = Values[k][m];
        }
      }
      Values = parsedValues;
      this.setValues( iwc , Values );
      this.setValuesCount(iwc,numOfCat) ;

      Table MainTable = makeMainTable(3);
      MainTable.setWidth(tablewidth);
      Text T = new Text("<H3>Veldu VISTA til að vista gjaldskrá í gagnagrunn</H3>");
      MainTable.add(makeSubTable(),1,3);
      MainTable.add(T ,1,3);
      add(MainTable);
    }

    private void doView(IWContext iwc) throws SQLException{
      Values = getValues( iwc);
      Table MainTable = this.makeMainTable(3);
      MainTable.add(makeSubTable(),1,3);
      add(MainTable);
    }

    private void doSave(IWContext iwc) throws SQLException{
      Values = this.getValues( iwc );
      Text messageText;
      if(Values != null){
      	makeAllUnUsable();
      	saveCatalogs();
      	messageText = new Text("<H3> Gjaldskrá var vistuð ! </H3> ");
      }
      else{
      	messageText = new Text("<H3> Ekkert vistað !  </H3> ");
      }
      Table MainTable = makeMainTable(4);
      MainTable.setWidth(tablewidth);
      MainTable.add(messageText,1,3);
      add(MainTable);
    }

    private void doList(IWContext iwc) throws SQLException{

    }

  private void saveCatalogs()throws SQLException{
    PriceCatalogue pcl;
    for(int i = 0; i < Values.length; i++){
      String text = Values[i][1];
      String price = Values[i][2];
      if(!(text.equalsIgnoreCase("null")) && !(price.equalsIgnoreCase("null"))){
        pcl = new PriceCatalogue();
        pcl.setUnion_id(new Integer(union_id));
        pcl.setName(text);
        pcl.setPrice( Integer.valueOf(price));
        StringBuffer SB = new StringBuffer();
        for(int k = 0; k < Values[i].length ; k++){
          SB.append(Values[i][k]);
          SB.append("#");
        }
        SB.append("null#null#null#null#null#");
        pcl.setExtraInfo(SB.toString()) ;
        pcl.setInUse(true);
        pcl.setIndependent(true);
        pcl.insert();

      }
    }
  }

  private void makeAllUnUsable() throws SQLException{
  //PriceCatalogue[] pcls = (PriceCatalogue[]) (new PriceCatalogue()).findAllByColumn("union_id",union_id,"in_use","Y");
    PriceCatalogue[] pcls= (PriceCatalogue[]) (new PriceCatalogue()).findAll("select * from price_catalogue where union_id = '"+union_id+"' and in_use = 'Y' and is_independent = 'Y'");
    if(pcls != null){
    	for(int i = 0; i < pcls.length; i++){
          pcls[i].setInUse(false);
          pcls[i].setIndependent(false);
          pcls[i].update();
    	}
    }
  }

  private void setValues(IWContext iwc , String[][] values){
    iwc.getSession().setAttribute("extra_catalog_tarifs", values);
  }
  private String[][] getValues(IWContext iwc){
    String S[][] = (String[][]) iwc.getSession().getAttribute("extra_catalog_tarifs");
    return S;
  }

  private void setValuesCount(IWContext iwc , int count){
    iwc.getSession().setAttribute("extra_catalog_count", new Integer(count));
  }
  private int getValuesCount(IWContext iwc){
    if(iwc.getSession().getAttribute("extra_catalog_count")!= null){
      Integer I = (Integer)iwc.getSession().getAttribute("extra_catalog_count");
      return I.intValue();
    }
    else return 0;
  }

  public void main(IWContext iwc) throws IOException {
    //isAdmin = com.idega.presentation.PresentationObject.isAdministrator(this.iwc);
    /** @todo: fixa Admin*/
    isAdmin = true;
    control(iwc);
  }
}// class PriceCatalogueMaker


