/*
 * $Id: MealBlock.java,v 1.2 2005/08/12 19:30:43 gimmi Exp $
 * Created on Aug 10, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.presentation;

import java.rmi.RemoteException;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.meal.business.MealBusiness;
import se.idega.idegaweb.commune.school.meal.business.MealSession;
import se.idega.idegaweb.commune.school.meal.util.MealConstants;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;

/**
 * Last modified: $Date: 2005/08/12 19:30:43 $ by $Author: gimmi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public abstract class MealBlock extends CommuneBlock {
	
	private MealBusiness business;
	private MealSession session;
	private CommuneUserBusiness uBusiness;
	
	public void main(IWContext iwc) throws Exception {
		setBundle(getBundle(iwc));
		setResourceBundle(getResourceBundle(iwc));
		business = getBusiness(iwc);
		session = getSession(iwc);
		uBusiness = getUserBusiness(iwc);
		
		present(iwc);
	}
	
	protected MealBusiness getBusiness() {
		return business;
	}
	
	protected MealSession getSession() {
		return session;
	}
	
	protected CommuneUserBusiness getUserBusiness() {
		return uBusiness;
	}

	private MealBusiness getBusiness(IWApplicationContext iwac) {
		try {
			return (MealBusiness) IBOLookup.getServiceInstance(iwac, MealBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private MealSession getSession(IWUserContext iwuc) {
		try {
			return (MealSession) IBOLookup.getSessionInstance(iwuc, MealSession.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	protected CommuneUserBusiness getUserBusiness(IWApplicationContext iwac) {
		try {
			return super.getUserBusiness(iwac);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public String getBundleIdentifier() {
		return MealConstants.IW_BUNDLE_IDENTIFIER;
	}
	
	public abstract void present(IWContext iwc);
	
	protected Table getPersonInfoTable(IWContext iwc, SchoolClassMember member) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(0);
		table.setColumns(3);
		table.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;
		
		User user = member.getStudent();
		SchoolClass group = member.getSchoolClass();
		School school = group.getSchool();
		
		Address address = getUserBusiness().getUsersMainAddress(user);
		PostalCode postal = null;
		if (address != null) {
			postal = address.getPostalCode();
		}
		Phone phone = null;
		try {
			phone = getUserBusiness().getUsersHomePhone(user);
		}
		catch (NoPhoneFoundException npfe) {
			phone = null;
		}
		Email email = null;
		try {
			email = getUserBusiness().getUsersMainEmail(user);
		}
		catch (NoEmailFoundException nefe) {
			email = null;
		}
		
		table.add(getSmallHeader(localize("name", "Name")), 1, row);
		table.add(new Break(), 1, row);
		table.add(getText(user.getName()), 1, row);
		
		table.add(getSmallHeader(localize("personal_id", "Personal ID")), 2, row);
		table.add(new Break(), 2, row);
		table.add(getText(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())), 2, row);

		table.add(getSmallHeader(localize("home_phone", "Home phone")), 3, row);
		table.add(new Break(), 3, row);
		if (phone != null && phone.getNumber() != null) {
			table.add(getText(phone.getNumber()), 3, row);
		}
		row++;
		
		table.add(getSmallHeader(localize("address", "Address")), 1, row);
		table.add(getSmallHeader(localize("zip_code", "Postal code")), 2, row);
		table.add(getSmallHeader(localize("zip_city", "City")), 2, row);
		table.add(new Break(), 1, row);
		table.add(new Break(), 2, row);
		table.add(new Break(), 3, row);
		if (address != null) {
			table.add(getText(address.getStreetAddress()), 1, row);
		}
		if (postal != null) {
			table.add(getText(postal.getPostalCode()), 2, row);
			table.add(getText(postal.getName()), 3, row);
		}
		row++;
		
		table.add(getSmallHeader(localize("school", "School")), 1, row);
		table.add(getSmallHeader(localize("group", "Group")), 2, row);
		table.add(getSmallHeader(localize("email", "E-mail")), 3, row);
		table.add(new Break(), 1, row);
		table.add(new Break(), 2, row);
		table.add(new Break(), 3, row);
		if (school != null) {
			table.add(getText(school.getSchoolName()), 1, row);
		}
		if (group != null) {
			table.add(getText(group.getSchoolClassName()), 2, row);
		}
		if (email != null && email.getEmailAddress() != null) {
			table.add(getText(email.getEmailAddress()), 3, row);
		}
		row++;
		
		table.setWidth(1, "33%");
		table.setWidth(2, "33%");
		table.setWidth(3, "33%");

		table.setHeight(row, 6);
		table.mergeCells(1, row, table.getColumns(), row);
		table.setBottomCellBorder(1, row++, 1, "#D7D7D7", "solid");
		table.setHeight(row++, 6);
		
		return table;
	}
}