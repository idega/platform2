/*
 * Created on 30.7.2004
 *
 * Copyright (C) 2004 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 */
package is.idega.idegaweb.campus.block.application.data;

import java.util.ArrayList;
import java.util.List;

import com.idega.block.application.data.Applicant;

/**
 * @author aron
 *
 * ApplicantFamily TODO Describe this type
 */
public class ApplicantFamily {
	
	Applicant applicant = null;
	Applicant spouse = null;
	List children = new ArrayList();
	
	public ApplicantFamily(Applicant applicant){
		setApplicant(applicant);
		java.util.Iterator iter = applicant.getChildrenIterator();
        if(iter !=null){
          String status;
          while(iter.hasNext()){
            Applicant cant = (Applicant) iter.next();
            status = cant.getStatus();
            if("P".equals(status)){
              setSpouse(cant);
            }
            else if("C".equals(status)){
              addChild(cant);
            }

          }
        }
	}

	/**
	 * @return Returns the applicant.
	 */
	public Applicant getApplicant() {
		return applicant;
	}
	/**
	 * @param applicant The applicant to set.
	 */
	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}
	/**
	 * @return Returns the children.
	 */
	public List getChildren() {
		return children;
	}
	/**
	 * @param children The children to set.
	 */
	public void setChildren(List children) {
		this.children = children;
	}
	/**
	 * @return Returns the spouse.
	 */
	public Applicant getSpouse() {
		return spouse;
	}
	/**
	 * @param spouse The spouse to set.
	 */
	public void setSpouse(Applicant spouse) {
		this.spouse = spouse;
	}
	
	public void addChild(Applicant child){
		if(children==null)
			children = new ArrayList();
		children.add(child);
	}
	
	
	
}
