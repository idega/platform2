/*
 * Created on Apr 23, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.block.forum.presentation;

import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.block.category.business.CategoryFinder;
import com.idega.block.category.data.ICCategory;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;


/**
 * In this window you should be able to edit the name, description and the invalidation date of the topic (ICCategery)
 */
public class CommuneForumTopicEditor extends CommuneBlock {
	
	private final int ACTION_EDIT = 1;
	private final int ACTION_SAVE = 2;
	
	private int _topicID = -1;
	private int action = -1;
	
	public static String PARAMETER_TOPIC_ID = "cm_forum_t_id";
	public static String PARAMETER_ACTION = "cm_forum_action";
	public static String PARAMETER_NAME = "cm_forum_name";
	public static String PARAMETER_DESCRIPTION = "cm_forum_description";
	public static String PARAMETER_DATE = "cm_forum_date";
	
	public CommuneForumTopicEditor(){
		super();
	}
	
	public void main(IWContext iwc) throws Exception {
		parse(iwc);
		
		switch (action) {
			case ACTION_EDIT :
				createTable();
				break;
			case ACTION_SAVE :
				save(iwc);
				break;
			default :
				getParentPage().close();
				break;
		}
	}
	
	public void createTable() {
		Form form = new Form();
		form.maintainParameter(PARAMETER_TOPIC_ID);
		
		Table table = new Table(3, 5);
		table.setRowColor(1, "#000000");
		table.setRowColor(3, "#000000");
		table.setRowColor(5, "#000000");
		table.setColumnColor(1, "#000000");
		table.setColumnColor(3, "#000000");
		table.setColor(2, 2, "#CCCCCC");
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setWidth(2, Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		table.setHeight(4, Table.HUNDRED_PERCENT);
		table.setCellpadding(0);
		table.setCellspacing(0);
		form.add(table);

		Table headerTable = new Table(1, 1);
		headerTable.setCellpadding(6);
		table.add(headerTable, 2, 2);

		Table contentTable = new Table(1, 1);
		contentTable.setCellpadding(10);
		contentTable.setWidth(Table.HUNDRED_PERCENT);
		contentTable.setHeight(Table.HUNDRED_PERCENT);
		table.add(contentTable, 2, 4);

		headerTable.add(getHeader(localize("forum.topic_editor", "Edit topic")));
		contentTable.add(getEditForm());
		add(form);
	}
	
	private Table getEditForm() {
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
	
		int row = 1;
		ICCategory category = CategoryFinder.getInstance().getCategory(_topicID);
		
		TextInput textInput = (TextInput) getStyledInterface(new TextInput(PARAMETER_NAME));
		textInput.setLength(30);
		textInput.setAsNotEmpty(localize("forum.must_fill_in_name", "Name must be set."));
		textInput.setContent(category.getName());
		
		table.add(getSmallHeader(localize("forum.name", "Name")+":"), 1, row);
		table.add(textInput, 2, row++);

		TextArea textArea = (TextArea) getStyledInterface(new TextArea(PARAMETER_DESCRIPTION));
		textArea.setWidth(Table.HUNDRED_PERCENT);
		textArea.setRows(5);
		if (category.getDescription() != null)
			textArea.setContent(category.getDescription());

		table.setVerticalAlignment(1, row, Table.VERTICAL_ALIGN_TOP);
		table.add(getSmallHeader(localize("forum.description", "Description")+":"), 1, row);
		table.add(textArea, 2, row++);

		DateInput dateInput = (DateInput) getStyledInterface(new DateInput(PARAMETER_DATE));
		if (category.getInvalidationDate() != null)
			dateInput.setDate(new IWTimestamp(category.getInvalidationDate()).getDate());
		else
			dateInput.setDate(new IWTimestamp().getDate());
		dateInput.setAsNotEmpty(localize("forum.must_fill_in_date", "Date must be set."));

		table.add(getSmallHeader(localize("forum.close_date", "Close date")+":"), 1, row);
		table.add(dateInput, 2, row++);

		SubmitButton save = (SubmitButton) getStyledInterface(new SubmitButton(localize("save", "Save"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE)));
		CloseButton close = (CloseButton) getStyledInterface(new CloseButton(localize("close_window", "Close")));
		table.add(save, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(close, 1, row);
		table.setHeight(row, Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);

		return table;
	}
	
	public void save(IWContext iwc) {
		String description = iwc.getParameter(PARAMETER_DESCRIPTION);
		String date = iwc.getParameter(PARAMETER_DATE);
		String name = iwc.getParameter(PARAMETER_NAME);
		
		if (date != null && name != null) {
			ICCategory category = CategoryFinder.getInstance().getCategory(_topicID);
			category.setInvalidationDate(new IWTimestamp(date).getTimestamp());
			if (description != null)
				category.setDescription(description);
			category.setName(name);
			category.store();
			getParentPage().setParentToReload();
			getParentPage().close();
		}
		else
			createTable();
	}
	
	public void parse(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_TOPIC_ID))
			_topicID = Integer.parseInt(iwc.getParameter(PARAMETER_TOPIC_ID));
		
		if (iwc.isParameterSet(PARAMETER_ACTION))
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		else
			action = ACTION_EDIT;
	}
}