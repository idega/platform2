package com.idega.projects.golf.startingtime.presentation;

import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.projects.golf.startingtime.business.TeeTimeBusiness;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.interfaceobject.TextInput;
import com.idega.jmodule.object.interfaceobject.Form;
import com.idega.jmodule.object.interfaceobject.DropdownMenu;
import com.idega.jmodule.object.interfaceobject.SubmitButton;
import com.idega.jmodule.object.interfaceobject.HiddenInput;
import com.idega.jmodule.object.Image;
import com.idega.projects.golf.GolfField;
import com.idega.util.idegaTimestamp;
import com.idega.projects.golf.entity.TournamentDay;
import com.idega.projects.golf.entity.Tournament;
import com.idega.projects.golf.entity.TournamentRound;
import com.idega.data.EntityFinder;
import com.idega.jmodule.object.textObject.Text;
import com.idega.jmodule.object.textObject.Link;
import com.idega.projects.golf.entity.Union;
import com.idega.projects.golf.entity.Member;
import com.idega.jmodule.object.interfaceobject.CloseButton;
import com.idega.jmodule.object.interfaceobject.BackButton;
import com.idega.projects.golf.entity.StartingtimeFieldConfig;
import com.idega.projects.golf.business.GolfCacher;
import com.idega.projects.golf.templates.page.JmoduleWindowModuleWindow;

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

public class RegisterTime extends JmoduleWindowModuleWindow {

  private TeeTimeBusiness business;
  private DropdownMenu unionDropdown;
  private Form myForm = null;
  private Table frameTable;
  private int maxPerMemberPerDay = 1;
  private int maxCountInGroups = 4;
  private int maxPerOwnerPerDay = 4;

  private idegaTimestamp currentDay;
  private String currentField;
  private String currentUnion;
  private String currentMember;
  private StartingtimeFieldConfig fieldInfo;
  private Text templText;
  private static String closeParameterString = "window_close";


