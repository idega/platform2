package is.idega.idegaweb.campus.block.allocation.business;




/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public abstract class ContractFinder {
	
   public final  static int NAME = 0,SSN=1,APARTMENT = 2,FLOOR=3,BUILDING=4,
      COMPLEX=5,CATEGORY=6,TYPE=7,CONTRACT = 8,APPLICANT = 9;
   
   public static String getOrderString(int type){
    String order = null;
    switch (type) {
      case NAME :  order = " p.first_name,p.middle_name,p.last_name "; break;
      case SSN:  order = " p.ssn "; break;
      case BUILDING : order = " b.name " ; break;
      case COMPLEX: order =  " c.name " ; break;
      case FLOOR:  order =  " f.name " ; break;
      case APARTMENT: order =" a.name " ; break;
      case CATEGORY:  order =  " y.name " ; break;
      case TYPE: order =" t.name " ; break;
      default: order = " con.bu_apartment_id ";

    }
    return order;
  }

}
