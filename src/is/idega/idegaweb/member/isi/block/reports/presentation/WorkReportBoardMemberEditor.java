package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.idega.block.entity.business.EntityToPresentationObjectConverter;
import com.idega.block.entity.data.EntityPathValueContainer;
import com.idega.block.entity.presentation.EntityBrowser;
import com.idega.block.entity.presentation.converters.DropDownMenuConverter;
import com.idega.block.entity.presentation.converters.TextEditorConverter;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
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
  
  public WorkReportBoardMemberEditor() {
    super();
    setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
  }  
  
  
  public void main(IWContext iwc) throws Exception {
    super.main(iwc);
    
    if (this.getWorkReportId() != -1) {
			//sets this step as bold, if another class calls it this will be overwritten 
			setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
      parseAction(iwc);
      Form form = new Form();
      PresentationObject pres = getContent(iwc);
      form.maintainParameters(this.getParametersToMaintain());
      form.add(pres);
      add(form);
    }
  }
  
  private String parseAction(IWContext iwc) {
    EntityPathValueContainer entityPathValueContainer = TextEditorConverter.getResultByParsing(iwc);
    if (entityPathValueContainer.isValid()) {
      // store
      Object object = entityPathValueContainer.getValue();
    }
    return "";
  }
  
  private PresentationObject getContent(IWContext iwc) {
    WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
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
    TextEditorConverter textEditorConverter = new TextEditorConverter();
    DropDownMenuConverter dropdownMenuConverter = new DropDownMenuConverter();
    dropdownMenuConverter.maintainParameters(this.getParametersToMaintain());
    textEditorConverter.maintainParameters(this.getParametersToMaintain());
    // define path short keys and map corresponding converters
    Object[] columns = {
      "is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember.STATUS", dropdownMenuConverter,
      "is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember.NAME", textEditorConverter,
      "is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember.PERSONAL_ID", textEditorConverter,
      "is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember.STREET_NAME", textEditorConverter,
      "is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember.POSTAL_CODE_ID", textEditorConverter,
      "is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember.HOME_PHONE", textEditorConverter,
      "is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember.WORK_PHONE", textEditorConverter,
      "is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember.FAX", textEditorConverter,
      "is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember.EMAIL", textEditorConverter};
    EntityBrowser browser = new EntityBrowser();
    browser.setAcceptUserSettingsShowUserSettingsButton(false,false);
    if( entities!=null && !entities.isEmpty()) browser.setDefaultNumberOfRows(entities.size());
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
}



  