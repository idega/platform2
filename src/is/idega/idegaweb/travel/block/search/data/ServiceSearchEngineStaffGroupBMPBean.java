package is.idega.idegaweb.travel.block.search.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.user.data.GroupBMPBean;

public class ServiceSearchEngineStaffGroupBMPBean extends GroupBMPBean implements ServiceSearchEngineStaffGroup {
  public static final String GROUP_TYPE_VALUE = "TB_SERV_SEARCH_ENGINE_STAFF";

  public String getGroupTypeValue() {
    return GROUP_TYPE_VALUE;
  }
  
  public static String getClassName(){
    return ServiceSearchEngineStaffGroup.class.getName();
  }
  
  protected boolean identicalGroupExistsInDatabase() throws Exception {
    return false;
  }
  
  public Collection ejbFindGroupsByName(String name) throws FinderException {
  	return super.ejbFindGroupsByName(name);
  }
  
  public Collection ejbFindGroupsByNameAndDescription(String name, String description) throws FinderException {
  	return super.ejbFindGroupsByNameAndDescription(name, description);
  }
} 