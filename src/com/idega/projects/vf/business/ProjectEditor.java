package com.idega.projects.vf.business;

/**
 * Title: Project Editor
 * Description:
 * Copyright:    idega Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

import java.sql.*;
import java.io.*;
import com.idega.data.*;
import com.idega.util.*;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.projects.vf.entity.*;
import com.idega.jmodule.boxoffice.data.*;
import com.idega.jmodule.text.data.TextModule;

public class ProjectEditor extends Block {

private int projectID = -1;
private int projectCategoryID = -1;
private int projectStatusID = -1;

public static final int PROJECT = 1;
public static final int STATUS = 2;
public static final int CATEGORY = 3;
public static final int CLOSE = 4;

private Form myForm;
private Table outerTable;
private Table myTable;

private int selection = -1;

  public ProjectEditor() {
    selection = PROJECT;
  }

  public ProjectEditor(int selection) {
    this.selection = selection;
  }

  public void main(IWContext iwc) {
    try {
      String action = iwc.getParameter("action");
      if ( action == null ) action = getAction();
      String mode = iwc.getParameter("mode");
      if ( mode == null ) mode = "select";

      if ( iwc.getParameter("new.x") != null ) {
        mode = "new";
      }
      if ( iwc.getParameter("edit.x") != null ) {
        mode = "edit";
      }
      if ( iwc.getParameter("delete.x") != null ) {
        mode = "delete";
      }
      if ( iwc.getParameter("save.x") != null ) {
        mode = "save";
      }

      myForm = new Form();
      myTable = new Table();
      outerTable = new Table();
      myForm.add(outerTable);

      getEditor(iwc,action,mode);
      drawOuterTable();

      add(myForm);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void getEditor(IWContext iwc,String action,String mode) {
    try {
      HiddenInput hidden = new HiddenInput("action",action);
      myForm.add(hidden);

      if ( action.equalsIgnoreCase("editProject") ) {
        selection = PROJECT;
        editProject(iwc,mode);
      }
      else if ( action.equalsIgnoreCase("editCategories") ) {
        selection = CATEGORY;
        editCategories(iwc,mode);
      }
      else if ( action.equalsIgnoreCase("editStatus") ) {
        selection = STATUS;
        editStatus(iwc,mode);
      }
      else if ( action.equalsIgnoreCase("close") ) {
        selection = CLOSE;
        close();
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void editProject(IWContext iwc,String mode) {
    try {
      if ( mode.equalsIgnoreCase("select") ) {
        ProjectModule[] project = (ProjectModule[]) ProjectModule.getStaticInstance("com.idega.projects.vf.entity.ProjectModule").findAllOrdered("name");

        DropdownMenu dropdown = new DropdownMenu("project_id");
        for ( int a = 0; a < project.length; a++ ) {
          dropdown.addMenuElement(project[a].getID(),project[a].getName());
        }
        if ( project.length == 0 ) {
          dropdown.addMenuElement("","Engin verk til");
        }

        SubmitButton newProject = new SubmitButton(new Image("/pics/buttons/new.gif","",58,16),"new");
        SubmitButton editProject = new SubmitButton(new Image("/pics/buttons/update.gif","",58,16),"edit");

        myTable.mergeCells(1,1,2,1);
        myTable.add(dropdown,1,1);
        myTable.add(newProject,1,2);
        if ( project.length > 0 ) {
          myTable.add(editProject,2,2);
        }
      }
      else if ( mode.equalsIgnoreCase("edit") ) {
        ProjectModule project = null;

        TextInput name = new TextInput("name");
          name.setMaxlength(128);

        ProjectStatus[] status = (ProjectStatus[]) ProjectStatus.getStaticInstance("com.idega.projects.vf.entity.ProjectStatus").findAllOrdered("name");
        DropdownMenu statusMenu = new DropdownMenu("status_id");
        for ( int a = 0; a < status.length; a++ ) {
          statusMenu.addMenuElement(status[a].getID(),status[a].getName());
        }

        ProjectCategory[] category = (ProjectCategory[]) ProjectCategory.getStaticInstance("com.idega.projects.vf.entity.ProjectCategory").findAllOrdered("name");
        DropdownMenu categoryMenu = new DropdownMenu("category_id");
        for ( int a = 0; a < category.length; a++ ) {
          categoryMenu.addMenuElement(category[a].getID(),category[a].getName());
        }

        String projectIDString = iwc.getParameter("project_id");
        if ( projectIDString != null && projectIDString.length() > 0 ) {
          projectID = Integer.parseInt(projectIDString);
          project = new ProjectModule(projectID);
          myForm.add(new HiddenInput("project_id",projectIDString));
          name.setContent(project.getName());
          statusMenu.setSelectedElement(Integer.toString(project.getStatusID()));
          categoryMenu.setSelectedElement(Integer.toString(project.getCategoryID()));
        }


        SubmitButton saveProject = new SubmitButton(new Image("/pics/buttons/save.gif","",58,16),"save");
        SubmitButton deleteProject = new SubmitButton(new Image("/pics/buttons/delete.gif","",58,16),"delete");

        myTable.mergeCells(1,1,2,1);
        myTable.add(name,1,1);
        myTable.add(statusMenu,1,2);
        myTable.add(categoryMenu,2,2);
        myTable.add(saveProject,1,3);
        myTable.add(deleteProject,2,3);
      }
      else if ( mode.equalsIgnoreCase("new") ) {
        TextInput name = new TextInput("name");
          name.setMaxlength(128);

        ProjectStatus[] status = (ProjectStatus[]) ProjectStatus.getStaticInstance("com.idega.projects.vf.entity.ProjectStatus").findAllOrdered("name");
        DropdownMenu statusMenu = new DropdownMenu("status_id");
        for ( int a = 0; a < status.length; a++ ) {
          statusMenu.addMenuElement(status[a].getID(),status[a].getName());
        }

        ProjectCategory[] category = (ProjectCategory[]) ProjectCategory.getStaticInstance("com.idega.projects.vf.entity.ProjectCategory").findAllOrdered("name");
        DropdownMenu categoryMenu = new DropdownMenu("category_id");
        for ( int a = 0; a < category.length; a++ ) {
          categoryMenu.addMenuElement(category[a].getID(),category[a].getName());
        }

        SubmitButton saveProject = new SubmitButton(new Image("/pics/buttons/save.gif","",58,16),"save");

        myTable.mergeCells(1,1,2,1);
        myTable.mergeCells(1,3,2,3);
        myTable.add(name,1,1);
        myTable.add(statusMenu,1,2);
        myTable.add(categoryMenu,2,2);
        myTable.add(saveProject,1,3);
      }
      else if ( mode.equalsIgnoreCase("delete") ) {
        String projectIDString = iwc.getParameter("project_id");
        if ( projectIDString != null ) {
          ProjectModule project = new ProjectModule(Integer.parseInt(projectIDString));

          Subject.getStaticInstance("com.idega.jmodule.boxoffice.data.Subject").deleteMultiple("issue_id",Integer.toString(project.getIssueID()));
          IssuesIssuesCategory.getStaticInstance("com.idega.jmodule.boxoffice.data.IssuesIssuesCategory").deleteMultiple("issue_id",Integer.toString(project.getIssueID()));
          new Issues(project.getIssueID()).delete();
          new TextModule(project.getTextID()).delete();

          project.delete();
          myTable.add("Verkefni eytt");
        }
        else {
          myTable.add("Ekkert verkefni valið");
        }
      }
      else if ( mode.equalsIgnoreCase("save") ) {
        boolean update = false;

        String name = iwc.getParameter("name");
        if ( name == null || name.length() == 0 ) {
          name = "Óþekkt";
        }

        String statusID = iwc.getParameter("status_id");
        if ( statusID != null && statusID.length() > 0) {
          projectStatusID = Integer.parseInt(statusID);
        }

        String categoryID = iwc.getParameter("category_id");
        if ( categoryID != null && categoryID.length() > 0) {
          projectCategoryID = Integer.parseInt(categoryID);
        }

        String projectIDString = iwc.getParameter("project_id");
        if ( projectIDString != null && projectIDString.length() > 0 ) {
          update = true;
        }

        ProjectModule project = new ProjectModule();
        if ( update ) {
          project = new ProjectModule(Integer.parseInt(projectIDString));
        }

        Issues issues = new Issues();
        if ( update ) {
          issues = new Issues(project.getIssueID());
        }
          issues.setIssueName(name);
          issues.setImageId(-1);

          if ( update ) {
            issues.update();
          }
          else {
            issues.insert();
          }

        TextModule text = new TextModule();
        if ( update ) {
          text = new TextModule(project.getTextID());
        }
          text.setTextHeadline(name);
          text.setIncludeImage("N");
          text.setImageId(-1);
          text.setTextDate(new idegaTimestamp().getTimestampRightNow());

          if ( update ) {
            text.update();
          }
          else {
            text.insert();
          }

          project.setCategoryID(projectCategoryID);
          project.setStatusID(projectStatusID);
          project.setIssueID(issues.getID());
          project.setTextID(text.getID());
          project.setName(name);

          if ( update ) {
            project.update();
          }
          else {
            project.insert();
          }

        myTable.add("Verkefni vistað");
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void editCategories(IWContext iwc,String mode) {
    try {
      if ( mode.equalsIgnoreCase("select") ) {
        ProjectCategory[] project = (ProjectCategory[]) ProjectCategory.getStaticInstance("com.idega.projects.vf.entity.ProjectCategory").findAllOrdered("name");

        DropdownMenu dropdown = new DropdownMenu("project_category_id");
        for ( int a = 0; a < project.length; a++ ) {
          dropdown.addMenuElement(project[a].getID(),project[a].getName());
        }
        if ( project.length == 0 ) {
          dropdown.addMenuElement("","Engir flokkar til");
        }

        SubmitButton newProject = new SubmitButton(new Image("/pics/buttons/new.gif","",58,16),"new");
        SubmitButton editProject = new SubmitButton(new Image("/pics/buttons/update.gif","",58,16),"edit");

        myTable.mergeCells(1,1,2,1);
        myTable.add(dropdown,1,1);
        myTable.add(newProject,1,2);
        if ( project.length > 0 ) {
          myTable.add(editProject,2,2);
        }
      }
      else if ( mode.equalsIgnoreCase("edit") ) {
        ProjectCategory project = null;

        TextInput name = new TextInput("name");
          name.setMaxlength(128);

        String projectCategoryIDString = iwc.getParameter("project_category_id");
        if ( projectCategoryIDString != null && projectCategoryIDString.length() > 0 ) {
          projectCategoryID = Integer.parseInt(projectCategoryIDString);
          project = new ProjectCategory(projectCategoryID);
          myForm.add(new HiddenInput("project_category_id",projectCategoryIDString));
          name.setContent(project.getName());
        }

        SubmitButton saveProject = new SubmitButton(new Image("/pics/buttons/save.gif","",58,16),"save");
        SubmitButton deleteProject = new SubmitButton(new Image("/pics/buttons/delete.gif","",58,16),"delete");

        myTable.mergeCells(1,1,2,1);
        myTable.add(name,1,1);
        myTable.add(saveProject,1,2);
        myTable.add(deleteProject,2,2);
      }
      else if ( mode.equalsIgnoreCase("new") ) {
        TextInput name = new TextInput("name");
          name.setMaxlength(128);

        SubmitButton saveProject = new SubmitButton(new Image("/pics/buttons/save.gif","",58,16),"save");

        myTable.add(name,1,1);
        myTable.add(saveProject,1,2);
      }
      else if ( mode.equalsIgnoreCase("delete") ) {
        String projectCategoryIDString = iwc.getParameter("project_category_id");
        if ( projectCategoryIDString != null ) {
          ProjectCategory projectCategory = new ProjectCategory(Integer.parseInt(projectCategoryIDString));

          ProjectModule[] project = (ProjectModule[]) ProjectModule.getStaticInstance("com.idega.projects.vf.entity.ProjectModule").findAllByColumn("vf_project_category_id",projectCategory.getID());
          for ( int a = 0; a < project.length; a++ ) {
            Subject.getStaticInstance("com.idega.jmodule.boxoffice.data.Subject").deleteMultiple("issue_id",Integer.toString(project[a].getIssueID()));
            IssuesIssuesCategory.getStaticInstance("com.idega.jmodule.boxoffice.data.IssuesIssuesCategory").deleteMultiple("issue_id",Integer.toString(project[a].getIssueID()));
            new Issues(project[a].getIssueID()).delete();
            new TextModule(project[a].getTextID()).delete();
            project[a].delete();
          }

          projectCategory.delete();

          myTable.add("Flokki eytt");
        }
        else {
          myTable.add("Enginn flokkur valinn");
        }
      }
      else if ( mode.equalsIgnoreCase("save") ) {
        boolean update = false;

        String name = iwc.getParameter("name");
        if ( name == null || name.length() == 0 ) {
          name = "Óþekkt";
        }

        String projectCategoryIDString = iwc.getParameter("project_category_id");
        if ( projectCategoryIDString != null && projectCategoryIDString.length() > 0 ) {
          update = true;
        }

        ProjectCategory project = new ProjectCategory();
        if ( update ) {
          project = new ProjectCategory(Integer.parseInt(projectCategoryIDString));
        }
          project.setName(name);

          if ( update ) {
            project.update();
          }
          else {
            project.insert();
          }

        myTable.add("Flokkur vistaður");
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void editStatus(IWContext iwc,String mode) {
    try {
      if ( mode.equalsIgnoreCase("select") ) {
        ProjectStatus[] project = (ProjectStatus[]) ProjectStatus.getStaticInstance("com.idega.projects.vf.entity.ProjectStatus").findAllOrdered("name");

        DropdownMenu dropdown = new DropdownMenu("project_status_id");
        for ( int a = 0; a < project.length; a++ ) {
          dropdown.addMenuElement(project[a].getID(),project[a].getName());
        }
        if ( project.length == 0 ) {
          dropdown.addMenuElement("","Engin staða til");
        }

        SubmitButton newProject = new SubmitButton(new Image("/pics/buttons/new.gif","",58,16),"new");
        SubmitButton editProject = new SubmitButton(new Image("/pics/buttons/update.gif","",58,16),"edit");

        myTable.mergeCells(1,1,2,1);
        myTable.add(dropdown,1,1);
        myTable.add(newProject,1,2);
        if ( project.length > 0 ) {
          myTable.add(editProject,2,2);
        }
      }
      else if ( mode.equalsIgnoreCase("edit") ) {
        ProjectStatus project = null;

        TextInput name = new TextInput("name");
          name.setMaxlength(128);

        String projectStatusIDString = iwc.getParameter("project_status_id");
        if ( projectStatusIDString != null && projectStatusIDString.length() > 0 ) {
          projectStatusID = Integer.parseInt(projectStatusIDString);
          project = new ProjectStatus(projectStatusID);
          myForm.add(new HiddenInput("project_status_id",projectStatusIDString));
          name.setContent(project.getName());
        }

        SubmitButton saveProject = new SubmitButton(new Image("/pics/buttons/save.gif","",58,16),"save");
        SubmitButton deleteProject = new SubmitButton(new Image("/pics/buttons/delete.gif","",58,16),"delete");

        myTable.mergeCells(1,1,2,1);
        myTable.add(name,1,1);
        myTable.add(saveProject,1,2);
        myTable.add(deleteProject,2,2);
      }
      else if ( mode.equalsIgnoreCase("new") ) {
        TextInput name = new TextInput("name");
          name.setMaxlength(128);

        SubmitButton saveProject = new SubmitButton(new Image("/pics/buttons/save.gif","",58,16),"save");

        myTable.add(name,1,1);
        myTable.add(saveProject,1,2);
      }
      else if ( mode.equalsIgnoreCase("delete") ) {
        String projectStatusIDString = iwc.getParameter("project_status_id");
        if ( projectStatusIDString != null ) {
          ProjectStatus projectStatus = new ProjectStatus(Integer.parseInt(projectStatusIDString));

          ProjectModule[] project = (ProjectModule[]) ProjectModule.getStaticInstance("com.idega.projects.vf.entity.ProjectModule").findAllByColumn("vf_project_status_id",projectStatus.getID());
          for ( int a = 0; a < project.length; a++ ) {
            Subject.getStaticInstance("com.idega.jmodule.boxoffice.data.Subject").deleteMultiple("issue_id",Integer.toString(project[a].getIssueID()));
            IssuesIssuesCategory.getStaticInstance("com.idega.jmodule.boxoffice.data.IssuesIssuesCategory").deleteMultiple("issue_id",Integer.toString(project[a].getIssueID()));
            new Issues(project[a].getIssueID()).delete();
            new TextModule(project[a].getTextID()).delete();
            project[a].delete();
          }

          projectStatus.delete();

          myTable.add("Stöðu eytt");
        }
        else {
          myTable.add("Engin staða valin");
        }
      }
      else if ( mode.equalsIgnoreCase("save") ) {
        boolean update = false;

        String name = iwc.getParameter("name");
        if ( name == null || name.length() == 0 ) {
          name = "Óþekkt";
        }

        String projectStatusIDString = iwc.getParameter("project_status_id");
        if ( projectStatusIDString != null && projectStatusIDString.length() > 0 ) {
          update = true;
        }

        ProjectStatus project = new ProjectStatus();
        if ( update ) {
          project = new ProjectStatus(Integer.parseInt(projectStatusIDString));
        }
          project.setName(name);

          if ( update ) {
            project.update();
          }
          else {
            project.insert();
          }

        myTable.add("Staða vistuð");
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void close() {
    try {
      myTable.add("Closing...");
      getParentPage().setParentToReload();
      getParentPage().close();
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private String getAction() {
    String action = "";

    switch (selection) {
      case PROJECT :
        action = "editProject";
      break;

      case STATUS :
        action = "editStatus";
      break;

      case CATEGORY :
        action = "editCategory";
      break;
    }

    return action;
  }

  private void drawOuterTable() {
    try {
      outerTable.setColumns(1);
      outerTable.setRows(2);
      outerTable.setCellpadding(0);
      outerTable.setCellspacing(0);
      outerTable.setWidth("100%");
      outerTable.setHeight("100%");
      outerTable.setHeight(2,"100%");
      outerTable.setColor(1,2,"#C4C4EB");
      outerTable.setAlignment(1,2,"center");
      outerTable.setAlignment(1,1,"right");

      Image projectImage = new Image("/pics/flipar/projects.gif","Verkefni",77,15);
      if ( selection == PROJECT ) projectImage.setSrc("/pics/flipar/projects1.gif");
      Link projectLink = new Link(projectImage,"");
        projectLink.addParameter("action","editProject");

      Image statusImage = new Image("/pics/flipar/status.gif","Staða",77,15);
      if ( selection == STATUS ) statusImage.setSrc("/pics/flipar/status1.gif");
      Link statusLink = new Link(statusImage,"");
        statusLink.addParameter("action","editStatus");

      Image categoryImage = new Image("/pics/flipar/category.gif","Flokkar",77,15);
      if ( selection == CATEGORY ) categoryImage.setSrc("/pics/flipar/category1.gif");
      Link categoryLink = new Link(categoryImage,"");
        categoryLink.addParameter("action","editCategories");

      Image closeImage = new Image("/pics/flipar/close.gif","Loka",77,15);
      if ( selection == CLOSE ) closeImage.setSrc("/pics/flipar/close1.gif");
      Link closeLink = new Link(closeImage,"");
        closeLink.addParameter("action","close");

      outerTable.add(projectLink,1,1);
      outerTable.add(statusLink,1,1);
      outerTable.add(categoryLink,1,1);
      outerTable.add(closeLink,1,1);

      outerTable.add(myTable,1,2);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }
}