package is.idega.idegaweb.member.business;

/**
 * Title:        NoLeagueFoundException
 * Description:  Thrown if a group has no connections to a league
 * Copyright:    Idega Software, Copyright (c) 2003
 * Company:      Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class NoLeagueFoundException extends javax.ejb.FinderException {

  public NoLeagueFoundException(String groupName) {
      super("No league found for group/user "+groupName);
  }
}