package com.idega.block.email.presentation;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.idega.block.category.business.CategoryFinder;
import com.idega.block.category.data.ICCategory;
import com.idega.block.email.business.EmailAccount;
import com.idega.block.email.business.EmailLetter;
import com.idega.block.email.business.EmailTopic;
import com.idega.block.email.business.MailBusiness;
import com.idega.block.email.business.MailFinder;
import com.idega.block.email.business.MailProtocol;
import com.idega.block.email.data.MailAccount;
import com.idega.block.email.data.MailAccountHome;
import com.idega.block.email.data.MailLetter;
import com.idega.block.email.data.MailLetterHome;
import com.idega.core.contact.data.EmailDataView;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.util.TextFormat;

/**
 *  Title: Description: Copyright: Copyright (c) 2001 Company:
 *
 * @author     <br>
 *      <a href="mailto:aron@idega.is">Aron Birkir</a> <br>
 *
 * @created    14. mars 2002
 * @version    1.0
 */

public class SetupEditor extends Block {

  //private final static String prmGroupId = "eme_group";
  private final static String prmTopicId = "eme_topic";
  /**
   * @todo    Description of the Field
   */
  public final static String prmInstanceId = "eme_instance";
  private final static String prmAccountId = "eme_account";
  private final static String prmEdit = "eme_edit";
  private final static String prmNew = "eme_new";
  private final static String prmSave = "eme_save";
  private final static String prmView = "eme_view";
  private final static String prmDel = "eme_del";
  private final static String prmEmails = "eme_emails";
  private final static String prmWelcome = "eme_welc";
  private final static String prmEditTopic = "eme_tpced";

  private final static String EMAIL_BUNDLE_IDENTIFIER = "com.idega.block.email";

  private int instance = -1;
  private int topic = -1;
  private int account = -1;
  private int letterID = -1;
  private boolean New = false;
  private boolean Save = false;
  private boolean Edit = true;
  private boolean View = true;
  private String NewObject = "", EditObject = "";

  private Image editImage;
  private Image deleteImage;

  private boolean formAdded = false;

  private int category = -1;
  private IWBundle iwb, core;
  private IWResourceBundle iwrb;
  private Map categories;
  private Map topics;

  private TextFormat tf;


  /**
   *  Gets the bundleIdentifier of the SetupEditor object
   *
   * @return    The bundle identifier value
   */
  public String getBundleIdentifier() {
    return EMAIL_BUNDLE_IDENTIFIER;
  }


  /**
   * @param  iwc  Description of the Parameter
   * @todo        Description of the Method
   */
  public void main(IWContext iwc) {
    //debugParameters(iwc);
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);
    core = iwc.getIWMainApplication().getCoreBundle();

    deleteImage = core.getImage("/shared/delete.gif");
    editImage = core.getImage("/shared/edit.gif");

    if (iwc.isParameterSet(prmInstanceId)) {
      instance = Integer.parseInt(iwc.getParameter(prmInstanceId));
    }
    /*
    if (iwc.isParameterSet(prmGroupId)) {
      group = Integer.parseInt(iwc.getParameter(prmGroupId));
    }*/
    if (iwc.isParameterSet(prmTopicId)) {
      topic = Integer.parseInt(iwc.getParameter(prmTopicId));
    }
    if (iwc.isParameterSet(prmAccountId)) {
      account = Integer.parseInt(iwc.getParameter(prmAccountId));
    }
    
    if(iwc.isParameterSet(prmWelcome))
    	letterID = Integer.parseInt(iwc.getParameter(prmWelcome));


    // Heavy work :
    processForms(iwc);
    
    categories = CategoryFinder.getInstance().getMapOfCategoriesById(instance);
    topics = MailFinder.getInstance().mapOfTopics(instance);

    tf = TextFormat.getInstance();
    Table T = new Table(1, 3);
    T.add(getTopicsOverView(iwc), 1, 1);

