//idega 2000 - Gimmi



package com.idega.block.timesheet.data;



//import java.util.*;

import java.sql.SQLException;
import java.sql.Timestamp;

import com.idega.core.user.data.User;



public class TimesheetEntryBMPBean extends com.idega.data.GenericEntity implements com.idega.block.timesheet.data.TimesheetEntry {



	public TimesheetEntryBMPBean(){

		super();

	}



	public TimesheetEntryBMPBean(int id)throws SQLException{

		super(id);

	}





	public void initializeAttributes(){

		addAttribute(getIDColumnName());

		addAttribute("timesheet_entry_date","date",true,true,Timestamp.class);

		addAttribute("user_id","user_id",true,true,Integer.class,"many-to-one",com.idega.core.user.data.User.class);

		addAttribute("resource_id","resource_id",true,true,Integer.class,"many-to-one",Resource.class);

                addAttribute("project_id","númer verkefnis",true,true,Integer.class,"many-to-one",TimesheetProject.class);

		addAttribute("how_many","fjoldi",true,true,Double.class);

		addAttribute("description","stutt lýsing",true,true,String.class);

		addAttribute("booked","bókað",true,true,Boolean.class);

		addAttribute("registered","skráð",true,true,Boolean.class);

	}



	public String getIDColumnName() {

		return "timesheet_entry_id";

	}



	public String getEntityName(){

		return "timesheet_entry";

	}





	public java.sql.Timestamp getDate(){

		return (java.sql.Timestamp) getColumnValue("timesheet_entry_date");

	}



	public void setDate(java.sql.Timestamp date){

			setColumn("timesheet_entry_date", date);

	}



	public int getUserId() {

		return getIntColumnValue("user_id");

	}



	public void setUserId(int user_id) {

		setColumn("user_id",user_id);

	}



        public User getUser() {

          User user = null;

          try {

            if (getUserId() == -1) {

            }

            else {

              user = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(getUserId());

            }

          }

          catch (SQLException s) {

          }



          return user;

        }



        public int getResourceId() {

          return getIntColumnValue("resource_id");

        }



        public void setResourceId(int resource_id) {

          setColumn("resource_id",(new Integer(resource_id)));

        }





        public Resource getResource() {

          Resource resource = null;

          try {

            if (getResourceId() == -1) {

            }

            else {

              resource = ((com.idega.block.timesheet.data.ResourceHome)com.idega.data.IDOLookup.getHomeLegacy(Resource.class)).findByPrimaryKeyLegacy(getResourceId());

            }

          }

          catch (SQLException s) {

          }



          return resource;

       }





       public void setProjectId(int project_id) {

         setColumn("project_id",(new Integer(project_id)));

       }



       public int getProjectId() {

         return getIntColumnValue("project_id");

       }



       public TimesheetProject getProject() {

          TimesheetProject project = null;

          try {

              if (getProjectId() != -1 )

                project = ((com.idega.block.timesheet.data.TimesheetProjectHome)com.idega.data.IDOLookup.getHomeLegacy(TimesheetProject.class)).findByPrimaryKeyLegacy(getProjectId());

          }

          catch (SQLException s) {

          }



          return project;

       }



	public double getHowMany() {

		return ((Double)getColumnValue("how_many")).doubleValue();

	}



	public void setHowMany(double how_many) {

		setColumn("how_many",new Double(how_many));

	}



	public String getDescription() {

		return getStringColumnValue("description");

	}



	public void setDescription(String description) {

		setColumn("description",description);

	}



	public boolean isBooked() {

		return ((Boolean)getColumnValue("booked")).booleanValue();

	}



	public void setBooked(boolean booked) {

		setColumn("booked",booked);

	}



	public boolean isRegistered() {

		return ((Boolean)getColumnValue("registered")).booleanValue();

	}



	public void setRegistered (boolean registered) {

		setColumn("registered",registered);

	}



        public static boolean arePreviousEntries(int day, int month , int yearFourLetter, int member_id) {

              boolean returner = false;



              String dateString = day+"."+month+"."+yearFourLetter;



              String SQLString = "Select * from timesheet_entry where member_id='"+member_id+"' and timesheet_entry_date < '"+dateString+"'";

              try {

                                                                                            //Select * from timesheet_entry where member_id=1 and timesheet_entry_date < '1.3.2001' and booked = 'N';

                  TimesheetEntry[] entry = (TimesheetEntry[]) (((com.idega.block.timesheet.data.TimesheetEntryHome)com.idega.data.IDOLookup.getHomeLegacy(TimesheetEntry.class)).createLegacy()).findAll(SQLString);

                  if (entry.length > 0) {

                      returner = true;

                  }

              }

              catch (Exception e) {

                    System.err.println("Villa í com.idega.block.timesheet.data.TimesheetEntryBMPBean.arePreviouseEntries() !! : " +SQLString);

                    e.printStackTrace(System.err);

              }

              return returner;

        }



        public static TimesheetEntry[] getPreviousEntries(int day, int month , int yearFourLetter, int member_id) {

              TimesheetEntry[] entry = null;



              String dateString = day+"."+month+"."+yearFourLetter;

              String SQLString = "Select * from timesheet_entry where member_id='"+member_id+"' and timesheet_entry_date < '"+dateString+"' and booked='N' order by timesheet_entry_date";



              try {

                  entry = (TimesheetEntry[]) (((com.idega.block.timesheet.data.TimesheetEntryHome)com.idega.data.IDOLookup.getHomeLegacy(TimesheetEntry.class)).createLegacy()).findAll(SQLString);

              }

              catch (Exception e) {

                    System.err.println("Villa í com.idega.block.timesheet.data.TimesheetEntryBMPBean.arePreviouseEntries() !! : " +SQLString);

                    e.printStackTrace(System.err);

              }



              return entry;

        }



}
