/*
 * $Id: NoSuchTagException.java,v 1.1 2001/11/08 14:43:05 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.exception;


/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class NoSuchTagException extends Exception {
  public NoSuchTagException(String exceptionText) {
    super(exceptionText);
  }
}
