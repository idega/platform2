/*

 * $Id: AdminTemplate.java,v 1.4 2004/05/24 14:21:40 palli Exp $

 *

 * Copyright (C) 2001 Idega hf. All Rights Reserved.

 *

 * This software is the proprietary information of Idega hf.

 * Use is subject to license terms.

 *

 */

package is.idega.idegaweb.campus.templates;







import javax.servlet.jsp.JspPage;

import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.jsp.JSPModule;
import com.idega.presentation.text.Text;



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

  /** Adds a PresentationObject to the middle section

   *

   */

  public void add(PresentationObject objectToAdd){

    getMainPage().add(objectToAdd);

  }



  public void add(String stringToAdd){

    getMainPage().add(new Text(stringToAdd));

  }

  /** Adds a PresentationObject to the center of the left side

   *

   */

  public void addLeft(PresentationObject objectToAdd){

    getMainPage().addLeft(objectToAdd);

  }



  /** Adds a PresentationObject to the titlebar on the left side

   *

   */

  public void addMenuTitle(PresentationObject objectToAdd){

    getMainPage().addMenuTitle(objectToAdd);

  }

  /** Adds a PresentationObject to the titlebar in the middle

   *

   */

  public void addMainTitle(PresentationObject objectToAdd){

    getMainPage().addMainTitle(objectToAdd);

  }

  /** Adds a PresentationObject to the titlebar on the right side

   *

   */

  public void addRightTitle(PresentationObject objectToAdd){

    getMainPage().addRightTitle(objectToAdd);

  }

  /** Adds a PresentationObject to the top of left side

   *

   */

  public void addTopLeft(PresentationObject objectToAdd){

    getMainPage().addTopLeft(objectToAdd);

  }



  /** Adds a PresentationObject to the upper logo area on the left

   *

   */

  public void addLogo(PresentationObject objectToAdd){

    getMainPage().addLogo( objectToAdd);

  }

  /** Adds a PresentationObject to the lower logo area on the left

   *

   */

  public void addLowerLogo(PresentationObject objectToAdd){

    getMainPage().addLowerLogo( objectToAdd);

  }

  /** Adds a PresentationObject into tab area

   *

   */

  public void addTabs(PresentationObject objectToAdd){

    getMainPage().addTabs( objectToAdd);



  }



  public void setBorder(int iBorder){

    getMainPage().setBorder(iBorder);

  }

}

