package com.idega.block.email.business;
import com.idega.block.email.data.MailAccount;
import com.idega.block.email.data.MailGroup;
import com.idega.block.email.data.MailLetter;
import com.idega.block.email.data.MailList;
import com.idega.block.email.data.MailTopic;

import com.idega.core.data.Email;
import com.idega.core.business.CategoryFinder;
import com.idega.core.data.Email;
import com.idega.data.EntityFinder;
import com.idega.data.IDOFinderException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 *  Title: Description: Copyright: Copyright (c) 2001 Company:
 *
 * @author     <br>
 *      <a href="mailto:aron@idega.is">Aron Birkir</a> <br>
 *
 * @created    9. mars 2002
 * @version    1.0
 */

public class MailFinder {

  private static MailFinder letterFinder;


  /**
   *  Gets a static instance of the LetterFinder class
   *
   * @return    The instance value
   */
  public static MailFinder getInstance() {
    if (letterFinder == null) {
      letterFinder = new MailFinder();
    }
    return letterFinder;
  }


  /**
   *  Returns a Collection of Email objects bound to a specified EmailList
   *
   * @param  l  - the EmailList object bound
   * @return    a collection of emails in the specified emaillist
   */
  public Collection getEmails(MailList l) {
    try {
      return EntityFinder.getInstance().findRelatedOrdered(l, Email.class, Email.getColumnNameAddress(), true);
    } catch (IDOFinderException ex) {

    }
    return null;
  }


  /**
   *  Returns a Collection of Letter objects bound to a specified Topic
   *
   * @param  t  - the letter topic
   * @return    a collection of the letters contained in specified topic.
   */
  public Collection getEmailLetters(MailTopic t) {
    try {
      return EntityFinder.getInstance().findRelatedOrdered(t, MailLetter.class, MailLetter.CREATED, true);
    } catch (IDOFinderException ex) {}
    return null;
  }

  public Collection getEmailLetters(int topicId) {
    try {
      MailTopic t = new MailTopic(topicId);
      return EntityFinder.getInstance().findRelatedOrdered(t, MailLetter.class, MailLetter.CREATED, true);
    } catch (IDOFinderException ex) {}
    catch(Exception ex){}
    return null;
  }


  /**
   *  Gets the letterGroups
   *
   * @param  ICObjectInstanceId  Description of the Parameter
   * @return                     The letter groups value
   */
  public Collection getEmailGroups(int ICObjectInstanceId) {
    return CategoryFinder.getInstance().listOfCategoryEntityByInstanceId(MailGroup.class, ICObjectInstanceId);
  }


  /**
   * @param  ICObjectInstanceId  Description of the Parameter
   * @return                     Description of the Return Value
   * @todo                       Description of the Method
   */
  public Map mapOfEmailGroups(int ICObjectInstanceId) {
    return EntityFinder.getInstance().getMapOfEntity(getEmailGroups(ICObjectInstanceId), ((MailGroup) MailGroup.getEntityInstance(MailGroup.class)).getIDColumnName());
  }


  /**
   *  Gets the instanceTopics
   *
   * @param  ICObjectInstanceId  Description of the Parameter
   * @return                     The instance topics value
   */
  public Collection getInstanceTopics(int ICObjectInstanceId) {
    return CategoryFinder.getInstance().getCategoryRelatedEntityFromInstanceId(MailGroup.class, MailTopic.class, MailTopic.GROUP, ICObjectInstanceId);
  }


  /**
   *  Gets the groupAccounts of the LetterFinder object
   *
   * @param  GroupId  Description of the Parameter
   * @return          The group accounts value
   */
  public Collection getGroupAccounts(int GroupId) {
    try {
      Collection c = EntityFinder.getInstance().findRelated(MailGroup.getEntityInstance(MailGroup.class, GroupId), MailAccount.class);
      return c;
    } catch (IDOFinderException ex) {

    }
    return null;
  }


