package is.idega.idegaweb.campus.block.questionaire1011.data;

import java.util.Collection;

import javax.ejb.FinderException;
import com.idega.data.GenericEntity;
import com.idega.user.data.User;

/**
 * @author palli
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class QuestionaireBMPBean extends GenericEntity implements Questionaire{
	private final static String ENTITY_NAME = "cam_quest1011";
	
	private final static String ANSWER_1 = "answer_1";
	private final static String ANSWER_2 = "answer_2";
	private final static String ANSWER_3 = "answer_3";
	private final static String ANSWER_4 = "answer_4";
	private final static String ANSWER_5 = "answer_5";
	private final static String ANSWER_6 = "answer_6";
	private final static String ANSWER_7 = "answer_7";
	private final static String ANSWER_8 = "answer_8";
	private final static String ANSWER_9 = "answer_9";
	private final static String USER_ID = "user_id";


	public QuestionaireBMPBean() {
		super();
	}

	/**
	 * @see com.idega.data.IDOLegacyEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/**
	 * @see com.idega.data.IDOLegacyEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(ANSWER_1,"Answer to question 1",true,true,java.lang.Integer.class);
		addAttribute(ANSWER_2,"Answer to question 2",true,true,java.lang.Integer.class);
		addAttribute(ANSWER_3,"Answer to question 3",true,true,java.lang.Integer.class);
		addAttribute(ANSWER_4,"Answer to question 4",true,true,java.lang.Integer.class);
		addAttribute(ANSWER_5,"Answer to question 5",true,true,java.lang.Integer.class);
		addAttribute(ANSWER_6,"Answer to question 6",true,true,java.lang.Integer.class);
		addAttribute(ANSWER_7,"Answer to question 7",true,true,java.lang.Integer.class);
		addAttribute(ANSWER_8,"Answer to question 8",true,true,java.lang.Integer.class);
		addAttribute(ANSWER_9,"Answer to question 9",true,true,java.lang.Integer.class);
		
		addManyToOneRelationship(USER_ID,User.class);
	}

	public int getAnswer1() {
		return getIntColumnValue(ANSWER_1);	
	}
	
	public void setAnswer1(int answer) {
		setColumn(ANSWER_1,answer);	
	}
	
	public int getAnswer2() {
		return getIntColumnValue(ANSWER_2);	
	}
	
	public void setAnswer2(int answer) {
		setColumn(ANSWER_2,answer);	
	}

	public int getAnswer3() {
		return getIntColumnValue(ANSWER_3);	
	}
	
	public void setAnswer3(int answer) {
		setColumn(ANSWER_3,answer);	
	}

	public int getAnswer4() {
		return getIntColumnValue(ANSWER_4);	
	}
	
	public void setAnswer4(int answer) {
		setColumn(ANSWER_4,answer);	
	}

	public int getAnswer5() {
		return getIntColumnValue(ANSWER_5);	
	}
	
	public void setAnswer5(int answer) {
		setColumn(ANSWER_5,answer);	
	}

	public int getAnswer6() {
		return getIntColumnValue(ANSWER_6);	
	}
	
	public void setAnswer6(int answer) {
		setColumn(ANSWER_6,answer);	
	}

	public int getAnswer7() {
		return getIntColumnValue(ANSWER_7);	
	}
	
	public void setAnswer7(int answer) {
		setColumn(ANSWER_7,answer);	
	}

	public int getAnswer8() {
		return getIntColumnValue(ANSWER_8);	
	}
	
	public void setAnswer8(int answer) {
		setColumn(ANSWER_8,answer);	
	}

	public int getAnswer9() {
		return getIntColumnValue(ANSWER_9);	
	}
	
	public void setAnswer9(int answer) {
		setColumn(ANSWER_9,answer);	
	}

	public User getUser() {
		return (User)getColumnValue(USER_ID);	
	}
	
	public int getUserID() {
		return getIntColumnValue(USER_ID);
	}
	
	public void setUser(User user) {
		setColumn(USER_ID,user);
	}
	
	public void setUserID(int id) {
		setColumn(USER_ID,id);
	}
	
	public Collection ejbFindAllByUser(User user) throws FinderException{
		int i = ((Integer)user.getPrimaryKey()).intValue();
		
		return ejbFindAllByUser(i);
	}
	
	public Collection ejbFindAllByUser(int userID) throws FinderException {
		StringBuffer sql = new StringBuffer("select * from ");
		sql.append(getTableName()); 
		sql.append(" where ");
		sql.append(USER_ID);
		sql.append(" = ");
		sql.append(userID);

		return super.idoFindPKsBySQL(sql.toString());		
	}
}