/*
 * Created on Jul 8, 2004
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.business.RunGroup;
import is.idega.idegaweb.marathon.business.RunGroupComparator;
import is.idega.idegaweb.marathon.business.RunGroupMap;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.rmi.RemoteException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.location.data.Country;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.Counter;
import com.idega.util.IWTimestamp;

/**
 * Show the results of a run. This block can show results for a specific run,
 * year and run group
 * 
 * @author birna
 */
public class RunResultViewer extends Block {

	private static final String STYLENAME_INTERFACE = "interface";
	private static final String STYLENAME_GROUP_ROW = "groupRow";
	private static final String STYLENAME_HEADER_ROW = "headerRow";
	private static final String STYLENAME_LIST_ROW = "listRow";

	private static final String DEFAULT_INTERFACE_STYLE = "font-family:Verdana,Arial,Helvetica,sans-serif;font-size:8px;font-weight:bold;border-width:1px;border-color:#000000;border-style:solid;";
	private static final String DEFAULT_GROUP_ROW_STYLE = "font-family:Arial,Helvetica,sans-serif;font-size:10px;font-weight:bold;padding:4px;background-color:#ACACAC;color:#FFFFFF";
	private static final String DEFAULT_HEADER_ROW_STYLE = "font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;padding:2px;";
	private static final String DEFAULT_LIST_ROW_STYLE = "font-family:Arial,Helvetica,sans-serif;font-size:10px;padding:2px;";
	
	private static final String HEADLINE_BACKGROUND_COLOR = "#ACACAC";
	private static final String HEADLINE_COLOR = "FFFFFF";
	private static final String DARK_COLOR = "#E9E9E9";
	private static final String LIGHT_COLOR = "#FFFFFF";

	private static final int COLUMN_COUNT = 13;
	private static final int HEADLINE_SIZE = 12;

	private static String _groupYear;
	private static String _groupDistance;

	private Group distance;
	private Group year;

	private Collator _collator;
	private IWContext _iwc;
	private IWResourceBundle iwrb;

	private RunBusiness _runBiz;
	private GroupBusiness _groupBiz;
	private UserBusiness _userBiz;

	private String runPK;
	private Group run;
	private int sortBy = IWMarathonConstants.RYSDD_TOTAL;

	private SelectorUtility util;

