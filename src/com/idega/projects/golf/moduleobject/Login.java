package com.idega.projects.golf.moduleobject;

import com.idega.jmodule.*;
import com.idega.data.*;
import java.io.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import javax.servlet.http.*;
import java.sql.SQLException;
import java.util.*;
import com.idega.projects.golf.entity.*;




public class Login extends ModuleObjectContainer{


	public Login() {
		super();
	}

	public boolean isAdmin(ModuleInfo modinfo)throws SQLException{
		Member member = getMember(modinfo);
		if (member != null){
			Group[] access = member.getGroups();
			for(int i = 0; i < access.length; i++){
			if ("administrator".equals(access[i].getName()))
				return true;
			}
		}
		return false;
	}

       public boolean isDeveloper(ModuleInfo modinfo)throws SQLException{
		Member member = getMember(modinfo);
	    if (member != null){
         Group[] access = member.getGroups();
         for(int i = 0; i < access.length; i++){
           if ("developer".equals(access[i].getName()))
             return true;
         }
         }
         return false;
       }

       public boolean isClubAdmin(ModuleInfo modinfo)throws SQLException{
        Member member = getMember(modinfo);
		if (member != null){
         Group[] access = member.getGroups();
         for(int i = 0; i < access.length; i++){
           if ("club_admin".equals(access[i].getName()))
             return true;
         }
         }
         return false;
       }

       public boolean isUser(ModuleInfo modinfo)throws SQLException{
		Member member = getMember(modinfo);
         if (member != null){
          Group[] access = member.getGroups();
          for(int i = 0; i < access.length; i++){
             if ("user".equals(access[i].getName()))
               return true;
           }
         }
         return false;
       }


	public Member getMember(ModuleInfo modinfo){
		return (Member)modinfo.getSession().getAttribute("member_login");
	}

	private void logOut(ModuleInfo modinfo) throws IOException{
		modinfo.getSession().removeAttribute("member_login");
		if (modinfo.getSession().getAttribute("member_access") != null) {
			modinfo.getSession().removeAttribute("member_access");
		}

		modinfo.getResponse().sendRedirect("/index.jsp");

	}




	private boolean verifyPassword(ModuleInfo modinfo,String login, String password) throws SQLException{
		boolean returner = false;
		LoginTable[] login_table = (LoginTable[]) (new LoginTable()).findAllByColumn("user_login",login);

		for (int i = 0 ; i < login_table.length ; i++ ) {
			if (login_table[i].getUserPassword().equals(password)) {
				modinfo.getSession().setAttribute("member_login",new Member(login_table[i].getMemberId())   );
				returner = true;
			}
		}
		if (isAdmin(modinfo)) {
			modinfo.getSession().setAttribute("member_access","admin");
		}
		if (isDeveloper(modinfo)) {
			modinfo.getSession().setAttribute("member_access","developer");
		}
		if (isClubAdmin(modinfo)) {
			modinfo.getSession().setAttribute("member_access","club_admin");
		}
		if (isUser(modinfo)) {
			modinfo.getSession().setAttribute("member_access","user");
		}


		return returner;
	}


	private void nyskraning(ModuleInfo modinfo, String kennitala) throws IOException {
		modinfo.getResponse().sendRedirect("/test/nyskraning.jsp?kt="+kennitala);

	}


