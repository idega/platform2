package com.idega.block.email.presentation;

import com.idega.block.email.business.*;
import com.idega.core.business.CategoryFinder;
import com.idega.core.business.EmailDataView;
import com.idega.core.business.Category;
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
import com.idega.presentation.ui.TextInput;
import com.idega.util.text.TextFormat;
import java.util.Collection;
import java.util.Iterator;

import java.util.Map;

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

  private final static String prmGroupId = "eme_group";
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

  private final static String EMAIL_BUNDLE_IDENTIFIER = "com.idega.block.email";

  private int instance = -1;
  private int group = -1;
  private int topic = -1;
  private int account = -1;
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
  private Map groups;
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
    core = iwc.getApplication().getCoreBundle();

    deleteImage = core.getImage("/shared/delete.gif");
    editImage = core.getImage("/shared/edit.gif");

    if (iwc.isParameterSet(prmInstanceId)) {
      instance = Integer.parseInt(iwc.getParameter(prmInstanceId));
    }
    if (iwc.isParameterSet(prmGroupId)) {
      group = Integer.parseInt(iwc.getParameter(prmGroupId));
    }
    if (iwc.isParameterSet(prmTopicId)) {
      topic = Integer.parseInt(iwc.getParameter(prmTopicId));
    }
    if (iwc.isParameterSet(prmAccountId)) {
      account = Integer.parseInt(iwc.getParameter(prmAccountId));
    }

    if (iwc.isParameterSet(prmNew)) {
      NewObject = iwc.getParameter(prmNew);
    }

    if (iwc.isParameterSet(prmEdit)) {
      EditObject = iwc.getParameter(prmEdit);
    }

    Save = iwc.isParameterSet(prmSave);
    Edit = iwc.isParameterSet(prmEdit);

    Collection Coll = MailFinder.getInstance().getEmailGroups(instance);

    // Heavy work :
    processForm(iwc);
    groups = MailFinder.getInstance().mapOfEmailGroups(instance);
    categories = CategoryFinder.getInstance().getMapOfCategoriesById(instance);
    topics = MailFinder.getInstance().mapOfTopics(group);

    tf = TextFormat.getInstance();
    Table T = new Table(3, 2);
    T.setWidth("800");
    T.setWidth(2, "200");
    T.setWidth(3, "200");
    T.setColumnVerticalAlignment(2, "top");
    T.setColumnVerticalAlignment(3, "top");
    T.mergeCells(2, 1, 2, 2);
    T.mergeCells(3, 1, 3, 2);
    T.add(getGroups(iwc), 1, 1);
    if (group > 0) {
      T.add(getTopics(iwc), 1, 2);
    }
    if (group > 0 || topic > 0) {
      T.add(getAccounts(iwc), 2, 1);
    }
    if(topic > 0)
      T.add(getTopicEmails(iwc),3,1);
    Form F = new Form();
    F.add(new HiddenInput(prmInstanceId, String.valueOf(instance)));
    F.add(new HiddenInput(prmGroupId, String.valueOf(group)));
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
      if (savedObject.equals("group")) {
        saveGroup(iwc);
      } else if (savedObject.equals("topic")) {
        saveTopic(iwc);
      } else if (savedObject.equals("account")) {
        saveAccount(iwc);
      }
    } else if (iwc.isParameterSet(prmDel)) {
      String delObject = iwc.getParameter(prmDel);
      if (delObject.equals("group")) {
        deleteGroup();
        group = -1;
      } else if (delObject.equals("topic")) {
        deleteTopic();
        topic = -1;
      } else if (delObject.equals("account")) {
        deleteAccount();
        account = -1;
      }
    }
  }


  /**
   *  Gets the groups of the SetupEditor object
   *
   * @param  iwc  Description of the Parameter
   * @return      The groups value
   */
  public PresentationObject getGroups(IWContext iwc) {
    DataTable T = new DataTable();
    T.setWidth("100%");
    T.addTitle(iwrb.getLocalizedString("list.groups", "Groups"));
    T.setTitlesHorizontal(true);

    int row = 1;
    int col = 1;
    T.add(tf.format(iwrb.getLocalizedString("name", "Name")), 1, row);
    T.add(tf.format(iwrb.getLocalizedString("description", "Description")), 2, row);
    T.add(tf.format(iwrb.getLocalizedString("category", "Category")),3, row);
    row++;
    TextInput name = new TextInput("name");
    TextInput info = new TextInput("info");
    DropdownMenu cats = getCategoryDropdown("cat", "");

    if (groups != null && groups.size() > 0) {
      java.util.Iterator iter = groups.values().iterator();
      EmailGroup grp;
      Link deleteLink;
      Link groupLink;
      Link editLink;
      int id;
      while (iter.hasNext()) {
        col = 1;
        grp = (EmailGroup) iter.next();
        id = Integer.parseInt(grp.toString());
        if (id == group && EditObject.equals("group")) {
          name.setContent(grp.getName());
          if (grp.getDescription() != null) {
            info.setContent(grp.getDescription());
          }
          T.add(name, 1, row);
          T.add(info, 2, row);
          T.add(cats, 3, row);
          T.add(new HiddenInput(prmGroupId, String.valueOf(id)));
          formAdded = true;
        } else {
          groupLink = new Link(tf.format(grp.getName()));
          groupLink.addParameter(prmInstanceId, String.valueOf(instance));
          groupLink.addParameter(prmGroupId, id);
          T.add(groupLink, 1, row);
          T.add(tf.format(grp.getDescription()), 2, row);
          T.add(tf.format(((Category) categories.get(Integer.toString(grp.getCategoryId()))).getName()), 3, row);
          editLink = new Link((Image) editImage.clone());
          editLink.addParameter(prmInstanceId, String.valueOf(instance));
          editLink.addParameter(prmGroupId, id);
          editLink.addParameter(prmEdit, "group");
          T.add(editLink, 4, row);
          deleteLink = new Link((Image) deleteImage.clone());
          deleteLink.addParameter(prmInstanceId, String.valueOf(instance));
          deleteLink.addParameter(prmGroupId, id);
          deleteLink.addParameter(prmDel, "group");
          T.add(deleteLink, 5, row);

        }
        row++;
      }
    }
    if (!formAdded && NewObject.equals("group")) {
      T.add(name, 1, row);
      T.add(info, 2, row);
      T.add(cats, 3, row);
      formAdded = true;
    } else {
      Link li = new Link(iwrb.getLocalizedImageButton("new", "New"));
      li.addParameter(prmInstanceId, String.valueOf(instance));
      li.addParameter(prmNew, "group");
      T.addButton(li);
    }
    if (formAdded) {
      T.addButton(new SubmitButton(iwrb.getLocalizedImageButton("save", "Save"), "save", "group"));
    }
    return T;
  }


  /**
   *  Gets the topics of the SetupEditor object
   *
   * @param  iwc  Description of the Parameter
   * @return      The topics value
   */
  public PresentationObject getTopics(IWContext iwc) {
    DataTable T = new DataTable();
    T.setWidth("100%");
    String title = iwrb.getLocalizedString("list.topics", "Topics");
    title += " " + iwrb.getLocalizedString("list.ingroup", "in group") + " ";

    EmailGroup grp = (EmailGroup) groups.get(String.valueOf(group));
    title += grp.getName();

    T.addTitle(title);
    T.setTitlesHorizontal(true);

    int row = 1;
    int col = 1;
    T.add(tf.format(iwrb.getLocalizedString("name", "Name")), 1, row);
    T.add(tf.format(iwrb.getLocalizedString("description", "Description")), 2, row);
    T.add(tf.format(iwrb.getLocalizedString("category", "Category")), 3, row);
    row++;
    TextInput name = new TextInput("name");
    TextInput info = new TextInput("info");
    DropdownMenu grps = getGroupDropdown("grp", String.valueOf(group));


    if (topics != null && topics.size() > 0) {
      java.util.Iterator iter = topics.values().iterator();
      EmailTopic tpc;
      Link deleteLink;
      Link groupLink;
      Link editLink;
      int id;
      while (iter.hasNext()) {
        col = 1;
        tpc = (EmailTopic) iter.next();
        id = Integer.parseInt(tpc.toString());
        if (id == topic && EditObject.equals("topic")) {
          name.setContent(tpc.getName());
          if (tpc.getDescription() != null) {
            info.setContent(tpc.getDescription());
          }
          T.add(name, 1, row);
          T.add(info, 2, row);
          T.add(grps, 3, row);
          T.add(new HiddenInput(prmTopicId, String.valueOf(id)));
          formAdded = true;
        } else {
          groupLink = new Link(tf.format(tpc.getName()));
          groupLink.addParameter(prmInstanceId, String.valueOf(instance));
          groupLink.addParameter(prmGroupId, group);
          groupLink.addParameter(prmTopicId, id);
          T.add(groupLink, 1, row);
          T.add(tf.format(tpc.getDescription()), 2, row);
          T.add(tf.format(((EmailGroup) groups.get(Integer.toString(tpc.getGroupId()))).getName()), 3, row);
          editLink = new Link((Image) editImage.clone());
          editLink.addParameter(prmInstanceId, String.valueOf(instance));
          editLink.addParameter(prmTopicId, id);
          editLink.addParameter(prmEdit, "topic");
          editLink.addParameter(prmGroupId, group);
          T.add(editLink, 4, row);
          deleteLink = new Link((Image) deleteImage.clone());
          deleteLink.addParameter(prmInstanceId, String.valueOf(instance));
          deleteLink.addParameter(prmTopicId, id);
          deleteLink.addParameter(prmGroupId, group);
          deleteLink.addParameter(prmDel, "topic");
          T.add(deleteLink, 5, row);

        }
        row++;
      }
    }
    if (!formAdded && NewObject.equals("topic")) {
      T.add(name, 1, row);
      T.add(info, 2, row);
      T.add(grps, 3, row);
      formAdded = true;
    } else {
      Link li = new Link(iwrb.getLocalizedImageButton("new", "New"));
      li.addParameter(prmInstanceId, String.valueOf(instance));
      li.addParameter(prmGroupId, group);
      li.addParameter(prmNew, "topic");
      T.addButton(li);
    }
    if (formAdded) {
      T.addButton(new SubmitButton(iwrb.getLocalizedImageButton("save", "Save"), "save", "topic"));
    }

    return T;
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
    } else if (group > 0) {
      accounts = MailFinder.getInstance().getGroupAccounts(group);
      styp = iwrb.getLocalizedString("list.group", "group");
      EmailGroup grp = (EmailGroup) groups.get(String.valueOf(group));
      if(grp!=null)
      styp += " " + grp.getName();
    }

    String title = iwrb.getLocalizedString("list.accounts", "Accounts");
    title += " " + iwrb.getLocalizedString("list.in", "in") + " " + styp + " ";

    T.addTitle(title);
    T.setTitlesHorizontal(true);

    int row = 1;
    int col = 1;
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
    DropdownMenu grps = getGroupDropdown("grp", "");
    if (accounts != null && accounts.size() > 0) {
      java.util.Iterator iter = accounts.iterator();
      EmailAccount acc;
      Link deleteLink;
      Link accountLink;
      Link editLink;
      int id;
      while (iter.hasNext()) {
        col = 1;
        acc = (EmailAccount) iter.next();
        id = Integer.parseInt(acc.toString());
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
          accountLink.addParameter(prmGroupId, group);
          accountLink.addParameter(prmTopicId, topic);

          editLink = new Link((Image) editImage.clone());
          editLink.addParameter(prmInstanceId, String.valueOf(instance));
          editLink.addParameter(prmAccountId, id);
          editLink.addParameter(prmEdit, "account");
          editLink.addParameter(prmGroupId, group);
          editLink.addParameter(prmTopicId, topic);

          deleteLink = new Link((Image) deleteImage.clone());
          deleteLink.addParameter(prmInstanceId, String.valueOf(instance));
          deleteLink.addParameter(prmAccountId, id);
          deleteLink.addParameter(prmDel, "account");
          deleteLink.addParameter(prmGroupId, group);
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
      li.addParameter(prmGroupId, group);
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
   *  Gets the topicEmails of the SetupEditor object
   *
   * @param  iwc  Description of the Parameter
   * @return      The topic emails value
   */
  public PresentationObject getTopicEmails(IWContext iwc) {
    DataTable T = new DataTable();
    T.setWidth("100%");
    T.addTitle(iwrb.getLocalizedString("list.topic_emails", "Topic emails"));
    T.setUseTitles(false);
    EmailTopic tpc = (EmailTopic) topics.get(String.valueOf(topic));
    if (tpc != null) {
      Collection emails = MailFinder.getInstance().getListEmails(tpc.getListId());
      int row = 1;
      if (emails != null) {
        Iterator iter = emails.iterator();
        while (iter.hasNext()) {
          T.add(tf.format(((EmailDataView) iter.next()).getEmailAddress()), 1, row++);
        }
      }
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
  public void saveGroup(IWContext iwc) {
    String name = iwc.getParameter("name");
    String info = iwc.getParameter("info");
    int catId = Integer.parseInt(iwc.getParameter("cat"));
    MailBusiness.getInstance().saveLetterGroup(group, name, info, catId);
  }


  /**
   * @param  iwc  Description of the Parameter
   * @todo        Description of the Method
   */
  public void saveTopic(IWContext iwc) {
    String name = iwc.getParameter("name");
    String info = iwc.getParameter("info");
    int grpId = Integer.parseInt(iwc.getParameter("grp"));
    MailBusiness.getInstance().saveTopic(topic, name, info, grpId);

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
    else if (group > 0) {
      if(account<=0)
        ent = group;
      MailBusiness.getInstance().saveGroupAccount(account, name, host, user, pass, proto, ent);
    }
    else {
      debug("can not save");
    }
  }


  /**
   * @todo    Description of the Method
   */
  public void deleteGroup() {
    MailBusiness.getInstance().deleteGroup(group);
  }


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
    } else if (group > 0) {
      MailBusiness.getInstance().deleteGroupAccount(account);
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
}
