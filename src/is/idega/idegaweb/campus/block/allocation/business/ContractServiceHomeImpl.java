/*
 * Created on 1.9.2004
 *
 * Copyright (C) 2004 Idega hf. All Rights Reserved.
 *
 *  This software is the proprietary information of Idega hf.
 *  Use is subject to license terms.
 */
package is.idega.idegaweb.campus.block.allocation.business;




import com.idega.business.IBOHomeImpl;

/**
 * @author aron
 *
 * ContractServiceHomeImpl TODO Describe this type
 */
public class ContractServiceHomeImpl extends IBOHomeImpl implements
        ContractServiceHome {
    protected Class getBeanInterfaceClass() {
        return ContractService.class;
    }

    public ContractService create() throws javax.ejb.CreateException {
        return (ContractService) super.createIBO();
    }

}
