package com.idega.block.school.presentation;

import java.util.Collection;
import java.util.Iterator;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolTypeHome;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
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

public class SchoolYearEditor extends Block {

  IWResourceBundle iwrb;
  IWBundle iwb;
  TextFormat tFormat;

  SchoolBusiness sabBean;
  public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.school";

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private void control(IWContext iwc) throws Exception{
    debugParameters(iwc);
    initBeans(iwc);
    Form F = new Form();

    if(iwc.isParameterSet("sch_save_year")){
      saveYear(iwc);
      F.add(getListTable(null));
    }
    else if(iwc.isParameterSet("sch_delete_year")){
      int id = Integer.parseInt(iwc.getParameter("sch_delete_year"));
      sabBean.removeSchoolYear(id);
      F.add(getListTable(null));
    }
    else if(iwc.isParameterSet("sch_school_year_id")){
      int id =Integer.parseInt(iwc.getParameter("sch_school_year_id"));
      F.add(getInput(id));

    }
    else if(iwc.isParameterSet("sch_new_area")){
      F.add(getInput(-1));
    }
    else
      F.add(getListTable(null));

     add(F);

  }

  private void initBeans(IWContext iwc) throws java.rmi.RemoteException{
    sabBean = (SchoolBusiness) IBOLookup.getServiceInstance(iwc,SchoolBusiness.class);
    //sabBean = sabHome.create();
  }

  private PresentationObject getInput(int id)throws java.rmi.RemoteException{
    return getInputTable(sabBean.getSchoolYear(new Integer(id)));
  }

  private void saveYear(IWContext iwc)throws java.rmi.RemoteException{
    if(iwc.isParameterSet("sch_save_year")){
      String id = iwc.getParameter("sch_school_year_id");
      String name = iwc.getParameter("sch_year_name");
      String type = iwc.getParameter("sch_type");
      String age = iwc.getParameter("sch_year_age");
      String info = iwc.getParameter("sch_year_info");
      String localizedKey = iwc.getParameter("localized_key");
      String category = null;
      if (iwc.isParameterSet("sch_category")) {
      		category = iwc.getParameter("sch_category");
      }
      int iAge = -1,sid = -1, stid = -1;
      if(id !=null)
        sid = Integer.parseInt(id);
      if(age!=null)
        iAge = Integer.parseInt(age);
       if(type!=null)
       	stid = Integer.parseInt(type);
      sabBean.storeSchoolYear(sid,name,stid,category,info,localizedKey,iAge);
    }
  }

  public PresentationObject getListTable(SchoolYear area) {
    Table T = new Table();
    int row = 1;

    Collection SchoolYears = new java.util.Vector(0);
    try{
      SchoolYears = sabBean.findAllSchoolYears();
    }
    catch(java.rmi.RemoteException rex){

    }
    T.mergeCells(1,1,2,1);
    Link newLink = new Link(iwrb.getLocalizedImageButton("new","New"));
    newLink.addParameter("sch_new_area","true");
    T.add(newLink,1,row);
    row++;
    T.add(tFormat.format(iwrb.getLocalizedString("name","Name"),tFormat.HEADER),2,row);
    T.add(tFormat.format(iwrb.getLocalizedString("school_type","Type"),tFormat.HEADER),3,row);
		T.add(tFormat.format(iwrb.getLocalizedString("info","Info"),tFormat.HEADER),4,row);
    T.add(tFormat.format(iwrb.getLocalizedString("age","Age"),tFormat.HEADER),5,row);
    row++;

    java.util.Iterator iter = SchoolYears.iterator();
    SchoolYear sYear ;
    SchoolType sType;
    SchoolTypeHome stHome;
    while(iter.hasNext()){
			sYear = (SchoolYear) iter.next();
      try{
      	stHome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
      	try {
      		sType = stHome.findByPrimaryKey(new Integer(sYear.getSchoolTypeId()));
      	} catch (Exception e) {
      		sType = null;
      	}
      	Link L = new Link(tFormat.format("edit"));
      	L.addParameter("sch_school_year_id",((Integer)sYear.getPrimaryKey()).intValue());
      	T.add(L,1,row);
      	T.add(tFormat.format(sYear.getSchoolYearName()),2,row);
      	if (sType != null) {
      		T.add(tFormat.format(iwrb.getLocalizedString(sType.getLocalizationKey(), sType.getName())), 3, row);
      	}
      	T.add(tFormat.format(sYear.getSchoolYearInfo()),4,row);
      	T.add(tFormat.format(sYear.getSchoolYearAge()),5,row);
      }
      catch(Exception ex){}
      row++;
    }
    return T;
  }

