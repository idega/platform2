/*
 * Created on 15.7.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.presentation;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.idega.block.dataquery.business.QueryConditionPart;
import com.idega.block.dataquery.business.QueryHelper;
import com.idega.block.dataquery.business.QueryService;
import com.idega.block.dataquery.data.Query;
import com.idega.block.dataquery.data.QueryHome;
import com.idega.block.dataquery.data.QueryResult;
import com.idega.block.datareport.business.DynamicReportDesign;
import com.idega.block.datareport.business.JasperReportBusiness;
import com.idega.business.IBOLookup;
import com.idega.data.IDOEntityField;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.FolderBlock;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.IntegerInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TimeInput;
import com.idega.presentation.ui.TimestampInput;
import com.idega.xml.XMLException;

import dori.jasper.engine.JRException;
import dori.jasper.engine.JasperPrint;
import dori.jasper.engine.design.JasperDesign;

/**
 * Title:		ReportGenerator
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class ReportGenerator extends FolderBlock {
	
	public final static String STYLE = "font-family:arial; font-size:8pt; color:#000000; text-align: justify; border: 1 solid #000000;";
	public final static String STYLE_2 = "font-family:arial; font-size:8pt; color:#000000; text-align: justify;";
	public final static String PRIFIX_PRM = "dr_";
	private static final String PRM_DR_GEN_STATE="dr_gen_state";

	
	private Integer _queryPK=null;
	private Vector _dynamicFields=new Vector();
	private String _reportFilePath="";
	private QueryHelper _queryParser = null;
	private QueryResult _dataSource = null;
	private Table _fieldTable = null;
	private JasperDesign _design = null;
	
	private String _reportName = "Report";
	
	/**
	 *	
	 */
	public ReportGenerator() {
		super();
	}
	
	
	private void parseQuery() throws XMLException, Exception{
		if(_queryParser == null){
			Query query = (Query)((QueryHome)IDOLookup.getHome(Query.class)).findByPrimaryKey(_queryPK);
			_queryParser = new QueryHelper(query);
		}
		Collection conditionsCollection = _queryParser.getListOfConditions();
		if(conditionsCollection != null){
			Iterator iter = conditionsCollection.iterator();
			while (iter.hasNext()) {
				QueryConditionPart element = (QueryConditionPart)iter.next();
				if(element.isDynamic()){
					
					//element.getEntity();
					//_dynamicFields.add(element.get)
				}
			}
		}
		
		//_dynamicFields
	}
	
	private void generateLayout(IWContext iwc) throws IOException, JRException{
		DynamicReportDesign designTemplate = new DynamicReportDesign(_reportName);
		
		int fieldCount = _dynamicFields.size();
		if(fieldCount > 0){
			int columnWidth = 555/fieldCount;
			
			Iterator iter = _dynamicFields.iterator();
			while (iter.hasNext()) {
				IDOEntityField element = (IDOEntityField)iter.next();
				designTemplate.addField(element.getUniqueFieldName(),element.getDataTypeClass(),columnWidth);
			}
			
			
		}
		
		designTemplate.close();
		_design = designTemplate.getJasperDesign(iwc);
	}
	
	private void generateDataSource(IWContext iwc) throws XMLException, Exception{
//		if(_queryParser == null){
//			Query query = (Query)((QueryHome)IDOLookup.getHome(Query.class)).findByPrimaryKey(_queryPK);
//			_queryParser = new QueryHelper(query);
//		}
		QueryService service = (QueryService)(IBOLookup.getServiceInstance(iwc,QueryService.class));
		_dataSource = service.generateQueryResult(_queryPK);

	}
	
	private void generateReport(IWContext iwc) throws RemoteException, JRException{
		if(_dataSource != null && _design != null){
			JasperReportBusiness business = (JasperReportBusiness)IBOLookup.getServiceInstance(iwc,JasperReportBusiness.class);
			HashMap _parameterMap = new HashMap();
			_parameterMap.put(DynamicReportDesign.PRM_REPORT_NAME,"Test Report");
			JasperPrint print = business.getReport(_dataSource,_parameterMap,_design);
			_reportFilePath = business.getExcelReport(print,_reportName);
			//business.getPdfReport(print,_reportName);
			//business.getHtmlReport(print,_reportName);
		}
	}
	
	
	public void setQuery(Integer queryPK){
		_queryPK = queryPK;
	}
	

	
	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		if(_queryPK!=null){
			String genState = iwc.getParameter(PRM_DR_GEN_STATE);
			if(genState==null || "".equals(genState)){
				parseQuery();
				lineUpElements(iwrb,iwc);
				Form submForm = new Form();
				submForm.add(_fieldTable);
				this.add(submForm);
			} else {
				generateDataSource(iwc);
				generateLayout(iwc);
				generateReport(iwc);
				this.add(getReportLink(iwc));
			}
		} else if(hasEditPermission()){
			add(iwrb.getLocalizedString("no_query_has_been_chosen_for_this_instance","No query has been chosen for this instance"));
		}//else{//Do nothing}
	}



	/**
	 * @param iwc
	 * @return
	 */
	private PresentationObject getReportLink(IWContext iwc) {
		Link link = new Link(_reportName,_reportFilePath);
		return link;
	}


	/**
	 * 
	 */
	private void lineUpElements(IWResourceBundle iwrb, IWContext iwc) {
		_fieldTable =new Table();
		_fieldTable.setBorder(1);
		
		if(_dynamicFields.size()>0){
			
			Iterator iterator = _dynamicFields.iterator();
			int row = 0;
			while (iterator.hasNext()) {
				IDOEntityField element = (IDOEntityField)iterator.next();
				row++;
				_fieldTable.add(getFieldLabel("SomeText-"+element.getUniqueFieldName())+":",1,row);
				_fieldTable.add(getFieldInputObject(element.getUniqueFieldName(),null,element.getDataTypeClass()),2,row);
			}
			
			_fieldTable.add(getSubmitButton(iwrb.getLocalizedString("generate_report"," Generate ")),1,++row);
			_fieldTable.mergeCells(1,row,2,row);
			_fieldTable.setColumnAlignment(1,Table.HORIZONTAL_ALIGN_RIGHT);
			_fieldTable.setColumnAlignment(2,Table.HORIZONTAL_ALIGN_LEFT);
		} else {
			_fieldTable.add(getSubmitButton(iwrb.getLocalizedString("generate_report"," Generate ")));
		}
		
	}
	
	private PresentationObject getSubmitButton(String text){
		SubmitButton button = new SubmitButton(text,PRM_DR_GEN_STATE,"2");
		setStyle(button);
		return button;
	}
	
	
	private Text getFieldLabel(String text){
		Text fieldLabel = new Text(text);
		setStyle(fieldLabel);
		return fieldLabel;
	}
	
	private PresentationObject getFieldInputObject(String key, String value, Class dataType) {
			
			if(dataType == Integer.class){
				IntegerInput fieldInput = new IntegerInput(PRIFIX_PRM + key);
				setStyle(fieldInput);
				return fieldInput;
			}else if(dataType == Time.class){
				TimeInput fieldInput = new TimeInput(PRIFIX_PRM + key);
				setStyle(fieldInput);
				return fieldInput;
			}else if(dataType == Date.class){
				DateInput fieldInput = new DateInput(PRIFIX_PRM + key);
				setStyle(fieldInput);
				return fieldInput;
			}else if(dataType == Timestamp.class){
				TimestampInput fieldInput = new TimestampInput(PRIFIX_PRM + key);
				setStyle(fieldInput);
				return fieldInput;
			}else{
				TextInput fieldInput = new TextInput(PRIFIX_PRM + key);
				setStyle(fieldInput);
				return fieldInput;
			}
	}
	
	
	public void setStyle(PresentationObject obj){
		if(obj instanceof Text){
			this.setStyle((Text)obj);
		} else {
			obj.setAttribute("style",STYLE);
		}
	}

	public void setStyle(Text obj){
		obj.setAttribute("style",STYLE_2);
	}
	
	
	
	//Old
