//idega 2000 - Tryggvi Larusson
/*
*Copyright 2000 idega.is All Rights Reserved.
*/

package com.idega.jmodule.login.business;

import com.idega.presentation.text.*;
import com.idega.presentation.*;
import java.util.*;
import java.io.*;
import com.idega.jmodule.login.data.*;
import java.sql.*;
import java.io.*;
import com.idega.data.genericentity.*;

/**
*@author <a href="mailto:tryggvi@idega.is">Tryggvi Larusson</a>
*@version 1.0
*/

public class AccessControl{

        public static final String ACCESSCONTROL_GROUP_PARAMETER = "iw_accesscontrol_group";

        public static final String ADMIN_GROUP = "administrator";
        public static final String CLUB_ADMIN_GROUP = "club_admin";
        public static final String CLUB_WORKER_GROUP = "club_worker";
        public static final String TOURNAMENT_MANAGER_GROUP = "tournament_manager";

        public static final String CURRENT_GOLF_UNION_ID_ATTRIBUTE = "golf_union_id";
        public static final String CLUB_ADMIN_GOLF_UNION_ID_ATTRIBUTE = "admin_golf_union_id";


	public static Member getMember(IWContext iwc){
		return LoginBusiness.getMember(iwc);
	}

        public static String getAccesscontrolGroupForUser(IWContext iwc){
          return (String)iwc.getSessionAttribute(ACCESSCONTROL_GROUP_PARAMETER);
        }

        public static void setCurrentGolfUnionID(IWContext iwc,String unionID){
          iwc.setSessionAttribute(CURRENT_GOLF_UNION_ID_ATTRIBUTE,unionID);
        }

        public static String getCurrentGolfUnionID(IWContext iwc){
          return (String)iwc.getSessionAttribute(CURRENT_GOLF_UNION_ID_ATTRIBUTE);
        }

        public static String getGolfUnionOfClubAdmin(IWContext iwc){
          return (String)iwc.getSessionAttribute(CLUB_ADMIN_GOLF_UNION_ID_ATTRIBUTE);
        }

        static void setGolfUnionOfClubAdmin(IWContext iwc,String unionID){
          iwc.setSessionAttribute(CLUB_ADMIN_GOLF_UNION_ID_ATTRIBUTE,unionID);
        }

        public static void setAccesscontrolGroupForUser(IWContext iwc,String accessControlGroup){
          iwc.setSessionAttribute(ACCESSCONTROL_GROUP_PARAMETER,accessControlGroup);
        }

	public static boolean isAdmin(IWContext iwc)throws SQLException{
		Member member = getMember(iwc);
                if(member!=null){
                    if(member instanceof com.idega.projects.golf.entity.Member){
                      String acc_group = getAccesscontrolGroupForUser(iwc);
                      if(acc_group==null){
                        com.idega.projects.golf.entity.Member membi = (com.idega.projects.golf.entity.Member)member;
                    	Group[] access = membi.getGroups(); //  (member).getGenericGroups();
			for(int i = 0; i < access.length; i++){
                          if ("administrator".equals(access[i].getName())){
                                  setAccesscontrolGroupForUser(iwc,ADMIN_GROUP);
                                  return true;
                          }
                          if ("club_admin".equals(access[i].getName())){
                            int uni_id = membi.getMainUnionID();
                            setAccesscontrolGroupForUser(iwc,CLUB_ADMIN_GROUP);
                            setGolfUnionOfClubAdmin(iwc,Integer.toString(uni_id));
                            Object ID = iwc.getSessionAttribute(CURRENT_GOLF_UNION_ID_ATTRIBUTE);
                            if( ID != null){
                              if (uni_id == Integer.parseInt( ((String)ID) ) ){
                                return true;
                              }
                            }

                          }

                        }
                        return false;
                      }
                      else{
                          if(acc_group.equals(ADMIN_GROUP)){
                            return true;
                          }
                          else if(acc_group.equals(CLUB_ADMIN_GROUP)){
                            String currentUnion = getCurrentGolfUnionID(iwc);
                            if(currentUnion!=null){
                              if(currentUnion.equals(getGolfUnionOfClubAdmin(iwc))){
                                return true;
                              }
                            }
                            return false;
                          }
                      }
                    }
                    else{
                      String acc_group = getAccesscontrolGroupForUser(iwc);
                      if(acc_group==null){
			LoginType[] access = member.getLoginType();
                        if (access != null){
                          for(int i = 0; i < access.length; i++){
                              if ("administrator".equals(access[i].getName())){
                                  setAccesscontrolGroupForUser(iwc,ADMIN_GROUP);
                                  return true;
                              }
                          }
                        }
                      }
                      else{
                        if(acc_group.equals(ADMIN_GROUP)){
                          return true;
                        }
                      }
                    }

		}
		return false;
	}




