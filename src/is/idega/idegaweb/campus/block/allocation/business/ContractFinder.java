package is.idega.idegaweb.campus.block.allocation.business;


import is.idega.idegaweb.campus.data.ApartmentContracts;
import is.idega.idegaweb.campus.block.allocation.data.*;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import com.idega.data.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Map;
import java.util.Iterator;
import com.idega.core.user.data.User;
import com.idega.block.application.data.Applicant;
//import com.idega.block.application.data.ApplicantBean;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.business.BuildingCacher;
import is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriods;
import java.sql.*;
import com.idega.util.database.ConnectionBroker;


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

}
