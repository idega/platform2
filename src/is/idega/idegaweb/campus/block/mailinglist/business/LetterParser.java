package is.idega.idegaweb.campus.block.mailinglist.business;

import is.idega.idegaweb.campus.block.allocation.data.Contract;
import com.idega.util.idegaTimestamp;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class LetterParser implements ContentParsable {

  public final static String tenant_name = "TENANT_NAME";
  public final static String tenant_address = "TENANT_ADDRESS";
  public final static String tenant_id = "TENANT_ID";
  public final static String contract_starts = "START_DATE";
  public final static String contract_ends = "END_DATE";
  public final static String con_deliver = "KEY_DELIVER_DATE";
  public final static String con_return = "KEY_RETURN_DATE";
  public final static String today = "TODAY";

  public final static String aprt_name = "APRT_NAME";
  public final static String aprt_desc = "APRT_DESCR";
  public final static String floor_name = "FLOOR_NAME";
  public final static String bldg_name = "BUILDING_NAME";
  public final static String bldg_desc = "BUILDING_DESCR";
  public final static String camp_name = "CAMPUS_NAME";
  public final static String camp_info = "CAMPUS_DESCR";
  public final static String type_name = "TYPE_NAME";
  public final static String type_desc = "TYPE_DESCR";
  public final static String type_area = "TYPE_AREA";
  public final static String cat_name = "CATEGORY_NAME";
  public final static String cat_desc = "CATEGORY_DESC";

  public static String APPROVAL = "APPROVAL";
  public static String REJECTION = "REJECTION";
  public static String ALLOCATION = "ALLOCATION";
  public static String SIGNATURE = "SIGNATURE";
  public static String RESIGN = "RESIGN";
  public static String TERMINATION = "TERMINATION";
  public static String RETURN = "RETURN";
  public static String DELIVER = "DELIVER";


  public  static String[] TAGS = { tenant_name,tenant_address,tenant_id,
                            contract_starts,contract_ends,today,aprt_name,aprt_desc,
                            floor_name,bldg_name,bldg_desc,camp_name,camp_info,
                            type_name,type_desc,type_area,cat_name,cat_desc};

  public  static String[] types = { APPROVAL,REJECTION,ALLOCATION,
                            SIGNATURE,RETURN,DELIVER,RESIGN,TERMINATION};

  private EntityHolder holder ;


  public LetterParser() {

  }

  public LetterParser(EntityHolder holder) {
    this.holder = holder;
  }

  public void setEntityHolder(EntityHolder holder){
    this.holder = holder;
  }

  public String[] getParseTags(){
    return TAGS;
  }

  public String getParsedString(String tag){
    try{
      if(tag.equals(tenant_name)){ return holder.getUser().getName();}
      else if(tag.equals(tenant_address)){ return holder.getApplicant().getResidence();}
      else if(tag.equals(tenant_id)){ return holder.getApplicant().getSSN();}
      else if(tag.equals(contract_starts)){ return holder.getContract().getValidFrom().toString();}
      else if(tag.equals(contract_ends)){return holder.getContract().getValidTo().toString();}
      else if(tag.equals(today)){return idegaTimestamp.RightNow().getISLDate();}
      else if(tag.equals( aprt_name )){ return holder.getApartmentHolder().getApartment().getName();}
      else if(tag.equals( aprt_desc )){return holder.getApartmentHolder().getApartment().getInfo();}
      else if(tag.equals( floor_name )){return holder.getApartmentHolder().getFloor().getName();}
      else if(tag.equals( bldg_name )){return holder.getApartmentHolder().getBuilding().getName();}
      else if(tag.equals( bldg_desc )){return holder.getApartmentHolder().getBuilding().getInfo();}
      else if(tag.equals( camp_name )){return holder.getApartmentHolder().getComplex().getName();}
      else if(tag.equals( camp_info )){return holder.getApartmentHolder().getComplex().getInfo();}
      else if(tag.equals( type_name )){return holder.getApartmentHolder().getApartmentType().getInfo();}
      else if(tag.equals( type_desc )){return holder.getApartmentHolder().getApartmentType().getInfo();}
      else if(tag.equals( type_area )){return String.valueOf(holder.getApartmentHolder().getApartmentType().getArea());}
      else if(tag.equals( cat_name )){return holder.getApartmentHolder().getApartmentCategory().getName();}
      else if(tag.equals( cat_desc )){return holder.getApartmentHolder().getApartmentCategory().getInfo();}
    }
    catch(NullPointerException ex){
      //  do nothing
    }
    return null;
  }
  public String[] getParseTypes(){
    return types;
  }

  public Object getParseObject(){
    return holder;
  }

}