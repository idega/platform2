package se.idega.idegaweb.commune.childcare.presentation;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.childcare.data.ChildCareQueue;
import se.idega.idegaweb.commune.childcare.data.ChildCareQueueHomeImpl;
import se.idega.idegaweb.commune.presentation.CitizenChildren;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.builder.data.IBPage;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;
/**
 * ChildCareQueueUpdateTable
 * @author <a href="mailto:joakim@idega.is">Joakim</a>
 * @version $Id: ChildCareQueueUpdateTable.java,v 1.4 2003/04/25 12:31:46 laddi Exp $
 * @since 12.2.2003 
 */
public class ChildCareQueueUpdateTable extends CommuneBlock {

	private final static String[] SUBMIT = new String[] { "ccot_submit", "Next" };
	private final static String[] CANCEL = new String[] { "ccot_cancel", "Cancel" };
	public final static int PAGE_1 = 1;
	public final static int PAGE_2 = 2;
	public final static int PAGE_3 = 3;
	public final static String STATUS_UBEH = "UBEH";
	private String prmChildId = CitizenChildren.getChildIDParameterName();
	private IBPage _endPage;

	/**
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		Form form = new Form();
		Table layoutTbl = new Table();
		switch (parseAction(iwc)) {
			case CCConstants.ACTION_SUBMIT_1 :
				iwc.setSessionAttribute(CCConstants.SESSION_ACCEPTED_STATUS, getAcceptedStatus(iwc));
				break;
			case CCConstants.ACTION_SUBMIT_CONFIRM :
				createPagePhase1(iwc, layoutTbl);
				break;
			case CCConstants.ACTION_CANCEL_1 :
				iwc.forwardToIBPage(getParentPage(), getEndPage());
				break;
			case CCConstants.ACTION_SUBMIT_2 :
				handleAcceptStatus((List) iwc.getSessionAttribute(CCConstants.SESSION_ACCEPTED_STATUS));
				handleKeepQueueStatus(getKeepInQueue(iwc));
				iwc.forwardToIBPage(getParentPage(), getEndPage());
				break;
			case CCConstants.ACTION_CANCEL_2 :
				iwc.forwardToIBPage(getParentPage(), getEndPage());
				break;
			case CCConstants.ACTION_REQUEST_INFO :
				createRequestInfoConfirmPage(layoutTbl);
				break;
			case CCConstants.ACTION_CANCEL_3 :
				break;
			default :
				createPagePhase1(iwc, layoutTbl);
		}
		form.add(layoutTbl);
		add(form);
	}
	
	/**
	 * Method handleKeepQueueStatus.
	 * @param iwc
	 * @param l
	 * @throws RemoteException
	 * @throws RemoveException
	 */
	private void handleKeepQueueStatus(List l) throws RemoteException, RemoveException {
		Iterator i = l.iterator();
		while (i.hasNext()) {
			String[] status = (String[]) i.next();
			if (status[0] != null) {
				if (status[1] != null && status[1].equals(CCConstants.NO)) {
				}
			}
		}
	}

	/**
	* Method handleAcceptStatus.
	* @param iwc
	* @param l
	* @throws RemoteException
	*/
	private void handleAcceptStatus(List l) throws RemoteException {
		Iterator i = l.iterator();
		while (i.hasNext()) {
			AcceptedStatus status = (AcceptedStatus) i.next();
			if (status.isDefined()) {
				if (status.equals(CCConstants.YES)) {
					System.out.println("Accepting application.");
				}
				else if (status.equals(CCConstants.NO_NEW_DATE)) {
				}
				else if (status.equals(CCConstants.NO)) {
				}
			}
		}
	}

	/**
	 * Method getAcceptedStatus.
	 * @param iwc
	 * @return List of AcceptedStatus objects
	 */
	private List getAcceptedStatus(IWContext iwc) {
		List list = new ArrayList();
		int i = 1;
		while (iwc.isParameterSet(CCConstants.APPID + i)) {
			list.add(new AcceptedStatus(iwc.getParameter(CCConstants.APPID + i), iwc.getParameter(CCConstants.ACCEPT_OFFER + i), iwc.getParameter(CCConstants.NEW_DATE + i + "_day"), iwc.getParameter(CCConstants.NEW_DATE + i + "_month"), iwc.getParameter(CCConstants.NEW_DATE + i + "_year")));
			i++;
		}
		return list;
	}
	private class AcceptedStatus {
		String _appid, _status;
		Date _date;
		AcceptedStatus(String appId, String status, String day, String month, String year) {
			_appid = appId;
			_status = status;
			if (day != null && month != null && year != null) {
				IWTimestamp stamp = new IWTimestamp(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
				try {
					_date = stamp.getDate();
				}
				catch (IllegalArgumentException ex) {
					_date = new Date(0);
				}
			}
		}
		boolean equals(String status) {
			return _status.equals(status);
		}
		boolean isAccepted() {
			return _status != null && _status.equals(CCConstants.YES);
		}
		boolean isRejected() {
			return _status != null && _status.equals(CCConstants.NO);
		}
		boolean isRejectedNewDate() {
			return _status != null && _status.equals(CCConstants.NO_NEW_DATE);
		}
		boolean isDefined() {
			return _status != null;
		}
	}
	
