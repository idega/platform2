/*
 * Created on 8.8.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.util;

import java.util.HashMap;
import java.util.Map;

import com.idega.data.IDOReportableEntity;
import com.idega.data.IDOReportableField;

/**
 * Title:		ReportableEntityReportableEntity
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class ReportableData implements IDOReportableEntity{
	
	Map _data = new HashMap();
	
	/**
	 * 
	 */
	public ReportableData() {
		super();
	}
	
	
	public void addData(IDOReportableField key, Object data){
		_data.put(key.getName(),data);
	}

	public Object getFieldValue(IDOReportableField arg0) {
		return _data.get(arg0.getName());
	}
	
	

}
