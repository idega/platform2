package com.idega.block.questions.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import javax.ejb.FinderException;

import com.idega.block.category.data.CategoryEntityBMPBean;
import com.idega.block.text.data.TxText;
import com.idega.data.IDOQuery;

/**
 * 
 * <p>Company: idegaweb </p>
 * @author aron
 * 
 *
 */
public class QuestionBMPBean extends CategoryEntityBMPBean implements Question{
	
	public final static String TABLE_NAME = "qa_question";
	public final static String QUESTION = "question_id";
	public final static String ANSWER = "answer_id";
	public final static String VALID = "valid";
	public final static String SEQUENCE = "sequence_number";
	
	/**
	 * @see com.idega.data.IDOLegacyEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		 addAttribute(getIDColumnName());
		 addAttribute(QUESTION, "Question", true, true, Integer.class,MANY_TO_ONE,TxText.class);
		 addAttribute(ANSWER, "Answert", true, true, Integer.class,MANY_TO_ONE,TxText.class);
		 addAttribute(VALID,"Valid",true,true,Boolean.class);
		 addAttribute(SEQUENCE,"sequence",true,true,Integer.class);
	}

	
	/**
	 * @see com.idega.data.IDOLegacyEntity#getEntityName()
	 */
	public String getEntityName() {
		return TABLE_NAME;
	}
	
	public int getQuestionID(){
		return this.getIntColumnValue(QUESTION);
	}
	
	public int getAnswerID(){
		return this.getIntColumnValue(ANSWER);
	}
	
	public void setQuestionID(int question){
		this.setColumn(QUESTION,question);
	}
	
	public void setAnswerID(int answer){
		this.setColumn(ANSWER,answer);
	}
	
	public boolean getValid(){
		return this.getBooleanColumnValue(VALID);
	}
	
	public void setValid(boolean valid){
		this.setColumn(VALID,valid);
	}
	
	public void setSequence(int sequence){
		this.setColumn(SEQUENCE,sequence);
	}
	
	public int getSequence(){
		return this.getIntColumnValue(SEQUENCE);
	}
	
	public Collection ejbFindAllByCategory(int iCategory) throws FinderException{
		StringBuffer sql = new StringBuffer("select * from ").append(TABLE_NAME);
		sql.append(" where ").append(this.getColumnCategoryId()).append("=").append(iCategory);
		sql.append(" and ").append(VALID).append("='Y'");
		sql.append(" order by ").append(SEQUENCE);
		return this.idoFindPKsBySQL(sql.toString());
	} 
	
	public Collection ejbFindAllInvalidByCategory(int iCategory) throws FinderException{
		StringBuffer sql = new StringBuffer("select * from ").append(TABLE_NAME);
		sql.append(" where ").append(this.getColumnCategoryId()).append("=").append(iCategory);
		sql.append(" and ").append(VALID).append("='N'");
		sql.append(" order by ").append(SEQUENCE);
		return this.idoFindPKsBySQL(sql.toString());
	} 
	
	public Object ejbFindRandom(String[] categoryIds)throws FinderException{
	   IDOQuery query = idoQueryGetSelect().appendWhere().append(getColumnCategoryId()).appendInArray(categoryIds);
	   Collection ids = this.idoFindPKsByQuery(query);
	   ArrayList list = new ArrayList(ids);
	   int index = new Random().nextInt(list.size());
	   return list.get(index);
	}
	
	
}
