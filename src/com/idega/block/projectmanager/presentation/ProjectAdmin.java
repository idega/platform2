package com.idega.block.projectmanager.presentation;

import com.idega.block.projectmanager.data.*;
import com.idega.presentation.*;
import com.idega.jmodule.boxoffice.data.*;
import com.idega.core.user.data.User;
import com.idega.core.data.*;
import com.idega.idegaweb.employment.data.*;
import java.sql.*;
import java.io.*;
import java.util.*;
import com.idega.util.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;

/**
 * Title: Project Administrative Tool
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:
 * @author Grímur Jónsson
 * @version 1.0
 */

public class ProjectAdmin extends Block{

    String header_color="#ABABAB";
    String color="#EFEFEF";
    private boolean isAdmin = false;
    IWContext iwc;
    String URI = null;


    public ProjectAdmin() {
    }


	public void begin() throws SQLException{

		Form myForm = new Form();
//                  myForm.setMethod("get");

		DropdownMenu projects = new DropdownMenu((new Project()).findAllByColumnDescendingOrdered("valid","Y","project_id"));
                    projects.setAttribute("size","10");

		Project[] projectin = (Project[])(new Project()).findAllByColumnOrdered("valid","N","name");
                if (projectin != null) {
                    if (projectin.length > 0){
                        for (int i = 0 ; i < projectin.length ; i++) {
                            projects.addMenuElement(projectin[i].getID(),"* "+projectin[i].getName()+" (óklárað)");
                        }
                    }
                }

		Table myTable = new Table(3,3);
			myForm.add(myTable);
			myTable.setColor(color);
			myTable.setCellspacing(0);
			myTable.setRowColor(1,header_color);
			myTable.setRowColor(3,header_color);
			myTable.setBorder(0);
			myTable.mergeCells(1,1,3,1);
			myTable.mergeCells(2,2,3,2);
			myTable.setAlignment(1,1,"center");
			myTable.setAlignment(3,3,"right");

		myTable.add("<b>Verkefnisstjórinn</b>",1,1);

		myTable.add(projects,2,2);
//		if (isAdmin()) {
			myTable.add(new SubmitButton("action","Stofna"),1,3);
			myTable.add(new SubmitButton("action","Henda"),3,3);
//		}
		myTable.add(new SubmitButton("action","Skoða/breyta"),2,3);


		add(myForm);

	}


	public void projectFinances() throws SQLException{
		String project_id = (String) iwc.getSession().getAttribute("project_id");
		String view = iwc.getParameter("view");
			if (view == null) {
				view = "";
			}


		Project project = new Project(Integer.parseInt(project_id));
		ProjectExtra[] extra = (ProjectExtra[]) project.findRelated(new ProjectExtra());
		String finances = "";
		if (extra.length > 0) {
			if (extra[0].getFinances() != null) {
				finances = extra[0].getFinances() ;
			}
		}

		Form myForm = new Form();
//                myForm.setMethod("get");

		Table myTable = new Table(2,3);
			myTable.setBorder(0);
			myForm.add(myTable);
			myTable.setColor(color);
			myTable.setCellspacing(0);
			myTable.setRowColor(1,header_color);
			myTable.setRowColor(3,header_color);
			myTable.mergeCells(1,1,2,1);
			myTable.mergeCells(1,2,2,2);


		myTable.add("<b>Fjármál</b>",1,1);
			myTable.setAlignment(1,1,"center");
		TextArea texti = new TextArea("project_goals",finances,50,10);
			myTable.add(texti,1,2);
		myTable.add(new BackButton("Til baka"),1,3);

		SubmitButton saveButton = new SubmitButton("action","Vista");
		myTable.add(new HiddenInput("save_action","project_finances"));
		myTable.setAlignment(2,3,"right");

//		if (view.equals("")) {
			myTable.add(saveButton,2,3);
//		}

		add(myForm);
	}

	public void saveProjectFinances() throws SQLException{
		String project_id = (String) iwc.getSession().getAttribute("project_id");
		if (project_id != null) {
			String finances = iwc.getParameter("project_goals");
			if (finances != null) {
				Project project = new Project(Integer.parseInt(project_id));
				ProjectExtra[] extra = (ProjectExtra[]) project.findRelated(new ProjectExtra());
				if (extra.length > 0 ) {
						extra[0].setFinances(finances);
						extra[0].update();
				}
				else {
					ProjectExtra proExtra = new ProjectExtra();
					proExtra.setFinances(finances);
					proExtra.insert();
					project.addTo(proExtra);
				}
			}
		}
//	projectDescription();
	newProject2();
	}


	public void projectTasks() throws SQLException{
		String project_id = (String) iwc.getSession().getAttribute("project_id");
		String view = iwc.getParameter("view");
			if (view == null) {
				view = "";
			}

		Project project = new Project(Integer.parseInt(project_id));
		ProjectExtra[] extra = (ProjectExtra[]) project.findRelated(new ProjectExtra());
		String tasks = "";
		if (extra.length > 0) {
			if (extra[0].getTasks() != null) {
				tasks = extra[0].getTasks() ;
			}
		}

		Form myForm = new Form();
//                myForm.setMethod("get");

		Table myTable = new Table(2,3);
			myTable.setBorder(0);
			myForm.add(myTable);
			myTable.setColor(color);
			myTable.setCellspacing(0);
			myTable.setRowColor(1,header_color);
			myTable.setRowColor(3,header_color);
			myTable.mergeCells(1,1,2,1);
			myTable.mergeCells(1,2,2,2);


		myTable.add("<b>Verkliðir - Tímarammi</b>",1,1);
			myTable.setAlignment(1,1,"center");
		TextArea texti = new TextArea("project_tasks",tasks,50,10);
			myTable.add(texti,1,2);
		myTable.add(new BackButton("Til baka"),1,3);

		SubmitButton saveButton = new SubmitButton("action","Vista");
		myTable.add(new HiddenInput("save_action","project_tasks"));
		myTable.setAlignment(2,3,"right");
//		if (view.equals("")) {
			myTable.add(saveButton,2,3);
//		}
		add(myForm);
	}

	public void saveProjectTasks() throws SQLException{
		String project_id = (String) iwc.getSession().getAttribute("project_id");
		if (project_id != null) {
			String tasks = iwc.getParameter("project_tasks");
			if (tasks != null) {
				Project project = new Project(Integer.parseInt(project_id));
				ProjectExtra[] extra = (ProjectExtra[]) project.findRelated(new ProjectExtra());
				if (extra.length > 0 ) {
						extra[0].setTasks(tasks);
						extra[0].update();
				}
				else {
					ProjectExtra proExtra = new ProjectExtra();
					proExtra.setTasks(tasks);
					proExtra.insert();
					project.addTo(proExtra);
				}
			}
		}
//	projectDescription();
	newProject2();
	}







