/*
 * $Id: CampusTariffer.java,v 1.3 2001/07/23 10:00:00 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.tariffs;

import com.idega.block.finance.data.*;
import com.idega.block.building.data.*;
import com.idega.block.finance.business.Finder;
import com.idega.block.building.business.BuildingFinder;
import com.idega.block.finance.presentation.KeyEditor;
import com.idega.data.GenericEntity;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.textObject.*;
import com.idega.util.idegaTimestamp;
import com.idega.util.idegaCalendar;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusTariffer extends KeyEditor {

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4;
  protected final int ACT5 = 5,ACT6 = 6, ACT7 = 7,ACT8  = 8;
  public  String strAction = "te_action";
  public final   int YEAR=1,MONTH=2,WEEK=3,DAY=4;
  private idegaTimestamp workingPeriod;
  private int period = MONTH;
  private GenericEntity[] entities = null;
  private int iPeriod;

  public CampusTariffer(String sHeader) {
    super(sHeader);
  }

  public void setEntities(GenericEntity[] entities){
    this.entities = entities;
  }

  protected void control(ModuleInfo modinfo){

  }


}
