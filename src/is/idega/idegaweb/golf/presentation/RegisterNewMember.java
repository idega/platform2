package is.idega.idegaweb.golf.presentation;

import is.idega.idegaweb.golf.access.AccessControl;
import is.idega.idegaweb.golf.access.LoginTable;
import is.idega.idegaweb.golf.access.LoginTableHome;
import is.idega.idegaweb.golf.entity.Address;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.MemberInfoHome;
import is.idega.idegaweb.golf.entity.Phone;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author gimmi
 */
public class RegisterNewMember extends GolfBlock {

	public static final String PARAMETER_PERSONAL_ID = "kt";
	private MemberHome mHome;
	private LoginTableHome ltHome;

	public void main(IWContext iwc) throws Exception {
		String kt = iwc.getParameter(PARAMETER_PERSONAL_ID);
		//	boolean needToLogin = false;

		mHome = (MemberHome) IDOLookup.getHome(Member.class);
		ltHome = (LoginTableHome) IDOLookup.getHome(LoginTable.class);

		if (kt != null) {
			Member member = null;
			try {
				member = mHome.findBySSN(kt);
			}
			catch (FinderException f) {
			}

			String action = iwc.getParameter("action");

			if (action == null) {
				action = "formid";
			}

			if (action.equals("formid")) {
				if (member != null) {
					LoginTable lTable = null;
					try {
						lTable = ltHome.findByMember(member);
					}
					catch (FinderException f) {
					}

					if (lTable != null) {
						login(member, lTable, kt);
					}
					else {
						form(member);
					}
				}
				else {
					nobodyWasFound(kt);
				}
			}
			else if (action.equals("submitted")) {
				update(iwc, member);
			}
			else if (action.equals("afram")) {
				verifyPassword(iwc);
			}
		}
		else {
			add(getSmallText(localize("need_a_personal_id", "Need a personal ID")));
		}
	}

	/**
	 * Return memberID when valid, -1 when username is invalid, -2 when invalid password, -3 when ssn is invalid, -4 other error;
	 * 
	 * @throws FinderException
	 */
	public int checkPassword(IWContext iwc) throws FinderException {

		String login = iwc.getParameter("login");
		String password = iwc.getParameter("password");
		String kt = iwc.getParameter("kt");

		boolean returner = false;
		Collection coll = ltHome.findByUserLogin(login);

		if (coll != null) {
			Iterator iter = coll.iterator();
			LoginTable lTable;
			Member member;
			while (iter.hasNext()) {
				lTable = (LoginTable) iter.next();
				if (lTable.getUserPassword().equals(password)) {
					member = mHome.findByPrimaryKey(lTable.getMemberId());
					if (member.getSocialSecurityNumber().equals(kt)) {
						return member.getID();
					}
					else {
						return -3;
					}
				}
				else {
					return -2;
				}
			}
		}
		else {
			return -1;
		}
		return -4;
	}

	public void verifyPassword(IWContext iwc) throws FinderException, IOException, SQLException {

		int passwStatus = checkPassword(iwc);
		String kt = iwc.getParameter("kt");

		switch (passwStatus) {
			case -1:
				VillaIInnslaetti(kt, "login2");
				break;
			case -2:
				VillaIInnslaetti(kt, "wrong_password");
				break;
			case -3:
				VillaIInnslaetti(kt, "wrong_ssn");
				break;
			case -4:
				break;
			default:
				form(mHome.findByPrimaryKey(passwStatus));
				break;
		}
	}

	public void login(Member member, LoginTable logTable, String kt) {
		Form myForm = new Form();

		Table myTable = new Table(2, 3);
		myTable.setBorder(0);
		myTable.setCellpadding(3);
		myTable.setCellspacing(0);

		myTable.add(getHeader(localize("user", "User")), 1, 1);
		myTable.add(getHeader(localize("password", "Password")), 1, 2);

		myTable.add((TextInput) getStyledInterface(new TextInput("login")), 2, 1);
		myTable.add((PasswordInput) getStyledInterface(new PasswordInput("password")), 2, 2);
		myTable.add(new HiddenInput("kt", kt), 1, 3);
		myTable.add(new HiddenInput("action", "afram"), 1, 3);

		myTable.add(getButton(new SubmitButton(localize("next", "Next"))), 1, 3);
		myTable.mergeCells(1, 3, 2, 3);

		myForm.add(myTable);
		add(myForm);
	}

	public boolean checkForUpdate(Member member, String login) throws FinderException {
		if (login == null) { return false; }
		Collection coll = ltHome.findByUserLogin(login);

		if (coll.isEmpty() || coll == null) {
			return true;
		}
		else if (coll.size() == 1) {
			LoginTable lTable = ltHome.findByMember(member);
			if (lTable != null) {
				if (login.equals(lTable.getUserLogin())) { return true; }
			}
		}

		return false;
	}

