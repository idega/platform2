package is.idega.idegaweb.campus.block.mailinglist.presentation;
import is.idega.idegaweb.campus.block.mailinglist.business.LetterParser;
import is.idega.idegaweb.campus.block.mailinglist.business.MailingListBusiness;
import is.idega.idegaweb.campus.block.mailinglist.data.EmailLetter;
import is.idega.idegaweb.campus.block.mailinglist.data.MailingList;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.idega.core.contact.data.Email;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
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

public class Emailer extends Block {

    private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.campus.emails";

    private IWBundle iwb, core;
    private IWResourceBundle iwrb;
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

    private Image editImage, deleteImage, mailImage,listImage;


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
    private void control(IWContext iwc) {
        //debugParameters(iwc);
        Table T = new Table(3, 2);
        T.setWidth("100%");
        T.setWidth(1, 1, "30%");
        T.setWidth(2, 1, "30%");
        T.setWidth(3, 1, "30%");
        T.setVerticalAlignment(1, 1, "top");
        T.setVerticalAlignment(2, 1, "top");
        T.setVerticalAlignment(3, 1, "top");
        T.mergeCells(3, 1, 3, 2);

        if (iwc.isParameterSet(prmLists)) {
            MailingList mlist = null;
            int mid = -1;
            int cat = -1;
            if (iwc.isParameterSet(prmListId)) {
                mlist = MailingListBusiness.getMailingList(Integer.parseInt(iwc.getParameter(prmListId)));
                mid = mlist.getID();
            }
            if (iwc.isParameterSet("save_list.x")) {
                mlist = MailingListBusiness.saveMailingList(cat, mid, iwc.getParameter(prmEmail));
            } else if (iwc.isParameterSet(prmListDelete)) {
                MailingListBusiness.deleteMailingList(mlist);
                mlist = null;
            } else if (iwc.isParameterSet(prmEmailDelete) && mlist != null) {
                MailingListBusiness.removeEmail(mlist, Integer.parseInt(iwc.getParameter(prmEmailDelete)));
            } else if (iwc.isParameterSet("save_email.x")) {
                MailingListBusiness.addEmail(mlist, iwc.getParameter(prmEmail));
            }
            T.add(getMailingLists(mid), 1, 1);
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

                T.add(getEmailList(mlist), 2, row);
            }
        } else if (iwc.isParameterSet(prmEmailDelete)) {

        } else {
            EmailLetter letter = null;
            boolean saved = false;
            int id = -1;
            if (iwc.isParameterSet(prmLetter)) {
                letter = MailingListBusiness.getEmailLetter(Integer.parseInt(iwc.getParameter(prmLetter)));
                id = letter.getID();
            }
            if (iwc.isParameterSet("save_letter.x")) {
                MailingListBusiness.createEmailLetter(letter, iwc.getParameter(prmHost),
                        iwc.getParameter(prmFrom), iwc.getParameter(prmSubject),
                        iwc.getParameter(prmBody), iwc.isParameterSet(prmParse), iwc.isParameterSet(prmUserOnly),
                        iwc.getParameter(prmType));
                saved = true;
            } else if (iwc.isParameterSet("save_letter_list.x")) {
                //MailingListBusiness.createEmailLetter();
                String[] letUse = iwc.getParameterValues("let_use");
                String[] wasUsed = iwc.getParameterValues("was_used");
                letUse = letUse == null ? new String[0] : letUse;
                wasUsed = wasUsed == null ? new String[0] : wasUsed;
                if (letter != null && letUse.length != wasUsed.length) {
                    MailingListBusiness.saveEmailLetterMailingLists(letter, parseArray(letUse), parseArray(wasUsed));
                }
                letter = null;
            } else if (iwc.isParameterSet(prmLetterDelete)) {
                if (letter != null) {
                    MailingListBusiness.deleteEmailLetter(letter);
                    letter = null;
                }
            } else if (iwc.isParameterSet("mail_letter")) {
                if (letter != null) {
                    MailingListBusiness.sendMail(iwc,letter, null);
                }
            }
            T.add(getEmailLetterList(), 1, 1);
            if (iwc.isParameterSet("letter_list")) {
                if (letter != null) {
                    T.add(getLetterMailingLists(letter), 2, 1);
                }
            } else if (!saved && (iwc.isParameterSet("new_letter") || letter != null)) {
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
    public PresentationObject getEmailLetterList() {
        List letters = MailingListBusiness.listOfEmailLetter();
        DataTable dTable = new DataTable();
        dTable.setWidth("100%");
        dTable.setTitlesHorizontal(true);
        dTable.addTitle(iwrb.getLocalizedString("email_letters", "Email letters"));
        int row = 1;
        dTable.add(Edit.formatText(iwrb.getLocalizedString("subject", "Subject")), 1, row);
        dTable.add(Edit.formatText(iwrb.getLocalizedString("from", "From")), 2, row);
        dTable.add(Edit.formatText(iwrb.getLocalizedString("type", "Type")), 3, row);
        row++;
        EmailLetter letter;
        if (letters != null) {
            Iterator iter = letters.iterator();
            while (iter.hasNext()) {
                letter = (EmailLetter) iter.next();
                dTable.add(Edit.formatText(letter.getSubject()), 1, row);
                dTable.add(Edit.formatText(letter.getFrom()), 2, row);
                dTable.add(Edit.formatText(letter.getType()), 3, row);
                dTable.add(getLetterChangeLink(letter), 4, row);
                dTable.add(getLetterDeleteLink(letter), 5, row);
                dTable.add(getLetterMailLink(letter), 6, row);
                dTable.add(getLetterListLink(letter), 7, row);
                row++;
            }
        }
        Link newButton = new Link(iwrb.getLocalizedImageButton("new", "New"));
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
        dTable.setWidth("100%");
        dTable.addTitle(iwrb.getLocalizedString("new_letter", "New letter"));

        DropdownMenu types = null;
        if (contentParsable != null) {
            types = getTypeDrop(prmType);
        }
        TextInput subject = new TextInput(prmSubject);
        TextInput from = new TextInput(prmFrom);
        TextInput host = new TextInput(prmHost);
        TextArea body = new TextArea(prmBody);
        body.setWidth(80);
        body.setHeight(14);
        CheckBox parse = new CheckBox(prmParse);
        CheckBox useronly = new CheckBox(prmUserOnly);

        if (letter != null) {
            subject.setContent(letter.getSubject());
            body.setContent(letter.getBody());
            host.setContent(letter.getHost());
            from.setContent(letter.getFrom());
            if (types != null) {
                types.setSelectedElement(letter.getType());
            }
            parse.setChecked(letter.getParse());
            useronly.setChecked(letter.getOnlyUser());
            dTable.add(new HiddenInput(prmLetter, String.valueOf(letter.getID())));
        }
        int col_1 = 1;
        int col_2 = 2;
        int row = 1;

        if (types != null) {
            dTable.add(Edit.formatText(iwrb.getLocalizedString("type", "Type")), col_1, row);
            dTable.add(types, col_2, row);
            row++;
        }
        dTable.add(Edit.formatText(iwrb.getLocalizedString("host", "Host")), col_1, row);
        dTable.add(host, col_2, row);
        row++;
        dTable.add(Edit.formatText(iwrb.getLocalizedString("from", "From")), col_1, row);
        dTable.add(from, col_2, row);
        row++;
        dTable.add(Edit.formatText(iwrb.getLocalizedString("subject", "Subject")), col_1, row);
        dTable.add(subject, col_2, row);
        row++;
        dTable.add(Edit.formatText(iwrb.getLocalizedString("body", "Body")), col_1, row);
        dTable.add(body, col_2, row);
        row++;
        dTable.add(Edit.formatText(iwrb.getLocalizedString("parse", "Parse")), col_1, row);
        dTable.add(parse, col_2, row);
        row++;
        dTable.add(Edit.formatText(iwrb.getLocalizedString("skip_user", "Skip user")), col_1, row);
        dTable.add(useronly, col_2, row);
        dTable.addButton(new SubmitButton(iwrb.getLocalizedImageButton("save", "Save"), "save_letter"));
        return dTable;
    }


    /**
     *  Gets the tags of the Emailer object
     *
     * @return    The tags value
     */
    public PresentationObject getTags() {
        DataTable dTable = new DataTable();
        dTable.addTitle(iwrb.getLocalizedString("email_tags", "Email tags"));
        String[] tags = contentParsable.getParseTags();
        int row = 1;
        Link L;
        String tag;
        for (int i = 0; i < tags.length; i++) {
            tag = contentParsable.formatTag(tags[i]);
            L = new Link(Edit.formatText(tag));
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
    public PresentationObject getMailingLists(int mlistId) {
        DataTable dTable = new DataTable();
        dTable.setWidth("100%");
        dTable.setTitlesHorizontal(true);
        int row = 1;
        dTable.addTitle(iwrb.getLocalizedString("mailing_lists", "Mailing lists"));
        dTable.add(Edit.formatText(iwrb.getLocalizedString("name", "Name")), 1, row);
        dTable.add(Edit.formatText(iwrb.getLocalizedString("created", "Created")), 2, row);
        row++;
        List mlists = MailingListBusiness.listOfMailingList();
        if (mlists != null) {
            MailingList mlist;
            String color;
            Iterator iter = mlists.iterator();
            while (iter.hasNext()) {
                mlist = (MailingList) iter.next();
                color = mlist.getID() != mlistId ? "#000000" : "#FF0000";
                dTable.add(Edit.formatText(mlist.getName(), color), 1, row);
                dTable.add(Edit.formatText(mlist.getCreated().toString(), color), 2, row);
                dTable.add(getListChangeLink(mlist), 3, row);
                dTable.add(getListDeleteLink(mlist), 4, row);
                row++;
            }
        }
        Link newbutton = new Link(iwrb.getLocalizedImageButton("new", "New"));
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
        dTable.setWidth("100%");
        dTable.addTitle(iwrb.getLocalizedString("mailinglist", "Mailinglist"));
        TextInput name = new TextInput(prmEmail);
        if (mlist != null) {
            name.setContent(mlist.getName());
            dTable.add(new HiddenInput(prmListId, String.valueOf(mlist.getID())));
        }
        dTable.add(new HiddenInput(prmLists, "true"));
        dTable.add(Edit.formatText(iwrb.getLocalizedString("name", "Name")), 1, 1);
        dTable.add(name, 2, 1);
        dTable.addButton(new SubmitButton(iwrb.getLocalizedImageButton("save", "Save"), "save_list"));

        return dTable;
    }


    /**
     *  Gets the emailList of the Emailer object
     *
     * @param  mlist  Description of the Parameter
     * @return        The email list value
     */
    public PresentationObject getEmailList(MailingList mlist) {
        DataTable dTable = new DataTable();
        dTable.setWidth("100%");
        dTable.setTitlesHorizontal(true);
        dTable.addTitle(iwrb.getLocalizedString("emails", "Emails"));
        int row = 1;
        dTable.add(Edit.formatText(iwrb.getLocalizedString("email_address", "Email address")), 1, row);
        row++;
        int id = -1;
        List emails = MailingListBusiness.listOfEmails(mlist);
        if (emails != null) {
            id = mlist.getID();
            Email email;
            Iterator iter = emails.iterator();
            while (iter.hasNext()) {
                email = (Email) iter.next();
                dTable.add(Edit.formatText(email.getEmailAddress()), 1, row);
                dTable.add(getEmailDeleteLink(email, mlist), 2, row);
                row++;
            }
        }
        dTable.addButton(new HiddenInput(prmList, String.valueOf(mlist.getID())));
        TextInput name = new TextInput(prmEmail);
        Edit.setStyle(name);
        dTable.addButton(name);

        dTable.addButton(new SubmitButton(iwrb.getLocalizedImageButton("add", "add"), "save_email"));
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
    public PresentationObject getLetterMailingLists(EmailLetter letter) {
        DataTable dTable = new DataTable();
        dTable.setWidth("100%");
        dTable.setTitlesHorizontal(true);
        int row = 1;
        dTable.addTitle(iwrb.getLocalizedString("mailing_lists", "Mailing lists"));
        dTable.add(Edit.formatText(iwrb.getLocalizedString("use", "Use")), 1, row);
        dTable.add(Edit.formatText(iwrb.getLocalizedString("name", "Name")), 2, row);
        row++;
        Map listMap = MailingListBusiness.mapOfMailingList(letter);
        boolean hasMap = listMap != null;
        List mlists = MailingListBusiness.listOfMailingList();
        if (mlists != null) {
            MailingList mlist;
            String color;
            CheckBox use;
            HiddenInput used;
            String prmUse = "let_use";
            Iterator iter = mlists.iterator();
            while (iter.hasNext()) {
                mlist = (MailingList) iter.next();
                use = new CheckBox(prmUse, String.valueOf(mlist.getID()));
                if (hasMap && listMap.containsKey(new Integer(mlist.getID()))) {
                    use.setChecked(true);
                    used = new HiddenInput("was_used", String.valueOf(mlist.getID()));
                    dTable.add(used);
                } else {
                    use.setChecked(false);
                }

                dTable.add(use, 1, row);
                dTable.add(Edit.formatText(mlist.getName()), 2, row);
                row++;
            }

        }
        dTable.add(new HiddenInput(prmLetter, String.valueOf(letter.getID())));
        dTable.addButton(new SubmitButton(iwrb.getLocalizedImageButton("save", "Save"), "save_letter_list"));
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
        dTable.addTitle(iwrb.getLocalizedString("email", "Email"));
        TextInput name = new TextInput(prmEmail);
        if (mlist != null) {
            //name.setContent(mlist.getName());
            dTable.add(new HiddenInput(prmList, String.valueOf(mlist.getID())));
        }
        dTable.add(Edit.formatText(iwrb.getLocalizedString("email_address", "Email address")), 1, 1);
        dTable.add(name, 2, 1);

        return dTable;
    }


    /**
     *  Gets the letterChangeLink of the Emailer object
     *
     * @param  letter  Description of the Parameter
     * @return         The letter change link value
     */
    public Link getLetterChangeLink(EmailLetter letter) {
        Link L = new Link(editImage);
        L.addParameter(prmLetter, letter.getID());
        L.setToolTip(iwrb.getLocalizedString("tooltip.edit_letter","Edit letter"));
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
        L.addParameter(prmLetterDelete, letter.getID());
        L.addParameter(prmLetter, letter.getID());
        L.setToolTip(iwrb.getLocalizedString("tooltip.delete_letter","Delete letter"));
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
        L.addParameter(prmLetter, letter.getID());
        L.setToolTip(iwrb.getLocalizedString("tooltip.mail_letter_sample","Mail letter to email list"));
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
        L.setToolTip(iwrb.getLocalizedString("tooltip.view_mail_list","View email list"));
        L.addParameter(prmLetter, letter.getID());
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
        L.addParameter(prmListId, list.getID());
        L.addParameter(prmLists, "true");
        L.setToolTip(iwrb.getLocalizedString("tooltip.delete_list","Delete list"));
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
        L.addParameter(prmListId, list.getID());
        L.addParameter(prmLists, "true");
        L.setToolTip(iwrb.getLocalizedString("tooltip.edit_list","Edit email list"));
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
        L.addParameter(prmEmailDelete, email.getID());
        L.addParameter(prmListId, mlist.getID());
        L.addParameter(prmLists, "true");
        L.setToolTip(iwrb.getLocalizedString("tooltip.remove_email","Remove email"));
        return L;
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
            drp.addMenuElement(types[i], iwrb.getLocalizedString(types[i], types[i]));
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
        iwb = getBundle(iwc);
        core = iwc.getIWMainApplication().getCoreBundle();
        editImage = core.getImage("/shared/edit.gif");
        deleteImage = core.getImage("/shared/delete.gif");
        mailImage = core.getImage("/shared/mail.gif");
        listImage = core.getImage("/shared/detach.gif");
        iwrb = getResourceBundle(iwc);
        control(iwc);
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
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        return obj;
    }
}