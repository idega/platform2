// idega 2000 - gimmi
package com.idega.jmodule.poll.moduleobject;

import com.idega.jmodule.*;
import com.idega.data.*;
import java.io.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import javax.servlet.http.*;
import com.idega.jmodule.poll.data.*;
import java.sql.SQLException;
import java.util.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;


public class BasicPollVoter extends JModuleObject{
        private String attributeName = "idega_id";
        private int attributeId = 1;

        private boolean showTotalVotes = false;
        private boolean showVotes = true;
        private boolean showPollCollection = true;

        private Image separatorImage = null;
        private Image otherPollsImage = null;
        private String otherPollsImageUrl = null;

        private int number_of_shown_polls = 7;

int poll_id=0;
boolean isAdmin;
Poll poll;
Table table;
Form form;
ModuleInfo modinfo;
Window gluggi;
private String resultPageUrl  = "poll_result.jsp";
private String pollAdminUrl = "/poll/pollAdmin.jsp";
String submitButtonText;
String union_id;
String pollWidth;

private boolean leftHeader = true;
private boolean rightHeader = true;
private String headlineColor = "#000000";
private int headlineSize = 2;
private boolean headerLeft = false;
private String submitButtonURL = null;
private String adminButtonURL = null;
private int headerSize = 21;
private String headerFontFace = "Verdana, Arial, Helvetica, sans-serif";
private String styleAttribute = "";

private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.poll";
protected IWResourceBundle iwrb;
protected IWBundle iwb;



private String header_color = "#FFFFFF";
private String header_text_color = "000000";
private String color_1 = null;
private String color_2 = null;

        public BasicPollVoter() {
		this.pollWidth = "148";
		this.submitButtonText = "Kjósa";
		this.isAdmin=false;
        }

	public BasicPollVoter(String resultPageUrl){
		this.resultPageUrl = resultPageUrl;
		this.pollWidth = "148";
		this.submitButtonText = "Kjósa";
		this.isAdmin=false;
	}
	public BasicPollVoter(String resultPageUrl, boolean isAdmin){
		this.resultPageUrl = resultPageUrl;
		this.pollWidth = "148";
		this.submitButtonText = "Kjósa";
		this.isAdmin=isAdmin;
	}
	public BasicPollVoter(String resultPageUrl, String pollWidth ){
		this.resultPageUrl = resultPageUrl;
		this.pollWidth = pollWidth;
		this.submitButtonText = "Kjósa";
		this.isAdmin=false;
	}

	public BasicPollVoter(String resultPageUrl, String pollWidth, boolean isAdmin ){
		this.resultPageUrl = resultPageUrl;
		this.pollWidth = pollWidth;
		this.submitButtonText = "Kjósa";
		this.isAdmin=isAdmin;
	}

