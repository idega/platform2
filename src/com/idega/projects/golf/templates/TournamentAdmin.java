//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.projects.golf.templates;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.projects.golf.entity.*;
import com.idega.projects.golf.*;
import com.idega.projects.golf.moduleobject.*;
import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import java.sql.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import java.io.*;
import java.util.*;
import com.idega.jmodule.template.*;


/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.0
*/
public abstract class TournamentAdmin extends JmoduleWindowModule{


	/*public final void __theService(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		//super._jspService();
		try{
			main__();
		}

		catch(Exception ex){
			handleException(ex);
		}
	}


	private final void main__()throws Exception{
		String tournament = getParameter("tournament");
		if (tournament==null){
			selectTournament();
		}
		else{
			setTournament(tournament);
			main();
		}
	}


	public void handleException(Exception ex){
		System.out.println("Villa varð: "+ex.getMessage());
	}

	public Tournament getTournament(){
		return (Tournament) getSessionAttribute("tournament_admin_tournament");
	}

	public void setTournament(String tournament_id)throws Exception{
		setSessionAttribute("tournament_admin_tournament",new Tournament(Integer.parseInt(tournament_id)));
	}

	public void selectTournament()throws Exception{
		GolfDialog dialog = new GolfDialog("Veldu mót");
		add(dialog);
		dialog.add(new DropdownMenu((new Tournament()).findAll()));
		dialog.add(new SubmitButton("adminbutton_1","Áfram"));
	}


	public final void actionPerformed(ModuleEvent ev)throws Exception{
		if(getParameter("adminbutton_1") != null){
			setTournament(getParameter("tournament"));
			main();
		}
      actionPerformed2(ev);

	}

   public void setPageAttribute(String attributeName,Object attribute){
      setSessionAttribute(getModuleInfo().getRequest().getRequestURI()+attributeName,attribute);
   }

   public Object getPageAttribute(String attributeName){
      return getSessionAttribute(getModuleInfo().getRequest().getRequestURI()+attributeName);
   }

   public void removePageAttribute(String attributeName){
      removeSessionAttribute(getModuleInfo().getRequest().getRequestURI()+attributeName);
   }

   public void removeAllPageAttributes(){
      Enumeration enum = getModuleInfo().getSession().getAttributeNames();
      String attributeName;
      String Page = getModuleInfo().getRequest().getRequestURI();
      while (enum.hasMoreElements()){
         attributeName = (String)enum.nextElement();
         if (attributeName.indexOf(Page) != -1){
            removeSessionAttribute(attributeName);
         }
      }
   }

   public abstract void actionPerformed2(ModuleEvent ev)throws Exception;

	public abstract void main()throws Exception;


 */

        public void _main(ModuleInfo modinfo)throws Exception{
          super._main(modinfo);
          getPage().setTitle("Mótastjóri");
        }

	public Member getMember(){
		return (Member)getModuleInfo().getSession().getAttribute("member_login");
	}



        public boolean isAdmin() {

          try{
            return com.idega.jmodule.login.business.AccessControl.isAdmin(getModuleInfo());
          }catch (SQLException E) {
            /*
            out.print("SQLException: " + E.getMessage());
            out.print("SQLState:     " + E.getSQLState());
            out.print("VendorError:  " + E.getErrorCode());*/
          }catch (Exception E) {
		E.printStackTrace();
	  }finally {
	  }
          return false;
        }



        public boolean isDeveloper() {
            return com.idega.jmodule.login.business.AccessControl.isDeveloper(getModuleInfo());
       }

        public boolean isClubAdmin() {
            return com.idega.jmodule.login.business.AccessControl.isClubAdmin(getModuleInfo());
        }

        public boolean isUser() {
            return com.idega.jmodule.login.business.AccessControl.isUser(getModuleInfo());
        }



}  // class
