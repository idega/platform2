package com.idega.projects.golf.startingtime.presentation;

import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.projects.golf.service.StartService;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.interfaceobject.TextInput;
import com.idega.jmodule.object.interfaceobject.Form;
import com.idega.jmodule.object.interfaceobject.DropdownMenu;
import com.idega.jmodule.object.interfaceobject.SubmitButton;
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

public class RegisterTime extends ModuleObjectContainer {

  private StartService business;
  private DropdownMenu unionDropdown;
  private Form myForm = null;
  private Table frameTable;
  private int countInGroups = 4;

  private idegaTimestamp currentDay;
  private String currentField;
  private String currentUnion;
  private StartingtimeFieldConfig fieldInfo;


  public RegisterTime() {
    super();
    myForm = new Form();
    frameTable = new Table();
    frameTable.setAlignment("center");
    frameTable.setWidth("100%");
    myForm.add(frameTable);
    this.add(myForm);
    business = new StartService();
    unionDropdown = (DropdownMenu)GolfCacher.getUnionAbbreviationDropdown("club").clone();
  }



    public TextInput insertEditBox(String name, Form myForm)
    {
            TextInput myInput = new TextInput(name);
            myInput.setParentObject(myForm);
            myInput.setMaxlength(10);
            return myInput;
    }

    public TextInput insertEditBox(String name, String text)
    {
            TextInput myInput = new TextInput(name);
            myInput.setAsNotEmpty();
            myInput.setContent(text);
            return myInput;
    }

    public TextInput insertEditBox(String name, String text, int size)
    {
            TextInput myInput = new TextInput(name);
            myInput.setSize(size);
            myInput.setAsNotEmpty();
            myInput.setContent(text);
            return myInput;
    }


    public DropdownMenu insertUnionDropdown(String name, String text, int size) throws SQLException{
      DropdownMenu mydropdown = (DropdownMenu)unionDropdown.clone();

      mydropdown.setSelectedElement(text);
      mydropdown.keepStatusOnAction();
      return mydropdown;
    }


    public TextInput insertEditBox(String name, int size)
    {
            TextInput myInput = new TextInput(name);
            myInput.setSize(size);
            return myInput;
    }

    private SubmitButton insertButton(String btnName, String Action, String Method, Form theForm)
    {
            SubmitButton mySubmit = new SubmitButton(btnName);
            theForm.setMethod(Method);
            theForm.setAction(Action);
            return mySubmit;
    }

    private SubmitButton insertButton(Image image, String imageName, String Action, String Method, Form theForm)
    {
            SubmitButton mySubmit = new SubmitButton(image, imageName);
            theForm.setMethod(Method);
            theForm.setAction(Action);
            return mySubmit;
    }

    private SubmitButton insertButton(String btnName, String Action, String Method, String onSub, Form theForm)
    {
            SubmitButton mySubmit = new SubmitButton(btnName);
            mySubmit.setOnSubmit(onSub);
            theForm.setMethod(Method);
            theForm.setAction(Action);
            return mySubmit;
    }

