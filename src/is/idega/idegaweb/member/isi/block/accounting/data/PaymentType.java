/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.data;



import com.idega.data.IDOEntity;

/**
 * @author palli
 */
public interface PaymentType extends IDOEntity {
    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeBMPBean#setName
     */
    public void setName(String name);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeBMPBean#getName
     */
    public String getName();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeBMPBean#setLocalizationKey
     */
    public void setLocalizationKey(String key);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeBMPBean#getLocalizationKey
     */
    public String getLocalizationKey();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeBMPBean#setDeleted
     */
    public void setDeleted(boolean deleted);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeBMPBean#getDeleted
     */
    public boolean getDeleted();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeBMPBean#setPlugin
     */
    public void setPlugin(String plugin);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeBMPBean#getPlugin
     */
    public String getPlugin();

}
