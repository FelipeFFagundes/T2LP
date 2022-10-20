package T2LP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class trabalhoLP {  
    ArrayList<String> simbolosTerminais = new ArrayList();
    ArrayList<String> simbolosNaoTerminais = new ArrayList();
    ArrayList<TabelaFirstFollow> tabelaFF = new ArrayList();

    public class Gramatica{
        private String simbolo;
        private String producao;

        public Gramatica(String simbolo, String producao){
            this.simbolo = simbolo;
            this.producao = producao;
        }

        public String getSimbolo(){
            return this.simbolo;
        }

        public String getProducao(){
            return this.producao;
        }

        @Override
        public String toString() {
            return simbolo + " -> " + producao;
        }
    }



    
    public class TabelaFirstFollow{
        private String simbolo;
        private String[] first;
        private String[] follow;        

        public TabelaFirstFollow(String simbolo, List<String> first, List<String> follow){
            this.simbolo = simbolo;
            this.first = first.toArray(new String[first.size()]);
            this.follow = follow.toArray(new String[follow.size()]);
        }

        public String getSimbolo(){
            return this.simbolo;
        }

        public String[] getFirst(){
            return this.first;
        }

        public String[] getFollow(){
            return this.follow;
        }

        @Override
        public String toString() {
            return "TabelaFirstFollow:  Símbolo Não terminal: " + simbolo + "    First: " + Arrays.toString(first) + "    Follow: " + Arrays.toString(follow) + "";
        }
    }





    private void manipulaGLL(String linha) {


        for (int i = 0; i < linha.length(); i++) {
            if(Character.isUpperCase(linha.charAt(i)) && simbolosNaoTerminais.contains(linha.substring(i, i+1))==false && linha.charAt(i) != 'E'){
                simbolosNaoTerminais.add(linha.substring(i, i+1));
            }
            else if (Character.isLowerCase(linha.charAt(i)) && simbolosTerminais.contains(linha.substring(i, i+1))==false){
                simbolosTerminais.add(linha.substring(i, i+1));
            }
            else if (!Character.isLetter(linha.charAt(i)) && linha.charAt(i) != '-' && linha.charAt(i) != 'E' && simbolosTerminais.contains(linha.substring(i, i+1))==false){
                 simbolosTerminais.add(linha.substring(i, i+1));
            }                
        }            
    }

    





    public void geraTabelaFirstFollow(Gramatica [] gramatica){       
        for (int i = 0; i < simbolosNaoTerminais.size(); i++) {
            String simbolo = simbolosNaoTerminais.get(i);
            List<String> first = geraFirst(simbolosNaoTerminais.get(i), gramatica);
            List<String> follow = geraFollow(simbolosNaoTerminais.get(i), gramatica);

            tabelaFF.add(new TabelaFirstFollow(simbolo, first, follow));
        }

        System.out.println("\nTabela First Follow: \n");
        for (int i = 0; i < tabelaFF.size(); i++) {
            System.out.println(tabelaFF.get(i).getSimbolo() + " - " + Arrays.toString(tabelaFF.get(i).getFirst()) + " - " + Arrays.toString(tabelaFF.get(i).getFollow()));
        }
    }




    public  List<String> geraFirst(String simbolo, Gramatica[] gramatica){
        List<String> first = new ArrayList<String>();
        for (int i = 0; i < gramatica.length; i++) {
            if(gramatica[i].getSimbolo().equals(simbolo) ){
                if(Character.isLowerCase(gramatica[i].getProducao().charAt(0))){
                    first.add(gramatica[i].getProducao().substring(0, 1));
                }
                else if(Character.isUpperCase(gramatica[i].getProducao().charAt(0))){
                    List<String> aux = geraFirst(Character.toString(gramatica[i].getProducao().charAt(0)), gramatica);
                    for (int j = 0; j < aux.size(); j++) {
                        first.add(aux.get(j));
                    }
                }
                    if(gramatica[i].getProducao().equals("E")){
                        first.add("E");
                    }
                }    
        }
        return first;
    }

    public List<String> geraFollow(String simbolo, Gramatica[] gramatica){
        List<String> follow = new ArrayList<String>();
        if (gramatica[0].getSimbolo().equals(simbolo)) {
            follow.add("$");
        }
        for (int i = 0; i < gramatica.length; i++) {
            for (int j = 0; j < gramatica[i].getProducao().length(); j++) {
                if (gramatica[i].getProducao().charAt(j) == simbolo.charAt(0)) { 
                    if (j == gramatica[i].getProducao().length()-1) {
                        List<String> aux = geraFollow(gramatica[i].getSimbolo(), gramatica);

                        for (int k = 0; k < aux.size(); k++) {
                            if(follow.contains(aux.get(k)) == false){
                                follow.add(aux.get(k));
                            }
                        }
                    }
                    else{
                        if (Character.isLowerCase(gramatica[i].getProducao().charAt(j+1)) || !Character.isLetter(gramatica[i].getProducao().charAt(j+1)) ) {

                            if(follow.contains(Character.toString(gramatica[i].getProducao().charAt(j+1))) == false){
                                follow.add(Character.toString(gramatica[i].getProducao().charAt(j+1)));
                            }
                        }

                        else{
                            if (Character.isUpperCase(gramatica[i].getProducao().charAt(j+1))) {
                                List<String> aux = geraFirst(Character.toString(gramatica[i].getProducao().charAt(j+1)), gramatica);
                                for (int k = 0; k < aux.size(); k++) {
                                    if (!aux.get(k).equals("E")) {
                                        if(follow.contains(aux.get(k)) == false){
                                            follow.add(aux.get(k));
                                        }
                                    }
                                }
                                if (aux.contains("E")) {
                                    if (!gramatica[i].getSimbolo().equals(simbolo)) {
                                        List<String> aux2 = geraFollow(gramatica[i].getSimbolo(), gramatica);
                                        for (int k = 0; k < aux2.size(); k++) {
                                            if(follow.contains(aux2.get(k)) == false){
                                                follow.add(aux2.get(k));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }            
        }
        return follow;    
    }

    


    public static void main(String args[]) throws IOException {
        System.out.println("\n======================================================");
        System.out.println("Favor escrever a linguagem no arquivo exemploTeste.txt");

        File arquivo = new File("exemploTeste.txt");
        trabalhoLP analisadorPreditivo = new trabalhoLP();
        List<String> valores = new ArrayList<String>();    

        try{
            Reader reader = new FileReader(arquivo);
		    BufferedReader bufferReader = new BufferedReader(reader);
		    String ggl;
		
		    while((ggl = bufferReader.readLine()) != null){		
			    valores.add(ggl);						
		    }				
		
		    reader.close();
		    bufferReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo");
            e.printStackTrace();
        }
        

        for (int i = 0; i < valores.size(); i++) {
            analisadorPreditivo.manipulaGLL(valores.get(i));                
        }
        System.out.println("Simbolos Terminais: " + analisadorPreditivo.simbolosTerminais+"\n");
        System.out.println("Simbolos Não Terminais: " + analisadorPreditivo.simbolosNaoTerminais+"\n");


        Gramatica [] g = new Gramatica[valores.size()]; 
        for (int i = 0; i < valores.size(); i++) {
            g[i] = analisadorPreditivo.new Gramatica(valores.get(i).substring(0, 1), valores.get(i).substring(2));
        }

        System.out.println("\nGramática:\n");
        for (int i = 0; i < g.length; i++) {
            System.out.println(g[i]);
        }

        analisadorPreditivo.geraTabelaFirstFollow(g);
        System.out.println();

    }      
}