/*
 * Created on 5.7.2004
 *
 * Copyright (C) 2004 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 */
package is.idega.idegaweb.campus.block.application.data;

import java.util.Collection;
import java.util.Locale;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.util.IWColor;

/**
 * @author aron
 *
 * ApplicationPriorityBMPBean TODO Describe this type
 */
public class PriorityBMPBean extends GenericEntity implements Priority{

	public static final String TABLE_NAME = "CAM_APP_PRIORITY";

	  private static final String PRIORITY = "PRIORITY";
	  private static final String PRIORITY_DESC = "PRIOR_DESC";
	  private static final String PRIORITY_DESC_LOC_KEY = "DESC_LOC_KEY";
	  private static final String PRIORITY_HEXCOLOR = "HEX_COLOR";

	  private static final String LOC_KEY_PREFIX = "application.priority";

	  public void initializeAttributes() {
	    addAttribute(PRIORITY,"Status",String.class,4);
	    this.setAsPrimaryKey(PRIORITY,true);
	    addAttribute(PRIORITY_DESC,"Description",String.class);
	    addAttribute(PRIORITY_DESC_LOC_KEY,"Localized Description Key",String.class);
	    addAttribute(PRIORITY_HEXCOLOR,"Hex color",String.class,7);
	  }
	  public String getEntityName() {
	    return TABLE_NAME;
	  }

	  public String getIDColumnName(){
	    return PRIORITY;
	  }

	  public Class getPrimaryKeyClass(){
	    return String.class;
	  }
	  
	  public String getPriorityCode() {
	    return(this.getStringColumnValue(PRIORITY));
	  }

	  public void setDefaultValues(){
	    String sCode = this.getPriority();
	    if(sCode!=null){
	      this.setDescriptionLocalizedKey(LOC_KEY_PREFIX+sCode);
	    }
	  }

	  public void setPriority(String status) {
	    String sKey = this.getDescriptionLocalizedKey();
	    if(sKey!=null){
	      this.setDescriptionLocalizedKey(LOC_KEY_PREFIX+status);
	    }
	    setColumn(PRIORITY,status);
	  }

	  public String getPriority() {
	    return(this.getStringColumnValue(PRIORITY));
	  }


	  public void setDescription(String desc) {
	    setColumn(PRIORITY_DESC,desc);
	  }

	  public String getDescription() {
	    return(this.getStringColumnValue(PRIORITY_DESC));
	  }

	  public void setDescriptionLocalizedKey(String key) {
	    setColumn(PRIORITY_DESC_LOC_KEY,key);
	  }
	  
	  public String getHexColor(){
	  	return getStringColumnValue(PRIORITY_HEXCOLOR);
	  }
	  
	  public void setHexColor(String color){
	  	setColumn(PRIORITY_HEXCOLOR,IWColor.getIWColorFromHex(color).getHexColorString());
	  }

	  public String getDescriptionLocalizedKey() {
	    return(this.getStringColumnValue(PRIORITY_DESC_LOC_KEY));
	  }

	    /**
	     * @todo: Implement
	     */
	  public void setDescription(String desc,IWApplicationContext iwac,Locale locale) {
	    setDescription(desc);
	  }

	    /**
	     * @todo: Implement
	     */
	  public String getDescription(IWApplicationContext iwac,Locale inLocale) {
	    return getDescription();
	  }


	  public Collection ejbFindAll()throws FinderException{
	    return super.idoFindPKsByQuery(idoQueryGetSelect().appendOrderBy(PRIORITY));
	  }


}
