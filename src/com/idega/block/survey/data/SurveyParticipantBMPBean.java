/*
 * Created on 18.1.2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.survey.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOException;
import com.idega.data.IDOQuery;
import com.idega.util.ListUtil;

/**
 * Title:		SurveyParticipant
 * Description:
 * Copyright:	Copyright (c) 2004
 * Company:		idega Software
 * @author		2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class SurveyParticipantBMPBean extends GenericEntity implements SurveyParticipant{
	
	public static final String COLUMNNAME_NAME = "PARTICIPANT_NAME";
	public static final String COLUMNNAME_SURVEY = "SU_SURVEY_ID";
	
	/**
	 * 
	 */
	public SurveyParticipantBMPBean() {
		super();
	}


	public String getEntityName() {
		return "SU_SURVEY_PARTICIPANT";
	}


	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMNNAME_NAME,"Participant name",true,true,String.class);
		
		addManyToOneRelationship(COLUMNNAME_SURVEY,SurveyEntity.class);
	}
	
	public void setParticipantName(String name){
		setColumn(COLUMNNAME_NAME,name);
	}
	
	public void setSurvey(SurveyEntity survey){
		setColumn(COLUMNNAME_SURVEY,survey);
	}
	
	public String getParticipantName(){
		return getStringColumnValue(COLUMNNAME_NAME);
	}
	
	public SurveyEntity getSurvey(){
		return (SurveyEntity)getColumnValue(COLUMNNAME_SURVEY);
	}
	
	public int ejbHomeGetNumberOfParticipations(SurveyEntity survey, String name) throws IDOException{
		IDOQuery query = idoQueryGetSelectCount();
		query.appendWhereEquals(COLUMNNAME_SURVEY,survey);
		query.appendAndEquals(COLUMNNAME_NAME,name);
		
		return idoGetNumberOfRecords(query);
	}
	
	public Collection ejbFindRandomParticipants(SurveyEntity survey,int maxNumberOfReturnedParticipants, boolean evenChance) throws FinderException{
		Collection toReturn = new Vector();
		IDOQuery query = idoQueryGetSelect();
		query.appendWhereEquals(COLUMNNAME_SURVEY,survey);
		query.appendAndIsNotNull(COLUMNNAME_NAME);
		query.appendAnd();
		query.append(COLUMNNAME_NAME);
		query.appendNOTLike();
		query.appendWithinSingleQuotes("");
		
//		if(evenChance){
//			query.appendGroupBy(COLUMNNAME_NAME);
//		}
		
		List pks = ListUtil.convertCollectionToList(idoFindPKsByQuery(query));
		
		if(pks.size() <= maxNumberOfReturnedParticipants){
			return pks;
		} else {
			
			Set set = new HashSet();
			
			while(set.size() < maxNumberOfReturnedParticipants){
				Random rand = new Random();
				int index = rand.nextInt(pks.size());
				boolean success = set.add(pks.get(index));
				int ring = index;
				boolean coil = false;
				while(!success){
					if(ring == ++index){
						coil = true;
						break;
					}
					if(index == pks.size()){
						index = 0;
					}
					success = set.add(pks.get(index));
				}
				if(coil){
					break;
				}
			}
			toReturn = set;
						
//			int index=0;
//			int endlessLoopBreaker = 0;
//			int[] choice = new int[maxNumberOfReturnedParticipants]; 
//			for(int i=0; i<maxNumberOfReturnedParticipants;i++){
//				index = (int)(maxNumberOfReturnedParticipants*Math.random());
//				for(int j = 0; i < j; j++){
//					if(choice[j]==index){
//						index++;
//						j=0;
//					}
//					endlessLoopBreaker++;
//					if(endlessLoopBreaker >= Integer.MAX_VALUE){
//						System.out.println("[ERROR]: ("+this.getClass().getName()+"): endless loop in random choice");
//						break;
//					} else if(endlessLoopBreaker >= Integer.MAX_VALUE/3){
//						System.out.println("[WARNING]: ("+this.getClass().getName()+"): long loop in random choice");
//					}
//				}
//				endlessLoopBreaker=0;
//				choice[i]=index;
//			}
//			for (int i = 0; i < choice.length; i++) {
//				toReturn.add(pks.get(choice[i]));
//			}
		}
		return toReturn;		
	}
	
	
	

}
