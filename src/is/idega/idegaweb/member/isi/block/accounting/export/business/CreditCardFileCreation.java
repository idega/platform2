package is.idega.idegaweb.member.isi.block.accounting.export.business;

import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContract;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public interface CreditCardFileCreation {
	public File createFile(CreditCardContract contract, Collection entries) throws IOException;
}
