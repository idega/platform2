package is.idega.idegaweb.campus.block.mailinglist.presentation;


import is.idega.idegaweb.campus.block.mailinglist.business.LetterParser;
import is.idega.idegaweb.campus.block.mailinglist.business.MailingListService;
import is.idega.idegaweb.campus.block.mailinglist.data.EmailLetter;
import is.idega.idegaweb.campus.block.mailinglist.data.MailingList;
import is.idega.idegaweb.campus.presentation.CampusBlock;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.core.contact.data.Email;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.util.Edit;
import com.idega.util.text.ContentParsable;

/**
 *  Title: Description: Copyright: Copyright (c) 2001 Company:
 *
 * @author     <br>
 *      <a href="mailto:aron@idega.is">Aron Birkir</a> <br>
 *
 * @created    9. mars 2002
 * @version    1.0
 */

public class Emailer extends CampusBlock {

	private IWBundle core;
	
	private static String prmLocale = "em_locales";
	private static String prmLetter = "em_letter";
	private static String prmLetterDelete = "em_letter_del";
	private static String prmList = "em_list";
	private static String prmLists = "em_lists";
	private static String prmListId = "em_list_id";
	private static String prmListDelete = "em_list_del";
	private static String prmType = "em_type";
	private static String prmSubject = "em_subject";
	private static String prmFrom = "em_from";
	private static String prmParse = "em_parse";
	private static String prmUserOnly = "em_useronly";
	private static String prmHost = "em_host";
	private static String prmBody = "em_body";
	private static String prmForm = "em_form";
	private static String prmEmailDelete = "em_email_delete";
	private static String prmEmail = "em_email";
	private ContentParsable contentParsable;
	private boolean hasPermission = false;

	private Image editImage, deleteImage, mailImage, listImage;

	/**  Constructor for the Emailer object */
	public Emailer() {
		contentParsable = new LetterParser();
	}

	/**
	 *  Constructor for the Emailer object
	 *
	 * @param  contentParsable  Description of the Parameter
	 */
	public Emailer(ContentParsable contentParsable) {
		this.contentParsable = contentParsable;
	}

	/**
	 *  Sets the contentParsable attribute of the Emailer object
	 *
	 * @param  contentParsable  The new contentParsable value
	 */
	public void setContentParsable(ContentParsable contentParsable) {
		this.contentParsable = contentParsable;
	}

	/**
	 *  Gets the localizedNameKey of the Emailer object
	 *
	 * @return    The localized name key value
	 */
	public String getLocalizedNameKey() {
		return "emails";
	}

	/**
	 *  Gets the localizedNameValue of the Emailer object
	 *
	 * @return    The localized name value value
	 */
	public String getLocalizedNameValue() {
		return "Emails";
	}

