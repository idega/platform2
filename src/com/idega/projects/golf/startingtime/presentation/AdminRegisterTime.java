package com.idega.projects.golf.startingtime.presentation;

import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.projects.golf.startingtime.business.TeeTimeBusiness;
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
import com.idega.data.GenericEntity;
import com.idega.util.idegaTimestamp;
import com.idega.projects.golf.entity.TournamentRound;
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
import com.idega.projects.golf.startingtime.data.TeeTime;
import com.idega.util.text.TextSoap;
import com.idega.jmodule.login.business.AccessControl;

import java.sql.SQLException;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.text.DecimalFormat;

/**
 * Title:        Golf
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class AdminRegisterTime extends com.idega.projects.golf.templates.page.JmoduleWindowModuleWindow {

  private TeeTimeBusiness business;
  private Form myForm;
  private Table frameTable;
  private idegaTimestamp currentDay;
  private String currentField;
  private String currentUnion;
  private String MemberID;
  private int daytime;
  private StartingtimeFieldConfig fieldInfo;
  private DecimalFormat hadycapFormat;

  private static String saveParameterString = "STsave";
  private static String timeParameterString  = "STtime";
  private static String nameParameterString  = "STname";
  private static String unionParameterString  = "STunion";
  private static String handycapParameterString  = "SThadycap";
  private static String deleteParameterString  = "STdelete";
  private static String groupNumParameterString  = "STgroup";
  private static String lastGroupParameterString = "STlastGroup";
  private static String timeChangeStartIDParameterString = "STstartID";
  private static String formParmeterIDParameterString = "STfpID";



  private static String color1 = "#336661";
  private static String color2 = "#CEDFCF";
  private static String color3 = "#ADC9D0";
  private static String color5 = "#FFFFFF";
  private static String color4 = "#6E9173";
  private static String color6 = "#FF6666";

  public AdminRegisterTime() {
    super();
    this.setResizable(true);
    myForm = new Form();
    frameTable = new Table();
    frameTable.setAlignment("center");
    frameTable.setWidth("100%");
    myForm.add(frameTable);
    super.add(Text.getBreak());
    super.add(myForm);
    business = new TeeTimeBusiness();
    hadycapFormat = new DecimalFormat("###.0");
  }


  public List getTournamentRoundList() throws SQLException {

    int interval = fieldInfo.getMinutesBetweenStart();
    Vector tournamentGroups = new Vector(0);
    int tournamentGroupsIndex = 0;
    List TournamentRounds = EntityFinder.findAll(new TournamentRound(),"select tournament_round.* from tournament,tournament_round where tournament_round.tournament_id=tournament.tournament_id and tournament_round.round_date >= '"+currentDay.toSQLDateString()+" 00:00' and tournament_round.round_date <= '"+currentDay.toSQLDateString()+" 23:59' and tournament.field_id = " + this.currentField );

    if(TournamentRounds != null){
      for (int i = 0; i < TournamentRounds.size(); i++) {
        TournamentRound tempRound = (TournamentRound)TournamentRounds.get(i);

        idegaTimestamp begin = new idegaTimestamp(tempRound.getRoundDate());
        begin.setAsTime();
        idegaTimestamp End_ = new idegaTimestamp(tempRound.getRoundEndDate());
        End_.setAsTime();
        idegaTimestamp begintime = new idegaTimestamp(fieldInfo.getOpenTime());
        begintime.setAsTime();

        int firstGroup = idegaTimestamp.getMinutesBetween(begintime, begin)/interval;
        int groupCount = idegaTimestamp.getMinutesBetween(begin,End_)/interval;
        int[] tempBeginGroupAndEnd = new int[2];

        tempBeginGroupAndEnd[0] = firstGroup+1;
        tempBeginGroupAndEnd[1] = firstGroup+groupCount;

        String tournamentName = tempRound.getTournament().getName();

        tournamentGroups.add(tournamentGroupsIndex++,tournamentName);
        tournamentGroups.add(tournamentGroupsIndex++,tempBeginGroupAndEnd);

      }
    }
    return tournamentGroups;
  }


   public String getTournamentName(List rounds, int groupNumber){

        for(int c = 0 ; c < rounds.size(); c+=2){
          int[] temp = (int[])rounds.get(c+1);
          if(groupNumber >= temp[0] && groupNumber <= temp[1]){
            return (String)rounds.get(c);
          }
        }
        return null;
  }

  public void lineUpTable(ModuleInfo modinfo) throws SQLException {

    Vector illegalTimes = new Vector(0);
    int illegalTimesIndex = 0;

    idegaTimestamp openTime = new idegaTimestamp(fieldInfo.getOpenTime());
    int minBetween = fieldInfo.getMinutesBetweenStart();

    idegaTimestamp noon = new idegaTimestamp(1,2,1,13,0,0);
    noon.setAsTime();
    idegaTimestamp afternoon = new idegaTimestamp(1,2,1,17,0,0);
    afternoon.setAsTime();

    int groupCount = 0;
    int firstGroup = 1;
    List takenTimes = null;
    idegaTimestamp firstTime = null;
    switch (daytime) {
      case 1: //afternoon
        groupCount = idegaTimestamp.getMinutesBetween(noon,afternoon)/minBetween;
        firstGroup = idegaTimestamp.getMinutesBetween(openTime ,noon)/minBetween+1;
        takenTimes = business.getStartingtimeTableEntries(this.currentDay,this.currentField,firstGroup,firstGroup+groupCount-1);
        firstTime = noon;
        break;
      case 2: //evening
        groupCount = idegaTimestamp.getMinutesBetween(afternoon,new idegaTimestamp(fieldInfo.getCloseTime()))/minBetween;
        firstGroup = idegaTimestamp.getMinutesBetween(openTime ,afternoon)/minBetween+1;
        takenTimes = business.getStartingtimeTableEntries(this.currentDay,this.currentField,firstGroup,firstGroup+groupCount-1);
        firstTime = afternoon;
        break;
      default: // morning
        groupCount = idegaTimestamp.getMinutesBetween(openTime,noon)/minBetween;
        firstGroup = 1;
        takenTimes = business.getStartingtimeTableEntries(this.currentDay,this.currentField,firstGroup,groupCount);
        firstTime = new idegaTimestamp(fieldInfo.getOpenTime());
        break;
    }


//    int groupCount = idegaTimestamp.getMinutesBetween(openTime,new idegaTimestamp(fieldInfo.getCloseTime()))/minBetween;

    List tournamentGroups = getTournamentRoundList();

    int countInGroups = 4;
    int lines = groupCount*countInGroups;

    frameTable.empty();

    String width = "520";
    String width1 = "70";
    String width2 = "250";
    String width3 = "70";
    String width4 = "70";
    String width5 = "60";


    Table startTable = new Table();
    startTable.setRows(lines+1);
    startTable.setColumns(5);
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
    time.setText(iwrb.getLocalizedString("start.time","Time"));
    time.setBold();
    headerTable.add(time,1,1);

    Text name = (Text)textProxy.clone();
    name.setText(iwrb.getLocalizedString("start.social_nr","Social nr.") +" ("+ iwrb.getLocalizedString("start.name","Name")+")");
    name.setBold();
    headerTable.add(name,2,1);

    Text club = (Text)textProxy.clone();
    club.setText("("+iwrb.getLocalizedString("start.club","Club")+")");
    club.setBold();
    headerTable.add(club,3,1);

    Text handicap = (Text)textProxy.clone();
    handicap.setText("("+iwrb.getLocalizedString("start.handicap","Handicap")+")");
    handicap.setBold();
    headerTable.add(handicap,4,1);

    Text delete = (Text)textProxy.clone();
    delete.setText(iwrb.getLocalizedString("start.delete","Delete"));
    delete.setBold();
    headerTable.add(delete,5,1);

    CheckBox delCheck = new CheckBox(deleteParameterString);

    int groupCounter = 1;
    int lastGroup = -1;
    boolean insert = true;

    if(takenTimes != null){
      for (int i = 0; i < takenTimes.size(); i++) {
        TeeTime tempStart = (TeeTime)takenTimes.get(i);
        int tempGroupNum = tempStart.getGroupNum();

        String tName = getTournamentName(tournamentGroups,tempGroupNum);

        if(tempGroupNum < 1 || tName != null){
          insert = false;
        }else{
          if(lastGroup == tempGroupNum){
            groupCounter++;
            if(groupCounter > countInGroups){
              insert = false;
            }
          }else{
            groupCounter = 1;
          }
        }
        if(insert){
          int line = (tempGroupNum-firstGroup)*countInGroups + groupCounter+1; //(-1+1) -groupCounter++ +headerLine
//          openTime.addMinutes((tempGroupNum-1)*minBetween);
//          startTable.add(TextSoap.addZero(openTime.getHour()) + ":" + TextSoap.addZero(openTime.getMinute()),1,line);
//          openTime.addMinutes(-(tempGroupNum-1)*minBetween);
          startTable.add(tempStart.getPlayerName(),2,line);
          startTable.add(tempStart.getClubName(),3,line);
          if(tempStart.getHandicap()>= 0){
            startTable.add(hadycapFormat.format((double)tempStart.getHandicap()),4,line);
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











    groupCounter = 1;
    lastGroup = -1;
    List allTakenTimes = business.getStartingtimeTableEntries(this.currentDay,this.currentField);
    int allGroupCount = idegaTimestamp.getMinutesBetween(new idegaTimestamp(fieldInfo.getOpenTime()),new idegaTimestamp(fieldInfo.getCloseTime()))/minBetween;
    int[] freeGroups = new int[allGroupCount];


    if(allTakenTimes != null){
      for (int i = 0; i < allTakenTimes.size(); i++) {
        TeeTime tempStart = (TeeTime)allTakenTimes.get(i);
        int tempGroupNum = tempStart.getGroupNum();
        String tName = getTournamentName(tournamentGroups,tempGroupNum);
        if(tempGroupNum < 1 || tName != null){
          illegalTimes.insertElementAt(tempStart,illegalTimesIndex++);
        }else{
          if(lastGroup == tempGroupNum){
            groupCounter++;
            if(groupCounter == countInGroups){
              freeGroups[tempGroupNum-1] = 1;
            }
            if(groupCounter > countInGroups){
              illegalTimes.insertElementAt(tempStart,illegalTimesIndex++);
            }
          }else{
            groupCounter = 1;
          }
        }
        lastGroup = tempGroupNum;
      }
    }

    // takes TournamentTimes out of timeDropdownMenu
    for(int c = 0 ; c < tournamentGroups.size(); c+=2){
      int[] temp = (int[])tournamentGroups.get(c+1);
      for (int g = temp[0]-1; g < temp[1]; g++) {
        if( g > -1 && g < freeGroups.length )
          freeGroups[g] = 1;
      }
    }


    DropdownMenu timeMenu = new DropdownMenu(this.timeParameterString);
    for (int i = 0; i < allGroupCount; i++) {
      if(freeGroups[i] != 1){
        timeMenu.addMenuElement(i+1,TextSoap.addZero(openTime.getHour()) + ":" + TextSoap.addZero(openTime.getMinute()));
      }
      openTime.addMinutes(minBetween);
    }
    openTime.addMinutes(-allGroupCount*minBetween);
    timeMenu.addMenuElement("-","-");




    int loop = lines+2;
  //    int takenTimesIndex = 0;
    TextInput nameInput = new TextInput(nameParameterString);
    nameInput.setSize(30);
    FloatInput handycapInput = new FloatInput(handycapParameterString);
    handycapInput.setSize(3);

    TextInput unionMenu = new TextInput(unionParameterString,GolfCacher.getCachedUnion(currentUnion).getAbbrevation());
    unionMenu.setLength(3);
    boolean firstColor = true;
    int count = 0;
    int min = 0;
    Text templ = new Text("");
    for (int i = 1; i < loop; i++) {

      String tName = getTournamentName(tournamentGroups, (int)(((firstGroup-1)*countInGroups+i-2)/countInGroups+1) );

      if(startTable.isEmpty(5,i)){
        if(tName == null){
          min = ((i-2)/countInGroups)*minBetween;
          firstTime.addMinutes(min);
          startTable.add("<b>"+TextSoap.addZero(firstTime.getHour()) + ":" + TextSoap.addZero(firstTime.getMinute())+"</b>",1,i);
          firstTime.addMinutes(-min);
          startTable.add(nameInput,2,i);
          startTable.add(unionMenu,3,i);
          startTable.add(handycapInput,4,i);
          startTable.add(new HiddenInput(groupNumParameterString, Integer.toString((int)(((firstGroup-1)*countInGroups+i-2)/countInGroups+1))));
        }else{
          min = ((i-2)/countInGroups)*minBetween;
          firstTime.addMinutes(min);
          startTable.add("<b>"+TextSoap.addZero(firstTime.getHour()) + ":" + TextSoap.addZero(firstTime.getMinute())+"</b>",1,i);
          firstTime.addMinutes(-min);
          Text temp = (Text)templ.clone();
          temp.setText(tName);
          startTable.add(temp,2,i);
        }
      }else if(i>1 && tName == null){
        DropdownMenu tempTimeMenu = (DropdownMenu)timeMenu.clone();
        min = ((i-2)/countInGroups)*minBetween;
        firstTime.addMinutes(min);
        tempTimeMenu.addMenuElement(Integer.toString((int)(((firstGroup-1)*countInGroups+i-2)/countInGroups+1)),TextSoap.addZero(firstTime.getHour()) + ":" + TextSoap.addZero(firstTime.getMinute()));
        firstTime.addMinutes(-min);
        tempTimeMenu.setSelectedElement(Integer.toString((int)(((firstGroup-1)*countInGroups+i-2)/countInGroups+1)));
        startTable.add(tempTimeMenu,1,i);
        startTable.add(new HiddenInput(lastGroupParameterString,Integer.toString((int)(((firstGroup-1)*countInGroups+i-2)/countInGroups+1))),1,i);
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


        TeeTime tempStart = (TeeTime)illegalTimes.get(i-1);

        illegalTable.setColor(1,i,color6);
        illegalTable.setColor(2,i,color6);
        illegalTable.setColor(3,i,color6);
        illegalTable.setColor(4,i,color6);
        illegalTable.setColor(5,i,color6);


        illegalTable.add(tempStart.getPlayerName(),2,i);
        illegalTable.add(tempStart.getClubName(),3,i);
        if(tempStart.getHandicap()>= 0){
          illegalTable.add(hadycapFormat.format((double)tempStart.getHandicap()),4,i);
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

    String sDayTime;

    switch (daytime) {
      case 1:
        sDayTime = " - " + iwrb.getLocalizedString("start.afternoon","afternoon") + " - ";
        break;
      case 2:
        sDayTime =" - " + iwrb.getLocalizedString("start.evening","evening") + " - ";
        break;
      case 0:
        sDayTime =" - " + iwrb.getLocalizedString("start.morning","morning") + " - ";
        break;
      default:
        sDayTime =" - ";
        break;
    }


    Text dateText = new Text(business.getFieldName(Integer.parseInt(this.currentField))+ sDayTime +this.currentDay.getISLDate());
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
//    SubmitButton save = new SubmitButton(new Image("/pics/formtakks/vista.gif","Vista"), saveParameterString, "do");
    SubmitButton save = new SubmitButton(this.iwrb.getImage("buttons/save.gif","Vista"), saveParameterString, "do");
    Table submSave = new Table();
    submSave.add(save);
    submSave.setAlignment("center");
    submSave.setAlignment(1,1,"right");
    submSave.setHeight(1,"30");
    submSave.setWidth(width);
    frameTable.add(submSave);
  }


  public void handleFormInfo(ModuleInfo modinfo) throws SQLException {

    Object rfObj = modinfo.getSessionAttribute(formParmeterIDParameterString);
    String rfParam = modinfo.getParameter(formParmeterIDParameterString);


    if(!((String)rfObj).equals(rfParam)){

      modinfo.setSessionAttribute(formParmeterIDParameterString,rfParam);

      String[] sentTimes = modinfo.getParameterValues(timeParameterString);
      String[] sentLastGroups = modinfo.getParameterValues(lastGroupParameterString);
      String[] sentStartIDs = modinfo.getParameterValues(timeChangeStartIDParameterString);

      String[] sentDeletes = modinfo.getParameterValues(deleteParameterString);

      String[] sentNames = modinfo.getParameterValues(nameParameterString);
      String[] sentGroupNums = modinfo.getParameterValues(groupNumParameterString);
      String[] sentUnions = modinfo.getParameterValues(unionParameterString);
      String[] sentHandycaps = modinfo.getParameterValues(handycapParameterString);



      if(sentTimes != null){
        for (int i = 0; i < sentTimes.length; i++) {
          if(!sentTimes[i].equals(sentLastGroups[i]) || sentTimes[i].equals("-") ){
            try{
              TeeTime tempSt = new TeeTime(Integer.parseInt(sentStartIDs[i]));
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
            new TeeTime(Integer.parseInt(sentDeletes[i])).delete();
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
                business.setStartingtime(Integer.parseInt(sentGroupNums[i]), this.currentDay, this.currentField, Integer.toString(tempMemb.getID()), MemberID, tempMemb.getName(), Float.toString(tempMemb.getHandicap()), GolfCacher.getCachedUnion(tempMemb.getMainUnionID()).getAbbrevation(), null, null);
              }else{
                //business.setStartingtime(Integer.parseInt(sentGroupNums[i]), this.currentDay, this.currentField, null, MemberID, sentNames[i], sentHandycaps[i], GolfCacher.getCachedUnion(sentUnions[i]).getAbbrevation(), null, null);
                business.setStartingtime(Integer.parseInt(sentGroupNums[i]), this.currentDay, this.currentField, null, MemberID, sentNames[i], sentHandycaps[i], sentUnions[i], null, null);
              }
            }else{
              //business.setStartingtime(Integer.parseInt(sentGroupNums[i]), this.currentDay, this.currentField, null, MemberID, sentNames[i], sentHandycaps[i], GolfCacher.getCachedUnion(sentUnions[i]).getAbbrevation(), null, null);
              business.setStartingtime(Integer.parseInt(sentGroupNums[i]), this.currentDay, this.currentField, null, MemberID, sentNames[i], sentHandycaps[i], sentUnions[i], null, null);
            }
          }
        }
      }
    }

    //this.setParentToReload();

  }


  public void noPermission(){
    Text satyOut = new Text(this.iwrb.getLocalizedString("start.no_permission","No permission"));
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
    super.main(modinfo);
    String date = modinfo.getParameter("date");
    currentField = modinfo.getParameter("field_id");
    currentUnion = modinfo.getParameter("union_id");
    MemberID= modinfo.getParameter("member_id");
    String sDayTime = modinfo.getParameter("daytime");

    String rfParam = modinfo.getParameter(formParmeterIDParameterString);

    if(modinfo.getSessionAttribute(formParmeterIDParameterString) == null){
      modinfo.setSessionAttribute(formParmeterIDParameterString,Integer.toString(myForm.hashCode()-1));
      myForm.add(new HiddenInput(formParmeterIDParameterString,Integer.toString(myForm.hashCode())));
    }else if ( rfParam != null){
      myForm.add(new HiddenInput(formParmeterIDParameterString, Integer.toString((Integer.parseInt(rfParam)+rfParam.hashCode())%Integer.MAX_VALUE)));
    }else{
      myForm.add(new HiddenInput(formParmeterIDParameterString,Integer.toString(myForm.hashCode())));
    }

    if(sDayTime == null){
      Object tempObj = modinfo.getSessionAttribute("when");
      if(tempObj != null){
        daytime = Integer.parseInt(tempObj.toString());
        myForm.add(new HiddenInput("daytime",tempObj.toString()));
      }
    }else{
      daytime = Integer.parseInt(sDayTime.toString());
      myForm.maintainParameter("daytime");
    }

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

    if(MemberID == null){
      Object tempObj = com.idega.projects.golf.login.business.LoginBusiness.getMember(modinfo);
      if(tempObj != null){
        MemberID = Integer.toString(((GenericEntity)tempObj).getID());
        myForm.add(new HiddenInput("member_id",MemberID));
      }
    }else{
      myForm.maintainParameter("member_id");
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
        String hasPermission = modinfo.getParameter("golf");
        if( hasPermission != null || AccessControl.isAdmin(modinfo) || (AccessControl.isClubAdmin(modinfo) && modinfo.getSessionAttribute("member_main_union_id").equals(modinfo.getSessionAttribute("union_id"))) || (AccessControl.isClubWorker(modinfo) && modinfo.getSessionAttribute("member_main_union_id").equals(modinfo.getSessionAttribute("union_id"))) ){
          if(hasPermission == null){
            myForm.add(new HiddenInput("golf","79")); // some dummy value
          }else{
            myForm.maintainParameter("golf");
          }
          fieldInfo = business.getFieldConfig( Integer.parseInt(currentField) , currentDay );

          String sDayTimeString;

          switch (daytime) {
            case 1:
              sDayTimeString = " - " + iwrb.getLocalizedString("start.afternoon","afternoon") + " - ";
              break;
            case 2:
              sDayTimeString =" - " + iwrb.getLocalizedString("start.evening","evening") + " - ";
              break;
            case 0:
              sDayTimeString =" - " + iwrb.getLocalizedString("start.morning","morning") + " - ";
              break;
            default:
              sDayTimeString =" - ";
              break;
          }


          this.setTitle( business.getFieldName(Integer.parseInt(this.currentField)) + sDayTimeString + this.currentDay.getISLDate());
          lineUpTable(modinfo);
        }else{
          noPermission();
        }

    }
  } // method main() ends


} // Class ends
