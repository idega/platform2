package com.idega.block.calendar.presentation;


import java.util.GregorianCalendar;
import com.idega.util.*;
import com.idega.jmodule.*;
import com.idega.block.calendar.data.*;
import com.idega.data.*;
import java.io.*;
import java.sql.Timestamp;
import com.idega.jmodule.projectmanager.data.*;
import com.idega.jmodule.timesheet.data.TimesheetEntry;
import com.idega.jmodule.timesheet.data.Resource;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import javax.servlet.http.*;
import com.idega.util.idegaTimestamp;
import java.sql.SQLException;
import java.util.*;
import com.idega.util.text.*;

public class Calendar extends JModuleObject{
        boolean connect_to_timesheet = false;
        boolean is_announcement = false;
        boolean showNameOfMonth = true;


	private String URI;
        com.idega.data.genericentity.Member member = null;
        int member_id = -1;
	public int dombreyta;
	public int dagur;
	public int manudur;
	public int ar;
	int dagarman;
	public int vdagur=0;
	public String manudurnafn = "";
	public String userid;
	public String cellColor="";
	String typa;

	public GregorianCalendar calendar;
	public idegaCalendar FunctColl = new idegaCalendar();
	private boolean useHorizontal;


        private int max_shown;
	public String color_1;
	public String color_2;
	private String header_color;
        private String header_text_color;
        private String text_color;
	private String width;
	private String height;

	private String manudu;
	private String ar2;
	private String dom2;
	public int fontSize = 1;

        private String previous_image_url;
        private String next_image_url;
        private String today_image_url;

        private com.idega.jmodule.object.Image schedule_header_image = null;
        private String schedule_header_height = "22";

  private String language = "IS";
  private String[] LANG =  {"...more","Previous month","Next month","Back","Name","Description","Clear","Save","An error occured","Today"};
  private String[] IS = {"...meira","Fyrri Mánuður","Næsti Mánuður","Til Baka","Nafn","Lýsing","Hreinsa","Vista","Villa kom upp","Henda","Breyta","Í dag"};
  private String[] EN = {"...more","Previous month","Next month","Back","Name","Description","Clear","Save","An error occured","Delete","Modify","Today"};
  //                            0           1               2         3     4           5           6     7         8                9       10     11
  private String[] DK = {"...mere","Förre måned","Neste måned","Til bage","Navn","Beskrivelse","kleer","Save","Baa!! error","Smide","modify"};

	private boolean isAdmin = false;

private boolean leftHeader = true;
private boolean rightHeader = true;
private boolean headerLeft = false;
private int outlineWidth = 1;
private int headerFontSize = 2;
private int headerSize = 21;
private String headerFontFace = "Verdana, Arial, Helvetica, sans-serif";

  private CalendarEntry myCalendarEntry;


	public Calendar() {
		super();
                myCalendarEntry = new CalendarEntry();
		setDefaultValues();
	}


        /**
         * @deprecated replaced with the default constructor
         */
	public Calendar(boolean isAdmin) {
		super();
                myCalendarEntry = new CalendarEntry();
		setDefaultValues();
		//this.isAdmin = isAdmin;
	}

	public Calendar(String year, String month, String day) {
		super();
                myCalendarEntry = new CalendarEntry();
		setDefaultValues();
		manudu = month;
		dom2 = day;
		ar2 = year;
	}

	public Calendar(String year, String month, String day, boolean isAdmin) {
		super();
                myCalendarEntry = new CalendarEntry();
		setDefaultValues();
		manudu = month;
		dom2 = day;
		ar2 = year;
		this.isAdmin = isAdmin;
	}

	private void setDefaultValues() {
		typa = "Manudur";
		useHorizontal = true;
		header_color = "#4D6476";
		color_1 = "#EFEFEF";
		color_2 = "#ABABAB";
                text_color = "#000000";
                header_text_color = "#000000";
		width = "500";
		height = "550";
                max_shown = 10;
		isAdmin=false;
	}

      public void connectToTimesheet() {
        this.connect_to_timesheet = true;
      }

      public void showNameOfMonth(boolean show_name) {
        this.showNameOfMonth = show_name;
      }

        public void setScheduleHeaderImage(com.idega.jmodule.object.Image header_image) {
          this.schedule_header_image = header_image;
        }

        public void setScheduleHeaderHeight(String height) {
          this.schedule_header_height = height;
        }

        public void setNextImageUrl(String url) {
          this.next_image_url = url;
        }

        public void setTodayImageUrl(String url) {
          this.today_image_url = url;
        }

	public void setTextSize(int fontSize){
		this.fontSize=fontSize;
	}

      private void setSpokenLanguage(ModuleInfo modinfo){
        language = modinfo.getSpokenLanguage();
        if (language != null) {
        if (language.equalsIgnoreCase("IS")){
          LANG = IS;
        }else{
            LANG = EN;
        }
        }
        else {
            LANG = IS;
        }
      }


        public void setPreviousImageUrl(String url) {
          this.previous_image_url = url;
        }

        public void setMaxShown(int max_shown) {
          this.max_shown = max_shown;
        }

