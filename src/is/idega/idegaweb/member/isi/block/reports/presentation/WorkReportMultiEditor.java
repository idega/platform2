package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportDivisionBoard;
import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;
import is.idega.idegaweb.member.util.IWMemberConstants;

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
import com.idega.block.entity.presentation.converters.CheckBoxConverter;
import com.idega.block.entity.presentation.converters.ConverterConstants;
import com.idega.block.entity.presentation.converters.DropDownMenuConverter;
import com.idega.block.entity.presentation.converters.EditOkayButtonConverter;
import com.idega.block.entity.presentation.converters.OptionProvider;
import com.idega.block.entity.presentation.converters.TextEditorConverter;
import com.idega.data.EntityRepresentation;
import com.idega.data.GenericEntity;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
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
  private static final String GROUP_TYPE = WorkReport.class.getName()+".GROUP_TYPE";
	private static final String INACTIVE = WorkReport.class.getName()+".INACTIVE";
  private static final String REGIONAL_UNION_NUMBER = IWMemberConstants.META_DATA_CLUB_NUMBER;
  private static final String REGIONAL_UNION_ABBR = IWMemberConstants.META_DATA_CLUB_ABRV;
	private static final String REPORT_STATUS = "STATUS";
	private static final String MEMBER_COUNT = WorkReport.class.getName()+".TOTAL_MEMBERS";
	private static final String PLAYER_COUNT = WorkReport.class.getName()+".TOTAL_PLAYERS";

  
  private boolean editable = true;
    
  public WorkReportMultiEditor() {
    super();
  }  
  
  
  public void main(IWContext iwc) throws Exception {
    super.main(iwc);
    IWResourceBundle iwrb = getResourceBundle(iwc);

    String action = parseAction(iwc);
    Form form = new Form();
    PresentationObject pres = getContent(iwc, iwrb, form, action);
    form.add(pres);
    //HACK!
    form.maintainParameter(WorkReportWindow.ACTION);
    add(form);
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
        System.err.println("[WorkReportBoardMemberEditor] Wrong primary key. Message is: " +
          ex.getMessage());
        ex.printStackTrace(System.err);
      }
      
      WorkReport report = workReportBusiness.getWorkReportById(primaryKey.intValue());
      
          
      EntityPathValueContainer entityPathValueContainerFromDropDownMenu = DropDownMenuConverter.getResultByEntityIdAndEntityPathShortKey(primaryKey, REPORT_STATUS, iwc);
        
      if (entityPathValueContainerFromDropDownMenu.isValid()) {
        report.setStatus((String)entityPathValueContainerFromDropDownMenu.getValue());
      }
      
			boolean isChecked = CheckBoxConverter.getResultByParsingUsingDefaultKey(iwc).contains(primaryKey);
			
			if(isChecked){
				report.setAsInactive();
			}
			else{
				report.setAsActive();
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
    
		reportStatusDropDownMenuConverter.maintainParameters(params);
		GroupMetaDataConverter metaConverter = new GroupMetaDataConverter();
 
    // define if the converters should be editable or not
    textEditorConverter.setEditable(editable);
		reportStatusDropDownMenuConverter.setEditable(editable);
		
		InActiveCheckBoxConverter inActiveConverter = new InActiveCheckBoxConverter();
		inActiveConverter.maintainParameters(params);
		EditOkayButtonConverter okCancelButton = new EditOkayButtonConverter();
		
		okCancelButton.maintainParameters(params);
		
    // define path short keys and map corresponding converters
    Object[] columns = {
      "okay", okCancelButton,
			GROUP_NUMBER, new LinkConverter(form),
			GROUP_TYPE, new TextToLocalizedTextConverter(),
			REGIONAL_UNION_NUMBER, metaConverter,
			REGIONAL_UNION_ABBR, metaConverter,
			INACTIVE, inActiveConverter,
			REPORT_STATUS, reportStatusDropDownMenuConverter,
			MEMBER_COUNT,null,
			PLAYER_COUNT,null
		};
      
      
    EntityBrowser browser = new EntityBrowser();
    browser.setWidth(browser.HUNDRED_PERCENT);
    browser.setLeadingEntity(WorkReport.class);
    browser.setAcceptUserSettingsShowUserSettingsButton(false,false);
    if( entities!=null && !entities.isEmpty()){
			browser.setDefaultNumberOfRows(entities.size());
    }
    // switch off the internal form of the browser
    browser.setUseExternalForm(true);
    
    for (int i = 0; i < columns.length; i+=2) {
      String column = (String) columns[i];
      EntityToPresentationObjectConverter converter = (EntityToPresentationObjectConverter) columns[i+1];
      browser.setMandatoryColumn(i, column);
      browser.setEntityToPresentationConverter(column, converter);
    }
    
    browser.setEntities("dummy_string", entities);
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
          return ((EntityRepresentation) entity).getColumnValue(REPORT_STATUS);
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
	protected int getYear(){
		return (new IWTimestamp(IWTimestamp.getTimestampRightNow()).getYear());
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
				text.setText(iwc.getApplicationContext().getApplication().getBundle(IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc.getCurrentLocale()).getLocalizedString(valueString,valueString));               
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
				link.addParameter(WorkReportWindow.ACTION,iwc.getParameter(WorkReportWindow.ACTION));
				

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
			
			try {
				Group regionalUnion = getWorkReportBusiness(iwc).getGroupBusiness().getGroupByGroupID(report.getRegionalUnionGroupId().intValue());
				String metaDataKey = path.getShortKey();
				String metaDataValue = regionalUnion.getMetaData(metaDataKey);

				if(metaDataValue!=null){
					text.setText(metaDataValue);   
				}
				else{
					text.setText("");
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
	
	/** 
	 * CheckBoxConverterHelper:
	 * Inner class.
	 */
  
	class InActiveCheckBoxConverter extends CheckBoxConverter {
    
		private List maintainParameterList = new ArrayList(0);
    
		
		public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc)	{
			return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
		}
    
		/** This method uses a copy of the specified list */
		public void maintainParameters(List maintainParameters) {
			this.maintainParameterList.addAll(maintainParameters);
		}    
        
		public PresentationObject getPresentationObject( Object entity,EntityPath path,EntityBrowser browser,IWContext iwc) {
      
			WorkReport report = (WorkReport) entity;
			Integer id = (Integer) report.getPrimaryKey();

			
			boolean shouldBeChecked = report.isInActive();

			if (iwc.isParameterSet(ConverterConstants.EDIT_ENTITY_KEY)) {
				String idEditEntity = iwc.getParameter(ConverterConstants.EDIT_ENTITY_KEY);
				try {
					Integer primaryKey = new Integer(idEditEntity);
					if (id.equals(primaryKey))  {
						CheckBox checkBox = new CheckBox(getKeyForCheckBox(), id.toString());
						checkBox.setChecked(shouldBeChecked);
						return checkBox;
					}
				}
				catch (NumberFormatException ex)  {
				}
			}
			String text;
			if (shouldBeChecked) {
				// black dot
				text = "X";
			}
			else {
				text = "_";
			}
			if (! editable) {
				return new Text(text);
			}
			
			Link link = new Link(text);
			link.addParameter(ConverterConstants.EDIT_ENTITY_KEY, id.toString());
			link.addParameter(WorkReportWindow.ACTION,iwc.getParameter(WorkReportWindow.ACTION));

			return link;
		}
    

	}
	
}
