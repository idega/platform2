package is.idega.idegaweb.campus.block.mailinglist.business;
import is.idega.idegaweb.campus.block.mailinglist.data.EmailLetter;
import is.idega.idegaweb.campus.block.mailinglist.data.EmailLetterHome;
import is.idega.idegaweb.campus.block.mailinglist.data.MailingList;
import is.idega.idegaweb.campus.block.mailinglist.data.MailingListHome;
import is.idega.idegaweb.campus.business.CampusService;
import is.idega.idegaweb.campus.business.CampusSettings;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.email.business.EmailAccount;
import com.idega.block.email.business.ListServer;
import com.idega.business.IBOServiceBean;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.EmailHome;
import com.idega.data.EntityBulkUpdater;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDOStoreException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.util.IWTimestamp;
import com.idega.util.SendMail;
import com.idega.util.text.ContentParser;

/**
 * @author aron
 *
 */
/**
 *  Title: Description: Copyright: Copyright (c) 2001 Company:
 *
 * @author     <br>
 *      <a href="mailto:aron@idega.is">Aron Birkir</a> <br>
 *
 * @created    9. mars 2002
 * @version    1.0
 */

public class MailingListServiceBean extends IBOServiceBean implements MailingListService {

	/**  @todo Description of the Field */
	public static String CATEGORYTYPE = "cam_mail";

	/**
	 *  @todo Description of the Method
	 *
	 * @param  iCategoryId  Description of the Parameter
	 * @param  name         Description of the Parameter
	 * @return              Description of the Return Value
	 */
	public MailingList createMailingList(int iCategoryId, String name)throws RemoteException, FinderException,CreateException {
		return storeMailingList(iCategoryId, -1, name);
	}

	/**
	 *  @todo Description of the Method
	 *
	 * @param  mlist     Description of the Parameter
	 * @param  email_id  Description of the Parameter
	 */
	public void removeEmail(MailingList mlist, int email_id) throws RemoteException, FinderException {

		EmailHome home = ((EmailHome) getIDOHome(Email.class));
		Email email = home.findByPrimaryKey(new Integer(email_id));
		mlist.removeEmail(email);

	}

	

	/**
	 *  Gets the mailingList of the MailingListBusiness class
	 *
	 * @param  id  Description of the Parameter
	 * @return     The mailing list value
	 */
	public MailingList getMailingList(int id) throws RemoteException, FinderException {
		return ((MailingListHome) getIDOHome(MailingList.class)).findByPrimaryKey(new Integer(id));
	}

	/**
	 *  Gets the emailLetter of the MailingListBusiness class
	 *
	 * @param  id  Description of the Parameter
	 * @return     The email letter value
	 */
	public EmailLetter getEmailLetter(int id) throws RemoteException, FinderException {
		return ((EmailLetterHome) getIDOHome(EmailLetter.class)).findByPrimaryKey(new Integer(id));
	}

	
	/**
	 * @param iCategoryId
	 * @param MailingListId
	 * @param name
	 * @return
	 * @throws RemoteException
	 * @throws CreateException
	 * @throws FinderException
	 */
	public MailingList storeMailingList(int iCategoryId, int MailingListId, String name) throws RemoteException,CreateException,FinderException{
	
			MailingList mlist = 	((MailingListHome) getIDOHome(MailingList.class)).create();
			if (MailingListId > 0) {
				mlist =((MailingListHome)getIDOHome(MailingList.class)).findByPrimaryKey(new Integer(MailingListId));
			}
			mlist.setName(name);
			mlist.setCreated(IWTimestamp.getTimestampRightNow());
			if (iCategoryId > 0) {
				mlist.setCategoryId(iCategoryId);
			}
			mlist.store();
			return mlist;
	}

	/**
	 *  Adds a feature to the Email attribute of the MailingListBusiness class
	 *
	 * @param  iMailingListId  The feature to be added to the Email attribute
	 * @param  address         The feature to be added to the Email attribute
	 * @return                 Description of the Return Value
	 */
	public boolean addEmail(int iMailingListId, String address)throws RemoteException,FinderException,IDORelationshipException,CreateException {
		MailingList mlist =	((MailingListHome) getIDOHome(MailingList.class)).findByPrimaryKey(new Integer(iMailingListId));
		addEmail(mlist,address);		
		return true;
		
	}

