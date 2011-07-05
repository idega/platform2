package is.idega.idegaweb.campus.block.allocation.presentation;

import is.idega.idegaweb.campus.block.allocation.business.ContractService;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
import is.idega.idegaweb.campus.block.allocation.data.ContractBMPBean;
import is.idega.idegaweb.campus.block.allocation.data.ContractTariff;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.building.business.BuildingCacher;
import com.idega.block.finance.data.Tariff;
import com.idega.block.finance.data.TariffHome;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;


public class ContractTariffSetter extends Block {
	protected static final String SUBMIT = "set_tariff";
	
	protected void control(IWContext iwc) {
		if (iwc.isParameterSet(SUBMIT)) {
			Contract contract = null;
			Collection contracts = null;
			try {
				contracts = getContractService(iwc).getContractHome().findByStatus(ContractBMPBean.STATUS_CREATED);
			} catch (RemoteException e) {
				contracts = null;
			} catch (FinderException e) {
				contracts = null;
			}
			if (contracts != null && !contracts.isEmpty()) {
				Iterator it = contracts.iterator();
				while (it.hasNext()) {
					contract = (Contract) it.next();
					Collection contractTariffEntries = getContractTariffEntries(iwc, contract);
					if (contractTariffEntries == null || contractTariffEntries.isEmpty()) {
						createContractTariffEntries(iwc, contract);
					}				
				}
			}
			
			try {
				contracts = getContractService(iwc).getContractHome().findByStatus(ContractBMPBean.STATUS_PRINTED);
			} catch (RemoteException e) {
				contracts = null;
			} catch (FinderException e) {
				contracts = null;
			}
			if (contracts != null && !contracts.isEmpty()) {
				Iterator it = contracts.iterator();
				while (it.hasNext()) {
					contract = (Contract) it.next();
					Collection contractTariffEntries = getContractTariffEntries(iwc, contract);
					if (contractTariffEntries == null || contractTariffEntries.isEmpty()) {
						createContractTariffEntries(iwc, contract);
					}				
				}
			}

			try {
				contracts = getContractService(iwc).getContractHome().findByStatus(ContractBMPBean.STATUS_SIGNED);
			} catch (RemoteException e) {
				contracts = null;
			} catch (FinderException e) {
				contracts = null;
			}
			if (contracts != null && !contracts.isEmpty()) {
				Iterator it = contracts.iterator();
				while (it.hasNext()) {
					contract = (Contract) it.next();
					Collection contractTariffEntries = getContractTariffEntries(iwc, contract);
					if (contractTariffEntries == null || contractTariffEntries.isEmpty()) {
						createContractTariffEntries(iwc, contract);
					}				
				}
			}

			try {
				contracts = getContractService(iwc).getContractHome().findByStatus(ContractBMPBean.STATUS_ENDED);
			} catch (RemoteException e) {
				contracts = null;
			} catch (FinderException e) {
				contracts = null;
			}
			if (contracts != null && !contracts.isEmpty()) {
				Iterator it = contracts.iterator();
				while (it.hasNext()) {
					contract = (Contract) it.next();
					Collection contractTariffEntries = getContractTariffEntries(iwc, contract);
					if (contractTariffEntries == null || contractTariffEntries.isEmpty()) {
						createContractTariffEntries(iwc, contract);
					}				
				}
			}

			try {
				contracts = getContractService(iwc).getContractHome().findByStatus(ContractBMPBean.STATUS_RESIGNED);
			} catch (RemoteException e) {
				contracts = null;
			} catch (FinderException e) {
				contracts = null;
			}
			if (contracts != null && !contracts.isEmpty()) {
				Iterator it = contracts.iterator();
				while (it.hasNext()) {
					contract = (Contract) it.next();
					Collection contractTariffEntries = getContractTariffEntries(iwc, contract);
					if (contractTariffEntries == null || contractTariffEntries.isEmpty()) {
						createContractTariffEntries(iwc, contract);
					}				
				}
			}
		}
		
		displayForm();			
	}

	protected void displayForm() {
		Form form = new Form();
		SubmitButton button = new SubmitButton(SUBMIT,"Set contract tariff");
		form.add(button);
		add(new Text("Set contract tariff on all contracts without a contract tariff"));
		add(form);	
	}

