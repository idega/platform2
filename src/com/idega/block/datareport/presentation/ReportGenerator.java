/*
 * Created on 15.7.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.presentation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import javax.ejb.FinderException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.design.JasperDesign;
import com.idega.block.dataquery.business.QueryService;
import com.idega.block.dataquery.data.Query;
import com.idega.block.dataquery.data.QueryHome;
import com.idega.block.dataquery.data.xml.QueryConditionPart;
import com.idega.block.dataquery.data.xml.QueryFieldPart;
import com.idega.block.dataquery.data.xml.QueryHelper;
import com.idega.block.datareport.business.DynamicReportDesign;
import com.idega.block.datareport.business.JasperReportBusiness;
import com.idega.block.datareport.data.MethodInvocationXMLFile;
import com.idega.block.datareport.data.MethodInvocationXMLFileHome;
import com.idega.block.datareport.util.ReportDescription;
import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.datareport.util.ReportableField;
import com.idega.block.datareport.xml.methodinvocation.ClassDescription;
import com.idega.block.datareport.xml.methodinvocation.ClassHandler;
import com.idega.block.datareport.xml.methodinvocation.MethodDescription;
import com.idega.block.datareport.xml.methodinvocation.MethodInput;
import com.idega.block.datareport.xml.methodinvocation.MethodInvocationDocument;
import com.idega.block.datareport.xml.methodinvocation.MethodInvocationParser;
import com.idega.business.HiddenInputHandler;
import com.idega.business.IBOLookup;
import com.idega.business.InputHandler;
import com.idega.core.file.data.ICFile;
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
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;
import com.idega.util.reflect.MethodFinder;
import com.idega.xml.XMLException;

/**
 * Title: ReportGenerator Description: Copyright: Copyright (c) 2003 Company:
 * idega Software
 * 
 * @author 2003 - idega team -<br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version 1.0
 */
public class ReportGenerator extends Block {

	private static final String HTML_FORMAT = "html";
	private static final String PDF_FORMAT = "pdf";
	private static final String EXCEL_FORMAT = "excel";
	private static final String SIMPLE_EXCEL_FORMAT = "simple_excel";
	public final static String STYLE = "font-family:arial; font-size:8pt; color:#000000; text-align: justify; border: 1 solid #000000;";
	public final static String STYLE_2 = "font-family:arial; font-size:8pt; color:#000000; text-align: justify;";
	public final static String PRIFIX_PRM = "dr_";
	private static final String PRM_STATE = "dr_gen_state";
	private static final String VALUE_STATE_GENERATE_REPORT = "2";
	private static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.datareport";

	private Integer _queryPK = null;
	private Integer _methodInvocationPK = null;
	private Integer _layoutICFilePK = null;
	private String _layoutFileName = null;
	private IWBundle _layoutIWBundle = null;
	private String _methodInvocationFileName = null;
	private IWBundle _methodInvocationIWBundle = null;
	
	private MethodInvocationDocument _methodInvokeDoc = null;
	private Vector _dynamicFields = new Vector();
	private Map _reportFilePathsMap = null;
	private QueryHelper _queryParser = null;
	private JRDataSource _dataSource = null;
	private Table _fieldTable = null;
	private JasperDesign _design = null;
	private ReportDescription _reportDescription = null;//new ReportDescription();
	private BusyBar _busy = null;
	
	private List maintainParameterList = new Vector();

	private String _prmLablePrefix = "label_";

	private String _reportName = "Generated Report";
	private boolean _canChangeReportName = true;
	private boolean _showReportNameInputIfCannotChangeIt = false;
	private String PRM_REPORT_NAME = "report_name";

	private boolean _generateExcelReport=true;
	private boolean _generateHTMLReport=true;
	private boolean _generatePDFReport=true;
	private boolean _generateSimpleExcelReport=true;

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

	private void parseQuery(IWContext iwc) throws XMLException, Exception {
		if (_queryParser == null) {
			Query query = ((QueryHome)IDOLookup.getHome(Query.class)).findByPrimaryKey(_queryPK);
			_queryParser = new QueryHelper(query, iwc);
		}

		List allFields = _queryParser.getListOfFields();
		if (allFields != null) {
			_reportDescription.addFields(allFields);
			//System.out.println("ReportGenerator#parseQuery() - _queryParser.getListOfFields().size() = " + allFields.size());
		}
		else {
			//System.out.println("ReportGenerator#parseQuery() - _queryParser.getListOfFields() == null");
		}

		Collection conditionsCollection = _queryParser.getListOfConditions();
		if (conditionsCollection != null) {
			Iterator iter = conditionsCollection.iterator();
			while (iter.hasNext()) {
				QueryConditionPart element = (QueryConditionPart) iter.next();
				if (element.isDynamic()) {
					_dynamicFields.add(new ReportableField(element.getIDOEntityField()));
				}
			}
		}

		//_dynamicFields
	}
	
