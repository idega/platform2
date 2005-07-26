package com.idega.block.datareport.data;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.design.JasperDesign;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jan 15, 2004
 */
public class DesignBox {
	
	public static final String REPORT_HEADLINE_KEY = "ReportTitle";
	
	private JasperDesign design;
	
	private Map parameterMap;

	/**
	 * @return Returns the design.
	 */
	public JasperDesign getDesign() {
		return design;
	}

	/**
	 * @param design The design to set.
	 */
	public void setDesign(JasperDesign design) {
		this.design = design;
	}

	/**
	 * @return Returns the parameterMap.
	 */
	public Map getParameterMap() {
		if (parameterMap == null) {
			parameterMap = new HashMap(0);
		}
		return parameterMap;
	}

	/**
	 * @param parameterMap The parameterMap to set.
	 */
	public void setParameterMap(Map parameterMap) {
		this.parameterMap = parameterMap;
	}

}
