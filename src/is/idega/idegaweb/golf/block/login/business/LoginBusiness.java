// idega 2000 - Tryggvi Larusson - Grimur Jonsson
/*
 * Copyright 2000 idega.is All Rights Reserved.
 */

package is.idega.idegaweb.golf.block.login.business;

import is.idega.idegaweb.golf.block.login.data.LoginTable;
import is.idega.idegaweb.golf.block.login.data.LoginTableHome;
import is.idega.idegaweb.golf.entity.Member;
import is.idega.idegaweb.golf.entity.MemberHome;

import java.io.IOException;
import java.sql.SQLException;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.data.IDOLookup;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;

/**
 * Title: LoginBusiness Description: Copyright: Copyright (c) 2000-2001 idega.is
 * All Rights Reserved Company: idega
 * 
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson </a>, <a
 *         href="mailto:tryggvi@idega.is">Tryggvi Larusson </a>
 * @version 1.1
 */

public class LoginBusiness extends LoginBusinessBean implements IWPageEventListener {

	public static String UserAttributeParameter = "member_login";
	public static String UserAccessAttributeParameter = "member_access";
	public static String LoginStateParameter = GolfLoginBusiness.LoginStateParameter;//"login_state";

	public LoginBusiness() {
	}

	public static boolean isLoggedOn(IWContext modinfo) {
		if (modinfo.getSessionAttribute(UserAttributeParameter) == null) {
			return false;
		}
		return true;
	}

	public static void internalSetState(IWContext modinfo, String state) {
		modinfo.setSessionAttribute(LoginStateParameter, state);
	}

	public static String internalGetStateString(IWContext modinfo) {
		return (String) modinfo.getSessionAttribute(LoginStateParameter);
	}

	public boolean actionPerformed(IWContext modinfo) throws IWException {
		//System.out.println("LoginBusiness.actionPerformed");

		try {

			if (isLoggedOn(modinfo)) {
				String controlParameter = modinfo.getParameter(LoginBusiness.LoginStateParameter);
				if (controlParameter != null) {

					if (controlParameter.equals("logoff")) {
						logOut(modinfo);
						internalSetState(modinfo, "loggedoff");

					}

				}

			}
			else {
				String controlParameter = modinfo.getParameter(LoginBusiness.LoginStateParameter);

				if (controlParameter != null) {
					if (controlParameter.equals("login")) {

						boolean canLogin = false;
						if ((modinfo.getParameter("login") != null) && (modinfo.getParameter("password") != null)) {
							canLogin = verifyPassword(modinfo, modinfo.getParameter("login"), modinfo.getParameter("password"));
							if (canLogin) {
								isLoggedOn(modinfo);
								internalSetState(modinfo, "loggedon");
							}
							else {
								internalSetState(modinfo, "loginfailed");
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

	public boolean isAdmin(IWContext modinfo) throws SQLException {
		return AccessControl.isAdmin(modinfo);
	}

	public static Member getMember(IWContext modinfo) {
		return (Member) modinfo.getSession().getAttribute(UserAttributeParameter);
	}

	private boolean verifyPassword(IWContext modinfo, String login, String password) throws IOException, SQLException {
		boolean returner = false;
		LoginTable[] login_table = (LoginTable[]) ((LoginTable) IDOLookup.instanciateEntity(LoginTable.class)).findAllByColumn("user_login", login);
		MemberHome mh = ((MemberHome) IDOLookup.getHomeLegacy(Member.class));
		
		for (int i = 0; i < login_table.length; i++) {
			if (login_table[i].getUserPassword().equals(password)) {
				try {
					Member member = mh.findByPrimaryKey(login_table[i].getMemberId());
					modinfo.getSession().setAttribute(UserAttributeParameter, member);
				
					returner = true;
					
					User user = member.getICUser();
	                if(user!=null) {
	                		try {
							logInAsAnotherUser(modinfo,user);
						} catch (Exception e) {
							e.printStackTrace();
						}
	                }
	                
	                break;
				}
				catch (FinderException fe) {
					throw new SQLException(fe.getMessage());
				}
			}
		}
		
        if(!returner) {
	    		//New login
	    		boolean newLogin = logInUser(modinfo,login,password);
	    		if(newLogin) {
	    			try {
					Member m = mh.findMemberByIWMemberSystemUser(modinfo.getCurrentUser());
					modinfo.setSessionAttribute(UserAttributeParameter,m);
				} catch (FinderException e) {
					e.printStackTrace();
				}
	    		}
        }

        
		if (isAdmin(modinfo)) {
			modinfo.getSession().setAttribute(UserAccessAttributeParameter, "admin");
		}

		return returner;
	}

	protected void logOut(IWContext modinfo) throws Exception {
		//System.out.print("inside logOut");
		
		try {
			super.logOut(modinfo);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			super.logOut(modinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		modinfo.removeSessionAttribute(UserAttributeParameter);
		if (modinfo.getSessionAttribute(UserAccessAttributeParameter) != null) {
			modinfo.removeSessionAttribute(UserAccessAttributeParameter);
		}
	}

	public static boolean registerMemberLogin(int member_id, String user_login, String user_pass_one, String user_pass_two) throws SQLException {
		boolean returner = false;

		if (user_pass_one.equals(user_pass_two)) {
			LoginTable[] logTable = (LoginTable[]) ((LoginTable) IDOLookup.instanciateEntity(LoginTable.class)).findAllByColumn("USER_LOGIN", user_login);
			if (logTable.length == 0) {
				try {
					LoginTable logT = ((LoginTableHome) IDOLookup.getHomeLegacy(LoginTable.class)).create();
					logT.setMemberId(member_id);
					logT.setUserLogin(user_login);
					logT.setUserPassword(user_pass_one);
					logT.insert();
				}
				catch (CreateException ce) {
					throw new SQLException(ce.getMessage());
				}
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


}