/*
 * Created on Mar 9, 2004
 *
 */
package com.idega.block.finance.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.user.data.User;
import com.idega.user.data.UserBMPBean;

/**
 * AccountUserBMPBean
 * @author aron 
 * @version 1.0
 */
public class AccountUserBMPBean extends UserBMPBean implements AccountUser{
	
	public Collection ejbFindByAssessmentRound(Integer roundID)throws FinderException{
		 StringBuffer sql = new StringBuffer("select distinct u.* ");
		    sql.append(" from fin_account a,fin_acc_entry e,fin_assessment_round r ,ic_user u ");
		    sql.append(" where a.fin_account_id = e.fin_account_id ");
		    sql.append(" and e.fin_assessment_round_id = r.fin_assessment_round_id ");
		    sql.append(" and a.ic_user_id = u.ic_user_id");
		    sql.append(" and r.fin_assessment_round_id = ");
		    sql.append(roundID);
		    return super.idoFindPKsBySQL(sql.toString());
	}
	
	public Collection ejbFindBySearch(String first,String middle,String last)throws FinderException{
		StringBuffer sql = new StringBuffer("select * from ");
	    sql.append("ic_user u ");
	    boolean isfirst = true;
	    if(first != null || middle !=null || last !=null){
	      sql.append(" where ");
	      if(first !=null && !"".equals(first)){
	        if(!isfirst)
	          sql.append(" and ");
	        sql.append(" u.first_name like '%");
	        sql.append(first);
	        sql.append("%' ");
	        isfirst = false;
	      }
	      if(middle !=null && !"".equals(middle)){
	        if(!isfirst)
	          sql.append(" and ");
	        sql.append(" u.middle_name like '%");
	        sql.append(middle);
	        sql.append("%' ");
	        isfirst = false;
	      }
	      if(last !=null && !"".equals(last )){
	        if(!isfirst)
	          sql.append(" and ");
	        sql.append(" u.last_name like '%");
	        sql.append(last);
	        sql.append("%' ");
	        isfirst = false;
	      }
	      return super.idoFindPKsBySQL(sql.toString());
	}
	    throw new FinderException("Searching user with null conditions");
	}
}