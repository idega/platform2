/*
 * Created on Mar 20, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.atvr.supplier.application.presentation;

import is.idega.idegaweb.atvr.supplier.application.business.NewProductApplicationBusiness;
import is.idega.idegaweb.atvr.supplier.application.data.NewProductApplication;
import is.idega.idegaweb.atvr.supplier.application.data.NewProductApplicationHome;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Window;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ApplicationDetailsWindow extends Window {
	private final static int ACTION_VIEW_FORM = 0;
	private final static int ACTION_SUBMIT_FORM = 1;

	private final static int TYPE_TRIAL = 0;
	private final static int TYPE_SPECIAL = 1;
	private final static int TYPE_MONTH = 2;
	private final static int TYPE_TOBACCO = 3;

	private final static String PARAM_FORM_TYPE = "npa_type";
	private final static String PARAM_FORM_CATEGORY = "npa_cat";
	private final static String PARAM_FORM_SUBMIT = "npa_submit";
	private final static String PARAM_FORM_SUBMIT_X = "npa_submit.x";
	private final static String PARAM_FORM_SUBMIT_Y = "npa_submit.y";

	private final static String PARAM_DESC = "npa_desc";
	private final static String PARAM_DESC2 = "npa_desc2";
	private final static String PARAM_QUANTITY = "npa_qty";
	private final static String PARAM_STRENGTH = "npa_str";
	private final static String PARAM_PRODUCER = "npa_prdc";
	private final static String PARAM_COUNTRY = "npa_ctr";
	private final static String PARAM_BAR_CODE = "npa_bar";
	private final static String PARAM_SUB_CATEGORY = "npa_sub_cat";
	private final static String PARAM_AMOUNT = "npa_amount";
	private final static String PARAM_WEIGHT = "npa_weigth";
	private final static String PARAM_MONOXIDE = "npa_monoxide";

	private String _type = null;
	private String _id = null;

	private int parseType(String type) {
		if (type == null)
			return TYPE_TRIAL;

		return Integer.parseInt(type);
	}

	private void showForm(IWContext iwc) {
		int typeId = parseType(_type);
		NewProductApplication appl = null;
		String subCat = null;
		try {
			appl = ((NewProductApplicationHome) com.idega.data.IDOLookup.getHome(NewProductApplication.class)).findByPrimaryKey(Integer.valueOf(_id));
			subCat = appl.getProductCategory().getDescription();
		}
		catch (Exception e) {
		}

		Form form = new Form();

		if (appl != null) {
			if (typeId == TYPE_TRIAL || typeId == TYPE_SPECIAL || typeId == TYPE_MONTH) {
				Table t = new Table(2, 12);
				t.add("Lýsing", 1, 1);
				t.add("Lýsing 2", 1, 2);
				t.add("Millilítrar", 1, 3);
				t.add("Vínstyrkur", 1, 4);
				t.add("Framleiðandi", 1, 5);
				t.add("Upprunaland", 1, 6);
				t.add("Strikamerki", 1, 7);
				//			t.add("Vöruflokkur", 1, 8);
				t.add("Flokksdeild", 1, 9);
				t.add("Flöskur pr. ks.", 1, 10);

//				TextInput desc = new TextInput(PARAM_DESC);
//				TextInput desc2 = new TextInput(PARAM_DESC2);
//				TextInput qty = new TextInput(PARAM_QUANTITY);
//				TextInput str = new TextInput(PARAM_STRENGTH);
//				TextInput prod = new TextInput(PARAM_PRODUCER);
//				TextInput ctry = new TextInput(PARAM_COUNTRY);
//				TextInput bar = new TextInput(PARAM_BAR_CODE);
//				TextInput bottles = new TextInput(PARAM_AMOUNT);

				t.add(appl.getDescription(), 2, 1);
				t.add(appl.getDescription2(), 2, 2);
				t.add(appl.getQuantity(), 2, 3);
				t.add(appl.getStrength(), 2, 4);
				t.add(appl.getProducer(), 2, 5);
				t.add(appl.getCountryOfOrigin(), 2, 6);
				t.add(appl.getBarCode(), 2, 7);
				t.add(appl.getAmount(), 2, 10);

//				String selected = iwc.getParameter(PARAM_FORM_CATEGORY);

				//			DropdownMenu category = (DropdownMenu) getCategoryDropdown(iwc, selected);
				//			t.add(category, 2, 8);
				t.add(subCat, 2, 9);

				//			SubmitButton submit = new SubmitButton(PARAM_FORM_SUBMIT, "Geyma");
				//			submit.setAsImageButton(true);
				//			t.setAlignment(2, 12, "Right");
				//			t.add(submit, 2, 12);

				form.add(t);
			}
			else if (typeId == TYPE_TOBACCO) {
				Table t = new Table(2, 10);
				t.add("Lýsing", 1, 1);
				//			t.add("Vöruflokkur", 1, 2);
				t.add("Flokksdeild", 1, 3);
				t.add("Framleiðandi", 1, 4);
				t.add("Upprunaland", 1, 5);
				t.add("Tjörumagn", 1, 6);
				t.add("Þyngd tóbaks", 1, 7);
				t.add("Magn kolmónoxíðs", 1, 8);

				//			TextInput desc = new TextInput(PARAM_DESC);
				//			TextInput prod = new TextInput(PARAM_PRODUCER);
				//			TextInput ctry = new TextInput(PARAM_COUNTRY);
				//			TextInput amount = new TextInput(PARAM_AMOUNT);
				//			TextInput weight = new TextInput(PARAM_WEIGHT);
				//			TextInput monoxide = new TextInput(PARAM_MONOXIDE);
				//			monoxide.setAsFloat();

				t.add(appl.getDescription(), 2, 1);
				t.add(appl.getProducer(), 2, 4);
				t.add(appl.getCountryOfOrigin(), 2, 5);
				t.add(appl.getAmount(), 2, 6);
				t.add(appl.getWeigth(), 2, 7);
				t.add(Float.toString(appl.getCarbonMonoxide()), 2, 8);

//				String selected = iwc.getParameter(PARAM_FORM_CATEGORY);

				//			DropdownMenu category = (DropdownMenu) getCategoryDropdown(iwc, selected);
				//			t.add(category, 2, 2);
				t.add(subCat, 2, 3);

				//			SubmitButton submit = new SubmitButton(PARAM_FORM_SUBMIT, "Geyma");
				//			submit.setAsImageButton(true);
				//			t.setAlignment(2, 10, "Right");
				//			t.add(submit, 2, 10);

				form.add(t);
			}
		}

		add(form);
	}


	private NewProductApplicationBusiness getApplicationBusiness(IWContext iwc) throws Exception {
		return (NewProductApplicationBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, NewProductApplicationBusiness.class);
	}

	public void main(IWContext iwc) {
		_type = iwc.getParameter("app_type");
		_id = iwc.getParameter("app_id");

		add("Details gluggi");
		showForm(iwc);
	}
}
