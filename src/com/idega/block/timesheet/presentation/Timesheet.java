package com.idega.block.timesheet.presentation;

import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.block.login.business.LoginBusiness;
import com.idega.core.user.data.User;

import com.idega.block.timesheet.business.*;
import java.util.GregorianCalendar;
import com.idega.block.timesheet.data.*;
import com.idega.data.*;
import java.io.*;
import java.sql.Timestamp;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import javax.servlet.http.*;
import com.idega.util.idegaTimestamp;
import java.sql.SQLException;
import java.util.*;
import com.idega.jmodule.days.data.*;
import com.idega.jmodule.calendar.data.CalendarEntry;
import com.idega.util.idegaCalendar;

import com.idega.util.text.*;

public class Timesheet extends JModuleObject{
    private boolean bookAllAtOnce = true;
    private boolean displayReportButton = true;
    private boolean allowCorrection = false;
    private boolean isPrintable = false;
    String timesheet_project_id = null;


    private boolean isAdmin;
    private String URI;
    private int daysShown;
    private int extraLines;
    public	int manudur,ar,dagur;
    private com.idega.util.idegaCalendar FunctColl = new com.idega.util.idegaCalendar();
    private int fjoldidaga;
    public	int	manudurInn;
    public	int arInn;
    private	String manadarnafn;
    private Text myDags;
    private String nameOfMonth;

    private User user;
    private int user_id;
    private String temp_user_id;

    private	String table_width;
    private	String table_height;
    private	int cellspacing;
    private	int cellpadding;
    private	String color_1;
    private	String color_2;
    private String header_color;
    private String header_text_color;
    private	int border;
    private boolean correct;


    private String edit;
//        private String URI = "";
    private String hour_report_image_url;
    private String delete_image_url;
    private String report_image_url;
    private String next_image_url;
    private String previous_image_url;
    private String today_image_url;
    private String correction_image_url;
    private String back_image_url;
    private String save_image_url;
    private String employee_report_image_url;
    private String book_image_url;
    private String booked_image_url;
    private String register_image_url;

    private String language = "IS";


    private idegaTimestamp stamp;
    private	int fjLinuIToflu=40;


////////////////////////////////////     STRENGIRNIR
     private String report_string = " ";
     private String  day_string = " ";
     private String  resource_string = " ";
     private String  project_string = " ";
     private String  quantity_string = " ";
     private String  description_string = " ";
     private String  delete_string = " ";
     private String  total_hours_string = " ";
     private String  previous_week_string = " ";
     private String  next_week_string = " ";
     private String  correct_string = " ";
     private String  back_string = " ";
     private String  today_string = " ";
     private String  save_string = " ";

     private String total_string = " ";
     private String previous_month_string = " ";
     private String next_month_string = " ";
     private String employee_report_string = " ";
     private String project_id_string = " ";
     private String hours_string = " ";
     private String project_name_string = " ";
     private String must_login = " ";

     private int userDefinedProjectId = -1;


