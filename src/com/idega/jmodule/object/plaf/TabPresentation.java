package com.idega.jmodule.object.plaf;

import javax.swing.SingleSelectionModel;
import java.util.Vector;
import com.idega.jmodule.object.ModuleObject;
import com.idega.util.IWColor;
/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author
 * @version 1.0
 */

public interface TabPresentation extends SingleSelectionModel {

	public void add(ModuleObject obj, int index);
	public void empty(int index);
//	public void empty(ModuleObject obj);
	public void setWidth(String width);
	public void SetHeight(String height);
        public Vector getAddedTabs();
        public void setAddedTabs(Vector tabs);
        public void setColor(IWColor color);


}   //  Interface TabPresentation