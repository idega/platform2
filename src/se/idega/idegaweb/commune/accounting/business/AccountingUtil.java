package se.idega.idegaweb.commune.accounting.business;


public class AccountingUtil {
    public static long roundAmount (float f) {
        final long round = Math.round (f);
        return round - (0.5f == f - Math.floor (f) ? (round % 2) : 0);
    }
}
