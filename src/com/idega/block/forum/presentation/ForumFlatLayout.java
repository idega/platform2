/*
 * Created on Jun 15, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.forum.presentation;

import java.util.Iterator;

import com.idega.block.category.business.CategoryFinder;
import com.idega.block.category.data.ICCategory;
import com.idega.block.forum.business.ForumBusiness;
import com.idega.block.forum.data.ForumData;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

/**
 * @author Anna
 */
public class ForumFlatLayout extends Forum {
	
	private int initialBodyIndent = 10;
	private int initialHeaderIndent = 10;
	private int indent = 15;
	private int _threadID = -1;
	
	//finds all threads and their children, displays children (answer to a thread) following their parent (thread)  - ac
	protected int displaySelectedForum(IWContext iwc, Table table, int row, ForumData thread, int depth) {
		if (thread != null) {
			row = displaySelectedForumFlatLayout(iwc, table, row, thread, depth);
			
			Iterator iter = thread.getChildrenIterator();
			while (iter != null && iter.hasNext()) {
				row = this.displaySelectedForum(iwc, table, row, (ForumData) iter.next(), depth + 1);
			}
		}
		return row;
	}
	
	//shows the forum for a selected thread - ac 
	protected int displaySelectedForumFlatLayout(IWContext iwc, Table table, int row, ForumData thread, int depth) {
		table.add(getThreadHeaderTable(thread, iwc, depth), 1, row++);
	
		table.add(getThreadBody(thread), 1, row);
		
		//here the body is indented for every new answer
		table.setRowStyleClass(row, getStyleName(BODY_ROW_STYLE));
		table.setCellpaddingLeft(1, row, initialBodyIndent);
		if (depth > 0) {
			table.setCellpaddingLeft(1, row, initialBodyIndent + (indent * depth));
		}
		row++;
		
		return row;
	}
	
	//overwrited to fit ForumFlatLayout - ac
	protected Table getThreadHeaderTable(ForumData thread, IWContext iwc, int depth) {
		Table table = new Table(2, 2);
		table.setWidth("100%");
		table.setCellpadding(0);
		table.setCellspacing(0);

		table.setCellpaddingLeft(1, 1, initialHeaderIndent);
		table.setCellpaddingLeft(1, 2, initialHeaderIndent);
		if (depth > 0) {
			table.setCellpaddingLeft(1, 1, initialHeaderIndent + (indent * depth));
			table.setCellpaddingLeft(1, 2, initialHeaderIndent + (indent * depth));
		}

		table.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setAlignment(2, 2, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setRowStyleClass(1, getStyleName(HEADER_ROW_STYLE));
		table.setRowStyleClass(2, getStyleName(LIGHT_ROW_STYLE));

		table.add(getThreadSubject(thread), 1, 1);
		table.add(getThreadDate(iwc, thread, INFORMATION_STYLE), 2, 1);
		table.add(getUser(thread), 1, 2);
		table.add(getThreadLinks(iwc, thread), 2, 2);

		return table;
	}
	
	protected Table getThreadLinks(IWContext iwc, ForumData thread) {
		Table table = new Table();
		table.setCellspacing(0);
		table.setCellpadding(0);
		int column = 1;
		Text separator = getStyleText(Text.NON_BREAKING_SPACE + "|" + Text.NON_BREAKING_SPACE, TEXT_STYLE);
		boolean added = false;

		if (hasReplyPermission()) {
			ThreadReplyLink replyLink = new ThreadReplyLink(thread);
			replyLink.setShowImage(false);

			table.add(replyLink, column++, 1);
			added = true;
		}

		if (hasDeletePermission()) {
			ThreadDeleteLink deleteLink = new ThreadDeleteLink(thread);
			deleteLink.setShowImage(false);

			if (added) {
				table.add(separator, column++, 1);
			}
			table.add(deleteLink, column++, 1);
		}

		return table;
	}

	
	//a table that shows a list of all threads at the bottom of the forum - ac
	protected PresentationObject getForumTree(IWContext iwc, ForumData[] topThreads) {
		Table table = new Table();
		int row = 1;
		table.setCellspacing(0);/*controls the space between table cells */
		table.setCellpadding(0);/*sets the amount of space between the contents of the cell and the cell wall */
		table.setWidth("100%");
		table.setBorder(0);
		table.setColumnWidth(2, getAuthorWidth());
		table.setColumnWidth(3, getReplyWidth());
		table.setColumnWidth(4, getDateWidth());

		if(topThreads == null) {
			return table;
		}
		
		Text author = formatText(_iwrb.getLocalizedString("author", "Author"), HEADER_STYLE);
		Text replies = formatText(_iwrb.getLocalizedString("replies", "Replies"), HEADER_STYLE);
		Text date = formatText(_iwrb.getLocalizedString("date", "Date"), HEADER_STYLE);

		table.add(formatText(_iwrb.getLocalizedString("thread", "Thread"), HEADER_STYLE),1,row);
		table.add(author,2,row);
		table.add(replies, 3,row);
		table.add(date, 4,row);
		table.setRowStyleClass(row, getStyleName(HEADER_ROW_STYLE));
		row++;
		
		for(int i = 0; i < topThreads.length; i++){
			if(topThreads [i]!= null){
				if (i % 2 == 0) {
					table.setRowStyleClass(row, getStyleName(DARK_ROW_STYLE));
				} else {
					table.setRowStyleClass(row, getStyleName(LIGHT_ROW_STYLE));
				}
				Link nameLink = formatLink(topThreads[i].getThreadSubject());
				nameLink.addParameter(ForumBusiness.PARAMETER_THREAD_ID, topThreads[i].getPrimaryKey().toString());
				nameLink.addParameter(ForumBusiness.PARAMETER_TOPIC_ID, super._topicID);
				nameLink.addParameter(ForumBusiness.PARAMETER_STATE, super._state);
				
				table.add(getThreadImage(),1,row);
				table.add(formatText(Text.NON_BREAKING_SPACE), 1, row);
				table.add(nameLink, 1, row);
				table.add(getUser(topThreads[i]), 2, row);
				table.add(formatText(Integer.toString(topThreads[i].getNumberOfResponses())),3,row);
				table.add(getThreadDate(iwc, topThreads[i], Forum.TEXT_STYLE),4,row++);
			}
		}
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_RIGHT);
		
		return table;
	}
	
