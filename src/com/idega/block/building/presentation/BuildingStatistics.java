package com.idega.block.building.presentation;

import com.idega.block.building.business.BuildingCacher;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.util.Edit;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class BuildingStatistics extends PresentationObjectContainer {

	private static final String IW_RESOURCE_BUNDLE = "com.idega.block.building";
	protected IWResourceBundle iwrb;
	private int complexID;

  public BuildingStatistics() {

  }

  public void main(IWContext iwc) {
    iwrb = this.getResourceBundle(iwc);
		Table T = new Table();
		int numOfComplex = BuildingCacher.getNumberOfComplexes();
		int numOfBuildings = BuildingCacher.getNumberOfBuildings();
		int numOfFloors = BuildingCacher.getNumberOfFloors();
		int numOfApartments = BuildingCacher.getNumberOfApartments();
		int row = 1;
		int col = 1;
		int col2 = 2;
		T.add(Edit.formatText(iwrb.getLocalizedString("numberofcomplexes","Number of complexes")),col,row);
		T.add(Edit.formatText(numOfComplex),col2,row);
		row++;
		T.add(Edit.formatText(iwrb.getLocalizedString("numberofbuildings","Number of buildings")),col,row);
		T.add(Edit.formatText(numOfBuildings),col2,row);
		row++;
		T.add(Edit.formatText(iwrb.getLocalizedString("numberoffloors","Number of floors")),col,row);
		T.add(Edit.formatText(numOfFloors),col2,row);
		row++;
		T.add(Edit.formatText(iwrb.getLocalizedString("numberofapartments","Number of apartments")),col,row);
		T.add(Edit.formatText(numOfApartments),col2,row);
		row++;
		add(T);

  }

  public String getBundleIdentifier() {
    return(IW_RESOURCE_BUNDLE);
  }
}
