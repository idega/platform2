/*
 * Temporary block for sending mail for application reminders.
 * @author al
 */
package se.idega.idegaweb.commune.childcare.presentation.admin;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.SubmitButton;

public class ChildCareReminderMailer extends ChildCareBlock {
	
	public void init(IWContext iwc) throws Exception {
		if (iwc.isParameterSet("send_mail")) {
			int n = sendMails();
			add(new Text("" + n + " e-mails sent"));
		} else {
			add(new SubmitButton("send_mail", "Send reminder e-mails"));
		}
	}

	private int sendMails() throws RemoteException {
		int nrOfMailsSent = 0;
		
		ChildCareBusiness service = getBusiness();
		Collection applications = getBusiness().findAllPendingApplications();

		try {			
			IWResourceBundle iwrb = getIWApplicationContext().getIWMainApplication().getBundle(service.getBundleIdentifier()).getResourceBundle(getIWApplicationContext().getIWMainApplication().getSettings().getDefaultLocale());
			String subject = iwrb.getLocalizedString("child_care.clean_queue_subject", "Old application in queue");
			String body = iwrb.getLocalizedString("child_care.clean_queue_body", "Your application for {0}, {2},Êto {1}Êhas been in the queue for 6 months.  You now have until {3}Êto update your choices in the childcare overview.  After that, the choices will be removed from our queue. \n\nBest regards,\n{1}");
			String letterBody = iwrb.getLocalizedString("child_care.clean_queue_body_letter", "Your application for {0}, {2},Êto {1}Êhas been in the queue for 6 months.  You now have until {3}Êto update your choices in the childcare overview.  After that, the choices will be removed from our queue. \n\nBest regards,\n{1}");

			Iterator iter = applications.iterator();
			while (iter.hasNext()) {
				ChildCareApplication application = (ChildCareApplication) iter.next();
				service.sendMessageToParents(application, subject, body, letterBody, false);
				nrOfMailsSent++;				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nrOfMailsSent;
	}
}
