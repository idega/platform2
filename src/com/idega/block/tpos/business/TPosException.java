package com.idega.block.tpos.business;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Idega hf
 * @author <a href="mail:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */

public class TPosException extends Exception {
  protected String _errorMessage = null;
  protected String _errorNumber = null;
  protected String _displayError = null;

  public TPosException() {
    super();
  }

  public TPosException(String message) {
    super(message);
  }

  public void setErrorMessage(String message) {
    _errorMessage = message;
  }

  public String getErrorMessage() {
    return(_errorMessage);
  }

  public void setErrorNumber(String number) {
    _errorNumber = number;
  }

  public String getErrorNumber() {
    return(_errorNumber);
  }

  public void setDisplayError(String message) {
    _displayError = message;
  }

  public String getDisplayError() {
    return(_displayError);
  }
}