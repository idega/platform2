/*
 * Temporary block for sending mail for accepted appliactions.
 * @author al
 */
package se.idega.idegaweb.commune.childcare.presentation.admin;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock;

import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

public class ChildCareApplicationMailer extends ChildCareBlock {
	
	public void init(IWContext iwc) throws Exception {
		if (iwc.isParameterSet("send_mail")) {
			int n = sendMails(iwc);
			add(new Text("" + n + " e-mails sent"));
		} else {
			add(new SubmitButton("send_mail", "Send e-mails"));
		}
	}

	private int sendMails(IWContext iwc) throws RemoteException {
		int nrOfMailsSent = 0;
		Collection c = getBusiness().findAllGrantedApplications();
		Iterator iter = c.iterator();
		
		while (iter.hasNext()) {
			ChildCareApplication application = (ChildCareApplication) iter.next();
			
			String messageHeader = localize("child_care.application_accepted_subject", "Child care application accepted.");
			String messageBody = MessageFormat.format(localize("child_care.offer_message", "We can offer {0} a placing in {5} from {4}.\n\nRegards,\n{1}\n{2}\n{3}"), getArguments(iwc, application));

			if (messageBody.indexOf("$datum$") != -1) {
				messageBody = TextSoap.findAndReplace(messageBody, "$datum$", "{4}");
			}
			if (messageBody.indexOf("$link$") != -1) {
				messageBody = TextSoap.findAndReplace(messageBody, "$link$", "");
			}
			
			getBusiness().sendMessageToParents(application, messageHeader, messageBody);
			nrOfMailsSent++;
		}
		
		return nrOfMailsSent;
	}
	
	private Object[] getArguments(IWContext iwc, ChildCareApplication application) {
		User child = application.getChild();
		String email = "";
		String workphone = "";

		Object[] arguments = { child.getName(), application.getProvider().getName(), email, workphone, new IWTimestamp(application.getFromDate()).getLocaleDate(iwc.getCurrentLocale()), application.getProvider().getName() };
		
		return arguments;
	}
}
