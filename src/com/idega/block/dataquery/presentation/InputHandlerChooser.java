package com.idega.block.dataquery.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import com.idega.block.dataquery.business.QueryService;
import com.idega.business.IBOLookup;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: 
 * This class and the class InputHandlerWindow are not in use at the moment.
 * <br>
 * Both classes are used for choosing (or changing) an InputHandler that is linked to a certain field in a query,
 * that is when defining a condition for that  field the specified InputHandler is used.
 * This feature is disabled in the QueryBuilder at the moment and should work only in the expert mode.
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Dec 1, 2003
 */
public class InputHandlerChooser extends Block {
	
	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.dataquery";
	
	public static final String DESCRIPTION_KEY = "descriptionKey";
	public static final String INPUT_HANDLER_KEY = "inputHandlerKey";
	public static final String ENTITY_KEY = "entityKey";
	public static final String ENTITY_FIELD = "entityField";
	
//	private String description = null;
//	private String field = null;
//	private String entity = null;
	
	public static String parseDescription(IWContext iwc) {
		return iwc.getParameter(DESCRIPTION_KEY);
	}
	
	public static String parseInputHandler(IWContext iwc) {
		return iwc.getParameter(INPUT_HANDLER_KEY);
	}
		
	
	public void main(IWContext iwc) throws Exception {
		add(getContent(iwc));
	}	
		
	public PresentationObject getContent(IWContext iwc) throws RemoteException {
		Table table = new Table(1,1);
		//table.add(getDescriptionField(),1,1);
		table.add(getInputHandlerDropDown(iwc),1,1);
		return table;
	}
		
//	private PresentationObject getDescriptionField() {
//		TextInput description = new TextInput(DESCRIPTION_KEY);
//		description.setWidth("80");
//		return description;
//	}
	
	private PresentationObject getInputHandlerDropDown(IWContext iwc)	throws RemoteException {
		DropdownMenu menu = new DropdownMenu(INPUT_HANDLER_KEY);
		Iterator iterator = getInputHandlerClassNames(iwc).iterator();
		while (iterator.hasNext()) {
			String name = (String) iterator.next();
			String localizedName = getLocalizedString(name, name, iwc);
			menu.addMenuElement(name, localizedName);
		}
		return menu;
	}
	
//	public void setField(String field) {
//		this.field = field;
//	}
//	
//	public void setEntity(String entity) {
//		this.entity = entity;
//	}
//	
//	public void setDescription(String description) {
//		this.description = description;
//	}
	
	private Collection getInputHandlerClassNames(IWContext iwc) throws RemoteException {
		QueryService service = getQueryService(iwc);
		return service.getInputHandlerNames();
	}
		
	public QueryService getQueryService(IWContext iwc)  throws RemoteException {
		return (QueryService) IBOLookup.getServiceInstance(iwc, QueryService.class);
	}

	public String getBundleIdentifier() {
    return IW_BUNDLE_IDENTIFIER;
  } 

	
}
