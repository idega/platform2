package is.idega.idegaweb.golf.startingtime.presentation;

import is.idega.idegaweb.golf.GolfField;
import is.idega.idegaweb.golf.business.GolfCacher;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberBMPBean;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.StartingtimeFieldConfig;
import is.idega.idegaweb.golf.block.login.business.GolfLoginBusiness;
import is.idega.idegaweb.golf.startingtime.business.TeeTimeBusinessBean;
import is.idega.idegaweb.golf.templates.page.GolfWindow;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

/**
 * Title:        Golf
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a>
 * @version 1.0
 */

public class RegisterTime extends GolfWindow {

  private TeeTimeBusinessBean business;
  private DropdownMenu unionDropdown;
  private Form myForm = null;
  private Table frameTable;
  private int maxPerMemberPerDay = 1;
  private int maxCountInGroups = 4;
  private int maxPerOwnerPerDay = 4;

  private IWTimestamp currentDay;
  private String currentField;
  private String currentUnion;
  private String currentMember;
  private StartingtimeFieldConfig fieldInfo;
  private Text templText;
  private static String closeParameterString = "window_close";


  public RegisterTime() {
  	super("Register Tee Time",400,340);
    this.setScrollbar(true);
    myForm = new Form();
    frameTable = new Table();
    frameTable.setAlignment("center");
    frameTable.setWidth("100%");
    myForm.add(frameTable);
    this.add(myForm);
    business = new TeeTimeBusinessBean();
    unionDropdown = (DropdownMenu)GolfCacher.getUnionAbbreviationDropdown("club").clone();
    templText = getSmallText("");
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

    public void lineUpTable(int skraMarga, IWContext modinfo)throws IOException
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
                member = ((MemberHome) IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKey(memberId);
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


              myTable.add(getSmallText(this._iwrb.getLocalizedString("start.time", "Time")), 2, 1);
              myTable.add(getSmallText(this._iwrb.getLocalizedString("start.social_nr","Social nr.")), 3, 1);
              myTable.add(getSmallText(this._iwrb.getLocalizedString("start.vip_card","VIP card")), 5, 1);
              myTable.add(getSmallText(this._iwrb.getLocalizedString("start.card_number","Card number")), 6, 1);

              myTable.setColumnAlignment(1,"center");
              myTable.setColumnAlignment(5,"center");
              myTable.setColumnAlignment(6,"center");



              boolean admin = false;
              boolean clubadmin = false;
              boolean clubworker = false;
              String unionAbbrevation = null;

              if(memberAvailable){
                admin = is.idega.idegaweb.golf.block.login.business.AccessControl.isAdmin(modinfo);
                clubadmin = is.idega.idegaweb.golf.block.login.business.AccessControl.isClubAdmin(modinfo);
                clubworker = is.idega.idegaweb.golf.block.login.business.AccessControl.isClubWorker(modinfo);
                unionAbbrevation = member.getMainUnion().getAbbrevation();
              }

              int i = 1;
              for ( ;i < skraMarga+1 ; i++)
              {
                  myTable.setWidth(1, "25");
                  myTable.add(getSmallText(lines[i-1]), 2, i+1);
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
              myTable.add(getButton(new SubmitButton(localize("teetime.book","Book"))), 4, i+2);
              myForm.setMethod("post");
              myTable.add(getButton(new CloseButton(localize("teetime.cancel","Cancel"))), 4, i+2);
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
            catch (FinderException E) {
            		E.printStackTrace();
            }
    }

    public void handleFormInfo(IWContext modinfo)throws SQLException, IOException {

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
                    Member tempMemb = (is.idega.idegaweb.golf.entity.Member)MemberBMPBean.getMember(sentSecureNums[j]);
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
                myText.setText(localize("start.error_in_following","Error in following:"));

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
                  noError.setText(localize("start.all_others_where_registered","All others where registered"));
                  frameTable.add(Text.getBreak());
                  frameTable.add(noError);
                }

                if(fullGroup){
                  Text Error = (Text)templText.clone();
                  Error.setText(localize("start.group_is_full","Group is full"));//"Holl sem reynt var aÝ skr‡ ’ er fullt ");
                  frameTable.add(Text.getBreak());
                  frameTable.add(Error);
                }

                if(fullOwnerQuota){
                  Text ownerQuota = (Text)templText.clone();
                  ownerQuota.setText(localize("start.ownerquota","Not allowed to register more golfers to day"));//"Hefur ekki rŽttindi til aÝ skr‡ fleiri ‡ ßessum velli ’ dag");
                  frameTable.add(Text.getBreak());
                  frameTable.add(ownerQuota);

                  Text comment = (Text)templText.clone();
                  comment.setText(localize("start.contact_club_to_register","Contact the club to register"));//"HafiÝ samband viÝ klœbbinn ef skr‡ ‡ fleiri");
                  frameTable.add(Text.getBreak());
                  frameTable.add(comment);
                } else if(fullMemberQuota){
                  Text memberQuota = (Text)templText.clone();
                  memberQuota.setText(localize("start.memberquota","Not allowed to register golfer again in public registration to day"));//"Ekki m‡ skr‡ sama mann oftar en einu sinni ‡ dag ’ netskr‡ningu");
                  frameTable.add(Text.getBreak());
                  frameTable.add(memberQuota);

                  Text comment = (Text)templText.clone();
                  comment.setText(localize("start.contact_club_to_register","Contact the club to register"));//"HafiÝ samband viÝ klœbbinn ef skr‡ ‡ fleiri");
                  frameTable.add(Text.getBreak());
                  frameTable.add(comment);
                }else{
                  Text comment = (Text)templText.clone();
                  comment.setText(localize("start.try_again_or_contact_the_club","Try again or contact the club"));//"ReyniÝ aftur eÝa hafiÝ samband viÝ klœbbinn");
                  frameTable.add(Text.getBreak());
                  frameTable.add(comment);
                }

                  //this.add(new BackButton(new Image("/pics/rastimask/Takkar/Ttilbaka1.gif")));
                  frameTable.add(Text.getBreak());
                  frameTable.add(Text.getBreak());
                  frameTable.add(new CloseButton(_iwrb.getLocalizedString("start.close_window","Close Window")));

              }else{
                this.setParentToReload();
                this.close();
              }

          }else{
            Text comment = (Text)templText.clone();
            comment.setText(localize("start.no_one_was_registered","No one was registered"));//"Enginn skr‡Ýist");
            frameTable.add(Text.getBreak());
            frameTable.add(comment);

            //this.add(new BackButton(new Image("/pics/rastimask/Takkar/Ttilbaka1.gif")));
            frameTable.add(Text.getBreak());
            frameTable.add(Text.getBreak());
            frameTable.add(new CloseButton(_iwrb.getLocalizedString("start.close_window","Close Window")));
          }
        }else{
          Text comment = (Text)templText.clone();
          comment.setText(localize("start.no_one_was_registered","No one was registered"));//"Enginn skr‡Ýist");
          frameTable.add(Text.getBreak());
          frameTable.add(comment);

          //this.add(new BackButton(new Image("/pics/rastimask/Takkar/Ttilbaka1.gif")));
          frameTable.add(Text.getBreak());
          frameTable.add(Text.getBreak());
          frameTable.add(getButton(new CloseButton(_iwrb.getLocalizedString("start.close_window","Close Window"))));
        }
    }

    public void setErroResponse(Form myForm, boolean inputErr)
    {
            Table myTable = new Table(2, 3);
            if(inputErr){
                    myTable.add(getErrorText(localize("start.you_have_to_register_as_many_as_reserved","You have to register as many as reserved")),2,1);//"NauÝsynlegt er aÝ skr‡ eins marga og teknir voru fr‡"), 2, 1);
                    myTable.add(getButton(new BackButton()), 2, 3);
            }
            else{
                    myTable.add(getErrorText(localize("start.group_is_full","This group is full. Choose another time.")),2,1);//"Þetta holl er ßv’ miÝur fullt. GjšrÝu svo vel aÝ velja ßŽr nàjan t’ma"), 2, 1);
                    myTable.add(getButton(new CloseButton(_iwrb.getLocalizedString("start.close_window","Close Window"))), 2, 3);
            }

            myTable.setAlignment(2, 3, "center");
            myTable.setCellpadding(0);
            myTable.setCellspacing(0);
            frameTable.empty();
            frameTable.add(myTable);

    }

    public GolfField getFieldInfo( int field, String date) throws SQLException,IOException{
            StartingtimeFieldConfig FieldConfig = business.getFieldConfig( field , date );
            GolfField field_info = new GolfField ( new IWTimestamp(FieldConfig.getOpenTime()).toSQLTimeString(), new IWTimestamp(FieldConfig.getCloseTime()).toSQLTimeString(), FieldConfig.getMinutesBetweenStart(), field, date, FieldConfig.getDaysShown(), FieldConfig.publicRegistration());
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
    Text satyOut = getErrorText(this._iwrb.getLocalizedString("start.no_permission","No permission"));
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




   public void main(IWContext modinfo) throws Exception {
  	  super.main(modinfo);
      this.setTitle(this._iwrb.getLocalizedString("start.register_tee_time","Register tee time"));

	  boolean keepOn = true;
	  try{
	    String date = modinfo.getSession().getAttribute("date").toString();
	    //String field_id = modinfo.getSession().getAttribute("field_id").toString();
	    currentField = modinfo.getSession().getAttribute("field_id").toString();
	    currentUnion = modinfo.getSession().getAttribute("union_id").toString();
	
	
        currentMember = Integer.toString(GolfLoginBusiness.getMember(modinfo).getID());
        currentDay = new IWTimestamp(date);
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
    } // method main() ends

} // Class ends