	public void update(IWContext iwc, Member member) throws SQLException, FinderException, IDOLookupException, CreateException {

		String login = iwc.getParameter("login");
		String personalID = iwc.getParameter("social_security_number");

		boolean isLoginValid = true;
		boolean updateLogin = false;
		boolean isPasswordValid = false;

		String password = iwc.getParameter("password");
		String retypePassword = iwc.getParameter("password2");

		if (!(login.equals(""))) {
			try {
				isLoginValid = checkForUpdate(member, login);

			}
			catch (FinderException e) {
				isLoginValid = false;
//				e.printStackTrace();
			}
			updateLogin = isLoginValid;
		}

		if (isLoginValid) {
			if (retypePassword.equals("") || password.equals("")) {
				updateLogin = false;
			}
			else {
				if (password.equals(retypePassword)) {
					isPasswordValid = true;
				}
				else {
					isPasswordValid = false;
				}
			}

			if (isPasswordValid || !updateLogin) {
				LoginTable lTable = null;
				try {
					lTable = ltHome.findByMember(member);
				}
				catch (FinderException ignore) {
				}


				///////-------------Login-----------////////////////////////////////
				if (updateLogin) {
					if (lTable != null) {
						lTable.setMemberId(member.getID());
						lTable.setUserLogin(login);
						lTable.setUserPassword(password);
						lTable.store();
						//Syst em.out.println("createLogin : update : updatingLogin");
					}
					else {
						LoginTable logTable = ltHome.create();
						logTable.setMemberId(member.getID());
						logTable.setUserLogin(login);
						logTable.setUserPassword(password);
						logTable.store();
						//Syst em.out.println("createLogin : update : creatingLogin");
					}
				}
				//////////////////////////////// Login buid


				////////-------Handicap-----------//////////////////
				String handicap = iwc.getParameter("handicap");
				float floatHandicap;
				boolean villa = false;

				if (handicap.equals("")) {
					handicap = "36";
				}
				handicap.replace(',', '.');

				try {
					floatHandicap = Float.parseFloat(handicap);
				}
				catch (NumberFormatException n) {
					villa = true;
				}
				MemberInfoHome miHome = (MemberInfoHome) IDOLookup.getHome(MemberInfo.class);
				if (!(villa)) {
					MemberInfo mInfo = null;
					try {
						mInfo = miHome.findByMember(member);
					}
					catch (FinderException ignore) {
					}
					if (mInfo != null) {
						mInfo.setFirstHandicap(Float.parseFloat(handicap));
						mInfo.store();
					}
					else {
						MemberInfo memberInfo = miHome.create();
						memberInfo.setFirstHandicap(Float.parseFloat(handicap));
						memberInfo.setMemberId(member.getID());
						memberInfo.store();
					}
				}
				if (villa) {
					VillaIInnslaetti(personalID, "handicap");
				}
				
				////////////////////////////////////// Handicap buid

				//	bool ean villa= false;
				if (!villa) {
					completed(iwc, member);
				}

			}
			else {
				VillaIInnslaetti(personalID, "pass");
			}

		}
		else { //ef loginEkkiILagi
			VillaIInnslaetti(personalID, "login");
		}

	}

	public void completed(IWContext iwc, Member member) {

		Table myTable = new Table(1, 3);
		myTable.setBorder(0);
		myTable.setCellpaddingAndCellspacing(0);
		myTable.setHeight(2, 12);

		myTable.add(getHeader(localize("registration_complete", "Registration complete")), 1, 1);
		Link link = getLink(localize("click_here_for_start_page", "Click here to return to start page"));
		try {
			link.setPage(getBuilderService(iwc).getRootPage());
		}
		catch (RemoteException re) {
			log(re);
		}
		myTable.add(link, 1, 3);//"smellið hér til að fara á upphafssíðuna","/index.jsp"),1,2);

		add(myTable);

		iwc.getSession().setAttribute("member_login", member);
		AccessControl.internalSetState(iwc, "loggedon");
	}

