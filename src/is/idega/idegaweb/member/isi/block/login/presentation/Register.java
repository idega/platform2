/*
 * Created on Feb 3, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package is.idega.idegaweb.member.isi.block.login.presentation;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.idega.block.login.business.LoginBusiness;
import com.idega.business.IBOLookup;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.accesscontrol.data.LoginTableHome;
import com.idega.core.contact.data.Email;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.PasswordInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.SendMail;

/**
 * @author jonas
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Register extends Block {
	
	private IWContext _iwc = null;
	private IWResourceBundle _iwrb;
	private IWBundle _iwb;
	
	private String _kt = null;
	
	public final static int PERSONAL_NUMBER_NOT_FOUND = 1000;
	
	public final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private static final int[] KT_MULT = {3, 2, 7, 6, 5, 4, 3, 2}; 
	
	public void control() {
		PresentationObject po;
		String message;
		if(_iwc.getParameter("reg_personal_number")!=null) {
			message = processStage1();
			if(message==null) {
				System.out.println("Stage 1 completed");
				po = getStage2Page(null);
			} else {
				System.out.println("Stage 1 redisplayed");
				po = getStage1Page(message);
			}
		} else if (_iwc.getParameter("reg_password")!=null) {
			message = processStage2();
			if(message==null) {
				System.out.println("Stage 2 completed");
				po = getStage3Page(null);
			} else {
				System.out.println("Stage 2 redisplayed");
				po = getStage2Page(message);
			}
		} else {
			System.out.println("Stage 1 - starting registration");
			po = getStage1Page(null);
		}
		
		add(po);
	}
	
	private PresentationObject getStage1Page(String message) {
		Table T = new Table(2, 3);
		if(message!=null) {
			T.mergeCells(1,1,1,2);
			T.add(message, 1, 1);
		}
		String labelPersonNumber = _iwrb.getLocalizedString("register.person_number", "Kennitala");
		TextInput inputPersonalNumber = new TextInput("reg_personal_number");
		if (_iwc.isParameterSet("reg_personal_number")) {
			inputPersonalNumber.setContent(_iwc.getParameter("reg_personal_number"));
		}
		T.add(labelPersonNumber, 1, 2);
		T.add(inputPersonalNumber, 2, 2);
		
		SubmitButton ok =
			new SubmitButton(
				_iwrb.getLocalizedImageButton("send", "Send"),
				"send");

		CloseButton close =
			new CloseButton(_iwrb.getLocalizedImageButton("close", "Close"));

		T.add(ok, 1, 3);
		T.add(close, 2, 3);
		Form myForm = new Form();
		myForm.add(T);
		return myForm;
	}
	
	private String processStage1() {
		String kt = _iwc.getParameter("reg_personal_number");
		kt = getNumericalsOnly(kt);
		if(kt.length()!=10) {
			return _iwrb.getLocalizedString("register.personal_number_invalid", "Kennitala ekki logleg");
		}
		try {
			System.out.println("getting user with kt PN: " + kt);
			User user = getUserBusiness().getUser(kt);
			if(user==null) {
				return _iwrb.getLocalizedString("register.pn_not_found", "Engin fannst med gefna kennitolu");
			}
			if(user.getGroupID()==-1) {
				return _iwrb.getLocalizedString("register.user_not_in_any_group", "Notandi verdur ad vera i eihverjum hop.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return _iwrb.getLocalizedString("register.error_looking_pn", "Villa kom upp vid leit eftir kennitolu");
		}
		_kt = kt;
		return null;
	}
	
	private PresentationObject getStage2Page(String message) {
		Table T = new Table(2, 7);
		if(message!=null) {
			T.mergeCells(1,1,2,1);
			T.add(message, 1, 1);
		}
		
		String labelPassword = _iwrb.getLocalizedString("register.password", "Lykilord");
		TextInput inputPassword = new PasswordInput("reg_password");
		String labelPasswordConfirmed = _iwrb.getLocalizedString("register.password_confirmed", "Lykilord Aftur");
		TextInput inputPasswordConfirmed = new PasswordInput("reg_password_confirmed");
		String labelHintQuestion = _iwrb.getLocalizedString("register.hint_question", "Visbendingaspurning");
		TextInput inputHintQuestion = new TextInput("reg_hint_question");
		String labelHintAnswer = _iwrb.getLocalizedString("register.hint_answer", "Svar");
		TextInput inputHintAnswer = new TextInput("reg_hint_answer");
		
		String textHint = _iwrb.getLocalizedString("reg_hint_instructions", "Gefdu upp spurningu sem hjalpar ther ef thu gleymir lykilordi. Ma sleppa.");
		
		T.add(labelPassword, 1, 2);
		T.add(inputPassword, 2, 2);
		T.add(labelPasswordConfirmed, 1, 3);
		T.add(inputPasswordConfirmed, 2, 3);
		T.mergeCells(1, 4, 2, 4);
		T.add(textHint, 1, 4);
		T.add(labelHintQuestion, 1, 5);
		T.add(inputHintQuestion, 2, 5);
		T.add(labelHintAnswer, 1, 6);
		T.add(inputHintAnswer, 2, 6);
		
		
		SubmitButton ok =
			new SubmitButton(
				_iwrb.getLocalizedImageButton("send", "Send"),
				"send");

		CloseButton close =
			new CloseButton(_iwrb.getLocalizedImageButton("close", "Close"));
		T.add(ok, 1, 7);
		T.add(close, 2, 7);
		
		Form myForm = new Form();
		myForm.add(T);
		if(_kt!=null) {
			HiddenInput hiKT = new HiddenInput("reg_kt", _kt);
			myForm.add(hiKT);
		} else {
			System.out.println("No kt known to use for stage 2!! bummer.");
		}
		return myForm;
	}
	
	private String processStage2() {
		String password = _iwc.getParameter("reg_password");
		String passwordConfirmed = _iwc.getParameter("reg_password_confirmed");
		String hintQ = _iwc.getParameter("reg_hint_question");
		String hintA = _iwc.getParameter("reg_hint_answer");
		String kt = _iwc.getParameter("reg_kt");
		if(kt!=null) {
			_kt = kt;
		}
		if(password==null) {
			return _iwrb.getLocalizedString("register.no_password", "Ekkert lykilord slegid inn");
		}
		if(!password.equals(passwordConfirmed)) {
			return _iwrb.getLocalizedString("register.password_mismatch", "Lykilord stemma ekki");
		}
		hintQ = hintQ==null?"":hintQ;
		hintA = hintA==null?"":hintA;
		if( (hintQ.length()==0 && hintA.length()!=0) || (hintQ.length()!=0 && hintA.length()==0)) {
			return _iwrb.getLocalizedString("register.hint_answer_invalid", "Gefdu upp baedi spurningu og svar eda hvorugt.");
		}
		User user = null;
		boolean ok = true;
		try {
			System.out.println("getting user with kt PN: " + _kt);
			user = getUserBusiness().getUser(_kt);
		} catch (Exception e) {
			e.printStackTrace();
			ok = false;
		}
		if(user == null || !ok) {
			return _iwrb.getLocalizedString("register.error_editing_user", "Villa kom upp vid ad skra upplysingar, Lykilord ekki sett");
		}
		try {
			LoginTable lt = getLoginTable(user);
			lt.setUserId(user.getID());
			lt.setUserLogin(kt);
			lt.store();
			LoginBusiness.changeUserPassword(user, password);
			sendMessage(user, kt, password);
		} catch (Exception e) {
			e.printStackTrace();
			return _iwrb.getLocalizedString("register.error_changing_password", "Villa kom upp vid ad breyta lykilordi. Lykilord obreytt");
		}
		if(hintQ.length() != 0) {
			System.out.println("Setting hint question and answer");
			user.addMetaData("HINT_QUESTION", hintQ.trim());
			user.addMetaData("HINT_ANSWER", hintA.trim());
			user.store();
		}
		return null;
	}
	
	private PresentationObject getStage3Page(String message) {
		Table T = new Table(1, 3);
		if(message!=null) {
			T.add(message, 1, 1);
		}
		
		String done = _iwrb.getLocalizedString("register.done", "Skraningu lokid.");
		T.add(done, 1, 2);
		
		CloseButton close =
			new CloseButton(_iwrb.getLocalizedImageButton("close", "Close"));
		T.add(close, 1, 3);
		
		return T;
	}
	
	public void main(IWContext iwc) throws RemoteException {
		_iwc = iwc;
		_iwb = getBundle(iwc);
		_iwrb = getResourceBundle(iwc);
		control();
	}
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	private String getNumericalsOnly(String userName) {
		userName = userName.trim();
		StringBuffer buf = new StringBuffer();
		
		int count = userName.length();
		char[] chars = userName.toCharArray();
		for(int i=0; i<count; i++) {
			char c = chars[i];
			if(Character.isDigit(c)) {
				buf.append(c);
			}
		}

		return buf.toString();
	}
	
	private UserBusiness getUserBusiness() throws RemoteException{
		return (UserBusiness) IBOLookup.getServiceInstance(_iwc.getApplicationContext(),UserBusiness.class);
	}
	
	private void sendMessage(User user, String login, String password) {
		String server = _iwb.getProperty("register.email_server");
		
		if(server == null) {
			System.out.println("email server bundle property not set, no registration notification sent to user " + user.getName());
		}
		
		String letter =
						_iwrb.getLocalizedString(
						"register.email_body",
						"Thu hefur verid skradur a Felix.\nNotendanafn : {0} \nLykilord: {1} \n");

		if (letter != null) {
			try {
				Collection emailCol = user.getEmails();
				if(emailCol!=null) {
					Iterator emailIter = user.getEmails().iterator();
					while(emailIter.hasNext()) {
						String address = ((Email) emailIter.next()).getEmailAddress();
						Object[] objs = {login,password};
						String body = MessageFormat.format(letter,objs);
						
						System.out.println("Sending registration notification to " + address);
						
						SendMail.send(
							_iwrb.getLocalizedString("register.email_sender", "<Felix-felagakerfi>felix@isi.is"),
							address,
							"",
							"",
							server,
							_iwrb.getLocalizedString("register.email_subject", "Nyskraning a Felix"),
							body);
					}
				}
			} catch (Exception e) {
				System.out.println("Couldn't send email notification for registration for user " + login);
				e.printStackTrace();
			}
		} else {
			System.out.println("No registration notification letter found, nothing sent to user " + login);
		}
	}
	
	private LoginTable getLoginTable(User user) {
		LoginTable lt = null;
		try {
			LoginTableHome ltHome = (LoginTableHome) IDOLookup.getHome(LoginTable.class);
			Iterator iter = ltHome.findLoginsForUser(user).iterator();
			while(iter.hasNext()) {
				lt =  (LoginTable) iter.next();
				if(lt.getLoginType()==null || lt.getLoginType().length()==0) {
					System.out.println("found existing logintable");
					break;
				} else {
					lt = null;
				}
			}
			if(lt==null) {
				lt = ltHome.create();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lt;
	}
}
