package is.idega.idegaweb.campus.block.building.presentation;
import is.idega.idegaweb.campus.block.building.data.ApartmentTypeRent;
import is.idega.idegaweb.campus.block.building.data.ApartmentTypeRentHome;
import is.idega.idegaweb.campus.presentation.CampusBlock;


import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.data.ApartmentType;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOStoreException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;
/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */
public class AprtTypeRentSetter extends CampusBlock {
	
	private boolean isAdmin = false;
	private String prmATid = "AT_id";
	private String prmATRid = "ATR_id";
	
	public String getLocalizedNameKey() {
		return "rent";
	}
	public String getLocalizedNameValue() {
		return "rent";
	}
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	protected void control(IWContext iwc) {
		//debugParameters(iwc);
		if (isAdmin) {
			if (iwc.isParameterSet("create.x") || iwc.isParameterSet("create")) {
				createRent(iwc);
			}
			else if (iwc.isParameterSet("update.x") || iwc.isParameterSet("update")) {
				updateRent(iwc);
			}
			else if (iwc.isParameterSet("delete.x") || iwc.isParameterSet("delete")) {
				deleteRent(iwc);
			}
			if (iwc.isParameterSet(prmATid)) {
				add(getTypeRentForm(iwc));
			}
			else
				this.add(getTypeTable());
		}
		else
			this.add(getNoAccessObject(iwc));
	}
	public PresentationObject getTypeTable() {
		DataTable T = new DataTable();
		T.addTitle(localize("apartment_types", "Apartment types"));
		T.setTitlesVertical(false);
		List Types = BuildingCacher.getTypes();
		if (Types != null) {
			Iterator iter = Types.iterator();
			ApartmentType AT;
			T.add(getHeader(localize("apartment_type", "Apartment type")), 1, 1);
			int row = 2;
			while (iter.hasNext()) {
				AT = (ApartmentType) iter.next();
				Link link = new Link(getText(AT.getName()));
				link.addParameter(prmATid, AT.getPrimaryKey().toString());
				T.add(link, 1, row++);
			}
		}
		return T;
	}
	public PresentationObject getTypeRentForm(IWContext iwc) {
		Form F = new Form();
		DataTable T = new DataTable();
		T.setWidth(Table.HUNDRED_PERCENT);
		Table inputTable = new Table(4, 2);
		try {
			String ATid = iwc.getParameter(prmATid);
			String ATRid = iwc.getParameter(prmATRid);
			if (ATid != null) {
				int atID = Integer.parseInt(ATid);
				ApartmentType AT = BuildingCacher.getApartmentType(atID);
				T.addTitle(AT.getName());
				T.setTitlesVertical(false);
				T.add(getHeader(localize("rent", "Rent")), 1, 1);
				T.add(getHeader(localize("from_date", "From date (D/M/Y)")), 2, 1);
				T.add(getHeader(localize("to_date", "To date (D/M/Y)")), 3, 1);
				T.add(getHeader(localize("choise", "Choice")), 4, 1);
				int row = 2;
				Collection atrs = getAPRHome().findByType(atID);
				NumberFormat nf = NumberFormat.getInstance();
				DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, iwc.getCurrentLocale());
				RadioButton rb;
				if (atrs != null) {
					Iterator iter = atrs.iterator();
					while (iter.hasNext()) {
						ApartmentTypeRent theRent = (ApartmentTypeRent) iter.next();
						T.add(getText(String.valueOf((double) theRent.getRent())), 1, row);
						T.add(getText(df.format(theRent.getValidFrom())), 2, row);
						if (theRent.getValidTo() != null)
							T.add(getText(df.format(theRent.getValidFrom())), 3, row);
						rb = new RadioButton(prmATRid, theRent.getPrimaryKey().toString());
						if(theRent.getPrimaryKey().toString().equals(ATRid))
							rb.setSelected();
						T.add(rb, 4, row);
						row++;
					}
				}
				T.add(new HiddenInput(prmATid, ATid));
				Link btnNew = new Link(localize("btn_new","New"));
				btnNew.addParameter(prmATid,ATid);
				SubmitButton edit = new SubmitButton(getResourceBundle().getLocalizedImageButton("btn_edit", "Edit"), "edit");
				SubmitButton delete = new SubmitButton(getResourceBundle().getLocalizedImageButton("btn_delete", "Delete"), "delete");
				SubmitButton create = new SubmitButton(getResourceBundle().getLocalizedImageButton("btn_create", "Create"), "create");
				SubmitButton update = new SubmitButton(getResourceBundle().getLocalizedImageButton("btn_update", "Update"), "update");
				T.addButton(btnNew);
				T.addButton(edit);
				T.addButton(delete);
				
				TextInput rent = new TextInput("apr_rent");
				DateInput from = new DateInput("apr_from");
				DateInput to = new DateInput("apr_to");
				inputTable.add(getHeader(localize("rent", "Rent")), 1, 1);
				inputTable.add(getHeader(localize("from_date", "From date (D/M/Y)")), 2, 1);
				inputTable.add(getHeader(localize("to_date", "To date (D/M/Y)")), 3, 1);
				//inputTable.add(Edit.formatText(localize("choise", "Choice")), 4, 1);
				inputTable.add(rent, 1, 2);
				inputTable.add(from, 2, 2);
				inputTable.add(to, 3, 2);
				if (iwc.isParameterSet(prmATRid)) {
					
					if (ATRid != null) {
						ApartmentTypeRent apr = getAPRHome().findByPrimaryKey(ATRid);
						rent.setContent(String.valueOf(apr.getRent()));
						from.setDate(apr.getValidFrom());
						if (apr.getValidTo() != null)
							to.setDate(apr.getValidTo());
					}
					inputTable.add(update, 4, 2);
				}
				else {
					inputTable.add(create, 4, 2);
				}
			}
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (EJBException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		F.add(T);
		F.add(inputTable);
		return F;
	}
	private boolean updateRent(IWContext iwc) {
		try {
			if (iwc.isParameterSet(prmATRid )&&iwc.isParameterSet("apr_rent") && iwc.isParameterSet("apr_from")) {
				Integer ATid = new Integer(iwc.getParameter(prmATid));
				Integer ATRid = new Integer(iwc.getParameter(prmATRid));
				Float rent = new Float(iwc.getParameter("apr_rent"));
				IWTimestamp from = new IWTimestamp(iwc.getParameter("apr_from"));
				IWTimestamp to = null;
				if (iwc.isParameterSet("apr_to"))
					to = new IWTimestamp(iwc.getParameter("apr_to"));
				ApartmentTypeRent typeRent = getAPRHome().findByPrimaryKey(ATRid);
				typeRent.setApartmentTypeId(ATid.intValue());
				typeRent.setRent(rent);
				typeRent.setValidFrom(from.getDate());
				if (to != null)
					typeRent.setValidTo(to.getDate());
				typeRent.store();
				return true;
			}
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return false;
	}
	private boolean createRent(IWContext iwc) {
		try {
			if (iwc.isParameterSet("apr_rent") && iwc.isParameterSet("apr_from")) {
				Integer ATid = new Integer(iwc.getParameter(prmATid));
				Float rent = new Float(iwc.getParameter("apr_rent"));
				IWTimestamp from = new IWTimestamp(iwc.getParameter("apr_from"));
				IWTimestamp to = null;
				if (iwc.isParameterSet("apr_to"))
					to = new IWTimestamp(iwc.getParameter("apr_to"));
				ApartmentTypeRent typeRent = getAPRHome().create();
				typeRent.setApartmentTypeId(ATid.intValue());
				typeRent.setRent(rent);
				typeRent.setValidFrom(from.getDate());
				if (to != null)
					typeRent.setValidTo(to.getDate());
				typeRent.store();
				return true;
			}
		}
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (IDOStoreException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
		return false;
	}
	private boolean deleteRent(IWContext iwc) {
		try {
			if (iwc.isParameterSet(prmATRid)) {
				String ID = iwc.getParameter(prmATRid);
				if (ID != null) {
					getAPRHome().findByPrimaryKey(ID).remove();
					return true;
				}
			}
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (EJBException e) {
			e.printStackTrace();
		}
		catch (RemoveException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return false;
	}
	public ApartmentTypeRentHome getAPRHome() throws IDOLookupException {
		return (ApartmentTypeRentHome) IDOLookup.getHome(ApartmentTypeRent.class);
	}
	public void main(IWContext iwc) {
		isAdmin = iwc.hasEditPermission(this);
		control(iwc);
	}
}
