package is.idega.idegaweb.golf.startingtime.presentation;

import is.idega.idegaweb.golf.GolfField;
import is.idega.idegaweb.golf.block.login.business.GolfLoginBusiness;
import is.idega.idegaweb.golf.business.GolfCacher;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberBMPBean;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.StartingtimeFieldConfig;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import is.idega.idegaweb.golf.startingtime.business.TeeTimeBusinessBean;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.text.Strong;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.FieldSet;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.Label;
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

public class RegisterTime extends GolfBlock {

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

  private boolean lockedAsWapLayout = false;
  public static final String PRM_LOCKED_AS_WML_LAYOUT = "iw_lock_as_wml_layout";
  private int backPage = -1;
  public static final String PRM_BACK_PAGE = "bpage";

  public RegisterTime() {
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
            //f· member id fyrir member til a finna hann og setja inn Ì textinputi fyrir hann
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


              myTable.add(getSmallText(localize("start.time", "Time")), 2, 1);
              myTable.add(getSmallText(localize("start.social_nr","Social nr.")), 3, 1);
              myTable.add(getSmallText(localize("start.vip_card","VIP card")), 5, 1);
              myTable.add(getSmallText(localize("start.card_number","Card number")), 6, 1);

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
    
    public void lineUpWMLTable(int skraMarga, IWContext modinfo)throws IOException
    {


            int memberId = -1;
            boolean memberAvailable = true;
            //get member_id for member to find him and put his SSN into the textinput

            String lines[] = new String[skraMarga];
            int groupNums[] = new int[skraMarga];

            try
            {
              Member member = GolfLoginBusiness.getMember(modinfo);
              String FieldID = currentField;
              String Date = modinfo.getSession().getAttribute("date").toString();
              String MemberId = String.valueOf(GolfLoginBusiness.getMember(modinfo).getID());
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

              Text timeText = new Text(localize("start.time", "Time"));
              //new Label(this.localize("start.social_nr","Social nr."),);

              String unionAbbrevation = null;

              if(memberAvailable){
                unionAbbrevation = member.getMainUnion().getAbbrevation();
              }
              
              if(false){
	              Table myTable = new Table();
	              int topRows = 0;
	
	              int i = 1;
	              for ( ;i < skraMarga+1 ; i++)
	              {
	              	FieldSet set = new FieldSet(lines[i-1]+" - " +i);
	              	set.add(new HiddenInput("group_num",Integer.toString(groupNums[i-1])));
	              	set.add(Text.getBreak());
	              	InterfaceObject ob;
	                  if(i == 1 && memberAvailable){
	                  	ob = insertEditBox("secure_num", member.getSocialSecurityNumber());
	                  }else{
	                  	ob = insertEditBox("secure_num", myForm);
	                  }
	                  Label label = new Label(localize("start.social_nr","Social nr."),ob);
	                  set.add(label);
	                  set.add(Text.getBreak());
	                  set.add(ob);
	                  
	                  myTable.add(set,1,i+topRows);
	              }
	
	              myTable.add(new SubmitButton(localize("start.reserve","Reserve")),1, i+topRows);
	              frameTable.empty();
	              frameTable.add(myTable);
              } else {
              	
              	this.empty();
              	
              	
              	Paragraph message = new Paragraph();
              	
              	String sMessage = localize("start.are_you_sure_you_want_to_reserve_this_teetime","Are you sure you want to reserve teetime at");
              	sMessage += " "+lines[0]+" "+(new IWTimestamp(Date)).getDateString("EEE d MMM", modinfo.getCurrentLocale())+" ";
              	//sMessage += localize("start.at_the_field","at the field")+....;
              	sMessage += "?";
				
              	message.add(new Text(sMessage));
              	
              	add(message);
              	
              	Paragraph links = new Paragraph();
              	
              	Link yes = new Link(localize("start.reserve","Reserve"));
              	yes.maintainParameter("secure_num",modinfo);
              	yes.maintainParameter("line",modinfo);
              	yes.maintainParameter("date",modinfo);
              	yes.maintainParameter("field_id",modinfo);
              	yes.maintainParameter("union_id",modinfo);
              	yes.maintainParameter(PRM_LOCKED_AS_WML_LAYOUT,modinfo);
              	yes.maintainParameter(PRM_BACK_PAGE,modinfo);
              	
              	yes.addParameter("secure_num",member.getSocialSecurityNumber());
              	yes.addParameter("group_num",Integer.toString(groupNums[0]));
              	
              	Strong y = new Strong();
              	y.add(yes);
              	links.add(y);
              	
          		BackButton cancel = new BackButton(localize("start.cancel","Cancel"));
              	links.add(new Text(" | "));
              	Strong s = new Strong();
              	s.add(cancel);
              	links.add(s);
              	add(links);
              }
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
      Vector legal = new Vector(0);
      int k = 0;
      int l = 0;
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

                      if( business.countEntriesInGroup(
                      		Integer.parseInt(lines[j]),
							this.currentField,this.currentDay) >= maxCountInGroups){
                        illegal.add(k++,new Integer(j));
                        fullGroup = true;
                      }else if(business.countOwnersEntries(Integer.parseInt(this.currentMember),this.currentField,this.currentDay) >= maxPerOwnerPerDay ){
                        illegal.add(k++,new Integer(j));
                        fullOwnerQuota = true;
                      }else if(business.countMembersEntries(tempMemb.getID(),this.currentField,this.currentDay) >= maxPerMemberPerDay){
                        illegal.add(k++,new Integer(j));
                        fullMemberQuota = true;
                      }else{
						String unionAbbr = "-";
						Union union = GolfCacher.getCachedUnion(tempMemb.getMainUnionID());
						if(union !=null){
							unionAbbr = union.getAbbrevation();
						}

						

					   if(playerCard != null && playerCard.length>j){
					   	business.setStartingtime(
					   			Integer.parseInt(lines[j]),
								this.currentDay, this.currentField, 
								Integer.toString(tempMemb.getID()),
								this.currentMember, tempMemb.getName(), 
								Float.toString(tempMemb.getHandicap()), 
								unionAbbr, playerCard[j], 
								playerCardNo[j]);
					   } else {
					   	business.setStartingtime(
							   	Integer.parseInt(lines[j]),
								this.currentDay, this.currentField, 
								Integer.toString(tempMemb.getID()),
								this.currentMember, tempMemb.getName(), 
								Float.toString(tempMemb.getHandicap()), 
								unionAbbr, "", 
								"");
					   }
					   legal.add(l++,new Integer(j));
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
            	
            
            if(legal.size() > 0){
            	 if(lockedAsWapLayout || modinfo.isClientHandheld()){    
            	 	Text myText = (Text)templText.clone();
                    myText.setText(localize("start.you_have_been_registered","You have been registered"));

            	 	Paragraph p = new Paragraph();
	             p.add(myText);
	             frameTable.add(p);
	             
            	 } else {
            	
                Text myText = (Text)templText.clone();
                myText.setText(localize("start.the_following_where_registered","The following where registered:"));

                Table tempTable = new Table(3,legal.size());
                tempTable.setToForceToRenderAsTableInWML(true);

                for (int i = 0; i < legal.size(); i++) {
                  int index = ((Integer)legal.get(i)).intValue();

                  Text tempIllegal1 = (Text)templText.clone();
                  tempIllegal1.setText(sentSecureNums[index]);
                  tempTable.add(tempIllegal1,1,i+1);

                  if(playerCard != null && playerCard.length>index){
    	                  Text tempIllegal2 = (Text)templText.clone();
    	                  tempIllegal2.setText(playerCard[index]);
    	                  tempTable.add(tempIllegal2,2,i+1);
    	
    	                  Text tempIllegal3 = (Text)templText.clone();
    	                  tempIllegal3.setText(playerCardNo[index]);
    	                  tempTable.add(tempIllegal3,3,i+1);
                  }
                }
                	
                Paragraph p = new Paragraph();
                p.add(Text.getBreak());
                p.add(myText);
                p.add(Text.getBreak());
                p.add(tempTable);
                frameTable.add(p);
            	 }
//                frameTable.add(Text.getBreak());
//                frameTable.add(Text.getBreak());
//                frameTable.add(new CloseButton(localize("start.close_window","Close Window")));

              }

              if(illegal.size() > 0){
                Text myText = (Text)templText.clone();
                myText.setText(localize("start.error_in_following","Error in following:"));

                Table tempTable = new Table(3,illegal.size());
                tempTable.setToForceToRenderAsTableInWML(true);

                for (int i = 0; i < illegal.size(); i++) {
                  int index = ((Integer)illegal.get(i)).intValue();

                  Text tempIllegal1 = (Text)templText.clone();
                  tempIllegal1.setText(sentSecureNums[index]);
                  tempTable.add(tempIllegal1,1,i+1);

                  if(playerCard != null && playerCard.length>index){
	                  Text tempIllegal2 = (Text)templText.clone();
	                  tempIllegal2.setText(playerCard[index]);
	                  tempTable.add(tempIllegal2,2,i+1);
	
	                  Text tempIllegal3 = (Text)templText.clone();
	                  tempIllegal3.setText(playerCardNo[index]);
	                  tempTable.add(tempIllegal3,3,i+1);
                  }
                }
                
                Paragraph p2 = new Paragraph();
                p2.add(myText);
                p2.add(Text.getBreak());
                p2.add(tempTable);
                frameTable.add(p2);

                if(ones){
                	Paragraph p = new Paragraph();
                  Text noError = (Text)templText.clone();
                  noError.setText(localize("start.all_others_where_registered","All others where registered"));
                  p.add(noError);
                  frameTable.add(p);
                }

                if(fullGroup){
                	Paragraph p = new Paragraph();
                  Text Error = (Text)templText.clone();
                  Error.setText(localize("start.group_is_full","Group is full"));//"Holl sem reynt var a› skrá í er fullt ");
                  p.add(Error);
                  frameTable.add(p);
                }

                if(fullOwnerQuota){
                	Paragraph p = new Paragraph();
                  Text ownerQuota = (Text)templText.clone();
                  ownerQuota.setText(localize("start.ownerquota","Not allowed to register more golfers to day"));//"Hefur ekki réttindi til a› skrá fleiri á ﬂessum velli í dag");
                  p.add(ownerQuota);

                  Text comment = (Text)templText.clone();
                  comment.setText(localize("start.contact_club_to_register","Contact the club to register"));//"Hafi› samband vi› klúbbinn ef skrá á fleiri");
                  p.add(comment);
                  frameTable.add(p);
                } else if(fullMemberQuota){
                	Paragraph p = new Paragraph();
                  Text memberQuota = (Text)templText.clone();
                  memberQuota.setText(localize("start.memberquota","Not allowed to register golfer again in public registration to day"));//"Ekki má skrá sama mann oftar en einu sinni á dag í netskráningu");
                  p.add(memberQuota);

                  Text comment = (Text)templText.clone();
                  comment.setText(localize("start.contact_club_to_register","Contact the club to register"));//"Hafi› samband vi› klúbbinn ef skrá á fleiri");
                  p.add(Text.getBreak());
                  p.add(comment);
                  frameTable.add(p);
                }else{
                	Paragraph p = new Paragraph();
                  Text comment = (Text)templText.clone();
                  comment.setText(localize("start.try_again_or_contact_the_club","Try again or contact the club"));//"Reyni› aftur e›a hafi› samband vi› klúbbinn");
                  p.add(Text.getBreak());
                  p.add(comment);
                  frameTable.add(p);
                }
                
                if(lockedAsWapLayout || modinfo.isClientHandheld()){
                		if(backPage!=-1){
                			Paragraph p = new Paragraph();
                			Link link = new Link(localize("start.wml_back_link","Back to overview"));
                			link.setPage(backPage);
                			p.add(link);
                          frameTable.add(p);
                		}
                } else{
                  //this.add(new BackButton(new Image("/pics/rastimask/Takkar/Ttilbaka1.gif")));
                  frameTable.add(Text.getBreak());
                  frameTable.add(Text.getBreak());
                  frameTable.add(new CloseButton(localize("start.close_window","Close Window")));
                }
              }else{
              	if(lockedAsWapLayout || modinfo.isClientHandheld()){    
              		if(backPage!=-1){
	            			Paragraph p = new Paragraph();
	            			Link link = new Link(localize("start.wml_back_link","Back to overview"));
	            			link.setPage(backPage);
	            			p.add(link);
	                     frameTable.add(p);
              		}
              	}else{
	                this.getParentPage().setParentToReload();
	                this.getParentPage().close();
              	}
              }

          }else{
          	
            Paragraph p = new Paragraph();
          	
            Text comment = (Text)templText.clone();
            comment.setText(localize("start.no_one_was_registered","No one was registered"));//"Enginn skrá›ist");
            p.add(Text.getBreak());
            p.add(comment);

            //this.add(new BackButton(new Image("/pics/rastimask/Takkar/Ttilbaka1.gif")));
            p.add(new Break());
            p.add(new Break());
            
            if(lockedAsWapLayout || modinfo.isClientHandheld()){    
                if(backPage!=-1){
        			p.add(new Break());
        			Link link = new Link(localize("start.wml_back_link","Back to overview"));
        			link.setPage(backPage);
                  frameTable.add(link);
            	   }
          	}else{
          		p.add(new CloseButton(localize("start.close_window","Close Window")));
          	}
            
            
            
            frameTable.add(p);
          }
        }else{
        	
        	  Paragraph p = new Paragraph();
          Text comment = (Text)templText.clone();
          comment.setText(localize("start.no_one_was_registered","No one was registered"));//"Enginn skrá›ist");
          p.add(Text.getBreak());
          p.add(comment);

          //this.add(new BackButton(new Image("/pics/rastimask/Takkar/Ttilbaka1.gif")));
          p.add(new Break());
          p.add(new Break());
          
          if(lockedAsWapLayout || modinfo.isClientHandheld()){    
            if(backPage!=-1){
    			p.add(new Break());
    			Link link = new Link(localize("start.wml_back_link","Back to overview"));
    			link.setPage(backPage);
              p.add(link);
        	   }
      	}else{
      		p.add(getButton(new CloseButton(localize("start.close_window","Close Window"))));
      	}
          frameTable.add(p);
        }
    }

    public void setErroResponse(Form myForm, boolean inputErr)
    {
            Table myTable = new Table(2, 3);
            if(inputErr){
                    myTable.add(getErrorText(localize("start.you_have_to_register_as_many_as_reserved","You have to register as many as reserved")),2,1);//"Nau›synlegt er a› skrá eins marga og teknir voru frá"), 2, 1);
                    myTable.add(getButton(new BackButton()), 2, 3);
            }
            else{
                    myTable.add(getErrorText(localize("start.group_is_full","This group is full. Choose another time.")),2,1);//"ﬁetta holl er ﬂví mi›ur fullt. Gjör›u svo vel a› velja ﬂér n‡jan tíma"), 2, 1);
                    myTable.add(getButton(new CloseButton(localize("start.close_window","Close Window"))), 2, 3);
            }

            myTable.setAlignment(2, 3, "center");
            myTable.setCellpadding(0);
            myTable.setCellspacing(0);
            frameTable.empty();
            Paragraph p = new Paragraph();
            p.add(myTable);
            frameTable.add(p);
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
    Text satyOut = getErrorText(this.localize("start.no_permission","No permission"));
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
   	
   	this.empty();
   	myForm = new Form();
    frameTable = new Table();
    frameTable.setAlignment("center");
    frameTable.setWidth("100%");
    myForm.add(frameTable);
    this.add(myForm);
   	
	this.getParentPage().setTitle(this.localize("start.register_tee_time","Register tee time"));
	
	
	
	try {
		  boolean keepOn = true;
		  try{
		  	
		    String date = modinfo.getParameter("date");
		    //String field_id = modinfo.getSession().getAttribute("field_id").toString();
		    currentField = modinfo.getParameter("field_id");
		    currentUnion = modinfo.getParameter("union_id");
		    
		    if(date==null){
		    		date = (String)modinfo.getSession().getAttribute("date");
			    //String field_id = modinfo.getSession().getAttribute("field_id").toString();
			    currentField = (String)modinfo.getSession().getAttribute("field_id");
			    currentUnion = (String)modinfo.getSession().getAttribute("union_id");
		    }
		    
		    String wmlLock = modinfo.getParameter(PRM_LOCKED_AS_WML_LAYOUT);
		    lockedAsWapLayout = (wmlLock != null && !"".equals(wmlLock));
		    
		    String bPage = modinfo.getParameter(PRM_BACK_PAGE);
		    if(bPage!=null){
		    		try {
					backPage = Integer.parseInt(bPage);
				} catch (NumberFormatException e1) {
				}
		    }
		
		
		    currentMember = Integer.toString(GolfLoginBusiness.getMember(modinfo).getID());
		    currentDay = new IWTimestamp(date);
		  }catch(Exception e){
		  	e.printStackTrace();
		    keepOn = false;
		    this.noPermission();
		  }
		
		
		
		  //    if(modinfo.getParameter(saveParameterString+".x") != null || modinfo.getParameter(saveParameterString) != null){
		  //      this.handleFormInfo(modinfo);
		  //    }
		
		  if(keepOn){
		      myForm.maintainParameter("secure_num");
		      myForm.maintainParameter("line");
		      myForm.maintainParameter("date");
		      myForm.maintainParameter("field_id");
		      myForm.maintainParameter("union_id");
		      myForm.maintainParameter(PRM_LOCKED_AS_WML_LAYOUT);
		      myForm.maintainParameter(PRM_BACK_PAGE);
		      
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
		          if(lockedAsWapLayout || modinfo.isClientHandheld()){
		          	this.empty();
		          	this.add(frameTable);
		          }
		        }else{
		          fieldInfo = business.getFieldConfig( Integer.parseInt(currentField) , currentDay );
		          skraMargaInt = Integer.parseInt(skraMarga);
		          if(lockedAsWapLayout || modinfo.isClientHandheld()){
		          	lineUpWMLTable(skraMargaInt, modinfo);
		          } else {
		          	lineUpTable(skraMargaInt, modinfo);
		          }
		        }
		      }
		
		   }else{
		     this.noPermission();
		   }
	} catch (NumberFormatException e) {
		e.printStackTrace();
	} catch (NullPointerException e){
		e.printStackTrace();
	} catch (SQLException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
    } // method main() ends

/**
 * @param backPage The backPage to set.
 */
public void setBackPage(int backPage) {
	this.backPage = backPage;
}
} // Class ends