  /**
   *  Gets the topicAccounts of the LetterFinder object
   *
   * @param  TopicId  Description of the Parameter
   * @return          The topic accounts value
   */
  public Collection getTopicAccounts(int TopicId) {
    try {
      Collection c = EntityFinder.getInstance().findRelated(MailGroup.getEntityInstance(MailTopic.class, TopicId), MailAccount.class);
      return c;
    } catch (IDOFinderException ex) {

    }
    return null;
  }


  /**
   *  Gets the topicAccounts of the MailFinder object
   *
   * @param  TopicId   Description of the Parameter
   * @param  protocol  Description of the Parameter
   * @return           The topic accounts value
   */
  public Collection getTopicAccounts(int TopicId, int protocol) {
    Collection accounts = getTopicAccounts(TopicId);
    if (accounts != null && accounts.size() > 0) {
      Iterator iter = accounts.iterator();
      MailAccount ma;
      while (iter.hasNext()) {
        ma = (MailAccount) iter.next();
        if (ma.getProtocol() != protocol) {
          iter.remove();
        }
      }
      return accounts;
    }
    else
      System.err.println("topic accounts empty");
    return null;
  }


  /**
   *  Gets the groupAccounts of the MailFinder object
   *
   * @param  GroupId   Description of the Parameter
   * @param  protocol  Description of the Parameter
   * @return           The group accounts value
   */
  public Collection getGroupAccounts(int GroupId, int protocol) {
    Collection accounts = getGroupAccounts(GroupId);
    if (accounts != null && accounts.size() > 0) {
      Iterator iter = accounts.iterator();
      MailAccount ma;
      while (iter.hasNext()) {
        ma = (MailAccount) iter.next();
        if (ma.getProtocol() != protocol) {
          iter.remove();
        }
      }
      return accounts;
    }
   else
    System.err.println("group accounts empty");
    return null;
  }


  /**
   *  Gets the topics of the LetterFinder object
   *
   * @param  GroupId  Description of the Parameter
   * @return          The topics value
   */
  public Collection getTopics(int GroupId) {
    try {
      Collection L = EntityFinder.getInstance().findAllByColumn(MailTopic.class, MailTopic.GROUP, GroupId);
      return L;
    } catch (Exception ex) {

    }
    return null;
  }

  public MailTopic getTopic(int id){
    try {
      return (MailTopic) MailTopic.getEntityInstance(MailTopic.class,id);
    }
    catch (Exception ex) {

    }
    return null;
  }


  /**
   *  Gets the Email objects bound to a specifiedList
   *
   * @param  listId  Description of the Parameter
   * @return         The list emails value
   */
  public Collection getListEmails(int listId) {
    try {
      return EntityFinder.getInstance().findRelated(MailList.getEntityInstance(MailList.class, listId), Email.class);
    } catch (Exception ex) {

    }
    return null;
  }


  /**
   * @param  GroupId  Description of the Parameter
   * @return          Description of the Return Value
   * @todo            Description of the Method
   */
  public Map mapOfTopics(int GroupId) {
    return EntityFinder.getInstance().getMapOfEntity(getTopics(GroupId), ((MailTopic) MailTopic.getEntityInstance(MailTopic.class)).getIDColumnName());
  }

  public Email lookupEmail(String EmailAddress){
    try {
      EntityFinder.debug = true;
      java.util.List c = EntityFinder.getInstance().findAllByColumn(Email.class,Email.getColumnNameAddress(),EmailAddress);
      EntityFinder.debug = false;
      if(c!=null && c.size() > 0)
        return (Email) c.get(0);
    }
    catch (Exception ex) {

    }
    return null;
  }

  public String getProtocolName(int protocol){
    String p = "pop3";
    switch (protocol) {
      case MailProtocol.POP3: p="pop3";break;
      case MailProtocol.SMTP: p="smtp";break;
      case MailProtocol.IMAP4: p="imap";break;
    }
    return p;
  }
}
