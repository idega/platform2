//idega 2000 - Gimmi

package com.idega.jmodule.timesheet.data;

//import java.util.*;
import java.sql.*;
import com.idega.data.*;
import com.idega.data.genericentity.*;
import com.idega.jmodule.timesheet.data.*;
import com.idega.util.idegaTimestamp;
import com.idega.jmodule.projectmanager.data.Project;

public class TimesheetEntry extends GenericEntity{

	public TimesheetEntry(){
		super();
	}

	public TimesheetEntry(int id)throws SQLException{
		super(id);
	}


	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("timesheet_entry_date","date",true,true,"java.sql.Timestamp");
		addAttribute("member_id","member_id",true,true,"java.lang.Integer","many-to-one","com.idega.data.genericentity.Member");
		addAttribute("resource_id","resource_id",true,true,"java.lang.Integer","many-to-one","com.idega.jmodule.timesheet.data.Resource");
                addAttribute("project_id","númer verkefnis",true,true,"java.lang.Integer","many-to-one","com.idega.jmodule.projectmanager.data.Project");
		addAttribute("how_many","fjoldi",true,true,"java.lang.Double");
		addAttribute("description","stutt lýsing",true,true,"java.lang.String");
		addAttribute("booked","bókað",true,true,"java.lang.Boolean");
		addAttribute("registered","skráð",true,true,"java.lang.Boolean");
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

	public int getMemberId() {
		return getIntColumnValue("member_id");
	}

	public void setMemberId(int member_id) {
		setColumn("member_id",(new Integer(member_id)));
	}

        public Member getMember() {
          Member member = null;
          try {
            if (getMemberId() == -1) {
            }
            else {
              member = new Member(getMemberId());
            }
          }
          catch (SQLException s) {
          }

          return member;
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
              resource = new Resource(getResourceId());
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

       public Project getProject() {
          Project project = null;
          try {
              if (getProjectId() != -1 )
                project = new Project(getProjectId());
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
                  TimesheetEntry[] entry = (TimesheetEntry[]) (new TimesheetEntry()).findAll(SQLString);
                  if (entry.length > 0) {
                      returner = true;
                  }
              }
              catch (Exception e) {
                    System.err.println("Villa í TimesheetEntry.arePreviouseEntries() !! : " +SQLString);
                    e.printStackTrace(System.err);
              }
              return returner;
        }

        public static TimesheetEntry[] getPreviousEntries(int day, int month , int yearFourLetter, int member_id) {
              TimesheetEntry[] entry = null;

              String dateString = day+"."+month+"."+yearFourLetter;
              String SQLString = "Select * from timesheet_entry where member_id='"+member_id+"' and timesheet_entry_date < '"+dateString+"' and booked='N' order by timesheet_entry_date";

              try {
                  entry = (TimesheetEntry[]) (new TimesheetEntry()).findAll(SQLString);
              }
              catch (Exception e) {
                    System.err.println("Villa í TimesheetEntry.arePreviouseEntries() !! : " +SQLString);
                    e.printStackTrace(System.err);
              }

              return entry;
        }

}