	public void VillaIInnslaetti(String kt, String comment) {
		Table myTable = new Table(1, 5);
		myTable.setBorder(0);
		myTable.setCellpaddingAndCellspacing(0);
		myTable.setHeight(2, 12);
		myTable.setHeight(4, 12);

		myTable.add(getHeader(localize("input_error", "Input error")), 1, 1);
		if (comment.equals("pass")) {
			myTable.add(getText(localize("passwords_not_the_same", "Passwords were not the same")), 1, 3);
		}
		else if (comment.equals("login")) {
			myTable.add(getText(localize("login_in_use", "Login is in use")), 1, 3);
		}
		else if (comment.equals("wrong_password")) {
			myTable.add(getText(localize("incorrect_password", "Incorrect password")), 1, 3);
		}
		else if (comment.equals("handicap")) {
			myTable.add(getText(localize("incorrect_handicap", "Incorrect handicap")), 1, 3);
		}

		myTable.add(new BackButton(localize("back", "Back")), 1, 5);
		add(myTable);
	}

	public void nobodyWasFound(String kt) throws IOException {

		Table myTable = new Table();
		myTable.setAlignment(1, 1, "center");
		myTable.setBorder(0);
		myTable.setCellpaddingAndCellspacing(0);
		myTable.add(getHeader(localize("member_not_found", "No member was found (kt: " + kt + ")")));
		add(myTable);
	}

	public void form(Member member) throws IOException, SQLException, FinderException {

		MemberInfoHome miHome = (MemberInfoHome) IDOLookup.getHome(MemberInfo.class);
		
		MemberInfo mInfo = null;
		try {
			mInfo = miHome.findByMember(member);
		}
		catch (FinderException e1) {
		}
		Address[] address = member.getAddress();
		Phone[] phone = member.getPhone();

		Form myForm = new Form();

		Table myTable = new Table();
		myForm.add(myTable);
		myTable.setBorder(0);
		myTable.setCellpadding(3);
		myTable.setCellspacing(0);
		myTable.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;

		myTable.add(new HiddenInput("member_id", Integer.toString(member.getID())), 1, row);
		// For checkpassword to work we need this (kt) hiddenInput
		myTable.add(new HiddenInput("kt", member.getSocialSecurityNumber()), 1, row);

		String fullName = member.getName();
		String socialSecurityNumber = member.getSocialSecurityNumber();
		if (socialSecurityNumber == null) {
			socialSecurityNumber = "";
		}

		Text name = getText(fullName);
		myTable.add(new HiddenInput("social_security_number", socialSecurityNumber), 1, 1);

		myTable.add(getHeader(localize("name", "Name")), 1, row);
		myTable.add(name, 2, row++);

		
		//////----------Handicap------------/////////////////////////////////////////////////////////
		if (mInfo == null) {
			myTable.add(getHeader(localize("handicap", "Handicap")), 1, row);
			TextInput handicap = (TextInput) getStyledSmallInterface(new TextInput("handicap", ""));
			handicap.setSize(4);
			myTable.add(handicap, 2, row++);
		}
		else if (mInfo != null) {
			myTable.add(getHeader(localize("handicap", "Handicap")), 1, row);
			myTable.add(getText(Float.toString(mInfo.getFirstHandicap())), 2, row);
			myTable.add(new HiddenInput("handicap", Float.toString(mInfo.getFirstHandicap())), 2, row++);
		}
		//////////////////////////////////////////////////////////////// Handicap endar

		////////------Login / Password-----------//////////////////////////////////////////////////////////
		LoginTable lTable = null;
		try {
			lTable = ltHome.findByMember(member);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		row++;
		if (lTable != null) {
			TextInput login = (TextInput) getStyledSmallInterface(new TextInput("login", lTable.getUserLogin()));
			PasswordInput password = (PasswordInput) getStyledSmallInterface(new PasswordInput("password", lTable.getUserPassword()));
			myTable.add(getHeader(localize("user_name", "User name")), 1, row);
			myTable.add(login, 2, row++);
			myTable.add(getHeader(localize("password", "Password")), 1, row);
			myTable.add(password, 2, row++);
		}
		else {
			TextInput login = (TextInput) getStyledSmallInterface(new TextInput("login"));
			PasswordInput password = (PasswordInput) getStyledSmallInterface(new PasswordInput("password"));
			myTable.add(getHeader(localize("user_name", "User name")), 1, row);
			myTable.add(login, 2, row++);
			myTable.add(getHeader(localize("password", "Password")), 1, row);
			myTable.add(password, 2, row++);
		}
		PasswordInput password2 = (PasswordInput) getStyledSmallInterface(new PasswordInput("password2"));
		myTable.add(getHeader(localize("retype_password", "Retype password")), 1, row);
		myTable.add(password2, 2, row++);
		//////////////////////////////////////////////////////////////////// Login/Password endar

		row = myTable.getRows() + 1;
		SubmitButton submit = new SubmitButton(localize("save", "Save"));
		myTable.mergeCells(1, row, 2, row);
		myTable.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		myTable.add(submit, 1, row);
		myTable.add(new HiddenInput("action", "submitted"), 1, 1);

		add(myForm);
	}
}