  public RegisterTime() {
    super();
    myForm = new Form();
    frameTable = new Table();
    frameTable.setAlignment("center");
    frameTable.setWidth("100%");
    myForm.add(frameTable);
    this.add(myForm);
    business = new TeeTimeBusiness();
    unionDropdown = (DropdownMenu)GolfCacher.getUnionAbbreviationDropdown("club").clone();
    templText = new Text("");
    templText.setFontSize(1);
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


            int memberId = -1;
            boolean memberAvailable = false;
            //fá member id fyrir member til að finna hann og setja inn í textinputið fyrir hann
            if(modinfo.getSession().getAttribute("member_id") != null){
              memberId = Integer.parseInt((String)modinfo.getSession().getAttribute("member_id"));
              memberAvailable = true;
            }

            String lines[] = new String[skraMarga];
            int groupNums[] = new int[skraMarga];

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
              int max =business.countEntriesInGroup(Line,this.currentField,this.currentDay);

              for(int j = 0; j < skraMarga ; j++){
                if(max > 3){
                  while(max > 3){
                    Line++;
                    max = business.countEntriesInGroup(Line,this.currentField,this.currentDay);
                  }
                }
                max++;
                lines[j] = getTime(Line, myGolfField);
                groupNums[j] = Line;

              }

              Table myTable =  new Table(6, skraMarga+3);
              myTable.setCellpadding(0);
              myTable.setCellspacing(0);
              myTable.setWidth(2, "40");
              myTable.setHeight(1,"30");


              myTable.addText("<b>"+this.iwrb.getLocalizedString("start.time", "Time")+"</b>", 2, 1);
              myTable.addText("<b>"+this.iwrb.getLocalizedString("start.social_nr","Social nr.")+"</b>", 3, 1);
              myTable.addText("<b>"+this.iwrb.getLocalizedString("start.vip_card","VIP card")+"</b>", 5, 1);
              myTable.addText("<b>"+this.iwrb.getLocalizedString("start.card_number","Card number")+"</b>", 6, 1);

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
                  myTable.add(new HiddenInput("group_num",Integer.toString(groupNums[i-1])),2, i+1);
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
              myTable.add(insertButton(this.iwrb.getImage("buttons/book.gif"),"", modinfo.getRequestURI(), "post", myForm), 4, i+2);
              myTable.add(new SubmitButton(this.iwrb.getImage("buttons/cancel.gif"),closeParameterString, "true"), 4, i+2);
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

    public void handleFormInfo(ModuleInfo modinfo)throws SQLException, IOException {

      Vector illegal = new Vector(0);
      int k = 0;
      frameTable.empty();
      frameTable.setAlignment(1,1,"center");
        if( modinfo.getParameter("secure_num") != null){
          String sentSecureNums[] =  modinfo.getParameterValues("secure_num");
          String playerCard[] =  modinfo.getParameterValues("card");
          String playerCardNo[] =  modinfo.getParameterValues("cardNo");
          String lines[] = modinfo.getParameterValues("group_num");
          int numPlayers = sentSecureNums.length;
          boolean ones = false;
          boolean fullGroup = false;
          boolean fullOwnerQuota = false;
          boolean fullMemberQuota = false;

          if(sentSecureNums != null){
            for (int j = 0; j < sentSecureNums.length; j++) {
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

                  if(ssn){
                    Member tempMemb = (com.idega.projects.golf.entity.Member)Member.getMember(sentSecureNums[j]);
                    if(tempMemb != null){

                      if( business.countEntriesInGroup(Integer.parseInt(lines[j]),this.currentField,this.currentDay) >= maxCountInGroups){
                        illegal.add(k++,new Integer(j));
                        fullGroup = true;
                      }else if(business.countOwnersEntries(Integer.parseInt(this.currentMember),this.currentField,this.currentDay) >= maxPerOwnerPerDay ){
                        illegal.add(k++,new Integer(j));
                        fullOwnerQuota = true;
                      }else if(business.countMembersEntries(tempMemb.getID(),this.currentField,this.currentDay) >= maxPerMemberPerDay){
                        illegal.add(k++,new Integer(j));
                        fullMemberQuota = true;
                      }else{
                        business.setStartingtime(Integer.parseInt(lines[j]), this.currentDay, this.currentField, Integer.toString(tempMemb.getID()),this.currentMember, tempMemb.getName(), Float.toString(tempMemb.getHandicap()), GolfCacher.getCachedUnion(tempMemb.getMainUnionID()).getAbbrevation(), playerCard[j], playerCardNo[j]);
                        ones = true;
                      }
                    }else{
                      illegal.add(k++,new Integer(j));
                    }
                  }else{
                    illegal.add(k++,new Integer(j));
                  }
                }
              }catch(SQLException e){
                illegal.add(k++,new Integer(j));
              }
            }

              if(illegal.size() > 0){

                Text myText = (Text)templText.clone();
                myText.setText("villa kom upp í eftirfarandi: ");

                Table tempTable = new Table(3,illegal.size());

                for (int i = 0; i < illegal.size(); i++) {
                  int index = ((Integer)illegal.get(i)).intValue();

                  Text tempIllegal1 = (Text)templText.clone();
                  tempIllegal1.setText(sentSecureNums[index]);
                  tempTable.add(tempIllegal1,1,i+1);

                  Text tempIllegal2 = (Text)templText.clone();
                  tempIllegal2.setText(playerCard[index]);
                  tempTable.add(tempIllegal2,2,i+1);

                  Text tempIllegal3 = (Text)templText.clone();
                  tempIllegal3.setText(playerCardNo[index]);
                  tempTable.add(tempIllegal3,3,i+1);

                }

                frameTable.add(Text.getBreak());
                frameTable.add(myText);
                frameTable.add(Text.getBreak());
                frameTable.add(tempTable);

                if(ones){
                  Text noError = (Text)templText.clone();
                  noError.setText("...aðrir voru skráðir inn");
                  frameTable.add(Text.getBreak());
                  frameTable.add(noError);
                }

                if(fullGroup){
                  Text Error = (Text)templText.clone();
                  Error.setText("Holl sem reynt var að skrá í er fullt ");
                  frameTable.add(Text.getBreak());
                  frameTable.add(Error);
                }

                if(fullOwnerQuota){
                  Text ownerQuota = (Text)templText.clone();
                  ownerQuota.setText("Hefur ekki réttindi til að skrá fleiri á þessum velli í dag");
                  frameTable.add(Text.getBreak());
                  frameTable.add(ownerQuota);

                  Text comment = (Text)templText.clone();
                  comment.setText("Hafið samband við klúbbinn ef skrá á fleiri");
                  frameTable.add(Text.getBreak());
                  frameTable.add(comment);
                } else if(fullMemberQuota){
                  Text memberQuota = (Text)templText.clone();
                  memberQuota.setText("Ekki má skrá sama mann oftar en einu sinni á dag í netskráningu");
                  frameTable.add(Text.getBreak());
                  frameTable.add(memberQuota);

                  Text comment = (Text)templText.clone();
                  comment.setText("Hafið samband við klúbbinn til að klára þessa skráningu");
                  frameTable.add(Text.getBreak());
                  frameTable.add(comment);
                }else{
                  Text comment = (Text)templText.clone();
                  comment.setText("Reynið aftur eða hafið samband við klúbbinn");
                  frameTable.add(Text.getBreak());
                  frameTable.add(comment);
                }

                  //this.add(new BackButton(new Image("/pics/rastimask/Takkar/Ttilbaka1.gif")));
                  frameTable.add(Text.getBreak());
                  frameTable.add(Text.getBreak());
                  frameTable.add(new CloseButton(iwrb.getLocalizedString("start.close_window","Close Window")));

              }else{
                this.setParentToReload();
                this.close();
              }

          }else{
            Text comment = (Text)templText.clone();
            comment.setText("Enginn skráðist");
            frameTable.add(Text.getBreak());
            frameTable.add(comment);

            //this.add(new BackButton(new Image("/pics/rastimask/Takkar/Ttilbaka1.gif")));
            frameTable.add(Text.getBreak());
            frameTable.add(Text.getBreak());
            frameTable.add(new CloseButton(iwrb.getLocalizedString("start.close_window","Close Window")));
          }
        }else{
          Text comment = (Text)templText.clone();
          comment.setText("Enginn skráðist");
          frameTable.add(Text.getBreak());
          frameTable.add(comment);

          //this.add(new BackButton(new Image("/pics/rastimask/Takkar/Ttilbaka1.gif")));
          frameTable.add(Text.getBreak());
          frameTable.add(Text.getBreak());
          frameTable.add(new CloseButton(iwrb.getLocalizedString("start.close_window","Close Window")));
        }
    }

    public void setErroResponse(Form myForm, boolean inputErr)
    {
            Table myTable = new Table(2, 3);
            if(inputErr){
                    myTable.addText("Nauðsynlegt er að skrá eins marga og teknir voru frá", 2, 1);
                    myTable.add(new BackButton("Til baka"), 2, 3);
            }
            else{
                    myTable.addText("Þetta holl er því miður fullt. Gjörðu svo vel að velja þér nýjan tíma", 2, 1);
                    myTable.add(new CloseButton(iwrb.getLocalizedString("start.close_window","Close Window")), 2, 3);
            }

            myTable.setAlignment(2, 3, "center");
            myTable.setCellpadding(0);
            myTable.setCellspacing(0);
            frameTable.empty();
            frameTable.add(myTable);

    }

    public GolfField getFieldInfo( int field, String date) throws SQLException,IOException{
            StartingtimeFieldConfig FieldConfig = business.getFieldConfig( field , date );
            GolfField field_info = new GolfField ( new idegaTimestamp(FieldConfig.getOpenTime()).toSQLTimeString(), new idegaTimestamp(FieldConfig.getCloseTime()).toSQLTimeString(), FieldConfig.getMinutesBetweenStart(), field, date, FieldConfig.getDaysShown(), FieldConfig.publicRegistration());
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
    Text satyOut = new Text(this.iwrb.getLocalizedString("start.no_permission","No permission"));
    satyOut.setFontSize(4);
    Table AlignmentTable = new Table();
    AlignmentTable.setBorder(0);
    AlignmentTable.add(Text.getBreak());
    AlignmentTable.add(satyOut);
    AlignmentTable.setAlignment("center");
    AlignmentTable.add(Text.getBreak());
    AlignmentTable.add(Text.getBreak());
    frameTable.empty();
    frameTable.add(AlignmentTable);
  }




