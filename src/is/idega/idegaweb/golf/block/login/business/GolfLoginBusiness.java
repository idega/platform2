// idega 2000 - Tryggvi Larusson - Grimur Jonsson
/*
 * Copyright 2000 idega.is All Rights Reserved.
 */
package is.idega.idegaweb.golf.block.login.business;

import is.idega.idegaweb.golf.access.AccessControl;
import is.idega.idegaweb.golf.access.LoginTable;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;
import java.io.IOException;
import java.sql.SQLException;
import javax.ejb.FinderException;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.genericentity.Group;
import com.idega.event.IWPageEventListener;
import com.idega.event.IWPresentationEvent;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;

/**
 * Title: GolfLoginBusiness Description: Copyright: Copyright (c) 2000-2001
 * idega.is All Rights Reserved Company: idega
 * 
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson </a>, <a
 *         href="mailto:tryggvi@idega.is">Tryggvi Larusson </a>
 * @version 1.1
 */
public class GolfLoginBusiness extends LoginBusinessBean implements IWPageEventListener {

	public static String UserAttributeParameter = "member_login";

	public static String UserAccessAttributeParameter = "member_access";

	public static String LoginStateParameterForGolf = AccessControl.LOGIN_STATE_PARAMETER;

	public static String newLoginStateParameter = "new_login_state";

	public static final String ACCESSCONTROL_GROUP_PARAMETER = "iw_accesscontrol_group";

	public static final String CURRENT_GOLF_UNION_ID_ATTRIBUTE = "golf_union_id";

	public static final String CLUB_ADMIN_GOLF_UNION_ID_ATTRIBUTE = "admin_golf_union_id";

	public GolfLoginBusiness() {
	}

	// this method must be implemented in this class otherwise the static method
	// getControlActionValue
	// of the super class is used! (inheritance problem of static methods in
	// java)
	public static boolean isLogOnAction(IWContext iwc) {
		return "login".equals(getControlActionValue(iwc));
	}

	// this method must be implemented in this class otherwise the static method
	// getControlActionValue
	// of the super class is used! (inheritance problem of static methods in
	// java)
	public static boolean isLogOffAction(IWContext iwc) {
		return "logoff".equals(getControlActionValue(iwc));
	}

	// this method must be implemented in this class otherwise the static method
	// getControlActionValue
	// of the super class is used! (inheritance problem of static methods in
	// java)
	protected static boolean isTryAgainAction(IWContext iwc) {
		return "tryagain".equals(getControlActionValue(iwc));
	}

	// this method uses a different parameter than the super method!
	private static String getControlActionValue(IWContext iwc) {
		return iwc.getParameter(GolfLoginBusiness.LoginStateParameterForGolf);
	}

	public static boolean isLoggedOn(IWContext modinfo) {
		if (modinfo.getSessionAttribute(UserAttributeParameter) == null) {
			return false;
		}
		return true;
	}

	public static void internalSetState(IWContext modinfo, String state) {
		modinfo.setSessionAttribute(GolfLoginBusiness.LoginStateParameterForGolf, state);
	}

	public static String internalGetStateString(IWContext modinfo) {
		return (String) modinfo.getSessionAttribute(GolfLoginBusiness.LoginStateParameterForGolf);
	}

