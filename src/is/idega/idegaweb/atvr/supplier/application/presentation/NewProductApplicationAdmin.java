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

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.core.user.data.User;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class NewProductApplicationAdmin extends Block {
	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.atvr";

	protected final static String PARAM_FORM_SUBMIT = "npaa_submit";
	protected final static String PARAM_CHECKBOX = "npaa_checkbox";

	public void main(IWContext iwc) {
		control(iwc);
	}

	private void control(IWContext iwc) {
		if (iwc.isParameterSet(PARAM_FORM_SUBMIT))
			confirmApplications(iwc);
		showApplications(iwc);
	}

	private void confirmApplications(IWContext iwc) {
		String values[] = iwc.getParameterValues(PARAM_CHECKBOX);
		
		if (values != null && values.length > 0) {
			try {
				getApplicationBusiness(iwc).confirmApplications(values);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void showApplications(IWContext iwc) {
		try {
			Collection col = getApplicationBusiness(iwc).getAllUnconfirmedApplications();
			if (col != null) {
				int size = col.size();

				Form f = new Form();

				Table t = new Table(5, size + 3);
				t.add("Tegund", 2, 1);
				t.add("Lýsing", 3, 1);
				t.add("Umsókn frá", 4, 1);
				t.add("Dags. umsóknar", 5, 1);

				int i = 2;
				Iterator it = col.iterator();
				int j = 0;
				while (it.hasNext()) {
					j++;
					NewProductApplication appl = (NewProductApplication) it.next();
					CheckBox check = new CheckBox(PARAM_CHECKBOX,((Integer)appl.getPrimaryKey()).toString());
					t.add(check, 1, i);
					String type = appl.getApplicationType();
					Link typeLink = new Link();
					if (type.equals("0"))
						typeLink.setText("Reynsla");
					else if (type.equals("1"))
					typeLink.setText("Sérlisti");
					else if (type.equals("2"))
						typeLink.setText("Mánaðarfl.");
					else if (type.equals("3"))
						typeLink.setText("Tóbak");
						
					typeLink.addParameter("app_type",type);
					typeLink.addParameter("app_id",((Integer)appl.getPrimaryKey()).intValue());
					typeLink.setWindowToOpen(ApplicationDetailsWindow.class);
						
					t.add(typeLink,2,i);

					t.add(appl.getDescription(), 3, i);
					User supplier = appl.getSupplier();
					t.add(supplier.getName(), 4, i);
					t.add(appl.getApplicationSent().toString(), 5, i);
					i++;
				}

				SubmitButton submit = new SubmitButton(PARAM_FORM_SUBMIT, "Stadfesta");
				submit.setAsImageButton(true);
				t.setAlignment(5, size + 3, "Right");
				t.add(submit, 5, size + 3);

				f.add(t);

				add(f);
			}
			else {
				this.add("Engar nýjar umsóknir");
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