package is.idega.idegaweb.travel.block.search.presentation;

import is.idega.idegaweb.travel.block.search.business.ServiceSearchBusiness;
import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine;
import is.idega.idegaweb.travel.presentation.TravelManager;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.business.IBOLookup;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.CountryHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author gimmi
 */
public class ServiceSearchEditor extends TravelManager {

	private static final String ACTION = "sse_a";
	private static final String ACTION_NEW = "sse_an";
	private static final String ACTION_SAVE = "sse_as";
	private static final String ACTION_EDIT = "sse_ed";
	private static final String ACTION_DELETE = "sse_d";
	private static final String ACTION_SUPPLIERS = "sse_s";
	private static final String ACTION_CLEAR_CACHE = "sse_cc";
	private static final String ACTION_UPDATE_SUPPLIERS = "sse_us";
	
	private static final String PARAMETER_NAME = "sse_prm_n";
	private static final String PARAMETER_CODE = "sse_prm_c";
	private static final String PARAMETER_SSE_ID = "sse_prm_sse_i";
	private static final String PARAMETER_URL = "sse_prm_url";
	private static final String PARAMETER_IN_USE = "sse_prm_iu_";
	private static final String PARAMETER_SUPPLIER_ID = "sse_prm_sid";
	
	IWResourceBundle iwrb;
	ServiceSearchEngine engine;
	
