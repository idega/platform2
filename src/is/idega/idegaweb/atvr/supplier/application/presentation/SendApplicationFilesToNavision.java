/*
 * $Id$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.atvr.supplier.application.presentation;

import is.idega.idegaweb.atvr.supplier.application.business.NewProductApplicationBusiness;
import is.idega.idegaweb.atvr.supplier.application.data.NewProductApplication;
import is.idega.idegaweb.atvr.supplier.application.data.ProductCategory;

import java.io.FileWriter;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.core.user.data.User;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class SendApplicationFilesToNavision extends Block {
	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.atvr";

	protected final static String PARAM_FORM_SUBMIT = "npaa_submit";

	protected final static String FILE_LOCATION_PARAMETER = "npaa_file_location";

	public void main(IWContext iwc) {
		control(iwc);
	}

	private void control(IWContext iwc) {
		if (iwc.isParameterSet(PARAM_FORM_SUBMIT))
			sendApplications(iwc);
		showApplications(iwc);
	}
	
	private void sendApplications(IWContext iwc) {
		try {
			Collection col = getApplicationBusiness(iwc).getAllConfirmedApplications();
			
			if (col != null) {
				IWBundle bundle = getBundle(iwc);
				String fileLocation = bundle.getProperty(FILE_LOCATION_PARAMETER);
				if (fileLocation == null || fileLocation.equals("")) {
					add("Engin skráarstaðsetning skilgreind í bundle");
					return;	
				}
				
				IWTimestamp now = new IWTimestamp();
				String fileName = "n_" + now.getDateString("ddMMyyHHmmss") + ".csv";
				
				FileWriter writer = new FileWriter(fileLocation + fileName);
				Iterator it = col.iterator();
				while (it.hasNext()) {
					NewProductApplication appl = (NewProductApplication) it.next();
					StringBuffer line = new StringBuffer();
					String type = appl.getApplicationType();
					if (type.equals("0"))   
						line.append("R"); //Tegund umsóknar
					else if (type.equals("1"))
						line.append("S"); //Tegund umsóknar
					else if (type.equals("2"))
						line.append("M"); //Tegund umsóknar
					else if (type.equals("3"))
						line.append("T"); //Tegund umsóknar
					line.append(";");
					User suppl = appl.getSupplier();
					String ssn = suppl.getPersonalID();
					if (ssn != null) {
						ssn = ssn.substring(0,6) + "-" + ssn.substring(6);
						line.append(ssn); //kennitala birgja
					}
					line.append(";");
					line.append(""); //vörunúmer
					line.append(";");
					line.append(""); //vörunúmer gamla
					line.append(";");
					String desc = appl.getDescription();
					if (desc != null)
						line.append(desc); //Lýsing
					line.append(";");
					String desc2 = appl.getDescription2();
					if (desc2 != null)
						line.append(desc2); //Lýsing 2
					line.append(";");
					String qty = appl.getQuantity();
					if (qty != null)
						line.append(qty); //Magn (ml)
					else
						line.append("0");
					line.append(";");
					String str = appl.getStrength();
					if (str != null)
						line.append(str); //Styrkur(%)
					else
						line.append("0");
					line.append(";");
					String prod = appl.getProducer();
					if (prod != null)
						line.append(prod); //Framleiðandi
					line.append(";");
					String cntr = appl.getCountryOfOrigin();
					if (cntr != null)
						line.append(cntr); //Framleiðsluland
					line.append(";");
					String bar = appl.getBarCode();
					if (bar != null)
						line.append(bar); //Strikamerki
					line.append(";");
					ProductCategory cat = null;
					if (appl.getProductCategoryId() != -1)
						cat = appl.getProductCategory();
					if (cat != null) {
						line.append(cat.getCategory()); //Flokksdeild
					}
					line.append(";");
					String amnt = appl.getAmount();
					if (amnt != null && !type.equals("3"))
						line.append(amnt); //Fjöldi í kassa
					else
						line.append("0");
					line.append(";");
					IWTimestamp sent = new IWTimestamp(appl.getApplicationSent());
					line.append(sent.getDateString("dd.MM.yy")); //Dags. umsóknar
					line.append(";");
					String tar = appl.getAmount();
					if (tar != null && type.equals("3"))
						line.append(tar); //Magn tjöru
					else
						line.append("0");
					line.append(";");
					String tobw = appl.getWeigth();
					if (tobw != null && type.equals("3"))
						line.append(tobw); //Þyngd tóbaks
					else
						line.append("0");
					line.append(";");
					float price = appl.getPrice();
					if (price != -1)
						line.append(Float.toString(price)); //Verð
					else
						line.append("0");
					line.append(";");
					String supplprodid = appl.getSuppliersProductId();
					if (supplprodid != null)
						line.append(supplprodid); //Vörunúmer birgja
					line.append(";");
					float mono = appl.getCarbonMonoxide();
					if (mono != -1 && type.equals("3"))
						line.append(Float.toString(mono)); //Koltvísýringur
					else
						line.append("0");
					line.append("\r\n");
					
					writer.write(line.toString());
				}
				
				writer.close();
			}
			
			getApplicationBusiness(iwc).markApplicationsAsSent(col);
			
			add("Skrá send");
			
			return;
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}

	private void showApplications(IWContext iwc) {
		try {
			Collection col = getApplicationBusiness(iwc).getAllConfirmedApplications();
			if (col != null) {
				int size = col.size();

				Table t = new Table(4, size + 3);
				t.add("Tegund", 1, 1);
				t.add("Lýsing", 2, 1);
				t.add("Umsókn frá", 3, 1);
				t.add("Dags. umsóknar", 4, 1);

				int i = 2;
				Iterator it = col.iterator();
				while (it.hasNext()) {
					NewProductApplication appl = (NewProductApplication) it.next();
					String type = appl.getApplicationType();
					if (type.equals("0"))
						t.add("Reynsla", 1, i);
					else if (type.equals("1"))
						t.add("Sérlisti", 1, i);
					else if (type.equals("2"))
						t.add("Mánaðarfl.", 1, i);
					else if (type.equals("3"))
						t.add("Tóbak", 1, i);

					t.add(appl.getDescription(), 2, i);
					User supplier = appl.getSupplier();
					t.add(supplier.getName(), 3, i);
					t.add(appl.getApplicationSent().toString(), 4, i);
					i++;
				}

				SubmitButton submit = new SubmitButton(PARAM_FORM_SUBMIT, "Senda");
				submit.setAsImageButton(true);
				t.setAlignment(4, size + 3, "Right");
				t.add(submit, 4, size + 3);

				add(t);
			}
			else {
				this.add("Engar ósendar umsóknir");
			}
			return;
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		add("Unable to get applications");
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	private NewProductApplicationBusiness getApplicationBusiness(IWContext iwc) throws Exception {
		return (NewProductApplicationBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, NewProductApplicationBusiness.class);
	}
}