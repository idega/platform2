package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportDivisionBoard;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;
import is.idega.idegaweb.member.isi.block.reports.util.WorkReportConstants;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.entity.business.EntityToPresentationObjectConverter;
import com.idega.block.entity.data.EntityPath;
import com.idega.block.entity.presentation.EntityBrowser;
import com.idega.block.entity.presentation.converter.CheckBoxConverter;
import com.idega.block.entity.presentation.converter.ConverterConstants;
import com.idega.block.entity.presentation.converter.editable.CheckBoxAsLinkConverter;
import com.idega.block.entity.presentation.converter.editable.EditOkayButtonConverter;
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
import com.idega.util.IWTimestamp;

/**
 * <p>Description: A viewer/editor for a single workreport</p>
 * <p>Copyright: Idega SoftwareCopyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="eiki@idega.is">Eirikur Hrafnsson</a>
 * @version 1.0
 */

public class WorkReportOverViewCloseView extends Block {
	 
	protected WorkReportBusiness reportBiz;
	
  private static final String SUBMIT_SAVE_NEW_ENTRY_KEY = "submit_sv_new_entry_key";
  private static final String SUBMIT_CANCEL_KEY = "submit_cancel_key";

  private static final Integer NEW_ENTRY_ID_VALUE = new Integer(-1);
  private static final String NO_LEAGUE_VALUE = "no_league_value";
  
  private static final String ACTION_SHOW_NEW_ENTRY = "action_show_new_entry";
  
  private static final String CHECK_BOX = "checkBox";
  
  public static final String CLOSE_VIEW_WORK_REPORT_ID = "close_view_work_report_id";


	private static final String REPORT_YEAR = WorkReport.class.getName()+".ISI_WORK_REPORT_ID"+"|"+"YEAR_OF_REPORT";
	private static final String REGIONAL_UNION_NUMBER = WorkReport.class.getName()+".ISI_WORK_REPORT_ID"+"|"+"REG_UNI_NR"; 
	private static final String REGIONAL_UNION_ABBR = WorkReport.class.getName()+".ISI_WORK_REPORT_ID"+"|"+"REG_UNI_ABBR";
	private static final String GROUP_NUMBER = WorkReport.class.getName()+".ISI_WORK_REPORT_ID"+"|"+"GROUP_NUMBER";
	private static final String GROUP_NAME = WorkReport.class.getName()+".ISI_WORK_REPORT_ID"+"|"+"GROUP_NAME";
	private static final String HAS_NATIONAL_LEAGUE = WorkReportDivisionBoard.class.getName()+".HAS_NATIONAL_LEAGUE";
	private static final String GROUP_TYPE = WorkReport.class.getName()+".ISI_WORK_REPORT_ID"+"|"+"GROUP_TYPE";


	private static final String MEMBER_COUNT = WorkReport.class.getName()+".ISI_WORK_REPORT_ID"+"|"+"TOTAL_MEMBERS";
	private static final String PLAYER_COUNT = WorkReportDivisionBoard.class.getName()+".TOTAL_PLAYERS";
	private static final String COMPETITOR_COUNT = WorkReportDivisionBoard.class.getName()+".TOTAL_COMPETITORS";
	
	
	private static final String LEAGUE_NR = WorkReportGroup.class.getName()+".ISI_WR_GROUP_ID"+"|"+"GROUP_NUMBER";
	private static final String LEAGUE_SHORT_NAME = WorkReportGroup.class.getName()+".ISI_WR_GROUP_ID"+"|"+"SHORT_NAME";

	private static final String HAS_MEMBERS = "WorkReportOverViewCloseView.HAS_MEMBERS";
	private static final String HAS_ACCOUNT = "WorkReportOverViewCloseView.HAS_ACCOUNT";
	private static final String HAS_BOARD= "WorkReportOverViewCloseView.HAS_BOARD";
	
	private int year = -1;
	
	
	
  
	// define path short keys and map corresponding converters
		 //year of report
		 //regional union nr
		 //regional union abbr
		 //has national league checkbox
		 //club type
		 //Leagues nr and abbr.
		 //Total members
		 //Total players
		 //is
			 //memberpart
			 //accountpart
			 //board part arrived
			 
