/*
 * Created on 2003-sep-23
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.school.presentation;

import is.idega.idegaweb.member.presentation.UserSearcher;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.resource.business.ResourceBusiness;
import se.idega.idegaweb.commune.accounting.resource.data.Resource;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean;
import se.idega.idegaweb.commune.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import se.idega.idegaweb.commune.school.business.SchoolCommuneBusiness;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryHome;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import com.idega.business.IBOLookup;
import com.idega.core.data.Address;
import com.idega.core.data.Phone;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author Göran Borgman
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CentralPlacingEditor extends CommuneBlock {
  // Localization keys
  private static final String KP = "central_placing_editor.";
  private static final String KEY_WINDOW_HEADING = KP+"window_heading";
  private static final String KEY_SEARCH_PUPIL_HEADING = KP+"search_pupil_heading";
  private static final String KEY_PUPIL_HEADING = KP+"pupil_heading";
  private static final String KEY_CURRENT_PLACEMENT_HEADING = KP+"current_placemant_heading";
  private static final String KEY_NEW_PLACEMENT_HEADING = KP+"new_placement_heading";
  
  private static final String KEY_PERSONAL_ID_LABEL = KP+"personal_id_label";
  private static final String KEY_FIRST_NAME_LABEL = KP+"first_name_label";
  private static final String KEY_LAST_NAME_LABEL = KP+"last_name_label";
  private static final String KEY_ADDRESS_LABEL = KP+"address_label";
  private static final String KEY_PHONE_LABEL = KP+"telephone_label";
  private static final String KEY_ACTIVITY_LABEL = KP+"activity_label";
  private static final String KEY_PLACEMENT_LABEL = KP+"placement_label";
  private static final String KEY_RESOURCES_LABEL = KP+"resources_label";
  private static final String KEY_CONTRACT_LABEL = KP+"contract_label";
  private static final String KEY_CONTRACT_YES = KP+"contract_yes";
  private static final String KEY_PROVIDER_LABEL = KP+"provider_label";
  private static final String KEY_MAIN_ACTIVITY_LABEL = KP+"main_activity";
  private static final String KEY_ADMIN_LABEL = KP+"admin_label";
  private static final String KEY_SCHOOL_YEAR_LABEL = KP+"school_year_label";
  private static final String KEY_SCHOOL_GROUP_LABEL = KP+"school_group_label";
  private static final String KEY_STUDY_PATH_LABEL = KP+"study_path_label";
  private static final String KEY_RESOURCE_LABEL = KP+"resource_label";
  private static final String KEY_COMMUNE_LABEL = KP+"commune_label";
  private static final String KEY_PAYMENT_BY_INVOICE_LABEL = KP+"payment_by_invoice_label";
  private static final String KEY_PLACEMENT_PARAGRAPH_LABEL = "placement_paragraph_label";
  private static final String KEY_GROUP_LABEL = KP+"group_label";
  private static final String KEY_PAYMENT_BY_AGREEMENT_LABEL = KP+"Payment by agreement: ";
  private static final String KEY_INVOICE_INTERVAL_LABEL = KP+"Invoice interval: ";
  private static final String KEY_PLACEMENT_DATE_LABEL = KP+"placement_date_label";

  private static final String KEY_BUTTON_SEARCH = KP+"button_search";
  private static final String KEY_DROPDOWN_CHOSE = KP+"dropdown_chose";
  private static final String KEY_SCHOOL_YEAR = KP+"school_year";
  private static final String KEY_SCHOOL_GROUP = KP+"school_group";

      //  Keys for error messages
  private static final String KEY_ERROR_PAST_TIME = KP+"error.no_past_dates";
  
  // Http request parameters  
  private static final String PARAM_SCHOOL_CATEGORY = "param_school_category";
  private static final String PARAM_PROVIDER = "param_provider";
  private static final String PARAM_ACTIVITY = "param_activity";
  private static final String PARAM_SCHOOL_YEAR = "param_school_year";
  private static final String PARAM_SCHOOL_GROUP = "param_school_group";
  private static final String PARAM_STUDY_PATH = "param_study_path";
  private static final String PARAM_PLACEMENT_DATE = "param_placement_date";
  private static final String PARAM_RESOURCES = "param_resources";
  private static final String PARAM_HIDDEN_SUBMIT_SRC = "param_hidden_submit_src";
  private static final String PARAM_ACTION = "param_action";
  private static final String PARAM_PRESENTATION = "param_presentation";
  
  // Actions
  private static final int ACTION_REMOVE_SESSION_CHILD = 1;
  
  // CSS styles   
  private static final String STYLE_UNDERLINED_SMALL_HEADER = 
                  "font-style:normal;text-decoration:underline;color:#000000;" 
               + "font-size:10px;font-family:Verdana,Arial,Helvetica;font-weight:bold;";

  private static final String PATH_TRANS_GIF = 
                   "/idegaweb/bundles/com.idega.core.bundle/resources/transparentcell.gif";
                   
  // Session java bean keys
  private static final String SESSION_KEY_CHILD = KP+"session_key.child";
  // Instance variables
  private Table mainTable;
  private User child;
  private Address address;
  private boolean hasChild = false;
  private ApplicationForm appForm;
  private Image transGIF = new Image(PATH_TRANS_GIF);
  private String errMsgMid = null;
  private String errMsgBottom = null;
    // Form status variables
  private String categoryStatus = "-1";
  private String providerStatus = "-1";
  private String activityStatus = "-1";
  private String yearStatus = "-1";
  private String groupStatus = "-1";
  
  private int _action = -1;
  private int _presentation = -1;
  
  	public void main(IWContext iwc) throws Exception {
	  	setResourceBundle(getResourceBundle(iwc));
	  	mainTable = new Table();
	  	parse(iwc);
	  
		// Perform actions according the _action input parameter
		switch (_action) {
			case ACTION_REMOVE_SESSION_CHILD :
				removeSessionChild(iwc);
				break;	
		}

		child = (User) iwc.getSession().getAttribute(SESSION_KEY_CHILD);		
	  	// Only process search if no current child
	  	if (child == null) {
		  	processSearch(iwc);
			mainTable.add(getSearchForm(iwc), 1, 1);
		} else {
			mainTable.add(getPlacementForm(iwc), 1, 1);			
		}

	 	 add(mainTable);    
  	}

  public Table getSearchForm(IWContext iwc) {
      // *** Search Table *** START - the uppermost table
      Table table = new Table();
      table.setBorder(1);
      table.setCellpadding(0);
      table.setCellspacing(0);

      int col = 1;
      int row = 1;
      // User search module - configure and add 
      table.add(getUserSearchModule(iwc), col, row);
          
      return table;  
  }

  public Form getPlacementForm(IWContext iwc) {
    // *** Search Table *** START - the uppermost table
    Form form = new Form();
    Table table = new Table();
    form.add(table);
    table.setBorder(1);
    table.setCellpadding(0);
    table.setCellspacing(0);
    transGIF.setHeight("1");
    transGIF.setWidth("1");
        
    int row = 1;
    int col = 1;
    // add empty space row
    table.add(transGIF, col++, row);
    table.add(transGIF, col++, row);
    table.add(transGIF, col++, row);
    table.add(transGIF, col++, row);
    table.add(transGIF, col++, row);
    // Set COLUMN WIDTH for column 1 to 5
    table.setWidth(1, row, "5");
    table.setWidth(2, row, "70");
    table.setWidth(3, row, "70");
    table.setWidth(4, row, "70");
    table.setWidth(5, row, "100");    
    
    // Hidden parameters
    table.add(new HiddenInput(PARAM_ACTION, "-1"), 1, 1);
//	table.add(new HiddenInput(PARAMETER_METHOD, String.valueOf(METHOD_MOVE_YEAR)), 1, 1);
    
    row++; col = 2;

	// *** HEADING Pupil ***
	Text pupilTxt = new Text(localize(KEY_PUPIL_HEADING, "Pupil"));
	pupilTxt.setFontStyle(STYLE_UNDERLINED_SMALL_HEADER);
	table.add(pupilTxt, col++, row);
	table.setRowHeight(row, "20");
	table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);	
	SubmitButton searchButton = (SubmitButton) getStyledInterface(new SubmitButton(
										localize(KEY_BUTTON_SEARCH, "Search"), PARAM_PRESENTATION, "3"));
//	searchButton.setValueOnClick(PARAM_ACTION, String.valueOf(ACTION_REMOVE_SESSION_CHILD));
	searchButton.setValueOnClick(PARAM_ACTION, "1");
	table.add(searchButton, 5, row);

	row++; col = 2;
    // Personal Id Number
	table.add(getSmallHeader(localize(KEY_PERSONAL_ID_LABEL, "Personal id: ")), col++, row);
	if (child != null)
		table.add(getSmallText(child.getPersonalID()), col++, row);
	row++; col = 2;	
	// Last Name
	table.add(getSmallHeader(localize(KEY_LAST_NAME_LABEL, "Last name: ")), col++, row);       
 
    Table nameTable = new Table();
    col = 1;
	nameTable.setCellpadding(0);
	nameTable.setCellspacing(0);
	if (child != null)
	    nameTable.add(getSmallText(child.getLastName()), col++, 1);
    // First Name       
    nameTable.add(getSmallHeader(localize(KEY_FIRST_NAME_LABEL, "First name: ")), col++, 1);       
	if (child != null)
	    nameTable.add(getSmallText(child.getFirstName()), col++, 1);
    nameTable.setWidth(1, 1, "100");
    nameTable.setWidth(2, 1, "100");
    nameTable.setWidth(3, 1, "100");
    table.add(nameTable, 3, row);
	table.mergeCells(3, row, 5, row);
    row++; col = 2;
          
    // Address and Phone
    table.add(getSmallHeader(localize(KEY_ADDRESS_LABEL, "Address: ")), col++, row);
    row++; col = 2;
    table.add(getSmallHeader(localize(KEY_PHONE_LABEL, "Phone: ")), col++, row);
    if (child != null) {
      try {
        // child address
        address = getUserBusiness(iwc).getUsersMainAddress(child);
        StringBuffer buf = new StringBuffer(address.getStreetAddress());
        buf.append(", ");
        buf.append(address.getPostalCode().getPostalAddress());
        row--;
        table.add(getSmallText(buf.toString()), col, row);
        row++;
        // Get child phones
        Collection phones = child.getPhones();
        int i = 0;
        int phonesSize = phones.size();
        buf = new StringBuffer();
        for (Iterator iter = phones.iterator(); iter.hasNext(); i++) {
          Phone phone = (Phone) iter.next();
          buf.append(phone.getNumber());
          if (i < phonesSize - 1)
            buf.append(", ");
        }
        buf.append("&nbsp;");
        table.add(getSmallText(buf.toString()), col, row);
      } catch (Exception e) {
        e.printStackTrace();
      }    
    }
    row++; col = 2;
    
    // *** HEADING Current placment ***
    Text currentPlacementTxt = new Text(localize(KEY_CURRENT_PLACEMENT_HEADING, "Current placement"));
    currentPlacementTxt.setFontStyle(STYLE_UNDERLINED_SMALL_HEADER);
    table.add(currentPlacementTxt, col++, row);
    table.setRowHeight(row, "20");
    table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);
    row++; col = 2;
    // Labels - Current placement
    table.add(getSmallHeader(localize(KEY_ACTIVITY_LABEL, "Activity: ")), col, row++);
    table.add(getSmallHeader(localize(KEY_PLACEMENT_LABEL, "Placement: ")),col, row++);
    table.add(getSmallHeader(localize(KEY_RESOURCES_LABEL, "Resources: ")), col, row++);
    table.add(getSmallHeader(localize(KEY_CONTRACT_LABEL, "Contract: ")), col, row);
    // Values - Current placement
    if (child != null) {
      try {
        SchoolClassMember placement = getCurrentSchoolClassMembership(child, iwc);
        if (placement != null) {
          row--;row--;row--;
          col = 3;
          // Activity
          
          //table.add(placement.getSchoolClass().getSchoolType().getName(), col, row);
          row++;
          
          // Placement
          StringBuffer buf = new StringBuffer(placement.getSchoolClass().getSchool().getName());
          buf.append(", "+localize(KEY_SCHOOL_YEAR, "school year")+" "
                           +placement.getSchoolClass().getSchoolYear().getName()+", "
                           +localize(KEY_SCHOOL_GROUP, "group")+" "
                           +placement.getSchoolClass().getSchoolClassName());         
          table.add(getSmallText(buf.toString()), col, row);
 		  row++;
 		  
          // Resources
          row++;
          
        }
	  } catch (Exception e) {
		e.printStackTrace();
	  }

	  // Contract	  
      try {
        ChildCareContract contract = getChildCareBusiness(iwc).
                                        getValidContractByChild(((Integer) child.getPrimaryKey()).intValue());         
        if (contract != null) {
          table.add(getSmallText(localize(KEY_CONTRACT_YES, "Yes")), col, row);
		}
	  } catch (Exception e) {
		e.printStackTrace();
	  }
    }
    row++; col = 2;
    
    // ERROR MSG - errMsgMid
    if (errMsgMid != null) {
      table.add(getSmallErrorText(errMsgMid), col, row);
      row++;
    }
    
    // *** HEADING New placement *** 
    Text newPlacementTxt = new Text(localize(KEY_NEW_PLACEMENT_HEADING, "New placement"));
    newPlacementTxt.setFontStyle(STYLE_UNDERLINED_SMALL_HEADER);
    table.add(newPlacementTxt, col++, row);
    table.setRowHeight(row, "20");
    table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);
    row++; col = 2;
    // School Category
    table.add(getSmallHeader(localize(KEY_MAIN_ACTIVITY_LABEL,  "Main activity: ")), col++, row);
    table.add(getSchoolCategories(iwc), col++, row); 
    row++; col = 2;
    // Provider
    table.add(getSmallHeader(localize(KEY_PROVIDER_LABEL, "Provider: ")), col++, row);
    table.add(getProviders(iwc), col++, row);
    row++; col = 2;
    table.add(getSmallHeader(localize(KEY_ADDRESS_LABEL, "Address: ")), col++, row);
    table.add(getSmallHeader(localize(KEY_COMMUNE_LABEL, "Commune: ")), ++col, row);    
    row++; col = 2;
    table.add(getSmallHeader(localize(KEY_PHONE_LABEL, "Phone: ")), col++, row);
    row++; col = 2;
    table.add(getSmallHeader(localize(KEY_ADMIN_LABEL, "Administrator: ")), col++, row);
    table.add(getSmallHeader(localize(KEY_PAYMENT_BY_INVOICE_LABEL, "Payment by invoice: ")),
                  ++col, row);
    row++; col = 2;
    table.add(transGIF, col, row); // EMPTY SPACE ROW
    table.setRowHeight(row, "10");
    row++; col = 2;
      // Activity input
    table.add(getSmallHeader(localize(KEY_ACTIVITY_LABEL, "Activity:")), col++, row);
    table.add(getActivities(iwc), col++, row);
    table.add(getSmallHeader(localize(KEY_PLACEMENT_PARAGRAPH_LABEL, "Placement paragraph: ")),
                  col++, row);
    row++; col = 2;
      // School Year input
    table.add(getSmallHeader(localize(KEY_SCHOOL_YEAR_LABEL, "School year: ")), col++, row);
    table.add(getSchoolYears(iwc), col++, row);
      // School group input
    table.add(getSmallHeader(localize(KEY_SCHOOL_GROUP_LABEL, "School group: ")), col++, row);
    table.add(getSchoolGroups(iwc), col++, row);
    row++; col = 2;
      // Study Path input
    table.add(getSmallHeader(localize(KEY_STUDY_PATH_LABEL, "Study path: ")), col ++, row);
    row++; col = 2;
    table.add(transGIF, col, row); // EMPTY SPACE ROW
    table.setRowHeight(row, "10");
    row++; col = 2;    
      // Resource
    table.add(getSmallHeader(localize(KEY_RESOURCE_LABEL, "Resource: ")), col++, row);
        //  Resource input checkboxes
    if (iwc.isParameterSet(PARAM_ACTIVITY) && iwc.isParameterSet(PARAM_SCHOOL_YEAR)) {
      try {
        Collection rscColl = getResourceBusiness(iwc).getAssignableResourcesByYearAndType(
                                iwc.getParameter(PARAM_SCHOOL_YEAR), iwc.getParameter(PARAM_ACTIVITY));
        CheckBox typeRscBox = new CheckBox(PARAM_RESOURCES);
        Integer primaryKey;
        Iterator loop = rscColl.iterator();
        while (loop.hasNext()) {
          col = 3;
          Resource rsc = (Resource) loop.next();
          CheckBox cBox = (CheckBox) typeRscBox.clone();
          primaryKey = (Integer) rsc.getPrimaryKey();
          cBox.setValue(primaryKey.intValue());

          // Set related school types to checked
         /* if (theRsc != null) {
            Map typeMap = busyBean.getRelatedSchoolTypes(theRsc);
            Set typeKeys = typeMap.keySet();
            if (typeKeys.contains(primaryKey)) {
              cBox.setChecked(true);
            }
          } */
          table.add(cBox, col++, row);
          table.add(getSmallText(rsc.getResourceName()), col++, row);    
          row++;
        }        
      } catch (RemoteException e) {
        e.printStackTrace();
      }
    }      
    
    row++; col = 2;
    Table rowTable = new Table();
    rowTable.setCellpadding(0);
    rowTable.setCellspacing(0);
    int tmpRow = 1;
    int tmpCol = 1;
      // Payment by agreement
    rowTable.add(getSmallHeader(localize(KEY_PAYMENT_BY_AGREEMENT_LABEL, "Payment by agreement: ")),
                         tmpCol++, tmpRow);
      // Invoice interval
    rowTable.add(getSmallHeader(localize(KEY_INVOICE_INTERVAL_LABEL, "Invoice interval: ")),
                        ++tmpCol, tmpRow);    
    table.add(rowTable, col, row);
    table.mergeCells(col, row, col+3, row);
    row++; col = 2;
      // Placement date
    table.add(getSmallHeader(localize(KEY_PLACEMENT_DATE_LABEL, "Placement date: ")), col++, row);        
    table.add(getPlacementDateInput(), col, row);
    table.mergeCells(col, row, col+2, row);
    
    return form;   
  }
  
  /**
   * Process the search for a pupil. Gets the parameter and child User object
   * @param iwc Request object context
   */  
  public void processSearch(IWContext iwc) {
    String prm = UserSearcher.getUniqueUserParameterName("one");
    if (iwc.isParameterSet(prm)) {
      Integer firstUserID = Integer.valueOf(iwc.getParameter(prm));
      try {
        child = getUserBusiness(iwc).getUser(firstUserID);
        iwc.getSession().setAttribute(SESSION_KEY_CHILD, child);
      }
      catch (RemoteException e) {
        e.printStackTrace();
      } 
    }
  }
  
  private UserSearcher getUserSearchModule(IWContext iwc) {
    UserSearcher searcher = new UserSearcher();
    searcher.setShowMiddleNameInSearch(true);
    searcher.setOwnFormContainer(false);
    searcher.setUniqueIdentifier("one");
    searcher.setSkipResultsForOneFound(false);
    searcher.setHeaderFontStyleName(getStyleName(STYLENAME_SMALL_HEADER));
    searcher.setButtonStyleName(getStyleName(STYLENAME_INTERFACE_BUTTON));
    searcher.setPersonalIDLength(12);
    searcher.setFirstNameLength(15);
    searcher.setMiddleNameLength(15);
    searcher.setLastNameLength(20);
    
    String prmChild = UserSearcher.getUniqueUserParameterName("child");
    if (iwc.isParameterSet(prmChild)) {
        searcher.maintainParameter(new Parameter(prmChild, iwc.getParameter(prmChild)));
    }
       
    return searcher;    
  }
  
  private DropdownMenu getSchoolCategories(IWContext iwc) {
    // Get dropdown for school categories
    DropdownMenu schoolCats = new DropdownMenu(PARAM_SCHOOL_CATEGORY);
    schoolCats.setToSubmit(true);
    schoolCats.addMenuElement("-1", localize(KEY_DROPDOWN_CHOSE, "- Chose -"));   
    try {
      SchoolCategoryHome schCatHome = getSchoolBusiness(iwc).getSchoolCategoryHome();
      SchoolCategory elementary = schCatHome.findElementarySchoolCategory();
      SchoolCategory collage = schCatHome.findCollegeCategory();
      schoolCats.addMenuElement((String) elementary.getPrimaryKey(), 
                                                localize(elementary.getLocalizedKey(), "Elementary school"));   
      schoolCats.addMenuElement((String) collage.getPrimaryKey(),
                                                localize(collage.getLocalizedKey(), "Collage"));
      if (iwc.isParameterSet(PARAM_SCHOOL_CATEGORY))
        schoolCats.setSelectedElement(iwc.getParameter(PARAM_SCHOOL_CATEGORY));  
		} catch (Exception e) {
			e.printStackTrace();
		}    
    return schoolCats;
  }
  
  private DropdownMenu getProviders(IWContext iwc) {
    // Get dropdown for providers
    DropdownMenu providers = new DropdownMenu(PARAM_PROVIDER);
    providers.setToSubmit(true);
    providers.addMenuElement("-1", localize(KEY_DROPDOWN_CHOSE, "- Chose -"));   
      try {
        // Get school category from topmost dropdown
        if (iwc.isParameterSet(PARAM_SCHOOL_CATEGORY)) {
          // Get schooltypes in category
          Collection schTypes = getSchoolBusiness(iwc).findAllSchoolTypesInCategory(
                                                                           iwc.getParameter(PARAM_SCHOOL_CATEGORY));
          // Get schools in school types of chosen school category
          Collection schools = getSchoolBusiness(iwc).findAllSchoolsByType(schTypes);
          // Fill upp dropdown with schools
          for (Iterator iter = schools.iterator(); iter.hasNext();) {
						School tmpSchool = (School) iter.next();
            int schoolID =((Integer) tmpSchool.getPrimaryKey()).intValue();
            providers.addMenuElement(schoolID, tmpSchool.getSchoolName());
					}
          if (iwc.isParameterSet(PARAM_PROVIDER))
            providers.setSelectedElement(iwc.getParameter(PARAM_PROVIDER));
        }
  		} catch (Exception e) {
  			e.printStackTrace();        
  		}
    return providers;
  }
  
  private DropdownMenu getActivities(IWContext iwc) {
    DropdownMenu activities = new DropdownMenu(PARAM_ACTIVITY);
    activities.setToSubmit(true);
    activities.addMenuElement("-1", localize(KEY_DROPDOWN_CHOSE, "- Chose -"));
    if (iwc.isParameterSet(PARAM_PROVIDER)) {
      try {
        School school = getSchoolBusiness(iwc).
                                getSchool(new Integer(iwc.getParameter(PARAM_PROVIDER)));
        if (school != null) {
	        Collection schTypes = school.findRelatedSchoolTypes();
	        for (Iterator iter = schTypes.iterator(); iter.hasNext();) {
						SchoolType type = (SchoolType) iter.next();
	          int typeID = ((Integer) type.getPrimaryKey()).intValue();
	          activities.addMenuElement(typeID, type.getName());					
					}
	        if (iwc.isParameterSet(PARAM_ACTIVITY)) {
	          activities.setSelectedElement(iwc.getParameter(PARAM_ACTIVITY));			
			}
        }
	  } catch (Exception e) {
		e.printStackTrace();
	  }
  
    }    
    
    return activities;
  }

  private DropdownMenu getSchoolYears(IWContext iwc) {
    DropdownMenu years = new DropdownMenu(PARAM_SCHOOL_YEAR);
    years.setToSubmit(true);
    years.addMenuElement("-1", localize(KEY_DROPDOWN_CHOSE, "- Chose -"));
    if (iwc.isParameterSet(PARAM_ACTIVITY)) {
      try {
      	if (iwc.isParameterSet(PARAM_PROVIDER)) {
      		String providerIdStr = iwc.getParameter(PARAM_PROVIDER);
      		School school = getSchoolBusiness(iwc).getSchool(new Integer(providerIdStr));
      		if (school != null) {
		        Collection yearColl = school.findRelatedSchoolYears();
				if (yearColl != null) {
					for (Iterator iter = yearColl.iterator(); iter.hasNext();) {
						SchoolYear year = (SchoolYear) iter.next();
			          	int paramTypeID = Integer.parseInt(iwc.getParameter(PARAM_ACTIVITY));
			          	if (year.getSchoolTypeId() == paramTypeID) {
			            	int yearID = ((Integer) year.getPrimaryKey()).intValue();
			            	years.addMenuElement(yearID, year.getName());					
			        	}
					}
			        if (iwc.isParameterSet(PARAM_SCHOOL_YEAR)) {
			          years.setSelectedElement(iwc.getParameter(PARAM_SCHOOL_YEAR));
					}
				}
      		}
		}
	  } catch (Exception e) {
		e.printStackTrace();
	  }
      
        
    }
        
    return years;
  }

  private DropdownMenu getSchoolGroups(IWContext iwc) {
    DropdownMenu groups = new DropdownMenu(PARAM_SCHOOL_GROUP);
    groups.setToSubmit(true);
    groups.addMenuElement("-1", localize(KEY_DROPDOWN_CHOSE, "- Chose -"));   
    if (iwc.isParameterSet(PARAM_PROVIDER) && iwc.isParameterSet(PARAM_SCHOOL_YEAR)) {
      int schoolID = Integer.parseInt(iwc.getParameter(PARAM_PROVIDER));
      int yearID = Integer.parseInt(iwc.getParameter(PARAM_SCHOOL_YEAR));
      try {
        SchoolSeason currentSeason = getSchoolChoiceBusiness(iwc).getCurrentSeason();
        int seasonID = ((Integer) currentSeason.getPrimaryKey()).intValue();
        Collection groupColl = getSchoolBusiness(iwc).
                                 findSchoolClassesBySchoolAndSeasonAndYear(schoolID, seasonID, yearID);
        if (groupColl != null) {
	        for (Iterator iter = groupColl.iterator(); iter.hasNext();) {
						SchoolClass group = (SchoolClass) iter.next();
						int groupID = ((Integer) group.getPrimaryKey()).intValue();
	          groups.addMenuElement(groupID, group.getName());
					}
	        if (iwc.isParameterSet(PARAM_SCHOOL_GROUP))
	          groups.setSelectedElement(iwc.getParameter(PARAM_SCHOOL_GROUP));
       }
     } catch (Exception e) {
        e.printStackTrace();
     }    
    }
    return groups;
  }
  
  private DateInput getPlacementDateInput() {
    DateInput dInput = new DateInput(PARAM_PLACEMENT_DATE);
    IWTimestamp today = IWTimestamp.RightNow();
    today.setAsDate();
    java.sql.Date todayDate = today.getDate();
    dInput.setEarliestPossibleDate(todayDate, 
                                              localize(KEY_ERROR_PAST_TIME, "Can't set date earlier than today"));
    dInput.setToDisplayDayLast(true);
    dInput.setDate(todayDate);
    return dInput;
  }

  public SchoolClassMember getCurrentSchoolClassMembership(User user, IWContext iwc)
                                                                                                          throws RemoteException {
      try {
          final SchoolSeason season
                  = getSchoolChoiceBusiness(iwc).getCurrentSeason();
          int childID = ((Integer) child.getPrimaryKey()).intValue();
          //final SchoolClassMember placement = 
          //    getSchoolBusiness(iwc).getSchoolClassMemberHome().findByUserAndSeason(childID, 2);
          final SchoolClassMember placement =
              getSchoolBusiness(iwc).getSchoolClassMemberHome().findByUserAndSeason(child, season); 
          return (null == placement || null != placement.getRemovedDate ())
                  ? null : placement;
      } catch (final FinderException e) {
          return null;
      }
  }

  /**
   * Parse input request parameters 
   * @param iwc Request object context
   */    
  private void parse(IWContext iwc) {
  	if (iwc.isParameterSet(PARAM_ACTION)) {
		String actionStr = iwc.getParameter(PARAM_ACTION);
  		_action = Integer.parseInt(actionStr);

  	}
	if (iwc.isParameterSet(PARAM_PRESENTATION)) {
		String presStr = iwc.getParameter(PARAM_PRESENTATION);
		_presentation = Integer.parseInt(presStr);
	}  	    
  }
  
  // *** ACTIONS ***
  protected void removeSessionChild(IWContext iwc) {
  	iwc.getSession().removeAttribute(SESSION_KEY_CHILD);
  	child = null;
  }
  
  private CommuneUserBusiness getUserBusiness(IWContext iwc) throws RemoteException {
    return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
  }
  
  private SchoolBusiness getSchoolBusiness(IWContext iwc) throws RemoteException {
    return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
  }
  
  private SchoolChoiceBusiness getSchoolChoiceBusiness(IWContext iwc) throws RemoteException {
    return (SchoolChoiceBusiness) IBOLookup.getServiceInstance(iwc, SchoolChoiceBusiness.class);
  }
  
  private SchoolCommuneBusiness getSchoolCommuneBusiness(IWContext iwc) 
                                                                                                            throws RemoteException {
    return (SchoolCommuneBusiness) 
                          IBOLookup.getServiceInstance(iwc, SchoolCommuneBusiness.class);                                                                                                    
  }
  
  private ChildCareBusinessBean getChildCareBusiness(IWContext iwc) throws RemoteException {
    return (ChildCareBusinessBean) 
                IBOLookup.getServiceInstance(iwc, ChildCareBusinessBean.class);
  }
  
  private ResourceBusiness getResourceBusiness(IWContext iwc) throws RemoteException {
    return (ResourceBusiness) IBOLookup.getServiceInstance(iwc, ResourceBusiness.class);
  }
  
}
