package com.idega.block.questions.business;
import java.rmi.RemoteException;

import com.idega.block.questions.data.Question;
import com.idega.block.questions.data.QuestionHome;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
/**
 * 
 * <p>Company: idegaweb </p>
 * @author aron
 * 
 *
 */
public class QuestionsServiceBean extends IBOServiceBean implements QuestionsService {
	
	public QuestionHome getQuestionHome()throws RemoteException{
		return (QuestionHome)IDOLookup.getHome(Question.class);
	}
	
	public Question storeQuestion(int entityID,int QuestionID,int AnswerID,int CategoryID)throws RemoteException{
		try {
			if(CategoryID >0){
				Question quest = getQuestionHome().create();
				if(entityID>0)
					quest = getQuestionHome().findByPrimaryKey(new Integer(entityID));
				quest.setQuestionID(QuestionID);
				if(AnswerID>0)
					quest.setAnswerID(AnswerID);
				quest.setCategoryId(CategoryID);
				quest.setValid(true);
				quest.store();
				return quest;
			}
			
		}
		catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
		return null;
	}
	
	public void invalidateQueston(int id)throws RemoteException{
		try {
			Question question = getQuestionHome().findByPrimaryKey(new Integer(id));
			question.setValid(false);
			question.store();
		}
		catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}
}