	public BasicPollVoter(String resultPageUrl, String pollWidth, String submitButtonText ){
		this.resultPageUrl = resultPageUrl;
		this.pollWidth = pollWidth;
		this.submitButtonText = submitButtonText;
		this.isAdmin=false;
	}
	public BasicPollVoter(String resultPageUrl, String pollWidth, String submitButtonText, boolean isAdmin ){
		this.resultPageUrl = resultPageUrl;
		this.pollWidth = pollWidth;
		this.submitButtonText = submitButtonText;
		this.isAdmin=isAdmin;
	}

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }


        public void setConnectionAttributes(String attributeName, int attributeId) {
            this.attributeName = attributeName;
            this.attributeId = attributeId;
        }


        public void setNumberOfShownPolls(int number_of_shown_polls) {
            this.number_of_shown_polls = number_of_shown_polls;
        }

        public void setSeparatorImage(Image separatorImage) {
                this.separatorImage = separatorImage;
        }

        public void setOtherPollsImage(Image otherPollsImage) {
                this.otherPollsImage = otherPollsImage;
        }

        public void setOtherPollsImage(String otherPollsImageUrl) {
          this.otherPollsImageUrl = otherPollsImageUrl;
        }

        public void showVotes(boolean show) {
                this.showVotes = show;
        }

        public void showTotalVotes(boolean show) {
                this.showTotalVotes = show;
        }

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public void setWidth(String pollWidth) {
		this.pollWidth = pollWidth;
	}

        public void setHeaderColor(String header_color) {
          this.header_color = header_color;
        }

        public void setStyle(String styleAttribute) {
          this.styleAttribute=styleAttribute;
        }

        public void setColor1(String color_1) {
          this.color_1 = color_1;
        }
        public void setColor2(String color_2) {
          this.color_2 = color_2;
        }


	public void setWidth(int pollWidth) {
		this.pollWidth = Integer.toString(pollWidth);
	}

	public void setSubmitButtonText(String submitButtonText) {
		this.submitButtonText = submitButtonText;
	}


	private boolean thisObjectSubmitted(ModuleInfo modinfo){
                String idega_poll_voter = modinfo.getParameter("idega_poll_voter");
                boolean returner = false;

		if (idega_poll_voter != null){
			if (idega_poll_voter.equals("true")){
				returner = true;
			}

		}
                return returner;
	}

        private boolean canVote(ModuleInfo modinfo) {
               Cookie[] cookies = (Cookie[]) modinfo.getRequest().getCookies();
               String URI = modinfo.getRequest().getRequestURI();
               boolean returner = true;

                if (cookies != null) {
                    if (cookies.length > 0) {
                      for (int i = 0 ; i < cookies.length ; i++) {
                        if ( cookies[i].getName().equals(URI+"idega_poll_"+poll_id) ) {
                          returner = false;
                          continue;
                        }
                      }
                    }
                }

               return returner;
        }


	private void handleInsert(ModuleInfo modinfo)throws SQLException,IOException{
               String URI = modinfo.getRequest().getRequestURI();
                HttpServletResponse response = modinfo.getResponse();
		boolean mayVote = true;
                String poll_option_id = modinfo.getRequest().getParameter("poll_option");
                String temp_poll_id = modinfo.getParameter("result_poll_id");

                PollResult[] result = (PollResult[])(new PollResult()).findAllByColumn("poll_option_id",poll_option_id);

//                PollOption option = new PollOption(Integer.parseInt(poll_option_id));
                poll = new Poll(Integer.parseInt(temp_poll_id));
                poll_id = poll.getID();


                if (canVote(modinfo)) {

                  if (poll_option_id != null)
  		  if (result != null)
		  {
                        if (result.length > 0) {
                          int previous_hits = result[0].getHits();
                          result[0].setHits(previous_hits +1);
                          result[0].update();
                        }
                        else {
                          PollResult result_kall = new PollResult();
                            result_kall.setHits(1);
                            result_kall.setOption(Integer.parseInt(poll_option_id));
                            result_kall.insert();
                        }

                        Cookie cookie = new Cookie(URI+"idega_poll_"+poll_id,"vote");
                        cookie.setMaxAge(20000);
                        response.addCookie(cookie);

     			//response.sendRedirect(resultPageUrl+"?result_poll_id="+poll_id);
		  }
                  /*else {
			response.sendRedirect(resultPageUrl+"?result_poll_id="+poll_id);
		  }*/
                }
    	        results(poll_id);
                //response.sendRedirect(resultPageUrl+"?result_poll_id="+poll_id);


	}

	private void noPollAvailable() throws IOException{


	}

	public void setTextColor(String header_text_color){
		this.header_text_color=header_text_color;
	}

	public void main(ModuleInfo modinfo)throws Exception{
          iwrb = getResourceBundle(modinfo);
          iwb = getBundle(modinfo);

          this.isAdmin=isAdministrator(modinfo);

            String temp_attribute_id = modinfo.getParameter("i_poll_attribute_id");
            String temp_attribute_name = modinfo.getParameter("i_poll_attribute_name");
            if ((temp_attribute_id != null) && (temp_attribute_name != null) ) {
                this.attributeId = Integer.parseInt(temp_attribute_id);
                this.attributeName = temp_attribute_name;
            }

            String poll_action = modinfo.getParameter("i_poll_action");

            if (poll_action == null)  {
                String result_poll_id = modinfo.getRequest().getParameter("result_poll_id");
                String temp_poll_id = modinfo.getRequest().getParameter("poll_id");

                if (thisObjectSubmitted(modinfo)) {
                    handleInsert(modinfo);
                }
                else {
                    if (result_poll_id == null) {
                        if ( temp_poll_id == null) {
                            showPoll(modinfo);
                        }
                        else {
                            displayPoll(modinfo,temp_poll_id);
                        }
                    }
                    else {
                  //      results(result_poll_id);
                    }
                }
            }
            else if (poll_action.equals("view_collection")) {
                viewCollection(modinfo);
            }



	}


  private void viewCollection(ModuleInfo modinfo) throws SQLException {
      PollAttributes[] poll_attrib = (PollAttributes[]) (new PollAttributes()).findAllByColumnDescendingOrdered("attribute_name",attributeName,"attribute_id",""+attributeId,"poll_id");
      String URI = modinfo.getRequest().getRequestURI();

      int first = 0;
      String first_string = modinfo.getParameter("i_poll_first");
      try {
          first = Integer.parseInt(first_string);
          if (first < 0 ) {
            first = 0;
          }
          else if (first > (poll_attrib.length -1)) {
              first = (poll_attrib.length -1);
          }
      }
      catch (NumberFormatException n) {}

      for (int i = first; i < poll_attrib.length; i++) {
          if (i == first + number_of_shown_polls) { break;}
          results(poll_attrib[i].getPollId());
          if (this.separatorImage != null) {
              add(separatorImage);
          }
      }


      Text nextText = new Text("Næstu "+number_of_shown_polls);
          nextText.setFontSize(1);
      Text prevText = new Text("Síðustu "+number_of_shown_polls);
          prevText.setFontSize(1);

      Link next = new Link(nextText,URI);
          next.addParameter("i_poll_action","view_collection");
          next.addParameter("i_poll_attribute_id",""+attributeId);
          next.addParameter("i_poll_attribute_name",""+attributeName);
          next.addParameter("i_poll_first",""+(first + number_of_shown_polls));
      Link prev = new Link(prevText,URI);
          prev.addParameter("i_poll_action","view_collection");
          prev.addParameter("i_poll_attribute_id",""+attributeId);
          prev.addParameter("i_poll_attribute_name",""+attributeName);
          prev.addParameter("i_poll_first",""+(first - number_of_shown_polls));


      Table table = new Table(3,1);
          table.setWidth("100%");
          table.setBorder(0);
          table.setAlignment(3,1,"right");
          if (! (first - number_of_shown_polls < 0)) {
              table.add(prev,1,1);
          }
          if (! (first + number_of_shown_polls >= poll_attrib.length)) {
              table.add(next,3,1);
          }


      add(table);
      add("<br>");
  }

  private void results(int poll_id) throws SQLException {
      results(String.valueOf(poll_id));
  }

  private void results(String poll_id) throws SQLException {

    String URI = getRequest().getRequestURI();

    PollOption[] option = (PollOption[]) (new PollOption()).findAllByColumn("poll_id",poll_id);
    Poll poll = new Poll(Integer.parseInt(poll_id));

    PollResult[] result;
    Vector hits = new Vector();
    Vector answers = new Vector();
    int total = 0;
    String font_face = "arial";

    if (option != null) {
      if (option.length > 0) {
        for (int i = 0 ; i < option.length ; i++ ) {
          answers.addElement(option[i].getAnswer());
          result = (PollResult[])(new PollResult()).findAllByColumn("poll_option_id",""+option[i].getID());
          if (result != null) {
            if (result.length > 0) {
              for (int j = 0 ; j < result.length ; j++) {
                hits.addElement(new Integer(result[j].getHits()));
                total += result[j].getHits();
              }
            }
            else {
              hits.addElement(new Integer(0));
            }
          } // if result != null endar
          else {
            hits.addElement(new Integer(0));
          }


        }
      }
    }

    int row = 1;
    int current_hits = 0;

    Table myTable = new Table();
      myTable.setBorder(0);
      if (color_1 != null) {
        myTable.setColor(color_1);
      }
      myTable.setWidth(1,1,"2");
      myTable.mergeCells(2,1,6,1);
//      myTable.setWidth(pollWidth);
      myTable.setWidth("100%");

      Text question = new Text(poll.getQuestion());
        question.setBold();
        question.setFontFace(font_face);
      myTable.add(question,2,1);

      if (!answers.isEmpty()) {
          for (int i = 0 ; i < answers.size() ; i++) {
            ++row;
            current_hits = (  (Integer) hits.elementAt(i) ).intValue();

            Text spurn = new  Text((String) answers.elementAt(i));
              spurn.setFontSize(1);
              question.setFontFace(font_face);
            Text percent;
            if (total > 0) {
              percent = new Text( Integer.toString( (current_hits *100)/(total)  )+"%");
            }
            else {
              percent = new Text("0%");
            }
            percent.setFontSize(1);
            question.setFontFace(font_face);

            myTable.add(spurn,2,row);
            if (showVotes) {
                Text fjoldi = new Text( Integer.toString(current_hits) );
                  fjoldi.setFontSize(1);
                  question.setFontFace(font_face);
                myTable.add(fjoldi,4,row);
            }
            myTable.add(percent,6,row);

            myTable.setWidth(1,row,"2");
            myTable.setWidth(2,row,""+500);
            myTable.setWidth(3,row,"2");
            myTable.setWidth(4,row,"2");
            myTable.setWidth(5,row,"2");
            myTable.setWidth(6,row,"2");
            myTable.setWidth(7,row,"2");

            myTable.setAlignment(4,row,"right");
            myTable.setAlignment(6,row,"right");
            }

      }


        if (header_color != null) {
          myTable.setRowColor(1,header_color);
        }

      add(myTable);



  }


        private void showPoll(ModuleInfo modinfo) throws SQLException, IOException{
                int manyQuestions = 0;
                int manyAnswers = 0;

                int windowHeightForCollection = 95;
                int windowHeight = 95;
                int questionHeight = 35;
                int answerHeight = 25;


                PollAttributes[] poll_attrib = (PollAttributes[]) (new PollAttributes()).findAllByColumnDescendingOrdered("attribute_name",attributeName,"attribute_id",""+attributeId,"poll_id");

                  PollOption[] tempAnswers;
                  for (int i = 0; i < poll_attrib.length; i++) {
                      if (i == number_of_shown_polls) {break;}
                      try {
                        manyQuestions++;
                        tempAnswers = (PollOption[]) (new PollOption()).findAllByColumn("poll_id",poll_attrib[i].getPollId());
                        manyAnswers += tempAnswers.length;
                      }
                      catch (Exception e) {}
                  }

                  windowHeightForCollection += (manyQuestions * questionHeight);
                  windowHeightForCollection += (manyAnswers * answerHeight);



                Poll[] polls = null;

                String sql_string = "Select * from poll where (";
                String sub_sql_string = "";
                if (poll_attrib != null) {
                  if (poll_attrib.length > 0 ) {
                    for (int i = 0 ; i < poll_attrib.length ; i++) {
                      sub_sql_string = sub_sql_string + " poll_id = " +poll_attrib[i].getPollId();
                      if (i != poll_attrib.length-1 ) {
                        sub_sql_string = sub_sql_string + " or";
                      }
                    }
                  }
                  if (!(sub_sql_string.equals(""))) {
                  sql_string = sql_string + sub_sql_string + ") AND (in_use ='Y')";
//                    System.err.println(sql_string);

                    polls = (Poll[]) (new Poll()).findAll(sql_string);
                  }
                }
//                System.err.println(sql_string);



                if (polls != null)
                  if (polls.length > 0 ) {



                  this.poll_id=polls[0].getID();
			if (this.poll == null){
				poll = new Poll(poll_id);
			}


			// Bætt inn af Ladda....

			Table contentTable = new Table(1,2);
				contentTable.setCellpadding(0);
				contentTable.setCellspacing(0);
				contentTable.setColor("#FFFFFF");

			Table outlineTable = new Table(1,1);
				outlineTable.setCellpadding(0);
				outlineTable.setCellspacing(1);
				outlineTable.setColor(header_color);

			Table headerTable = new Table(3,1);
				headerTable.setCellpadding(0);
				headerTable.setCellspacing(0);
				headerTable.setColor(header_color);
				headerTable.setWidth(1,"17");
				headerTable.setWidth(3,"17");
				headerTable.setWidth("100%");
				headerTable.setHeight(String.valueOf(headerSize));
				headerTable.setVerticalAlignment(1,1,"top");
				headerTable.setVerticalAlignment(2,1,"middle");
				headerTable.setVerticalAlignment(3,1,"top");
				headerTable.setAlignment(1,1,"left");
				headerTable.setAlignment(2,1,"center");
				headerTable.setAlignment(3,1,"right");

				if ( leftHeader ) {
					headerTable.add(new Image("/pics/jmodules/poll/leftcorner.gif",""),1,1);
				}

				String headerText = "Skoðanakönnun";
				Text header = new Text(headerText);

				if ( headerLeft ) {
					header = new Text("&nbsp;"+headerText);
					headerTable.empty();
					headerTable.mergeCells(1,1,2,1);
					headerTable.setWidth(1,"100%");
					headerTable.setVerticalAlignment(1,1,"middle");
					headerTable.add(header,1,1);
				}

				else {
					headerTable.add(header,2,1);
				}

					header.setBold();
					header.setFontColor(headlineColor);
					header.setFontSize(headlineSize);
					header.setFontFace(headerFontFace);


				if ( rightHeader ) {
					headerTable.add(new Image("/pics/jmodules/poll/rightcorner.gif",""),3,1);
				}

			// Hér lýkur innbætingu!

			table=new Table(2,3);
                                if (color_1 != null) {
                                  table.setColor(color_1);
                                }
                                table.setBorder(0);
                                /*if (header_color != null) {
                                  table.setRowColor(1,header_color);
                                  table.setRowColor(3,header_color);
                                }*/
				table.setCellpadding(2);
				table.setCellspacing(0);
				table.setWidth(pollWidth);
                                table.mergeCells(1,1,2,1);
                                table.mergeCells(1,2,2,2);
				table.setAlignment(1,1,"center");
                                table.setAlignment(2,3,"right");



			//form.add(table);

			// Bætt inn....
			outlineTable.add(table,1,1);
			contentTable.add(headerTable,1,1);
			contentTable.add(outlineTable,1,2);


			//....hingað....


			if (thisObjectSubmitted(modinfo)){
				handleInsert(modinfo);
			}
			else{
				Text spurning = new Text(poll.getQuestion());
					spurning.setBold();
					spurning.setFontSize(2);
					spurning.setFontColor(header_text_color);
				table.add(spurning,1,1);


				RadioGroup radioGroup = new RadioGroup(poll.findOptions());
                                  radioGroup.setAttribute("style",styleAttribute);

                                table.add(radioGroup,1,2);

				PollOption[] optionArray;
				optionArray = poll.findOptions();

                                windowHeight += (optionArray.length * answerHeight) + questionHeight;

                                if (showPollCollection) {
                                    Text otherText = null;

                                      if (this.otherPollsImage == null) {
                                       if(this.otherPollsImageUrl == null){
                                          otherText = new Text("Fyrri kannanir");
                                              otherText.setFontSize(1);
                                       }else{
                                         otherPollsImage = iwrb.getImage(otherPollsImageUrl);
                                       }
                                      }

                                      Window collectionWindow = new Window("Fyrri kannanir",280,windowHeightForCollection,this.resultPageUrl);
                                          collectionWindow.setResizable(false);
                                          collectionWindow.setToolbar(false);
                                          collectionWindow.setLocation(false);
                                          collectionWindow.setDirectories(false);
                                          collectionWindow.setStatus(false);
                                          collectionWindow.setScrollbar(false);
                                          collectionWindow.setMenubar(false);
                                          collectionWindow.setTitlebar(false);

                                      Link collection;
                                      if (otherPollsImage == null) {
                                        collection = new Link(otherText,collectionWindow);
                                      }else  {
                                        collection = new Link(otherPollsImage,collectionWindow);
                                      }
                                          collection.addParameter("i_poll_action","view_collection");
                                          collection.addParameter("i_poll_attribute_id",""+this.attributeId);
                                          collection.addParameter("i_poll_attribute_name",this.attributeName);

                                      //table.setVerticalAlignment(1,3,"bottom");
                                      table.add(collection,1,3);
                                }



                                gluggi = new Window("Nidurstodur",280,windowHeight,this.resultPageUrl);
                                        gluggi.setResizable(false);
                                        gluggi.setToolbar(false);
                                        gluggi.setLocation(false);
                                        gluggi.setDirectories(false);
                                        gluggi.setStatus(false);
                                        gluggi.setScrollbar(false);
                                        gluggi.setMenubar(false);
                                        gluggi.setTitlebar(false);

                                form = new Form(gluggi);

                                form.maintainAllParameters();
                                form.setMethod("post");
                                form.add(table);


				if ( submitButtonURL != null ) {
                                    /*
                                    Link theLink = new Link(new Image(submitButtonURL,""),gluggi);
                                        theLink.setToFormSubmit(form);
                                    table.add(theLink,2,3);
                                    */

                                    table.add(new SubmitButton(iwrb.getImage(submitButtonURL),"Kjósa"),2,3);
				}
				else {
					table.add(new SubmitButton(submitButtonText),2,3);
				}
                                //table.setVerticalAlignment(2,3,"middle");
				table.add(new Parameter("idega_poll_voter","true"));
				table.add(new Parameter("result_poll_id",""+poll.getID()));
			}



		}
                else {
                  this.noPollAvailable();
                }
		if (isAdmin) {
			Form adForm = new Form(new Window("Poll_Admin",600,400,pollAdminUrl));
				if ( adminButtonURL != null ) {
                                  adForm.add(new SubmitButton(iwrb.getImage(adminButtonURL)));
				}
                                else {
                                  adForm.add(new SubmitButton("ja","Pollstjóri"));
                                }
//				adForm.add(new HiddenInput(attributeName, ""+attributeId));
                                adForm.add(new HiddenInput("attribute_name",this.attributeName));
                                adForm.add(new HiddenInput("attribute_id",""+this.attributeId));
                                if (this.poll != null) {
                                  modinfo.getRequest().getSession().setAttribute("admin_poll_id",""+poll.getID());
//  				  adForm.add(new HiddenInput("admin_poll_id", ""+poll.getID()));
                                }
			add(adForm);
		}


		super.add(form);


        }


        private void displayPoll(ModuleInfo modinfo, String poll_id) throws SQLException, IOException{

                this.poll_id = Integer.parseInt(poll_id);

			if (this.poll == null){
				poll = new Poll(this.poll_id);
			}

			gluggi = new Window("Nidurstodur",445,450);
				gluggi.setResizable(true);
			form = new Form(gluggi);
			form.maintainAllParameters();
			form.setMethod("post");

			table=new Table(1,3);
                                if (color_1 != null) {
                                  table.setColor(color_1);
                                }
                                if (header_color != null) {
                                  table.setRowColor(1,header_color);
                                  table.setRowColor(3,header_color);
                                }
				table.setCellpadding(5);
				table.setCellspacing(0);
				table.setWidth(pollWidth);
				table.setAlignment(1,1,"center");
				table.setAlignment(1,3,"right");
			form.add(table);

			if (thisObjectSubmitted(modinfo)){
				handleInsert(modinfo);
			}
			else{
				Text spurning = new Text(poll.getQuestion());
					spurning.setBold();
					spurning.setFontSize(2);
				table.add(spurning,1,1);

				RadioGroup radioGroup = new RadioGroup(poll.findOptions());
                                  radioGroup.setAttribute("style",styleAttribute);

				table.add(radioGroup,1,2);

				PollOption[] optionArray;
				optionArray = poll.findOptions();

				table.add(new SubmitButton(submitButtonText),1,3);
				table.add(new Parameter("idega_poll_voter","true"));
			}

		if (isAdmin) {
			Form adForm = new Form(new Window("Poll_Admin",600,400,"/poll/pollAdmin.jsp"));
				adForm.add(new SubmitButton("ja","Pollstjóri"));
                                adForm.add(new HiddenInput("poll_id",poll_id));
                                adForm.add(new HiddenInput("attribute_name",this.attributeName));
                                adForm.add(new HiddenInput("attribute_id",""+this.attributeId));
                                if (this.poll != null) {
                                  modinfo.getRequest().getSession().setAttribute("admin_poll_id",""+this.poll.getID());
//  				  adForm.add(new HiddenInput("admin_poll_id", ""+poll.getID()));
                                }
			add(adForm);
		}

		super.add(form);


        }



	public void print(ModuleInfo modinfo)throws IOException{
		//try{
			super.print(modinfo);
		//}
		//catch(SQLException ex){
		//	throw new IOException(ex.getMessage());
		//}
	}

	public void setHeadlineColor(String headlineColor){
		this.headlineColor=headlineColor;
	}

	public void setHeadlineSize(int headlineSize){
		this.headlineSize=headlineSize;
	}

    public void setLeftHeader(boolean leftHeader){
		this.leftHeader=leftHeader;
	}

    public void setRightHeader(boolean rightHeader){
		this.rightHeader=rightHeader;
	}

    public void setHeadlineLeft(){
		this.headerLeft=true;

	}

    public void setSubmitButtonURL(String submitButtonURL){
		this.submitButtonURL=submitButtonURL;

	}

    public void setAdminButtonURL(String adminButtonURL){
        this.adminButtonURL=adminButtonURL;

    }

        public void setHeaderSize(int headerSize){
		this.headerSize=headerSize;

	}

        public void setHeaderFontFace(String headerFontFace){
		this.headerFontFace=headerFontFace;

	}

}
