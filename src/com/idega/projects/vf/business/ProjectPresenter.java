package com.idega.projects.vf.business;

/**
 * Title: Project Presenter
 * Description:
 * Copyright:    idega Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

import java.sql.*;
import java.io.*;
import com.idega.data.*;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.projects.vf.entity.*;
import com.idega.jmodule.text.presentation.TextReader;
import com.idega.jmodule.boxoffice.presentation.BoxReader;

public class ProjectPresenter extends Block {

private int projectID = -1;
private int projectStatusID = -1;
private boolean isAdmin = false;

private Table table;

  public ProjectPresenter() {
  }

  public ProjectPresenter(int projectID) {
    this.projectID=projectID;
  }

  public ProjectPresenter(int projectID,int projectStatusID) {
    this.projectID=projectID;
    this.projectStatusID=projectStatusID;
  }

  public void main(IWContext iwc) {
    try {
      isAdmin=isAdministrator(iwc);

      if ( projectStatusID == -1 ) {
        String projectStatusID_ = iwc.getParameter("project_status_id");
        if ( projectStatusID_ != null ) {
          projectStatusID = Integer.parseInt(projectStatusID_);
        }
      }

      if ( projectID == -1 ) {
        String projectID_ = iwc.getParameter("project_id");
        if ( projectID_ != null ) {
          projectID = Integer.parseInt(projectID_);
        }
      }

      getContent();

    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void getContent() {
    try {
      table = new Table();

      if ( projectID != -1 ) {
        getProject();
      }
      else {
        getProjectStatus();
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void getProject() {
    try {
      table.setColumns(2);
      table.setRows(2);
      table.mergeCells(1,2,2,2);
      table.setWidth("100%");
      table.setWidth(1,"100%");
      table.setWidth(2,"160");
      table.setVerticalAlignment(1,1,"top");
      table.setVerticalAlignment(2,1,"top");
      addAdminButtons();

      ProjectModule project = new ProjectModule(projectID);
      if ( project.getName() == null || project.getTextID() == -1 ) {
        getParentPage().setToRedirect("/index.jsp");
      }
      else {
        TextReader text = new TextReader(project.getTextID());
          text.setEnableDelete(false);
        BoxReader box = new BoxReader(Integer.toString(project.getIssueID()),1);
          box.setBoxSpacing(4);
          box.setBoxBorder(0);
          box.setInnerBoxBorder(0);
          box.setBoxWidth(150);
          box.setNoIcons(true);
          box.setShowCategoryHeadline(true);
          box.setBoxCategoryHeadlineSize(1);
          box.setBoxCategoryHeadlineColor("#FFFFFF");
          box.setBoxOutline(1);
          box.setOutlineColor("#589221");
          box.setInBoxColor("#F4F7EF");
          box.setNumberOfDisplayed(4);
          box.setNumberOfLetters(60);
          box.setLeftBoxWidth(0);
          box.setRightBoxWidth(0);
          box.setTableAlignment("center");
          box.setBoxPadding(1);
          box.setBoxOnly(true);
          box.setLeftHeader(false);
          box.setHeadlineLeft();

        table.add(text,1,1);
        table.add(box,2,1);
      }

      add(table);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void getProjectStatus() {
    try {
      table.setColumns(1);
      table.setRows(2);
      table.setWidth("100%");
      addAdminButtons();

      Table innerTable = new Table();
        innerTable.setWidth("100%");
        innerTable.setCellpadding(6);

      int row = 0;
      int column = 0;

      ProjectStatus status = new ProjectStatus(projectStatusID);
      if ( status.getName() == null ) {
        this.getParentPage().setToRedirect("/index.jsp");
      }
      else {
        ProjectCategory[] projectCategory = (ProjectCategory[]) ProjectCategory.getStaticInstance("com.idega.projects.vf.entity.ProjectCategory").findAllOrdered("name");
        for ( int a = 0; a < projectCategory.length; a++ ) {
          Table categoryTable = new Table();
            categoryTable.setWidth("100%");

          Text categoryText = new Text(projectCategory[a].getName());
            categoryText.setBold();
            categoryText.setFontSize(3);

            categoryTable.add(categoryText,1,1);

          ProjectModule[] project = (ProjectModule[]) ProjectModule.getStaticInstance("com.idega.projects.vf.entity.ProjectModule").findAllByColumnOrdered("vf_project_status_id",Integer.toString(projectStatusID),"vf_project_category_id",Integer.toString(projectCategory[a].getID()),"name");
          for ( int b = 0; b < project.length; b++ ) {
            Link projectLink = new Link(project[b].getName(),"");
              projectLink.addParameter("project_id",project[b].getID());
              projectLink.addParameter("project_status_id",projectStatusID);

            categoryTable.add(" - ",1,b+2);
            categoryTable.add(projectLink,1,b+2);
          }

          innerTable.add(categoryTable,1,a+1);
        }

        table.add(innerTable);
      }
      add(table);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  private void addAdminButtons() {
    try {
      Image newImage = new Image("/pics/buttons/new.gif","Nýtt verkefni",58,16);
        newImage.setHorizontalSpacing(4);
      Image updateImage = new Image("/pics/buttons/update.gif","Breyta verkefni",58,16);
        updateImage.setHorizontalSpacing(4);
      Image deleteImage = new Image("/pics/buttons/delete.gif","Eyða verkefni",58,16);
        deleteImage.setHorizontalSpacing(4);

      Window newWindow = new Window("Nýtt verkefni",400,300,"/projecteditor.jsp");
      Window updateWindow = new Window("Breyta verkefni",400,300,"/projecteditor.jsp");
      Window deleteWindow = new Window("Eyða verkefni",400,300,"/projecteditor.jsp");

      Link newLink = new Link(newImage,newWindow);

      Link updateLink = new Link(updateImage,updateWindow);
      if ( projectID != -1 ) {
        updateLink.addParameter("project_id",projectID);
        updateLink.addParameter("mode","edit");
        updateLink.addParameter("action","editProject");
      }

      Link deleteLink = new Link(deleteImage,deleteWindow);
      if ( projectID != -1 ) {
        deleteLink.addParameter("project_id",projectID);
        deleteLink.addParameter("mode","delete");
        deleteLink.addParameter("action","editProject");
      }

      if ( isAdmin ) {
        table.add(newLink,1,2);
        table.add(updateLink,1,2);
        if ( projectID != -1 ) {
          table.add(deleteLink,1,2);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

}