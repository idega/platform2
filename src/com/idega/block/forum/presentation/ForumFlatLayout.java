/*
 * Created on Jun 15, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.forum.presentation;

import java.util.Iterator;

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
	

	
	protected int displaySelectedForum(IWContext iwc, Table table, int row, ForumData thread) {
		if (thread != null) {
			row = super.displaySelectedForum(iwc, table, row, thread);
			
			Iterator iter = thread.getChildrenIterator();
			while (iter != null && iter.hasNext()) {
				row = this.displaySelectedForum(iwc, table, row, (ForumData) iter.next());
			}
		}
		return row;
	}

	protected PresentationObject getForumTree(IWContext iwc, ForumData[] topThreads) {
		Table table = new Table();
		int row = 1;
		table.setCellspacing(0);
		table.setCellpadding(0);
		table.setWidth("100%");
		table.setColumnWidth(2, _authorWidth);
		table.setColumnWidth(3, _replyWidth);
		table.setColumnWidth(4, _dateWidth);

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
		table.setRowStyleClass(row, getStyleName(HEADING_STYLE));
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
				
				table.add(nameLink, 1, row);
				table.add(getUser(topThreads[i]), 2, row);
				table.add(formatText(Integer.toString(topThreads[i].getNumberOfResponses())),3,row);
				table.add(getThreadDate(iwc, topThreads[i], Forum.TEXT_STYLE),4,row++);
			}
		}
		
		
		return table;
	}
	
	private Link formatLink(String string) {
		Link link = getStyleLink(string, THREAD_LINK_STYLE);
		return link;
	}
}