  	if(iwc.isParameterSet(prmEmails))
  		T.add(getSubscribers(iwc),1,2);
  	else if(iwc.isParameterSet(prmWelcome))
  		T.add(getLetterForm(iwc),1,2);
  	else if(iwc.isParameterSet(prmEditTopic))
  		T.add(getTopicForm(iwc),1,2);
  	else if(iwc.isParameterSet(prmAccountId))
  		T.add(getAccountForm(iwc),1,2);
  		
    Form F = new Form();
    F.add(new HiddenInput(prmInstanceId, String.valueOf(instance)));
    //F.add(new HiddenInput(prmGroupId, String.valueOf(group)));
    F.add(new HiddenInput(prmTopicId, String.valueOf(topic)));
    F.add(T);
    add(F);
  }


  /**
   * @param  iwc  Description of the Parameter
   * @todo        Description of the Method
   */
  private void processForm(IWContext iwc) {
    if (iwc.isParameterSet("save") || iwc.isParameterSet("save.x")) {
      String savedObject = iwc.getParameter("save");
     /*
      if (savedObject.equals("group")) {
        saveGroup(iwc);
      } else
      */
      if (savedObject.equals("topic")) {
        saveTopic(iwc);
      } else if (savedObject.equals("account")) {
        saveAccount(iwc);
      }
    } else if (iwc.isParameterSet(prmDel)) {
      String delObject = iwc.getParameter(prmDel);
      /*
      if (delObject.equals("group")) {
        deleteGroup();
        group = -1;
      } else
      */
      if (delObject.equals("topic")) {
        deleteTopic();
        topic = -1;
      } else if (delObject.equals("account")) {
        deleteAccount();
        account = -1;
      }
    }
  }

  /**
   *  Gets the accounts of the SetupEditor object
   *
   * @param  iwc  Description of the Parameter
   * @return      The accounts value
   */
  public PresentationObject getAccounts(IWContext iwc) {
    DataTable T = new DataTable();
    T.setWidth("100%");

    String styp = "";
    Collection accounts = null;
    if (topic > 0) {
      accounts = MailFinder.getInstance().getTopicAccounts(topic);
      styp = iwrb.getLocalizedString("list.topic", "topic");
      EmailTopic tpc = (EmailTopic) topics.get(String.valueOf(topic));
      if(tpc !=null)
      styp += " " + tpc.getName();
    } /*else if (group > 0) {
      accounts = MailFinder.getInstance().getGroupAccounts(group);
      styp = iwrb.getLocalizedString("list.group", "group");
      EmailGroup grp = (EmailGroup) groups.get(String.valueOf(group));
      if(grp!=null)
      styp += " " + grp.getName();
    }*/

    String title = iwrb.getLocalizedString("list.accounts", "Accounts");
    title += " " + iwrb.getLocalizedString("list.in", "in") + " " + styp + " ";

    T.addTitle(title);
    T.setTitlesHorizontal(true);

    int row = 1;
    Text tName = tf.format(iwrb.getLocalizedString("name", "Name"));
    Text tHost = tf.format(iwrb.getLocalizedString("host", "Host"));
    Text tUser = tf.format(iwrb.getLocalizedString("user", "User"));
    Text tPass = tf.format(iwrb.getLocalizedString("pass", "Passwd"));
    Text tProto = tf.format(iwrb.getLocalizedString("protocol", "Protocol"));
    row++;
    TextInput name = new TextInput("name");
    TextInput host = new TextInput("host");
    TextInput user = new TextInput("user");
    TextInput pass = new TextInput("pass");
    DropdownMenu proto = getProtocolDropdown("proto", "");
    //DropdownMenu grps = getGroupDropdown("grp", "");
    if (accounts != null && accounts.size() > 0) {
      java.util.Iterator iter = accounts.iterator();
      EmailAccount acc;
      Link deleteLink;
      Link accountLink;
      Link editLink;
      int id;
      while (iter.hasNext()) {
        acc = (EmailAccount) iter.next();
        id = acc.getIdentifier().intValue();
        if (id == account && EditObject.equals("account")) {
          name.setContent(acc.getName());
          host.setContent(acc.getHost());
          user.setContent(acc.getUser());
          pass.setContent(acc.getPassword());
          proto.setSelectedElement(String.valueOf(acc.getProtocol()));
          T.add(tName, 1, row);
          T.add(name, 2, row++);
          T.add(tHost, 1, row);
          T.add(host, 2, row++);
          T.add(tUser, 1, row);
          T.add(user, 2, row++);
          T.add(tPass, 1, row);
          T.add(pass, 2, row++);
          T.add(tProto, 1, row);
          T.add(proto, 2, row++);

          T.add(new HiddenInput(prmAccountId, String.valueOf(id)));
          formAdded = true;
        } else {
          accountLink = new Link(tf.format(acc.getName()));
          accountLink.addParameter(prmInstanceId, String.valueOf(instance));
          accountLink.addParameter(prmAccountId, id);
          //accountLink.addParameter(prmGroupId, group);
          accountLink.addParameter(prmTopicId, topic);

          editLink = new Link((Image) editImage.clone());
          editLink.addParameter(prmInstanceId, String.valueOf(instance));
          editLink.addParameter(prmAccountId, id);
          editLink.addParameter(prmEdit, "account");
          //editLink.addParameter(prmGroupId, group);
          editLink.addParameter(prmTopicId, topic);

          deleteLink = new Link((Image) deleteImage.clone());
          deleteLink.addParameter(prmInstanceId, String.valueOf(instance));
          deleteLink.addParameter(prmAccountId, id);
          deleteLink.addParameter(prmDel, "account");
          //deleteLink.addParameter(prmGroupId, group);
          deleteLink.addParameter(prmTopicId, topic);

          T.add(accountLink, 1, row);
          T.add(editLink, 2, row);
          T.add(deleteLink, 2, row);
          row++;
          T.add(tHost, 1, row);
          T.add(tf.format(acc.getHost()), 2, row++);
          T.add(tUser, 1, row);
          T.add(tf.format(acc.getUser()), 2, row++);
          T.add(tPass, 1, row);
          T.add(tf.format(acc.getPassword()), 2, row++);
          T.add(tProto, 1, row);
          T.add(tf.format(getProtocolName(acc.getProtocol())), 2, row++);
        }
        row++;
      }
    }
    if (!formAdded && NewObject.equals("account")) {
      T.add(tName, 1, row);
      T.add(name, 2, row++);
      T.add(tHost, 1, row);
      T.add(host, 2, row++);
      T.add(tUser, 1, row);
      T.add(user, 2, row++);
      T.add(tPass, 1, row);
      T.add(pass, 2, row++);
      T.add(tProto, 1, row);
      T.add(proto, 2, row++);
      formAdded = true;
    } else {
      Link li = new Link(iwrb.getLocalizedImageButton("new", "New"));
      li.addParameter(prmInstanceId, String.valueOf(instance));
      //li.addParameter(prmGroupId, group);
      li.addParameter(prmTopicId, topic);
      li.addParameter(prmNew, "account");
      T.addButton(li);
    }
    if (formAdded) {
      T.addButton(new SubmitButton(iwrb.getLocalizedImageButton("save", "Save"), "save", "account"));
    }
    return T;
  }


  /**
   *  Gets the categoryDropdown of the SetupEditor object
   *
   * @param  name      Description of the Parameter
   * @param  selected  Description of the Parameter
   * @return           The category dropdown value
   */
  public DropdownMenu getCategoryDropdown(String name, String selected) {
    DropdownMenu drp = new DropdownMenu(name);
    if (categories != null) {
      Collection coll = categories.values();
      if (coll != null && coll.size() > 0) {
        drp.addMenuElements(coll);
      }
    } else {
      add("cats null");
    }
    if (selected != null) {
      drp.setSelectedElement(selected);
    }
    return drp;
  }


  /**
   *  Gets the groupDropdown of the SetupEditor object
   *
   * @param  name      Description of the Parameter
   * @param  selected  Description of the Parameter
   * @return           The group dropdown value
   */
  /*
  public DropdownMenu getGroupDropdown(String name, String selected) {
    DropdownMenu drp = new DropdownMenu(name);
    if (groups != null && groups.size() > 0) {
      drp.addMenuElements(groups.values());
    }
    if (selected != null) {
      drp.setSelectedElement(selected);
    }
    return drp;
  }

*/
  /**
   *  Gets the protocolDropdown of the SetupEditor object
   *
   * @param  name      Description of the Parameter
   * @param  selected  Description of the Parameter
   * @return           The protocol dropdown value
   */
  public DropdownMenu getProtocolDropdown(String name, String selected) {
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement(MailProtocol.SMTP, "SMTP");
    drp.addMenuElement(MailProtocol.POP3, "POP3");
    drp.addMenuElement(MailProtocol.IMAP4, "IMAP4");
    if (selected != null) {
      drp.setSelectedElement(selected);
    }
    return drp;
  }


  /**
   * @param  iwc  Description of the Parameter
   * @todo        Description of the Method
   */
   /*
  public void saveGroup(IWContext iwc) {
    String name = iwc.getParameter("name");
    String info = iwc.getParameter("info");
    int catId = Integer.parseInt(iwc.getParameter("cat"));
    MailBusiness.getInstance().saveLetterGroup(group, name, info, catId);
  }
*/

  /**
   * @param  iwc  Description of the Parameter
   * @todo        Description of the Method
   */
  public void saveTopic(IWContext iwc) {
    String name = iwc.getParameter("name");
    String info = iwc.getParameter("info");
    String senderName = iwc.getParameter("snd_name");
    String senderEmail = iwc.getParameter("snd_email");
    int catId = Integer.parseInt(iwc.getParameter("cat"));
    MailBusiness.getInstance().saveTopic(topic, name, info, catId,senderName,senderEmail);

  }


  /**
   * @param  iwc  Description of the Parameter
   * @todo        Description of the Method
   */
  public void saveAccount(IWContext iwc) {
    String name = iwc.getParameter("name");
    String host = iwc.getParameter("host");
    String user = iwc.getParameter("user");
    String pass = iwc.getParameter("pass");
    int proto = Integer.parseInt(iwc.getParameter("proto"));
    int ent = -1;
    if (topic >= 0) {
      if(account <0)
        ent = topic;
      MailBusiness.getInstance().saveTopicAccount(account, name, host, user, pass, proto, ent);
    }
    /*
    else if (group > 0) {
      if(account<=0)
        ent = group;
      MailBusiness.getInstance().saveGroupAccount(account, name, host, user, pass, proto, ent);
    }
    */
    else {
      debug("can not save");
    }
  }


  /**
   * @todo    Description of the Method
   */
   /*
  public void deleteGroup() {
    MailBusiness.getInstance().deleteGroup(group);
  }
*/

  /**
   * @todo    Description of the Method
   */
  public void deleteTopic() {
    MailBusiness.getInstance().deleteTopic(topic);
  }


  /**
   * @todo    Description of the Method
   */
  public void deleteAccount() {
    if (topic > 0) {
      MailBusiness.getInstance().deleteTopicAccount(account);
    }
  }


  /**
   *  Gets the protocolName of the SetupEditor object
   *
   * @param  protocol  Description of the Parameter
   * @return           The protocol name value
   */
  public String getProtocolName(int protocol) {
    switch (protocol) {
      case MailProtocol.SMTP:
        return "SMTP";
      case MailProtocol.POP3:
        return "POP3";
      case MailProtocol.IMAP4:
        return "IMAP4";
    }
    return "";
  }
  
  public PresentationObject getTopicsOverView(IWContext iwc){
  	Table T = new Table();
  	int row = 1;
 	T.add(getTopicLink(-1,iwrb.getLocalizedString("new_topic","New topic")),1,row);
 	row++;
  	T.add(tf.format(iwrb.getLocalizedString("name","Name"),tf.HEADER),1,row);
  	T.add(tf.format(iwrb.getLocalizedString("category","Category"),tf.HEADER),2,row);
  	T.add(tf.format(iwrb.getLocalizedString("mail_server","Mail server"),tf.HEADER),3,row);
  	T.add(tf.format(iwrb.getLocalizedString("subscribers","Subscribers"),tf.HEADER),4,row);
  	T.add(tf.format(iwrb.getLocalizedString("welcome","Welcome"),tf.HEADER),5,row);
  	row++;
  	if(!topics.isEmpty()){
  		Iterator iter = topics.values().iterator();
  		EmailTopic topic;
  		ICCategory category;
  		EmailAccount account;
  		EmailLetter welcome;
  		Collection welcomes;
  		Collection accounts;
  		int emailCount;
  		int topicID;
  		while(iter.hasNext()){
  			topic = (EmailTopic) iter.next();
  			topicID = topic.getIdentifier().intValue();
  			T.add(getTopicLink(topicID,topic.getName()),1,row);
  			
  			category = (ICCategory) categories.get(Integer.toString(topic.getCategoryId()));
  			T.add(tf.format(category.getName()),2,row);
  			accounts = MailFinder.getInstance().getTopicAccounts(topicID,MailProtocol.SMTP);
  			if(accounts!=null && !accounts.isEmpty()){
  				account = (EmailAccount) accounts.iterator().next();
  				T.add(getAccountLink(topicID,( account.getIdentifier().intValue()),account.getHost()),3,row);
  			}
  			else{
  				T.add(getAccountLink(topicID,-1,"X"),3,row);
  			}
  			emailCount = MailFinder.getInstance().getListEmailsCount(topic.getListId());
  			T.add((getSubscribersLink(topic.getListId(),String.valueOf(emailCount))),4,row);
  			welcomes = MailFinder.getInstance().getEmailLetters(topicID,MailLetter.TYPE_SUBSCRIPTION);
  			if(welcomes!=null && !welcomes.isEmpty()){
  				welcome = (MailLetter) welcomes.iterator().next();
  				T.add(getWelcomeLetterLink(welcome.getIdentifier().intValue(),topicID,welcome.getSubject()),5,row);
  				//T.add(tf.format(welcome.getSubject()),5,row);
  			}
  			else{
  				T.add(getWelcomeLetterLink(-1,topicID,"X"),5,row);
  			}
  			row++;
  		}
  	}
  	return T;
  }
  
  public Link getSubscribersLink(int topicID,String text){
  	Link L = new Link(tf.format(text));
  	L.addParameter(prmEmails,"true");
  	L.addParameter(prmInstanceId, String.valueOf(instance));
    L.addParameter(prmTopicId, topicID);
  	return L;
  }
  
  public Link getWelcomeLetterLink(int letterId,int topicID,String text){
  	Link L = new Link(tf.format(text));
  	L.addParameter(prmInstanceId, String.valueOf(instance));
  	L.addParameter(prmWelcome,String.valueOf(letterId));
    L.addParameter(prmTopicId, topicID);
  	return L;
  }
  
   public Link getTopicLink(int topicID,String text){
  	Link L = new Link(tf.format(text));
  	L.addParameter(prmInstanceId, String.valueOf(instance));
    L.addParameter(prmEditTopic,"true");
    L.addParameter(prmTopicId, topicID);
  	return L;
  }
  
  public Link getAccountLink(int topicID,int accountID,String text){
  	Link L = new Link(tf.format(text));
  	L.addParameter(prmInstanceId, String.valueOf(instance));
    L.addParameter(prmAccountId,String.valueOf(accountID));
    L.addParameter(prmTopicId, topicID);
  	return L;
  }
  
  
  public PresentationObject getSubscribers(IWContext iwc){
  	Table T = new Table();
  	int row= 1;
  	T.add(tf.format(iwrb.getLocalizedString("subscribing_emails","Subscribing emails"),tf.HEADER),1,row++);
  	
  	if (topic > 0) {
      Collection emails = MailFinder.getInstance().getListEmails(topic);
      if (emails != null) {
        Iterator iter = emails.iterator();
        while (iter.hasNext()) {
          T.add(tf.format(((EmailDataView) iter.next()).getEmailAddress()), 1, row++);
        }
      }
  	}
  	return T;
  }
  
  public PresentationObject getLetterForm(IWContext iwc){
    Table T = new Table();
    TextInput fromAddress = new TextInput("from_address");
    fromAddress.setLength(80);
    TextInput fromName = new TextInput("from_name");
    fromName.setLength(80);
    TextInput subject = new TextInput("subject");
    subject.setLength(80);
    TextArea body = new TextArea("body",70,20);
    if(letterID >0){
    	EmailLetter letter = null;
    	try {
			letter = ((MailLetterHome)IDOLookup.getHome(MailLetter.class)).findByPrimaryKey(new Integer(letterID));
			fromAddress.setContent(letter.getFromAddress());
			fromName.setContent(letter.getFromName());
			subject.setContent(letter.getSubject());
			body.setContent(letter.getBody());
		}
		catch (Exception e) {
		}
    	T.add(new HiddenInput(prmWelcome,String.valueOf(letterID)));	
    }
    
    int row = 1;

    T.add(tf.format(iwrb.getLocalizedString("letter.from_name","Sender name"),tf.HEADER),1,row);
    T.add(fromName,2,row++);
    T.add(tf.format(iwrb.getLocalizedString("letter.from_address","Sender address"),tf.HEADER),1,row);
    T.add(fromAddress,2,row++);
    T.add(tf.format(iwrb.getLocalizedString("letter.subject","Subject"),tf.HEADER),1,row);
    T.add(subject,2,row++);
    T.add(tf.format(iwrb.getLocalizedString("letter.body","Body"),tf.HEADER),1,row);
    T.add(body,2,row++);

    SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),"save_letter");
    SubmitButton delete = new SubmitButton(iwrb.getLocalizedImageButton("delete","Delete"),"remove_letter");
    //SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),"save");

	//CheckBox save = new CheckBox("save","true");
    Table submitTable = new Table(5,1);
		//submitTable.add(tf.format(iwrb.getLocalizedString("save_to_archive","Save to archive")),3,1);
    submitTable.add(save,3,1);
    submitTable.add(delete,5,1);
    //submitTable.add(send,5,1);
    T.add(submitTable,2,row);
    T.setAlignment(2,row,"right");


    return T;
  }
  
  public void processForms(IWContext iwc){
  	
    if(iwc.isParameterSet("save_letter.x") && topic>0){
		this.saveLetter(iwc);
    }
    else if(iwc.isParameterSet("save_topic.x")){
    	this.saveTopic(iwc);
    }
    else if(letterID>0 && iwc.isParameterSet("remove_letter.x")){
    	MailBusiness.getInstance().deleteLetter(letterID);
    }
    else if(iwc.isParameterSet("save_account.x")){
    	this.saveAccount(iwc);
    }
    else if(account>0 && iwc.isParameterSet("remove_account.x") ){
    	this.deleteAccount();
    }
    else if(topic>0 && iwc.isParameterSet("remove_topic.x") ){
    	this.deleteTopic();
    }
  }
  
  public void saveLetter(IWContext iwc){
  	
  	String fromname = iwc.getParameter("from_name");
	String fromaddress = iwc.getParameter("from_address");
	String subject = iwc.getParameter("subject");
	String body = iwc.getParameter("body");
    MailBusiness.getInstance().saveTopicLetter(letterID,fromname,fromaddress,subject,body,EmailLetter.TYPE_SUBSCRIPTION,topic);
  }
  
  public PresentationObject getTopicForm(IWContext iwc){
  	Table T = new Table();
  	int row = 1;

  	T.add(tf.format(iwrb.getLocalizedString("name", "Name"),tf.HEADER), 1, row++);
    T.add(tf.format(iwrb.getLocalizedString("description", "Description"),tf.HEADER), 1, row++);
	T.add(tf.format(iwrb.getLocalizedString("sender_name", "Sender name"),tf.HEADER), 1, row++);
	T.add(tf.format(iwrb.getLocalizedString("sender_email", "Sender email"),tf.HEADER), 1, row++);
    T.add(tf.format(iwrb.getLocalizedString("category", "Category"),tf.HEADER), 1, row++);
    row=1;
    TextInput name = new TextInput("name");
    TextInput info = new TextInput("info");
	TextInput snd_name = new TextInput("snd_name");
	TextInput snd_email = new TextInput("snd_email");
    //DropdownMenu grps = getGroupDropdown("grp", String.valueOf(group));
    DropdownMenu cats = getCategoryDropdown("cat","");
    if(topic>0){
    	EmailTopic top = (EmailTopic) topics.get(String.valueOf(topic));
    	if(top!=null){
    		name.setContent(top.getName());
    		info.setContent(top.getDescription());
    		snd_name.setContent(top.getSenderName());
			snd_email.setContent(top.getSenderEmail());
    		cats.setSelectedElement(top.getCategoryId());
    	}
    }    
    T.add(name,2,row++);
    T.add(info,2,row++);
	T.add(snd_name,2,row++);
	T.add(snd_email,2,row++);
    T.add(cats,2,row++);
    T.add(new SubmitButton(iwrb.getLocalizedImageButton("save", "Save"), "save_topic"),2,row);
     T.add(new SubmitButton(iwrb.getLocalizedImageButton("delete","Delete"),"remove_topic"),2,row);
    return T;
  }
  
  public PresentationObject getAccountForm(IWContext iwc){
  	Table T = new Table();
  	int row = 1;
  	TextInput name = new TextInput("name");
    TextInput host = new TextInput("host");
    TextInput user = new TextInput("user");
    TextInput pass = new TextInput("pass");
    T.add(tf.format(iwrb.getLocalizedString("name","Name"),tf.HEADER),1,row++);
    T.add(tf.format(iwrb.getLocalizedString("host","Host"),tf.HEADER),1,row++);
    T.add(tf.format(iwrb.getLocalizedString("user","User"),tf.HEADER),1,row++);
    T.add(tf.format(iwrb.getLocalizedString("pass","Passwd"),tf.HEADER),1,row++);
    T.add(new HiddenInput("proto",String.valueOf(MailProtocol.SMTP)));
    if(account > 0){
    	try {
			MailAccount acc =((MailAccountHome) IDOLookup.getHome(MailAccount.class)).findByPrimaryKey(new Integer(account));
			name.setContent(acc.getName());
			host.setContent(acc.getHost());
			user.setContent(acc.getUser());
			pass.setContent(acc.getPassword());
			T.add(new HiddenInput(prmAccountId,acc.getPrimaryKey().toString()));
		}
		catch (Exception e) {
		}
   	}
   	row = 1;
   	T.add(name,2,row++);
   	T.add(host,2,row++);
   	T.add(user,2,row++);
   	T.add(pass,2,row++);
   	T.add(new SubmitButton(iwrb.getLocalizedImageButton("save","Save"),"save_account"),2,row);;
    T.add(new SubmitButton(iwrb.getLocalizedImageButton("delete","Delete"),"remove_account"),2,row);;
    return T;
  }
  
}