	public boolean actionPerformed(IWContext modinfo) throws IWException {
		try {
			if (isLoggedOn(modinfo)) {
				String controlParameter = modinfo.getParameter(GolfLoginBusiness.LoginStateParameterForGolf);
				if (controlParameter != null) {
					if (controlParameter.equals("logoff")) {
						logOut(modinfo);
						internalSetState(modinfo, "loggedoff");
					}
				}
			}
			else {
				String controlParameter = modinfo.getParameter(GolfLoginBusiness.LoginStateParameterForGolf);
				if (controlParameter != null) {
					if (controlParameter.equals("login")) {
						if (modinfo.getParameter(newLoginStateParameter) != null
								|| modinfo.getParameter(newLoginStateParameter + ".x") != null) {
							String temp = modinfo.getRequest().getParameter("login");
							if (temp != null) {
								if (temp.length() == 10) {
									registerLogin(modinfo, modinfo.getRequest().getParameter("login"));
									internalSetState(modinfo, "loggedoff");
								}
								else {
									internalSetState(modinfo, "newlogin");
								}
							}
							else {
								internalSetState(modinfo, "newlogin");
							}
						}
						else if (isLoginByUUID(modinfo)) {
							String uuid = modinfo.getParameter(PARAM_LOGIN_BY_UNIQUE_ID);
							boolean success = logInByUUID(modinfo, uuid);
							if (!success) {
								System.err.println("[GolfLoginBusinessBean] Attempt to login with UUID: " + uuid
										+ " failed from referer: " + modinfo.getReferer() + " , might be an attack");
							}
						}
						else {
							boolean canLogin = false;
							if ((modinfo.getParameter("login") != null) && (modinfo.getParameter("password") != null)) {
								canLogin = verifyPassword(modinfo, modinfo.getParameter("login"),
										modinfo.getParameter("password"));
								if (canLogin) {
									isLoggedOn(modinfo);
									internalSetState(modinfo, "loggedon");
								}
								else {
									internalSetState(modinfo, "loginfailed");
								}
							}
						}
					}
					else if (controlParameter.equals("tryagain")) {
						internalSetState(modinfo, "loggedoff");
					}
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace(System.err);
			//throw (IdegaWebException)ex.fillInStackTrace();
			return false;
		}
		return true;
	}

	private void registerLogin(IWContext modinfo, String kennitala) throws IOException {
		modinfo.getResponse().sendRedirect("/createlogin.jsp?kt=" + kennitala);
	}

	public boolean isAdmin(IWContext modinfo) throws SQLException {
		return is.idega.idegaweb.golf.access.AccessControl.isAdmin(modinfo);
	}

	public boolean isDeveloper(IWContext modinfo) throws SQLException {
		Member member = AccessControl.getMember(modinfo);
		if (member != null) {
			Group[] access = member.getGroups();
			for (int i = 0; i < access.length; i++) {
				if ("developer".equals(access[i].getName()))
					return true;
			}
		}
		return false;
	}

	public boolean isClubAdmin(IWContext modinfo) throws SQLException {
		Member member = AccessControl.getMember(modinfo);
		if (member != null) {
			Group[] access = member.getGroups();
			for (int i = 0; i < access.length; i++) {
				if ("club_admin".equals(access[i].getName()))
					return true;
			}
		}
		return false;
	}

	public boolean isUser(IWContext modinfo) throws SQLException {
		Member member = AccessControl.getMember(modinfo);
		if (member != null) {
			Group[] access = member.getGroups();
			for (int i = 0; i < access.length; i++) {
				if ("user".equals(access[i].getName()))
					return true;
			}
		}
		return false;
	}

	public static Member getMemberByEmail(String email) throws SQLException {
		Member member = null;
		Member[] members = (Member[]) GenericEntity.getStaticInstance(Member.class).findAllByColumn("email", email);
		if (members != null && members.length > 0) {
			member = members[members.length - 1];
		}
		return member;
	}

	public static LoginTable getLoginForMember(Member member) throws SQLException {
		LoginTable login = null;
		LoginTable[] logins = (LoginTable[]) GenericEntity.getStaticInstance(LoginTable.class).findAllByColumn(
				"member_id", member.getID());
		if (logins != null && logins.length > 0) {
			login = logins[logins.length - 1];
		}
		return login;
	}

	private boolean verifyPassword(IWContext modinfo, String login, String password) throws Exception, FinderException {
		boolean returner = false;
		LoginTable[] login_table = (LoginTable[]) ((LoginTable) IDOLookup.instanciateEntity(LoginTable.class)).findAllByColumn(
				"user_login", login);
		MemberHome mh = ((MemberHome) IDOLookup.getHomeLegacy(Member.class));
		for (int i = 0; i < login_table.length; i++) {
			if (login_table[i].getUserPassword().equals(password)) {
				Member member = mh.findByPrimaryKey(login_table[i].getMemberId());
				modinfo.setSessionAttribute(UserAttributeParameter, member);
				returner = true;
				//new login
				User user = member.getICUser();
				if (user != null) {
					logIn(modinfo, user);
				}
				break;
			}
		}
		if (!returner) {
			//New login
			boolean newLogin = logInUser(modinfo, login, password);
			if (newLogin) {
				try {
					Member m = mh.findMemberByIWMemberSystemUser(modinfo.getCurrentUser());
					modinfo.setSessionAttribute(UserAttributeParameter, m);
					returner = true;
				}
				catch (FinderException e) {
					e.printStackTrace();
				}
			}
		}
		if (isAdmin(modinfo)) {
			modinfo.getSession().setAttribute(UserAccessAttributeParameter, "admin");
		}
		if (isDeveloper(modinfo)) {
			modinfo.getSession().setAttribute(UserAccessAttributeParameter, "developer");
		}
		if (isClubAdmin(modinfo)) {
			modinfo.getSession().setAttribute(UserAccessAttributeParameter, "club_admin");
		}
		if (isUser(modinfo)) {
			modinfo.getSession().setAttribute(UserAccessAttributeParameter, "user");
		}
		return returner;
	}

	public static void logOut2(IWContext modinfo) throws Exception {
		//System.out.print("inside logOut");
		modinfo.removeSessionAttribute(UserAttributeParameter);
		//if (modinfo.getSessionAttribute(UserAccessAttributeParameter) !=
		// null) {
		modinfo.removeSessionAttribute(UserAccessAttributeParameter);
		//}
		//Added on June 14th 2002 by Laddi
		modinfo.removeSessionAttribute(ACCESSCONTROL_GROUP_PARAMETER);
		modinfo.removeSessionAttribute(CURRENT_GOLF_UNION_ID_ATTRIBUTE);
		modinfo.removeSessionAttribute(CLUB_ADMIN_GOLF_UNION_ID_ATTRIBUTE);
		modinfo.removeSessionAttribute("member_id");
	}

	public void logOut(IWContext modinfo) throws Exception {
		try {
			super.logOut(modinfo);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		logOut2(modinfo);
	}

	public static boolean registerMemberLogin(int member_id, String user_login, String user_pass_one,
			String user_pass_two) throws SQLException {
		boolean returner = false;
		if (user_pass_one.equals(user_pass_two)) {
			LoginTable[] logTable = (LoginTable[]) ((LoginTable) IDOLookup.instanciateEntity(LoginTable.class)).findAllByColumn(
					"USER_LOGIN", user_login);
			if (logTable.length == 0) {
				LoginTable logT = (LoginTable) IDOLookup.createLegacy(LoginTable.class);
				logT.setMemberId(member_id);
				logT.setUserLogin(user_login);
				logT.setUserPassword(user_pass_one);
				logT.insert();
				returner = true;
			}
			else if (logTable.length == 1) {
				if (logTable[0].getMemberId() == member_id) {
					logTable[0].setMemberId(member_id);
					logTable[0].setUserLogin(user_login);
					logTable[0].setUserPassword(user_pass_one);
					logTable[0].update();
					returner = true;
				}
			}
			else {
				returner = false;
			}
		}
		if (returner) {
		}
		return returner;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.event.IWEventListener#actionPerformed(com.idega.event.IWPresentationEvent)
	 */
	public boolean actionPerformed(IWPresentationEvent e) throws IWException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.core.accesscontrol.business.LoginBusinessBean#logInByUUID(com.idega.presentation.IWContext,
	 *      java.lang.String)
	 */
	public boolean logInByUUID(IWContext iwc, String uuid) throws Exception {
		Member member = null;
		MemberHome home = (MemberHome) IDOLookup.getHomeLegacy(Member.class);
		try {
			//try to find by uuid in member first
			member = home.findByUniqueID(uuid);
		}
		catch (FinderException fe) {
			//if that fails get the user and then the member from the user
			UserHome userHome = (UserHome) IDOLookup.getHome(User.class);
			try {
				User user = userHome.findUserByUniqueId(uuid);
				member = home.findMemberByIWMemberSystemUser(user);
			}
			catch (FinderException e) {
				//Nothing found...
			}
		}
		if (member != null) {
			iwc.setSessionAttribute(UserAttributeParameter, member);
		}
		return super.logInByUUID(iwc, uuid);
	}
}