package com.idega.projects.golf.member;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.projects.golf.entity.*;
import com.idega.projects.golf.service.*;
import com.idega.jmodule.image.presentation.ImageInserter;
import java.sql.SQLException;

public class Service{

  public static String mbsShipMap(String type){
    if("main".equalsIgnoreCase(type))
      return "Aðalkl.";
    else if("sub".equalsIgnoreCase(type))
      return "Aukakl.";
    else return "";
  }

  public static DropdownMenu CountryDrp(String name, String selected) {
    DropdownMenu drp = new DropdownMenu(name);
    Country country = new Country();
    try {
      Country[] countryArr = (Country[]) country.findAll();
      for(int i = 0; i < countryArr.length; i++) {
          drp.addMenuElement(countryArr[i].getID(), countryArr[i].getName());
      }
      drp.setSelectedElement(selected);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    return drp;
  }

  public static DropdownMenu ZipDrp(String name, String selected) {
    DropdownMenu drp = new DropdownMenu(name);
    ZipCode zip = new ZipCode();
    try {
        ZipCode [] zipArr = (ZipCode[]) zip.findAllOrdered("code");
        for(int i = 0; i < zipArr.length; i++) {
            drp.addMenuElement(zipArr[i].getID(), zipArr[i].getCode()+" "+zipArr[i].getCity());
        }
        drp.setSelectedElement(selected);
    }
    catch(SQLException e) {
        e.printStackTrace();
    }
    return drp;
  }



}