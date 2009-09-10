package com.idega.block.school.presentation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolYear;
import com.idega.block.school.data.SchoolYearPlaces;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.util.TextFormat;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolYearPlacesEditor extends Block {

  IWResourceBundle iwrb;
  IWBundle iwb;
  TextFormat tFormat;
  SchoolBusiness schBean;
  School school = null;
  int schoolId = -1;
  String prmSchoolId = "sch_school_id";
  public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.school";

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private void control(IWContext iwc) throws Exception{
    //debugParameters(iwc);
    initBeans(iwc);
    Form F = new Form();

    if(iwc.isParameterSet(this.prmSchoolId)) {
		this.schoolId = Integer.parseInt(iwc.getParameter(this.prmSchoolId));
	}

    if(this.schoolId > 0) {
		this.school = this.schBean.getSchool(new Integer(this.schoolId));
	}

    if(iwc.isParameterSet("sch_save_school_places")){
      savePlaces(iwc);
      F.add(getListTable(this.school,false));
    }
    else if(iwc.isParameterSet("sch_new_school_places")){
      F.add(getListTable(this.school,true));
    }
	else {
		F.add(getListTable(this.school,true));
	}

     add(F);

  }

  private void initBeans(IWContext iwc) throws java.rmi.RemoteException{
    this.schBean = (SchoolBusiness) IBOLookup.getServiceInstance(iwc,SchoolBusiness.class);
  }

/*
  private PresentationObject getInput(IWContext iwc ,int id)throws java.rmi.RemoteException{
    return getInputTable(iwc,schBean.getSchoolYearPlaces(new Integer(id)));
  }
*/
  
  private void savePlaces(IWContext iwc)throws java.rmi.RemoteException{
    if(iwc.isParameterSet("sch_save_school_places")){
      if(iwc.getParameter("sch_save_school_places").equals("true")){
        String count = iwc.getParameter("pl_count");
        int plcount = Integer.parseInt(count);
        for (int i = 0; i < plcount; i++) {
          int id = Integer.parseInt(iwc.getParameter("sch_year_places_id_"+i));
          int year = Integer.parseInt(iwc.getParameter("sch_year_"+i));
          String sPlaces = iwc.getParameter("sch_places_"+i);
          int places = 0;
          if(!"".equals(sPlaces)){
            places = Integer.parseInt(sPlaces);
            this.schBean.storeSchoolYearPlaces(id,this.schoolId,year,places);
          }
        }
      }
    }
  }

  public PresentationObject getListTable(School ent,boolean edit){
    Table T = new Table();
    int row = 1;
    Map schoolsYearPlaces = null;
    Collection schoolYears = null;

    DropdownMenu drpSchools = new DropdownMenu();

    try{
      if(ent!=null){
      Collection places = this.schBean.findAllSchoolYearPlaces(((Integer)ent.getPrimaryKey()).intValue());
      schoolsYearPlaces = getMapOfYears(places);
      //schoolYears schoolYears = schBean.getSchoolRelatedSchoolYears(ent);
      schoolYears = getSchoolYears(ent);
      System.err.println("places +"+schoolsYearPlaces.size());
      System.err.println("years +"+schoolsYearPlaces.size());
      }
	else {
		add("school is null");
	}
      drpSchools = new DropdownMenu(this.schBean.findAllSchools(),this.prmSchoolId);
      drpSchools.setSelectedElement(String.valueOf(this.schoolId));
      drpSchools.addMenuElementFirst("-1",this.iwrb.getLocalizedString("school","School"));
    }
    catch(java.rmi.RemoteException rex){
      rex.printStackTrace();
    }



    T.mergeCells(1,1,2,1);
    //Link newLink = new Link(iwrb.getLocalizedImageButton("edit","Edit"));
    //newLink.addParameter("sch_new_school_places","true");
    int col = 1;
    //T.add(newLink,1,row);
    row++;

    T.add(drpSchools,col++,row);
    drpSchools.setToSubmit();
    T.add(this.tFormat.format(this.iwrb.getLocalizedString("year","Year")),col++,row);
    T.add(this.tFormat.format(this.iwrb.getLocalizedString("places","Places")),col++,row);

    row++;

    if(schoolYears!=null && !schoolYears.isEmpty()){
    java.util.Iterator iter = schoolYears.iterator();
      SchoolYearPlaces schoolPlaces ;
      col = 2;
      Integer yearId;
      Integer placesId;
      SchoolYear year;
      int i = 0;
      while(iter.hasNext()){
        Object obj = iter.next();
        year = (SchoolYear)obj;
        placesId = new Integer(-1);
        schoolPlaces = null;
        try{
          yearId = (Integer) year.getPrimaryKey();
          T.add(this.tFormat.format(year.getSchoolYearName()),col++,row);
          if(schoolsYearPlaces.containsKey(yearId)){

            schoolPlaces = (SchoolYearPlaces) schoolsYearPlaces.get(yearId);
            placesId = (Integer) schoolPlaces.getPrimaryKey();
          }

          if(edit){
            TextInput places = new TextInput("sch_places_"+i);
            places.setLength(4);
            if(schoolPlaces!=null) {
				places.setContent(String.valueOf(schoolPlaces.getPlaces()));
			}
            HiddenInput hyear = new HiddenInput("sch_year_"+i,String.valueOf(yearId));
            HiddenInput hplaces = new HiddenInput("sch_year_places_id_"+i,placesId.toString());

            i++;
            T.add(places,col,row);
            T.add(hyear,col,row);
            T.add((hplaces),col++,row);

          }
          else if(schoolPlaces!=null){
            T.add(this.tFormat.format(schoolPlaces.getPlaces()),col++,row);
          }
        }
        catch(Exception ex){ex.printStackTrace();}
        row++;
        col = 2;
      }
      if(edit){
        T.add(new HiddenInput("pl_count",String.valueOf(i)));
        T.add(new SubmitButton(this.iwrb.getLocalizedImageButton("save","Save"), "sch_save_school_places","true"),col,row);
      }
    }
    return T;
  }

  public PresentationObject getInputTable(){
    Table T = new Table(3,16);
    T.mergeCells(1,1,3,1);



    return T;
  }

  private Map getMapOfYears(Collection c){
    Map m = new HashMap(c.size());
    try {
      Iterator iter = c.iterator();
      while(iter.hasNext()){
        SchoolYearPlaces p = (SchoolYearPlaces) iter.next();
        m.put(new Integer(p.getSchoolYearId()),p);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return m;
  }


  private Collection getSchoolYears(School school)throws java.rmi.RemoteException{
    try{
      return school.findRelatedSchoolYears();
    }
    catch(Exception ex){
      throw new java.rmi.RemoteException("Error finding schoolyears",ex);
    }
  }

  public void main(IWContext iwc)throws Exception{
    this.iwb = getBundle(iwc);
    this.iwrb = getResourceBundle(iwc);
    this.tFormat = TextFormat.getInstance();
    control(iwc);
  }
}