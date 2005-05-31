/*
 * $Id: GroupRegistration.java,v 1.1 2005/05/31 19:04:35 laddi Exp $
 * Created on May 30, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.data.Run;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.FinderException;
import com.idega.business.IBORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;


/**
 * Last modified: $Date: 2005/05/31 19:04:35 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class GroupRegistration extends RunBlock {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final String PARAMETER_RUN = "prm_run";
	private static final String PARAMETER_DISTANCE = "prm_distance";
	private static final String PARAMETER_GROUP_NAME = "prm_group_name";
	private static final String PARAMETER_PARTICIPANT_NUMBER = "prm_participant_number";
	private static final String PARAMETER_BEST_TIME = "prm_best_time";
	private static final String PARAMETER_ESTIMATED_TIME = "prm_estimated_time";
	private static final String PARAMETER_PARTICIPANT = "prm_participant";

	private static final int ACTION_STEP_ONE = 1;
	private static final int ACTION_STEP_TWO = 2;
	private static final int ACTION_STEP_THREE = 3;
	private static final int ACTION_SAVE = 4;
	
	private Run run;
	private Distance distance;
	private String groupName;
	private String[] participants;
	private String[] bestTimes;
	private String[] estimatedTimes;
	private Map participantMap;

	public void main(IWContext iwc) throws Exception {
		switch (parseAction(iwc)) {
			case ACTION_STEP_ONE:
				stepOne(iwc);
				break;
			case ACTION_STEP_TWO:
				stepTwo(iwc);
				break;
			case ACTION_STEP_THREE:
				stepThree(iwc);
				break;
			case ACTION_SAVE:
				save(iwc);
				break;
		}
	}
	
	private void stepOne(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, ACTION_STEP_TWO);
		form.maintainParameter(PARAMETER_PARTICIPANT_NUMBER);
		form.maintainParameter(PARAMETER_BEST_TIME);
		form.maintainParameter(PARAMETER_ESTIMATED_TIME);
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.add(getPhasesTable(1, 4, "run_reg.group_registration", "Group registration"), 1, row++);
		table.setHeight(row++, 12);

		table.add(getInformationTable(localize("run_reg.group_information_text_step_1", "Information text 1...")), 1, row++);
		table.setHeight(row++, 6);
		
		IWTimestamp ts = IWTimestamp.RightNow();
    Integer y = new Integer(ts.getYear());
    String yearString = y.toString();
    
		DropdownMenu runDropdown = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_RUN));
		Collection runs = getRunBusiness(iwc).getRuns();
		runDropdown.addMenuElement("-1", localize("run_year_ddd.select_run","Select run..."));
		if(runs != null) {
			Iterator iter = runs.iterator();
			while (iter.hasNext()) {
				Group run = (Group) iter.next();
				runDropdown.addMenuElement(run.getPrimaryKey().toString(), localize(run.getName(), run.getName()));
			}
		}
		if (run != null) {
			runDropdown.setSelectedElement(run.getPrimaryKey().toString());
		}

		DropdownMenu distanceDropdown = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_DISTANCE));
		distanceDropdown.addMenuElement("", localize("run_year_ddd.select_distance","Select distance..."));
		distanceDropdown.setAsNotEmpty(localize("run_reg.must_select_distance", "You have to select a distance"), "");
		if(run != null) {
			Collection distances = getRunBusiness(iwc).getDistancesMap(run, yearString);
			if(distances != null) {
				Iterator iter = distances.iterator();
				while (iter.hasNext()) {
					Group element = (Group) iter.next();
					distanceDropdown.addMenuElement(element.getPrimaryKey().toString(), localize(element.getName(), element.getName()));
				}
			}
			if (distance != null) {
				distanceDropdown.setSelectedElement(distance.getPrimaryKey().toString());
			}
		}

		Table choiceTable = new Table();
		choiceTable.setColumns(2);
		choiceTable.setCellpadding(2);
		choiceTable.setCellspacing(0);
		table.add(choiceTable, 1, row++);
		int iRow = 1;
				
		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_PRIMARY_DD, "Run") + "/" + localize(IWMarathonConstants.RR_SECONDARY_DD, "Distance")), 1, iRow);
		choiceTable.add(runDropdown, 2, iRow);
		choiceTable.add(distanceDropdown, 2, iRow++);
		
		RemoteScriptHandler rsh = new RemoteScriptHandler(runDropdown, distanceDropdown);
		try {
			rsh.setRemoteScriptCollectionClass(GroupRunInputCollectionHandler.class);
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		add(rsh);
		
		TextInput groupNameField = (TextInput) getStyledInterface(new TextInput(PARAMETER_GROUP_NAME));
		groupNameField.setAsNotEmpty(localize("run_reg.group_name_not_empty", "Group name can not be empty"));

		choiceTable.setHeight(iRow++, 12);
		choiceTable.add(getHeader(localize("run_reg.group_name", "Group name")), 1, iRow);
		choiceTable.add(groupNameField, 2, iRow++);

		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next")));
		table.setHeight(row++, 18);
		table.add(next, 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);

		add(form);
	}
	
	private void stepTwo(IWContext iwc) {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, ACTION_STEP_THREE);
		form.maintainParameter(PARAMETER_GROUP_NAME);
		form.maintainParameter(PARAMETER_RUN);
		form.maintainParameter(PARAMETER_DISTANCE);
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.add(getPhasesTable(2, 4, "run_reg.group_registration", "Group registration"), 1, row++);
		table.setHeight(row++, 12);

		table.add(getInformationTable(localize("run_reg.group_information_text_step_2", "Information text 2...")), 1, row++);
		table.setHeight(row++, 6);
		
		Table choiceTable = new Table();
		choiceTable.setColumns(5);
		choiceTable.setCellpadding(2);
		choiceTable.setCellspacing(0);
		choiceTable.setWidth(1, "33%");
		choiceTable.setWidth(2, 6);
		choiceTable.setWidth(3, "33%");
		choiceTable.setWidth(4, 6);
		choiceTable.setWidth(5, "33%");
		choiceTable.setWidth(Table.HUNDRED_PERCENT);
		table.add(choiceTable, 1, row++);
		int iRow = 1;
		
		for (int a = 1; a <= 5; a++) {
			TextInput participant = (TextInput) getStyledInterface(new TextInput(PARAMETER_PARTICIPANT_NUMBER));
			participant.setWidth(Table.HUNDRED_PERCENT);
			participant.setAsIntegers(localize("run_reg.invalid_participant_number", "Invalid participant number"));
			if (participants != null && participants.length >= a) {
				participant.setContent(participants[a - 1]);
			}
			
			TextInput bestTime = (TextInput) getStyledInterface(new TextInput(PARAMETER_BEST_TIME));
			bestTime.setWidth(Table.HUNDRED_PERCENT);
			if (bestTimes != null && bestTimes.length >= a) {
				bestTime.setContent(bestTimes[a - 1]);
			}
			
			TextInput estimatedTime = (TextInput) getStyledInterface(new TextInput(PARAMETER_ESTIMATED_TIME));
			estimatedTime.setWidth(Table.HUNDRED_PERCENT);
			if (estimatedTimes != null && estimatedTimes.length >= a) {
				estimatedTime.setContent(estimatedTimes[a - 1]);
			}
			
			choiceTable.add(getHeader(localize("run_reg.partipant_nr", "Participant nr.") + " " + String.valueOf(a)), 1, iRow);
			choiceTable.add(getHeader(localize("run_reg.best_time", "Best time in distance")), 3, iRow);
			choiceTable.add(getHeader(localize("run_reg.estimated_time", "Estimated time in distance")), 5, iRow++);
			choiceTable.add(participant, 1, iRow);
			choiceTable.add(bestTime, 3, iRow);
			choiceTable.add(estimatedTime, 5, iRow++);
			
			if (a != 5) {
				choiceTable.setHeight(iRow++, 3);
			}
		}
		
		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STEP_ONE));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next")));

		table.setHeight(row++, 18);
		table.add(previous, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(next, 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);

		add(form);
	}
	
	private void stepThree(IWContext iwc) {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, ACTION_SAVE);
		form.maintainParameter(PARAMETER_GROUP_NAME);
		form.maintainParameter(PARAMETER_RUN);
		form.maintainParameter(PARAMETER_DISTANCE);
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.add(getPhasesTable(3, 4, "run_reg.group_registration_overview", "Group registration overview"), 1, row++);
		table.setHeight(row++, 12);

		table.add(getInformationTable(localize("run_reg.group_information_text_step_3", "Information text 3...")), 1, row++);
		table.setHeight(row++, 12);
		
		table.setCellpaddingLeft(1, row, 3);
		table.add(getHeader(localize("run_reg.group_name", "Group name") + ":"), 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getText(groupName), 1, row++);
		table.setHeight(row++, 12);
		
		Table runnerTable = new Table();
		runnerTable.setColumns(3);
		runnerTable.setWidth(Table.HUNDRED_PERCENT);
		runnerTable.add(getHeader(localize("run_reg.runner_name", "Runner name")), 1, 1);
		runnerTable.add(getHeader(localize("run_reg.run", "Run")), 2, 1);
		runnerTable.add(getHeader(localize("run_reg.distance", "Distance")), 3, 1);
		table.add(runnerTable, 1, row++);
		int runRow = 2;
		
		for (int a = 0; a < participants.length; a++) {
			String runner = participants[a];
			if (runner.length() > 0) {
				Participant participant = (Participant) participantMap.get(runner);
				
				if (participant != null) {
					runnerTable.add(new HiddenInput(PARAMETER_PARTICIPANT_NUMBER, runner), 1, runRow);
					runnerTable.add(new HiddenInput(PARAMETER_BEST_TIME, bestTimes[a]), 1, runRow);
					runnerTable.add(new HiddenInput(PARAMETER_ESTIMATED_TIME, estimatedTimes[a]), 1, runRow);
					runnerTable.add(new HiddenInput(PARAMETER_PARTICIPANT, participant.getPrimaryKey().toString()), 1, runRow);
					runnerTable.add(getText(participant.getUser().getName()), 1, runRow);
					runnerTable.add(getText(localize(run.getName(), run.getName())), 2, runRow);
					runnerTable.add(getText(localize(distance.getName(), distance.getName())), 3, runRow++);
				}
			}
		}

		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STEP_TWO));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("save", "Save")));

		table.setHeight(row++, 18);
		table.add(previous, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(next, 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);

		add(form);
	}
	
	private void save(IWContext iwc) {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;
		
		try {
			String[] runners = iwc.getParameterValues(PARAMETER_PARTICIPANT);
			getRunBusiness(iwc).addParticipantsToGroup(runners, bestTimes, estimatedTimes, groupName);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}

		table.add(getPhasesTable(4, 4, "run_reg.group_registration_done", "Group registration completed"), 1, row++);
		table.setHeight(row++, 12);

		table.add(getInformationTable(localize("run_reg.group_information_text_step_4", "Information text 4...")), 1, row++);
		table.setHeight(row++, 18);
		
		table.add(getText(localize("run_reg.group_registered_for", "You have registered for the team competition in the ")), 1, row);
		table.add(getHeader(localize(run.getName(), run.getName())), 1, row++);
		table.setHeight(row++, 16);

		table.add(getText(localize("run_reg.group_registered_name", "Your team is registered under the name ")), 1, row);
		table.add(getHeader(groupName), 1, row++);
		table.setHeight(row++, 16);

		table.add(getText(localize("run_reg.group_registered_distance", "Each team member has chosen to run ")), 1, row);
		table.add(getHeader(localize(distance.getName(), distance.getName())), 1, row++);
		table.setHeight(row++, 16);

		table.add(getText(localize("run_reg.group_registered_runners", "The runners in your team are") + ":"), 1, row++);
		for (int a = 0; a < participants.length; a++) {
			String runner = participants[a];
			if (runner.length() > 0) {
				Participant participant = (Participant) participantMap.get(runner);
				
				if (participant != null) {
					table.add(getHeader(participant.getParticipantNumber() + " - " + participant.getUser().getName()), 1, row++);
				}
			}
		}

		add(table);
	}

	private int parseAction(IWContext iwc) {
		int action = ACTION_STEP_ONE;
		
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		
		if (iwc.isParameterSet(PARAMETER_RUN)) {
			try {
				run = ConverterUtility.getInstance().convertGroupToRun(new Integer(iwc.getParameter(PARAMETER_RUN)));
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
		}
		if (iwc.isParameterSet(PARAMETER_DISTANCE)) {
			try {
				distance = ConverterUtility.getInstance().convertGroupToDistance(new Integer(iwc.getParameter(PARAMETER_DISTANCE)));
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
		}
		if (iwc.isParameterSet(PARAMETER_GROUP_NAME)) {
			groupName = iwc.getParameter(PARAMETER_GROUP_NAME);
		}
		participants = iwc.getParameterValues(PARAMETER_PARTICIPANT_NUMBER);
		bestTimes = iwc.getParameterValues(PARAMETER_BEST_TIME);
		estimatedTimes = iwc.getParameterValues(PARAMETER_ESTIMATED_TIME);
		
		int numberOfParticipants = 0;
		if (action == ACTION_STEP_THREE || action == ACTION_SAVE) {
			participantMap = new HashMap();
			for (int i = 0; i < participants.length; i++) {
				String participant = participants[i];
				if (participant.length() > 0) {
					try {
						Participant runner = getRunBusiness(iwc).getParticipantByDistanceAndParticipantNumber(distance.getPrimaryKey(), Integer.parseInt(participant));
						if (runner.getRunGroupName() != null) {
							action = ACTION_STEP_TWO;
							getParentPage().setAlertOnLoad(localize("run_reg.participant_already_in_a_group", "Participant is already in another group: ") + participant);
							return action;
						}
						participantMap.put(participant, runner);
						numberOfParticipants++;
					}
					catch (RemoteException re) {
						throw new IBORuntimeException(re);
					}
					catch (FinderException fe) {
						action = ACTION_STEP_TWO;
						getParentPage().setAlertOnLoad(localize("run_reg.participant_not_found_in_distance", "Participant not found in distance: ") + participant);
						return action;
					}
				}
			}
		}
		
		if (action == ACTION_STEP_THREE && numberOfParticipants < 3) {
			action = ACTION_STEP_TWO;
			getParentPage().setAlertOnLoad(localize("run_reg.must_select_three", "You have to select a minimum of three to form a team."));
		}

		return action;
	}
}