	public void projectGoals() throws SQLException{
		String project_id = (String) iwc.getSession().getAttribute("project_id");
		String view = iwc.getParameter("view");
			if (view == null) {
				view = "";
			}

		Project project = new Project(Integer.parseInt(project_id));
		ProjectExtra[] extra = (ProjectExtra[]) project.findRelated(new ProjectExtra());
		String goals = "";
		if (extra.length > 0) {
			if (extra[0].getGoals() != null) {
				goals = extra[0].getGoals() ;
			}
		}

		Form myForm = new Form();
//                myForm.setMethod("get");

		Table myTable = new Table(2,3);
			myTable.setBorder(0);
			myForm.add(myTable);
			myTable.setColor(color);
			myTable.setCellspacing(0);
			myTable.setRowColor(1,header_color);
			myTable.setRowColor(3,header_color);
			myTable.mergeCells(1,1,2,1);
			myTable.mergeCells(1,2,2,2);


		myTable.add("<b>Markmið / Árangur</b>",1,1);
			myTable.setAlignment(1,1,"center");
		TextArea texti = new TextArea("project_goals",goals,50,10);
			myTable.add(texti,1,2);
		myTable.add(new BackButton("Til baka"),1,3);

		SubmitButton saveButton = new SubmitButton("action","Vista");
		myTable.add(new HiddenInput("save_action","project_goals"));
		myTable.setAlignment(2,3,"right");
//		if (view.equals("")) {
			myTable.add(saveButton,2,3);
//		}
		add(myForm);
	}

	public void saveProjectGoals() throws SQLException{
		String project_id = (String) iwc.getSession().getAttribute("project_id");
		if (project_id != null) {
			String goals = iwc.getParameter("project_goals");
			if (goals != null) {
				Project project = new Project(Integer.parseInt(project_id));
				ProjectExtra[] extra = (ProjectExtra[]) project.findRelated(new ProjectExtra());
				if (extra.length > 0 ) {
						extra[0].setGoals(goals);
						extra[0].update();
				}
				else {
					ProjectExtra proExtra = new ProjectExtra();
					proExtra.setGoals(goals);
					proExtra.insert();
					project.addTo(proExtra);
				}
			}
		}
//	projectDescription();
	newProject2();
	}


	public void projectDescription() throws SQLException{
		String project_id = (String) iwc.getSession().getAttribute("project_id");
		String view = iwc.getParameter("view");
			if (view == null) {
				view = "";
			}
		Project project = new Project(Integer.parseInt(project_id));
		ProjectExtra[] extra = (ProjectExtra[]) project.findRelated(new ProjectExtra());
		String description = "";
		if (extra.length > 0) {
			if (extra[0].getDescription() != null) {
				description = extra[0].getDescription() ;
			}
		}

		Form myForm = new Form();
//                myForm.setMethod("get");

		Table myTable = new Table(2,3);
			myTable.setBorder(0);
			myForm.add(myTable);
			myTable.setColor(color);
			myTable.setCellspacing(0);
			myTable.setRowColor(1,header_color);
			myTable.setRowColor(3,header_color);
			myTable.mergeCells(1,1,2,1);
			myTable.mergeCells(1,2,2,2);


		myTable.add("<b>Verklýsing</b>",1,1);
			myTable.setAlignment(1,1,"center");
		TextArea texti = new TextArea("project_description",description,50,10);
			myTable.add(texti,1,2);
		myTable.add(new BackButton("Til baka"),1,3);

		SubmitButton saveButton = new SubmitButton("action","Vista");
		myTable.add(new HiddenInput("save_action","project_description"));
		myTable.setAlignment(2,3,"right");
//		if (view.equals("")) {
			myTable.add(saveButton,2,3);
//		}

		add(myForm);
	}

	public void saveProjectDescription() throws SQLException{
		String project_id = (String) iwc.getSession().getAttribute("project_id");
		if (project_id != null) {
			String description = iwc.getParameter("project_description");
			if (description != null) {
				Project project = new Project(Integer.parseInt(project_id));
				ProjectExtra[] extra = (ProjectExtra[]) project.findRelated(new ProjectExtra());
				if (extra.length > 0 ) {
						extra[0].setDescription(description);
						extra[0].update();
				}
				else {
					ProjectExtra proExtra = new ProjectExtra();
					proExtra.setDescription(description);
					proExtra.insert();
					project.addTo(proExtra);
				}
			}
		}
//	projectDescription();
	newProject2();
	}


	public void viewGroup() throws SQLException {
		String[] groups = iwc.getParameterValues("group_");
		if (groups != null) {
			ProjectGroup group;
			User[] user;
			SelectionBox listi;
			Table table = new Table();
				table.setBorder(0);
			int row = 1;
			table.add("<b>Hópar</b>",1,1);
				table.mergeCells(1,1,2,1);
				table.setAlignment(1,1,"center");

			for (int i=0 ; i < groups.length ; i++) {
				row +=2;
				group = new ProjectGroup(Integer.parseInt(groups[i]));
				user = (User[])group.findReverseRelated(new User());
				listi = new SelectionBox(user);
					listi.setHeight(10);
				table.add("<b>"+group.getName()+"</b>",1,row);
				table.add(listi,2,row);
				table.setVerticalAlignment(1,row,"top");
			}
			table.add(new BackButton("Til Baka"),2,row+1);

			table.setColor(color);
			table.setCellspacing(0);
			table.setRowColor(1,header_color);
			table.setRowColor(row+1,header_color);
			add(table);
		}
                else {
                  add("Enginn hópur valinn<br>");
      	          add(new BackButton("Til Baka"));
                }

	}



	public void newGroup() throws SQLException {
		Form myForm = new Form();
//                myForm.setMethod("get");
		Table myTable = new Table();
			myForm.add(myTable);
	//		myTable.setBorder(1);


		TextInput name = new TextInput("group_name");
		SelectionBox members = new SelectionBox((new User()).findAllOrdered(User.getColumnNameFirstName()));
			members.setHeight(10);
		SubmitButton velja = new SubmitButton("action","Vista");
		myTable.add(new HiddenInput("save_action","saveGroup"));


		myTable.add("Nýr Hópur",1,1);
		myTable.add("Nafn",1,2);
		myTable.add(name,2,2);
		myTable.add("Velju notendur",1,3);
                        myTable.setVerticalAlignment(1,3,"top");
		myTable.add(members,2,3);
		myTable.add(velja,1,4);

			myTable.setColor(color);
			myTable.setCellspacing(0);
			myTable.setRowColor(1,header_color);
			myTable.setRowColor(4,header_color);

		add(myForm);
	}



