package is.idega.idegaweb.golf.presentation;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;

import com.idega.idegaweb.*;

import is.idega.idegaweb.golf.business.UnionCreator;
import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.Country;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class UnionCreatorForm extends Block {

  private static final String PARAMETER_NAME="unioncreator_name";
  private static final String PARAMETER_TYPE="unioncreator_type";
  private static final String PARAMETER_ABBR="unioncreator_abbr";
  private static final String PARAMETER_COUNTRY="unioncreator_country";
  private static final String PARAMETER_LOGIN="unioncreator_login";
  private static final String PARAMETER_PASSWD="unioncreator_passwd";
  private static final String PARAMETER_PARENT="unioncreator_parent";
  private static final String PARAMETER_CITY="unioncreator_city";
  private static final String PARAMETER_ZIPCODE="unioncreator_zipcode";

  private static final String PARAMETER_ACTION = "union_creator_action";

  private static final String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";

  public UnionCreatorForm() {
  }


  public void main(IWContext iwc)throws Exception{
    IWBundle iwb = this.getBundle(iwc);
    IWResourceBundle iwrb = iwb.getResourceBundle(iwc);
    if(iwc.isParameterSet(PARAMETER_ACTION)){
      processForm(iwc);
    }
    Form form = new Form();
    add(form);
    form.addParameter(PARAMETER_ACTION,"default");
    Table table = new Table(2,11);
    form.add(table);


    String headerText = iwrb.getLocalizedString("unioncreator.header","Create Union");
    Text header = new Text(headerText);
    table.add(header,1,1);

    String nameString = iwrb.getLocalizedString("unioncreator.name","Name");
    Text nameText = new Text(nameString);
    table.add(nameText,1,2);
    TextInput nameInput = new TextInput(PARAMETER_NAME);
    table.add(nameInput,2,2);


    String typeString = iwrb.getLocalizedString("unioncreator.type","Type");
    Text typeText = new Text(typeString);
    table.add(typeText,1,3);
    String golfClubString = iwrb.getLocalizedString("unioncreator.golfclub","Golf Club");
    String unionString = iwrb.getLocalizedString("unioncreator.union","Golf Union");
    DropdownMenu types = new DropdownMenu(PARAMETER_TYPE);
    types.addMenuElement("golf_club",golfClubString);
    types.addMenuElement("golf_union",unionString);
    table.add(types,2,3);


    String abbrString = iwrb.getLocalizedString("unioncreator.abbr","Abbreviation");
    Text abbrText = new Text(abbrString);
    table.add(abbrText,1,4);
    TextInput abbrInput = new TextInput(PARAMETER_ABBR);
    abbrInput.setMaxlength(3);
    table.add(abbrInput,2,4);

    String loginString = iwrb.getLocalizedString("unioncreator.login","ClubAdmin login");
    Text loginText = new Text(loginString);
    table.add(loginText,1,5);
    TextInput loginInput = new TextInput(PARAMETER_LOGIN);
    loginInput.setMaxlength(20);
    table.add(loginInput,2,5);

    String passwdString = iwrb.getLocalizedString("ClubAdmin password","unioncreator.passwd");
    Text passwdText = new Text(passwdString);
    table.add(passwdText,1,6);
    TextInput passwdInput = new TextInput(PARAMETER_PASSWD);
    passwdInput.setMaxlength(20);
    table.add(passwdInput,2,6);

    String parentString = iwrb.getLocalizedString("unioncreator.parentunion","Create under");
    String noneString = iwrb.getLocalizedString("unioncreator.none","None");
    Text parentText = new Text(parentString);
    table.add(parentText,1,7);
    DropdownMenu parents = new DropdownMenu(PARAMETER_PARENT);
    parents.addMenuElement("",noneString);
    java.util.List unions = com.idega.data.EntityFinder.findAllByColumn(((is.idega.idegaweb.golf.entity.UnionHome)com.idega.data.IDOLookup.getHomeLegacy(Union.class)).createLegacy(),"union_type","golf_union");
    parents.addMenuElements(unions);
    table.add(parents,2,7);

    String countryString = iwrb.getLocalizedString("unioncreator.country","Create in");
    Text countryText = new Text(countryString);
    table.add(countryText,1,8);
    DropdownMenu counts = new DropdownMenu(PARAMETER_COUNTRY);
    java.util.List countries = com.idega.data.EntityFinder.findAll(((is.idega.idegaweb.golf.entity.CountryHome)com.idega.data.IDOLookup.getHomeLegacy(Country.class)).createLegacy());
    counts.addMenuElements(countries);
    table.add(counts,2,8);

    String cityString = iwrb.getLocalizedString("unioncreator.city","City");
    Text cityText = new Text(cityString);
    table.add(cityText,1,9);
    TextInput cityInput = new TextInput(PARAMETER_CITY);
    cityInput.setMaxlength(30);
    table.add(cityInput,2,9);

    String zipCodeString = iwrb.getLocalizedString("unioncreator.zipcode","Zipcode");
    Text zipCodeText = new Text(zipCodeString);
    table.add(zipCodeText,1,10);
    TextInput zipCodeInput = new TextInput(PARAMETER_ZIPCODE);
    zipCodeInput.setMaxlength(20);
    table.add(zipCodeInput,2,10);

    String submitString = iwrb.getLocalizedString("unioncreator.submit","Submit");
    SubmitButton button = new SubmitButton(submitString);
    table.add(button,2,11);

  }

  private void processForm(IWContext iwc)throws Exception{
      String name = iwc.getParameter(PARAMETER_NAME);
      String type = iwc.getParameter(PARAMETER_TYPE);
      String abbreviation = iwc.getParameter(PARAMETER_ABBR);
      if(abbreviation.equals("")){
        abbreviation=name.substring(0,2).toUpperCase();
      }
      int CountryID = Integer.parseInt(iwc.getParameter(PARAMETER_COUNTRY));
      String loginName = iwc.getParameter(PARAMETER_LOGIN);
      String passwd = iwc.getParameter(PARAMETER_PASSWD);
      String city = iwc.getParameter(PARAMETER_CITY);
      String zipcode = iwc.getParameter(PARAMETER_ZIPCODE);

      int unionID=-1;
      String unionParentID = iwc.getParameter(PARAMETER_PARENT);
      if(unionParentID!=null){
        if(!unionParentID.equals("")){
          unionID = Integer.parseInt(unionParentID);
        }
      }
      Union parent = null;
      if(unionID!=-1){
        parent = ((is.idega.idegaweb.golf.entity.UnionHome)com.idega.data.IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKeyLegacy(unionID);
      }
      UnionCreator.createUnion(name,type,abbreviation,loginName,passwd,parent,CountryID,city,zipcode);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}
