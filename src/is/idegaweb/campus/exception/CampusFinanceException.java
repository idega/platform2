package is.idegaweb.campus.exception;



/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class CampusFinanceException extends Exception {
  public CampusFinanceException(String exceptionText) {
    super(exceptionText);
  }
  public CampusFinanceException() {
    super();
  }
}
