/*
 * Created on 30.7.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.data;

import java.sql.SQLException;

import com.idega.core.data.ICFileBMPBean;

/**
 * Title:		ReportDesignFileBMPBean
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class ReportDesignFileBMPBean extends ICFileBMPBean implements ReportDesignFile{

	/**
	 * 
	 */
	public ReportDesignFileBMPBean() {
		super();
	}

	/**
	 * @param id
	 * @throws SQLException
	 */
	public ReportDesignFileBMPBean(int id) throws SQLException {
		super(id);
	}

}