	private void smidaTable(){

        String color = "";
        String loginWidth = "148";
        String loginHeight = "90";
        String backgroundImageUrl = "/pics/templates/login/loginBack.gif";
        String userText = "Notandi";
        String userTextSize = "";
        String userTextColor = "";

        String passwordText = "Lykilorð";
        String passwordTextColor = "";
        String passwordTextSize = "";

        boolean vertical = false;

        String loginImageUrl = "/pics/templates/login/tengjast.gif";
        String newUserImageUrl = "/pics/templates/login/nyskraning.gif";
        String tryAgainImageUrl = "/pics/templates/login/reynaaftur_ny.gif";

        String userImageUrl = null;
          userImageUrl = "/pics/templates/notandi.gif";
        String passwordImageUrl = null;
          passwordImageUrl = "/pics/templates/lykilord.gif";

		Form myForm = new Form();
  //              myForm.submitTo(this);
			myForm.setMethod("post");
			myForm.maintainAllParameters();

		Table TveirTable = new Table(2,1);
//			TveirTable.setBorder(1);
//			TveirTable.setBackgroundImage(1,1,new com.idega.jmodule.object.Image(backgroundImageUrl));
			TveirTable.setAlignment(1,1,"center");
			if (!(color.equals(""))) {
			TveirTable.setColor(color);
			}
			TveirTable.setWidth(loginWidth);
			TveirTable.setHeight(loginHeight);
			TveirTable.setCellpadding(0);
			TveirTable.setCellspacing(0);

                        TveirTable.add(new com.idega.jmodule.object.Image("/pics/templates/login/bogi.gif"),2,1);
                        TveirTable.setVerticalAlignment(2,1,"top");

//			TveirTable.setHeight(1,1,"89");
//			EinnTable.add(TveirTable,1,1);

		Table TrirTable = new Table(1,2);
//			TrirTable.setBorder(1);
			TrirTable.setWidth("100%");
//			TrirTable.setWidth("146");
			TrirTable.setBackgroundImage(1,1,new com.idega.jmodule.object.Image(backgroundImageUrl));
			TrirTable.setHeight("100%");
			if (!(color.equals(""))) {
			TrirTable.setColor(color);
			}
			TrirTable.setCellpadding(0);
			TrirTable.setCellspacing(0);
			TrirTable.setBackgroundImage(1,2,new com.idega.jmodule.object.Image(""));
//			TrirTable.setColor(1,2,"#FFFFFF");



			Text loginTexti = null;
                            if (userText != null) {
                                loginTexti = new Text(userText);
				if (!(userTextSize.equals(""))) {
					loginTexti.setFontSize(Integer.parseInt(userTextSize));
				}
				if (!(userTextColor.equals(""))) {
					loginTexti.setFontColor(userTextColor);
				}
                            }
			Text passwordTexti = null;
                            if (passwordText != null) {
                                passwordTexti = new Text(passwordText);
				if (!(passwordTextSize.equals(""))) {
					passwordTexti.setFontSize(Integer.parseInt(passwordTextSize));
				}
				if (!(passwordTextColor.equals(""))) {
					passwordTexti.setFontColor(passwordTextColor);
				}
                            }


                        com.idega.jmodule.object.Image loginImage = null;
                          if (userImageUrl != null) {
                            loginImage = new com.idega.jmodule.object.Image(userImageUrl);
                          }

                        com.idega.jmodule.object.Image passwordImage = null;
                          if (passwordImageUrl != null) {
                            passwordImage = new com.idega.jmodule.object.Image(passwordImageUrl);
                          }
		Table uppi;


		if (!(vertical)) {
			uppi = new Table(5,2);
//			uppi.setBorder(1);
			if (!(color.equals(""))) {
			uppi.setColor(color);
			}
			uppi.setCellpadding(0);
			uppi.setCellspacing(0);
			uppi.setWidth("100%");
			TrirTable.add(uppi,1,1);

                        if (loginImage != null) {
                            uppi.add(loginImage,2,1);
                        }
                        else if (loginTexti != null) {
			    uppi.add(loginTexti,2,1);
                        }

			TextInput login = new TextInput("login");
				login.setAttribute("style","fontsize: 10pt");
				login.setSize(6);
			uppi.add(login,2,2);
			//uppi.setAlignment(2,1,"right");
			uppi.setAlignment(2,2,"left");

			if (passwordImage != null) {
                            uppi.add(passwordImage,4,1);
                        }
                        else if (passwordTexti != null) {
                            uppi.add(passwordTexti,4,1);
                        }
			PasswordInput passw = new PasswordInput("password");
				passw.setAttribute("style","fontsize: 10pt");
				passw.setSize(6);
			uppi.add(passw,4,2);


		}
		else {
			uppi = new Table(3,3);
			uppi.setBorder(0);
			if (!(color.equals(""))) {
			uppi.setColor(color);
			}
			uppi.setCellpadding(0);
			uppi.setCellspacing(0);
			uppi.setAlignment("center");
//			uppi.setWidth("100%");
			TrirTable.add(uppi,1,1);


			uppi.add(loginTexti,1,1);
			TextInput login = new TextInput("login");
				login.setAttribute("style","fontsize: 10pt");
				login.setSize(10);
			uppi.add(login,3,1);
//			uppi.setAlignment(1,1,"right");
//			uppi.setAlignment(2,2,"right");

			uppi.add(passwordTexti,1,3);
			PasswordInput passw = new PasswordInput("password");
				passw.setAttribute("style","fontsize: 10pt");
				passw.setSize(10);
			uppi.add(passw,3,3);
		}


		Table nidri = new Table(3,1);
			nidri.setBorder(0);
			if (!(color.equals(""))) {
			nidri.setColor(color);
			}
			nidri.setVerticalAlignment(1,1,"top");
			nidri.setVerticalAlignment(3,1,"top");
			nidri.setAlignment(1,1,"left");
			nidri.setAlignment(3,1,"right");
			nidri.setWidth("100%");
			nidri.setHeight("100%");
                        nidri.setCellpadding(0);
                        nidri.setCellspacing(0);
//			nidri.mergeCells(1,1,2,1);
			TrirTable.add(nidri,1,2);




			nidri.add(new SubmitButton(new com.idega.jmodule.object.Image(loginImageUrl),"tengja"),1,1);
			nidri.add(new SubmitButton(new com.idega.jmodule.object.Image(newUserImageUrl),"nyskraning"),3,1);


		TveirTable.add(TrirTable);

	myForm.add(TveirTable);
//	myForm.add(EinnTable);
	add(myForm);



	}


