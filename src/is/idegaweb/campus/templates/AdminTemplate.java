/*
 * $Id: AdminTemplate.java,v 1.2 2001/08/29 21:18:24 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.templates;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.data.*;
import is.idegaweb.campus.templates.MainPage;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public abstract class AdminTemplate extends JSPModule implements JspPage{

   public void initializePage(){
    setPage(new AdminPage());
  }
  public AdminPage getMainPage(){
    return (AdminPage)this.getPage();
  }

  public Table getDivider() {
    return getMainPage().getDivider();
  }

  public void setFaceLogo(Image FaceLogo){
    getMainPage().setFaceLogo(FaceLogo);
  }
  /**
   *
   */
  public void setLeftAlignment(String align){
    getMainPage().setLeftAlignment(align);
  }
  /**
   *
   */
  public void setCenterAlignment(String align){
    getMainPage().setCenterAlignment(align);
  }
  /** Adds a ModuleObject to the middle section
   *
   */
  public void add(ModuleObject objectToAdd){
    getMainPage().add(objectToAdd);
  }

  public void add(String stringToAdd){
    getMainPage().add(new Text(stringToAdd));
  }
  /** Adds a ModuleObject to the center of the left side
   *
   */
  public void addLeft(ModuleObject objectToAdd){
    getMainPage().addLeft(objectToAdd);
  }

  /** Adds a ModuleObject to the titlebar on the left side
   *
   */
  public void addMenuTitle(ModuleObject objectToAdd){
    getMainPage().addMenuTitle(objectToAdd);
  }
  /** Adds a ModuleObject to the titlebar in the middle
   *
   */
  public void addMainTitle(ModuleObject objectToAdd){
    getMainPage().addMainTitle(objectToAdd);
  }
  /** Adds a ModuleObject to the titlebar on the right side
   *
   */
  public void addRightTitle(ModuleObject objectToAdd){
    getMainPage().addRightTitle(objectToAdd);
  }
  /** Adds a ModuleObject to the top of left side
   *
   */
  public void addTopLeft(ModuleObject objectToAdd){
    getMainPage().addTopLeft(objectToAdd);
  }

  /** Adds a ModuleObject to the upper logo area on the left
   *
   */
  public void addLogo(ModuleObject objectToAdd){
    getMainPage().addLogo( objectToAdd);
  }
  /** Adds a ModuleObject to the lower logo area on the left
   *
   */
  public void addLowerLogo(ModuleObject objectToAdd){
    getMainPage().addLowerLogo( objectToAdd);
  }
  /** Adds a ModuleObject into tab area
   *
   */
  public void addTabs(ModuleObject objectToAdd){
    getMainPage().addTabs( objectToAdd);

  }

  public void setBorder(int iBorder){
    getMainPage().setBorder(iBorder);
  }
}