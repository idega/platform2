package com.idega.block.presentation;

import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.Script;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Image;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import java.util.Vector;
import java.util.List;
import java.util.Collection;
import java.util.TreeMap;
import java.util.StringTokenizer;
import com.idega.core.data.ICCategory;
import com.idega.block.category.business.*;
import com.idega.io.ObjectSerializer;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class CategoryWindow extends IWAdminWindow {

  protected boolean isAdmin = false;

  private int iCategoryId = -1;
  private int iObjectInstanceId = -1;
  private String sType = "no_type";

	public static final  String prmCategoryId = "iccat_categoryid";
  public static final  String prmObjInstId = "iccat_obinstid";
	public  final static String prmCategoryType = "iccat_type";
  private static final String actDelete = "iccat_del";
  private static final String actSave = "iccat_save";
  private static final String actClose = "iccat_close";
  private static final String actForm = "iccat_form";

  protected IWResourceBundle iwrb;
  protected IWBundle iwb,core;

	private int iObjInsId = -1;
	private int iUserId = -1;

  public CategoryWindow() {
    setWidth(500);
    setHeight(460);
    setResizable(true);
		setUnMerged();
  }

  protected void control(IWContext iwc){
    //debugParameters(iwc);
    Table T = new Table();
    T.setCellpadding(0);
    T.setCellspacing(0);

		if(iCategoryId <= 0 && iwc.isParameterSet(prmCategoryId)){
			iCategoryId = Integer.parseInt(iwc.getParameter(prmCategoryId ));
		}

    if(iObjectInstanceId <= 0 && iwc.isParameterSet(prmObjInstId)){
			iObjectInstanceId = Integer.parseInt(iwc.getParameter(prmObjInstId ));
		}

    if( iwc.isParameterSet(prmCategoryType)){
			sType = iwc.getParameter(prmCategoryType );
		}

    if(isAdmin){
			if(iwc.isParameterSet(actForm)){
        processCategoryForm(iwc);
      }
      addCategoryFields(CategoryFinder.getCategory(iCategoryId));
    }
    else{
      add(formatText(iwrb.getLocalizedString("access_denied","Access denied")));
    }
  }

  private void processCategoryForm(IWContext iwc){
			// saving :
			if(iwc.isParameterSet(actSave) || iwc.isParameterSet(actSave+".x") ){
        String sName = iwc.getParameter("name");
        String sDesc = iwc.getParameter("desc");
        String sType = iwc.getParameter(prmCategoryType);
        String multi = iwc.getParameter("multi");
				if(sName!=null && sType !=null){
          //System.err.println("saving category :"+iCategoryId+" icoid :"+iObjectInstanceId);
					iCategoryId = CategoryBusiness.saveCategory(iCategoryId,sName,sDesc,iObjectInstanceId,sType,multi!=null).getID();
				}
			}
			if(iwc.isParameterSet(actClose) || iwc.isParameterSet(actClose+".x") ){
				setParentToReload();
				close();
			}
			// deleting :
			else if(iwc.isParameterSet(actDelete) || iwc.isParameterSet(actDelete+".x") ){
        try{
				CategoryBusiness.deleteCategory(iCategoryId);
        }
        catch(Exception ex){
          ex.printStackTrace();
        }
			}
  }

	 private void addCategoryFields(ICCategory eCategory){

	  String sCategory= iwrb.getLocalizedString("category","Category");
    String sName = iwrb.getLocalizedString("name","Name");
    String sDesc = iwrb.getLocalizedString("description","Description");
		//String sFields = iwrb.getLocalizedString("fields","Fields");
		boolean hasCategory = eCategory !=null ? true:false;

		Link newLink = new Link(core.getImage("/shared/create.gif"));
		newLink.addParameter(prmCategoryId,-1);
		newLink.addParameter(prmObjInstId,iObjectInstanceId);
		newLink.addParameter(actForm,"true");

    /** @todo  permission handling */
		List L = CategoryFinder.listOfCategories(sType);
    /*
    Collection C = CategoryFinder.collectCategoryIntegerIds(iObjectInstanceId);
    try{
      String collString = ObjectSerializer.serialize(C);
      System.err.println("collString");
      System.err.println(collString);
    }catch(Exception ex){

    }
*/



		DropdownMenu catDrop = new DropdownMenu(L,prmCategoryId);
		catDrop.addMenuElementFirst("-1",sCategory);
		catDrop.setToSubmit();

		TextInput tiName = new TextInput("name");
    tiName.setLength(40);
    tiName.setMaxlength(255);

		TextArea taDesc = new TextArea("desc",65,5);

		Table catTable = new Table(5,1);
		catTable.setCellpadding(0);
		catTable.setCellspacing(0);
		setStyle(catDrop);
		catTable.add(catDrop,1,1);
		catTable.add(newLink,3,1);
		catTable.setWidth(2,1,"20");
		catTable.setWidth(4,1,"20");

		addLeft(sCategory,catTable,true,false);
		addLeft(sName,tiName,true);
		addLeft(sDesc,taDesc,true);

    if(hasCategory){
			int id = eCategory.getID();
			if(eCategory.getName()!=null)
				tiName.setContent(eCategory.getName());
			if(eCategory.getDescription()!=null)
				taDesc.setContent(eCategory.getDescription());

		  catDrop.setSelectedElement(String.valueOf(id));

			if(true){
        Link deleteLink = new Link(core.getImage("/shared/delete.gif"));
        deleteLink.addParameter(actDelete,"true");
        deleteLink.addParameter(prmCategoryId,id);
        deleteLink.addParameter(actForm,"true");
        catTable.add(deleteLink,5,1);
			}
		}
		SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),actSave);
		SubmitButton close = new SubmitButton(iwrb.getLocalizedImageButton("close","Close"),actClose);
    addSubmitButton(save);
		addSubmitButton(close);
    addHiddenInput( new HiddenInput (prmCategoryType,sType));
    addHiddenInput( new HiddenInput (prmObjInstId,String.valueOf(iObjectInstanceId)));
    addHiddenInput( new HiddenInput (actForm,"true"));

  }

  public void main(IWContext iwc) throws Exception{
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
		core = iwc.getApplication().getCoreBundle();
    String title = iwrb.getLocalizedString("ic_category_editor","Category Editor");
    setTitle(title);
    addTitle(title);
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
  }

}
