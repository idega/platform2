/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.atvr.supplier.application.presentation;

import com.idega.core.user.data.User;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.SubmitButton;

import is.idega.idegaweb.atvr.supplier.application.business.NewProductApplicationBusiness;
import is.idega.idegaweb.atvr.supplier.application.data.NewProductApplication;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class NewProductApplicationAdmin extends Block {
	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.atvr";

	protected final static String PARAM_FORM_SUBMIT = "npaa_submit";

	public void main(IWContext iwc) {
		control(iwc);
	}

	private void control(IWContext iwc) {
		showApplications(iwc);
	}

	private void showApplications(IWContext iwc) {
		try {
			Collection col = getApplicationBusiness(iwc).getAllApplications();
			System.out.println("Got the applications");
			if (col != null) {
				System.out.println("application != null");
				int size = col.size();

				Table t = new Table(5, size + 3);
				t.add("Tegund", 2, 1);
				t.add("Lýsing", 3, 1);
				t.add("Umsókn frá", 4, 1);
				t.add("Dags. umsóknar", 5, 1);

				int i = 2;
				Iterator it = col.iterator();
				while (it.hasNext()) {
					NewProductApplication appl = (NewProductApplication) it.next();
					CheckBox check = new CheckBox();
					t.add(check, 1, i);
					String type = appl.getApplicationType();
					if (type.equals("0"))
						t.add("Reynsla", 2, i);
					else if (type.equals("1"))
						t.add("Sérlisti", 2, i);
					else if (type.equals("2"))
						t.add("Mánaðarfl.", 2, i);
					else if (type.equals("3"))
						t.add("Tóbak", 2, i);

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

				add(t);
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