package com.idega.block.email.business;

import com.idega.block.email.data.*;
import com.idega.core.data.Email;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
/**
 *  Title: Description: Copyright: Copyright (c) 2001 Company:
 *
 * @author     <br>
 *      <a href="mailto:aron@idega.is">Aron Birkir</a> <br>
 *
 * @created    14. mars 2002
 * @version    1.0
 */

public class MailBusiness {

  private static MailBusiness letterBusiness;


  /**
   *  Gets the instance of the LetterBusiness class
   *
   * @return    The instance value
   */
  public static MailBusiness getInstance() {
    if (letterBusiness == null) {
      letterBusiness = new MailBusiness();
    }
    return letterBusiness;
  }


  /**
   * @param  id           Description of the Parameter
   * @param  name         Description of the Parameter
   * @param  info         Description of the Parameter
   * @param  iCategoryId  Description of the Parameter
   * @return              Description of the Return Value
   * @todo                Description of the Method
   */
  public MailGroup saveLetterGroup(int id, String name, String info, int iCategoryId) {
    try {
      MailGroup group = (MailGroup) com.idega.block.email.data.MailGroupBMPBean.getEntityInstance(MailGroup.class);
      boolean update = false;
      if (id > 0) {
        group.findByPrimaryKey(id);
        update = true;
      }

      group.setName(name);
      group.setDescription(info);
      group.setCategoryId(iCategoryId);

      if (update) {
        group.update();
      } else {
        group.setCreated(com.idega.util.IWTimeStamp.getTimestampRightNow());
        group.insert();
      }

      return group;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }


  /**
   * @param  id        Description of the Parameter
   * @param  name      Description of the Parameter
   * @param  info      Description of the Parameter
   * @param  iGroupId  Description of the Parameter
   * @return           Description of the Return Value
   * @todo             Description of the Method
   */
  public MailTopic saveTopic(int id, String name, String info, int iCategoryId) {
    try {
      MailTopic topic = (MailTopic) com.idega.block.email.data.MailTopicBMPBean.getEntityInstance(MailTopic.class);
      boolean update = false;
      if (id > 0) {
        topic.findByPrimaryKey(id);
        update = true;
      }

      topic.setName(name);
      topic.setDescription(info);
      topic.setCategoryId(iCategoryId);
      if (update) {
        topic.update();
      } else {
        MailList list = ((com.idega.block.email.data.MailListHome)com.idega.data.IDOLookup.getHomeLegacy(MailList.class)).createLegacy();
        list.setCreated(com.idega.util.IWTimeStamp.getTimestampRightNow());
        list.setName(topic.getName() + " list");
        list.setDescription(topic.getDescription());
        list.insert();
        topic.setListId(list.getID());
        topic.setCreated(com.idega.util.IWTimeStamp.getTimestampRightNow());
        topic.insert();
      }

      return topic;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }


  /**
   *  @todo Description of the Method
   *
   * @param  id        Description of the Parameter
   * @param  name      Description of the Parameter
   * @param  host      Description of the Parameter
   * @param  user      Description of the Parameter
   * @param  pass      Description of the Parameter
   * @param  protocol  Description of the Parameter
   * @param  entityId  Description of the Parameter
   * @return           Description of the Return Value
   */
  public MailAccount saveTopicAccount(int id, String name, String host, String user, String pass, int protocol, int entityId) {
    return saveAccount(id, name, host, user, pass, protocol, entityId, MailTopic.class);
  }


  /**
   *  @todo Description of the Method
   *
   * @param  id        Description of the Parameter
   * @param  name      Description of the Parameter
   * @param  host      Description of the Parameter
   * @param  user      Description of the Parameter
   * @param  pass      Description of the Parameter
   * @param  protocol  Description of the Parameter
   * @param  entityId  Description of the Parameter
   * @return           Description of the Return Value
   */
  public MailAccount saveGroupAccount(int id, String name, String host, String user, String pass, int protocol, int entityId) {
    return saveAccount(id, name, host, user, pass, protocol, entityId, MailGroup.class);
  }


  /**
   * @param  id           Description of the Parameter
   * @param  name         Description of the Parameter
   * @param  host         Description of the Parameter
   * @param  user         Description of the Parameter
   * @param  pass         Description of the Parameter
   * @param  protocol     Description of the Parameter
   * @param  entityId     Description of the Parameter
   * @param  entityClass  Description of the Parameter
   * @return              Description of the Return Value
   * @todo                Description of the Method
   */
  public MailAccount saveAccount(int id, String name, String host, String user, String pass, int protocol, int entityId, Class entityClass) {
    try {
      MailAccount account = (MailAccount) com.idega.block.email.data.MailAccountBMPBean.getEntityInstance(MailAccount.class);
      boolean update = false;
      if (id > 0) {
        account.findByPrimaryKey(id);
        update = true;
      }

      account.setName(name);
      account.setHost(host);
      account.setUser(user);
      account.setPassword(pass);
      account.setProtocol(protocol);

      if (update) {
        account.update();
      } else {
        account.insert();
      }

      if(entityClass!=null && entityId > 0)
      account.addTo(entityClass, entityId);

      return account;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }


  /**
   * @param  id           Description of the Parameter
   * @param  entityClass  Description of the Parameter
   * @todo                Description of the Method
   */
  private void deleteAccount(int id, Class entityClass) {
    try {
      MailAccount a = (MailAccount) com.idega.block.email.data.MailAccountBMPBean.getEntityInstance(MailAccount.class, id);
      a.removeFrom(entityClass);
      a.delete();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * @param  id  Description of the Parameter
   * @todo       Description of the Method
   */
  public void deleteGroupAccount(int id) {
    deleteAccount(id, MailGroup.class);
  }


  /**
   * @param  id  Description of the Parameter
   * @todo       Description of the Method
   */
  public void deleteTopicAccount(int id) {
    deleteAccount(id, MailTopic.class);
  }


  /**
   * @param  email    Description of the Parameter
   * @param  ListIds  Description of the Parameter
   * @todo            Description of the Method
   */
  public void saveEmailToLists(String email, int[] ListIds) {
    try {

      Email eEmail =MailFinder.getInstance().lookupEmail(email);
      if(eEmail == null){
        eEmail = ((com.idega.core.data.EmailHome)com.idega.data.IDOLookup.getHomeLegacy(Email.class)).createLegacy();
        eEmail.setEmailAddress(email);
        eEmail.insert();
      }

      for (int i = 0; i < ListIds.length; i++) {
        try{
        eEmail.addTo(MailList.class, ListIds[i]);
        }
        catch(Exception ex){}
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * @param  id  Description of the Parameter
   * @todo       Description of the Method
   */
  public void deleteTopic(int id) {
    try {

      ((com.idega.block.email.data.MailTopicHome)com.idega.data.IDOLookup.getHomeLegacy(MailTopic.class)).findByPrimaryKeyLegacy(id).delete();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * @param  id  Description of the Parameter
   * @todo       Description of the Method
   */
  public void deleteGroup(int id) {
    try {

      ((com.idega.block.email.data.MailGroupHome)com.idega.data.IDOLookup.getHomeLegacy(MailGroup.class)).findByPrimaryKeyLegacy(id).delete();

    } catch (Exception ex) {
      ex.printStackTrace();
    }

  }


  /**
   *  @todo Description of the Method
   *
   * @param  id           Description of the Parameter
   * @param  fromName     Description of the Parameter
   * @param  fromAddress  Description of the Parameter
   * @param  subject      Description of the Parameter
   * @param  body         Description of the Parameter
   * @param  type         Description of the Parameter
   * @param  topicId      Description of the Parameter
   * @return              Description of the Return Value
   */
  public MailLetter saveTopicLetter(int id, String fromName, String fromAddress, String subject, String body, int type, int topicId) {
    return saveMailLetter(id, fromName, fromAddress, subject, body, type, topicId, MailTopic.class);
  }


  /**
   *  @todo Description of the Method
   *
   * @param  id           Description of the Parameter
   * @param  fromName     Description of the Parameter
   * @param  fromAddress  Description of the Parameter
   * @param  subject      Description of the Parameter
   * @param  body         Description of the Parameter
   * @param  type         Description of the Parameter
   * @param  entityId     Description of the Parameter
   * @param  entityClass  Description of the Parameter
   * @return              Description of the Return Value
   */
  public MailLetter saveMailLetter(int id, String fromName, String fromAddress, String subject, String body, int type, int entityId, Class entityClass) {
    try {
      MailLetter letter = (MailLetter) com.idega.block.email.data.MailLetterBMPBean.getEntityInstance(MailLetter.class);
      boolean update = false;
      if (id > 0) {
        letter.findByPrimaryKey(id);
        update = true;
      }

      letter.setFromName(fromName);
      letter.setFromAddress(fromAddress);
      letter.setSubject(subject);
      letter.setBody(body);
      letter.setType(type);

      if (update) {
        letter.update();
      } else {
        letter.insert();
      }

      if (entityId > 0) {
        letter.addTo(entityClass, entityId);
      }

      return letter;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }


  /**
   *  @todo Description of the Method
   *
   * @param  letter  Description of the Parameter
   * @param  topic   Description of the Parameter
   */
  public void sendLetter(EmailLetter letter, MailTopic topic) {
    Collection accounts = MailFinder.getInstance().getTopicAccounts(topic.getID(), MailProtocol.SMTP);
    if (accounts == null || accounts.size() == 0) {
      accounts = MailFinder.getInstance().getGroupAccounts(topic.getGroupId(), MailProtocol.SMTP);
    }

    if (accounts != null && accounts.size() > 0) {
      Collection emails = MailFinder.getInstance().getListEmails(topic.getListId());
      Iterator iter = accounts.iterator();
      EmailAccount account = iter.hasNext() ? ((EmailAccount) iter.next()) : null;
      if (account != null) {
        ListServer server = new ListServer();
        server.sendMailLetter(letter, account, emails);
      }
    } else {
      System.err.println("unable to send mail: no account");
    }
  }

}

