/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.data;

import com.idega.data.GenericEntity;

/**
 * @author palli
 */
public class AssessmentRoundRecordBMPBean extends GenericEntity implements AssessmentRoundRecord {
	protected final static String ENTITY_NAME = "isi_ass_rec";
	
	protected final static String COLUMN_ASSESSMENT_ROUND = "ass_round_id";
	protected final static String COLUMN_USER = "user_id";
//	protected final static String
	
	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/* (non-Javadoc)
	 * @see com.idega.data.GenericEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_ASSESSMENT_ROUND,AssessmentRound.class);
	}
}