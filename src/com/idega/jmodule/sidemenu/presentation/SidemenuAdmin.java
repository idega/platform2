// idega 2000 - gimmi
package com.idega.jmodule.sidemenu.presentation;

import com.idega.jmodule.*;
import com.idega.data.*;
import java.io.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import javax.servlet.http.*;
import com.idega.jmodule.poll.data.*;
import java.sql.SQLException;
import com.idega.jmodule.text.data.*;
import com.idega.jmodule.sidemenu.data.*;
import java.util.*;
import com.idega.util.*;
import com.idega.builder.data.*;
import com.idega.builder.data.IBObject;

public class SidemenuAdmin extends Block{
        private String attribute_name = "idega_id";
        private int attribute_id = 1;

        private String admin_page_url = "sidemenu_admin.jsp";

Vector colors = new Vector();
int width = 150;
int depth = 10;

String header_color = "#ABABAB";
String color = "#EFEFEF";

boolean isAdmin = false;
boolean show_menu_name = false;




    public SidemenuAdmin() {
      super();
      setDefaultColor();
    }

    private void setDefaultColor() {
      colors.addElement("#EFEFEF");
      colors.addElement("#DEDEDE");
      colors.addElement("#CDCDCD");
      colors.addElement("#BCBCBC");
      colors.addElement("#ABABAB");
      colors.addElement("#9A9A9A");
      colors.addElement("#898989");
      colors.addElement("#787878");
      colors.addElement("#676767");
      colors.addElement("#565656");
      colors.addElement("#454545");
      colors.addElement("#343434");
      colors.addElement("#232323");
      colors.addElement("#121212");
      colors.addElement("#010101");
    }

    public void setColors(Vector colors) {
      this.colors = colors;
    }

