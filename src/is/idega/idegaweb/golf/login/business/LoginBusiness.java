//idega 2000 - Tryggvi Larusson - Grimur Jonsson

/*

*Copyright 2000 idega.is All Rights Reserved.

*/



package is.idega.idegaweb.golf.login.business;





import com.idega.presentation.*;

//import com.idega.presentation.ui.*;

import is.idega.idegaweb.golf.entity.Member;

import com.idega.jmodule.login.data.*;

import com.idega.data.genericentity.Group;
import com.idega.event.IWPageEventListener;

import java.sql.*;

import java.io.*;


import com.idega.idegaweb.IWException;



/**

 * Title:        LoginBusiness

 * Description:

 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved

 * Company:      idega

  *@author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>,<a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>

 * @version 1.1

 */





public class LoginBusiness implements IWPageEventListener{





  public static String UserAttributeParameter="member_login";

  public static String UserAccessAttributeParameter="member_access";

  public static String LoginStateParameter="login_state";

  public static String newLoginStateParameter="new_login_state";



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







  public boolean actionPerformed(IWContext iwc)throws IWException{

    try{



      if(isLoggedOn(iwc)){

        String controlParameter = iwc.getParameter(LoginBusiness.LoginStateParameter);

        if (controlParameter != null) {

          if(controlParameter.equals("logoff")){

            logOut(iwc);

            internalSetState(iwc,"loggedoff");

          }

        }

      } else {

        String controlParameter = iwc.getParameter(LoginBusiness.LoginStateParameter);



        if (controlParameter != null) {

          if(controlParameter.equals("login")){

            if (iwc.getParameter(newLoginStateParameter)!= null || iwc.getParameter(newLoginStateParameter+ ".x")!= null ){

              String temp = iwc.getRequest().getParameter("login");

              if(temp != null){

                if(temp.length() == 10){

                  registerLogin(iwc,iwc.getRequest().getParameter("login"));

                  internalSetState(iwc,"loggedoff");

                }else {

                  internalSetState(iwc,"newlogin");

                }

              }else{

                internalSetState(iwc,"newlogin");

              }

            }else{

              boolean canLogin = false;

              if ((iwc.getParameter("login") != null) && (iwc.getParameter("password") != null)) {

                canLogin = verifyPassword(iwc, iwc.getParameter("login"),iwc.getParameter("password"));

                if (canLogin) {

                  isLoggedOn(iwc);

                  internalSetState(iwc,"loggedon");

                } else {

                  internalSetState(iwc,"loginfailed");

                }

              }

            }

          }else if(controlParameter.equals("tryagain")){

            internalSetState(iwc,"loggedoff");

          }

        }

      }

    }

    catch(Exception ex){

        ex.printStackTrace(System.err);

        //throw (IdegaWebException)ex.fillInStackTrace();

    }


    return true;
  }



  private void registerLogin(IWContext iwc, String kennitala) throws IOException {

            iwc.getResponse().sendRedirect("/createlogin.jsp?kt="+kennitala);

    }



  public boolean isAdmin(IWContext iwc)throws SQLException{

    return com.idega.jmodule.login.business.AccessControl.isAdmin(iwc);

  }



   public boolean isDeveloper(IWContext iwc)throws SQLException{

            Member member = getMember(iwc);

        if (member != null){

     Group[] access = member.getGroups();

     for(int i = 0; i < access.length; i++){

       if ("developer".equals(access[i].getName()))

         return true;

     }

     }

     return false;

   }



   public boolean isClubAdmin(IWContext iwc)throws SQLException{

    Member member = getMember(iwc);

            if (member != null){

     Group[] access = member.getGroups();

     for(int i = 0; i < access.length; i++){

       if ("club_admin".equals(access[i].getName()))

         return true;

     }

     }

     return false;

   }



   public boolean isUser(IWContext iwc)throws SQLException{

            Member member = getMember(iwc);

     if (member != null){

      Group[] access = member.getGroups();

      for(int i = 0; i < access.length; i++){

         if ("user".equals(access[i].getName()))

           return true;

       }

     }

     return false;

   }







  public static Member getMember(IWContext iwc){

    return (Member)iwc.getSession().getAttribute(UserAttributeParameter);

  }





  private boolean verifyPassword(IWContext iwc,String login, String password) throws SQLException{

		boolean returner = false;

		LoginTable[] login_table = (LoginTable[]) (new LoginTable()).findAllByColumn("user_login",login);



		for (int i = 0 ; i < login_table.length ; i++ ) {

                  if (login_table[i].getUserPassword().equals(password)) {

                    //iwc.getSession().setAttribute("member_login",((is.idega.idegaweb.golf.entity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKeyLegacy(login_table[i].getMemberId())   );

                    iwc.setSessionAttribute(UserAttributeParameter,((is.idega.idegaweb.golf.entity.MemberHome)com.idega.data.IDOLookup.getHomeLegacy(Member.class)).findByPrimaryKeyLegacy(login_table[i].getMemberId()) );

                    returner = true;

                  }

		}

		if (isAdmin(iwc)) {

			iwc.getSession().setAttribute("member_access","admin");

		}

                if(isDeveloper(iwc)) {

			iwc.getSession().setAttribute("member_access","developer");

		}

                if (isClubAdmin(iwc)) {

			iwc.getSession().setAttribute("member_access","club_admin");

		}

                if (isUser(iwc)) {

			iwc.getSession().setAttribute("member_access","user");

		}



		return returner;

	}







    public static void logOut2(IWContext iwc) throws Exception{

            //System.out.print("inside logOut");

            iwc.removeSessionAttribute(UserAttributeParameter);

            if (iwc.getSessionAttribute(UserAccessAttributeParameter) != null) {

                    iwc.removeSessionAttribute(UserAccessAttributeParameter);

            }

    }





    public void logOut(IWContext iwc) throws Exception{

      logOut2(iwc);

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

