package is.idega.idegaweb.campus.block.mailinglist.business;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public interface ContentParsable {

  /**
   *  Returns all available tags from this parser
   */
  String[] getParseTags();

  /**
   *  Returns a parsed tags value
   */
  String getParsedString(String tag);

  /**
   *  Returns the object to be parsed
   */
  Object getParseObject();

  /**
  *  Returns available types
  */
  String[] getParseTypes();
}