    public void setAdminPageUrl(String url){
      this.admin_page_url = url;
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

    public void showMenuName(boolean show) {
      this.show_menu_name = show;
    }


    private void setAttributes(IWContext iwc) {
        HttpServletRequest request = getRequest();
        String temp_attribute_name = request.getParameter("attribute_name");
	String temp_attribute_id = request.getParameter("attribute_id");

	if ((temp_attribute_name != null ) && (temp_attribute_id!= null)) {
		this.attribute_name = temp_attribute_name;
		this.attribute_id = Integer.parseInt(temp_attribute_id);

                iwc.getSession().setAttribute("attribute_name",attribute_name);
                iwc.getSession().setAttribute("attribute_id",""+attribute_id);
	}
        else {
            String temp_attribute_name_2 = (String) iwc.getSession().getAttribute("attribute_name");
            String temp_attribute_id_2 = (String) iwc.getSession().getAttribute("attribute_id");
            if ((temp_attribute_name_2 != null ) && (temp_attribute_id_2 != null)) {
		this.attribute_name = temp_attribute_name_2;
		this.attribute_id = Integer.parseInt(temp_attribute_id_2);
            }

        }

    }

    public void main(IWContext iwc) throws Exception{

      this.isAdmin=this.isAdministrator(iwc);

      HttpServletRequest request = getRequest();
      String URI = request.getRequestURI();
      String action = request.getParameter("side_menu_action");

      setAttributes(iwc);

      if (action.equals("admin")){
        if (isAdmin)
        removeApplication(iwc);
        admin();
      }
      else if (action.equals("new_menu")) {
        newMenu();
      }
      else if (action.equals("save_menu_name")) {
        saveMenu();
      }
      else if (action.equals("Breyta Menu")) {
          String menu_id = getRequest().getParameter("menu_id");
          if (menu_id != null) {
              editMenu(Integer.parseInt(menu_id));
          }
          else {
            add("Gleymdir að velja<br>");
            add(new BackButton("Til baka"));
          }
      }
      else if (action.equals("Henda Menu")) {
          String menu_id = getRequest().getParameter("menu_id");
          if (menu_id != null) {
              deleteMenu(iwc,Integer.parseInt(menu_id));
          }
          else {
            add("Gleymdir að velja<br>");
            add(new BackButton("Til baka"));
          }
      }
      else if (action.equals("use_no_menu")) {
        useNoMenu(true);
      }
      else if (action.equals("Ný grein")) {
        newOption();
      }
      else if (action.equals("Vista")) {
        save(iwc);
      }
      else if (action.equals("Til baka")) {
        if (isAdmin)
          admin();
      }
      else if (action.equals("Nota Menu")) {
          String menu_id = getRequest().getParameter("menu_id");
          if (menu_id != null) {
              useMenu(Integer.parseInt(menu_id));
          }
          else {
            add("Gleymdir að velja<br>");
            add(new BackButton("Til baka"));
          }
      }
      else if (action.equals("Henda grein")) {
        String option_id = getRequest().getParameter("sidemenu_option_id");
        String menu_id = getRequest().getParameter("menu_id");

        deleteOption(iwc, Integer.parseInt(option_id));

        editMenu(Integer.parseInt(menu_id));
      }
      else if (action.equals("Breyta grein")) {
        editOption();
      }



      /*
        form.add(new SubmitButton("side_menu_action","Henda Menu"));

        table.add(new SubmitButton("side_menu_action","Ný grein"),2,1);

        table.add(new SubmitButton("side_menu_action","Breyta grein"),2,2);

        table.add(new SubmitButton("side_menu_action","Henda grein"),2,3);
  */


    }

    private void removeApplication(IWContext iwc) throws SQLException{

        Table table = (Table) iwc.getServletContext().getAttribute("sidemenu_table"+attribute_id+"0");
        getRequest().getSession().removeAttribute("side_menu_session"+attribute_id);

        String open = getRequest().getParameter("open");
          if (open == null) { open = "0";}


        if (table != null) {
            try {
              System.out.println("Table : sidemenu_table"+attribute_id+"0 removed from application");
              iwc.getServletContext().removeAttribute("sidemenu_table"+attribute_id+"0");
            }
            catch (Exception e) {
              System.out.println("Table : sidemenu_table"+attribute_id+"0 NOT removed from application");
            }
            SidemenuAttributes[] attributes = (SidemenuAttributes[])new SidemenuAttributes().findAllByColumn("attribute_name",attribute_name,"attribute_id",""+attribute_id);
            Text text = new Text();

            if (attributes != null) {
                if (attributes.length > 0 ) {
                    for (int i = 0 ; i < attributes.length ; i++) {
                        SidemenuModule side = new SidemenuModule(attributes[0].getSidemenuId());
                        SidemenuOption[] opt = (SidemenuOption[]) side.findReverseRelated(new SidemenuOption());
                        if (opt != null) {
                          if (opt.length > 0) {
                            for (int o=0 ; o < opt.length ; o ++) {
                                iwc.getServletContext().removeAttribute("sidemenu_table"+attribute_id+""+opt[o].getID());
                                System.out.println("Table : sidemenu_table"+attribute_id+""+opt[o].getID()+" removed from application");
                            }
                          }
                        }
                    }
                }
            }





        }


    }

    private void admin() throws SQLException{
      SidemenuAttributes[] attributes = (SidemenuAttributes[])new SidemenuAttributes().findAllByColumn("attribute_name",attribute_name,"attribute_id",""+attribute_id);
      SidemenuModule menu = null;
      DropdownMenu dropdown = new DropdownMenu("menu_id");
        dropdown.setAttribute("size","15");

      if (attributes != null) {
        if (attributes.length > 0 ) {
          String display = "";
          for (int i = 0 ; i < attributes.length ; i++) {
            menu = new SidemenuModule(attributes[i].getSidemenuId());
            display = menu.getName();
                dropdown.addMenuElement(menu.getID(),display);
            if (menu.isInUse()) {
              dropdown.setSelectedElement(""+menu.getID());
            }

          }
        }
      }

      Form form = new Form();
        form.setMethod("get");

      Table table = new Table(2,6);
        form.add(table);
        table.setBorder(0);
        table.setCellpadding(0);
        table.setColor(color);
        table.setRowColor(1,header_color);
        table.setRowColor(6,header_color);

        table.mergeCells(1,1,2,1);
        table.setAlignment(1,1,"center");
        Text header = new Text("SidemenuStjórinn");
          header.setFontColor("#FFFFFF");
          header.setBold();
        table.add(header,1,1);

        table.add(dropdown,1,2);
          table.mergeCells(1,2,1,5);
          table.setVerticalAlignment(1,2,"top");
//        table.add(new SubmitButton("side_menu_action","Nýtt Menu"),2,2);
        table.add(new SubmitButton("side_menu_action","Breyta Menu"),2,2);
        table.add(new SubmitButton("side_menu_action","Nota Menu"),2,3);
//        table.add(new SubmitButton("side_menu_action","Nota ekkert menu"),2,4);
        table.add(new SubmitButton("side_menu_action","Henda Menu"),2,4);



      add(form);

    }

    private void save(IWContext iwc) throws Exception{
      String action = getRequest().getParameter("save_action");

      if (action != null) {
        if (action.equals("new_option")) {
          saveNewOption(iwc);
        }
        else if (action.equals("update_option")) {
          updateOption();
        }




      }
      else {
        add("Eitthvað mis....<br>");
        add(new BackButton("Til baka"));
      }


    }

    private void newMenu() {
        Form form = new Form();
        Table table = new Table();
          form.add(table);
          table.setBorder(1);
          table.add("Name : ",1,2);
          table.add(new TextInput("menu_name",""),2,2);
          table.add(new BackButton("Til baka"),1,3);
          table.add(new SubmitButton("event","Vista"),2,3);
          table.add(new HiddenInput("side_menu_action","save_menu_name"));
        add(form);
    }

    private void saveMenu() throws SQLException {
      String name = getRequest().getParameter("menu_name");

      if (name == null) {
          add("Villa!!!!<br>");
          add(new BackButton("Til baka"));
      }
      else {

          SidemenuModule menu = new SidemenuModule();
            menu.setName(name);
            menu.setInUse(false);
            menu.insert();

          SidemenuAttributes attrib = new SidemenuAttributes();
            attrib.setAttributeId(attribute_id);
            attrib.setAttributeName(attribute_name);
            attrib.setSidemenuId(menu.getID());
            attrib.insert();

          editMenu(menu.getID());

      }
    }

    private void deleteMenu(IWContext iwc, int menu_id) throws SQLException{
        SidemenuModule menu = new SidemenuModule(menu_id);
        SidemenuOption[] options = (SidemenuOption[]) menu.findReverseRelated(new SidemenuOption());
        SidemenuAttributes[] attrib = (SidemenuAttributes[])(new SidemenuAttributes()).findAllByColumn("sidemenu_id",""+menu.getID());
        if (options != null) {
          if (options.length > 0) {
            for (int i = 0 ; i < options.length ; i++ ) {
                deleteOption(iwc,options[i].getID());
            }
          }
        }

        if (attrib != null) {
          if (attrib.length > 0) {
            for (int i = 0 ; i < attrib.length ; i++) {
                attrib[i].delete();
            }
          }
        }


        menu.delete();
        admin();

    }

    private void useMenu(int menu_id) throws SQLException{
      useNoMenu(false);

      SidemenuModule menu = new SidemenuModule(menu_id);
        menu.setInUse(true);
        menu.update();

      admin();
    }


    private void useNoMenu(boolean go_to_startpage) throws SQLException{
        SidemenuAttributes[] attrib = (SidemenuAttributes[])(new SidemenuAttributes()).findAllByColumn("attribute_name",attribute_name,"attribute_id",attribute_id+"");
        SidemenuModule menu;

        if (attrib != null) {
          if (attrib.length > 0) {
            for (int i = 0 ; i < attrib.length ; i++) {
              menu = new SidemenuModule(attrib[i].getSidemenuId());
              if (menu != null) {
                menu.setInUse(false);
                menu.update();
              }
            }
          }
        }
        if (go_to_startpage) {
          admin();
        }

    }

    private void editMenu(int menu_id) throws SQLException{

        Vector items = new Vector();
        int position = 0;
        int option_id = 0;
        String pre_string = "";
        Integer[] intArray = new Integer[2];
        SidemenuModule menu = new SidemenuModule(menu_id);
        SidemenuOption option;
        SidemenuOption[] options = (SidemenuOption[]) menu.findReverseRelated(new SidemenuOption());
        DropdownMenu dropdown = new DropdownMenu("sidemenu_option_id");
          dropdown.setAttribute("size","10");

        if (options != null) {
          if (options.length > 0) {
            for (int i = 0 ; i < options.length ; i++ ) {
              findNodes(items,options[i].getID(),1,new SidemenuOption());
            }
          }
        }


///////////////////////////////////////////////////dropdown smíðað

        dropdown.addMenuElement(option_id,menu.getName());

        if (items.size() > 0) {
          for (int i = 0 ; i < items.size() ; i++) {
            pre_string = "";
            intArray = (Integer[])items.elementAt(i);
            option_id = intArray[0].intValue();

            option = new SidemenuOption(option_id);

            position =  intArray[1].intValue();
            for (int j = 0 ; j < (position+1) ; j++) {
              pre_string = pre_string + "&nbsp;&nbsp;";
            }

            dropdown.addMenuElement(option_id,pre_string + "-"+option.getName());
          }
        } /// dropdown til

        Form form = new Form();
          form.setMethod("get");
        Table table = new Table();
          form.add(table);
          table.setBorder(1);
          table.mergeCells(1,1,1,6);


        table.add(dropdown,1,1);

        table.add(new SubmitButton("side_menu_action","Ný grein"),2,1);
          table.setVerticalAlignment(2,1,"top");
            table.addBreak(2,1);
          table.add("Vertu búin/n að velja <u>yfirgrein</u> hinnar nýju",2,1);
        table.add(new SubmitButton("side_menu_action","Breyta grein"),2,2);
          table.setVerticalAlignment(2,2,"top");
        table.add(new SubmitButton("side_menu_action","Henda grein"),2,3);
          table.setVerticalAlignment(2,3,"top");
        table.add(new SubmitButton("side_menu_action","Til baka"),2,4);
          table.setAlignment(2,4,"right");
          table.setVerticalAlignment(2,4,"botton");

        table.add(new HiddenInput("menu_id",menu_id+""));



        add(form);


    }

    private void newOption() throws SQLException{
      String option_id = getRequest().getParameter("sidemenu_option_id");
      String menu_id = getRequest().getParameter("menu_id");

      if (option_id != null) {
        Form form = new Form();
          form.setMethod("get");

        Table table = new Table();
          form.add(table);
          table.setBorder(1);


	IBObject[] object = (IBObject[])(new IBObject()).findAllOrdered("object_name");

	DropdownMenu dropdown = new DropdownMenu("ar_object_id");
	if (object != null) {
		if (object.length > 0 ) {
			for (int i = 0 ; i < object.length ; i++ ) {
				dropdown.addMenuElement(object[i].getID(), object[i].getName());
			}
		}
	}



        table.add("Nafn : ",1,2);
        table.add(new TextInput("sidemenu_option_name"),2,2);
        table.add(new HiddenInput("sidemenu_option_id",option_id),2,2);
        table.add(new HiddenInput("menu_id",menu_id),2,2);
        table.add(new HiddenInput("save_action","new_option"),2,2);
        table.add("Skilar : ",1,3);
        table.add(dropdown,2,3);
        table.add(new SubmitButton("side_menu_action","Vista"),2,4);

        add(form);
      }
      else {
        add("Það verður að velja<br>");
        add(new BackButton("til baka"));
      }

    }
    private void editOption() throws SQLException{
      String option_id = getRequest().getParameter("sidemenu_option_id");
      String menu_id = getRequest().getParameter("menu_id");
      String name = "";



      if (option_id != null) {
        Form form = new Form();
          form.setMethod("get");

        Table table = new Table();
          form.add(table);
          table.setBorder(1);

        SidemenuOption option = new SidemenuOption(Integer.parseInt(option_id));
        name = option.getName();


        table.add("Nafn : ",1,2);
        table.add(new TextInput("sidemenu_option_name",name),2,2);
        table.add(new HiddenInput("sidemenu_option_id",""+option.getID()),2,2);
        table.add(new HiddenInput("menu_id",menu_id),2,2);
        table.add(new HiddenInput("save_action","update_option"),2,2);
        table.add(new SubmitButton("side_menu_action","Vista"),2,3);

        add(form);
      }
      else {
        add("Það verður að velja<br>");
        add(new BackButton("til baka"));
      }

    }

    private void saveNewOption(IWContext iwc) throws Exception{
      String option_id = getRequest().getParameter("sidemenu_option_id");
      String name = getRequest().getParameter("sidemenu_option_name");
      String menu_id = getRequest().getParameter("menu_id");


      if (!name.equals("")) {
        SidemenuOption option = new SidemenuOption();
          option.setParentId(Integer.parseInt(option_id));
          option.setName(name);
        option.insert();

        String ar_object_id = getRequest().getParameter("ar_object_id");
        if (ar_object_id != null) {
          IBObject object = new IBObject(Integer.parseInt(ar_object_id));
          String class_name = object.getClassName();
          String entity_name = "";
          int entity_id = -1;

          IBEntity[] entity = (IBEntity[])object.findReverseRelated(new IBEntity());
          if (entity != null) {
              if (entity.length > 0) {
                  GenericEntity gen_entity = (GenericEntity)Class.forName(entity[0].getClassName()).newInstance();
                  if (gen_entity != null) {
                    try {
                      gen_entity.insert();

                      entity_name = gen_entity.getIDColumnName();
                      entity_id = gen_entity.getID();

//                      add(entity_name + " " +entity_id);
                      IBEntity entity_temp = new IBEntity();
                      entity[0].addTo(option);

                    }
                     catch (Exception e){
                        e.printStackTrace(iwc.getResponse().getWriter());
                     }

                  }
              }
          }

          option.setPresentationObjectClassName(class_name);
          option.setEntityIdColumnName(entity_name);
          option.setEntityIdColumnValue(entity_id);
          option.update();

        }

        if (option_id.equals("0")) {
          if (menu_id != null) {
            SidemenuModule menu = new SidemenuModule(Integer.parseInt(menu_id));
            option.addTo(menu);
          }
        }

        editMenu(Integer.parseInt(menu_id));
      }
      else {
        add("Þú verður að skrifa inn nafn<br>");
        add(new BackButton("Til baka"));
      }


    }

    private void updateOption() throws SQLException{
      String option_id = getRequest().getParameter("sidemenu_option_id");
      String name = getRequest().getParameter("sidemenu_option_name");
      String menu_id = getRequest().getParameter("menu_id");


      if (!name.equals("")) {
        SidemenuOption option = new SidemenuOption(Integer.parseInt(option_id));
          option.setName(name);
        option.update();

/*        if (option_id.equals("0")) {
          if (menu_id != null) {
            SidemenuModule menu = new SidemenuModule(Integer.parseInt(menu_id));
            option.addTo(menu);
          }
        }

        TextModule text = new TextModule();
          idegaTimestamp stamp = new idegaTimestamp();
          text.setTextDate(stamp.getTimestampRightNow());
          text.setImageId(-1);
          text.setIncludeImage("N");
        text.insert();

        text.addTo(option);
*/

        editMenu(Integer.parseInt(menu_id));
      }
      else {
        add("Þú verður að skrifa inn nafn<br>");
        add(new BackButton("Til baka"));
      }


    }


    private void deleteOption(IWContext iwc, int option_id) throws SQLException {


        Vector items = new Vector();
        Integer[] intArr = new Integer[2];

        findNodes(items,option_id,1,new SidemenuOption());


        if (items.size() > 0) {
            for (int j = (items.size()) ; j > 0 ; j--) {
              intArr = (Integer[])items.elementAt(j-1);
              if (intArr[0].intValue() != 0) {
                deleteOptions(iwc, intArr[0].intValue());
              }
              else {
              }
            }
          }


    }





    private void deleteOptions(IWContext iwc,int option_id) throws SQLException {
      String menu_id = getRequest().getParameter("menu_id");
        SidemenuOption option = new SidemenuOption(option_id);

        if (menu_id != null) {
            option.removeFrom(new SidemenuModule(Integer.parseInt(menu_id)));

        }

        IBEntity[] entity = (IBEntity[]) option.findReverseRelated(new IBEntity());
        if (entity != null) {
          if (entity.length > 0 ) {
            for (int i = 0 ; i < entity.length ; i++) {
              entity[i].removeFrom(option);
            }
          }
        }

/*
        TextModule[] text = (TextModule[]) option.findReverseRelated(new TextModule());
        if (text != null) {
          if (text.length > 0) {
            for (int i = 0 ; i < text.length ; i++) {
              text[i].removeFrom(option);
              iwc.getServletContext().removeAttribute("sidemenu_table"+attribute_id+""+text[i].getID());

            }
          }
        }
*/
        option.delete();
    }





    private void findNodes(Vector vector,int id, int position,GenericEntity entity, GenericEntity[] options) throws SQLException{
        Integer[] intArray = new Integer[2];
          intArray[0] = new Integer(id);
          intArray[1] = new Integer(position);

        vector.addElement(intArray);

       options = (GenericEntity[]) (entity).findAllByColumn("parent_id",""+id);
      int i = 0;

        if (options != null ) {
          if (options.length > 0) {
            ++position;
            for (i = 0 ; i < options.length ; i++) {
              findNodes(vector,options[i].getID(), position,entity,options);
            }
          }
        }
    }

    private void findNodes(Vector vector,int id, int position,GenericEntity entity) throws SQLException{
        findNodes(vector,id,position,entity,new GenericEntity[1]);
    }

}
