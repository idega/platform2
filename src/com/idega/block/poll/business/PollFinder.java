package com.idega.block.poll.business;

import java.sql.SQLException;
import java.util.List;

import com.idega.block.poll.data.PollEntity;
import com.idega.core.component.business.ICObjectBusiness;
import com.idega.core.component.data.ICObjectInstance;
import com.idega.data.EntityFinder;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class PollFinder {

  public PollFinder() {

  }

  public static PollEntity getPoll(String sAttribute){
    PollEntity poll = null;
    try {
      List L = EntityFinder.findAllByColumn(((com.idega.block.poll.data.PollEntityHome)com.idega.data.IDOLookup.getHomeLegacy(PollEntity.class)).createLegacy(),com.idega.block.poll.data.PollEntityBMPBean.getColumnNameAttribute(),sAttribute);
      if(L!= null) {
		poll =  (PollEntity) L.get(0);
	}
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      poll = null;
    }
    return poll;
  }

  public static int getRelatedEntityId(ICObjectInstance eObjectInstance){
    ICObjectBusiness bis = ICObjectBusiness.getInstance();
    return bis.getRelatedEntityId(eObjectInstance,PollEntity.class);
  }

  public static PollEntity getObjectInstanceFromID(int ICObjectInstanceID){
    try {
      ICObjectBusiness icob = ICObjectBusiness.getInstance();
      ICObjectInstance ICObjInst = icob.getICObjectInstance(ICObjectInstanceID);
      return (PollEntity)icob.getRelatedEntity(ICObjInst,PollEntity.class);
    }
    catch (com.idega.data.IDOFinderException ex) {
      ex.printStackTrace();
      return null;
    }
  }




}
