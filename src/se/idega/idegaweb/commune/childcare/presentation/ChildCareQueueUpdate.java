/*
 * Created on 24.4.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import se.idega.idegaweb.commune.childcare.data.ChildCareQueue;
import se.idega.idegaweb.commune.presentation.CitizenChildren;

import com.idega.block.navigation.presentation.UserHomeLink;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.core.location.data.Address;
import com.idega.presentation.IWContext;
import com.idega.presentation.Script;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;

/**
 * @author laddi
 */
public class ChildCareQueueUpdate extends ChildCareBlock {

	protected final int DBV_WITH_PLACE = 0;
	protected final int DBV_WITHOUT_PLACE = 1;
	protected final int FS_WITH_PLACE = 2;
	protected final int FS_WITHOUT_PLACE = 3;

	public static final String PARAMETER_ACTION = "ccqu_action";
	public static final String PARAMETER_STAGE = "ccqu_stage";
	public static final String PARAMETER_QUEUE = "ccqu_queue_id";
	public static final String PARAMETER_MESSAGE = "ccqu_message";
	public static final String PARAMETER_DATE = "ccqu_date";

	public static final int ACTION_FORM = 1;
	public static final int ACTION_SAVE = 2;
	public static final int ACTION_EXPORT_ALL = 3;

	public static final int STAGE_ONE = 1;
	public static final int STAGE_TWO = 2;
	public static final int STAGE_THREE = 3;
	
	private int _action = -1;
	private int _stage = -1;
	private int _childID = -1;
	private int maximumChecked = 5;
	private boolean _hasPlacing = false;
	
	private BackButton back;
	private User child;
	
	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		parse(iwc);
		back = (BackButton) getStyledInterface(new BackButton(localize("back", "Back")));
		
