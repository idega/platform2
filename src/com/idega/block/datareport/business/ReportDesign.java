/*
 * Created on 30.7.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.business;

import java.io.IOException;
import java.io.InputStream;

import com.idega.block.datareport.data.ReportDesignFile;

import dori.jasper.engine.JRException;
import dori.jasper.engine.JasperManager;
import dori.jasper.engine.design.JasperDesign;

/**
 * Title:		ReportDesign
 * Description: 
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		0.1 - Unfinished
 */
public class ReportDesign {
	
	private JasperDesign _design;
	
	/**
	 * 
	 */
	public ReportDesign(ReportDesignFile design) throws JRException, IOException {
		InputStream inputStream = design.getFileValue();
		_design = JasperManager.loadXmlDesign(inputStream);
		inputStream.close();
	}

}