	public void setHeight(String height) {
		this.height = height;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setHeaderColor(String header_color) {
		this.header_color = header_color;
	}

	public void setHeaderFontSize(int headerFontSize) {
		this.headerFontSize = headerFontSize;
	}

        public void setTextColor(String text_color) {
          this.text_color = text_color;
        }

        public void setHeaderTextColor(String header_text_color) {
          this.header_text_color = header_text_color;
        }

	public void setHorizontalColors(String color_1, String color_2) {
		this.color_1 = color_1;
		this.color_2 = color_2;
		useHorizontal = true;
	}

	public void viewMonth() {
		typa = "Manudur";
	}
	public void viewDay() {
		typa = "Dagur";
	}
	public void viewWeek() {
		typa = "Vika";
	}
	public void viewYear() {
		typa = "Ar";
	}
	public void viewSchedule() {
		typa = "Schedule";
	}

	private String getDate(ModuleInfo modinfo) {
		String	temp_manudu = modinfo.getRequest().getParameter("idega_calendar_month");
		String	temp_ar2 = modinfo.getRequest().getParameter("idega_calendar_year");
		String	temp_dom2 = modinfo.getRequest().getParameter("idega_calendar_day");

		return temp_dom2 + " " + temp_manudu + " " + temp_ar2;
	}

        private Timestamp getTimestamp(ModuleInfo modinfo) {
		String	temp_manudu = modinfo.getRequest().getParameter("idega_calendar_month");
		String	temp_ar2 = modinfo.getRequest().getParameter("idega_calendar_year");
		String	temp_dom2 = modinfo.getRequest().getParameter("idega_calendar_day");

                Timestamp returner = null;
                if ((temp_manudu != null) && (temp_ar2 != null) && (temp_dom2 != null) ) {
                    idegaTimestamp stamp = new idegaTimestamp(Integer.parseInt(temp_ar2),Integer.parseInt(temp_manudu),Integer.parseInt(temp_dom2),0,0,0);
                    returner = stamp.getTimestamp();
                }

                return returner;
        }

        private void forwardDate(ModuleInfo modinfo,Form form) {
		String	temp_manudu = modinfo.getRequest().getParameter("idega_calendar_month");
		String	temp_ar2 = modinfo.getRequest().getParameter("idega_calendar_year");
		String	temp_dom2 = modinfo.getRequest().getParameter("idega_calendar_day");

                form.add(new HiddenInput("idega_calendar_month",temp_manudu));
                form.add(new HiddenInput("idega_calendar_year",temp_ar2));
                form.add(new HiddenInput("idega_calendar_day",temp_dom2));
        }


	private String getSQLDate(ModuleInfo modinfo) {
		String	temp_manudu = modinfo.getRequest().getParameter("idega_calendar_month");
		String	temp_ar2 = modinfo.getRequest().getParameter("idega_calendar_year");
		String	temp_dom2 = modinfo.getRequest().getParameter("idega_calendar_day");

		return temp_ar2 + "-" + temp_manudu + "-" + dom2;
	}


	private void Calculate(ModuleInfo modinfo) {
		String	temp_manudu = modinfo.getRequest().getParameter("idega_calendar_month");
		String	temp_ar2 = modinfo.getRequest().getParameter("idega_calendar_year");
		String	temp_dom2 = modinfo.getRequest().getParameter("idega_calendar_day");

		if (( temp_manudu != null) && (temp_ar2 != null) && (temp_dom2 != null))  {
			manudu = temp_manudu;
			ar2 = temp_ar2;
			dom2 = temp_dom2;
		}

		String temp_typa = modinfo.getRequest().getParameter("idega_calendar_skoda");
		if (temp_typa != null) {
			typa = temp_typa;
		}


		if (( manudu == null) || (ar2 == null) || (dom2 == null))  {

			calendar = new GregorianCalendar();

			dombreyta = FunctColl.getDay();
			manudur = FunctColl.getMonth();
			ar = FunctColl.getYear();

			dom2 = Integer.toString(dombreyta);
			ar2 = Integer.toString(ar);
			manudu = Integer.toString(manudur);

			calendar = new GregorianCalendar(ar,(manudur-1),1);
			if (typa.equals("Manudur")) {
				vdagur = (calendar.get(calendar.DAY_OF_WEEK));
			}
			else {
				vdagur = (calendar.get(calendar.DAY_OF_WEEK)+2);
			}

		}
		else {
		 	manudur = Integer.parseInt(manudu);
			ar = Integer.parseInt(ar2);
			if (typa.equals("Manudur")) {
				dombreyta = 1;
			}
			else {
				dombreyta = Integer.parseInt(dom2);
			}
			calendar = new GregorianCalendar(ar,(manudur-1),dombreyta);

			vdagur = (calendar.get(calendar.DAY_OF_WEEK));
		}

		if (typa.equals("Vika") || typa.equals("Manudur") ) {
			dombreyta = dombreyta - (vdagur -1);
		}

		if (typa.equals("Vika")) {
			if (dombreyta < 1) {
				dombreyta= dombreyta+ FunctColl.getLengthOfMonth( (manudur-1),ar);
				manudur= manudur -1;
				if (manudur == 0) {
					manudur=12;
				ar=ar-1;
				}
			}
		}

		switch (manudur)  {
			case 0 :
					manudur=12;
					ar= ar-1;
				break;

			case 13 :
					manudur= 1;
					ar= ar+1;
				break;
		}

		dagarman = (FunctColl.getLengthOfMonth(manudur,ar));
		manudurnafn =(FunctColl.getNameOfMonth(manudur));


		if (vdagur > 7) {
			vdagur = vdagur % 7;
		}
	}

	private void schedule(ModuleInfo modinfo) throws SQLException {

		int dag = (FunctColl.getDay()-1);
		int man = (FunctColl.getMonth());
		int ar = FunctColl.getYear();

		if (dag == 0) {
		  --man;
		  if (man == 0) {
			--ar;
			man = 12;
		  }
		  dag= FunctColl.getLengthOfMonth(man,ar);
		}


		String idag = dag+"."+man+"."+ar;
			Link linkur;
			int lengd = 10;
			int row = 0;
			int fjoldi_lina = max_shown;

		Table back = new Table(1,2);
			back.setColor(header_color);
			back.setBorder(0);
			back.setCellspacing(0);
			back.setCellpadding(0);
			back.setWidth(width);

		Table outlineTable = new Table(1,1);
			outlineTable.setColor(header_color);
			outlineTable.setCellpadding(0);
			outlineTable.setCellspacing(outlineWidth);
			outlineTable.setWidth("100%");

		Table table = new Table();
			table.setWidth("100%");
			/*if (!(height.equals(""))) {
				table.setHeight(height);
			}*/
			table.setColor(header_color);
			table.setBorder(0);
			table.setCellpadding(2);
			table.setCellspacing(0);

                if (schedule_header_image != null) {
					back.setBackgroundImage(schedule_header_image);
					back.setHeight(1,1,schedule_header_height);
                }

                else {

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
							headerTable.add(new Image("/pics/jmodules/boxoffice/leftcorner.gif",""),1,1);
						}

						Text header = new Text("Á næstunni");

						if ( headerLeft ) {
							header = new Text("&nbsp;Á næstunni");
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
							header.setFontColor(header_text_color);
							header.setFontSize(headerFontSize);
							header.setFontFace(headerFontFace);

						if ( rightHeader ) {
							headerTable.add(new Image("/pics/jmodules/boxoffice/rightcorner.gif",""),3,1);
						}

                      back.add(headerTable,1,1);
                }


        	CalendarEntry[] entry = (CalendarEntry[]) (new CalendarEntry()).findAll("select * from "+myCalendarEntry.getEntityName()+" where entry_date>'"+idag+"' and member_id = -1 order by entry_date");

                if (entry.length < max_shown ) {
                  lengd = entry.length;
                }

                else { lengd = max_shown; }

                idegaTimestamp stamp;

                for (int i = 0 ; i < lengd ; i++ ) {
                  ++row;
                    //table.setHeight(row,"22");
                    table.setAlignment(1,row,"center");
                    table.setVerticalAlignment(1,row,"top");
                    stamp = new idegaTimestamp(entry[i].getDate());
                    Text dags = new Text(stamp.getDate()+"/"+stamp.getMonth()+"/"+String.valueOf(stamp.getYear()).substring(2,4));
                      dags.setFontSize(1);
                    table.add(dags,1,row);
    		    Text fundur = new Text(entry[i].getName());
    		    	fundur.setFontSize(fontSize);
    		    	fundur.setFontFace("Verdana,Arial");
		    linkur = new Link(fundur,"/calendar.jsp");
		    linkur.addParameter("idega_calendar_action","view");
		    linkur.addParameter("idega_calendar_faerslur_id",entry[i].getID());
		    linkur.setAttribute("style","text-decoration: none");
	 	    table.add(linkur,2,row);
                }

                if ( entry.length == 0 ) {
					table.setAlignment(1,1,"center");
					table.addText("Ekkert á döfinni");
				}


        	table.setHorizontalZebraColored(color_1, color_2);

        outlineTable.add(table);
        back.add(outlineTable,1,2);

		add(back);
	}




	private Table year(ModuleInfo modinfo) throws SQLException {

		Table table = new Table();
			table.setBorder(0);
			table.setCellspacing(1);
			table.setColor("#000000");


		width = "0";
		height = "0";
		manudur = 0;

		table.add(""+ar);
			table.mergeCells(1,1,3,1);
			table.setAlignment(1,1,"center");

		++manudur;
			table.add(FunctColl.getNameOfMonth(manudur),1,2);
			table.add(month(modinfo,true),1,2);
			table.setVerticalAlignment(1,2,"top");
		++manudur;
			table.add(FunctColl.getNameOfMonth(manudur),2,2);
			table.add(month(modinfo,true),2,2);
			table.setVerticalAlignment(2,2,"top");
		++manudur;
			table.add(FunctColl.getNameOfMonth(manudur),3,2);
			table.add(month(modinfo,true),3,2);
			table.setVerticalAlignment(3,2,"top");
		++manudur;
			table.add(FunctColl.getNameOfMonth(manudur),1,3);
			table.add(month(modinfo,true),1,3);
			table.setVerticalAlignment(1,3,"top");
		++manudur;
			table.add(FunctColl.getNameOfMonth(manudur),2,3);
			table.add(month(modinfo,true),2,3);
			table.setVerticalAlignment(2,3,"top");
		++manudur;
			table.add(FunctColl.getNameOfMonth(manudur),3,3);
			table.add(month(modinfo,true),3,3);
			table.setVerticalAlignment(3,3,"top");
		++manudur;
			table.add(FunctColl.getNameOfMonth(manudur),1,4);
			table.add(month(modinfo,true),1,4);
			table.setVerticalAlignment(1,4,"top");
		++manudur;
			table.add(FunctColl.getNameOfMonth(manudur),2,4);
			table.add(month(modinfo,true),2,4);
			table.setVerticalAlignment(2,4,"top");
		++manudur;
			table.add(FunctColl.getNameOfMonth(manudur),3,4);
			table.add(month(modinfo,true),3,4);
			table.setVerticalAlignment(3,4,"top");
		++manudur;
			table.add(FunctColl.getNameOfMonth(manudur),1,5);
			table.add(month(modinfo,true),1,5);
			table.setVerticalAlignment(1,5,"top");
		++manudur;
			table.add(FunctColl.getNameOfMonth(manudur),2,5);
			table.add(month(modinfo,true),2,5);
			table.setVerticalAlignment(2,5,"top");
		++manudur;
			table.add(FunctColl.getNameOfMonth(manudur),3,5);
			table.add(month(modinfo,true),3,5);
			table.setVerticalAlignment(3,5,"top");

		return table;
	}


	private void addLinks() {
		Paragraph myPar = new Paragraph();
			myPar.setAlign("left");

		Link dayLink = new Link("Dagur",URI);
			dayLink.addParameter("idega_calendar_skoda","Dagur");
			dayLink.addParameter("idega_calendar_month",manudu);
			dayLink.addParameter("idega_calendar_day",dom2);
			dayLink.addParameter("idega_calendar_year",ar2);
			dayLink.addParameter("action","calendar");

		Link weekLink = new Link("Vika",URI);
			weekLink.addParameter("idega_calendar_skoda","Vika");
			weekLink.addParameter("idega_calendar_month",manudu);
			weekLink.addParameter("idega_calendar_day",dom2);
			weekLink.addParameter("idega_calendar_year",ar2);
			weekLink.addParameter("action","calendar");

		Link monthLink = new Link("Mánuður",URI);
			monthLink.addParameter("idega_calendar_skoda","Manudur");
			monthLink.addParameter("idega_calendar_month",manudu);
			monthLink.addParameter("idega_calendar_day",dom2);
			monthLink.addParameter("idega_calendar_year",ar2);
			monthLink.addParameter("action","calendar");

		myPar.add(dayLink);
		myPar.add(weekLink);
		myPar.add(monthLink);

		add(myPar);
	}

	private void addMenu() {
		Paragraph myPar1 = new Paragraph();
			myPar1.setAlign("right");

		Link dayLink = new Link("Tímaskráning",URI);
                        dayLink.addParameter("idega_calendar_action","new_entry");
//			dayLink.addParameter("idega_calendar_skoda","Dagur");
			dayLink.addParameter("idega_calendar_month",manudu);
			dayLink.addParameter("idega_calendar_day",dom2);
			dayLink.addParameter("idega_calendar_year",ar2);
			dayLink.addParameter("action","calendar");

		Link weekLink = new Link("Tilkynning",URI);
                        weekLink.addParameter("idega_calendar_action","idega_calendar_new");
//			weekLink.addParameter("idega_calendar_skoda","Vika");
			weekLink.addParameter("idega_calendar_month",manudu);
			weekLink.addParameter("idega_calendar_day",dom2);
			weekLink.addParameter("idega_calendar_year",ar2);
			weekLink.addParameter("action","calendar");


		myPar1.add(dayLink);
                myPar1.add("&nbsp;&nbsp;");
		myPar1.add(weekLink);

		add(myPar1);
	}


	private void day(ModuleInfo modinfo) throws SQLException, IOException{
		if (dombreyta-1== FunctColl.getLengthOfMonth(manudur, ar))  {
			dombreyta=1;
			++manudur;
			if (manudur==13) {
				manudur=1;
				++ar;
			}
		}

		if (dombreyta < 1) {
			--manudur;
			if (manudur == 0) {
				manudur=12;
				--ar;
			}
			dombreyta = FunctColl.getLengthOfMonth(manudur,ar) + dombreyta;
		}



/*		ResultSet RS_2;
		Statement Stmt_2 = Conn.createStatement();
		RS_2 = Stmt_2.executeQuery("Select nafn from fordi where forda_id='"+userid+"' ");

//			Image myHeader = new Image("http://clarke.idega.is/timaskra2.swt?type=jpg&title="+FunctColl.getNameOfDay(vdagur)+" "+dombreyta+"&subtitle="+RS_2.getString("nafn")+" ");
			Image myHeader = new Image("http://clarke.idega.is/timaskra2.swt?type=jpg&title="+FunctColl.getNameOfDay(vdagur).substring(0,3)+" "+dombreyta+". "+FunctColl.getShortNameOfMonth(manudur)+"&subtitle="+RS_2.getString("nafn")+" ");

		RS_2.close();
		Stmt_2.close();
*/


//	myPage.add(myHeader);



//		myPar.add(new Link("Verkbók","timaskra.jsp?dagur="+dombreyta+"&manudur="+manudur+"&ar="+ar+" "));
/*		setLinkOfImage("Verkbok","timaskra.jsp?dagur="+dombreyta+"&manudur="+manudur+"&ar="+ar);
		setLinkOfImage("MinVerk","timaskra_save.jsp?edit=plusverk&dagur="+dombreyta+"&manudur="+manudur+"&ar="+ar);
		setLinkOfImage("Skyrslur","timaskra_save.jsp?edit=undirskyrsla&manudur="+manudur+"&ar="+ar);
		setLinkOfImage("Bokun","timaskra_save.jsp?edit=book&manudur="+manudur+"&ar="+ar);
//		setLinkOfImage("Bokun","timaskra_save.jsp?edit=book");
		setLinkOfImage("Dagatal","jcalendarMeeting.jsp?ar="+ar+"&daym="+dombreyta+"&month="+manudur);
*/
		addLinks();

//                addMenu();


//		myPar.add(new Link("Dagur","jcalendarMeeting.jsp?skoda=Dagur&daym="+dombreyta+"&month="+manudur+"&ar="+ar));
//		myPar.add(new Link("Vika","jcalendarMeeting.jsp?skoda=Vika&daym="+dombreyta+"&month="+manudur+"&ar="+ar));
//		myPar.add(new Link("Mánuður","jcalendarMeeting.jsp?skoda=Manudur&daym="+dombreyta+"&month="+manudur+"&ar="+ar));



//		myPage.addText(FunctColl.getNameOfDay(vdagur)+"&nbsp;&nbsp;&nbsp;");
//		myPage.addText(dombreyta+" "+FunctColl.getNameOfMonth(manudur)+" "+ar);

		boolean svara=false;
		double i;

			//	out.println("<a href=\"timaskra.jsp?dagur="+dombreyta+"&manudur="+manudur+"&ar="+ar+"\">Hitt</a><font size=\"2\" color=#444444><br>");


/*		myPage.setTextColor("#000000");
		myPage.setLinkColor("#000000");
 		myPage.setVlinkColor("#000000");
		myPage.setAlinkColor("#000000");
*/
/*		Table myTable2 = new Table(3,1);
			myTable2.setWidth("75%");
			myTable2.setCellspacing(0);
			myTable2.setBorder(1);
			myTable2.setHeight("75");
		add(myTable2);
*/

		Table myTable = new Table(4,35);
//		Table myTable = new Table(4,33);
			myTable.setWidth(width);
			if (!(height.equals(""))) {
				myTable.setHeight(height);
			}
//			myTable.setColor(this.color_1);
			myTable.setBorder(0);
			myTable.setCellpadding(0);
			myTable.setCellspacing(0);
			myTable.setColumnAttribute(1,"width","100");
//			myTable.setColumnAttribute(2,"width","500");
//			myTable.setColumnAttribute(3,"width","79");
			myTable.setColumnAttribute(4,"width","185");


/*		myTable2.setWidth(1,"380");myTable2.setBackgroundImage(1,1,myHeader);
//		myTable2.setWidth(1,"380");myTable2.setAttribute(1,1,"background","myndir/HeaderV.gif");
		Image myImage12;
//		myImage12 = new Image("myndir/HeaderM.gif");
//		myTable2.setBackgroundImage(2,1,myImage12);
		myTable2.setAttribute(2,1,"background","/pics/timereg/HeaderM.gif");
		myTable2.setWidth(3,"67");myTable2.setAttribute(3,1,"background","/pics/timereg/HeaderH.gif");
*/
//		myTable.setHeight(1,"75");
//		myTable.setHeight(19,"76");

//		myTable.mergeCells(2,9,6,9);

		add(myTable);

		int timaTeljari=0;

		int jz=0;
		int i_timi= 0;
		int row=1;

                Text header = new Text(this.getDate(modinfo));
                  header.setFontColor(this.header_text_color);
                  header.setBold();

                myTable.mergeCells(1,1,4,1);
                myTable.add(header,1,1);
                myTable.setAlignment(1,1,"center");

                Link link;

		for ( i=1;i<=17;i = i+0.5) {
			++row;
			if ((i%1) == 0) {
			++i_timi;
			}

			int timi=(i_timi+7);
/*			if ((timi%2) != 0) {
				cellColor="#ABABAB";
			}
			else {
				cellColor="#EFEFEF";
			}
*/
//			myTable.setRowColor(row,cellColor);
			myTable.mergeCells(2,row,4,row);
			Image myImage = new Image("/pics/timereg/hn_plus.gif","Bæta við");
				myImage.setAttribute("align","right");
				myImage.setBorder(0);
                        Link myLink = new Link(myImage,URI);
			if (timi != 24) {
//                                myLink.addParameter("edit","nytt");
                                myLink.addParameter("idega_calendar_action","new_entry");
                                myLink.addParameter("timi",""+(i+7));
                                myLink.addParameter("idega_calendar_day",dombreyta);
                                myLink.addParameter("idega_calendar_month",manudur);
                                myLink.addParameter("idega_calendar_year",ar);

                                myTable.add(myLink,1,row);
/*				myTable.add(new Link(myImage,"jcalendarMeeting_admin.jsp?
                                edit=nytt&
                                timi="+(i+7)+"&
                                daym="+dombreyta+"
                                &month="+manudur+"&
                                ar="+ar)
                                ,1,row);
*/
                        }
			if ((i % 1) == 0) {
				myTable.addText("&nbsp&nbsp"+(timi)+":00&nbsp;&nbsp;",1,row);
			}
			else {
				myTable.addText("&nbsp&nbsp"+(timi)+":30&nbsp;&nbsp;",1,row);
			}

			myTable.setVerticalAlignment(1,row,"top");
/*
			Statement Stmt = Conn.createStatement();
			ResultSet RS;
//			RS = Stmt.executeQuery("Select * from MeetingCalendar where meeting_date='"+ar+"-"+manudur+"-"+dombreyta+"' AND HOUR(timefrom)<='"+(timi)+"' AND HOUR(timeto)>'"+(timi+1)+"' AND valid='Y' ");
//			RS = Stmt.executeQuery("Select * from faerslur,verk where date='"+ar+"-"+manudur+"-"+dombreyta+"' AND owner_id="+userid+" AND verk.verk_id=faerslur.verk_id  ");
//			RS = Stmt.executeQuery("Select * from faerslur,faerslur_extra,verk where faerslur.faerslu_id = faerslur_extra.faerslu_id AND fra<='"+(timi)+"' AND til>='"+(timi+1)+"'  AND date='"+ar+"-"+manudur+"-"+dombreyta+"' AND owner_id="+userid+" AND verk.verk_id=faerslur.verk_id  ");
			RS = Stmt.executeQuery("Select * from faerslur, faerslur_extra,verk where faerslur.faerslu_id=faerslur_extra.faerslu_id AND fra<='"+(i+7)+"' AND til>='"+(i+7+0.5)+"' AND owner_id='"+userid+"' AND verk.verk_id = faerslur.verk_id AND date='"+ar+"-"+manudur+"-"+dombreyta+"' ");

			svara=false;
*/
//                        myTable.add("Select * from calendar_entry where time_from<='"+(i+7)+"' AND time_to >='"+(i+7+0.5)+"' AND member_id ='"+this.member_id+"' AND entry_date ='"+this.ar+"-"+this.manudur+"-"+this.dombreyta+"'",2,row);
                        CalendarEntry[] entry = (CalendarEntry[]) new CalendarEntry().findAll("Select * from "+myCalendarEntry.getEntityName()+" where time_from<='"+(i+7)+"' AND time_to >='"+(i+7+0.5)+"' AND member_id ='"+this.member_id+"' AND entry_date ='"+this.ar+"-"+this.manudur+"-"+this.dombreyta+"'");

                        if (entry != null) {
                          if (entry.length > 0) {
                            for (int y = 0 ; y < entry.length ; y++) {
                                link = new Link(entry[y].getName(),URI);
                                link.addParameter("idega_calendar_action","view");
                                link.addParameter("idega_calendar_faerslur_id",entry[y].getID());
                              if (y > 0)
                                myTable.add("<br>",2,row);
                              myTable.add(link,2,row);
                            }
                          }
                        }

/*
                while (RS.next()) {
				myTable.add(new Link(RS.getString("verk_nafn"),"jcalendarMeeting_admin.jsp?edit=skoda&daym="+dombreyta+"&month="+manudur+"&ar="+ar+"&faerslu_id="+RS.getString("faerslu_id")),2,row);
//				myTable.add(new Link(RS.getString("verk_nafn"),"jcalendarMeeting_admin.jsp?edit=skoda&faerslu_id="+RS.getString("faerslu_id")+" "),2,i);
			}

			RS.close();
			Stmt.close();
*/

		} //For setningin endar ...

/*		ResultSet RS2;
		Statement Stmt2 = Conn.createStatement();
		ResultSet RS3;
		Statement Stmt3 = Conn.createStatement();
		RS3 = Stmt3.executeQuery("SELECT timafjoldi FROM faerslur WHERE date='"+ar+"-"+manudur+"-"+dombreyta+"' AND owner_id='"+userid+"' ");
		while (RS3.next()) {
			timaTeljari = timaTeljari + RS3.getInt("timafjoldi");
		 }
		RS3.close();
		Stmt3.close();

		RS2 = Stmt2.executeQuery("SELECT timafjoldi FROM faerslur,faerslur_extra WHERE faerslur.faerslu_id=faerslur_extra.faerslu_id AND date='"+ar+"-"+manudur+"-"+dombreyta+"' AND owner_id='"+userid+"' ");
		while (RS2.next()) {
			timaTeljari = timaTeljari - RS2.getInt("timafjoldi");
		 }
		if (timaTeljari > 0 ) {
			myTable.setAlignment(4,17,"right");
			myTable.addText("Aðrir tímar : "+Integer.toString(timaTeljari)+"&nbsp;&nbsp;&nbsp;",4,17);
		}
		RS2.close();
		Stmt2.close();
*/

/*
		Table myTable3 = new Table(3,1);
			myTable3.setCellspacing(0);
			myTable3.setBorder(1);
			myTable3.setWidth("75%");
			myTable3.setHeight("74");
			myTable3.setWidth(1,"72");
			myTable3.setWidth(3,"72");
/*		myTable3.setAttribute(1,1,"background","/pics/timereg/FooterV.gif");
		myTable3.setAttribute(2,1,"background","/pics/timereg/FooterTiler.gif");
		myTable3.setAttribute(3,1,"background","/pics/timereg/FooterH.gif");

		Image OrVUpp = new Image("/pics/timereg/Or-V-upp.gif","Fyrri dagur");
		Image OrHUpp = new Image("/pics/timereg/Or-H-upp.gif","Næsti dagur");
		myTable3.setVerticalAlignment(2,1,"bottom");

//		myTable3.add(new Link(OrVUpp,"jcalendarMeeting.jsp?skoda=Dagur&month="+(manudur)+"&daym="+(dombreyta-1)+"&ar="+ar),2,1);
//		myTable3.add(new Link(OrHUpp,"jcalendarMeeting.jsp?skoda=Dagur&month="+(manudur)+"&daym="+(dombreyta+1)+"&ar="+ar),2,1);
		myTable3.add(new Link("Til baka","jcalendarMeeting.jsp?skoda=Dagur&month="+(manudur)+"&daym="+(dombreyta-1)+"&ar="+ar),2,1);
		myTable3.add(new Link("Áfram","jcalendarMeeting.jsp?skoda=Dagur&month="+(manudur)+"&daym="+(dombreyta+1)+"&ar="+ar),2,1);



		add(myTable3);

*/

//			add(new Link("Fyrri dagur","jcalendarMeeting.jsp?skoda=Dagur&daym="+(dombreyta-1)+"&month="+manudur+"&ar="+ar+"  "));
//			add(new Link("Næsti dagur","jcalendarMeeting.jsp?skoda=Dagur&daym="+(dombreyta+1)+"&month="+manudur+"&ar="+ar+"  "));

//	myPage.print(modinfo);

          	myTable.setHorizontalZebraColored(color_1, color_2);
                myTable.setRowColor(1,header_color);
                myTable.setRowColor(row+1,header_color);
                myTable.mergeCells(1,row+1,4,row+1);

}		//Dagur endar




	private void week(ModuleInfo modinfo) throws SQLException{

		//add(getDate());
//public void skodaVika(ModuleInfo modinfo) throws SQLException, IOException {

//	Page myPage = new Page("i d e g a");

/*	myPage.setTextColor("#000000");
	myPage.setLinkColor("#000000");
	myPage.setVlinkColor("#000000");
	myPage.setAlinkColor("#000000");
*/
	Paragraph myPar = new Paragraph();
		myPar.setAlign("left");
		add(myPar);

//	myPar.add(new Link("Verkbók","timaskra.jsp?dagur="+dombreyta+"&manudur="+manudur+"&ar="+ar+" "));


/*	setLinkOfImage("Verkbok","timaskra.jsp?dagur="+dombreyta+"&manudur="+manudur+"&ar="+ar);
	setLinkOfImage("MinVerk","timaskra_save.jsp?edit=plusverk&dagur="+dombreyta+"&manudur="+manudur+"&ar="+ar);
	setLinkOfImage("Skyrslur","timaskra_save.jsp?edit=undirskyrsla&manudur="+manudur+"&ar="+ar);
	setLinkOfImage("Bokun","timaskra_save.jsp?edit=book&manudur="+manudur+"&ar="+ar);
//	setLinkOfImage("Bokun","timaskra_save.jsp?edit=book");
	setLinkOfImage("Dagatal","jcalendarMeeting.jsp?ar="+ar+"&daym="+dombreyta+"&month="+manudur);
*/

//	addLinks();
/*	Link dayLink = new Link("Dagur",URI);
		dayLink.addParameter("idega_calendar_skoda","Dagur");
		dayLink.addParameter("idega_calendar_month",manudu);
		dayLink.addParameter("idega_calendar_day",dom2);
		dayLink.addParameter("idega_calendar_year",ar2);

	Link weekLink = new Link("Vika",URI);
		weekLink.addParameter("idega_calendar_skoda","Vika");
		weekLink.addParameter("idega_calendar_month",manudu);
		weekLink.addParameter("idega_calendar_day",dom2);
		weekLink.addParameter("idega_calendar_year",ar2);

	Link monthLink = new Link("Mánuður",URI);
		monthLink.addParameter("idega_calendar_skoda","Manudur");
		monthLink.addParameter("idega_calendar_month",manudu);
		monthLink.addParameter("idega_calendar_day",dom2);
		monthLink.addParameter("idega_calendar_year",ar2);

	myPar.add(dayLink);
	myPar.add(weekLink);
	myPar.add(monthLink);
*/

//	myPar.add(new Link("Dagur","jcalendarMeeting.jsp?skoda=Dagur&daym="+dombreyta+"&month="+manudur+"&ar="+ar));
//	myPar.add(new Link("Vika","jcalendarMeeting.jsp?skoda=Vika&daym="+dombreyta+"&month="+manudur+"&ar="+ar));
//	myPar.add(new Link("Mánuður","jcalendarMeeting.jsp?skoda=Manudur&daym="+dombreyta+"&month="+manudur+"&ar="+ar));


/*		ResultSet RS_2;
		Statement Stmt_2 = Conn.createStatement();
		RS_2 = Stmt_2.executeQuery("Select nafn from fordi where forda_id='"+userid+"' ");

			Image myHeader = new Image("http://clarke.idega.is/timaskra2.swt?type=jpg&title=Vika "+FunctColl.getWeekOfYear(ar,manudur,dagur)+" "+ar+"&subtitle="+RS_2.getString("nafn")+" ");

		RS_2.close();
		Stmt_2.close();
*/
//	Image myHeader = new Image("http://clarke.idega.is/timaskra.swt?type=jpg&title="+manudurnafn+" "+ar+" ");


//	myPage.addText(manudurnafn+"  "+ar);


	String dagurviku;
    String s;

	int dagarmanaundan = FunctColl.getLengthOfMonth(manudur-1, ar);
	manudurnafn = FunctColl.getNameOfMonth(manudur);
	String haed="15";
	String breidd = "75";


	if (dombreyta<1) {
		vdagur= vdagur+(dombreyta-1);
		dombreyta = dagarmanaundan + dombreyta;
	}


/*	Table myTable2 = new Table(3,1);
		add(myTable2);
			myTable2.setWidth("75%");
			myTable2.setBorder(1);
			myTable2.setCellspacing(0);
			myTable2.setHeight("75");
			myTable2.setWidth(1,"400");
//			myTable2.setWidth(1,);
			myTable2.setWidth(3,"67");
//			myTable2.setAttribute(1,1,"background","/pics/timereg/HeaderV.gif");
			myTable2.setBackgroundImage(1,1,myHeader);
			myTable2.setAttribute(2,1,"background","/pics/timereg/HeaderM.gif");
			myTable2.setAttribute(3,1,"background","/pics/timereg/HeaderH.gif");
*/




	Table myTable = new Table(8,34);
			myTable.setWidth(width);
			if (!(height.equals(""))) {
				myTable.setHeight(height);
			}
		myTable.setBorder(0);
		myTable.setCellpadding(2);
		myTable.setCellspacing(1);
		myTable.setColor("#000000");
//		myTable.setWidth("75%");
		myTable.setHorizontalZebraColored(color_1,color_2);

/*		myTable.setHeight(1,"75");

		myTable.mergeCells(1,1,5,1);
		myTable.mergeCells(6,1,7,1);
			myTable.setAttribute(1,1,"background","HeaderV.gif");
			myTable.setAttribute(6,1,"background","HeaderM.gif");
			myTable.setAttribute(8,1,"background","HeaderH.gif");


		myTable.setHeight(20,"80");

		myTable.mergeCells(2,20,7,20);

		myTable.setAttribute(1,20,"background","FooterHornV.gif");
		myTable.setAttribute(2,20,"background","FooterTiler.gif");
		myTable.setAttribute(8,20,"background","FooterHornH.gif");
*/

	add(myTable);
//	myTable.addText("gimmi steri",10,20);

        CalendarEntry[] entry;
        String short_name_of_month = "";

	int row=1;
	int g_timi=7;
	int talan_min=7;
 	for (double g=1; g<=17;g =g+0.5) {
		++row;
                myTable.setVerticalAlignment(1,row,"top");
		if ((g%1)==0) {
			++g_timi;
		}
		myTable.setHeight(row,haed);
		if (g == 17) {
//			myTable.addText("&nbsp&nbsp00:00",1,g+1);
			myTable.addText("&nbsp&nbsp00:00",1,row);
		}
		else {
			if ((row+6) < 12) {
				if ((g%1) == 0) {
				myTable.addText("&nbsp&nbsp0"+(g_timi)+":00",1,row);
				}
				else {
				myTable.addText("&nbsp&nbsp0"+(g_timi)+":30",1,row);
				}
//				myTable.addText("&nbsp&nbsp0"+(g+7)+":00",1,g+1);
			}
		 	else {
				if ((g%1) == 0) {
					myTable.addText("&nbsp&nbsp"+(g_timi)+":00",1,row);
				}
				else {
					myTable.addText("&nbsp&nbsp"+(g_timi)+":30",1,row);
				}
//				myTable.addText("&nbsp&nbsp"+(g+7)+":00",1,g+1);
			}
		}
	}

		Link minnHlekkur;
                Text myText;
                Link link;
		for(int j=1; j<=7; j++){
			if (dombreyta > dagarman)  {
				dombreyta=1;
				++manudur;
			}
			if (manudur==13) {
				manudur = 1;
				++ar;
			}


			myTable.setAttribute(j+1,1,"align","center");

//			myTable.setWidth(1,"30");
			myTable.setWidth(1,"12.5%");

			minnHlekkur = new Link("Sun",URI);
				minnHlekkur.addParameter("idega_calendar_skoda","Dagur");
				minnHlekkur.addParameter("idega_calendar_month",manudur);
				minnHlekkur.addParameter("idega_calendar_day",dombreyta);
				minnHlekkur.addParameter("idega_calendar_year",ar);
				minnHlekkur.addParameter("action","calendar");

                        short_name_of_month = this.FunctColl.getShortNameOfMonth(manudur, modinfo);
			switch (j) {
				case 1 :
					myTable.setWidth(j+1,"12.5%");
//					myTable.setWidth(j+1,breidd);
					minnHlekkur.setText("Sun "+dombreyta+"."+manudur);
					myTable.add(minnHlekkur,j+1,1);
//					Link minnHlekkur = new Link("Sun "+dombreyta,"jcalendarMeeting.jsp?skoda=Dagur&daym="+dombreyta+"&ar="+ar+"&month="+manudur);
//					 myTable.add(minnHlekkur,j+1,1);
					break;
				case 2 :
					myTable.setWidth(j+1,"12.5%");
					minnHlekkur.setText("Mán "+dombreyta+"."+manudur);
					myTable.add(minnHlekkur,j+1,1);
// 					 myTable.add(new Link("Mán "+dombreyta,"jcalendarMeeting.jsp?skoda=Dagur&daym="+dombreyta+"&ar="+ar+"&month="+manudur),j+1,1);
					break;
				case 3 :
					myTable.setWidth(j+1,"12.5%");
					minnHlekkur.setText("Þri "+dombreyta+"."+manudur);
					myTable.add(minnHlekkur,j+1,1);
//					 myTable.add(new Link("Þri "+dombreyta,"jcalendarMeeting.jsp?skoda=Dagur&daym="+dombreyta+"&ar="+ar+"&month="+manudur),j+1,1);
					break;
				case 4 :
					myTable.setWidth(j+1,"12.5%");
					minnHlekkur.setText("Mið "+dombreyta+"."+manudur);
					myTable.add(minnHlekkur,j+1,1);
//					 myTable.add(new Link("Mið "+dombreyta,"jcalendarMeeting.jsp?skoda=Dagur&daym="+dombreyta+"&ar="+ar+"&month="+manudur),j+1,1);
					break;
				case 5 :
					myTable.setWidth(j+1,"12.5%");
					minnHlekkur.setText("Fim "+dombreyta+"."+manudur);
					myTable.add(minnHlekkur,j+1,1);
//					 myTable.add(new Link("Fim "+dombreyta,"jcalendarMeeting.jsp?skoda=Dagur&daym="+dombreyta+"&ar="+ar+"&month="+manudur),j+1,1);
					break;
				case 6 :
					myTable.setWidth(j+1,"12.5%");
					minnHlekkur.setText("Fös "+dombreyta+"."+manudur);
					myTable.add(minnHlekkur,j+1,1);
//					 myTable.add(new Link("Fös "+dombreyta,"jcalendarMeeting.jsp?skoda=Dagur&daym="+dombreyta+"&ar="+ar+"&month="+manudur),j+1,1);
					break;
				case 7 :
					myTable.setWidth(j+1,"12.5%");
					minnHlekkur.setText("Lau "+dombreyta+"."+manudur);
					myTable.add(minnHlekkur,j+1,1);
//					 myTable.add(new Link("Lau "+dombreyta,"jcalendarMeeting.jsp?skoda=Dagur&daym="+dombreyta+"&ar="+ar+"&month="+manudur),j+1,1);
					break;
			}

			int j_row= 0;
			int j_timi = 7;

			for (double g=1; g<=17; g=g+0.5) {


				++j_row;
                                myTable.setVerticalAlignment(j,j_row,"top");

				if (g%1== 0) {
					++j_timi;
				}

				if (j_timi < 12) {
					if ((g%1) == 0) {
//						myTable.addText("&nbsp&nbsp0"+(g_timi)+":00",1,row);
						s = "0" + j_timi + ":00:00";
					}
					else {
						s = "0" + j_timi + ":30:00";
//					myTable.addText("&nbsp&nbsp0"+(g_timi)+":30",1,row);
					}
				}
				else {
					if ((g%1) == 0) {
//						myTable.addText("&nbsp&nbsp0"+(g_timi)+":00",1,row);
						s = j_timi + ":00:00";
					}
					else {
						s = j_timi + ":30:00";
//					myTable.addText("&nbsp&nbsp0"+(g_timi)+":30",1,row);
					}
				}


                                entry = (CalendarEntry[]) new CalendarEntry().findAll("Select * from "+myCalendarEntry.getEntityName()+" where time_from<='"+(g+7)+"' AND time_to >='"+(g+7+0.5)+"' AND member_id ='"+this.member_id+"' AND entry_date ='"+this.ar+"-"+this.manudur+"-"+this.dombreyta+"'");

                                if (entry != null) {
                                  if (entry.length > 0) {
                                    for (int h = 0 ; h < entry.length ; h++) {
                                      if (h > 0) {
                                        myTable.addBreak(j+1,j_row);
                                      }
                                      myText = new Text(entry[h].getName());
                                      myText.setFontSize(1);
                                      link = new Link(myText,URI);
                                      link.addParameter("idega_calendar_action","view");
                                      link.addParameter("idega_calendar_faerslur_id",entry[h].getID());

                                      if (myText.toString().length() > 11) {
                                        myText.setText(myText.toString().substring(0,10)+"...");
                                       }
                                      myTable.add(link,j+1,j_row);
                                    }
                                  }
                                }
                                /*
				Statement Stmt = Conn.createStatement();
				ResultSet RS = Stmt.executeQuery("SELECT * FROM faerslur,faerslur_extra,verk where faerslur.faerslu_id = faerslur_extra.faerslu_id AND fra<='"+(g+6.5)+"' AND til>='"+(g+6.5+0.5)+"'  AND date='"+ar+"-"+manudur+"-"+dombreyta+"' AND owner_id='"+userid+"' AND verk.verk_id = faerslur.verk_id ");



				if (RS.getString("verk_id") == null) {
//					Image myImage = new Image("hn_plus.gif","Bæta við");
	//					myImage.setBorder(0);
				}
				else {
					myTable.setAlignment(j+1,j_row,"center");
					if (RS.getString("verk_nafn").length() > 11) {
						myTable.add(new Link(RS.getString("verk_nafn").substring(0,10)+"...","jcalendarMeeting_admin.jsp?edit=skoda&daym="+dombreyta+"&month="+manudur+"&ar="+ar+"&faerslu_id="+RS.getString("faerslu_id")),j+1,j_row);

//						myTable.addText(RS.getString("verk_nafn").substring(0,10)+"...",j+1,g+1);
					}
					else {
						myTable.add(new Link(RS.getString("verk_nafn"),"jcalendarMeeting_admin.jsp?edit=skoda&daym="+dombreyta+"&month="+manudur+"&ar="+ar+"&faerslu_id="+RS.getString("faerslu_id")),j+1,j_row);
//						myTable.addText(RS.getString("verk_nafn"),j+1,g+1);
					}
				}
			RS.close();
			Stmt.close();
*/			}
		++dombreyta;
		}

/*	Table myTable3 = new Table(3,1);
		add(myTable3);
		myTable3.setWidth("75%");
		myTable3.setCellspacing(0);
		myTable3.setBorder(1);
		myTable3.setHeight("74");

		myTable3.setWidth(1,"72");
		myTable3.setWidth(3,"72");
/*		myTable3.setAttribute(1,1,"background","/pics/timereg/FooterV.gif");
		myTable3.setAttribute(2,1,"background","/pics/timereg/FooterTiler.gif");
		myTable3.setAttribute(3,1,"background","/pics/timereg/FooterH.gif");


		Image OrVUpp = new Image("/pics/timereg/Or-V-upp.gif","Fyrri vika");
		Image OrHUpp = new Image("/pics/timereg/Or-H-upp.gif","Næsta vika");
		myTable3.setVerticalAlignment(2,1,"bottom");
		myTable3.add(new Link(OrVUpp,"jcalendarMeeting.jsp?month="+manudur+"&daym="+((dombreyta)-14)+"&ar="+ar+"&skoda="+typa),2,1);
		myTable3.add(new Link(OrHUpp,"jcalendarMeeting.jsp?month="+manudur+"&daym="+((dombreyta))+"&ar="+ar+"&skoda="+typa),2,1);
		myTable3.add(new Link("Til baka","jcalendarMeeting.jsp?month="+manudur+"&daym="+((dombreyta)-14)+"&ar="+ar+"&skoda="+typa),2,1);
		myTable3.add(new Link("Áfram","jcalendarMeeting.jsp?month="+manudur+"&daym="+((dombreyta))+"&ar="+ar+"&skoda="+typa),2,1);
*/
//	myPage.print(modinfo);

}	//if setninging endar





	private Table month(ModuleInfo modinfo, boolean isYear) throws SQLException{

	//	add(getDate());

//public void skodaManudur(ModuleInfo modinfo) throws SQLException, IOException {


	String breiddin = "539";

	calendar = new GregorianCalendar(ar,(manudur-1),1);
	vdagur = (calendar.get(calendar.DAY_OF_WEEK));

	dagarman = (FunctColl.getLengthOfMonth(manudur,ar));
	manudurnafn =(FunctColl.getNameOfMonth(manudur,modinfo));

/*	Page myPage = new Page("i d e g a");
	myPage.setTextColor("#000000");
	myPage.setLinkColor("#000000");
	myPage.setVlinkColor("#000000");
	myPage.setAlinkColor("#000000");
*/

	int dombreyta2 = 1;
//	myPar.add(new Link("Verkbók","timaskra.jsp?dagur="+dombreyta+"&manudur="+manudur+"&ar="+ar+" "));
/*	setLinkOfImage("Verkbok","timaskra.jsp?dagur="+dombreyta2+"&manudur="+manudur+"&ar="+ar);
	setLinkOfImage("MinVerk","timaskra_save.jsp?edit=plusverk&dagur="+dombreyta2+"&manudur="+manudur+"&ar="+ar);
	setLinkOfImage("Skyrslur","timaskra_save.jsp?edit=undirskyrsla&manudur="+manudur+"&ar="+ar);
	setLinkOfImage("Bokun","timaskra_save.jsp?edit=book&manudur="+manudur+"&ar="+ar);
	setLinkOfImage("Dagatal","jcalendarMeeting.jsp?ar="+ar+"&daym="+dombreyta2+"&month="+manudur);
*/
//	addLinks();

//	myPar.add(new Link("Dagur","jcalendarMeeting.jsp?skoda=Dagur&daym="+dombreyta+"&month="+manudur+"&ar="+ar));
//	myPar.add(new Link("Vika","jcalendarMeeting.jsp?skoda=Vika&daym="+dombreyta+"&month="+manudur+"&ar="+ar));
//	myPar.add(new Link("Mánuður","jcalendarMeeting.jsp?skoda=Manudur&daym="+dombreyta+"&month="+manudur+"&ar="+ar));

//	Image myHeader = new Image("http://clarke.idega.is/header.swt?type=jpg&title="+manudurnafn+" ");

//	myPage.add(myHeader);
//	myPage.addText(manudurnafn +"  " + ar);


	int dagur = 1;
	int m=1;
	String athid="";


	int i,j = 1;
	int margarlinur = 0;
	int y;


	/*	ResultSet RS_2;
		Statement Stmt_2 = Conn.createStatement();
		RS_2 = Stmt_2.executeQuery("Select nafn from fordi where forda_id='"+userid+"' ");

			Image myHeader = new Image("http://clarke.idega.is/timaskra2.swt?type=jpg&title="+manudurnafn+" "+ar+"&subtitle="+RS_2.getString("nafn")+" ");

		RS_2.close();
		Stmt_2.close();
	//Image myHeader = new Image("http://clarke.idega.is/timaskra.swt?type=jpg&title="+manudurnafn+" "+ar+" ");
*/

/*	Table myTable2 = new Table(3,1);
		add(myTable2);
			myTable2.setWidth("75%");
			myTable2.setBorder(1);
			myTable2.setCellspacing(0);
			myTable2.setHeight("75");
			myTable2.setWidth(1,"400");
			myTable2.setWidth(3,"67");
//			myTable2.setBackgroundImage(1,1,myHeader);
//			myTable2.setAttribute(1,1,"background","/pics/timereg/HeaderV.gif");
//			myTable2.setAttribute(2,1,"background","/pics/timereg/HeaderM.gif");
//			myTable2.setAttribute(3,1,"background","/pics/timereg/HeaderH.gif");

*/
//	String holidayColor = "#DDD8C1";

	Link link;


	Table myTable = new Table(7,10);
		myTable.setAlignment("center");
		myTable.setHorizontalZebraColored(color_1,color_2);
		myTable.setCellpadding(0);
		myTable.setCellspacing(1);
		myTable.setBorder(0);
		myTable.setColor("#000000");
			myTable.setWidth(width);
			if (!(height.equals(""))) {
				myTable.setHeight(height);
			}

        int row = 1;

//		myTable.setWidth("75%");
//		myTable.mergeCells(1,1,5,1);
//		myTable.mergeCells(6,1,7,1);
//		myTable.mergeCells(1,1,7,1);

//		myTable.setAttribute(6,1,"background","/pics/timereg/HeaderM.gif");
//		Image mynd2 = new Image("/pics/timereg/HeaderH.gif");
//		mynd2.setAttribute("align","right");
//		myTable.add(mynd2,6,1);
//		myTable.setAttribute(6,1,"align","right");
//		myTable.setAttribute(6,1,"valign","top");

//		myTable.setHeight(1,"75");
//		myTable.setHeight(8,"0");




//	add(myTable);

	if (!(isYear)) {
            if (showNameOfMonth) {
		Table headerTable = new Table(3,1);
			headerTable.setCellpadding(0);
			headerTable.setCellspacing(0);
			headerTable.setColor(this.header_color);
			headerTable.setWidth(1,"17");
			headerTable.setWidth(3,"17");
			headerTable.setWidth(this.width);
			headerTable.setHeight("36");
			headerTable.setVerticalAlignment(1,1,"top");
			headerTable.setVerticalAlignment(2,1,"middle");
			headerTable.setVerticalAlignment(3,1,"top");
			headerTable.setAlignment(1,1,"left");
			headerTable.setAlignment(2,1,"center");
			headerTable.setAlignment(3,1,"right");
			headerTable.setAlignment("center");

                        headerTable.add(new Image("/pics/jmodules/poll/leftcorner.gif",""),1,1);
                        headerTable.add(new Image("/pics/jmodules/poll/rightcorner.gif",""),3,1);



                        Text nafnPaMoned = new Text(manudurnafn+" "+ar);
                            nafnPaMoned.setFontSize(3);
                            nafnPaMoned.setBold();
                            nafnPaMoned.setFontColor(this.header_text_color);

                        headerTable.add(nafnPaMoned,2,1);
                    add(headerTable);

            }
	}



	myTable.setHeight(row,"20");

	Text Sun = new Text(FunctColl.getNameOfDay(1,modinfo).substring(0,3));
		Sun.setBold();
		Sun.setFontColor(header_text_color);
	Text Man = new Text(FunctColl.getNameOfDay(2,modinfo).substring(0,3));
		Man.setBold();
		Man.setFontColor(header_text_color);
	Text Tri = new Text(FunctColl.getNameOfDay(3,modinfo).substring(0,3));
		Tri.setBold();
		Tri.setFontColor(header_text_color);
	Text Mid = new Text(FunctColl.getNameOfDay(4,modinfo).substring(0,3));
		Mid.setBold();
		Mid.setFontColor(header_text_color);
	Text Fim  = new Text(FunctColl.getNameOfDay(5,modinfo).substring(0,3));
		Fim.setBold();
		Fim.setFontColor(header_text_color);
	Text Fos = new Text(FunctColl.getNameOfDay(6,modinfo).substring(0,3));
		Fos.setBold();
		Fos.setFontColor(header_text_color);
	Text Lau = new Text(FunctColl.getNameOfDay(7,modinfo).substring(0,3));
		Lau.setBold();
		Lau.setFontColor(header_text_color);


	myTable.add(Sun,1,row);	myTable.setAttribute(1,row,"align","center");//	myTable.setAttribute(1,1,"background","/pics/timereg/HeadddddErV.gif");
	myTable.add(Man,2,row);myTable.setAttribute(2,row,"align","center");//	myTable.setAttribute(2,1,"background","/pics/timereg/HeadddddErM.gif");
	myTable.add(Tri,3,row);myTable.setAttribute(3,row,"align","center");//	myTable.setAttribute(3,1,"background","/pics/timereg/HeadddddErM.gif");
	myTable.add(Mid,4,row);myTable.setAttribute(4,row,"align","center");//	myTable.setAttribute(4,1,"background","/pics/timereg/HeadddddErM.gif");
	myTable.add(Fim,5,row);myTable.setAttribute(5,row,"align","center");//	myTable.setAttribute(5,1,"background","/pics/timereg/HeadddddErM.gif");
	myTable.add(Fos,6,row);myTable.setAttribute(6,row,"align","center");//	myTable.setAttribute(6,1,"background","/pics/timereg/HeadddddErM.gif");
	myTable.add(Lau,7,row);myTable.setAttribute(7,row,"align","center");//	myTable.setAttribute(7,1,"background","/pics/timereg/HeadddddErH.gif");

	myTable.setRowColor(row,header_color);

/*	myTable.addText("Sunnudagur",1,2);
	myTable.addText("Mánudagur",2,2);
	myTable.addText("Þriðjudagur",3,2);
	myTable.addText("Miðvikudagur",4,2);
	myTable.addText("Fimmtudagur",5,2);
	myTable.addText("Föstudagur",6,2);
	myTable.addText("Laugardagur",7,2);
*/
//	Faerslur[] faerslur;
        CalendarEntry[] entry;
	idegaTimestamp stamp;
	int teljaSkipti;
	int maxShown = 2;
	String dagsString;
	Text fundur;
	Link linkur;
	String temp_name;
	int fyrriDagur,fyrriMan,fyrriAr;

        row++;

	if (vdagur != 1) {
		for (m=1;m<=(vdagur-1);m++) {
			myTable.setColor(m,row,"#CCCCCC");
			myTable.setAttribute(m,row,"valign","top");
			myTable.addText("&nbsp;&nbsp;",m,row);
			fyrriDagur = FunctColl.getLengthOfMonth(manudur-1,ar) - (vdagur-1) + m;
			fyrriMan = manudur -1;
			fyrriAr = ar;
				if (fyrriMan == 0) {
					fyrriMan=12;
					--fyrriAr;
				}

			Text myText= new Text(Integer.toString(fyrriDagur));
				myText.setFontColor("gray");
				myText.setFontSize(1);
/*			link = new Link(myText.toString(),URI);
				link.addParameter("idega_calendar_skoda","Dagur");
				link.addParameter("idega_calendar_month",fyrriMan);
				link.addParameter("idega_calendar_day",fyrriDagur);
				link.addParameter("idega_calendar_year",fyrriAr);
				link.addParameter("action","calendar");
			myTable.add(link,m,2);
*/
			myTable.add(myText,m,row);


			if (isYear) {
			}
			else {
				dagsString = (TextSoap.addZero(fyrriDagur)+"."+TextSoap.addZero(fyrriMan)+"."+fyrriAr);
//				faerslur = (Faerslur[]) (new Faerslur()).findAll("Select * from faerslur where faerslur_date='"+dagsString+"'");
//                                  entry = (CalendarEntry[]) (new CalendarEntry()).findAll("Select * from calendar_entry where entry_date='"+dagsString+"'");
                                entry = (CalendarEntry[]) (myCalendarEntry).findAll("Select * from "+myCalendarEntry.getEntityName()+" where entry_date='"+dagsString+"' AND (member_id = -1 OR member_id = "+this.member_id+")");
				if (entry.length > 0) {
					teljaSkipti = entry.length;
					if (teljaSkipti > maxShown) {
						teljaSkipti = maxShown;
					}
					for (int v = 0 ; v < teljaSkipti ; v++) {
                                            if ((entry[v].getMemberId() == -1) || (entry[v].getMemberId() == this.member_id)) {
						temp_name = " "+entry[v].getName();
						if (temp_name.length() > 10) {
							temp_name = temp_name.substring(0,10)+"...";
						}
						fundur = new Text(temp_name);
							fundur.setFontSize(1);
						linkur = new Link(fundur,URI);
							linkur.addParameter("idega_calendar_action","view");
							linkur.addParameter("idega_calendar_faerslur_id",entry[v].getID());
						myTable.add("<br>",m,row);
						myTable.add(linkur,m,row);
                                            }
					}
					if (entry.length > maxShown) {
						myTable.add("<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",m,2);
						Text meiraTxt = new Text(LANG[0]);
							meiraTxt.setFontSize(1);
						Link meira = new Link(meiraTxt,URI);
							meira.addParameter("idega_calendar_month",fyrriMan);
							meira.addParameter("idega_calendar_day",fyrriDagur);
							meira.addParameter("idega_calendar_year",fyrriAr);
							meira.addParameter("idega_calendar_action","complete_list");
						myTable.add(meira,m,row);
					}
				}
			}








//			myTable.add(new Link(myText,"jcalendarMeeting.jsp?skoda=Dagur&daym="+fyrriDagur+"&month="+fyrriMan+"&ar="+fyrriAr),m,2);

//			myTable.add(myText,m,2);

			cellColor="#666699";
		}
	}

	boolean svara=false;

	for (i=1 ; i<=6 ; i++) {
        if (i != 1) {
            row++;
        }
//	myTable.setHeight(i+1,"56");
		for (j=1; j<=(7); j++) {
		if ((i==1) && ((m + j) == 9))
			break;

		if ((i % 2)== 0) cellColor="#666699";
		else cellColor="#9999CC";

		double hoursToday=0;

//		myTable.setWidth(j,"14.3%");

/*		Statement Stmt = Conn.createStatement();
		ResultSet RS = Stmt.executeQuery("SELECT * FROM fordi,faerslur,verk WHERE fordi.forda_id = faerslur.forda_id AND verk.verk_id = faerslur.verk_id AND owner_id = '"+userid+"' AND date='"+ar+"-"+manudur+"-"+dagur+"'");
//		ResultSet RS = Stmt.executeQuery("Select * from faerslur,verk,fordi WHERE fordi.forda_id = faerslur.forda_id AND date='"+ar+"-"+manudur+"-"+dagur+"' AND verk.verk_id = faerslur.verk_id AND owner_id="+userid+" ");

		while (RS.next()) {
			if (RS.getString("tegund_nafn").equals("Maður") ) {
				hoursToday += RS.getDouble("timafjoldi");
			}
		}

		Stmt.close();
		RS.close();
*/
//		Text myText = new Text();

/*		if (hoursToday > 0) {
			myText = new Text("<br><br><br>&nbsp;&nbsp;Heildartímar "+hoursToday);
				myText.setFontSize(1);
//				myText.addBreak();
//			myTable.add(new Link(myText,"hlekkurinn hér"),j,i+1);
		}
*/
//		boolean fri = false;
		link = new Link(""+dagur,URI);
			link.addParameter("idega_calendar_skoda","Dagur");
			link.addParameter("idega_calendar_month",manudur);
			link.addParameter("idega_calendar_day",dagur);
			link.addParameter("idega_calendar_year",ar);
//			link.addParameter("idega_calendar_action","idega_calendar_new");
			link.addParameter("action","calendar");


		if (i == 1) {
			myTable.setAttribute(j+m-1,row,"valign","top");
			myTable.addText("&nbsp;",j+m-1,row);
//			add(m+"");
//			add("<br>"+(j+m-1)+" , "+(i+1)+"<br>");

			if (this.member_id != -1) {
				myTable.add(link,j+m-1,row);
			}
			else {
				myTable.add(""+dagur,j+m-1,row);
			}

//			myTable.add(new Link(Integer.toString(dagur),"jcalendarMeeting.jsp?skoda=Dagur&daym="+dagur+"&month="+manudur+"&ar="+ar),j+m-1,i+1);

//			myTable.setColor(j+m-1,i+2,cellColor);

// þetta
//			myTable.add(myText,j+m-1,i+1);


//			fri = FunctColl.getHoliday(ar,manudur,dagur);
//			if (fri) myTable.setColor(j+m-1,i+1,holidayColor);


			if (isYear) {
			}
			else {
				dagsString = (TextSoap.addZero(dagur)+"."+TextSoap.addZero(manudur)+"."+ar);
//				faerslur = (Faerslur[]) (new Faerslur()).findAll("Select * from faerslur where faerslur_date='"+dagsString+"'");
				entry = (CalendarEntry[]) (new CalendarEntry()).findAll("Select * from "+myCalendarEntry.getEntityName()+" where entry_date='"+dagsString+"' AND (member_id = -1 OR member_id = "+this.member_id+")");

				if (entry.length > 0) {
					teljaSkipti = entry.length;
					if (teljaSkipti > maxShown) {
						teljaSkipti = maxShown;
					}
					for (int v = 0 ; v < teljaSkipti ; v++) {
                                            if ((entry[v].getMemberId() == -1) || (entry[v].getMemberId() == this.member_id)) {
						temp_name = " "+entry[v].getName();
						if (temp_name.length() > 10) {
							temp_name = temp_name.substring(0,10)+"...";
						}
						fundur = new Text(temp_name);
							fundur.setFontSize(1);
						linkur = new Link(fundur,URI);
							linkur.addParameter("idega_calendar_action","view");
							linkur.addParameter("idega_calendar_faerslur_id",entry[v].getID());
						myTable.add("<br>",j+m-1,row);
						myTable.add(linkur,j+m-1,row);
                                            }
					}
					if (teljaSkipti == maxShown) {
						Text meiraTxt = new Text(LANG[0]);
							meiraTxt.setFontSize(1);
						Link meira = new Link(meiraTxt,URI);
							meira.addParameter("idega_calendar_month",manudur);
							meira.addParameter("idega_calendar_day",dagur);
							meira.addParameter("idega_calendar_year",ar);
							meira.addParameter("idega_calendar_action","complete_list");
						myTable.add(meira,j+m-1,row);
					}
				}
			}

		}

		else {
		myTable.setAttribute(j,row,"valign","top");
			myTable.addText("&nbsp;",j,row);
			if (this.member_id != -1) {
				myTable.add(link,j,row);
			}
			else {
				myTable.add(""+dagur,j,row);
			}
//			myTable.add(link,j,i+1);


//			myTable.add(new Link(Integer.toString(dagur),"jcalendarMeeting.jsp?skoda=Dagur&daym="+dagur+"&month="+manudur+"&ar="+ar),j,i+1);
//			myTable.setColor(j,i+2,cellColor);
// þetta
//			myTable.add(myText,j,i+1);
//			fri = FunctColl.getHoliday(ar,manudur,dagur);
//			if (fri) myTable.setColor(j,i+1,holidayColor);
		}

/*		if (fri) {
			myTable.setColor(j,i+1,"#F0c0ff");
			myTable.addText(ar+" "+manudur+" "+dagur,j,i+1);
		}
*/

			if (i!=1)
			if (isYear) {
			}
			else {
				dagsString = (TextSoap.addZero(dagur)+"."+TextSoap.addZero(manudur)+"."+ar);
//				faerslur = (Faerslur[]) (new Faerslur()).findAll("Select * from faerslur where faerslur_date='"+dagsString+"'");
//                                entry = (CalendarEntry[]) (new CalendarEntry()).findAll("Select * from calendar_entry where entry_date='"+dagsString+"'");
				entry = (CalendarEntry[]) (new CalendarEntry()).findAll("Select * from "+myCalendarEntry.getEntityName()+" where entry_date='"+dagsString+"' AND (member_id = -1 OR member_id = "+this.member_id+")");
				if (entry.length > 0) {
					teljaSkipti = entry.length;
					if (teljaSkipti > maxShown) {
						teljaSkipti = maxShown;
					}
					for (int v = 0 ; v < teljaSkipti ; v++) {
                                            if ((entry[v].getMemberId() == -1) || (entry[v].getMemberId() == this.member_id)) {
						temp_name = " "+entry[v].getName();
						if (temp_name.length() > 10) {
							temp_name = temp_name.substring(0,10)+"...";
						}
						fundur = new Text(temp_name);
//						fundur = new Text(" "+faerslur[v].getName());
							fundur.setFontSize(1);
						linkur = new Link(fundur,URI);
							linkur.addParameter("idega_calendar_action","view");
							linkur.addParameter("idega_calendar_faerslur_id",entry[v].getID());
						myTable.add("<br>",j,row);
						myTable.add(linkur,j,row);
                                            }
					}
					if (entry.length > maxShown) {
						myTable.add("<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",j,row);
						Text meiraTxt = new Text(LANG[0]);
							meiraTxt.setFontSize(1);
						Link meira = new Link(meiraTxt,URI);
							meira.addParameter("idega_calendar_month",manudur);
							meira.addParameter("idega_calendar_day",dagur);
							meira.addParameter("idega_calendar_year",ar);
							meira.addParameter("idega_calendar_action","complete_list");
						myTable.add(meira,j,row);
					}
				}
			}



		dagur++;
		margarlinur=0;

		if (dagur > dagarman) break;
	}

	if (dagur > dagarman) break;

//	out.println("<tr valign=\"top\" height=\"80\">");
	}

	for (int klam = 1;klam <= 7-j; klam++) {
		myTable.addText("&nbsp;&nbsp;",j+klam,row);
		Text minnTexti = new Text(Integer.toString(klam));
				minnTexti.setFontColor("gray");
				minnTexti.setFontSize(1);
		myTable.setColor(j+klam,row,"#CCCCCC");
		myTable.setAttribute(j+klam,row,"valign","top");
//		myTable.add(minnTexti,j+klam,i+1);

		int naestMan = manudur +1;
		int naestAr = ar;
		if (naestMan ==13) {
			naestMan = 1;
			++naestAr;
		}

/*		link = new Link(minnTexti,URI);
			link.addParameter("idega_calendar_skoda","Dagur");
			link.addParameter("idega_calendar_month",naestMan);
			link.addParameter("idega_calendar_day",minnTexti.toString());
			link.addParameter("idega_calendar_year",naestAr);
			link.addParameter("action","calendar");
*/		myTable.add(minnTexti,j+klam,row);

			if (isYear) {
			}
			else {
				dagsString = (TextSoap.addZero(klam)+"."+TextSoap.addZero(naestMan)+"."+naestAr);
//				faerslur = (Faerslur[]) (new Faerslur()).findAll("Select * from faerslur where faerslur_date='"+dagsString+"'");
//                                entry = (CalendarEntry[]) (new CalendarEntry()).findAll("Select * from calendar_entry where entry_date='"+dagsString+"'");
				entry = (CalendarEntry[]) (new CalendarEntry()).findAll("Select * from "+myCalendarEntry.getEntityName()+" where entry_date='"+dagsString+"' AND (member_id = -1 OR member_id = "+this.member_id+")");
				if (entry.length > 0) {
					teljaSkipti = entry.length;
					if (teljaSkipti > maxShown) {
						teljaSkipti = maxShown;
					}
					for (int v = 0 ; v < teljaSkipti ; v++) {
                                            if ((entry[v].getMemberId() == -1) || (entry[v].getMemberId() == this.member_id)) {
						temp_name = " "+entry[v].getName();
						if (temp_name.length() > 10) {
							temp_name = temp_name.substring(0,10)+"...";
						}
						fundur = new Text(temp_name);
//						fundur = new Text(" "+faerslur[v].getName());
							fundur.setFontSize(1);
						linkur = new Link(fundur,URI);
							linkur.addParameter("idega_calendar_action","view");
							linkur.addParameter("idega_calendar_faerslur_id",entry[v].getID());
						myTable.add("<br>",j+klam,row);
						myTable.add(linkur,j+klam,row);
                                            }
					}
					if (entry.length > maxShown) {
						myTable.add("<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",j+klam,row);
						Text meiraTxt = new Text(LANG[0]);
							meiraTxt.setFontSize(1);
						Link meira = new Link(meiraTxt,URI);
							meira.addParameter("idega_calendar_month",naestMan);
							meira.addParameter("idega_calendar_day",klam);
							meira.addParameter("idega_calendar_year",naestAr);
							meira.addParameter("idega_calendar_action","complete_list");
						myTable.add(meira,j+klam,row);
					}
				}
			}

//		myTable.add(new Link(minnTexti,"jcalendarMeeting.jsp?skoda=Dagur&daym="+minnTexti+"&month="+naestMan+"&ar="+naestAr),j+klam,i+1);
	}
//        row++;

	int radir = row+1;
	if (isYear) {
		radir = row;
	}
	myTable.setRows(radir);

	int tala = Integer.parseInt(height) - 55;
	int heightPerRow = tala / radir;
	int widthPerRow = Integer.parseInt(width) / 7;

	for (int u = 2 ; u <radir ; u++) {
		myTable.setHeight(1,u,Integer.toString(heightPerRow));
	}
	for (int yl = 1 ; yl <= 7 ; yl ++) {
		myTable.setWidth(yl,1,Integer.toString(widthPerRow));
	}


/*	Table myTable3 = new Table(3,1);
		add(myTable3);
		myTable3.setWidth("75%");
		myTable3.setCellspacing(0);
		myTable3.setBorder(1);
		myTable3.setHeight("74");

		myTable3.setWidth(1,"72");
		myTable3.setWidth(3,"72");
/*		myTable3.setAttribute(1,1,"background","/pics/timereg/FooterV.gif");
		myTable3.setAttribute(2,1,"background","/pics/timereg/FooterTiler.gif");
		myTable3.setAttribute(3,1,"background","/pics/timereg/FooterH.gif");

		Image OrVUpp = new Image("/pics/timereg/Or-V-upp.gif","Fyrri mánuður");
		Image OrHUpp = new Image("/pics/timereg/Or-H-upp.gif","Næsta mánuður");
		myTable3.setVerticalAlignment(2,1,"bottom");
		myTable3.add(new Link(OrVUpp,"jcalendarMeeting.jsp?month="+(manudur-1)+"&daym="+(1)+"&ar="+ar),2,1);
		myTable3.add(new Link(OrHUpp,"jcalendarMeeting.jsp?month="+(manudur+1)+"&daym="+(1)+"&ar="+ar),2,1);
		myTable3.add(new Link("Til baka","jcalendarMeeting.jsp?month="+(manudur-1)+"&daym="+(1)+"&ar="+ar),2,1);
		myTable3.add(new Link("Áfram","jcalendarMeeting.jsp?month="+(manudur+1)+"&daym="+(1)+"&ar="+ar),2,1);
*/


			//out.println(m);

/*for (y=j;y<=6;y++)
	if ((i % 2)==0 ) out.println("<td bgcolor=\"#666699\">&nbsp;</td>");
	else out.println("<td bgcolor=\"#9999CC\">&nbsp;</td>");

out.println("</table>");*/
	Table bottomTable = new Table(3,1);
//		bottomTable.setHeight(1,1,"15");
		bottomTable.setWidth("100%");
                bottomTable.setHeight("100%");
		bottomTable.setAlignment("center");
		bottomTable.setAlignment(2,1,"center");
		bottomTable.setAlignment(3,1,"right");
                bottomTable.setColor(header_color);


        if (previous_image_url == null) {
	  Link linken = new Link(LANG[1],URI);
			linken.addParameter("idega_calendar_skoda","Manudur");
			linken.addParameter("idega_calendar_month",(manudur-1));
			linken.addParameter("idega_calendar_day","1");
			linken.addParameter("idega_calendar_year",ar);
//			linken.addParameter("idega_calendar_action","idega_calendar_new");
			linken.addParameter("action","calendar");
        	bottomTable.add(linken,1,1);
        }
        else {
	  Link linken = new Link(new Image(previous_image_url),URI);
			linken.addParameter("idega_calendar_skoda","Manudur");
			linken.addParameter("idega_calendar_month",(manudur-1));
			linken.addParameter("idega_calendar_day","1");
			linken.addParameter("idega_calendar_year",ar);
//			linken.addParameter("idega_calendar_action","idega_calendar_new");
			linken.addParameter("action","calendar");
        	bottomTable.add(linken,1,1);
        }


        if (today_image_url == null) {
      	  Link linken2 = new Link(LANG[11],URI);
			linken2.addParameter("idega_calendar_skoda","Manudur");
//			linken2.addParameter("idega_calendar_action","idega_calendar_new");
			linken2.addParameter("action","calendar");
	        bottomTable.add(linken2,2,1);
        }
        else {
      	  Link linken2 = new Link(new Image(today_image_url),URI);
			linken2.addParameter("idega_calendar_skoda","Manudur");
//			linken2.addParameter("idega_calendar_action","idega_calendar_new");
			linken2.addParameter("action","calendar");
	        bottomTable.add(linken2,2,1);
        }


        if (next_image_url == null) {
      	  Link linken2 = new Link(LANG[2],URI);
			linken2.addParameter("idega_calendar_skoda","Manudur");
			linken2.addParameter("idega_calendar_month",(manudur+1));
			linken2.addParameter("idega_calendar_day","1");
			linken2.addParameter("idega_calendar_year",ar);
//			linken2.addParameter("idega_calendar_action","idega_calendar_new");
			linken2.addParameter("action","calendar");
	        bottomTable.add(linken2,3,1);
        }
        else {
      	  Link linken2 = new Link(new Image(next_image_url),URI);
			linken2.addParameter("idega_calendar_skoda","Manudur");
			linken2.addParameter("idega_calendar_month",(manudur+1));
			linken2.addParameter("idega_calendar_day","1");
			linken2.addParameter("idega_calendar_year",ar);
//			linken2.addParameter("idega_calendar_action","idega_calendar_new");
			linken2.addParameter("action","calendar");
	        bottomTable.add(linken2,3,1);
        }


//	add(new Link("Fyrri Mánuður","jcalendarMeeting.jsp?month="+(manudur-1)+"&daym="+(1)+"&ar="+ar+" "));
//	add(new Link("Næsti Mánuður","jcalendarMeeting.jsp?month="+(manudur+1)+"&daym="+(1)+"&ar="+ar+" "));

//	out.println("<a href=\"jcalendarMeeting.jsp?month="+(manudur-1)+"&daym="+(1)+"&ar="+ar+"\">Fyrri mánuður</a>");
//	out.println("<a href=\"jcalendarMeeting.jsp?month="+(manudur+1)+"&daym="+(1)+"&ar="+ar+"\">Næsti mánuður</a>");

//	myPage.print(modinfo);
	if (!(isYear)) {
	myTable.mergeCells(1,radir,7,radir);
	myTable.setHeight(1,radir,"17");

		myTable.add(bottomTable,1,radir);
	}

	return myTable;

}	//Mánudur Endar


private String formatText(String textString)
{
	TextSoap soap = new TextSoap();
	textString = soap.findAndReplace(textString,"\n","<br>");
	textString = soap.findAndReplace(textString,"\t","&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

	return textString;
}

protected void deleteEntry(ModuleInfo modinfo)  throws SQLException, IOException{
	String faerslur_id = modinfo.getRequest().getParameter("idega_calendar_faerslu_id");

        CalendarEntry entry = new CalendarEntry(Integer.parseInt(faerslur_id));
        idegaTimestamp i_stamp = new idegaTimestamp(entry.getDate());

        if (this.connect_to_timesheet) {
            TimesheetEntry[] timesheets = (TimesheetEntry[]) entry.findRelated(new TimesheetEntry());
            if (timesheets != null) {
              if (timesheets.length > 0 ) {
                for (int i = 0 ; i < timesheets.length ; i++) {
                  entry.removeFrom(timesheets[i]);
                  timesheets[i].delete();
                }
              }
            }
        }

        entry.delete();




	modinfo.getResponse().sendRedirect("/calendar.jsp");

}



private void viewToday(ModuleInfo modinfo) throws SQLException {
	String day   = modinfo.getRequest().getParameter("idega_calendar_day");
	String month = modinfo.getRequest().getParameter("idega_calendar_month");
	String year  = modinfo.getRequest().getParameter("idega_calendar_year");

	String dateString = day+"."+month+"."+year;
	Text header = new Text(this.getDate(modinfo));
          header.setBold();
          header.setFontColor(this.header_text_color);

	Link linkur;
	int row = 1;
//	Faerslur[] faerslur = (Faerslur[]) (new Faerslur()).findAll("select * from faerslur where faerslur_date='"+dateString+"' order by faerslur_date");

        CalendarEntry[] entry = (CalendarEntry[]) (new CalendarEntry()).findAll("select * from "+myCalendarEntry.getEntityName()+" where entry_date ='"+dateString+"' and (member_id = -1 OR member_id ='"+this.member_id+"') order by "+myCalendarEntry.getIDColumnName());

	Table table = new Table();
		table.setBorder(0);
		table.setWidth(width);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setRowColor(1,this.header_color);
		table.mergeCells(1,1,2,1);
		table.setWidth(1,2,"25");
		table.add(header,1,1);



	for (int i = 0 ; i < entry.length ; i++) {
		Text fundur = new Text(entry[i].getName());
		linkur = new Link(fundur,URI);
		linkur.addParameter("idega_calendar_action","view");
		linkur.addParameter("idega_calendar_faerslur_id",entry[i].getID());
	 	table.add(linkur,2,i+2);
		table.setRowColor(i+2,color_2);
		row = i+2;
	}

	table.add(new BackButton(LANG[3]),1,row+1);
		table.setRowColor(row+1,this.header_color);
		table.mergeCells(1,row+1,2,row+1);


	add(table);
}



private void view(ModuleInfo modinfo) throws SQLException {
	String faerslu_id = modinfo.getRequest().getParameter("idega_calendar_faerslur_id");

//	Faerslur faerslur = new Faerslur(Integer.parseInt(faerslu_id));
        CalendarEntry  entry = new CalendarEntry(Integer.parseInt(faerslu_id));

	String nameSt = entry.getName();
        String dateSt = entry.getDate().toString().substring(0,10);
        double timeFr = entry.getFrom();
        double timeTo = entry.getTo();

	String commentSt=entry.getBody();
	Text name = new Text("&nbsp;&nbsp;&nbsp;&nbsp;");
	Text comment = new Text("");
	if (nameSt != null) {
		name.addToText(nameSt);
	}
/*
        if (dateSt != null) {
                name.addToText("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+dateSt+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        }
        name.addToText(""+timeFr);
        name.addToText("&nbsp;-&nbsp;"+timeTo);
*/
	if (commentSt != null) {
		comment.setText(formatText(commentSt));
	}

		name.setBold();
		name.setFontSize(3);
		name.setFontColor(header_text_color);

		comment.setFontSize(3);


	Timestamp stamp = entry.getDate();

	Form myForm = new Form();
	Table back = new Table(1,1);
		back.setColor(header_color);
//		back.setCellpadding(0);
//		back.setCellspacing(0);
		myForm.add(back);

	Table table = new Table(1,3);
		back.add(table);

		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setBorder(0);
		table.setWidth(width);
		table.setAlignment("center");
		table.setColor(1,1,header_color);
		table.setColor(1,2,color_2);
		table.setColor(1,3,header_color);
//		table.setAlignment(1,1,"center");


	table.add(name,1,1);
	table.addBreak(1,2);
	table.add(comment,1,2);
	table.addBreak(1,2);
	table.addBreak(1,2);



	table.add(new BackButton(LANG[3]),1,3);
        if (this.member_id > 0 )
	if  ((this.isAdmin = true) || (this.member_id == entry.getMemberId()) ) {

//		Link henda = new Link("Henda",URI);
		table.add(new SubmitButton("idega_calendar_action",LANG[9]),1,3);
		table.add(new SubmitButton("idega_calendar_action",LANG[10]),1,3);
			//table.add(new HiddenInput("idega_calendar_action","delete_faerslur"));
			table.add(new HiddenInput("idega_calendar_faerslu_id",Integer.toString(entry.getID())));
//		table.add(henda,1,3);
	}
	add(myForm);



}



private void nytt(ModuleInfo modinfo) throws SQLException{

        addMenu();
	Form myForm = new Form();
		myForm.setMethod("post");
		myForm.add(new HiddenInput("idega_calendar_action","idega_calendar_nytt_save"));
		myForm.add(new HiddenInput("action","calendar"));
		myForm.add(new HiddenInput("idega_calendar_year",modinfo.getRequest().getParameter("idega_calendar_year")));
		myForm.add(new HiddenInput("idega_calendar_month",modinfo.getRequest().getParameter("idega_calendar_month")));
		myForm.add(new HiddenInput("idega_calendar_day",modinfo.getRequest().getParameter("idega_calendar_day")));

//		myForm.add(new HiddenInput("idega_calendar_year",modinfo.getRequest().gerParameter("idega_calendar_year")));

	add(myForm);


	idegaCalendar cal = new idegaCalendar();


	Table myTable = new Table(1,6);
		myTable.setBorder(0);
		myTable.setAlignment("center");
		myTable.setColor(color_1);
		myTable.setCellpadding(5);
		myTable.setCellspacing(0);
//		myTable.setWidth(width);
	myForm.add(myTable);

	String head = modinfo.getRequest().getParameter("idega_calendar_day") + ". " + cal.getNameOfMonth(modinfo.getRequest().getParameter("idega_calendar_month")) + " "+modinfo.getRequest().getParameter("idega_calendar_year");
	Text header = new Text(head);
		header.setBold();
		header.setFontColor("#FFFFFF");
		header.setFontSize(3);

	myTable.add(header,1,1);
		myTable.setAlignment(1,1,"center");

	Text name = new Text(LANG[4]);
		name.setBold();
		name.setFontColor("#FFFFFF");

	myTable.add(name,1,2);
	TextInput nameHere = new TextInput("forda_id_string");
		nameHere.setSize(50);
	myTable.add(nameHere,1,3);

	Text lysing = new Text(LANG[5]);
		lysing.setBold();
		lysing.setFontColor("#FFFFFF");

	myTable.add(lysing,1,4);
//	myTable.setVerticalAlignment(1,3,"top");
	TextArea writeHere = new TextArea("comment");
		writeHere.setHeight(15);
		writeHere.setWidth(50);
	myTable.add(writeHere,1,5);




	myTable.add(new ResetButton(LANG[6]),1,6);

	myTable.add(new SubmitButton(LANG[7]),1,6);
//		myTable.setAlignment(2,4,"right");
}


private void modifyEntry(ModuleInfo modinfo) throws SQLException {

	String faerslur_id = modinfo.getRequest().getParameter("idega_calendar_faerslu_id");

	if (faerslur_id != null) {
//	Faerslur faerslur = new Faerslur(Integer.parseInt(faerslur_id));
        CalendarEntry entry = new CalendarEntry(Integer.parseInt(faerslur_id));

	Form myForm = new Form();
		myForm.setMethod("post");
		myForm.add(new HiddenInput("idega_calendar_action","idega_calendar_modify_save"));
		myForm.add(new HiddenInput("idega_calendar_modify","true"));
		myForm.add(new HiddenInput("idega_calendar_faerslu_id",faerslur_id));
		myForm.add(new HiddenInput("action","calendar"));

/*		myForm.add(new HiddenInput("idega_calendar_year",""+(faerslur.getDate().getYear()+1900)));
		myForm.add(new HiddenInput("idega_calendar_month",""+(faerslur.getDate().getMonth()+1)));
		myForm.add(new HiddenInput("idega_calendar_day",""+faerslur.getDate().getDate()));
*/

//	add(""+(faerslur.getDate().getYear()+1900));
//	add("-"+(faerslur.getDate().getMonth()+1));
//	add("-"+faerslur.getDate().getDate());
/*		myForm.add(new HiddenInput("idega_calendar_year",modinfo.getRequest().getParameter("idega_calendar_year")));
		myForm.add(new HiddenInput("idega_calendar_month",modinfo.getRequest().getParameter("idega_calendar_month")));
		myForm.add(new HiddenInput("idega_calendar_day",modinfo.getRequest().getParameter("idega_calendar_day")));
*/
//		myForm.add(new HiddenInput("idega_calendar_year",modinfo.getRequest().gerParameter("idega_calendar_year")));

	add(myForm);

	idegaCalendar cal = new idegaCalendar();


	Table myTable = new Table(1,6);
		myTable.setBorder(0);
		myTable.setAlignment("center");
		myTable.setColor(color_1);
		myTable.setCellpadding(5);
		myTable.setCellspacing(0);
//		myTable.setWidth(width);
	myForm.add(myTable);

	idegaTimestamp idegaStamp = new idegaTimestamp(entry.getDate());
	String head = idegaStamp.getISLDate();
//	String head = modinfo.getRequest().getParameter("idega_calendar_day") + ". " + cal.getNameOfMonth(modinfo.getRequest().getParameter("idega_calendar_month")) + " "+modinfo.getRequest().getParameter("idega_calendar_year");
	Text header = new Text(head);
		header.setBold();
		header.setFontColor("#FFFFFF");
		header.setFontSize(3);

	myTable.add(header,1,1);
		myTable.setAlignment(1,1,"center");


	Text name = new Text(LANG[4]);
		name.setBold();
		name.setFontColor("#FFFFFF");

//	String content = faerslur.getComment();
	String nameSt = entry.getName();
	String commentSt= entry.getBody();
	if (nameSt == null) {
		nameSt = "";
	}
	if (commentSt == null) {
		commentSt= "";
	}

	myTable.add(name,1,2);
	TextInput nameHere = new TextInput("forda_id_string",nameSt);
		nameHere.setSize(50);
	myTable.add(nameHere,1,3);

	Text lysing = new Text(LANG[5]);
		lysing.setBold();
		lysing.setFontColor("#FFFFFF");

	myTable.add(lysing,1,4);
//	myTable.setVerticalAlignment(1,3,"top");
	TextArea writeHere = new TextArea("comment",commentSt);
		writeHere.setHeight(15);
		writeHere.setWidth(50);
	myTable.add(writeHere,1,5);


	myTable.add(new ResetButton(LANG[6]),1,6);
	myTable.add(new SubmitButton(LANG[7]),1,6);
//		myTable.setAlignment(2,4,"right");

	}
	else {
		add("entry_id = null");
	}

}


private void saveModified(ModuleInfo modinfo) throws SQLException , IOException{
	String forda_id_string = modinfo.getRequest().getParameter("forda_id_string");
	String comment = modinfo.getRequest().getParameter("comment");

	String faerslur_id = modinfo.getRequest().getParameter("idega_calendar_faerslu_id");

//	String modifySt = modinfo.getRequest().getParameter("idega_calendar_modify");

	boolean villa = false;

	if (forda_id_string == null) {
		forda_id_string="";
	}

	if (forda_id_string.equals("")) {
		nytt(modinfo);
	}
	{
		if (comment==null) {
			comment="";
		}
		try {
	/*		String	temp_manudu = modinfo.getRequest().getParameter("idega_calendar_month");
			String	temp_ar2 = modinfo.getRequest().getParameter("idega_calendar_year");
			String	temp_dom2 = modinfo.getRequest().getParameter("idega_calendar_day");

			idegaTimestamp stamp = new idegaTimestamp(temp_ar2+"-"+TextSoap.addZero(Integer.parseInt(temp_manudu))+"-"+TextSoap.addZero(Integer.parseInt(temp_dom2)));
	*/

//			Faerslur faerslur = new Faerslur(Integer.parseInt(faerslur_id));
                        CalendarEntry entry = new CalendarEntry(Integer.parseInt(faerslur_id));
				entry.setHeader(forda_id_string);
				entry.setBody(comment);
	//			faerslur.setDate(stamp.getTimestamp());

				entry.update();
//			add(stamp.getTimestamp().toString());

//			month(modinfo);

		}
		catch (Exception e) {
			villa = true;
		}

	}



	if (villa) {
		add(LANG[8]+"<br>");
		add(new BackButton(LANG[3]));
	}
	else {
/*		add("Skráning heppnaðist<br>");
		Link link = new Link("Áfram","/calendar.jsp");
			link.addParameter("action","calendar");
		add(link);
		add("sa
*/		modinfo.getResponse().sendRedirect("/calendar.jsp");
/*		add("save_modified");
		add(forda_id_string);
		add(comment);
*/
	}

}



private void saveNytt(ModuleInfo modinfo) throws SQLException,IOException{
	String forda_id_string = modinfo.getRequest().getParameter("forda_id_string");
	String comment = modinfo.getRequest().getParameter("comment");

//	String modifySt = modinfo.getRequest().getParameter("idega_calendar_modify");

	boolean villa = false;

	if (forda_id_string == null) {
		forda_id_string="";
	}

	if (forda_id_string.equals("")) {
		nytt(modinfo);
	}
	{
		if (comment==null) {
			comment="";
		}
		try {
			String	temp_manudu = modinfo.getRequest().getParameter("idega_calendar_month");
			String	temp_ar2 = modinfo.getRequest().getParameter("idega_calendar_year");
			String	temp_dom2 = modinfo.getRequest().getParameter("idega_calendar_day");

			idegaTimestamp stamp = new idegaTimestamp(temp_ar2+"-"+TextSoap.addZero(Integer.parseInt(temp_manudu))+"-"+TextSoap.addZero(Integer.parseInt(temp_dom2)));

			CalendarEntry entry = new CalendarEntry();
                            entry.setHeader(forda_id_string);
                            entry.setBody(comment);
                            entry.setDate(stamp.getTimestamp());
                            entry.setMemberId(-1);
                            //entry.setTo(-1);
                            //entry.setFrom(-1);
                            //entry.setTotal(-1);

                            entry.insert();

/*                        Faerslur faerslur = new Faerslur();
				faerslur.setFordiIdString(forda_id_string);
				faerslur.setComment(comment);
				faerslur.setDate(stamp.getTimestamp());

				faerslur.insert();
*/
//			add(stamp.getTimestamp().toString());

//			month(modinfo);

		}
		catch (Exception e) {
			villa = true;
		}

	}

	if (villa) {
		add(LANG[8]+"<br>");
		add(new BackButton(LANG[3]));
	}
	else {
/*		add("Skráning heppnaðist<br>");
		Link link = new Link("Áfram","/calendar.jsp");
			link.addParameter("action","calendar");
		add(link);
*/
			String	temp_manudu = modinfo.getRequest().getParameter("idega_calendar_month");
			String	temp_ar2 = modinfo.getRequest().getParameter("idega_calendar_year");
			String	temp_dom2 = modinfo.getRequest().getParameter("idega_calendar_day");

		modinfo.getResponse().sendRedirect("/calendar.jsp?idega_calendar_month="+temp_manudu+"&idega_calendar_year="+temp_ar2+"&idega_calendar_day="+temp_dom2);
	}

}

private void newEntry(ModuleInfo modinfo) throws SQLException {

        addMenu();
	HttpServletRequest request = modinfo.getRequest();


	String timi = request.getParameter("timi");
	double timifra;
	double timitil;

	if (timi != null ) {
	 	timifra = (new Double(timi)).doubleValue();
                timitil = timifra + 0.5;
	}
	else {
		timifra=8;
                timitil = timifra + 0.5;
	}

        int row = 1;

//	String meeting_id = request.getParameter("meeting_id");
//	ResultSet RS;
//	Statement Stmt = Conn.createStatement();


//	String group_ids[]=  request.getParameterValues("hopar");
//	String user_ids[]=  request.getParameterValues("userar");


//	Image myHeader = new Image("http://clarke.idega.is/timaskra2.swt?type=jpg&title="+FunctColl.getNameOfDay(FunctColl.getDayOfWeek(Integer.parseInt(ar2),Integer.parseInt(manudur),Integer.parseInt(dom2))).substring(0,3)+" "+dom2+". "+FunctColl.getShortNameOfMonth(Integer.parseInt(manudur))+"&subtitle="+dom2+"-"+FunctColl.getNameOfMonth(manudur)+"-"+ar2+" ");
//	Image myHeader = new Image("http://clarke.idega.is/timaskra2.swt?type=jpg&title=Tímaskráning&subtitle="+dom2+"-"+FunctColl.getNameOfMonth(manudur)+"-"+ar2+" ");

	Form myForm = new Form();
		myForm.setMethod("post");
		myForm.add(new HiddenInput("edit","save"));



        this.forwardDate(modinfo,myForm);

	add(myForm);




	Table myTable = new Table();
		myTable.setWidth("521");
		myTable.setBorder(0);
		myTable.setColor("#DDDDDD");
		myTable.setCellpadding(5);
		myTable.setCellspacing(0);
	myForm.add(myTable);


        Text theDate = new Text(this.getDate(modinfo));
          theDate.setFontColor(this.header_text_color);
          theDate.setBold();
        myTable.add(theDate,1,1);
        ++row;


	DropdownMenu myDropdown = new DropdownMenu("forda_id");
            if (member != null) {
                myDropdown.addMenuElement("-1",member.getName());
            }

                Resource[] resource = (Resource[])(new Resource()).findAllOrdered("resource_name");
                if (resource != null) {
                  if (resource.length > 0 ) {
                    for (int t = 0 ; t < resource.length ; t++) {
                        myDropdown.addMenuElement(resource[t].getID(), resource[t].getName());
                    }
                  }
                }
        myTable.add(myDropdown,2,row);


	DropdownMenu myDropdown2 = new DropdownMenu("verk_id");
                myDropdown2.addMenuElement(-1,"&nbsp;");
//                Project[] projects = (Project[]) (new Project()).findAllByColumnOrdered("valid","Y","name");
                  Project[] projects = (Project[]) member.findRelated(new Project());
                if (projects != null) {
                    if (projects.length > 0 ) {
                        for (int l = 0 ; l < projects.length ; l++) {
                            myDropdown2.addMenuElement(projects[l].getID(),projects[l].getName());                        }
                    }
                }


/*		Statement Stmt4= Conn.createStatement();
		ResultSet RS3 = Stmt4.executeQuery("select * from verk,fordi_verk where fordi_verk.forda_id='"+forda_id+"' AND verk.verk_id = fordi_verk.verk_id  order by verk_nafn");

		myDropdown2.addMenuElement("null","");
		while (RS3.next()) {
			myDropdown2.addMenuElement(RS3.getString("verk.verk_id"),RS3.getString("verk_nafn"));
		}
		Stmt4.close();
		RS3.close();
*/

	Text myText0 = new Text("Forði");
		myText0.setBold();
	myTable.add(myText0,1,row);

        row++;
	myTable.add(myDropdown2,2,row);


	Text myText1 = new Text("Verk");
		myText1.setBold();
	myTable.add(myText1,1,row);


        row++;
 	Text myText2 = new Text("Tími");
		myText2.setBold();
	myTable.add(myText2,1,row);

	String setja_inn,setja_inn2;
	int talan=7;
	DropdownMenu myDropdownMenu = new DropdownMenu("fra");
		for (double tilli = 8; tilli <=23.5; tilli=tilli+0.5) {
			if ((tilli%1) == 0) {
		  		++talan;
				setja_inn = (talan+":00");
			}
			else {
				setja_inn = (talan+":30");
			}
			myDropdownMenu.addMenuElement(Double.toString(tilli),setja_inn);
//			myDropdownMenu.addMenuElement(Double.toString(tilli),Double.toString(tilli));
		}
		myDropdownMenu.setSelectedElement(Double.toString(timifra));

	talan=8;
	DropdownMenu myDropdownMenu1 = new DropdownMenu("til");
		for (double till =8.5; till <= 24; till = till+0.5) {
			if ((till%1) == 0) {
				++talan;
				setja_inn2 = (talan+":00");
			}
			else {
				setja_inn2 = (talan+":30");
			}
			myDropdownMenu1.addMenuElement(Double.toString(till),setja_inn2);
//			myDropdownMenu1.addMenuElement(Double.toString(till),Double.toString(till));
				if (till <= timifra) {
//					myDropdownMenu1.setDisabled(setja_inn2);
					myDropdownMenu1.setDisabled(Double.toString(till));
				}
		}
		myDropdownMenu1.setSelectedElement(Double.toString(timitil));

	myTable.add(myDropdownMenu,2,row);
	myTable.addText(" - ",2,row);
	myTable.add(myDropdownMenu1,2,row);

        row++;
	Text myText4 = new Text("Lýsing");
		myText4.setBold();
		myTable.setAttribute(1,row,"valign","top");
	myTable.add(myText4,1,row);


	TextArea myTextArea = new TextArea("lysing");
		myTextArea.setWidth(50);
		myTextArea.setHeight(3);
	myTable.add(myTextArea,2,row);

        ++row;
	Text myText5 = new Text("Athugasemd");
		myText5.setBold();
		myTable.setAttribute(1,row,"valign","top");
	myTable.add(myText5,1,row);

	TextArea myTextArea1 = new TextArea("athugasemd");
		myTextArea1.setWidth(50);
		myTextArea1.setHeight(3);
	myTable.add(myTextArea1,2,row);


        ++row;
		myTable.setAttribute(2,row,"align","right");

//	myTable.add(new Link("Henda","jcalendarMeeting_admin.jsp?edit=henda&meeting_id="+meeting_id+" "),2,6);

	myTable.add(new SubmitButton("Vista"),2,row);
        myTable.add(new HiddenInput("idega_calendar_action","save_new_entry"),2,row);

        myTable.setRowColor(1,header_color);
        myTable.setRowColor(row,header_color);



/*	myForm.add(new HiddenInput("ar",ar2));
	myForm.add(new HiddenInput("daym",dom2));
	myForm.add(new HiddenInput("month",manudur));
*/
//	myPage.print(modinfo);

// nýr fundur skráður inn  búið


}


private void saveNewEntry(ModuleInfo modinfo) throws SQLException {
    String forda_id = modinfo.getParameter("forda_id");
    String verk_id = modinfo.getParameter("verk_id");
    String fra = modinfo.getParameter("fra");
    String til = modinfo.getParameter("til");
    String lysing = modinfo.getParameter("lysing");
    String athugasemd = modinfo.getParameter("athugasemd");


    CalendarEntry calEntry = new CalendarEntry();
        if (verk_id != null) {
          if (!verk_id.equals("-1")) {
            Project project = new Project(Integer.parseInt(verk_id));
            calEntry.setHeader(project.getName());
          }
        }

        calEntry.setMemberId(this.member_id);

        calEntry.setBody(" ");
        if ( (!lysing.equals("")) && (athugasemd.equals(""))  ) {
            calEntry.setBody(lysing);
        }
        else if ( (lysing.equals("")) && (!athugasemd.equals(""))  ) {
            calEntry.setBody(athugasemd);
        }
        else if (!(lysing.equals("")) && (!athugasemd.equals(""))  ) {
            calEntry.setBody(lysing + "<br>" +athugasemd);
        }

        if ((fra != null ) && (til != null)) {
            double from = Double.parseDouble(fra);
            double to = Double.parseDouble(til);
            if ( (to - from) >  0 ) {
                calEntry.setFrom(from);
                calEntry.setTo(to);
                calEntry.setTotal(to - from);
            }

        }

        calEntry.setDate(this.getTimestamp(modinfo));

        calEntry.insert();


    if (this.connect_to_timesheet) {
        TimesheetEntry entry = new TimesheetEntry();

          if (forda_id != null) {
            if (!forda_id.equals("-1")) {
              entry.setResourceId(Integer.parseInt(forda_id));
            }
          }

          if (verk_id != null) {
            if (!verk_id.equals("-1")) {
              entry.setProjectId(Integer.parseInt(verk_id));
            }
          }

          if ((fra != null ) && (til != null)) {
              double from = Double.parseDouble(fra);
              double to = Double.parseDouble(til);
              if ( (to - from) >  0 ) {
                  entry.setHowMany( to - from);
              }
              else {
                  entry.setHowMany(0);
              }
          }
          if (lysing != null) {
              entry.setDescription(lysing);
          }

          entry.setDate(this.getTimestamp(modinfo));
          entry.setMemberId(this.member_id);
          entry.setBooked(false);
          entry.setRegistered(false);

          entry.insert();

          calEntry.addTo(entry);

    }

    try {
       modinfo.getResponse().sendRedirect(URI);
    }
    catch (IOException io ) {
       System.err.println(io.getMessage());
    }

}

	public void main(ModuleInfo modinfo) throws Exception{
            this.empty();

            if (member == null) {
                member = (com.idega.data.genericentity.Member) modinfo.getSession().getAttribute("member_login");
            }
            try {
              this.member_id = this.member.getID();
            }
            catch (Exception e) {
            }

                this.isAdmin=super.isAdministrator(modinfo);


           setSpokenLanguage(modinfo);

		URI = modinfo.getRequest().getRequestURI();
		String action = modinfo.getRequest().getParameter("idega_calendar_action");

		if (action == null) {
  		  Calculate(modinfo);
			if (typa.equals("Dagur")) {
				day(modinfo);
			}
			else if (typa.equals("Vika")) {
				addLinks();
				week(modinfo);
			}
			else if (typa.equals("Manudur")) {
                                if (this.member != null) {
                                  addLinks();
                                }
				add(month(modinfo,false));
			}
			else if (typa.equals("Ar")) {
				add(year(modinfo));
			}
			else if (typa.equals("Schedule")) {
				schedule(modinfo);
			}
		}
		else {  // action != null
                    this.Calculate(modinfo);
			if (action.equals("idega_calendar_new")) {
//				add("gimmi");
				nytt(modinfo);
			}
			else if (action.equals("idega_calendar_nytt_save")) {
				saveNytt(modinfo);
			}
			else if (action.equals("idega_calendar_modify_save")) {
				saveModified(modinfo);
			}

			else if (action.equals("view")) {
				view(modinfo);
			}
			else if (action.equals("complete_list")) {
				viewToday(modinfo);
			}
			else if (action.equals(LANG[9])) {
				deleteEntry(modinfo);
			}
			else if (action.equals(LANG[10])) {
				modifyEntry(modinfo);
			}
                        else if (action.equals("new_entry")) {
                                newEntry(modinfo);
                        }
                        else if (action.equals("save_new_entry")) {
                                saveNewEntry(modinfo);
                        }
		}

	}

	/**
         * Sets a rounded corner on the left side of the category headline in each box. (only visible if setShowCategoryHeadline is set to true).
         */

        public void setLeftHeader(boolean leftHeader){
		this.leftHeader=leftHeader;
	}

	/**
         * Sets a rounded corner on the right side of the category headline in each box. (only visible if setShowCategoryHeadline is set to true).
         */

        public void setRightHeader(boolean rightHeader){
		this.rightHeader=rightHeader;
	}

	/**
         * Sets rounded corners on the category headline in each box. (only visible if setShowCategoryHeadline is set to true).
         */

        public void setRoundedHeader(boolean roundedHeader){
		this.leftHeader=roundedHeader;
		this.rightHeader=roundedHeader;
	}

        public void setOutlineWidth(int outlineWidth){
		this.outlineWidth=outlineWidth;
	}

        public void setHeadlineLeft(){
		this.headerLeft=true;

	}

        public void setHeaderSize(int headerSize){
		this.headerSize=headerSize;

	}

        public void setHeaderFontFace(String headerFontFace){
		this.headerFontFace=headerFontFace;

	}
}
