package com.idega.block.datareport.business;

import java.io.IOException;
import net.sf.jasperreports.engine.JRDataSource;
import com.idega.block.datareport.util.ReportDescription;



public interface SimpleReportBusiness extends com.idega.business.IBOService
{
	public void writeSimpleExcelFile(JRDataSource reportData, String nameOfReport, String filePathAndName, ReportDescription parameterMap) throws IOException;
}
