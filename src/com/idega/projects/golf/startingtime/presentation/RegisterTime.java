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

  public RegisterTime() {
    super();
    business = new StartService();
    unionDropdown = (DropdownMenu)GolfCacher.getUnionAbbreviationDropdown("club").clone();
  }



    public TextInput insertEditBox(String name, Form myForm)
    {
            TextInput myInput = new TextInput(name);
            myInput.setParentObject(myForm);
            myInput.setAsNotEmpty();
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


      /*DropdownMenu mydropdown = new DropdownMenu(name);

        Union union = Union.getStaticInstance();
        List unions = EntityFinder.findAll(union,"Select * from " +union.getEntityName() + " order by abbrevation");
          for(int i = 0; i < unions.size(); i++){
            union = (Union)unions.get(i);
            mydropdown.addMenuElement(union.getAbbrevation(), union.getAbbrevation());
        }
        mydropdown.setSelectedElement(text);
        mydropdown.keepStatusOnAction();
      return mydropdown;*/
    }


    public DropdownMenu insertUnionDropdown(String name, int size) throws SQLException{
      DropdownMenu mydropdown = new DropdownMenu(name);

        Union union = Union.getStaticInstance();
        List unions = EntityFinder.findAll(union,"Select * from " +union.getEntityName() + " order by abbrevation");
          for(int i = 0; i < unions.size(); i++){
            union = (Union)unions.get(i);
            mydropdown.addMenuElement(union.getAbbrevation(), union.getAbbrevation());
        }
        mydropdown.setSelectedElement("");
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

    public void drawTable(int skraMarga, Form myForm, ModuleInfo modinfo)throws IOException
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
                            String FieldID = modinfo.getSession().getAttribute("field_id").toString();
                            String Date = modinfo.getSession().getAttribute("date").toString();
                            String MemberId = modinfo.getSession().getAttribute("member_id").toString();
                            GolfField myGolfField = getFieldInfo( Integer.parseInt(FieldID), Date);

                            int Line = Integer.parseInt(modinfo.getRequest().getParameter("line"));

                            int max = checkLine(Line, FieldID, Date, modinfo);

                            for(int j = 0; j < skraMarga ; j++)
                            {

                                    if(max > 3){
                                            while(max > 3){
                                                    Line++;
                                                    max = checkLine(Line, FieldID, Date, modinfo);
                                            }
                                    }
                                    max++;
                                    lines[j] = getTime(Line, myGolfField);
                            }

                            Table myTable =  new Table(7, skraMarga+3);
                            myTable.setCellpadding(0);
                            myTable.setCellspacing(0);
                            myTable.setWidth(2, "40");
                            myTable.setHeight(1,"30");


                            myTable.addText("Tími", 2, 1);
                            myTable.addText("Nafn", 3, 1);
                            myTable.addText("Klúbbur", 4, 1);
                            myTable.addText("Forgjöf", 5, 1);
                            myTable.addText("Sérkort", 6, 1);
                            myTable.addText("Kortanúmer", 7, 1);

                            myTable.setAlignment(4,1,"center");
                            myTable.setAlignment(5,1,"center");
                            myTable.setAlignment(6,1,"center");
                            myTable.setAlignment(7,1,"center");


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
                                    myTable.addText(lines[i-1], 2, i+1);
                                    myTable.setAlignment(2, i+1, "left");

                                    if(admin || clubadmin || clubworker){
                                      myTable.add(insertEditBox("name", myForm), 3, i+1);
                                      myTable.add(insertUnionDropdown("club", unionAbbrevation, 5), 4, i+1);
                                      myTable.add(insertEditBox("handycap",6), 5, i+1);
                                    }else{
                                        if(i == 1 && memberAvailable){
                                              String handicap = new Float(member.getHandicap()).toString();
                                              if(handicap.equals("-1.0")) handicap = "100.0";
                                              myTable.add(insertEditBox("name", member.getName()), 3, i+1);
                                              myTable.add(insertUnionDropdown("club", unionAbbrevation, 5), 4, i+1);
                                              myTable.add(insertEditBox("handycap", handicap, 6), 5, i+1);
                                        }else{
                                              myTable.add(insertEditBox("name", myForm), 3, i+1);
                                              myTable.add(insertUnionDropdown("club",unionAbbrevation, 5), 4, i+1);
                                              myTable.add(insertEditBox("handycap",6), 5, i+1);
                                        }
                                    }

                                    myTable.add(insertEditBox("card", 4), 6, i+1);
                                    myTable.add(insertEditBox("cardNo", 12), 7, i+1);

                            }


                            setPlayers(modinfo);

                    myTable.mergeCells(4, i+2, 7, i+2);
                    myTable.add(insertButton(new Image(btnSkraUrl),"", "innskraning1.jsp", "post", myForm), 4, i+2);
                    myTable.add(new CloseButton(new Image(btnCancelUrl)), 4, i+2);
                    myTable.setAlignment(4, i+2, "right");
                    myForm.add(myTable);

                    }
                    catch (SQLException E) {
                            E.printStackTrace();
                    }
                    catch (IOException E) {
                            E.printStackTrace();
                    }
    }

    public boolean setPlayers(ModuleInfo modinfo)throws SQLException, IOException
    {
            int i = 0;

            String FieldID = modinfo.getSession().getAttribute("field_id").toString();
            String Date = modinfo.getSession().getAttribute("date").toString();
            String MemberId = modinfo.getSession().getAttribute("member_id").toString();

            int Line = Integer.parseInt(modinfo.getRequest().getParameter("line"));
            int max = checkLine(Line, FieldID, Date, modinfo);
            int fjoldi = 4 - max;

            try
            {
                    if(modinfo.getRequest().getParameter("name") != null)
                    {

                            String playerName[] = modinfo.getRequest().getParameterValues("name");
                            String playerClub[] = modinfo.getRequest().getParameterValues("club");
                            String playerHandyCap[] = modinfo.getRequest().getParameterValues("handycap");
                            String playerCard[] = modinfo.getRequest().getParameterValues("card");
                            String playerCardNo[] = modinfo.getRequest().getParameterValues("cardNo");
                            int numPlayers = playerName.length;

                            if(modinfo.getRequest().getParameter("handycap") != null){
                                    for(int j = 0; j < playerHandyCap.length; j++)
                                    {
                                            if(playerHandyCap[j].equals(null) || playerHandyCap[j].equals(""))
                                                    playerHandyCap[j] = "-1";
                                            if(playerClub[j].equals(null) || playerClub[j].equals(""))
                                                     playerClub[j] = "&nbsp";
                                            if(playerName[j].equals(""))
                                                    return false;
                                    }
                            }

                            for(; i < numPlayers; i++)
                            {
                                    if(max > 3){
                                            while(max > 3){
                                                    Line++;
                                                    max = checkLine(Line, FieldID, Date, modinfo);
                                            }
                                    }
                                    business.setStartingtime( Line, Date, FieldID, MemberId, playerName[i], playerHandyCap[i], playerClub[i], playerCard[i], playerCardNo[i] );
                                    max++;
                            }
                    }
            }
            catch (SQLException E) {
              E.printStackTrace();
              System.err.println("SQLException: " + E.getMessage());
              System.err.println("SQLState:     " + E.getSQLState());
        }
                    return true;
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

            myForm.add(myTable);

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




  public void main(ModuleInfo modinfo) throws Exception {
    String date = modinfo.getSession().getAttribute("date").toString();
    String field_id = modinfo.getSession().getAttribute("field_id").toString();
    idegaTimestamp timestamp = new idegaTimestamp(date);
    try {

      TournamentDay tempTD = new TournamentDay();

      List Tournaments = EntityFinder.findAll(new Tournament(),"select tournament.* from tournament,tournament_day where tournament_day.tournament_id=tournament.tournament_id and tournament_day.day_date = '"+timestamp.toSQLDateString()+"' and tournament.field_id = " + field_id );

      if(Tournaments != null ){
        String closeParameterString = "closewidow";
        if("true".equals(modinfo.getParameter(closeParameterString))){
          //this.close();
          //this.print(modinfo);


        }else{
          Form myForm = new Form();
          this.add(myForm);
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
          close.addParameter(closeParameterString, "true");
          AlignmentTable.add(close);
          myForm.add(AlignmentTable);
        }

      }else{

            Form myForm = new Form();
            myForm.maintainParameter("name");
            myForm.maintainParameter("line");
            int skraMargaInt = 0;
            String skraMarga = modinfo.getRequest().getParameter("skraMarga");
            this.add(myForm);

            int line = Integer.parseInt(modinfo.getRequest().getParameter("line"));


            try
            {

                    int check = checkLine(line, field_id, date, modinfo);

                    if( checkLine(line, field_id, date, modinfo) > 3){
                            setErroResponse(myForm, false);

                    }
                    else if( checkLine(line, field_id, date, modinfo) == -1){
                            this.add(new Text("Ekki næst samband við gagnagrunn"));

                    }
                    else
                    {
                            if(modinfo.getRequest().getParameter("name") != null)
                            {

                                    if(!setPlayers(modinfo)){
                                            setErroResponse(myForm, true);
                                    }
                                    else{
                                    //this.setParentToReload();
                                    //this.close();
                                    //this.print(modinfo);
                                    }
                            }
                            else{

                                    skraMargaInt = Integer.parseInt(skraMarga);
                                    drawTable(skraMargaInt, myForm, modinfo);
                            }
                    }
            }
            catch (SQLException E)
        {
            System.err.println("SQLException: " + E.getMessage());
            System.err.println("SQLState:     " + E.getSQLState());
            System.err.println("VendorError:  " + E.getErrorCode());

        }
            catch (Exception E)
        {
            E.printStackTrace();
        }
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }


  } // method main() ends






} // Class ends