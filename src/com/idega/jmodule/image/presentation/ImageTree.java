package com.idega.jmodule.image.presentation;

import java.sql.*;
import java.util.*;
import java.io.*;
import com.idega.util.*;
import com.idega.jmodule.object.textObject.*;
import	com.idega.jmodule.object.*;
import	com.idega.jmodule.object.interfaceobject.*;
import	com.idega.jmodule.image.data.*;
import	com.idega.data.*;
import com.idega.util.text.*;
import javax.servlet.http.HttpServletRequest;


public class ImageTree extends JModuleObject{

private int width=200;

public Table getTreeTable(ModuleInfo modinfo) throws SQLException {

    HttpServletRequest request = modinfo.getRequest();
    ImageCatagory[] catagory = (ImageCatagory[]) (new ImageCatagory()).findAll("Select * from image_catagory where parent_id = -1");
    ImageEntity[] images;
    //Debug
    Vector items = (Vector) modinfo.getServletContext().getAttribute("image_tree_vector");
    //Vector items = null;
    Integer[] intArr = new Integer[3];
    int pos;

    Table returnTable = new Table();

    if (items == null) {
      items = new Vector();
      if ( catagory != null) {
        if (catagory.length > 0) {
          for (int i = 0 ; i < catagory.length ; i++ ) {
            findNodes(items,catagory[i].getID(),1,new ImageCatagory(),1);
            images = (ImageEntity[])catagory[i].findRelated(new ImageEntity());

            if (images != null) {
              if (images.length > 0 ) {
                  intArr = (Integer[])(items.lastElement());
                  pos = intArr[1].intValue()+1;
                for (int j = 0 ; j < images.length ; j++) {
                  if (images[j].getParentId()== -1 ) {
                    findNodes(items,images[j].getID(),pos,new ImageEntity(),2);
                  }
                }
              }
            }

          modinfo.getServletContext().setAttribute("image_tree_vector",items);
          }
        }
      }
    }


    if (items.size() > 0) {
      String open_cat = request.getParameter("open_catagory_id");
/*      Link remove = new Link("remove application");
        remove.setFontColor("black");
        remove.addParameter("image_tree_action","remove_applications");
        add(remove);
*/
      if (open_cat == null) { open_cat = "-3";}
        Table isTable = (Table) modinfo.getServletContext().getAttribute("image_tree_table"+open_cat);
        //debug
        //Table isTable = null;
       if (isTable != null) {
          returnTable = isTable;
        }
        else {
          returnTable = writeTable(items,modinfo);
        }

    }

    return returnTable;
}

public int getWidth(){
  return this.width;
}

public void setWidth(int width){
  this.width =  width;
}
public Table writeTable(Vector items,ModuleInfo modinfo) throws SQLException {
HttpServletRequest request = modinfo.getRequest();
  Table table = new Table();
    table.setBorder(0 );
    table.setWidth(getWidth());
    table.setCellpadding(2);
    table.setCellspacing(0);
    table.setAlignment("left");

  com.idega.jmodule.object.Image more = new com.idega.jmodule.object.Image("/pics/more.gif");
  String URI = request.getRequestURI();
  String image_id = request.getParameter("image_id");
  String open_cat = request.getParameter("open_catagory_id");
    if (open_cat == null) { open_cat = "-3";}
  String open_img = request.getParameter("open_image_id");
    if (open_img == null) { open_img = "-3";}

  Link openLink;
  Link idLink;
  Table smallTable;
  String color_1 = "#f29b00";
  String color_2 = "#f0c0ff";
  int depth = 10;

  Text text;

  ImageCatagory catagory;
  ImageEntity image;
  Integer[] intArr = new Integer[3];
  int pos = 1;
  int id;
  int spe;
  int row = 0;
  int pre_cat_id = -1;

  for (int i = 0 ; i < items.size() ; i++) {
      intArr = (Integer[]) items.elementAt(i);
      id = intArr[0].intValue();
      pos= intArr[1].intValue();
      spe= intArr[2].intValue();
      if (spe == 1) {
        ++row;
        catagory = new ImageCatagory(id);
        pre_cat_id = id;

        table.mergeCells(1,row,depth,row);

        text = new Text(catagory.getImageCatagoryName());

        openLink = new Link(more,URI);
        idLink = new Link(text,URI);
        idLink.setFontColor("#000000");


        if (!open_cat.equals(Integer.toString(id))) {
          openLink.addParameter("open_catagory_id",""+id);
        }
        else {
          idLink.addParameter("open_catagory_id",""+id);
        }
        table.add(openLink,pos,row);

          idLink.addParameter("catagory_id",""+id);
        table.add(idLink,pos,row);
//        table.setColor(pos,row,color_2);
      }

      if (open_cat.equals(Integer.toString(pre_cat_id)))
      if (spe == 2) {
        ++row;
        image = new ImageEntity(id);

        String extrainfo = "";
        if ( ( image.getWidth()!=null)&& ( image.getHeight()!=null) ) extrainfo = " ("+image.getWidth()+"*"+image.getHeight()+")";

        text = new Text(image.getName()+extrainfo);
          text.setFontSize(1);

//        openLink = new Link(more,URI);
        idLink = new Link(text,URI);
        idLink.setFontColor("#f29b00");
        if (pre_cat_id != -1 ) {
//          openLink.addParameter("open_catagory_id",""+pre_cat_id);
          idLink.addParameter("open_catagory_id",""+pre_cat_id);
        }
//        openLink.addParameter("open_image_id",""+id);
        table.mergeCells(pos,row,depth,row);
//        table.setColor(pos,row,color_1);

  //      table.add(openLink,pos,row);

          idLink.addParameter("image_id",""+id);

        table.add(idLink, pos,row);
      }


  }

  modinfo.getServletContext().setAttribute("image_tree_table"+open_cat,table);

  return table;
//  add(table);
}



