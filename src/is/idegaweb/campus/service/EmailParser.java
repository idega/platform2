/*
 * $Id: EmailParser.java,v 1.1 2001/08/10 11:32:51 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.service;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public abstract class EmailParser {
  public static String parseAllocatedEmail(String text, int applicantId) {
    return(text);
  }
}