package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.FinderException;

import com.idega.block.entity.business.EntityToPresentationObjectConverter;
import com.idega.block.entity.data.EntityPath;
import com.idega.block.entity.data.EntityPathValueContainer;
import com.idega.block.entity.presentation.EntityBrowser;
import com.idega.block.entity.presentation.converter.CheckBoxConverter;
import com.idega.block.entity.presentation.converter.ConverterConstants;
import com.idega.block.entity.presentation.converter.editable.CheckBoxAsLinkConverter;
import com.idega.block.entity.presentation.converter.editable.DropDownMenuConverter;
import com.idega.block.entity.presentation.converter.editable.EditOkayButtonConverter;
import com.idega.block.entity.presentation.converter.editable.OptionProvider;
import com.idega.block.entity.presentation.converter.editable.TextEditorConverter;
import com.idega.data.EntityRepresentation;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.PrintButton;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;

/**
 * <p>Description: A viewer/editor for many workreports at a time</p>
 * <p>Copyright: Idega SoftwareCopyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="eiki@idega.is">Eirikur Hrafnsson</a>
 * @version 1.0
 */

public class WorkReportMultiEditor extends Block {
	 
	protected WorkReportBusiness reportBiz;
	
  private static final String SUBMIT_SAVE_NEW_ENTRY_KEY = "submit_sv_new_entry_key";
  private static final String SUBMIT_CANCEL_KEY = "submit_cancel_key";

  private static final Integer NEW_ENTRY_ID_VALUE = new Integer(-1);
  private static final String NO_LEAGUE_VALUE = "no_league_value";
  
  private static final String ACTION_SHOW_NEW_ENTRY = "action_show_new_entry";
  
  private static final String CHECK_BOX = "checkBox";



  private static final String GROUP_NUMBER = WorkReport.class.getName()+".GROUP_NUMBER";
	private static final String GROUP_NAME = WorkReport.class.getName()+".GROUP_NAME";
  private static final String GROUP_TYPE = WorkReport.class.getName()+".GROUP_TYPE";
	private static final String INACTIVE = WorkReport.class.getName()+".INACTIVE";
  private static final String REGIONAL_UNION_NUMBER = WorkReport.class.getName()+".REG_UNI_NR";
  private static final String REGIONAL_UNION_ABBR = WorkReport.class.getName()+".REG_UNI_ABBR";
	private static final String REPORT_STATUS = "STATUS";
	private static final String MEMBER_COUNT = WorkReport.class.getName()+".TOTAL_MEMBERS";
	private static final String PLAYER_COUNT = WorkReport.class.getName()+".TOTAL_PLAYERS";

	private static final String  WHATS_LEFT = "workreportmultieditor.WHATSLEFT";
	private static final String  CONTINUANCE_TILL = WorkReport.class.getName()+".CONTINUANCE_TILL";
  
  private boolean editable = true;
  
  int year = -1;
   
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";


	public String getBundleIdentifier(){
		return IW_BUNDLE_IDENTIFIER;
	}
	
  public WorkReportMultiEditor() {
    super();
  }  
  
  
  public void main(IWContext iwc) throws Exception {
   
    IWResourceBundle iwrb = getResourceBundle(iwc);
    if(year==-1) {
    	year = (new IWTimestamp(IWTimestamp.getTimestampRightNow()).getYear());
    }
    
    String action = parseAction(iwc);
    Form form = new Form();
    PresentationObject pres = getContent(iwc, iwrb, form, action);
    form.add(pres);
    //HACK!
    form.maintainParameter(WorkReportWindow.ACTION);
	form.maintainParameter(WorkReportConstants.WR_SESSION_PARAM_WORK_REPORT_YEAR);
    add(form);
    add(new PrintButton(iwrb.getLocalizedImageButton("workreportsender.print","print")));
  }
  
  private String parseAction(IWContext iwc) throws RemoteException{
    String action = "";

    // does the user want to modify an existing entity?
    if (iwc.isParameterSet(ConverterConstants.EDIT_ENTITY_SUBMIT_KEY)) {
      WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
      String id = iwc.getParameter(ConverterConstants.EDIT_ENTITY_SUBMIT_KEY);
      Integer primaryKey = null;
      try {
        primaryKey = new Integer(id);
      }
      catch (NumberFormatException ex)  {
        ex.printStackTrace(System.err);
      }
      
      WorkReport report = workReportBusiness.getWorkReportById(primaryKey.intValue());
      
          
      EntityPathValueContainer entityPathValueContainerFromDropDownMenu = DropDownMenuConverter.getResultByEntityIdAndEntityPathShortKey(primaryKey, REPORT_STATUS, iwc);
        
      if (entityPathValueContainerFromDropDownMenu.isValid()) {
        report.setStatus((String)entityPathValueContainerFromDropDownMenu.getValue());
      }
      
			boolean isChecked = CheckBoxConverter.isEntityCheckedUsingDefaultKey(iwc,primaryKey);
			
			if(isChecked){
				report.setAsInactive();
			}
			else{
				report.setAsActive();
			}
			
			EntityPathValueContainer entityPathValueContainerFromTextEditor = TextEditorConverter.getResultByEntityIdAndEntityPathShortKey(primaryKey, CONTINUANCE_TILL, iwc);
			
			if( entityPathValueContainerFromTextEditor.isValid()) {
				report.setContinuanceTill((String)entityPathValueContainerFromTextEditor.getValue());
			}
        

      report.store();
    }  

    return action;
  }
  
