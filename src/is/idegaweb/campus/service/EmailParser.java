/*
 * $Id: EmailParser.java,v 1.5 2001/11/08 15:40:40 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.service;


import is.idegaweb.campus.exception.NoSuchTagException;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Set;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class EmailParser {
  private Hashtable tags_ = null;

  public EmailParser() {
    createTags();
  }

  public String parseAllocatedEmail(String text, int applicantId) throws NoSuchTagException {
    return(text);
  }

  public String parseApplicationSentEmail(String text, int applicantId) throws NoSuchTagException {
    return(text);
  }

  public String parseInvalidEmail(String text, int applicantId) throws NoSuchTagException {
    return(text);
  }

  public String parseApplicationAcceptedtEmail(String text, int applicantId) throws NoSuchTagException {
    return(text);
  }

  public String parseLostPasswordEmail(String text, int applicantId) throws NoSuchTagException {
    return(text);
  }

  public Enumeration getTags() {
    return(tags_.keys());
  }

  public void setTag(String key, String value) throws NoSuchTagException {
    Set keySet = tags_.keySet();
    if (!keySet.contains(key))
      throw new NoSuchTagException("Tag " + key + " does not exists");

  }

  private void createTags() {
    tags_ = new Hashtable();
    tags_.put("[fullname]",null);
    tags_.put("[firstname]",null);
    tags_.put("[middlename]",null);
    tags_.put("[lastname]",null);
    tags_.put("[passwd]",null);
    tags_.put("[username]",null);
    tags_.put("[status]",null);
    tags_.put("[refno]",null);
  }
}
