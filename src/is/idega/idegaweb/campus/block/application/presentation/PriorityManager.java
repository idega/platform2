/*
 * Created on 12.7.2004
 *
 * Copyright (C) 2004 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 */
package is.idega.idegaweb.campus.block.application.presentation;

import is.idega.idegaweb.campus.block.application.data.Priority;
import is.idega.idegaweb.campus.presentation.CampusBlock;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.builder.handler.ColorHandler;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author aron
 *
 * PriorityManager TODO Describe this type
 */
public class PriorityManager extends CampusBlock {
	
	
	
	private static final String PRIORITY_COLOR = "prior_subj_extra";
	private static final String PRIORITY_DESC = "prior_subj_desc";
	private Priority priority = null;

	private static final String PRIORITY_ID = "prior_priority_id";
	
	public void main(IWContext iwc){

	      if(iwc.hasEditPermission(this)){
	       
	        if(iwc.isParameterSet(PRIORITY_ID)){
	          try {
	          	String id = iwc.getParameter(PRIORITY_ID);
	            priority = getApplicationService(iwc).getPriorityHome().findByPrimaryKey(id);
	          }
	          catch (Exception ex) {

	          }

	        }

	        if(iwc.isParameterSet("save")||iwc.isParameterSet("save.x")){
	          doUpdate(iwc);
	        }
	        else if(iwc.isParameterSet("delete")){
	          doDelete(iwc);
	        }

	        Table T = new Table();
	        T.setVerticalAlignment(1,1,"top");
	        T.setVerticalAlignment(2,1,"top");
	        T.add(getFormTable(iwc),1,1);
	        T.add(getTable(iwc),2,1);
	        add(T);

	      }
	      else
	        this.add(getNoAccessObject(iwc));

	  }

	 

	  private PresentationObject getTable(IWContext iwc){

	    Collection L = null;
		try {
			L = getApplicationService(iwc).getPriorityHome().findAll();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	    DataTable dTable = new DataTable();
	    dTable.setTitlesHorizontal(true);
	    dTable.addTitle(localize("priorities","Priorities"));
	    dTable.add(getHeader(localize("priority_code","Priority Code")),1,1);
	    dTable.add(getHeader(localize("description", "Description")),2,1);
	    dTable.add(getHeader(localize("hex_color", "Hex color")),3,1);
	   

	    if(L != null){
	      int a = 2;
	      for (Iterator iter = L.iterator(); iter.hasNext();) {
			Priority AS = (Priority) iter.next();
			
	        dTable.add(getSubjectLink(AS),1,a);
	        dTable.add(getText(AS.getDescription()),2,a);
	        if(AS.getHexColor()!=null)
	        dTable.add(getText(AS.getHexColor()),3,a);
	        dTable.add((getDeleteLink(AS)),4,a);
	        a++;
	      }
	    }
	    return dTable;
	  }

	  private PresentationObject getFormTable(IWContext iwc){
	    DataTable dTable = new DataTable();
	    dTable.setTitlesHorizontal(true);
	    dTable.addTitle(localize("new_priority","New priority"));

	    TextInput code = getTextInput(PRIORITY_ID);
	    TextInput Description = getTextInput(PRIORITY_DESC);
	    code.setLength(4);
	    code.setMaxlength(4);
	    String color = "";
	   
	    if(priority !=null){
	    	  code.setContent(priority.getPrimaryKey().toString());
	      Description.setContent(priority.getDescription());
	      
	      if(priority.getHexColor()!=null)
	      	color = (priority.getHexColor());
	      if(color==null)
	      	color = "";
	      
	      dTable.add(new HiddenInput(PRIORITY_ID,priority.getPrimaryKey().toString()));
	    }
	    dTable.add(getHeader(localize("priority_code","Priority Code")),1,1);
	    dTable.add(getHeader(localize("description", "Description")),2,1);
	    dTable.add(getHeader(localize("hex_color", "Hex color")),3,1);
	    dTable.add(code,1,2);
	    dTable.add(Description,2,2);

	    ColorHandler colorHandler = new ColorHandler();
	    PresentationObject handlerObject = colorHandler.getHandlerObject(PRIORITY_COLOR,color,iwc);
	    
	    dTable.add(handlerObject,3,2);

	    dTable.addButton((SubmitButton)getSaveButton("save"));

	    Form F = new Form();
	    F.add(dTable);
	    return F;
	  }

	  public Link getDeleteLink(Priority AS){
	    Link L = new Link("X");
	    L.addParameter("delete",AS.getPrimaryKey().toString());
	    return L;
	  }

	   public Link getSubjectLink(Priority AS){
	    Link L = new Link(AS.getPriority());
	    L.addParameter(PRIORITY_ID,AS.getPrimaryKey().toString());
	    return L;
	  }

	  public void doDelete(IWContext iwc){
	    String id = (iwc.getParameter("delete"));
	   try {
			 getApplicationService(iwc).removePriority(id);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	  }

	  public void doUpdate(IWContext iwc){
	    String desc= iwc.getParameter(PRIORITY_DESC).trim();
	    String extra = iwc.getParameter(PRIORITY_COLOR);
	    String id = iwc.getParameter(PRIORITY_ID);
	 
	     try {
			 getApplicationService(iwc).storePriority(id,desc,extra);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	   
	  }

	   
}
