package T2LP;

public class Gramaticas {
    public String simbolo;
    public String producao;

    public Gramaticas(String simbolo, String producao){
        this.simbolo = simbolo;
        this.producao = producao;
    }

    public String getSimbolo() {
        return this.simbolo;
    }

    public String getProducao() {
        return this.producao;
    }

    @Override
    public String toString() {
        return "    " + simbolo + " -> " + producao;
    }
}