	public void saveGroup()throws SQLException {
		String[] members = iwc.getParameterValues("member");
		String group_name = iwc.getParameter("group_name");
		ProjectGroup group = new ProjectGroup();
			group.setName(group_name);
			group.insert();

		for (int i = 0 ; i < members.length ; i++ ) {
			User user = new User(Integer.parseInt(members[i]));
			user.addTo(group);
		}

		groups();
	}

        public void deleteGroup() throws SQLException {
              String[] group_id = (String[]) iwc.getParameterValues("group_");
              ProjectGroup group;
              User user = new User();

              if  (group_id == null) {
                  add("upps");
              }
              else {
                  try {
                      if (group_id.length > 0) {
                          for (int i = 0 ; i < group_id.length ; i++) {
                              group = new ProjectGroup(Integer.parseInt(group_id[i]));
                              user.reverseRemoveFrom(group);
                              group.delete();
                          }
                      }
                      groups();
                  }
                  catch (Exception e) {
                    add("Gat ekki framkvæmt skipun<br> - Hópur er ennþá í notkun<br>");
                    add(new BackButton(new com.idega.presentation.Image("/pics/bakka.gif")));
                  }
              }



        }

	public void save() throws SQLException {
		String save_action = iwc.getParameter("save_action");
		if (save_action != null) {
			if (save_action.equals("saveGroup")) {
				saveGroup();
			}
			else if (save_action.equals("project_description") ) {
				saveProjectDescription();
			}
			else if (save_action.equals("project_goals") ) {
				saveProjectGoals();
			}
			else if (save_action.equals("project_finances") ) {
				saveProjectFinances();
			}
			else if (save_action.equals("project_tasks") ) {
				saveProjectTasks();
			}
			else {
				groupChosen(save_action);
			}

		}
        }

	public void groupChosen(String what)throws SQLException {
                User sUser = (User) (User.getStaticInstance(User.class));
		String[] group = iwc.getParameterValues("group_");
		String[] members = iwc.getParameterValues(sUser.getEntityName());
		String project_id = (String) iwc.getSessionAttribute("project_id");


		if (what != null) {
			if ((group != null) && (members == null) ){
				if (group.length == 1) {
                                        if (what.equals("saveStyri")) {
						Project project = new Project(Integer.parseInt(project_id));
						project.setWheelGroupId(Integer.parseInt(group[0]));
						project.update();
						newProject2();
					}
					else if (what.equals("saveGroupe")) {
						Project project = new Project(Integer.parseInt(project_id));
						project.setGroupId(Integer.parseInt(group[0]));
						project.update();
						newProject2();
					}
					else if (what.equals("saveVerkhop")) {
						Project project = new Project(Integer.parseInt(project_id));
						project.setProjectGroupId(Integer.parseInt(group[0]));
						project.update();
						newProject2();
					}

				}
				else {
                                  Project project = new Project(Integer.parseInt(project_id));
                                  User[] members_temp;
                                  User member_temp;
                                  ProjectGroup groupa;

                                  ProjectGroup grouppe = new ProjectGroup();
				    grouppe.setName(project.getName()+"-"+what);
				    grouppe.setVisible(false);
				    grouppe.insert();

                                  String sql_query = "Select * from "+sUser.getEntityName()+" where";
                                  boolean altered = false;

                                  if (members != null)
                                  if (members.length > 0) {
                                    for (int i = 0 ; i < members.length ; i++) {
                                        member_temp = new User(Integer.parseInt(members[i]));
                                        altered = true;
                                        sql_query = sql_query + " "+sUser.getIDColumnName()+" = "+member_temp.getID();
                                        if (i != members.length-1) {
                                          sql_query = sql_query + " OR";
                                        }
                                    }
                                  }


                                  if (group != null)
                                  if (group.length > 0) {
                                      for (int i = 0 ; i < group.length ; i++ ) {
                                          groupa = new ProjectGroup(Integer.parseInt(group[i]));
                                          members_temp = (User[]) groupa.findReverseRelated(new User());
                                          if (members_temp != null) {
                                            if (members_temp.length > 0) {
                                                for (int j = 0 ; j < members_temp.length ; j++) {
                                                  if (altered) {
                                                    sql_query = sql_query + " OR";
                                                  }
                                                  else {
                                                   altered = true;
                                                  }
                                                    sql_query = sql_query + " "+sUser.getIDColumnName()+" = "+members_temp[j].getID();
                                                }
                                            }
                                          }


                                      }

                              }

                              sql_query = sql_query + " order by "+sUser.getColumnNameFirstName();

//                              System.err.println("SQL 1: "+sql_query);
                              User[] memberarnir = (User[]) (new User()).findAll(sql_query);
                              if (memberarnir != null) {
                                if (memberarnir.length > 0) {
                                  for (int k = 0 ; k < memberarnir.length ; k++) {
      				    memberarnir[k].addTo(grouppe);
                                  }
                                }
                              }

				if (what.equals("saveVerkhop")) {
					project.setProjectGroupId(grouppe.getID());
				}
				else if (what.equals("saveStyri")) {
					project.setWheelGroupId(grouppe.getID());
				}
				else if (what.equals("saveGroupe")) {
					project.setGroupId(grouppe.getID());
				}
				else if (what.equals("saveVerkMan")) {
					project.setProjectManagerId(Integer.parseInt(members[0]));
				}
				project.update();
				newProject2();


				}	//group.length == 1 endar
			}
			else if ((members != null) && (group == null)) {

					Project project = new Project(Integer.parseInt(project_id));

                                        String group_nameth = project.getName()+"-"+what;


					User user = new User();


					ProjectGroup grouppe = new ProjectGroup();
						grouppe.setName(group_nameth);
						grouppe.setVisible(false);
						grouppe.insert();

					for (int i = 0 ; i < members.length ; i++) {
						user = new User(Integer.parseInt(members[i]));
						user.addTo(grouppe);
					}

					if (what.equals("saveVerkhop")) {
						project.setProjectGroupId(grouppe.getID());
					}
					else if (what.equals("saveStyri")) {
						project.setWheelGroupId(grouppe.getID());
					}
					else if (what.equals("saveGroupe")) {
						project.setGroupId(grouppe.getID());
					}
					else if (what.equals("saveVerkMan")) {
						project.setProjectManagerId(Integer.parseInt(members[0]));
					}
					project.update();
					newProject2();

			}
                        else {
                              Project project = new Project(Integer.parseInt(project_id));
                              User[] members_temp;
                              User member_temp;
                              ProjectGroup groupa;

                              ProjectGroup grouppe = new ProjectGroup();
				grouppe.setName(project.getName()+"-"+what);
				grouppe.setVisible(false);
				grouppe.insert();


                              String sql_query = "Select * from "+sUser.getEntityName()+" where";
                              boolean altered = false;

                              if (members != null)
                              if (members.length > 0) {
                                for (int i = 0 ; i < members.length ; i++) {
                                    member_temp = new User(Integer.parseInt(members[i]));
                                    altered = true;
                                    sql_query = sql_query + " "+sUser.getIDColumnName()+" = "+member_temp.getID();
                                    if (i != members.length-1) {
                                      sql_query = sql_query + " OR";
                                    }
                                }
                              }


                              if (group != null)
                              if (group.length > 0) {
                                  for (int i = 0 ; i < group.length ; i++ ) {
                                      groupa = new ProjectGroup(Integer.parseInt(group[i]));
                                      members_temp = (User[]) groupa.findReverseRelated(new User());
                                      if (members_temp != null) {
                                        if (members_temp.length > 0) {
                                            for (int j = 0 ; j < members_temp.length ; j++) {
                                              if (altered) {
                                                sql_query = sql_query + " OR";
                                              }
                                              else {
                                                altered = true;
                                              }
                                                sql_query = sql_query + " "+sUser.getIDColumnName()+" = "+members_temp[j].getID();
                                            }
                                        }
                                      }


                                  }

                              }
                              sql_query = sql_query + " order by "+sUser.getColumnNameFirstName();
//                              System.err.println("SQL 2: "+sql_query);
                              User[] memberarnir = (User[]) (new User()).findAll(sql_query);
                              if (memberarnir != null) {
                                if (memberarnir.length > 0) {
                                  for (int k = 0 ; k < memberarnir.length ; k++) {
      				    memberarnir[k].addTo(grouppe);
                                  }
                                }
                              }

				if (what.equals("saveVerkhop")) {
					project.setProjectGroupId(grouppe.getID());
				}
				else if (what.equals("saveStyri")) {
					project.setWheelGroupId(grouppe.getID());
				}
				else if (what.equals("saveGroupe")) {
					project.setGroupId(grouppe.getID());
				}
				else if (what.equals("saveVerkMan")) {
					project.setProjectManagerId(Integer.parseInt(members[0]));
				}
				project.update();
				newProject2();





                        }
		}  // what != null endar



	}


