package se.idega.idegaweb.commune.message.presentation;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.message.business.MessageBusiness;
import se.idega.idegaweb.commune.message.data.Message;
import se.idega.idegaweb.commune.message.event.MessageListener;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import com.idega.builder.business.BuilderLogic;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.text.Name;
import com.idega.util.text.TextSoap;
import com.idega.presentation.Page;
import com.idega.presentation.Script;

/**
 * @author Laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MessageViewer extends CommuneBlock {

  public final static String PARAMETER_MESSAGE_ID = MessageListener.PARAM_MESSAGE_ID;
	public final static String PARAMETER_SENDER_ID = "msg_sender_id";
	public final static String PARAMETER_PAGE_ID = "msg_page_id";
	public final static String PARAMETER_METHOD = "msg_method";
	public final static String PARAMETER_ACTION = "msg_action";
	public final static String PARAMETER_SUBJECT = "msg_subject";
	public final static String PARAMETER_BODY = "msg_body";

  private int _pageID = -1;
  private int _messageID = -1;
	private int _senderID = -1;
	private int _action = -1;
	private int _method = -1;
	
	public static final int ACTION_CLOSE = 0;
	public static final int ACTION_REPLY_MESSAGE = 1;

	public static final int METHOD_VIEW_MESSAGE = 1;
	public static final int METHOD_REPLY_MESSAGE = 2;
	
	private SubmitButton close;

	public void main(IWContext iwc) throws Exception {
		getParentPage().setAllMargins(0);
		setResourceBundle(getResourceBundle(iwc));
		parse(iwc);
		Page p = this.getParentPage();
		if (p != null) {
			Script S = p.getAssociatedScript();
			S.addFunction("openChildcareParentWindow", getOpenInParentPage());
		}
		
		switch (_action) {
			case ACTION_CLOSE :
				close(iwc);
				break;
			case ACTION_REPLY_MESSAGE :
				reply(iwc);
				break;
		}
		
		if (_method != -1)
			drawForm(iwc);
	}
	
	//open childcare overview in parent window
	//link set in childcareAdminWindow
	public String getOpenInParentPage(){
		StringBuffer s = new StringBuffer();
		s.append("\nfunction openChildcareParentWindow(link){\n\t");
		
		s.append("opener.window.parent.location.href=link;");
		s.append("\n\t }");
		
		return s.toString();
	}
	
	private void drawForm(IWContext iwc) throws Exception {
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
		contentTable.setHeight(Table.HUNDRED_PERCENT);
		contentTable.setWidth(Table.HUNDRED_PERCENT);
		table.add(contentTable,2,4);
		
		close = (SubmitButton) getStyledInterface(new SubmitButton(localize("message.close", "Close"), PARAMETER_ACTION, String.valueOf(ACTION_CLOSE)));
		
		switch (_method) {
			case METHOD_VIEW_MESSAGE :
				headerTable.add(getHeader(localize("message.message","Message")));
				contentTable.add(getMessageTable(iwc));
				break;
			case METHOD_REPLY_MESSAGE :
				headerTable.add(getHeader(localize("message.reply_message", "Reply message")));
				contentTable.add(getReplyTable(iwc));
				break;
		}

		add(table);
	}
	
	private Form getMessageTable(IWContext iwc) throws Exception {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PAGE_ID);
		form.maintainParameter(PARAMETER_MESSAGE_ID);
		
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		Message msg = getMessageBusiness(iwc).getUserMessage(_messageID);
		getMessageBusiness(iwc).markMessageAsRead(msg);

		IWTimestamp stamp = new IWTimestamp(msg.getCreated());
		table.add(getSmallHeader(localize("message.date","Date")),1,row);
		table.add(getSmallText(stamp.getDateString("yyyy-MM-dd kk:mm")),2,row++);

		table.add(getSmallHeader(localize("message.subject","Subject")),1,row);
		table.add(getSmallText(msg.getSubject()),2,row++);

		table.add(getSmallText(TextSoap.formatText(msg.getBody())),2,row++);

		if (msg.getSenderID() != -1) {
			SubmitButton reply = (SubmitButton) getStyledInterface(new SubmitButton(localize("message.Reply", "Reply"), PARAMETER_METHOD, String.valueOf(METHOD_REPLY_MESSAGE)));
			table.add(reply, 1, row);
			table.add(Text.getNonBrakingSpace(), 1, row);
			form.addParameter(PARAMETER_SENDER_ID, msg.getSenderID());
		}

		table.add(close,1,row);
		table.mergeCells(1, row, 2, row);
		table.setHeight(row,Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);
		
		return form;
	}

	private Form getReplyTable(IWContext iwc) throws Exception {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PAGE_ID);
		form.maintainParameter(PARAMETER_MESSAGE_ID);
		form.maintainParameter(PARAMETER_SENDER_ID);
		
		Table table = new Table();
		table.setCellpadding(5);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setHeight(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		Message msg = getMessageBusiness(iwc).getUserMessage(_messageID);

		TextInput subject = (TextInput) getStyledInterface(new TextInput(PARAMETER_SUBJECT));
		subject.setLength(30);
		subject.setContent(localize("message.re","RE")+": "+msg.getSubject());
		
		table.add(getSmallHeader(localize("message.subject","Subject")+": "), 1, row);
		table.add(subject, 1, row++);

		TextArea body = (TextArea) getStyledInterface(new TextArea(PARAMETER_BODY));
		body.setWidth(Table.HUNDRED_PERCENT);
		body.setRows(7);
		
		User sender = msg.getSender();
		IWTimestamp stamp = new IWTimestamp(msg.getCreated());
		
		Name name = new Name(sender.getFirstName(), sender.getMiddleName(), sender.getLastName());
		Object[] arguments = { name.getName(iwc.getApplicationSettings().getDefaultLocale(), true), stamp.getLocaleDateAndTime(iwc.getCurrentLocale(), IWTimestamp.SHORT, IWTimestamp.SHORT) };
		String bodyMessage = MessageFormat.format(localize("message.reply_info", "On {1}, {0} wrote:\n"), arguments);
		if (msg.getBody() != null)
			bodyMessage += "\"" + TextSoap.formatText(msg.getBody()) + "\"";
		body.setContent("\n\n"+bodyMessage);

		table.add(getSmallHeader(localize("message.body","Body")+":"), 1, row);
		table.add(new Break(), 1, row);
		table.add(body, 1, row++);

		SubmitButton reply = (SubmitButton) getStyledInterface(new SubmitButton(localize("message.send", "Send"), PARAMETER_ACTION, String.valueOf(ACTION_REPLY_MESSAGE)));
		table.add(reply, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(close,1,row);
		table.mergeCells(1, row, 2, row);
		table.setHeight(row,Table.HUNDRED_PERCENT);
		table.setRowVerticalAlignment(row, Table.VERTICAL_ALIGN_BOTTOM);
		
		return form;
	}

	private void close(IWContext iwc) {
		getParentPage().setParentToRedirect(BuilderLogic.getInstance().getIBPageURL(iwc, _pageID));
		getParentPage().close();
	}
	
	private void reply(IWContext iwc) throws RemoteException {
		String subject = iwc.getParameter(PARAMETER_SUBJECT);
		String body = iwc.getParameter(PARAMETER_BODY);
		User receiver = getUserBusiness(iwc).getUser(_senderID);
		getMessageBusiness(iwc).createUserMessage(receiver, subject, body, iwc.getCurrentUser(), false);
		
		close(iwc);
	}
	
	private void parse(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_MESSAGE_ID))
			_messageID = Integer.parseInt(iwc.getParameter(PARAMETER_MESSAGE_ID));
		if (iwc.isParameterSet(PARAMETER_SENDER_ID))
			_senderID = Integer.parseInt(iwc.getParameter(PARAMETER_SENDER_ID));
		if (iwc.isParameterSet(PARAMETER_ACTION))
			_action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		if (iwc.isParameterSet(PARAMETER_METHOD))
			_method = Integer.parseInt(iwc.getParameter(PARAMETER_METHOD));
		if (iwc.isParameterSet(PARAMETER_PAGE_ID))
			_pageID = Integer.parseInt(iwc.getParameter(PARAMETER_PAGE_ID));
	}

	private MessageBusiness getMessageBusiness(IWContext iwc) throws RemoteException {
		return (MessageBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, MessageBusiness.class);
	}

	private CommuneUserBusiness getUserBusiness(IWContext iwc) throws RemoteException {
		return (CommuneUserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}
}