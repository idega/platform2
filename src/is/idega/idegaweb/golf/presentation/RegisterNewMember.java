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
import com.idega.presentation.ui.DropdownMenu;
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
			} catch (FinderException f) {}
			
			String action = iwc.getParameter("action");
		
			if (action == null) {
		          action = "formid";
			}
		
			if (action.equals("formid")) {
	      if (member != null) {
	      		LoginTable lTable = null;
	      		try {
	      			lTable = ltHome.findByMember(member);
	      		} catch (FinderException f) {}
	
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
		} else {
			add(getSmallText(localize("need_a_personal_id", "Need a personal ID")));
		}
	}

	
	/**
	 * Return memberID when valid, -1 when username is invalid, -2 when invalid password, -3 when ssn is invalid, -4 other error;
	 * @throws FinderException
	 *
	 **/
	public int checkPassword(IWContext iwc) throws FinderException {
	
    String login = iwc.getParameter("login");
    String password = iwc.getParameter("password");
    String kt = iwc.getParameter("kt");

    boolean returner = false;
    Collection coll = ltHome.findByUserLogin(login);

    	if(coll != null){
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
     	    else{
     	    		return -3;
     	    }
     		}
     		else{
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
			case -1 : 
			        VillaIInnslaetti(kt,"login2");
				break;
			case -2 :
				VillaIInnslaetti(kt,"wrong_password");
				break;
			case -3 :
				VillaIInnslaetti(kt,"wrong_ssn");
				break;
			case -4 :
				break;
			default :
				form(mHome.findByPrimaryKey(passwStatus));
				break;
		}
  }
	
	
	public void login(Member member, LoginTable logTable, String kt) {
	
		Form myForm = new Form();
	
		Table myTable = new Table(2,4);
    myTable.setBorder(0);
    myTable.setCellpaddingAndCellspacing(0);
    myTable.mergeCells(1,1,2,1);
    myTable.mergeCells(1,4,2,4);
    myTable.setRowStyleClass(1, getHeaderRowClass());
    myTable.setRowStyleClass(2, getDarkRowClass());
    myTable.setRowStyleClass(3, getLightRowClass());
    myTable.setRowStyleClass(4, getDarkRowClass());

    myTable.add(getSmallHeader(localize("log_in", "Log in")), 1, 1);
    myTable.add(getSmallText(localize("user", "User")),1,2);
    myTable.add(getSmallText(localize("password", "Password")),1,3);

    myTable.add((TextInput) getStyledInterface(new TextInput("login")),2,2);
    myTable.add((PasswordInput) getStyledInterface(new PasswordInput("password")),2,3);
    myTable.add(new HiddenInput("kt",kt),1,2);

    myTable.add(new SubmitButton("action","afram"),1,4);
    myTable.setAlignment(1,4,"center");
	
		myForm.add(myTable);
		add(myForm);
	
	}
	
	public boolean checkForUpdate(Member member, String login) throws FinderException{
		if (login == null) {
			return false;
		}
		Collection coll = ltHome.findByUserLogin(login);
		
		//LoginTable[] eruTilFleiriLogin = (LoginTable[]) (new LoginTable()).findAllByColumn("user_login",login);

		if (coll.isEmpty() || coll == null) {
			return true;
		}else if (coll.size() == 1) {
			LoginTable lTable = ltHome.findByMember(member);
			//LoginTable[] userLogins = (LoginTable[]) (new LoginTable()).findAllByColumn("member_id", memberId);
			//System.out.println("createlogin.jsp : userLogins = "+userLogins.length);
			if (lTable != null) {
				if (login.equals(lTable.getUserLogin())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void update(IWContext iwc, Member member, String[] address_id, String[] zipcode_id) throws SQLException, FinderException, IDOLookupException, CreateException {
	
		String login = iwc.getParameter("login");
		String kt = iwc.getParameter("social_security_number");
	
		String[] phone_id = iwc.getParameterValues("phone_id");
		String[] phone_country_id = iwc.getParameterValues("phone_country_id");
	
	
		boolean	loginILagi = true;
		boolean updateLogin = false;
		boolean isCardValid = false;
		boolean isPasswordValid = false;
	
		String creditcard_number = iwc.getParameter("cc1") +  iwc.getParameter("cc2") +  iwc.getParameter("cc3") + iwc.getParameter("cc4") ;
	
	        String cc_month = iwc.getParameter("cc_month");
	        String cc_year = iwc.getParameter("cc_year");
		String password = iwc.getParameter("password");
		String password2 = iwc.getParameter("password2");
	
	
		if (!(login.equals(""))) {
			loginILagi = checkForUpdate(member, login);
			updateLogin = loginILagi;
		}
	
	
	  if (loginILagi) {
	  		if (password2.equals("") || password.equals("")) {
	  			updateLogin = false;
	  		} else {
	  			if (password.equals(password2)) {
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
	  			} catch (FinderException ignore) {}
	  			
	  			String first_name = iwc.getParameter("first_name");
	  			if (first_name== null) {
	  				first_name="";
	  			}
	  			String middle_name = iwc.getParameter("middle_name");
	  			if (middle_name== null) {
	  				middle_name="";
	  			}
	  			String last_name = iwc.getParameter("last_name");
	  			if (last_name== null) {
	  				last_name="";
	  			}
	
	  			String[] street = iwc.getParameterValues("street");
	  			String[] zipcode = iwc.getParameterValues("zipcode");
	  			String[] country_id = iwc.getParameterValues("country_id");
	  			String email = iwc.getParameter("email");
	  			if (email == null) {
	  				email = "";
	  			}
	
	  			member.setFirstName(first_name);
	  			member.setMiddleName(middle_name);
	  			member.setLastName(last_name);
	  			member.setSocialSecurityNumber(kt);
	  			member.setEmail(email);
	
	  			/////--------------Siminn----------///////////////////////
	  			String[] siminn = iwc.getParameterValues("phone");
  				PhoneHome pHome = (PhoneHome) IDOLookup.getHome(Phone.class);
	  			if (phone_id == null) {
	  				String simiString = (String) siminn[0];
	  				if (!(siminn[0].equals(""))) {
	  					Phone phone = pHome.create();
	  					phone.setNumber(siminn[0]);
	  					phone.setCountryId(new Integer(Integer.parseInt(phone_country_id[0])));
	  					phone.store();
	  					phone.addTo(member);
	  				}
	  			}
	  			else {
	  				for (int kill = 0 ; kill < phone_id.length ; kill++) {
	  					String simiString = (String) siminn[kill];
	  					if (!(siminn[kill].equals(""))) {
	  						Phone phone = pHome.findByPrimaryKey(Integer.parseInt(phone_id[kill]));
	  						//Phone phone = new Phone(Integer.parseInt(phone_id[kill]));
	  						phone.setNumber(siminn[kill]);
	  						if (phone_country_id != null) {
	  							phone.setCountryId(new Integer(Integer.parseInt(phone_country_id[kill])));
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
			//Syst	em.out.println("createLogin : update : updatingLogin");
	  				}
	  				else{
	  					LoginTable logTable = ltHome.create();
	  					logTable.setMemberId(member.getID());
	  					logTable.setUserLogin(login);
	  					logTable.setUserPassword(password);
	  					logTable.store();
			//Syst	em.out.println("createLogin : update : creatingLogin");
	  				}
	  			}
	        ////////////////////////////////  Login buid
	
	  			member.update();
	
	
	  			///////////////-------------Card------------////////////////////////
	  			/*if (isCardValid) {
	                Card[] cardLength = (Card[])member.findReverseRelated(new Card());
	                if (cardLength.length > 0 ) {
	                        idegaTimestamp idegaTime = new idegaTimestamp();
	                                idegaTime.setDate(1);
	                                idegaTime.setMonth(Integer.parseInt(cc_month)-1);
	                                idegaTime.setYear(Integer.parseInt(cc_year)+2000);
	                        cardLength[0].setCardNumber(creditcard_number);
	                        cardLength[0].setExpireDate(idegaTime.getSQLDate());
	                        cardLength[0].update();
	                }
	                else {
	                        Card card = new Card();
	                        idegaTimestamp idegaTime = new idegaTimestamp();
	                                idegaTime.setDate(1);
	                                idegaTime.setMonth(Integer.parseInt(cc_month)-1);
	                                idegaTime.setYear(Integer.parseInt(cc_year)+2000);
	                        card.setCardNumber(creditcard_number);
	                        card.setExpireDate(idegaTime.getSQLDate());
	                        card.insert();
	                        card.addTo(member);
	                }
	        }
	        */
	      ///////////////////////////////////////  Card buid
	
	
	      /////////-----------Address-----------/////////////////////////
	  			boolean zipcodeVilla = false;
	  			AddressHome aHome = (AddressHome) IDOLookup.getHome(Address.class);
	  			ZipCodeHome zHome = (ZipCodeHome) IDOLookup.getHome(ZipCode.class);
	  			if (address_id != null) {
	  				for (int g = 0 ; g < address_id.length ; g++) {
	  					try {
	  						int zipcodeInt = Integer.parseInt(zipcode[g]);
	  					}
	  					catch (NumberFormatException n) {
	  						zipcodeVilla = true;
	  					}
	
	  					
	  					if (!(zipcodeVilla)) {
	  						Address address = aHome.findByPrimaryKey(Integer.parseInt(address_id[g]));
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
	
	  			if (address_id==null) {
	  				try {
	  					int zipcodeInt = Integer.parseInt(zipcode[0]);
	  				}
	  				catch (NumberFormatException n) {
	  					zipcodeVilla = true;
	  				}
	  				if (!(zipcodeVilla)) {
	  					Address address = aHome.create();
	  					if (street[0] != null)
	  						address.setStreet(street[0]);
	  					//if (street_number[0] != null)
	  					//address.setStreetNumber(street_number[0]);
	  					if (country_id[0] != null)
	  						address.setCountryId(Integer.parseInt(country_id[0]));
	  					
	  					if (zipcode[0] != null) {
	  						ZipCode code = zHome.findByCode(zipcode[0]);
	  						//ZipCode[] zipcode_arr = (ZipCode[])(new ZipCode()).findAllByColumn("code",zipcode[0]);
	  						address.setZipcodeId(code.getID());
	  					}
	  					address.store();
	  					address.addTo(member);
	  				}
	  			}
	  			////////////////////////////  Address buid
	  			
	  			
	  			////////-------Handicap-----------//////////////////
	  			String handicap = iwc.getParameter("handicap");
	  			float floatHandicap;
	  			boolean villa = false;
	  			
	  			
	  			if (handicap.equals("")) {
	  				handicap="36";
	  			}
	  			handicap.replace(',','.');
	  			
	  			try {
	  				floatHandicap = Float.parseFloat(handicap);
	  			}
	  			catch (NumberFormatException n) {
	  				villa=true;
	  			}
	  			MemberInfoHome miHome = (MemberInfoHome) IDOLookup.getHome(MemberInfo.class);
	  			if (!(villa)) {
	  				MemberInfo mInfo = null;
	  				try {
	  					mInfo = miHome.findByMember(member);
	  				} catch (FinderException ignore) {}
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
	  				VillaIInnslaetti(kt,"handicap");
	  			}
	  			if (zipcodeVilla) {
	  				VillaIInnslaetti(kt,"zipcode");
	  			}
	  			//////////////////////////////////////  Handicap buid
	  			
	  			
	//	bool	ean villa= false;
	  			if (	 (!(villa)) && (!(zipcodeVilla)) ) {
	  				completed(iwc, member);
	  			}
	
	  		}
	  		else {
	  			VillaIInnslaetti(kt,"pass");
	  		}
	  		
	  }
	  else {   //ef loginEkkiILagi
	  		VillaIInnslaetti(kt,"login");
	  }
	
	}	
	
	public void completed(IWContext iwc, Member member) {
	
		Table myTable = new Table(1,2);
		myTable.setAlignment(1,1,"center");
		myTable.setAlignment(1,2,"center");
		myTable.setRowStyleClass(1, getHeaderRowClass());
		myTable.setRowStyleClass(2, getLightRowClass());
		myTable.setBorder(0);
		myTable.setCellpaddingAndCellspacing(0);
		
		
		myTable.add(getSmallHeader(localize("registration_complete", "Registration complete")),1,1);
		myTable.add(new Link(getSmallText(localize("click_here_for_start_page", "Click here to return to start page"))), 1, 2);//"smellið hér til að fara á upphafssíðuna","/index.jsp"),1,2);
		
		add(myTable);
	
		iwc.getSession().setAttribute("member_login",member);
		LoginBusiness.internalSetState(iwc, "loggedon");
	
  }
	
	
	public void VillaIInnslaetti(String kt,String comment) {
		Table myTable = new Table();
		myTable.setAlignment(1,1,"center");
		myTable.setAlignment(1,2,"center");
		myTable.setAlignment(1,3,"center");
		myTable.setBorder(0);
		myTable.setCellpaddingAndCellspacing(0);
		
		myTable.setRowStyleClass(1, getHeaderRowClass());
		myTable.setRowStyleClass(2, getLightRowClass());
		myTable.setRowStyleClass(3, getDarkRowClass());

		myTable.add(getSmallHeader(localize("input_error", "Input error")), 1, 1);
		if (comment.equals("pass")) {
			myTable.add(getSmallText(localize("passwords_not_the_save", "Passwords were not the save")), 1, 2);
		}/*
		else if (comment.equals("card")) {
			myTable.add("Kortanúmer er ekki rétt",1,2);
		}
		else*/ if (comment.equals("login")) {
			myTable.add(getSmallText(localize("login_in_use", "Login is in use")), 1, 2);
		}
		else if (comment.equals("zipcode")) {
			myTable.add(getSmallText(localize("incorrect_zip_code", "Incorrect zip code")), 1, 2);
		}
		else if (comment.equals("login2")) {
			myTable.add(getSmallText(localize("registration_failed", "Registration failed")),1,2);
		}
    else if (comment.equals("wrong_ssn")) {
			myTable.add(getSmallText(localize("incorrect_pid", "Incorrect personal id")),1,2);
		}
    else if (comment.equals("wrong_password")) {
			myTable.add(getSmallText(localize("incorrect_password", "Incorrect password")),1,2);
		}
		else if (comment.equals("handicap")) {
			myTable.add(getSmallText(localize("incorrect_handicap", "Incorrect handicap")),1,2);
		}

		myTable.add(new BackButton(localize("back", "Back")),1,3);
		add(myTable);
	}
	
	public void nobodyWasFound(String kt) throws IOException{
	
		Table myTable = new Table();
		myTable.setAlignment(1,1,"center");
		myTable.setBorder(0);
		myTable.setCellpaddingAndCellspacing(0);
		myTable.add(getSmallText(localize("member_not_found", "No member was found (kt: "+kt+")")));
		add(myTable);
	}
	
	
	
	
	public void form(Member member) throws IOException, SQLException, FinderException{

		MemberInfoHome miHome = (MemberInfoHome) IDOLookup.getHome(MemberInfo.class);
		MemberInfo mInfo = miHome.findByMember(member);
		Address[] address = member.getAddress();
		Phone[] phone = member.getPhone();
	
		int row = 1;
		Form myForm = new Form();
			myForm.setMethod("post");
	
		Table myTable = new Table();
		myForm.add(myTable);
		myTable.mergeCells(1,row,4,row);
		myTable.setBorder(0);
		myTable.setCellpaddingAndCellspacing(0);
		
		myTable.setRowStyleClass(row, getHeaderRowClass());
	
		myTable.add(new HiddenInput("member_id",Integer.toString(member.getID())),1,row);
		// For checkpassword to work we need this (kt) hiddenInput
		myTable.add(new HiddenInput("kt", member.getSocialSecurityNumber()), 1, row);
		
		String firstName = member.getFirstName();
			if (firstName==null) {
				firstName = "";
			}
		String middleName =member.getMiddleName();
			if (middleName==null) {
				middleName = "";
			}
		String lastName  = member.getLastName();
			if (lastName==null) {
				lastName = "";
			}
		String socialSecurityNumber = member.getSocialSecurityNumber();
			if (socialSecurityNumber==null) {
				socialSecurityNumber = "";
			}
	
	
		TextInput fornafn = (TextInput) getStyledSmallInterface(new TextInput("first_name",firstName));
		TextInput millinafn = (TextInput) getStyledSmallInterface(new TextInput("middle_name",middleName));
		TextInput eftirnafn = (TextInput) getStyledSmallInterface(new TextInput("last_name",lastName));
	//Text kennitala = new Text(member.getSocialSecurityNumber());
		myTable.add(new HiddenInput("social_security_number",socialSecurityNumber),1,1);
	
		Text fyrirsogn =  getSmallHeader(localize("registration", "Registration"));
	
		myTable.add(fyrirsogn,1,row);
		row++;
	
		myTable.add(getSmallText(localize("first_name", "First name")),1,row);
			myTable.add(fornafn,2,row);
		row++;
		myTable.add(getSmallText(localize("middle_name", "Middle name")),1,row);
			myTable.add(millinafn,2,row);
		row++;
		myTable.add(getSmallText(localize("last_name", "Last name")),1,row);
			myTable.add(eftirnafn,2,row);
		row++;
		myTable.add(getSmallText(localize("personal_id", "Personal ID")),1,row);
			myTable.add(getSmallText(socialSecurityNumber),2,row);
		row+=2;
	
	
	////////-----------Address--------------////////////////////////////////////////////////
		if (address.length == 0) {
			TextInput gata= (TextInput) getStyledSmallInterface(new TextInput("street",""));
			TextInput postnumer= (TextInput) getStyledSmallInterface(new TextInput("zipcode",""));
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
						land.addMenuElement(country.getID(),country.getName());
					}
				}
			} catch (FinderException e) {}
		
	//		land.setSelectedElement(Integer.toString(address[j].getCountryId()));
		myTable.add(getSmallText(localize("street", "Street")),1,row);
			myTable.add(gata,2,row);
		row++;
		myTable.add(getSmallText(localize("zip_code", "Zip Code")),1,row);
			myTable.add(postnumer,2,row);
		row++;
		myTable.add(getSmallText(localize("country", "Country")),1,row);
			myTable.add(land,2,row);
		row+=2;
		}
	
    int address_id_int;
    int zipcode_id_int;
    String street_str;
    String zipcode_str;
	
		for (int j = 0; j < address.length ; j++) {
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
				} catch (Exception e) {
					zipcode_str = "";
					e.printStackTrace();
				}
      		try {
      			myTable.add(new HiddenInput("address_id",Integer.toString(address_id_int)),1,1);
      			myTable.add(new HiddenInput("zipcode_id",Integer.toString(zipcode_id_int)),1,1);
      		}
      		catch (Exception e) {}

      		TextInput gata= (TextInput) getStyledSmallInterface(new TextInput("street",street_str));
      		TextInput postnumer= (TextInput) getStyledSmallInterface(new TextInput("zipcode",zipcode_str));
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
    						land.addMenuElement(country.getID(),country.getName());
    					}
    				}
    			} catch (FinderException e) {}

      		land.setSelectedElement(Integer.toString(address[j].getCountryId()));
      		myTable.add(getSmallText(localize("street", "Street")),1,row);
    			myTable.add(gata,2,row);
    			row++;
    			myTable.add(getSmallText(localize("zip_code", "Zip Code")),1,row);
    			myTable.add(postnumer,2,row);
    			row++;
    			myTable.add(getSmallText(localize("country", "Country")),1,row);
    			myTable.add(land,2,row);
      		row+=2;
			}
	/////////////////////////////////////////////////////////////	Address endar
	
	
	//////--------Emil------------//////////////////////////////////////////////////////
		String e_mail = member.getEmail();
	
		myTable.add(getSmallText(localize("email", "Email")),1,row);
	
	
		if (e_mail == null) {
			TextInput email = (TextInput) getStyledSmallInterface(new TextInput("email"));
			myTable.add(email,2,row);
		}
		else {
			TextInput email = (TextInput) getStyledSmallInterface(new TextInput("email",e_mail));
			myTable.add(email,2,row);
		}
		row++;
	////////////////////////////////////////////////////////////	Emil endar
	
	
	
	//////------------Phone----------/////////////////////////////////////////////////////
		for ( int org = 0; org < phone.length ; org++) {
			myTable.add(new HiddenInput("phone_id",Integer.toString(phone[org].getID())),1,1);
	
			TextInput simi=(TextInput) getStyledSmallInterface(new TextInput("phone",phone[org].getNumber()));
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
						land.addMenuElement(country.getID(),country.getName());
					}
				}
			} catch (FinderException e) {}
	
			myTable.add(getSmallText(localize("telephone_number", "Telephone number")),1,row);
			myTable.add(simi,2,row);
			row++;
			myTable.add(getSmallText(localize("country", "Country")),1,row);
			myTable.add(land,2,row);
			row++;
		}
		if (phone.length == 0){
			TextInput simi=(TextInput) getStyledSmallInterface(new TextInput("phone",""));
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
						land.addMenuElement(country.getID(),country.getName());
					}
				}
			} catch (FinderException e) {}
	
			myTable.add(getSmallText(localize("telephone_number", "Telephone number")),1,row);
			myTable.add(simi,2,row);
			row++;
			myTable.add(getSmallText(localize("country", "Country")),1,row);
			myTable.add(land,2,row);
			row++;
		}
		row ++;
	
	
	/////////////////////////////////////////////////////////		Phone endar
	
	
	
	/*
	
	//////--------Card------------/////////////////////////////////////////////////
		Card[] card = (Card[]) member.findReverseRelated(new Card());
	
		if (card.length > 0) {
			TextInput cc1 = new TextInput("cc1");
	//		TextInput cc1 = new TextInput("cc1",card[0].getCardNumber().substring(0,4));
				cc1.setSize(4);
				cc1.setMaxlength(4);
			TextInput cc2 = new TextInput("cc2");
	//		TextInput cc2 = new TextInput("cc2",card[0].getCardNumber().substring(4,8));
				cc2.setSize(4);
				cc2.setMaxlength(4);
			TextInput cc3 = new TextInput("cc3");
	//		TextInput cc3 = new TextInput("cc3",card[0].getCardNumber().substring(8,12));
				cc3.setSize(4);
				cc3.setMaxlength(4);
			TextInput cc4 = new TextInput("cc4");
	//		TextInput cc4 = new TextInput("cc4",card[0].getCardNumber().substring(12,16));
				cc4.setSize(4);
				cc4.setMaxlength(4);
	
	
			IntegerInput cc_vt_month = new IntegerInput("cc_month");
	//		IntegerInput cc_vt_month = new IntegerInput("cc_month",card[0].getExpireDate().getMonth()+1);
				cc_vt_month.setSize(2);
				cc_vt_month.setMaxlength(2);
			IntegerInput cc_vt_year = new IntegerInput("cc_year");
	//		IntegerInput cc_vt_year = new IntegerInput("cc_year",card[0].getExpireDate().getYear()-100);
				cc_vt_year.setSize(2);
				cc_vt_year.setMaxlength(2);
	
			myTable.addText("Golfkortsnúmer",1,row);
				myTable.add(cc1,2,row);
				myTable.add(cc2,2,row);
				myTable.add(cc3,2,row);
				myTable.add(cc4,2,row);
			++row;
			myTable.addText("Gildir til",1,row);
				myTable.add(cc_vt_month,2,row);
				myTable.addText("/",2,row);
				myTable.add(cc_vt_year,2,row);
			row+=2;
		}
		else {
			IntegerInput cc1 = new IntegerInput("cc1");
				cc1.setSize(4);
				cc1.setMaxlength(4);
			IntegerInput cc2 = new IntegerInput("cc2");
				cc2.setSize(4);
				cc2.setMaxlength(4);
			IntegerInput cc3 = new IntegerInput("cc3");
				cc3.setSize(4);
				cc3.setMaxlength(4);
			IntegerInput cc4 = new IntegerInput("cc4");
				cc4.setSize(4);
				cc4.setMaxlength(4);
	
			IntegerInput cc_vt_month = new IntegerInput("cc_month");
				cc_vt_month.setSize(2);
				cc_vt_month.setMaxlength(2);
			IntegerInput cc_vt_year = new IntegerInput("cc_year");
				cc_vt_year.setSize(2);
				cc_vt_year.setMaxlength(2);
	
			myTable.addText("Golfkortsnúmer",1,row);
				myTable.add(cc1,2,row);
				myTable.add(cc2,2,row);
				myTable.add(cc3,2,row);
				myTable.add(cc4,2,row);
			++row;
			myTable.addText("Gildir til",1,row);
				myTable.add(cc_vt_month,2,row);
				myTable.addText("/",2,row);
				myTable.add(cc_vt_year,2,row);
			row+=2;
		}
	////////////////////////////////////////////////////////////////	Card endar
	*/
	
	//////----------Handicap------------/////////////////////////////////////////////////////////
		if (mInfo == null) {
			myTable.add(getSmallText(localize("handicap", "Handicap")),1,row);
			TextInput handicap = (TextInput) getStyledSmallInterface(new TextInput("handicap",""));
				handicap.setSize(4);
			myTable.add(handicap,2,row);
			myTable.add(getSmallText("("+localize("user_dot_not_comma", "User . not ,")+")"),2,row);
		}
		else if (mInfo != null) {
			myTable.add(getSmallText(localize("handicap", "Handicap")),1,row);
			myTable.add(getSmallText(Float.toString(mInfo.getFirstHandicap())),2,row);
			myTable.add(new HiddenInput("handicap",Float.toString(mInfo.getFirstHandicap())),2,row);
		}
		row+=2;
	////////////////////////////////////////////////////////////////	Handicap endar
	
	
	////////------Login / Password-----------//////////////////////////////////////////////////////////
		LoginTable lTable = null;
		try {
			lTable = ltHome.findByMember(member);
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
		
		if (lTable != null) {
			TextInput login = (TextInput) getStyledSmallInterface(new TextInput("login",lTable.getUserLogin()));
			PasswordInput password = (PasswordInput) getStyledSmallInterface(new PasswordInput("password", lTable.getUserPassword()));
			myTable.add(getSmallText(localize("user_name", "User name")),1,row);
				myTable.add(login,2,row);
			++row;
			myTable.add(getSmallText(localize("password", "Password")),1,row);
				myTable.add(password,2,row);
			++row;
		}
		else {
			TextInput login = (TextInput) getStyledSmallInterface(new TextInput("login"));
			PasswordInput password = (PasswordInput) getStyledSmallInterface(new PasswordInput("password"));
			myTable.add(getSmallText(localize("user_name", "User name")),1,row);
			myTable.add(login,2,row);
			++row;
			myTable.add(getSmallText(localize("password", "Password")),1,row);
			myTable.add(password,2,row);
			++row;
		}
		PasswordInput password2 = (PasswordInput) getStyledSmallInterface(new PasswordInput("password2"));
		myTable.add(getSmallText(localize("retype_password", "Retype password")),1,row);
			myTable.add(password2,2,row);
		++row;
	//////////////////////////////////////////////////////////////////// Login/Password endar
	
	
		SubmitButton submit = new SubmitButton(localize("save", "Save"));
		myTable.mergeCells(1, row, 2, row);
		myTable.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		myTable.add(submit,1,row);
		myTable.add(new HiddenInput("action","submitted"),1,1);
	
		for (int i = 2; i <= row; i++) {
			if (i % 2 == 0) {
				myTable.setRowStyleClass(i, getDarkRowClass());
			} else {
				myTable.setRowStyleClass(i, getLightRowClass());
			}
		}
		
		add(myForm);
	
	
	}
	

	

}
