package com.idega.block.projectmanager.presentation;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import java.sql.*;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.util.text.*;
import java.io.*;
import com.idega.block.projectmanager.data.*;
import com.idega.core.user.data.User;
import com.idega.util.*;
import com.idega.util.text.*;


/**
 * Title:        ProjectManager
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author
 * @version 1.0
 */

public class ProjectManager extends Block {

  IWContext iwc;
    String URI;
    String project_id;
    String language;
  boolean isAdmin = false;

  public ProjectManager() {
    language ="IS";
  }




    public void displayProjects() throws Exception{
            Project[] projects = (Project[]) (new Project()).findAllByColumnOrdered("valid","Y","project_id");
            for (int i = (projects.length -1) ; i >= 0 ; i-- ) {



System.err.print("1");

              Text texti = new Text();
System.err.print("2");
                    texti.setText("");
System.err.print("3");
                    texti.addToText("Verkefni");
System.err.print("4");
//                    if (projects[i].getIssueId() != 0 ) {
//                            com.idega.jmodule.boxoffice.data.Issues iss = new com.idega.jmodule.boxoffice.data.Issues(projects[i].getIssueId());
//                            texti.addToText(" | "+iss.getIssueName());
//                    }
                    texti.setFontSize(1);
System.err.print("5");
                    texti.setFontColor("gray");
System.err.print("6");
                    add(texti);
System.err.print("7");
                    add("<br>");
System.err.print("8");
                    add("<b>"+projects[i].getName()+"</b><br>");
System.err.print("9");
                    Link theLink = new Link("nánar");
System.err.print("0");
                      theLink.addParameter("project_id",projects[i].getID());
System.err.print("1");
                    add(theLink);
System.err.print("2");
                    add("<p>");
                    if (isAdmin) {
                        Form myForm = new Form(ProjectAdmin.class,"project"+Integer.toString(projects[i].getID()));
                                myForm.add(new SubmitButton(new com.idega.presentation.Image("/pics/breyta_medium.gif"),"Breyta"));
                                myForm.add("&nbsp;&nbsp;&nbsp;");
                                myForm.add(new SubmitButton(new com.idega.presentation.Image("/pics/eyda_medium.gif"),"Henda"));
                                myForm.add(new HiddenInput("project_id",Integer.toString(projects[i].getID())));
                                myForm.add(new HiddenInput("action","Breyta"));
                        add(myForm);
                    }



            }
              add("Fann er hér");
    }

