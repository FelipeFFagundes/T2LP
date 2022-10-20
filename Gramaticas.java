package T2LP;
public class Gramaticas{
    private String simbol;
    private String prod;

    public Gramaticas(String simbol, String prod){
        this.simbol = simbol;
        this.prod = prod;
    }

    public String getSimbol(){
        return this.simbol;
    }
    public String getProd(){
        return this.prod;
    }

    @Override
    public String toString() {
        return "  Gramática --> [ " + simbol + " -> " + prod + "]";
    }
}