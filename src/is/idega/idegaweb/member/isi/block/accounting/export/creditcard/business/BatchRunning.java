/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.export.creditcard.business;

/**
 * @author palli
 */
public class BatchRunning {
    private static boolean sendFilesBatchRunning = false;
    private static boolean getFilesBatchRunning = false;
    
    public static boolean reserveSendFileBatch() {
        if (!sendFilesBatchRunning) {
            sendFilesBatchRunning = true;
            System.out.println("Reserving sendFiles batch");
            return true;
        } else {
            System.out.println("Can't resever sendFiles batch, already running");
            return false;
        }
    }
    
    public static void releaseSendFilesBatch() {
        sendFilesBatchRunning = false;
        
        System.out.println("Releasing sendFiles batch");
    }
    
    public static boolean reserveGetFileBatch() {
        if (!getFilesBatchRunning) {
            getFilesBatchRunning = true;
            System.out.println("Reserving getFiles batch");
            return true;
        } else {
            System.out.println("Can't resever getFiles batch, already running");
            return false;
        }
    }
    
    public static void releaseGetFilesBatch() {
        getFilesBatchRunning = false;
        
        System.out.println("Releasing getFiles batch");
    }
}