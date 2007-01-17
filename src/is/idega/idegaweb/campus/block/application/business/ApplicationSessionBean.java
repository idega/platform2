/*
 * Created on 5.1.2007
 *
 * Copyright (C) 2007 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 */
package is.idega.idegaweb.campus.block.application.business;

import com.idega.business.IBOSessionBean;

/**
 * @author palli
 *
 * ApplicationSessionBean handles the multistep application form
 */
public class ApplicationSessionBean extends IBOSessionBean implements ApplicationSession {
	protected boolean newUser = false;

	protected String subject = null;
	
	protected String apartment_category = null;
	
	protected String personalID = null;
		
	public void setIsNewUser(boolean isNewUser) {
		newUser = isNewUser;
	}
	
	public boolean getIsNewUser() {
		return newUser;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setApartmentCategory(String apartmentCategory) {
		this.apartment_category = apartmentCategory;
	}
	
	public String getApartmentCategory() {
		return apartment_category;
	}
}