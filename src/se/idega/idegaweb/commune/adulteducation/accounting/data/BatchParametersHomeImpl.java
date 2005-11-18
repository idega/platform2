/**
 * 
 */
package se.idega.idegaweb.commune.adulteducation.accounting.data;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolType;
import com.idega.data.IDOFactory;

/**
 * @author bluebottle
 *
 */
public class BatchParametersHomeImpl extends IDOFactory implements
		BatchParametersHome {
	protected Class getEntityInterfaceClass() {
		return BatchParameters.class;
	}

	public BatchParameters create() throws javax.ejb.CreateException {
		return (BatchParameters) super.createIDO();
	}

	public BatchParameters findByPrimaryKey(Object pk)
			throws javax.ejb.FinderException {
		return (BatchParameters) super.findByPrimaryKeyIDO(pk);
	}

	public BatchParameters findBySchoolType(SchoolType schoolType)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((BatchParametersBMPBean) entity)
				.ejbFindBySchoolType(schoolType);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public BatchParameters findBySchoolTypeId(int schoolTypeId)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((BatchParametersBMPBean) entity)
				.ejbFindBySchoolTypeId(schoolTypeId);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

}
