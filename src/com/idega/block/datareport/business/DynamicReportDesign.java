/*
 * Created on 30.7.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.business;

import java.io.IOException;
import java.io.InputStream;

import com.idega.block.datareport.business.jasperdesignxml.ColumnFooter;
import com.idega.block.datareport.business.jasperdesignxml.ColumnHeader;
import com.idega.block.datareport.business.jasperdesignxml.DesignDocument;
import com.idega.block.datareport.business.jasperdesignxml.Detail;
import com.idega.block.datareport.business.jasperdesignxml.Font;
import com.idega.block.datareport.business.jasperdesignxml.PageFooter;
import com.idega.block.datareport.business.jasperdesignxml.PageHeader;
import com.idega.block.datareport.business.jasperdesignxml.ReportElement;
import com.idega.block.datareport.business.jasperdesignxml.StaticText;
import com.idega.block.datareport.business.jasperdesignxml.Summary;
import com.idega.block.datareport.business.jasperdesignxml.Text;
import com.idega.block.datareport.business.jasperdesignxml.TextElement;
import com.idega.block.datareport.business.jasperdesignxml.TextField;
import com.idega.block.datareport.business.jasperdesignxml.TextFieldExpression;
import com.idega.block.datareport.business.jasperdesignxml.Title;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWContext;
import com.idega.util.FileUtil;

import dori.jasper.engine.JRException;
import dori.jasper.engine.JasperManager;
import dori.jasper.engine.design.JasperDesign;

/**
 * Title:		DynamicReportDesign
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class DynamicReportDesign {
	
	private static int _tempFileCounter = 1;
	private static final int _TEMP_FILE_COUNTER_MAX = 20;
	private DesignDocument _designDoc;
	private int _tempFileNumber=-1;
	
	private int _columnsXCoordinateForColumnHeader = 0;
	private int _columnsXCoordinateForDetail = 0;
	private int _detailHeight = 12;
	private int _columnHeaderHeight = 20;
	
	private int _reportTitleHeight = 40;
	
	private int _headerParametersXCoordinate = 0;
	private int _headerParametersMaxXCoordinate = -1;
	private int _headerParametersYCoordinate = 0;
	private int _headerParametersHeight = 16;
	private int _extraspaceBetweenParameterGroupsInHeader = 10;
	private int _pageHeaderHeight = 20;
	
	
	private static String DYNAMIC_DESIGN_FOLDER = "dynamicDesigns";
	private static String TEMP_DESIGN_NAME = "tmpDesigne";
	private static String REPORT_FOLDER = "reports";
	private static char DOT = '.';
	private static String XML_FILE_EXTENSION = "xml";
	
	public static final String PRM_REPORT_NAME = "ReportTitle";
	
	/**
	 * 
	 */
	public DynamicReportDesign(String name) {
		initializeDocument(name);
		createTitle();
		createPageHeader();
		createColumnHeader();
		createDetail();
		createColumnFooter();
		createPageFooter();
		createSummary();
	}
	

	/**
	 *
	 */
	private void initializeDocument(String name) {
		_designDoc = new DesignDocument(name);
	
		_designDoc.setPageWidth(595);
		_designDoc.setPageHeight(842);
		_designDoc.setColumnWidth(540);
		_designDoc.setColumnSpacing(15);
		_designDoc.setLeftMargin(20);
		_designDoc.setRightMargin(20);
		_designDoc.setTopMargin(10);
		_designDoc.setBottomMargin(30);
		_designDoc.setColumnCount(1);
		_designDoc.setPrintOrderVertical();
		_designDoc.setOrientationPortrait();
		_designDoc.setWhenNoDataTypeAsAllSectionNoDetail();
		_designDoc.setIsTitleNewPage(false);
		_designDoc.setIsSummaryNewPage(false);
	
	
		_designDoc.addParameter(PRM_REPORT_NAME,String.class,true);
	
	}

	/**
	 * 
	 */
	private void createTitle() {
		Title title = new Title();
		title.setHeight(_reportTitleHeight);
		//title.setIsSplitAllowed(true);
			TextField tField1 = new TextField();
			tField1.setIsBlankWhenNull(true);
			tField1.setIsStretchWithOverflow(true);
			tField1.setEvaluationTimeAsNow();
			tField1.setHyperlinkTypeAsNone();
				ReportElement rElement = new ReportElement(0,0,500,_reportTitleHeight);
				rElement.setPositionTypeAsFixRelativeToTop();
				rElement.setIsPrintRepeatedValues(true);
				rElement.setIsRemoveLineWhenBlank(false);
				rElement.setIsPrintInFirstWholeBand(false);
				rElement.setIsPrintWhenDetailOverflows(false);
			tField1.addContent(rElement);
				TextElement tElement = new TextElement();
				tElement.setTextAlignmentAsCenter();
				tElement.setVerticalAlignmentAsTop();
				tElement.setLineSpacingAsSingle();
					Font tElementFont = new Font();
					tElementFont.setIsUnderline(true);
					tElementFont.setFontSize(16);
					tElementFont.setIsBold(true);
				tElement.addContent(tElementFont);
			tField1.addContent(tElement);
				TextFieldExpression tfExpression = new TextFieldExpression();
				tfExpression.setClassType(String.class);
				tfExpression.addParameter(PRM_REPORT_NAME);	
			tField1.addContent(tfExpression);
		title.addContent(tField1);
	
		_designDoc.setTitle(title);
	}

	/**
	 * 
	 */
	private void createPageHeader() {
		PageHeader pHeader = new PageHeader();
		pHeader.setHeight(_pageHeaderHeight);
		//pHeader.setIsSplitAllowed(true);
		_designDoc.setPageHeader(pHeader);		
	}
	
	/**
	 * 
	 */
	private void createColumnHeader() {
		ColumnHeader colHeader = new ColumnHeader();
		colHeader.setHeight(_columnHeaderHeight);
		//colHeader.setIsSplitAllowed(true);
		_designDoc.setColumnHeader(colHeader);
	}
	
	/**
	 * 
	 */
	private void createDetail() {
		Detail detail = new Detail();
		detail.setHeight(_detailHeight);
		//detail.setIsSplitAllowed(true);
		_designDoc.setDetail(detail);
	}
	
	/**
	 * 
	 */
	private void createColumnFooter() {
		ColumnFooter colFooter = new ColumnFooter();
		colFooter.setHeight(0);
		//colFooter.setIsSplitAllowed(true);
		_designDoc.setColumnFooter(colFooter);	
	}
	
	/**
	 * 
	 */
	private void createPageFooter() {
		PageFooter pFooter = new PageFooter();
		pFooter.setHeight(15);
		//pFooter.setIsSplitAllowed(true);
			StaticText sText = new StaticText();
				ReportElement rElement = new ReportElement(0,0,40,15);
				rElement.setPositionTypeAsFloat();
				rElement.setIsPrintRepeatedValues(true);
				rElement.setIsRemoveLineWhenBlank(false);
				rElement.setIsPrintInFirstWholeBand(false);
				rElement.setIsPrintWhenDetailOverflows(false);
			sText.addContent(rElement);
				Text text = new Text("Page:  ");
			sText.addContent(text);
		pFooter.addContent(sText);
		
		
		TextField tField = new TextField();
			tField.setIsStretchWithOverflow(false);
			tField.setEvaluationTimeAsNow();
			tField.setIsBlankWhenNull(false);
			tField.setHyperlinkTypeAsNone();
				ReportElement rElement2 = new ReportElement(40,0,100,15);
				rElement2.setPositionTypeAsFloat();
				rElement2.setIsPrintRepeatedValues(true);
				rElement2.setIsRemoveLineWhenBlank(false);
				rElement2.setIsPrintInFirstWholeBand(false);
				rElement2.setIsPrintWhenDetailOverflows(false);
			tField.addContent(rElement2);
				TextElement tElement = new TextElement();
				tElement.setTextAlignmentAsLeft();
				tElement.setVerticalAlignmentAsTop();
				tElement.setLineSpacingAsSingle();
			tField.addContent(tElement);
				TextFieldExpression tfExpression = new TextFieldExpression();
				tfExpression.setClassType(Integer.class);
				tfExpression.addVariable("PAGE_NUMBER");
			tField.addContent(tfExpression);
		pFooter.addContent(tField);

		
		
		_designDoc.setPageFooter(pFooter);
	}
	
	/**
	 * 
	 */
	private void createSummary() {
		Summary summary = new Summary();
		summary.setHeight(0);
		//summary.setIsSplitAllowed(true);
		_designDoc.setSummary(summary);
	}
	
	
	
	
	
	
	public void addField(String fieldName, Class classType, int columnWidth){
		_designDoc.addField(fieldName,classType);
		addColumn(fieldName, classType, columnWidth);
	}


	private void addColumn(String fieldName, Class classType, int columnWidth){
		addFieldToColumnHeader(fieldName, String.class, columnWidth);
		addToFieldDetail(fieldName, classType, columnWidth);
	}
	
	private void addFieldToColumnHeader(String fieldName, Class classType, int columnWidth){
		_designDoc.addParameter(fieldName,String.class);
		
		TextField tField = new TextField();
		tField.setIsBlankWhenNull(true);
		tField.setIsStretchWithOverflow(true);
		tField.setEvaluationTimeAsNow();
		tField.setHyperlinkTypeAsNone();
			ReportElement rElement = new ReportElement(_columnsXCoordinateForColumnHeader,0,columnWidth,_columnHeaderHeight);
			_columnsXCoordinateForColumnHeader += columnWidth;
			rElement.setPositionTypeAsFloat();
			rElement.setIsPrintRepeatedValues(true);
			rElement.setIsRemoveLineWhenBlank(false);
			rElement.setIsPrintInFirstWholeBand(false);
			rElement.setIsPrintWhenDetailOverflows(false);
		tField.addContent(rElement);
			TextElement tElement = new TextElement();
			tElement.setTextAlignmentAsLeft();
			tElement.setVerticalAlignmentAsTop();
			tElement.setLineSpacingAsSingle();
				Font tElementFont = new Font();
				tElementFont.setIsUnderline(true);
				tElementFont.setIsBold(true);
				tElementFont.setFontSize(9);
			tElement.addContent(tElementFont);
		tField.addContent(tElement);
			TextFieldExpression tfExpression = new TextFieldExpression();
			tfExpression.setClassType(classType);
			tfExpression.addParameter(fieldName);	
		tField.addContent(tfExpression);
		
		
		_designDoc.getColumnHeader().addContent(tField);
	}
	
	private void addToFieldDetail(String fieldName, Class classType, int columnWidth){
		TextField tField = new TextField();
		tField.setIsBlankWhenNull(true);
		tField.setIsStretchWithOverflow(true);
		tField.setEvaluationTimeAsNow();
		tField.setHyperlinkTypeAsNone();
			ReportElement rElement = new ReportElement(_columnsXCoordinateForDetail,0,columnWidth,_detailHeight);
			_columnsXCoordinateForDetail += columnWidth;
			rElement.setPositionTypeAsFloat();
			rElement.setIsPrintRepeatedValues(true);
			rElement.setIsRemoveLineWhenBlank(false);
			rElement.setIsPrintInFirstWholeBand(false);
			rElement.setIsPrintWhenDetailOverflows(false);
		tField.addContent(rElement);
			TextElement tElement = new TextElement();
			tElement.setTextAlignmentAsLeft();
			tElement.setVerticalAlignmentAsTop();
			tElement.setLineSpacingAsSingle();
				Font tElementFont = new Font();
				tElementFont.setFontSize(8);
			tElement.addContent(tElementFont);
		tField.addContent(tElement);
			TextFieldExpression tfExpression = new TextFieldExpression();
			tfExpression.setClassType(classType);
			tfExpression.addField(fieldName);	
		tField.addContent(tfExpression);

		_designDoc.getDetail().addContent(tField);
	}
	
	private void checkHeaderBoundaries(int x1ToAdd, int x2ToAdd){
		if(_headerParametersMaxXCoordinate >0){
			int totalAfterThis = _headerParametersXCoordinate+x1ToAdd+x2ToAdd;
			if(_headerParametersMaxXCoordinate < totalAfterThis){
				_headerParametersXCoordinate=0;
				_pageHeaderHeight += _headerParametersHeight;
				_headerParametersYCoordinate += _headerParametersHeight;
				_designDoc.getPageHeader().setHeight(_pageHeaderHeight);
			}
		}
		if(_headerParametersXCoordinate == 0 && _headerParametersYCoordinate==0){
			_pageHeaderHeight += _headerParametersHeight;
			_designDoc.getPageHeader().setHeight(_pageHeaderHeight);
		}
	}
	
	private void addParameterToPageHeader(String prmName, int prmWidth, Class classType,boolean underline){
		_designDoc.addParameter(prmName,String.class);
		
		TextField tField = new TextField();
		tField.setIsBlankWhenNull(false);
		tField.setIsStretchWithOverflow(true);
		tField.setEvaluationTimeAsNow();
		tField.setHyperlinkTypeAsNone();
			ReportElement rElement = new ReportElement(_headerParametersXCoordinate,_headerParametersYCoordinate,prmWidth,_headerParametersHeight);
			_headerParametersXCoordinate += prmWidth;
			rElement.setPositionTypeAsFloat();
			rElement.setIsPrintRepeatedValues(true);
			rElement.setIsRemoveLineWhenBlank(false);
			rElement.setIsPrintInFirstWholeBand(false);
			rElement.setIsPrintWhenDetailOverflows(false);
		tField.addContent(rElement);
			TextElement tElement = new TextElement();
			tElement.setTextAlignmentAsLeft();
			tElement.setVerticalAlignmentAsTop();
			tElement.setLineSpacingAsSingle();
					Font tElementFont = new Font();
					tElementFont.setIsUnderline(underline);
					tElementFont.setFontSize(9);
			tElement.addContent(tElementFont);
		tField.addContent(tElement);
			TextFieldExpression tfExpression = new TextFieldExpression();
			tfExpression.setClassType(classType);
			tfExpression.addParameter(prmName);	
		tField.addContent(tfExpression);

		_designDoc.getPageHeader().addContent(tField);
		
		
	}
	
	public void addHeaderParameter(String prmLableName, int prmLableWidth, String prmValueName, Class prmValueClass, int prmValueWidth){
		checkHeaderBoundaries(prmLableWidth,prmValueWidth);
		
		addParameterToPageHeader(prmLableName,prmLableWidth, String.class,true);
		addParameterToPageHeader(prmValueName,prmValueWidth, prmValueClass,false);
		_headerParametersXCoordinate += _extraspaceBetweenParameterGroupsInHeader;
	}
	

	private static synchronized int getNextTempFileNumber(){
		if(_tempFileCounter == _TEMP_FILE_COUNTER_MAX){
		 _tempFileCounter=1;
		}
		return _tempFileCounter++;
	}
	
	private int getTempFileNumber(){
		if(_tempFileNumber == -1){
			_tempFileNumber = getNextTempFileNumber();
		}
		return _tempFileNumber;
	}
	
	public JasperDesign getJasperDesign(IWApplicationContext iwc) throws IOException, JRException{
		if(_designDoc != null){
			InputStream inputStream = _designDoc.getInputstream(getRealPathToDesignFile(iwc,TEMP_DESIGN_NAME,XML_FILE_EXTENSION));
			JasperDesign designToReturn = JasperManager.loadXmlDesign(inputStream);
			inputStream.close();
			return designToReturn;
		}
		return null;
	}
	
	
	private String getURIToDesign(IWContext iwc, String fileName, String extension) {
		IWMainApplication mainApp = iwc.getApplication();
		String separator = FileUtil.getFileSeparator();
		StringBuffer uri = new StringBuffer(mainApp.getApplicationContextURI());
		uri.append(separator)
			.append(mainApp.getIWCacheManager().IW_ROOT_CACHE_DIRECTORY)
			.append(separator)
			.append(REPORT_FOLDER)
			.append(separator)
			.append(DYNAMIC_DESIGN_FOLDER)
			.append(separator)
		    .append(getTempFileNumber())
		 	.append("_")
			.append(fileName)
			.append(DOT)
			.append(extension);
		return uri.toString();
	}
	
	
	private String getRealPathToDesignFile(IWApplicationContext iwc, String fileName, String extension) {
		IWMainApplication mainApp = iwc.getApplication();
		String separator = FileUtil.getFileSeparator();
		StringBuffer path = new StringBuffer(mainApp.getApplicationRealPath());
		path.append(mainApp.getIWCacheManager().IW_ROOT_CACHE_DIRECTORY)
			.append(separator)
			.append(REPORT_FOLDER)
			.append(separator)
			.append(DYNAMIC_DESIGN_FOLDER);
		
		// check if the folder exists create it if necessary
		// usually the folder should be already be there.
		// the folder is never deleted by this class
		String folderPath = path.toString();
		FileUtil.createFolder(folderPath);
		path.append(separator)
			.append(getTempFileNumber())
			.append("_")
			.append(fileName)
			.append(DOT)
			.append(extension);
		return path.toString();
	}
	
	
	
	/**
	 * Use this method to close the document before writing it to file
	 */
	public void close(){
		_designDoc.close();
	}
	
	
	public void setPageHeight(int height){
		_designDoc.setPageHeight(height);
	}
	
	public void setPageWidth(int width){
		_designDoc.setPageWidth(width);
	}
	
	public void setColumnWidth(int width){
		_designDoc.setColumnWidth(width);
	}
	
	public void setHeaderParametersHeight(int height){
		_headerParametersHeight = height;
	}
	
	public void setHeaderParametersMaxWidth(int width){
		_headerParametersMaxXCoordinate = width;
	}
	
	public void setColumnHeaderHeight(int heigth){
		_columnHeaderHeight = heigth;
		_designDoc.getColumnHeader().setHeight(heigth);
	}
	
	public void setDetailHeight(int heigth){
		_detailHeight = heigth;
		_designDoc.getDetail().setHeight(heigth);
	}
	
	public void setPageHeaderHeight(int heigth){
		_pageHeaderHeight = heigth;
		_designDoc.getPageHeader().setHeight(heigth);
	}
	
	public void setTitleHeight(int heigth){
		_reportTitleHeight = heigth;
		_designDoc.getTitle().setHeight(heigth);
	}
	
	
}
