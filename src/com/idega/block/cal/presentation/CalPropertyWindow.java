package com.idega.block.cal.presentation;

import com.idega.presentation.PresentationObjectType;
import com.idega.repository.data.ImplementorPlaceholder;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jul 22, 2004
 */
public interface CalPropertyWindow 	extends PresentationObjectType, ImplementorPlaceholder {
	
	String getIdParameter();
	
}
