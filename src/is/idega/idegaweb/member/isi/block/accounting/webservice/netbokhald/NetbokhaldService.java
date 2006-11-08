package is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald;

import java.util.Date;

public interface NetbokhaldService {
	public NetbokhaldEntry[] getEntries(String companyNumber, Date fromStamp);
}
