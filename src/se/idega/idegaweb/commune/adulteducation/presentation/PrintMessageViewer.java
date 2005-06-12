package se.idega.idegaweb.commune.adulteducation.presentation;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.FinderException;
import se.idega.idegaweb.commune.adulteducation.business.AdultEducationSession;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;

/**
 * Title: PrintMessageViewer
 * Description: A class to view and manage PrintedLetterMessage in the idegaWeb Commune application
 * Copyright:    Copyright idega Software (c) 2002
 * Company:	idega Software
 * @author <a href="mailto:roar@idega.is">roar</a>
 * @version $Id: PrintMessageViewer.java,v 1.1 2005/06/12 13:46:45 laddi Exp $
 * @since 17.3.2003 
 */

public class PrintMessageViewer extends se.idega.idegaweb.commune.message.presentation.PrintMessageViewer {

	private AdultEducationSession getSession(IWUserContext iwuc) {
		try {
			return (AdultEducationSession) IBOLookup.getSessionInstance(iwuc, AdultEducationSession.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	protected Collection getLetters(IWContext iwc)  throws FinderException {
		try {
			Collection unprintedLetters =
				getPrintedLetter().findLettersByAdultEducation(
						getSession(iwc).getSchool(),
						getSearchSsn(),
						getSearchMsgId(),
						getUFrom(),
						getUTo());
			return unprintedLetters;
		}
		catch (RemoteException e) {
			throw new FinderException(e.getMessage());
		}
		
	}
	
//	private void viewMessages(String[] ids)
//	throws FinderException {
//		System.out.println("Viewing " + (ids==null?0:ids.length) + " messages");
//		Table layout = new Table();
//		Collection selectedLetters = getPrintedLetter().findLetters(ids);
//
//		Iterator iter = selectedLetters.iterator();
//
//		//int bulkId;
//		int layoutRow = 1;
//		while (iter.hasNext()) {
//			int row = 1;
//
//			Table message = new Table(2, 9);
//			PrintedLetterMessage msg = (PrintedLetterMessage) iter.next();
//
//			addField(
//					message,
//					localize(LOCALE_MSGID, "Message Id"),
//					String.valueOf(msg.getNodeID()),
//					row++);
//			addField(
//					message,
//					localize(LOCALE_DATE_CREATED, "Message created"),
//					msg.getCreated().toString(),
//					row++);
//			//		addField(message, "From", msg.getSenderName(), row++);
//			addField(
//					message,
//					localize(LOCALE_RECEIVER, "Receiver"),
//					msg.getOwner().getName(),
//					row++);
//			addField(
//					message,
//					localize(LOCALE_SSN, "SSN"),
//					msg.getOwner().getPersonalID(),
//					row++);
//			//		addField(layout, "Type:", msg.getMessageType(), row++);
//			//		addField(layout, "Type:", msg.msg.getLetterType(), row++);
//			addField(
//					message,
//					localize(LOCALE_EVENT, "Event"),
//					msg.getSubject(),
//					row++);
//			message.add(new Text(""), 1, row++);
//			//Body
//			message.mergeCells(1, row, 2, row);
//			message.add(getSmallText(msg.getBody()), 1, row++);
//			
//			layout.add(message, 1, layoutRow++);
//			
//			if (iter.hasNext()) {
//				layout.add(new HorizontalRule(), 1, layoutRow);
//				layout.setHeight(layoutRow++, 1, "70");
//			}
//		}
//
//		Table toolbar = new Table();
//		toolbar.setCellpadding(2);
//		GenericButton printBtn = getButton(new PrintButton(localize("school.print","Print")));
//		GenericButton backBtn = getButton(new BackButton());
//
//		toolbar.add(backBtn, 1, 1);
//		toolbar.add(printBtn, 2, 1);
//
//		layout.setAlignment(1, layoutRow, Table.HORIZONTAL_ALIGN_RIGHT);
//		layout.add(toolbar, 1, layoutRow);
//		add(layout);
//	}

}
