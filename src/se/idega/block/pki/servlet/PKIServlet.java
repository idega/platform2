/*
 * Created on 26.4.2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package se.idega.block.pki.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.presentation.IWContext;

import se.nexus.nbs.sdk.HttpMessage;
import se.nexus.nbs.sdk.NBSAuthResult;
import se.nexus.nbs.sdk.NBSException;
import se.nexus.nbs.sdk.NBSMessageHttp;
import se.nexus.nbs.sdk.NBSMessageResult;
import se.nexus.nbs.sdk.NBSResult;
import se.nexus.nbs.sdk.NBSServer;
import se.nexus.nbs.sdk.NBSServerFactory;
import se.nexus.nbs.sdk.NBSServerHttp;
import se.nexus.nbs.sdk.servlet.ServletUtil;
/**
 * @author tryggvil
 */
public class PKIServlet extends HttpServlet
{
	/**
	 * 
	 */
	public PKIServlet()
	{
		super();
	}

	/** Names for objects stored in the servlet context or session. */
	protected final static String SERVER_FACTORY = "se.nexus.cbt.ServerFactory",
		SERVER = "se.nexus.cbt.Server",
		SERVLET_URI = "se.nexus.cbt.ServletURI";

	/**
	 * Initializes the Message API and the Browserhandler API.
	 */
	public void init() throws ServletException
	{
		try
		{
			ServletContext context = getServletContext();

			// Get the servlet URL and save in the context.
			String servletUri = getServletURI();
			context.setAttribute(SERVLET_URI, servletUri);

			// Retreive the configration file.
			File configFile = new File(getConfigFilePath());

			// Init the Message API and save in the context.
			// Since are in a servlet environmnet, choose the "Servlet"
			// interface.
			NBSServerFactory serverGenerator = new NBSServerFactory();
			serverGenerator.init(configFile);
			context.setAttribute(SERVER_FACTORY, serverGenerator);
			
			System.out.println("PKIServlet.init() - done");

		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new ServletException("Error in init" + e.getMessage());
		}
	}

	private String getConfigFilePath()
	{
		//TODO Change implementation to use bundle
		return (getInitParameter("CbtConfigFile"));
	}

	private String getServletURI()
	{
		//TODO Change implementation to use bundle
		return (getInitParameter("CbtServletURI"));
	}

