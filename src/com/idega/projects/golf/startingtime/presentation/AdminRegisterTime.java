package com.idega.projects.golf.startingtime.presentation;

import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.projects.golf.service.StartService;
import com.idega.projects.golf.entity.Tournament;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.interfaceobject.TextInput;
import com.idega.jmodule.object.interfaceobject.Form;
import com.idega.jmodule.object.interfaceobject.DropdownMenu;
import com.idega.jmodule.object.interfaceobject.SubmitButton;
import com.idega.jmodule.object.interfaceobject.CheckBox;
import com.idega.jmodule.object.interfaceobject.FloatInput;
import com.idega.jmodule.object.interfaceobject.HiddenInput;
import com.idega.jmodule.object.Image;
import com.idega.projects.golf.GolfField;
import com.idega.util.idegaTimestamp;
import com.idega.projects.golf.entity.TournamentDay;
import com.idega.projects.golf.entity.Tournament;
import com.idega.data.EntityFinder;
import com.idega.jmodule.object.textObject.Text;
import com.idega.jmodule.object.textObject.Link;
import com.idega.projects.golf.entity.Union;
import com.idega.projects.golf.entity.Member;
import com.idega.jmodule.object.interfaceobject.CloseButton;
import com.idega.jmodule.object.interfaceobject.BackButton;
import com.idega.projects.golf.entity.StartingtimeFieldConfig;
import com.idega.projects.golf.business.GolfCacher;
import com.idega.projects.golf.entity.Startingtime;
import com.idega.util.text.TextSoap;
import com.idega.jmodule.login.business.AccessControl;

