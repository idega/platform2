/*
 * $Id: NoSuchTagException.java,v 1.2 2001/11/08 15:40:39 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.exception;


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
