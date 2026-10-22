package ro.terbank;

import java.time.LocalDateTime;
import java.util.Locale;

/** Reprezintă o tranzacție bancară înregistrată. */
public final class Tranzactie {

    private final String idTranzactie;
    private final TipTranzactie tip;
    private final double suma;
    private final LocalDateTime dataOra;
    private final String contSursa;
    private final String contDestinatie;

    /** Creează o tranzacție și verifică datele primite. */
    public Tranzactie(String idTranzactie, TipTranzactie tip, double suma,
                      LocalDateTime dataOra, String contSursa, String contDestinatie) {
        this.idTranzactie = ContBancar.valideazaText(idTranzactie, "ID-ul tranzacției");

        if (tip == null) {
            throw new IllegalArgumentException("Tipul tranzacției nu poate fi null.");
        }
        if (Double.isNaN(suma) || Double.isInfinite(suma) || suma <= 0) {
            throw new IllegalArgumentException("Suma trebuie să fie pozitivă și finită.");
        }
        if (dataOra == null) {
            throw new IllegalArgumentException("Data și ora nu pot fi null.");
        }

        String sursaValida = null;
        if (contSursa != null) {
            sursaValida = ContBancar.valideazaNumarCont(contSursa);
        }

        String destinatieValida = null;
        if (contDestinatie != null) {
            destinatieValida = ContBancar.valideazaNumarCont(contDestinatie);
        }

        if (tip == TipTranzactie.DEPUNERE) {
            if (sursaValida != null || destinatieValida == null) {
                throw new IllegalArgumentException("Depunerea trebuie să aibă doar un cont destinație.");
            }
        } else if (tip == TipTranzactie.RETRAGERE) {
            if (sursaValida == null || destinatieValida != null) {
                throw new IllegalArgumentException("Retragerea trebuie să aibă doar un cont sursă.");
            }
        } else {
            if (sursaValida == null || destinatieValida == null) {
                throw new IllegalArgumentException("Transferul trebuie să aibă cont sursă și destinație.");
            }
            if (sursaValida.equals(destinatieValida)) {
                throw new IllegalArgumentException("Conturile trebuie să fie diferite.");
            }
        }

        this.tip = tip;
        this.suma = suma;
        this.dataOra = dataOra;
        this.contSursa = sursaValida;
        this.contDestinatie = destinatieValida;
    }

    /** Returnează ID-ul tranzacției. */
    public String getIdTranzactie() {
        return idTranzactie;
    }

    /** Returnează tipul tranzacției. */
    public TipTranzactie getTip() {
        return tip;
    }

    /** Returnează suma tranzacției. */
    public double getSuma() {
        return suma;
    }

    /** Returnează data și ora tranzacției. */
    public LocalDateTime getDataOra() {
        return dataOra;
    }

    /** Returnează contul sursă sau null pentru o depunere. */
    public String getContSursa() {
        return contSursa;
    }

    /** Returnează contul destinație sau null pentru o retragere. */
    public String getContDestinatie() {
        return contDestinatie;
    }

    /** Returnează informațiile tranzacției. */
    @Override
    public String toString() {
        String sursaAfisata = "-";
        if (contSursa != null) {
            sursaAfisata = contSursa;
        }

        String destinatieAfisata = "-";
        if (contDestinatie != null) {
            destinatieAfisata = contDestinatie;
        }

        String sumaAfisata = String.format(Locale.US, "%.2f", suma);
        return tip + " | " + sumaAfisata + " | " + dataOra
                + " | Sursă: " + sursaAfisata
                + " | Destinație: " + destinatieAfisata
                + " | ID: " + idTranzactie;
    }
}
