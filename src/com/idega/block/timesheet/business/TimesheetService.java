package com.idega.block.timesheet.business;

import com.idega.block.timesheet.data.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.*;
import com.idega.util.text.TextSoap;
import java.sql.SQLException;
import java.util.Enumeration;
import com.idega.jmodule.projectmanager.data.Project;

public class TimesheetService {

      private String table_width;
      private String table_height;
      private int cellspacing;
      private int cellpadding;
      private String color_1;
      private String color_2;
      private String header_color;
      private String header_text_color;
      private int border;
      private boolean correct;

      private TimesheetService() {
          initialize();
      }

      private void initialize() {
      }

      /**
       * Removes all application attributes concerning Timesheet
       */
      public static void removeAllProjectApplicationAttributes(ModuleInfo modinfo) {
          Enumeration enum = modinfo.getServletContext().getAttributeNames();
          String myString = "";
          while (enum.hasMoreElements()) {
              myString = (String) enum.nextElement();
              if (myString.indexOf("i_timesheet_all_projects_array") != -1) {
                  modinfo.removeApplicationAttribute(myString);
              }
          }
      }

      /**
       * Returns valid projects...
       */
      public static TimesheetProject[] getAllProjects(ModuleInfo modinfo) throws SQLException {
          TimesheetProject[] allProjects = (TimesheetProject[]) modinfo.getApplicationAttribute("i_timesheet_all_projects_array");

          if (allProjects == null) {
                allProjects = (TimesheetProject[]) (new TimesheetProject()).findAllByColumn("valid","Y");
                modinfo.setApplicationAttribute("i_timesheet_all_projects_array",allProjects);
          }

          return allProjects;
      }

      /**
       * Returns valid projects order by ProjectNumber
       */
      public static TimesheetProject[] getAllProjectsOrderByProjectNumber(ModuleInfo modinfo) throws SQLException {
          return getAllProjects(modinfo,"project_number,name");
      }

      /**
       * Returns valid projects order by <i>order_by</i>
       */
      public static TimesheetProject[] getAllProjects(ModuleInfo modinfo, String order_by) throws SQLException{
          TimesheetProject[] allProjects = (TimesheetProject[]) modinfo.getApplicationAttribute("i_timesheet_all_projects_array_"+TextSoap.findAndCut(order_by,","));

          if (allProjects == null) {
                allProjects = (TimesheetProject[]) (new TimesheetProject()).findAllByColumnOrdered("valid","Y",order_by);
                modinfo.setApplicationAttribute("i_timesheet_all_projects_array_"+TextSoap.findAndCut(order_by,","),allProjects);
          }
          return allProjects;
      }





}