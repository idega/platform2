package is.idega.idegaweb.golf.member;

import is.idega.idegaweb.golf.entity.Country;
import is.idega.idegaweb.golf.entity.ZipCode;

import java.sql.SQLException;

import com.idega.data.IDOLookup;
import com.idega.presentation.ui.DropdownMenu;

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
    Country country = (Country) IDOLookup.instanciateEntity(Country.class);
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
    ZipCode zip = (ZipCode) IDOLookup.instanciateEntity(ZipCode.class);
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