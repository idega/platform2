package is.idega.idegaweb.project.data;

import java.sql.SQLException;

/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

 public class IPParticipantGroupBMPBean extends com.idega.core.data.GenericGroupBMPBean implements is.idega.idegaweb.project.data.IPParticipantGroup {
//public class IPParticipantGroupBMPBean extends com.idega.user.data.GroupBMPBean implements is.idega.idegaweb.project.data.IPParticipantGroup {

  public IPParticipantGroupBMPBean() {
    super();
  }

  public IPParticipantGroupBMPBean(int id) throws SQLException{
    super(id);
  }

  public String getGroupTypeValue(){
    return "ip_participant";
  }

  public static IPParticipantGroup getStaticGroupInstance(){
    return (IPParticipantGroup)getStaticInstance(IPParticipantGroup.class);
  }


  protected boolean identicalGroupExistsInDatabase() throws Exception {
    return false;
  }
}
