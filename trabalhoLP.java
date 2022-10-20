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
    static ArrayList<String> terminalSymbol = new ArrayList<>();
    static ArrayList<String> nonTerminalSymbol = new ArrayList<>();
    static ArrayList<FirstFollowTable> tabelaFirstFollow = new ArrayList<>();

    private static void GLLEditor(String linha) {

        for (int i = 0; i < linha.length(); i++) {
            if(Character.isUpperCase(linha.charAt(i)) && nonTerminalSymbol.contains(linha.substring(i, i+1))==false && linha.charAt(i) != 'E'){
                nonTerminalSymbol.add(linha.substring(i, i+1));
            }
            else if (Character.isLowerCase(linha.charAt(i)) && terminalSymbol.contains(linha.substring(i, i+1))==false){
                terminalSymbol.add(linha.substring(i, i+1));
            }
            else if (!Character.isLetter(linha.charAt(i)) && linha.charAt(i) != '-' && linha.charAt(i) != 'E' && terminalSymbol.contains(linha.substring(i, i+1))==false){
                 terminalSymbol.add(linha.substring(i, i+1));
            }                
        }            
    }

    public static void generateFirstFollowTable(Gramaticas [] gramatica){       
        for (int i = 0; i < nonTerminalSymbol.size(); i++) {
            String simbolo = nonTerminalSymbol.get(i);
            List<String> first = generateFirst(nonTerminalSymbol.get(i), gramatica);
            List<String> follow = generateFollow(nonTerminalSymbol.get(i), gramatica);


            tabelaFirstFollow.add(new FirstFollowTable(simbolo, first, follow));
        }
        System.out.println("========================================================");
        System.out.println("|   SIMBOLO    |      FIRST         FOLLOW    ");

        for (int i = 0; i < tabelaFirstFollow.size(); i++) {
            System.out.println("|      "+tabelaFirstFollow.get(i).getSimbolo() + "       |     " 
            + Arrays.toString(tabelaFirstFollow.get(i).getFirst()) + "          " 
            + Arrays.toString(tabelaFirstFollow.get(i).getFollow()));
        }
    }




    public static  List<String> generateFirst(String simbolo, Gramaticas[] gramatica){
        List<String> first = new ArrayList<String>();
        for (int i = 0; i < gramatica.length; i++) {
            if(gramatica[i].getSimbolo().equals(simbolo) ){


                if(Character.isLowerCase(gramatica[i].getProducao().charAt(0))){
                    first.add(gramatica[i].getProducao().substring(0, 1));
                }


                else if(Character.isUpperCase(gramatica[i].getProducao().charAt(0))){
                    List<String> aux = generateFirst(Character.toString(gramatica[i].getProducao().charAt(0)), gramatica);
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

    public static List<String> generateFollow(String simbolo, Gramaticas[] gramatica){
        List<String> follow = new ArrayList<String>();
        if (gramatica[0].getSimbolo().equals(simbolo)) {
            follow.add("$");
        }
        for (int i = 0; i < gramatica.length; i++) {
            for (int j = 0; j < gramatica[i].getProducao().length(); j++) {
                if (gramatica[i].getProducao().charAt(j) == simbolo.charAt(0)) { 
                    if (j == gramatica[i].getProducao().length()-1) {
                        List<String> aux = generateFollow(gramatica[i].getSimbolo(), gramatica);

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
                                List<String> aux = generateFirst(Character.toString(gramatica[i].getProducao().charAt(j+1)), gramatica);
                                for (int k = 0; k < aux.size(); k++) {
                                    if (!aux.get(k).equals("E")) {
                                        if(follow.contains(aux.get(k)) == false){
                                            follow.add(aux.get(k));
                                        }
                                    }
                                }
                                if (aux.contains("E")) {
                                    if (!gramatica[i].getSimbolo().equals(simbolo)) {
                                        List<String> aux2 = generateFollow(gramatica[i].getSimbolo(), gramatica);
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
        System.out.println("========================================================");
        System.out.println("|Favor escrever a linguagem no arquivo exemploTeste.txt|");
        System.out.println("========================================================");


        File arquivo = new File("T2LP/exemploTeste.txt");
        List<String> linha = new ArrayList<String>();    

        try{
            Reader reader = new FileReader(arquivo);
		    BufferedReader bufferReader = new BufferedReader(reader);
		    String leitor;
		
		    while((leitor = bufferReader.readLine()) != null){		
			    linha.add(leitor);						
		    }				
		
		    reader.close();
		    bufferReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("404");
        }
        

        for (int i = 0; i < linha.size(); i++) {
            GLLEditor(linha.get(i));                
        }

        

        Gramaticas [] g = new Gramaticas[linha.size()]; 


        for (int i = 0; i < linha.size(); i++) {
            g[i] = new Gramaticas(linha.get(i).substring(0, 1), linha.get(i).substring(2));
        }

        System.out.println("|Produções:");
        for (int i = 0; i < g.length; i++) {
            System.out.println("|" + g[i]);
        }
        System.out.println("========================================================");


        System.out.println("|Simbolos Não Terminais: \n|    " + nonTerminalSymbol);
        System.out.println("|Simbolos Terminais: \n|    " + terminalSymbol);
        System.out.println("========================================================");


        generateFirstFollowTable(g);


        
        System.out.println("=====================FIM DO PROGRAMA====================\n");

    }      
}