/*
 * Created on 25.3.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosis;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolComparator;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.block.school.data.SchoolSubArea;
import com.idega.business.IBOLookup;
import com.idega.core.location.business.CommuneBusiness;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.ScrollTable;
import com.idega.presentation.ui.SelectDropdownDouble;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class ChildCareStatistics extends ChildCareBlock {

	protected static final String PARAMETER_AREA = "cc_area";
	protected static final String PARAMETER_SUB_AREA = "cc_sub_area";
	protected static final String PARAMETER_ACTION = "cc_action";
	protected static final String PARAMETER_FROM_DATE = "cc_from_date";
	protected static final String PARAMETER_TO_DATE = "cc_to_date";
	protected static final String PARAMETER_SCHOOL_TYPES = "cc_sch_types";
	protected static final String PARAMETER_QUEUE_TYPE = "cc_q_type";

	protected static final int ORDER_BY_ALL_CHOICES = 1;
	protected static final int ORDER_BY_FIRST_HAND_CHOICES = 2;

	protected static final int SCHOOL_TYPES_CHILD_CARE = 1;
	protected static final int SCHOOL_TYPES_PRE_SCHOOL = 2;
	protected static final int SCHOOL_TYPES_FAMILY_DAYCARE = 3;
	protected static final int SCHOOL_TYPES_FAMILY_AFTER_SCHOOL = 4;

	protected static final int QUEUE_TYPE_ALL = 1;
	protected static final int QUEUE_TYPE_NETTO = 2;
	protected static final int QUEUE_TYPE_BRUTTO = 3;

	private int _action = ORDER_BY_ALL_CHOICES;
	private int _areaID = -1;
	private int _subAreaID = -1;
	private int _schoolTypes = SCHOOL_TYPES_CHILD_CARE;
	private int _queueType = QUEUE_TYPE_ALL;
	private Date _fromDate = null;
	private Date _toDate = null;
	
	private boolean _useSorting = false;
    
    private boolean containsSortedByBirthdateProvider = false;     
	
	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		parse(iwc);
		
		add(getNavigationTable(iwc));
		add(new Break());
		
		switch (_action) {
			case ORDER_BY_ALL_CHOICES :
				add(getAllProviderTable(iwc, false));
				break;
			case ORDER_BY_FIRST_HAND_CHOICES :
				add(getAllProviderTable(iwc, true));
				break;
		}
	}
	
	private PresentationObjectContainer getAllProviderTable(IWContext iwc, boolean isFirstHandOnly) throws RemoteException {

		ScrollTable table = new ScrollTable();
        
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(8);
		table.setRowColor(1, getHeaderColor());
		table.setScrollLayerHeaderRowThickness(47);
		table.setWidth(600);
		table.setHeight(400);
		int row = 1;
		int column = 1;

		table.add(getLocalizedSmallHeader("child_care.name","Name"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.not_processed","Order"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.queue_order","Queue order"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.placement_within_3_month","Within months (3)"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.placement_within_12_month","Within months (12)"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.prognosis_3m","Prognosis (3M)"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.prognosis_12m","Prognosis (12M)"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.last_updated","Last updated"), column++, row++);

		Collection schoolTypes = null;
		switch (_schoolTypes) {
			case SCHOOL_TYPES_CHILD_CARE:
				schoolTypes = getBusiness().getSchoolBusiness().findAllSchoolTypesForChildCare();
				break;
			case SCHOOL_TYPES_PRE_SCHOOL:
				schoolTypes = getBusiness().getPreSchoolTypes();
				break;
			case SCHOOL_TYPES_FAMILY_DAYCARE:
				schoolTypes = getBusiness().getFamilyDayCareTypes();
				break;
			case SCHOOL_TYPES_FAMILY_AFTER_SCHOOL:
				schoolTypes = getBusiness().getFamilyAfterSchoolTypes();
				break;
		}
		
		List providers = null;
		if (_areaID == -1 && _subAreaID == -1) {
			providers = new Vector(getBusiness().getSchoolBusiness().findAllSchoolsByType(schoolTypes));
		} else if (_subAreaID == -1) {
			providers = new Vector(getBusiness().getSchoolBusiness().findAllSchoolsByAreaAndTypes(_areaID, schoolTypes));
		} else {
			providers = new Vector(getBusiness().getSchoolBusiness().findAllSchoolsBySubAreaAndTypes(_subAreaID, schoolTypes));
		}

		Date from = null;
		try {
			from = Date.valueOf(iwc.getParameter(PARAMETER_FROM_DATE));
		} catch (Exception e) {}
		
		Date to = null;
		try {
			to = Date.valueOf(iwc.getParameter(PARAMETER_TO_DATE));
		} catch (Exception e) {}
		
		if (providers != null && !providers.isEmpty()) {
			int defaultCommuneId = ((Integer) getCommuneBusiness(iwc).getDefaultCommune().getPrimaryKey()).intValue(); 
			
			School school;
			ChildCarePrognosis prognosis;
			int providerID = -1;
			
			if (_useSorting)
				Collections.sort(providers, new SchoolComparator(iwc.getCurrentLocale()));

			int queueOrderSum = 0;
			int queueTotalSum = 0;
			int queueWithin3MonthsSum = 0;
			int queueWithin12MonthsSum = 0;
			int prognosisThreeMonthsSum = 0;
			int prognosisOneYearSum = 0;
			
			Iterator iter = providers.iterator();
			while (iter.hasNext()) {
				column = 1;
				school = (School) iter.next();
				if (school.getCommuneId() != defaultCommuneId) {
					continue;
				}
				providerID = ((Integer)school.getPrimaryKey()).intValue();
				prognosis = getBusiness().getPrognosis(providerID);
				
				if (row % 2 == 0)
					table.setRowColor(row, getZebraColor1());
				else
					table.setRowColor(row, getZebraColor2());

				int queueOrder = 0;
				int queueTotal = 0;
				int queueWithin3Months = 0;
				int queueWithin12Months = 0;
				
				switch (_queueType) {
					case QUEUE_TYPE_ALL:
						queueOrder = getBusiness().getQueueByProvider(providerID, from, to, isFirstHandOnly);
						queueTotal = getBusiness().getQueueTotalByProvider(providerID, from, to, isFirstHandOnly);
						queueWithin3Months = getBusiness().getQueueTotalByProviderWithinMonths(providerID, 3, isFirstHandOnly);
						queueWithin12Months = getBusiness().getQueueTotalByProviderWithinMonths(providerID, 12, isFirstHandOnly);
						break;
					case QUEUE_TYPE_NETTO:
						queueOrder = getBusiness().getNettoQueueByProvider(providerID, from, to, isFirstHandOnly);
						queueTotal = getBusiness().getNettoQueueTotalByProvider(providerID, from, to, isFirstHandOnly);
						queueWithin3Months = getBusiness().getNettoQueueTotalByProviderWithinMonths(providerID, 3, isFirstHandOnly);
						queueWithin12Months = getBusiness().getNettoQueueTotalByProviderWithinMonths(providerID, 12, isFirstHandOnly);
						break;
					case QUEUE_TYPE_BRUTTO:
						queueOrder = getBusiness().getBruttoQueueByProvider(providerID, from, to, isFirstHandOnly);
						queueTotal = getBusiness().getBruttoQueueTotalByProvider(providerID, from, to, isFirstHandOnly);
						queueWithin3Months = getBusiness().getBruttoQueueTotalByProviderWithinMonths(providerID, 3, isFirstHandOnly);
						queueWithin12Months = getBusiness().getBruttoQueueTotalByProviderWithinMonths(providerID, 12, isFirstHandOnly);
						break;						
				}
				queueOrderSum += queueOrder;
				queueTotalSum += queueTotal;
				queueWithin3MonthsSum += queueWithin3Months;
				queueWithin12MonthsSum += queueWithin12Months;
				
                table.add(getProviderName(school), column++, row);                
				table.add(getSmallText(String.valueOf(queueOrder)), column++, row);
				table.add(getSmallText(String.valueOf(queueTotal)), column++, row);
				table.add(getSmallText(String.valueOf(queueWithin3Months)), column++, row);
				table.add(getSmallText(String.valueOf(queueWithin12Months)), column++, row);
				if (prognosis != null) {
					int prognosisThreeMonths = prognosis.getThreeMonthsPrognosis();
					int prognosisOneYear = prognosis.getOneYearPrognosis();
					table.add(getSmallText(String.valueOf(prognosisThreeMonths)), column++, row);
					table.add(getSmallText(String.valueOf(prognosisOneYear)), column++, row);
					table.add(getSmallText(new IWTimestamp(prognosis.getUpdatedDate()).getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row++);
					prognosisThreeMonthsSum += prognosisThreeMonths;
					prognosisOneYearSum += prognosisOneYear;
				}
				else {
					table.add(getSmallText("-"), column++, row);
					table.add(getSmallText("-"), column++, row);
					table.add(getSmallText("-"), column++, row++);
				}
			}

			column = 1;
		
			table.add(getLocalizedSmallHeader("child_care.sum","Sum"), column++, row);
			table.add(getSmallHeader(String.valueOf(queueOrderSum)), column++, row);
			table.add(getSmallHeader(String.valueOf(queueTotalSum)), column++, row);
			table.add(getSmallHeader(String.valueOf(queueWithin3MonthsSum)), column++, row);
			table.add(getSmallHeader(String.valueOf(queueWithin12MonthsSum)), column++, row);
			table.add(getSmallHeader(String.valueOf(prognosisThreeMonthsSum)), column++, row);
			table.add(getSmallHeader(String.valueOf(prognosisOneYearSum)), column++, row);		
			table.add(getSmallHeader("-"), column++, row++);		
		}
		
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(6, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(7, Table.HORIZONTAL_ALIGN_CENTER);
		
        PresentationObjectContainer container = new PresentationObjectContainer();        
        if (this.containsSortedByBirthdateProvider) {
            container.add(getSortedByBirthdateExplanation());
        }
        container.add(table);
		return container;
	}
	
	private Form getNavigationTable(IWContext iwc) throws RemoteException {
		Form form = new Form();
		
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		
		IWTimestamp stamp = new IWTimestamp();

		table.add(getSmallHeader(localize("child_care.from", "From")), 1, 1);
		DateInput from = new DateInput(PARAMETER_FROM_DATE);
		from.setToDisplayDayLast(true);
		from = (DateInput) getStyledInterface(from);
		if (_fromDate != null) {
			from.setDate(_fromDate);
		}
		from.setYearRange(stamp.getYear() - 2, stamp.getYear() + 5);
		table.add(from, 2, 1);
		
		table.add(getSmallHeader(localize("child_care.to", "To")), 3, 1);
		DateInput to = new DateInput(PARAMETER_TO_DATE);
		to.setToDisplayDayLast(true);
		to = (DateInput) getStyledInterface(to);		
		if (_toDate != null) {
			to.setDate(_toDate);
		}
		to.setYearRange(stamp.getYear() - 2, stamp.getYear() + 5);
		table.add(to, 4, 1);
		form.add(table);
		form.add(Text.getBreak());
		
		table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_ACTION));
		menu.addMenuElement(ORDER_BY_ALL_CHOICES, localize("child_care.show_provider_statistics","Show by area"));
		menu.addMenuElement(ORDER_BY_FIRST_HAND_CHOICES, localize("child_care.show_first_hand_statistics","Show by first hand choices"));
		menu.setSelectedElement(_action);
		table.add(menu, 1, 1);
		

		SelectDropdownDouble areas = new SelectDropdownDouble(PARAMETER_AREA, PARAMETER_SUB_AREA);
		areas.setLayoutVertical(true);
		areas.setVerticalSpaceBetween(7);
		areas = (SelectDropdownDouble) getStyledInterface(areas);	

		Iterator iter = getSchoolAreas(iwc).iterator();
		areas.addMenuElement("-1", localize("child_care.all_areas","All areas"), new HashMap());
		while (iter.hasNext()) {
			SchoolArea area = (SchoolArea) iter.next();
			areas.addMenuElement(area.getPrimaryKey().toString(), area.getName(), getSchoolSubAreas(iwc, area.getPrimaryKey().toString()));
		}
		
		
		areas.setSelectedValues(iwc.getParameter(PARAMETER_AREA),iwc.getParameter(PARAMETER_SUB_AREA));

		table.mergeCells(2, 1, 2, 2);
		table.add(areas, 2, 1);
				
		
		DropdownMenu schoolTypes = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_SCHOOL_TYPES));
		schoolTypes.addMenuElement(SCHOOL_TYPES_CHILD_CARE, localize("child_care.all_operations", "All operations"));
		schoolTypes.addMenuElement(SCHOOL_TYPES_PRE_SCHOOL, localize("child_care.pre_schools","Pre-schools"));
		schoolTypes.addMenuElement(SCHOOL_TYPES_FAMILY_DAYCARE, localize("child_care.family_daycare","Family daycare"));
		if (!getBusiness().getFamilyAfterSchoolTypes().isEmpty()) {
			schoolTypes.addMenuElement(SCHOOL_TYPES_FAMILY_AFTER_SCHOOL, localize("child_care.family_after_school","Family after school"));
		}
		schoolTypes.setSelectedElement(_schoolTypes);
		table.add(schoolTypes, 1, 2);
		
		DropdownMenu queueType = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_QUEUE_TYPE));
		queueType.addMenuElement(QUEUE_TYPE_ALL, localize("child_care.all_in_queue", "All in queue"));
		queueType.addMenuElement(QUEUE_TYPE_NETTO, localize("child_care.netto_queue","Netto queue"));
		queueType.addMenuElement(QUEUE_TYPE_BRUTTO, localize("child_care.brutto_queue","Brutto queue"));
		queueType.setSelectedElement(_queueType);
		table.add(queueType, 3, 1);
		
		SubmitButton submit = (SubmitButton) getButton(new SubmitButton(localize("child_care.get", "Get")));
		table.add(submit, 4, 1);
		
		form.add(table);
				
		return form;
	}
	
	private Collection getSchoolAreas(IWContext iwc) {
		Collection c = null;
		try {
			c = getSchoolBusiness(iwc).findAllSchoolAreas();
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e));
		}
		return c;
	}
		
	private Map getSchoolSubAreas(IWContext iwc, String schoolArea) {
		Collection c = null;
		Map m = new TreeMap();
		try {
			c = getSchoolBusiness(iwc).findAllSchoolSubAreasByArea(schoolArea);
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e));
		}
		Iterator i = c.iterator();
		m.put("-1", localize("child_care.all_sub_areas","All sub areas"));
		while (i.hasNext()){
			SchoolSubArea a = (SchoolSubArea) i.next();
			m.put(a.getPrimaryKey().toString(), a.getName());
		}

		return m;
	}	
	
	/*
	 * Returns a school business object
	 */
	private SchoolBusiness getSchoolBusiness(IWContext iwc) {
		SchoolBusiness sb = null;
		try {
			sb = (SchoolBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
		} catch (RemoteException e) {
			add(new ExceptionWrapper(e));
		}
		return sb;
	}	
	
	private void parse(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION))
			_action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		if (iwc.isParameterSet(PARAMETER_AREA))
			_areaID = Integer.parseInt(iwc.getParameter(PARAMETER_AREA));
		if (iwc.isParameterSet(PARAMETER_SUB_AREA))
			_subAreaID = Integer.parseInt(iwc.getParameter(PARAMETER_SUB_AREA));
		if (iwc.isParameterSet(PARAMETER_SCHOOL_TYPES))
			_schoolTypes = Integer.parseInt(iwc.getParameter(PARAMETER_SCHOOL_TYPES));
		if (iwc.isParameterSet(PARAMETER_QUEUE_TYPE))
			_queueType = Integer.parseInt(iwc.getParameter(PARAMETER_QUEUE_TYPE));
		if (iwc.isParameterSet(PARAMETER_FROM_DATE)) {
			String s = iwc.getParameter(PARAMETER_FROM_DATE);
			if (s.length() > 0) {
				_fromDate = Date.valueOf(s);
			}
		}
		if (iwc.isParameterSet(PARAMETER_TO_DATE)) {
			String s = iwc.getParameter(PARAMETER_TO_DATE);
			if (s.length() > 0) {
				_toDate = Date.valueOf(s);
			}
		}
	}
	
	/**
	 * @param useSorting
	 */
	public void setUseSorting(boolean useSorting) {
		_useSorting = useSorting;
	}
	
	protected CommuneBusiness getCommuneBusiness(IWApplicationContext iwac) throws RemoteException {
		return (CommuneBusiness) IBOLookup.getServiceInstance(iwac, CommuneBusiness.class);
	}
    
    private PresentationObjectContainer getProviderName(School provider) {
        PresentationObjectContainer nameContainer = new PresentationObjectContainer();        
        if (provider.getSortByBirthdate()) {            
            Text star = new Text("* ");
            star.setStyleClass("commune_" + CommuneBlock.STYLENAME_SMALL_EXPLANATION_STAR_TEXT);            
            nameContainer.add(star);            
            
            this.containsSortedByBirthdateProvider = true;
        }  
        nameContainer.add(getSmallText(provider.getSchoolName()));        
        return nameContainer;
    }    
}