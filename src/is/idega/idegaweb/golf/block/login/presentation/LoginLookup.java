/*
 * $Id: LoginLookup.java,v 1.1 2005/06/03 15:06:57 gimmi Exp $
 * Created on 3.6.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.block.login.presentation;

import is.idega.idegaweb.golf.access.LoginTable;
import is.idega.idegaweb.golf.access.LoginTableHome;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.presentation.GolfBlock;
import javax.ejb.FinderException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;


public class LoginLookup extends GolfBlock {

	private final String PARAMETER_KT = "pm_kt";
	
	public void main(IWContext iwc) throws Exception {
		addLookupForm(iwc);
	}

	private void addLookupForm(IWContext iwc) {
		String kt = iwc.getParameter(PARAMETER_KT);
		boolean lookupDone = false;
		Member member = null;
		LoginTable lt = null;
		if (kt != null) {
			lookupDone = true;
			member = getMember(kt);
			if (member != null) {
				lt = getLoginTable(member);
			}
		}
		
		TextInput textInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_KT));
		if (kt != null) {
			textInput.setValue(kt);
		}
		SubmitButton search = (SubmitButton) getStyledInterface(new SubmitButton(getResourceBundle().getLocalizedImageButton("search", "Search")));
		int row = 1;
		
		Form form = new Form();
		Table table = new Table();
		table.setCellpaddingAndCellspacing(0);
		form.add(table);
		add(form);
		
		table.mergeCells(1, row, 2, row);
		table.add(getHeader(super.getResourceBundle().getLocalizedString("password_lookup","Password Lookup")), 1, row);
		table.setRowColor(row++, getHeaderColor());
		
		table.add(getSmallText(getResourceBundle().getLocalizedString("social_security_number", "Social Security Number")), 1, row);
		table.add(textInput, 2, row);
		table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setRowColor(row++, getZebraColor1());

		table.add(search, 2, row);
		table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setRowColor(row++, getZebraColor2());

		if (lookupDone) {
			table.mergeCells(1, row, 2, row);
			table.add(getHeader(super.getResourceBundle().getLocalizedString("results","Results")), 1, row);
			table.setRowColor(row, getHeaderColor());
			
			if (member != null) {
				table.add(getSmallText(getResourceBundle().getLocalizedString("user_not_found", "User not found")), 1, row);
				table.mergeCells(1, row, 2, row);
				table.setRowColor(row++, getZebraColor1());
			} else {
				table.add(getSmallText(getResourceBundle().getLocalizedString("user", "User")), 1, row);
				table.add(getSmallText(member.getName()), 2, row);
				table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
				table.setRowColor(row++, getZebraColor1());
				
				if (lt == null) {
					table.add(getSmallText(getResourceBundle().getLocalizedString("login_info_not_found", "Login info not found.")), 1, row);
					table.mergeCells(1, row, 2, row);
					table.setRowColor(row++, getZebraColor2());
				} else {
					table.add(getSmallText(getResourceBundle().getLocalizedString("login", "Login")), 1, row);
					table.add(getSmallText(lt.getUserLogin()), 2, row);
					table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
					table.setRowColor(row++, getZebraColor2());

					table.add(getSmallText(getResourceBundle().getLocalizedString("password", "Password")), 1, row);
					table.add(getSmallText(lt.getUserPassword()), 2, row);
					table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
					table.setRowColor(row++, getZebraColor1());
				}
			}
			
		}
	}
	
	private LoginTable getLoginTable(Member member) {
		try {
			LoginTableHome ltHome = (LoginTableHome) IDOLookup.getHome(LoginTable.class);
			return ltHome.findByMember(member);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Member getMember(String kt) {
		try {
			MemberHome mHome = (MemberHome) IDOLookup.getHome(Member.class);
			Member member = mHome.findBySSN(kt);
			return member;
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