  private PresentationObject getContent(IWContext iwc, IWResourceBundle resourceBundle, Form form, String action) throws RemoteException {
  	  
    Collection reports =  getWorkReportBusiness(iwc).getAllWorkReportsForYear(getYear());
   
    EntityBrowser browser = getEntityBrowser(reports, resourceBundle, form);
    
    // put browser into a table
    Table mainTable = new Table(1,2);
    mainTable.add(browser, 1,1);    
    
    return mainTable;    
  }
  

 
  private EntityBrowser getEntityBrowser(Collection entities, IWResourceBundle resourceBundle, Form form)  {
 
		TextEditorConverter textEditorConverter = new TextEditorConverter(form);
    //textEditorConverter.maintainParameters(this.getParametersToMaintain());
    DropDownMenuConverter reportStatusDropDownMenuConverter = getConverterForReportStatus(resourceBundle, form);
    List params = new ArrayList();
    params.add(WorkReportWindow.ACTION);
    params.add(WorkReportConstants.WR_SESSION_PARAM_WORK_REPORT_YEAR);
    
		reportStatusDropDownMenuConverter.maintainParameters(params);
		GroupMetaDataConverter metaConverter = new GroupMetaDataConverter();
 
    // define if the converters should be editable or not
    textEditorConverter.setEditable(editable);
		reportStatusDropDownMenuConverter.setEditable(editable);
		
		InActiveCheckBoxConverter inActiveConverter = new InActiveCheckBoxConverter();
		inActiveConverter.maintainParameters(params);
        inActiveConverter.setEditable(editable);
		EditOkayButtonConverter okCancelButton = new EditOkayButtonConverter();
		
		okCancelButton.maintainParameters(params);
		textEditorConverter.maintainParameters(params);
		
		
		
    // define path short keys and map corresponding converters
    Object[] columns = {
      "okay", okCancelButton,
			GROUP_NUMBER, new LinkConverter(form),
			GROUP_NAME,new LinkConverter(form),
			GROUP_TYPE, new TextToLocalizedTextConverter(),
			REGIONAL_UNION_NUMBER, new LinkConverter(form),
			REGIONAL_UNION_ABBR, new LinkConverter(form),
			REPORT_STATUS, reportStatusDropDownMenuConverter,
			CONTINUANCE_TILL,textEditorConverter,
			INACTIVE, inActiveConverter,
			WHATS_LEFT,new WhatIsMissingConverter(),
			MEMBER_COUNT,new LinkConverter(form),
			PLAYER_COUNT,new LinkConverter(form),
			"more",new MoreButtonConverter(resourceBundle),
		};
      
      
    EntityBrowser browser = EntityBrowser.getInstanceUsingEventSystemAndExternalForm();
    browser.setWidth(EntityBrowser.HUNDRED_PERCENT);
	browser.setScrollableWithHeightAndWidth(550, 750);
    browser.setCellpadding(3);
    browser.setRowHeight(1,"15");
    browser.setLeadingEntity(WorkReport.class);
    browser.setAcceptUserSettingsShowUserSettingsButton(false,false);
    if( entities!=null && !entities.isEmpty()){
			browser.setDefaultNumberOfRows(entities.size());
    }
    
    for (int i = 0; i < columns.length; i+=2) {
      String column = (String) columns[i];
      EntityToPresentationObjectConverter converter = (EntityToPresentationObjectConverter) columns[i+1];
      browser.setMandatoryColumn(i, column);
      browser.setEntityToPresentationConverter(column, converter);
    }
    
    browser.setEntities("dummy_string", entities);
    browser.setNullValueForNumbers("0");
    return browser;
  }
  
  
	/**
	 * Converter for report status column 
	 */
	private DropDownMenuConverter getConverterForReportStatus(final IWResourceBundle resourceBundle, Form form) {
		DropDownMenuConverter converter = new DropDownMenuConverter(form) {
			protected Object getValue(
				Object entity,
				EntityPath path,
				EntityBrowser browser,
				IWContext iwc)  {
					String statusCode = (String) ((EntityRepresentation) entity).getColumnValue(REPORT_STATUS);
					
					return (statusCode==null) ? WorkReportConstants.WR_STATUS_NOT_DONE: statusCode;
				}
			};        

        
		OptionProvider optionProvider = new OptionProvider() {
      
		Map optionMap = null;
      
		public Map getOptions(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
			if (optionMap == null)  {
				optionMap = new TreeMap();
 			
				optionMap.put(WorkReportConstants.WR_STATUS_NOT_DONE, resourceBundle.getLocalizedString(WorkReportConstants.WR_STATUS_NOT_DONE, WorkReportConstants.WR_STATUS_NOT_DONE));
				optionMap.put(WorkReportConstants.WR_STATUS_SOME_DONE, resourceBundle.getLocalizedString(WorkReportConstants.WR_STATUS_SOME_DONE, WorkReportConstants.WR_STATUS_SOME_DONE));
				optionMap.put(WorkReportConstants.WR_STATUS_CONTINUANCE, resourceBundle.getLocalizedString(WorkReportConstants.WR_STATUS_CONTINUANCE, WorkReportConstants.WR_STATUS_CONTINUANCE));
				optionMap.put(WorkReportConstants.WR_STATUS_AT_REGIONAL_UNION, resourceBundle.getLocalizedString(WorkReportConstants.WR_STATUS_AT_REGIONAL_UNION, WorkReportConstants.WR_STATUS_AT_REGIONAL_UNION));
				optionMap.put(WorkReportConstants.WR_STATUS_INVESTIGATE, resourceBundle.getLocalizedString(WorkReportConstants.WR_STATUS_INVESTIGATE, WorkReportConstants.WR_STATUS_INVESTIGATE));
				optionMap.put(WorkReportConstants.WR_STATUS_COMPETITION_BAN, resourceBundle.getLocalizedString(WorkReportConstants.WR_STATUS_COMPETITION_BAN, WorkReportConstants.WR_STATUS_COMPETITION_BAN));
				optionMap.put(WorkReportConstants.WR_STATUS_SHOULD_BE_BANNED, resourceBundle.getLocalizedString(WorkReportConstants.WR_STATUS_SHOULD_BE_BANNED, WorkReportConstants.WR_STATUS_SHOULD_BE_BANNED));
				optionMap.put(WorkReportConstants.WR_STATUS_NO_REPORT, resourceBundle.getLocalizedString(WorkReportConstants.WR_STATUS_NO_REPORT, WorkReportConstants.WR_STATUS_NO_REPORT));	 
				optionMap.put(WorkReportConstants.WR_STATUS_DONE, resourceBundle.getLocalizedString(WorkReportConstants.WR_STATUS_DONE, WorkReportConstants.WR_STATUS_DONE));
				
			}
				return optionMap;
			}
		};     
		converter.setOptionProvider(optionProvider); 
		//converter.maintainParameters(getParametersToMaintain());
		return converter;
	}  
	
	
	
  

