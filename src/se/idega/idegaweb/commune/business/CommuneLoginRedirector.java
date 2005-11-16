/*
 * $Id: CommuneLoginRedirector.java,v 1.2.2.1 2005/11/16 01:06:40 palli Exp $
 * Created on Nov 3, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.business;

import java.io.IOException;
import java.rmi.RemoteException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.accesscontrol.business.AuthenticationBusiness;
import com.idega.core.accesscontrol.business.AuthenticationListener;
import com.idega.core.accesscontrol.business.ServletFilterChainInterruptException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;


public class CommuneLoginRedirector implements AuthenticationListener, ServletContextListener{
	
	public static final String PARAMETER_REDIRECT_TO_COMMUNE_WEB = "redirectToCommuneWeb";

	public CommuneLoginRedirector(){}
	
	/**
	 * 
	 * @return a human readable identifier for the listener
	 */
	public String getAuthenticationListenerName(){
		return "egovSkjalfandiLogonRedirector";
	}
	
	/**
	 * Called when a user successfully logs on
	 * @param iwc request and response wrapper
	 * @param currentUser
	 * @throws ServletFilterChainInterruptException 
	 */
	public void onLogon(IWContext iwc,User currentUser) throws ServletFilterChainInterruptException{
		//get address and commune code and forward to correct server...
		boolean tryRedirect = iwc.isParameterSet(PARAMETER_REDIRECT_TO_COMMUNE_WEB);
		
		if(tryRedirect){
			String communeURL;
			try {
				communeURL = getCommuneUserBusiness(iwc).getUsersCommuneURL(currentUser);
				if(communeURL!=null){
					try {
						iwc.getResponse().sendRedirect(communeURL);
						throw new ServletFilterChainInterruptException("CommuneLoginRedirector sending the user to his home commune website: "+communeURL);
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			catch (RemoteException e1) {
				e1.printStackTrace();
			}
			
		}
	}
	
	/**
	 * Called when a user successfully logs off
	 * @param iwc request and response wrapper
	 * @param lastUser
	 */
	public void onLogoff(IWContext iwc, User lastUser){
		//do nothing
	}
	
	
	
	/**
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent event) {
		IWApplicationContext iwac = IWMainApplication.getDefaultIWApplicationContext();
		try {
			AuthenticationBusiness biz = (AuthenticationBusiness)IBOLookup.getServiceInstance(iwac,AuthenticationBusiness.class);
			biz.addAuthenticationListener(this);
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		System.out.println("[eGovSkjalfandi] LogonRedirector(AuthenticationListener) initialized.");
	}
	
	/**
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
	}
	
	
	public CommuneUserBusiness getCommuneUserBusiness(IWContext iwc){
		CommuneUserBusiness biz = null;
		try {
			biz = (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		return biz;
	}
	
}