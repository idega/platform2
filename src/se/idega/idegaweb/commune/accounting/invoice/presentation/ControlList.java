/* $Id: ControlList.java,v 1.1 2003/10/30 09:07:43 kjell Exp $
*
* Copyright (C) 2003 Agura IT. All Rights Reserved.
*
* This software is the proprietary information of Agura IT AB.
* Use is subject to license terms.
*
*/
package se.idega.idegaweb.commune.accounting.invoice.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.presentation.IWContext;
import com.idega.presentation.ExceptionWrapper;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.Window;
import com.idega.presentation.Image;
import com.idega.idegaweb.IWMainApplication;

import se.idega.idegaweb.commune.accounting.presentation.AccountingBlock;
import se.idega.idegaweb.commune.accounting.presentation.ApplicationForm;
import se.idega.idegaweb.commune.accounting.presentation.ListTable;
import se.idega.idegaweb.commune.accounting.presentation.ButtonPanel;
import se.idega.idegaweb.commune.accounting.invoice.business.ControlListWriter;

import se.idega.idegaweb.commune.accounting.invoice.business.ControlListBusiness;

/**
 * ControlList
 * used to check the a batch run after it has completed
 *  
 * <p>
 * Last modified: $Modified$
 *
 * @author <a href="http://www.lindman.se">Kelly Lindman</a>
 * @version $$
 */
public class ControlList extends AccountingBlock {

	private final static int ACTION_DEFAULT = 0;
	
	private final static String KEY_PREFIX = "batch_reg_list."; 
	
	public final static String KEY_TITLE = KEY_PREFIX + "title";
	public final static String KEY_PROVIDER = KEY_PREFIX + "provider";
	public final static String KEY_NUM_INDIVIDUALS_PREL = KEY_PREFIX + "num_individuals_prel";
	public final static String KEY_LAST_MONTH = KEY_PREFIX + "last_month";
	public final static String KEY_TOTAL_AMOUNT_PREL = KEY_PREFIX + "total_amount_prel";
	public final static String KEY_MEDIA_WINDOW_TITLE = KEY_PREFIX + "media_window_title";

	/**
	 * Handles all of the blocks presentation.
	 * @param iwc user/session context 
	 */
	public void init(final IWContext iwc) {
		try {
			int action = parseAction();
			switch (action) {
				case ACTION_DEFAULT :
					viewDefaultForm(iwc);
					break;
			}
		}
		catch (Exception e) {
			super.add(new ExceptionWrapper(e, this));
		}
	}

	/*
	 * Returns the action constant for the action to perform based 
	 * on the POST parameters in the specified context.
	 */
	private int parseAction() {
		return ACTION_DEFAULT;
	}

	/*
	 * Adds the default form to the block.
	 */	
	private void viewDefaultForm(IWContext iwc) {
		ApplicationForm app = new ApplicationForm(this);
		app.setLocalizedTitle(KEY_TITLE, "Control list");
		app.setMainPanel(getControlList(iwc));
		app.setButtonPanel(getButtonPanel());
		add(app);
	}
	
	/*
	 * Returns the BatchControlList
	 */
	private ListTable getControlList(IWContext iwc) {

		ListTable list = new ListTable(this, 5);

		list.setLocalizedHeader(KEY_PROVIDER, "Provider", 1);
		list.setLocalizedHeader(KEY_NUM_INDIVIDUALS_PREL, "No of individuals Prel.", 2);
		list.setLocalizedHeader(KEY_LAST_MONTH, "Previous", 3);
		list.setLocalizedHeader(KEY_TOTAL_AMOUNT_PREL, "Total amount Prel.", 4);
		list.setLocalizedHeader(KEY_LAST_MONTH, "Previous", 5);

		Collection collection = null;
		try {
			collection = (Collection) getControlListBusiness(iwc).getControlListValues();
		} catch (RemoteException e) {}
		
		Iterator iter = collection.iterator();
		
		while (iter.hasNext()) {
			Object[] obj = (Object[]) iter.next();
			
			list.add(getSmallText((String)(obj[1])));
			list.add(getSmallText((String)(obj[2])));
			list.add(getSmallText((String)(obj[3])));
			list.add(getSmallText((String)(obj[4])));
			list.add(getSmallText((String)(obj[5])));
		}
		return list;
	}
	
	/*
	 * Returns the button panel for this block
	 */
	private ButtonPanel getButtonPanel() {
		ButtonPanel bp = new ButtonPanel(this);
		Link excelLink = null;
//		Link pdfLink = null;
		try {
			excelLink = getXLSLink(ControlListWriter.class, getBundle().getImage("shared/xls.gif"));
//			pdfLink = getPDFLink(ControlListWriter.class, getBundle().getImage("shared/pdf.gif"));
		} catch (RemoteException e) {}
		bp.add(excelLink);
//		bp.add(pdfLink); //Hmmm, this doesnt work for some reason /Kelly
		
		return bp;
	}
	
// TODO: Move these below to AccountingBlock
	
	public Link getXLSLink(Class classToUse,Image image) throws RemoteException {
		Link link = new Link(image);
		link.setWindow(getFileWindow());
		link.addParameter(ControlListWriter.prmPrintType, ControlListWriter.XLS);
		link.addParameter(ControlListWriter.PRM_WRITABLE_CLASS, IWMainApplication.getEncryptedClassName(classToUse));
		return link;
	}

	public Link getPDFLink(Class classToUse,Image image) throws RemoteException {
		Link link = new Link(image);
		link.setWindow(getFileWindow());
		link.addParameter(ControlListWriter.prmPrintType, ControlListWriter.PDF);
		link.addParameter(ControlListWriter.PRM_WRITABLE_CLASS, IWMainApplication.getEncryptedClassName(classToUse));
		return link;
	}

	public Window getFileWindow() {
		Window w = new Window(localize(KEY_MEDIA_WINDOW_TITLE, "Control list"), getIWApplicationContext().getApplication().getMediaServletURI());
		w.setResizable(true);
		w.setMenubar(true);
		w.setHeight(480);
		w.setWidth(640);
		return w;
	}

	private ControlListBusiness getControlListBusiness(IWContext iwc) throws RemoteException {
		return (ControlListBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ControlListBusiness.class);
	}	


}
