/*
 * $Id: TPosException.java,v 1.3 2002/04/06 19:07:37 tryggvil Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.tpos.business;

/**
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class TPosException extends Exception {
  protected String _errorMessage = null;
  protected String _errorNumber = null;
  protected String _displayError = null;

  /**
   *
   */
  public TPosException() {
    super();
  }

  /**
   *
   */
  public TPosException(String message) {
    super(message);
  }

  /**
   *
   */
  public void setErrorMessage(String message) {
    _errorMessage = message;
  }

  /**
   *
   */
  public String getErrorMessage() {
    return(_errorMessage);
  }

  /**
   *
   */
  public void setErrorNumber(String number) {
    _errorNumber = number;
  }

  /**
   *
   */
  public String getErrorNumber() {
    return(_errorNumber);
  }

  /**
   *
   */
  public void setDisplayError(String message) {
    _displayError = message;
  }

  /**
   *
   */
  public String getDisplayError() {
    return(_displayError);
  }
}