        public static boolean hasPermission(String permissionType, PresentationObject obj,IWContext info){
          try {
            return com.idega.core.accesscontrol.business.AccessControl.hasPermission(permissionType,obj,info);
          }
          catch (Exception ex) {
            ex.printStackTrace();
            return false;
          }


        }



        /*public boolean isAdmin(IWContext iwc) {

          try{
            return com.idega.jmodule.login.business.AccessControl.isAdmin(getIWContext());
          }catch (SQLException E) {

            //out.print("SQLException: " + E.getMessage());
            //out.print("SQLState:     " + E.getSQLState());
            //out.print("VendorError:  " + E.getErrorCode());
          }catch (Exception E) {
		E.printStackTrace();
	  }finally {
	  }
          return false;
        }*/



        public static boolean isDeveloper(IWContext iwc) {
			if (iwc.getSession().getAttribute("member_access") != null) {
				if (iwc.getSession().getAttribute("member_access").equals("developer")) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
        }

        public static boolean isClubAdmin(IWContext iwc) {
			if (iwc.getSession().getAttribute("member_access") != null) {
				if (iwc.getSession().getAttribute("member_access").equals("club_admin")) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
        }

        public static boolean isUser(IWContext iwc) {
			if (iwc.getSession().getAttribute("member_access") != null) {
				if (iwc.getSession().getAttribute("member_access").equals("user")) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
        }




        public static boolean isClubWorker(IWContext iwc) throws SQLException{
		Member member = getMember(iwc);
                if(member!=null){
                    if(member instanceof com.idega.projects.golf.entity.Member){

                        com.idega.projects.golf.entity.Member membi = (com.idega.projects.golf.entity.Member)member;
                    	Group[] access = membi.getGroups(); //  (member).getGenericGroups();
			for(int i = 0; i < access.length; i++){
                          if ("administrator".equals(access[i].getName())){

                                  return true;
                          }

                          if ("club_worker".equals(access[i].getName())){
                            Object ID = iwc.getSessionAttribute("golf_union_id");
                            if( ID != null){
                              int uni_id = membi.getMainUnionID();
                              if (uni_id == Integer.parseInt( ((String)ID) ) ){
                                return true;
                              }
                            }

                          }

                        }
                        return false;

                    }
                    else{

			LoginType[] access = member.getLoginType();
                        if (access != null){
                          for(int i = 0; i < access.length; i++){
                              if ("administrator".equals(access[i].getName())){
                                  return true;
                              }
                          }
                        }
                    }
		}
		return false;

        }

        public static boolean isTournamentManager(IWContext iwc) throws SQLException{
		Member member = getMember(iwc);
                if(member!=null){
                    if(member instanceof com.idega.projects.golf.entity.Member){

                        com.idega.projects.golf.entity.Member membi = (com.idega.projects.golf.entity.Member)member;
                    	Group[] access = membi.getGroups(); //  (member).getGenericGroups();
			for(int i = 0; i < access.length; i++){
                          if ("administrator".equals(access[i].getName())){
                                  return true;
                          }

                          if ("tournament_manager".equals(access[i].getName())){
                            return true;
                          }

                        }
                        return false;
                    }
                    else{

			LoginType[] access = member.getLoginType();
                        if (access != null){
                          for(int i = 0; i < access.length; i++){
                              if ("administrator".equals(access[i].getName())){
                                  return true;
                              }
                          }
                        }
                    }
		}
		return false;

        }



}