package is.idega.idegaweb.member.business;

/**
 * Title:        NoRegionalUnionFoundException
 * Description:  Thrown the group is not a part of a regional union
 * Copyright:    Idega Software, Copyright (c) 2003
 * Company:      Idega Software
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class NoRegionalUnionFoundException extends javax.ejb.FinderException {

  public NoRegionalUnionFoundException(String groupName) {
      super("No regional union found for group "+groupName);
  }
}