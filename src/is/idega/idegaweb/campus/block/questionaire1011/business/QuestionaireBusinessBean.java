package is.idega.idegaweb.campus.block.questionaire1011.business;

import is.idega.idegaweb.campus.block.questionaire1011.data.Questionaire;
import is.idega.idegaweb.campus.block.questionaire1011.data.QuestionaireHome;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;

import com.idega.business.IBOServiceBean;
import com.idega.user.data.User;
import com.idega.data.IDOLookup;

/**
 * @author palli
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class QuestionaireBusinessBean extends IBOServiceBean implements QuestionaireBusiness {
	public boolean hasUserAlreadyAnswered(User user) {
		try {
			Collection col = getQuestionaireHome().findAllByUser(user);
		
			if (col == null || col.isEmpty())
				return false;
			else
				return true;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean insertAnswers(int a1, int a2, int a3, int a4, int a5, int a6, int a7, int a8, int a9, User user) {
		try {
			Questionaire questionaire = getQuestionaireHome().create();
			
			questionaire.setAnswer1(a1);
			questionaire.setAnswer2(a2);
			questionaire.setAnswer3(a3);
			questionaire.setAnswer4(a4);
			questionaire.setAnswer5(a5);
			questionaire.setAnswer6(a6);
			questionaire.setAnswer7(a7);
			questionaire.setAnswer8(a8);
			questionaire.setAnswer9(a9);
			if (user != null)
				questionaire.setUser(user);
			
			questionaire.store();
			return true;
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (CreateException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private QuestionaireHome getQuestionaireHome() throws RemoteException {
		return (QuestionaireHome) IDOLookup.getHome(Questionaire.class);
	}	
}