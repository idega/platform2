package is.idega.idegaweb.member.business;

/**
 * Title:        idegaWeb User Subsystem
 * Description:  idegaWeb User Subsystem is the base system for Users and Group management
 * Copyright:    Copyright (c) 2002
 * Company:      idega
 * @author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class NoCustodianFound extends javax.ejb.FinderException {

  public NoCustodianFound(String UserName) {
      super("No custodian found for user "+UserName);
  }
}