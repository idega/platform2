// Copyright (C) 1998 by Jason Hunter <jhunter@acm.org>.  All rights reserved.
// Use of this class is limited.  Please see the LICENSE for more information.

package com.idega.jmodule.server;

import com.idega.jmodule.server.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * A class to simplify HTTP applet-server communication.  It abstracts
 * the communication into messages, which can be either GET or POST.
 * <p>
 * It can be used like this:
 * <blockquote><pre>
 * URL url = new URL(getCodeBase(), "/servlet/ServletName");
 * &nbsp;
 * HttpMessage msg = new HttpMessage(url);
 * &nbsp;
 * Properties props = new Properties();
 * props.put("name", "value");
 * &nbsp;
 * InputStream in = msg.sendGetMessage(props);
 * </pre></blockquote>
 * This class is loosely modeled after the ServletMessage class written
 * by Rod McChesney of JavaSoft.
 *
 * @author <b>Jason Hunter</b>, Copyright &#169; 1998
 * @version 1.0, 98/09/18
 */
public class HttpMessage {

  URL servlet = null;
  String args = null;

  /**
   * Constructs a new HttpMessage that can be used to communicate with the
   * servlet at the specified URL.
   *
   * @param servlet the server resource (typically a servlet) with which
   * to communicate
   */
  public HttpMessage(URL servlet) {
    this.servlet = servlet;
  }

  /**
   * Performs a GET request to the servlet, with no query string.
   *
   * @return an InputStream to read the response
   * @exception IOException if an I/O error occurs
   */
  public InputStream sendGetMessage() throws IOException {
    return sendGetMessage(null);
  }

  /**
   * Performs a GET request to the servlet, building
   * a query string from the supplied properties list.
   *
   * @param args the properties list from which to build a query string
   * @return an InputStream to read the response
   * @exception IOException if an I/O error occurs
   */
  public InputStream sendGetMessage(Properties args) throws IOException {
    String argString = "";  // default

    if (args != null) {
      argString = "?" + toEncodedString(args);
    }
    URL url = new URL(servlet.toExternalForm() + argString);

    // Turn off caching
    URLConnection con = url.openConnection();
    con.setUseCaches(false);

    return con.getInputStream();
  }

  /**
   * Performs a POST request to the servlet, with no query string.
   *
   * @return an InputStream to read the response
   * @exception IOException if an I/O error occurs
   */
  public InputStream sendPostMessage() throws IOException {
    return sendPostMessage(null);
  }

  /**
   * Performs a POST request to the servlet, building
   * post data from the supplied properties list.
   *
   * @param args the properties list from which to build the post data
   * @return an InputStream to read the response
   * @exception IOException if an I/O error occurs
   */
  public InputStream sendPostMessage(Properties args) throws IOException {
    String argString = "";  // default
    if (args != null) {
      argString = toEncodedString(args);  // notice no "?"
    }

    URLConnection con = servlet.openConnection();

    // Prepare for both input and output
    con.setDoInput(true);
    con.setDoOutput(true);

    // Turn off caching
    con.setUseCaches(false);

    // Work around a Netscape bug
    con.setRequestProperty("Content-Type",
                           "application/x-www-form-urlencoded");

    // Write the arguments as post data
    DataOutputStream out = new DataOutputStream(con.getOutputStream());
    out.writeBytes(argString);
    out.flush();
    out.close();

    return con.getInputStream();
  }

  /**
   * Performs a POST request to the servlet, uploading a serialized object.
   * <p>
   * The servlet can receive the object in its <tt>doPost()</tt> method
   * like this:
   * <pre>
   *     ObjectInputStream objin =
   *       new ObjectInputStream(req.getInputStream());
   *     Object obj = objin.readObject();
   * </pre>
   * The type of the uploaded object can be retrieved as the subtype of the
   * content type (<tt>java-internal/<i>classname</i></tt>).
   *
   * @param obj the serializable object to upload
   * @return an InputStream to read the response
   * @exception IOException if an I/O error occurs
   */
  public InputStream sendPostMessage(Serializable obj) throws IOException {
    URLConnection con = servlet.openConnection();

    // Prepare for both input and output
    con.setDoInput(true);
    con.setDoOutput(true);

    // Turn off caching
    con.setUseCaches(false);

    // Set the content type to be java-internal/classname
    con.setRequestProperty("Content-Type",
                           "java-internal/" + obj.getClass().getName());

    // Write the serialized object as post data
    ObjectOutputStream out = new ObjectOutputStream(con.getOutputStream());
    out.writeObject(obj);
    out.flush();
    out.close();

    return con.getInputStream();
  }

  /*
   * Converts a properties list to a URL-encoded query string
   */
  private String toEncodedString(Properties args) {
    StringBuffer buf = new StringBuffer();
    Enumeration names = args.propertyNames();
    while (names.hasMoreElements()) {
      String name = (String) names.nextElement();
      String value = args.getProperty(name);
      buf.append(URLEncoder.encode(name) + "=" + URLEncoder.encode(value));
      if (names.hasMoreElements()) buf.append("&");
    }
    return buf.toString();
  }
}
