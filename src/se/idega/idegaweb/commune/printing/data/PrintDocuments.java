/*
 * $Id: PrintDocuments.java 1.1 6.10.2004 aron Exp $
 * Created on 6.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.printing.data;

import java.sql.Timestamp;


import com.idega.core.file.data.ICFile;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;

/**
 * 
 *  Last modified: $Date: 6.10.2004 20:53:46 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public interface PrintDocuments extends IDOEntity {
    /**
     * @see se.idega.idegaweb.commune.printing.data.PrintDocumentsBMPBean#setNumberOfSubDocuments
     */
    public void setNumberOfSubDocuments(int number);

    /**
     * @see se.idega.idegaweb.commune.printing.data.PrintDocumentsBMPBean#getNumberOfSubDocuments
     */
    public int getNumberOfSubDocuments();

    /**
     * @see se.idega.idegaweb.commune.printing.data.PrintDocumentsBMPBean#getType
     */
    public String getType();

    /**
     * @see se.idega.idegaweb.commune.printing.data.PrintDocumentsBMPBean#setType
     */
    public void setType(String type);

    /**
     * @see se.idega.idegaweb.commune.printing.data.PrintDocumentsBMPBean#setAsPrintedLetter
     */
    public void setAsPrintedLetter();

    /**
     * @see se.idega.idegaweb.commune.printing.data.PrintDocumentsBMPBean#getDocument
     */
    public ICFile getDocument();

    /**
     * @see se.idega.idegaweb.commune.printing.data.PrintDocumentsBMPBean#getDocumentFileID
     */
    public int getDocumentFileID();

    /**
     * @see se.idega.idegaweb.commune.printing.data.PrintDocumentsBMPBean#setDocument
     */
    public void setDocument(ICFile file);

    /**
     * @see se.idega.idegaweb.commune.printing.data.PrintDocumentsBMPBean#setDocument
     */
    public void setDocument(int fileID);

    /**
     * @see se.idega.idegaweb.commune.printing.data.PrintDocumentsBMPBean#setCreated
     */
    public void setCreated(Timestamp created);

    /**
     * @see se.idega.idegaweb.commune.printing.data.PrintDocumentsBMPBean#getCreated
     */
    public Timestamp getCreated();

    /**
     * @see se.idega.idegaweb.commune.printing.data.PrintDocumentsBMPBean#getCreator
     */
    public User getCreator();

    /**
     * @see se.idega.idegaweb.commune.printing.data.PrintDocumentsBMPBean#getCreatorUserID
     */
    public int getCreatorUserID();

    /**
     * @see se.idega.idegaweb.commune.printing.data.PrintDocumentsBMPBean#isIfPrinted
     */
    public boolean isIfPrinted();

    /**
     * @see se.idega.idegaweb.commune.printing.data.PrintDocumentsBMPBean#getLastPrinted
     */
    public Timestamp getLastPrinted();

    /**
     * @see se.idega.idegaweb.commune.printing.data.PrintDocumentsBMPBean#setCreator
     */
    public void setCreator(User creator);

    /**
     * @see se.idega.idegaweb.commune.printing.data.PrintDocumentsBMPBean#setCreator
     */
    public void setCreator(int creatorUserID);

    /**
     * @see se.idega.idegaweb.commune.printing.data.PrintDocumentsBMPBean#setIfPrinted
     */
    public void setIfPrinted(boolean ifPrinted);

    /**
     * @see se.idega.idegaweb.commune.printing.data.PrintDocumentsBMPBean#setLastPrinted
     */
    public void setLastPrinted(Timestamp lastPrinted);

}