	/**
	 * Method getKeepInQueue.
	 * @param iwc
	 * @return List of String arrays of length two. Index 0 is application id,
	 * index 1 is keep status.
	 */
	private List getKeepInQueue(IWContext iwc) {
		List list = new ArrayList();
		int i = 1;
		while (iwc.isParameterSet(CCConstants.APPID + i)) {
			list.add(new String[] { iwc.getParameter(CCConstants.APPID + i), iwc.getParameter(CCConstants.KEEP_IN_QUEUE + i)});
			i++;
		}
		add(new Text("Length:" + list.size()));
		return list;
	}
	
	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(CCConstants.ACTION)) {
			return Integer.parseInt(iwc.getParameter(CCConstants.ACTION));
		}
		else if (iwc.isParameterSet(ChildCareQueueTable.REQUEST_INFO[0])) {
			return CCConstants.ACTION_REQUEST_INFO;
		}
		return CCConstants.NO_ACTION;
	}
	
	private void createRequestInfoConfirmPage(Table layoutTbl) throws RemoteException {
		SubmitButton submitBtn = new SubmitButton(localize(SUBMIT), CCConstants.ACTION, new Integer(CCConstants.ACTION_SUBMIT_CONFIRM).toString());
		submitBtn.setAsImageButton(true);
		layoutTbl.add(new Text("Your request has been sent."), 1, 1);
		layoutTbl.add(submitBtn, 1, 2);
		layoutTbl.setAlignment(1, 2, "right");
	}

	/**
	 * Creates the object for the first step of the selection process
	 * @param iwc
	 * @param layoutTbl
	 * @throws RemoteException
	 */
	private void createPagePhase1(IWContext iwc, Table layoutTbl) throws RemoteException {
		Collection choices = findChoices(iwc);
		if (choices.size() == 0) {
			layoutTbl.add(new Text("No choices have been made for this person."));
		}
		else {
			Table choiceTable = new ChildCareQueueTable(this, sortApplications(choices, false));
			SubmitButton submitBtn = new SubmitButton(localize(SUBMIT), CCConstants.ACTION, new Integer(CCConstants.ACTION_SUBMIT_1).toString());
			submitBtn.setAsImageButton(true);
			SubmitButton cancelBtn = new SubmitButton(localize(CANCEL), CCConstants.ACTION, new Integer(CCConstants.ACTION_CANCEL_1).toString());
			cancelBtn.setAsImageButton(true);
			layoutTbl.add(choiceTable, 1, 1);
			layoutTbl.add(submitBtn, 1, 3);
			layoutTbl.add(cancelBtn, 1, 3);
			layoutTbl.setAlignment(1, 3, "right");
			layoutTbl.add(getHelpTextPage1(), 1, 4);
			layoutTbl.setStyle(1, 4, "padding-top", "15px");
		}
	}

	/**
	 * Method findApplications.
	 * @param iwc
	 * @return Collection
	 */
	private Collection findChoices(IWContext iwc) {
		Collection choices = null;
		try {
			int childId = Integer.parseInt(iwc.getParameter(prmChildId));
			ChildCareQueueHomeImpl ccqHome = (ChildCareQueueHomeImpl) IDOLookup.getHome(ChildCareQueue.class);
			choices = ccqHome.findQueueByChild(childId);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (NumberFormatException e) {
			return new ArrayList();
		}
		catch (NullPointerException e) {
			return new ArrayList();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		return choices;
	}

	/**
	 * Method sortApplications.
	 * @param apps
	 * @param offerFirst true means that granted application is placed first
	 * @return SortedSet
	 */
	public SortedSet sortApplications(Collection apps, boolean grantedFirst) {
		SortedSet set = new TreeSet();
		Iterator i = apps.iterator();
		while (i.hasNext()) {
			set.add(new ComparableQueue(i.next(), grantedFirst));
		}
		return set;
	}

	/**
	 * Method getLocalHeader is used from classes that doens't subclass
	 * CommuneBlock, but has a refernce to an object of this class.
	 * @param key
	 * @param defaultValue
	 * @return Text
	 */
	public Text getLocalHeader(String key, String defaultValue) {
		return getSmallHeader(localize(key, defaultValue));
	}

	public void setEndPage(IBPage page) {
		_endPage = page;
	}

	public IBPage getEndPage() {
		return _endPage;
	}

	private Table getHelpTextPage1() {
		Table tbl = new Table(1, 1);
		tbl.setWidth(1, 1, 700);
		Text t = getLocalizedSmallText("ccatp1_help", "Om du accepterar erbjudande kan du enbart kvarstå i kö till i de ovanstående valen. Du stryks automatiskt från de underliggande alternativen. Om ditt erbjudande gäller ditt förstahandsval har du möjlighet att välja att kvarstå i kö för ETT alternativ av de underliggande alternativen.");
		t.setItalic(true);
		tbl.add(t);
		return tbl;
	}
}