/**
 * KrofurWSSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package is.idega.block.finance.business.isb.ws;

public interface KrofurWSSoap extends java.rmi.Remote {

    // Stofnar kr�fu � innheimtukerfi �slandsbanka.
    public void stofnaKrofu(is.idega.block.finance.business.isb.ws.Krafa krafa) throws java.rmi.RemoteException;

    // Endurvekur kr�fu sem hefur veri? felld ni?ur og breytir � samr�mi
    // vi? kr�funa sem er send inn.
    public void endurvekjaKrofu(is.idega.block.finance.business.isb.ws.Krafa krafa) throws java.rmi.RemoteException;

    // Fellir kr�fu � innheimtukerfi �slandsbanka.
    public void fellaKrofu(is.idega.block.finance.business.isb.ws.Krafa krafa) throws java.rmi.RemoteException;

    // Breytir kr�fu � innheimtukerfi �slandsbanka.
    public void breytaKrofu(is.idega.block.finance.business.isb.ws.Krafa krafa) throws java.rmi.RemoteException;

    // Stofnar kr�fur sem kr�fubunka � innheimtukerfi �slandsbanka.
    public java.math.BigDecimal stofnaKrofubunka(is.idega.block.finance.business.isb.ws.ArrayOfKrafa krofur) throws java.rmi.RemoteException;

    // S�kir sv�r vi? kr�fubunka sem stofna?ur hefur veri? � innheimtukerfi
    // �slandsbanka.
    public is.idega.block.finance.business.isb.ws.ArrayOfUppreiknudKrafa saekjaKrofubunkasvar(java.math.BigDecimal bunkanumer) throws java.rmi.RemoteException;

    // S�kir grei?sluuppl?singar � innheimtukerfi �slandsbanka. S�tt er fr�
    // og me? upphafsdagsetningu og til og me? lokadagsetningu.
    public is.idega.block.finance.business.isb.ws.ArrayOfGreidsla saekjaGreidslurKrafnaTimabil(java.lang.String kennitalaKrofuhafa, java.lang.String audkenni, java.util.Calendar dagsetningFra, java.util.Calendar dagsetningTil, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException;

    // S�kir grei?sluuppl?singar � innheimtukerfi �slandsbanka. Einungis
    // koma ?�r grei?slur sem hafa borist fr� ?v� grei?slur voru s�ttar s�?ast
    // me? ?essari a?ger?.
    public is.idega.block.finance.business.isb.ws.ArrayOfGreidsla saekjaGreidslurKrafna(java.lang.String kennitalaKrofuhafa, java.lang.String audkenni, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException;

    // S�kir grei?sluuppl?singar � innheimtukerfi �slandsbanka.
    public is.idega.block.finance.business.isb.ws.ArrayOfGreidsla saekjaGreidsluKrofu(java.lang.String kennitalaKrofuhafa, int banki, int hofudbok, int krofunumer, java.util.Calendar gjalddagi) throws java.rmi.RemoteException;

    // S�kir kr�fuuppl?singar � innheimtukerfi �slandsbanka.
    public is.idega.block.finance.business.isb.ws.ArrayOfUppreiknudKrafa saekjaKrofur(java.lang.String kennitalaKrofuhafa, java.lang.String audkenni, java.util.Calendar gjalddagiFra, java.util.Calendar gjalddagiTil, is.idega.block.finance.business.isb.ws.AstandKrofu astand, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException;

    // S�kir kr�fuuppl?singar � innheimtukerfi �slandsbanka.
    public is.idega.block.finance.business.isb.ws.UppreiknudKrafa saekjaKrofu(java.lang.String kennitalaKrofuhafa, int banki, int hofudbok, int krofunumer, java.util.Calendar gjalddagi) throws java.rmi.RemoteException;

    // Stofna kr�fuskr� � innheimtukerfi �slandsbanka. Skr�in er send inn
    // sem DIME attachment. Skilar bunkan�meri skr�arinnar sem er nota? til
    // a? s�kja svarskeyti vi? stofnuninni.
    public java.math.BigDecimal stofnaKrofuskra(is.idega.block.finance.business.isb.ws.TegundKrofuskra tegund) throws java.rmi.RemoteException;

    // S�kir svarskr� vi? innsendri kr�fuskr� me? gefnu bunkan�meri. Skr�in
    // er send til baka sem DIME attachment.
    public void saekjaKrofuskrasvar(is.idega.block.finance.business.isb.ws.TegundKrofuskra tegund, java.math.BigDecimal bunkanumer) throws java.rmi.RemoteException;

    // S�kir grei?sluuppl?singar � kr�fum. Fyrirspurnin er send inn sem DIME
    // attachment. Grei?sluuppl?singarnar eru sendar til baka sem DIME attachment.
    public void saekjaKrofuupplysingaskra(is.idega.block.finance.business.isb.ws.TegundKrofufyrirspurnar tegundFyrirspurnar) throws java.rmi.RemoteException;

    // S�kir allar beingrei?slubei?nir kr�fuhafa, �h�? textalykli,  � innheimtukerfi
    // �slandsbanka.
    public is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni saekjaAllarBeingreidslubeidnir(java.lang.String kennitalaKrofuhafa, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException;

    // S�kir beingrei?slubei?nir kr�fuhafa fyrir �kve?inn textalykil � innheimtukerfi
    // �slandsbanka.
    public is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni saekjaBeingreidslubeidnir(java.lang.String kennitalaKrofuhafa, java.lang.String textalykill, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException;

    // S�kir hreyfingar � beingrei?slubei?num kr�fuhafa fyrir �kve?inn textalykil
    // � innheimtukerfi �slandsbanka (n?jar og ni?urfelldar beingrei?slubei?nir
    // fr� ?v� kr�fuhafi s�tti bei?nirnar s�?ast)
    public is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni saekjaBeingreidslubeidnaHreyfingar(java.lang.String kennitalaKrofuhafa, java.lang.String textalykill, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException;

    // S�kir hreyfingar � beingrei?slubei?num kr�fuhafa fyrir alla textalykla
    // � innheimtukerfi �slandsbanka (n?jar og ni?urfelldar beingrei?slubei?nir
    // fr� ?v� kr�fuhafi s�tti bei?nirnar s�?ast)
    public is.idega.block.finance.business.isb.ws.ArrayOfBeingreidslubeidni saekjaAllarBeingreidslubeidnaHreyfingar(java.lang.String kennitalaKrofuhafa, int faerslaFra, int faerslaTil) throws java.rmi.RemoteException;
}