	/**
	 * Authentication is initiated
	 * with HTTP GET.
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		System.out.println("PKIServlet.doGet(...) - begins");
		NBSMessageResult result = null;

		// Get the action.
		String action = req.getParameter("action");

		try
		{
			// Get the server object.
			NBSServerHttp server = getServer(req);
			HttpMessage httpReq = new HttpMessage();
			ServletUtil.servletRequestToHttpMessage(req, httpReq);

			if (action != null)
			{
				action.toLowerCase();

				if (action.equals("authenticate"))
				{
					// Initiates authentication.
					result = (NBSMessageResult) server.doAuthenticate(httpReq);
				}

				if (result != null)
				{
					// Send the message to the Client.
					NBSMessageHttp mpMsg = (NBSMessageHttp) result.getMessage();
					sendMessage(res, mpMsg.toHttpMessage());
				}
				else
				{
					// Else just show the main page again.
					printPage(res, null);
				}
			}
			else
			{
				// If no action is specified, show the main page.
				printPage(res, null);
			}

			setServer(req, server);
		}
		catch (NBSException mpse)
		{
			printErrorCode(res, mpse.getCode(), mpse.getMessage());
		}
		catch (Exception e)
		{
			printErrorMessage(res, e.getMessage());
		}
		System.out.println("PKIServlet.doGet(...) - done");
	}

	/**
	 *
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		System.out.println("PKIServlet.doPost(...) - begins");
		NBSResult result = null;

		// Get the action.
		String action = req.getParameter("action");

		try
		{
			
			
			// Get the server object.
			NBSServerHttp server = getServer(req);
			HttpMessage httpReq = new HttpMessage();
			ServletUtil.servletRequestToHttpMessage(req, httpReq);

			if (action != null)
			{
				action.toLowerCase();

				if (action.equals("sign"))
				{
					// Make the tbs a proper java string.
					String tbs = req.getParameter("tbs");

					// Initiate signing.
					result = server.doSign(tbs, httpReq);
				}

				if (result != null)
				{
					// Send the message to the Client.
					NBSMessageHttp mpMsg = (NBSMessageHttp) ((NBSMessageResult) result).getMessage();
					sendMessage(res, mpMsg.toHttpMessage());
				}
				else
				{
					// Else just show the main page again.
					printPage(res, null);
				}
			}
			else
			{
				// No action specified means that a message
				// probably has been received.

				System.out.println("PKI servlet - Message received");


				System.out.println("ib_page"+" = "+req.getParameter("ib_page"));
				System.out.println("ib_error_page"+" = "+req.getParameter("ib_error_page"));


				// Process the message.

				result = server.handleMessage(httpReq);

				// Interpret the result.
				String info = null;
				int type = result.getType();
				switch (type)
				{
					case (NBSResult.TYPE_AUTH) :
						
						System.out.println("NBSResult = TYPE_AUTH");
						info = getCertInfoString((NBSAuthResult) result);
						//info += "<br>"+getXmlInfoString((NBSAuthResult)result);
						info += postAuthentication(req, res, result);
						printPage(res, "NBSAuthentication successful<br>" + info);
						break;
					case (NBSResult.TYPE_SIGN) :
						System.out.println("NBSResult = TYPE_SIGN");
						info = getCertInfoString((NBSAuthResult) result);
						info += "<br>" + getXmlInfoString((NBSAuthResult) result);
						printPage(res, "Signature successful<br>" + info);
						break;
					case (NBSResult.TYPE_MESSAGE) :
						// If a new message was created, send it to
						// the Client.
						System.out.println("NBSResult = TYPE_MESSAGE");
						NBSMessageResult msgResult = (NBSMessageResult) result;
						NBSMessageHttp mpMsg = (NBSMessageHttp) msgResult.getMessage();
//						String errorPageID = req.getParameter("ib_error_page");
//						if(errorPageID != null && !errorPageID.equals("")){
//							res.sendRedirect(req.getRequestURI()+"?ib_page"+errorPageID);
//						}
						//IWMainApplication.getIWMainApplication(this.getServletContext()).getTranslatedURIWithContext())
						sendMessage(res, mpMsg.toHttpMessage());
						break;
					default :
						throw new ServletException("Unknown result");
				}
			}
			setServer(req, server);
		}
		catch (NBSException mpse)
		{
			System.out.println("NBSException");
			System.err.println(mpse.getMessage());
			mpse.printStackTrace();
			printErrorCode(res, mpse.getCode(), mpse.getMessage());
		}
		catch (Exception e)
		{
			e.printStackTrace();	
			System.out.println("Exception");
			printErrorMessage(res, e.getMessage());
		}
		
		System.out.println("PKIServlet.doPost(...) - ends");
		
	}

	/**
	 * Writes a message from a request.
	 */
	void sendMessage(HttpServletResponse res, HttpMessage msg)
	{
		// Set cache directives+
		res.setDateHeader("Expires", 0l);
		res.setHeader("Cache-Control", "private, no-store, no-transform");
		res.setHeader("Pragma", "no-cache");

		// Convert and send
		try
		{
			ServletUtil.httpMessageToServletResponse(msg, res);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * Gets the BidtServer instance.
	 */
	protected NBSServerHttp getServer(HttpServletRequest req) throws NBSException
	{
		// Get the factory from the context.
		ServletContext context = getServletContext();
		NBSServerFactory serverGenerator = (NBSServerFactory) context.getAttribute(SERVER_FACTORY);

		// If created, the server should be in the session.
		HttpSession session = req.getSession(true);
		NBSServerHttp server = (NBSServerHttp) session.getAttribute(SERVER);

		// If not created, create it now.
		if (server == null)
		{
			// Create a server and save it in the session.
			server = (NBSServerHttp) serverGenerator.getInstance("Http");
			session.setAttribute(SERVER, server);
		}
		else
		{
			// In case of a replicated session environment
			serverGenerator.updateInstance(server);
		}

		String servletUri = (String) (getServletContext().getAttribute(SERVLET_URI));
		server.setActionUrl(servletUri);

		return server;
	}

	protected void setServer(HttpServletRequest req, NBSServer server)
	{
		HttpSession session = req.getSession(true);
		session.setAttribute(SERVER, server);
	}

	String getCertInfoString(NBSAuthResult result)
	{
		String info = "<br>";
		Enumeration oids = result.getSubjectOids();
		while (oids.hasMoreElements())
		{
			String oid = (String) oids.nextElement();
			String value = result.getSubjectAttributeValue(oid);
			info = info + oid + ": " + value + "<br>";
		}

		return info;
	}

	String getXmlInfoString(NBSAuthResult result)
	{
		return "Signature produced at Host: "
			+ result.getSignatureHost()
			+ " IP: "
			+ result.getSignatureIP()
			+ " Nonce: ";
//			+ result.getSignatureNonce();
	}

	/**
	 * Prints the main page together with an eventual status.
	 */
	protected void printPage(HttpServletResponse res, String status) throws IOException
	{

		// Add some cache-prevention headers.
		res.addDateHeader("Expires", 0l);
		res.addHeader("Cache-Control", "private, no-store, no-transform");
		res.addHeader("Pragma", "no-cache");

		// Just to be sure.
		res.addHeader("Content-Type", "text/html");

		PrintWriter pw = res.getWriter();

		// Build the page.
		pw.println("<HTML><HEAD><TITLE>Login</TITLE>");
		pw.println("</HEAD>");
		pw.println("<BODY onUnLoad=\"window.parent.reload();\">");

		// Add the status message.
		if (status != null)
		{
			pw.println("<h3>" + status + "</h3>");
		}

		// Authenticate link
		//pw.print("<P><A href=\""+getServletURI()+"?action=authenticate\">");
		//pw.print("authenticate</A></P>");

		// Signing form
		pw.println("<P><FORM action=\"" + getServletURI() + "\" method=\"POST\" " + "enctype=\"multipart/form-data; charset=utf-8\">");
		pw.println("<INPUT type=\"HIDDEN\" name=\"action\" value=\"sign\">");
		pw.print("<INPUT type=\"TEXT\" name=\"tbs\" size=\"20\">");
		pw.println("<INPUT type=\"SUBMIT\" name=\"Sign\" value=\"Sign\">");
		pw.println("</FORM></P>");

		pw.flush();
	}

	/**
	 * Prints an HTML page containing an error code and an error message.
	 */
	protected void printErrorCode(HttpServletResponse res, int code, String message) throws IOException
	{

		// Just to be sure.
		res.addHeader("Content-Type", "text/html");

		PrintWriter pw = res.getWriter();

		// Build the page.
		pw.println("<HTML><HEAD><TITLE>Login</TITLE>");
		pw.println("<STYLE type=\"text/css\">P { width: 300px; }</STYLE>");
		pw.println("</HEAD>");
		pw.println("<BODY>");
		pw.println("<H3>An error occurred</H3>");
		pw.println("<b>Code: <b>" + code + "<br>\n");
		pw.println("<b>Description: <b>" + message + "<br>\n");
		pw.println("<P><A HREF=\"" + getServletURI() + "\">Back</A>");
		pw.println("</BODY></HTML>");
		pw.flush();
	}

	/**
	 * Prints an HTML page containing an message.
	 */
	protected void printErrorMessage(HttpServletResponse res, String msg) throws IOException
	{

		// Just to be sure.
		res.addHeader("Content-Type", "text/html");

		PrintWriter pw = res.getWriter();

		// Build the page.
		pw.println("<HTML><HEAD><TITLE>Login Error</TITLE>");
		pw.println("<STYLE type=\"text/css\">P { width: 300px; }</STYLE>");
		pw.println("</HEAD>");
		pw.println("<BODY>");
		pw.println("<H3>An error occurred</H3>");
		pw.println("<b>Description: <b>" + msg + "<br>\n");
		pw.println("<P><A HREF=\"" + getServletURI() + "\">Back</A>");
		pw.println("</BODY></HTML>");
		pw.flush();
	}

	/**
	 * Method called after a successful authentication to the BankID server to do the idegaWeb login.
	 * @return
	 */
	private String postAuthentication(HttpServletRequest req, HttpServletResponse res, NBSResult result)
	{
		String info = "";
		NBSAuthResult authResult = (NBSAuthResult) result;
		//info = getCertInfoString(authResult);
		//info += "<br>"+getXmlInfoString(authResult);
		//printPage(req, res, "Authentication successful<br>" + info);
		//break;
		String personalIDKey = "serialNumber";
		//String nameKey = "CN";
		//String organizationKey = "O";
		//String countryKey = "C";
		String personalID = authResult.getSubjectAttributeValue(personalIDKey);
		//String commonName = authResult.getSubjectAttributeValue(nameKey);
		//String country = authResult.getSubjectAttributeValue(countryKey);
		//String organization = authResult.getSubjectAttributeValue(organizationKey);
		try
		{

			/*info += "<br>idegaWeb personalID: '"+personalID+"'";
			info += "<br>idegaWeb commonName: '"+commonName+"'";
			info += "<br>idegaWeb country : '"+country+"'";
			info += "<br>idegaWeb organization : '"+organization+"'";
			Enumeration enumeration = authResult.getSubjectOids();
			while (enumeration.hasMoreElements())
			{
				String oid = (String) enumeration.nextElement();
				info += "<br>oid: '"+oid+"'";
			}*/

			LoginBusinessBean lb = new LoginBusinessBean();
			//TODO Change this implementation to a more graceful usage of IWContext or equivalent
			IWContext iwc = new IWContext(req, res,getServletContext());

			lb.logInByPersonalID(iwc, personalID);

			info += "<br>idegaWeb Login successful for personalId : '" + personalID + "'";
		}
		catch (Exception ex)
		{
			info += "<br>idegaWeb Login failed for personalId : '" + personalID + "'";
			ex.printStackTrace();
		}
		return info;
	}
}