	public void isLoggedOn(ModuleInfo modinfo) throws SQLException{
        String color = "";
        String loginWidth = "150";
        String loginHeight = "90";
        String backgroundImageUrl = "/pics/templates/login/loginBack.gif";
        String userText = "Notandi";
        String userTextColor = "";
        String userTextSize = "";
        String passwordText = "Lykilorð";
        String passwordTextColor = "";
        String passwordTextSize = "";
        boolean vertical = false;

        String loginImageUrl = "/pics/templates/login/tengjast.gif";
        String newUserImageUrl = "/pics/templates/login/nyskraning.gif";
        String tryAgainImageUrl = "/pics/templates/login/reynaaftur_ny.gif";
        String logOutImageUrl = "/pics/templates/login/utskraning_ny.gif";







		Form myForm = new Form();
  //              myForm.submitTo(this);
			myForm.setMethod("post");
			myForm.maintainAllParameters();


		Table TveirTable = new Table(2,1);
//			TveirTable.setBorder(1);
//			TveirTable.setBackgroundImage(1,1,new com.idega.jmodule.object.Image(backgroundImageUrl));
			TveirTable.setAlignment(1,1,"center");
			if (!(color.equals(""))) {
			TveirTable.setColor(color);
			}
			TveirTable.setWidth(loginWidth);
			TveirTable.setHeight(loginHeight);
			TveirTable.setCellpadding(0);
			TveirTable.setCellspacing(0);

                        TveirTable.add(new com.idega.jmodule.object.Image("/pics/templates/login/bogi.gif"),2,1);
                        TveirTable.setVerticalAlignment(2,1,"top");

//			TveirTable.setHeight(1,1,"89");
//			EinnTable.add(TveirTable,1,1);

		Table TrirTable = new Table(1,2);
//			TrirTable.setBorder(1);
			TrirTable.setWidth("100%");
//			TrirTable.setWidth("146");
			TrirTable.setBackgroundImage(1,1,new com.idega.jmodule.object.Image(backgroundImageUrl));
			TrirTable.setHeight("100%");
			if (!(color.equals(""))) {
			TrirTable.setColor(color);
			}
			TrirTable.setCellpadding(0);
			TrirTable.setCellspacing(0);
			TrirTable.setBackgroundImage(1,2,new com.idega.jmodule.object.Image(""));
//			TrirTable.setColor(1,2,"#FFFFFF");


			Text loginTexti = new Text(userText);
				if (!(userTextSize.equals(""))) {
					loginTexti.setFontSize(Integer.parseInt(userTextSize));
				}
				if (!(userTextColor.equals(""))) {
					loginTexti.setFontColor(userTextColor);
				}
			Text passwordTexti = new Text(passwordText);
				if (!(passwordTextSize.equals(""))) {
					passwordTexti.setFontSize(Integer.parseInt(passwordTextSize));
				}
				if (!(passwordTextColor.equals(""))) {
					passwordTexti.setFontColor(passwordTextColor);
				}

		Table uppi = new Table();

			uppi.setBorder(0);
                        uppi.setWidth("100%");
                        Text user = new Text();
                                user.setBold();
                                user.setFontSize(1);

                        Member member = (Member) modinfo.getSession().getAttribute("member_login");
                        user.addToText(member.getName());

                        Link hlekkur = new Link(user,"/test/nyskraning.jsp?kt="+member.getSocialSecurityNumber());

                        uppi.add(hlekkur);
                        uppi.setAlignment(1,1,"center");


                TrirTable.add(uppi,1,1);

		Table nidri = new Table(1,1);
			nidri.setBorder(0);
			if (!(color.equals(""))) {
			nidri.setColor(color);
			}
			nidri.setVerticalAlignment(1,1,"top");
//			nidri.setVerticalAlignment(2,1,"top");
			nidri.setAlignment(1,1,"left");
//			nidri.setAlignment(2,1,"right");
			nidri.setWidth("100%");
			nidri.setHeight("100%");
                        nidri.setCellpadding(0);
                        nidri.setCellspacing(0);
//			nidri.mergeCells(1,1,2,1);
			TrirTable.add(nidri,1,2);




//			nidri.add(new SubmitButton(new com.idega.jmodule.object.Image(loginImageUrl),"tengja"),1,1);
//			nidri.add(new SubmitButton(new com.idega.jmodule.object.Image(newUserImageUrl),"nyskraning"),2,1);
			nidri.add(new SubmitButton(new com.idega.jmodule.object.Image(logOutImageUrl),""),1,1);
                        nidri.add(new HiddenInput("action","Útskráning"));
		TveirTable.add(TrirTable);

	myForm.add(TveirTable);
//	myForm.add(EinnTable);
	add(myForm);


	}