	public void setParameterToMaintain(String param){
		maintainParameterList.add(param);
	}
	
	public void setParametersToMaintain(List paramList){
		maintainParameterList.addAll(paramList);
	}
	
	private int calculateTextFieldWidthForString(String str) {
		int fontSize = 9;
		return (int) (5 + (str.length() * fontSize * 0.58));
	}

	private void getLayoutFromICFileOrGenerate(IWContext iwc) throws IOException, JRException {
		boolean isMethodInvocation = false;
		String tmpName = iwc.getParameter(getParameterName(PRM_REPORT_NAME));
		if (tmpName != null) {
			_reportName = tmpName;
		}
		if (_queryPK == null && (_methodInvocationPK!= null || _methodInvocationFileName!= null) ) {
			isMethodInvocation = true;
			if (_dataSource != null && _dataSource instanceof ReportableCollection) {
				_reportDescription = ((ReportableCollection) _dataSource).getReportDescription();
			}
		}

		//Fetch or generate the layout

		//fetch, only available for method invocation, TODO make available for other types
		if ( ((_layoutFileName!=null) || (_layoutICFilePK != null)) && isMethodInvocation) {
			getLayoutAndAddParameters(iwc);
		}
		else { //generate
			generateLayoutAndAddParameters(iwc, isMethodInvocation);
		}

	}

