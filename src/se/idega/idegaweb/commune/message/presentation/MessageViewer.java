package se.idega.idegaweb.commune.message.presentation;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.data.Message;
import se.idega.idegaweb.commune.message.event.MessageListener;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CloseButton;
import com.idega.util.IWTimestamp;

/**
 * @author Laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MessageViewer extends CommuneBlock {

  private final static String PARAM_MESSAGE_ID = MessageListener.PARAM_MESSAGE_ID;
  private int messageID = -1;

	public void main(IWContext iwc) throws Exception {
		setResourceBundle(getResourceBundle(iwc));
		parse(iwc);
		if (messageID != -1)
			drawForm(iwc);
		else
			getParentPage().close();
	}
	
	private void drawForm(IWContext iwc) throws RemoteException {
		Table table = new Table(3,5);
		table.setRowColor(1, "#000000");
		table.setRowColor(3, "#000000");
		table.setRowColor(5, "#000000");
		table.setColumnColor(1, "#000000");
		table.setColumnColor(3, "#000000");
		table.setColor(2, 2, "#CCCCCC");
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setWidth(2,Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		table.setHeight(4,Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(4, Table.VERTICAL_ALIGN_TOP);
		table.setCellpadding(0);
		table.setCellspacing(0);
		
		Table headerTable = new Table(1,1);
		headerTable.setCellpadding(6);
		table.add(headerTable,2,2);
		
		Table contentTable = new Table(1,1);
		contentTable.setCellpadding(10);
		table.add(contentTable,2,4);
		
		headerTable.add(getHeader(localize("message.message","Message")));
		try {
			contentTable.add(getMessageTable(iwc));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		add(table);
	}
	
	private void parse(IWContext iwc) throws RemoteException {
		if (iwc.isParameterSet(PARAM_MESSAGE_ID))
			messageID = Integer.parseInt(iwc.getParameter(PARAM_MESSAGE_ID));
	}

	private Table getMessageTable(IWContext iwc) throws Exception {
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;

		Message msg = getMessage(iwc.getParameter(PARAM_MESSAGE_ID), iwc);
		getMessageBusiness(iwc).markMessageAsRead(msg);

		table.add(getSmallHeader(localize("message.from","From")),1,row);
		table.add(getSmallText(""),2,row++);

		IWTimestamp stamp = new IWTimestamp(msg.getCreated());
		table.add(getSmallHeader(localize("message.date","Date")),1,row);
		table.add(getSmallText(stamp.getLocaleDate(iwc.getCurrentLocale())),2,row++);

		table.add(getSmallHeader(localize("message.subject","Subject")),1,row);
		table.add(getSmallText(msg.getSubject()),2,row++);

		table.add(getSmallText(msg.getBody()),2,row++);

		table.add((CloseButton) getButton(new CloseButton(localize("message.back", "Back"))),1,row);
		table.mergeCells(1, row, 2, row);
		table.setHeight(row,Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);
		
		return table;
	}

	private Message getMessage(String id, IWContext iwc) throws Exception {
		int msgId = Integer.parseInt(id);
		Message msg = getMessageBusiness(iwc).getUserMessage(msgId);
		return msg;
	}

	private MessageBusiness getMessageBusiness(IWContext iwc) throws Exception {
		return (MessageBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, MessageBusiness.class);
	}
}