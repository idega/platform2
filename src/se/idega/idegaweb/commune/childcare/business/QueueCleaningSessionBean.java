/*
 * $Id: QueueCleaningSessionBean.java,v 1.3 2004/12/14 12:23:18 laddi Exp $
 * Created on 25.11.2004
 * 
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import se.idega.idegaweb.commune.care.data.ChildCareApplication;

import com.idega.business.IBOSessionBean;
import com.idega.idegaweb.IWPropertyList;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * 
 * Last modified: $Date: 2004/12/14 12:23:18 $ by $Author: laddi $
 * 
 * @author <a href="mailto:aron@idega.com">aron </a>
 * @version $Revision: 1.3 $
 */
public class QueueCleaningSessionBean extends IBOSessionBean implements QueueCleaningSession {

	private boolean cleaning = false;

	public boolean cleanQueueInThread(int providerID, User performer) throws FinderException, RemoteException {
		cleaning = true;
		IWPropertyList properties = getIWApplicationContext().getSystemProperties().getProperties(ChildCareConstants.PROPERTIES_CHILD_CARE);
		int monthsInQueue = Integer.parseInt(properties.getProperty(ChildCareConstants.PROPERTY_MAX_MONTHS_IN_QUEUE, "6"));
		int daysToReply = Integer.parseInt(properties.getProperty(ChildCareConstants.PROPERTY_DAYS_TO_REPLY, "30"));

		IWTimestamp beforeDate = new IWTimestamp();
		beforeDate.addMonths(-monthsInQueue);

		IWTimestamp stamp = new IWTimestamp();
		ChildCareBusiness service = getService();
		Collection applications = service.getApplicationsInQueueBeforeDate(providerID, beforeDate.getDate());

		UserTransaction transaction = getSessionContext().getUserTransaction();
		try {
			transaction.begin();

			IWTimestamp lastReplyDate = new IWTimestamp();
			lastReplyDate.addDays(daysToReply);
			IWResourceBundle iwrb = getIWApplicationContext().getIWMainApplication().getBundle(service.getBundleIdentifier()).getResourceBundle(getIWApplicationContext().getIWMainApplication().getSettings().getDefaultLocale());
			String subject = iwrb.getLocalizedString("child_care.clean_queue_subject", "Old application in queue");
			String body = iwrb.getLocalizedString("child_care.clean_queue_body", "Your application for {0}, {2},Êto {1}Êhas been in the queue for 6 months.  You now have until {3}Êto update your choices in the childcare overview.  After that, the choices will be removed from our queue. \n\nBest regards,\n{1}");
			String letterBody = iwrb.getLocalizedString("child_care.clean_queue_body_letter", "Your application for {0}, {2},Êto {1}Êhas been in the queue for 6 months.  You now have until {3}Êto update your choices in the childcare overview.  After that, the choices will be removed from our queue. \n\nBest regards,\n{1}");

			Iterator iter = applications.iterator();
			while (iter.hasNext()) {
				ChildCareApplication application = (ChildCareApplication) iter.next();
				if (!service.hasOutstandingOffers(application.getChildId(), application.getCode()) && service.hasActiveApplications(application.getChildId(), application.getCaseCode().getCode(), stamp.getDate())) {
					application.setLastReplyDate(lastReplyDate.getDate());
					service.changeCaseStatus(application, service.getCaseStatusPending().getStatus(), performer);

					service.sendMessageToParents(application, subject, body, letterBody, false);
				}
			}

			transaction.commit();
			cleaning = false;
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				transaction.rollback();
			}
			catch (SystemException ex) {
				ex.printStackTrace();
			}
		}

		return false;
	}

	public boolean cleanQueue(int providerID, User performer) {
		final int providID = providerID;
		final User perf = performer;
		if (!isStillCleaningQueue()) {
			new Thread() {

				public void run() {
					try {
						cleanQueueInThread(providID, perf);
					}
					catch (FinderException e) {
						e.printStackTrace();
					}
					catch (RemoteException r) {
						r.printStackTrace();
					}
				}
			}.start();
			return true;
		}
		else
			return false;

	}

	public boolean isStillCleaningQueue() {
		return cleaning;
	}

	public ChildCareBusiness getService() throws RemoteException {
		return (ChildCareBusiness) getServiceInstance(ChildCareBusiness.class);
	}

}
