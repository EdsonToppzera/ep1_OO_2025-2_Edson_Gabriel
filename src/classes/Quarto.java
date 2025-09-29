package classes;

public class Quarto {
    private int numero;
    private boolean livre;

    public Quarto(int numero) {
        this.numero = numero;
        this.livre = true;
    }

    public boolean ocuparQuarto() {
        if (this.livre) {
            this.livre = false;
            return true;
        } else {
            System.out.println("O quarto " + this.numero + " esta ocupado.");
            return false;
        }
    }

    public void liberarQuarto() {
        this.livre = true;
    }

    public int getNumero() {
        return numero;
    }

    public boolean taLivre() {
        return livre;
    }

    public String log() {
        return "Quarto: [numero: "+numero+"; livre: "+livre+"]";
    }
}