	protected WorkReportBusiness getWorkReportBusiness(IWApplicationContext iwc) {
		if (reportBiz == null) {
			try {
				reportBiz = (WorkReportBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, WorkReportBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return reportBiz;
	}
	
	//TODO Make the year choosable
	public int getYear(){
		return year;
	}
	

	
	class TextToLocalizedTextConverter implements EntityToPresentationObjectConverter {
		
		public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc)	{
			return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
		}
		
		public PresentationObject getPresentationObject(Object value, EntityPath path, EntityBrowser browser, IWContext iwc){
			
			Object obj = path.getValue((EntityRepresentation) value);
			if(obj!=null){
				String valueString = obj.toString();
				Text text = (Text) browser.getDefaultTextProxy().clone();
				text.setText(iwc.getApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc.getCurrentLocale()).getLocalizedString(valueString,valueString));               
				return text;
			}
			else{
				return new Text("");
			}
			
		}
		
	}
	
	class WhatIsMissingConverter implements EntityToPresentationObjectConverter {
		
		public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc)	{
			return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
		}
		
		public PresentationObject getPresentationObject(Object value, EntityPath path, EntityBrowser browser, IWContext iwc){
			
			WorkReport report = (WorkReport)value;
			if(report!=null){
				boolean memberPart = report.isMembersPartDone();
				boolean accountPart = report.isAccountPartDone();
				boolean boardPart = report.isBoardPartDone();

				String memberDone = (memberPart)? "member_done_" : "no_member_";
				String accountDone = (accountPart)? "account_done_" : "no_account_";
				String boardDone = (boardPart)? "board_done" : "no_board";
				
				String localizedkey  = memberDone + accountDone + boardDone;
				
				Text text = (Text) browser.getDefaultTextProxy().clone();
				
				text.setText(iwc.getApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc.getCurrentLocale()).getLocalizedString("workreportmultieditor.whatisleftkey_"+localizedkey , localizedkey));               
				return text;
			}
			else{
				return new Text("");
			}
			
		}
		
	}
	
	
	
	
	class LinkConverter extends TextEditorConverter {
		
		public LinkConverter(Form form){
			super(form);
		}
		
		public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc)	{
			return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
		}
		
		public PresentationObject getPresentationObject(
			Object entity,
			EntityPath path,
			EntityBrowser browser,
			IWContext iwc) {
			Object value = getValue(entity, path, browser, iwc);
			String text = value.toString(); 
   
			Integer id = (Integer) ((EntityRepresentation) entity).getPrimaryKey();
			
			String shortKeyPath = path.getShortKey();
			String uniqueKeyLink = getLinkUniqueKey(id, shortKeyPath);
			
			boolean editEntity = false;
			if (iwc.isParameterSet(ConverterConstants.EDIT_ENTITY_KEY)) {
				String idEditEntity = iwc.getParameter(ConverterConstants.EDIT_ENTITY_KEY);
				Integer primaryKey = null;

				try {
					primaryKey = new Integer(idEditEntity);
					editEntity = id.equals(primaryKey);
				}
				catch (NumberFormatException ex)  {
				}
			}
			iwc.isParameterSet(ConverterConstants.EDIT_ENTITY_KEY);
			// decide to show a link or a text inputfield
			if (editEntity || iwc.isParameterSet(uniqueKeyLink)) {
				return new Text(text);      
			} 
			else {
				// show link
				text = (text.length() == 0) ? "_" : text;  
				if (! editable) {
					return new Text(text);
				}
				Link link = new Link(text);
				link.addParameter(ConverterConstants.EDIT_ENTITY_KEY,id.toString());
				link.maintainParameter(WorkReportWindow.ACTION,iwc);
				link.maintainParameter(WorkReportConstants.WR_SESSION_PARAM_WORK_REPORT_YEAR,iwc);
				

				return link;
			}
      
		}
		
	}
	
	
	class GroupMetaDataConverter implements EntityToPresentationObjectConverter {
		
		public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc){
			return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
		}
		
		public PresentationObject getPresentationObject(Object value, EntityPath path, EntityBrowser browser, IWContext iwc){
			
			WorkReport report = (WorkReport)value;
			Text text = (Text) browser.getDefaultTextProxy().clone();
			text.setText("");
			
			try {
				
				Integer regionalPrimaryKey = report.getRegionalUnionGroupId();
				if(regionalPrimaryKey!=null){
				Group regionalUnion = getWorkReportBusiness(iwc).getGroupBusiness().getGroupByGroupID(regionalPrimaryKey.intValue());
				String metaDataKey = path.getShortKey();
				String metaDataValue = regionalUnion.getMetaData(metaDataKey);

					if(metaDataValue!=null){
						text.setText(metaDataValue);   
					}
				}            
				
			}
			catch (FinderException e) {
				//found nothing
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
			
			return text;
		}
		
	}
	
	class MoreButtonConverter implements EntityToPresentationObjectConverter {
		IWResourceBundle iwrb;
		
			public MoreButtonConverter(IWResourceBundle iwrb) {
				this.iwrb = iwrb;
			}
		
			public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc){
				return new Text("");
			}
		
			public PresentationObject getPresentationObject(Object value, EntityPath path, EntityBrowser browser, IWContext iwc){
			
				WorkReport report = (WorkReport)value;
				Link moreLink = new Link(iwrb.getLocalizedString("workreportmultieditor.more_button","more"));
				moreLink.setAsImageButton(true);
				moreLink.addParameter(WorkReportWindow.ACTION,WorkReportWindow.ACTION_REPORT_OVERVIEW_CLOSE_VIEW);
				moreLink.addParameter(WorkReportOverViewCloseView.CLOSE_VIEW_WORK_REPORT_ID,report.getPrimaryKey().toString());
				moreLink.maintainParameter(WorkReportConstants.WR_SESSION_PARAM_WORK_REPORT_YEAR,iwc);
				return moreLink;
			}
		
		}
	
	
	/** 
	 * CheckBoxConverterHelper:
	 * Inner class.
	 */
  
	class InActiveCheckBoxConverter extends CheckBoxAsLinkConverter {
    
      protected boolean shouldEntityBeChecked(Object entity, Integer primaryKey) {
        WorkReport report = (WorkReport) entity;
        return  report.isInActive();
      }
  }
    

	
	/**
	 * @param year
	 */
	public void setYear(int year) {
		this.year = year;
	}

}
