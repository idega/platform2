/*
 * Created on 31.7.2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package is.idega.idegaweb.golf.block.login.business;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;
import com.idega.servlet.filter.BaseFilter;

/**
 * 
 * This class is responsible for authenticating users into the old golf login
 * system
 * 
 * @author eiki
 */
public class GolfAuthenticatorFilter extends BaseFilter{

	private GolfLoginBusiness loginBusiness = new GolfLoginBusiness();

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig arg0) throws ServletException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest srequest, ServletResponse sresponse, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) srequest;
		HttpServletResponse response = (HttpServletResponse) sresponse;
		IWContext iwc = new IWContext(request, response, request.getSession().getServletContext());		
		
		doLoginORLogoff(iwc);

		chain.doFilter(request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

	public void doLoginORLogoff(IWContext iwc) {
		try {
			loginBusiness.actionPerformed(iwc);
		}
		catch (IWException e) {
			e.printStackTrace();
		}
	}
}