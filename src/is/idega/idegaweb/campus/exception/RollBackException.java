package is.idega.idegaweb.campus.exception;



/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class RollBackException extends Exception {
  public RollBackException(String exceptionText) {
    super(exceptionText);
  }
  public RollBackException() {
    super();
  }
}
