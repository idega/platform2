package is.idega.idegaweb.golf.presentation;

import is.idega.idegaweb.golf.block.login.business.LoginBusiness;
import is.idega.idegaweb.golf.block.login.data.LoginTable;
import is.idega.idegaweb.golf.block.login.data.LoginTableHome;
import is.idega.idegaweb.golf.entity.Address;
import is.idega.idegaweb.golf.entity.AddressHome;
import is.idega.idegaweb.golf.entity.Country;
import is.idega.idegaweb.golf.entity.CountryHome;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import is.idega.idegaweb.golf.entity.MemberInfo;
import is.idega.idegaweb.golf.entity.MemberInfoHome;
import is.idega.idegaweb.golf.entity.Phone;
import is.idega.idegaweb.golf.entity.PhoneHome;
import is.idega.idegaweb.golf.entity.ZipCode;
import is.idega.idegaweb.golf.entity.ZipCodeHome;

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
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.text.Name;

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
				String membr_id = iwc.getParameter("member_id");
				String[] address_id = iwc.getParameterValues("address_id");
				String[] zipcode_id = iwc.getParameterValues("zipcode_id");
				update(iwc, member, address_id, zipcode_id);
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

		myTable.add(new SubmitButton("action", "afram"), 2, 3);
		myTable.setAlignment(2, 3, Table.HORIZONTAL_ALIGN_RIGHT);

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

	public void update(IWContext iwc, Member member, String[] addressIDs, String[] zipcodeIDs) throws SQLException, FinderException, IDOLookupException, CreateException {

		String login = iwc.getParameter("login");
		String personalID = iwc.getParameter("social_security_number");

		String[] phoneIDs = iwc.getParameterValues("phone_id");
		String[] phoneCountryIDs = iwc.getParameterValues("phone_country_id");

		boolean isLoginValid = true;
		boolean updateLogin = false;
		boolean isPasswordValid = false;

		String password = iwc.getParameter("password");
		String retypePassword = iwc.getParameter("password2");

		if (!(login.equals(""))) {
			isLoginValid = checkForUpdate(member, login);
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

				String name = iwc.getParameter("first_name");
				if (name == null) {
					name = "";
				}
				Name userName = new Name(name);

				String[] street = iwc.getParameterValues("street");
				String[] zipcode = iwc.getParameterValues("zipcode");
				String[] country_id = iwc.getParameterValues("country_id");
				String email = iwc.getParameter("email");
				if (email == null) {
					email = "";
				}

				member.setFirstName(userName.getFirstName());
				member.setMiddleName(userName.getMiddleName());
				member.setLastName(userName.getLastName());
				member.setSocialSecurityNumber(personalID);
				member.setEmail(email);

				/////--------------Siminn----------///////////////////////
				String[] siminn = iwc.getParameterValues("phone");
				PhoneHome pHome = (PhoneHome) IDOLookup.getHome(Phone.class);
				if (phoneIDs == null) {
					String simiString = (String) siminn[0];
					if (!(siminn[0].equals(""))) {
						Phone phone = pHome.create();
						phone.setNumber(siminn[0]);
						phone.setCountryId(new Integer(Integer.parseInt(phoneCountryIDs[0])));
						phone.store();
						phone.addTo(member);
					}
				}
				else {
					for (int kill = 0; kill < phoneIDs.length; kill++) {
						String simiString = (String) siminn[kill];
						if (!(siminn[kill].equals(""))) {
							Phone phone = pHome.findByPrimaryKey(Integer.parseInt(phoneIDs[kill]));
							//Phone phone = new Phone(Integer.parseInt(phone_id[kill]));
							phone.setNumber(siminn[kill]);
							if (phoneCountryIDs != null) {
								phone.setCountryId(new Integer(Integer.parseInt(phoneCountryIDs[kill])));
							}
							phone.update();
						}
					}
				}
				////////////////////////// Siminn buinn

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

				member.update();

				///////////////-------------Card------------////////////////////////
				/*
				 * if (isCardValid) { Card[] cardLength = (Card[])member.findReverseRelated(new Card()); if (cardLength.length > 0 ) { idegaTimestamp idegaTime = new idegaTimestamp(); idegaTime.setDate(1); idegaTime.setMonth(Integer.parseInt(cc_month)-1); idegaTime.setYear(Integer.parseInt(cc_year)+2000); cardLength[0].setCardNumber(creditcard_number); cardLength[0].setExpireDate(idegaTime.getSQLDate()); cardLength[0].update(); } else { Card card = new Card(); idegaTimestamp idegaTime = new idegaTimestamp(); idegaTime.setDate(1); idegaTime.setMonth(Integer.parseInt(cc_month)-1); idegaTime.setYear(Integer.parseInt(cc_year)+2000); card.setCardNumber(creditcard_number); card.setExpireDate(idegaTime.getSQLDate()); card.insert(); card.addTo(member); } }
				 */
				/////////////////////////////////////// Card buid

				/////////-----------Address-----------/////////////////////////
				boolean zipcodeVilla = false;
				AddressHome aHome = (AddressHome) IDOLookup.getHome(Address.class);
				ZipCodeHome zHome = (ZipCodeHome) IDOLookup.getHome(ZipCode.class);
				if (addressIDs != null) {
					for (int g = 0; g < addressIDs.length; g++) {
						try {
							int zipcodeInt = Integer.parseInt(zipcode[g]);
						}
						catch (NumberFormatException n) {
							zipcodeVilla = true;
						}

						if (!(zipcodeVilla)) {
							Address address = aHome.findByPrimaryKey(Integer.parseInt(addressIDs[g]));
							address.setStreet(street[g]);
							//address.setStreetNumber(street_number[g]);
							if (country_id != null) {
								address.setCountryId(Integer.parseInt(country_id[g]));
							}
							ZipCode code = zHome.findByCode(zipcode[g]);
							//ZipCode[] zipcode_arr = (ZipCode[])(new ZipCode()).findAllByColumn("code",zipcode[g]);
							address.setZipcodeId(code.getID());
							address.update();
						}
					}
				}

				if (addressIDs == null) {
					try {
						int zipcodeInt = Integer.parseInt(zipcode[0]);
					}
					catch (NumberFormatException n) {
						zipcodeVilla = true;
					}
					if (!(zipcodeVilla)) {
						Address address = aHome.create();
						if (street[0] != null) address.setStreet(street[0]);
						//if (street_number[0] != null)
						//address.setStreetNumber(street_number[0]);
						if (country_id[0] != null) address.setCountryId(Integer.parseInt(country_id[0]));

						if (zipcode[0] != null) {
							ZipCode code = zHome.findByCode(zipcode[0]);
							//ZipCode[] zipcode_arr = (ZipCode[])(new ZipCode()).findAllByColumn("code",zipcode[0]);
							address.setZipcodeId(code.getID());
						}
						address.store();
						address.addTo(member);
					}
				}
				//////////////////////////// Address buid

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
					//MemberInfo[] memberInfoLength = (MemberInfo[])(new MemberInfo()).findAllByColumn("member_id",member_id);
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
				if (zipcodeVilla) {
					VillaIInnslaetti(personalID, "zipcode");
				}
				////////////////////////////////////// Handicap buid

				//	bool ean villa= false;
				if ((!(villa)) && (!(zipcodeVilla))) {
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
		LoginBusiness.internalSetState(iwc, "loggedon");

	}

	public void VillaIInnslaetti(String kt, String comment) {
		Table myTable = new Table(1, 5);
		myTable.setBorder(0);
		myTable.setCellpaddingAndCellspacing(0);
		myTable.setHeight(2, 12);
		myTable.setHeight(4, 12);

		myTable.add(getHeader(localize("input_error", "Input error")), 1, 1);
		if (comment.equals("pass")) {
			myTable.add(getText(localize("passwords_not_the_save", "Passwords were not the save")), 1, 3);
		}
		else if (comment.equals("login")) {
			myTable.add(getText(localize("login_in_use", "Login is in use")), 1, 3);
		}
		else if (comment.equals("zipcode")) {
			myTable.add(getText(localize("incorrect_zip_code", "Incorrect zip code")), 1, 3);
		}
		else if (comment.equals("login2")) {
			myTable.add(getText(localize("registration_failed", "Registration failed")), 1, 3);
		}
		else if (comment.equals("wrong_ssn")) {
			myTable.add(getText(localize("incorrect_pid", "Incorrect personal id")), 1, 3);
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
		MemberInfo mInfo = miHome.findByMember(member);
		Address[] address = member.getAddress();
		Phone[] phone = member.getPhone();

		Form myForm = new Form();

		Table myTable = new Table();
		myForm.add(myTable);
		myTable.setBorder(0);
		myTable.setCellpadding(3);
		myTable.setCellspacing(0);
		int row = 1;

		myTable.add(new HiddenInput("member_id", Integer.toString(member.getID())), 1, row);
		// For checkpassword to work we need this (kt) hiddenInput
		myTable.add(new HiddenInput("kt", member.getSocialSecurityNumber()), 1, row);

		String fullName = member.getName();
		String socialSecurityNumber = member.getSocialSecurityNumber();
		if (socialSecurityNumber == null) {
			socialSecurityNumber = "";
		}

		TextInput name = (TextInput) getStyledSmallInterface(new TextInput("first_name", fullName));
		myTable.add(new HiddenInput("social_security_number", socialSecurityNumber), 1, 1);

		myTable.add(getHeader(localize("name", "Name")), 1, row);
		myTable.add(name, 2, row++);

		////////-----------Address--------------////////////////////////////////////////////////
		if (address.length == 0) {
			TextInput gata = (TextInput) getStyledSmallInterface(new TextInput("street", ""));
			TextInput postnumer = (TextInput) getStyledSmallInterface(new TextInput("zipcode", ""));
			postnumer.setSize(3);
			postnumer.setMaxlength(3);
			DropdownMenu land = (DropdownMenu) getStyledSmallInterface(new DropdownMenu("country_id"));
			CountryHome cHome = (CountryHome) IDOLookup.getHome(Country.class);
			try {
				Collection coll = cHome.findAll();
				if (coll != null && coll.isEmpty()) {
					Iterator iter = coll.iterator();
					Country country;
					while (iter.hasNext()) {
						country = (Country) iter.next();
						land.addMenuElement(country.getID(), country.getName());
					}
				}
			}
			catch (FinderException e) {
			}

			myTable.add(getHeader(localize("street", "Street")), 1, row);
			myTable.add(gata, 2, row++);
			myTable.add(getHeader(localize("zip_code", "Zip Code")), 1, row);
			myTable.add(postnumer, 2, row++);
			myTable.add(getHeader(localize("country", "Country")), 1, row);
			myTable.add(land, 2, row++);
		}

		int address_id_int;
		int zipcode_id_int;
		String street_str;
		String zipcode_str;

		for (int j = 0; j < address.length; j++) {
			address_id_int = address[j].getID();
			zipcode_id_int = address[j].getZipcodeId();
			street_str = address[j].getStreet();
			if (street_str == null) street_str = "";
			try {
				ZipCodeHome zHome = (ZipCodeHome) IDOLookup.getHome(ZipCode.class);
				ZipCode zipcode = zHome.findByPrimaryKey(zipcode_id_int);
				zipcode_str = zipcode.getCode();
				if (zipcode_str == null) {
					zipcode_str = "";
				}
			}
			catch (Exception e) {
				zipcode_str = "";
				e.printStackTrace();
			}
			try {
				myTable.add(new HiddenInput("address_id", Integer.toString(address_id_int)), 1, 1);
				myTable.add(new HiddenInput("zipcode_id", Integer.toString(zipcode_id_int)), 1, 1);
			}
			catch (Exception e) {
			}

			TextInput gata = (TextInput) getStyledSmallInterface(new TextInput("street", street_str));
			TextInput postnumer = (TextInput) getStyledSmallInterface(new TextInput("zipcode", zipcode_str));
			postnumer.setSize(3);
			postnumer.setMaxlength(3);
			DropdownMenu land = (DropdownMenu) getStyledSmallInterface(new DropdownMenu("country_id"));
			CountryHome cHome = (CountryHome) IDOLookup.getHome(Country.class);
			try {
				Collection coll = cHome.findAll();
				if (coll != null && coll.isEmpty()) {
					Iterator iter = coll.iterator();
					Country country;
					while (iter.hasNext()) {
						country = (Country) iter.next();
						land.addMenuElement(country.getID(), country.getName());
					}
				}
			}
			catch (FinderException e) {
			}

			land.setSelectedElement(Integer.toString(address[j].getCountryId()));
			myTable.add(getHeader(localize("street", "Street")), 1, row);
			myTable.add(gata, 2, row++);
			myTable.add(getHeader(localize("zip_code", "Zip Code")), 1, row);
			myTable.add(postnumer, 2, row++);
			myTable.add(getHeader(localize("country", "Country")), 1, row);
			myTable.add(land, 2, row++);
		}
		///////////////////////////////////////////////////////////// Address endar

		//////------------Phone----------/////////////////////////////////////////////////////
		for (int org = 0; org < phone.length; org++) {
			myTable.add(new HiddenInput("phone_id", Integer.toString(phone[org].getID())), 1, 1);

			TextInput simi = (TextInput) getStyledSmallInterface(new TextInput("phone", phone[org].getNumber()));
			simi.setSize(10);
			simi.setMaxlength(10);

			DropdownMenu land = (DropdownMenu) getStyledSmallInterface(new DropdownMenu("phone_country_id"));
			CountryHome cHome = (CountryHome) IDOLookup.getHome(Country.class);
			try {
				Collection coll = cHome.findAll();
				if (coll != null && coll.isEmpty()) {
					Iterator iter = coll.iterator();
					Country country;
					while (iter.hasNext()) {
						country = (Country) iter.next();
						land.addMenuElement(country.getID(), country.getName());
					}
				}
			}
			catch (FinderException e) {
			}

			myTable.add(getHeader(localize("telephone_number", "Telephone number")), 1, row);
			myTable.add(simi, 2, row++);
			myTable.add(getHeader(localize("country", "Country")), 1, row);
			myTable.add(land, 2, row++);
		}
		if (phone.length == 0) {
			TextInput simi = (TextInput) getStyledSmallInterface(new TextInput("phone", ""));
			simi.setSize(10);
			simi.setMaxlength(10);

			DropdownMenu land = (DropdownMenu) getStyledSmallInterface(new DropdownMenu("phone_country_id"));
			CountryHome cHome = (CountryHome) IDOLookup.getHome(Country.class);
			try {
				Collection coll = cHome.findAll();
				if (coll != null && coll.isEmpty()) {
					Iterator iter = coll.iterator();
					Country country;
					while (iter.hasNext()) {
						country = (Country) iter.next();
						land.addMenuElement(country.getID(), country.getName());
					}
				}
			}
			catch (FinderException e) {
			}

			myTable.add(getHeader(localize("telephone_number", "Telephone number")), 1, row);
			myTable.add(simi, 2, row++);
			myTable.add(getHeader(localize("country", "Country")), 1, row);
			myTable.add(land, 2, row++);
		}
		///////////////////////////////////////////////////////// Phone endar

		row = 1;
		
		//////----------Handicap------------/////////////////////////////////////////////////////////
		if (mInfo == null) {
			myTable.add(getHeader(localize("handicap", "Handicap")), 3, row);
			TextInput handicap = (TextInput) getStyledSmallInterface(new TextInput("handicap", ""));
			handicap.setSize(4);
			myTable.add(handicap, 4, row++);
		}
		else if (mInfo != null) {
			myTable.add(getHeader(localize("handicap", "Handicap")), 3, row);
			myTable.add(getText(Float.toString(mInfo.getFirstHandicap())), 4, row);
			myTable.add(new HiddenInput("handicap", Float.toString(mInfo.getFirstHandicap())), 4, row);
		}
		//////////////////////////////////////////////////////////////// Handicap endar

		//////--------Emil------------//////////////////////////////////////////////////////
		String e_mail = member.getEmail();

		myTable.add(getHeader(localize("email", "Email")), 3, row);

		if (e_mail == null) {
			TextInput email = (TextInput) getStyledSmallInterface(new TextInput("email"));
			myTable.add(email, 4, row++);
		}
		else {
			TextInput email = (TextInput) getStyledSmallInterface(new TextInput("email", e_mail));
			myTable.add(email, 4, row++);
		}
		//////////////////////////////////////////////////////////// Emil endar

		////////------Login / Password-----------//////////////////////////////////////////////////////////
		LoginTable lTable = null;
		try {
			lTable = ltHome.findByMember(member);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}

		if (lTable != null) {
			TextInput login = (TextInput) getStyledSmallInterface(new TextInput("login", lTable.getUserLogin()));
			PasswordInput password = (PasswordInput) getStyledSmallInterface(new PasswordInput("password", lTable.getUserPassword()));
			myTable.add(getHeader(localize("user_name", "User name")), 3, row);
			myTable.add(login, 4, row++);
			myTable.add(getHeader(localize("password", "Password")), 3, row);
			myTable.add(password, 4, row++);
			++row;
		}
		else {
			TextInput login = (TextInput) getStyledSmallInterface(new TextInput("login"));
			PasswordInput password = (PasswordInput) getStyledSmallInterface(new PasswordInput("password"));
			myTable.add(getHeader(localize("user_name", "User name")), 3, row);
			myTable.add(login, 4, row++);
			myTable.add(getHeader(localize("password", "Password")), 3, row);
			myTable.add(password, 4, row++);
		}
		PasswordInput password2 = (PasswordInput) getStyledSmallInterface(new PasswordInput("password2"));
		myTable.add(getHeader(localize("retype_password", "Retype password")), 3, row);
		myTable.add(password2, 4, row++);
		//////////////////////////////////////////////////////////////////// Login/Password endar

		row = myTable.getRows() + 1;
		SubmitButton submit = new SubmitButton(localize("save", "Save"));
		myTable.mergeCells(1, row, 4, row);
		myTable.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		myTable.add(submit, 1, row);
		myTable.add(new HiddenInput("action", "submitted"), 1, 1);

		add(myForm);
	}
}