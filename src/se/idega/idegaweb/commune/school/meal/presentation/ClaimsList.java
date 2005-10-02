/*
 * $Id: ClaimsList.java,v 1.1 2005/10/02 22:12:23 laddi Exp $
 * Created on Oct 2, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.presentation;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import se.idega.idegaweb.commune.school.meal.data.MealChoice;
import com.idega.block.finance.data.AccountEntry;
import com.idega.block.finance.data.AccountEntryBMPBean;
import com.idega.business.IBORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;


/**
 * Last modified: $Date: 2005/10/02 22:12:23 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class ClaimsList extends MealBlock {

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.meal.presentation.MealBlock#present(com.idega.presentation.IWContext)
	 */
	public void present(IWContext iwc) {
		try {
			if (getSession().getSchool() != null) {
				showClaimList(iwc);
			}
			else {
				add(new Text(localize("no_school_found_for_user", "No school found for user")));
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	private void showClaimList(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.setID(STYLENAME_MEAL_FORM);
		
		Table2 table = new Table2();
		table.setWidth("100%");
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setStyleClass(STYLENAME_LIST_TABLE);
		
		Collection choices = getBusiness().getChoicesByClaimStatus(getSession().getSchool());

		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		row.createHeaderCell().add(new Text(localize("claims.name", "Name")));
		row.createHeaderCell().add(new Text(localize("claims.personal_id", "Personal ID")));
		row.createHeaderCell().add(new Text(localize("claims.amount", "Amount")));
		row.createHeaderCell().add(new Text(localize("claims.status", "Status")));
		
		group = table.createBodyRowGroup();
		int iRow = 1;
		
		NumberFormat format = NumberFormat.getCurrencyInstance(iwc.getCurrentLocale());
		float totalAmount = 0;
		Iterator iter = choices.iterator();
		while (iter.hasNext()) {
			row = group.createRow();
			MealChoice choice = (MealChoice) iter.next();
			AccountEntry entry = choice.getAccountEntry();
			User user = choice.getUser();
			Name name = new Name(user.getFirstName(), user.getMiddleName(), user.getLastName());
			
			if (entry != null) {
				String status = entry.getStatus().equals(AccountEntryBMPBean.statusCreated) ? localize("created", "Created") : localize("billed", "Billed");
				try {
					row.createCell().add(new Text(name.getName(iwc.getCurrentLocale())));
					row.createCell().add(new Text(user.getPersonalID() != null ? PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale()) : "-"));
					row.createCell().add(new Text(format.format(entry.getTotal())));
					row.createCell().add(new Text(status));
					totalAmount += entry.getTotal();
					
					if (iRow % 2 == 0) {
						row.setStyleClass(STYLENAME_LIST_TABLE_EVEN_ROW);
					}
					else {
						row.setStyleClass(STYLENAME_LIST_TABLE_ODD_ROW);
					}
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
				iRow++;
			}
		}

		group = table.createFooterRowGroup();
		row = group.createRow();
		TableCell2 cell = row.createCell();
		cell.setColumnSpan(3);
		cell.add(new Text(localize("diners.total", "Total")));
		row.createCell().add(new Text(String.valueOf(totalAmount)));

		form.add(table);
		add(form);
	}
}