package is.idega.idegaweb.golf.member;



import is.idega.idegaweb.golf.member.Service;

import com.idega.presentation.*;

import com.idega.presentation.ui.*;

import com.idega.presentation.text.*;

import is.idega.idegaweb.golf.entity.*;

import is.idega.idegaweb.golf.service.*;

import com.idega.jmodule.image.presentation.ImageInserter;

import java.sql.SQLException;

import java.util.List;

import java.util.ListIterator;



public class UnionMemberInfoInput extends Input{



  private UnionMemberInfo eUMI = null;

  private static String prmNumber = "umii.number" ,prmHnumber = "umiih.number";

  private static String prmStatus = "umii.status" ,prmHstatus = "umiih.status";

  private static String prmMbShip = "umii.mbship" ,prmHmbship = "umiih.mbship";

  private static String prmPayments = "umii.paydate",prmHpayments = "umiih.paydate";

  private static String prmPayDate = "umii.paydate",prmHpayDate = "umiih.paydate";

  private static String prmVisible = "umii.visible",prmHvisible = "umiih.visible";

  private static String prmLocker = "umii.locker", prmHlocker = "umiih.locker";

  private static String prmFamstat = "umii.famstat", prmHfamstat = "umiih.famstat";



  public PresentationObject getUnionMemberInfoTable(GolfMemberProfile profile) {

    UnionMemberInfo umi = null;

    boolean m = false;

    if(profile.bHasUMI){

      umi = profile.getUnionMemberInfo();

      m = true;

    }

    Table table = new Table(2, 8);



    TextInput tiMemberNumber = new TextInput(prmNumber,m? String.valueOf(umi.getMemberNumber()):"");

    HiddenInput hiMemberNumber  = new HiddenInput(prmHnumber,m? String.valueOf(umi.getMemberNumber()):"");



    DropdownMenu tiStatus = statusDropdown(prmStatus, m? umi.getMemberStatus():"");

    HiddenInput hiStatus = new HiddenInput(prmHstatus, m?  umi.getMemberStatus():"");



    DropdownMenu tiMbship = mbshiptypeDropdown(prmMbShip, m? umi.getMembershipType():"");

    HiddenInput hiMbship = new HiddenInput(prmHmbship, m? umi.getMembershipType():"");



    DropdownMenu tiPayments = numberOfPaymentDrop(prmPayments, m? String.valueOf(umi.getPreferredInstallmentNr()):"");

    HiddenInput hiPayments = new HiddenInput(prmHpayments, m? String.valueOf(umi.getPreferredInstallmentNr()):"");



    //DateInput di

/*

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

*/

    return table;

  }



  public void AddressUpdate(IWContext iwc,GolfMemberProfile profile){

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

    String street1 = iwc.getParameter("street1").trim();

    String hstreet1 = iwc.getParameter("hstreet1").trim();

    boolean bstreet1 = street1.equalsIgnoreCase(hstreet1)?false:true;

    String zip1 = iwc.getParameter("zip1");

    String hzip1 = iwc.getParameter("hzip1");

    boolean bzip1 = zip1.equalsIgnoreCase(hzip1)?false:true;

    String country1 = iwc.getParameter("country1");

    String hcountry1 = iwc.getParameter("hcountry1");

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

        eAddress1 = ((is.idega.idegaweb.golf.entity.AddressHome)com.idega.data.IDOLookup.getHomeLegacy(Address.class)).createLegacy();

        eAddress1.setStreet(street1);

        eAddress1.setZipcodeId(Integer.parseInt(zip1));

        eAddress1.setCountryId(Integer.parseInt(country1));

      }

