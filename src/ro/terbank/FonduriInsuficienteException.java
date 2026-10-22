package ro.terbank;

/** Semnalează că soldul nu acoperă suma solicitată. */
public class FonduriInsuficienteException extends Exception {

    /** Creează excepția cu numărul contului. */
    public FonduriInsuficienteException(String numarCont) {
        super("Fonduri insuficiente în contul " + numarCont + ".");
    }
}
