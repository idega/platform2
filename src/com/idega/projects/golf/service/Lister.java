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
public class Lister extends com.idega.jmodule.object.ModuleObjectContainer {

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


  public Lister(){

    HeaderColor = "#336660";
    LightColor = "#CEDFD0";
    DarkColor = "#ADCAB1";
    OtherColor = "#6E9073 ";

    setMenuColor("#ADCAB1");//,"#CEDFD0"
    setItemColor("#CEDFD0");//"#D0F0D0"
    setInputLines(15);
    currentLocale = java.util.Locale.getDefault();
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

    try{

      if(modinfo.getRequest().getParameter("list_action") == null){
        doMain(modinfo);
      }
      if(modinfo.getRequest().getParameter("list_action") != null){
        extra_catal_action = modinfo.getRequest().getParameter("list_action");

        if(extra_catal_action.equals("main"))	{ doMain(modinfo); 		}
        if(extra_catal_action.equals("change"))	{ doChange(modinfo); 	}
        if(extra_catal_action.equals("update"))	{ doUpdate(modinfo); 	}
        if(extra_catal_action.equals("view"))	{ doView(modinfo); 		}
        if(extra_catal_action.equals("save"))	{ doSave(modinfo); 		}
        if(extra_catal_action.equals("list"))	{ doList(modinfo); 		}

      }
    }
    catch(SQLException S){	S.printStackTrace();	}
    }

    private void doMain(ModuleInfo modinfo) throws SQLException {
      add(this.makeMainTable());
    }

    private void doChange(ModuleInfo modinfo) throws SQLException{

    }

    private void doUpdate(ModuleInfo modinfo) throws SQLException,IOException{
      idegaTimestamp dateToday = new idegaTimestamp(idegaTimestamp.getTimestampRightNow());
      int thisYear = dateToday.getYear();
      String strGroup,strHandicap,strGender,strAgefrom,strAgeto,strZip;
      String strOrder1,strOrder2,strOrder3,strOrder4,strOrder5;
      int group,handicap,agefrom,ageto,zip;
      int order1,order2,order3,order4,order5;

      strGroup = modinfo.getRequest().getParameter("list_groups" );
      strHandicap = modinfo.getRequest().getParameter("list_handicap");
      strGender = modinfo.getRequest().getParameter("list_gender");
      strAgefrom = modinfo.getRequest().getParameter("list_agefrom");
      strAgeto = modinfo.getRequest().getParameter("list_ageto" );
      strZip = modinfo.getRequest().getParameter("list_zip");

      strOrder1 = modinfo.getRequest().getParameter("list_types1" );
      strOrder2 = modinfo.getRequest().getParameter("list_types2" );
      strOrder3 = modinfo.getRequest().getParameter("list_types3" );
      strOrder4 = modinfo.getRequest().getParameter("list_types4" );
      strOrder5 = modinfo.getRequest().getParameter("list_types5" );

      StringBuffer SQL = new StringBuffer();
      StringBuffer tables = new StringBuffer(" from member ");
      StringBuffer wheres = new StringBuffer(" where " );

      int count = 0;
      if(strGroup != null){   group = Integer.parseInt(strGroup); if(group != 0){tables.append(" ,group_member"); wheres.append(" member.member_id = group_member.member_id and group_member.group_id ='"+group+"' ");count++;}}
      if(strHandicap != null){  handicap = Integer.parseInt(strHandicap);}
      if(strGender != null){   if(!strGender.equalsIgnoreCase("0")){if(count != 0)wheres.append(" and "); wheres.append(" member.gender = '"+strGender+"' ");count++;}}
      if(strAgefrom != null){  agefrom = Integer.parseInt(strAgefrom); if(agefrom != 0){ int year = thisYear - agefrom  ; if(count != 0)wheres.append(" and "); wheres.append(" member.date_of_birth <= '"+year+"-01-01'");count++;}}
      if(strAgeto != null){  ageto = Integer.parseInt(strAgeto); if(ageto != 0){ int year = thisYear - ageto ;if(count != 0)wheres.append(" and ");  wheres.append(" member.date_of_birth >= '"+year+"-01-01'");count++;}}
      if(strZip != null){  zip = Integer.parseInt(strZip); }
      Vector V = new Vector();
      StringBuffer orderby = new StringBuffer();
      if(strOrder1 != null){  order1 = Integer.parseInt(strOrder1); if(order1 != 0){orderby.append(this.getOrderByString(order1));V.addElement(getOrderByString(order1)); orderby.append(",");}}
      if(strOrder2 != null){  order2 = Integer.parseInt(strOrder3); if(order2 != 0){orderby.append(this.getOrderByString(order2));V.addElement(getOrderByString(order2)); orderby.append(",");}}
      if(strOrder3 != null){  order3 = Integer.parseInt(strOrder2); if(order3 != 0){orderby.append(this.getOrderByString(order3));V.addElement(getOrderByString(order3)); orderby.append(",");}}
      if(strOrder4 != null){  order4 = Integer.parseInt(strOrder4); if(order4 != 0){orderby.append(this.getOrderByString(order4));V.addElement(getOrderByString(order4)); orderby.append(",");}}
      if(strOrder5 != null){  order5 = Integer.parseInt(strOrder5); if(order5 != 0){orderby.append(this.getOrderByString(order5));V.addElement(getOrderByString(order5)); orderby.append(",");}}
      if(orderby.toString().endsWith(",")) orderby.append("member_id");

      SQL.append("select ");
      SQL.append(orderby.toString());
      SQL.append(tables.toString());
      SQL.append(wheres.toString());
      SQL.append(" order by ");
      SQL.append(orderby.toString());

      add("select "+orderby.toString());
      add(tables.toString());
      add(wheres.toString());
      add(" order by "+orderby.toString());

      get( modinfo,  SQL.toString(),  orderby.toString());
    }

