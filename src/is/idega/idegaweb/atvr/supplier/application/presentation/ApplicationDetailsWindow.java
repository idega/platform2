/*
 * Created on Mar 20, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.atvr.supplier.application.presentation;

import is.idega.idegaweb.atvr.supplier.application.business.NewProductApplicationBusiness;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
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
		
		Form form = new Form();

		if (typeId == TYPE_TRIAL || typeId == TYPE_SPECIAL || typeId == TYPE_MONTH) {
			Table t = new Table(2, 12);
			t.add("Lýsing", 1, 1);
			t.add("Lýsing 2", 1, 2);
			t.add("Millilítrar", 1, 3);
			t.add("Vínstyrkur", 1, 4);
			t.add("Framleiðandi", 1, 5);
			t.add("Upprunaland", 1, 6);
			t.add("Strikamerki", 1, 7);
			t.add("Vöruflokkur", 1, 8);
			t.add("Flokksdeild", 1, 9);
			t.add("Flöskur pr. ks.", 1, 10);

			TextInput desc = new TextInput(PARAM_DESC);
			TextInput desc2 = new TextInput(PARAM_DESC2);
			TextInput qty = new TextInput(PARAM_QUANTITY);
			TextInput str = new TextInput(PARAM_STRENGTH);
			TextInput prod = new TextInput(PARAM_PRODUCER);
			TextInput ctry = new TextInput(PARAM_COUNTRY);
			TextInput bar = new TextInput(PARAM_BAR_CODE);
			TextInput bottles = new TextInput(PARAM_AMOUNT);

			t.add(desc, 2, 1);
			t.add(desc2, 2, 2);
			t.add(qty, 2, 3);
			t.add(str, 2, 4);
			t.add(prod, 2, 5);
			t.add(ctry, 2, 6);
			t.add(bar, 2, 7);
			t.add(bottles, 2, 10);

			String selected = iwc.getParameter(PARAM_FORM_CATEGORY);

			DropdownMenu category = (DropdownMenu) getCategoryDropdown(iwc, selected);
			t.add(category, 2, 8);
			t.add(getSubCategoryDropdown(iwc, selected), 2, 9);

			SubmitButton submit = new SubmitButton(PARAM_FORM_SUBMIT, "Geyma");
			submit.setAsImageButton(true);
			t.setAlignment(2, 12, "Right");
			t.add(submit, 2, 12);

			form.add(t);
		}
		else if (typeId == TYPE_TOBACCO) {
			Table t = new Table(2, 10);
			t.add("Lýsing", 1, 1);
			t.add("Vöruflokkur", 1, 2);
			t.add("Flokksdeild", 1, 3);
			t.add("Framleiðandi", 1, 4);
			t.add("Upprunaland", 1, 5);
			t.add("Tjörumagn", 1, 6);
			t.add("Þyngd tóbaks", 1, 7);
			t.add("Magn kolmónoxíðs",1,8);

			TextInput desc = new TextInput(PARAM_DESC);
			TextInput prod = new TextInput(PARAM_PRODUCER);
			TextInput ctry = new TextInput(PARAM_COUNTRY);
			TextInput amount = new TextInput(PARAM_AMOUNT);
			TextInput weight = new TextInput(PARAM_WEIGHT);
			TextInput monoxide = new TextInput(PARAM_MONOXIDE);
			monoxide.setAsFloat();

			t.add(desc, 2, 1);
			t.add(prod, 2, 4);
			t.add(ctry, 2, 5);
			t.add(amount, 2, 6);
			t.add(weight, 2, 7);
			t.add(monoxide,2,8);

			String selected = iwc.getParameter(PARAM_FORM_CATEGORY);

			DropdownMenu category = (DropdownMenu) getCategoryDropdown(iwc, selected);
			t.add(category, 2, 2);
			t.add(getSubCategoryDropdown(iwc, selected), 2, 3);

			SubmitButton submit = new SubmitButton(PARAM_FORM_SUBMIT, "Geyma");
			submit.setAsImageButton(true);
			t.setAlignment(2, 10, "Right");
			t.add(submit, 2, 10);

			form.add(t);
		}

		add(form);
	}
	
	private PresentationObject getCategoryDropdown(IWContext iwc, String selected) {
		DropdownMenu menu = new DropdownMenu(PARAM_FORM_CATEGORY);
		menu.addMenuElement("01.","Rauðvín");
		menu.addMenuElement("02.","Hvítvín");
		menu.addMenuElement("03.","Rósavín");
		menu.addMenuElement("04.","Freyðivín");
		menu.addMenuElement("05.","Styrkt vín");
		menu.addMenuElement("06.","Ávaxtavín");
		menu.addMenuElement("10.","Brandí");
		menu.addMenuElement("11.","Ávaxtabrandí");
		menu.addMenuElement("13.","Viskí");
		menu.addMenuElement("14.","Romm");
		menu.addMenuElement("15.","Tequila o.fl.");
		menu.addMenuElement("16.","Ókryddað brennivín og vodka");
		menu.addMenuElement("17.","Gin & sénever");
		menu.addMenuElement("18.","Snafs");
		menu.addMenuElement("20.","Líkjör");
		menu.addMenuElement("21.","Bitterar, kryddvín, aperitífar");
		menu.addMenuElement("23.","Blandaðir drykkir");
		menu.addMenuElement("36.","Umbúðir");
		menu.addMenuElement("60.","Lagerbjór");
		menu.addMenuElement("61.","Öl");
		menu.addMenuElement("62.","Aðrar bjórtegundir");
		menu.addMenuElement("89.","Niðurlagðir ávextir");
		menu.addMenuElement("90.","Neftóbak");
		menu.addMenuElement("91.","Reyktóbak");
		menu.addMenuElement("92.","Vindlingar");
		menu.addMenuElement("93.","Vindlar");
		menu.addMenuElement("94.","Munntóbak");
		

		menu.setSelectedElement(selected);

		return menu;
	}

	private PresentationObject getSubCategoryDropdown(IWContext iwc, String category) {
		DropdownMenu menu = new DropdownMenu(PARAM_SUB_CATEGORY);

		menu.addMenuElement("01.1","Rauðvín - stærri en 750 ml");
		menu.addMenuElement("01.10","Rauðvín Argentína");
		menu.addMenuElement("01.11","Rauðvín Chile");
		menu.addMenuElement("01.12","Rauðvín Suður-Afríka");
		menu.addMenuElement("01.13","Rauðvín Ástralía, Nýja Sjáland");
		menu.addMenuElement("01.13.1","Rauðvín Suður-Ástralía");
		menu.addMenuElement("01.2","Rauðvín - minni en 500 ml");
		menu.addMenuElement("01.3","Rauðvín Frakkland");
		menu.addMenuElement("01.3.1","Rauðvín Bordeaux/Bergerac");
		menu.addMenuElement("01.3.1.1","Rauðv. Medoc,Graves,Libournais");
		menu.addMenuElement("01.3.2","Rauðvín Búrgund");
		menu.addMenuElement("01.3.2.1","Rauðvín Beaujolais");
		menu.addMenuElement("01.3.2.2","Cote de Nuits, Cote de Beaune");
		menu.addMenuElement("01.3.3","Rauðvín Rón og Próvens");
		menu.addMenuElement("01.4","Rauðvín Ítalía");
		menu.addMenuElement("01.4.1","Rauðvín Norður-Ítalía");
		menu.addMenuElement("01.4.1.1","Rauðvín Toskana");
		menu.addMenuElement("01.4.1.2","Rauðvín Piemonte");
		menu.addMenuElement("01.4.1.3","Rauðvín Veneto");
		menu.addMenuElement("01.4.2","Rauðvín Suður-Ítalía");
		menu.addMenuElement("01.5","Rauðvín Spánn");
		menu.addMenuElement("01.5.1","Rauðvín Rioja");
		menu.addMenuElement("01.5.2","Rauðvín Katalónía");
		menu.addMenuElement("01.6","Rauðvín Portúgal");
		menu.addMenuElement("01.7","Rauðvín Evrópa annað");
		menu.addMenuElement("01.8","Rauðvín Washington, Oregon");
		menu.addMenuElement("01.9","Rauðvín Kalifornía");
		menu.addMenuElement("01.9.1","Rauðvín Napa og Sonoma");
		menu.addMenuElement("01.99","Rauðvín - önnur");
		menu.addMenuElement("02.1","Hvítvín - stærri en 750 ml");
		menu.addMenuElement("02.10","Hvítvín Kalifornía");
		menu.addMenuElement("02.10.1","Hvítvín Napa og Sonoma");
		menu.addMenuElement("02.11","Hvítvín Chile");
		menu.addMenuElement("02.12","Hvítvín Suður-Afríka");
		menu.addMenuElement("02.13","Hvítvín Ástralía");
		menu.addMenuElement("02.13.1","Hvítvín Nýja Sjáland");
		menu.addMenuElement("02.2","Hvítvín - minni en 500 ml");
		menu.addMenuElement("02.3","Hvítvín Frakkland");
		menu.addMenuElement("02.3.1","Hvítvín Bordeaux");
		menu.addMenuElement("02.3.2","Hvítvín Búrgund");
		menu.addMenuElement("02.3.3","Hvítvín Alsace");
		menu.addMenuElement("02.3.4","Hvítvín Loire");
		menu.addMenuElement("02.4","Hvítvín Ítalía");
		menu.addMenuElement("02.5","Hvítvín Spánn");
		menu.addMenuElement("02.6","Hvítvín Portúgal");
		menu.addMenuElement("02.7","Hvítvín Þýskaland");
		menu.addMenuElement("02.7.1","Hvítvín Riesling - Qmp");
		menu.addMenuElement("02.8","Hvítvín Evrópa annað");
		menu.addMenuElement("02.9","Hvítvín Washington og Oregon");
		menu.addMenuElement("02.90","Hvítvín - sæt");
		menu.addMenuElement("02.99","Hvítvín - önnur");
		menu.addMenuElement("03.1","Rósavín stærri en 750 ml");
		menu.addMenuElement("03.2","Rósavín Blush - Roðavín");
		menu.addMenuElement("03.9","Rósavín - önnur");
		menu.addMenuElement("04.1","Champagne");
		menu.addMenuElement("04.2","Freyðvín Asti");
		menu.addMenuElement("04.9","Freyðivín - önnur");
		menu.addMenuElement("05.1.1","Sérrí - Fino og skyldar tegund");
		menu.addMenuElement("05.1.2","Sérrí - Amontillado og skyldar");
		menu.addMenuElement("05.1.3","Sérrí - Olroso og skyldar teg");
		menu.addMenuElement("05.2.1","Portvín - hvít");
		menu.addMenuElement("05.2.2","Portvín - tunnuþroskuð (Tawny)");
		menu.addMenuElement("05.2.3","Portvín - rauð (Ruby)");
		menu.addMenuElement("05.2.3.1","Portvín - árgangsvín, rauð");
		menu.addMenuElement("05.9","Styrkt vín - önnur");
		menu.addMenuElement("06.1","Síder");
		menu.addMenuElement("06.2","Ávaxtavín");
		menu.addMenuElement("06.3","Hrísgrjónavín");
		menu.addMenuElement("06.9","Ávaxtavín - blöndur");
		menu.addMenuElement("10.1","Cognac VS og VSOP");
		menu.addMenuElement("10.1.1","Cognac - önnur");
		menu.addMenuElement("10.2","Armagnac");
		menu.addMenuElement("10.9","Brandí - önnur");
		menu.addMenuElement("11.1","Calvados");
		menu.addMenuElement("11.2","Annað Ávaxtabrandí");
		menu.addMenuElement("11.3","Hratbrandí / Grappa");
		menu.addMenuElement("13.1","Viskí - Skoskt");
		menu.addMenuElement("13.1.1","Viskí - Skoskt malt");
		menu.addMenuElement("13.2","Viskí - Írskt");
		menu.addMenuElement("13.9","Viskí - önnur");
		menu.addMenuElement("14.1","Hvítt Romm frá Vestur-Indíum");
		menu.addMenuElement("14.2","Ljóst Romm frá Vestur-Indíum");
		menu.addMenuElement("14.3","Dökkt Romm frá Vestur-Indíum");
		menu.addMenuElement("14.9","Romm - önnur, þ.m.t. Kryddromm");
		menu.addMenuElement("15.1","Tequila");
		menu.addMenuElement("16.1","Vodka");
		menu.addMenuElement("16.2","Annað ókryddað brennivín");
		menu.addMenuElement("17.1","Gin");
		menu.addMenuElement("17.2","Sénever");
		menu.addMenuElement("18.1","Akvavít");
		menu.addMenuElement("18.2","Anís");
		menu.addMenuElement("18.9","Aðrir Snafsar");
		menu.addMenuElement("20.1","Rjómalíkjör");
		menu.addMenuElement("20.2","Hnetu og baunalíkjör");
		menu.addMenuElement("20.2.1","Kaffi/Kakólíkjör");
		menu.addMenuElement("20.2.2","Kókoslíkjör");
		menu.addMenuElement("20.3","Grasa og Kryddlíkjör");
		menu.addMenuElement("20.3.1","Mintulíkjör");
		menu.addMenuElement("20.3.2","Líkjör með anísbragði");
		menu.addMenuElement("20.4","Ávaxtalíkjör");
		menu.addMenuElement("20.4.1","Epla/Perulíkjör");
		menu.addMenuElement("20.4.2","Ferskju/Apríkóskulíkjör");
		menu.addMenuElement("20.4.3","Sítruslíkjör");
		menu.addMenuElement("20.4.4","Berjalíkjör");
		menu.addMenuElement("20.9","Aðrir líkjörar");
		menu.addMenuElement("21.1","Bitter");
		menu.addMenuElement("21.2","Kryddvín");
		menu.addMenuElement("21.2.1","Kryddvín - Vermút");
		menu.addMenuElement("21.3","Apertífar");
		menu.addMenuElement("23.1","Blandaðir drykkir - undir 6,5%");
		menu.addMenuElement("23.9","Blandaðir drykkir - aðrir");
		menu.addMenuElement("60.1","Lager - ljós í flöskum");
		menu.addMenuElement("60.1.1","Lager - ljós í fl. íslenskur");
		menu.addMenuElement("60.2","Lager - ljós í dósum");
		menu.addMenuElement("60.2.1","Lager - ljós í dósum íslenskur");
		menu.addMenuElement("60.3","Lager - millidökkur/dökkur");
		menu.addMenuElement("60.4","Lager - sterkur a.m.k. 6,2%");
		menu.addMenuElement("60.9","Lager - annar");
		menu.addMenuElement("61.1","Öl - Belgía");
		menu.addMenuElement("61.2","Öl - Bretland og Írland");
		menu.addMenuElement("61.3","Öl - Þýskaland");
		menu.addMenuElement("61.4","Öl - Stát og portari");
		menu.addMenuElement("61.9","Öl - annað");
		menu.addMenuElement("62.1","Hveitibjór");
		menu.addMenuElement("62.2","Lambik");
		menu.addMenuElement("62.9","Annar bjór");
		menu.addMenuElement("92.1","Vindlingar - tjara 0 til 4 mg");
		menu.addMenuElement("92.2","Vindlingar - tjara 5 til 7 mg");
		menu.addMenuElement("92.3","Vindlingar - tjara yfir 7 mg");
		menu.addMenuElement("93.1","Vindlar tóbak < 3,15");
		menu.addMenuElement("93.2","Vindlar tóbak 3,15 - 4,25");
		menu.addMenuElement("93.3","Vindlar tóbak > 4,25");

		return menu;
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