    public void lineUpTable(int skraMarga, ModuleInfo modinfo)throws IOException
    {

            String btnSkraUrl = "/pics/formtakks/boka.gif";
            String btnCancelUrl = "/pics/formtakks/cancel.gif";

            int memberId = -1;
            boolean memberAvailable = false;
            //fá member id fyrir member til að finna hann og setja inn í textinputið fyrir hann
            if(modinfo.getSession().getAttribute("member_id") != null){
              memberId = Integer.parseInt((String)modinfo.getSession().getAttribute("member_id"));
              memberAvailable = true;
            }

            String lines[] = new String[skraMarga];
            int Lines[] = new int[skraMarga];

            try
            {
              Member member = null;
              if(memberId != -1)
                member = new Member(memberId);
              String FieldID = currentField;
              String Date = modinfo.getSession().getAttribute("date").toString();
              String MemberId = modinfo.getSession().getAttribute("member_id").toString();
              GolfField myGolfField = getFieldInfo( Integer.parseInt(FieldID), Date);
              int Line = Integer.parseInt( modinfo.getParameter("line"));
              int max = checkLine(Line, FieldID, Date, modinfo);

              for(int j = 0; j < skraMarga ; j++){
                if(max > 3){
                  while(max > 3){
                    Line++;
                    max = checkLine(Line, FieldID, Date, modinfo);
                  }
                }
                max++;
                lines[j] = getTime(Line, myGolfField);
              }

              Table myTable =  new Table(6, skraMarga+3);
              myTable.setCellpadding(0);
              myTable.setCellspacing(0);
              myTable.setWidth(2, "40");
              myTable.setHeight(1,"30");


              myTable.addText("<b>Tími</b>", 2, 1);
              myTable.addText("<b>Kennitala</b>", 3, 1);
              myTable.addText("<b>Sérkort</b>", 5, 1);
              myTable.addText("<b>Kortanúmer</b>", 6, 1);

              myTable.setColumnAlignment(1,"center");
              myTable.setColumnAlignment(5,"center");
              myTable.setColumnAlignment(6,"center");



              boolean admin = false;
              boolean clubadmin = false;
              boolean clubworker = false;
              String unionAbbrevation = null;

              if(memberAvailable){
                admin = com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo);
                clubadmin = com.idega.jmodule.login.business.AccessControl.isClubAdmin(modinfo);
                clubworker = com.idega.jmodule.login.business.AccessControl.isClubWorker(modinfo);
                unionAbbrevation = member.getMainUnion().getAbbrevation();
              }

              int i = 1;
              for ( ;i < skraMarga+1 ; i++)
              {
                  myTable.setWidth(1, "25");
                  myTable.addText("<b>"+lines[i-1]+"</b>", 2, i+1);
                  myTable.setAlignment(2, i+1, "left");

                  if(i == 1 && memberAvailable){
                    myTable.add(insertEditBox("secure_num", member.getSocialSecurityNumber()), 3, i+1);
                  }else{
                    myTable.add(insertEditBox("secure_num", myForm), 3, i+1);
                  }

                  myTable.add(insertEditBox("card", 4), 5, i+1);
                  myTable.add(insertEditBox("cardNo", 12), 6, i+1);
              }

              myTable.setColumnAlignment(5,"center");
              myTable.setColumnAlignment(6,"center");

              //setPlayers(modinfo);

              myTable.mergeCells(4, i+2, 6, i+2);
              myTable.add(insertButton(new Image(btnSkraUrl),"", "innskraning1.jsp", "post", myForm), 4, i+2);
              myTable.add(new CloseButton(new Image(btnCancelUrl)), 4, i+2);
              myTable.setAlignment(4, i+2, "right");
              frameTable.empty();
              frameTable.add(myTable);

            }
            catch (SQLException E) {
                    E.printStackTrace();
            }
            catch (IOException E) {
                    E.printStackTrace();
            }
    }

    public List handleFormInfo(ModuleInfo modinfo)throws SQLException, IOException {
      int i = 0;

      String FieldID = modinfo.getSession().getAttribute("field_id").toString();
      String Date = modinfo.getSession().getAttribute("date").toString();
      String MemberId = modinfo.getSession().getAttribute("member_id").toString();

      int Line = Integer.parseInt( modinfo.getParameter("line"));
      int max = checkLine(Line, FieldID, Date, modinfo);
      int fjoldi = 4 - max;
/*
      try
      {*/
        if( modinfo.getParameter("secure_num") != null){
          String sentSecureNums[] =  modinfo.getParameterValues("secure_num");
          String playerCard[] =  modinfo.getParameterValues("card");
          String playerCardNo[] =  modinfo.getParameterValues("cardNo");
          int numPlayers = sentSecureNums.length;


          if(sentSecureNums != null){
            for (int j = 0; i < sentSecureNums.length; j++) {
              try{
                if(sentSecureNums[j] != null && !"".equals(sentSecureNums[j]) ){
                  boolean ssn = false; // social security number
                  if(sentSecureNums[j].length() == 10){
                    try{
                      Integer.parseInt(sentSecureNums[j].substring(0,5));
                      Integer.parseInt(sentSecureNums[j].substring(6,9));
                      ssn = true;
                    }catch(NumberFormatException e){
                      ssn = false;
                    }
                  }

        /*
                  if(sentSecureNums[j].length() == 11){
                    try{
                      Integer.parseInt(sentSecureNums[j].substring(0,5));
                      Integer.parseInt(sentSecureNums[j].substring(7,10));
                      String tempString;
                      tempString = sentSecureNums[j].substring(0,5);
                      tempString += sentSecureNums[j].substring(7,10);
                      sentSecureNums[j] = tempString;
                      ssn = true;
                    }catch(NumberFormatException e){
                      ssn = false;
                    }
                  }
        */

                  if(ssn){
                    Member tempMemb = (com.idega.projects.golf.entity.Member)Member.getMember(sentSecureNums[i]);
                    if(tempMemb != null){
                      business.setStartingtime(Line, this.currentDay, this.currentField, Integer.toString(tempMemb.getID()), tempMemb.getName(), Float.toString(tempMemb.getHandicap()), GolfCacher.getCachedUnion(tempMemb.getMainUnionID()).getAbbrevation(), null, null);
                    }else{
                      //
                      //business.setStartingtime(Line, this.currentDay, this.currentField, null, sentSecureNums[i], sentHandycaps[i], GolfCacher.getCachedUnion(sentUnions[i]).getAbbrevation(), null, null);
                    }
                  }else{
                    //
                    //business.setStartingtime(Integer.parseInt(sentGroupNums[i]), this.currentDay, this.currentField, null, sentSecureNums[i], sentHandycaps[i], GolfCacher.getCachedUnion(sentUnions[i]).getAbbrevation(), null, null);
                  }
                }
              }catch(SQLException e){

              }
            }
          }

/*
          for(; i < numPlayers; i++){
            if(max > 3){
              while(max > 3){
                Line++;
                max = checkLine(Line, FieldID, Date, modinfo);
              }
          }
            business.setStartingtime( Line, Date, FieldID, MemberId, sentSecureNums[i], playerHandyCap[i], playerClub[i], playerCard[i], playerCardNo[i] );
            max++;
          }*/
        }
/*      }
      catch (SQLException E) {
          return new Vector();
      }*/
      return null;
    }

    public int checkLine(int LineNo, String fieldID, String date, ModuleInfo modinfo)throws SQLException, IOException
    {
            int totalLines = -1;
                    totalLines = business.entriesInGroup( LineNo, fieldID, date );
            return totalLines;
    }


    public void setErroResponse(Form myForm, boolean inputErr)
    {
            String btnCloseUrl = "/pics/rastimask/Takkar/TLoka1.gif";
            String btnBackUrl = "/pics/rastimask/Takkar/Ttilbaka1.gif";

            Table myTable = new Table(2, 3);
            if(inputErr){
                    myTable.addText("Nauðsynlegt er að skrá eins marga og teknir voru frá", 2, 1);
            }
            else
                    myTable.addText("Þetta holl er því miður fullt. Gjörðu svo vel að velja þér nýjan tíma", 2, 1);
            if(inputErr){
                    myTable.add(new BackButton(new Image(btnBackUrl)), 2, 3);
            }
            else
                    myTable.add(new CloseButton(new Image(btnCloseUrl)), 2, 3);
            myTable.setAlignment(2, 3, "center");
            myTable.setCellpadding(0);
            myTable.setCellspacing(0);
            frameTable.empty();
            frameTable.add(myTable);

    }

    public GolfField getFieldInfo( int field, String date) throws SQLException,IOException{
            StartingtimeFieldConfig FieldConfig = business.getFieldConfig( field , date );
            GolfField field_info = new GolfField ( new idegaTimestamp(FieldConfig.getOpenTime()).toSQLTimeString(), new idegaTimestamp(FieldConfig.getCloseTime()).toSQLTimeString(), FieldConfig.getMinutesBetweenStart(), field, date, FieldConfig.getDaysShown() );
            return field_info;
    }

     public String getTime( int end, GolfField myGolfField)
    {

        int interval = myGolfField.get_interval();
        int openMin = myGolfField.get_open_min();
            int openHour = myGolfField.get_open_hour();

            String Time = "";

            for(int i = 1; i <= end; i ++){

                    if (openMin >= 60){
                            openMin -= 60;
                            openHour++;
                    }

                    if (openMin < 10)
                            Time = openHour + ":0" + openMin;
                    else
                            Time = openHour + ":" + openMin;

                    openMin += interval;

            }
            return Time;
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



public void main(ModuleInfo modinfo) throws Exception {

    String date = modinfo.getSession().getAttribute("date").toString();
    //String field_id = modinfo.getSession().getAttribute("field_id").toString();
    currentField = modinfo.getSession().getAttribute("field_id").toString();
    currentUnion = modinfo.getSession().getAttribute("union_id").toString();



    boolean keepOn = true;

    try{
      currentDay = new idegaTimestamp(date);
    }catch(NullPointerException e){
      keepOn = false;
      this.noPermission();
    }



//    if(modinfo.getParameter(saveParameterString+".x") != null || modinfo.getParameter(saveParameterString) != null){
//      this.handleFormInfo(modinfo);
//    }

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
        myForm.maintainParameter("secure_num");
        myForm.maintainParameter("line");
        int skraMargaInt = 0;
        String skraMarga =  modinfo.getParameter("skraMarga");

        int line = Integer.parseInt( modinfo.getParameter("line"));


        int check = checkLine(line, currentField, date, modinfo);
        if( check > 3){
                setErroResponse(myForm, false);
        }
        else if( check == -1){
                this.add(new Text("Ekki næst samband við gagnagrunn"));
        }
        else{
          if( modinfo.getParameter("secure_num") != null){
            List illegalVector = handleFormInfo(modinfo);
            if(illegalVector != null){
              //setErroResponse(myForm, true);
            }
            else{
            //this.setParentToReload();
            //this.close();
            //this.print(modinfo);
            }
          }
          else{
            fieldInfo = business.getFieldConfig( Integer.parseInt(currentField) , currentDay );
            skraMargaInt = Integer.parseInt(skraMarga);
            lineUpTable(skraMargaInt, modinfo);
          }
        }
      }
    }else{
      this.noPermission();
    }
  } // method main() ends

} // Class ends