//idega 2000 - Gimmi

package com.idega.jmodule.timesheet.data;

//import java.util.*;
import java.sql.*;
import com.idega.data.*;
import com.idega.jmodule.projectmanager.data.*;

public class Resource extends GenericEntity{

	public Resource(){
		super();
	}

	public Resource(int id)throws SQLException{
		super(id);
	}


	public void initializeAttributes(){
		addAttribute(getIDColumnName());
		addAttribute("resource_name","nafn",true,true,"java.lang.String");
        	addAttribute("resource_type","týpa",true,true,"java.lang.String");
                addAttribute("project_id","númer verkefnis",true,true,"java.lang.Integer","many-to-one","com.idega.jmodule.projectmanager.data.Project");
                addAttribute("division_id","númer deildar",true,true,"java.lang.Integer");
//              addAttribute("division_id","númer deildar", true, true,"java.lang.Integer","many-to-one","com.idega.jmodule.timesheet.data.Division");
                addAttribute("unit_name","eining",true,true,"java.lang.String");
                addAttribute("is_closed","lokaður",true,true,"java.lang.Boolean");
                addAttribute("resource_short_name","Stutt nafn",true,true,"java.lang.String");
	}

	public String getIDColumnName() {
		return "resource_id";
	}

	public String getEntityName(){
		return "resource";
	}


        public String getName() {
          return getResourceName();
        }

        public String getResourceName() {
          return getStringColumnValue("resource_name");
        }

        public void setResourceName(String name) {
          setColumn("resource_name",name);
        }

        public String getResourceShortName() {
          return getStringColumnValue("resource_short_name");
        }

        public void setResourceShortName(String short_name) {
          setColumn("resource_short_name",short_name);
        }

        public String getResourceType() {
          return getStringColumnValue("resource_type");
        }

        public void setResourceType(String resource_type) {
          setColumn("resource_type",resource_type);
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


        public int getDivisionId() {
          return getIntColumnValue("division_id");
        }

        public void setDivisionId(int division_id) {
          setColumn("division_id",(new Integer(division_id)));
        }

/*        public Division getDivision() {
          Division division;
          try {
            if (getDivisionId() != -1 )
              division = new Division(getDivisionId());
          }
          catch (SQLException s) {
          }
        }
  */

        public String getUnitName() {
          return getStringColumnValue("unit_name");
        }


        public void setUnitName(String unit_name) {
          setColumn("unit_name",unit_name);
        }


        public boolean isClosed() {
		return ((Boolean)getColumnValue("is_closed")).booleanValue();
	}

	public void setClosed (boolean closed) {
		setColumn("is_closed",closed);
	}

}
