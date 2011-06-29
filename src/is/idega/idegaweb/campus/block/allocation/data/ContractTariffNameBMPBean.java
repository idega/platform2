package is.idega.idegaweb.campus.block.allocation.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

public class ContractTariffNameBMPBean extends GenericEntity implements
		ContractTariffName {

	private static final String ENTITY_NAME = "cam_contract_tariff_name";
	
	private static final String COLUMN_NAME = "name";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "Name", String.class);
	}
	
	//getters
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}
	
	//setters
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}
	
	//ejb
	public Collection ejbFindAll() throws FinderException {
		return super.idoFindAllIDsBySQL();
	}
	
	public Object ejbFindByContract(String name) throws FinderException {
		IDOQuery query = super.idoQueryGetSelect();
		query.appendWhereEquals(COLUMN_NAME, name);
		
		return super.idoFindOnePKByQuery(query);
	}	
}