//public void main(IWContext iwc) throws Exception {
////		IWBundle coreBundle = iwc.getApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
//	//IWBundle iwb = getBundle(iwc);
//	IWResourceBundle iwrb = getResourceBundle(iwc);
////		if(this.hasEditPermission()){
////			Link reportChooser = new Link(coreBundle.getImage("/shared/edit.gif"));
////			reportChooser.setWindowToOpen(ReportLayoutChooserWindow.class);
//////			ICInformationFolder folder =  this.getWorkingFolder()
//////			ICFile queryFolder = lookUpFile(QUERY_FOLDER_NAME);
//////			ICFile layoutFolder = lookUpFile(LAYOUT_FOLDER_NAME);
//////			if (queryFolder != null)  {
//////				reportChooser.addParameter(ReportLayoutChooser.SET_ID_OF_QUERY_FOLDER_KEY, queryFolder.getPrimaryKey().toString());
//////			}
//////			if (layoutFolder != null) {
//////				reportChooser.addParameter(ReportLayoutChooser.SET_ID_OF_DESIGN_FOLDER_KEY, layoutFolder.getPrimaryKey().toString());
//////			}
////			//reportChooser.addParameter();
////			this.add(reportChooser);
////		}
//
//	ICObjectInstance inst = this.getICObjectInstance();
//	if(inst != null){
//		ReportTranscription rTranscription = ((ReportTranscriptionHome)IDOLookup.getHome(ReportTranscription.class)).getReportTranscriptionForObjectInstance(inst);
//		if(rTranscription != null){
//			//Here starts the main functionality in the block
//				
//			//this.add(getPreGenerationState());
//			//this.add(getPostGenerationState());
//			/////	
//				Query queryData = rTranscription.getQuery();
//				QueryHelper query = new QueryHelper(queryData);
//					
//				QueryConditionPart part = (QueryConditionPart)query.getListOfConditions().get(0);
//					
//			//////
//				
//				
//				
//			Form formForGeneratedElements = new Form(); 		
//			int rows=1;
//			Table fieldTable = new Table(2,rows);
//			SubmitButton generateButton = new SubmitButton("Generate","generate","true");
//		
//			fieldTable.add(generateButton,1,rows);
//			formForGeneratedElements.add(fieldTable);
//			add(formForGeneratedElements);
//
//				
//		} else {
//			add(iwrb.getLocalizedString("no_report_transcription_is_defined_for_this_instance","No Report transcription is defined for this instance"));
//		}
//	} else {
//		add(iwrb.getLocalizedString("block_has_no_instance_id","Block has no Instance id"));
//	}
//		
//		
//}


}