		if (getBusiness().getHasUnexportedChoices(_childID)) {
			switch (_action) {
				case ACTION_FORM :
					getForm(iwc);
					break;
				case ACTION_SAVE :
					save(iwc);
					break;
				case ACTION_EXPORT_ALL :
					exportAll();
					break;
			}
		}
		else {
			Table table = new Table(1, 3);
			table.setCellpaddingAndCellspacing(0);
			table.setHeight(2, 12);
			
			table.add(getSmallErrorText(localize("child_care.already_updated","No choices found or already updated.")), 1, 1);
			table.add(new UserHomeLink(), 1, 3);
			add(table);
		}
	}
	
	private void getForm(IWContext iwc) {
		Collection choices = null;
		try {
			choices = getBusiness().getQueueChoices(_childID);
		}
		catch (RemoteException e) {
			choices = null;
		}
		
		if (choices != null && choices.size() > 0) {
			Map choiceMap = getChoiceMap(choices);
			Table table = new Table();
			table.setCellpadding(0);
			table.setCellspacing(0);
			table.setWidth(getWidth());
			int row = 1;
			
			table.add(getChildInfo(iwc), 1, row++);
			table.setHeight(row++, 18);
			table.add(getStageText(), 1, row++);
			table.setHeight(row++, 18);
			
			switch (_stage) {
				case STAGE_ONE :
					if (_hasPlacing) {
						table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
						table.add(getExportAllButton(), 1, row++);
						table.setHeight(row++, 18);
					}
					table.add(getFirstStage(iwc, choices), 1, row);
					break;
				case STAGE_TWO :
					table.add(getSecondStage(iwc, choices, choiceMap), 1, row);
					break;
				case STAGE_THREE :
					table.add(getThirdStage(iwc, choices, choiceMap), 1, row);
					break;
			}
		
			add(table);
		}
		else {
			Table table = new Table(1, 3);
			table.setCellpaddingAndCellspacing(0);
			table.setHeight(2, 12);
			
			table.add(getSmallErrorText(localize("child_care.already_updated","No choices found or already updated.")), 1, 1);
			table.add(new UserHomeLink(), 1, 3);
			add(table);
		}
	}
	
	private Form getFirstStage(IWContext iwc, Collection choices) {
		Form form = new Form();
		form.maintainParameter(CitizenChildren.prmChildId);
		
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(6);
		table.setRowColor(1, getHeaderColor());
		form.add(table);
		int row = 1;
		int column = 2;
			
		table.add(getLocalizedSmallHeader("child_care.provider","Provider"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.area","Area"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.queue_date","Queue date"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.queue_order","Queue order"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.select","Select"), column, row++);
			
		ChildCareQueue queue;
		School provider;
		SchoolArea area;
		IWTimestamp queueDate;
		CheckBox select = null;
		int queuePosition = 1;

		Iterator iter = choices.iterator();
		while (iter.hasNext()) {
			column = 1;
			queue = (ChildCareQueue) iter.next();
			queueDate = new IWTimestamp(queue.getQueueDate());
			provider = queue.getProvider();
			area = provider.getSchoolArea();
			select = this.getCheckBox(PARAMETER_QUEUE, queue.getPrimaryKey().toString());
			try {
				queuePosition = getBusiness().getPositionInQueue(queue);
			}
			catch (RemoteException e) {
				queuePosition = 1;
			}

			if (row % 2 == 0)
				table.setRowColor(row, getZebraColor1());
			else
				table.setRowColor(row, getZebraColor2());

			table.add(getSmallText(String.valueOf(row - 1)), column++, row);
			table.add(getSmallText(provider.getSchoolName()), column++, row);
			table.add(getSmallText(area.getSchoolAreaName()), column++, row);
			table.add(getSmallText(queueDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
			table.add(getSmallText("("+queuePosition+")"), column++, row);
			table.add(select, column++, row++);
			if (!iter.hasNext())
				select.setMustBeChecked(localize("child_care.must_check_provider","You must select at least one provider."));
		}
		table.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(6, Table.HORIZONTAL_ALIGN_CENTER);

		if (_hasPlacing)
			maximumChecked = maximumChecked - 1;
			
		String errorMessage = localize("child_care.can_only_select_five","You can only select five providers.");
		if (maximumChecked == 4)
			errorMessage = localize("child_care.can_only_select_four","You can only select four providers.");
		if (select != null)
			select.setMaximumChecked(maximumChecked, errorMessage);

		Table buttonTable = new Table(2,1);
		buttonTable.setCellpadding(getCellpadding());
		buttonTable.setCellspacing(getCellspacing());
		buttonTable.setWidth(Table.HUNDRED_PERCENT);
		buttonTable.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		form.add(buttonTable);
		
		SubmitButton next = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.next","Next"), PARAMETER_STAGE, String.valueOf(STAGE_TWO)));
		/*if (select != null)
			next.setToEnableWhenChecked(select);*/
		buttonTable.add(next, 2, 1);
		
		return form;
	}
	
	private Form getSecondStage(IWContext iwc, Collection choiceCollection, Map choiceMap) {
		String[] choices = iwc.getParameterValues(PARAMETER_QUEUE);
		if (choices == null || choices.length == 0) {
			return getFirstStage(iwc, choiceCollection);
		}
		else {
			Form form = new Form();
			form.maintainParameter(CitizenChildren.prmChildId);
			form.setOnSubmit("return checkInputs(findObj('"+PARAMETER_QUEUE+"'))");
		
			Script script = form.getAssociatedFormScript();
			if (script == null)
				script = new Script();
			script.addFunction("checkInputs", getCheckSubmitString(localize("child_care.must_not_be_the_same", "Please do not choose the same provider more than once.")));
			form.setAssociatedFormScript(script);
		
			DropdownMenu drop = getChoiceDropdown(choices, choiceMap);
		
			Table table = new Table();
			table.setWidth(Table.HUNDRED_PERCENT);
			table.setCellpadding(getCellpadding());
			table.setCellspacing(getCellspacing());
			table.setColumns(3);
			table.setWidth(1, 100);
			table.setWidth(2, 6);
			int row = 1;
			
			DropdownMenu provider;
			for (int a = 0; a < choices.length; a++) {
				provider = (DropdownMenu) drop.clone();
				provider.setSelectedElement(choices[a]);
				//if (a == 0)
					//provider.setOnSubmitFunction("checkInputs", getCheckSubmitString(), localize("child_care.must_not_be_the_same", "Please do not choose the same provider more than once."));
				table.add(getSmallHeader(localize("child_care.provider","Provider")+" "+String.valueOf(a+1)+":"), 1, row);
				table.add(provider, 3, row++);
			}
		
			TextArea area = (TextArea) getStyledInterface(new TextArea(PARAMETER_MESSAGE));
			area.setWidth(Table.HUNDRED_PERCENT);
			area.setRows(6);
			table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
			table.add(getSmallHeader(localize("child_care.message","Message")+":"), 1, row);
			table.add(area, 3, row++);
		
			Table buttonTable = new Table(2,1);
			buttonTable.setCellpadding(getCellpadding());
			buttonTable.setCellspacing(getCellspacing());
			buttonTable.setWidth(Table.HUNDRED_PERCENT);
			buttonTable.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);

			SubmitButton next = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.next","Next"), PARAMETER_STAGE, String.valueOf(STAGE_THREE)));
			buttonTable.add(back, 1, 1);
			buttonTable.add(next, 2, 1);

			form.add(table);
			form.add(buttonTable);
		
			return form;
		}
	}
	
	private Form getThirdStage(IWContext iwc, Collection choiceCollection, Map choiceMap) {
		String[] choices = iwc.getParameterValues(PARAMETER_QUEUE);
		boolean sameChoice = false;
		for (int a = 0; a < choices.length; a++) {
			int choice = Integer.parseInt(choices[a]);
			for (int b = 0; b < choices.length; b++) {
				int otherChoice = Integer.parseInt(choices[b]);
				if (a != b && choice == otherChoice) {
					sameChoice = true;
				}
			}
		}
		
		if (sameChoice)	{
			return getSecondStage(iwc, choiceCollection, choiceMap);
		}
		else {
			Form form = new Form();
			form.maintainParameter(CitizenChildren.prmChildId);
			form.maintainParameter(PARAMETER_MESSAGE);
		
			Table table = new Table();
			table.setWidth(Table.HUNDRED_PERCENT);
			table.setCellpadding(getCellpadding());
			table.setCellspacing(getCellspacing());
			table.setColumns(5);
			table.setWidth(1, 100);
			table.setWidth(2, 6);
			table.setWidth(4, 3);
			int row = 1;
			
			ChildCareQueue queue;
			School provider;
			SchoolArea area;
			DateInput date;
			HiddenInput choice;
		
			//IWTimestamp earliestDate = new IWTimestamp(1, 6, 2003);
		
			for (int a = 0; a < choices.length; a++) {
				queue = (ChildCareQueue) choiceMap.get(choices[a]);
				provider = queue.getProvider();
				area = provider.getSchoolArea();
			
				date = new DateInput(PARAMETER_DATE+"_"+(a+1));
				date.setDate(queue.getStartDate());
				date.setAsNotEmpty(localize("child_care.must_select_date","You must select a date."));
				//date.setEarliestPossibleDate(earliestDate.getDate(), localize("child_care.invalid_dates_selected","Earliest selectable date is 01-06-2003."));
				choice = new HiddenInput(PARAMETER_QUEUE, choices[a]);
			
				table.add(getSmallHeader(localize("child_care.provider","Provider")+" "+String.valueOf(a+1)+":"), 1, row);
				table.add(getSmallText(area.getSchoolAreaName()+": "+provider.getSchoolName()), 3, row);
				table.add(date, 5, row);
				table.add(choice, 5, row++);
			}
		
			Table buttonTable = new Table(2,1);
			buttonTable.setCellpadding(getCellpadding());
			buttonTable.setCellspacing(getCellspacing());
			buttonTable.setWidth(Table.HUNDRED_PERCENT);
			buttonTable.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);

			SubmitButton update = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.update","Update"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
			update.setSingleSubmitConfirm(localize("child_care.confirm_queue_update","Are you sure you want to update? Selection can not be altered."));
			buttonTable.add(back, 1, 1);
			buttonTable.add(update, 2, 1);

			form.add(table);
			form.add(buttonTable);
		
			return form;
		}
	}
	
	private GenericButton getExportAllButton() {
		GenericButton button = (GenericButton) getStyledInterface(new GenericButton("export_all", localize("child_care.export_all","Export all")));
		button.setPageToOpen(getParentPageID());
		button.addParameterToPage(PARAMETER_ACTION, ACTION_EXPORT_ALL);
		button.addParameterToPage(CitizenChildren.prmChildId, _childID);
		button.setOnClickConfirm(localize("child_care.confirm_export_all","Are you sure you want to export all and drop all queues?"));
		
		return button;
	}
	
	private Text getStageText() {
		if (_hasPlacing) {
			return getSmallText(localize("child_care.placed_queue_text_stage_"+_stage,"Text for stage "+_stage));
		}
		else {
			return getSmallText(localize("child_care.non_placed_queue_text_stage_"+_stage,"Text for stage "+_stage));
		}
	}
	
	private DropdownMenu getChoiceDropdown(String[] choices, Map choiceMap) {
		DropdownMenu drop = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_QUEUE));
		for (int a = 0; a < choices.length; a++) {
			ChildCareQueue queue = (ChildCareQueue) choiceMap.get(choices[a]);
			School provider = queue.getProvider();
			SchoolArea area = provider.getSchoolArea();
			drop.addMenuElement(choices[a], area.getSchoolAreaName()+": "+provider.getSchoolName());
		}
		return drop;
	}
	
	private Table getChildInfo(IWContext iwc) {
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setColumns(3);
		table.setWidth(1, 100);
		table.setWidth(2, 6);
		int row = 1;
		
		try {
			child = getBusiness().getUserBusiness().getUser(_childID);
			if (child != null) {
				Address address = getBusiness().getUserBusiness().getUsersMainAddress(child);
				
				table.add(getLocalizedSmallHeader("child_care.child","Child"), 1, row);
				Name name = new Name(child.getFirstName(), child.getMiddleName(), child.getLastName());
				table.add(getSmallText(name.getName(iwc.getApplicationSettings().getDefaultLocale(), true)), 3, row++);
				table.setHeight(row++, 3);
				table.add(getLocalizedSmallHeader("child_care.personal_id","Personal ID"), 1, row);
				table.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), 3, row++);
				
				if (address != null) {
					table.setHeight(row++, 3);
					table.add(getLocalizedSmallHeader("child_care.address","Address"), 1, row);
					table.add(getSmallText(address.getStreetAddress()), 3, row);
					if (address.getPostalAddress() != null)
						table.add(getSmallText(", "+address.getPostalAddress()), 3, row);
				}
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return table;
	}
	
	private String getCheckSubmitString(String message) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("function checkInputs(inputs) {").append("\n\t");
		buffer.append("for(var i=0;i<inputs.length;i++) {").append("\n\t\t");
		buffer.append("var inputValue = inputs[i].options[inputs[i].selectedIndex].value;").append("\n\t\t");
		buffer.append("for (var j=0;j<inputs.length;j++) {").append("\n\t\t\t");
		buffer.append("if (i != j) {").append("\n\t\t\t\t");
		buffer.append("if (inputs[j].options[inputs[j].selectedIndex].value == inputValue) {").append("\n\t\t\t\t\t");
		buffer.append("alert('"+message+"');").append("\n\t\t\t\t\t");
		buffer.append("return false;").append("\n\t\t\t\t");
		buffer.append("}").append("\n\t\t\t");
		buffer.append("}").append("\n\t\t");
		buffer.append("}").append("\n\t");
		buffer.append("}").append("\n\t");
		buffer.append("return true;").append("\n");
		buffer.append("}");
		return buffer.toString();
	}
	
	private Map getChoiceMap(Collection choices) {
		Map map = new HashMap();
		Iterator iter = choices.iterator();
		while (iter.hasNext()) {
			ChildCareQueue queue = (ChildCareQueue) iter.next();
			map.put(queue.getPrimaryKey().toString(), queue);
			if (!iter.hasNext()) {
				int queueType = queue.getQueueType();
				switch (queueType) {
					case DBV_WITH_PLACE :
						_hasPlacing = true;
						break;
					case DBV_WITHOUT_PLACE :
						_hasPlacing = false;
						break;
					case FS_WITH_PLACE :
						_hasPlacing = true;
						break;
					case FS_WITHOUT_PLACE :
						_hasPlacing = false;
						break;
				}
			}
		}
		return map;
	}
	
	protected void save(IWContext iwc) {
		String[] choice = iwc.getParameterValues(PARAMETER_QUEUE);
		int[] provider = new int[choice.length];
		String[] dates = new String[choice.length];
		Date[] queueDates = new Date[choice.length];
		boolean[] hasPriority = new boolean[choice.length];
		
		//IWTimestamp startDate = new IWTimestamp(1, 6, 2003);
		//boolean isEarlier = false;
		for (int a = 0; a < choice.length; a++) {
			String date = iwc.getParameter(PARAMETER_DATE+"_"+(a+1));
			//IWTimestamp stamp = new IWTimestamp(date);
			dates[a] = date;
			//if (stamp.isEarlierThan(startDate))
				//isEarlier = true;
		}
		
		/*if (isEarlier) {
			Table table = new Table(1, 3);
			table.setCellpaddingAndCellspacing(0);
			table.setHeight(2, 12);
			
			table.add(getSmallErrorText(localize("child_care.invalid_dates_selected","Earliest selectable date is 01-06-2003.")), 1, 1);
			table.add(back, 1, 3);
			add(table);
		}*/
		//else {
			Collection choices = null;
			try {
				choices = getBusiness().getQueueChoices(_childID);
			}
			catch (RemoteException e) {
				choices = new ArrayList();
			}
			
			Iterator iter = choices.iterator();
			while (iter.hasNext()) {
				ChildCareQueue queue = (ChildCareQueue) iter.next();
				for (int a = 0; a < choice.length; a++) {
					if (queue.getPrimaryKey().toString().equals(choice[a])) {
						provider[a] = queue.getProviderId();
						queueDates[a] = queue.getQueueDate();
						if (queue.getPriority() != null)
							hasPriority[a] = true;
						else
							hasPriority[a] = false;
					}
				}
			}
			
			boolean success = false;
			try {
				success = getBusiness().insertApplications(iwc.getCurrentUser(), provider, dates, iwc.getParameter(PARAMETER_MESSAGE), _childID, queueDates, hasPriority);
			}
			catch (RemoteException e1) {
				e1.printStackTrace();
			}
			
			Table table = new Table(1, 3);
			table.setCellpaddingAndCellspacing(0);
			table.setHeight(2, 12);
			
			if (success) {
				try {
					getBusiness().exportQueue(choices);
				}
				catch (RemoteException e2) {
					e2.printStackTrace();
				}

				table.add(getSmallHeader(localize("child_care.queue_update_completed","Queue update completed.")), 1, 1);
				table.add(new UserHomeLink(), 1, 3);
				add(table);
			}
			else {
				table.add(getSmallErrorText(localize("child_care.queue_update_failed","Queue update failed.")), 1, 1);
				table.add(new UserHomeLink(), 1, 3);
				add(table);
			}
		//}
	}
	
	private void exportAll() {
		try {
			Collection choices = getBusiness().getQueueChoices(_childID);
			Iterator iter = choices.iterator();
			while (iter.hasNext()) {
				ChildCareQueue queue = (ChildCareQueue) iter.next();
				getBusiness().setChildCareQueueExported(queue);
			}

			Table table = new Table(1, 3);
			table.setCellpaddingAndCellspacing(0);
			table.setHeight(2, 12);
			
			table.add(getSmallHeader(localize("child_care.queue_update_completed","Queue update completed.")), 1, 1);
			table.add(new UserHomeLink(), 1, 3);
			add(table);
		}
		catch (RemoteException e) {
			e.printStackTrace();

			Table table = new Table(1, 3);
			table.setCellpaddingAndCellspacing(0);
			table.setHeight(2, 12);
			
			table.add(getSmallErrorText(localize("child_care.queue_update_failed","Queue update failed.")), 1, 1);
			table.add(new UserHomeLink(), 1, 3);
			add(table);
		}
	}

	protected void parse(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION))
			_action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		else
			_action = ACTION_FORM;

		if (iwc.isParameterSet(PARAMETER_STAGE))
			_stage = Integer.parseInt(iwc.getParameter(PARAMETER_STAGE));
		else
			_stage = STAGE_ONE;

		if (iwc.isParameterSet(CitizenChildren.prmChildId))
			_childID = Integer.parseInt(iwc.getParameter(CitizenChildren.prmChildId));
	}
}