	public String getBundleIdentifier() {
		return super.getBundleIdentifier();
	}

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		init(iwc);
		add(Text.BREAK);
		if (isSupplierManager()) {
			parseAction(iwc);
		} else {
			add(getText(iwrb.getLocalizedString("travel.no_access", "You dont have access to this page.")));
		}
	}
	
	private void init(IWContext iwc) throws RemoteException {
		iwrb = super.getResourceBundle(iwc);
		String engineID = iwc.getParameter(PARAMETER_SSE_ID);
		if (engineID != null) {
			try {
				engine = getBusiness(iwc).getSearchEngineHome().findByPrimaryKey(new Integer(engineID));
			} catch (Exception e) {
				System.out.println();
			}
		}
	}
	
	private void parseAction(IWContext iwc) throws RemoteException {
		String action = iwc.getParameter(ACTION);
		
		if (action == null) {
			displaySearchEngineList(iwc);
		} else if (action.equals(ACTION_NEW) || action.equals(ACTION_EDIT)) {
			displayCreation(iwc);
		} else if (action.equals(ACTION_SAVE)) {
			saveEngine(iwc);
		} else if (action.equals(ACTION_DELETE)) {
			deleteServiceSearchEngine(iwc);
			displaySearchEngineList(iwc);
		} else if (action.equals(ACTION_SUPPLIERS)) {
			suppliers(iwc);
		} else if (action.equals(ACTION_UPDATE_SUPPLIERS)) {
			updateSuppliers(iwc);
			suppliers(iwc);
		} else if (action.equals(ACTION_CLEAR_CACHE)) {
			clearCache(iwc);
			displaySearchEngineList(iwc);
		} else {
			displaySearchEngineList(iwc);
		}
	}
	
	private void clearCache(IWContext iwc) throws RemoteException {
		getBusiness(iwc).clearAllEngineCache();
	}
	
	private void displaySearchEngineList(IWContext iwc) throws RemoteException {
		Collection allEngines = getBusiness(iwc).getServiceSearchEngines(getSupplierManager());
		Table table = super.getTable();
		Form form = new Form();
		form.add(table);
		int row = 1;
		
		table.add(super.getHeaderText(iwrb.getLocalizedString("travel.search.name", "Name")), 1,row);
		table.add(super.getHeaderText(iwrb.getLocalizedString("travel.search.code", "Code")), 2,row);
		table.add(getHeaderText(iwrb.getLocalizedString("travel.search.url", "Url")), 3, row);
		table.add("", 4, row);
		table.add("", 5, row);
		table.add("", 6, row);
		table.setRowColor(row, super.backgroundColor);
		
		if (allEngines != null && !allEngines.isEmpty()) {
			Iterator iter = allEngines.iterator();
			ServiceSearchEngine engine;
			Link edit = new Link();
			Link delete;
			Link supplierLink;
			Link select;
			Link engineLink;
			
			while (iter.hasNext()) {
				++row;
				table.setRowColor(row, super.GRAY);
				engine = (ServiceSearchEngine) iter.next();
				select = new Link(getText(engine.getName()), ServiceSearchAdmin.class);
				select.addParameter(ServiceSearchAdmin.PARAMETER_SERVICE_SEARCH_ENGINE_ID, engine.getPrimaryKey().toString());
				
				supplierLink = new Link(iwrb.getLocalizedImageButton("travel.search.suppliers", "Suppliers"));
				supplierLink.addParameter(ACTION, ACTION_SUPPLIERS);
				supplierLink.addParameter(PARAMETER_SSE_ID, engine.getPrimaryKey().toString());
				
				edit = new Link(iwrb.getLocalizedImageButton("travel.edit", "Edit"));
				edit.addParameter(ACTION, ACTION_EDIT);
				edit.addParameter(PARAMETER_SSE_ID, engine.getPrimaryKey().toString());
				
				delete = new Link(iwrb.getLocalizedImageButton("travel.delete", "Delete"));
				delete.addParameter(ACTION, ACTION_DELETE);
				delete.addParameter(PARAMETER_SSE_ID, engine.getPrimaryKey().toString());
				
				table.add(select, 1, row);
				table.add(getText(engine.getCode()), 2, row);
				if (engine.getURL() != null && !"".equals(engine.getURL().trim())) {
					engineLink = new Link(iwrb.getLocalizedImageButton("travel.search.url", "URL"), engine.getURL());
					engineLink.setTarget(Link.TARGET_NEW_WINDOW);
					table.add(engineLink, 3, row);
				}
				table.add(supplierLink, 4, row);
				table.add(edit, 5, row);
				table.add(delete, 6, row);
			}
		} else {
			table.add("No search engines found", 1, ++row);
			table.setRowColor(row, super.GRAY);
		}
		
		// Buttons
		++row;
		SubmitButton bNew = new SubmitButton(iwrb.getLocalizedImageButton("travel.new","New"), ACTION, ACTION_NEW);
		if (super.isSupplierManager()) {
			table.add(bNew, 1, row);
		}
		table.setRowColor(row, GRAY);
		
		Link clearCache = new Link(iwrb.getLocalizedImageButton("travel.clear_cache", "Clear Cache"));
		clearCache.addParameter(ACTION, ACTION_CLEAR_CACHE);
		table.add(clearCache, 5, row);
		table.setAlignment(5, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.mergeCells(5, row, 6, row);
		
		add(form);
	}
	
	
	private void displayCreation(IWContext iwc) {
		Table table = super.getTable();
		Form form = new Form();
		form.add(table);
		int row = 1;
		
		table.add(super.getHeaderText(iwrb.getLocalizedString("travel.serach.create_new_engine","Create a new Search Engine")), 1, row);
		table.mergeCells(1, row, 2, row);
		table.setRowColor(row, super.backgroundColor);
		
		
		++row;
		TextInput tiName = new TextInput(PARAMETER_NAME);
		table.add(super.getText(iwrb.getLocalizedString("travel.search.name", "Name")), 1,row);
		table.add(tiName, 2, row);
		table.setRowColor(row, GRAY);
		
		++row;
		TextInput tiCode = new TextInput(PARAMETER_CODE);
		table.add(super.getText(iwrb.getLocalizedString("travel.search.code", "Code")), 1,row);
		table.add(tiCode, 2, row);
		table.setRowColor(row, GRAY);
		
		++row;
		TextInput tiUrl = new TextInput(PARAMETER_URL);
		table.add(getText(iwrb.getLocalizedString("travel.url", "URL")), 1,row);
		table.add(tiUrl, 2, row);
		table.setRowColor(row, GRAY);
		
		String pName = iwc.getParameter(PARAMETER_NAME);
		String pCode = iwc.getParameter(PARAMETER_CODE);
		String pUrl = iwc.getParameter(PARAMETER_URL);
		
		String pID = iwc.getParameter(PARAMETER_SSE_ID);
		
		if (pID != null) {
			try {
				ServiceSearchEngine engine = getBusiness(iwc).getSearchEngineHome().findByPrimaryKey(new Integer(pID));
				table.add(new HiddenInput(PARAMETER_SSE_ID, pID), 1, row);
				tiName.setContent(engine.getName());
				tiCode.setContent(engine.getCode());
				if (engine.getURL() != null) {
					tiUrl.setContent(engine.getURL());
				}
			} catch (Exception e) {
				System.err.println("[ServiceSearchEditor] Failed to populate fields");
			}
		}
		
		if (pName != null) {
			tiName.setContent(pName);
			tiCode.setContent(pCode);
			tiUrl.setContent(pUrl);
		}
		
		SubmitButton bSave = new SubmitButton(iwrb.getLocalizedImageButton("travel.save", "Save"), ACTION, ACTION_SAVE);
		Link lCancel = new Link(iwrb.getLocalizedImageButton("travel.cancel", "Cancel"));
		
		++row;
		table.mergeCells(1, row, 2, row);
		table.setRowColor(row, GRAY);
		table.add(bSave,1, row);
		table.add(Text.NON_BREAKING_SPACE, 1, row);
		table.add(lCancel, 1,row);
		
		add(form);
	}
	
	private void suppliers(IWContext iwc) {
		Form form = new Form();
		Table table = getTable();
		form.add(table);
		form.maintainParameter(PARAMETER_SSE_ID);
		int row = 1;

		table.add(getHeaderText(iwrb.getLocalizedString("travel.search.supplier", "Supplier")), 1,row);
		table.add(getHeaderText(iwrb.getLocalizedString("travel.search.use", "Use")), 2,row);
		table.setRowColor(row++, backgroundColor);
		
		try {
			Collection allSupps = getSupplierHome().findAll(getSupplierManager());
			Collection engineSupps = engine.getSuppliers();
			
			if (allSupps != null && engineSupps != null) {
				allSupps.removeAll(engineSupps);
			}
			
			table.add(getHeaderText(iwrb.getLocalizedString("travel.search.used", "Used")), 1, row);
			table.setRowColor(row, backgroundColor);
			table.mergeCells(1, row, 2, row);
			++row;
			
			if ( engineSupps != null && !engineSupps.isEmpty()) {
				row = insertSuppliers(table, row, engineSupps, true);
			} else {
				table.setRowColor(row, GRAY);
				table.add(getText(iwrb.getLocalizedString("travel.search.no_suppliers", "No suppliers")), 1, row++);
			}

			table.add(getHeaderText(iwrb.getLocalizedString("travel.search.unused", "Unused")), 1, row);
			table.setRowColor(row, backgroundColor);
			table.mergeCells(1, row, 2, row);
			++row;
			
			if ( allSupps != null && !allSupps.isEmpty()) {
				row = insertSuppliers(table, row, allSupps, false);
			} else {
				table.setRowColor(row, GRAY);
				table.add(getText(iwrb.getLocalizedString("travel.search.no_suppliers", "No suppliers")), 1, row++);
			}
		
			table.setRowColor(row, GRAY);
			table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
			table.add(new BackButton(iwrb.getLocalizedImageButton("travel.search.back", "Back")), 1, row);
			table.add(new SubmitButton(iwrb.getLocalizedImageButton("travel.search.update", "Update"), ACTION, ACTION_UPDATE_SUPPLIERS), 2, row);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		add(form);
	}
	
	private void updateSuppliers(IWContext iwc) {
		String[] suppIDs = iwc.getParameterValues(PARAMETER_SUPPLIER_ID);
		if (suppIDs != null) {
			try {
				String checkSupp;
				Supplier supp;
				Collection addrs;
				Address address;
				Vector postalCodes = new Vector();
				engine.removeAllSuppliers();
				for (int i = 0; i < suppIDs.length; i++) {
					checkSupp = iwc.getParameter(PARAMETER_IN_USE+suppIDs[i]);
					if (checkSupp != null) {
						supp = getSupplierHome().findByPrimaryKey(new Integer(suppIDs[i]));
						engine.addSupplier(supp);
						
						// Finding postalCodes to use in finding the countries to be used with the engine
						addrs = supp.getAddresses();
						if (addrs != null) {
							Iterator iter = addrs.iterator();
							while (iter.hasNext()) {
								address = (Address) iter.next();
								if (address != null) {
									if (address.getPostalCodeID() > 0) {
										postalCodes.add(address.getPostalCode());
									
									}
								}
							}
						}
						// Finding postalCodes done
						
					}
				}
				
				CountryHome cHome = (CountryHome) IDOLookup.getHome(Country.class);
				Collection countries = cHome.findAllFromPostalCodes(postalCodes);

				engine.setCountries(countries);
			}catch (Exception e) {
				e.printStackTrace(System.err);
			}
		} else {
			System.out.println("[ServiceSearchEditor] no supplierIDs");
		}
	}
	
	private int insertSuppliers(Table table, int row, Collection suppliers, boolean setCheckBox) {
		Supplier supplier;
		CheckBox box;
		Iterator iter = suppliers.iterator();
		while (iter.hasNext()) {
			supplier = (Supplier) iter.next();
			table.setRowColor(row, GRAY);
			box = new CheckBox(PARAMETER_IN_USE+supplier.getPrimaryKey().toString());
			box.setChecked(setCheckBox);
			
			table.add(new HiddenInput(PARAMETER_SUPPLIER_ID, supplier.getPrimaryKey().toString()), 1, row);
			table.add(getText(supplier.getName()), 1, row);
			table.add(box ,2, row++);
		}
		return row;
	}

	private void saveEngine(IWContext iwc) {
		String name = iwc.getParameter(PARAMETER_NAME);
		String code = iwc.getParameter(PARAMETER_CODE);
		String url = iwc.getParameter(PARAMETER_URL);
		String id = iwc.getParameter(PARAMETER_SSE_ID);
		
		boolean isValid = true;
		ServiceSearchEngine engine = null;
		
		if (name != null && code != null) {
			try {
				Integer iID = null;
				boolean proceed = true;
				try {
					iID = new Integer(id);
				} catch (NumberFormatException n) {}
				
				// Checking if name is valid
				engine = getBusiness(iwc).findByName(name);
				if (engine != null) {
					if (iID == null) {
						add(getHeaderText(iwrb.getLocalizedString("travel.name_in_use","Name already is use")+Text.BREAK));
						proceed = false;
					} else {
						if (!iID.equals(engine.getPrimaryKey())) {
							add(getHeaderText(iwrb.getLocalizedString("travel.name_in_use","Name already is use")+Text.BREAK));
							proceed = false;
						}
					}
				}
				// Checking if code is valid
				engine = getBusiness(iwc).findByCode(code);
				if (engine != null) {
					if (iID == null) {
						add(getHeaderText(iwrb.getLocalizedString("travel.code_in_use","Code already is use")+Text.BREAK));
						proceed = false;
					} else {
						if (!iID.equals(engine.getPrimaryKey())) {
							add(getHeaderText(iwrb.getLocalizedString("travel.code_in_use","Code already is use")+Text.BREAK));
							proceed = false;
						}
					}
				}
				
				if (proceed) {
					engine = getBusiness(iwc).storeEngine(iID, name, code, url, getSupplierManager());
					displaySearchEngineList(iwc);
				} else {
					displayCreation(iwc);
				}
			} catch (Exception e) {
				e.printStackTrace();
				add(getHeaderText(iwrb.getLocalizedString("travel.save_failed","Save failed")+Text.BREAK));
				displayCreation(iwc);
			}
		}
	}
	
	private void deleteServiceSearchEngine(IWContext iwc) {
		try {
			getBusiness(iwc).deleteServiceSearchEngine(getBusiness(iwc).getSearchEngineHome().findByPrimaryKey(new Integer(iwc.getParameter(PARAMETER_SSE_ID))), iwc.getCurrentUser());
		}catch (Exception e){
			add(getHeaderText(iwrb.getLocalizedString("travel.delete_failed","Delete failed")+Text.BREAK));
		}
	}
	
	public SupplierHome getSupplierHome() throws IDOLookupException {
		return (SupplierHome) IDOLookup.getHome(Supplier.class);
	}
	
	public ServiceSearchBusiness getBusiness(IWApplicationContext iwac) throws RemoteException {
		return (ServiceSearchBusiness) IBOLookup.getServiceInstance(iwac, ServiceSearchBusiness.class);
	}
	
}