	//overrided from Forum
	protected void getForumThreads(IWContext iwc, Table table) {
		int row = 1;

		ICCategory topic = null;
		if (_topicID != -1)
			topic = CategoryFinder.getInstance().getCategory(_topicID);

		ForumData thread = null;
		try {
			thread = forumBusiness.getForumData(Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_THREAD_ID)));
			_threadID = Integer.parseInt(iwc.getParameter(ForumBusiness.PARAMETER_THREAD_ID));
		}
		catch (NumberFormatException e) {
			thread = null;
		}

		if (topic != null) {
			if (_showTopicName) {
				Text topicText = formatText(topic.getName(), HEADER_STYLE);
				table.setRowColor(row, _headingColor);
				table.setRowPadding(row, 2);
				table.add(topicText, 1, row++);
			}
			
			row = addBelowTopic(iwc, topic, table, row);

			if (thread != null && thread.isValid()) {
				row = displaySelectedForum(iwc, table, row, thread, 0);
			}
			
			table.setHeight(1, row++, "20");
			//this is the only difference in this overrided method from Forum - removing of getForumLinks()
			//table.add(getForumLinks(), 1, row++);

			updateThreadCount(iwc);

			ForumData[] threads = forumBusiness.getThreads(topic);
			ForumData[] someThreads = forumBusiness.getThreads(threads, _firstThread, _lastThread);
			boolean hasNextThreads = forumBusiness.hasNextThreads(threads, _lastThread);
			boolean hasPreviousThreads = forumBusiness.hasPreviousThreads(_firstThread);

			table.add(getForumTree(iwc, someThreads), 1, row++);

			if (hasNextThreads || hasPreviousThreads)
				table.setHeight(row++, 3);
				table.add(getNextPreviousTable(hasNextThreads, hasPreviousThreads), 1, row);
		}
	}
	
	private Link formatLink(String string) {
		Link link = getStyleLink(string, THREAD_LINK_STYLE);
		return link;
	}
	
	public void setIndent(int indent) {
		this.indent = indent;
	}
	
	public void setInitialBodyIndent(int initialIndent) {
		this.initialBodyIndent = initialIndent;
	}
	
	public void setInitialHeaderIndent(int initialHeaderIndent) {
		this.initialHeaderIndent = initialHeaderIndent;
	}
}