package is.idega.idegaweb.travel.block.search.data;

import com.idega.core.data.GenericGroupBMPBean;

public class ServiceSearchEngineStaffGroupBMPBean extends GenericGroupBMPBean implements ServiceSearchEngineStaffGroup {
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

} 