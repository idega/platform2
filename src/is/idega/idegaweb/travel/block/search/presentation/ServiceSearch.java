package is.idega.idegaweb.travel.block.search.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import is.idega.idegaweb.travel.business.TravelSessionManager;

import com.idega.business.IBOLookup;
import com.idega.core.component.data.ICObject;
import com.idega.core.component.data.ICObjectHome;
import com.idega.data.IDOFinderException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.TextInput;

/**
 * @author gimmi
 */
public class ServiceSearch extends Block {

	public static final String IC_OBJECT_TYPE = "iw.travel.search";
	
	public static final String PARAMETER_SERVICE_SEARCH_FORM = "p_ssf";
	
	protected String textFontStyle;
	protected String headerFontStyle;
	protected String linkFontStyle;
	protected String errorFontStyle;
	
	protected String headerBackgroundColor;
	protected String linkBackgroundColor;
	protected String backgroundColor;
	
	protected String width;
	protected String formInputStyle;
	
	private Image headerImage;
	private Image windowHeaderImage;
	
	IWResourceBundle iwrb;
	public static List searchForms; // Laga eitthvad, kannski grillad ad hafa public static....
	Collection ICObjectList;
	private AbstractSearchForm currentSearchForm;

	public ServiceSearch() {
	}
	
	public void main(IWContext iwc) throws Exception {
		init(iwc);
		if (currentSearchForm == null) {
			add("cannot get searchform instance");
		} else {
			add(currentSearchForm);
		}
		//drawForm();
	}

	protected void init(IWContext iwc) throws Exception {
		iwrb = getTravelSessionManager(iwc).getIWResourceBundle();
		IWBundle bundle = getTravelSessionManager(iwc).getIWBundle();
		ICObjectList = bundle.getICObjectsList(IC_OBJECT_TYPE);
		Iterator iter = ICObjectList.iterator();
		ICObject object;
		ICObjectHome objectHome = (ICObjectHome) IDOLookup.getHome(ICObject.class);
		searchForms = new Vector();
		AbstractSearchForm ss;
		while (iter.hasNext()) {
			object = (ICObject) iter.next();
			try {
				Class tmpClass = Class.forName(object.getClassName());
				ss = (AbstractSearchForm) tmpClass.newInstance();
				searchForms.add( ss );
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		currentSearchForm = setCurrentSearchForm(iwc);
	}

	protected AbstractSearchForm setCurrentSearchForm(IWContext iwc) {
		try {
			String currentSF = iwc.getParameter(PARAMETER_SERVICE_SEARCH_FORM);
			AbstractSearchForm ss = null;
			if (currentSF != null) {
				ss = (AbstractSearchForm) Class.forName(currentSF).newInstance();
//				ss.main(iwc);
			} else if (!searchForms.isEmpty()) {
				ss = (AbstractSearchForm) searchForms.get(0);
			} else {
				ss = (AbstractSearchForm) Class.forName(AbstractSearchForm.class.getName()).newInstance();
			}
			ss.setTextFontStyle(textFontStyle);
			ss.setHeaderFontStyle(headerFontStyle);
			ss.setLinkFontStyle(linkFontStyle);
			ss.setErrorFontStyle(errorFontStyle);
			ss.setHeaderBackgroundColor(headerBackgroundColor);
			ss.setLinksBackgroundColor(linkBackgroundColor);
			ss.setHeaderImage(headerImage);
			ss.setWidth(width);
			ss.setFormInputStyle(formInputStyle);
			ss.setWindowHeaderImage(windowHeaderImage);
			return ss;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setTextFontStyle(String fontStyle) {
		this.textFontStyle = fontStyle;
	}
	
	public void setHeaderFontStyle(String fontStyle) {
		this.headerFontStyle = fontStyle;
	}
	
	public void setLinkFontStyle(String fontStyle) {
		this.linkFontStyle = fontStyle;
	}
	
	public void setErrorTextStyle(String fontStyle) {
		this.errorFontStyle = fontStyle;
	}

	public void setHeaderImage(Image image) {
		this.headerImage = image;
	}
	
	public void setHeaderBackgroundColor(String color) {
		this.headerBackgroundColor = color;
	}
	
	public void setLinksBackgroundColor(String color) {
		this.linkBackgroundColor = color;
	}
	
	public void setBackgroundColor(String color) {
		this.backgroundColor = color;
	}

	public void setFormInputStyle(String style) {
		this.formInputStyle = style;
	}

	public void setWidth(String width) {
		this.width = width;
	}
	
	public void setWindowHeaderImage(Image image) {
		this.windowHeaderImage = image;
	}
	
	protected TravelSessionManager getTravelSessionManager(IWUserContext iwuc) throws RemoteException {
		return (TravelSessionManager) IBOLookup.getSessionInstance(iwuc, TravelSessionManager.class);
	}
	
}
