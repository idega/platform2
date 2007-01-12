package com.idega.block.reports.business;



import com.idega.block.reports.data.*;

import java.util.StringTokenizer;

import java.sql.SQLException;

 /**

  * Title:

  * Description:

  * Copyright:    Copyright (c) 2001

  * Company:      idega multimedia

  * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>

  * @version 1.0

  */



 public class ReportCondition{



  private ReportItem Item;

  private String sJoin="",sOperator="",sVariable="";

  private String[] sOps,sVars;

  private String sCondition="";

  private boolean bCondition,bSelect,bField = false;

  private boolean bBetween = false;

  private Integer orderNumber = null;

  private Integer colOrder = null;

	private String sVarOne = null,sVarTwo = null;

	private String sFunction = null;

	private boolean bFunction = false;



  public ReportCondition(ReportItem Item) {

   this.Item = Item;

   init();

  }

  public ReportCondition(int ItemId){

    try {

      this.Item  = ((com.idega.block.reports.data.ReportItemHome)com.idega.data.IDOLookup.getHomeLegacy(ReportItem.class)).findByPrimaryKeyLegacy(ItemId);

      init();

    }

    catch (SQLException ex) {

      ex.printStackTrace();

    }

  }

  private void init(){

    if(this.Item.getField()!= null){

      this.bField = true;

      StringBuffer sb = new StringBuffer(this.Item.getMainTable());

      sb.append(".");

      sb.append(this.Item.getField());

      this.sJoin = sb.toString();

      this.sOps = this.Item.getOps();

    }

    this.bCondition = false;

    this.bSelect   = false;

  }

  public void setCondition(){



  }

  public ReportItem getItem(){

    return this.Item;

  }

  public String getCondition(){

    StringBuffer sb = new StringBuffer("");



    if(this.bCondition && this.bField){

      if(this.sOperator.equalsIgnoreCase("BETWEEN") && this.sVarOne !=null && this.sVarTwo!=null){

        sb.append(this.sJoin);

        sb.append(" BETWEEN '");

        sb.append(this.sVarOne);

        sb.append("' AND '");

        sb.append(this.sVarTwo);

        sb.append("'");

      }

			else if(this.sOperator.equalsIgnoreCase("IN")){

				sb.append(this.sJoin);

        sb.append(" ");

        sb.append(" BETWEEN '");

        sb.append(this.sVarOne);

			}

			/* //old

      else{

        int len = this.Item.getOps().length;

        for (int i = 0; i < len; i++) {

          sb.append(this.sJoin);

          sb.append(" ");

          sb.append(this.sOps[i]);

          sb.append(" ");

          if(sVars != null){

            sb.append("'");

            sb.append(this.sVars[i]);

            sb.append("' ");

          }

        }

      }

			*/

    }

    this.sCondition = sb.toString();

		if(this.Item.getConditionType().equalsIgnoreCase("I")) {
			this.sCondition = this.sCondition.replace("'".charAt(0)," ".charAt(0) );
		}

    return this.sCondition;

  }

  public void setOperator(String[] sOps){

    this.sOps = sOps;

  }



	public void setVariableOne(String sVar){

	  this.sVarOne = sVar;

		this.bCondition = true;

	}

	public void setVariableTwo(String sVar){

	  this.sVarTwo = sVar;

		this.bCondition = true;

	}



  public void setVariable(String sVar){

    StringTokenizer st = new StringTokenizer(sVar,":");

    if(st.countTokens() == 2){

      this.bBetween = true;

      this.sVars = new String[2];

      this.sVars[0] = st.nextToken();

      this.sVars[1] = st.nextToken();

    }

    else{

      this.sVars = new String[1];

      this.sVars[0] = sVar;

    }

    this.bCondition = true;

  }

  public void setVariables(String[] sVars){

    this.sVars = sVars;

    this.bCondition = true;

  }

  public String getDisplay(){

    return this.getItem().getName();

  }

  public void setIsSelect(){

    this.bSelect = true;

  }

  public boolean isSelect(){

    return this.bSelect;

  }

  public void setColumnOrder(Integer order){

    this.colOrder = order;

  }

  public Integer getColumnOrder(){

    return this.colOrder;

  }

  public void setOrder(Integer order){

    this.orderNumber = order;

  }

  public Integer getOrder(){

    return this.orderNumber;

  }

	public String getOperator(){

	  return this.sOperator;

	}

	public void setOperator(String sOp){

	  this.sOperator = sOp;

	}

	public String getFunction(){

	  return this.sOperator;

	}

	public void setFunction(String sOp){

		this.bFunction = true;

	  this.sOperator = sOp;

	}

	public boolean isFunction(){

	  return this.bFunction;

	}

	public String getFieldFullName(){

	  return this.Item.getMainTable() +"."+this.Item.getField();

	}

	public String getFieldFunction(){

	  return getFunction()+"("+getFieldFullName()+")";

	}

	public String getField(){

	  if(isFunction()) {
		return getFieldFunction();
	}
	else {
		return getFieldFullName();
	}

	}

}
