package is.idega.idegaweb.member.business;

/**
 * Title:        NoFederationFoundException
 * Description:  Thrown if a group is not a part of a federation
 * Copyright:    Idega Software, Copyright (c) 2003
 * Company:      Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class NoFederationFoundException extends javax.ejb.FinderException {

  public NoFederationFoundException(String groupName) {
      super("No federation found for group "+groupName);
  }
}