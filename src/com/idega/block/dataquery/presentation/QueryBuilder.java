/*
 * Created on May 21, 2003
 *
 * QueryBuilder is a wizard that constructs a ReportQuery from the input the user enters.
 */
package com.idega.block.dataquery.presentation;

import com.idega.block.presentation.CategoryBlock;

/**
 * @author aron
 */
public class QueryBuilder extends CategoryBlock {

	/* (non-Javadoc)
	 * @see com.idega.block.presentation.CategoryBlock#getCategoryType()
	 */
	public String getCategoryType() {
		// TODO Auto-generated method stub
		return "DataQuery";
	}

	/* (non-Javadoc)
	 * @see com.idega.block.presentation.CategoryBlock#getMultible()
	 */
	public boolean getMultible() {
		// TODO Auto-generated method stub
		return false;
	}

}
