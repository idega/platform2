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
import com.idega.core.business.Category;
import com.idega.core.business.CategoryFinder;
import com.idega.core.business.CategoryBusiness;
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

  private int iCategoryId = -1;
  private int iObjectInstanceId = -1;
  private String sType = "no_type";
  private boolean multi = false;

	public static final  String prmCategoryId = "iccat_categoryid";
  public static final  String prmObjInstId = "iccat_obinstid";
	public  final static String prmCategoryType = "iccat_type";
  public  final static String prmMulti = "iccat_multi";
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
    setHeight(400);
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

    multi = iwc.isParameterSet(prmMulti);
    multi = true;

    if(true){
			if(iwc.isParameterSet(actForm)){
        processCategoryForm(iwc);
      }
      //addCategoryFields(CategoryFinder.getCategory(iCategoryId));
      getCategoryFields(CategoryFinder.getInstance().getCategory(iCategoryId));
    }
    else{
      add(formatText(iwrb.getLocalizedString("access_denied","Access denied")));
    }
  }

  private void processCategoryForm(IWContext iwc){
			// saving :
			if(iwc.isParameterSet(actSave) || iwc.isParameterSet(actSave+".x") ){
        String sName = iwc.getParameter("name");
        String sDesc = iwc.getParameter("info");
        String sType = iwc.getParameter(prmCategoryType);


				if(sName!=null && sType !=null){
          //System.err.println("saving category :"+iCategoryId+" icoid :"+iObjectInstanceId);

          if(iCategoryId <= 0){
            iCategoryId = CategoryBusiness.getInstance().saveCategory(iCategoryId,sName,sDesc,iObjectInstanceId,sType,multi).getID();
          }
          else {
            String[] sids= iwc.getParameterValues("id_box");
            int[] savedids = new int[0];
            if(sids!=null)
             savedids = new int[sids.length];
            for (int i = 0; i < savedids.length; i++) {
              savedids[i] = Integer.parseInt(sids[i]);
              System.err.println("save id "+savedids[i]);
            }
            CategoryBusiness.getInstance().updateCategory(iCategoryId,sName,sDesc);
            CategoryBusiness.getInstance().saveRelatedCategories(iObjectInstanceId,savedids);
          }
				}
			}
			if(iwc.isParameterSet(actClose) || iwc.isParameterSet(actClose+".x") ){
				setParentToReload();
				close();
			}
			// deleting :
			else if(iwc.isParameterSet(actDelete) || iwc.isParameterSet(actDelete+".x") ){
        try{
				  CategoryBusiness.getInstance().deleteCategory(iCategoryId);
          iCategoryId = -1;
        }
        catch(Exception ex){
          ex.printStackTrace();
        }
			}
  }

	 private void addCategoryFields(Category eCategory){

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
		List L = CategoryFinder.getInstance().listOfCategories(sType);
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

   private void getCategoryFields(Category eCategory){

	  String sCategory= iwrb.getLocalizedString("category","Category");
    String sName = iwrb.getLocalizedString("name","Name");
    String sDesc = iwrb.getLocalizedString("description","Description");
		//String sFields = iwrb.getLocalizedString("fields","Fields");

		Link newLink = new Link(core.getImage("/shared/create.gif"));
		newLink.addParameter(prmCategoryId,-1);
		newLink.addParameter(prmObjInstId,iObjectInstanceId);
		newLink.addParameter(actForm,"true");

    /** @todo  permission handling */
		List L = CategoryFinder.getInstance().listOfCategories(sType);
    Collection coll = CategoryFinder.getInstance().collectCategoryIntegerIds(iObjectInstanceId);

    int chosenId = eCategory!=null?eCategory.getID():-1;

    Table T = new Table();
    int row = 1;
    int col = 1;
    T.add(formatText(iwrb.getLocalizedString("name","Name")),2,row);
    T.add(formatText(iwrb.getLocalizedString("info","Info")),3,row);
    T.add(formatText(iwrb.getLocalizedString("use","Use")),1,row);
    row++;
    TextInput name = new TextInput("name");
    TextInput info = new TextInput("info");
    setStyle(name);
    setStyle(info);
    boolean formAdded = false;
    if(L !=null){
      java.util.Iterator iter = L.iterator();
      Category cat ;
      CheckBox box;
      RadioButton rad;
      Link deleteLink;
      int id;
      while(iter.hasNext()){
        col = 1;
        cat = (Category) iter.next();
        id = cat.getID();
        if(id == chosenId){
          name.setContent(cat.getName());
          if(cat.getDescription()!=null)
            info.setContent(cat.getDescription());
          T.add(name,2,row);
          T.add(info,3,row);
          T.add(new HiddenInput(prmCategoryId,String.valueOf(id)));
          formAdded = true;
        }
        else{
          Link Li = new Link(formatText(cat.getName()));
          Li.addParameter(prmCategoryId,id);
          Li.addParameter(prmCategoryType,sType);
          Li.addParameter(prmObjInstId,String.valueOf(iObjectInstanceId));
          T.add(Li,2,row);
          T.add(formatText(cat.getDescription()),3,row);
          deleteLink = new Link(core.getImage("/shared/delete.gif"));
          deleteLink.addParameter(actDelete,"true");
          deleteLink.addParameter(prmCategoryId,id);
          deleteLink.addParameter(prmCategoryType,sType);
          deleteLink.addParameter(prmObjInstId,String.valueOf(iObjectInstanceId));
          deleteLink.addParameter(actForm,"true");
          T.add(deleteLink,4,row);
        }
        if(multi){
          box = new CheckBox("id_box",String.valueOf(cat.getID()));
          box.setChecked(coll!=null && coll.contains(new Integer(cat.getID())));
          //setStyle(box);
          T.add(box,1,row);
        }
        else{
          rad = new RadioButton("id_box",String.valueOf(cat.getID()) );
          if(coll!=null && coll.contains(new Integer(cat.getID())))
            rad.setSelected();
          //setStyle(rad);
          T.add(rad,1,row);
        }
        row++;
      }
    }
    if(!formAdded){
       T.add(name,2,row);
       T.add(info,3,row);
    }
    else{
      Link li = new Link(iwrb.getLocalizedImageButton("new","New"));
      li.addParameter(prmCategoryType,sType);
      li.addParameter(prmObjInstId,String.valueOf(iObjectInstanceId));
      T.add(li,1,row);

    }

    addLeft(sCategory,T,true,false);

		SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),actSave);
		SubmitButton close = new SubmitButton(iwrb.getLocalizedImageButton("close","Close"),actClose);
    addSubmitButton(save);
		addSubmitButton(close);
    addHiddenInput( new HiddenInput (prmCategoryType,sType));
    addHiddenInput( new HiddenInput (prmObjInstId,String.valueOf(iObjectInstanceId)));
    addHiddenInput( new HiddenInput (actForm,"true"));

  }

  public static Link getWindowLink(int iCategoryId,int iInstanceId,String type,boolean multible){
    Link L = new Link();
    L.addParameter(CategoryWindow.prmCategoryId,iCategoryId);
    L.addParameter(CategoryWindow.prmObjInstId,iInstanceId);
    L.addParameter(CategoryWindow.prmCategoryType,type);
    if(multible)
      L.addParameter(CategoryWindow.prmMulti,"true");
    L.setWindowToOpen(CategoryWindow.class);
    return L;
  }

  public PresentationObject getNameInput(Category node){
    TextInput name = new TextInput("name");
    if(node!=null){
      name.setContent(node.getName());
    }
    return name;
  }

  public void main(IWContext iwc) throws Exception{
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
		core = iwc.getApplication().getCoreBundle();
    String title = iwrb.getLocalizedString("ic_category_editor","Category Editor");
    setTitle(title);
    addTitle(title);
    control(iwc);
  }

}