	public void isNotLoggedOn() {
        String color = "";
        String loginWidth = "148";
        String loginHeight = "90";
        String backgroundImageUrl = "/pics/templates/login/loginBack.gif";
        String userText = "Notandi";
        String userTextColor = "";
        String userTextSize = "";
        String passwordText = "Lykilorð";
        String passwordTextColor = "";
        String passwordTextSize = "";
        boolean vertical = false;

        String loginImageUrl = "/pics/templates/login/tengjast.gif";
        String newUserImageUrl = "/pics/templates/login/nyskraning.gif";
        String tryAgainImageUrl = "/pics/templates/login/reynaaftur_ny.gif";







		Form myForm = new Form();
  //              myForm.submitTo(this);
			myForm.setMethod("post");
			myForm.maintainAllParameters();


		Table TveirTable = new Table(2,1);
//			TveirTable.setBorder(1);
//			TveirTable.setBackgroundImage(1,1,new com.idega.jmodule.object.Image(backgroundImageUrl));
			TveirTable.setAlignment(1,1,"center");
			if (!(color.equals(""))) {
			TveirTable.setColor(color);
			}
			TveirTable.setWidth(loginWidth);
			TveirTable.setHeight(loginHeight);
			TveirTable.setCellpadding(0);
			TveirTable.setCellspacing(0);

                        TveirTable.add(new com.idega.jmodule.object.Image("/pics/templates/login/bogi.gif"),2,1);
                        TveirTable.setVerticalAlignment(2,1,"top");

//			TveirTable.setHeight(1,1,"89");
//			EinnTable.add(TveirTable,1,1);

		Table TrirTable = new Table(1,2);
//			TrirTable.setBorder(1);
//			TrirTable.setWidth("100%");
			TrirTable.setWidth("148");
			TrirTable.setBackgroundImage(1,1,new com.idega.jmodule.object.Image(backgroundImageUrl));
			TrirTable.setHeight("100%");
			if (!(color.equals(""))) {
			TrirTable.setColor(color);
			}
			TrirTable.setCellpadding(0);
			TrirTable.setCellspacing(0);
			TrirTable.setBackgroundImage(1,2,new com.idega.jmodule.object.Image(""));
//			TrirTable.setColor(1,2,"#FFFFFF");


			Text loginTexti = new Text(userText);
				if (!(userTextSize.equals(""))) {
					loginTexti.setFontSize(Integer.parseInt(userTextSize));
				}
				if (!(userTextColor.equals(""))) {
					loginTexti.setFontColor(userTextColor);
				}
			Text passwordTexti = new Text(passwordText);
				if (!(passwordTextSize.equals(""))) {
					passwordTexti.setFontSize(Integer.parseInt(passwordTextSize));
				}
				if (!(passwordTextColor.equals(""))) {
					passwordTexti.setFontColor(passwordTextColor);
				}

		Table uppi = new Table();

			uppi.setBorder(0);
                        uppi.setWidth("100%");
                        Text failed = new Text("Innskráning mistókst");
                            failed.setFontSize(1);
                        uppi.add(failed);
                        uppi.setAlignment(1,1,"center");


                TrirTable.add(uppi,1,1);

		Table nidri = new Table(1,1);
			nidri.setBorder(0);
			if (!(color.equals(""))) {
			nidri.setColor(color);
			}
			nidri.setVerticalAlignment(1,1,"top");
			nidri.setAlignment(1,1,"left");
			nidri.setWidth("100%");
			nidri.setHeight("100%");
                        nidri.setCellpadding(0);
                        nidri.setCellspacing(0);
//			nidri.mergeCells(1,1,2,1);
			TrirTable.add(nidri,1,2);




//			nidri.add(new SubmitButton(new com.idega.jmodule.object.Image(loginImageUrl),"tengja"),1,1);
//			nidri.add(new SubmitButton(new com.idega.jmodule.object.Image(newUserImageUrl),"nyskraning"),2,1);
			nidri.add(new SubmitButton(new com.idega.jmodule.object.Image(tryAgainImageUrl),"Reyna aftur"),1,1);

		TveirTable.add(TrirTable);

	myForm.add(TveirTable);
//	myForm.add(EinnTable);
	add(myForm);


	}
	public void isNotSignedOn(String what) {
        String color = "";
        String loginWidth = "148";
        String loginHeight = "90";
        String backgroundImageUrl = "/pics/templates/login/loginBack.gif";
        String userText = "Notandi";
        String userTextColor = "";
        String userTextSize = "";
        String passwordText = "Lykilorð";
        String passwordTextColor = "";
        String passwordTextSize = "";
        boolean vertical = false;

        String loginImageUrl = "/pics/templates/login/tengjast.gif";
        String newUserImageUrl = "/pics/templates/login/nyskraning.gif";
        String tryAgainImageUrl = "/pics/templates/login/reynaaftur_ny.gif";







		Form myForm = new Form();
  //              myForm.submitTo(this);
			myForm.setMethod("post");
			myForm.maintainAllParameters();


		Table TveirTable = new Table(2,1);
//			TveirTable.setBorder(1);
//			TveirTable.setBackgroundImage(1,1,new com.idega.jmodule.object.Image(backgroundImageUrl));
			TveirTable.setAlignment(1,1,"center");
			if (!(color.equals(""))) {
			TveirTable.setColor(color);
			}
			TveirTable.setWidth(loginWidth);
			TveirTable.setHeight(loginHeight);
			TveirTable.setCellpadding(0);
			TveirTable.setCellspacing(0);

                        TveirTable.add(new com.idega.jmodule.object.Image("/pics/templates/login/bogi.gif"),2,1);
                        TveirTable.setVerticalAlignment(2,1,"top");

//			TveirTable.setHeight(1,1,"89");
//			EinnTable.add(TveirTable,1,1);

		Table TrirTable = new Table(1,2);
//			TrirTable.setBorder(1);
//			TrirTable.setWidth("100%");
			TrirTable.setWidth("148");
			TrirTable.setBackgroundImage(1,1,new com.idega.jmodule.object.Image(backgroundImageUrl));
			TrirTable.setHeight("100%");
			if (!(color.equals(""))) {
			TrirTable.setColor(color);
			}
			TrirTable.setCellpadding(0);
			TrirTable.setCellspacing(0);
			TrirTable.setBackgroundImage(1,2,new com.idega.jmodule.object.Image(""));
//			TrirTable.setColor(1,2,"#FFFFFF");


			Text loginTexti = new Text(userText);
				if (!(userTextSize.equals(""))) {
					loginTexti.setFontSize(Integer.parseInt(userTextSize));
				}
				if (!(userTextColor.equals(""))) {
					loginTexti.setFontColor(userTextColor);
				}
			Text passwordTexti = new Text(passwordText);
				if (!(passwordTextSize.equals(""))) {
					passwordTexti.setFontSize(Integer.parseInt(passwordTextSize));
				}
				if (!(passwordTextColor.equals(""))) {
					passwordTexti.setFontColor(passwordTextColor);
				}

		Table uppi = new Table();

			uppi.setBorder(0);
                        uppi.setWidth("100%");
                        Text failed = new Text("Innskráning mistókst");
                            failed.setFontSize(1);
                          if (what.equals("empty")) {
                                  failed.setText("Skrifið kennitölu í notandareitinn");
                          }
                          else if (what.equals("toBig")) {
                                  failed.setText("Kennitala skal vera skrifuð án bandstriks");
                          }
                        uppi.add(failed);
                        uppi.setAlignment(1,1,"center");


                TrirTable.add(uppi,1,1);

		Table nidri = new Table(1,1);
			nidri.setBorder(0);
			if (!(color.equals(""))) {
			nidri.setColor(color);
			}
			nidri.setVerticalAlignment(1,1,"top");
			nidri.setAlignment(1,1,"left");
			nidri.setWidth("100%");
			nidri.setHeight("100%");
                        nidri.setCellpadding(0);
                        nidri.setCellspacing(0);
//			nidri.mergeCells(1,1,2,1);
			TrirTable.add(nidri,1,2);




//			nidri.add(new SubmitButton(new com.idega.jmodule.object.Image(loginImageUrl),"tengja"),1,1);
//			nidri.add(new SubmitButton(new com.idega.jmodule.object.Image(newUserImageUrl),"nyskraning"),2,1);
			nidri.add(new SubmitButton(new com.idega.jmodule.object.Image(tryAgainImageUrl),"Reyna aftur"),1,1);

		TveirTable.add(TrirTable);

	myForm.add(TveirTable);
//	myForm.add(EinnTable);
	add(myForm);



	}


