/*
 * Created on 2005-maj-11
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.adulteducation.data;



import com.idega.data.IDOFactory;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AdultEducationPersonalInfoHomeImpl extends IDOFactory implements
		AdultEducationPersonalInfoHome {
	protected Class getEntityInterfaceClass() {
		return AdultEducationPersonalInfo.class;
	}

	public AdultEducationPersonalInfo create() throws javax.ejb.CreateException {
		return (AdultEducationPersonalInfo) super.createIDO();
	}

	public AdultEducationPersonalInfo findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (AdultEducationPersonalInfo) super.findByPrimaryKeyIDO(pk);
	}

	public AdultEducationPersonalInfo findByUserId(Integer userId)
			throws javax.ejb.FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((AdultEducationPersonalInfoBMPBean) entity)
				.ejbFindByUserId(userId);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

}
