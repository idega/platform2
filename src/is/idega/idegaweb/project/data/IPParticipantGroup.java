package is.idega.idegaweb.project.data;

import com.idega.core.data.GenericGroup;
import java.sql.*;

/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class IPParticipantGroup extends GenericGroup {

  public IPParticipantGroup() {
    super();
  }

  public IPParticipantGroup(int id) throws SQLException{
    super(id);
  }

  public String getGroupTypeValue(){
    return "ip_participant";
  }

  public static IPParticipantGroup getStaticGroupInstance(){
    return (IPParticipantGroup)getStaticInstance(IPParticipantGroup.class);
  }
}