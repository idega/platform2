/*
 * $Id: GolfRegister.java,v 1.1 2005/04/17 17:47:39 eiki Exp $
 * Created on Apr 17, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.golf.servlet.filter;

import is.idega.idegaweb.golf.business.UserSynchronizationBusiness;
import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.presentation.IWContext;

/**
 * 
 * This filter grabs requests to change the club status of a member
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 */
public class GolfRegister implements Filter {

	private static Logger log = Logger.getLogger(GolfRegister.class.getName());
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest myRequest, ServletResponse myResponse,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest)myRequest;
		HttpServletResponse response = (HttpServletResponse)myResponse;
		
		try {
			IWContext iwc = new IWContext(request,response, request.getSession().getServletContext());
			UserSynchronizationBusiness sync = (UserSynchronizationBusiness) IBOLookup.getServiceInstance(iwc, UserSynchronizationBusiness.class);
			sync.syncUserFromRequest(iwc);
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		
		chain.doFilter(request, response);
	}
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig arg0) throws ServletException {	
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}
}