
package is.idega.idegaweb.golf.entity;

import com.idega.data.GenericEntity;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public class UserIdBMPBean extends GenericEntity implements UserId{

    public void initializeAttributes(){
      addAttribute(getIDColumnName());
      addAttribute("member_id", "Klúbbur", true, true, "java.lang.Integer","one-to-one","is.idega.idegaweb.golf.entity.Member");
      addAttribute("userid","Auðkenni",true,true,"java.lang.String");
      super.setMaxLength("userid",16);
    }
    public String getEntityName(){
      return "userid";
    }
    public void setUserId(String userid){
      setColumn("userid", userid);
    }
    public String getUserId(){
      return getStringColumnValue("userid");
    }
    public void setMemberId(int member_id){
      setColumn("member_id", member_id);
    }
    public int getMemberId(){
      return getIntColumnValue("member_id");
    }
}
