package com.idega.block.school.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.ui.*;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.presentation.text.*;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.util.text.TextFormat;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolType;

import java.rmi.RemoteException;
import java.util.Collection;
import com.idega.business.IBOLookup;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class SchoolTypeEditor extends Block {

  IWResourceBundle iwrb;
  IWBundle iwb;
  TextFormat tFormat;
  SchoolBusiness sbBean;
  public final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.school";

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  private void control(IWContext iwc) throws Exception{
    //debugParameters(iwc);
    initBeans(iwc);
    Form F = new Form();

    if(iwc.isParameterSet("sch_save_type")){
      saveArea(iwc);
      F.add(getListTable(null));
    }
    else if(iwc.isParameterSet("sch_delete_type")){
      int id = Integer.parseInt(iwc.getParameter("sch_delete_type"));
      sbBean.removeSchoolType(id);
      F.add(getListTable(null));
    }
    else if(iwc.isParameterSet("sch_school_type_id")){
      int id = Integer.parseInt(iwc.getParameter("sch_school_type_id"));
      F.add(getInput(id));

    }
    else if(iwc.isParameterSet("sch_new_type")){
      F.add(getInput(-1));
    }
    else
      F.add(getListTable(null));

     add(F);

  }

  private void initBeans(IWContext iwc) throws java.rmi.RemoteException,javax.ejb.CreateException{
    sbBean = (SchoolBusiness) IBOLookup.getServiceInstance(iwc,SchoolBusiness.class);
    //sabBean = sabHome.create();
  }

  private PresentationObject getInput(int id)throws java.rmi.RemoteException{
    return getInputTable(sbBean.getSchoolType(new Integer(id)));
  }

  private void saveArea(IWContext iwc)throws java.rmi.RemoteException{
    if(iwc.isParameterSet("sch_save_type")){
      String id = iwc.getParameter("sch_school_type_id");
      String name = iwc.getParameter("sch_type_name");
      String info = iwc.getParameter("sch_type_info");
      String cat = iwc.getParameter("sch_type_cat");
      String locKey = iwc.getParameter("sch_type_lockey");
	  String maxAge = iwc.getParameter("sch_type_maxage");
	  Integer iMaxAge = null;
	  	try {
			iMaxAge = Integer.valueOf(maxAge);
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
      int aid = -1;
      if(id!=null)
        aid = Integer.parseInt(id);

      sbBean.storeSchoolType(aid,name,info,cat,locKey,iMaxAge);
    }
  }

  public PresentationObject getListTable(SchoolType area) {
    Table T = new Table();
    int row = 1;

    Collection schoolTypes = new java.util.Vector(0);
    try{
      schoolTypes = sbBean.findAllSchoolTypes();
    }
    catch(java.rmi.RemoteException rex){

    }
    T.mergeCells(1,1,2,1);
    Link newLink = new Link(iwrb.getLocalizedImageButton("new","New"));
    newLink.addParameter("sch_new_type","true");
    T.add(newLink,1,row);
    row++;
    T.add(tFormat.format(iwrb.getLocalizedString("name","Name"),tFormat.HEADER),2,row);
    T.add(tFormat.format(iwrb.getLocalizedString("info","Info"),tFormat.HEADER),3,row);
    row++;

    java.util.Iterator iter = schoolTypes.iterator();
    SchoolType sType ;
    while(iter.hasNext()){
      sType = (SchoolType) iter.next();
      try{
      Link L = new Link(tFormat.format("edit"));
      L.addParameter("sch_school_type_id",((Integer)sType.getPrimaryKey()).intValue());
      T.add(L,1,row);
      T.add(tFormat.format(sType.getSchoolTypeName()),2,row);
      T.add(tFormat.format(sType.getSchoolTypeInfo()),3,row);
      }
      catch(Exception ex){}
      row++;
    }
    return T;
  }

  public PresentationObject getInputTable(SchoolType type) throws RemoteException {
    Table T = new Table(3,6);
    T.mergeCells(1,1,3,1);
    T.add(tFormat.format(iwrb.getLocalizedString("school_type","Schooltype"),tFormat.TITLE),1,1);
    T.add(tFormat.format(iwrb.getLocalizedString("category","Category")),1,2);
    T.add(tFormat.format(iwrb.getLocalizedString("name","Name")),1,3);
    T.add(tFormat.format(iwrb.getLocalizedString("info","Info")),1,4);
	T.add(tFormat.format(iwrb.getLocalizedString("maxage","Max school age")),1,4);
    T.add(tFormat.format(iwrb.getLocalizedString("localization_key","Key")),1,5);

		SelectorUtility util = new SelectorUtility();
		DropdownMenu drpCategory = (DropdownMenu) util.getSelectorFromIDOEntities(new DropdownMenu("sch_type_cat"), sbBean.getSchoolCategories(), "getLocalizedKey", iwrb);

    TextInput inputName = new TextInput("sch_type_name");
    TextInput inputKey = new TextInput("sch_type_lockey");
	TextInput inputAge = new TextInput("sch_type_maxage");
	inputAge.setLength(4);
    TextArea inputInfo = new TextArea("sch_type_info");

    int typeId = -1;
    if(type!=null){
      try{
        String name = type.getSchoolTypeName();
        if(name!=null)
        inputName.setContent(name);
        
        String info = type.getSchoolTypeInfo();
        if(info !=null)
        inputInfo.setContent(info);
        String maxAge = Integer.toString(type.getMaxSchoolAge());
        inputAge.setContent(maxAge);
        typeId = ((Integer)type.getPrimaryKey()).intValue();
        T.add(new HiddenInput("sch_school_type_id",String.valueOf(typeId)));
        String category = type.getSchoolCategory();
        drpCategory.setSelectedElement((category) );
        String key = type.getLocalizationKey();
        if(key!=null)
          inputKey.setContent(key);
      }
      catch(Exception ex){
        ex.printStackTrace();
      }
    }

    T.add(drpCategory,3,2);
    T.add(inputName,3,3);
    T.add(inputInfo,3,4);
	T.add(inputAge,3,5);
     T.add(inputKey,3,6);
    T.add(new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),"sch_save_type","true"),3,7);
    Link cancel = new Link(iwrb.getLocalizedImageButton("cancel","Cancel"));
    T.add(cancel,3,8);
    if(typeId > 0){
      Link delete = new Link(iwrb.getLocalizedImageButton("delete","Delete"));
      delete.addParameter("sch_delete_type",typeId);
      T.add(delete,3,9);
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
