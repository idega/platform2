package se.idega.idegaweb.commune.presentation;

import is.idega.idegaweb.member.business.MemberFamilyLogic;
import is.idega.idegaweb.member.business.NoChildrenFound;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import se.idega.util.PIDChecker;

import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.Age;

/**
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */
public class CitizenChildren extends CommuneBlock {
	private IWBundle iwb;
	private IWResourceBundle iwrb;
	private int userID;
	private User user;
	private Text buttonLabel;
	private Text ssnLabel;
	private static final String prmChildId = "comm_child_id";
  private static final String prmParentId = "comm_parent_id";
	private static final String prmChildSSN = "comm_child_ssn";
	private String prmSubmitName = "submit_cits_child";
	private boolean showSSNSearchForm = false;
	private boolean showOutOfRangeChildren =true;
	private int fromAge =-1, toAge = 1000;


	public CitizenChildren() {
		buttonLabel = getText("");
		ssnLabel = getText("");
	}

	public void main(IWContext iwc) throws java.rmi.RemoteException {
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		userID = iwc.getUserId();
		if (userID > 0) {
			user = ((UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class)).getUser(userID);
			Table T = new Table();
			int row = 1;
			int col = 1;
			if (iwc.isParameterSet(prmSubmitName) && iwc.getParameter(prmSubmitName).equals("true")) {
				try {
					User child = processSSNRequest(iwc);
					T.add(getChildLink(child), col, row);
				}
				catch (javax.ejb.FinderException fix) {
					T.add(fix.getMessage(), col, row);
				}
				row++;
			}

				T.add(getChildrenForm(iwc));
			add(T);
		}
		else
			add(iwrb.getLocalizedString("citizen_children.no_citizen_logged_on", "You are not a citizen of Nacka Commune"));
	}

	public static String getChildIDParameterName() {
		return prmChildId;
	}

  public static String getParentIDParameterName() {
		return prmParentId;
	}

	public static String getChildSSNParameterName() {
		return prmChildId;
	}

	public static Parameter getChildIDParameter(int child_id) {
		return new Parameter(prmChildId, String.valueOf(child_id));
	}

	public static Parameter getChildSSNParameter(int child_ssn) {
		return new Parameter(prmChildSSN, String.valueOf(child_ssn));
	}

	public void setLocalizedButtonLabel(String localeString, String text) {
		buttonLabel.setLocalizedText(localeString, text);
	}

	private PresentationObject getChildrenForm(IWContext iwc) throws java.rmi.RemoteException {
		Form f = new Form();
		Table T = new Table();
		int row = 1;
		Collection childs = getChilds(iwc, this.user);
		if (!childs.isEmpty()) {
			Iterator iter = childs.iterator();
			User child;
			RadioButton rad;
			ArrayList outOfRangeChilds = new ArrayList();
			while (iter.hasNext()) {
				child = (User) iter.next();
				if (child.getDateOfBirth() != null) {
					Age age = null;
					if (child.getDateOfBirth() != null)
						age = new Age(child.getDateOfBirth());
					else
						age = new Age(PIDChecker.getInstance().getDateFromPersonalID(child.getPersonalID()));
					if(age.getYears() <= toAge && age.getYears() >=fromAge){
						//rad = new RadioButton(prmChildId, ((Integer) user.getPrimaryKey()).toString());
						T.add(getChildLink(user), 1, row);
						row++;
					}
					else{
						outOfRangeChilds.add(child);
					}
				}else {
					outOfRangeChilds.add(child);
				}
			}
			if(showOutOfRangeChildren && !outOfRangeChilds.isEmpty()){
				T.add(getHeader(iwrb.getLocalizedString("citizen_children.out_of_range_childs","Children out of range:")),1,row);
				Iterator iter2 = outOfRangeChilds.iterator();
				++row;
				while(iter2.hasNext()){
					child = (User) iter2.next();
					T.add(child.getName(),1,row++);
				}
			}

		}

		if(showSSNSearchForm){
			TextInput inputSSN = new TextInput(prmChildSSN);
			String label = buttonLabel.getLocalizedText(iwc);

			SubmitButton submit = new SubmitButton(label, prmSubmitName, "true");
			T.add(inputSSN, 1, row);
			row++;
			T.add(submit, 1, row);
		}
		f.add(T);
		return f;
	}

	private Link getChildLink(User child) throws java.rmi.RemoteException {
		Link L = new Link(child.getName());
		if (getResponsePage() != null)
			L.setPage(getResponsePage());
		L.addParameter(prmChildId, ((Integer) child.getPrimaryKey()).toString());
		return L;
	}

	private User processSSNRequest(IWContext iwc) throws javax.ejb.FinderException, java.rmi.RemoteException {
		String ssn = iwc.getParameter(prmChildSSN);
		if (ssn != null && !ssn.equals("")) {
			UserHome userHome = (UserHome) IDOLookup.getHome(User.class);
			User child = userHome.findByPersonalID(ssn);
			if (child != null) {
				return child;
			}
		}
		throw new javax.ejb.FinderException("No user with that ssn");
	}

	private Collection getChilds(IWContext iwc, User user) throws RemoteException {
		/** @todo familymethods from usersystem */
		MemberFamilyLogic ml = (MemberFamilyLogic) IBOLookup.getServiceInstance(iwc, MemberFamilyLogic.class);
		try {
			return ml.getChildrenFor(user);
		}
		catch (RemoteException e) {
		}
		catch (NoChildrenFound e) {
		}
		return new Vector();
	}

	/**
	 * Sets if the component is to show the search for to search by personal_id.
	 * <br>This defaults to false
	 **/
	public void setShowSSNSearchForm( boolean showForm){
		this.showSSNSearchForm = showForm;
	}
	
	/**
	 * Sets if the component is to set children age range.
	 * <br>This defaults to the range -1 to 1000
	 **/
	public void setAgeRange(int from,int to){
		this.fromAge = from;
		this.toAge = to;
	}
	
	/**
	 * Sets if the component is to show the children that dont fit specified range of age.
	 * <br>This defaults to true
	 **/
	public void setShowOutOfRangeChilds(boolean show){
		this.showOutOfRangeChildren = show;
	}
}