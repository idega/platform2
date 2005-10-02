/*
 * $Id: MealBlock.java,v 1.3 2005/10/02 13:44:24 laddi Exp $
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
import com.idega.presentation.Layer;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.Text;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;

/**
 * Last modified: $Date: 2005/10/02 13:44:24 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public abstract class MealBlock extends CommuneBlock {
	
	private MealBusiness business;
	private MealSession session;
	private CommuneUserBusiness uBusiness;
	
	protected static final String STYLENAME_MEAL_FORM = "mealForm";
  protected final static String STYLENAME_LIST_TABLE = "listTable";
  protected final static String STYLENAME_LIST_TABLE_ODD_ROW = "listTable_oddRow";
  protected final static String STYLENAME_LIST_TABLE_EVEN_ROW = "listTable_evenRow";
	
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
	
	protected Layer getPersonInfo(IWContext iwc, SchoolClassMember member) throws RemoteException {
		User user = member.getStudent();
		SchoolClass group = member.getSchoolClass();
		School school = group.getSchool();
		
		return getPersonInfo(iwc, user, school, group);
	}
	
	protected Layer getPersonInfo(IWContext iwc, User user, School school, SchoolClass group) throws RemoteException {
		Layer layer = new Layer();
		layer.setID("personInfo");
		
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
		
		Layer formElement = new Layer(Layer.DIV);
		formElement.setStyleClass("personInfoItem");
		Heading1 heading = new Heading1(localize("name", "Name"));
		Text text = new Text(user.getName());
		formElement.add(heading);
		formElement.add(text);
		layer.add(formElement);
		
		formElement = new Layer(Layer.DIV);
		formElement.setStyleClass("personInfoItem");
		heading = new Heading1(localize("personal_id", "Personal ID"));
		text = new Text(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale()));
		formElement.add(heading);
		formElement.add(text);
		layer.add(formElement);
		
		formElement = new Layer(Layer.DIV);
		formElement.setStyleClass("personInfoItem");
		heading = new Heading1(localize("home_phone", "Home phone"));
		if (phone != null && phone.getNumber() != null) {
			text = new Text(phone.getNumber());
		}
		else {
			text = new Text("-");
		}
		formElement.add(heading);
		formElement.add(text);
		layer.add(formElement);
		
		formElement = new Layer(Layer.DIV);
		formElement.setStyleClass("personInfoItem");
		heading = new Heading1(localize("address", "Address"));
		if (address != null) {
			text = new Text(address.getStreetAddress());
		}
		else {
			text = new Text("-");
		}
		formElement.add(heading);
		formElement.add(text);
		layer.add(formElement);
		
		formElement = new Layer(Layer.DIV);
		formElement.setStyleClass("personInfoItem");
		heading = new Heading1(localize("zip_code", "Postal code"));
		if (postal != null) {
			text = new Text(postal.getPostalCode());
		}
		else {
			text = new Text("-");
		}
		formElement.add(heading);
		formElement.add(text);
		layer.add(formElement);
		
		formElement = new Layer(Layer.DIV);
		formElement.setStyleClass("personInfoItem");
		heading = new Heading1(localize("zip_city", "City"));
		if (postal != null) {
			text = new Text(postal.getName());
		}
		else {
			text = new Text("-");
		}
		formElement.add(heading);
		formElement.add(text);
		layer.add(formElement);
		
		if (school != null && group != null) {
			formElement = new Layer(Layer.DIV);
			formElement.setStyleClass("personInfoItem");
			heading = new Heading1(localize("school", "School"));
			text = new Text(school.getSchoolName());
			formElement.add(heading);
			formElement.add(text);
			layer.add(formElement);
			
			formElement = new Layer(Layer.DIV);
			formElement.setStyleClass("personInfoItem");
			heading = new Heading1(localize("group", "Group"));
			text = new Text(group.getSchoolClassName());
			formElement.add(heading);
			formElement.add(text);
			layer.add(formElement);
		}

		formElement = new Layer(Layer.DIV);
		formElement.setStyleClass("personInfoItem");
		heading = new Heading1(localize("email", "E-mail"));
		if (email != null && email.getEmailAddress() != null) {
			text = new Text(email.getEmailAddress());
		}
		else {
			text = new Text("-");
		}
		formElement.add(heading);
		formElement.add(text);
		layer.add(formElement);
		
		Layer clear = new Layer(Layer.DIV);
		clear.setStyleClass("Clear");
		layer.add(clear);
		
		return layer;
	}
}