package com.idega.block.questions.business;
import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import com.idega.block.questions.data.Question;
import com.idega.block.questions.data.QuestionHome;
import com.idega.block.text.business.ContentHelper;
import com.idega.block.text.business.TextFinder;
import com.idega.block.text.business.TextService;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.data.TxText;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOStoreException;
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
				quest.setSequence(((Integer)quest.getPrimaryKey()).intValue());
				quest.store();
				return quest;
			}
			
		}
		catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
		return null;
	}
	
	public Question storeQuestion(Integer entityID,Integer localeID,Integer categoryID,Integer userID,String qTitle,String qBody,String aTitle,String aBody){
		if(categoryID!=null && categoryID.intValue() >0){
			try {
				Question entity = getQuestionHome().create();
				Integer qlocID = null,alocID =null;
				Integer qtextID = null,atextID = null;
				boolean freshentity = true;
				if(entityID!=null && entityID.intValue()>0){
					freshentity = false;
					entity = getQuestionHome().findByPrimaryKey(entityID);
					qtextID = new Integer(entity.getQuestionID());
					ContentHelper qhelper = TextFinder.getContentHelper(qtextID.intValue(),localeID.intValue());
					if(qhelper!=null){
					    LocalizedText qtext = qhelper.getLocalizedText();
					    if(qtext!=null){
					    	qlocID = (Integer) qtext.getPrimaryKey();
					    }
					}
					atextID = new Integer(entity.getAnswerID());
					ContentHelper ahelper = TextFinder.getContentHelper(atextID.intValue(),localeID.intValue());
					if(ahelper!=null){
						LocalizedText atext = ahelper.getLocalizedText();
						if(atext!=null){
							alocID = (Integer) atext.getPrimaryKey();
						}
					}
				}
				TextService tservice = getTextService();
				if(qTitle!=null || qBody!=null){
					TxText qText = tservice.storeText(qtextID,qlocID,localeID,userID,qTitle,null,qBody);
					qtextID = (Integer) qText.getPrimaryKey();
				}
				if(aTitle!=null || aBody!=null){
					TxText aText = tservice.storeText(atextID,alocID,localeID,userID,aTitle,null,aBody);
					atextID = (Integer) aText.getPrimaryKey();
				}
				entity.setCategoryId(categoryID.intValue());
				entity.setValid(true);
				if(qtextID!=null)
					entity.setQuestionID(qtextID.intValue());
				if(atextID!=null)
					entity.setAnswerID(atextID.intValue());
				entity.store();
				if(freshentity){
					entity.setSequence(((Integer)entity.getPrimaryKey()).intValue());
					entity.store();
				}
				
				return entity;
			}
			catch (IDOStoreException e) {
				e.printStackTrace();
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (EJBException e) {
				e.printStackTrace();
			}
			catch (CreateException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void invalidateQuestion(int id)throws RemoteException{
		try {
			Question question = getQuestionHome().findByPrimaryKey(new Integer(id));
			question.setValid(false);
			question.store();
		}
		catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	public void validateQuestion(int id)throws RemoteException{
		try {
			Question question = getQuestionHome().findByPrimaryKey(new Integer(id));
			question.setValid(true);
			question.store();
		}
		catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	public void swapSequences(int currentId,int otherId){
		try {
			Question currentQuestion = getQuestionHome().findByPrimaryKey(new Integer(currentId));	
			Question otherQuestion = getQuestionHome().findByPrimaryKey(new Integer(otherId));
			int currentSeqence = currentQuestion.getSequence();
			currentQuestion.setSequence(otherQuestion.getSequence());
			otherQuestion.setSequence(currentSeqence);
			currentQuestion.store();
			otherQuestion.store();
		}
		catch (Exception e) {
		}
	
	}
	
	public void resetQuestionSequence(int questionId)throws RemoteException{
		try{
			Question currentQuestion = getQuestionHome().findByPrimaryKey(new Integer(questionId));
			currentQuestion.setSequence(((Integer)currentQuestion.getPrimaryKey()).intValue());
			currentQuestion.store();	
		}catch(Exception e){throw new RemoteException(e.getMessage());}
	}
	
	public void removeQuestion(int questionId)throws RemoteException{
		try {
			getQuestionHome().findByPrimaryKey(new Integer(questionId)).remove();
		}
		catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}
	
	public TextService getTextService()throws RemoteException{
		return (TextService)getServiceInstance(TextService.class);
	}
	
	
    /* (non-Javadoc)
     * @see com.idega.block.questions.business.QuestionsService#getRandomQuestion(int[])
     */
    public Question getRandomQuestion(int[] categoryIds)throws RemoteException {
        try {
            String[] ids = new String[categoryIds.length];
            for (int i = 0; i < categoryIds.length; i++) {
                ids[i] = String.valueOf(categoryIds[i]);
            }
            return getQuestionHome().findRandom(ids);
        } catch (FinderException e) {
            e.printStackTrace();
        }
        return null;
    }
}