//idega 2001 -eiki
package is.idega.idegaweb.golf.entity;

//import java.util.*;
import com.idega.data.GenericEntity;

public class FamilyBMPBean extends GenericEntity implements Family{

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute("name","Fjölskylda",true,true,"java.lang.String");
  }
  public String getEntityName(){
    return "family";
  }
  public String getName(){
    return getStringColumnValue("name");
  }
  public void setName(String name){
    setColumn("name", name);
  }


}
