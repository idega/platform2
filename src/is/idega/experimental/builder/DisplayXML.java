/*
 * $Id: DisplayXML.java,v 1.1 2001/05/14 14:26:51 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.experimental.builder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import com.idega.servlet.IWPresentationServlet;
import com.idega.util.XMLPageDescriptor;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.ModuleObjectContainer;

import java.util.ResourceBundle;
import java.util.Locale;
import java.util.List;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;

import java.io.IOException;

import java.lang.reflect.*;

import org.jdom.*;

/**
 * A simple class to display the modules as described in XML.
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0alpha
 */
public class DisplayXML extends IWPresentationServlet {

  private static ResourceBundle startup = null;

  public DisplayXML() {
  }

  public void __theService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if (startup == null)
      startup = ResourceBundle.getBundle("com.idega.builder.BuilderResource",Locale.getDefault());

    String xmlFile = startup.getString("startPage");
    boolean verify = Boolean.getBoolean(startup.getString("verifyPage"));
    XMLPageDescriptor xmlDesc = new XMLPageDescriptor(verify);

    try {
      xmlDesc.setXMLPageDescriptionFile(xmlFile);
      Element root = xmlDesc.getRootElement();
      List rootAttr = root.getAttributes();
      /**
       * @todo Hérna vantar eitthvað út af template dóti í attribute'um.
       */

      if (root.hasChildren()) {
        List children = root.getChildren();
        Iterator it = children.iterator();

        while (it.hasNext()) {
          Element child = (Element)it.next();
          if (child.getName().equalsIgnoreCase("property")) {
            setProperties(child,getPage());
          }
          else if (child.getName().equalsIgnoreCase("element") || child.getName().equalsIgnoreCase("module")) {
            parseElement(child,getPage());
          }
          else
            System.err.println("Unknow tag in xml description file : " + child.getName());
        }
      }
    }
    catch(com.idega.exception.PageDescriptionDoesNotExists e) {
      System.err.println("The xml description file could not be found : " + xmlFile);
    }
  }

  public void setProperties(Element properties, ModuleObject object) {
    String key = null;
    Vector values = new Vector(1);
    String vals[] = null;

    List li = properties.getChildren();
    Iterator it = li.iterator();

    while (it.hasNext()) {
      Element e = (Element)it.next();

      if (e.getName().equalsIgnoreCase("name")) {
        if (key != null) {
          vals = new String[values.size()];
          for (int i = 0; i < values.size(); i++)
            vals[i] = (String)values.elementAt(i);
          object.setProperty(key,vals);
          values.clear();
        }
        key = e.getTextTrim();
      }
      else if (e.getName().equalsIgnoreCase("value")) {
        values.addElement(e.getTextTrim());
      }
      else
        System.err.println("Error!!!!");
    }

    if (key != null) {
      vals = new String[values.size()];
      for (int i = 0; i < values.size(); i++)
        vals[i] = (String)values.elementAt(i);
      object.setProperty(key,vals);
    }
  }

  public void parseElement(Element el, ModuleObjectContainer parent) {
    ModuleObject inst = null;
    List at = el.getAttributes();

    if ((at == null) || (at.isEmpty())) {
      System.err.println("No attributes specified");
      return;
    }

    Iterator it = at.iterator();
    while (it.hasNext()) {
      Attribute attr = (Attribute)it.next();
      if (attr.getName().equalsIgnoreCase("class")) {
        try {
          inst = (ModuleObject)Class.forName(attr.getValue()).newInstance();

          if (inst instanceof com.idega.jmodule.object.Table) {
            com.idega.jmodule.object.Table table = (com.idega.jmodule.object.Table)inst;

            if (el.hasChildren()) {
              List children = el.getChildren();
              Iterator itr = children.iterator();

              while (itr.hasNext()) {
                Element child = (Element)itr.next();
                if (child.getName().equalsIgnoreCase("property")) {
                  setProperties(child,table);
                }
                else if (child.getName().equalsIgnoreCase("element") || child.getName().equalsIgnoreCase("module")) {
                  parseElement(child,table);
                }
                if (child.getName().equalsIgnoreCase("region")) {
                  /**
                   * @todo Eitthvað dót út af region
                   */
                }
                else
                  System.err.println("Unknow tag in xml description file : " + child.getName());
              }
            }
            parent.add(table);
          }
          else {
            if (el.hasChildren()) {
              List children = el.getChildren();
              Iterator itr = children.iterator();

              while (itr.hasNext()) {
                Element child = (Element)itr.next();
                if (child.getName().equalsIgnoreCase("property")) {
                  setProperties(child,inst);
                }
                else if (child.getName().equalsIgnoreCase("element") || child.getName().equalsIgnoreCase("module")) {
                  parseElement(child,(ModuleObjectContainer)inst);
                }
                else
                  System.err.println("Unknow tag in xml description file : " + child.getName());
              }
            }
            parent.add(inst);
          }
        }
        catch(ClassNotFoundException e) {
          System.err.println("The specified class can not be found: " + attr.getValue());
          e.printStackTrace();
        }
        catch(java.lang.IllegalAccessException e2) {
          System.err.println("Illegal access");
          e2.printStackTrace();
        }
        catch(java.lang.InstantiationException e3) {
          System.err.println("Unable to instanciate class: " + attr.getValue());
          e3.printStackTrace();
        }
      }
      else if (attr.getName().equalsIgnoreCase("id")) {
        //Eitthvað db dót
      }
      else if (attr.getName().equalsIgnoreCase("name")) {
        //Hver veit?
      }
    }
  }

  public void junk() {

    try {
      ModuleObject t = (ModuleObject)Class.forName("com.idega.jmodule.object.Table").newInstance();

      if (t instanceof com.idega.jmodule.object.Table) {
        com.idega.jmodule.object.Table tab = (com.idega.jmodule.object.Table)t;

        tab.setColumns(3);
        tab.setRows(3);
        tab.setBorder(1);
        tab.add("test2",2,2);

        ModuleObjectContainer page = this.getPage();
        page.add(tab);
      }
      else {

        add(t);
      }
    }
    catch(ClassNotFoundException e) {
      System.err.println("e1");
    }
    catch(java.lang.IllegalAccessException e4) {
      System.err.println("e4");
    }
    catch(java.lang.InstantiationException e5) {
      System.err.println("e5");
    }
  }



  public void test() {
    if (startup == null)
      startup = ResourceBundle.getBundle("com.idega.builder.BuilderResource");

    String xmlFile = startup.getString("startPage");
    System.out.println("StartPage = " + xmlFile);
  }

  public static void main(String args[]) {
    DisplayXML xml = new DisplayXML();
    xml.test();
  }
}