	/**
	 *  Gets the bundleIdentifier of the Emailer object
	 *
	 * @return    The bundle identifier value
	 */
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	/**
	 * @param  iwc  Description of the Parameter
	 * @todo        Description of the Method
	 */
	private void control(IWContext iwc) throws RemoteException {
		//debugParameters(iwc);
		Table T = new Table(3, 2);
		T.setWidth(Table.HUNDRED_PERCENT);
		T.setBorder(1);
		T.setWidth(1, 1, "60%");
		T.setWidth(2, 1, "40%");
		//T.setWidth(3, 1, "30%");
		T.setVerticalAlignment(1, 1, "top");
		T.setVerticalAlignment(2, 1, "top");
		T.setVerticalAlignment(3, 1, "top");
		T.mergeCells(3, 1, 3, 2);
		MailingListService MailingListBusiness = getMailService(iwc);
		if (iwc.isParameterSet(prmLists)) {
			try {
				MailingList mlist = null;
				int mid = -1;
				int cat = -1;
				if (iwc.isParameterSet(prmListId)) {
					mlist =MailingListBusiness.getMailingList(Integer.parseInt(iwc.getParameter(prmListId)));
					mid = new Integer(mlist.getPrimaryKey().toString()).intValue();
				}
				if (iwc.isParameterSet("save_list")) {
					mlist = MailingListBusiness.storeMailingList(cat, mid, iwc.getParameter(prmEmail));
				}
				else if (iwc.isParameterSet(prmListDelete)) {
					MailingListBusiness.removeMailingList(mlist);
					mlist = null;
				}
				else if (iwc.isParameterSet(prmEmailDelete) && mlist != null) {
					MailingListBusiness.removeEmail(mlist, Integer.parseInt(iwc.getParameter(prmEmailDelete)));
				}
				else if (iwc.isParameterSet("save_email")) {
					MailingListBusiness.addEmail(mlist, iwc.getParameter(prmEmail));
				}
				T.add(getMailingLists(iwc,mid), 1, 1);
				if (iwc.isParameterSet("new_list") || mlist != null) {
					T.add(getMailingListForm(mlist), 1, 2);
				}
				if (mlist != null) {
					int row = 1;
					/*
					 *  if(iwc.isParameterSet("add_email")){
					 *  T.add(getEmailForm(mlist),2,1);
					 *  row++;
					 *  }
					 *  else{
					 */
					T.mergeCells(2, 1, 2, 2);
				
					//}
				
					T.add(getEmailList(iwc,mlist), 2, row);
				}
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (EJBException e) {
				e.printStackTrace();
			}
			catch (IDORelationshipException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
			catch (CreateException e) {
				e.printStackTrace();
			}
		}
		else if (iwc.isParameterSet(prmEmailDelete)) {

		}
		else {
			EmailLetter letter = null;
			boolean saved = false;
			int id = -1;
			if (iwc.isParameterSet(prmLetter)) {
				try {
					letter = MailingListBusiness.getEmailLetter(Integer.parseInt(iwc.getParameter(prmLetter)));
					id = new Integer(letter.getPrimaryKey().toString()).intValue();
				}
				catch (NumberFormatException e) {
					e.printStackTrace();
				}
				catch (RemoteException e) {
					e.printStackTrace();
				}
				catch (EJBException e) {
					e.printStackTrace();
				}
				catch (FinderException e) {
					e.printStackTrace();
				}
			}
			if (iwc.isParameterSet("save_letter")) {
				MailingListBusiness.createEmailLetter(
					letter,
					iwc.getParameter(prmHost),
					iwc.getParameter(prmFrom),
					iwc.getParameter(prmSubject),
					iwc.getParameter(prmBody),
					iwc.isParameterSet(prmParse),
					iwc.isParameterSet(prmUserOnly),
					iwc.getParameter(prmType));
				saved = true;
			}
			else if (iwc.isParameterSet("save_letter_list")) {
				//MailingListBusiness.createEmailLetter();
				String[] letUse = iwc.getParameterValues("let_use");
				String[] wasUsed = iwc.getParameterValues("was_used");
				letUse = letUse == null ? new String[0] : letUse;
				wasUsed = wasUsed == null ? new String[0] : wasUsed;
				if (letter != null && letUse.length != wasUsed.length) {
					MailingListBusiness.storeEmailLetterMailingLists(letter, parseArray(letUse), parseArray(wasUsed));
				}
				letter = null;
			}
			else if (iwc.isParameterSet(prmLetterDelete)) {
				if (letter != null) {
					MailingListBusiness.removeEmailLetter(letter);
					letter = null;
				}
			}
			else if (iwc.isParameterSet("mail_letter")) {
				if (letter != null) {
					getMailService(iwc).sendMail( letter, null);
				}
			}
			T.add(getEmailLetterList(iwc), 1, 1);
			if (iwc.isParameterSet("letter_list")) {
				if (letter != null) {
					T.add(getLetterMailingLists(iwc,letter), 2, 1);
				}
			}
			else if (!saved && (iwc.isParameterSet("new_letter") || letter != null)) {
				T.add(getEmailLetterForm(iwc, letter), 1, 2);
				if (contentParsable != null) {
					T.add(getTags(), 2, 2);
				}
			}
		}
		Form F = new Form();
		F.setName(prmForm);
		F.add(T);
		add(getLinkTable());
		add(F);
	}

	/**
	 *  Gets the linkTable of the Emailer object
	 *
	 * @return    The link table value
	 */
	public PresentationObject getLinkTable() {
		Table T = new Table();
		Link letter = new Link("Letters");
		T.add(letter, 1, 1);
		Link lists = new Link("Mailinglists");
		lists.addParameter(prmLists, "true");
		T.add(lists, 2, 1);
		return T;
	}

	/**
	 *  Gets the emailLetterList of the Emailer object
	 *
	 * @return    The email letter list value
	 */
	public PresentationObject getEmailLetterList(IWContext iwc) {
		Collection letters = null;
		try {
			 letters = getMailService(iwc).getEmailLetters();
		}
		catch (RemoteException e) {
			
		}
		DataTable dTable = new DataTable();
		dTable.setWidth("100%");
		dTable.setTitlesHorizontal(true);
		dTable.addTitle(localize("email_letters", "Email letters"));
		int row = 1;
		dTable.add(getHeader(localize("subject", "Subject")), 1, row);
		dTable.add(getHeader(localize("from", "From")), 2, row);
		dTable.add(getHeader(localize("type", "Type")), 3, row);
		row++;
		EmailLetter letter;
		if (letters != null) {
			Iterator iter = letters.iterator();
			while (iter.hasNext()) {
				letter = (EmailLetter) iter.next();
				dTable.add(getText(letter.getSubject()), 1, row);
				dTable.add(getText(letter.getFrom()), 2, row);
				dTable.add(getText(localize(letter.getMailType(),letter.getMailType())), 3, row);
				dTable.add(getLetterChangeLink(letter), 4, row);
				dTable.add(getLetterDeleteLink(letter), 5, row);
				dTable.add(getLetterMailLink(letter), 6, row);
				dTable.add(getLetterListLink(letter), 7, row);
				row++;
			}
		}
		Link newButton = new Link(getResourceBundle().getLocalizedImageButton("new", "New"));
		newButton.addParameter("new_letter", "true");
		dTable.addButton(newButton);
		return dTable;
	}

	/**
	 *  Gets the emailLetterForm of the Emailer object
	 *
	 * @param  iwc     Description of the Parameter
	 * @param  letter  Description of the Parameter
	 * @return         The email letter form value
	 */
	public PresentationObject getEmailLetterForm(IWContext iwc, EmailLetter letter) {
		DataTable dTable = new DataTable();
		dTable.setWidth(Table.HUNDRED_PERCENT);
		dTable.addTitle(localize("new_letter", "New letter"));

		DropdownMenu types = null;
		if (contentParsable != null) {
			types = getTypeDrop(prmType);
		}
		TextInput subject = new TextInput(prmSubject);
		TextInput from = new TextInput(prmFrom);
		TextInput host = new TextInput(prmHost);
		TextArea body = new TextArea(prmBody);
		body.setColumns(80);
		body.setRows(14);
		CheckBox parse = new CheckBox(prmParse);
		CheckBox useronly = new CheckBox(prmUserOnly);

		if (letter != null) {
			subject.setContent(letter.getSubject());
			body.setContent(letter.getBody());
			host.setContent(letter.getHost());
			from.setContent(letter.getFrom());
			if (types != null) {
				types.setSelectedElement(letter.getMailType());
			}
			parse.setChecked(letter.getParse());
			useronly.setChecked(letter.getOnlyUser());
			dTable.add(new HiddenInput(prmLetter, letter.getPrimaryKey().toString()));
		}
		int col_1 = 1;
		int col_2 = 2;
		int row = 1;

		if (types != null) {
			dTable.add(getHeader(localize("type", "Type")), col_1, row);
			dTable.add(types, col_2, row);
			row++;
		}
		dTable.add(getHeader(localize("host", "Host")), col_1, row);
		dTable.add(host, col_2, row);
		row++;
		dTable.add(getHeader(localize("from", "From")), col_1, row);
		dTable.add(from, col_2, row);
		row++;
		dTable.add(getHeader(localize("subject", "Subject")), col_1, row);
		dTable.add(subject, col_2, row);
		row++;
		dTable.add(getHeader(localize("body", "Body")), col_1, row);
		dTable.add(body, col_2, row);
		row++;
		dTable.add(getHeader(localize("parse", "Parse")), col_1, row);
		dTable.add(parse, col_2, row);
		row++;
		dTable.add(getHeader(localize("skip_user", "Skip user")), col_1, row);
		dTable.add(useronly, col_2, row);
		dTable.addButton(new SubmitButton(getResourceBundle().getLocalizedImageButton("save", "Save"), "save_letter"));
		return dTable;
	}

	/**
	 *  Gets the tags of the Emailer object
	 *
	 * @return    The tags value
	 */
	public PresentationObject getTags() {
		DataTable dTable = new DataTable();
		dTable.addTitle(localize("email_tags", "Email tags"));
		String[] tags = contentParsable.getParseTags();
		int row = 1;
		Link L;
		String tag;
		for (int i = 0; i < tags.length; i++) {
			tag = contentParsable.formatTag(tags[i]);
			L = new Link(getHeader(tag));
			//L.setOnClick("this.form."+prmBody+".value += this.options[this.selectedIndex].value;");
			L.setURL("javascript://");
			L.setOnClick("document." + prmForm + "." + prmBody + ".value += '" + tag + "' ;");
			dTable.add(L, 1, row++);
		}

		return dTable;
	}

	/**
	 *  Gets the mailingLists of the Emailer object
	 *
	 * @param  mlistId  Description of the Parameter
	 * @return          The mailing lists value
	 */
	public PresentationObject getMailingLists(IWContext iwc,int mlistId) {
		DataTable dTable = new DataTable();
		dTable.setWidth(Table.HUNDRED_PERCENT);
		dTable.setTitlesHorizontal(true);
		int row = 1;
		dTable.addTitle(localize("mailing_lists", "Mailing lists"));
		dTable.add(getHeader(localize("name", "Name")), 1, row);
		dTable.add(getHeader(localize("created", "Created")), 2, row);
		row++;
		Collection mlists = null;
		try {
			mlists = getMailService(iwc).getMailingLists();
		}
		catch (RemoteException e) {
			
		}
		if (mlists != null) {
			MailingList mlist;
			String color;
			Iterator iter = mlists.iterator();
			while (iter.hasNext()) {
				mlist = (MailingList) iter.next();
				color = new Integer(mlist.getPrimaryKey().toString()).intValue() != mlistId ? "#000000" : "#FF0000";
				dTable.add(getText(mlist.getName()), 1, row);
				dTable.add(getText(mlist.getCreated().toString()), 2, row);
				dTable.add(getListChangeLink(mlist), 3, row);
				dTable.add(getListDeleteLink(mlist), 4, row);
				row++;
			}
		}
		Link newbutton = new Link(getResourceBundle().getLocalizedImageButton("new", "New"));
		newbutton.addParameter(prmLists, "true");
		newbutton.addParameter("new_list", "true");
		dTable.addButton(newbutton);
		return dTable;
	}

	/**
	 *  Gets the mailingListForm of the Emailer object
	 *
	 * @param  mlist  Description of the Parameter
	 * @return        The mailing list form value
	 */
	public PresentationObject getMailingListForm(MailingList mlist) {
		DataTable dTable = new DataTable();
		dTable.setWidth(Table.HUNDRED_PERCENT);
		dTable.addTitle(localize("mailinglist", "Mailinglist"));
		TextInput name = new TextInput(prmEmail);
		if (mlist != null) {
			name.setContent(mlist.getName());
			dTable.add(new HiddenInput(prmListId, mlist.getPrimaryKey().toString()));
		}
		dTable.add(new HiddenInput(prmLists, "true"));
		dTable.add(getHeader(localize("name", "Name")), 1, 1);
		dTable.add(name, 2, 1);
		dTable.addButton(new SubmitButton(getResourceBundle().getLocalizedImageButton("save", "Save"), "save_list"));

		return dTable;
	}

	/**
	 *  Gets the emailList of the Emailer object
	 *
	 * @param  mlist  Description of the Parameter
	 * @return        The email list value
	 */
	public PresentationObject getEmailList(IWContext iwc, MailingList mlist) {
		DataTable dTable = new DataTable();
		dTable.setWidth(Table.HUNDRED_PERCENT);
		dTable.setTitlesHorizontal(true);
		dTable.addTitle(localize("emails", "Emails"));
		int row = 1;
		dTable.add(getHeader(localize("email_address", "Email address")), 1, row);
		row++;
		int id = -1;
		Collection emails = null;

		try {
			emails = getMailService(iwc).getEmails(mlist);
		}
		catch (RemoteException e) {

		}
		if (emails != null) {

			Email email;
			Iterator iter = emails.iterator();
			while (iter.hasNext()) {
				email = (Email) iter.next();
				dTable.add(getText(email.getEmailAddress()), 1, row);
				dTable.add(getEmailDeleteLink(email, mlist), 2, row);
				row++;
			}
		}
		dTable.addButton(new HiddenInput(prmList, mlist.getPrimaryKey().toString()));
		TextInput name = new TextInput(prmEmail);
		Edit.setStyle(name);
		dTable.addButton(name);

		dTable.addButton(new SubmitButton(getResourceBundle().getLocalizedImageButton("add", "add"), "save_email"));
		/*
		 *  Link addButton = new Link(iwrb.getLocalizedImageButton("add","Add"));
		 *  addButton.addParameter("add_email","true");
		 *  addButton.addParameter(prmLists,"true");
		 *  addButton.addParameter(prmListId,mlist.getID());
		 *  dTable.addButton(addButton);
		 */
		return dTable;
	}

	/**
	 *  Gets the letterMailingLists of the Emailer object
	 *
	 * @param  letter  Description of the Parameter
	 * @return         The letter mailing lists value
	 */
	public PresentationObject getLetterMailingLists(IWContext iwc, EmailLetter letter) throws RemoteException {
		DataTable dTable = new DataTable();
		dTable.setWidth("100%");
		dTable.setTitlesHorizontal(true);
		int row = 1;
		dTable.addTitle(localize("mailing_lists", "Mailing lists"));
		dTable.add(getHeader(localize("use", "Use")), 1, row);
		dTable.add(getHeader(localize("name", "Name")), 2, row);
		row++;
		Map listMap = getMailService(iwc).mapOfMailingList(letter);
		boolean hasMap = listMap != null;
		Collection mlists = getMailService(iwc).getMailingLists(); //MailingListBusiness.listOfMailingList();
		if (mlists != null) {
			MailingList mlist;
			String color;
			CheckBox use;
			HiddenInput used;
			String prmUse = "let_use";
			Iterator iter = mlists.iterator();
			while (iter.hasNext()) {
				mlist = (MailingList) iter.next();
				Integer primKey = (Integer) mlist.getPrimaryKey();
				use = new CheckBox(prmUse, primKey.toString());
				if (hasMap && listMap.containsKey(primKey)) {
					use.setChecked(true);
					used = new HiddenInput("was_used", primKey.toString());
					dTable.add(used);
				}
				else {
					use.setChecked(false);
				}

				dTable.add(use, 1, row);
				dTable.add(getText(mlist.getName()), 2, row);
				row++;
			}

		}
		dTable.add(new HiddenInput(prmLetter, letter.getPrimaryKey().toString()));
		dTable.addButton(new SubmitButton(getResourceBundle().getLocalizedImageButton("save", "Save"), "save_letter_list"));
		return dTable;
	}

	// Used to add new email to mailinglist
	/**
	 *  Gets the emailForm of the Emailer object
	 *
	 * @param  mlist  Description of the Parameter
	 * @return        The email form value
	 */
	public PresentationObject getEmailForm(MailingList mlist) {
		DataTable dTable = new DataTable();
		dTable.setWidth("100%");
		dTable.addTitle(localize("email", "Email"));
		TextInput name = new TextInput(prmEmail);
		if (mlist != null) {
			//name.setContent(mlist.getName());
			dTable.add(new HiddenInput(prmList, mlist.getPrimaryKey().toString()));
		}
		dTable.add(getHeader(localize("email_address", "Email address")), 1, 1);
		dTable.add(name, 2, 1);

		return dTable;
	}

	/**
	 *  Gets the typeDrop of the Emailer object
	 *
	 * @param  name  Description of the Parameter
	 * @return       The type drop value
	 */
	public DropdownMenu getTypeDrop(String name) {
		DropdownMenu drp = new DropdownMenu(name);
		String[] types = contentParsable.getParseTypes();
		for (int i = 0; i < types.length; i++) {
			drp.addMenuElement(types[i], localize(types[i], types[i]));
		}
		return drp;
	}

	/**
	 * @param  array  Description of the Parameter
	 * @return        Description of the Return Value
	 * @todo          Description of the Method
	 */
	private int[] parseArray(String[] array) {
		int[] intArray = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			intArray[i] = Integer.parseInt(array[i]);
		}
		return intArray;
	}

	/**
	 * @param  iwc  Description of the Parameter
	 * @todo        Description of the Method
	 */
	public void main(IWContext iwc) {
		
		
		core = iwc.getIWMainApplication().getCoreBundle();
		editImage = core.getImage("/shared/edit.gif");
		deleteImage = core.getImage("/shared/delete.gif");
		mailImage = getResourceBundle().getLocalizedImageButton("send_letter","Send");//core.getImage("/shared/empty.gif");
		listImage = getResourceBundle().getLocalizedImageButton("show_lists","Show lists");
		if(hasEditPermission()){
		try {
			control(iwc);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		}
		else
			addText(localize("no_permission","No permission"));
	}

	public MailingListService getMailService(IWApplicationContext iwac) throws RemoteException {
		return (MailingListService) IBOLookup.getServiceInstance(iwac, MailingListService.class);
	}

	/**
	 * @return    Description of the Return Value
	 * @todo      Description of the Method
	 */
	public synchronized Object clone() {
		Emailer obj = null;
		try {
			obj = (Emailer) super.clone();

			// integers :
			obj.contentParsable = contentParsable;
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
		}
		return obj;
	}



    /**
     *  Gets the letterChangeLink of the Emailer object
     *
     * @param  letter  Description of the Parameter
     * @return         The letter change link value
     */
    public Link getLetterChangeLink(EmailLetter letter) {
        Link L = new Link(editImage);
        L.addParameter(prmLetter, letter.getPrimaryKey().toString());
        L.setToolTip(localize("tooltip.edit_letter","Edit letter"));
        return L;
    }


    /**
     *  Gets the letterDeleteLink of the Emailer object
     *
     * @param  letter  Description of the Parameter
     * @return         The letter delete link value
     */
    public Link getLetterDeleteLink(EmailLetter letter) {
        Link L = new Link(deleteImage);
        L.addParameter(prmLetterDelete, letter.getPrimaryKey().toString());
        L.addParameter(prmLetter, letter.getPrimaryKey().toString());
        L.setToolTip(localize("tooltip.delete_letter","Delete letter"));
        return L;
    }


    /**
     *  Gets the letterMailLink of the Emailer object
     *
     * @param  letter  Description of the Parameter
     * @return         The letter mail link value
     */
    public Link getLetterMailLink(EmailLetter letter) {
        Link L = new Link(mailImage);
        L.addParameter("mail_letter", "true");
        L.addParameter(prmLetter, letter.getPrimaryKey().toString());
        L.setToolTip(localize("tooltip.mail_letter_sample","Mail letter to email list"));
        return L;
    }


    /**
     *  Gets the letterListLink of the Emailer object
     *
     * @param  letter  Description of the Parameter
     * @return         The letter list link value
     */
    public Link getLetterListLink(EmailLetter letter) {
        Link L = new Link(listImage);
        L.addParameter("letter_list", "true");
        L.setToolTip(localize("tooltip.view_mail_list","View email list"));
        L.addParameter(prmLetter, letter.getPrimaryKey().toString());
        return L;
    }


    /**
     *  Gets the listDeleteLink of the Emailer object
     *
     * @param  list  Description of the Parameter
     * @return       The list delete link value
     */
    public Link getListDeleteLink(MailingList list) {
        Link L = new Link(deleteImage);
        L.addParameter(prmListDelete, "true");
        L.addParameter(prmListId, list.getPrimaryKey().toString());
        L.addParameter(prmLists, "true");
        L.setToolTip(localize("tooltip.delete_list","Delete list"));
        return L;
    }


    /**
     *  Gets the listChangeLink of the Emailer object
     *
     * @param  list  Description of the Parameter
     * @return       The list change link value
     */
    public Link getListChangeLink(MailingList list) {
        Link L = new Link(editImage);
        L.addParameter(prmListId, list.getPrimaryKey().toString());
        L.addParameter(prmLists, "true");
        L.setToolTip(localize("tooltip.edit_list","Edit email list"));
        return L;
    }


    /**
     *  Gets the emailDeleteLink of the Emailer object
     *
     * @param  email  Description of the Parameter
     * @param  mlist  Description of the Parameter
     * @return        The email delete link value
     */
    public Link getEmailDeleteLink(Email email, MailingList mlist) {
        Link L = new Link(deleteImage);
        L.addParameter(prmEmailDelete, email.getPrimaryKey().toString());
        L.addParameter(prmListId, mlist.getPrimaryKey().toString());
        L.addParameter(prmLists, "true");
        L.setToolTip(localize("tooltip.remove_email","Remove email"));
        return L;
    }

}