    private void doView(ModuleInfo modinfo) throws SQLException{

    }

    private void doSave(ModuleInfo modinfo) throws SQLException{

    }

    private void doList(ModuleInfo modinfo) throws SQLException{

    }

    private void get(ModuleInfo modinfo, String SQL, String O)throws SQLException,IOException{
      PrintWriter out = modinfo.getResponse().getWriter();
      Connection Conn = com.idega.util.database.ConnectionBroker.getConnection();
      Statement stmt = Conn.createStatement();
      ResultSet RS = stmt.executeQuery(SQL);
      StringBuffer sb = new StringBuffer();
      StringTokenizer S = new StringTokenizer(O,",");
      add("<br><br>");
      while ((S.hasMoreTokens()) ) {
              add( S.nextToken()+" ");
      }
      add("<br>");
      while(RS.next()){
        sb.append("<br>");
        sb.append(RS.getString("first_name"));
        sb.append("&nbsp;&nbsp;");
        sb.append(RS.getString("middle_name"));
        sb.append("&nbsp;&nbsp;");
        sb.append(RS.getString("last_name"));
        }

      stmt.close();
      add(sb.toString());
    }

    private String getOrderByString(int key){
      String s;
      switch(key){
        case(1): s = "first_name,middle_name,last_name";  break;
        case(2): s = "social_security_number";              break;
        case(3): s = "street,street_number";               break;
        case(4): s = "number" ;                             break;
        case(5): s = "email" ;                              break;
        default: s = "first_name,middle_name,last_name";  break;
      }
      return s;
    }

     private String getTableString(int key){
      String s;
      switch(key){
        case(1): s = " member "; break;
        case(2): s = " member "; break;
        case(3): s = " address "; break;
        case(4): s = " phone " ; break;
        case(5): s = " member " ; break;
        default: s = " member "; break;
      }
       return s;
    }

     private String getWhereString(int key){
      String s;
      switch(key){
        case(1): s = " member "; break;
        case(2): s = " member "; break;
        case(3): s = " address "; break;
        case(4): s = " phone " ; break;
        case(5): s = " member " ; break;
        default: s = " member "; break;
      }
       return s;
    }
    private Table makeMainTable()throws SQLException{
      Form myform = new Form();
      myform.maintainAllParameters();
      Table T = new Table(3,4);
      T.setWidth("700");
      T.setWidth(1,"100");
      T.add("<br><br><br>",2,2);
      HeaderTable HT = new HeaderTable();
      HT.setWidth(700);
      HT.setHeaderText("Listastjóri");
      HT.add("<br><br>");
      Table T2 = new Table(3,6);
      T2.setWidth(670);
      T2.setWidth(1,"100");
      T2.add(this.makeTopTable(),2,1);
      T2.add("<br>",2,2);
      T2.add(this.makeMidTable(),2,3);
      T2.add("<br>",2,4);
      T2.add(this.makeSubTable(),2,5);
      T2.add("<br>",2,6);
      HT.add(T2);
      myform.add(HT);
      T.add(myform,3,2);
      return T;
    }

    private HeaderTable makeHeaderTable(String headline){
      HeaderTable HT = new HeaderTable();
      HT.setHeaderText(headline);
      HT.setWidth(670);
      HT.setBorderColor("#000000");
      HT.setColor(LightColor);
      return HT;
    }

