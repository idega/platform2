package is.idega.idegaweb.member.business;

/**
 * Title:        idegaWeb User Subsystem
 * Description:  idegaWeb User Subsystem is the base system for Users and Group management
 * Copyright:    Copyright (c) 2002
 * Company:      idega
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class NoCohabitantFound extends javax.ejb.FinderException {

  public NoCohabitantFound(String UserName) {
      super("No cohabitant found for user "+UserName);
  }
}