	public void main(IWContext iwc) throws Exception {
		_iwc = iwc;
		iwrb = getResourceBundle(iwc);
		_collator = Collator.getInstance(iwc.getCurrentLocale());
		util = new SelectorUtility();

		if (runPK != null) {
			run = getGroupBiz().getGroupByGroupID(Integer.parseInt(runPK));
		}

		if (run == null) {
			add("No run set...");
			return;
		}

		if (iwc.isParameterSet(IWMarathonConstants.PARAMETER_SORT_BY)) {
			sortBy = Integer.parseInt(iwc.getParameter(IWMarathonConstants.PARAMETER_SORT_BY));
		}
		
		if (iwc.isParameterSet(IWMarathonConstants.GROUP_TYPE_RUN_YEAR)) {
			try {
				year = getGroupBiz().getGroupByGroupID(Integer.parseInt(iwc.getParameter(IWMarathonConstants.GROUP_TYPE_RUN_YEAR)));
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		}

		if (iwc.isParameterSet(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE)) {
			try {
				distance = getGroupBiz().getGroupByGroupID(Integer.parseInt(iwc.getParameter(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE)));
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		}

		Form form = null;//(Form) iwc.getApplicationAttribute("run_result_cache_" + runPK + "_" + (year != null ? year.getPrimaryKey() + "_" : "") + (distance != null ? distance.getPrimaryKey() + "_" : "") + sortBy);
		
		if (form == null) {
			form = new Form();
			Table table = new Table();
			table.setCellpadding(0);
			table.setCellspacing(0);
			table.setColumns(COLUMN_COUNT);
			table.setWidth(Table.HUNDRED_PERCENT);
			int row = 1;
			
			table.setCellpaddingLeft(1, row, 16);
			table.mergeCells(1, row, table.getColumns(), row);
			
			table.add(getYearsDropdown(run), 1, row);
			table.add(Text.getNonBrakingSpace(), 1, row);
			table.add(getDistanceDropdown(), 1, row);
			table.add(Text.getNonBrakingSpace(), 1, row);
			table.add(getSortDropdown(), 1, row++);
			table.setHeight(row++, 12);
	
			if (distance != null) {
					row = insertHeadersIntoTable(table, row);
				
					switch (sortBy) {
						case IWMarathonConstants.RYSDD_TOTAL:
							getTotalResults(table, row);
							break;
						case IWMarathonConstants.RYSDD_GROUPS:
							getGroupResults(table, row);
							break;
						case IWMarathonConstants.RYSDD_GROUPS_COMP:
							getGroupCompetitionResults(table, row);
							break;
					}
			}
			
			for (int a = 2; a < COLUMN_COUNT; a = a + 2) {
				table.setWidth(a, 2);
				table.setColumnColor(a, "#FFFFFF");
			}
			
			form.add(table);
			//iwc.setApplicationAttribute("run_result_cache_" + runPK + "_" + (year != null ? year.getPrimaryKey() + "_" : "") + (distance != null ? distance.getPrimaryKey() + "_" : "") + sortBy, form);
		}
		add(form);
	}
	
	private void getTotalResults(Table table, int row) {
		try {
			Collection runs = getRunBiz().getRunnersByDistance(distance, null);
			row = insertRunGroupIntoTable(table, row, "results.all_participants");

			Iterator runIter = runs.iterator();
			int num = 1;
			while (runIter.hasNext()) {
				Participant run = (Participant) runIter.next();
				if (run.getRunTime() != -1) {
					row = insertRunIntoTable(table, row, run, num, num);
					num++;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getGroupResults(Table table, int row) {
		try {
			List runGroups = new ArrayList(getGroupBiz().getChildGroups(distance));
			sortRunnerGroups(runGroups);
			Iterator runGroupIter = runGroups.iterator();
			
			while (runGroupIter.hasNext()) {
				Group runGroup = (Group) runGroupIter.next();
	
				Collection runners = getRunBiz().getRunnersByDistance(distance, runGroup);
				if (runners.size() > 0) {
					row = insertRunGroupIntoTable(table, row, "group_" + runGroup.getName());
					Iterator runIter = runners.iterator();
					int num = 1;
					while (runIter.hasNext()) {
						Participant run = (Participant) runIter.next();
						row = insertRunIntoTable(table, row, run, num, num);
						num++;
						
						if (!runIter.hasNext()) {
							table.setHeight(row++, 2);
						}
					}
					}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getGroupCompetitionResults(Table table, int row) {
		try {
			System.out.println("1. Getting all runners");
			List runs = new ArrayList(getRunBiz().getRunnersByDistance(distance, null));
			
			Map runGroups = new HashMap();
			RunGroupMap map = new RunGroupMap();
			
			System.out.println("2. Sorting out group runners");
			Participant runner;
			RunGroup runnerGroup;
			Iterator iterator = runs.iterator();
			while (iterator.hasNext()) {
				runner = (Participant) iterator.next();
				if (runner.getRunGroupName() != null && runner.getRunGroupName().trim().length() > 0) {
					runnerGroup = (RunGroup) runGroups.get(runner.getRunGroupName());
					if (runnerGroup == null) {
						runnerGroup = new RunGroup(runner.getRunGroupName());
						runGroups.put(runner.getRunGroupName(), runnerGroup);
					}
					
					System.out.println("- Adding group runners to map");
					map.put(runnerGroup, runner);
				}
			}

			List groupList = new ArrayList(map.keySet());
			Collections.sort(groupList, new RunGroupComparator(map));
			System.out.println("4. Sorting run groups");

			iterator = groupList.iterator();
			Participant run;
			int num = 1;
			int count = 0;
			Iterator runIter;
			Collection runnersInRunGroup;
			RunGroup runGroup;
			while (iterator.hasNext()) {
				runGroup = (RunGroup) iterator.next();
				runnersInRunGroup = map.getCollection(runGroup);
				if (runnersInRunGroup.size() == 3) {
					row = insertRunGroupIntoTable(table, row, runGroup.getGroupName() + " - " + runGroup.getCounter().toString());
					System.out.println("- Inserting group to table: " + runGroup.getGroupName());
	
					runIter = runnersInRunGroup.iterator();
					num = 1;
					count = 0;
					while (runIter.hasNext()) {
						run = (Participant) runIter.next();
						num = runs.indexOf(run);
						row = insertRunIntoTable(table, row, run, num, count+1);
						count++;
					}
					System.out.println("- Inserting runners from group to table");
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private DropdownMenu getYearsDropdown(Group run) throws RemoteException {
		DropdownMenu years = (DropdownMenu) util.getSelectorFromIDOEntities(new DropdownMenu(IWMarathonConstants.GROUP_TYPE_RUN_YEAR), run.getChildren(), "getName", iwrb);
		years.addMenuElementFirst("", "");
		years.setToSubmit();
		years.keepStatusOnAction();
		return years;
	}

	private DropdownMenu getDistanceDropdown() throws RemoteException {
		DropdownMenu distanceMenu = new DropdownMenu(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE);
		distanceMenu.setToSubmit();
		distanceMenu.keepStatusOnAction();
		distanceMenu.addMenuElementFirst("", "");

		Integer threeKM = new Integer(iwrb.getIWBundleParent().getProperty("3_km_id", "126"));
		//Integer sevenKM = new Integer(iwrb.getIWBundleParent().getProperty("7_km_id", "113"));

		if (year != null) {
			List distances = getRunBiz().getDistancesMap(run, year.getName());
			Iterator iter = distances.iterator();
			while (iter.hasNext()) {
				Group distance = (Group) iter.next();
				if (!distance.getPrimaryKey().equals(threeKM)/* && !distance.getPrimaryKey().equals(sevenKM)*/) {
					distanceMenu.addMenuElement(distance.getPrimaryKey().toString(), iwrb.getLocalizedString(distance.getName(), distance.getName()));
				}
			}
		}

		return distanceMenu;
	}

	private DropdownMenu getSortDropdown() throws RemoteException {
		DropdownMenu sort = new DropdownMenu(IWMarathonConstants.PARAMETER_SORT_BY);
		sort.addMenuElement(IWMarathonConstants.RYSDD_TOTAL, iwrb.getLocalizedString(IWMarathonConstants.PARAMETER_TOTAL, "Total result list"));
		sort.addMenuElement(IWMarathonConstants.RYSDD_GROUPS, iwrb.getLocalizedString(IWMarathonConstants.PARAMETER_GROUPS, "Groups"));
		sort.addMenuElement(IWMarathonConstants.RYSDD_GROUPS_COMP, iwrb.getLocalizedString(IWMarathonConstants.PARAMETER_GROUPS_COMPETITION, "Group competition"));
		sort.setToSubmit();
		sort.keepStatusOnAction();

		return sort;
	}

	/**
	 * Sorts groups based on their names
	 * 
	 * @param groups
	 *            The list of groups to sort
	 */
	private void sortRunnerGroups(List groups) {
		Collections.sort(groups, new Comparator() {

			public int compare(Object arg0, Object arg1) {
				Group g0 = (Group) arg0;
				Group g1 = (Group) arg1;
				return _collator.compare(g0.getName(), g1.getName());
			}
		});
	}

	private int insertRunIntoTable(Table table, int row, Participant run, int num, int participantRow) {
		table.add(getRunnerRowText(Integer.toString(num)), 1, row);
		table.setStyleClass(1, row, getStyleName(STYLENAME_LIST_ROW));
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_CENTER);

		String runTime = getTimeStringFromRunTime(run.getRunTime());
		table.add(getRunnerRowText(runTime), 3, row);
		table.setStyleClass(3, row, getStyleName(STYLENAME_LIST_ROW));
		table.setAlignment(3, row, Table.HORIZONTAL_ALIGN_CENTER);

		String chipTime = run.getChipTime() != -1 ? getTimeStringFromRunTime(run.getChipTime()) : "-";
		table.add(getRunnerRowText(chipTime), 5, row);
		table.setStyleClass(5, row, getStyleName(STYLENAME_LIST_ROW));
		table.setAlignment(5, row, Table.HORIZONTAL_ALIGN_CENTER);

		try {
			User user = getUserBiz().getUser(run.getUserID());
			table.add(getRunnerRowText(user.getName()), 7, row);
			table.setStyleClass(7, row, getStyleName(STYLENAME_LIST_ROW));
	
			IWTimestamp birthDate = new IWTimestamp(user.getDateOfBirth());
			table.add(getRunnerRowText(Integer.toString(birthDate.getYear())), 9, row);
			table.setStyleClass(9, row, getStyleName(STYLENAME_LIST_ROW));
			table.setAlignment(9, row, Table.HORIZONTAL_ALIGN_CENTER);
		}
		catch (RemoteException re) {
			log(re);
		}
		
		Country country = null;
		try {
			country = getRunBiz().getCountryByNationality(run.getUserNationality());
		}
		catch (RemoteException re) {
			log(re);
		}
		if (country != null) {
			table.add(getRunnerRowText(country.getName()), 11, row);
		}
		else {
			table.add(getRunnerRowText(run.getUserNationality()), 11, row);
		}
		table.setStyleClass(11, row, getStyleName(STYLENAME_LIST_ROW));
		table.setAlignment(11, row, Table.HORIZONTAL_ALIGN_CENTER);

		table.add(getRunnerRowText(run.getRunGroupName() != null ? run.getRunGroupName() : ""), 13, row);
		table.setStyleClass(13, row, getStyleName(STYLENAME_LIST_ROW));
		
		if (participantRow % 2 == 0) {
			table.setRowColor(row, LIGHT_COLOR);
		}
		else {
			table.setRowColor(row, DARK_COLOR);
		}
		
		return ++row;
	}

	private int insertHeadersIntoTable(Table table, int row) {
		table.add(getRunnerRowText(iwrb.getLocalizedString("results.number", "Nr.")), 1, row);
		table.setStyleClass(1, row, getStyleName(STYLENAME_HEADER_ROW));
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_CENTER);

		table.add(getRunnerRowText(iwrb.getLocalizedString("results.run_time", "Run time")), 3, row);
		table.setStyleClass(3, row, getStyleName(STYLENAME_HEADER_ROW));
		table.setAlignment(3, row, Table.HORIZONTAL_ALIGN_CENTER);

		table.add(getRunnerRowText(iwrb.getLocalizedString("results.chip_time", "Chip time")), 5, row);
		table.setStyleClass(5, row, getStyleName(STYLENAME_HEADER_ROW));
		table.setAlignment(5, row, Table.HORIZONTAL_ALIGN_CENTER);

		table.add(getRunnerRowText(iwrb.getLocalizedString("results.name", "Name")), 7, row);
		table.setStyleClass(7, row, getStyleName(STYLENAME_HEADER_ROW));

		table.add(getRunnerRowText(iwrb.getLocalizedString("results.birth_year", "Year")), 9, row);
		table.setStyleClass(9, row, getStyleName(STYLENAME_HEADER_ROW));
		table.setAlignment(9, row, Table.HORIZONTAL_ALIGN_CENTER);

		table.add(getRunnerRowText(iwrb.getLocalizedString("results.country", "Country")), 11, row);
		table.setStyleClass(11, row, getStyleName(STYLENAME_HEADER_ROW));
		table.setAlignment(11, row, Table.HORIZONTAL_ALIGN_CENTER);

		table.add(getRunnerRowText(iwrb.getLocalizedString("results.group", "Group")), 13, row);
		table.setStyleClass(13, row, getStyleName(STYLENAME_HEADER_ROW));
		table.setHeight(++row, 2);
		
		return ++row;
	}

	private String getTimeStringFromRunTime(int seconds) {
		Counter counter = new Counter();
		counter.addSeconds(seconds);
		return counter.toString();
	}

	private Text getRunnerRowText(String text) {
		Text t = new Text(text);
		return t;
	}

	private int insertRunGroupIntoTable(Table table, int row, String groupName) {
		table.mergeCells(1, row, COLUMN_COUNT, row);
		table.setStyleClass(1, row, getStyleName(STYLENAME_GROUP_ROW));
		table.add(getRunnerRowText(iwrb.getLocalizedString(groupName, groupName)), 1, row++);
		table.setHeight(row, 2);
		return ++row;
	}

	private RunBusiness getRunBiz() {
		if (_runBiz == null) {
			try {
				_runBiz = (RunBusiness) IBOLookup.getServiceInstance(_iwc, RunBusiness.class);
			}
			catch (IBOLookupException e) {
				e.printStackTrace();
			}
		}
		return _runBiz;
	}

	private GroupBusiness getGroupBiz() {
		if (_groupBiz == null) {
			try {
				_groupBiz = (GroupBusiness) IBOLookup.getServiceInstance(_iwc, GroupBusiness.class);
			}
			catch (IBOLookupException e) {
				e.printStackTrace();
			}
		}
		return _groupBiz;
	}

	private UserBusiness getUserBiz() {
		if (_userBiz == null) {
			try {
				_userBiz = (UserBusiness) IBOLookup.getServiceInstance(_iwc, UserBusiness.class);
			}
			catch (IBOLookupException e) {
				e.printStackTrace();
			}
		}
		return _userBiz;
	}

	public String getBundleIdentifier() {
		return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
	}

	/**
	 * @param run
	 *            The run to set.
	 */
	public void setRun(String runPK) {
		this.runPK = runPK;
	}
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.Block#getStyleNames()
	 */
	public Map getStyleNames() {
		Map map = new HashMap();
		map.put(STYLENAME_GROUP_ROW, DEFAULT_GROUP_ROW_STYLE);
		map.put(STYLENAME_HEADER_ROW, DEFAULT_HEADER_ROW_STYLE);
		map.put(STYLENAME_INTERFACE, DEFAULT_INTERFACE_STYLE);
		map.put(STYLENAME_LIST_ROW, DEFAULT_LIST_ROW_STYLE);
		
		return map;
	}
}