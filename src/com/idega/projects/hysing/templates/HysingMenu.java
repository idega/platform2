//idega 2001 - Tryggvi Larusson
/*
*Copyright 2001 idega.is All Rights Reserved.
*/
package com.idega.projects.hysing.templates;


import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.poll.moduleobject.*;
import com.idega.idegaweb.template.*;
import java.util.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.0
*/
        public class HysingMenu extends ClickableMenu{


              private static Image menuImageBlue = new Image("/pics/menubar/menubar2blatt.gif");
              private static Image menuImageBrown = new Image("/pics/menubar/menubar2brunt.gif");

              private static Image menuImagePlain = new Image("/pics/menubar/blattTakkabil.gif");

              private static Image main_i = new Image("Main page","/pics/menubar/Takkar/forsida1.gif","/pics/menubar/Takkar/forsida2.gif");
              private static Image main_s = new Image("/pics/menubar/Takkar/forsida_valid.gif","Main page");

              private static Image about_i = new Image("About","/pics/menubar/Takkar/umFyrir1.gif","/pics/menubar/Takkar/umFyrir2.gif");
              private static Image about_s = new Image("/pics/menubar/Takkar/umFyrir_valid.gif","About");

              private static Image goods_i = new Image("Goods & Services","/pics/menubar/Takkar/V&Th1.gif","/pics/menubar/Takkar/V&Th2.gif");
              private static Image goods_s = new Image("/pics/menubar/Takkar/V&Th_valid.gif","Goods & Services");

              private static Image staff_i = new Image("Staff","/pics/menubar/Takkar/starfsm1.gif","/pics/menubar/Takkar/starfsm2.gif");
              private static Image staff_s = new Image("/pics/menubar/Takkar/starfsm_valid.gif","Staff");

              private static Image associates_i = new Image("Associates","/pics/menubar/Takkar/samstarfs1.gif","/pics/menubar/Takkar/samstarfs2.gif");
              private static Image associates_s = new Image("/pics/menubar/Takkar/samstarfs_valid.gif","Associates");

              private static Link main = new Link(main_i,"/index.jsp");
              private static Link about = new Link(about_i,"/about.jsp");
              private static Link goods = new Link(goods_i,"/goods.jsp");
              private static Link staff = new Link(staff_i,"/staff.jsp");
              private static Link associates = new Link(associates_i,"/associates.jsp");

              private static Image menuItemBlueL = new Image("/pics/menubar/forsida_Vbil.gif");
              private static Image menuItemBlueR = new Image("/pics/menubar/forsida_Hbil.gif");
              private static Image menuItemBrownL = new Image("/pics/menubar/umFyrirtaekid_Vbil.gif");
              private static Image menuItemBrownR = new Image("/pics/menubar/umFyrirtaekid_Hbil.gif");

              public HysingMenu(){

                  super.addMenuItem("main",main,main_s);
                  super.addMenuItem("about",about,about_s);
                  super.addMenuItem("goods",goods,goods_s);
                  super.addMenuItem("staff",staff,staff_s);
                  super.addMenuItem("associates",associates,associates_s);

              }

            public ModuleObject getMenuItem(String menuItemName){
              /*Table returnTable = new Table(3,1);
              returnTable.setCellpadding(0);
              returnTable.setCellspacing(0);
              returnTable.setBorder(0);
              returnTable.add(left.getMenuItem(menuItemName),1,1);
              returnTable.add(super.getMenuItem(menuItemName),2,1);
              returnTable.add(right.getMenuItem(menuItemName),3,1);
              return returnTable;*/
              return super.getMenuItem(menuItemName);
            }


            public void setClickedMenuItem(String menuItemName){
                super.setClickedMenuItem(menuItemName);
            }


            public boolean isClicked(String itemName){
                String clickedItem = super.getClickedMenuItemName();
                if(clickedItem!=null){
                  if(clickedItem.equals(itemName)){
                    return true;
                  }
                }
                return false;
            }

            public ModuleObject getRightMenuItem(String itemName){

                  if(isClicked(itemName)){
                      if(itemName.equals("about")){
                          return menuItemBrownR;
                      }
                      else if(itemName.equals("staff")){
                          return menuItemBrownR;
                      }
                      else{
                        return menuItemBlueR;
                      }
                  }
                  else if(isNextItemClicked(itemName)){
                    String rightItem = getNextItemName(itemName);
                      if(rightItem.equals("about")){
                          return menuItemBrownL;
                      }
                      else if(rightItem.equals("staff")){
                          return menuItemBrownL;
                      }
                      else{
                        return menuItemBlueL;
                      }
                  }
                  else{
                      return menuImagePlain;
                  }
            }

            public ModuleObject getLeftMenuItem(String itemName){

                  if(isClicked(itemName)){
                      if(itemName.equals("about")){
                          return menuItemBrownL;
                      }
                      else if(itemName.equals("staff")){
                          return menuItemBrownL;
                      }
                      else{
                        return menuItemBlueL;
                      }
                  }
                  else if(isPreviousItemClicked(itemName)){
                    String leftItem = getPreviousItemName(itemName);
                      if(leftItem.equals("about")){
                          return menuItemBrownR;
                      }
                      else if(leftItem.equals("staff")){
                          return menuItemBrownR;
                      }
                      else{
                        return menuItemBlueR;
                      }
                  }
                  else{
                      return menuImagePlain;
                  }
            }

        }


