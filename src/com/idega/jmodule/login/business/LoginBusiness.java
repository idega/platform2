//idega 2000 - Tryggvi Larusson - Grimur Jonsson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.login.business;


import com.idega.presentation.*;
//import com.idega.presentation.ui.*;
import com.idega.data.genericentity.*;
import com.idega.jmodule.login.data.*;
import java.sql.*;
import java.io.*;
import com.idega.business.IWEventListener;
import com.idega.idegaweb.IWException;

/**
 * Title:        LoginBusiness
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>,<a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.1
 */


public class LoginBusiness implements IWEventListener{


  public static String UserAttributeParameter="member_login";
  public static String UserAccessAttributeParameter="member_access";
  public static String LoginStateParameter="login_state";

  public LoginBusiness() {
  }


  public static boolean isLoggedOn(IWContext iwc){
      if(iwc.getSessionAttribute(UserAttributeParameter)==null){
        return false;
      }
      return true;
  }

  public static void internalSetState(IWContext iwc,String state){
      iwc.setSessionAttribute(LoginStateParameter,state);
  }

  public static String internalGetState(IWContext iwc){
      return (String) iwc.getSessionAttribute(LoginStateParameter);
  }

  public void actionPerformed(IWContext iwc)throws IWException{
        //System.out.println("LoginBusiness.actionPerformed");

        try{

            if(isLoggedOn(iwc)){
                  String controlParameter = iwc.getParameter(LoginBusiness.LoginStateParameter);
                  if (controlParameter != null) {

                        if(controlParameter.equals("logoff")){
                            logOut(iwc);
                            internalSetState(iwc,"loggedoff");

                        }

                  }


            }
            else{
                  String controlParameter = iwc.getParameter(LoginBusiness.LoginStateParameter);

                  if (controlParameter != null) {
                      if(controlParameter.equals("login")){

                                boolean canLogin = false;
				if ((iwc.getParameter("login") != null) && (iwc.getParameter("password") != null)) {
					canLogin = verifyPassword(iwc, iwc.getParameter("login"),iwc.getParameter("password"));
					if (canLogin) {
						isLoggedOn(iwc);
                                                internalSetState(iwc,"loggedon");
					}
					else {
                                                internalSetState(iwc,"loginfailed");
					}
				}
			}
                        else if(controlParameter.equals("tryagain")){

                            internalSetState(iwc,"loggedoff");

                        }

		}
            }

      }
      catch(Exception ex){
          ex.printStackTrace(System.err);
          //throw (IdegaWebException)ex.fillInStackTrace();
      }

  }

  public boolean isAdmin(IWContext iwc)throws SQLException{
    return AccessControl.isAdmin(iwc);
  }


  public static Member getMember(IWContext iwc){
    return (Member)iwc.getSession().getAttribute(UserAttributeParameter);
  }


  private boolean verifyPassword(IWContext iwc,String login, String password) throws IOException,SQLException{
          boolean returner = false;
          LoginTable[] login_table = (LoginTable[]) (new LoginTable()).findAllByColumn("user_login",login);

          for (int i = 0 ; i < login_table.length ; i++ ) {
                  if (login_table[i].getUserPassword().equals(password)) {
                          iwc.getSession().setAttribute(UserAttributeParameter,new Member(login_table[i].getMemberId())   );
                          returner = true;
                  }
          }
          if (isAdmin(iwc)) {
                  iwc.getSession().setAttribute(UserAccessAttributeParameter,"admin");
          }
/*		if (isDeveloper(iwc)) {
                  iwc.getSession().setAttribute("member_access","developer");
          }
          if (isClubAdmin(iwc)) {
                  iwc.getSession().setAttribute("member_access","club_admin");
          }
          if (isUser(iwc)) {
                  iwc.getSession().setAttribute("member_access","user");
          }
*/
          return returner;
  }



    private void logOut(IWContext iwc) throws Exception{
            //System.out.print("inside logOut");
            iwc.removeSessionAttribute(UserAttributeParameter);
            if (iwc.getSessionAttribute(UserAccessAttributeParameter) != null) {
                    iwc.removeSessionAttribute(UserAccessAttributeParameter);
            }
    }

    public static boolean registerMemberLogin(int member_id,String user_login,String user_pass_one,String user_pass_two) throws SQLException {
        boolean returner = false;

        if (user_pass_one.equals(user_pass_two)) {
            LoginTable[] logTable = (LoginTable[]) (new LoginTable()).findAllByColumn("USER_LOGIN",user_login);
            if (logTable.length == 0) {
                LoginTable logT = new LoginTable();
                  logT.setMemberId(member_id);
                  logT.setUserLogin(user_login);
                  logT.setUserPassword(user_pass_one);
                logT.insert();
                returner = true;
            }
            else if (logTable.length == 1) {
                if (logTable[0].getMemberId()  == member_id ) {
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
