package com.idega.block.email.data;

import java.sql.Timestamp;

/**
 *  Title: Description: Copyright: Copyright (c) 2001 Company:
 *
 * @author     <br>
 *      <a href="mailto:aron@idega.is">Aron Birkir</a> <br>
 *
 * @created    14. mars 2002
 * @version    1.0
 */

public class MailGroupBMPBean extends com.idega.block.category.data.CategoryEntityBMPBean implements com.idega.block.email.data.MailGroup,com.idega.block.email.business.EmailGroup {

  /**
   * @todo    Description of the Field
   */
  public final static String TABLE_NAME = "em_group";
  /**
   * @todo    Description of the Field
   */
  public final static String ACCOUNT = "em_account_id";
  /**
   * @todo    Description of the Field
   */
  public final static String NAME = "name";
  /**
   * @todo    Description of the Field
   */
  public final static String DESCRIPTION = "description";
  /**
   * @todo    Description of the Field
   */
  public final static String CREATED = "created";


  /**  Constructor for the LetterGroup object */
  public MailGroupBMPBean() {
    super();
  }


  /**
   *  Constructor for the LetterGroup object
   *
   * @param  id                         Description of the Parameter
   * @exception  java.sql.SQLException  Description of the Exception
   */
  public MailGroupBMPBean(int id) throws java.sql.SQLException {
    super(id);
  }


  /**
   * @todo    Description of the Method
   */
  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(NAME, "Name", true, true, String.class);
    addAttribute(DESCRIPTION, "Description", true, true, String.class);
    addAttribute(CREATED, "created", true, true, Timestamp.class);
    addManyToManyRelationShip(MailAccount.class);
    addManyToManyRelationShip(MailLetter.class);
  }


  /**
   *  Gets the entityName of the LetterGroup object
   *
   * @return    The entity name value
   */
  public String getEntityName() {
    return TABLE_NAME;
  }


  /**
   *  Gets the name of the LetterGroup object
   *
   * @return    The name value
   */
  public String getName() {
    return getStringColumnValue(NAME);
  }


  /**
   *  Gets the description of the LetterGroup object
   *
   * @return    The description value
   */
  public String getDescription() {
    return getStringColumnValue(DESCRIPTION);
  }


  /**
   *  Gets the created of the LetterGroup object
   *
   * @return    The created value
   */
  public Timestamp getCreated() {
    return (Timestamp) getColumnValue(CREATED);
  }


  /**
   *  Sets the name attribute of the LetterGroup object
   *
   * @param  name  The new name value
   */
  public void setName(String name) {
    setColumn(NAME, name);
  }


  /**
   *  Sets the description attribute of the LetterGroup object
   *
   * @param  description  The new description value
   */
  public void setDescription(String description) {
    setColumn(DESCRIPTION, description);
  }


  /**
   *  Sets the created attribute of the LetterGroup object
   *
   * @param  created  The new created value
   */
  public void setCreated(Timestamp created) {
    setColumn(CREATED, created);
  }
}