      profile.bEditAddresses = true;

    }



    String street2 = iwc.getParameter("street2").trim();

    String hstreet2 = iwc.getParameter("hstreet2").trim();

    boolean bstreet2 = street1.equalsIgnoreCase(hstreet1)?false:true;

    String zip2 = iwc.getParameter("zip2");

    String hzip2 = iwc.getParameter("hzip2");

    boolean bzip2 = zip1.equalsIgnoreCase(hzip1)?false:true;

    String country2 = iwc.getParameter("country2");

    String hcountry2 = iwc.getParameter("hcountry2");

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

        eAddress2 = ((is.idega.idegaweb.golf.entity.AddressHome)com.idega.data.IDOLookup.getHomeLegacy(Address.class)).createLegacy();

        eAddress2.setStreet(street2);

        eAddress2.setZipcodeId(Integer.parseInt(zip2));

        eAddress2.setCountryId(Integer.parseInt(country2));

      }

      profile.bEditAddresses = true;

    }

    if(profile.bHasAddresses){



    }

  }



  public static DropdownMenu paymentTypeDrop(String name, String selected)throws java.sql.SQLException {

    DropdownMenu drp = new DropdownMenu(name);

    PaymentType type = ((is.idega.idegaweb.golf.entity.PaymentTypeHome)com.idega.data.IDOLookup.getHomeLegacy(PaymentType.class)).createLegacy();

    PaymentType[] types = (PaymentType[]) type.findAll();



    for( int i=0;i<types.length;i++){

      String sname = types[i].getName();



      if(sname.length() > 5)

        sname = sname.substring(0,5);

      drp.addMenuElement(types[i].getID(),sname);

    }

    drp.setSelectedElement(selected);

    return drp;

  }



  public static DropdownMenu catalogueDrop(String name, String selected,int iUnionId)throws java.sql.SQLException {

    if(selected.equals("-1"))

        selected = "0";

    DropdownMenu drp = new DropdownMenu( name);

    drp.addMenuElement(0, "-----");

    List list = Tariffer.getExtraCatalogList(String.valueOf(iUnionId));

    if(list != null) {

      ListIterator iter = list.listIterator();

      PriceCatalogue cate = null;

      String sname;

      while(iter.hasNext()) {

        cate = (PriceCatalogue) iter.next();

        sname = cate.getName();

        if(sname.length() > 15)

          sname = sname.substring(0,15)+"..";

        drp.addMenuElement(cate.getID(), sname);

      }

      drp.setSelectedElement(selected);

    }

    return drp;

  }



  public static DropdownMenu numberOfPaymentDrop(String name, String selected){



    DropdownMenu drp = new DropdownMenu( name);

    drp.addMenuElement(0, "--");



    for(int i = 1; i < 13; i++) {

        drp.addMenuElement(i, String.valueOf(i));

    }

    drp.setSelectedElement(selected);

    return drp;

  }



  public static DropdownMenu mbshiptypeDropdown(String name,String selected){

    DropdownMenu drp = new DropdownMenu(name);

    drp.addMenuElement("main","Aðalkl.");

    drp.addMenuElement("sub","Aukakl.");

    drp.setSelectedElement(selected);

    return drp;

  }





  public static DropdownMenu statusDropdown(String name, String selected) {

    DropdownMenu drp = new DropdownMenu(name);

    drp.addMenuElement("A", "Virkur");

    drp.addMenuElement("I", "Óvirkur");

    drp.addMenuElement("W", "Í bið");

    drp.addMenuElement("Q", "Hættur");

    drp.addMenuElement("D", "Látinn");

    drp.setSelectedElement(selected);

    return drp;

  }



  public static DropdownMenu familyStatusDropdown(String name, String selected) {

    DropdownMenu drp = new DropdownMenu(name);

    drp.addMenuElement("head", "Höfuð");

    drp.addMenuElement("partner", "Maki");

    drp.addMenuElement("child", "Barn");

    drp.setSelectedElement(selected);

    return drp;

  }



  public static DropdownMenu visibleDropdown(String name, String selected) {

    DropdownMenu drp = new DropdownMenu(name);

    drp.addMenuElement("Y", "Sjást á vef");

    drp.addMenuElement("N", "Ekki sjást á vef");

    drp.setSelectedElement(selected);

    return drp;

  }



}