	public void groups() throws SQLException{
		String actionInn = iwc.getParameter("action");
		String project_id = (String) iwc.getSessionAttribute("project_id");
		String veljaAction = "";
                String member_id = iwc.getParameter("member_id");
                String group_id = iwc.getParameter("group_id");

		if (actionInn != null) {
			if (actionInn.equals("plusStyri")) {
				veljaAction = "saveStyri";
			}
			else if (actionInn.equals("plusVerkhop")) {
				veljaAction = "saveVerkhop";
			}
			else if (actionInn.equals("plusVerkMan")) {
				veljaAction = "saveVerkMan";
			}
			else if (actionInn.equals("plusGroup")) {
				veljaAction = "saveGroupe";
			}
		}

//		add(project_id);

//		Project project = new Project(Integer.parseInt(project_id));
		Form myForm = new Form();
//                myForm.setMethod("get");
		Table myTable = new Table();
			myForm.add(myTable);
	//		myTable.setBorder(1);
			myTable.mergeCells(1,1,3,1);
			myTable.setVerticalAlignment(1,2,"top");
			myTable.setVerticalAlignment(1,3,"top");
			myTable.setVerticalAlignment(4,2,"top");
			myTable.setVerticalAlignment(4,3,"top");

			myTable.add("Veljið notendur / hópa",1,1);
			myTable.add("Hópar",1,2);
			myTable.add("Notendur",1,3);


		SelectionBox members = new SelectionBox((new User()).findAllOrdered(User.getColumnNameFirstName()));
			members.setHeight(10);
                if (member_id != null) {
                  members.setSelectedElement(member_id);
                }
                if (group_id != null) {
                    ProjectGroup goat = new ProjectGroup(Integer.parseInt(group_id));
                    User[] memm = (User[])goat.findReverseRelated(new User());
                    if (memm != null) {
                      if (memm.length > 0) {
                        for (int i = 0 ; i < memm.length ; i ++ ) {
                          members.setSelectedElement(memm[i].getID()+"");
                        }
                      }
                    }
                }
                     members.addMenuElement("","&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");



		ProjectGroup[] group = (ProjectGroup[]) (new ProjectGroup()).findAllByColumnOrdered("visible","Y","name");
		SelectionBox groups = new SelectionBox(group);
			groups.setHeight(10);
                        groups.addMenuElement("","&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

		SubmitButton nyrHopur = new SubmitButton("action","Nýr hópur");
		SubmitButton velja = new SubmitButton("action","Vista");
		SubmitButton skodaHop = new SubmitButton("action","Skoða hóp");
		SubmitButton hendaHop = new SubmitButton("action","Henda hóp");

		myTable.add(new HiddenInput("save_action",veljaAction));
		myTable.add(new HiddenInput("save_action2",actionInn));
                if (group_id != null) {
		  myTable.add(new HiddenInput("group_id",group_id));
                }
                if (member_id != null) {
		  myTable.add(new HiddenInput("member_id",member_id));
                }

		myTable.add(groups,3,2);
		myTable.add(skodaHop,4,2);
		myTable.addBreak(4,2);
		myTable.add(nyrHopur,4,2);
		myTable.addBreak(4,2);
		myTable.addBreak(4,2);
		myTable.add(hendaHop,4,2);
		myTable.add(members,3,3);
		myTable.add(velja,4,4);
                Link newMember = new Link("Nýr starfsmaður","/starfsmenn/index.jsp");
                  if (group_id != null) {
                   newMember.addParameter("group_id",group_id);
                  }
                  if (member_id != null) {
                    newMember.addParameter("member_id",member_id);
                  }
                  newMember.addParameter("action","new");
                  newMember.addParameter("project_action",actionInn);
                  newMember.addParameter("project_id",project_id);
                  newMember.addParameter("project_uri",URI);

                SubmitButton deleteMember = new SubmitButton("action","Eyða");
//                SubmitButton deleteMember = new SubmitButton(new com.idega.presentation.Image("/pics/eyda_medium.gif"),"delete");

                //myTable.add(newMember,4,3);
                myTable.addBreak(4,3);
                myTable.addBreak(4,3);
                myTable.add(deleteMember,4,3);

                myTable.add(new SubmitButton("action","Til Baka"),1,4);




			myTable.setColor(color);
			myTable.setCellspacing(0);
			myTable.setRowColor(1,header_color);
			myTable.setRowColor(4,header_color);


		add(myForm);
	}


	public void newProject() throws SQLException {
		Form myForm = new Form();
//                myForm.setMethod("get");

		Table myTable = new Table();
			myForm.add(myTable);
//			myTable.setBorder(1);

		DropdownMenu malaflokkur = new DropdownMenu((new Issues()).findAllOrdered("issue_name"));
		DropdownMenu mikilvaegi = new DropdownMenu((new Importance()).findAllOrdered("name"));


		myTable.add("Stofna",1,1);
		myTable.add("Heiti",1,2);
			myTable.add(new TextInput("name"),2,2);
		myTable.add("Málaflokkur",1,3);


			myTable.add(malaflokkur,2,3);
		myTable.add("Mikilvægi",1,4);
			myTable.add(mikilvaegi,2,4);
		myTable.add(new SubmitButton("takki","Áfram"),2,5);
			myTable.add(new HiddenInput("action","newProject2"),2,5);
			myTable.setAlignment(2,5,"right");

			myTable.setColor(color);
			myTable.setCellspacing(0);
			myTable.setRowColor(1,header_color);
			myTable.setRowColor(3,header_color);


		add(myForm);
	}


	public void newProject2() throws SQLException {
		String name = iwc.getParameter("name");
                       		String project_id = (String) iwc.getSession().getAttribute("project_id");
		Project project;
		String issue_id;
		String importance;
		Issues iss = null;// = new Issues(Integer.parseInt(issue_id));
		boolean newProject = true;

		if (project_id == null) {
			issue_id = iwc.getParameter("Issues");
                        // uses tablename as parametername
			importance = iwc.getParameter("i_pm_importance");
			project = new Project();
//			add(Integer.parseInt(issue_id)+"");
                        if (issue_id != null)
			project.setIssueId(Integer.parseInt(issue_id));
			project.setName(name);
                        if (importance != null)
			project.setImportanceId(Integer.parseInt(importance));
			project.setValid(false);
                        project.setWheelGroupIdFinal(false);
                        project.setProjectGroupIdFinal(false);
                        project.setProjectManagerIdFinal(false);
                        project.setGroupIdFinal(false);
			project.insert();
			iwc.setSessionAttribute("project_id",Integer.toString(project.getID()));
			//debug ma vera fyrir utan
                        if (issue_id != null)
                        iss = new Issues(Integer.parseInt(issue_id));
		}
		else {
			newProject = false;
			project = new Project(Integer.parseInt(project_id));
			name =project.getName();
//			importance = Integer.toString(project.getImportanceId());
			issue_id = Integer.toString(project.getIssueId());
                        if (!issue_id.equals("-1"))
			iss = new Issues(Integer.parseInt(issue_id));
		}

		if ((name.equals("")) || (name == null)) {
			//getResponse().sendRedirect("verktemp.jsp?action=Stofna");
                        add("Skráði nafn<br>");
                        add(new BackButton());
		}



//g		String URI = iwc.getRequest().getRequestURI();

//			myTable.add(new HiddenInput("project_id",Integer.toString(project.getID())));

		int row = 1;

		Form myForm = new Form();
//                myForm.setMethod("get");

                int select_size = 7;
                SelectionBox select1 = new SelectionBox();
                  select1.setHeight(select_size);
                SelectionBox select2 = new SelectionBox();
                  select2.setHeight(select_size);
                SelectionBox select3 = new SelectionBox();
                  select3.setHeight(select_size);
                SelectionBox select4 = new SelectionBox("Enginn");
                  select4.setHeight(1);
//                  select4.setAttribute("size","50");


                String empty_string = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                  select1.addMenuElement(-1,empty_string);
                  select2.addMenuElement(-1,empty_string);
                  select3.addMenuElement(-1,empty_string);
                  select4.addMenuElement(-1,empty_string);

                Table backTable = new Table(1,1);
                  backTable.setColor("black");
                  myForm.add(backTable);

		Table myTable = new Table();
			backTable.add(myTable,1,1);
			myTable.setBorder(0);
                        myTable.setCellpadding(3);
                        myTable.setCellspacing(3);
//			myTable.mergeCells(2,1,3,1);
			myTable.setAlignment(2,1,"right");


		myTable.add("<b>"+name+"</b>",1,row);
                myTable.mergeCells(1,row,3,row);
                myTable.setAlignment(1,row,"center");

			row +=1;

		myTable.add("<u>Stýrishópur</u>",1,row);
		myTable.add("<u>Verkefnishópur</u>",3,row);
			row +=1;
              CheckBox check1 = new CheckBox("wheel_group");
              if (!project.isWheelGroupIdFinal()) {
                check1.setChecked(true);
              }

              myTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",1,row+1);
              myTable.add(check1,1,row+1);
              myTable.add("Tillaga ? ",1,row+1);
                myTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",1,row+1);

              Link mellon;

		if (!(newProject)) {
			if (project.getWheelGroupId() != -1) {
				ProjectGroup group = new ProjectGroup(project.getWheelGroupId());
				if (group != null) {
					User[] user = (User[])group.findReverseRelated(new User());
                                        select1 = new SelectionBox(user);
                                        select1.addMenuElement(-1,empty_string);
                                          select1.setHeight(select_size);

/*					for (int i = 0 ; i < member.length ; i++ ) {
						myTable.add(member[i].getName(),2,row);
						++row;
					}
*/
					row +=1;
                                mellon = new Link("Breyta");
                                  mellon.addParameter("action","plusStyri");
                                  mellon.addParameter("group_id",group.getID());
                                  mellon.addParameter("project_id",project.getID());
				myTable.add(mellon,1,row);
				}
			}
			else {
					row +=1;
                                mellon = new Link("Bæta við");
                                  mellon.addParameter("action","plusStyri");
                                  mellon.addParameter("project_id",project.getID());
				myTable.add(mellon,1,row);
			}
		}
		else {
                                mellon = new Link("Bæta við");
                                  mellon.addParameter("action","plusStyri");
                                  mellon.addParameter("project_id",project.getID());
					row +=1;
			myTable.add(mellon,1,row);
		}

              myTable.add(select1,1,row-1);




                CheckBox check2 = new CheckBox("project_group");
              if (!project.isProjectGroupIdFinal() ) {
                check2.setChecked(true);
              }
                myTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",3,row);
                myTable.add(check2,3,row);
                myTable.add("Tillaga ? ",3,row);
                myTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",3,row);

		if (!(newProject)) {
			if (project.getProjectGroupId() != -1) {
			ProjectGroup group = new ProjectGroup(project.getProjectGroupId());
				User[] user = (User[])group.findReverseRelated(new User());
                                select2 = new SelectionBox(user);
                                          select2.setHeight(select_size);
                                          select2.addMenuElement(-1,empty_string);
/*				for (int i = 0 ; i < member.length ; i++ ) {
					myTable.add(member[i].getName(),2,row);
					++row;
				}
*/
                                mellon = new Link("Breyta");
                                  mellon.addParameter("action","plusVerkhop");
                                  mellon.addParameter("group_id",group.getID());
                                  mellon.addParameter("project_id",project.getID());
			myTable.add(mellon,3,row);
				row +=1;
			}
			else {
                                mellon = new Link("Bæta við");
                                  mellon.addParameter("action","plusVerkhop");
                                  mellon.addParameter("project_id",project.getID());
			myTable.add(mellon,3,row);
				row +=1;
			}
		}
		else {
                                mellon = new Link("Bæta við");
                                  mellon.addParameter("action","plusVerkhop");
                                  mellon.addParameter("project_id",project.getID());
			myTable.add(mellon,3,row);
				row +=1;
		}

              myTable.add(select2,3,row-2);


        row +=1;
		myTable.add("<u>Verkefnisstóri</u>",1,row);
		myTable.add("<u>Aðrir hópar</u>",3,row);
			row +=1;

                CheckBox check3 = new CheckBox("project_manager");
                Link theLink = new Link("mis");

		if (!(newProject)) {
			if (project.getProjectManagerId() != -1) {
				User user = new User(project.getProjectManagerId());
                                if (user != null) {
                                        select4 = new SelectionBox(user.getName());
                                          select4.addMenuElement(user.getID(),user.getName());
                                          select4.setHeight(1);
//					myTable.add(member.getName(),2,row);
        				++row;
                                        theLink = new Link("Breyta");
                                          theLink.addParameter("action","plusVerkMan");
                                          theLink.addParameter("member_id",user.getID());
                                          theLink.addParameter("project_id",project.getID());
//					myTable.add(new Link("Breyta",URI+"?action=plusVerkMan&member_id="+member.getID()+"&project_id="+project.getID()+""),1,row);
//						row +=1;
                                }

                                /*
                                ProjectGroup group = new ProjectGroup(project.getProjectManagerId());
				if (group != null) {
					Member[] member = (Member[])group.findReverseRelated(new Member());
					for (int i = 0 ; i < member.length ; i++ ) {
						myTable.add(member[i].getName(),2,row);
						++row;
					}
						row +=1;
                                        theLink = new Link("Breyta",URI+"?action=plusVerkMan&group_id="+member.getID()+"&project_id="+project.getID()+"");
//					myTable.add(new Link("Breyta",URI+"?action=plusVerkMan&group_id="+member.getID()+"&project_id="+project.getID()+""),1,row);
				}
                                */
			}
			else {
					row +=1;
                                        theLink = new Link("Bæta við");
                                          theLink.addParameter("action","plusVerkMan");
                                          theLink.addParameter("project_id",project.getID());
                                //theLink = new Link("Bæta við",URI+"?action=plusVerkMan&project_id="+project.getID()+"");
//				myTable.add(new Link("Bæta við",URI+"?action=plusVerkMan&project_id="+project.getID()+""),1,row);
			}
		}
		else {
                                        theLink = new Link("Bæta við");
                                          theLink.addParameter("action","plusVerkMan");
                                          theLink.addParameter("project_id",project.getID());
				row +=1;
                        //theLink = new Link("Bæta við",URI+"?action=plusVerkMan&project_id="+project.getID()+"");
//			myTable.add(new Link("Bæta við",URI+"?action=plusVerkMan&project_id="+project.getID()+""),1,row);
		}




                myTable.add(select4,1,row-1);
              if (!project.isProjectManagerIdFinal()) {
                check3.setChecked(true);
              }
                myTable.add("<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",1,row-1);
                myTable.add(check3,1,row-1);
                myTable.add("Tillaga ? ",1,row-1);
                myTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",1,row-1);
                myTable.add(theLink,1,row-1);


                myTable.setVerticalAlignment(1,row-1,"top");

                myTable.addBreak(1,row-1);
                myTable.addBreak(1,row-1);
                myTable.add("Málaflokkur : ",1,row-1);
                if (iss != null) {
                  myTable.add(iss.getName(),1,row-1);
                }
                myTable.addBreak(1,row-1);
                myTable.add("Mikilvægi : ",1,row-1);
                if (project.getImportanceId() != -1) myTable.add(new Importance(project.getImportanceId()).getName(),1,row-1);



                CheckBox check4 = new CheckBox("other_group");
              if (!project.isGroupIdFinal() ) {
                check4.setChecked(true);
              }
                myTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",3,row);
                myTable.add(check4,3,row);
                myTable.add("Tillaga ? ",3,row);
                myTable.add("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;",3,row);

		if (!(newProject)) {
			if (project.getGroupId() != -1) {
				ProjectGroup group = new ProjectGroup(project.getGroupId());
				if (group != null) {
					User[] user = (User[])group.findReverseRelated(new User());
                                        select3 = new SelectionBox(user);
                                          select3.setHeight(select_size);
                                          select3.addMenuElement(-1,empty_string);
/*
					for (int i = 0 ; i < member.length ; i++ ) {
						myTable.add(member[i].getName(),2,row);
						++row;
					}
*/
                                mellon = new Link("Breyta");
                                  mellon.addParameter("action","plusGroup");
                                  mellon.addParameter("group_id",group.getID());
                                  mellon.addParameter("project_id",project.getID());
				myTable.add(mellon,3,row);
					row +=1;
				}
			}
			else {
                                mellon = new Link("Bæta við");
                                  mellon.addParameter("action","plusGroup");
                                  mellon.addParameter("project_id",project.getID());
				myTable.add(mellon,3,row);
					row +=1;
			}
		}
		else {
                                mellon = new Link("Bæta við");
                                  mellon.addParameter("action","plusGroup");
                                  mellon.addParameter("project_id",project.getID());
			myTable.add(mellon,3,row);
				row +=1;
		}

                myTable.add(select3,3,row-2);

/*
		myTable.add(new SubmitButton("action","Verklýsing"),1,row);
			++row;

		myTable.add(new SubmitButton("action","Markmið/Árangur"),1,row);
			++row;

		myTable.add(new SubmitButton("action","Fjármál"),1,row);
			++row;
		myTable.add(new SubmitButton("action","Verkliðir - Tímarammi"),1,row);
			++row;
*/
		myTable.add(new SubmitButton("takki","Áfram"),3,row);
			myTable.add(new HiddenInput("action","newProject3"),2,row);
			myTable.setAlignment(3,row,"right");



			myTable.setColor(color);
			myTable.setCellspacing(0);
			myTable.setRowColor(1,header_color);
			myTable.setRowColor(row,header_color);


		add(myForm);
	}

	public void viewProject() throws SQLException {
		String name = iwc.getParameter("name");

		String project_id = (String) iwc.getSession().getAttribute("project_id");
		Project project;
		String issue_id;
		String importance;
		Issues iss = null;// = new Issues(Integer.parseInt(issue_id));
		boolean newProject = false;

			newProject = false;
			project = new Project(Integer.parseInt(project_id));
			name =project.getName();
//			importance = Integer.toString(project.getImportanceId());
			issue_id = Integer.toString(project.getIssueId());
			iss = new Issues(Integer.parseInt(issue_id));

//g		String URI = iwc.getRequest().getRequestURI();

//			myTable.add(new HiddenInput("project_id",Integer.toString(project.getID())));

		int row = 1;

		Form myForm = new Form();
//                myForm.setMethod("get");


		Table myTable = new Table();
			myForm.add(myTable);
//			myTable.setBorder(1);
			myTable.mergeCells(2,1,3,1);
			myTable.setAlignment(2,1,"right");


		myTable.add("<b><u>"+name+"</u></b>",1,row);
		myTable.add(iss.getIssueName(),2,row);
			row +=2;

		myTable.add("Stýrishópur",1,row);
			row +=1;

			if (project.getWheelGroupId() != -1) {
			ProjectGroup wheelGroup = new ProjectGroup(project.getWheelGroupId());
			if (wheelGroup != null) {
				User[] user = (User[])wheelGroup.findReverseRelated(new User());
				for (int i = 0 ; i < user.length ; i++ ) {
					myTable.add(user[i].getName(),2,row);
					++row;
				}
			}
			}

		myTable.add("Verkefnishópur",1,row);
			row +=1;

			if (project.getProjectGroupId() != -1) {
			ProjectGroup projectGroup = new ProjectGroup(project.getProjectGroupId());
				User[] user = (User[])projectGroup.findReverseRelated(new User());
				for (int i = 0 ; i < user.length ; i++ ) {
					myTable.add(user[i].getName(),2,row);
					++row;
				}
			}


		myTable.add("Verkefnisstóri",1,row);
			row +=1;

			if (project.getGroupId() != -1) {
				ProjectGroup projectManager = new ProjectGroup(project.getProjectManagerId());
				if (projectManager != null) {
					User[] user = (User[])projectManager.findReverseRelated(new User());
					for (int i = 0 ; i < user.length ; i++ ) {
						myTable.add(user[i].getName(),2,row);
						++row;
					}
				}
			}

		myTable.add("Aðrir hópar",1,row);
			row +=1;

			if (project.getGroupId() != -1) {
				ProjectGroup group = new ProjectGroup(project.getGroupId());
				if (group != null) {
					User[] user = (User[])group.findReverseRelated(new User());
					for (int i = 0 ; i < user.length ; i++ ) {
						myTable.add(user[i].getName(),2,row);
						++row;
					}
				}
			}




/*		myTable.add(new SubmitButton("takki","Áfram"),3,row);
			myTable.add(new HiddenInput("action","newProject3"),2,row);
			myTable.setAlignment(2,row,"right");
*/
		myTable.add(new SubmitButton("action","Verklýsing"),1,row);
		++row;
		myTable.add(new SubmitButton("action","Markmið/Árangur"),1,row);
		++row;
		myTable.add(new SubmitButton("action","Fjármál"),1,row);
		++row;
		myTable.add(new SubmitButton("action","Verkliðir - Tímarammi"),1,row);
			myTable.add(new HiddenInput("view","true"),1,1);
			++row;
			++row;

//		if (isAdmin()) {
			myTable.add(new SubmitButton("action","Breyta"),3,row);
                        myTable.add(new SubmitButton("action","Henda"),3,row);
//		}

			myTable.setColor(color);
			myTable.setCellspacing(0);
			myTable.setRowColor(1,header_color);
			myTable.setRowColor(row,header_color);



		myTable.add(new BackButton("Til Baka"),1,row);
		add(myForm);
	}



	public void newProject3() throws SQLException {
		String project_id = (String)iwc.getSession().getAttribute("project_id");
		Project project = new Project(Integer.parseInt(project_id));

                String wheel_final = iwc.getParameter("wheel_group");
                String project_final = iwc.getParameter("project_group");
                String manager_final = iwc.getParameter("project_manager");
                String other_final = iwc.getParameter("other_group");

                      project.setWheelGroupIdFinal(false);
                      project.setProjectGroupIdFinal(false);
                      project.setProjectManagerIdFinal(false);
                      project.setGroupIdFinal(false);

//                add(wheel_final+""+project_final+""+manager_final+""+other_final);

                    if (wheel_final == null) {
                      project.setWheelGroupIdFinal(true);
                    }
                    if (project_final == null) {
                      project.setProjectGroupIdFinal(true);
                    }
                    if (manager_final == null) {
                      project.setProjectManagerIdFinal(true);
                    }
                    if (other_final == null) {
                      project.setGroupIdFinal(true);
                    }

			project.setValid(true);
			project.update();
                        iwc.getServletContext().removeAttribute("all_projects_array");

		iwc.getSession().removeAttribute("project_id");

		Form myForm = new Form();
//                myForm.setMethod("get");


		Table myTable = new Table();
			myForm.add(myTable);
//			myTable.setBorder(0);
			myTable.add("Verkefni stofnað");
			myTable.addBreak();
			myTable.add(new Link(new com.idega.presentation.Image("/pics/bakka.gif","Til baka"),URI));

			myTable.setColor(color);
			myTable.setCellspacing(0);
//			myTable.setRowColor(1,header_color);
//			myTable.setRowColor(4,header_color);

		add(myForm);
	}

        public void delete(String project_id_string,boolean deletedFromProjectManager)throws SQLException,IOException {
            try {
                int project_id_int = Integer.parseInt(project_id_string);
		Project project = new Project(project_id_int);
		ProjectExtra[] extra = (ProjectExtra[]) project.findRelated(new ProjectExtra());

                if (extra != null) {
                  if (extra.length > 0) {
                    for (int i = 0 ; i < extra.length ; i++ ) {
                      project.removeFrom(extra[i]);
                      extra[i].delete();
                    }
                  }
                }

                project.delete();
                iwc.getServletContext().removeAttribute("all_projects_array");

            }
            catch (Exception e) {
               System.err.println(e.getMessage());
            }

            if (deletedFromProjectManager) {
              begin();
            }
            else {
              //getResponse().sendRedirect("/verkefni/index.jsp");
            }


        }

        public void deleteMembers() throws SQLException {
            String[] member_id = iwc.getParameterValues("member");
            String veljaAction = iwc.getParameter("save_action2");
            String group_id = iwc.getParameter("group_id");
            String member__id = iwc.getParameter("member_id");

            if (member_id != null)
            for (int p = 0 ; p < member_id.length ; p++ ) {
		User user= new User(Integer.parseInt(member_id[p]));
		EmploymentMemberInfo[] extra = (EmploymentMemberInfo[]) user.findReverseRelated(new EmploymentMemberInfo());
		Address[] address= (Address[]) user.findReverseRelated(new Address());
		Phone[] phone= (Phone[]) user.findReverseRelated(new Phone());
		//com.idega.projects.lv.entity.LoginTable[] logTable = (com.idega.projects.lv.entity.LoginTable[]) (new com.idega.projects.lv.entity.LoginTable()).findAllByColumn("member_id",member_id[0]);
		Division[] div  = (Division[]) user.findReverseRelated(new Division());
                ProjectGroup[] group = (ProjectGroup[]) user.findRelated(new ProjectGroup());
                //com.idega.jmodule.login.data.LoginType[] logType = (com.idega.jmodule.login.data.LoginType[]) member.findRelated(new com.idega.jmodule.login.data.LoginType());

                if (extra != null)
		for (int i = 0 ; i < extra.length ; i++) {
//			member.removeFrom(extra[i]);
			extra[i].removeFrom(user);
			extra[i].delete();
		}

		if (address != null)
		for (int i = 0 ; i < address.length ; i++) {
			address[i].removeFrom(user);
			address[i].delete();
		}

		if (phone != null)
		for (int i = 0 ; i < phone.length ; i++) {
			phone[i].removeFrom(user);
			phone[i].delete();
		}

		if (div != null)
		for (int i = 0 ; i < div.length ; i++ ) {
			div[i].reverseRemoveFrom(user);
		}

		if (group != null)
		for (int i = 0 ; i < group.length ; i++ ) {
			group[i].removeFrom(user);
		}
/*
		if (logType != null)
		for (int i = 0 ; i < logType.length ; i++ ) {
			logType[i].removeFrom(user);
		}

		if (logTable != null)
		for (int i = 0 ; i < logTable.length ; i++) {
			logTable[i].delete();
		}
*/
		user.delete();
            }

            if (member_id != null) {
              if (member_id.length == 1) {
		add("Starfsmanni hefur verið eytt<br>");
              }
              else {
		add("Starfsmönnum hefur verið eytt<br>");
              }
            }
            else {
               add("Enginn starfsmaður var valinn<br>");
            }
            Link linki = new Link(new com.idega.presentation.Image("/pics/bakka.gif","Til baka"),iwc.getRequestURI());
              linki.addParameter("action",veljaAction);
              if (group_id != null)
              linki.addParameter("groupd_id",group_id);
              if (member__id != null)
              linki.addParameter("member_id",member__id);



            add(linki);




        }







	public void main(IWContext iwc2)throws Exception {
          this.iwc = iwc2;
          URI = iwc.getRequestURI();

		String action = iwc.getParameter("action");

//                Integer kalli = (Integer) getRequest().getSession().getAttribute("project_id");

//           if (kalli != null) {
                if (iwc.getParameter("Breyta.y") != null) {
                  action = "Breyta";
                }
                if (iwc.getParameter("Henda.y") != null) {
                  action = "Henda";
                }
                if (iwc.getParameter("delete.x") != null){
                  action = "delete_members";
                }



		if (action == null) {
			begin();
		}
		else if (action.equals("Stofna")) {
			newProject();
		}
		else if ( (action.equals("newProject2")) || (action.equals("Breyta")) || (action.equalsIgnoreCase("Til Baka")) ) {
			if (action.equals("newProject2")) {
					iwc.getSession().removeAttribute("project_id");
			}
			else if  (iwc.getParameter("project_id") != null ) {
					iwc.getSession().setAttribute("project_id",iwc.getParameter("project_id"));
			}
			newProject2();
		}
		else if (action.equals("Skoða/breyta")) {
	/*session?*/		iwc.setSessionAttribute("project_id",iwc.getParameter("project"));
                        System.err.println(""+iwc.getParameter("project"));
//			getRequest().getSession().removeAttribute("project_id");
                        newProject2();
//			viewProject();
		}
                else if (action.equals("Henda")) {
                      String project_id_temp = iwc.getParameter("project");
                      if (project_id_temp == null) {
                        project_id_temp = (String) iwc.getSession().getAttribute("project_id");
                      }

                      if (project_id_temp != null) {
                          if (iwc.getParameter("Henda.y") == null) {
                            delete(project_id_temp,true);
                          }
                          else {
                            delete(project_id_temp,false);
                          }
                      }
                      else {
                        add(project_id_temp);
                      }

                }
		else if (action.equals("newProject3")) {
			if (iwc.getParameter("takki").equals("Áfram")) {
				newProject3();
			}
		}
		else if (action.equals("plusStyri") || action.equals("plusVerkhop") || action.equals("plusVerkMan") || action.equals("plusGroup")       ) {
			groups();
		}
		else if (action.equals("Nýr hópur")) {
			newGroup();
		}
		else if (action.equals("Skoða hóp")) {
			viewGroup();
		}
		else if (action.equals("Henda hóp")) {
                        this.deleteGroup();
		}
		else if (action.equals("Verklýsing")) {
			 if  (iwc.getParameter("project_id") != null ) {
					iwc.getSession().setAttribute("project_id",iwc.getParameter("project_id"));
			}
			projectDescription();
		}
		else if (action.equals("Markmið/Árangur")) {
			 if  (iwc.getParameter("project_id") != null ) {
					iwc.getSession().setAttribute("project_id",iwc.getParameter("project_id"));
			}
			projectGoals();
		}
		else if (action.equals("Fjármál")) {
			 if  (iwc.getParameter("project_id") != null ) {
					iwc.getSession().setAttribute("project_id",iwc.getParameter("project_id"));
			}
			projectFinances();
		}
		else if (action.equals("Verkliðir - Tímarammi")) {
			 if  (iwc.getParameter("project_id") != null ) {
					iwc.getSession().setAttribute("project_id",iwc.getParameter("project_id"));
			}
			projectTasks();
		}
		else if (action.equals("Vista")) {
			save();
		}
		else if (action.equals("Eyða")) {
			deleteMembers();
		}



		else {
			add(action);
		}
/*            } // kalli
            else {
        	begin();
            }
*/
	}

}