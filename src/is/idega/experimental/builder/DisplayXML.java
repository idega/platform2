/*
 * $Id: DisplayXML.java,v 1.2 2001/05/16 19:00:33 palli Exp $
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
  private Hashtable regions = null;

  public DisplayXML() {
  }

  public void __theService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String xmlFile = request.getParameter("xmlFile");
    if (startup == null)
      startup = ResourceBundle.getBundle("com.idega.builder.BuilderResource",Locale.getDefault());

    if (xmlFile == null)
      xmlFile = startup.getString("startPage");
    boolean verify = Boolean.getBoolean(startup.getString("verifyPage"));

    regions = new Hashtable();
    this.getPage().empty();

    parseXML(xmlFile,verify,getPage());
  }

  public void parseXML(String xmlFile, boolean verifyPage, ModuleObjectContainer parent) {
    try {
      XMLPageDescriptor xmlDesc = new XMLPageDescriptor(verifyPage);
      xmlDesc.setXMLPageDescriptionFile(xmlFile);
      Element root = xmlDesc.getRootElement();
      List rootAttr = root.getAttributes();
      Iterator attr = rootAttr.iterator();
      boolean hasTemplate = false;

      while (attr.hasNext()) {
        Attribute at = (Attribute)attr.next();
        if (at.getName().equalsIgnoreCase("template")) {
          hasTemplate = true;
          parseXML(at.getValue(),verifyPage,parent);
        }
        else if (at.getName().equalsIgnoreCase("id")) {
          System.out.println("Parsing document : " + at.getValue());
        }
        else
          System.err.println("Undefined attribute : " + at.getName());
      }

      if (root.hasChildren()) {
        List children = root.getChildren();
        Iterator it = children.iterator();

        while (it.hasNext()) {
          Element child = (Element)it.next();
          if (child.getName().equalsIgnoreCase("property")) {
            setProperties(child,parent);
          }
          else if (child.getName().equalsIgnoreCase("element") || child.getName().equalsIgnoreCase("module")) {
            if (hasTemplate)
              System.err.println("Using element or module on top level in a page having a template");
            else
              parseElement(child,parent,null);
          }
          else if (child.getName().equalsIgnoreCase("region")) {
            parseRegion(child,parent);
          }
          else {
            System.err.println("Unknown tag in xml description file : " + child.getName());
          }
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

  public void parseRegion(Element reg, ModuleObjectContainer regionParent) {
    List regionAttrList = reg.getAttributes();
    if ((regionAttrList == null) || (regionAttrList.isEmpty())) {
      System.err.println("Table region has no attributes");
      return;
    }

    Iterator regionAttrIt = regionAttrList.iterator();
    String regionId = null;
    int x = 0;
    int y = 0;

    while (regionAttrIt.hasNext()) {
      Attribute regionAttr = (Attribute)regionAttrIt.next();
      if (regionAttr.getName().equalsIgnoreCase("id")) {
        regionId = regionAttr.getValue();
      }
      else if (regionAttr.getName().equalsIgnoreCase("x")) {
        try {
          x = regionAttr.getIntValue();
        }
        catch(org.jdom.DataConversionException e) {
          System.err.println("Unable to convert x region attribute to integer");
          x = 0;
        }
      }
      else if (regionAttr.getName().equalsIgnoreCase("y")) {
        try {
          y = regionAttr.getIntValue();
        }
        catch(org.jdom.DataConversionException e) {
          System.err.println("Unable to convert y region attribute to integer");
          y = 0;
        }
      }
      else
        System.err.println("Unspecified table region attribute: " + regionAttr.getName());
    }

    if ((regionId == null) || (regionId.equals(""))) {
      System.err.println("Missing id attribute for region tag");
      return;
    }

    if (regions.containsKey(regionId)) {
      regionParent = (ModuleObjectContainer)regions.get(regionId);
    }
    else {
      Region region = new Region(regionId,x,y,true);
      regions.put(regionId,regionParent);
      if (regionParent instanceof com.idega.jmodule.object.Table) {
        com.idega.jmodule.object.Table tab = (com.idega.jmodule.object.Table)regionParent;
        tab.add(region,x,y);
      }
      else
        regionParent.add(region);
    }

    if (reg.hasChildren()) {
      List children = reg.getChildren();
      Iterator childrenIt = children.iterator();

      while (childrenIt.hasNext())
        parseElement((Element)childrenIt.next(),regionParent,regionId);
    }
  }

  public void parseElement(Element el, ModuleObjectContainer parent, String region) {
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
                  parseElement(child,table,null);
                }
                else if (child.getName().equalsIgnoreCase("region")) {
                  parseRegion(child,table);
                }
                else
                  System.err.println("Unknown tag in xml description file : " + child.getName());
              }
            }
            if (region != null) {
              Region reg = new Region(region);

              if (parent instanceof com.idega.jmodule.object.Table) {
                com.idega.jmodule.object.Table tableParent = (com.idega.jmodule.object.Table)parent;
                int ind[] = tableParent.getTableIndex(reg);
                if (ind != null) {
                  tableParent.add(table,ind[0],ind[1]);
                }
              }
              else {
                int index = parent.getIndex(reg);
                parent.insertAt(table,index);
              }
            }
            else
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
                  parseElement(child,(ModuleObjectContainer)inst,region);
                }
                else if (child.getName().equalsIgnoreCase("region")) {
                  parseRegion(child,(ModuleObjectContainer)inst);
                }
                else {
                  System.err.println("Unknown tag in xml description file : " + child.getName());
                }
              }
            }
            if (region != null) {
              Region reg = new Region(region);

              if (parent instanceof com.idega.jmodule.object.Table) {
                com.idega.jmodule.object.Table tableParent = (com.idega.jmodule.object.Table)parent;
                int ind[] = tableParent.getTableIndex(reg);
                if (ind != null) {
                  tableParent.add(inst,ind[0],ind[1]);
                }
              }
              else {
                int index = parent.getIndex(reg);
                parent.insertAt(inst,index);
              }
            }
            else
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
    }
  }
}