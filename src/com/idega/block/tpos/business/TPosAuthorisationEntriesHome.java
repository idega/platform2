/*

 *  $Id: TPosAuthorisationEntriesHome.java,v 1.2 2002/04/06 19:07:37 tryggvil Exp $

 *

 *  Copyright (C) 2002 Idega hf. All Rights Reserved.

 *

 *  This software is the proprietary information of Idega hf.

 *  Use is subject to license terms.

 *

 */

package com.idega.block.tpos.business;



import com.idega.block.tpos.data.TPosAuthorisationEntries;

import com.idega.block.tpos.data.TPosAuthorisationEntriesBean;



/**

 * @author    <a href="mail:palli@idega.is">Pall Helgason</a>

 * @version   1.0

 */

public class TPosAuthorisationEntriesHome {

  private static TPosAuthorisationEntriesHome _instance = null;



  /**

   * Constructor for the TPosAuthorisationEntriesHome object

   */

  private TPosAuthorisationEntriesHome() { }



  /**

   * Gets the instance attribute of the TPosAuthorisationEntriesHome class

   *

   * @return   The instance value

   */

  public static TPosAuthorisationEntriesHome getInstance() {

    if (_instance == null) {

      _instance = new TPosAuthorisationEntriesHome();

    }



    return _instance;

  }



  /**

   * Gets the newElement attribute of the TPosAuthorisationEntriesHome object

   *

   * @return   The newElement value

   */

  public TPosAuthorisationEntries getNewElement() {

    TPosAuthorisationEntriesBean bean = ((com.idega.block.tpos.data.TPosAuthorisationEntriesBeanHome)com.idega.data.IDOLookup.getHomeLegacy(TPosAuthorisationEntriesBean.class)).createLegacy();



    return bean;

  }



  /**

   * Description of the Method

   *

   * @param entry  Description of the Parameter

   * @return       Description of the Return Value

   */

  public boolean insert(TPosAuthorisationEntries entry) {

    if (entry instanceof TPosAuthorisationEntriesBean) {

      TPosAuthorisationEntriesBean bean = (TPosAuthorisationEntriesBean)entry;

      try {

        bean.insert();

      }

      catch (Exception e) {

        return false;

      }



      return true;

    }



    return false;

  }



  /**

   * Description of the Method

   *

   * @param entry  Description of the Parameter

   * @return       Description of the Return Value

   */

  public boolean update(TPosAuthorisationEntries entry) {

    return true;

  }



  /**

   * Description of the Method

   *

   * @param entry  Description of the Parameter

   * @return       Description of the Return Value

   */

  public boolean delete(TPosAuthorisationEntries entry) {

    return true;

  }

}

