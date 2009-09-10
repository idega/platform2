/*
 * Created on 19.2.2004
 */
package com.idega.block.blog.presentation;

import java.util.Collection;

//import com.idega.presentation.Presentable;

/**
 * Title:		BlogLayout
 * Description:
 * Copyright:	Copyright (c) 2004
 * Company:		idega Software
 * @author		2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0b
 */
public interface BlogLayout /*extends Presentable*/ {
	
	/**
	 * 
	 * @param coll is collection of blog records wrapped in 
	 */
	
	public void loadOrderedData(Collection coll);
	public void loadICObjectID(int id);
	public void loadICObjectInstanceID();
	
	
	public void setToShowDateAndTime(boolean value);
	public void setToShowDate(boolean value);
	public void hideDateAndTime(boolean value);
	// ....
}
