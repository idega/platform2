package is.idega.idegaweb.member.business;

/**
 * Title:        NoClubFoundException
 * Description:  Thrown if a group has no parent of the type iwme_club
 * Copyright:    Idega Software, Copyright (c) 2003
 * Company:      Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class NoClubFoundException extends javax.ejb.FinderException {

  public NoClubFoundException(String groupName) {
      super("No club found for group/user "+groupName);
  }
}