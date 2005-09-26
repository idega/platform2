/**
 * 
 */
package se.idega.idegaweb.commune.accounting.school.business;

import java.util.Collection;
import java.util.Map;


import se.idega.idegaweb.commune.accounting.school.data.Provider;

import com.idega.business.IBOService;

/**
 * @author Dainis
 *
 */
public interface ProviderBusiness extends IBOService {
    /**
     * @see se.idega.idegaweb.commune.accounting.school.business.ProviderBusinessBean#saveProvider
     */
    public void saveProvider(String schoolId, String providerStringId,
            String name, String info, String address, String zipCode,
            String zipArea, String phone, String keyCode, String latitude,
            String longitude, String schoolAreaId, String schoolSubAreaId,
            Map schoolTypeMap, String organizationNumber,
            String extraProviderId, String providerTypeId,
            String schoolManagementTypeId, java.sql.Date terminationDate,
            String communeId, String countryId,
            String centralizedAdministration, String invisibleForCitizen,
            String paymentByInvoice, String stateSubsidyGrant, String postgiro,
            String bankgiro, String statisticsType, String ownPosting,
            String doublePosting, boolean useProviderStringId)
            throws ProviderException, java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.accounting.school.business.ProviderBusinessBean#saveProvider
     */
    public void saveProvider(String schoolId, String providerStringId,
            String name, String info, String address, String zipCode,
            String zipArea, String phone, String keyCode, String latitude,
            String longitude, String schoolAreaId, String schoolSubAreaId,
            Map schoolTypeMap, String organizationNumber,
            String extraProviderId, String providerTypeId,
            String schoolManagementTypeId, java.sql.Date terminationDate,
            String communeId, String countryId,
            String centralizedAdministration, String invisibleForCitizen,
            String paymentByInvoice, String stateSubsidyGrant, String postgiro,
            String bankgiro, String statisticsType, String ownPosting,
            String doublePosting, boolean useProviderStringId,
            String sortByBirthdate) throws ProviderException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.accounting.school.business.ProviderBusinessBean#deleteProvider
     */
    public void deleteProvider(String id) throws ProviderException,
            java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.accounting.school.business.ProviderBusinessBean#getProvider
     */
    public Provider getProvider(int schoolId) throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.accounting.school.business.ProviderBusinessBean#findAllSchools
     */
    public Collection findAllSchools() throws java.rmi.RemoteException;

    /**
     * @see se.idega.idegaweb.commune.accounting.school.business.ProviderBusinessBean#findAllSchoolsByOperationalField
     */
    public Collection findAllSchoolsByOperationalField(String operationalField)
            throws java.rmi.RemoteException;

}
