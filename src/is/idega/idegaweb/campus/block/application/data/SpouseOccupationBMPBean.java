/*

 * $Id: SpouseOccupationBMPBean.java,v 1.2 2004/05/24 14:21:41 palli Exp $

 *

 * Copyright (C) 2001 Idega hf. All Rights Reserved.

 *

 * This software is the proprietary information of Idega hf.

 * Use is subject to license terms.

 *

 */

package is.idega.idegaweb.campus.block.application.data;





import java.sql.SQLException;



/**

 *

 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>

 * @version 1.0

 */

public class SpouseOccupationBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.campus.block.application.data.SpouseOccupation {

  private static final String name_ = "cam_spouse_occ";

  private static final String description_ = "description";



  public SpouseOccupationBMPBean() {

    super();

  }



  public SpouseOccupationBMPBean(int id) throws SQLException {

    super(id);

  }



  public void initializeAttributes() {

    addAttribute(getIDColumnName());

    addAttribute(description_,"Description",true,true,"java.lang.String");

    super.setMaxLength(description_,255);

  }



  public String getEntityName() {

    return(name_);

  }



  public String getDescriptionColumnName() {

    return(description_);

  }



  public String getName() {

    return(getDescription());

  }



  public String getDescription() {

    return((String)getColumnValue(description_));

  }



  public void setDescription(String description) {

    setColumn(description_,description);

  }

}

