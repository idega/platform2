package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Map;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolAreaHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.core.location.business.CommuneBusiness;
import com.idega.core.location.data.Commune;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.presentation.util.TextFormat;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolEditor extends Block {

  IWResourceBundle iwrb;
  IWBundle iwb;
  TextFormat tFormat;
  SchoolBusiness sabBean;
  
  Collection schoolTypeIds = null;
  
  public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.school";

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private void control(IWContext iwc) throws Exception{
    //debugParameters(iwc);
    initBeans(iwc);
    Form F = new Form();

    if(iwc.isParameterSet("sch_save_school")){
      saveSchool(iwc);
      F.add(getListTable(null));
    }
    else if(iwc.isParameterSet("sch_delete_school")){
      int id = Integer.parseInt(iwc.getParameter("sch_delete_school"));
      sabBean.removeSchool(id);
      F.add(getListTable(null));
    }
    else if(iwc.isParameterSet("sch_school_id")){
      int id = Integer.parseInt(iwc.getParameter("sch_school_id"));
      F.add(getInput(iwc,id));

    }
    else if(iwc.isParameterSet("sch_new_school")){
      F.add(getInput(iwc,-1));
    }
    else
      F.add(getListTable(null));

    add(F);

  }

  private void initBeans(IWContext iwc) throws java.rmi.RemoteException{
    sabBean = (SchoolBusiness) IBOLookup.getServiceInstance(iwc,SchoolBusiness.class);
  }

  private PresentationObject getInput(IWContext iwc ,int id)throws java.rmi.RemoteException{
    return getInputTable(iwc,sabBean.getSchool(new Integer(id)));
  }

  private void saveSchool(IWContext iwc)throws java.rmi.RemoteException{
    if(iwc.isParameterSet("sch_save_school")){
      String id = iwc.getParameter("sch_school_id");
      
      String name = iwc.getParameter("sch_name");
      String address = iwc.getParameter("sch_address");
      String info = iwc.getParameter("sch_info");
      //String type = iwc.getParameter("sch_type_id");
      String area = iwc.getParameter("sch_area_id");
      String zipcode = iwc.getParameter("sch_zip_code");
      String ziparea = iwc.getParameter("sch_zip_area");
      String phone = iwc.getParameter("sch_phone");
      String keycode = iwc.getParameter("sch_keycode");
      String lon = iwc.getParameter("sch_lon");
      String lat = iwc.getParameter("sch_lat");
      String commune = iwc.getParameter("sch_commune");
      String[] type_ids = iwc.getParameterValues("sch_type_ids");
      String[] year_ids = iwc.getParameterValues("sch_year_ids");
      int[] types = new int[0];
      int[] years = new int[0];
      if(type_ids!=null && type_ids.length > 0){
        types = new int[type_ids.length];
        for (int i = 0; i < type_ids.length; i++) {
          types[i] = Integer.parseInt(type_ids[i]);
        }

      }
      if(year_ids!=null && year_ids.length > 0){
        years = new int[year_ids.length];
        for (int i = 0; i < year_ids.length; i++) {
          years[i] = Integer.parseInt(year_ids[i]);
        }

      }
      int areaId = -1,sid = -1;
      if(id!=null)
        sid = Integer.parseInt(id);
      if(area!=null)
        areaId = Integer.parseInt(area);
      
      Integer communePK = null;
      if (commune != null) {
      	communePK = new Integer(commune);
      }  
//		System.err.println("school id is "+id);      
			sabBean.storeSchool(sid,name,info,address,zipcode,ziparea,phone,keycode,lat,lon,areaId,types,years, communePK);
    }
  }

  public PresentationObject getListTable(School ent)throws RemoteException {
    Table T = new Table();
    int row = 1;

    Collection schools = new java.util.Vector(0);
    try{
    	if (schoolTypeIds == null) {
      	schools = sabBean.findAllSchools();
    	}else {
    		schools = sabBean.findAllSchoolsByType(schoolTypeIds);	
    	}
    }
    catch(java.rmi.RemoteException rex){

    }
    T.mergeCells(1,1,2,1);
    Link newLink = new Link(iwrb.getLocalizedImageButton("new","New"));
    newLink.addParameter("sch_new_school","true");
    int col = 2;
    T.add(newLink,1,row);
    row++;

    //T.add(tFormat.format(iwrb.getLocalizedString("type","Type")),col++,row);
    T.add(tFormat.format(iwrb.getLocalizedString("area","Area")),col++,row);
    T.add(tFormat.format(iwrb.getLocalizedString("name","Name")),col++,row);
    T.add(tFormat.format(iwrb.getLocalizedString("address","Address")),col++,row);
    T.add(tFormat.format(iwrb.getLocalizedString("zipcode","Zipcode")),col++,row);
    T.add(tFormat.format(iwrb.getLocalizedString("ziparea","Ziparea")),col++,row);
		T.add(tFormat.format(iwrb.getLocalizedString("commune","Commune")),col++,row);
    T.add(tFormat.format(iwrb.getLocalizedString("phone","Phone")),col++,row);
//    T.add(tFormat.format(iwrb.getLocalizedString("keycode","Keycode")),col++,row);
//    T.add(tFormat.format(iwrb.getLocalizedString("latitude","Latitude")),col++,row);
//    T.add(tFormat.format(iwrb.getLocalizedString("longitude","Longitude")),col++,row);
    row++;

    java.util.Iterator iter = schools.iterator();
    School school ;
    SchoolAreaHome areaHome = (SchoolAreaHome)IDOLookup.getHome(SchoolArea.class);
    //CommuneHome communeHome = (CommuneHome) IDOLookup.getHome(Commune.class);
    SchoolArea area;
    //Commune commune;
    Commune communePK;
    col = 1;
    while(iter.hasNext()){
      Object obj = iter.next();
      System.err.println("Object class "+obj.getClass().getName());
      school = (School)obj;
      try{
      Link L = new Link(tFormat.format("edit"));
      L.addParameter("sch_school_id",((Integer)school.getPrimaryKey()).intValue());
      T.add(L,col++,row);
      //T.add(tFormat.format(school.getSchoolTypeId()),col++,row);
      area = areaHome.findByPrimaryKey(new Integer(school.getSchoolAreaId()));
      communePK = (Commune)school.getCommunePK();
      T.add(tFormat.format(area.getName()),col++,row);
      T.add(tFormat.format(school.getSchoolName()),col++,row);
      T.add(tFormat.format(school.getSchoolAddress()),col++,row);
      T.add(tFormat.format(school.getSchoolZipCode()),col++,row);
      T.add(tFormat.format(school.getSchoolZipArea()),col++,row);
      if (communePK != null) {
    		//commune = communeHome.findByPrimaryKey(communePK);
				T.add(tFormat.format(communePK.getCommuneName()),col,row);
      }
      col++;
      T.add(tFormat.format(school.getSchoolPhone()),col++,row);
 //     T.add(tFormat.format(school.getSchoolKeyCode()),col++,row);
 //     T.add(tFormat.format(school.getSchoolLatitude()),col++,row);
 //     T.add(tFormat.format(school.getSchoolLongitude()),col++,row);
      }
      catch(Exception ex){}
      row++;
      col = 1;
    }
    return T;
  }

  public PresentationObject getInputTable(IWContext iwc,School ent)throws java.rmi.RemoteException{
    int last = 16;
    Table T = new Table(3,last);
    T.mergeCells(1,1,3,1);

    TextInput inputName = new TextInput("sch_name");
    TextInput inputAddress = new TextInput("sch_address");
    TextArea inputInfo = new TextArea("sch_info");

    TextInput inputZipCode = new TextInput("sch_zip_code");
    TextInput inputZipArea = new TextInput("sch_zip_area");
    TextInput inputPhone = new TextInput("sch_phone");
    TextInput inputKeyCode = new TextInput("sch_keycode");
    TextInput inputLON = new TextInput("sch_lon");
    TextInput inputLAT = new TextInput("sch_lat");
    DropdownMenu drpArea = new DropdownMenu(getSchoolAreas(iwc),"sch_area_id");
    DropdownMenu communes = new DropdownMenu("sch_commune");
		SelectorUtility su = new SelectorUtility();
		su.getSelectorFromIDOEntities(communes, getCommuneBusiness(iwc).getCommunes(), "getCommuneName");
    
    Map schooltypes = null,schoolyears = null;
    int Id = -1;
    if(ent!=null){

      try{
	      schooltypes = getSchoolRelatedSchoolTypes(iwc,ent);
	      schoolyears = getSchoolRelatedSchoolYears(iwc,ent);
	
	      Id = ((Integer)ent.getPrimaryKey()).intValue();
	      inputName.setContent(ent.getSchoolName());
	      inputAddress.setContent(ent.getSchoolAddress());
	      inputInfo.setContent(ent.getSchoolInfo());
	      inputZipCode.setContent(ent.getSchoolZipCode());
	      inputZipArea.setContent(ent.getSchoolZipArea());
	      inputPhone.setContent(ent.getSchoolPhone());
	      inputKeyCode.setContent(ent.getSchoolKeyCode());
	      inputLON.setContent(ent.getSchoolLongitude());
	      inputLAT.setContent(ent.getSchoolLatitude());
	      //drpType.setSelectedElement(String.valueOf(ent.getSchoolTypeId()));//NO FIELD IN DATABASE!!
	      drpArea.setSelectedElement(String.valueOf(ent.getSchoolAreaId()));
	      if (ent.getCommunePK() != null) {
					communes.setSelectedElement(ent.getCommunePK().toString());
	      }
      }
      catch(Exception ex){}
    }

    int row = 1;

	T.add(new HiddenInput("sch_school_id",String.valueOf(Id)));
    //T.add(tFormat.format(iwrb.getLocalizedString("type","Type")),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("area","Area"),tFormat.HEADER),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("name","Name"),tFormat.HEADER),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("address","Address"),tFormat.HEADER),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("zipcode","Zipcode"),tFormat.HEADER),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("ziparea","Ziparea"),tFormat.HEADER),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("phone","Phone"),tFormat.HEADER),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("info","Info"),tFormat.HEADER),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("keycode","Keycode"),tFormat.HEADER),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("latitude","Latitude"),tFormat.HEADER),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("longitude","Longitude"),tFormat.HEADER),1,row++);
		T.add(tFormat.format(iwrb.getLocalizedString("commune","Commune"),tFormat.HEADER),1,row++);
    T.add(tFormat.format(iwrb.getLocalizedString("school_area","SchoolArea"),tFormat.HEADER),1,row++);

    row = 2;
    //T.add(drpType,3,row++);

    T.add(inputName,3,row++);
    T.add(inputAddress,3,row++);
    T.add(inputZipCode,3,row++);
    T.add(inputZipArea,3,row++);
    T.add(inputPhone,3,row++);
    T.add(inputInfo,3,row++);
    T.add(inputKeyCode,3,row++);
    T.add(inputLAT,3,row++);
    T.add(inputLON,3,row++);
    T.add(communes, 3, row++);
    T.add(drpArea,3,row++);


    Table typeTable = new Table();
    int row2 = 1;
    Collection types = getSchoolTypes(iwc);
    if(types!=null && !types.isEmpty()){
      java.util.Iterator iter = types.iterator();
      boolean hasMap = schooltypes!=null;
      SchoolType type;
      CheckBox chk = new CheckBox("sch_type_ids");
      CheckBox tjk;
      Integer primaryKey;
      while(iter.hasNext()){
        type = (SchoolType) iter.next();
        primaryKey = (Integer) type.getPrimaryKey();
        tjk = (CheckBox) chk.clone();
        tjk.setValue(primaryKey.intValue());
        if(hasMap && schooltypes.containsKey(primaryKey)){
          tjk.setChecked(true);
        }
        typeTable.add(tjk,1,row2);
        typeTable.add(type.getSchoolTypeName(),2,row2);
        //row2++;

				Table yearTable = new Table();
				/////////////////
				Collection years = getSchoolYears(iwc, primaryKey.intValue());
				if(years!=null && !years.isEmpty()){
				  java.util.Iterator yearIter = years.iterator();
				  boolean yearMap = schoolyears!=null;
				  SchoolYear year;
				  CheckBox ychk = new CheckBox("sch_year_ids");
				  CheckBox ytjk;
				  Integer yprimaryKey;
				  int col3=1;
				  int row3 = 1;
				  while(yearIter.hasNext()){
						year = (SchoolYear) yearIter.next();
						yprimaryKey = (Integer) year.getPrimaryKey();
						ytjk = (CheckBox) ychk.clone();
						ytjk.setValue(yprimaryKey.intValue());
						//ytjk.setDisabled(!yearMap);
						//tjk.setToDisableOnClick(ytjk, false);
						//ytjk.setOnClick("this.form."+tjk.getID()+".selected = true");
						if(yearMap && schoolyears.containsKey(yprimaryKey)){
						  ytjk.setChecked(true);
						}
					yearTable.add(ytjk,col3,row3++);
					yearTable.add(year.getSchoolYearName(),col3++,row3);
					row3=1;
		
				  }
		
				}
		
				//typeTable.mergeCells(1,row2,2,row2);
				typeTable.setVerticalAlignment(1, row2, Table.VERTICAL_ALIGN_TOP);
				typeTable.setVerticalAlignment(2, row2, Table.VERTICAL_ALIGN_TOP);
				typeTable.setVerticalAlignment(3, row2, Table.VERTICAL_ALIGN_TOP);
				typeTable.add(yearTable,3,row2);
				++row2;
		//////////////////
      }

    }

    T.add(typeTable,1,13);

    T.add(new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),"sch_save_school","true"),3,last);
    Link cancel = new Link(iwrb.getLocalizedImageButton("cancel","Cancel"));
    T.add(cancel,3,last);
    if(Id > 0){
      Link delete = new Link(iwrb.getLocalizedImageButton("delete","Delete"));
      delete.addParameter("sch_delete_school",Id);
      T.add(delete,3,last);
    }

    return T;
  }

  private Map getSchoolRelatedSchoolTypes(IWContext iwc,School school)throws java.rmi.RemoteException{
    return sabBean.getSchoolRelatedSchoolTypes(school);
  }

  private Map getSchoolRelatedSchoolYears(IWContext iwc,School school)throws java.rmi.RemoteException{
    return sabBean.getSchoolRelatedSchoolYears(school);
  }

  private Collection getSchoolTypes(IWContext iwc)throws java.rmi.RemoteException{
    return sabBean.findAllSchoolTypes();
  }

   private Collection getSchoolYears(IWContext iwc, int schoolTypeId)throws java.rmi.RemoteException{
     return sabBean.findAllSchoolYearsBySchoolType(schoolTypeId);
  }

  private Collection getSchoolAreas(IWContext iwc)throws java.rmi.RemoteException{
    return sabBean.findAllSchoolAreas();
  }

  public void main(IWContext iwc)throws Exception{
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
    tFormat = tFormat.getInstance();
    control(iwc);
  }
  
  public void setSchoolTypeCategory(String typeCategory) {
		if (typeCategory != null && !typeCategory.equals("") ) {
			try {
				SchoolTypeHome sth = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
				schoolTypeIds = sth.findAllByCategory(typeCategory);
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		}
  	
  }
  
  public CommuneBusiness getCommuneBusiness(IWApplicationContext iwac) throws RemoteException {
  	return (CommuneBusiness) IBOLookup.getServiceInstance(iwac, CommuneBusiness.class);
  }

}