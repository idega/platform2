// idega 2000 - gimmi
package com.idega.jmodule.sidemenu.presentation;

import com.idega.jmodule.*;
import com.idega.data.*;
import java.io.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import javax.servlet.http.*;
import com.idega.jmodule.poll.data.*;
import java.sql.SQLException;
import com.idega.jmodule.text.data.*;
import com.idega.jmodule.sidemenu.data.*;
import java.util.*;
import com.idega.util.*;

public class Sidemenu extends JModuleObject{
    private String attribute_name = "idega_id";
    private int attribute_id = 1;
    private boolean forward_id = true;

    private String admin_page_url = "/sidemenu/sidemenu_admin.jsp";

    Vector colors = new Vector();
    private String alignment = "left";
    public String selected_item_color;
    int width = 0;
    int depth = 10;
    private SidemenuModule menu = null;
    private int fontSize = 2;
    private String parentStyle = "";
    private String childStyle = "";
    private boolean addBullet = false;
    private int cellPadding = 2;
    private Image submitImage;

    private Image bulletImage = null;
    private String extraParameterName;
    private String extraParameterValue;

    boolean isAdmin = false;
    boolean show_menu_name = false;

    public Sidemenu() {
      super();
      setDefaultColor();
    }


    private void setDefaultColor() {
      selected_item_color="#FFFFFF";

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

    public void setFontSize(int fontSize) {
      this.fontSize=fontSize;
    }

    public void setParentStyle(String parentStyle) {
      this.parentStyle=parentStyle;
    }

    public void setChildStyle(String childStyle) {
      this.childStyle=childStyle;
    }

    public void setAddBullet(boolean addBullet) {
      this.addBullet=addBullet;
    }

    public void setBulletImage(Image bulletImage) {
      this.bulletImage = bulletImage;
    }

    public void addParameter(String parameterName, String parameterValue) {
        this.extraParameterName = parameterName;
        this.extraParameterValue = parameterValue;
    }

    public void setSubmitImageURL(String submitImageURL){
      this.submitImage = new Image(submitImageURL);
    }

    public void setCellPadding(int cellPadding) {
      this.cellPadding=cellPadding;
    }

    public void setSelectedItemColor(String color) {
      this.selected_item_color = color;
    }

    public void setForwardId(boolean forward) {
      this.forward_id = forward;
    }

    public void setAlignment(String alignment) {
      this.alignment = alignment;
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

    //changed Tryggvi 28.03.2001
    //public void setAlignment(String alignment) {
    //  this.alignment = alignment;
    //}

    public void showMenuName(boolean show) {
      this.show_menu_name = show;
    }


    public void main(ModuleInfo modinfo) throws Exception{
        this.isAdmin=this.isAdministrator(modinfo);

        drawMenu(modinfo);
    }

    private void removeApplication(ModuleInfo modinfo) throws SQLException{

        Table table = (Table) modinfo.getServletContext().getAttribute("sidemenu_table"+attribute_id+"0");
        modinfo.removeSessionAttribute("side_menu_session"+attribute_id);

        String open = modinfo.getParameter("open");
          if (open == null) { open = "0";}


        if (table != null) {
            try {
              System.out.println("Table : sidemenu_table"+attribute_id+"0 removed from application");
              modinfo.getServletContext().removeAttribute("sidemenu_table"+attribute_id+"0");
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
                                modinfo.getServletContext().removeAttribute("sidemenu_table"+attribute_id+""+opt[o].getID());
                                System.out.println("Table : sidemenu_table"+attribute_id+""+opt[o].getID()+" removed from application");
                            }
                          }
                        }
                    }
                }
            }





        }


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


    private Table createMenu(ModuleInfo modinfo,Vector items, int menu_id)throws SQLException {
        String URI = modinfo.getRequestURI();
        String open = modinfo.getParameter("open");
        String text_ids = modinfo.getParameter("text_id");

        String entity_id_column_name = "";
        String entity_id_column_value = "";
/*
        String current_entity_id_column_name = getRequest().getParameter("return_type");
          if ( current_entity_id_column_name == null) { current_entity_id_column_name = "-1"};
        String current_entity_id_column_value = getRequest().getParameter("return_value");
          if ( current_entity_id_column_value == null) {current_entity_id_column_value  = "-1"};

        String items_pos = getRequest().getParameter("items_position");
          if (items_pos == null) {
            items_pos = "-1";
          }
*/
        SidemenuModule menu = new SidemenuModule(menu_id);
        SidemenuOption option;
        TextModule[] text;
        Link link;

        Integer[] intArray;

        boolean isOpen = false;
        String last_open ="0";
        int previous_head_id = 0;
        int position = 1;
        int colors_position = 0;
        int row = 1;
        int id = 0;
        int text_id;
        Text textinn = new Text("");
          if (open == null) { open = "0"; }
          if (text_ids == null) { text_ids = "0"; }

        Table table = (Table) modinfo.getServletContext().getAttribute("sidemenu_table"+attribute_id+""+open);

          if (table != null) {
//              add("tafla til");
          }
          else if (table == null) {
//            add("tafla EKKI til");

            table = new Table(depth , items.size()+1);

              table.setBorder(0);
              table.setAlignment(alignment);
              table.setCellpadding(cellPadding);
              table.setCellspacing(0);
              table.setWidth(width);
              if (show_menu_name) {
                table.add("<b>"+menu.getName()+"</b>",1,row);
                table.mergeCells(1,1,1,depth);
                ++row;
              }

              for (int i = 0 ; i < items.size() ; i++ ) {
                 intArray = (Integer[])items.elementAt(i);
                 position = intArray[1].intValue();
                 id = intArray[0].intValue();

                 if (position <= depth) {
                   if ( position < colors.size()) {
                     colors_position = position-1;
                   }
                   else {
                     colors_position = (colors.size()-1);
                   }

                   option = new SidemenuOption(id);
                   textinn = new Text(option.getName());

                   if (position != 1) {
                      if ( addBullet ) {
                        textinn = new Text("- "+option.getName());
                      }
                      textinn.setAttribute("style",childStyle);
                      textinn.setFontSize(fontSize-1);
                  }
                  else {
                      textinn.setAttribute("style",parentStyle);
                      textinn.setFontSize(fontSize);
                  }

                  link = new Link(textinn,URI);
                    link.setAttribute("style","text-decoration: none");
                    if (this.extraParameterName != null) {
                      link.addParameter(this.extraParameterName ,this.extraParameterValue);
                    }
                  if (this.forward_id) {
                    link.addParameter("sidemenu_id",this.menu.getID());
                  }

                  entity_id_column_name = option.getEntityIdColumnName();
                  entity_id_column_value = ""+option.getEntityIdColumnValue();


                  link.addParameter(entity_id_column_name,entity_id_column_value);
                  link.addParameter("module_object",option.getModuleObjectClassName());
//                  link.addParameter("items_position",i);
//                  link.addParameter("return_type",entity_id_column_name);
//                  link.addParameter("return_value",entity_id_column_value);

                  if ((position == 1) && (!open.equals(option.getID()+""))) {
                    link.addParameter("open",option.getID()+"");
                    isOpen = false;
                  }
                  else if ((position == 1) && (open.equals(option.getID()+""))){
                    last_open = option.getID()+"";
                    isOpen = true;
                  }


                  if (this.bulletImage != null) {
                      table.add(bulletImage,position,row);
                  }

                  if (position == 1) {
                    table.add(link,position,row);
                    table.mergeCells(position,row,depth,row);
                    table.setRowColor(row,colors.elementAt(colors_position).toString());
                    ++row;
                  }
                  else {
                    if (isOpen) {
                      link.addParameter("open",last_open);
                      table.add(link,position,row);
                      table.mergeCells(position,row,depth,row);
                      table.setWidth(position,row,"200");
/*                      if (items_pos.equals(Integer.toString(i))) {
                        table.setRowColor(row,selected_item_color);
                      }
                      else {
  */                      table.setRowColor(row,colors.elementAt(colors_position).toString());
   //                   }
                      ++row;
                      }
                  }
/*                  if (!items_pos.equals("-1"))
                  table.setRowColor(Integer.parseInt(items_pos)+1,this.selected_item_color);
*/

                }

              }

            table.resize(depth,row-1);
            modinfo.getServletContext().setAttribute("sidemenu_table"+attribute_id+""+open,table);


/*
            if (text_ids != null) {
              modinfo.getServletContext().setAttribute("sidemenu_table"+attribute_id+""+open,table);
            }
            else {
              modinfo.getServletContext().setAttribute("sidemenu_table"+attribute_id+"0",table);
            }
*/
          }

          return table;
    }


    private void drawMenu(ModuleInfo modinfo) throws SQLException {

      if (isAdmin) {
        Window gluggi = new Window("Sidemenustjórinn",700,500,admin_page_url);
        Form form = new Form(gluggi);
          form.add(new HiddenInput("side_menu_action","admin"));
          form.add(new HiddenInput("attribute_name",attribute_name));
          form.add(new HiddenInput("attribute_id",""+attribute_id));
          if ( submitImage != null ) {
            form.add(new SubmitButton(submitImage));
          }
          else {
            form.add(new SubmitButton("Sidemenustjórinn"));
          }

        add(form);
      }


      SidemenuAttributes[] attributes = (SidemenuAttributes[])new SidemenuAttributes().findAllByColumn("attribute_name",attribute_name,"attribute_id",""+attribute_id);
      Vector items = new Vector();


      Table table = new Table();
        //changed Tryggvi 28.03.2001
        //table.setBorder(1);
        table.setBorder(0);

      if (attributes != null) {
        if (attributes.length > 0) {

           this.menu = new SidemenuModule(attributes[0].getSidemenuId());
            if (menu != null) {
              SidemenuOption[] options = (SidemenuOption[]) menu.findReverseRelated(new SidemenuOption());
              if (options != null) {
                if (options.length > 0) {
                  for (int j = 0 ; j < options.length ; j++) {
                    Vector session_vector = (Vector) modinfo.getSessionAttribute("side_menu_session"+attribute_id);

                    if (session_vector!= null) {
                      items = session_vector;
                    }
                    else {
                      findNodes(items,options[j].getID(),1,new SidemenuOption());
                    }
                  }
                  modinfo.setSessionAttribute("side_menu_session"+attribute_id,items);
                }
              }

            }

            if (items.size() > 0 ) {
              table = createMenu(modinfo,items, menu.getID());
            }


        }
        else {
          //changed Tryggvi 28.03.2001
          //add("Ekkert menu til");
          add("...");
        }
      }
      else {
        //changed Tryggvi 28.03.2001
        //add("Ekkert menu til");
        add("...");
      }








      add(table);
    }



}
