package com.idega.jmodule.object.plaf;

import com.idega.util.IWColor;

/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author
 * @version 1.0
 */

public interface TabPagePresentation {

//	public void add(ModuleObject obj);
//	public void empty();
	public void setWidth(String width);
	public void setHeight(String height);
	public void fireContentChange();
        public void setColor(IWColor color);

}