	private void prepareForLayoutGeneration(IWContext iwc, boolean isMethodInvocation) throws IOException, JRException {
		int prmLableWidth = 95;
		int prmValueWidth = 55;
		
		_reportDescription.setLocale(iwc.getCurrentLocale());

		if (_dynamicFields != null && _dynamicFields.size() > 0) {
			if (_queryPK != null) {
				Iterator iter = _dynamicFields.iterator();
				while (iter.hasNext()) {
					ReportableField element = (ReportableField) iter.next();
					String prmName = element.getName();
					String tmpPrmLabel = (String) _reportDescription.get(_prmLablePrefix + prmName);
					String tmpPrmValue = (String) _reportDescription.get(prmName);
					int tmpPrmLabelWidth = (tmpPrmLabel != null) ? calculateTextFieldWidthForString(tmpPrmLabel)
							: prmLableWidth;
					int tmpPrmValueWidth = (tmpPrmValue != null) ? calculateTextFieldWidthForString(tmpPrmValue)
							: prmValueWidth;
					_reportDescription.addHeaderParameter(_prmLablePrefix + prmName, tmpPrmLabelWidth, prmName,
							String.class, tmpPrmValueWidth);
				}
			}
			else {
				Iterator iter = _dynamicFields.iterator();
				while (iter.hasNext()) {
					ClassDescription element = (ClassDescription) iter.next();
					String prmName = element.getName();
					String tmpPrmLabel = (String) _reportDescription.get(_prmLablePrefix + prmName);
					String tmpPrmValue = (String) _reportDescription.get(prmName);
					if (tmpPrmLabel != null && tmpPrmValue != null) {
						int tmpPrmLabelWidth = (tmpPrmLabel != null) ? calculateTextFieldWidthForString(tmpPrmLabel)
								: prmLableWidth;
						int tmpPrmValueWidth = (tmpPrmValue != null) ? calculateTextFieldWidthForString(tmpPrmValue)
								: prmValueWidth;
						_reportDescription.addHeaderParameter(_prmLablePrefix + prmName, tmpPrmLabelWidth, prmName,
								String.class, tmpPrmValueWidth);
					}
				}
			}
		}
	}

	
	private void generateLayoutAndAddParameters(IWContext iwc, boolean isMethodInvocation) throws IOException,
			JRException {
		
		prepareForLayoutGeneration( iwc, isMethodInvocation);
		
		int columnWidth = 120;
		
		DynamicReportDesign designTemplate = new DynamicReportDesign("GeneratedReport");
		
		
		List keys = _reportDescription.getListOfHeaderParameterKeys();
		List labels = _reportDescription.getListOfHeaderParameterLabelKeys();
		Iterator keyIter = keys.iterator();
		Iterator labelIter = labels.iterator();
		while (keyIter.hasNext() && labelIter.hasNext()) {
			String key = (String) keyIter.next();
			String label = (String) labelIter.next();
			designTemplate.addHeaderParameter(label,_reportDescription.getWithOfParameterOrLabel(label),key,_reportDescription.getParameterClassType(key),_reportDescription.getWithOfParameterOrLabel(key));
		}
		
		List allFields = _reportDescription.getListOfFields();
		if (allFields != null && allFields.size() > 0) {
			////System.out.println("ReportGenerator.");
			//TODO thi: solve problem with the width of columns avoiding
			// merging of vertical cells in excel outputs
			// stretch with overflow merges two vertical cells, excel file can't
			// be sorted
			// see also and fix also JasperReportBusiness
			
			// ------------------------------------------------------------------------------------------------------------------------------------------------------------------
			//TMP
			//TODO get columnspacing (15) and it to columnsWidth;
			int numberOfFields = _reportDescription.getNumberOfFields();
			int columnsWidth = columnWidth * numberOfFields + 15 * (numberOfFields - 1);
			//TMP
			//TODO get page Margins (20) and add them to pageWidth;
			// does the width fit the page width?
			if (columnsWidth > DynamicReportDesign.PAGE_WIDTH_WITHOUT_MARGINS_PORTRAIT_A4) {
				// change to landscape!
				designTemplate.setOrientationLandscape();
				// does the the width now fit the page width?
				int landscapeWidth = (columnsWidth > DynamicReportDesign.PAGE_WIDTH_WITHOUT_MARGINS_LANDSCAPE_A4) ? columnsWidth
						+ DynamicReportDesign.PAGE_LEFT_MARGIN + DynamicReportDesign.PAGE_RIGHT_MARGIN
						: DynamicReportDesign.PAGE_WIDTH_LANDSCAPE_A4;
				designTemplate.setPageWidth(landscapeWidth);
				designTemplate.setPageHeight(DynamicReportDesign.PAGE_HEIGHT_LANDSCAPE_A4);
			}
			// do not change the width of the page!! prior:
			// designTemplate.setPageWidth(columnsWidth + 20 + 20);
			designTemplate.setColumnWidth(columnsWidth);
			//
			Iterator iter = allFields.iterator();
			if (isMethodInvocation) {
				while (iter.hasNext()) {
					ReportableField field = (ReportableField) iter.next();
					String name = field.getName();
					designTemplate.addField(name, field.getValueClass(), columnWidth);
				}
			}
			else {
				while (iter.hasNext()) {
					try {
						QueryFieldPart element = (QueryFieldPart) iter.next();
						ReportableField field = new ReportableField(element.getIDOEntityField());
						String name = field.getName();
						designTemplate.addField(name, field.getValueClass(), columnWidth);
					}
					catch (IDOLookupException e) {
						e.printStackTrace();
					}
					catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
		designTemplate.close();
		_design = designTemplate.getJasperDesign(iwc);
	}

	private void getLayoutAndAddParameters(IWContext iwc) throws RemoteException {
		JasperReportBusiness reportBusiness = getReportBusiness();
		if(_layoutICFilePK!=null){
			int designId = _layoutICFilePK.intValue();
			_design = reportBusiness.getDesignBox(designId).getDesign();	 
		}
		else if(_layoutFileName!=null){
			if(_layoutIWBundle!=null){
				_design = reportBusiness.getDesignFromBundle(_layoutIWBundle,_layoutFileName);
			}
			else{
				_design = reportBusiness.getDesignFromBundle(getBundle(iwc),_layoutFileName);
			}
		}
		//add parameters and fields
	}

	private void generateDataSource(IWContext iwc) throws XMLException, Exception {
		Locale currentLocale = iwc.getCurrentLocale();
		if(_queryPK != null) {
			QueryService service = (QueryService)(IBOLookup.getServiceInstance(iwc,QueryService.class));
			_dataSource = service.generateQueryResult(_queryPK, iwc);
		}
		else if (_methodInvokeDoc != null) {
				ReportDescription tmpReportDescriptionForCollectingData = new ReportDescription();
				List mDescs = _methodInvokeDoc.getMethodDescriptions();
				if (mDescs != null) {
					Iterator it = mDescs.iterator();
					if (it.hasNext()) {
						MethodDescription mDesc = (MethodDescription) it.next();

						ClassDescription mainClassDesc = mDesc.getClassDescription();
						Class mainClass = mainClassDesc.getClassObject();
						String type = mainClassDesc.getType();
						String methodName = mDesc.getName();

						MethodInput input = mDesc.getInput();
						List parameters = null;
						if (input != null) {
							parameters = input.getClassDescriptions();
						}

						Object[] prmVal = null;
						Class[] paramTypes = null;

						if (parameters != null) {
							prmVal = new Object[parameters.size()];
							paramTypes = new Class[parameters.size()];
							ListIterator iterator = parameters.listIterator();
							while (iterator.hasNext()) {
								int index = iterator.nextIndex();
								ClassDescription clDesc = (ClassDescription) iterator.next();
								Class prmClassType = clDesc.getClassObject();
								paramTypes[index] = prmClassType;
								String[] prmValues = iwc.getParameterValues(getParameterName(clDesc.getName()));
								String prm = null;
								Object obj = null;

								if (prmValues != null && prmValues.length > 0) {
									prm = prmValues[0];
								}

								ClassHandler cHandler = clDesc.getClassHandler();
								InputHandler iHandler = null;
								boolean isHidden = false;
								if (cHandler != null) {
									iHandler = cHandler.getHandler();
									isHidden = iHandler instanceof HiddenInputHandler;
								}

								if (iHandler != null) {
									obj = iHandler.getResultingObject(prmValues, iwc);
									String displayNameOfValue = iHandler.getDisplayForResultingObject(obj, iwc);
									if (displayNameOfValue != null) {
										tmpReportDescriptionForCollectingData.put(clDesc.getName(), displayNameOfValue);
									}
									if (isHidden) {
										tmpReportDescriptionForCollectingData.remove(clDesc.getName());
									}
								}
								else {
									//ONLY HANDLES ONE VALUE!
									obj = getParameterObject(iwc, prm, prmClassType);
									if (!isHidden) {
										tmpReportDescriptionForCollectingData.put(clDesc.getName(), prm);
									}
								}

								if (!isHidden) {
									tmpReportDescriptionForCollectingData.put(_prmLablePrefix + clDesc.getName(), clDesc.getLocalizedName(currentLocale) + ":");
								}
								else {
									tmpReportDescriptionForCollectingData.remove(_prmLablePrefix + clDesc.getName());
								}

								prmVal[index] = obj;
							}
						}
						
						Object forInvocationOfMethod = null;
						if (ClassDescription.VALUE_TYPE_IDO_SESSION_BEAN.equals(type)) {
							forInvocationOfMethod = IBOLookup.getSessionInstance(iwc, mainClass);
						}
						else
							if (ClassDescription.VALUE_TYPE_IDO_SERVICE_BEAN.equals(type)) {
								forInvocationOfMethod = IBOLookup.getServiceInstance(iwc, mainClass);
							}
							else
								if (ClassDescription.VALUE_TYPE_IDO_ENTITY_HOME.equals(type)) {
									forInvocationOfMethod = IDOLookup.getHome(mainClass);

								}
								else { //ClassDescription.VALUE_TYPE_CLASS.equals(type))
									forInvocationOfMethod = mainClass.newInstance();
								}

						MethodFinder mf = MethodFinder.getInstance();

						Method method = mf.getMethodWithNameAndParameters(mainClass, methodName, paramTypes);

						try {
							_dataSource = (JRDataSource) method.invoke(forInvocationOfMethod, prmVal);
						}
						catch (InvocationTargetException e) {
							Throwable someException = e.getTargetException();
							if (someException != null && someException instanceof Exception) {
								throw (Exception) someException;
							}
							else {
								throw e;
							}

						}

						if (_dataSource != null && _dataSource instanceof ReportableCollection) {
							_reportDescription = ((ReportableCollection) _dataSource).getReportDescription();
							_reportDescription.merge(tmpReportDescriptionForCollectingData);
						} else {
							_reportDescription = tmpReportDescriptionForCollectingData;
						}
						_reportDescription.setLocale(iwc.getCurrentLocale());
					}
				}

			}

	}

	private void generateReport() throws RemoteException, JRException {
		JasperReportBusiness business = getReportBusiness();
		if(_dataSource != null){
			if (doGenerateSomeJasperReport() && (_dataSource != null && _design != null)) {				
				_reportDescription.put(DynamicReportDesign.PRM_REPORT_NAME, _reportName);
				JasperPrint print = business.getReport(_dataSource, _reportDescription.getDisplayValueMap(), _design);
	
				if (_reportFilePathsMap == null) {
					_reportFilePathsMap = new HashMap();
				}
				
				if(_generatePDFReport){
					_reportFilePathsMap.put(PDF_FORMAT, business.getPdfReport(print, "report"));
				}
				
				if(_generateExcelReport){
					_reportFilePathsMap.put(EXCEL_FORMAT, business.getExcelReport(print, "report"));
				}
				
				if(_generateHTMLReport){
					_reportFilePathsMap.put(HTML_FORMAT, business.getHtmlReport(print, "report"));
				}
	
			}
			
			if(_generateSimpleExcelReport && (_dataSource instanceof ReportableCollection)){
				if (_reportFilePathsMap == null) {
					_reportFilePathsMap = new HashMap();
				}
				_reportFilePathsMap.put(SIMPLE_EXCEL_FORMAT, business.getSimpleExcelReport(((ReportableCollection) _dataSource).getJRDataSource(), _reportName,_reportDescription));
			}
		}
	}

	/**
	 * @return
	 */
	private boolean doGenerateSomeJasperReport() {
		return (_generateExcelReport || _generateHTMLReport || _generatePDFReport);
	}

	public JasperReportBusiness getReportBusiness() {
		try {
			return (JasperReportBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), JasperReportBusiness.class);
		}
		catch (RemoteException ex) {
			System.err.println("[ReportLayoutChooser]: Can't retrieve JasperReportBusiness. Message is: " + ex.getMessage());
			throw new RuntimeException("[ReportLayoutChooser]: Can't retrieve ReportBusiness");
		}
	}

	public void setQuery(Integer queryPK) {
		_queryPK = queryPK;
	}

	public void setMethodInvocationICFileID(Integer methodInvocationPK) {
		_methodInvocationPK = methodInvocationPK;
	}

	public void setMethodInvocation(Integer methodInvocationPK) {
		setMethodInvocationICFileID(methodInvocationPK);
	}

	public void setMethodInvocationICFile(ICFile file) {
		if (file != null)
			setMethodInvocationICFileID((Integer) file.getPrimaryKey());
	}

	public void setMethodInvocationBundleAndFileName(IWBundle bundle, String fileName){
		_methodInvocationFileName = fileName;
		_methodInvocationIWBundle = bundle;
	}
	
	public void setMethodInvocationFileNameAndUseDefaultBundle(String fileName){
		setMethodInvocationBundleAndFileName(null,fileName);
	}
	
	public void setLayoutICFile(ICFile file) {
		if (file != null)
			_layoutICFilePK = (Integer) file.getPrimaryKey();
	}
	
	public void setLayoutBundleAndFileName(IWBundle bundle, String fileName){
		_layoutFileName = fileName;
		_layoutIWBundle = bundle;
	}
	
	public void setLayoutFileNameAndUseDefaultBundle(String fileName){
		setLayoutBundleAndFileName(null,fileName);
	}
	
	public void setLayoutICFileID(Integer layoutICFilePK) {
		_layoutICFilePK = layoutICFilePK;
	}

	public void main(IWContext iwc) throws Exception {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		if (!iwc.isParameterSet(PRM_REPORT_NAME)) {
			_reportName = iwrb.getLocalizedString(PRM_REPORT_NAME, _reportName);
		}
		
		try {
			if (_queryPK != null) {
				String genState = iwc.getParameter(PRM_STATE);
				if (genState == null || "".equals(genState)) {
					parseQuery(iwc);
					lineUpElements(iwrb, iwc);
					Form submForm = new Form();
					submForm.maintainParameters(maintainParameterList);
					submForm.add(_fieldTable);
					this.add(submForm);
				}
				else {
					parseQuery(iwc);
					generateDataSource(iwc);
					getLayoutFromICFileOrGenerate(iwc);
					generateReport();
					this.add(getReportLink(iwc));
				}
			}
			else
				if ((_methodInvocationPK != null) || (_methodInvocationFileName != null)) {
					String genState = iwc.getParameter(PRM_STATE);
					if (genState == null || "".equals(genState)) {
						parseMethodInvocationXML(iwc,iwrb);
						lineUpElements(iwrb, iwc);
						Form submForm = new Form();
						submForm.maintainParameters(maintainParameterList);
						submForm.add(_fieldTable);
						this.add(submForm);
					}
					else {
						//System.out.println("\n[ReportGenerator]: starts generating...");
						//System.out.println("[ReportGenerator]: parsing xml...");
						//long time1 = System.currentTimeMillis();
						//long lastTime = time1;
						parseMethodInvocationXML(iwc,iwrb);
						//long time2 = System.currentTimeMillis();
						//System.out.println("[ReportGenerator]: took " + (time2 - lastTime) + "ms, total of " + (time2 - time1) + "ms");
						//lastTime = time2;
						//System.out.println("[ReportGenerator]: generating datasource...");
						generateDataSource(iwc);
						//long time3 = System.currentTimeMillis();
						//System.out.println("[ReportGenerator]: took " + (time3 - lastTime) + "ms, total of " + (time3 - time1) + "ms");
						//lastTime = time3;
						if(doGenerateSomeJasperReport()){
							//System.out.println("[ReportGenerator]: generating layout...");
							getLayoutFromICFileOrGenerate(iwc);
							//long time4 = System.currentTimeMillis();
							//System.out.println("[ReportGenerator]: took " + (time4 - lastTime) + "ms, total of " + (time4 - time1) + "ms");
							//lastTime = time4;
						} else {
							//System.out.println("[ReportGenerator]: prepareForLayoutGeneration()...");
							prepareForLayoutGeneration(iwc,true);
							//long time4 = System.currentTimeMillis();
							//System.out.println("[ReportGenerator]: took " + (time4 - lastTime) + "ms, total of " + (time4 - time1) + "ms");
							//lastTime = time4;
						}
						//System.out.println("[ReportGenerator]: generating report...");
						generateReport();
						//long time5 = System.currentTimeMillis();
						//System.out.println("[ReportGenerator]: took " + (time5 - lastTime) + "ms, total of " + (time5 - time1) + "ms");
						//lastTime = time5;
						//System.out.println("[ReportGenerator]: getting link to the report");
						this.add(getReportLink(iwc));
						//long time6 = System.currentTimeMillis();
						//System.out.println("[ReportGenerator]: took " + (time6 - lastTime) + "ms, total of " + (time6 - time1) + "ms");
						//System.out.println("[ReportGenerator]: ...finished\n");

					}
				}
				else
					if (hasEditPermission()) {
						add(iwrb.getLocalizedString("no_query_has_been_chosen_for_this_instance", "No query has been chosen for this instance"));
					} //else{//Do nothing}

		}catch (OutOfMemoryError e){
			add(iwrb.getLocalizedString("datareport.out_of_memory", "The server was not able to finish your request. Try to be more specific in your request or partition it so the result will be smaller."));
			add(Text.getBreak());
			add(Text.getBreak());
			BackButton back = new BackButton();
			setStyle(back);
			add(back);
			e.printStackTrace();

		}		
		catch (ReportGeneratorException e) {
			add(e.getLocalizedMessage());
			add(Text.getBreak());
			add(Text.getBreak());
			BackButton back = new BackButton();
			setStyle(back);
			add(back);

			//TMP
			Throwable cause = e.getCause();
			if (cause != null) {
				cause.printStackTrace();
			}
			else {
				e.printStackTrace();
			}

			//			if(false){ // if is developer
			//				add(Text.getBreak());
			//				add(Text.getBreak());
			//				Throwable cause = e.getCause();
			//				if(cause != null){
			//					cause.printStackTrace();
			//					add(stackTrace);
			//				}
			//			}

		}
	}

	/**
	 *  
	 */
	private void parseMethodInvocationXML(IWContext iwc,IWResourceBundle iwrb) throws IDOLookupException, ReportGeneratorException {
		MethodInvocationXMLFile file = null;
		InputStream fileStream = null;
			
		if(_methodInvocationPK!=null){
			try {
				file =
					(MethodInvocationXMLFile) ((MethodInvocationXMLFileHome) IDOLookup.getHome(MethodInvocationXMLFile.class)).findByPrimaryKey(
						_methodInvocationPK);
				
				fileStream = file.getFileValue();
			}
			catch (FinderException e) {
				throw new ReportGeneratorException(
					iwrb.getLocalizedString("report_transcription_not_found", "The report transcription was not found"),
					e);
				//e.printStackTrace();
			}
		}
		else if(_methodInvocationFileName!=null){
			if( _methodInvocationIWBundle==null){
				_methodInvocationIWBundle = getBundle(iwc);
			}
			
			try {
				fileStream = new FileInputStream(_methodInvocationIWBundle.getRealPathWithFileNameString(_methodInvocationFileName));
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		
		if (fileStream != null) {
			try {
				_methodInvokeDoc = (MethodInvocationDocument) new MethodInvocationParser().parse(fileStream);
			}
			catch (XMLException e1) {
				throw new ReportGeneratorException(
					iwrb.getLocalizedString(
						"error_while_parsing_transcription",
						"Error occured when trying to read the report generation transcription file"),
					e1);
			}
		}
		

		if (_methodInvokeDoc != null) {
			List methods = _methodInvokeDoc.getMethodDescriptions();
			if (methods != null) {
				Iterator iter = methods.iterator();
				if (iter.hasNext()) {
					MethodDescription mDesc = (MethodDescription) iter.next();

					MethodInput mInput = mDesc.getInput();
					if (mInput != null) {
						_dynamicFields.addAll(mInput.getClassDescriptions());
						//						List classDesc = mInput.getClassDescriptions();
						//						if(classDesc!= null){
						//							Iterator iterator = classDesc.iterator();
						//							while (iterator.hasNext()) {
						//								ClassDescription cDesc =
						// (ClassDescription)iterator.next();
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
	    IWResourceBundle iwrb = getResourceBundle(iwc);
		Table reports = new Table();
		reports.mergeCells(1, 1, 2, 1);
		reports.add(
				getResourceBundle(iwc).getLocalizedString("ReportGenerator.click_on_format", "Select a link for the desired output format."),
				1,
				1);
		
		String formats[] = new String[] {EXCEL_FORMAT,SIMPLE_EXCEL_FORMAT,PDF_FORMAT,HTML_FORMAT};
		String formatNames[] = new String[] {iwrb.getLocalizedString(EXCEL_FORMAT,"Excel"),iwrb.getLocalizedString(SIMPLE_EXCEL_FORMAT,"Excel without template"),iwrb.getLocalizedString(PDF_FORMAT,"PDF"),iwrb.getLocalizedString(HTML_FORMAT,"HTML")};
		
		int j = 1;
		for (int i = 0; i < formats.length; i++) {
			String relativeFilePath = (String) _reportFilePathsMap.get(formats[i]);
			if(relativeFilePath != null){
				j++;
				Link link = new Link(_reportName, relativeFilePath);
				link.setTarget(Link.TARGET_NEW_WINDOW);
//				DownloadLink link = new DownloadLink(_reportName);
//				link.setRelativeFilePath(relativeFilePath);
//				
				reports.add(formatNames[i]+" : ", 1, j);
				reports.add(link, 2, j);
			}
		}
		return reports;
	}

	/**
	 *  
	 */
	private void lineUpElements(IWResourceBundle iwrb, IWContext iwc) {
		IWMainApplication iwma = iwc.getApplicationContext().getIWMainApplication();
		IWBundle coreBundle = iwma.getBundle(IW_CORE_BUNDLE_IDENTIFIER);

		_fieldTable = new Table();
		//_fieldTable.setBorder(1);

		int row = 0;

		if (_canChangeReportName || (!_canChangeReportName && _showReportNameInputIfCannotChangeIt)) {
			row++;
			_fieldTable.add(getFieldLabel(iwrb.getLocalizedString("choose_report_name", "Report name")) + ":", 1, row);
			InterfaceObject nameInput = getFieldInputObject(PRM_REPORT_NAME); //null, String.class);
			nameInput.setDisabled(!_canChangeReportName);
			nameInput.setValue(_reportName);
			_fieldTable.add(nameInput, 2, row);
		}

		//TODO Let Reportable field and ClassDescription impliment the same
		// interface (IDODynamicReportableField) to decrease code duplications
		if (_queryPK != null) {
			if (_dynamicFields.size() > 0) {

				Iterator iterator = _dynamicFields.iterator();

				while (iterator.hasNext()) {
					ReportableField element = (ReportableField) iterator.next();
					row++;
					_fieldTable.add(getFieldLabel(element.getLocalizedName(iwc.getCurrentLocale())) + ":", 1, row);
					InterfaceObject input = getFieldInputObject(element.getName()); //null, element.getValueClass());
					//_busy.addDisabledObject(input);
					_fieldTable.add(input, 2, row);
				}

			}
		}
		else {

			if (_dynamicFields.size() > 0) {

				Iterator iterator = _dynamicFields.iterator();
				while (iterator.hasNext()) {
					try {
						ClassDescription element = (ClassDescription) iterator.next();

						row++;
						_fieldTable.add(getFieldLabel(element.getLocalizedName(iwc.getCurrentLocale())) + ":", 1, row);

						ClassHandler cHandler = element.getClassHandler();
						PresentationObject input = null;
						if (cHandler != null) {
							InputHandler iHandler = cHandler.getHandler();
							input = iHandler.getHandlerObject(getParameterName(element.getName()), cHandler.getValue(), iwc);
							setStyle(input);
						}
						else {
							input = getFieldInputObject(element.getName()); //null, element.getClassObject());
							//_busy.addDisabledObject(input);
						}
						_fieldTable.add(input, 2, row);

					}
					catch (InstantiationException e) {
						e.printStackTrace();
					}
					catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}

			}

		}

		InterfaceObject generateButton = (InterfaceObject) getSubmitButton(iwrb.getLocalizedString("generate_report", " Generate "));
		_fieldTable.add(generateButton, 1, ++row);
		_fieldTable.add(new HiddenInput(PRM_STATE, VALUE_STATE_GENERATE_REPORT), 1, row);
		if (_fieldTable.getRows() > 1) {
			_fieldTable.mergeCells(1, row, 2, row);
		}
		_fieldTable.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_RIGHT);

		_busy.addDisabledObject(generateButton);
		_busy.addBusyObject(generateButton);
		_busy.setBusyBarUrl(coreBundle.getImage("loading.gif").getURL());
		_fieldTable.add(_busy, 1, ++row);
		_fieldTable.mergeCells(1, row, 2, row);
		_fieldTable.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_RIGHT);

	}

	private PresentationObject getSubmitButton(String text) {
		SubmitButton button = new SubmitButton(text, PRM_STATE, VALUE_STATE_GENERATE_REPORT);
		setStyle(button);
		return button;
	}

	private Text getFieldLabel(String text) {
		Text fieldLabel = new Text(text);
		setStyle(fieldLabel);
		return fieldLabel;
	}

	private String getParameterName(String key) {
		return PRIFIX_PRM + key;
	}

	private Object getParameterObject(IWContext iwc, String prmValue, Class prmClassType) throws ReportGeneratorException {
		Locale locale = iwc.getCurrentLocale();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		if (prmValue != null) {
			if (prmClassType.equals(Integer.class)) {
				try {
					return Integer.decode(prmValue);
				}
				catch (NumberFormatException e) {
					throw new ReportGeneratorException(
						"'"
							+ prmValue
							+ "' "
							+ iwrb.getLocalizedString("integer_format_not_right", "is not of the right format, it should be an integer"),
						e);
				}
			}
			else
				if (prmClassType.equals(Time.class)) {
					DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
					try {
						java.util.Date current = df.parse(prmValue);
						return new Time(current.getTime());
					}
					catch (ParseException e) {
						throw new ReportGeneratorException(
							"'"
								+ prmValue
								+ "' "
								+ iwrb.getLocalizedString(
									"time_format_not_right_" + locale.getLanguage() + "_" + locale.getCountry(),
									"is not of the right format, it should be of the format: " + df.format(IWTimestamp.RightNow().getDate())),
							e);
					}
				}
				else
					if (prmClassType.equals(Date.class)) {
						DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
						try {
							java.util.Date current = df.parse(prmValue);
							return new Date(current.getTime());
						}
						catch (ParseException e) {

							throw new ReportGeneratorException(
								"'"
									+ prmValue
									+ "' "
									+ iwrb.getLocalizedString(
										"date_format_not_right_" + locale.getLanguage() + "_" + locale.getCountry(),
										"is not of the right format, it should be of the format: " + df.format(IWTimestamp.RightNow().getDate())),
								e);
						}
					}
					else
						if (prmClassType.equals(Timestamp.class)) {
							DateFormat df =
								DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, iwc.getCurrentLocale());
							try {
								java.util.Date current = df.parse(prmValue);
								return new Timestamp(current.getTime());
							}
							catch (ParseException e) {
								throw new ReportGeneratorException(
									"'"
										+ prmValue
										+ "' "
										+ iwrb.getLocalizedString(
											"timestamp_format_not_right_" + locale.getLanguage() + "_" + locale.getCountry(),
											"is not of the right format, it should be of the format: " + df.format(IWTimestamp.RightNow().getDate())),
									e);
							}
						}
		}
		//else {
		return prmValue;
		//}

	}

	private InterfaceObject getFieldInputObject(String key) { //String value, Class dataType) {

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
		//			TimestampInput fieldInput = new
		// TimestampInput(getParameterName(key));
		//			setStyle(fieldInput);
		//			return fieldInput;
		//		}else{
		TextInput fieldInput = new TextInput(getParameterName(key));
		setStyle(fieldInput);
		return fieldInput;
		//		}
	}

	public void setStyle(PresentationObject obj) {
		if (obj instanceof Text) {
			this.setStyle((Text) obj);
		}
		else {
			obj.setMarkupAttribute("style", STYLE);
		}
	}

	public void setStyle(Text obj) {
		obj.setMarkupAttribute("style", STYLE_2);
	}

	public synchronized Object clone() {
		ReportGenerator clone = (ReportGenerator) super.clone();

		clone._dynamicFields = new Vector();
		clone._dataSource = null;
		clone._design = null;
		clone._reportFilePathsMap = null;
		clone._queryParser = null;
		clone._fieldTable = null;
		clone._reportDescription = null;//new ReportDescription();

		return clone;
	}

	public void setReportName(String name) {
		if (name != null && !"".equals(name)) {
			_canChangeReportName = false;
			_reportName = name;
		}
	}
	
	
	public void setGenerateExcelReport(boolean value){
		_generateExcelReport = value;
	}
	
	public void setGenerateHTMLReport(boolean value){
		_generateHTMLReport = value;
	}
	
	public void setGeneratePDFReport(boolean value){
		_generatePDFReport = value;
	}
	
	public void setGenerateSimpleExcelReport(boolean value){
		_generateSimpleExcelReport = value;
	}
	
	

	private class ReportGeneratorException extends Exception {

		//	jdk 1.3 - 1.4 fix
		private Throwable _cause = this;

		private String _localizedMessage = null;
		//		
		//		private String _localizedKey = null;
		//		private String _defaultUserFriendlyMessage = null;

		public ReportGeneratorException(String tecnicalMessage, Throwable cause, String localizedMessage) {
			this(tecnicalMessage, cause);
			_localizedMessage = localizedMessage;
			//			_localizedKey = localizedKey;
			//			_defaultUserFriendlyMessage = defaultUserFriendlyMessage;
		}

		/**
		 * @param message
		 * @param cause
		 */
		public ReportGeneratorException(String message, Throwable cause) {
			super(message);
			_localizedMessage = message;
			// jdk 1.3 - 1.4 fix
			_cause = cause;
		}

		/**
		 *  
		 */
		private ReportGeneratorException() {
			super();
		}
		/**
		 * @param message
		 */
		private ReportGeneratorException(String message) {
			super(message);
		}

		/**
		 * @param cause
		 */
		private ReportGeneratorException(Throwable cause) {
			super();
			// jdk 1.3 - 1.4 fix
			_cause = cause;

		}

		//	jdk 1.3 - 1.4 fix
		public Throwable getCause() {
			return _cause;
		}

		//		public String getLocalizedMessageKey(){
		//			return _localizedKey;
		//		}
		//		
		//		public String getDefaultLocalizedMessage(){
		//			return _defaultUserFriendlyMessage;
		//		}

		public String getLocalizedMessage() {
			return _localizedMessage;
		}

	}

}
