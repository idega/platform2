// idega 2000 - gimmi
package com.idega.block.poll.presentation;

import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.jmodule.*;
import com.idega.data.*;
import java.io.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import javax.servlet.http.*;
import com.idega.block.poll.data.*;
import java.sql.SQLException;
import com.idega.block.text.data.*;
import java.util.*;
import com.idega.util.*;

public class PollAdmin extends JModuleObject{

private String attribute_name = "idega_id";
private int attribute_id = 1;

int width = 150;
int depth = 10;

//String header_color = "#ABABAB";
String color_1 = "#55aa55";
String color_2 = "#99CC66";

//String color = "#EFEFEF";

boolean isAdmin = false;

public PollAdmin() {
  super();
}

  public void main(ModuleInfo modinfo) throws Exception{
    /*this.isAdmin=AccessControl.hasEditPermission(this,modinfo);

    HttpServletRequest request = getRequest();
    String URI = request.getRequestURI();
    String action = request.getParameter("action");

    setAttributes(modinfo,request);
    getAction(modinfo, request);
  }

  private void getAction(ModuleInfo modinfo,HttpServletRequest request) throws IOException, SQLException {
    String submit = request.getParameter("submit");
    String submit2 = request.getParameter("submit2");
    String poll_id = request.getParameter("poll_id");

    if (poll_id == null)
    if (submit != null) {
      if (submit.equals("N\u00fd k\u00f6nnun")) {
              newQuestion();
      }
      else if (submit.equals("Vista")) {
          if (getRequest().getParameter("update") == null) {
              createQuestion(false);
            }
          else {
              createQuestion(true);
          }
      }
      else if (submit.equals("Breyta")) {
              String temp_poll_id = getRequest().getParameter("poll");
              if (temp_poll_id != null) {
                updateQuestion(temp_poll_id);
              }
      }
      else if (submit.equals("Nota")) {
              String temp_poll_id = getRequest().getParameter("poll");
              if (temp_poll_id != null) {
                usePoll(temp_poll_id);
              }
      }
      else if (submit.equals("Nota enga")) {
              useNoPoll();
      }
      else if (submit.equals("Henda")) {
              deleteQuestion();
      }
      else if (submit.equals("close_window")) {
      }
      else {
        start(modinfo);
      }
    }
    else if (submit2 != null) {
      if (submit2.equals("Breyta")) {
        changeAnswer(getRequest().getParameter("poll_option"));
      }
      else if (submit2.equals("Vista svar")) {
        saveAnswer();
      }
      else if (submit2.equals("Breyta svari")) {
        updateAnswer();
      }
      else if (submit2.equals("Henda")) {
        deleteAnswer();
      }
      else if (submit2.equals("N\u00fdtt svar")) {
        newAnswer();
      }
      else if (submit2.equals("Breyta spurningu")) {
        editQuestion();
      }
    }
    else {
      start(modinfo);
    }

    if (poll_id != null) {
        updateQuestion(poll_id);
    }
  }


  public void start(ModuleInfo modinfo) throws SQLException, IOException {
    String admin_poll_id = (String) modinfo.getRequest().getSession().getAttribute("admin_poll_id");

    Form myForm = new Form();

    Table myTable = new Table(1,6);
      myTable.setBorder(0);
      myTable.setColor(color_2);
      myTable.setRowColor(1,color_1);
      myTable.setAlignment(1,6,"right");
      myTable.setRowColor(6,color_1);
      myForm.add(myTable);

      myTable.addText("Sko\u00f0anakannanir",1,1);
      myTable.addBreak();

    PollAttributes[] poll_attrib = (PollAttributes[]) (new PollAttributes()).findAllByColumn("attribute_name",attribute_name,"attribute_id",""+attribute_id);

    String sql_string = "Select * from poll where";
    if (poll_attrib != null) {
      if (poll_attrib.length > 0 ) {
        for (int i = 0 ; i < poll_attrib.length ; i++) {
          sql_string = sql_string + " poll_id = " +poll_attrib[i].getPollId();
          if (i != poll_attrib.length-1 ) {
            sql_string = sql_string + " or";
          }
        }
      }
      if (!(sql_string.equals("Select * from poll where"))) {
        sql_string = sql_string + " order by poll_id descending";
        Poll[] polls = (Poll[]) (new Poll()).findAll(sql_string);

        DropdownMenu pollin= new DropdownMenu(polls);
          pollin.setAttribute("size","10");
          pollin.setSelectedElement(admin_poll_id);
          myTable.add(pollin,1,2) ;

          if (admin_poll_id != null) {
              pollin.setSelectedElement("admin_poll_id");
          }
      }
      else {
        myTable.add("Engin könnun til",1,2);
      }

    }

    myTable.add(new SubmitButton("submit","N\u00fd k\u00f6nnun"),1,3);
    myTable.add(new SubmitButton("submit","Breyta"),1,4);
    myTable.add(new SubmitButton("submit","Nota"),1,5);
    myTable.add(new SubmitButton("submit","Nota enga"),1,5);
    myTable.add(new SubmitButton("submit","Henda"),1,6);

    add(myForm);

  }

public void usePoll(String poll_id) throws SQLException, IOException{



              if (poll_id != null) {
                useNoPoll();
                Poll poll = new Poll(Integer.parseInt(poll_id));
                  poll.setInUse(true);
                  poll.update();

//                    add("Skoðanakönnunin \""+poll.getQuestion()+"\" hefur verið tekin í notkun");
              }

//                add("<br>");
//                add(new BackButton("Til baka"));

//		getModuleInfo().getResponse().sendRedirect("polltest.jsp");
  //this.setParentToReload();
  //this.close();
}

public void useNoPoll() throws SQLException, IOException{


              PollAttributes[] poll_attrib = (PollAttributes[]) (new PollAttributes()).findAllByColumn("attribute_name",attribute_name,"attribute_id",""+attribute_id);

              String sql_string = "Select * from poll where";
              if (poll_attrib != null) {
                if (poll_attrib.length > 0 ) {
                  for (int i = 0 ; i < poll_attrib.length ; i++) {
                    sql_string = sql_string + " poll_id = " +poll_attrib[i].getPollId();
                    if (i != poll_attrib.length-1 ) {
                      sql_string = sql_string + " or";
                    }
                  }
                }
                if (!(sql_string.equals("Select * from poll where"))) {
                  Poll[] polls = (Poll[]) (new Poll()).findAll(sql_string);
                  if (polls != null) {
                    if (polls.length > 0) {
                      for (int j = 0 ; j < polls.length ; j++) {
                        polls[j].setInUse(false);
                        polls[j].update();
                      }
                    }
                  }
                }
                else {
                  add("Engin könnun til");
                }

              }

//		getModuleInfo().getResponse().sendRedirect("polltest.jsp");

}



public void	updateQuestion(String poll_id) throws SQLException, IOException {
              String admin_poll_id = (String) getRequest().getSession().getAttribute("admin_poll_id");
//                add("|"+admin_poll_id+"|");

  Form myForm = new Form();
  Table myTable = new Table(1,6);
    myTable.setBorder(0);
    myTable.setColor(color_2);
    myTable.setRowColor(1,color_1);
    myTable.setAlignment(1,6,"right");
    myTable.setRowColor(6,color_1);
//			myForm.setMethod("get");
  myForm.add(myTable);


            if (poll_id != null) {
  Poll thePoll = new Poll(Integer.parseInt(poll_id));

  myTable.addText(thePoll.getQuestion());
              myTable.add(new SubmitButton("submit2","Breyta spurningu"),1,2);


  myTable.addText("<br>Sv\u00f6r",1,2);
  myTable.addBreak(1,2);
  myTable.add(new SelectionBox(new PollOption().findAllByColumn("poll_id",poll_id)),1,2);
  myTable.add(new SubmitButton("submit2","Breyta"),1,4);
  myTable.add(new SubmitButton("submit2","N\u00fdtt svar"),1,3);
  myTable.add(new SubmitButton("submit2","Henda"),1,5);

              myTable.add(new SubmitButton("submit","Til baka"),1,6);
  myTable.add(new HiddenInput("poll",poll_id));


  add(myForm);
            }

}

      public void editQuestion() throws SQLException, IOException{
          String poll_id = getRequest().getParameter("poll");
              String admin_poll_id = (String) getRequest().getSession().getAttribute("admin_poll_id");
//                add("|"+admin_poll_id+"|");


          if (poll_id != null) {

              Poll poll = new Poll(Integer.parseInt(poll_id));
  Form myForm = new Form();
//			myForm.setMethod("get");
  Table myTable = new Table();
    myTable.setBorder(0);
                      myTable.setCellpadding(0);
                      myTable.setCellspacing(0);
  myForm.add(myTable);

              myTable.add("<b>Spurning</b>",1,1);
              myTable.add("Nafn",1,2);
              TextInput name;



                if (poll.getName() != null) {
                  name = new TextInput("question_name",poll.getName() );
                }
                else {
                  name = new TextInput("question_name");
                }
              myTable.add(name,2,2);
              myTable.add("Frá",1,3);
              myTable.add(new DateInput("from"),2,3);
              myTable.add("Til",1,4);
              myTable.add(new DateInput("to"),2,4);

              myTable.add(new SubmitButton("submit","Vista"),2,6);
              myTable.add(new HiddenInput("update","true"));



    myTable.setColor(color_2);
    myTable.setRowColor(1,color_1);
    myTable.setAlignment(2,6,"right");
    myTable.setRowColor(6,color_1);

              add(myForm);
          }

      }

      public void createQuestion(boolean update) throws SQLException,IOException {
        String URI = getRequest().getRequestURI();

        String poll_id = getRequest().getParameter("poll");

        if (poll_id == null) {
          poll_id = (String) getRequest().getSession().getAttribute("admin_poll_id");
        }

        String name = getRequest().getParameter("question_name");
        String from_day = getRequest().getParameter("from_day");
        String from_month = getRequest().getParameter("from_month");
        String from_year = getRequest().getParameter("from_year");
        String to_day = getRequest().getParameter("to_day");
        String to_month = getRequest().getParameter("to_month");
        String to_year = getRequest().getParameter("to_year");

        boolean useDate = true;

        if ( (from_day.equals("")) || (from_month.equals("")) || (from_year.equals("")) || (to_day.equals("")) || (to_month.equals("")) || (to_year.equals("")) ) {
            useDate = false;
        }




//          add("|"+admin_poll_id+"|");
//          add("update"+update);
       if (update) {

              if (name.equals("")) {
                  add("Þú verður skrifa inn nafn<br>");
                  add(new BackButton("Til baka"));
              }
              else { // name != ""

                  Poll poll = new Poll(Integer.parseInt(poll_id));

                  poll.setQuestion(name);
                  if (useDate) {
                    idegaTimestamp stamp_from = new idegaTimestamp(from_year+"-"+from_month+"-"+from_day+" 00:00:00.0");
                    idegaTimestamp stamp_to = new idegaTimestamp(from_year+"-"+from_month+"-"+from_day+" 00:00:00.0");
                    poll.setStartTime(stamp_from.getTimestamp());
                    poll.setEndTime(stamp_to.getTimestamp());
                  }

                  poll.update();


                  PollAttributes poll_attrib = new PollAttributes();
                    poll_attrib.setPollId(poll.getID());
                    poll_attrib.setAttributeName(attribute_name);
                    poll_attrib.setAttributeId(attribute_id);
                  poll_attrib.insert();

                 updateQuestion(Integer.toString(poll.getID()));
              }

       }
       else {
              if (name.equals("")) {
                  add("Þú verður skrifa inn nafn<br>");
                  add(new BackButton("Til baka"));
              }
              else { // name != ""

                  Poll poll = new Poll();

                  poll.setQuestion(name);
                  if (useDate) {
                    idegaTimestamp stamp_from = new idegaTimestamp(from_year+"-"+from_month+"-"+from_day+" 00:00:00.0");
                    idegaTimestamp stamp_to = new idegaTimestamp(from_year+"-"+from_month+"-"+from_day+" 00:00:00.0");
                    poll.setStartTime(stamp_from.getTimestamp());
                    poll.setEndTime(stamp_to.getTimestamp());
                  }

                  poll.insert();

                  PollAttributes poll_attrib = new PollAttributes();
                    poll_attrib.setPollId(poll.getID());
                    poll_attrib.setAttributeName(attribute_name);
                    poll_attrib.setAttributeId(attribute_id);
                  poll_attrib.insert();

                  getRequest().getSession().removeAttribute("admin_poll_id");
                  getRequest().getSession().setAttribute("admin_poll_id",""+poll.getID());

                 updateQuestion(Integer.toString(poll.getID()));
              }
          }


      }

  public void newQuestion() throws SQLException, IOException {
    Form myForm = new Form();

    Table myTable = new Table();
      myTable.setBorder(0);
      myTable.setCellpadding(0);
      myTable.setCellspacing(0);
      myForm.add(myTable);

      myTable.add("<b>Spurning</b>",1,1);
      myTable.add("Nafn",1,2);
      myTable.add(new TextInput("question_name"),2,2);
      myTable.add("Frá",1,3);
      myTable.add(new DateInput("from"),2,3);
      myTable.add("Til",1,4);
      myTable.add(new DateInput("to"),2,4);
      myTable.add(new SubmitButton("submit","Vista"),2,6);
      myTable.setColor(color_2);
      myTable.setRowColor(1,color_1);
      myTable.setAlignment(2,6,"right");
      myTable.setRowColor(6,color_1);

    add(myForm);
  }

  public void newAnswer() throws SQLException, IOException  {
    String question_id = getRequest().getParameter("poll");

    Form myForm = new Form();
    Table table = new Table(2,4);
      myForm.add(table);
      table.setColor(color_2);
      table.setRowColor(1,color_1);
      table.setRowColor(4,color_1);
      table.setAlignment(2,4,"right");
      table.setBorder(0);
      table.add("<b>Nýtt svar</b>",1,1);
      table.add("Svar : ",1,2);
      table.mergeCells(1,1,2,1);
      table.setAlignment(1,1,"center");
      table.add(new TextInput("answer"),2,2);
      table.add(new SubmitButton("submit2","Vista svar"),2,3);

    if (question_id != null) {
      PollOption option = new PollOption();
        option.setPollID(Integer.parseInt(question_id));
        option.insert();

      table.add(new HiddenInput("poll_option",""+option.getID()));
      table.add(new HiddenInput("poll",question_id));
    }
    else{
        table.add("Engin spurning valin",1,2);
    }

    table.add(new BackButton("Til baka"),2,4);
    add(myForm);
  }

  public void saveAnswer() throws SQLException,IOException {
    String poll_option = getRequest().getParameter("poll_option");
    String poll_id = getRequest().getParameter("poll");
    String poll_answer = getRequest().getParameter("answer");


    if (poll_option != null) {
      PollOption poll_opt = new PollOption(Integer.parseInt(poll_option));
      if (poll_answer != null) {
        poll_opt.setAnswer(poll_answer);
        poll_opt.update();
      }
      else {
        poll_opt.delete();
      }
    }

    updateQuestion(poll_id);
  }

  public void updateAnswer() throws SQLException,IOException {
    String poll_option = getRequest().getParameter("answer_id");
    String poll_id = getRequest().getParameter("poll");
    String poll_answer = getRequest().getParameter("answer");

    if (poll_option != null) {
      PollOption poll_opt = new PollOption(Integer.parseInt(poll_option));
      if (poll_answer != null) {
        poll_opt.setAnswer(poll_answer);
        poll_opt.update();
      }
    }

    updateQuestion(poll_id);
  }

  public void changeAnswer(String answer_id) throws SQLException, IOException {
    String poll_id = getRequest().getParameter("poll");

    Form myForm = new Form();
    Table table = new Table(2,4);
      myForm.add(table);
      table.setColor(color_2);
      table.setRowColor(1,color_1);
      table.setRowColor(4,color_1);
      table.setAlignment(2,4,"right");
      table.setBorder(0);
      table.add("<b>Nýtt svar</b>",1,1);
      table.add("Svar : ",1,2);
      table.mergeCells(1,1,2,1);
      table.setAlignment(1,1,"center");
      table.add(new SubmitButton("submit2","Breyta svari"),2,3);
      table.add(new HiddenInput("poll",poll_id));

      if (answer_id != null ) {
        PollOption option = new PollOption(Integer.parseInt(answer_id));
        table.add(new TextInput("answer",option.getAnswer() ),2,2);
        table.add(new HiddenInput("answer_id",answer_id));

      }
      else {
        add("Það verður að velja svar!");
      }

      add(myForm);
      add(new BackButton("Til baka"));
  }

  public void deleteAnswer() throws SQLException,IOException {
    String answer_id = getRequest().getParameter("poll_option");
    String poll_id = getRequest().getParameter("poll");

    if (answer_id != null) {
      PollOption option = new PollOption(Integer.parseInt(answer_id));
      option.delete();
    }

    updateQuestion(poll_id);
  }

  public void deleteQuestion() throws SQLException, IOException {
    String question_id = getRequest().getParameter("poll");
    Poll poll = new Poll(Integer.parseInt(question_id));
    PollAttributes.getStaticInstance("com.idega.block.data.PollAttributes").deleteMultiple("poll_id",Integer.toString(poll.getID()));

    poll.delete();
    getResponse().sendRedirect("pollAdmin.jsp");
  }

  public void setConnectionAttributes(String attribute_name, int attribute_id) {
    this.attribute_name = attribute_name;
    this.attribute_id = attribute_id;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public void setDepth(int depth) {
    this.depth = depth;
  }

  private void setAttributes(ModuleInfo modinfo,HttpServletRequest request) {
      String temp_attribute_name = request.getParameter("attribute_name");
      String temp_attribute_id = request.getParameter("attribute_id");

      if ((temp_attribute_name != null ) && (temp_attribute_id!= null)) {
        this.attribute_name = temp_attribute_name;
        this.attribute_id = Integer.parseInt(temp_attribute_id);

        modinfo.getSession().setAttribute("attribute_name",attribute_name);
        modinfo.getSession().setAttribute("attribute_id",""+attribute_id);
      }
      else {
          String temp_attribute_name_2 = (String) modinfo.getSession().getAttribute("attribute_name");
          String temp_attribute_id_2 = (String) modinfo.getSession().getAttribute("attribute_id");
          if ((temp_attribute_name_2 != null ) && (temp_attribute_id_2 != null)) {
            this.attribute_name = temp_attribute_name_2;
            this.attribute_id = Integer.parseInt(temp_attribute_id_2);
          }
      }*/
  }
}