    private void findNodes(Vector vector,int id, int position,GenericEntity entity, GenericEntity[] options, int special_value) throws SQLException{
        Integer[] intArray = new Integer[3];
          intArray[0] = new Integer(id);
          intArray[1] = new Integer(position);
          intArray[2] = new Integer(special_value);

        vector.addElement(intArray);

       options = (GenericEntity[]) (entity).findAllByColumn("parent_id",""+id);
        int i = 0;

        if (options != null ) {
          if (options.length > 0) {
            ++position;
            for (i = 0 ; i < options.length ; i++) {
              findNodes(vector,options[i].getID(), position,entity,options, special_value);
            }
          }
        }


    }

    private void findNodes(Vector vector,int id, int position,GenericEntity entity) throws SQLException{
        findNodes(vector,id,position,entity,new GenericEntity[1],0);
    }

    private void findNodes(Vector vector,int id, int position,GenericEntity entity, int special_value) throws SQLException{
        findNodes(vector,id,position,entity,new GenericEntity[1], special_value);
    }


private void removeApplications(ModuleInfo modinfo) throws SQLException{
    HttpServletRequest request = modinfo.getRequest();

    Table table;
    Vector vector;
    String test;

    table = (Table) modinfo.getServletContext().getAttribute("image_tree_table-3");
    vector = (Vector) modinfo.getServletContext().getAttribute("image_tree_vector");
    if (table != null) {
      modinfo.getServletContext().removeAttribute("image_tree_table-3");
    }
    if (vector != null) {
      modinfo.getServletContext().removeAttribute("image_tree_vector");
    }

    test = (String) request.getSession().getAttribute("image_tree_catagory_id");
    if (test != null) {
      request.getSession().removeAttribute("image_tree_catagory_id");
    }
    test = (String) request.getSession().getAttribute("image_tree_image_id");
    if (test != null) {
      request.getSession().removeAttribute("image_tree_image_id");
    }


        ImageCatagory[] catagories = (ImageCatagory[])(new ImageCatagory()).findAll();

        if (catagories != null) {
            if (catagories.length > 0 ) {
                for (int i = 0 ; i < catagories.length ; i++ ) {
                    table = (Table) modinfo.getServletContext().getAttribute("image_tree_table"+catagories[i].getID());
                    if (table != null) {
                        modinfo.getServletContext().removeAttribute("image_tree_table"+catagories[i].getID());
                    }
                }
            }
        }
    }


public void deleteModule(ModuleInfo modinfo) throws Throwable{
    removeApplications(modinfo);

}

public void main(ModuleInfo modinfo)throws Exception{

  //this.isAdmin=this.isAdministrator(modinfo);

  //setSpokenLanguage(modinfo);
  HttpServletRequest request = modinfo.getRequest();

  String temp_image_id = request.getParameter("image_id");
  String temp_catagory_id = request.getParameter("catagory_id");
  String image_id = null;
  String catagory_id = null;

  if (temp_image_id != null) {
     request.getSession().setAttribute("image_tree_image_id",temp_image_id);
     request.getSession().removeAttribute("image_tree_catagory_id");
  }
  if (temp_catagory_id != null) {
     request.getSession().setAttribute("image_tree_catagory_id",temp_catagory_id);
     request.getSession().removeAttribute("image_tree_image_id");
  }
     image_id = (String) request.getSession().getAttribute("image_tree_image_id");
     catagory_id = (String) request.getSession().getAttribute("image_tree_catagory_id");

  add(getTreeTable(modinfo));

  }

}
