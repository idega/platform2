package is.idega.idegaweb.campus.presentation;

import com.idega.block.building.presentation.BuildingViewer;
import com.idega.presentation.IWContext;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class CampusBuildings extends CampusBlock {

  public void main(IWContext iwc){
    BuildingViewer fin = new BuildingViewer();
    fin.setApartmentTypeWindowClass(CampusTypeWindow.class);
    add(fin);
  }

  public String getBundleIdentifier(){
    return "is.idega.idegaweb.campus";
  }

}
