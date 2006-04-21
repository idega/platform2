package is.idega.idegaweb.member.isi.block.accounting.export.business;

import is.idega.idegaweb.member.isi.block.accounting.export.data.Batch;
import is.idega.idegaweb.member.isi.block.accounting.export.data.Configuration;

public interface CreditCardSendFile {
	public boolean sendFile(Configuration configuration, Batch batch);
}