	public void main(IWContext iwc) {
		control(iwc);
	}
	
	private Collection createContractTariffEntries(IWContext iwc, Contract contract) {
		Collection ret = new ArrayList();
		try {
			Collection tariffs = ((TariffHome) IDOLookup.getHome(Tariff.class))
					.findByTariffGroup(new Integer(9));
			Tariff tariff;
			char cAttribute;
			int attributeId = -1;

			// For each tariff (Inner loop)
			for (Iterator iter2 = tariffs.iterator(); iter2.hasNext();) {
				tariff = (Tariff) iter2.next();
				String sAttribute = tariff.getTariffAttribute();
				// If we have an tariff attribute
				if (sAttribute != null) {
					attributeId = -1;
					cAttribute = sAttribute.charAt(0);
					// If All
					if (cAttribute == BuildingCacher.CHARALL) {
						insertContractTariffEntry(iwc, contract, tariff);
					}
					// other than all
					else {
						// attribute check
						if (sAttribute.length() >= 3) {
							attributeId = Integer.parseInt(sAttribute
									.substring(2));
							switch (cAttribute) {
							case BuildingCacher.CHARTYPE:
								// Apartment type
								if (attributeId == contract.getApartment()
										.getApartmentTypeId()) {
									ContractTariff t = insertContractTariffEntry(
											iwc, contract, tariff);
									if (t != null) {
										ret.add(t);
									}
								}
								break;
							case BuildingCacher.CHARCATEGORY:
								// Apartment category
								if (attributeId == ((Integer) contract
										.getApartment().getApartmentType()
										.getApartmentSubcategory()
										.getApartmentCategory().getPrimaryKey())
										.intValue()) {
									ContractTariff t = insertContractTariffEntry(
											iwc, contract, tariff);
									if (t != null) {
										ret.add(t);
									}
								}
								break;
							case BuildingCacher.CHARBUILDING:
								// Building
								if (attributeId == contract.getApartment()
										.getFloor().getBuildingId()) {
									ContractTariff t = insertContractTariffEntry(
											iwc, contract, tariff);
									if (t != null) {
										ret.add(t);
									}
								}
								break;
							case BuildingCacher.CHARFLOOR:
								// Floor
								if (attributeId == contract.getApartment()
										.getFloorId()) {
									ContractTariff t = insertContractTariffEntry(
											iwc, contract, tariff);
									if (t != null) {
										ret.add(t);
									}
								}
								break;
							case BuildingCacher.CHARCOMPLEX:
								// Complex
								if (attributeId == contract.getApartment()
										.getFloor().getBuilding()
										.getComplexId()) {
									ContractTariff t = insertContractTariffEntry(
											iwc, contract, tariff);
									if (t != null) {
										ret.add(t);
									}
								}
								break;
							case BuildingCacher.CHARAPARTMENT:
								// Apartment
								if (attributeId == contract.getApartmentId()
										.intValue()) {
									ContractTariff t = insertContractTariffEntry(
											iwc, contract, tariff);
									if (t != null) {
										ret.add(t);
									}
								}
								break;
							} // switch
						} // attribute check
					}
				}
			}
		} catch (Exception e) {
		}

		return ret;
	}
	
	private ContractTariff insertContractTariffEntry(IWContext iwc,
			Contract contract, Tariff tariff) {
		try {
			ContractTariff ct = getContractService(iwc).getContractTariffHome()
					.create();
			ct.setAccountKey(tariff.getAccountKeyId());
			ct.setContract(contract);
			ct.setIndexType(tariff.getIndexType());
			ct.setIndexUpdated(tariff.getIndexUpdated());
			ct.setIsDeleted(false);
			ct.setName(tariff.getName());
			ct.setPrice(tariff.getPrice());
			ct.setUseIndex(tariff.getUseIndex());
			ct.store();

			return ct;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}

		return null;
	}

	
	private Collection getContractTariffEntries(IWContext iwc, Contract contract) {
		try {
			return getContractService(iwc).getContractTariffHome()
					.findByContract(contract);
		} catch (Exception e) {
		}

		return null;
	}

	public ContractService getContractService(IWContext iwac) throws RemoteException {
		return (ContractService) IBOLookup.getServiceInstance(iwac, ContractService.class);
	}
}