	/**
	 *  Adds a feature to the Email attribute of the MailingListBusiness class
	 *
	 * @param  mlist    The feature to be added to the Email attribute
	 * @param  address  The feature to be added to the Email attribute
	 * @return          Description of the Return Value
	 */
	public boolean addEmail(MailingList mlist, String address)throws CreateException,RemoteException,IDORelationshipException {
		if (mlist != null) {
			EmailHome eHome = (EmailHome) getIDOHome(Email.class);
			Email email = null ;
			try {
				email = eHome.findEmailByAddress(address);
			}
			catch (FinderException e) {}
			if(email == null)
				email = eHome.create();
				email.setEmailAddress(address);
				email.store();
				mlist.addEmail(email);
				return true;
			
		}
		return false;
	}

	/**
	 *  Adds a feature to the Email attribute of the MailingListBusiness class
	 *
	 * @param  iMailingListId  The feature to be added to the Email attribute
	 * @param  emails          The feature to be added to the Email attribute
	 * @return                 Description of the Return Value
	 */
	public boolean addEmail(int iMailingListId, List emails)throws RemoteException,FinderException {
			MailingList mlist =(	(MailingListHome) getIDOHome(MailingList.class)).findByPrimaryKey(new Integer(iMailingListId));
			EntityBulkUpdater bulk = new EntityBulkUpdater(mlist);
			bulk.addAll(emails, EntityBulkUpdater.addto);
			return true;
		
	}

	/**
	 *  @todo Description of the Method
	 *
	 * @param  letterId  Description of the Parameter
	 * @param  holder    Description of the Parameter
	 * @return           Description of the Return Value
	 */
	public boolean sendMail( int letterId, EntityHolder holder) {
		
			try {
				EmailLetter letter =((EmailLetterHome) getIDOHome(EmailLetter.class)).findByPrimaryKey(new Integer(letterId));
				return sendMail( letter, holder);
			}
			catch (RemoteException e) {
				
			}
			catch (FinderException e) {
				
			}
	
		return false;
	}

	/**
	 *  @todo Description of the Method
	 *
	 * @param  iContractId  Description of the Parameter
	 * @param  type         Description of the Parameter
	 * @return              Description of the Return Value
	 */
	public boolean processMailEvent( int iContractId, String type) {
		return processMailEvent( new EntityHolder(iContractId), type);
	}

