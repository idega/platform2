/*
 * $Id: PrintDocumentsHomeImpl.java 1.1 6.10.2004 aron Exp $
 * Created on 6.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.printing.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;
import com.idega.util.IWTimestamp;

/**
 * 
 *  Last modified: $Date: 6.10.2004 20:53:48 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class PrintDocumentsHomeImpl extends IDOFactory implements
        PrintDocumentsHome {
    protected Class getEntityInterfaceClass() {
        return PrintDocuments.class;
    }

    public PrintDocuments create() throws javax.ejb.CreateException {
        return (PrintDocuments) super.createIDO();
    }

    public PrintDocuments findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException {
        return (PrintDocuments) super.findByPrimaryKeyIDO(pk);
    }

    public Collection findAllPrintedLetterDocuments() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintDocumentsBMPBean) entity)
                .ejbFindAllPrintedLetterDocuments();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllDocumentByType(String type) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintDocumentsBMPBean) entity)
                .ejbFindAllDocumentByType(type);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllDocumentByType(String type, IWTimestamp from,
            IWTimestamp to) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintDocumentsBMPBean) entity)
                .ejbFindAllDocumentByType(type, from, to);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllDocumentByType(String type, int resultSize,
            int startingIndex) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintDocumentsBMPBean) entity)
                .ejbFindAllDocumentByType(type, resultSize, startingIndex);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllDocumentByType(String type, IWTimestamp from,
            IWTimestamp to, int resultSize, int startingIndex)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PrintDocumentsBMPBean) entity)
                .ejbFindAllDocumentByType(type, from, to, resultSize,
                        startingIndex);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

}
