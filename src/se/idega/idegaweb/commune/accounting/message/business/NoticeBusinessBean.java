/*
 * $Id: NoticeBusinessBean.java,v 1.6 2003/09/30 14:21:25 anders Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.accounting.message.business;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.rmi.RemoteException;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolUser;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;

import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.accounting.school.data.Provider;

/** 
 * Business logic for notice messages.
 * <p>
 * Last modified: $Date: 2003/09/30 14:21:25 $ by $Author: anders $
 *
 * @author Anders Lindman
 * @version $Revision: 1.6 $
 */
public class NoticeBusinessBean extends com.idega.business.IBOServiceBean implements NoticeBusiness  {

	private final static String KP = "notice_error."; // key prefix 

	public final static String KEY_EMPTY_BODY = KP + "empty_body";
	public final static String KEY_SYSTEM_ERROR = KP + "system_error";

	public final static String DEFAULT_EMPTY_BODY = "PŒminnelsen kan inte vara tom.";
	public final static String DEFAULT_SYSTEM_ERROR = "PŒminnelsen kunde inte skickas p.g.a. tekniskt fel.";
	
	/**
	 * Send message and e-mail to all headmasters for schools.
	 * @param subject the message subject
	 * @param body the message body
	 * @return a collection of {school_name, headmaster} 
	 * @throws NoticeException if body empty or technical send error
	 */
	public Collection sendNotice(String subject, String body) throws NoticeException {
		if (body.equals("")) {
			throw new NoticeException(KEY_EMPTY_BODY, DEFAULT_EMPTY_BODY);
		}
		
		Collection c = new ArrayList();
		try {
			SchoolBusiness sb = getSchoolBusiness();
			SchoolCategory childCareCategory = sb.getCategoryChildcare();
			String childCareCategoryId = childCareCategory.getCategory();
			Collection schoolTypes = sb.findAllSchoolTypes();
			Iterator iter = schoolTypes.iterator();
			while (iter.hasNext()) {
				SchoolType st = (SchoolType) iter.next();
				String sc = st.getSchoolCategory();				
				int schoolTypeId = ((Integer) st.getPrimaryKey()).intValue();
				Collection schools = sb.findAllSchoolsByType(schoolTypeId);
				Iterator iter2 = schools.iterator();
				while (iter2.hasNext()) {
					School school = (School) iter2.next();
					Collection users = sb.getSchoolUsers(school);
					Iterator iter3 = users.iterator();
					while (iter3.hasNext()) {
						SchoolUser schoolUser = (SchoolUser) iter3.next();
						User user = schoolUser.getUser();
						Provider provider = new Provider(school);
						if (!sc.equals(childCareCategoryId) || 
								(!school.getCentralizedAdministration() && !provider.getPaymentByInvoice())) {
							String[] s = new String[2];
							s[0] = school.getName();
							s[1] = user.getName();
							c.add(s);
//	   remove comment			Message message = getMessageBusiness().createUserMessage(user, subject, body, false);
//	   to activate message		message.store();							
						}
					}
				}
			}
		} catch (RemoteException e) {
			throw new NoticeException(KEY_SYSTEM_ERROR, DEFAULT_SYSTEM_ERROR);
		}
		return c;
	}

	/**
	 * Returns school business. 
	 */	
	public SchoolBusiness getSchoolBusiness() throws RemoteException {
		return (SchoolBusiness) this.getServiceInstance(SchoolBusiness.class);
	}

	/**
	 * Returns user business. 
	 */	
	public UserBusiness getUserBusiness() throws RemoteException {
		return (UserBusiness) this.getServiceInstance(UserBusiness.class);
	}

	/**
	 * Returns message business. 
	 */	
	public MessageBusiness getMessageBusiness() throws RemoteException {
		return (MessageBusiness) this.getServiceInstance(MessageBusiness.class);
	}
}
