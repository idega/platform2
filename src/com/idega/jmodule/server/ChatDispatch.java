package com.idega.jmodule.server;

import com.idega.jmodule.server.*;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ChatDispatch extends HttpServlet {

  public void doGet(HttpServletRequest req, HttpServletResponse res)
                               throws IOException, ServletException {
    res.setContentType("text/html");

    if (!req.getParameterNames().hasMoreElements()) {
      // There were no request parameters.  Print a welcome page.
      printWelcomePage(req, res);
    }
    else {
      // There was at least one request parameter.
      // Print a page containing the applet.
      printAppletPage(req, res);
    }
  }

  // The welcome page greets the reader and has a form where the user
  // can choose an applet-servlet communication method.
  private void printWelcomePage(HttpServletRequest req,
                                HttpServletResponse res)
                    throws IOException {
    PrintWriter out = res.getWriter();
    String me = req.getServletPath();

    out.println("<HTML>");
    out.println("<HEAD><TITLE>");
    out.println("Welcome to an Absurdly Simple Chat");
    out.println("</TITLE></HEAD>");
    out.println();
    out.println("<BODY>");
    out.println("<H1>Welcome to an Absurdly Simple Chat</H1>");
    out.println();
    out.println("Would you like to communicate via:");
    out.println("<UL>");
    out.println("  <LI><A HREF=\"" + me + "?method=http\">http</A>");
    out.println("  <LI><A HREF=\"" + me + "?method=socket\">socket</A>");
    out.println("  <LI><A HREF=\"" + me + "?method=rmi\">rmi</A>");
    out.println("</UL>");
    out.println("</BODY></HTML>");
  }

  // The applet page displays the chat applet.
  private void printAppletPage(HttpServletRequest req,
                               HttpServletResponse res)
                    throws IOException {
    PrintWriter out = res.getWriter();

    out.println("<HTML>");
    out.println("<HEAD><TITLE>An Absurdly Simple Chat</TITLE></HEAD>");
    out.println("<BODY>");
    out.println("<H1>An Absurdly Simple Chat</H1>");

    String method = req.getParameter("method");
    String user = req.getRemoteUser();
    String applet = null;

    if ("http".equals(method)) {
      applet = "HttpChatApplet";
    }
    else if ("socket".equals(method)) {
      applet = "SocketChatApplet";
    }
    else if ("rmi".equals(method)) {
      applet = "RMIChatApplet";
    }
    else {
      // No method given, or an invalid method given.
      // Explain to the user what we expect.
      out.println("Sorry, this servlet requires a <TT>method</TT> " +
                  "parameter with one of these values: " +
                  "http, socket, rmi");
      return;
    }

    // Print the HTML code to generate the applet.
    // Choose the applet code based on the method parameter.
    // Provide a user parameter if we know the remote user.
    out.println("<APPLET CODE=\"" + applet + "\" CODEBASE=/ " +  "WIDTH=500 HEIGHT=170>");
    if (user != null)
      out.println("<PARAM NAME=user VALUE=\"" + user + "\">");
    out.println("</APPLET>");

    out.println("</BODY></HTML>");
  }
}