    private HeaderTable makeTopTable()throws SQLException{
      HeaderTable HT = makeHeaderTable("Skilgreining Flokka");
      Table T = new Table(6,3);
      //T.setWidth();
      T.add(this.drpGroup(),1,2);
      T.add(this.drpHandicapGroup(),2,2);
      T.add(this.drpGender(),3,2);
      T.add(this.drpAgeFrom(),4,2);
      T.add(this.drpAgeTo(),5,2);
      T.add(this.drpZip(),6,2);
      HT.add(T);
      return HT;
    }

    private HeaderTable makeMidTable(){
      HeaderTable HT = makeHeaderTable("Útlit Lista");
      Table T = new Table(5,3);
      T.add(this.drpOutTypes(1),1,2);
      T.add(this.drpOutTypes(2),2,2);
      T.add(this.drpOutTypes(3),3,2);
      T.add(this.drpOutTypes(4),4,2);
      T.add(this.drpOutTypes(5),5,2);
      HT.add(T);
      return HT;
    }

    private HeaderTable makeSubTable(){
      HeaderTable HT = makeHeaderTable("Aðgerðir");
      Table T = new Table(5,3);
      //T.setWidth();
      T.add(new SubmitButton(new Image("/pics/tarif/uppfaera.gif")),1,2);
      T.add(new SubmitButton(new Image("/pics/tarif/uppfaera.gif")),2,2);
      T.add(new SubmitButton(new Image("/pics/tarif/uppfaera.gif")),3,2);
      T.add(new SubmitButton(new Image("/pics/tarif/uppfaera.gif")),4,2);
      T.add(new SubmitButton(new Image("/pics/tarif/uppfaera.gif")),5,2);
      T.add(new HiddenInput("list_action","update"));
      HT.add(T);
      return HT;
    }

    private DropdownMenu drpGroup() throws SQLException{
      Group[] group = (Group[]) (new Group()).findAll();
      DropdownMenu drp = new DropdownMenu(group,"list_groups");
      drp.addDisabledMenuElement("0","Flokkar");
      drp.setSelectedElement("0");
      return drp;
    }
    private DropdownMenu drpHandicapGroup() throws SQLException{
      DropdownMenu drp = new DropdownMenu("list_handicap");
      drp.addDisabledMenuElement("0","Forgjöf");
      drp.addMenuElement("1","Meistaraflokkur");
      drp.addMenuElement("2","1.Flokkur");
      drp.addMenuElement("3","2.Flokkur");
      drp.addMenuElement("4","3.Flokkur");
      drp.addMenuElement("5","4.Flokkur");
      drp.addMenuElement("6","5.Flokkur");
      return drp;
    }
    private DropdownMenu drpGender() throws SQLException{
      DropdownMenu drp = new DropdownMenu("list_gender");
      drp.addDisabledMenuElement("0","Kyn");
      drp.addMenuElement("M","Karlar");
      drp.addMenuElement("F","Konur");
      return drp;
    }
    private DropdownMenu drpAgeFrom(){
      DropdownMenu drp = new DropdownMenu("list_agefrom");
      drp.addDisabledMenuElement("0","Aldur Frá");
       for(int i = 1; i < 120; i++){
        drp.addMenuElement(String.valueOf(i));
      }
      return drp;
    }
    private DropdownMenu drpAgeTo() throws SQLException{
      DropdownMenu drp = new DropdownMenu("list_ageto");
      drp.addDisabledMenuElement("0","Aldur Til");
      for(int i = 1; i < 120; i++){
        drp.addMenuElement(String.valueOf(i));
      }
      return drp;
    }

    private DropdownMenu drpZip() throws SQLException{
      ZipCode[] zips = (ZipCode[]) (new ZipCode()).findAllOrdered("code");
      DropdownMenu drp = new DropdownMenu("list_zip");
      drp.addDisabledMenuElement("0","Póstnr");
      for(int i = 0; i < zips.length ; i++){
        drp.addMenuElement(zips[i].getCode());
      }
      return drp;
    }

    private DropdownMenu drpOutTypes( int nameNr ){
      DropdownMenu drp = new DropdownMenu("list_types"+nameNr);
      drp.addDisabledMenuElement("0","Flokkur");
      drp.addMenuElement("1","Nafn");
      drp.addMenuElement("2","Kennitala");
      drp.addMenuElement("3","Heimili");
      drp.addMenuElement("4","Sími");
      drp.addMenuElement("5","Netfang");
      return drp;
    }


  public void main(ModuleInfo modinfo) throws IOException {
    //isAdmin = com.idega.jmodule.object.ModuleObject.isAdministrator(this.modinfo);
    /** @todo: fixa Admin*/
    isAdmin = true;
    control(modinfo);
  }
}// class Lister