  private boolean editable = true;
   
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";


	public String getBundleIdentifier(){
		return IW_BUNDLE_IDENTIFIER;
	}
	
  public WorkReportOverViewCloseView() {
    super();
  }  
  
  
  public void main(IWContext iwc) throws Exception {
   
    IWResourceBundle iwrb = getResourceBundle(iwc);
		if(year==-1) {
			year = (new IWTimestamp(IWTimestamp.getTimestampRightNow()).getYear());
		}
	parseAction(iwc);
    Form form = new Form();
    PresentationObject pres = getContent(iwc, iwrb, form);
    form.add(pres);

    add(form);
	addBreak();
    add(new PrintButton(iwrb.getLocalizedImageButton("workreportsender.print","print")));
	Link backLink = new Link(iwrb.getLocalizedString("workreportmultieditor.back_button","back"));
	backLink.setAsImageButton(true);			
	backLink.addParameter(WorkReportWindow.ACTION,WorkReportWindow.ACTION_REPORT_OVERVIEW);
	backLink.addParameter(WorkReportConstants.WR_SESSION_PARAM_WORK_REPORT_YEAR,Integer.toString(year));
	add(backLink);
	
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
      
			WorkReportDivisionBoard division;
			try {
				division = workReportBusiness.getWorkReportDivisionBoardHome().findByPrimaryKey(primaryKey);
			
				boolean isChecked = CheckBoxConverter.isEntityCheckedUsingDefaultKey(iwc,primaryKey);
				
				 if(isChecked){
						division.setHasNationalLeague(true);
				 }
				 else{
					division.setHasNationalLeague(false);
				 }
				
	
				division.store();
			
			}
		
			catch (FinderException e) {
				e.printStackTrace();
			}
		 }  