    public void closerLook(int project_id) throws Exception{
            Project project = new Project(project_id);
            ProjectExtra[] extra = (ProjectExtra[])project.findRelated(new ProjectExtra());
            com.idega.jmodule.boxoffice.data.Issues iss;

            Table table = new Table();
                    table.setBorder(0);
                    int row=1;
            Text header = new Text();
                    header.addToText("Verkefni");
                    if (project.getIssueId() != -1 ) {
                            iss = new com.idega.jmodule.boxoffice.data.Issues(project.getIssueId());
                            header.addToText(" | "+iss.getName());
                    }
                    header.setFontSize(1);
                    header.setFontColor("gray");

            table.add(header,1,row);
                    table.mergeCells(1,row,3,row);
            ++row;

            table.add("<b><big>"+project.getName()+"</big></b>",1,row);
                    table.mergeCells(1,row,3,row);
            ++row;

            if (extra.length > 0) {
                    String description = extra[0].getDescription();
                    if (description != null) {
                            table.mergeCells(1,row,3,row);
                            table.add("<u>Lýsing á verkefni:</u>",1,row);
                    Form myForm1 = new Form(ProjectAdmin.class,"project"+Integer.toString(project.getID()));


                            myForm1.add(new SubmitButton(new com.idega.presentation.Image("/pics/breyta_medium.gif"),"Breyting"));
    //			myForm1.add(new SubmitButton("tranus","Breyta"));
                            myForm1.add(new HiddenInput("project_id",Integer.toString(project.getID())));
                            myForm1.add(new HiddenInput("action","Verklýsing"));
                    if (isAdmin) {
                    table.add(myForm1,1,row);
                    }
                            ++row;
                            table.mergeCells(2,row,3,row);
                            Text texti = new Text(formatText(description));
                            texti.setFontSize(2);
                            table.add(texti,2,row);
                            ++row;
                    }
                    String goals = extra[0].getGoals();
                    if (goals != null) {
                            table.mergeCells(1,row,3,row);
                            table.add("<u>Markmið / Árangur:</u>",1,row);
                    Form myForm1 = new Form(ProjectAdmin.class,"project"+Integer.toString(project.getID()));
                            myForm1.add(new SubmitButton(new com.idega.presentation.Image("/pics/breyta_medium.gif"),"Breyting"));
    //			myForm1.add(new SubmitButton("tranus","Breyta"));
                            myForm1.add(new HiddenInput("project_id",Integer.toString(project.getID())));
                            myForm1.add(new HiddenInput("action","Markmið/Árangur"));
                    if (isAdmin) {
                    table.add(myForm1,1,row);
                    }
                            ++row;
                            table.mergeCells(2,row,3,row);
                            Text texti1 = new Text(formatText(goals));
                            texti1.setFontSize(2);
                            table.add(texti1,2,row);
                            ++row;
                    }
                    String tasks = extra[0].getTasks();
                    if (tasks != null) {
                            table.mergeCells(1,row,3,row);
                            table.add("<u>Tímarammi - tillaga:</u>",1,row);
                    Form myForm1 = new Form(ProjectAdmin.class,"project"+Integer.toString(project.getID()));
                            myForm1.add(new SubmitButton(new com.idega.presentation.Image("/pics/breyta_medium.gif"),"Breyting"));
    //			myForm1.add(new SubmitButton("tranus","Breyta"));
                            myForm1.add(new HiddenInput("project_id",Integer.toString(project.getID())));
                            myForm1.add(new HiddenInput("action","Verkliðir - Tímarammi"));
                    if (isAdmin) {
                    table.add(myForm1,1,row);
                    }
                            ++row;
                            table.mergeCells(2,row,3,row);
                            Text texti2 = new Text(formatText(tasks));
                            texti2.setFontSize(2);
                            table.add(texti2,2,row);
                            ++row;
                    }
            }

            /////////////////////////////////////////////// Starfsmenn
            table.add("<u>Hverjir koma að verkinu:</u>",1,row);
            Link link;
                    table.mergeCells(1,row,3,row);
                            Form myForm2 = new Form(ProjectAdmin.class,"project"+Integer.toString(project.getID()));
                            myForm2.add(new SubmitButton(new com.idega.presentation.Image("/pics/breyta_medium.gif"),"Breyta"));
    //				myForm2.add(new SubmitButton("action","Breyta"));
                                    myForm2.add(new HiddenInput("project_id",Integer.toString(project.getID())));
    //				myForm1.add(new HiddenInput("action","Fjármál"));
                    if (isAdmin) {
                            table.add(myForm2,1,row);
                    }
                    ++row;
            if (project.getWheelGroupId() != -1) {
                    ProjectGroup group = new ProjectGroup(project.getWheelGroupId());
                    if (group != null) {
                            table.mergeCells(2,row,3,row);
                            table.add("<i>Stýrishópur:</i>",2,row);
                            if (!project.isWheelGroupIdFinal()) {
                              table.add("<i> - tillaga</i>",2,row);
                            }
                            ++row;
                            User[] user = (User[])group.findReverseRelated(new User());
                            for (int i = 0 ; i < user.length ; i++ ) {
                                    table.mergeCells(2,row,3,row);
                                    //link = new Link(user[i].getName(),"/starfsmenn/index.jsp");
                                      //link.addParameter("action","view_member");
                                      //link.addParameter("user_id",user[i].getID()+"");
                                    table.add("&nbsp;&nbsp;- ",2,row);
                                    table.add(user[i].getName(),2,row);
                                    ++row;
                            }
                    }
            }
            if (project.getProjectManagerId() != -1) {
                    User user = new User(project.getProjectManagerId());
                    if (user != null) {
                            table.mergeCells(2,row,3,row);
                            table.add("<i>Verkefnisstóri</i>",2,row);
                            if (!project.isProjectManagerIdFinal() ) {
                              table.add("<i> - tillaga</i>",2,row);
                            }
                            ++row;
                                    table.mergeCells(2,row,3,row);
                                    link = new Link(user.getName(),"/starfsmenn/index.jsp");
                                      link.addParameter("action","view_member");
                                      link.addParameter("user_id",user.getID()+"");
                                    table.add("&nbsp;&nbsp;- ",2,row);
                                    table.add(link,2,row);
                                    ++row;
                    }
            }

            if (project.getProjectGroupId() != -1) {
                    ProjectGroup group = new ProjectGroup(project.getProjectGroupId());
                    if (group != null) {
                            table.mergeCells(2,row,3,row);
                            table.add("<i>Verkefnishópur</i>",2,row);
                            if (!project.isProjectGroupIdFinal() ) {
                              table.add("<i> - tillaga</i>",2,row);
                            }
                            ++row;
                            User[] user = (User[])group.findReverseRelated(new User());
                            for (int i = 0 ; i < user.length ; i++ ) {
                                    table.mergeCells(2,row,3,row);
                                    //link = new Link(user[i].getName(),"/starfsmenn/index.jsp");
                                      //link.addParameter("action","view_member");
                                      //link.addParameter("user_id",user[i].getID()+"");
                                    table.add("&nbsp;&nbsp;- ",2,row);
                                    table.add(user[i].getName(),2,row);
                                    ++row;
                            }
                    }
            }


            if (project.getGroupId() != -1) {
                    ProjectGroup group = new ProjectGroup(project.getGroupId());
                    if (group != null) {
                            table.mergeCells(2,row,3,row);
                            table.add("<i>Aðrir:</i>",2,row);
                            if (!project.isGroupIdFinal() ) {
                              table.add("<i> - tillaga</i>",2,row);
                            }
                            ++row;
                            User[] user = (User[])group.findReverseRelated(new User());
                            for (int i = 0 ; i < user.length ; i++ ) {
                                    table.mergeCells(2,row,3,row);
                                    //link = new Link(user[i].getName(),"/starfsmenn/index.jsp");
                                      //link.addParameter("action","view_member");
                                      //link.addParameter("user_id",user[i].getID()+"");
                                    table.add("&nbsp;&nbsp;- ",2,row);
                                    table.add(user[i].getName(),2,row);
                                    ++row;
                            }
                    }
            }


            ///////////////////////////////////////////////

            if (extra.length > 0) {
                    String finances = extra[0].getFinances();
                    if (finances != null) {
                            table.mergeCells(1,row,3,row);
                            table.add("<u>Kostnaðaráætlun:</u>",1,row);
                            Form myForm1 = new Form(ProjectAdmin.class,"project"+Integer.toString(project.getID()));
                            myForm1.add(new SubmitButton(new com.idega.presentation.Image("/pics/breyta_medium.gif"),"Breyting"));
    //				myForm1.add(new SubmitButton("tranus","Breyta"));
                                    myForm1.add(new HiddenInput("project_id",Integer.toString(project.getID())));
                                    myForm1.add(new HiddenInput("action","Fjármál"));
                    if (isAdmin) {
                            table.add(myForm1,1,row);
                    }
                            ++row;
                            table.mergeCells(2,row,3,row);
                            Text texti3 = new Text(formatText(finances));
                            texti3.setFontSize(2);
                            table.add(texti3,2,row);
                            ++row;
                    }



            if (project.getImportanceId() != -1) {
                    Importance importance = new Importance(project.getImportanceId());
                    table.add("<u>Mikilvægi</u>",1,row);
                    table.mergeCells(1,row,3,row);
                    ++row;

                    table.add(importance.getName(),2,row);
                    ++row;
            }


            add(table);
            }

            add("<p>");
            add(new BackButton(new Image("/pics/bakka.gif","til baka")));
    }

    public String formatText(String textString){

            textString = TextSoap.findAndReplace(textString,"\n","<br>");
            textString = TextSoap.findAndReplace(textString,"\t","&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

            return textString;
    }


    public void main(IWContext iwc) throws Exception {
        this.iwc = iwc;
        URI = iwc.getRequestURI();
        project_id = iwc.getParameter("project_id");
        isAdmin = AccessControl.isAdmin(iwc);


        if ( isAdmin) {
System.err.println("ER admind");
          add("ER admind");

            Form myForm = new Form(ProjectAdmin.class);
                    myForm.add(new SubmitButton(new com.idega.presentation.Image("/pics/Verkefnastjori.gif","Verkefnisstjórinn")));
            add(myForm);

            if (project_id == null) {
                    displayProjects();
            }
            else {
                    try {
                            int project_id_int = Integer.parseInt(project_id);
                            closerLook(project_id_int);
                    }
                    catch (NumberFormatException n) {
                    }
            }
        }
        // temp		add2(new Image("/images/iconVerkefni.jpg","Verkefni",240,240));


    }


} // Class ProjectManager