public void main(ModuleInfo modinfo) throws Exception {
    super.main(modinfo);
    this.setTitle(this.iwrb.getLocalizedString("start.register_tee_time","Register tee time"));


    if(modinfo.getParameter(closeParameterString+".x") != null || modinfo.getParameter(closeParameterString) != null){
      this.close();
    }else{


      String date = modinfo.getSession().getAttribute("date").toString();
      //String field_id = modinfo.getSession().getAttribute("field_id").toString();
      currentField = modinfo.getSession().getAttribute("field_id").toString();
      currentUnion = modinfo.getSession().getAttribute("union_id").toString();


      boolean keepOn = true;

      try{
        currentMember = Integer.toString(com.idega.projects.golf.login.business.LoginBusiness.getMember(modinfo).getID());
        currentDay = new idegaTimestamp(date);
      }catch(Exception e){
        keepOn = false;
        this.noPermission();
      }



  //    if(modinfo.getParameter(saveParameterString+".x") != null || modinfo.getParameter(saveParameterString) != null){
  //      this.handleFormInfo(modinfo);
  //    }

      if(keepOn){
          myForm.maintainParameter("secure_num");
          myForm.maintainParameter("line");
          int skraMargaInt = 0;
          String skraMarga = modinfo.getParameter("skraMarga");

          int line = Integer.parseInt( modinfo.getParameter("line"));
          int check = business.countEntriesInGroup(line, currentField, currentDay);

          if( check > 3){
            setErroResponse(myForm, false);
          }
          else{
            if( modinfo.getParameter("secure_num") != null){
              handleFormInfo(modinfo);
            }else{
              fieldInfo = business.getFieldConfig( Integer.parseInt(currentField) , currentDay );
              skraMargaInt = Integer.parseInt(skraMarga);
              lineUpTable(skraMargaInt, modinfo);
            }
          }

       }else{
         this.noPermission();
       }
    }
  } // method main() ends

} // Class ends