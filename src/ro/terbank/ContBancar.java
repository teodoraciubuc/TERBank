package ro.terbank;

/**
 * Reprezintă un cont bancar cu număr, titular și sold disponibil.
 */
public class ContBancar {

    private final String numarCont;
    private String titular;
    private double sold;

    /**
     * Creează un cont bancar cu numărul, titularul și soldul inițial specificate.
     *
     * @param numarCont identificatorul contului; nu poate fi null sau gol
     * @param titular numele titularului; nu poate fi null sau gol
     * @param sold soldul inițial; trebuie să fie finit și nenegativ
     * @throws IllegalArgumentException dacă unul dintre argumente este invalid
     */
    public ContBancar(String numarCont, String titular, double sold) {
        this.numarCont = valideazaText(numarCont, "Numărul contului");
        this.titular = valideazaText(titular, "Titularul");

        if (!Double.isFinite(sold) || sold < 0) {
            throw new IllegalArgumentException("Soldul inițial trebuie să fie finit și nenegativ.");
        }

        this.sold = sold;
    }

    /**
     * Creează un cont bancar cu soldul inițial zero.
     *
     * @param numarCont identificatorul contului; nu poate fi null sau gol
     * @param titular numele titularului; nu poate fi null sau gol
     * @throws IllegalArgumentException dacă numărul contului sau titularul este invalid
     */
    public ContBancar(String numarCont, String titular) {
        this(numarCont, titular, 0);
    }

    /**
     * Returnează numărul contului.
     *
     * @return numărul contului
     */
    public String getNumarCont() {
        return numarCont;
    }

    /**
     * Returnează numele titularului.
     *
     * @return numele titularului
     */
    public String getTitular() {
        return titular;
    }

    /**
     * Returnează soldul contului.
     *
     * @return soldul disponibil
     */
    public double getSold() {
        return sold;
    }

    /**
     * Modifică numele titularului.
     *
     * @param titular noul nume al titularului; nu poate fi null sau gol
     * @throws IllegalArgumentException dacă numele este null sau gol
     */
    public void setTitular(String titular) {
        this.titular = valideazaText(titular, "Titularul");
    }

    /**
     * Returnează o reprezentare textuală a contului.
     *
     * @return informațiile despre cont
     */
    @Override
    public String toString() {
        return "ContBancar{numarCont='" + numarCont + "', titular='" + titular + "', sold=" + sold + '}';
    }

    private static String valideazaText(String valoare, String denumireCamp) {
        if (valoare == null || valoare.isBlank()) {
            throw new IllegalArgumentException(denumireCamp + " nu poate fi null sau gol.");
        }
        return valoare;
    }
}
