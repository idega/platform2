package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.idega.block.entity.presentation.EntityBrowser;
import com.idega.block.entity.presentation.converters.TextEditorConverter;
import com.idega.business.IBOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.Form;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jul 3, 2003
 */
public class WorkReportBoardMemberEditor extends WorkReportSelector {
	
	private static final String STEP_NAME_LOCALIZATION_KEY = "workreportboardmembereditor.step_name";
  
  public void main(IWContext iwc) throws Exception {
    super.main(iwc);
    
    if (this.getWorkReportId() != -1) {
			//sets this step as bold, if another class calls it this will be overridden
			setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
      //parseAction(iwc);
      Form form = new Form();
      PresentationObject pres = getContent(iwc);
      form.maintainParameters(this.getParametersToMaintain());
      form.add(pres);
      add(form);
    }
  }
  
  private PresentationObject getContent(IWContext iwc) {
    WorkReportBusiness workReportBusiness = getWorkReportBusiness();
    Collection coll;
    try {
      coll =
        workReportBusiness.getAllWorkReportBoardMembersForWorkReportId(
          getWorkReportId());
    } catch (RemoteException e) {
      System.err.println("[WorkReportMemberDataEditor] Can't get members. Message was: "+ e.getMessage());
      e.printStackTrace(System.err);
      coll = new ArrayList();
    }
    return getEntityBrowser(coll);
  }
  
  private EntityBrowser getEntityBrowser(Collection entities)  {
    // define converter
    TextEditorConverter converter = new TextEditorConverter();
    converter.maintainParameters(this.getParametersToMaintain());
    // define path short keys 
    String[] columns = {
      "is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember.NAME",
      "is.is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember.PERSONAL_ID",
      "is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember.STREET_NAME",
      "is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember.POSTAL_CODE_ID",
      "is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember.HOME_PHONE",
      "is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember.WORK_PHONE",
      "is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember.FAX",
      "is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember.EMAIL"};
    EntityBrowser browser = new EntityBrowser();
    browser.setAcceptUserSettingsShowUserSettingsButton(false,false);
    if( entities!=null && !entities.isEmpty()) browser.setDefaultNumberOfRows(entities.size());
    // switch off the internal form of the browser
    browser.setUseExternalForm(true);
    for (int i = 0; i < columns.length; i++) {
      String column = columns[i];
      browser.setMandatoryColumn(i, column);
      browser.setEntityToPresentationConverter(column, converter);
    }
    browser.setEntities("dummy_string", entities);
    return browser;
  }
    
  private WorkReport getWorkReport(int workreportId)  {
    WorkReportBusiness workReportBusiness = getWorkReportBusiness();
    try {
      return workReportBusiness.getWorkReportById(workreportId);
    } catch (RemoteException e) {
      throw new RuntimeException("[WorkReportMemberDataEditor] Can't get WorkReportBusiness.");
    }  
    
  }
    
	/**
	 * 
	 */
	public WorkReportBoardMemberEditor() {
		super();
		setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
	}

  public WorkReportBusiness getWorkReportBusiness() {
    try {
      return (WorkReportBusiness) IBOLookup.getServiceInstance( getIWApplicationContext(), WorkReportBusiness.class);
    }
    catch (RemoteException ex) {
      System.err.println("[WorkReportMemberDataEditor]: Can't retrieve WorkReportBusiness. Message is: " + ex.getMessage());
      throw new RuntimeException("[WorkReportMemberDataEditor]: Can't retrieve WorkReportBusiness");
    }
  }    
      
    
   
    
    
    

}



  