  public PresentationObject getInputTable(SchoolYear entity){
    Table T = new Table(3,8);
    T.mergeCells(1,1,3,1);
    T.add(tFormat.format(iwrb.getLocalizedString("school_year","SchoolYear"),tFormat.TITLE),1,1);
    T.add(tFormat.format(iwrb.getLocalizedString("name","Name")),1,2);
		T.add(tFormat.format(iwrb.getLocalizedString("school_type","Type"),tFormat.HEADER),1,3);
    T.add(tFormat.format(iwrb.getLocalizedString("info","Info")),1,4);
    T.add(tFormat.format(iwrb.getLocalizedString("age","Age")),1,5);
    T.add(tFormat.format(iwrb.getLocalizedString("localized_key","Localized key")),1,6);
    T.add(tFormat.format(iwrb.getLocalizedString("category","Category")),1,7);

    TextInput inputName = new TextInput("sch_year_name");
    TextInput inputAge = new TextInput("sch_year_age");
    DropdownMenu inputType = new DropdownMenu("sch_type");
    TextArea inputInfo = new TextArea("sch_year_info");
    TextInput localizedKey = new TextInput("localized_key");
    DropdownMenu inputCategory = new DropdownMenu("sch_category");

	try {
			SchoolTypeHome stHome = (SchoolTypeHome) IDOLookup.getHome(SchoolType.class);
			Collection coll = stHome.findAllSchoolTypes();
			if (coll != null && !coll.isEmpty() ) {
				inputType.addMenuElements(coll);
			}
		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		try {
			SchoolCategoryHome scHome = (SchoolCategoryHome) IDOLookup.getHome(SchoolCategory.class);
			Collection coll = scHome.findAllCategories();
			Iterator iter = coll.iterator();
			while (iter.hasNext()) {
				SchoolCategory category = (SchoolCategory) iter.next();
				inputCategory.addMenuElement(category.getCategory(), iwrb.getLocalizedString(category.getLocalizedKey(), category.getCategory()));
			}
			inputCategory.addMenuElementFirst("", "");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

    int beanId = -1;
    if(entity!=null){
      try{
	      beanId = ((Integer)entity.getPrimaryKey()).intValue();
      	inputName.setContent(entity.getSchoolYearName());
      	inputInfo.setContent(entity.getSchoolYearInfo());
      	inputAge.setContent(String.valueOf(entity.getSchoolYearAge()));
      	inputType.setSelectedElement(entity.getSchoolTypeId());
      	if (entity.getLocalizedKey() != null) {
      		localizedKey.setContent(entity.getLocalizedKey());
      	}
      	if (entity.getSchoolCategory() != null) {
      		inputCategory.setSelectedElement(entity.getSchoolCategoryPK().toString());
      	}
      	T.add(new HiddenInput("sch_school_year_id",String.valueOf(beanId)));
      }
      catch(Exception ex){}
    }
    

    T.add(inputName,3,2);
    T.add(inputType, 3, 3);
    T.add(inputInfo,3,4);
    T.add(inputAge,3,5);
    T.add(localizedKey,3,6);
    T.add(inputCategory,3,7);
    T.add(new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),"sch_save_year","true"),3,8);
    Link cancel = new Link(iwrb.getLocalizedImageButton("cancel","Cancel"));
    T.add(cancel,3,8);
    if(beanId>0){
      Link delete = new Link(iwrb.getLocalizedImageButton("delete","Delete"));
      delete.addParameter("sch_delete_year",beanId);
      T.add(delete,3,8);
    }

    return T;
  }

  public void main(IWContext iwc)throws Exception{
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
    tFormat = tFormat.getInstance();
    control(iwc);
  }
  
}