import java.sql.SQLException;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Title:        Golf
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class AdminRegisterTime extends ModuleObjectContainer {

  private StartService business;
  private Form myForm;
  private Table frameTable;
  private idegaTimestamp currentDay;
  private String currentField;
  private String currentUnion;
  private StartingtimeFieldConfig fieldInfo;

  private static String saveParameterString = "STsave";
  private static String timeParameterString  = "STtime";
  private static String nameParameterString  = "STname";
  private static String unionParameterString  = "STunion";
  private static String handycapParameterString  = "SThadycap";
  private static String deleteParameterString  = "STdelete";
  private static String groupNumParameterString  = "STgroup";
  private static String lastGroupParameterString = "STlastGroup";
  private static String timeChangeStartIDParameterString = "STstartID";

  private static String color1 = "#336661";
  private static String color2 = "#CEDFCF";
  private static String color3 = "#ADC9D0";
  private static String color5 = "#FFFFFF";
  private static String color4 = "#6E9173";
  private static String color6 = "#FF6666";

  public AdminRegisterTime() {
    super();
    myForm = new Form();
    frameTable = new Table();
    frameTable.setAlignment("center");
    frameTable.setWidth("100%");
    myForm.add(frameTable);
    this.add(Text.getBreak());
    this.add(myForm);
    business = new StartService();
  }

  public void lineUpTable(ModuleInfo modinfo) throws SQLException {

    Vector illegalTimes = new Vector(0);
    int illegalTimesIndex = 0;

    idegaTimestamp openTime = new idegaTimestamp(fieldInfo.getOpenTime());
    int minBetween = fieldInfo.getMinutesBetweenStart();

    int groupCount = idegaTimestamp.getMinutesBetween(openTime,new idegaTimestamp(fieldInfo.getCloseTime()))/minBetween;
    int countInGroups = 4;
    int lines = groupCount*countInGroups;

    frameTable.empty();

    String width = "520";
    String width1 = "70";
    String width2 = "250";
    String width3 = "70";
    String width4 = "70";
    String width5 = "60";

    Table startTable = new Table(5,lines+1);
    Table headerTable = new Table(5,1);
    Table illegalTable = null;

    startTable.setAlignment("center");
    startTable.setWidth(width);
    startTable.setCellspacing(1);

    headerTable.setAlignment("center");
    headerTable.setWidth(width);
    headerTable.setCellspacing(0);

    headerTable.setColor(color1);
/*
    startTable.setColor(1,1,color1);
    startTable.setColor(2,1,color1);
    startTable.setColor(3,1,color1);
    startTable.setColor(4,1,color1);
    startTable.setColor(5,1,color1);
*/
    startTable.add(Text.emptyString(),1,1);
    startTable.add(Text.emptyString(),2,1);
    startTable.add(Text.emptyString(),3,1);
    startTable.add(Text.emptyString(),4,1);
    startTable.add(Text.emptyString(),5,1);
    startTable.setHeight(1,"1");


    headerTable.setAlignment(1,1,"center");
    headerTable.setAlignment(3,1,"center");
    headerTable.setAlignment(4,1,"center");
    headerTable.setAlignment(5,1,"center");

    headerTable.setWidth(1,width1);
    headerTable.setWidth(2,width2);
    headerTable.setWidth(3,width3);
    headerTable.setWidth(4,width4);
//    headerTable.setWidth(5,width5);

    startTable.setWidth(1,width1);
    startTable.setWidth(2,width2);
    startTable.setWidth(3,width3);
    startTable.setWidth(4,width4);
    //startTable.setWidth(5,width5);

    startTable.setColumnAlignment(1,"center");
    startTable.setColumnAlignment(3,"center");
    startTable.setColumnAlignment(4,"center");
    startTable.setAlignment(5,1,"center");

    Text textProxy = new Text("");
    textProxy.setFontColor("#FFFFFF");

    Text time = (Text)textProxy.clone();
    time.setText("Kl.");
    time.setBold();
    headerTable.add(time,1,1);

    Text name = (Text)textProxy.clone();
    name.setText("Kennitala (Nafn)");
    name.setBold();
    headerTable.add(name,2,1);

    Text club = (Text)textProxy.clone();
    club.setText("(Klúbbur)");
    club.setBold();
    headerTable.add(club,3,1);

    Text handicap = (Text)textProxy.clone();
    handicap.setText("(Forgjöf)");
    handicap.setBold();
    headerTable.add(handicap,4,1);

    Text delete = (Text)textProxy.clone();
    delete.setText("Eyða");
    delete.setBold();
    headerTable.add(delete,5,1);

    CheckBox delCheck = new CheckBox(deleteParameterString);

    int groupCounter = 1;
    int lastGroup = -1;
    boolean insert = true;
    int[] freeGroups = new int[groupCount];
    System.err.println("freeGroups: "+freeGroups.length);
    List takenTimes = business.getStartingtimeTableEntries(this.currentDay,this.currentField);
    if(takenTimes != null){
      for (int i = 0; i < takenTimes.size(); i++) {
        Startingtime tempStart = (Startingtime)takenTimes.get(i);
        int tempGroupNum = tempStart.getGroupNum();

        if(tempGroupNum < 1){
          illegalTimes.insertElementAt(tempStart,illegalTimesIndex++);
          insert = false;
        }else{
          if(lastGroup == tempGroupNum){
            groupCounter++;
            if(groupCounter == countInGroups){
  //            System.err.println("tempGroupNum: "+tempGroupNum);
              freeGroups[tempGroupNum-1] = 1;
            }
            if(groupCounter > countInGroups){
  //            System.err.println("yfirfullt holl : "+tempGroupNum);
              illegalTimes.insertElementAt(tempStart,illegalTimesIndex++);
              //continue;
              insert = false;
            }
          }else{
            groupCounter = 1;
          }
        }
        if(insert){
          int line = (tempGroupNum-1)*countInGroups + groupCounter+1; //(-1+1) -groupCounter++ +headerLine
//          openTime.addMinutes((tempGroupNum-1)*minBetween);
//          startTable.add(TextSoap.addZero(openTime.getHour()) + ":" + TextSoap.addZero(openTime.getMinute()),1,line);
//          openTime.addMinutes(-(tempGroupNum-1)*minBetween);
          startTable.add(tempStart.getPlayerName(),2,line);
          startTable.add(tempStart.getClubName(),3,line);
          if(tempStart.getHandicap()>= 0){
            startTable.add(Float.toString(tempStart.getHandicap()),4,line);
          }else{
            startTable.add("-",4,line);
          }
          CheckBox tempDelCheck = (CheckBox)delCheck.clone();
          tempDelCheck.setContent(Integer.toString(tempStart.getID()));
          startTable.add(new HiddenInput(timeChangeStartIDParameterString,Integer.toString(tempStart.getID())),1,line);
          startTable.add(tempDelCheck,5,line);
          startTable.setAlignment(5,line,"center");


        }else{
          insert = true;
        }

        lastGroup = tempGroupNum;
      }
    }


    DropdownMenu timeMenu = new DropdownMenu(this.timeParameterString);
    for (int i = 0; i < groupCount; i++) {
      if(freeGroups[i] != 1){
        timeMenu.addMenuElement(i+1,TextSoap.addZero(openTime.getHour()) + ":" + TextSoap.addZero(openTime.getMinute()));
      }
      openTime.addMinutes(minBetween);
    }
    openTime.addMinutes(-groupCount*minBetween);
    timeMenu.addMenuElement("-","-");




    int loop = lines+2;
  //    int takenTimesIndex = 0;
    TextInput nameInput = new TextInput(nameParameterString);
    nameInput.setSize(30);
    FloatInput handycapInput = new FloatInput(handycapParameterString);
    handycapInput.setSize(3);

    DropdownMenu unionMenu = GolfCacher.getUnionAbbreviationDropdown(unionParameterString);
    unionMenu.setSelectedElement(currentUnion);
    boolean firstColor = true;
    int count = 0;
    int min = 0;
    for (int i = 1; i < loop; i++) {
      if(startTable.isEmpty(5,i)){
        min = ((i-2)/countInGroups)*minBetween;
        openTime.addMinutes(min);
        startTable.add("<b>"+TextSoap.addZero(openTime.getHour()) + ":" + TextSoap.addZero(openTime.getMinute())+"</b>",1,i);
        openTime.addMinutes(-min);
        startTable.add(nameInput,2,i);
        startTable.add(unionMenu,3,i);
        startTable.add(handycapInput,4,i);

        startTable.add(new HiddenInput(groupNumParameterString, Integer.toString((int)((i-2)/countInGroups)+1)));

      }else if(i>1){
        DropdownMenu tempTimeMenu = (DropdownMenu)timeMenu.clone();
        min = ((i-2)/countInGroups)*minBetween;
        openTime.addMinutes(min);
        tempTimeMenu.addMenuElement(Integer.toString((i-2)/countInGroups+1),TextSoap.addZero(openTime.getHour()) + ":" + TextSoap.addZero(openTime.getMinute()));
        openTime.addMinutes(-min);
        tempTimeMenu.setSelectedElement(Integer.toString((i-2)/countInGroups+1));
        startTable.add(tempTimeMenu,1,i);
        startTable.add(new HiddenInput(lastGroupParameterString,Integer.toString((i-2)/countInGroups)+1),1,i);
      }
      if(i>1){
        if(count >= countInGroups){
          if(firstColor){
            firstColor = false;
          }else{
            firstColor = true;
          }
          count = 0;
        }
        if(firstColor){
          startTable.setColor(1,i,color2);
          startTable.setColor(2,i,color2);
          startTable.setColor(3,i,color2);
          startTable.setColor(4,i,color2);
          startTable.setColor(5,i,color2);
        }else{
          startTable.setColor(1,i,color3);
          startTable.setColor(2,i,color3);
          startTable.setColor(3,i,color3);
          startTable.setColor(4,i,color3);
          startTable.setColor(5,i,color3);
        }
        count++;
      }
    }

    int illegal = illegalTimes.size();
    System.err.println("illegal = " + illegal);
    if(illegal > 0){
      illegalTable = new Table(5,illegal);
      illegalTable.setAlignment("center");
      illegalTable.setWidth(width);
      illegalTable.setCellspacing(1);

      illegalTable.setWidth(1,width1);
      illegalTable.setWidth(2,width2);
      illegalTable.setWidth(3,width3);
      illegalTable.setWidth(4,width4);
      //startTable.setWidth(5,width5);

      illegalTable.setColumnAlignment(1,"center");
      illegalTable.setColumnAlignment(3,"center");
      illegalTable.setColumnAlignment(4,"center");
      illegalTable.setAlignment(5,1,"center");


      for (int i = 1; i <= illegal; i++) {


        Startingtime tempStart = (Startingtime)illegalTimes.get(i-1);

        illegalTable.setColor(1,i,color6);
        illegalTable.setColor(2,i,color6);
        illegalTable.setColor(3,i,color6);
        illegalTable.setColor(4,i,color6);
        illegalTable.setColor(5,i,color6);


        illegalTable.add(tempStart.getPlayerName(),2,i);
        illegalTable.add(tempStart.getClubName(),3,i);
        if(tempStart.getHandicap()>= 0){
          illegalTable.add(Float.toString(tempStart.getHandicap()),4,i);
        }else{
          illegalTable.add("-",4,i);
        }
        CheckBox tempDelCheck = (CheckBox)delCheck.clone();
        tempDelCheck.setContent(Integer.toString(tempStart.getID()));
        illegalTable.add(new HiddenInput(timeChangeStartIDParameterString,Integer.toString(tempStart.getID())),1,i);
        illegalTable.add(tempDelCheck,5,i);
        illegalTable.setAlignment(5,i,"center");

        DropdownMenu tempTimeMenu = (DropdownMenu)timeMenu.clone();
        min = (tempStart.getGroupNum()-1)*minBetween;
        openTime.addMinutes(min);
        tempTimeMenu.addMenuElement(Integer.toString(tempStart.getGroupNum()),TextSoap.addZero(openTime.getHour()) + ":" + TextSoap.addZero(openTime.getMinute()));
        openTime.addMinutes(-min);
        tempTimeMenu.setSelectedElement(Integer.toString(tempStart.getGroupNum()));
        illegalTable.add(tempTimeMenu,1,i);
        illegalTable.add(new HiddenInput(lastGroupParameterString,Integer.toString(tempStart.getGroupNum())),1,i);



      }

    }
    Text dateText = new Text(this.currentDay.getISLDate());
    dateText.setBold();
    dateText.setFontSize(3);
    dateText.setFontColor("#000000");
    Table dateTable = new Table();
    dateTable.add(dateText);
    dateTable.setAlignment("center");
    dateTable.setAlignment(1,1,"left");
    dateTable.setHeight(1,"25");
    dateTable.setWidth(width);
    frameTable.add(dateTable);

    frameTable.add(headerTable);

    if(illegalTable != null){
      frameTable.add(illegalTable);
    }
    frameTable.add(startTable);


//    SubmitButton save = new SubmitButton("  Vista  ", saveParameterString+".x", "do");
    SubmitButton save = new SubmitButton(new Image("/pics/formtakks/vista.gif","Vista"), saveParameterString, "do");
    Table submSave = new Table();
    submSave.add(save);
    submSave.setAlignment("center");
    submSave.setAlignment(1,1,"right");
    submSave.setHeight(1,"30");
    submSave.setWidth(width);
    frameTable.add(submSave);
  }

  public void lineUpTournamentDay(ModuleInfo modinfo, List Tournaments){
    Text dayReserved = new Text("Dagur frátekinn fyrir mót");
    dayReserved.setFontSize(4);
    Table AlignmentTable = new Table();
    AlignmentTable.setBorder(0);
    AlignmentTable.add(Text.getBreak());
    AlignmentTable.add(dayReserved);
    for (int i = 0; i < Tournaments.size(); i++) {
      AlignmentTable.add("<p>" + ((Tournament)Tournaments.get(i)).getName());
    }
    AlignmentTable.setAlignment("center");
    AlignmentTable.add(Text.getBreak());
    AlignmentTable.add(Text.getBreak());
    Link close = new Link("Loka glugga");
//    close.addParameter(closeParameterString, "true");
    AlignmentTable.add(close);
    frameTable.empty();
    frameTable.add(AlignmentTable);
  }



  public void handleFormInfo(ModuleInfo modinfo) throws SQLException {

    String[] sentTimes = modinfo.getParameterValues(timeParameterString);
    String[] sentLastGroups = modinfo.getParameterValues(lastGroupParameterString);
    String[] sentStartIDs = modinfo.getParameterValues(timeChangeStartIDParameterString);

    String[] sentDeletes = modinfo.getParameterValues(deleteParameterString);

    String[] sentNames = modinfo.getParameterValues(nameParameterString);
    String[] sentGroupNums = modinfo.getParameterValues(groupNumParameterString);
    String[] sentUnions = modinfo.getParameterValues(unionParameterString);
    String[] sentHandycaps = modinfo.getParameterValues(handycapParameterString);

//    if(sentTimes != null)
//      System.err.println( timeParameterString+" : " + sentTimes.length );
//    if(sentLastGroups != null)
//      System.err.println( lastGroupParameterString+" : " + sentLastGroups.length );
//    if(sentDeletes != null)
//      System.err.println( deleteParameterString+" : " + sentDeletes.length );


    if(sentTimes != null){
      for (int i = 0; i < sentTimes.length; i++) {
        if(!sentTimes[i].equals(sentLastGroups[i]) || sentTimes[i].equals("-") ){
          try{
            System.err.println("");
            Startingtime tempSt = new Startingtime(Integer.parseInt(sentStartIDs[i]));
            tempSt.setGroupNum(Integer.parseInt(sentTimes[i]));
            tempSt.update();
          }catch(Exception e){
            //continue
          }
        }
      }
    }

    if(sentDeletes != null){
      for (int i = 0; i < sentDeletes.length; i++) {
        try{
          new Startingtime(Integer.parseInt(sentDeletes[i])).delete();
        }catch(Exception e){
//          System.err.println("tókst ekki að eyða tíma með id: " + sentDeletes[i] );
          // continue
        }
      }

    }

    if(sentNames != null){
      for (int i = 0; i < sentNames.length; i++) {
        if(sentNames[i] != null && !"".equals(sentNames[i]) ){
          boolean ssn = false; // social security number
          if(sentNames[i].length() == 10){
            try{
              Integer.parseInt(sentNames[i].substring(0,5));
              Integer.parseInt(sentNames[i].substring(6,9));
              ssn = true;
            }catch(NumberFormatException e){
              ssn = false;
            }
          }
/*
          if(sentNames[i].length() == 11){
            try{
              Integer.parseInt(sentNames[i].substring(0,5));
              Integer.parseInt(sentNames[i].substring(7,10));
              String tempString;
              tempString = sentNames[i].substring(0,5);
              tempString += sentNames[i].substring(7,10);
              sentNames[i] = tempString;
              ssn = true;
            }catch(NumberFormatException e){
              ssn = false;
            }
          }
*/
          if(ssn){
            //List lMember = EntityFinder.findAllByColumn((com.idega.projects.golf.entity.Member)Member.getStaticInstance(),Member.getSocialSecurityNumberColumnName(),sentNames[i]);
            Member tempMemb = (com.idega.projects.golf.entity.Member)Member.getMember(sentNames[i]);
            if(tempMemb != null){
//              Member tempMemb = (Member)lMember.get(0);
              business.setStartingtime(Integer.parseInt(sentGroupNums[i]), this.currentDay, this.currentField, Integer.toString(tempMemb.getID()), tempMemb.getName(), Float.toString(tempMemb.getHandicap()), GolfCacher.getCachedUnion(tempMemb.getMainUnionID()).getAbbrevation(), null, null);
            }else{
              business.setStartingtime(Integer.parseInt(sentGroupNums[i]), this.currentDay, this.currentField, null, sentNames[i], sentHandycaps[i], GolfCacher.getCachedUnion(sentUnions[i]).getAbbrevation(), null, null);
            }
          }else{
            business.setStartingtime(Integer.parseInt(sentGroupNums[i]), this.currentDay, this.currentField, null, sentNames[i], sentHandycaps[i], GolfCacher.getCachedUnion(sentUnions[i]).getAbbrevation(), null, null);
          }
        }
      }
    }

  }


  public void noPermission(){
    Text satyOut = new Text("Þú hefur ekki réttindi fyrir þessa síðu");
    satyOut.setFontSize(4);
    Table AlignmentTable = new Table();
    AlignmentTable.setBorder(0);
    AlignmentTable.add(Text.getBreak());
    AlignmentTable.add(satyOut);
    AlignmentTable.setAlignment("center");
    AlignmentTable.add(Text.getBreak());
    AlignmentTable.add(Text.getBreak());
//    Link close = new Link("Loka glugga");
//    close.addParameter(closeParameterString, "true");
//    AlignmentTable.add(close);
    frameTable.empty();
    frameTable.add(AlignmentTable);
  }




  public void main(ModuleInfo modinfo) throws Exception {
    String date = modinfo.getParameter("date");
    currentField = modinfo.getParameter("field_id");
    currentUnion = modinfo.getParameter("union");

    if(date == null){
      Object tempObj = modinfo.getSessionAttribute("date");
      if(tempObj != null){
        date = tempObj.toString();
        myForm.add(new HiddenInput("date",date));
      }
    }else{
      myForm.maintainParameter("date");
    }

    if(currentField == null){
      Object tempObj = modinfo.getSessionAttribute("field_id");
      if(tempObj != null){
        currentField = tempObj.toString();
        myForm.add(new HiddenInput("field_id",currentField));
      }
    }else{
      myForm.maintainParameter("field_id");
    }

    if(currentUnion == null){
      Object tempObj = modinfo.getSessionAttribute("union_id");
      if(tempObj != null){
        currentUnion = tempObj.toString();
        myForm.add(new HiddenInput("union_id",currentUnion));
      }
    }else{
      myForm.maintainParameter("union_id");
    }



    boolean keepOn = true;

    try{
      currentDay = new idegaTimestamp(date);
    }catch(NullPointerException e){
      keepOn = false;
      this.noPermission();
    }

    if(modinfo.getParameter(saveParameterString+".x") != null || modinfo.getParameter(saveParameterString) != null){
      this.handleFormInfo(modinfo);
    }

    if(keepOn){

      TournamentDay tempTD = new TournamentDay();
      List Tournaments = EntityFinder.findAll(new Tournament(),"select tournament.* from tournament,tournament_day where tournament_day.tournament_id=tournament.tournament_id and tournament_day.day_date = '"+currentDay.toSQLDateString()+"' and tournament.field_id = " + currentField );
      if(Tournaments != null ){
  //      if("true".equals(modinfo.getParameter(closeParameterString))){
          //this.close();
          //this.print(modinfo);
  //      }else{
          fieldInfo = business.getFieldConfig( Integer.parseInt(currentField) , currentDay );
          lineUpTournamentDay(modinfo, Tournaments );
  //      }
      }else{
        String hasPermission = modinfo.getParameter("golf");
        if( hasPermission != null || AccessControl.isAdmin(modinfo) || (AccessControl.isClubAdmin(modinfo) && modinfo.getSessionAttribute("member_main_union_id").equals(modinfo.getSessionAttribute("union_id"))) || (AccessControl.isClubWorker(modinfo) && modinfo.getSessionAttribute("member_main_union_id").equals(modinfo.getSessionAttribute("union_id"))) ){
          if(hasPermission == null){
            myForm.add(new HiddenInput("golf","79")); // some dummy value
          }else{
            myForm.maintainParameter("golf");
          }
          fieldInfo = business.getFieldConfig( Integer.parseInt(currentField) , currentDay );
          lineUpTable(modinfo);
        }else{
          noPermission();
        }
      }
    }
  } // method main() ends


} // Class ends
