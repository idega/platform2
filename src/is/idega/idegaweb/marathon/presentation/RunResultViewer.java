/*
 * Created on Jul 8, 2004
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Run;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.rmi.RemoteException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

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
	private static final String DEFAULT_GROUP_ROW_STYLE = "font-family:Arial,Helvetica,sans-serif;font-size:10px;font-weight:bold;padding:4px;background-color:#ACACAC";
	private static final String DEFAULT_HEADER_ROW_STYLE = "font-family:Arial,Helvetica,sans-serif;font-size:12px;font-weight:bold;color:#FFFFFF;padding:2px;";
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

	private Map _runToRunnerMap = null;
	private Collator _collator;
	private IWContext _iwc;
	private IWResourceBundle iwrb;

	private RunBusiness _runBiz;
	private GroupBusiness _groupBiz;

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

		Form form = new Form();
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setColumns(COLUMN_COUNT);
		table.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;
		
		table.setCellpaddingLeft(1, row, 16);
		table.mergeCells(1, row, table.getColumns(), row);
		
		table.add(getYearsDropdown(), 1, row);
		table.add(getDistanceDropdown(), 1, row);
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
						
						break;
				}
		}
		
		for (int a = 2; a < COLUMN_COUNT; a = a + 2) {
			table.setWidth(a, 2);
			table.setColumnColor(a, "#FFFFFF");
		}
		
		form.add(table);
		add(form);
	}
	
	private void getTotalResults(Table table, int row) {
		try {
			List runGroups = new ArrayList(getGroupBiz().getChildGroups(distance));
			List runs = new ArrayList();
			Iterator runGroupIter = runGroups.iterator();
			
			while (runGroupIter.hasNext()) {
				Group runGroup = (Group) runGroupIter.next();
				List runners = new ArrayList(getGroupBiz().getUsers(runGroup));
				runs.addAll(getRunsForRunners(runners));
			}

			row = insertRunGroupIntoTable(table, row, "results.all_participants");

			sortRuns(runs);
			Iterator runIter = runs.iterator();
			int num = 1;
			while (runIter.hasNext()) {
				Run run = (Run) runIter.next();
				row = insertRunIntoTable(table, row, run, num);
				num++;
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
				row = insertRunGroupIntoTable(table, row, "group_" + runGroup.getName());
	
				List runners = new ArrayList(getGroupBiz().getUsers(runGroup));
				List runs = getRunsForRunners(runners);
				sortRuns(runs);
				Iterator runIter = runs.iterator();
				int num = 1;
				while (runIter.hasNext()) {
					Run run = (Run) runIter.next();
					row = insertRunIntoTable(table, row, run, num);
					num++;
					
					if (!runIter.hasNext()) {
						table.setHeight(row++, 2);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private DropdownMenu getYearsDropdown() throws RemoteException {
		DropdownMenu years = (DropdownMenu) util.getSelectorFromIDOEntities(new DropdownMenu(IWMarathonConstants.GROUP_TYPE_RUN_YEAR), getRunBiz().getYears(run), "getName", iwrb);
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
		Integer sevenKM = new Integer(iwrb.getIWBundleParent().getProperty("7_km_id", "113"));

		if (year != null) {
			List distances = getRunBiz().getDistancesMap(run, year.getName());
			Iterator iter = distances.iterator();
			while (iter.hasNext()) {
				Group distance = (Group) iter.next();
				if (!distance.getPrimaryKey().equals(threeKM) && !distance.getPrimaryKey().equals(sevenKM)) {
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
	 * Gets the Run objects for a collection of runners, also creates the
	 * <code>_runToRunnerMap</code> map.
	 * 
	 * @param runners
	 *            A Collection of runners
	 * @return A Collection of Runs
	 */
	private List getRunsForRunners(List runners) {
		List runs = new ArrayList(runners.size());
		if (_runToRunnerMap == null) {
			_runToRunnerMap = new HashMap();
		}

		Iterator runnerIter = runners.iterator();
		while (runnerIter.hasNext()) {
			User runner = (User) runnerIter.next();
			Run run = getRunBiz().getRunObjByUserIDandDistanceID(((Integer) runner.getPrimaryKey()).intValue(), ((Integer) distance.getPrimaryKey()).intValue()); // @TODO
			if (run != null) {
				_runToRunnerMap.put(run, runner);
				runs.add(run);
			}
		}

		return runs;
	}

	/**
	 * Sorts runs by their time
	 * 
	 * @param runs
	 *            The list of Run objects to sort
	 */
	private void sortRuns(List runs) {
		Collections.sort(runs, new Comparator() {

			public int compare(Object arg0, Object arg1) {
				Run r0 = (Run) arg0;
				Run r1 = (Run) arg1;
				return r1.getRunTime() - r0.getRunTime();
			}
		});
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

	private int insertRunIntoTable(Table table, int row, Run run, int num) {
		User user = (User) _runToRunnerMap.get(run);
		table.add(getRunnerRowText(Integer.toString(num)), 1, row);
		table.setStyleClass(1, row, getStyleName(STYLENAME_LIST_ROW));
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_CENTER);

		String runTime = getTimeStringFromMillis(run.getRunTime());
		table.add(getRunnerRowText(runTime), 3, row);
		table.setStyleClass(3, row, getStyleName(STYLENAME_LIST_ROW));
		table.setAlignment(3, row, Table.HORIZONTAL_ALIGN_CENTER);

		String chipTime = getTimeStringFromMillis(run.getChipTime());
		table.add(getRunnerRowText(chipTime), 5, row);
		table.setStyleClass(5, row, getStyleName(STYLENAME_LIST_ROW));
		table.setAlignment(5, row, Table.HORIZONTAL_ALIGN_CENTER);

		table.add(getRunnerRowText(user.getName()), 7, row);
		table.setStyleClass(7, row, getStyleName(STYLENAME_LIST_ROW));

		table.add(getRunnerRowText(Integer.toString(user.getDateOfBirth().getYear())), 9, row);
		table.setStyleClass(9, row, getStyleName(STYLENAME_LIST_ROW));
		table.setAlignment(9, row, Table.HORIZONTAL_ALIGN_CENTER);

		table.add(getRunnerRowText(run.getUserNationality()), 11, row);
		table.setStyleClass(11, row, getStyleName(STYLENAME_LIST_ROW));
		table.setAlignment(11, row, Table.HORIZONTAL_ALIGN_CENTER);

		table.add(getRunnerRowText(run.getRunGroupName()), 13, row);
		table.setStyleClass(13, row, getStyleName(STYLENAME_LIST_ROW));
		
		if (num % 2 == 0) {
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
		table.setStyleClass(7, row, getStyleName(STYLENAME_LIST_ROW));

		table.add(getRunnerRowText(iwrb.getLocalizedString("results.birth_year", "Year")), 9, row);
		table.setStyleClass(9, row, getStyleName(STYLENAME_HEADER_ROW));
		table.setAlignment(9, row, Table.HORIZONTAL_ALIGN_CENTER);

		table.add(getRunnerRowText(iwrb.getLocalizedString("results.country", "Country")), 11, row);
		table.setStyleClass(11, row, getStyleName(STYLENAME_HEADER_ROW));
		table.setAlignment(11, row, Table.HORIZONTAL_ALIGN_CENTER);

		table.add(getRunnerRowText(iwrb.getLocalizedString("results.group", "Group")), 13, row);
		table.setStyleClass(13, row, getStyleName(STYLENAME_LIST_ROW));
		table.setHeight(++row, 2);
		
		return ++row;
	}

	private String getTimeStringFromMillis(int millis) {
		int seks = millis / 1000;
		int mins = millis / 60000;
		float fr = ((float) millis % 1000) / 10.0f;
		String strFr = Integer.toString(Math.round(fr));
		strFr = strFr.length() == 1 ? "0" + strFr : strFr;
		String strSecs = Integer.toString(seks - mins * 60);
		strSecs = strSecs.length() == 1 ? "0" + strSecs : strSecs;
		String strMins = Integer.toString(mins);
		strMins = strMins.length() == 1 ? "0" + strMins : strMins;
		return strMins + ":" + strSecs + ":" + strFr;
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