/*
 * Created on 15.7.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.presentation;

import java.io.IOException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.dataquery.business.QueryConditionPart;
import com.idega.block.dataquery.business.QueryFieldPart;
import com.idega.block.dataquery.business.QueryHelper;
import com.idega.block.dataquery.business.QueryService;
import com.idega.block.dataquery.data.Query;
import com.idega.block.dataquery.data.QueryHome;
import com.idega.block.datareport.business.DynamicReportDesign;
import com.idega.block.datareport.business.JasperReportBusiness;
import com.idega.block.datareport.data.MethodInvocationXMLFile;
import com.idega.block.datareport.data.MethodInvocationXMLFileHome;
import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.datareport.util.ReportableField;
import com.idega.block.datareport.xml.methodinvocation.ClassDescription;
import com.idega.block.datareport.xml.methodinvocation.MethodDescription;
import com.idega.block.datareport.xml.methodinvocation.MethodInput;
import com.idega.block.datareport.xml.methodinvocation.MethodInvocationDocument;
import com.idega.block.datareport.xml.methodinvocation.MethodInvocationParser;
import com.idega.business.IBOLookup;
import com.idega.business.IBOSession;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.BusyBar;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.IntegerInput;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TimeInput;
import com.idega.presentation.ui.TimestampInput;
import com.idega.util.reflect.MethodFinder;
import com.idega.xml.XMLException;


import dori.jasper.engine.JRDataSource;
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
public class ReportGenerator extends Block {
	
	public final static String STYLE = "font-family:arial; font-size:8pt; color:#000000; text-align: justify; border: 1 solid #000000;";
	public final static String STYLE_2 = "font-family:arial; font-size:8pt; color:#000000; text-align: justify;";
	public final static String PRIFIX_PRM = "dr_";
	private static final String PRM_DR_GEN_STATE="dr_gen_state";
	private static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.datareport";
	
	private Integer _queryPK=null;
	private Integer _methodInvacationPK = null;
	private MethodInvocationDocument _methodInvokeDoc = null;
	private Vector _dynamicFields=new Vector();
	private Collection _allFields = null;
	private String _reportFilePath="";
	private QueryHelper _queryParser = null;
	private JRDataSource _dataSource = null;
	private Table _fieldTable = null;
	private JasperDesign _design = null;
	private HashMap _parameterMap = new HashMap();
	private BusyBar _busy = null;
	
	private String _prmLablePrefix = "label_";
	
	private String _reportName = "Generated Report";
	private boolean _canChangeReportName = true;
	private String PRM_REPORT_NAME = "report_name";
	

	
	
	private Map _extraHeaderParameters = null;
	
	/**
	 *	
	 */
	public ReportGenerator() {
		super();
		_busy = new BusyBar("busy_generating_report");
	}
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	
	private void parseQuery() throws XMLException, Exception{
		if(_queryParser == null){
			Query query = (Query)((QueryHome)IDOLookup.getHome(Query.class)).findByPrimaryKey(_queryPK);
			_queryParser = new QueryHelper(query);
		}
		
		_allFields = _queryParser.getListOfFields();
		if(_allFields != null){
			System.out.println("ReportGenerator#parseQuery() - _queryParser.getListOfFields().size() = "+_allFields.size());
		} else {
			System.out.println("ReportGenerator#parseQuery() - _queryParser.getListOfFields() == null");
		}
		
		Collection conditionsCollection = _queryParser.getListOfConditions();
		if(conditionsCollection != null){
			Iterator iter = conditionsCollection.iterator();
			while (iter.hasNext()) {
				QueryConditionPart element = (QueryConditionPart)iter.next();
				if(element.isDynamic()){
					_dynamicFields.add(new ReportableField(element.getIDOEntityField()));
				}
			}
		}
		
		
		
		
		//_dynamicFields
	}
	
	private int calculateTextFieldWidthForString(String str){
		int fontSize = 9;
		return (int)( 5+(str.length()*fontSize*0.58));
	}
	
	private void generateLayout(IWContext iwc) throws IOException, JRException{
		int columnWidth = 120;
		int prmLableWidth = 95;
		int prmValueWidth = 55;
		String tmpName = iwc.getParameter(getParameterName(PRM_REPORT_NAME));
		if(tmpName != null){
			_reportName = tmpName;
		}
				
		DynamicReportDesign designTemplate = new DynamicReportDesign("GeneratedReport");
		boolean isMethodInvocation = false;
		if(_queryPK == null && _methodInvacationPK != null){
			isMethodInvocation=true;
			if(_dataSource != null && _dataSource instanceof ReportableCollection){
				_allFields = ((ReportableCollection)_dataSource).getListOfFields();
			}
			
		}
		
		if(_dynamicFields != null && _dynamicFields.size() > 0){
			if(_queryPK != null){
				Iterator iter = _dynamicFields.iterator();
				while (iter.hasNext()) {
					ReportableField element = (ReportableField)iter.next();
					String prmName =element.getName();
					
					String tmpPrmLabel = (String)_parameterMap.get(_prmLablePrefix+prmName);
					String tmpPrmValue = (String)_parameterMap.get(prmName);
					int tmpPrmLabelWidth = (tmpPrmLabel != null)?calculateTextFieldWidthForString(tmpPrmLabel):prmLableWidth;
					int tmpPrmValueWidth = (tmpPrmValue != null)?calculateTextFieldWidthForString(tmpPrmLabel):prmValueWidth;
					designTemplate.addHeaderParameter(_prmLablePrefix+prmName,tmpPrmLabelWidth,prmName,String.class,tmpPrmValueWidth);
				}
			} else {
				Iterator iter = _dynamicFields.iterator();
				while (iter.hasNext()) {
					ClassDescription element = (ClassDescription)iter.next();
					String prmName =element.getName();
					
					String tmpPrmLabel = (String)_parameterMap.get(_prmLablePrefix+prmName);
					String tmpPrmValue = (String)_parameterMap.get(prmName);
					int tmpPrmLabelWidth = (tmpPrmLabel != null)?calculateTextFieldWidthForString(tmpPrmLabel):prmLableWidth;
					int tmpPrmValueWidth = (tmpPrmValue != null)?calculateTextFieldWidthForString(tmpPrmValue):prmValueWidth;
					designTemplate.addHeaderParameter(_prmLablePrefix+prmName,tmpPrmLabelWidth,prmName,String.class,tmpPrmValueWidth);
				}
			}
		}
		
		if(_extraHeaderParameters != null){
			Iterator keyIter = _extraHeaderParameters.keySet().iterator();
			Iterator valueIter = _extraHeaderParameters.values().iterator();
			while (keyIter.hasNext()) {
				String keyLabel = (String)keyIter.next();
				String valueLabel = (String)valueIter.next();
				if(keyIter.hasNext()){
					String keyValue = (String)keyIter.next();
					String valueValue = (String)valueIter.next();
					
					String tmpPrmLabel = valueLabel;
					String tmpPrmValue = valueValue;
					int tmpPrmLabelWidth = (tmpPrmLabel != null)?calculateTextFieldWidthForString(tmpPrmLabel):prmLableWidth;
					int tmpPrmValueWidth = (tmpPrmValue != null)?calculateTextFieldWidthForString(tmpPrmValue):prmValueWidth;
					designTemplate.addHeaderParameter(keyLabel,tmpPrmLabelWidth,keyValue,String.class,tmpPrmValueWidth);
				}
				
			}
		}
		
		
		if(_allFields != null && _allFields.size() > 0){
			//System.out.println("ReportGenerator.");
			
			
			//TMP
			//TODO get columnspacing (15) and it to columnsWidth;
			int columnsWidth = columnWidth*_allFields.size()+15*(_allFields.size()-1);
			//TMP
			//TODO get page Margins (20) and add them to pageWidth;
			designTemplate.setPageWidth(columnsWidth+20+20);
			designTemplate.setColumnWidth(columnsWidth);
			
			//
			Locale currentLocale = iwc.getCurrentLocale();
			Iterator iter = _allFields.iterator();
			
			if(isMethodInvocation){
				while (iter.hasNext()) {
					ReportableField field = (ReportableField)iter.next();
					String name = field.getName();
					_parameterMap.put(name,field.getLocalizedName(currentLocale));
					designTemplate.addField(name,field.getValueClass(),columnWidth);
				}
			} else {
				while (iter.hasNext()) {
					try {
						QueryFieldPart element = (QueryFieldPart)iter.next();
						ReportableField field = new ReportableField(element.getIDOEntityField());
						String name = field.getName();
						_parameterMap.put(name,field.getLocalizedName(currentLocale)); 
						designTemplate.addField(name,field.getValueClass(),columnWidth);
					} catch (IDOLookupException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}	
		}
		
		designTemplate.close();
		_design = designTemplate.getJasperDesign(iwc);
	}
	
	private void generateDataSource(IWContext iwc) throws XMLException, Exception{
		Locale currentLocale = iwc.getCurrentLocale();
		if(_queryPK != null){
			QueryService service = (QueryService)(IBOLookup.getServiceInstance(iwc,QueryService.class));
			_dataSource = service.generateQueryResult(_queryPK);
		} else if(_methodInvokeDoc != null){
			List mDescs = _methodInvokeDoc.getMethodDescriptions();
			if(mDescs != null){
				Iterator it = mDescs.iterator();
				if(it.hasNext()){
					MethodDescription mDesc = (MethodDescription)it.next();
					
					ClassDescription mainClassDesc = mDesc.getClassDescription();
					Class mainClass = mainClassDesc.getClassObject();
					String type = mainClassDesc.getType();
					String methodName = mDesc.getName();
//					System.out.println("methodname: "+methodName);
					
					MethodInput input = mDesc.getInput();
					List parameters = null;
					if(input != null){
						parameters = input.getClassDescriptions();
					}
					
					Object[] prmVal = null;
					Class[] paramTypes=null;
					
					if(parameters != null){
						prmVal = new Object[parameters.size()];
						paramTypes = new Class[parameters.size()];
						ListIterator iterator = parameters.listIterator();
						while (iterator.hasNext()) {
							int index = iterator.nextIndex();
							ClassDescription clDesc = (ClassDescription)iterator.next();
							Class prmClassType = clDesc.getClassObject();
							paramTypes[index] = prmClassType;
							String prm = iwc.getParameter(getParameterName(clDesc.getName()));
							Object obj = getParameterObject(iwc,prm,prmClassType);
							
							
							_parameterMap.put(_prmLablePrefix+clDesc.getName(),clDesc.getLocalizedName(currentLocale)+":");
							_parameterMap.put(clDesc.getName(),prm);
							
							
							
//							switch (index) {
//								case 0:
//									obj = new IWTimestamp(23,12,1898).getDate();
//									break;
//								case 1:
//									obj = new IWTimestamp(23,12,1920).getDate();
//									break;
//								case 2:
//									obj = new IWTimestamp(2002,7,10,15,17,39).getTimestamp();
//									break;
//								case 3:
//									obj = new IWTimestamp(2002,7,12,15,17,40).getTimestamp();
//									break;
//							}
							prmVal[index] = obj;
						}
					} else {
						// prmVal = String[0];
					}
					 
				
					Object forInvocationOfMethod = null;
					if(ClassDescription.VALUE_TYPE_IDO_SESSION_BEAN.equals(type)){
						forInvocationOfMethod = IBOLookup.getSessionInstance(iwc,mainClass);						
					} else if(ClassDescription.VALUE_TYPE_IDO_SERVICE_BEAN.equals(type)){
						forInvocationOfMethod = IBOLookup.getServiceInstance(iwc,mainClass);
					} else if(ClassDescription.VALUE_TYPE_IDO_ENTITY_HOME.equals(type)){
						forInvocationOfMethod = IDOLookup.getHome(mainClass);//System.out.println("["+this.getClassName()+"]: not implemented yet for this classType: "+type);
					} else { //ClassDescription.VALUE_TYPE_CLASS.equals(type))
						forInvocationOfMethod = mainClass.newInstance();
					}
					
					MethodFinder mf = MethodFinder.getInstance();
						
					Method method = mf.getMethodWithNameAndParameters(mainClass,methodName,paramTypes);
					_dataSource = (JRDataSource)method.invoke(forInvocationOfMethod,prmVal);
					
					if(_dataSource!= null && _dataSource instanceof ReportableCollection){
						_extraHeaderParameters = ((ReportableCollection)_dataSource).getExtraHeaderParameters();
						if(_extraHeaderParameters != null){
							_parameterMap.putAll(_extraHeaderParameters);
						}
					}
				}
			}
			
			
		}
		

	}
	
	private void generateReport(IWContext iwc) throws RemoteException, JRException{
		if(_dataSource != null && _design != null){
			JasperReportBusiness business = (JasperReportBusiness)IBOLookup.getServiceInstance(iwc,JasperReportBusiness.class);
			
			
			
			
			_parameterMap.put(DynamicReportDesign.PRM_REPORT_NAME,_reportName);
			JasperPrint print = business.getReport(_dataSource,_parameterMap,_design);
			_reportFilePath = business.getExcelReport(print,_reportName);
			//business.getPdfReport(print,_reportName);
			//business.getHtmlReport(print,_reportName);
		}
	}
	
	
	public void setQuery(Integer queryPK){
		_queryPK = queryPK;
	}
	
	public void setMethodInvocation(Integer methodInvocationPK){
		_methodInvacationPK = methodInvocationPK;
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
				parseQuery();
				generateDataSource(iwc);
				generateLayout(iwc);
				generateReport(iwc);
				this.add(getReportLink(iwc));
			}
		} else if(_methodInvacationPK != null){
			String genState = iwc.getParameter(PRM_DR_GEN_STATE);
			if(genState==null || "".equals(genState)){
				parseMethodInvocationXML();
				lineUpElements(iwrb,iwc);
				Form submForm = new Form();
				submForm.add(_fieldTable);
				this.add(submForm);
			} else {
				System.out.println("\n[ReportGenerator]: starts generating...");
				System.out.println("[ReportGenerator]: parsing xml...");
				long time1 = System.currentTimeMillis();
				parseMethodInvocationXML();
				long time2 = System.currentTimeMillis();
				System.out.println("[ReportGenerator]: took "+(time2-time1)+"ms, total of "+(time2-time1)+"ms");
				System.out.println("[ReportGenerator]: generating datasource...");
				generateDataSource(iwc);
				long time3 = System.currentTimeMillis();
				System.out.println("[ReportGenerator]: took "+(time3-time2)+"ms, total of "+(time3-time1)+"ms");
				System.out.println("[ReportGenerator]: generating layout...");
				generateLayout(iwc);
				long time4 = System.currentTimeMillis();
				System.out.println("[ReportGenerator]: took "+(time4-time3)+"ms, total of "+(time4-time1)+"ms");
				System.out.println("[ReportGenerator]: generating report...");
				generateReport(iwc);
				long time5 = System.currentTimeMillis();
				System.out.println("[ReportGenerator]: took "+(time5-time4)+"ms, total of "+(time5-time1)+"ms");
				System.out.println("[ReportGenerator]: ...finished\n");
				this.add(getReportLink(iwc));

			}
		} else if(hasEditPermission()){
			add(iwrb.getLocalizedString("no_query_has_been_chosen_for_this_instance","No query has been chosen for this instance"));
		}//else{//Do nothing}
	}



	/**
	 * 
	 */
	private void parseMethodInvocationXML() throws IDOLookupException, FinderException, XMLException {
		MethodInvocationXMLFile file = (MethodInvocationXMLFile)((MethodInvocationXMLFileHome)IDOLookup.getHome(MethodInvocationXMLFile.class)).findByPrimaryKey(_methodInvacationPK);
		if(file != null){
			_methodInvokeDoc = (MethodInvocationDocument)new MethodInvocationParser().parse(file.getFileValue());
		}
		
		if(_methodInvokeDoc != null){
			List methods = _methodInvokeDoc.getMethodDescriptions();
			if(methods != null){
				Iterator iter = methods.iterator();
				if(iter.hasNext()){
					MethodDescription mDesc = (MethodDescription)iter.next();
					
					MethodInput mInput = mDesc.getInput();
					if(mInput != null){
						_dynamicFields.addAll(mInput.getClassDescriptions());
//						List classDesc = mInput.getClassDescriptions();
//						if(classDesc!= null){
//							Iterator iterator = classDesc.iterator();
//							while (iterator.hasNext()) {
//								ClassDescription cDesc = (ClassDescription)iterator.next();
//								
//								
//								
//							}
//						}
					}
					
				}
			}
		}
		
		
		
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
		IWMainApplication iwma = iwc.getApplicationContext().getApplication();		
		IWBundle coreBundle = iwma.getBundle(this.IW_CORE_BUNDLE_IDENTIFIER);
		
		_fieldTable =new Table();
		//_fieldTable.setBorder(1);
		
		int row = 0;
		
		row++;
		_fieldTable.add(getFieldLabel(iwrb.getLocalizedString("choose_report_name","Report name"))+":",1,row);
		InterfaceObject nameInput = getFieldInputObject(PRM_REPORT_NAME,null,String.class);
		nameInput.setDisabled(!_canChangeReportName);
		nameInput.setValue(_reportName);
		_fieldTable.add(nameInput,2,row);
		_fieldTable.add(new HiddenInput(PRM_DR_GEN_STATE,"2"),2,row);

		//TODO Let Reportable field and ClassDescription impliment the same interface (IDODynamicReportableField) to decrease code duplications
		if(_queryPK != null){
			if(_dynamicFields.size()>0){
			
				Iterator iterator = _dynamicFields.iterator();
				
				while (iterator.hasNext()) {
					ReportableField element = (ReportableField)iterator.next();
					row++;
					_fieldTable.add(getFieldLabel(element.getLocalizedName(iwc.getCurrentLocale()))+":",1,row);
					InterfaceObject input = getFieldInputObject(element.getName(),null,element.getValueClass());
					//_busy.addDisabledObject(input);
					_fieldTable.add(input,2,row);
				}
			
			}
		} else {

			if(_dynamicFields.size()>0){
				
				Iterator iterator = _dynamicFields.iterator();
				while (iterator.hasNext()) {
					try {
						ClassDescription element = (ClassDescription)iterator.next();
						
						row++;
						_fieldTable.add(getFieldLabel(element.getLocalizedName(iwc.getCurrentLocale()))+":",1,row);
						
						InterfaceObject input = getFieldInputObject(element.getName(),null,element.getClassObject());
						//_busy.addDisabledObject(input);
						_fieldTable.add(input,2,row);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				
			}
			
			
		}
		
		InterfaceObject generateButton = (InterfaceObject)getSubmitButton(iwrb.getLocalizedString("generate_report"," Generate "));
		_fieldTable.add(generateButton,1,++row);
		_fieldTable.mergeCells(1,row,2,row);
		_fieldTable.setColumnAlignment(1,Table.HORIZONTAL_ALIGN_RIGHT);
		
		//generateButton.setOnClick("this.form.submit()");
		_busy.addDisabledObject(generateButton);
		_busy.addBusyObject(generateButton);
	 	_busy.setBusyBarUrl(coreBundle.getImage("loading.gif").getURL());
		_fieldTable.add(_busy,1,++row);
		_fieldTable.mergeCells(1,row,2,row);
		_fieldTable.setColumnAlignment(1,Table.HORIZONTAL_ALIGN_RIGHT);

		
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
	
	private String getParameterName(String key){
		return PRIFIX_PRM+key;
	}
	
	private Object getParameterObject(IWContext iwc,String prmValue, Class prmClassType) throws ParseException{
		if(prmClassType.equals(Integer.class)) {
			return Integer.decode(prmValue);
		} else if(prmClassType.equals(Time.class)){
			DateFormat df = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT,iwc.getCurrentLocale()); 
			java.util.Date current = df.parse(prmValue);
			return new Time(current.getTime());
		} else if(prmClassType.equals(Date.class)) {
			DateFormat df = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT,iwc.getCurrentLocale()); 
			java.util.Date current = df.parse(prmValue);
			return new Date(current.getTime());
		} else if(prmClassType.equals(Timestamp.class)) {
			DateFormat df = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT,SimpleDateFormat.SHORT,iwc.getCurrentLocale()); 
			java.util.Date current = df.parse(prmValue);
			return new Timestamp(current.getTime());
		} 
		//else {
		return prmValue;
		//}	
		
	}
	
	private InterfaceObject getFieldInputObject(String key, String value, Class dataType) {
			
//		if(dataType == Integer.class){
//			IntegerInput fieldInput = new IntegerInput(getParameterName(key));
//			setStyle(fieldInput);
//			return fieldInput;
//		}else if(dataType == Time.class){
//			TimeInput fieldInput = new TimeInput(getParameterName(key));
//			setStyle(fieldInput);
//			return fieldInput;
//		}else if(dataType == Date.class){
//			DateInput fieldInput = new DateInput(getParameterName(key));
//			setStyle(fieldInput);
//			return fieldInput;
//		}else if(dataType == Timestamp.class){
//			TimestampInput fieldInput = new TimestampInput(getParameterName(key));
//			setStyle(fieldInput);
//			return fieldInput;
//		}else{
			TextInput fieldInput = new TextInput(getParameterName(key));
			setStyle(fieldInput);
			return fieldInput;
//		}
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
	
	
	public synchronized Object clone(){
		ReportGenerator clone = (ReportGenerator)super.clone();
		
		clone._allFields =null;
		clone._dynamicFields = new Vector();
		clone._dataSource = null;
		clone._design = null;
		clone._reportFilePath = null;
		clone._queryParser = null;
		clone._fieldTable = null;
		
		return clone;
	}
	
	public void setReportName(String name){
		if(name != null && !"".equals(name)){
			_canChangeReportName = false;
			_reportName = name;
		}
	}

}