  public synchronized Object clone() {
    Timesheet obj = null;
    try {
      obj = (Timesheet)super.clone();

//      obj.FunctColl = this.FunctColl.clone();
      if (this.myDags != null)
      obj.myDags = (Text) this.myDags.clone();
//      if (this.stamp != null)
//        obj.stamp = (idegaTimestamp)stamp.clone();

    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }


///////////////////////////////////

	public Timesheet() {
		super();
		setDefaultValues();
		setDate();
	}

	public Timesheet(boolean isAdmin) {
		super();
		setDefaultValues();
		this.isAdmin = isAdmin;
		setDate();
	}

	public Timesheet(int year, int month, int day) {
		super();
		setDefaultValues();
		ar = year;
		manudur = month;
		dagur = day;
	}

	public Timesheet(int year, int month, int day, boolean isAdmin) {
		super();
		setDefaultValues();
		ar = year;
		manudur = month;
		dagur = day;
		this.isAdmin = isAdmin;
	}

	private void setDefaultValues() {
		daysShown = 6;
		extraLines = 1;
		table_width = "20";
		cellspacing = 0;
		cellpadding = 3;
		color_1 = "#DDDDDD";
		color_2 = "#444444";
                header_color="#000000";
                header_text_color="#000000";
		border = 0;
                language = "IS";
                correct = false;
	}

        private void setStrings(ModuleInfo modinfo) {
          String temp_language = modinfo.getSpokenLanguage();
          if (temp_language != null) {
            language = temp_language;
          }

          if (language.equals("EN")) {
              report_string = "Reports";
              day_string = "Date";
              resource_string = "Resource";
              project_string = "Project";
              quantity_string = "Qty.";
              description_string = "Description";
              delete_string = "Delete";
              total_hours_string = "Total hours";
              previous_week_string = "Previous week";
              next_week_string = "Next week";
              correct_string = "Correction";
              back_string = "Back";
              today_string = "Today";
              save_string= "Save";
              total_string = "Total";
              previous_month_string = "Previous month";
              next_month_string = "Next month";
              employee_report_string = "Employee report";
              project_id_string = "Project #";
              hours_string = "Hours";
              project_name_string = "Name";
              must_login = "You have to log on first";

          }
          else {    // ef íslenska
              report_string = "Skýrslur";
              day_string = "Dagsetning";
              resource_string = "Forði";
              project_string = "Verk";
              quantity_string = "Eining";
              description_string = "Lýsing";
              delete_string = "Henda";
              total_hours_string = "Heildartímar";
              previous_week_string = "Fyrri vika";
              next_week_string = "Næsta vika";
              correct_string = "Leiðrétta";
              back_string = "Til baka";
              today_string = "Í dag";
              save_string= "Vista";
              total_string = "Samtals";
              previous_month_string = "Fyrri mánuður";
              next_month_string = "Næsti mánuður";
              employee_report_string = "Starfsmannaskýrsla";
              project_id_string = "Verknúmer";
              hours_string = "Tímar";
              project_name_string = "Verkheiti";
              must_login = "Þú verður að skrá þig inn fyrst";
          }

        }



/*        public void setLanguage(String language) {
          this.language = language
        }
*/

        public void setBookAllAtOnce(boolean bookAtOnce) {
            this.bookAllAtOnce = bookAtOnce;
        }

        public void setDisplayReportButton(boolean display) {
            this.displayReportButton = display;
        }

        public void setUserDefinedProjectId(int projectId) {
            this.userDefinedProjectId = projectId;
        }

        public void setAllowCorrection(boolean allowCorrection) {
            this.allowCorrection = allowCorrection;
        }

        public void setEmployeeReportImageUrl(String url) {
          this.employee_report_image_url = url;
        }
        public void setBookImageUrl(String url) {
          this.book_image_url = url;
        }
        public void setBookedImageUrl(String url) {
          this.booked_image_url = url;
        }
        public void setRegisterImageUrl(String url) {
          this.register_image_url = url;
        }

        public void setDeleteImageUrl(String url) {
          this.delete_image_url = url;
        }
        public void setSaveImageUrl(String url) {
          this.save_image_url = url;
        }
        public void setBackImageUrl(String url) {
          this.back_image_url = url;
        }
        public void setNextImageUrl(String url) {
          this.next_image_url = url;
        }
        public void setCorrectionImageUrl(String url) {
          this.correction_image_url = url;
        }

        public void setPreviousImageUrl(String url) {
          this.previous_image_url = url;
        }
        public void setTodayImageUrl(String url) {
          this.today_image_url = url;
        }

        public void setReportImageUrl(String url) {
          this.report_image_url = url;
        }

        public void setHourReportImageUrl(String url) {
            this.hour_report_image_url = url;
        }

	public void setDaysShown(int number_of_days_shown) {
		daysShown = number_of_days_shown;
	}

	public void setExtraLines(int number_if_extra_lines) {
		extraLines = number_if_extra_lines;
	}

	public void setWidth(int width) {
		table_width = Integer.toString(width);
	}

	public void setHeight(int height) {
		table_height = Integer.toString(height);
	}

	public void setCellpadding(int cellpadding) {
		this.cellpadding = cellpadding;
	}

	public void setCellspacing( int cellspacing) {
		this.cellspacing = cellspacing;
	}

	public void setColor(String color) {
		color_1 = color;
		color_2 = color;
	}

	public void setZebraColors(String color1, String color2){
		color_1 = color1;
		color_2 = color2;
	}

	public void setHeaderColor(String header_color) {
		this.header_color = header_color;
	}

	public void setHeaderTextColor(String header_text_color) {
		this.header_text_color = header_text_color;
	}

	public void setBorder(int border) {
		this.border = border;
	}


  private void initialize(ModuleInfo modinfo) throws SQLException {
    this.isAdmin = AccessControl.isAdmin(modinfo);
    user = LoginBusiness.getUser(modinfo);



    dagar(modinfo);
    calculate(modinfo);
    setStrings(modinfo);

  }

  public void main(ModuleInfo modinfo) throws  SQLException, IOException, Exception{
    initialize(modinfo);


    if (isAdmin) {
      Link prodMan = new Link("Verkefnastjórinn");
        prodMan.setWindowToOpen(com.idega.block.projectmanager.presentation.ProjectAdminWindow.class);
      add(prodMan);
      add("<br>");
    }


    URI = modinfo.getRequestURI();

          String sIsPrintable = modinfo.getParameter("i_timesheet_printable");
          if (sIsPrintable!= null) {
              if (sIsPrintable.equalsIgnoreCase("true")) {
                  this.isPrintable = true;
              }
          }
          timesheet_project_id = modinfo.getParameter("i_timesheet_project_id");




          if (edit == null) {
            edit = modinfo.getParameter("idega_timesheet_entry_edit");
          }

          URI = modinfo.getRequestURI();
          if (edit== null) {
            edit = "";
          }

      if (user != null) {
        user_id = user.getID();

          String temp_member_id = modinfo.getParameter("i_timesheet_member_id");
          if (temp_member_id != null) {
              try {
                  if (isAdmin ) {
                      user = new User(Integer.parseInt(temp_user_id));
                      this.user_id = user.getID();
                  }
              }
              catch (Exception e) {}
          }


        if (edit.equals("i_timesheet_correction")) {
          this.correct = true;
        }


        if (edit.equals("henda")) {
                  henda(modinfo);
        }
        else if (edit.equals(save_string)) {
                  save(modinfo);
        }
        else if (edit.equals("undirskyrsla")) {
                  undirskyrsla(modinfo);
                  if (!isPrintable) {
                      add(getPrintableLink("undirskyrsla"));
                  }
                  else {
                      add(getPrintButton());
                  }

        }
        else if (edit.equals("hreyfingStarfsmann")) {
                  hreyfingStarfsmann(modinfo);
                  if (!isPrintable) {
                      add(getPrintableLink("hreyfingStarfsmann"));
                  }
                  else {
                      add(getPrintButton());
                  }

        }
        else if (edit.equals("hreyfingVerk")) {
                  hreyfingVerk(modinfo);
                  if (!isPrintable) {
                  add(getPrintableLink("hreyfingVerk"));
                  }
                  else {
                      add(getPrintButton());
                  }
        }
        else if (edit.equals("hreyfingVerkAll")) {
                  hreyfingVerkAll(modinfo);
                  if (!isPrintable) {
                      add(getPrintableLink("hreyfingVerkAll"));
                  }
                  else {
                      add(getPrintButton());
                  }
        }
        else if (edit.equals("hour_pr_employee")) {
                  reportHourPrEmployee(modinfo);
                  if (!isPrintable) {
                      add(getPrintableLink("hour_pr_employee"));
                  }
                  else {
                      add(getPrintButton());
                  }
        }
        else if (edit.equals("hour_pr_project")) {
                  reportHourPrProject(modinfo);
                  if (!isPrintable) {
                      add(getPrintableLink("hour_pr_project"));
                  }
                  else {
                      add(getPrintButton());
                  }

        }
        else if (edit.equals("hour_pr_project_all")) {
                  reportHourPrProjectAll(modinfo);
                  if (!isPrintable) {
                      add(getPrintableLink("hour_pr_project_all"));
                  }
                  else {
                      add(getPrintButton());
                  }
        }
        else if (edit.equals("unbooked")) {
                showUnBooked(modinfo,false);
                  if (!isPrintable) {
                      add(getPrintableLink("unbooked"));
                  }
                  else {
                      add(getPrintButton());
                  }
        }
        else if (edit.equals("booked")) {
                showBooked(modinfo);
                  if (!isPrintable) {
                      add(getPrintableLink("booked"));
                  }
                  else {
                      add(getPrintButton());
                  }
        }
        else if (edit.equals("save_booked")) {
                saveBooked(modinfo);
        }
        else if (edit.equals("save_registered")) {
                saveRegistered(modinfo);
        }
        else if (edit.equals("my_projects")) {
                myProjects(modinfo);
        }
        else if (edit.equals("move_project")) {
                moveProjects(modinfo);
        }


        else if (edit.equals("checkPreviousEntries")) {
                showUnBooked(modinfo,true);
                  if (!isPrintable) {
                      add(getPrintableLink("checkPreviousEntries"));
                  }
                  else {
                      add(getPrintButton());
                  }
        }
        else {

                  drawTable(modinfo);
        }

        add(getMyProjectsLink());
      }
      else {
        add(must_login);
      }
  }


	private void setDate() {
		manudur = FunctColl.getMonth();
		ar      = FunctColl.getYear();
		dagur = FunctColl.getDay();
	}

        private void dagar(ModuleInfo modinfo) {

                try {
                  String temp_dagur = modinfo.getParameter("idega_timesheet_entry_dagur");
                  String temp_manudur = modinfo.getParameter("idega_timesheet_entry_manudur");
                  String temp_ar = modinfo.getParameter("idega_timesheet_entry_ar");

                  if (temp_manudur != null) {
                    manudur= Integer.parseInt(temp_manudur);
                  }
                  if (temp_dagur != null) {
                    dagur= Integer.parseInt(temp_dagur);
                  }
                  if (temp_ar != null) {
                    ar= Integer.parseInt(temp_ar);
                  }

                }
                catch (NumberFormatException n) {
                }


		if (dagur > FunctColl.getLengthOfMonth(manudur,ar) ) {
			dagur = dagur - FunctColl.getLengthOfMonth(manudur,ar);
			++manudur;
			if (manudur == 13) {
				manudur = 1;
				++ar;
			}

		}

		if ( (dagur) < 1) {
			--manudur;
			if (manudur ==0) {
				manudur=12;
				--ar;
			}
			dagur=FunctColl.getLengthOfMonth(manudur,ar)+dagur;
		}

                if ( manudur == 13 ) {
                  manudur = 1;
                  ++ar;
                }
                else if (manudur == 0) {
                  manudur = 12;
                  --ar;
                }




        }

	private void calculate(ModuleInfo modinfo) {

                  String temp_daysShown = modinfo.getParameter("idega_timesheet_entry_number_of_days");
                  if (temp_daysShown != null) {
                    try {
                      daysShown = Integer.parseInt(temp_daysShown);
                    }
                    catch (NumberFormatException n) {
                    }
                  }
                  String temp_extraLines = modinfo.getParameter("idega_timesheet_entry_number_of_lines");
                  if (temp_extraLines != null) {
                    try {
                      extraLines = Integer.parseInt(temp_extraLines);
                    }
                    catch (NumberFormatException n) {
                    }
                  }



		fjoldidaga = FunctColl.getLengthOfMonth(manudur,ar);

		manadarnafn = FunctColl.getNameOfMonth(manudur);
		String dags;
//		String plus_lina = request.getParameter("nylina");


		fjoldidaga = FunctColl.getLengthOfMonth(manudur,ar);
		manudurInn = manudur;
		arInn = ar;

		nameOfMonth =(FunctColl.getNameOfMonth(manudur));

		myDags = new Text();
                    myDags.setFontColor(this.header_text_color);
			myDags.setFontSize(3);
                        myDags.setBold();
			if ( (dagur - daysShown) < 1) {
				myDags.addToText(FunctColl.getNameOfMonth((manudur-1),modinfo)+"/");
			}
			myDags.addToText(FunctColl.getNameOfMonth(manudur,modinfo)+" "+ar);




	}

	private void drawTable(ModuleInfo modinfo) throws SQLException{
		boolean fridagur;
		String litur;
		String verk;
		String verk_id;
		double timar;
		String faersluid;
		String fordaid;
		String eining="Klst";
		String comment;

		boolean skrifaDags= true;

		int current_row=1;
		int current_id;
		int current_parent;
		String current_name;

		int vikuDagurNr;

		double heildartimar=0;
		double timaridag=0;
                String name = "";

		String dags;


/////////////////
                DropdownMenu resources = new DropdownMenu();
                  resources.setName("resource");

                  resources.addMenuElement(-1,(new User(this.user_id)).getName() );

                Resource[] res;
                  res = (Resource[]) modinfo.getServletContext().getAttribute("all_resource_array");
                if (res == null ) {
                    res = (Resource[])(new Resource()).findAllByColumnOrdered("is_closed","N","resource_name");
                    modinfo.getServletContext().setAttribute("all_resource_array",res);
                }

                if (res != null) {
                  String the_name;
                  if (res.length > 0 ) {
                    for (int j = 0 ; j < res.length ; j++) {
                      the_name = res[j].getName();
                          if (the_name.length() > 30) {
                            resources.addMenuElement(res[j].getID()+"",the_name.substring(0,30)+"...");
                          }
                          else{
                            resources.addMenuElement(res[j].getID()+"",the_name);
                          }

                    }
                  }
                }

                DropdownMenu projects = new DropdownMenu();
                if (userDefinedProjectId == -1) {
                      projects.setName("projects");

//                    com.idega.data.genericentity.Member memberja = com.idega.jmodule.login.business.AccessControl.getMember(modinfo);
                    TimesheetProject[] pro = (TimesheetProject[]) user.findRelated(new TimesheetProject());

                    if (pro != null) {
                      String the_name;
                      for (int j = 0 ; j < pro.length ; j ++) {
                        the_name = pro[j].getName();
                        if (the_name.length() > 30) {
                          projects.addMenuElement(pro[j].getID()+"",the_name.substring(0,30)+"...");
                        }
                        else{
                          projects.addMenuElement(pro[j].getID()+"",the_name);
                        }

                      }
                    }
                }

		DropdownMenu myDropdownTimar = new DropdownMenu("timar");
		myDropdownTimar.addMenuElement("null","&nbsp;");
		if (this.correct) {
			for (int i=0;i<=24;i++) {
                                if (i != 0)
				myDropdownTimar.addMenuElement("-"+i+".0","-"+i+".0");
                                if (i != 24)
				myDropdownTimar.addMenuElement("-"+i+".5","-"+i+".5");
			}
		}
		else {
			for (int i=0; i<=24;i++) {
                                if (i != 0)
				myDropdownTimar.addMenuElement(i+".0",i+".0");
                                if ( i != 24)
				myDropdownTimar.addMenuElement(i+".5",i+".5");
			}
		}





/////////////////////////
                User user = new User(user_id);




		Form myForm = new Form(URI);




		Table headerTable = this.getHeaderTable();
                        headerTable.add(myDags,2,1);
                    myForm.add(headerTable);

//                add(myDags);




		Table myTable = new Table();
			myForm.add(myTable);
			myTable.setWidth(table_width);
			if (table_height != null) {
				myTable.setHeight(table_height);
			}
			myTable.setCellspacing(cellspacing);
			myTable.setCellpadding(cellpadding);
			myTable.setColor(color_1);
			myTable.setBorder(border);



                Text dagur_text = new Text(day_string+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                  dagur_text.setFontColor(header_text_color);
                Text fordi_text = new Text(resource_string);
                  fordi_text.setFontColor(header_text_color);
                Text verk_text = new Text(project_string);
                  verk_text.setFontColor(header_text_color);
                Text eining_text = new Text(quantity_string);
                  eining_text.setFontColor(header_text_color);
                Text lysing_text = new Text(description_string);
                  lysing_text.setFontColor(header_text_color);

		myTable.add(dagur_text,1,1);
		myTable.add(fordi_text,2,1);
		myTable.add(verk_text,3,1);
		myTable.add(eining_text,4,1);
		myTable.add(lysing_text,5,1);
		myTable.setColumnAlignment(6,"center");

                TimesheetProject project;
                Resource resource;
                TimesheetEntry[] entry;

		for (int u=0;u<=daysShown;u++) {

			skrifaDags=true;
			fridagur = FunctColl.getHoliday(ar,manudur,(dagur-u));

			if ( (dagur-u) < 1) {
				--manudur;
				if (manudur ==0) {
					manudur=12;
					--ar;
				}
				dagur=FunctColl.getLengthOfMonth(manudur,ar)+dagur;
			}

			vikuDagurNr=FunctColl.getDayOfWeek(ar,manudur,(dagur)-u);

                        dags = (ar+"-"+manudur+"-"+(dagur-u));

			if ( (manudur < 10) && (dagur-u < 10) ) {
                          dags = (ar+"-0"+manudur+"-0"+(dagur-u));
                        }
                        else if ( (manudur >= 10) && (dagur-u < 10) ) {
                          dags = (ar+"-"+manudur+"-0"+(dagur-u));
                        }
                        else if ( (manudur < 10) && (dagur-u >= 10) ) {
                          dags = (ar+"-0"+manudur+"-"+(dagur-u));
                        }

                        //dags = (dagur-u+"."+manudur+"."+ar);

//                        entry = (com.idega.jmodule.timesheet.data.TimesheetEntry[]) (new com.idega.jmodule.timesheet.data.TimesheetEntry()).findAllByColumn("timesheet_entry_date",dags+"%");
                        entry = (com.idega.block.timesheet.data.TimesheetEntry[]) (new com.idega.block.timesheet.data.TimesheetEntry()).findAllByColumnOrdered("timesheet_entry_date",dags+"%","user_id",""+user_id,"timesheet_entry_id");
//myTable.add(" "+entry.length,1,(current_row+1));

                        if (entry != null)
                        for (int i = 0 ; i < entry.length ; i++ ) {
                          project = entry[i].getProject();
                          int resource_id = 0;
                          boolean resource_id_null = false;
                          if (project != null) {
        			++current_row;

				if (skrifaDags) {
					Text myText1 = new Text();
						myText1.setFontSize(1);
					if (fridagur) {
						myText1.setFontColor("red");
					}
					else {
					}
					myText1.addToText(FunctColl.getNameOfDay(vikuDagurNr,modinfo).substring(0,3)+" "+(dagur-u)+".");
				myTable.add(myText1,1,current_row);
				}
				skrifaDags=false;

                                try {
                                  resource_id = entry[i].getResourceId();
                                }
                                catch (Exception e) {
                                  resource_id_null = true;
                                }


                                if (resource_id_null) {
                                  myTable.add(user.getName(),2,current_row);
                                }


//				myTable.add( new HiddenInput("idega_timesheet_entry_project_id",project.getID()+""),3,current_row);
                                if (project.getName().length() > 27) {
                                        myTable.addText(project.getName().substring(0,26)+"...",3,current_row);
                                }
                                else {
                                        myTable.addText(project.getName(),3,current_row);
                                }



//                                myTable.add( new HiddenInput("idega_timesheet_entry_how_many",Double.toString(entry[i].getHowMany())),4,current_row);

                                if (!resource_id_null)
                                  if ( (resource_id != -1) && (resource_id != 0) ){
                                    resource = new Resource(resource_id);
                                    myTable.add(resource.getName(),2,current_row);

                                    if (resource.getUnitName() != null ) {
                                      eining = resource.getUnitName();
                                  }
                                }
                                else {
                                  myTable.add(user.getName(),2,current_row);
                                }

        			myTable.addText(entry[i].getHowMany()+" "+eining,4,current_row);

                                if (eining.equalsIgnoreCase("klst") ) {
                                      heildartimar+=entry[i].getHowMany();
                                      timaridag+=entry[i].getHowMany();
   			        }

				if (entry[i].getDescription().length() > 30) {
					myTable.addText(entry[i].getDescription().substring(0,30)+"...",5,current_row);
				}
				else {
					myTable.addText(entry[i].getDescription(),5,current_row);
				}

//				Image hendaImage = new Image("/pics/timereg/Henda-upp.gif","Henda");
//				Text myTextLink = new Text("Henda");
//					myTextLink.setFontColor("blue");
//					myTextLink.setFontSize(1);

//				myTable.add( new HiddenInput("timesheet_entry_id",entry[i].getID()+""),6,current_row);
//				myTable.add( new HiddenInput("timesheet_date",dags),6,current_row);

				if (!( entry[i].isBooked() )) {

                                  if (delete_image_url != null) {
					Image myTunna = new Image(delete_image_url,"Henda færslu");
                                          Link hlekkur = new Link(myTunna,URI);
                                            setLink(hlekkur,"henda",0,0,0);
                                          hlekkur.addParameter("idega_timesheet_entry_resource_id",resource_id);
                                          hlekkur.addParameter("idega_timesheet_entry_timesheet_entry_id",entry[i].getID());
                                          //hlekkur.addParameter("idega_timesheet_entry_member_id",member.getID());
                                          myTable.add(hlekkur,6,current_row);
                                   }
                                   else {
                                          Link hlekkur = new Link(delete_string,URI);
                                            setLink(hlekkur,"henda",0,0,0);
                                          hlekkur.addParameter("idega_timesheet_entry_resource_id",resource_id);
                                          hlekkur.addParameter("idega_timesheet_entry_timesheet_entry_id",entry[i].getID());
                                          //hlekkur.addParameter("idega_timesheet_entry_member_id",member.getID());
                                          myTable.add(hlekkur,6,current_row);
                                   }


                                          //				        myTable.add( new Link(myTunna,"timaskra_save.jsp?edit=henda&faerslu_id="+faersluid+
 //                                       "&forda_id="+fordaid +"&manudur="+manudur+"&ar="+ar+"&dagur="+dagur),6,current_row);
				}
				else {
//					myTable.addText("Bókað",6,current_row);
				}




                          }
                        }





/*
			if (u == fjLinuIToflu) {
				fjLinuIToflu = fjLinuIToflu*2;
				myTable.resize(6,fjLinuIToflu);
			}



			}
*/
			for (int y=1;y<=extraLines;y++) {
				++current_row;

						if (skrifaDags) {
							Text myTextAuka1 = new Text();
								myTextAuka1.setFontSize(1);
							if (fridagur) {
//							if (FunctColl.getHoliday(ar,manudur,dagur-u) ) {
								myTextAuka1.setFontColor("red");
							}
							else {
							}
							myTextAuka1.addToText(FunctColl.getNameOfDay(vikuDagurNr,modinfo).substring(0,3)+" "+(dagur-u)+".");
						myTable.add(myTextAuka1,1,current_row);
						}
						skrifaDags=false;


//              				myTable.add(myDropdownFordi,2,current_row);
                                                myTable.add(resources,2,current_row);
//                                                myTable.add(member.getName(),2,current_row);
                                                if (userDefinedProjectId == -1) {
		  				    myTable.add(projects,3,current_row);
                                                }
                                                else {
                                                    TimesheetProject userProject = new TimesheetProject(userDefinedProjectId);
                                                    myTable.add(userProject.getName(),3,current_row);
                                                    myTable.add(new HiddenInput("projects",""+userDefinedProjectId),3,current_row);
                                                }
//						myTable.add(myDropdownVerk,3,current_row);
						myTable.add(myDropdownTimar,4,current_row);
						TextInput myTextInputComment = new TextInput("description");
							myTextInputComment.setSize(30);
						myTable.add(myTextInputComment,5,current_row);
						myTable.add(new HiddenInput("timesheet_entry_id","null"),6,current_row);
						myTable.add(new HiddenInput("timesheet_date",dags),6,current_row);
                                                myTable.add(new HiddenInput("resource_id","0"),6,current_row);

				}
		++current_row;

			for (int j=1;j<=6;j++) {
				myTable.setColor(j,current_row,color_2);
			}
			myTable.addText(Double.toString(timaridag),4,current_row);
			timaridag=0;

		} //u endar;

                ++current_row;



        	myTable.addText(total_hours_string,3,current_row);
	        myTable.addText(Double.toString(heildartimar),4,current_row);


//        	myTable.add( new HiddenInput("idega_timesheet_entry_edit","save"),6,current_row);
        	myTable.add( new HiddenInput("idega_timesheet_entry_ar",Integer.toString(ar)),6,current_row);
        	myTable.add( new HiddenInput("idega_timesheet_entry_dagur",Integer.toString(dagur)),6,current_row);
        	myTable.add( new HiddenInput("idega_timesheet_entry_manudur",Integer.toString(manudur)),6,current_row);
                myTable.add( new HiddenInput("idega_timesheet_entry_number_of_days",""+daysShown),6,current_row);
                myTable.add( new HiddenInput("idega_timesheet_entry_number_of_lines",""+extraLines),6,current_row);


                ++current_row;


                if (previous_image_url != null) {
                  Image prev = new Image(previous_image_url,previous_week_string);
                  Link back = new Link(prev,URI);
                    setLink(back,"",0,0,-daysShown-1);
  		  myTable.add(back,2,current_row);
                }
                else {
                  Link back = new Link(previous_week_string,URI);
                    setLink(back,"",0,0,-daysShown-1);
       	  	    myTable.add(back,2,current_row);

                }

                myTable.add("&nbsp;&nbsp;",2,current_row);

		if (today_image_url != null) {
                  Image todayImg = new Image(today_image_url,today_string);
                  myTable.add( new Link(todayImg,URI),2,current_row);
                }
                else {
                  myTable.add( new Link(today_string,URI),2,current_row);
                }


                myTable.add("&nbsp;&nbsp;",2,current_row);


                if (next_image_url != null) {
                  Image nextImage = new Image(next_image_url,next_week_string);
                  Link forward = new Link(nextImage,URI);
                    setLink(forward,"",0,0,daysShown +1);
  		    myTable.add(forward,2,current_row);
                }
                else {
                  Link forward = new Link(next_week_string,URI);
                    setLink(forward,"",0,0,daysShown +1);
  		    myTable.add(forward,2,current_row);
                }

                if (this.displayReportButton) {
                    if (report_image_url == null) {
                      Link reports = new Link(report_string,URI);
                        setLink(reports,"undirskyrsla",0,0,0);
                      myTable.add(reports,5,current_row);
                    }
                    else {
                      Link reports = new Link(new Image(report_image_url,report_string),URI);
                        setLink(reports,"undirskyrsla",0,0,0);
                      myTable.add(reports,5,current_row);
                    }
                }




                if (allowCorrection) {
                    if (this.correction_image_url == null) {
                        Link correct = new Link("Leiðrétta",this.URI);
                            this.setLink(correct,"i_timesheet_correction",0,0,0);
                        myTable.add( correct,3,current_row);
                    }
                    else {
                        Link correct = new Link(new Image(correction_image_url),this.URI);
                            this.setLink(correct,"i_timesheet_correction",0,0,0);
                        myTable.add( correct,3,current_row);
                    }
                }



                if (save_image_url != null) {
                  Image saveImg = new Image(save_image_url, save_string);
  	           myTable.add( new SubmitButton(saveImg,"idega_timesheet_entry_edit",save_string),5,current_row);
                   myTable.add(new HiddenInput("idega_timesheet_entry_edit",save_string),6,current_row);
                }
                else {
   		  myTable.add( new SubmitButton("idega_timesheet_entry_edit",save_string ),5,current_row);
                   myTable.add(new HiddenInput("idega_timesheet_entry_edit",save_string),6,current_row);
                }
                myTable.setAlignment(5,current_row,"right");

		if (header_color != null) {
			myTable.setRowColor(1,header_color);
			myTable.setRowColor(current_row,header_color);
		}


		add(myForm);
	}


      private void undirskyrsla(ModuleInfo modinfo) throws SQLException{


		Text myText;
                Text myName;

                int resource_id;

		double timarverk = 0;
		double samtals = 0;

		int dagariman = FunctColl.getLengthOfMonth(manudur,ar);
		double[] timardag = new double[dagariman];


                String dags1 = (ar+"-"+manudur+"-01");
                String dags2 = (ar+"-"+manudur+"-"+(dagariman));

		if ( manudur < 10) {
                  dags1 = (ar+"-0"+manudur+"-01");
                  dags2 = (ar+"-0"+manudur+"-"+(dagariman));
                }


                TimesheetEntry[] entry = (TimesheetEntry[])(new TimesheetEntry()).findAll("select * from timesheet_entry where user_id="+user_id+" AND timesheet_entry_date >= '"+dags1+"' AND timesheet_entry_date <= '"+dags2+"' order by project_id,timesheet_entry_date");
                TimesheetEntry[] entry_count_projects = (TimesheetEntry[])(new TimesheetEntry()).findAll("select distinct project_id from timesheet_entry where user_id="+user_id+" AND timesheet_entry_date >= '"+dags1+"' AND timesheet_entry_date <= '"+dags2+"'");

                int rows = 0;
                if (entry_count_projects!= null) {
                  rows = entry_count_projects.length;
                }

		Form myForm = new Form();
		add(myForm);

		Table headerTable = new Table(3,1);
			headerTable.setCellpadding(0);
			headerTable.setCellspacing(0);
			headerTable.setColor(this.header_color);
			headerTable.setWidth(1,"17");
			headerTable.setWidth(3,"17");
			headerTable.setWidth(this.table_width);
			headerTable.setHeight("36");
			headerTable.setVerticalAlignment(1,1,"top");
			headerTable.setVerticalAlignment(2,1,"middle");
			headerTable.setVerticalAlignment(3,1,"top");
			headerTable.setAlignment(1,1,"left");
			headerTable.setAlignment(2,1,"center");
			headerTable.setAlignment(3,1,"right");
			//headerTable.setAlignment("center");

                        headerTable.add(new Image("/pics/jmodules/poll/leftcorner.gif",""),1,1);
                        headerTable.add(new Image("/pics/jmodules/poll/rightcorner.gif",""),3,1);


//                Text header = new Text(FunctColl.getNameOfMonth(manudur, modinfo) + " " +ar);
//                  header.setFontSize(5);
//                add(header);

                        Text nafnPaMoned = new Text("Unnir tímar í "+FunctColl.getNameOfMonth(manudur, modinfo) + " " +ar);
                            nafnPaMoned.setFontSize(3);
                            nafnPaMoned.setBold();
                            nafnPaMoned.setFontColor(this.header_text_color);

                        Text memberName = new Text( (new User(this.user_id)).getName());
                            memberName.setFontSize(3);
                            memberName.setBold();
                            memberName.setFontColor(this.header_text_color);

                        headerTable.add(memberName,2,1);
                        headerTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",2,1);
                        headerTable.add(nafnPaMoned,2,1);
                    myForm.add(headerTable);







		Table myTable= new Table(dagariman+4,rows+3);
		myForm.add(myTable);
		myTable.setBorder(border);
//                myTable.setBorder(1);
                myTable.setWidth(table_width);
                myTable.setColor(color_1);
//                myTable.setHorizontalZebraColored(color_1,color_2);
		myTable.setCellpadding(2);
		myTable.setCellspacing(0);
                Text project_id_text = new Text(project_string);
                  project_id_text.setFontColor(header_text_color);


                Text project_name_text = new Text(project_name_string+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
                  project_name_text.setFontColor(header_text_color);

//		myTable.add(project_id_text,1,1);
		myTable.add(project_name_text,2,1);
		myTable.setColumnAttribute(dagariman+3,"align","center");


		for (int j=1; j<=dagariman;j++) {
			myText = new Text(Integer.toString(j));
//                        myText.setFontSize(1);
                          myText.setFontColor(header_text_color);
			myTable.setWidth(2+j,"17");
			if (FunctColl.getHoliday(ar,manudur,j))
				myText.setFontColor("red");
				myTable.add(myText,2+j,1);
			timardag[j-1] = 0;
		}


                Text samtals_text1 = new Text(total_string);
                  samtals_text1.setFontColor(header_text_color);
		myTable.add(samtals_text1,dagariman+3,1);

		int currentrow=1;


                TimesheetEntry[] inside_entry;
                TimesheetProject project;
                int project_id;
                int project_id2 = -1;
                String today;
                String SQL_manudur = Integer.toString(manudur);
                double tala;
                double how_many_today;


                if (manudur < 10) {
                  SQL_manudur = "0"+manudur;
                }
                for ( int i = 0 ; i < rows ; i++) {

			timarverk=0;

                        project_id = entry_count_projects[i].getProjectId();

                        project = new TimesheetProject(project_id);

                        String project_name = project.getName();
                          if (project_name != null)
                            if (project_name.length() > 15) {
                              project_name = project_name.substring(0,15)+"...";
                            }

                        myName = new Text(project_name);
                          myName.setFontSize(1);
                        Link myLinkName = new Link(myName,this.URI);
                          setLink(myLinkName,"hreyfingVerk",0,0,0);
                          myLinkName.addParameter("i_timesheet_project_id",""+project.getID());

              		currentrow++;
                        myTable.add(myLinkName,2,currentrow);
                        for (int j=1; j <= dagariman; j++) {
                            if (j < 10) {
                                today = ar+"-"+SQL_manudur+"-0"+j;
                            }
                            else {
                                today = ar+"-"+SQL_manudur+"-"+j;
                            }

                            myTable.setColumnAttribute(j+2,"align","center");

                            inside_entry = (TimesheetEntry[]) (new TimesheetEntry()).findAll("select * from timesheet_entry where project_id="+project_id+" AND timesheet_entry_date = '"+today+"' AND user_id = "+this.user_id+"");
                            myTable.setVerticalAlignment(2+j,currentrow,"top");
                            if (inside_entry != null) {
                                how_many_today = 0;
                                myText = new Text("0");
                                myText.setFontSize(1);

                                for (int p = 0 ; p < inside_entry.length ; p++) {
                                    resource_id = inside_entry[p].getResourceId();
                                    if ( (resource_id == 0) || ( resource_id == -1) ) {
                                    tala = inside_entry[p].getHowMany();
                                        how_many_today += tala;
                                    }
                                }
                                if (how_many_today != 0) {
                                  myText.setText(Double.toString(how_many_today));
                                }
                                else {
                                  myText.setFontColor("gray");
                                }
                                myTable.add(myText,2+j,currentrow);

                                timarverk += how_many_today;
                                timardag[j-1] += how_many_today;
                            }
                            else {
				myTable.addText("&nbsp; ",2+j,currentrow);
                            }



                        }

			myText =new Text(Double.toString(timarverk));
			myText.setBold();
			myText.setFontSize("1");
                        myTable.setVerticalAlignment(dagariman+3, currentrow,"top");
			myTable.add(myText,dagariman+3,currentrow);

		//	myTable.add(new CheckBox("project_id",""+project_id) ,dagariman+4,currentrow);
        		samtals += timarverk;
 //                   }

                    project_id2 = project_id;

                }

                Text samtals_text = new Text(total_string);
                    samtals_text.setFontSize(1);
//                  samtals_text.setFontColor(header_text_color);

		myTable.add(samtals_text,2,rows+2);
		myTable.setAttribute(2,rows+2, "align","right");


		for (int k=1;k<=dagariman;k++) {
			if (timardag[k-1] != 0) {
  			        myText = new Text(Double.toString(timardag[k-1]));
				myText.setFontSize("1");
				myText.setFontColor("black");
				myText.setBold();
				myTable.add(myText,2+k,rows+2);
			}
			else {

				myText = new Text("0");
				myText.setFontSize("1");
				myText.setFontColor("gray");

				myTable.add(myText,2+k,rows+2);

				//myTable.addText(Integer.toString(timardag[k-1]),2+k,rows);
			}
		}
		myText = new Text(Double.toString(samtals));
                myText.setFontSize(1);
		myText.setBold();
		myTable.setAlignment(dagariman+3,rows+2,"center");
		myTable.add(myText,dagariman+3,rows+2);

//		myTable3.add(new SubmitButton(new Image("idega_timesheet_entry_edit","Velja")),dagariman+4,rows+2);

                myTable.mergeCells(1,rows+3,25,rows+3);
                myTable.mergeCells(26,rows+3,dagariman+3,rows+3);


                myTable.add("&nbsp;&nbsp;&nbsp;",1,rows+3);

                if (!isPrintable) {
                    Link bakka = this.getPreviousMonthLink("undirskyrsla");
                    Link afram = this.getNextMonthLink("undirskyrsla");
                    myTable.add(bakka,1,rows+3);
      //                myTable.add("&nbsp;&nbsp;&nbsp;",1,rows+3);
                    myTable.add(afram,1,rows+3);

      //                add("&nbsp;&nbsp;&nbsp;");

                    if (employee_report_image_url != null) {
                      Image staffRep = new Image(employee_report_image_url, employee_report_string);
                      Link starfsmanns = new Link(staffRep,URI);
                        setLink(starfsmanns,"hreyfingStarfsmann",0,0,0);
                      myTable.add(starfsmanns,26,rows+3);
                    }
                    else {
                      Link starfsmanns = new Link(employee_report_string,URI);
                        setLink(starfsmanns,"hreyfingStarfsmann",0,0,0);
                      myTable.add(starfsmanns,26,rows+3);
                    }
                }




		myForm.add(new HiddenInput("edit","valinskyrsla"));
		myForm.add(new HiddenInput("manudur",manudur+""));
		myForm.add(new HiddenInput("ar",ar+""));

//		myTable.add(new SubmitButton(new Image("/pics/timereg/Velja-upp.gif","Velja")),4+dagariman,rows+2);
		//myForm.add(new SubmitButton("Velja"));



              myTable.setHorizontalZebraColored(color_1,color_2);
              myTable.setRowColor(1,header_color);
              myTable.setRowColor(rows+3,header_color);


      }
      //undirskyrsla

 private void hreyfingVerk(ModuleInfo modinfo) throws SQLException{
	String project_id = modinfo.getParameter("i_timesheet_project_id");

        if (project_id != null) {


	  int dagariman = FunctColl.getLengthOfMonth(manudur,ar);
          String dags1 = (ar+"-"+manudur+"-01");
          String dags2 = (ar+"-"+manudur+"-"+(dagariman));

          if ( manudur < 10) {
             dags1 = (ar+"-0"+manudur+"-01");
             dags2 = (ar+"-0"+manudur+"-"+(dagariman));
          }

          User user = new User(this.user_id);
          TimesheetEntry[] entry = (TimesheetEntry[])(new TimesheetEntry()).findAll("select * from timesheet_entry where user_id="+this.user_id+" AND timesheet_entry_date >= '"+dags1+"' AND timesheet_entry_date <= '"+dags2+"' and project_id ="+project_id+" order by timesheet_entry_date");

          movementVerk(modinfo, entry, project_id, false);

          }
  }


  private void hreyfingVerkAll(ModuleInfo modinfo) throws SQLException{
	String project_id = modinfo.getParameter("i_timesheet_project_id");

        if (isAdmin) {
            if (project_id != null) {


              int dagariman = FunctColl.getLengthOfMonth(manudur,ar);
              String dags1 = (ar+"-"+manudur+"-01");
              String dags2 = (ar+"-"+manudur+"-"+(dagariman));

              if ( manudur < 10) {
                 dags1 = (ar+"-0"+manudur+"-01");
                 dags2 = (ar+"-0"+manudur+"-"+(dagariman));
              }

              TimesheetEntry[] entry = (TimesheetEntry[])(new TimesheetEntry()).findAll("select * from timesheet_entry where timesheet_entry_date >= '"+dags1+"' AND timesheet_entry_date <= '"+dags2+"' and project_id ="+project_id+" order by timesheet_entry_date");

              movementVerk(modinfo, entry, project_id, true);

              }

          }
          else {
              hreyfingVerk(modinfo);
          }
  }

  private void movementVerk(ModuleInfo modinfo, TimesheetEntry[] entry, String project_id, boolean viewAll) throws SQLException {

          String edit_string = "hreyfingVerk";
          String hour_report_string = "hour_pr_project";
          User current_user = this.user;
          if (viewAll) {
              edit_string = "hreyfingVerkAll";
              hour_report_string = "hour_pr_project_all";
          }

          TimesheetProject project = new TimesheetProject(Integer.parseInt(project_id));

	  double vinnaSamtals = 0;
          double aksturSamtals = 0;

  	  String varanafn;
	  int rows= entry.length;
          int row= 1;


          Table headerTable = this.getHeaderTable();

                Text nafnPaMoned = new Text("Verkskýrsla&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +FunctColl.getNameOfMonth(manudur, modinfo) + " " +ar);
                      nafnPaMoned.setFontSize(3);
                      nafnPaMoned.setBold();
                      nafnPaMoned.setFontColor(this.header_text_color);

                Text projectName = new Text(project.getName());
                      projectName.setFontSize(3);
                      projectName.setBold();
                      projectName.setFontColor(this.header_text_color);

                headerTable.add(projectName,2,1);
                      headerTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",2,1);
                      headerTable.add(nafnPaMoned,2,1);
                add(headerTable);





		Table myTable= new Table();
  			myTable.setWidth(this.table_width);
			add(myTable);
			myTable.setCellspacing(0);
			myTable.setCellpadding(0);
			myTable.setBorder(border);

               Text dags = new Text("Dagsetning");
               Text the_member = new Text("Starfsmaður");
               Text the_resource = new Text("Forði");
               Text horas = new Text("Tímar");
               Text miles = new Text("Akstur");
               Text other = new Text("Tækjanotkun (ein/stk)");
                  dags.setFontColor(this.header_text_color);
                  the_member.setFontColor(this.header_text_color);
                  the_resource.setFontColor(this.header_text_color);
                  horas.setFontColor(this.header_text_color);
                  miles.setFontColor(this.header_text_color);
                  other.setFontColor(this.header_text_color);

		myTable.add(dags,2,row);
		myTable.add(the_member,3,row);
		myTable.add(the_resource,4,row);
		myTable.add(horas,5,row);
		myTable.add(miles,6,row);
		myTable.mergeCells(7,1,8,row);
		myTable.add(other,7,row);


                String resource_id = null;
                String resource_name = null;
                String resource_unit_name = null;

              Text themDags;
              Text themMember;
              Text themResource;
              Text themHours;
              Text themMiles;
              Text themOther;
              int fontSize = 1;

              for (int i = 0; i < entry.length; i++) {
                    resource_id = null;
                    resource_name = null;
                    resource_unit_name = null;
                    if (entry[i].getResource() != null) {
                        Resource resource = new Resource(entry[i].getResourceId());
                        resource_id = Integer.toString(resource.getID());
                        resource_name = resource.getName();
                        resource_unit_name = resource.getUnitName();
                    }

                    ++row;
                      themDags = new Text(entry[i].getDate().toString().substring(0,10));
                    if (viewAll) {
                      current_user = entry[i].getUser();
                    }
                      themMember = new Text(current_user.getName());
                        themDags.setFontSize(fontSize);
                        themMember.setFontSize(fontSize);

                      myTable.add(themDags,2,row);
                      myTable.add(themMember,3,row);

                      if (resource_name != null) {
                        themResource = new Text(resource_name);
                          themResource.setFontSize(fontSize);

                        myTable.add(themResource,4,row);
                        if (resource_unit_name != null) {
                          if (resource_unit_name.equalsIgnoreCase("klst")) {
                              themHours = new Text(""+entry[i].getHowMany());
                                themHours.setFontSize(fontSize);
                              myTable.add(themHours,5,row);
                              vinnaSamtals += entry[i].getHowMany();
                          }
                          else if (resource_unit_name.equalsIgnoreCase("km")) {
                              themMiles = new Text(""+entry[i].getHowMany());
                                  themMiles.setFontSize(fontSize);
                              myTable.add(themMiles,6,row);
                              aksturSamtals += entry[i].getHowMany();
                          }
                          else {
                              themOther = new Text(""+entry[i].getHowMany());
                                  themOther.setFontSize(fontSize);
                              myTable.add(themOther,7,row);
                          }

                        }
                      }
                      else {
                        myTable.add("-",4,row);
                        themHours = new Text(""+entry[i].getHowMany());
                            themHours.setFontSize(fontSize);
                        myTable.add(themHours,5,row);
                        vinnaSamtals += entry[i].getHowMany();
                      }
              }

              row++;
                  Text totalWork = new Text(""+vinnaSamtals);
                      totalWork.setFontSize(fontSize);
                  Text totalMiles = new Text(""+aksturSamtals);
                      totalMiles.setFontSize(fontSize);
                  myTable.add(totalWork,5,row);
                  myTable.add(totalMiles,6,row);

              row++;


                  myTable.mergeCells(2,row,5,row);
                  myTable.setHeight(row,"34");
                  myTable.setVerticalAlignment(2,row,"middle");

                Link bakka = this.getPreviousMonthLink(edit_string);
                  if (bakka != null)
                  bakka.addParameter("i_timesheet_project_id",project_id);
                Link afram = this.getNextMonthLink(edit_string);
                  if (afram != null)
                  afram.addParameter("i_timesheet_project_id",project_id);

    		  myTable.add(bakka,2,row);
                  myTable.add(afram,2,row);


              if (this.hour_report_image_url != null) {
                Image repImg = new Image(hour_report_image_url);
                Link hour_report = new Link(repImg,this.URI);
                  setLink(hour_report,hour_report_string,0,0,0);
                  hour_report.addParameter("i_timesheet_project_id",project_id);
		myTable.add(hour_report,7,rows+3);
              }
              else {
                Link hour_report = new Link("Tímaskýrsla",this.URI);
                  setLink(hour_report,hour_report_string,0,0,0);
                  hour_report.addParameter("i_timesheet_project_id",project_id);
		myTable.add(hour_report,7,rows+3);
              }




              myTable.setHorizontalZebraColored(this.color_1, this.color_2);
              myTable.setRowColor(1,this.header_color);
              myTable.setRowColor(row,this.header_color);



}  //  hreyfingVerk endar


private void hreyfingStarfsmann(ModuleInfo modinfo) throws SQLException{

		int dagariman = FunctColl.getLengthOfMonth(manudur,ar);
		double vinnaSamtals = 0;
		double aksturSamtals = 0;
//		double annadSamtals = 0;

                String dags1 = (ar+"-"+manudur+"-01");
                String dags2 = (ar+"-"+manudur+"-"+(dagariman));

		if ( manudur < 10) {
                  dags1 = (ar+"-0"+manudur+"-01");
                  dags2 = (ar+"-0"+manudur+"-"+(dagariman));
                }




                TimesheetEntry[] entry = (TimesheetEntry[])(new TimesheetEntry()).findAll("select * from timesheet_entry where user_id="+this.user_id+" AND timesheet_entry_date >= '"+dags1+"' AND timesheet_entry_date <= '"+dags2+"' order by timesheet_entry_date,project_id");
                com.idega.data.genericentity.Member member = new com.idega.data.genericentity.Member();
                TimesheetProject project;
                //TimesheetEntry[] entry_count_projects = (TimesheetEntry[])(new TimesheetEntry()).findAll("select distinct project_id from timesheet_entry where member_id="+member_id+" AND timesheet_entry_date >= '"+dags1+"' AND timesheet_entry_date <= '"+dags2+"'");


		int rows=0;
                if (entry != null) {
                  rows = entry.length;
                }


//                Text header = new Text(FunctColl.getNameOfMonth(manudur, modinfo) + " " +ar);
                Text myText;


		Table headerTable = this.getHeaderTable();

                        Text nafnPaMoned = new Text(FunctColl.getNameOfMonth(manudur, modinfo) + " " +ar);
                            nafnPaMoned.setFontSize(3);
                            nafnPaMoned.setBold();
                            nafnPaMoned.setFontColor(this.header_text_color);

                        Text memberName = new Text("Starfsmannaskýrsla&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + (new User(this.user_id)).getName());
                            memberName.setFontSize(3);
                            memberName.setBold();
                            memberName.setFontColor(this.header_text_color);

                        headerTable.add(memberName,2,1);
                        headerTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",2,1);
                        headerTable.add(nafnPaMoned,2,1);
                    add(headerTable);

                int row = 1;

		Table myTable= new Table();
  			myTable.setWidth(this.table_width);
			add(myTable);
			myTable.setCellspacing(0);
			myTable.setCellpadding(0);
			myTable.setBorder(border);

               Text dags = new Text("Dagsetning");
               Text the_member = new Text("Verkefni");
               Text the_resource = new Text("Forði");
               Text horas = new Text("Tímar");
               Text miles = new Text("Akstur");
               Text other = new Text("Tækjanotkun (ein/stk)");
                  dags.setFontColor(this.header_text_color);
                  the_member.setFontColor(this.header_text_color);
                  the_resource.setFontColor(this.header_text_color);
                  horas.setFontColor(this.header_text_color);
                  miles.setFontColor(this.header_text_color);
                  other.setFontColor(this.header_text_color);

		myTable.add(dags,2,row);
		myTable.add(the_member,3,row);
		myTable.add(the_resource,4,row);
		myTable.add(horas,5,row);
		myTable.add(miles,6,row);
		myTable.mergeCells(7,1,8,row);
		myTable.add(other,7,row);


                String resource_id = null;
                String resource_name = null;
                String resource_unit_name = null;

              Text themDags;
              Text themProject;
              Text themResource;
              Text themHours;
              Text themMiles;
              Text themOther;
              int fontSize = 1;

              for (int i = 0; i < entry.length; i++) {
                    project = new TimesheetProject(entry[i].getProjectId());
                    resource_id = null;
                    resource_name = null;
                    resource_unit_name = null;
                    if (entry[i].getResource() != null) {
                        Resource resource = new Resource(entry[i].getResourceId());
                        resource_id = Integer.toString(resource.getID());
                        resource_name = resource.getName();
                        resource_unit_name = resource.getUnitName();
                    }

                    ++row;
                      themDags = new Text(entry[i].getDate().toString().substring(0,10));
                      themProject = new Text(project.getName());
                        themDags.setFontSize(fontSize);
                        themProject.setFontSize(fontSize);

                      myTable.add(themDags,2,row);
                      myTable.add(themProject,3,row);

                      if (resource_name != null) {
                        themResource = new Text(resource_name);
                          themResource.setFontSize(fontSize);

                        myTable.add(themResource,4,row);
                        if (resource_unit_name != null) {
                          if (resource_unit_name.equalsIgnoreCase("klst")) {
                              themHours = new Text(""+entry[i].getHowMany());
                                themHours.setFontSize(fontSize);
                              myTable.add(themHours,5,row);
                              vinnaSamtals += entry[i].getHowMany();
                          }
                          else if (resource_unit_name.equalsIgnoreCase("km")) {
                              themMiles = new Text(""+entry[i].getHowMany());
                                  themMiles.setFontSize(fontSize);
                              myTable.add(themMiles,6,row);
                              aksturSamtals += entry[i].getHowMany();
                          }
                          else {
                              themOther = new Text(""+entry[i].getHowMany());
                                  themOther.setFontSize(fontSize);
                              myTable.add(themOther,7,row);
                          }

                        }
                      }
                      else {
                        myTable.add("-",4,row);
                        themHours = new Text(""+entry[i].getHowMany());
                            themHours.setFontSize(fontSize);
                        myTable.add(themHours,5,row);
                        vinnaSamtals += entry[i].getHowMany();
                      }
              }

              row++;
                  Text totalWork = new Text(""+vinnaSamtals);
                      totalWork.setFontSize(fontSize);
                  Text totalMiles = new Text(""+aksturSamtals);
                      totalMiles.setFontSize(fontSize);
                  myTable.add(totalWork,5,row);
                  myTable.add(totalMiles,6,row);

              row++;

              if (!isPrintable) {
                  if (this.hour_report_image_url != null) {
                    Image repImg = new Image(hour_report_image_url);
                    Link hour_report = new Link(repImg,this.URI);
                      setLink(hour_report,"hour_pr_employee",0,0,0);
                    myTable.add(hour_report,7,rows+3);
                  }
                  else {
                    Link hour_report = new Link("Tímaskýrsla",this.URI);
                      setLink(hour_report,"hour_pr_employee",0,0,0);
                    myTable.add(hour_report,7,rows+3);
                  }
              }


              Link bakka = this.getPreviousMonthLink("hreyfingStarfsmann");
              Link afram = this.getNextMonthLink("hreyfingStarfsmann");


              myTable.add(bakka,2,rows+3);
              myTable.add(afram,2,rows+3);


                      myTable.setHorizontalZebraColored(color_1,color_2);
			myTable.setRowColor(1,header_color);
			myTable.setRowColor(rows+3,header_color);
}






      public void henda(ModuleInfo modinfo) throws Exception{

          String entry_id = modinfo.getParameter("idega_timesheet_entry_timesheet_entry_id");
          String member_id =modinfo.getParameter("i_timesheet_member_id");
          int member_id_int = Integer.parseInt(member_id);


          TimesheetEntry[] entry = (TimesheetEntry[]) (new TimesheetEntry()).findAllByColumn("timesheet_entry_id",entry_id);
          CalendarEntry[] calEntry;

          if (entry != null) {
            for (int i = 0 ; i < entry.length ; i++) {
              if ( (entry[i].getUserId() == member_id_int) || (!entry[i].isBooked()) ) {
                  calEntry = (CalendarEntry[]) entry[i].findReverseRelated(new CalendarEntry());
                  for (int j = 0; j < calEntry.length; j++) {
                      calEntry[j].removeFrom(entry[i]);
                      calEntry[j].delete();
                  }
                entry[i].delete();
              }
            }
          }

          edit = "";
          main(modinfo);
      }

      private void save(ModuleInfo modinfo) throws SQLException{

//		String member_id = modinfo.getParameter("member_id");
		String project_id[] = modinfo.getParameterValues("projects");
		String resource_id[] = modinfo.getParameterValues("resource");
		String timar[] = modinfo.getParameterValues("timar");
		String entry_id[] = modinfo.getParameterValues("timesheet_entry_id");
		String date[] = modinfo.getParameterValues("timesheet_date");
		String description[] = modinfo.getParameterValues("description");



                if (entry_id != null) {
                System.out.println("");
			idegaTimestamp stamp;
                        String timarString = "0";
			for (int telja=0;telja<date.length;telja++) {
              		    if (project_id != null) {
				if ( entry_id[telja].equals("null") ){
                                     if (!timar[telja].equals("null")) {

                                        TimesheetEntry entry = new TimesheetEntry();

                                        stamp = new idegaTimestamp(date[telja]);

                                        entry.setDate(stamp.getTimestamp());
                                        entry.setUserId(user_id);

                                        if (resource_id != null)
                                        if (! resource_id[telja].equals("-1") ) {
                                        if (! resource_id[telja].equals("0") ) {
                                          entry.setResourceId(Integer.parseInt(resource_id[telja]));
                                          }
                                        }
                                        entry.setProjectId(Integer.parseInt(project_id[telja]));

                                        timarString = timar[telja];

                                        entry.setHowMany(Double.parseDouble(timarString));

                                        entry.setDescription(description[telja]);

                                        if ((Double.parseDouble(timarString) < 0) && (description[telja].equalsIgnoreCase("")) ){
                                          entry.setDescription("Leiðrétt af : "+user.getName());
                                        }

                                        entry.setBooked(false);
                                        entry.setRegistered(false);
                                        entry.insert();
                                     }

			        }
/*				else {
                                        TimesheetEntry entry = new TimesheetEntry(Integer.parseInt(entry_id[telja]));

                                        stamp = new idegaTimestamp(date[telja]);

                                        entry.setDate(stamp.getTimestamp());
                                        entry.setMemberId(member_id);
                                        if (!resource_id[telja].equals("0")) {
                                          entry.setResourceId(Integer.parseInt(resource_id[telja]));
                                        }
                                        entry.setProjectId(Integer.parseInt(project_id[telja]));

                                        timarString = timar[telja];

                                        entry.setHowMany(Double.parseDouble(timarString));

                                        entry.setDescription(description[telja]);
                                        entry.setBooked(false);
                                        entry.setRegistered(false);
                                        entry.update();
				}
*/			}
		  }
                }

          drawTable(modinfo);
      }
//end save


       private void reportHourPrEmployee(ModuleInfo modinfo) throws SQLException {

            int dagariman = FunctColl.getLengthOfMonth(manudur,ar);
            idegaCalendar cal = new idegaCalendar();
            Timestamp stamp;
            idegaTimestamp i_stamp;

            Days[] days = (Days[])(new Days()).findAllOrdered("days_id");

            double[] hoursPerDay = new double[8];
            for (int i = 0; i < days.length; i++) {
                hoursPerDay[days[i].getID()] = days[i].getWorkHours();
            }

            double totalHours = 0;
            double totalOvertime = 0;
            double totalDay = 0;
            double totalOverhour = 0;

            int tableWidth = (int) ( 2 *(Integer.parseInt(table_width) / 3) );

            Table headerTable = this.getHeaderTable();
                  headerTable.setWidth(tableWidth);
                    Text nafnPaMoned = new Text("Tímaskýrsla&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + FunctColl.getNameOfMonth(manudur, modinfo) + " " +ar);
                        nafnPaMoned.setFontSize(3);
                        nafnPaMoned.setBold();
                        nafnPaMoned.setFontColor(this.header_text_color);

                    Text memberName = new Text( (new User(this.user_id)).getName());
                        memberName.setFontSize(3);
                        memberName.setBold();
                        memberName.setFontColor(this.header_text_color);

                    headerTable.add(memberName,2,1);
                    headerTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",2,1);
                    headerTable.add(nafnPaMoned,2,1);
                add(headerTable);

          Table table = new Table();
            table.setWidth(tableWidth);
            table.setCellpadding(0);
            table.setCellspacing(0);
            add(table);

          int row = 1;

          Text dayTxt = new Text("dags");
//          Text projectTxt = new Text("verk");
          Text hoursTxt = new Text("tímar");
          Text dayhourTxt = new Text("dagvinna");
          Text overhourTxt = new Text("yfirvinna");
          Text differenceTxt = new Text("mismunur");
              dayTxt.setFontColor(header_text_color);
//              projectTxt.setFontColor(header_text_color);
              hoursTxt.setFontColor(header_text_color);
              dayhourTxt.setFontColor(header_text_color);
              overhourTxt.setFontColor(header_text_color);
              differenceTxt.setFontColor(header_text_color);

          table.add(dayTxt,2,row);
//          table.add(projectTxt,3,row);
          table.add(hoursTxt,3,row);
          table.add(dayhourTxt,4,row);
          table.add(overhourTxt,5,row);
          table.add(differenceTxt,6,row);


          Text dayTxt_reusable;
//          Text projectTxt_reusable;
          Text hoursTxt_reusable;
          Text dayhourTxt_reusable;
          Text overhourTxt_reusable;
          Text overtimeTxt_reusable;

            String dagsString = "";
            TimesheetEntry[] entry;

          int day_of_week;
          double total_hour;
          double day_hour;
          double over_hour;
          double over_time;


            for (int i = 1; i < dagariman+1; i++) {
                ++row;

                entry = (TimesheetEntry[])(new TimesheetEntry()).findAll("select * from timesheet_entry where user_id="+this.user_id+" AND timesheet_entry_date = '"+ar+"-"+manudur+"-"+i+"'  order by timesheet_entry_date,project_id");

                day_of_week = cal.getDayOfWeek(ar,manudur,i);

                dayTxt_reusable = new Text(ar+"-"+TextSoap.addZero(manudur)+"-"+TextSoap.addZero(i));
                    dayTxt_reusable.setFontSize(1);
                table.add(dayTxt_reusable,2,row);

                total_hour = 0;
                day_hour = 0;
                over_hour = 0;
                over_time = 0;

                for (int j = 0; j < entry.length; j++) {
                      total_hour += entry[j].getHowMany();
                }

                if (cal.isHoliday(ar,manudur,i)) {
                    dayTxt_reusable.setFontColor("red");
//                    dayTxt_reusable.addToText(" - "+cal.getHolidayName(ar,manudur,i) );
                    over_hour = total_hour;
                    over_time = total_hour;
                }
                else {
                        if (total_hour > hoursPerDay[day_of_week]) {
                            over_hour = total_hour - hoursPerDay[day_of_week];
                            day_hour = hoursPerDay[day_of_week];
                            over_time = total_hour - hoursPerDay[day_of_week];
                        }
                        else if (total_hour < hoursPerDay[day_of_week]) {
                            over_hour =total_hour - hoursPerDay[day_of_week];
                            day_hour = total_hour;
                            over_time = 0;
                        }
                        else {
                          day_hour = total_hour;
                          over_hour = 0;
                          over_time = 0;
                        }
                }


                hoursTxt_reusable = new Text(""+total_hour);
                dayhourTxt_reusable = new Text(""+day_hour);
                overhourTxt_reusable = new Text(""+over_hour);
                overtimeTxt_reusable = new Text(""+over_time);


                if (total_hour == 0) {
                    hoursTxt_reusable.setFontColor("gray");
                }
                if (day_hour == 0) {
                    dayhourTxt_reusable.setFontColor("gray");
                }
                if (over_hour == 0) {
                    overhourTxt_reusable.setFontColor("gray");
                }
                if (over_time == 0) {
                    overtimeTxt_reusable.setFontColor("gray");
                }

                if (i == dagariman) {
                    hoursTxt_reusable.setUnderline();
                    dayhourTxt_reusable.setUnderline();
                    overhourTxt_reusable.setUnderline();
                    overtimeTxt_reusable.setUnderline();
                }

                hoursTxt_reusable.setFontSize(1);
                dayhourTxt_reusable.setFontSize(1);
                overhourTxt_reusable.setFontSize(1);
                overtimeTxt_reusable.setFontSize(1);

                table.add(hoursTxt_reusable,3,row);
                table.add(dayhourTxt_reusable,4,row);
                table.add(overtimeTxt_reusable,5,row);
                table.add(overhourTxt_reusable,6,row);


                totalHours += total_hour;
                totalOverhour += over_hour;
                totalOvertime += over_time;
                totalDay += day_hour;


            }
          ++row;
                  Text finalHours = new Text(""+totalHours);
                    finalHours.setFontSize(1);
                    finalHours.setBold();
                  Text finalDay = new Text(""+totalDay);
                    finalDay.setFontSize(1);
                    finalDay.setBold();
                  Text finalOvertime = new Text(""+totalOvertime);
                    finalOvertime.setFontSize(1);
                    finalOvertime.setBold();
                  Text finalOverhour = new Text(""+totalOverhour);
                    finalOverhour.setFontSize(1);
                    finalOverhour.setBold();

                  table.add(finalHours,3,row);
                  table.add(finalDay,4,row);
                  table.add(finalOvertime,5,row);
                  table.add(finalOverhour,6,row);
          ++row;

          Link bakka = this.getPreviousMonthLink("hour_pr_employee");
          Link afram = this.getNextMonthLink("hour_pr_employee");

            table.add(bakka,2,row);
            table.add(afram,2,row);


          table.setHorizontalZebraColored(color_1,color_2);
          table.setRowColor(1,this.header_color);
          table.setRowColor(row,this.header_color);


/*
            TimesheetEntry[] entry = (TimesheetEntry[])(new TimesheetEntry()).findAll("select * from timesheet_entry where member_id="+this.member_id+" AND timesheet_entry_date >= '"+dags1+"' AND timesheet_entry_date <= '"+dags2+"' order by timesheet_entry_date,project_id");
//            com.idega.data.genericentity.Member member = new com.idega.data.genericentity.Member();
            com.idega.jmodule.timesheet.data.TimesheetProject project;
            Days[] days = (Days[])(new Days()).findAllOrdered("days_id");
            double totalHours = 0;
            double totalOvertime = 0;
            double totalDay = 0;


            double[] hoursPerDay = new double[8];
            for (int i = 0; i < days.length; i++) {
                hoursPerDay[days[i].getID()] = days[i].getWorkHours();
            }

		Table headerTable = this.getHeaderTable();
                        Text nafnPaMoned = new Text("Tímaskýrsla&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + FunctColl.getNameOfMonth(manudur, modinfo) + " " +ar);
                            nafnPaMoned.setFontSize(3);
                            nafnPaMoned.setBold();
                            nafnPaMoned.setFontColor(this.header_text_color);

                        Text memberName = new Text( (new com.idega.data.genericentity.Member(this.member_id)).getName());
                            memberName.setFontSize(3);
                            memberName.setBold();
                            memberName.setFontColor(this.header_text_color);

                        headerTable.add(memberName,2,1);
                        headerTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",2,1);
                        headerTable.add(nafnPaMoned,2,1);
                    add(headerTable);

          Timestamp stamp;
          idegaTimestamp i_stamp;
          idegaCalendar cal = new idegaCalendar();
          int day_of_week;
          double total_hour;
          double day_hour;
          double over_hour;

          int row = 1;
          Table table = new Table();
            table.setWidth(this.table_width);
            table.setCellpadding(0);
            table.setCellspacing(0);
            add(table);

          Text dayTxt = new Text("dags");
          Text projectTxt = new Text("verk");
          Text hoursTxt = new Text("tímar");
          Text dayhourTxt = new Text("dagvinna");
          Text overhourTxt = new Text("yfirvinna");
              dayTxt.setFontColor("#FFFFFF");
              projectTxt.setFontColor("#FFFFFF");
              hoursTxt.setFontColor("#FFFFFF");
              dayhourTxt.setFontColor("#FFFFFF");
              overhourTxt.setFontColor("#FFFFFF");

          table.add(dayTxt,2,row);
          table.add(projectTxt,3,row);
          table.add(hoursTxt,4,row);
          table.add(dayhourTxt,5,row);
          table.add(overhourTxt,6,row);


          Text dayTxt_reusable;
          Text projectTxt_reusable;
          Text hoursTxt_reusable;
          Text dayhourTxt_reusable;
          Text overhourTxt_reusable;

              for (int i = 0; i < entry.length; i++) {
                  if (entry[i].getResourceId() == 0 ) {
                      ++row;
                      stamp = entry[i].getDate();
                      i_stamp = new idegaTimestamp(stamp);
                      project = new TimesheetProject(entry[i].getProjectId());
                      day_of_week = cal.getDayOfWeek(i_stamp.getYear(),i_stamp.getMonth(),i_stamp.getDate());
                      total_hour = 0;
                      day_hour = 0;
                      over_hour = 0;

                      total_hour =  entry[i].getHowMany();

                      if (total_hour > hoursPerDay[day_of_week] ) {
                          over_hour = total_hour - hoursPerDay[day_of_week];
                          day_hour = hoursPerDay[day_of_week];
                      }
                      else {
                        day_hour = total_hour;
                      }

                      totalHours += total_hour;
                      totalOvertime += over_hour;
                      totalDay += day_hour;

                      dayTxt_reusable = new Text(entry[i].getDate().toString().substring(0,10));
                      projectTxt_reusable = new Text(project.getName());
                      hoursTxt_reusable = new Text(""+total_hour);
                      dayhourTxt_reusable = new Text(""+day_hour);
                      overhourTxt_reusable = new Text(""+over_hour);
                          dayTxt_reusable.setFontSize(1);
                          projectTxt_reusable.setFontSize(1);
                          hoursTxt_reusable.setFontSize(1);
                          dayhourTxt_reusable.setFontSize(1);
                          overhourTxt_reusable.setFontSize(1);

                      table.add(dayTxt_reusable,2,row);
                      table.add(projectTxt_reusable,3,row);
                      table.add(hoursTxt_reusable,4,row);
                      table.add(dayhourTxt_reusable,5,row);
                      table.add(overhourTxt_reusable,6,row);
                  }

              }

                  ++row;
                  Text finalHours = new Text(""+totalHours);
                    finalHours.setFontSize(1);
                    finalHours.setBold();
                  Text finalDay = new Text(""+totalDay);
                    finalDay.setFontSize(1);
                    finalDay.setBold();
                  Text finalOver = new Text(""+totalOvertime);
                    finalOver.setFontSize(1);
                    finalOver.setBold();

                  table.add(finalHours,4,row);
                  table.add(finalDay,5,row);
                  table.add(finalOver,6,row);

                  ++row;

                Link bakka = this.getPreviousMonthLink("hour_pr_employee");
                Link afram = this.getNextMonthLink("hour_pr_employee");

    		  table.add(bakka,2,row);
                  table.add(afram,2,row);


                  table.setHorizontalZebraColored(color_1,color_2);
                  table.setRowColor(1,this.header_color);
                  table.setRowColor(row,this.header_color);




*/

        }
        // end reportPerEmployee


       private void reportHourPrProject(ModuleInfo modinfo) throws SQLException {
  	  String project_id = modinfo.getParameter("i_timesheet_project_id");

	  int dagariman = FunctColl.getLengthOfMonth(manudur,ar);
                String dags1 = (ar+"-"+manudur+"-01");
                String dags2 = (ar+"-"+manudur+"-"+(dagariman));

		if ( manudur < 10) {
                  dags1 = (ar+"-0"+manudur+"-01");
                  dags2 = (ar+"-0"+manudur+"-"+(dagariman));
                }


          TimesheetEntry[] entry = (TimesheetEntry[])(new TimesheetEntry()).findAll("select * from timesheet_entry where user_id="+this.user_id+" AND timesheet_entry_date >= '"+dags1+"' AND timesheet_entry_date <= '"+dags2+"' and project_id ="+project_id+" order by timesheet_entry_date,project_id");

          projectReportHour(modinfo, entry, project_id, false);

      }
      private void reportHourPrProjectAll(ModuleInfo modinfo) throws Exception {

          if (isAdmin) {
              String project_id = modinfo.getParameter("i_timesheet_project_id");
              int dagariman = FunctColl.getLengthOfMonth(manudur,ar);
                    String dags1 = (ar+"-"+manudur+"-01");
                    String dags2 = (ar+"-"+manudur+"-"+(dagariman));

                    if ( manudur < 10) {
                      dags1 = (ar+"-0"+manudur+"-01");
                      dags2 = (ar+"-0"+manudur+"-"+(dagariman));
                    }


              TimesheetEntry[] entry = (TimesheetEntry[])(new TimesheetEntry()).findAll("select * from timesheet_entry where timesheet_entry_date >= '"+dags1+"' AND timesheet_entry_date <= '"+dags2+"' and project_id ="+project_id+" order by timesheet_entry_date,project_id");

              projectReportHour(modinfo, entry, project_id, true);

          }
          else {
              reportHourPrProject(modinfo);
          }
      }

      private void projectReportHour(ModuleInfo modinfo, TimesheetEntry[] entry, String project_id, boolean viewAll) throws SQLException {
          String edit_string = "hour_pr_project";
          User current_user = this.user;
          if (viewAll) {
              edit_string = "hour_pr_project_all";
          }

            TimesheetProject project;
            Days[] days = (Days[])(new Days()).findAllOrdered("days_id");
            double totalHours = 0;
            double totalOvertime = 0;
            double totalDay = 0;


            double[] hoursPerDay = new double[8];
            for (int i = 0; i < days.length; i++) {
                hoursPerDay[days[i].getID()] = days[i].getWorkHours();
            }

		Table headerTable = this.getHeaderTable();
                        Text nafnPaMoned = new Text("Tímaskýrsla&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +FunctColl.getNameOfMonth(manudur, modinfo) + " " +ar);
                            nafnPaMoned.setFontSize(3);
                            nafnPaMoned.setBold();
                            nafnPaMoned.setFontColor(this.header_text_color);

                        Text memberName = new Text( (new TimesheetProject(Integer.parseInt(project_id))).getName());
                            memberName.setFontSize(3);
                            memberName.setBold();
                            memberName.setFontColor(this.header_text_color);

                        headerTable.add(memberName,2,1);
                        headerTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",2,1);
                        headerTable.add(nafnPaMoned,2,1);
                    add(headerTable);

          Timestamp stamp;
          idegaTimestamp i_stamp;
          idegaCalendar cal = new idegaCalendar();
          int day_of_week;
          double total_hour;
          double day_hour;
          double over_hour;

          int row = 1;
          Table table = new Table();
            table.setWidth(this.table_width);
            table.setCellpadding(0);
            table.setCellspacing(0);
            add(table);

          Text dayTxt = new Text("dags");
          Text projectTxt = new Text("starfsmaður");
          Text hoursTxt = new Text("tímar");
          Text dayhourTxt = new Text("dagvinna");
          Text overhourTxt = new Text("yfirvinna");
              dayTxt.setFontColor("#FFFFFF");
              projectTxt.setFontColor("#FFFFFF");
              hoursTxt.setFontColor("#FFFFFF");
              dayhourTxt.setFontColor("#FFFFFF");
              overhourTxt.setFontColor("#FFFFFF");

          table.add(dayTxt,2,row);
          table.add(projectTxt,3,row);
          table.add(hoursTxt,4,row);
          table.add(dayhourTxt,5,row);
          table.add(overhourTxt,6,row);


          Text dayTxt_reusable;
          Text projectTxt_reusable;
          Text hoursTxt_reusable;
          Text dayhourTxt_reusable;
          Text overhourTxt_reusable;

          String member_name = current_user.getName();

              for (int i = 0; i < entry.length; i++) {
                  if (entry[i].getResourceId() == 0 ) {
                      ++row;
                      stamp = entry[i].getDate();
                      i_stamp = new idegaTimestamp(stamp);
                      project = new TimesheetProject(entry[i].getProjectId());
                      day_of_week = cal.getDayOfWeek(i_stamp.getYear(),i_stamp.getMonth(),i_stamp.getDate());
                      if (viewAll) {
                          current_user = entry[i].getUser();
                          member_name = current_user.getName();
                      }
                      total_hour = 0;
                      day_hour = 0;
                      over_hour = 0;

                      total_hour =  entry[i].getHowMany();

                      if (total_hour > hoursPerDay[day_of_week] ) {
                          over_hour = total_hour - hoursPerDay[day_of_week];
                          day_hour = hoursPerDay[day_of_week];
                      }
                      else {
                        day_hour = total_hour;
                      }

                      totalHours += total_hour;
                      totalOvertime += over_hour;
                      totalDay += day_hour;

                      dayTxt_reusable = new Text(entry[i].getDate().toString().substring(0,10));
                      projectTxt_reusable = new Text(member_name);
                      hoursTxt_reusable = new Text(""+total_hour);
                      dayhourTxt_reusable = new Text(""+day_hour);
                      overhourTxt_reusable = new Text(""+over_hour);
                          dayTxt_reusable.setFontSize(1);
                          projectTxt_reusable.setFontSize(1);
                          hoursTxt_reusable.setFontSize(1);
                          dayhourTxt_reusable.setFontSize(1);
                          overhourTxt_reusable.setFontSize(1);

                      table.add(dayTxt_reusable,2,row);
                      table.add(projectTxt_reusable,3,row);
                      table.add(hoursTxt_reusable,4,row);
                      table.add(dayhourTxt_reusable,5,row);
                      table.add(overhourTxt_reusable,6,row);
                  }
              }

                  ++row;
                  Text finalHours = new Text(""+totalHours);
                    finalHours.setFontSize(1);
                    finalHours.setBold();
                  Text finalDay = new Text(""+totalDay);
                    finalDay.setFontSize(1);
                    finalDay.setBold();
                  Text finalOver = new Text(""+totalOvertime);
                    finalOver.setFontSize(1);
                    finalOver.setBold();

                  table.add(finalHours,4,row);
                  table.add(finalDay,5,row);
                  table.add(finalOver,6,row);

                  ++row;

                Link bakka = this.getPreviousMonthLink(edit_string);
                    if (bakka != null)
                    bakka.addParameter("i_timesheet_project_id",project_id);
                Link afram = this.getNextMonthLink(edit_string);
                    if (afram != null)
                    afram.addParameter("i_timesheet_project_id",project_id);

    		  table.add(bakka,2,row);
                  table.add(afram,2,row);


                  table.setHorizontalZebraColored(color_1,color_2);
                  table.setRowColor(1,this.header_color);
                  table.setRowColor(row,this.header_color);






        }
        // end reportPerEmployee

        private void showUnBooked(ModuleInfo modinfo, boolean viewPrevious) throws SQLException{

  	        int dagariman = FunctColl.getLengthOfMonth(manudur,ar);
                String dags1 = (ar+"-"+manudur+"-01");
                String dags2 = (ar+"-"+manudur+"-"+(dagariman));
		if ( manudur < 10) {
                  dags1 = (ar+"-0"+manudur+"-01");
                  dags2 = (ar+"-0"+manudur+"-"+(dagariman));
                }

                boolean arePreviousEntries = false;

                double vinnaSamtals = 0;
                double aksturSamtals = 0;

                TimesheetEntry[] entry = null;
                if (viewPrevious) {
                    entry = TimesheetEntry.getPreviousEntries(1,manudur,ar,this.user_id);
                }
                else {
                    entry = (TimesheetEntry[])(new TimesheetEntry()).findAll("select * from timesheet_entry where user_id="+this.user_id+" AND timesheet_entry_date >= '"+dags1+"' AND timesheet_entry_date <= '"+dags2+"' AND booked='N' AND registered='N' order by timesheet_entry_date,project_id");
                    arePreviousEntries = TimesheetEntry.arePreviousEntries(1,manudur,ar,this.user_id);
                }
                User user = new User(this.user_id);
                TimesheetProject project;


                Form form = new Form();

                Table headerTable = this.getHeaderTable();
                    Text unBooked = new Text("Óbókaðar færslur");
                      unBooked.setFontSize(3);
                      unBooked.setBold();
                      unBooked.setFontColor(this.header_text_color);
                    Text memName = new Text(user.getName());
                      memName.setFontSize(3);
                      memName.setBold();
                      memName.setFontColor(this.header_text_color);
                    Text monthName = new Text(FunctColl.getNameOfMonth(manudur,modinfo)+" "+this.ar);
                      if (viewPrevious) {
                          monthName.setText("fyrir " + monthName.getText());
                          add(monthName.getText());
                      }

                      monthName.setFontSize(3);
                      monthName.setBold();
                      monthName.setFontColor(this.header_text_color);

                    headerTable.add(unBooked,2,1);
                    headerTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",2,1);
                    headerTable.add(memName,2,1);
                    headerTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",2,1);
                    headerTable.add(monthName,2,1);
                form.add(headerTable);
                add(form);

                int row = 1;

		Table myTable= new Table();
  			myTable.setWidth(this.table_width);
			form.add(myTable);
			myTable.setCellspacing(0);
			myTable.setCellpadding(0);
			myTable.setBorder(border);

               Text dags = new Text("Dagsetning");
               Text the_number = new Text("Verknúmer");
               Text the_member = new Text("Verk");
               Text the_resource = new Text("Forði");
               Text horas = new Text("Tímar");
               Text miles = new Text("Akstur");
               Text other = new Text("Tækjanotkun (ein/stk)");
               Text booked = new Text("Bóka");
                  dags.setFontColor(this.header_text_color);
                  the_number.setFontColor(this.header_text_color);
                  the_member.setFontColor(this.header_text_color);
                  the_resource.setFontColor(this.header_text_color);
                  horas.setFontColor(this.header_text_color);
                  miles.setFontColor(this.header_text_color);
                  other.setFontColor(this.header_text_color);

		myTable.add(dags,2,row);
		myTable.add(the_number,3,row);
		myTable.add(the_member,4,row);
		myTable.add(the_resource,5,row);
		myTable.add(horas,6,row);
		myTable.add(miles,7,row);
		myTable.add(other,8,row);

               if (!bookAllAtOnce) {
                  booked.setFontColor(this.header_text_color);
		myTable.add(booked,9,row);
                }

                String resource_id = null;
                String resource_name = null;
                String resource_unit_name = null;
                String project_name = null;
                String project_number;

              Text themDags;
              Text themProject;
              Text themProjectNumber;
              Text themResource;
              Text themHours;
              Text themMiles;
              Text themOther;
              int fontSize = 1;

              for (int i = 0; i < entry.length; i++) {
                    project = new TimesheetProject(entry[i].getProjectId());
                    resource_id = null;
                    resource_name = null;
                    resource_unit_name = null;
                    project_name = null;
                    project_number = null;
                    if (entry[i].getResource() != null) {
                        Resource resource = new Resource(entry[i].getResourceId());
                        resource_id = Integer.toString(resource.getID());
                        resource_name = resource.getName();
                        resource_unit_name = resource.getUnitName();
                    }

                    ++row;
                      themDags = new Text(entry[i].getDate().toString().substring(0,10));

                    project_number = project.getProjectNumber();
                    if (project_number == null) {
                        project_number = "";
                    }

                    project_name = project.getName();
                    if (project_name.length() > 30) {
                        project_name = project_name.substring(0,30) + "...";
                    }

                      themProjectNumber = new Text(project_number);
                        themProjectNumber.setFontSize(fontSize);


                      themProject = new Text(project_name);
                        themDags.setFontSize(fontSize);
                        themProject.setFontSize(fontSize);

                      myTable.add(themDags,2,row);
                      myTable.add(themProjectNumber,3,row);
                      myTable.add(themProject,4,row);

                      if (resource_name != null) {
                        themResource = new Text(resource_name);
                          themResource.setFontSize(fontSize);

                        myTable.add(themResource,5,row);
                        if (resource_unit_name != null) {
                          if (resource_unit_name.equalsIgnoreCase("klst")) {
                              themHours = new Text(""+entry[i].getHowMany());
                                themHours.setFontSize(fontSize);
                              myTable.add(themHours,6,row);
                              vinnaSamtals += entry[i].getHowMany();
                          }
                          else if (resource_unit_name.equalsIgnoreCase("km")) {
                              themMiles = new Text(""+entry[i].getHowMany());
                                  themMiles.setFontSize(fontSize);
                              myTable.add(themMiles,7,row);
                              aksturSamtals += entry[i].getHowMany();
                          }
                          else {
                              themOther = new Text(""+entry[i].getHowMany());
                                  themOther.setFontSize(fontSize);
                              myTable.add(themOther,8,row);
                          }

                        }
                      }
                      else {
                        myTable.add("-",5,row);
                        themHours = new Text(""+entry[i].getHowMany());
                            themHours.setFontSize(fontSize);
                        myTable.add(themHours,6,row);
                        vinnaSamtals += entry[i].getHowMany();

                      }
                      myTable.add(new HiddenInput("idega_timesheet_entry_id",entry[i].getID()+""),9,row);
                      if (!bookAllAtOnce) {
                          myTable.add(new CheckBox("idega_timesheet_Book"+entry[i].getID()),9,row);
                      }
              }
              row++;
              myTable.add(new HiddenInput("idega_timesheet_entry_edit","save_booked"));

                  Text totalWork = new Text(""+vinnaSamtals);
                      totalWork.setFontSize(fontSize);
                  Text totalMiles = new Text(""+aksturSamtals);
                      totalMiles.setFontSize(fontSize);
                  myTable.add(totalWork,6,row);
                  myTable.add(totalMiles,7,row);
              ++row;


              if (this.booked_image_url != null) {
                  Link theBooked = new Link(new Image(this.booked_image_url),this.URI);
                    setLink(theBooked,"booked",0,0,0);
                  myTable.add(theBooked,8,row);
              }
              else {
                  Link theBooked = new Link("Bókaðar tímar",this.URI);
                    setLink(theBooked,"booked",0,0,0);
                  myTable.add(theBooked,8,row);
              }

              if (this.book_image_url != null) {
                  myTable.add(new SubmitButton(new Image(book_image_url)),9,row);
              }
              else {
                  myTable.add(new SubmitButton("action","Bóka"),9,row);
              }

              Link prev = this.getPreviousMonthLink("unbooked");
              Link next = this.getNextMonthLink("unbooked");

              myTable.add(prev,2,row);
              myTable.add(next,2,row);

              myTable.setWidth(2,"100");
              myTable.setWidth(3,"90");
              myTable.setWidth(8,"140");
              myTable.setWidth(9,"80");




              myTable.setHorizontalZebraColored(color_1,color_2);
              myTable.setRowColor(1,this.header_color);
              myTable.setRowColor(row,this.header_color);

              if (arePreviousEntries) {
                  add("þú átt óbókaðar færslur í fyrri mánuðum - ");
                  Link checkPrevious = new Link("listi",this.URI);
                      this.setLink(checkPrevious,"checkPreviousEntries",0,0,0);
                  add(checkPrevious);


              }


        }
        // end showUnBooked

        private void showBooked(ModuleInfo modinfo) throws SQLException{

  	        int dagariman = FunctColl.getLengthOfMonth(manudur,ar);
                String dags1 = (ar+"-"+manudur+"-01");
                String dags2 = (ar+"-"+manudur+"-"+(dagariman));
		if ( manudur < 10) {
                  dags1 = (ar+"-0"+manudur+"-01");
                  dags2 = (ar+"-0"+manudur+"-"+(dagariman));
                }

                double vinnaSamtals = 0;
                double aksturSamtals = 0;

                TimesheetEntry[] entry = (TimesheetEntry[])(new TimesheetEntry()).findAll("select * from timesheet_entry where user_id="+this.user_id+" AND timesheet_entry_date >= '"+dags1+"' AND timesheet_entry_date <= '"+dags2+"' AND booked='Y' AND registered='N' order by timesheet_entry_date,project_id");
                com.idega.data.genericentity.Member member = new com.idega.data.genericentity.Member(this.user_id);
                TimesheetProject project;


                Form form = new Form();

                Table headerTable = this.getHeaderTable();
                    Text unBooked = new Text("Bókaðar færslur");
                      unBooked.setFontSize(3);
                      unBooked.setBold();
                      unBooked.setFontColor(this.header_text_color);
                    Text memName = new Text(member.getName());
                      memName.setFontSize(3);
                      memName.setBold();
                      memName.setFontColor(this.header_text_color);
                    Text monthName = new Text(FunctColl.getNameOfMonth(manudur,modinfo)+" "+this.ar);
                      monthName.setFontSize(3);
                      monthName.setBold();
                      monthName.setFontColor(this.header_text_color);

                    headerTable.add(unBooked,2,1);
                    headerTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",2,1);
                    headerTable.add(memName,2,1);
                    headerTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",2,1);
                    headerTable.add(monthName,2,1);
                form.add(headerTable);
                add(form);

                int row = 1;

		Table myTable= new Table();
  			myTable.setWidth(this.table_width);
			form.add(myTable);
			myTable.setCellspacing(0);
			myTable.setCellpadding(0);
			myTable.setBorder(border);

               Text dags = new Text("Dagsetning");
               Text the_number = new Text("Verknúmer");
               Text the_member = new Text("Verkefni");
               Text the_resource = new Text("Forði");
               Text horas = new Text("Tímar");
               Text miles = new Text("Akstur");
               Text other = new Text("Tækjanotkun (ein/stk)");
               Text booked = new Text("Skrá ???");
                  dags.setFontColor(this.header_text_color);
                  the_number.setFontColor(this.header_text_color);
                  the_member.setFontColor(this.header_text_color);
                  the_resource.setFontColor(this.header_text_color);
                  horas.setFontColor(this.header_text_color);
                  miles.setFontColor(this.header_text_color);
                  other.setFontColor(this.header_text_color);
                  booked.setFontColor(this.header_text_color);

		myTable.add(dags,2,row);
		myTable.add(the_number,3,row);
		myTable.add(the_member,4,row);
		myTable.add(the_resource,5,row);
		myTable.add(horas,6,row);
		myTable.add(miles,7,row);
		myTable.add(other,8,row);
//		myTable.add(booked,8,row);


                String resource_id = null;
                String resource_name = null;
                String resource_unit_name = null;
                String project_name = null;
                String project_number = null;

              Text themDags;
              Text themProject;
              Text themProjectNumber;
              Text themResource;
              Text themHours;
              Text themMiles;
              Text themOther;
              int fontSize = 1;

              for (int i = 0; i < entry.length; i++) {
                    project = new TimesheetProject(entry[i].getProjectId());
                    resource_id = null;
                    resource_name = null;
                    resource_unit_name = null;
                    project_name = null;
                    project_number = null;
                    if (entry[i].getResource() != null) {
                        Resource resource = new Resource(entry[i].getResourceId());
                        resource_id = Integer.toString(resource.getID());
                        resource_name = resource.getName();
                        resource_unit_name = resource.getUnitName();
                    }

                    ++row;
                      themDags = new Text(entry[i].getDate().toString().substring(0,10));

                    project_number = project.getProjectNumber();
                    if (project_number == null) {
                        project_number = "";
                    }

                    project_name = project.getName();
                    if (project_name.length() > 30) {
                        project_name = project_name.substring(0,30) + "...";
                    }


                      themProjectNumber = new Text(project_number);
                        themProjectNumber.setFontSize(fontSize);

                      themProject = new Text(project_name);
                        themDags.setFontSize(fontSize);
                        themProject.setFontSize(fontSize);

                      myTable.add(themDags,2,row);
                      myTable.add(themProjectNumber,3,row);
                      myTable.add(themProject,4,row);

                      if (resource_name != null) {
                        themResource = new Text(resource_name);
                          themResource.setFontSize(fontSize);

                        myTable.add(themResource,5,row);
                        if (resource_unit_name != null) {
                          if (resource_unit_name.equalsIgnoreCase("klst")) {
                              themHours = new Text(""+entry[i].getHowMany());
                                themHours.setFontSize(fontSize);
                              myTable.add(themHours,6,row);
                              vinnaSamtals += entry[i].getHowMany();
                          }
                          else if (resource_unit_name.equalsIgnoreCase("km")) {
                              themMiles = new Text(""+entry[i].getHowMany());
                                  themMiles.setFontSize(fontSize);
                              myTable.add(themMiles,7,row);
                              aksturSamtals += entry[i].getHowMany();
                          }
                          else {
                              themOther = new Text(""+entry[i].getHowMany());
                                  themOther.setFontSize(fontSize);
                              myTable.add(themOther,8,row);
                          }

                        }
                      }
                      else {
                        myTable.add("-",5,row);
                        themHours = new Text(""+entry[i].getHowMany());
                            themHours.setFontSize(fontSize);
                        myTable.add(themHours,6,row);
                        vinnaSamtals += entry[i].getHowMany();

                      }
//                      myTable.add(new HiddenInput("idega_timesheet_entry_id",entry[i].getID()+""),8,row);
//                      myTable.add(new CheckBox("idega_timesheet_Register"+entry[i].getID()),8,row);
              }
              row++;
//              myTable.add(new HiddenInput("idega_timesheet_entry_edit","save_registered"));
/*
              if (this.register_image_url  != null) {
                  myTable.add(new SubmitButton(new Image(register_image_url)),8,row);
              }
              else {
                  myTable.add(new SubmitButton("action","Skrásetja"),8,row);
              }
*/
                  Text totalWork = new Text(""+vinnaSamtals);
                      totalWork.setFontSize(fontSize);
                  Text totalMiles = new Text(""+aksturSamtals);
                      totalMiles.setFontSize(fontSize);
                  myTable.add(totalWork,6,row);
                  myTable.add(totalMiles,7,row);
                  row ++;


              Link prev = this.getPreviousMonthLink("booked");
              Link next = this.getNextMonthLink("booked");

              myTable.add(prev,2,row);
              myTable.add(next,2,row);



              myTable.setHorizontalZebraColored(color_1,color_2);
              myTable.setRowColor(1,this.header_color);
              myTable.setRowColor(row,this.header_color);

              myTable.setWidth(2,"100");
              myTable.setWidth(3,"90");
              myTable.setWidth(8,"140");


        }
        //  end showBooked

        private void saveBooked(ModuleInfo modinfo) throws SQLException{
              String[] entry_id = (String[]) modinfo.getParameterValues("idega_timesheet_entry_id");
              TimesheetEntry entry;
              String active = "";
              if (entry_id != null) {
                  for (int i = 0; i < entry_id.length; i++) {
                      if (this.bookAllAtOnce) {
                          entry = new TimesheetEntry(Integer.parseInt(entry_id[i]));
                          entry.setBooked(true);
                          entry.update();
                      }
                      else {
                          active = "";
                          active = modinfo.getParameter("idega_timesheet_Book"+entry_id[i]);
                          if (active != null){
                            if (!active.equals("")) {
                                entry = new TimesheetEntry(Integer.parseInt(entry_id[i]));
                                entry.setBooked(true);
                                entry.update();
                            }
                          }
                      }
                  }
              }

              showBooked(modinfo);

        }

        private void saveRegistered(ModuleInfo modinfo) throws SQLException{
              String[] entry_id = (String[]) modinfo.getParameterValues("idega_timesheet_entry_id");
              TimesheetEntry entry;
              String active = "";
              if (entry_id != null) {
                  for (int i = 0; i < entry_id.length; i++) {
                      active = "";
                      active = modinfo.getParameter("idega_timesheet_Register"+entry_id[i]);
                      if (active != null){
                        if (!active.equals("")) {
                            entry = new TimesheetEntry(Integer.parseInt(entry_id[i]));
                            entry.setRegistered(true);
                            entry.update();
                        }
                      }

                  }
              }

              showBooked(modinfo);

        }


    private void moveProjects(ModuleInfo modinfo) throws Exception{
        String where_to = modinfo.getParameter("direction");
        String[] project_id = modinfo.getParameterValues("project_id");
        if ((where_to != null) && (project_id != null) ) {

            try {
                TimesheetProject project;
                if (where_to.equals("<=")) {
                    for (int i = 0; i < project_id.length; i++) {
                        project = new TimesheetProject(Integer.parseInt(project_id[i]));
                        user.addTo(project);
                    }
                }
                else if (where_to.equals("=>")) {
                    for (int i = 0; i < project_id.length; i++) {
                        project = new TimesheetProject(Integer.parseInt(project_id[i]));
                        user.reverseRemoveFrom(project);
                    }
                }
            }
            catch (Exception e) {

            }
        }

        myProjects(modinfo);

    }



        private void myProjects(ModuleInfo modinfo) throws SQLException{
            TimesheetProject[] allProjects = TimesheetService.getAllProjectsOrderByProjectNumber(modinfo);

            TimesheetProject[] usedProjects = (TimesheetProject[]) user.findRelated(new TimesheetProject());

            Vector projects_left = new Vector();
                if (allProjects.length > 0) {
                    for (int i = 0 ; i < allProjects.length ; i++ ) {
                        projects_left.addElement(allProjects[i]);
                    }
                }

            SelectionBox projects_used = new SelectionBox("project_id");
                projects_used.setAttribute("size","20");
            if (usedProjects.length > 0 ) {
                String project_number;
                String project_name;
                for (int i = 0 ; i < usedProjects.length ; i++ ) {
                    project_number = usedProjects[i].getProjectNumber();
                    project_name = usedProjects[i].getName();
                    for (int j = 0 ; j < projects_left.size() ; j++) {
                        if (( (TimesheetProject) projects_left.elementAt(j)).equals(usedProjects[i])){
                          projects_left.removeElementAt(j);
                        }

                    }

                    if (project_name.length() > 32) {
                      project_name = project_name.substring(0,32)+"...";
                    }

                    if (project_number == null) {
                        projects_used.addMenuElement(usedProjects[i].getID(), project_name);
                    }
                    else {
                        projects_used.addMenuElement(usedProjects[i].getID(),project_number+" - "+ project_name);
                    }
                }
            }



            SelectionBox projects_available = new SelectionBox("project_id");
                projects_available.setAttribute("size","20");
            if (projects_left.size() > 0 ) {
                TimesheetProject project;
                String project_number;
                String project_name;
                for (int i = 0; i < projects_left.size(); i++) {
                    project = (TimesheetProject) projects_left.elementAt(i);
                    project_number = project.getProjectNumber();
                    project_name = project.getName();

                    if (project_name.length() > 32) {
                      project_name = project_name.substring(0,32)+"...";
                    }

                    if (project_number == null) {
                        projects_available.addMenuElement(project.getID(),project_name);
                    }
                    else {
                        projects_available.addMenuElement(project.getID(),project_number+"  -  "+project_name);
                    }
                }
            }

              projects_used.addMenuElement(0,"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
              projects_available.addMenuElement(0,"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");


            Form form = new Form();

                    Table headerTable = new Table(3,1);
                            headerTable.setCellpadding(0);
                            headerTable.setCellspacing(0);
                            headerTable.setColor(header_color);
                            headerTable.setWidth(1,"17");
                            headerTable.setWidth(3,"17");
                            headerTable.setWidth(table_width);
                            headerTable.setHeight("36");
                            headerTable.setVerticalAlignment(1,1,"top");
                            headerTable.setVerticalAlignment(2,1,"middle");
                            headerTable.setVerticalAlignment(3,1,"top");
                            headerTable.setAlignment(1,1,"left");
                            headerTable.setAlignment(2,1,"center");
                            headerTable.setAlignment(3,1,"right");
                            headerTable.setAlignment("center");

                            headerTable.add(new com.idega.jmodule.object.Image("/pics/jmodules/headerTable/leftcorner.gif",""),1,1);
                            headerTable.add(new com.idega.jmodule.object.Image("/pics/jmodules/headerTable/rightcorner.gif",""),3,1);



                            Text nafnPaMoned = new Text("Mín verk");
                                nafnPaMoned.setFontSize(3);
                                nafnPaMoned.setBold();
                                nafnPaMoned.setFontColor(this.header_text_color);

                            headerTable.add(nafnPaMoned,2,1);
                        form.add(headerTable);


            Table table = new Table(3,3);
              form.add(table);
              table.setCellspacing(0);
              table.setBorder(0);
              table.setWidth(table_width);
              table.setAlignment(1,1,"right");
              table.setAlignment(1,2,"right");

              int row = 1;
              Text myProjects = new Text("Mín verk");
                  myProjects.setFontColor(header_text_color);
              Text otherProjects = new Text("Önnur verk");
                  otherProjects.setFontColor(header_text_color);
              table.add(myProjects,1,row);
              table.add(otherProjects,3,row);

              row++;

              table.add(projects_used,1,row);
              table.add(projects_available,3,row);

              table.add(new SubmitButton("direction","<="),2,row);
              table.addBreak(2,row);
              table.add(new SubmitButton("direction","=>"),2,row);
              table.add(new HiddenInput("idega_timesheet_entry_edit","move_project"));
              ++row;

              table.setColor(this.color_1);
              table.setRowColor(1,header_color);
              table.setRowColor(row,header_color);


            add(form);

        }




        private Table getHeaderTable() {
		Table headerTable = new Table(3,1);
			headerTable.setCellpadding(0);
			headerTable.setCellspacing(0);
			headerTable.setColor(this.header_color);
			headerTable.setWidth(1,"17");
			headerTable.setWidth(3,"17");
			headerTable.setWidth(this.table_width);
			headerTable.setHeight("36");
			headerTable.setVerticalAlignment(1,1,"top");
			headerTable.setVerticalAlignment(2,1,"middle");
			headerTable.setVerticalAlignment(3,1,"top");
			headerTable.setAlignment(1,1,"left");
			headerTable.setAlignment(2,1,"center");
			headerTable.setAlignment(3,1,"right");
			//headerTable.setAlignment("center");

                        headerTable.add(new Image("/pics/jmodules/poll/leftcorner.gif",""),1,1);
                        headerTable.add(new Image("/pics/jmodules/poll/rightcorner.gif",""),3,1);

              return headerTable;
        }
        // end getHeaderTable()


        private Link getPreviousMonthLink(String edit) {
            Link link = null;
            if (!isPrintable) {
                  if (this.previous_image_url != null) {
                      link = new Link(new Image(previous_image_url,"Fyrri mánuður"),this.URI);
                  }
                  else {
                      link = new Link("Fyrri mánuður",this.URI);
                  }
                  this.dagur = 1;
                  setLink(link,edit,0,-1,0);
            }
            return link;
        }

        private Link getNextMonthLink(String edit) {
            Link link = null;
            if (!isPrintable) {
                if (this.next_image_url != null) {
                    link = new Link(new Image(next_image_url,"Næsti mánuður"),this.URI);
                }
                else {
                    link = new Link("Næsti mánuður",this.URI);
                }
                this.dagur = 1;
                setLink(link,edit,0,1,0);
            }
            return link;

        }

        private void setLink(Link link,String edit, int year_adjustment,int month_adjustment, int day_adjustment) {
                link.addParameter("idega_timesheet_entry_edit",edit);
                link.addParameter("idega_timesheet_entry_manudur",(manudur+month_adjustment));
                link.addParameter("idega_timesheet_entry_ar",ar+year_adjustment);
                link.addParameter("idega_timesheet_entry_dagur",dagur+day_adjustment);
                link.addParameter("idega_timesheet_entry_number_of_days",daysShown);
                link.addParameter("idega_timesheet_entry_number_of_lines",extraLines);
                link.addParameter("i_timesheet_member_id",this.user_id);
        }


        private Link getPrintableLink(String edit) {
            Window gluggi = new Window("Prentvæn síða","/empty.jsp");
                gluggi.setResizable(true);
            Link link = new Link(gluggi);
              setLink(link,edit,0,0,0);
              link.addParameter("i_timesheet_printable","true");
              if (timesheet_project_id != null) {
                link.addParameter("i_timesheet_project_id",timesheet_project_id);
              }
            return link;
        }


        public Link getMyProjectsLink() {
            Link link = new Link("mín verk",URI);
                link.addParameter("idega_timesheet_entry_edit","my_projects");

            return link;
        }


        public PrintButton getPrintButton() {
            return new PrintButton("Prenta");
        }

}
