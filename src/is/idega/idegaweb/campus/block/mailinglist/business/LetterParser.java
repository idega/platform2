package is.idega.idegaweb.campus.block.mailinglist.business;

import is.idega.idegaweb.campus.block.allocation.data.Contract;

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
  public final static String contract_starts = "CONTRACTS_STARTS";
  public final static String contract_ends = "CONTRACTS_ENDS";
  public final static String today = "TODAY";

  public static String APPROVAL = "APPROVAL";
  public static String REJECTION = "REJECTION";
  public static String ALLOCATION = "ALLOCATION";
  public static String SIGNATURE = "SIGNATURE";
  public static String RETURN = "RETURN";
  public static String DELIVER = "DELIVER";


  public  static String[] TAGS = { tenant_name,tenant_address,tenant_id,
                            contract_starts,contract_ends,today};

  public  static String[] types = { APPROVAL,REJECTION,ALLOCATION,
                            SIGNATURE,RETURN,DELIVER};

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
    if(tag.equals(tenant_name)){
      return holder.getUser().getName();
    }
    else if(tag.equals(tenant_address)){
      return holder.getApplicant().getResidence();
    }
    else if(tag.equals(tenant_id)){
      return holder.getApplicant().getSSN();
    }
    else if(tag.equals(contract_starts)){
      return holder.getContract().getValidFrom().toString();
    }
    else if(tag.equals(contract_ends)){
      return holder.getContract().getValidTo().toString();
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