	/**
	 *  @todo Description of the Method
	 *
	 * @param  holder  Description of the Parameter
	 * @param  type    Description of the Parameter
	 * @return         Description of the Return Value
	 */
	public boolean processMailEvent( EntityHolder holder, String type) {
		try {
			CampusSettings settings = getCampusService().getCampusSettings();
			boolean sendEventMail = settings.getSendEventMail();
			
			if (!sendEventMail) {
				System.err.println("not sending any mail although requested");
				return false;
			}
			System.err.println("Sending email of type : " + type);
			EmailLetterHome elHome = (EmailLetterHome) getIDOHome(EmailLetter.class);
			Collection letters = elHome.findByType(type);
				
			if (letters != null) {
				System.err.println("Number of letters : " + letters.size());
				java.util.Iterator iter = letters.iterator();
				EmailLetter letter;
				while (iter.hasNext()) {
					letter = (EmailLetter) iter.next();
					sendMail( letter, holder);
				}
				return true;
			}
			else {
				System.err.println("no letters to send");
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return true;
	}

	/**
	 *  Parses an email letter and sends it to all recipients
	 *
	 * @param  letter  Description of the Parameter
	 * @param  holder  Description of the Parameter
	 * @return         Description of the Return Value
	 */
	public boolean sendMail( EmailLetter letter, EntityHolder holder) {
		try {
			String Body = letter.getBody();
			Collection holderEmails = new Vector();
			if (holder != null && letter.getParse()) {
				LetterParser parser = new LetterParser(holder);
				Body = new ContentParser().parse(getIWApplicationContext(), parser, Body);
				if (!letter.getOnlyUser())
					holderEmails = holder.getEmails();
			}
			String subject = letter.getSubject();

			List emails = new Vector();

			MailingList mlist ;
			Collection lists = letter.getMailingLists();
			if (lists != null) {
				Iterator mIter = lists.iterator();
				Collection temp;
				while (mIter.hasNext()) {
					mlist = (MailingList) mIter.next();
					temp = mlist.getEmails();
					if (temp != null) {
						emails.addAll(temp);
					}
				}
			}

			if (emails != null && !emails.isEmpty()) {
				Iterator eIter = emails.iterator();
				Email email;
				while (eIter.hasNext()) {
					email = (Email) eIter.next();
					holderEmails.add(email.getEmailAddress());
				}

			}
			if (holderEmails != null && !holderEmails.isEmpty()) {
				Iterator eIter = holderEmails.iterator();
				String email=null;
				Object emailObject ;
				while (eIter.hasNext()) {
					emailObject = eIter.next();
					if(emailObject instanceof String)
						email = (String)emailObject;
					else if(emailObject instanceof Email)
						email = ((Email)emailObject).getEmailAddress();
					if(email!=null){
						System.err.println("Sending letter to " + email);
						SendMail.send(letter.getFrom(), email, "", "", letter.getHost(), subject, Body);
					}
					email = null;
				}
			}
			return true;
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 *  Gets the emailSubject of the MailingListBusiness class
	 *
	 * @param  letter  Description of the Parameter
	 * @param  iwrb    Description of the Parameter
	 * @return         The email subject value
	 */
	public String getEmailSubject(EmailLetter letter, IWResourceBundle iwrb) {
		String subject = iwrb.getLocalizedString(letter.getSubjectKey());
		if (subject == null) {
			return "";
		}
		else {
			return subject;
		}
	}

	/**
	 *  Sets the emailSubject attribute of the MailingListBusiness class
	 *
	 * @param  letter   The new emailSubject value
	 * @param  iwrb     The new emailSubject value
	 * @param  subject  The new emailSubject value
	 */
	public void setEmailSubject(EmailLetter letter, IWResourceBundle iwrb, String subject) {
		iwrb.setString(letter.getSubjectKey(), subject);
		//iwrb.storeState();
	}

	/**
	 *  Gets the emailBody of the MailingListBusiness class
	 *
	 * @param  letter  Description of the Parameter
	 * @param  iwrb    Description of the Parameter
	 * @return         The email body value
	 */
	public String getEmailBody(EmailLetter letter, IWResourceBundle iwrb) {
		String subject = iwrb.getLocalizedString(letter.getEmailKey());
		if (subject == null) {
			return "";
		}
		else {
			return subject;
		}
	}

	/**
	 *  Sets the emailBody attribute of the MailingListBusiness class
	 *
	 * @param  letter  The new emailBody value
	 * @param  iwrb    The new emailBody value
	 * @param  body    The new emailBody value
	 */
	public void setEmailBody(EmailLetter letter, IWResourceBundle iwrb, String body) {
		iwrb.setString(letter.getEmailKey(), body);

		//iwrb.storeState();
	}

	/**
	 *  @todo Description of the Method
	 *
	 * @param  iEmailLetterId  Description of the Parameter
	 * @param  sHost           Description of the Parameter
	 * @param  sFrom           Description of the Parameter
	 * @param  subject         Description of the Parameter
	 * @param  body            Description of the Parameter
	 * @param  Parse           Description of the Parameter
	 * @param  OnlyUser        Description of the Parameter
	 * @param  type            Description of the Parameter
	 * @return                 Description of the Return Value
	 */
	public EmailLetter storeEmailLetter(
		int iEmailLetterId,
		String sHost,
		String sFrom,
		String subject,
		String body,
		boolean Parse,
		boolean OnlyUser,
		String type) {
		EmailLetter letter = null;
		
			
			try {
				EmailLetterHome lHome = (EmailLetterHome) getIDOHome(EmailLetter.class);
				letter = lHome.create();
				if (iEmailLetterId > 0) {
					letter =	lHome.findByPrimaryKey(new Integer(iEmailLetterId));
				}
				letter.setSubject(subject);
				letter.setBody(body);
				letter.setHost(sHost);
				letter.setFrom(sFrom);
				letter.setParse(Parse);
				letter.setOnlyUser(OnlyUser);
				letter.setType(type);
				letter.store();
				
			}
			catch (IDOStoreException e) {
				
			}
			catch (RemoteException e) {
				
			}
			catch (CreateException e) {
				
			}
			catch (FinderException e) {
				
			}
		
		return letter;
	}

	/**
	 *  @todo Description of the Method
	 *
	 * @param  sHost      Description of the Parameter
	 * @param  sFrom      Description of the Parameter
	 * @param  sSubject   Description of the Parameter
	 * @param  sBody      Description of the Parameter
	 * @param  bParse     Description of the Parameter
	 * @param  bOnlyUser  Description of the Parameter
	 * @param  type       Description of the Parameter
	 * @return            Description of the Return Value
	 */
	public EmailLetter createEmailLetter(
		String sHost,
		String sFrom,
		String sSubject,
		String sBody,
		boolean bParse,
		boolean bOnlyUser,
		String type) {
		return storeEmailLetter(-1, sHost, sFrom, sSubject, sBody, bParse, bOnlyUser, type);
	}

	/**
	 *  @todo Description of the Method
	 *
	 * @param  emailletter  Description of the Parameter
	 * @param  sHost        Description of the Parameter
	 * @param  sFrom        Description of the Parameter
	 * @param  sSubject     Description of the Parameter
	 * @param  sBody        Description of the Parameter
	 * @param  bParse       Description of the Parameter
	 * @param  onlyUser     Description of the Parameter
	 * @param  type         Description of the Parameter
	 * @return              Description of the Return Value
	 */
	public EmailLetter createEmailLetter(
		EmailLetter emailletter,
		String sHost,
		String sFrom,
		String sSubject,
		String sBody,
		boolean bParse,
		boolean onlyUser,
		String type) {
		int id = emailletter != null ? ((Integer)emailletter.getPrimaryKey()).intValue() : -1;
		return storeEmailLetter(id, sHost, sFrom, sSubject, sBody, bParse, onlyUser, type);
	}

	/**
	 *  @todo Description of the Method
	 *
	 * @param  letter  Description of the Parameter
	 */
	public void removeEmailLetter(EmailLetter letter) {
		try {
			letter.remove();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 *  @todo Description of the Method
	 *
	 * @param  list  Description of the Parameter
	 */
	public void removeMailingList(MailingList list) {
		if (list != null) {
			
				try {
					list.removeEmails();
					list.remove();
				}
				catch (RemoteException e) {
					e.printStackTrace();
				}
				catch (EJBException e) {
					e.printStackTrace();
				}
				catch (RemoveException e) {
					e.printStackTrace();
				}
			
		}
	}

	/**
	 *  @todo Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public Collection getEmailLetters() {
		
			try {
				EmailLetterHome elHome = (EmailLetterHome) getIDOHome(EmailLetter.class);
				return elHome.findAll();
			}
			catch (RemoteException e) {
			
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		return null;
	}

	/**
	 *  @todo Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public Collection getMailingLists() {
		try {
			MailingListHome home = (MailingListHome)getIDOHome(MailingList.class);
			return home.findAll();
		}
		catch (RemoteException e) {
			
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *  @todo Description of the Method
	 *
	 * @param  letter  Description of the Parameter
	 * @return         Description of the Return Value
	 */
	public Map mapOfMailingList(EmailLetter letter)  {
		
			try {
				Collection L = letter.getMailingLists();
				if (L != null) {
					MailingList mlist;
					Iterator I = L.iterator();
					Hashtable H = new Hashtable(L.size());
					while (I.hasNext()) {
						mlist = (MailingList) I.next();
						H.put(new Integer(mlist.getPrimaryKey().toString()), mlist);
					}
					return H;
				}
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			return null;
		
	}

	
	/**
	 * @param letter
	 * @param newIds
	 * @param oldIds
	 */
	public void storeEmailLetterMailingLists(EmailLetter letter, int[] newIds, int[] oldIds) {
		try {
			
			letter.removeMailingLists();
			MailingListHome lHome = (MailingListHome) getIDOHome(MailingList.class);
			MailingList list;
			for (int i = 0; i < newIds.length; i++) {
				list = lHome.findByPrimaryKey(new Integer(newIds[i]));
				letter.addMailingList(list);
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		

	}
	
	public void sendLetter(EmailLetter letter,EmailAccount account, Collection emails){
		ListServer server = new ListServer();
		server.sendMailLetter(letter, account, emails);
	}

	
	public Collection getEmails(MailingList mlist) throws RemoteException{
			return mlist.getEmails();
	}
	
	public CampusService getCampusService()throws RemoteException{
		return (CampusService) getServiceInstance(CampusService.class);
	}

}
