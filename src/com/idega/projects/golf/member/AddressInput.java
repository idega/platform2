package com.idega.projects.golf.member;

import com.idega.projects.golf.member.Service;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.projects.golf.entity.*;
import com.idega.projects.golf.service.*;
import com.idega.jmodule.image.presentation.ImageInserter;
import java.sql.SQLException;

public class AddressInput extends Input{

  public static ModuleObject getAddressTable(GolfMemberProfile profile) {
    Address eAddress1 = null,eAddress2 = null;
    boolean a1 = false,a2 = false;
    if(profile.bHasAddresses){
    int len = profile.getAddresses().length;
      if(len > 0 ){
        eAddress1 = profile.getAddresses()[0];
        a1 = true;
      }
      if(len > 1 ){
        eAddress2 = profile.getAddresses()[1];
        a2 = true;
      }
    }
    Table table = new Table(2, 6);

    TextInput Street1 = new TextInput("street1",a1? eAddress1.getStreet():"");
    HiddenInput HiddenStreet1  = new HiddenInput("hstreet1",a1? eAddress1.getStreet():"");
    TextInput Street2 = new TextInput("street2",a2? eAddress2.getStreet():"");
    HiddenInput HiddenStreet2 = new HiddenInput("hstreet2",a2? eAddress2.getStreet():"");

    DropdownMenu Zip1 = ZipDrp("zip1", a1? String.valueOf(eAddress1.getZipcodeId()):"");
    HiddenInput HiddenZip1 = new HiddenInput("hzip1", a1? String.valueOf(eAddress1.getZipcodeId()):"");
    DropdownMenu Zip2 = ZipDrp("zip2",  a2? String.valueOf(eAddress2.getZipcodeId()):"");
    HiddenInput HiddenZip2 = new HiddenInput("hzip1", a2? String.valueOf(eAddress2.getZipcodeId()):"");

    DropdownMenu Country1 = CountryDrp("country1", a1? String.valueOf(eAddress1.getCountryId()):"");
    HiddenInput HiddenCountry1 = new HiddenInput("hcountry1", a1? String.valueOf(eAddress1.getCountryId()):"");
    DropdownMenu Country2 = CountryDrp("country1", a2? String.valueOf(eAddress2.getCountryId()):"");
    HiddenInput HiddenCountry2 = new HiddenInput("hcountry1", a2? String.valueOf(eAddress2.getCountryId()):"");

    setStyle(Street1);
    setStyle(Zip1);
    setStyle(Country1);
    setStyle(Street2);
    setStyle(Zip2);
    setStyle(Country2);

    table.add(bodyText("Heimili"), 1, 1);
    table.add(Street1,2,1);
    table.add(HiddenStreet1,2,1);
    table.add(bodyText("Póstnr"), 1, 2);
    table.add(Zip1, 2, 2);
    table.add(HiddenZip1, 2, 2);
    table.add(bodyText("Land"), 1, 3);
    table.add(Country1, 2, 3);
    table.add(HiddenCountry1, 2, 3);

    table.add(bodyText("Heimili2"), 1, 4);
    table.add(Street2,2,4);
    table.add(HiddenStreet2,2,4);
    table.add(bodyText("Póstnr"), 1, 5);
    table.add(Zip2, 2, 5);
    table.add(HiddenZip2, 2, 5);
    table.add(bodyText("Land"), 1, 6);
    table.add(Country2, 2, 6);
    table.add(HiddenCountry2, 2, 6);

    return table;
  }

  public void AddressUpdate(ModuleInfo modinfo,GolfMemberProfile profile){
    Address eAddress1 = null,eAddress2 = null;
    boolean a1 = false,a2 = false;
    if(profile.bHasAddresses){
    int len = profile.getAddresses().length;
      if(len > 0 ){
        eAddress1 = profile.getAddresses()[0];
        a1 = true;
      }
      if(len > 1 ){
        eAddress2 = profile.getAddresses()[1];
        a2 = true;
      }
    }
    String street1 = modinfo.getParameter("street1").trim();
    String hstreet1 = modinfo.getParameter("hstreet1").trim();
    boolean bstreet1 = street1.equalsIgnoreCase(hstreet1)?false:true;
    String zip1 = modinfo.getParameter("zip1");
    String hzip1 = modinfo.getParameter("hzip1");
    boolean bzip1 = zip1.equalsIgnoreCase(hzip1)?false:true;
    String country1 = modinfo.getParameter("country1");
    String hcountry1 = modinfo.getParameter("hcountry1");
    boolean bcountry1 = country1.equalsIgnoreCase(hcountry1)?false:true;
    if(bstreet1 || bzip1 || bcountry1){
      if(a1){
        if(bstreet1)
          eAddress1.setStreet(street1);
        if(bzip1)
          eAddress1.setZipcodeId(Integer.parseInt(zip1));
        if(bcountry1)
          eAddress1.setCountryId(Integer.parseInt(country1));
      }
      else{
        eAddress1 = new Address();
        eAddress1.setStreet(street1);
        eAddress1.setZipcodeId(Integer.parseInt(zip1));
        eAddress1.setCountryId(Integer.parseInt(country1));
      }
      profile.bEditAddresses = true;
    }

    String street2 = modinfo.getParameter("street2").trim();
    String hstreet2 = modinfo.getParameter("hstreet2").trim();
    boolean bstreet2 = street1.equalsIgnoreCase(hstreet1)?false:true;
    String zip2 = modinfo.getParameter("zip2");
    String hzip2 = modinfo.getParameter("hzip2");
    boolean bzip2 = zip1.equalsIgnoreCase(hzip1)?false:true;
    String country2 = modinfo.getParameter("country2");
    String hcountry2 = modinfo.getParameter("hcountry2");
    boolean bcountry2 = country1.equalsIgnoreCase(hcountry1)?false:true;
    if(bstreet2 || bzip2 || bcountry2){
      if(a2){
        if(bstreet2)
          eAddress2.setStreet(street2);
        if(bzip2)
          eAddress2.setZipcodeId(Integer.parseInt(zip2));
        if(bcountry2)
          eAddress1.setCountryId(Integer.parseInt(country2));
      }
      else{
        eAddress2 = new Address();
        eAddress2.setStreet(street2);
        eAddress2.setZipcodeId(Integer.parseInt(zip2));
        eAddress2.setCountryId(Integer.parseInt(country2));
      }
      profile.bEditAddresses = true;
    }
    if(profile.bHasAddresses){

    }
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

  public  static DropdownMenu ZipDrp(String name, String selected) {
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