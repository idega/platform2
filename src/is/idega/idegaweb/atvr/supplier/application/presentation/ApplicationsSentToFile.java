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

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ApplicationsSentToFile extends Block {
	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.atvr";

	public void main(IWContext iwc) {
		control(iwc);
	}

	private void control(IWContext iwc) {
		showApplications(iwc);
	}
	

	private void showApplications(IWContext iwc) {
		try {
			Collection col = getApplicationBusiness(iwc).getAllSentToFileApplications();
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

				add(t);
			}
			else {
				add("Engar sendar umsóknir");
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