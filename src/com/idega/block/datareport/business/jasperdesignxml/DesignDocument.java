/*
 * Created on 30.7.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.business.jasperdesignxml;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
//import java.util.List;
import java.util.Vector;

import org.jdom.DocType;

import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLOutput;


/**
 * Title:		DesignDocument
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class DesignDocument extends XMLDocument {
	
	private static final String NAME = "jasperReport";
	
	private static final String ATTRIBUTE_NAME = "name"; //NMTOKEN #REQUIRED
	private static final String ATTRIBUTE_COLUMN_COUNT = "columnCount"; //NMTOKEN "1"
	
	private static final String ATTRIBUTE_PRINT_ORDER = "printOrder"; //(Vertical | Horizontal) "Vertical"
	private static final String VALUE_PRINT_ORDER_VERTICAL = "Vertical";
	private static final String VALUE_PRINT_ORDER_HORIZONTAL = "Horizontal";
	
	private static final String ATTRIBUTE_PAGE_WIDTH = "pageWidth"; //NMTOKEN "595"
	private static final String ATTRIBUTE_PAGE_HEIGHT = "pageHeight"; //NMTOKEN "842"
	
	private static final String ATTRIBUTE_ORIENTATION = "orientation"; //(Portrait | Landscape) "Portrait"
	private static final String VALUE_ORIENTATION_PORTRAIT = "Portrait";
	private static final String VALUE_ORIENTATION_LANDSCAPE = "Landscape";
	
	private static final String ATTRIBUTE_WHEN_NO_DATA_TYPE = "whenNoDataType"; //(NoPages | BlankPage | AllSectionsNoDetail) "NoPages"
	private static final String VALUE_WHEN_NO_DATA_TYPE_NOPAGES = "NoPages";
	private static final String VALUE_WHEN_NO_DATA_TYPE_BLANK_PAGE = "BlankPage";
	private static final String VALUE_WHEN_NO_DATA_TYPE_ALL_SECTION_NO_DETAIL = "AllSectionsNoDetail";
	
	private static final String ATTRIBUTE_COLUMN_WIDTH = "columnWidth"; //NMTOKEN "555"
	private static final String ATTRIBUTE_COLUMN_SPACING = "columnSpacing"; //NMTOKEN "0"
	private static final String ATTRIBUTE_LEFT_MARGIN = "leftMargin"; //NMTOKEN "20"
	private static final String ATTRIBUTE_RIGHT_MARGIN = "rightMargin"; //NMTOKEN "20"
	private static final String ATTRIBUTE_TOP_MARGIN = "topMargin"; //NMTOKEN "30"
	private static final String ATTRIBUTE_BOTTOM_MARGIN = "bottomMargin"; //NMTOKEN "30"
	private static final String ATTRIBUTE_IS_TITLE_NEW_PAGE = "isTitleNewPage"; //(true | false) "false"	
	private static final String ATTRIBUTE_IS_SUMMARY_NEW_PAGE = "isSummaryNewPage"; //(true | false) "false"
	private static final String ATTRIBUTE_SCRIPTLET_CLASS = "scriptletClass"; //NMTOKEN #IMPLIED
	
	
	private Vector _parameters = null;
	private Vector _fields = new Vector();
	private Title _title=null;
	private PageHeader _pageHeader=null;
	private ColumnHeader _columnHeader=null;
	private Detail _detail=null;
	private ColumnFooter _columnFooter=null;
	private PageFooter _pageFooter=null;
	private Summary _summary=null;
	
	

	private XMLElement _rootElement=null;
	/**
	 * @param element
	 */
	private DesignDocument(XMLElement element) {
		super(element);
		DocType docType = new DocType(NAME,"-//JasperReports//DTD Report Design//EN","http://jasperreports.sourceforge.net/dtds/jasperreport.dtd");
		this.setDocType(docType);
	}
	
	public DesignDocument(String name){
		this(new XMLElement(NAME));
		setReportName(name);
	}

	
	public XMLElement getRootElement() {
		if (_rootElement == null)
			_rootElement = super.getRootElement();

		return _rootElement;
	}
	
	public void setRootElement(XMLElement element) {
		_rootElement = element;
		super.setRootElement(element);
	}
	
	
	//Adders
	
	public void addParameter(Parameter parm){
		if(_parameters == null){
			_parameters = new Vector();
		}
		_parameters.add(parm);
		//this.getRootElement().addContent(parm);
	}
	
	public void addParameter(String name, Class classType, boolean isForPrompting){
		Parameter parameter = new Parameter(name);
		parameter.setClassType(classType);
		parameter.setIsForPrompting(isForPrompting);
		addParameter(parameter);
	}
	
	public void addParameter(String name, Class classType){
		Parameter parameter = new Parameter(name);
		parameter.setClassType(classType);
		addParameter(parameter);
	}
	
	public void addParameter(String name){
		Parameter parameter = new Parameter(name);
		addParameter(parameter);
	}
	
	public void addParameter(String name, boolean isForPrompting){
		Parameter parameter = new Parameter(name);
		parameter.setIsForPrompting(isForPrompting);
		addParameter(parameter);
	}
	
	
	public void addField(Field field){
		_fields.add(field);
		//this.getRootElement().addContent(field);
	}
	
	public void addField(String name, Class classType){
		Field field = new Field(name);
		field.setClassType(classType);
		addField(field);
	}
	
	public void addField(String name){
		addField(new Field(name));
	}
	
	public void addObjectField(String name){
		addField(name,java.lang.Object.class);
	}
	
	public void addBooleanField(String name){
		addField(name,java.lang.Boolean.class);
	}
	
	public void addByteField(String name){
		addField(name,java.lang.Byte.class);
	}
	
	public void addDateField(String name){
		addField(name,java.util.Date.class);
	}
	
	public void addTimestampField(String name){
		addField(name,java.sql.Timestamp.class);
	}
	
	public void addTimeField(String name){
		addField(name,java.sql.Time.class);
	}
	
	public void addDoubleField(String name){
		addField(name,java.lang.Double.class);
	}
	
	public void addFloadField(String name){
		addField(name,java.lang.Float.class);
	}
	
	public void addIntegerField(String name){
		addField(name,java.lang.Integer.class);
	}
	
	public void addInputStreamField(String name){
		addField(name,java.io.InputStream.class);
	}
	
	public void addLongField(String name){
		addField(name,java.lang.Long.class);
	}
	
	public void addShortField(String name){
		addField(name,java.lang.Short.class);
	}
	
	public void addBigDecimalField(String name){
		addField(name,java.math.BigDecimal.class);
	}
	
	
	
	// Setters 
	public void setTitle(Title title){
		if(_title != null){
			this.getRootElement().removeContent(_title);
		}
		_title=title;
		//this.getRootElement().addContent(_title);
	}
	
	public void setPageHeader(PageHeader pageHeader){
		if(_pageHeader != null){
			this.getRootElement().removeContent(_pageHeader);
		}
		_pageHeader=pageHeader;
		//this.getRootElement().addContent(_pageHeader);
	}
	
	public void setColumnHeader(ColumnHeader columnHeader){
		if(_columnHeader != null){
			this.getRootElement().removeContent(_columnHeader);
		}
		_columnHeader=columnHeader;
		//this.getRootElement().addContent(_columnHeader);
	}
	
	public void setDetail(Detail detail){
		if(_detail != null){
			this.getRootElement().removeContent(_detail);
		}
		_detail=detail;
		//this.getRootElement().addContent(_detail);
	}
	
	public void setColumnFooter(ColumnFooter columnFooter){
		if(_columnFooter != null){
			this.getRootElement().removeContent(_columnFooter);
		}
		_columnFooter=columnFooter;
		//this.getRootElement().addContent(_columnFooter);
	}
	
	public void setPageFooter(PageFooter pageFooter){
		if(_pageFooter != null){
			this.getRootElement().removeContent(_pageFooter);
		}
		_pageFooter=pageFooter;
		//this.getRootElement().addContent(_pageFooter);
	}
	
	public void setSummary(Summary summary){
		if(_summary != null){
			this.getRootElement().removeContent(_summary);
		}
		_summary=summary;
		//this.getRootElement().addContent(_summary);
	}
	
	public void setReportName(String name){
		this.getRootElement().setAttribute(ATTRIBUTE_NAME,name);
	}
	
	public void setColumnCount(int count){
		this.getRootElement().setAttribute(ATTRIBUTE_COLUMN_COUNT,Integer.toString(count));
	}
	
	public void setPrintOrderVertical(){
		this.getRootElement().setAttribute(ATTRIBUTE_PRINT_ORDER,VALUE_PRINT_ORDER_VERTICAL);
	}
	
	public void setPrintOrderHorizontal(){
		this.getRootElement().setAttribute(ATTRIBUTE_PRINT_ORDER,VALUE_PRINT_ORDER_HORIZONTAL);
	}
	
	public void setPageWidth(int width){
		this.getRootElement().setAttribute(ATTRIBUTE_PAGE_WIDTH,Integer.toString(width));
	}
	
	public void setPageHeight(int height){
		this.getRootElement().setAttribute(ATTRIBUTE_PAGE_HEIGHT,Integer.toString(height));
	}
	
	public void setOrientationLandscape(){
		this.getRootElement().setAttribute(ATTRIBUTE_ORIENTATION,VALUE_ORIENTATION_LANDSCAPE);
	}
	
	public void setOrientationPortrait(){
		this.getRootElement().setAttribute(ATTRIBUTE_ORIENTATION,VALUE_ORIENTATION_PORTRAIT);
	}
	
	public void setColumnWidth(int width){
		this.getRootElement().setAttribute(ATTRIBUTE_COLUMN_WIDTH,Integer.toString(width));
	}
	
	public void setColumnSpacing(int spacing){
		this.getRootElement().setAttribute(ATTRIBUTE_COLUMN_SPACING,Integer.toString(spacing));
	}
	
	public void setLeftMargin(int lMargin){
		this.getRootElement().setAttribute(ATTRIBUTE_LEFT_MARGIN,Integer.toString(lMargin));
	}
	
	public void setRightMargin(int rMargin){
		this.getRootElement().setAttribute(ATTRIBUTE_RIGHT_MARGIN,Integer.toString(rMargin));
	}
	
	public void setTopMargin(int tMargin){
		this.getRootElement().setAttribute(ATTRIBUTE_TOP_MARGIN,Integer.toString(tMargin));
	}
	
	public void setBottomMargin(int bMargin){
		this.getRootElement().setAttribute(ATTRIBUTE_BOTTOM_MARGIN,Integer.toString(bMargin));
	}
	
	public void setIsTitleNewPage(boolean value){
		this.getRootElement().setAttribute(ATTRIBUTE_IS_TITLE_NEW_PAGE,String.valueOf(value));
	}
	
	public void setIsSummaryNewPage(boolean value){
		this.getRootElement().setAttribute(ATTRIBUTE_IS_SUMMARY_NEW_PAGE,String.valueOf(value));
	}
	
	private void setWhenNoDataType(String value){
		this.getRootElement().setAttribute(ATTRIBUTE_WHEN_NO_DATA_TYPE,value);
	}
	
	public void setWhenNoDataTypeAsNoPages(){
		setWhenNoDataType(VALUE_WHEN_NO_DATA_TYPE_NOPAGES);
	}
	
	public void setWhenNoDataTypeAsBlankPage(){
		setWhenNoDataType(VALUE_WHEN_NO_DATA_TYPE_BLANK_PAGE);
	}
	
	public void setWhenNoDataTypeAsAllSectionNoDetail(){
		setWhenNoDataType(VALUE_WHEN_NO_DATA_TYPE_ALL_SECTION_NO_DETAIL);
	}
		
	
	
	//Getters
	
	/**
	 * @return
	 */
	public ColumnFooter getColumnFooter() {
		return _columnFooter;
	}

	/**
	 * @return
	 */
	public ColumnHeader getColumnHeader() {
		return _columnHeader;
	}

	/**
	 * @return
	 */
	public Detail getDetail() {
		return _detail;
	}

	/**
	 * @return
	 */
	public PageFooter getPageFooter() {
		return _pageFooter;
	}

	/**
	 * @return
	 */
	public PageHeader getPageHeader() {
		return _pageHeader;
	}

	/**
	 * @return
	 */
	public Summary getSummary() {
		return _summary;
	}

	/**
	 * @return
	 */
	public Title getTitle() {
		return _title;
	}
	
	
	
	
	
	public File writeToFile(String path)throws IOException{
		BufferedOutputStream outputStream = null;
		File auxiliaryFile = null;
		auxiliaryFile = new File(path);
		
		//	TODO remove check  TMP
		//if(auxiliaryFile != null && !auxiliaryFile.exists()){
			try {
	//			boolean dirsCreated = auxiliaryFile.mkdirs();
	//			System.out.println("Directories were created: "+dirsCreated);
				outputStream = new BufferedOutputStream(new FileOutputStream(auxiliaryFile));
			}
			catch (FileNotFoundException ex)  {
				System.err.println("[XMLData] problem creating file. Message is: "+ex.getMessage());
				ex.printStackTrace(System.err);
				throw new IOException("xml file could not be stored");
			}
		
		
		
			// now we have an output stream of the auxiliary file
			// write to the xml file
			XMLOutput xmlOutput = new XMLOutput("  ", true);
			xmlOutput.setLineSeparator(System.getProperty("line.separator"));
			xmlOutput.setTextNormalize(true);
			//xmlOutput.setEncoding("ISO8859_1");
			//xmlOutput.setEncoding("iso-8859-1");
			//xmlOutput.setEncoding("UTF-8");
	
			try {
				xmlOutput.output(this, outputStream);
			}
			catch (IOException ex) {
				System.err.println("[XMLData] problem writing to file. Message is: "+ex.getMessage());
				ex.printStackTrace(System.err);
				outputStream.close();
				throw new IOException("xml file could not be stored");
			}
			outputStream.close();
			// writing finished
		
		//}
				
		return auxiliaryFile;

	}
	
	
	/**
	 * @param file, to get File from this class use writeToFile(String path)
	 * @return 
	 * @throws IOException
	 * @see DesignDocument#writeToFile(String path)
	 */
	public InputStream getInputstream(File file) throws IOException{
		BufferedInputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
		}
		catch (FileNotFoundException ex)  {
			System.err.println("[XMLData] problem reading file. Message is: "+ex.getMessage());
			ex.printStackTrace(System.err);
			throw new IOException("xml file could not be stored");
		}
		return inputStream;
	}
	
	
	/**
	 * 
	 * 
	 * @param tmpPath, path on the disk where File is stored before geting Inputstream from it
	 * @return
	 * @throws IOException
	 * @see DesignDocument#writeToFile(String path)
	 * @see InputStream getInputstream(File file)
	 */
	public InputStream getInputstream(String tmpPath) throws IOException{
		
		
		return getInputstream(writeToFile(tmpPath));
	}
	
	
	
	/**
	 * Use this method to close the document before writing it to file
	 */
	public void close(){
		Iterator iter = _parameters.iterator();
		while (iter.hasNext()) {
			Parameter element = (Parameter)iter.next();
			this.getRootElement().addContent(element);
		}
		
		Iterator iter2 = _fields.iterator();
		while (iter2.hasNext()) {
			Field element = (Field)iter2.next();
			this.getRootElement().addContent(element);
		}
		
		if(_title != null){
			this.getRootElement().addContent(_title);
		}
		
		if(_pageHeader != null){
			this.getRootElement().addContent(_pageHeader);
		}
		
		if(_columnHeader != null){
			this.getRootElement().addContent(_columnHeader);
		}
		
		if(_detail != null){
			this.getRootElement().addContent(_detail);
		}
		
		if(_columnFooter != null){
			this.getRootElement().addContent(_columnFooter);
		}
		
		if(_pageFooter != null){
			this.getRootElement().addContent(_pageFooter);
		}
		
		if(_summary != null){
			this.getRootElement().addContent(_summary);
		}
	}
	
//	public List getParameters(){
//		if(_parameters == null){
//			_parameters = new Vector();
//			Iterator iter = getRootElement().getContent().iterator();
//			while (iter.hasNext()) {
//				XMLElement element = (XMLElement)iter.next();
//				if(element.getName().equals(Parameter.ELEMENT_NAME)){
//					Parameter prm = new Parameter();
//					//Vantar að geta búið til parameter klasa út frá XMLElement
//				}
//					
//			}
//		}
//		return _parameters;
//	}
	

}
