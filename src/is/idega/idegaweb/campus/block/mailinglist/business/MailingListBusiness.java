package is.idega.idegaweb.campus.block.mailinglist.business;


/**
 *  Title: Description: Copyright: Copyright (c) 2001 Company:
 *
 * @author     <br>
 *      <a href="mailto:aron@idega.is">Aron Birkir</a> <br>
 *
 * @created    9. mars 2002
 * @version    1.0
 */
/** @deprecated */
public class MailingListBusiness {


//
//    /**  @todo Description of the Field */
//    public static String CATEGORYTYPE = "cam_mail";
//
//
//    /**
//     *  @todo Description of the Method
//     *
//     * @param  iCategoryId  Description of the Parameter
//     * @param  name         Description of the Parameter
//     * @return              Description of the Return Value
//     */
//    public static MailingList createMailingList(int iCategoryId, String name) {
//        return saveMailingList(iCategoryId, -1, name);
//    }
//
//
//    /**
//     *  @todo Description of the Method
//     *
//     * @param  mlist     Description of the Parameter
//     * @param  email_id  Description of the Parameter
//     */
//    public static void removeEmail(MailingList mlist, int email_id) {
//        try {
//            ((com.idega.core.data.EmailHome)com.idega.data.IDOLookup.getHomeLegacy(Email.class)).findByPrimaryKeyLegacy(email_id).removeFrom(mlist);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//    }
//
//
//    /**
//     *  @todo Description of the Method
//     *
//     * @param  iCategoryId     Description of the Parameter
//     * @param  iMailingListId  Description of the Parameter
//     * @param  name            Description of the Parameter
//     * @return                 Description of the Return Value
//     */
//    public static MailingList updateMailingList(int iCategoryId, int iMailingListId, String name) {
//        return saveMailingList(iCategoryId, iMailingListId, name);
//    }
//
//
//    /**
//     *  Gets the mailingList of the MailingListBusiness class
//     *
//     * @param  id  Description of the Parameter
//     * @return     The mailing list value
//     */
//    public static MailingList getMailingList(int id) {
//        try {
//            return ((is.idega.idegaweb.campus.block.mailinglist.data.MailingListHome)com.idega.data.IDOLookup.getHomeLegacy(MailingList.class)).findByPrimaryKeyLegacy(id);
//        } catch (Exception ex) {
//
//        }
//        return null;
//    }
//
//
//    /**
//     *  Gets the emailLetter of the MailingListBusiness class
//     *
//     * @param  id  Description of the Parameter
//     * @return     The email letter value
//     */
//    public static EmailLetter getEmailLetter(int id) {
//        try {
//            return ((is.idega.idegaweb.campus.block.mailinglist.data.EmailLetterHome)com.idega.data.IDOLookup.getHomeLegacy(EmailLetter.class)).findByPrimaryKeyLegacy(id);
//        } catch (Exception ex) {
//
//        }
//        return null;
//    }
//
//
//    /**
//     *  @todo Description of the Method
//     *
//     * @param  iCategoryId    Description of the Parameter
//     * @param  MailingListId  Description of the Parameter
//     * @param  name           Description of the Parameter
//     * @return                Description of the Return Value
//     */
//    public static MailingList saveMailingList(int iCategoryId, int MailingListId, String name) {
//        try {
//            boolean update = false;
//            MailingList mlist = ((is.idega.idegaweb.campus.block.mailinglist.data.MailingListHome)com.idega.data.IDOLookup.getHomeLegacy(MailingList.class)).createLegacy();
//            if (MailingListId > 0) {
//                mlist = ((is.idega.idegaweb.campus.block.mailinglist.data.MailingListHome)com.idega.data.IDOLookup.getHomeLegacy(MailingList.class)).findByPrimaryKeyLegacy(MailingListId);
//                update = true;
//            }
//            mlist.setName(name);
//            mlist.setCreated(IWTimestamp.getTimestampRightNow());
//            if (iCategoryId > 0) {
//                mlist.setCategoryId(iCategoryId);
//            }
//            if (update) {
//                mlist.update();
//            } else {
//                mlist.insert();
//            }
//            return mlist;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
//
//
//    /**
//     *  Adds a feature to the Email attribute of the MailingListBusiness class
//     *
//     * @param  iMailingListId  The feature to be added to the Email attribute
//     * @param  address         The feature to be added to the Email attribute
//     * @return                 Description of the Return Value
//     */
//    public static boolean addEmail(int iMailingListId, String address) {
//        try {
//            Email email = ((com.idega.core.data.EmailHome)com.idega.data.IDOLookup.getHomeLegacy(Email.class)).createLegacy();
//            email.setEmailAddress(address);
//            email.insert();
//            MailingList mlist = ((is.idega.idegaweb.campus.block.mailinglist.data.MailingListHome)com.idega.data.IDOLookup.getHomeLegacy(MailingList.class)).findByPrimaryKeyLegacy(iMailingListId);
//            email.addTo(mlist);
//            return true;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return false;
//    }
//
//
//    /**
//     *  Adds a feature to the Email attribute of the MailingListBusiness class
//     *
//     * @param  mlist    The feature to be added to the Email attribute
//     * @param  address  The feature to be added to the Email attribute
//     * @return          Description of the Return Value
//     */
//    public static boolean addEmail(MailingList mlist, String address) {
//        if (mlist != null) {
//            try {
//                Email email = ((com.idega.core.data.EmailHome)com.idega.data.IDOLookup.getHomeLegacy(Email.class)).createLegacy();
//                email.setEmailAddress(address);
//                email.insert();
//                email.addTo(mlist);
//                return true;
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//        return false;
//    }
//
//
//    /**
//     *  Adds a feature to the Email attribute of the MailingListBusiness class
//     *
//     * @param  iMailingListId  The feature to be added to the Email attribute
//     * @param  emails          The feature to be added to the Email attribute
//     * @return                 Description of the Return Value
//     */
//    public static boolean addEmail(int iMailingListId, List emails) {
//
//        try {
//            MailingList mlist = ((is.idega.idegaweb.campus.block.mailinglist.data.MailingListHome)com.idega.data.IDOLookup.getHomeLegacy(MailingList.class)).findByPrimaryKeyLegacy(iMailingListId);
//            EntityBulkUpdater bulk = new EntityBulkUpdater(mlist);
//            bulk.addAll(emails, bulk.addto);
//
//            return true;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return false;
//    }
//
//
//    /**
//     *  @todo Description of the Method
//     *
//     * @param  letterId  Description of the Parameter
//     * @param  holder    Description of the Parameter
//     * @return           Description of the Return Value
//     */
//    public static boolean sendMail(IWApplicationContext iwac,int letterId, EntityHolder holder) {
//        try {
//            EmailLetter letter = ((is.idega.idegaweb.campus.block.mailinglist.data.EmailLetterHome)com.idega.data.IDOLookup.getHomeLegacy(EmailLetter.class)).findByPrimaryKeyLegacy(letterId);
//            return sendMail(iwac,letter, holder);
//        } catch (Exception ex) {
//
//        }
//        return false;
//    }
//
//
//    /**
//     *  @todo Description of the Method
//     *
//     * @param  iContractId  Description of the Parameter
//     * @param  type         Description of the Parameter
//     * @return              Description of the Return Value
//     */
//    public static boolean processMailEvent(IWApplicationContext iwac ,int iContractId, String type) {
//        return processMailEvent(iwac,new EntityHolder(iContractId), type);
//    }
//
//
//    /**
//     *  @todo Description of the Method
//     *
//     * @param  holder  Description of the Parameter
//     * @param  type    Description of the Parameter
//     * @return         Description of the Return Value
//     */
//    public static boolean processMailEvent(IWApplicationContext iwac, EntityHolder holder, String type) {
//        try {
//        	IWBundle bundle = iwac.getApplication().getBundle(Campus.CAMPUS_BUNDLE_IDENTIFIER);
//        	if(bundle.getProperty("no_mailevents","false")!=null && bundle.getProperty("no_mailevents").equalsIgnoreCase("true")){
//        		System.err.println("not sending any mail although requested");
//        		return false;
//        	}
//            System.err.println("Sending email of type : " + type);
//            List letters = EntityFinder.findAllByColumn(((is.idega.idegaweb.campus.block.mailinglist.data.EmailLetterHome)com.idega.data.IDOLookup.getHomeLegacy(EmailLetter.class)).createLegacy(), is.idega.idegaweb.campus.block.mailinglist.data.EmailLetterBMPBean.TYPE, type);
//            if (letters != null) {
//                System.err.println("Number of letters : " + letters.size());
//                java.util.Iterator iter = letters.iterator();
//                EmailLetter letter;
//                while (iter.hasNext()) {
//                    letter = (EmailLetter) iter.next();
//                    sendMail(iwac,letter, holder);
//                }
//                return true;
//            } else {
//                System.err.println("no letters to send");
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return true;
//    }
//
//
//    /**
//     *  Parses an email letter and sends it to all recipients
//     *
//     * @param  letter  Description of the Parameter
//     * @param  holder  Description of the Parameter
//     * @return         Description of the Return Value
//     */
//    public static boolean sendMail(IWApplicationContext iwac,EmailLetter letter, EntityHolder holder) {
//        try {
//            String Body = letter.getBody();
//            List holderEmails = new Vector();
//            if (holder != null && letter.getParse()) {
//                LetterParser parser = new LetterParser(holder);
//                Body = new ContentParser().parse(iwac,parser, Body);
//                if(!letter.getOnlyUser())
//                holderEmails = holder.getEmails();
//            }
//            String subject = letter.getSubject();
//
//            List emails = new Vector();
//
//
//            MailingList mlist;
//
//              List lists = EntityFinder.findRelated(letter, ((is.idega.idegaweb.campus.block.mailinglist.data.MailingListHome)com.idega.data.IDOLookup.getHomeLegacy(MailingList.class)).createLegacy());
//              if(lists != null) {
//                Iterator mIter = lists.iterator();
//                List temp;
//                while (mIter.hasNext()) {
//                    mlist = (MailingList) mIter.next();
//                    temp = EntityFinder.findRelated(mlist, ((com.idega.core.data.EmailHome)com.idega.data.IDOLookup.getHomeLegacy(Email.class)).createLegacy());
//                    if (temp != null) {
//                        emails.addAll(temp);
//                    }
//                }
//              }
//
//            if (emails != null && !emails.isEmpty()) {
//                Iterator eIter = emails.iterator();
//                Email email;
//                while (eIter.hasNext()) {
//                    email = (Email) eIter.next();
//                    holderEmails.add(email.getEmailAddress());
//                }
//
//            }
//            if (holderEmails != null && !holderEmails.isEmpty()) {
//                Iterator eIter = holderEmails.iterator();
//                String email;
//                while (eIter.hasNext()) {
//                    email = (String) eIter.next();
//                    System.err.println("Sending letter to " + email);
//                    SendMail.send(letter.getFrom(), email, "", "", letter.getHost(), subject, Body);
//                }
//            }
//            return true;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return false;
//    }
//
//
//    /**
//     *  Gets the emailSubject of the MailingListBusiness class
//     *
//     * @param  letter  Description of the Parameter
//     * @param  iwrb    Description of the Parameter
//     * @return         The email subject value
//     */
//    public static String getEmailSubject(EmailLetter letter, IWResourceBundle iwrb) {
//        String subject = iwrb.getLocalizedString(letter.getSubjectKey());
//        if (subject == null) {
//            return "";
//        } else {
//            return subject;
//        }
//    }
//
//
//    /**
//     *  Sets the emailSubject attribute of the MailingListBusiness class
//     *
//     * @param  letter   The new emailSubject value
//     * @param  iwrb     The new emailSubject value
//     * @param  subject  The new emailSubject value
//     */
//    public static void setEmailSubject(EmailLetter letter, IWResourceBundle iwrb, String subject) {
//        iwrb.setString(letter.getSubjectKey(), subject);
//        //iwrb.storeState();
//    }
//
//
//    /**
//     *  Gets the emailBody of the MailingListBusiness class
//     *
//     * @param  letter  Description of the Parameter
//     * @param  iwrb    Description of the Parameter
//     * @return         The email body value
//     */
//    public static String getEmailBody(EmailLetter letter, IWResourceBundle iwrb) {
//        String subject = iwrb.getLocalizedString(letter.getEmailKey());
//        if (subject == null) {
//            return "";
//        } else {
//            return subject;
//        }
//    }
//
//
//    /**
//     *  Sets the emailBody attribute of the MailingListBusiness class
//     *
//     * @param  letter  The new emailBody value
//     * @param  iwrb    The new emailBody value
//     * @param  body    The new emailBody value
//     */
//    public static void setEmailBody(EmailLetter letter, IWResourceBundle iwrb, String body) {
//        iwrb.setString(letter.getEmailKey(), body);
//
//        //iwrb.storeState();
//    }
//
//
//    /**
//     *  @todo Description of the Method
//     *
//     * @param  iEmailLetterId  Description of the Parameter
//     * @param  sHost           Description of the Parameter
//     * @param  sFrom           Description of the Parameter
//     * @param  subject         Description of the Parameter
//     * @param  body            Description of the Parameter
//     * @param  Parse           Description of the Parameter
//     * @param  OnlyUser        Description of the Parameter
//     * @param  type            Description of the Parameter
//     * @return                 Description of the Return Value
//     */
//    public static EmailLetter saveEmailLetter(int iEmailLetterId, String sHost, String sFrom, String subject, String body, boolean Parse, boolean OnlyUser, String type) {
//        EmailLetter letter = null;
//        try {
//            boolean update = false;
//            letter = ((is.idega.idegaweb.campus.block.mailinglist.data.EmailLetterHome)com.idega.data.IDOLookup.getHomeLegacy(EmailLetter.class)).createLegacy();
//            if (iEmailLetterId > 0) {
//                letter = ((is.idega.idegaweb.campus.block.mailinglist.data.EmailLetterHome)com.idega.data.IDOLookup.getHomeLegacy(EmailLetter.class)).findByPrimaryKeyLegacy(iEmailLetterId);
//                update = true;
//            }
//            letter.setSubject(subject);
//            letter.setBody(body);
//            letter.setHost(sHost);
//            letter.setFrom(sFrom);
//            letter.setParse(Parse);
//            letter.setOnlyUser(OnlyUser);
//            letter.setType(type);
//            if (update) {
//                letter.update();
//            } else {
//                letter.insert();
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return letter;
//    }
//
//
//    /**
//     *  @todo Description of the Method
//     *
//     * @param  sHost      Description of the Parameter
//     * @param  sFrom      Description of the Parameter
//     * @param  sSubject   Description of the Parameter
//     * @param  sBody      Description of the Parameter
//     * @param  bParse     Description of the Parameter
//     * @param  bOnlyUser  Description of the Parameter
//     * @param  type       Description of the Parameter
//     * @return            Description of the Return Value
//     */
//    public static EmailLetter createEmailLetter(String sHost, String sFrom, String sSubject, String sBody, boolean bParse, boolean bOnlyUser, String type) {
//        return saveEmailLetter(-1, sHost, sFrom, sSubject, sBody, bParse, bOnlyUser, type);
//    }
//
//
//    /**
//     *  @todo Description of the Method
//     *
//     * @param  emailletter  Description of the Parameter
//     * @param  sHost        Description of the Parameter
//     * @param  sFrom        Description of the Parameter
//     * @param  sSubject     Description of the Parameter
//     * @param  sBody        Description of the Parameter
//     * @param  bParse       Description of the Parameter
//     * @param  onlyUser     Description of the Parameter
//     * @param  type         Description of the Parameter
//     * @return              Description of the Return Value
//     */
//    public static EmailLetter createEmailLetter(EmailLetter emailletter, String sHost, String sFrom, String sSubject, String sBody, boolean bParse, boolean onlyUser, String type) {
//        int id = emailletter != null ? emailletter.getID() : -1;
//        return saveEmailLetter(id, sHost, sFrom, sSubject, sBody, bParse, onlyUser, type);
//    }
//
//
//    /**
//     *  @todo Description of the Method
//     *
//     * @param  letter  Description of the Parameter
//     */
//    public static void deleteEmailLetter(EmailLetter letter) {
//        try {
//            letter.delete();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//
//    /**
//     *  @todo Description of the Method
//     *
//     * @param  list  Description of the Parameter
//     */
//    public static void deleteMailingList(MailingList list) {
//        if (list != null) {
//            try {
//                list.removeFrom(((com.idega.core.data.EmailHome)com.idega.data.IDOLookup.getHomeLegacy(Email.class)).createLegacy());
//                list.delete();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//
//    /**
//     *  @todo Description of the Method
//     *
//     * @return    Description of the Return Value
//     */
//    public static List listOfEmailLetter() {
//        try {
//            return EntityFinder.findAllOrdered(((is.idega.idegaweb.campus.block.mailinglist.data.EmailLetterHome)com.idega.data.IDOLookup.getHomeLegacy(EmailLetter.class)).createLegacy(), is.idega.idegaweb.campus.block.mailinglist.data.EmailLetterBMPBean.TYPE);
//        } catch (SQLException ex) {}
//        return null;
//    }
//
//
//    /**
//     *  @todo Description of the Method
//     *
//     * @return    Description of the Return Value
//     */
//    public static List listOfMailingList() {
//        try {
//            return EntityFinder.findAll(((is.idega.idegaweb.campus.block.mailinglist.data.MailingListHome)com.idega.data.IDOLookup.getHomeLegacy(MailingList.class)).createLegacy());
//        } catch (SQLException ex) {}
//        return null;
//    }
//
//
//    /**
//     *  @todo Description of the Method
//     *
//     * @param  letter  Description of the Parameter
//     * @return         Description of the Return Value
//     */
//    public static Map mapOfMailingList(EmailLetter letter) {
//        try {
//            List L = EntityFinder.findRelated(letter, ((is.idega.idegaweb.campus.block.mailinglist.data.MailingListHome)com.idega.data.IDOLookup.getHomeLegacy(MailingList.class)).createLegacy());
//            if (L != null) {
//                MailingList mlist;
//                Iterator I = L.iterator();
//                Hashtable H = new Hashtable(L.size());
//                while (I.hasNext()) {
//                    mlist = (MailingList) I.next();
//                    H.put(new Integer(mlist.getID()), mlist);
//                }
//                return H;
//            }
//        } catch (SQLException ex) {}
//        return null;
//    }
//
//
//    /**
//     *  @todo Description of the Method
//     *
//     * @param  letter  Description of the Parameter
//     * @param  newIds  Description of the Parameter
//     * @param  oldIds  Description of the Parameter
//     */
//    public static void saveEmailLetterMailingLists(EmailLetter letter, int[] newIds, int[] oldIds) {
//        try {
//            for (int j = 0; j < oldIds.length; j++) {
//                letter.removeFrom(MailingList.class, oldIds[j]);
//            }
//            for (int i = 0; i < newIds.length; i++) {
//                letter.addTo(MailingList.class, newIds[i]);
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//
//    }
//
//
//    /**
//     *  @todo Description of the Method
//     *
//     * @param  mlist  Description of the Parameter
//     * @return        Description of the Return Value
//     */
//    public static List listOfEmails(MailingList mlist) {
//        try {
//            return EntityFinder.findRelated(mlist, ((EmailHome)com.idega.data.IDOLookup.getHomeLegacy(Email.class)).createLegacy());
//        } catch (SQLException ex) {}
//        return null;
//    }
}