		 return action;
	 }
  
  private PresentationObject getContent(IWContext iwc, IWResourceBundle resourceBundle, Form form) throws RemoteException {
  	  
  	String workReportId = iwc.getParameter(CLOSE_VIEW_WORK_REPORT_ID);
    Collection divisions =  getWorkReportBusiness(iwc).getAllWorkReportDivisionBoardForWorkReportIdAndYear(Integer.parseInt(workReportId),getYear());
   
    EntityBrowser browser = getEntityBrowser(divisions, resourceBundle, form);
    
    // put browser into a table
    Table mainTable = new Table(1,2);
    mainTable.add(browser, 1,1);    
    
    return mainTable;    
  }
  

 
  private EntityBrowser getEntityBrowser(Collection entities, IWResourceBundle resourceBundle, Form form)  {
 
    List params = new ArrayList();
    params.add(WorkReportWindow.ACTION);
    params.add(CLOSE_VIEW_WORK_REPORT_ID);
    params.add(WorkReportConstants.WR_SESSION_PARAM_WORK_REPORT_YEAR);
    form.maintainParameters(params);
    
    // define path short keys and map corresponding converters
    //year of report
    //regional union nr
    //regional union abbr
    //has national league checkbox
    //club type
    //Leagues nr and abbr.
    //Total members
    //Total players
    //is
    	//memberpart
    	//accountpart
    	//board part arrived
    /*

		private static final String HAS_MEMBERS = "HAS_MEMBERS";
		private static final String HAS_ACCOUNT = "HAS_ACCOUNT";
		private static final String HAS_BOARD= "HAS_BOARD";*/
		
		HasNationLeagueCheckBoxConverter hasNLConverter = new HasNationLeagueCheckBoxConverter();
		hasNLConverter.maintainParameters(params);
		
		EditOkayButtonConverter okCancelButton = new EditOkayButtonConverter();
		okCancelButton.maintainParameters(params);
		
    Object[] columns = {
    	"okeCancel", okCancelButton,
    	REPORT_YEAR,null,
	//		REGIONAL_UNION_NUMBER,null,
			REGIONAL_UNION_ABBR,null,
	//		GROUP_NUMBER,null,
			GROUP_NAME,null,
			HAS_NATIONAL_LEAGUE,hasNLConverter,
			GROUP_TYPE,new TextToLocalizedTextConverter(),
	//		MEMBER_COUNT,null,
			PLAYER_COUNT,null,
			COMPETITOR_COUNT,null,
	//		LEAGUE_NR,null,
			LEAGUE_SHORT_NAME,null,
			HAS_MEMBERS, new HasSomeDataConverter(),
			HAS_ACCOUNT, new HasSomeDataConverter(),
			HAS_BOARD, new HasSomeDataConverter(),
			//"back",new BackButtonConverter(resourceBundle),
		};
      
      
    EntityBrowser browser = EntityBrowser.getInstanceUsingEventSystemAndExternalForm();
    browser.setWidth(Table.HUNDRED_PERCENT);
    browser.setCellpadding(3);
    browser.setRowHeight(1,"15");
    browser.setLeadingEntity(WorkReportDivisionBoard.class);
    browser.addEntity(WorkReport.class.getName());
		browser.addEntity(WorkReportGroup.class.getName());
    
    
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
	
	protected int getYear(){
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
	


	class BackButtonConverter implements EntityToPresentationObjectConverter {
		IWResourceBundle iwrb;
		
			public BackButtonConverter(IWResourceBundle iwrb) {
				this.iwrb = iwrb;
			}
		
			public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc){
				return new Text("");
			}
		
			public PresentationObject getPresentationObject(Object value, EntityPath path, EntityBrowser browser, IWContext iwc){
			
				Link backLink = new Link(iwrb.getLocalizedString("workreportmultieditor.back_button","back"));
				backLink.setAsImageButton(true);
				
				backLink.addParameter(WorkReportWindow.ACTION,WorkReportWindow.ACTION_REPORT_OVERVIEW);
				
				return backLink;
			}
		
		}
	
	
	/** 
	 * CheckBoxConverterHelper:
	 * Inner class.
	 */
  
	class HasNationLeagueCheckBoxConverter extends CheckBoxAsLinkConverter {
    
			protected boolean shouldEntityBeChecked(Object entity, Integer primaryKey) {
				WorkReportDivisionBoard division = (WorkReportDivisionBoard) entity;
				return  division.hasNationalLeague();
			}
	}
	
	
	class HasSomeDataConverter implements EntityToPresentationObjectConverter {
		
		public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc){
			return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
		}
		
		public PresentationObject getPresentationObject(Object value, EntityPath path, EntityBrowser browser, IWContext iwc){
			
			WorkReportDivisionBoard division = (WorkReportDivisionBoard)value;	
			boolean hasData = false;
			
			Text text = (Text) browser.getDefaultTextProxy().clone();
			text.setText("-");
			
			try {

				
				WorkReportGroup wrGroup = getWorkReportBusiness(iwc).getWorkReportGroupHome().findByPrimaryKey(new Integer(division.getWorkReportGroupID()));
				int wrGroupId = division.getWorkReportGroupID();
				int wrId = division.getReportId();

				String shortKey = path.getShortKey();
				if(HAS_MEMBERS.equals(shortKey) ) {		
					int count = getWorkReportBusiness(iwc).getCountOfPlayersByWorkReportAndWorkReportGroup(getWorkReportBusiness(iwc).getWorkReportById(wrId),wrGroup);
					hasData = (count>0);
				}
				else if(HAS_ACCOUNT.equals(shortKey) ) {
					Collection records = getWorkReportBusiness(iwc).getWorkReportClubAccountRecordHome().findAllRecordsByWorkReportIdAndWorkReportGroupId(wrId,wrGroupId);
					hasData = (records!=null && !records.isEmpty());
					
				}
				else if(HAS_BOARD.equals(shortKey) ) {
					int count = getWorkReportBusiness(iwc).getWorkReportBoardMemberHome().getCountOfWorkReportBoardMembersByWorkReportIdAndWorkReportGroupId(wrId,wrGroupId);
					hasData = (count>0);
				}        
			  

				
			}
			catch (FinderException e) {
				hasData = false;
				e.printStackTrace();
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			
			if(hasData) {
				 text.setText("X");
			}
			
			return text;
		}
		
	}


	/**
	 * @param year
	 */
	public void setYear(int year) {
		this.year = year;
	}
	
	
	
}