	public void main(ModuleInfo modinfo) throws SQLException, IOException{

		if (modinfo.getSession().getAttribute("member_login") == null) {
			if (modinfo.getRequest().getParameter("tengja.x") != null) {
				boolean canLogin = false;
				if ((modinfo.getRequest().getParameter("login") != null) && (modinfo.getRequest().getParameter("password") != null)) {
					canLogin = verifyPassword(modinfo, modinfo.getRequest().getParameter("login"),modinfo.getRequest().getParameter("password"));
					if (canLogin) {
						modinfo.getResponse().sendRedirect(modinfo.getRequest().getRequestURI());
//						isLoggedOn(modinfo);
						if (isAdmin(modinfo)) {
//							modinfo.getResponse().sendRedirect("/");
//							getParentPage().setToReload();
						}
					}
					else {
						isNotLoggedOn();
					}
				}
			}

			else if (modinfo.getRequest().getParameter("nyskraning.x")!= null){
				boolean canSignIn = false;
				boolean ofLangt = false;

				if (modinfo.getRequest().getParameter("login") != null ){
					if (!(modinfo.getRequest().getParameter("login").equals(""))) {
						canSignIn=true;
						if (modinfo.getRequest().getParameter("login").length() != 10 ) {
							ofLangt = true;
							canSignIn=false;
						}

					}
				}

				if (canSignIn) {
					nyskraning(modinfo,modinfo.getRequest().getParameter("login"));
				}
				else {
					if (ofLangt) {
						isNotSignedOn("toBig");
					}
					else {
						isNotSignedOn("empty");
					}
				}
//			add("nýskráning");
			}
			else {
				smidaTable();
			}
		}
		else {
			isLoggedOn(modinfo);
		}

		if (modinfo.getRequest().getParameter("action") != null ) {
			if (modinfo.getRequest().getParameter("action").equals("Útskráning") ) {
				logOut(modinfo);
			}
		}


	}



}
