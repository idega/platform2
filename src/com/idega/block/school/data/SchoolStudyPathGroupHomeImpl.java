/**
 * 
 */
package com.idega.block.school.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;


/**
 * <p>
 * TODO Dainis Describe Type SchoolStudyPathGroupHomeImpl
 * </p>
 *  Last modified: $Date: 2005/11/28 18:48:26 $ by $Author: dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.2.2.1 $
 */
public class SchoolStudyPathGroupHomeImpl extends IDOFactory implements SchoolStudyPathGroupHome {

	protected Class getEntityInterfaceClass() {
		return SchoolStudyPathGroup.class;
	}

	public SchoolStudyPathGroup create() throws javax.ejb.CreateException {
		return (SchoolStudyPathGroup) super.createIDO();
	}

	public SchoolStudyPathGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (SchoolStudyPathGroup) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllStudyPathGroups() throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolStudyPathGroupBMPBean) entity).ejbFindAllStudyPathGroups();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public SchoolStudyPathGroup findByGroupName(String name) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((SchoolStudyPathGroupBMPBean) entity).ejbFindByGroupName(name);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}
