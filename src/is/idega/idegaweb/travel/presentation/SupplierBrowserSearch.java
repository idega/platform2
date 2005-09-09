/*
 * $Id: SupplierBrowserSearch.java,v 1.8 2005/09/09 16:16:52 gimmi Exp $
 * Created on Aug 16, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.business.SupplierBrowserBusiness;
import is.idega.idegaweb.travel.business.SupplierBrowserBusinessBean;
import is.idega.idegaweb.travel.data.SupplierBrowserSearchForm;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.text.Heading2;
import com.idega.presentation.text.Paragraph;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.ResetButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.util.IWTimestamp;
import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLParser;


public class SupplierBrowserSearch extends TravelBlock {

	private static final String PARAMETER_FORM = "sbs_prm_f";
	
	private Group supplierManager = null;
	
	private String maindiv = "bookingform";
	private String inputdiv = "sbs_input_div";
	private String separatordiv = "sbs_separator_div";
	private String engineDefinitionFile = null;
	private String currentForm = null;
	private String formDropdownStyleClass = null;
	
	private String CACHE_KEY = "sbs_ck";
	private int supplierManagerId = -1;
	private File engineXML = null;
	private String engineName = null;
	private XMLElement engineHeading = null;
	private String engineStyleClass =  null;
	private String defaultSearchForm = null;
	private String defaultParameterName = null;
	private String defaultParameterValue = null;

	
	public SupplierBrowserSearch() {
		setCacheable(getCacheKey(),0);
		maindiv = "bookingform box";
	}
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		if (engineDefinitionFile != null) {
			
			if (supplierManagerId > 0) {
				GroupHome gHome = (GroupHome) IDOLookup.getHome(Group.class, super.getDatasource());
				supplierManager = gHome.findByPrimaryKey(new Integer(supplierManagerId));
			}

			if (supplierManager != null) {
				addSearchForm(iwc);
			} else {
				add(getResourceBundle().getLocalizedString("travel.supplier_manager_not_found","Supplier manager not found."));
			}
		} else {
			add(getResourceBundle().getLocalizedString("travel.engine_deinition_not_found","Engine definition not found."));
		}
	}
	
	protected boolean isCacheable(IWContext iwc) {
		return true;
//		getCacheState(iwc, "removeme please");
//		return false;
	}
	public String getCacheKey(){
		return CACHE_KEY;
	}
	
	protected String getCacheState(IWContext iwc, String cacheStatePrefix){
		String returnString = "";
		try {
			if (engineDefinitionFile != null) {
				returnString += engineDefinitionFile;
				engineXML = new File(engineDefinitionFile);
				XMLParser parser = new XMLParser();
				XMLDocument doc = parser.parse(engineXML);
				XMLElement el = doc.getRootElement();
				engineName = el.getAttributeValue(SupplierBrowserBusinessBean.ATTRIBUTE_NAME);
				engineHeading = el.getChild(SupplierBrowserBusinessBean.ELEMENT_HEADING);
				engineStyleClass = el.getAttributeValue(SupplierBrowserBusinessBean.ATTRIBUTE_STYLE_CLASS);
				XMLElement sfChild = el.getChild(SupplierBrowserBusinessBean.ELEMENT_SEARCH_FORM);
				if (sfChild != null) {
					currentForm = (sfChild).getAttributeValue(SupplierBrowserBusinessBean.ATTRIBUTE_LOCALIZATION_KEY);
				}
				formDropdownStyleClass = sfChild.getAttributeValue(SupplierBrowserBusinessBean.ATTRIBUTE_STYLE_CLASS);
	
				String form = iwc.getParameter(PARAMETER_FORM);
				if (form != null) {
					currentForm = form;
				} else if (defaultSearchForm != null){
					currentForm = defaultSearchForm;
				}
				Collection coll = getSupplierBrowserBusiness(iwc).getParameters(engineName, currentForm);
				if (coll != null) {
					Iterator iter = coll.iterator();
					while (iter.hasNext()) {
						String p = (String) iter.next();
						returnString+= "_"+iwc.getParameter(p);
					}
				}
				returnString += engineName+"_"+currentForm;
				returnString += defaultParameterName+"_"+defaultParameterValue;
			} else {
				returnString += Double.toString(Math.random());
				System.out.println("[SupplierBrowserSerach] CacheState = "+returnString);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return  cacheStatePrefix+returnString+IWTimestamp.RightNow().toSQLDateString();
	}
	
	private void addSearchForm(IWContext iwc) throws Exception {

		Collection forms = getSupplierBrowserBusiness(iwc).parseXML(supplierManager, engineXML);
		
		SupplierBrowserSearchForm sf = null;
		DropdownMenu formSelector = new DropdownMenu(PARAMETER_FORM);
		formSelector.setToSubmit();

		Iterator fIter = forms.iterator();
		while (fIter.hasNext()) {
			SupplierBrowserSearchForm tmp = (SupplierBrowserSearchForm) fIter.next();
			formSelector.addMenuElement(tmp.getLocalizationKey(), getResourceBundle().getLocalizedString(tmp.getLocalizationKey(), tmp.getLocalizationKey()));
			if (tmp.getLocalizationKey().equals(currentForm)) {
				sf = tmp;
			}
		}
		formSelector.setSelectedElement(currentForm);
		
		Form form2 = new Form();
		Paragraph p2 = new Paragraph();
		p2.setStyleClass(formDropdownStyleClass);
		Label lab = new Label();
		lab.setLabel(getResourceBundle().getLocalizedString("travel.search_for","Search for"));
		p2.add(lab);
		p2.add(formSelector);
		form2.maintainAllParameters();
		form2.add(p2);

		Collection ps = sf.getParagraphs();
		Form form = new Form();
		form.maintainParameter(PARAMETER_FORM);
		form.addParameter(SupplierBrowser.SHOW_SEARCH_INPUTS, Boolean.toString(false));
		try {
			form.setPageToSubmitTo(Integer.parseInt(sf.getPageID()));
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		Iterator it = ps.iterator();
		while (it.hasNext()) {
			Paragraph p = (Paragraph) it.next();
			form.add(p);
		}

		Paragraph inputLayer = new Paragraph();
		inputLayer.setStyleClass("fi");
		inputLayer.add(new SubmitButton(getResourceBundle().getLocalizedString("search","search")));
		inputLayer.add(new ResetButton(getResourceBundle().getLocalizedString("reset", "reset")));
		form.add(inputLayer);

		Layer mainLayer = new Layer();
		mainLayer.setStyleClass(maindiv);
		if (engineHeading != null) {
			Heading2 h2 = new Heading2();
			String engineHeader = engineHeading.getAttributeValue(SupplierBrowserBusinessBean.ATTRIBUTE_LOCALIZATION_KEY);
			String engineStyle = engineHeading.getAttributeValue(SupplierBrowserBusinessBean.ATTRIBUTE_STYLE_CLASS);
			h2.setStyleClass(engineStyle);
			h2.setText(getResourceBundle().getLocalizedString(engineHeader, engineHeader));
			mainLayer.add(h2);
		}
		
		Layer l2 = new Layer();
		if (engineStyleClass != null) {
			l2.setStyleClass(engineStyleClass);
		}
		
		l2.add(form2);
		l2.add(form);
		mainLayer.add(l2);
		add(mainLayer);
	}
	
	public void setEngineDefinitionFile(String definition) {
		engineDefinitionFile = definition;
	}
	
	public void setSupplierManagerID(int supplierManagerId) throws FinderException, RemoteException {
		this.supplierManagerId = supplierManagerId;
		
	}
	
	public void setDefaultSearchEngine(String defaultSearchForm) {
		this.defaultSearchForm = defaultSearchForm;
	}
	
	public void setDefaultParameter(String  parameterName, String parameterValue) {
		this.defaultParameterName  = parameterName;
		this.defaultParameterValue = parameterValue;
	}
	
	public SupplierBrowserBusiness getSupplierBrowserBusiness(IWContext iwc) {
		try {
			return (SupplierBrowserBusiness) IBOLookup.getServiceInstance(iwc, SupplierBrowserBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
}
