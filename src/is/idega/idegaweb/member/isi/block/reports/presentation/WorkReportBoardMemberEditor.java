package is.idega.idegaweb.member.isi.block.reports.presentation;

import is.idega.idegaweb.member.isi.block.reports.business.WorkReportBusiness;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReport;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember;
import is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.idega.block.entity.business.EntityToPresentationObjectConverter;
import com.idega.block.entity.data.EntityPath;
import com.idega.block.entity.data.EntityPathValueContainer;
import com.idega.block.entity.presentation.EntityBrowser;
import com.idega.block.entity.presentation.converters.DropDownMenuConverter;
import com.idega.block.entity.presentation.converters.OptionProvider;
import com.idega.block.entity.presentation.converters.TextEditorConverter;
import com.idega.business.IBOLookup;
import com.idega.data.IDOException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
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
  
  private List orderedHelperList;

  public WorkReportBoardMemberEditor() {
    super();
    setStepNameLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
  }  
  
  
  public void main(IWContext iwc) throws Exception {
    super.main(iwc);
    IWResourceBundle resourceBundle = getResourceBundle(iwc);
    
    if (this.getWorkReportId() != -1) {
			//sets this step as bold, if another class calls it this will be overwritten 
			setAsCurrentStepByStepLocalizableKey(STEP_NAME_LOCALIZATION_KEY);
      parseAction(iwc);
      Form form = new Form();
      PresentationObject pres = getContent(iwc, resourceBundle);
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
  
  private PresentationObject getContent(IWContext iwc, IWResourceBundle resourceBundle) {
    WorkReportBusiness workReportBusiness = getWorkReportBusiness(iwc);
    Collection coll;
    try {
      coll =
        workReportBusiness.getAllWorkReportMembersForWorkReportId(
          getWorkReportId());
    } catch (RemoteException e) {
      System.err.println("[WorkReportMemberDataEditor] Can't get members. Message was: "+ e.getMessage());
      e.printStackTrace(System.err);
      coll = new ArrayList();
    }
    // create new entities
    List list = new ArrayList();
    Iterator iterator = coll.iterator();
    while (iterator.hasNext())  {
      WorkReportMember member = (WorkReportMember) iterator.next();
      Collection leagues = null;
      try {
        leagues = member.getLeaguesForMember();
      }
      catch (IDOException ex) {
        System.err.println(
          "[WorkReportBoardMemberEditor]: Can't retrieve leagues. Message is: "
            + ex.getMessage());
        ex.printStackTrace(System.err);
        throw new RuntimeException("[WorkReportBoardMemberEditor]: Can't retrieve leagues.");
      }
      Iterator leagueIterator = leagues.iterator();
      while (leagueIterator.hasNext())  {
        WorkReportGroup league = (WorkReportGroup) leagueIterator.next();
        String leagueName = league.getName();
        WorkReportBoardMemberHelper helper = new WorkReportBoardMemberHelper(leagueName, member);
        list.add(helper);
      }
    }
    // sort list
    Comparator comparator = new Comparator()  {
      public int compare(Object first, Object second) {
        String firstLeague = ((WorkReportBoardMemberHelper) first).getLeague();
        String secondLeague = ((WorkReportBoardMemberHelper) second).getLeague();
        return firstLeague.compareTo(secondLeague);
      }
    };
    Collections.sort(list, comparator);
    // store this list for converters
    orderedHelperList = list;
    Collection entities = new ArrayList();
    Iterator iteratorHelper = list.iterator();
    while (iteratorHelper.hasNext())  {
      WorkReportBoardMemberHelper helper = (WorkReportBoardMemberHelper) iteratorHelper.next();
      entities.add(helper.getMember());
    }        
    return getEntityBrowser(entities, resourceBundle);
  }
  
  private EntityBrowser getEntityBrowser(Collection entities, IWResourceBundle resourceBundle)  {
    // define converter
    TextEditorConverter textEditorConverter = new TextEditorConverter();
    textEditorConverter.maintainParameters(this.getParametersToMaintain());
    EntityToPresentationObjectConverter statusDropDownMenuConverter = getConverterForStatus(resourceBundle);
    EntityToPresentationObjectConverter leagueDropDownMenuConverter = getConverterForLeague(resourceBundle);
    
    // define path short keys and map corresponding converters
    Object[] columns = {
      "league", leagueDropDownMenuConverter,
      "is.idega.idegaweb.member.isi.block.reports.data.WorkReportMember.STATUS", statusDropDownMenuConverter,
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
  
  private EntityToPresentationObjectConverter getConverterForStatus(final IWResourceBundle resourceBundle) {
    DropDownMenuConverter converter = new DropDownMenuConverter();
    OptionProvider optionProvider = new OptionProvider() {
      
      Map optionMap = null;
      
      public Map getOptions(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
        if (optionMap == null)  {
          optionMap = new TreeMap();
          String[] options = { 
            IWMemberConstants.MEMBER_BOARD_MEMBER, 
            IWMemberConstants.MEMBER_BOARD_STATUS_CHAIR_MAN,
            IWMemberConstants.MEMBER_CASHIER,
            IWMemberConstants.MEMBER_SECRETARY};
          for (int i = 0; i < options.length; i++) {  
            String display = resourceBundle.getLocalizedString(options[i], options[i]);
            optionMap.put(options[i],display);
          }
        }
        return optionMap;
      }
    };     
    converter.setOptionProvider(optionProvider); 
    converter.maintainParameters(getParametersToMaintain());
    return converter;
  }
  
  private EntityToPresentationObjectConverter getConverterForLeague(final IWResourceBundle resourceBundle) {
    DropDownMenuConverter converter = new DropDownMenuConverter() {
      protected Object getValue(
        Object entity,
        EntityPath path,
        EntityBrowser browser,
        IWContext iwc)  {
          int i = browser.getCurrentRow();
          WorkReportBoardMemberHelper helper = (WorkReportBoardMemberHelper) orderedHelperList.get(i-1);
          return helper.getLeague();
        }
    };
        
    OptionProvider optionProvider = new OptionProvider() {
      
    Map optionMap = null;
      
    public Map getOptions(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
      if (optionMap == null)  {
        optionMap = new TreeMap();
        WorkReportBusiness business = getWorkReportBusiness(iwc);
        Collection coll = null;
        try {
          coll = business.getAllLeagueWorkReportGroupsForYear(getYear());
        }
        catch (RemoteException ex) {
          System.err.println(
            "[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBusiness. Message is: "
              + ex.getMessage());
          ex.printStackTrace(System.err);
          throw new RuntimeException("[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBusiness.");
        }
        Iterator collIterator = coll.iterator();
        while (collIterator.hasNext())  {
          WorkReportGroup league = (WorkReportGroup) collIterator.next();
          String name = league.getName();  
          String display = resourceBundle.getLocalizedString(name, name);
          optionMap.put(name, display);
          }
        }
        return optionMap;
      }
    };     
    converter.setOptionProvider(optionProvider); 
    converter.maintainParameters(getParametersToMaintain());
    return converter;
  }    
  
}  
//            protected Object getValue(
//          Object entity,
//          EntityPath path,
//          EntityBrowser browser,
//          IWContext iwc)  {
//        WorkReportMember member = (WorkReportMember) entity;
//        Collection coll = member.getLeaguesForYear(yearOfReport);
//        // should be at most one league
//        IDOEntity idoEntity = (IDOEntity) entity;
//        Object object = path.getValue((GenericEntity) entity);
//        return (object == null) ? "" : object;
//      }
//    }      
//      
//      
//    WorkReportBusiness business = getWorkReportBusiness(iwac);
//    Collection coll = null;
//    try {
//      coll = business.getAllLeagueWorkReportGroupsForYear(getYear());
//    }
//    catch(RemoteException ex) {
//      System.err.println("[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBusiness. Message is: "+ ex.getMessage());
//      ex.printStackTrace(System.err);
//      throw new RuntimeException("[WorkReportBoardMemberEditor]: Can't retrieve WorkReportBusiness.");
//    } 
//    Iterator iterator = coll.iterator();
//    while (iterator.hasNext()) {
//      WorkReportGroup group = (WorkReportGroup) iterator.next();
//      group.getShortName();
//    }
//    String[] options = { 
//      IWMemberConstants.MEMBER_BOARD_MEMBER, 
//      IWMemberConstants.MEMBER_BOARD_STATUS_CHAIR_MAN,
//      IWMemberConstants.MEMBER_CASHIER,
//      IWMemberConstants.MEMBER_SECRETARY};
//    for (int i = 0; i < options.length; i++) {  
//      String display = resourceBundle.getLocalizedString(options[i], options[i]);
//      converter.addOption(options[i],display);
//    }  
//    return converter;
//  }  
    
  class WorkReportBoardMemberHelper {
    
    String league = null;
    WorkReportMember member = null;
    
    public WorkReportBoardMemberHelper()  {
    }
    
    public WorkReportBoardMemberHelper(String league, WorkReportMember member) {
      this.league = league;
      this.member = member;
    }
    
    public void setLeague(String league)  {
      this.league = league;
    }
    
    public void setMember(WorkReportMember member) {
      this.member = member;
    }
    
    public String getLeague() {
      return league;
    }  
    
    public WorkReportMember getMember() {
      return member;
    }
  }

  


  