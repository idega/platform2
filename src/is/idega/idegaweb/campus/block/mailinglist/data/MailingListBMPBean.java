package is.idega.idegaweb.campus.block.mailinglist.data;

import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class MailingListBMPBean extends com.idega.data.CategoryEntityBMPBean implements is.idega.idegaweb.campus.block.mailinglist.data.MailingList {

  private final static String TABLE_NAME = "cam_mail_list";
  private final static String NAME = "name";
  private final static String CREATED = "created_date";

  public MailingListBMPBean() {
    super();
  }

  public MailingListBMPBean(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(this.getIDColumnName());
    addAttribute(NAME , "Name", true, true, String.class);
    addAttribute(CREATED , "Created", true, true, Timestamp.class);
    addManyToManyRelationShip(com.idega.core.contact.data.Email.class);

  }
  public String getEntityName() {
    return TABLE_NAME;
  }
  public String getName(){
    return getStringColumnValue(NAME);
  }
  public void setName(String name){
    setColumn(NAME,name);
  }
  public Timestamp getCreated(){
    return (Timestamp) getColumnValue(CREATED);
  }
  public void setCreated(Timestamp created){
    setColumn(CREATED,created);
  }
}
