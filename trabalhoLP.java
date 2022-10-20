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
    static ArrayList<String> terminalSimbol = new ArrayList<String>();
    static ArrayList<String> nonTerminalSimbol = new ArrayList<String>();
    static ArrayList<FirstFollowTable> FFtable = new ArrayList<FirstFollowTable>();


    //Método para criar a tabela de first e follow
    public static void createFirstFollowTable(Gramatica [] grammar){
       
        for (int i = 0; i < nonTerminalSimbol.size(); i++) {
            String simbol = nonTerminalSimbol.get(i);
            List<String> follow = createFollow(nonTerminalSimbol.get(i), grammar);
            List<String> first = createFirst(nonTerminalSimbol.get(i), grammar);
            FFtable.add(new FirstFollowTable(simbol, first, follow));
        }

        System.out.println("\n Tabela First/Follow: \n");
        for (int i = 0; i < FFtable.size(); i++) {
            System.out.println(" " + FFtable.get(i).getSimbol() + " ---> " + Arrays.toString(FFtable.get(i).getFirst()) + " --- " + Arrays.toString(FFtable.get(i).getFollow()));
        }

    }



    public static void GLLEditor(String line) {//Recebe uma linha do arquivo de teste
        
        for (int i = 0; i < line.length(); i++) {//Percorre cada linha

            if(Character.isUpperCase(line.charAt(i)) && nonTerminalSimbol.contains(line.substring(i, i + 1)) == false && line.charAt(i) != 'E'){
                nonTerminalSimbol.add(line.substring(i, i+1));
            }
            else if (Character.isLowerCase(line.charAt(i)) && terminalSimbol.contains(line.substring(i, i + 1)) == false){
                terminalSimbol.add(line.substring(i, i+1));
            }
            else if (!Character.isLetter(line.charAt(i)) && line.charAt(i) != '-' && line.charAt(i) != 'E' && terminalSimbol.contains(line.substring(i, i+1))==false){
                terminalSimbol.add(line.substring(i, i+1));
            }
        }        
    }
    


    public static List<String> createFirst(String simbol, Gramatica[] grammar){
        List<String> first = new ArrayList<String>();

        for (int i = 0; i < grammar.length; i++) {

            if(grammar[i].getSimbol().equals(simbol) ){
                if(Character.isLowerCase(grammar[i].getProd().charAt(0))){
                    first.add(grammar[i].getProd().substring(0, 1));
                }
                else if(Character.isUpperCase(grammar[i].getProd().charAt(0))){
                    List<String> aux = createFirst(Character.toString(grammar[i].getProd().charAt(0)), grammar);
                    for (int j = 0; j < aux.size(); j++) {
                        first.add(aux.get(j));
                    }
                }
                    if(grammar[i].getProd().equals("E")){
                        first.add("E");
                    }
                }    
        }
        return first;

    }

    public static List<String> createFollow(String simbol, Gramatica[] gramaticas){

        List<String> follow = new ArrayList<String>();

        if (gramaticas[0].getSimbol().equals(simbol)) {
            follow.add("$");
        }

        for (int i = 0; i < gramaticas.length; i++) {

            if(gramaticas[i].getSimbol().equals(simbol) ){
                for(int j = 0; j < gramaticas[i].getProd().length(); j++){
                    try{
                        if(gramaticas[i].getSimbol().equals(String.valueOf(gramaticas[i].getProd().charAt(j))) 
                        && Character.isLowerCase(gramaticas[i].getProd().charAt(j+1))){
                            follow.add(gramaticas[i].getProd().substring(j, j+1));
                        }
                    } catch(java.lang.StringIndexOutOfBoundsException exception){
                    }
                }
            }
        }
        return follow;
    }



    public static void main(String args[]) throws IOException {
        System.out.println("<------------------------ ANALISADOR PREDITIVO TABULAR ------------------------>");
        System.out.println("Para a funcionalidade do programa a Linguagem deve ser inserida dentro do arquivo 'exemploTeste'");
        
        File file = new File("T2LP/exemploTeste");

        List<String> valores = new ArrayList<String>();
        Reader reader = new FileReader(file);
        BufferedReader bufferReader = new BufferedReader(reader);


        //Le o arquivo de texto
        try{
            String leitor = bufferReader.readLine();
        
            while((leitor = bufferReader.readLine()) != null){
                valores.add(leitor);            
            }                
        
        //Se não encontrar o arquivo aponta erro
        } catch (FileNotFoundException e) { System.out.println("File not found"); }

        for (int i = 0; i < valores.size(); i++) {
            GLLEditor(valores.get(i));                
        }

        System.out.println(" Simbolos Não Terminais: " + nonTerminalSimbol);
        System.out.println(" Simbolos Terminais: " + terminalSimbol);

        Gramatica [] gramatica = new Gramatica[valores.size()]; 

        for (int i = 0; i < valores.size(); i++) {
            gramatica[i] = new Gramatica(valores.get(i).substring(0, 1), valores.get(i).substring(2));
        }

        //Printa cada objeto da gramatica
        //Exemplo A é um objeto da gramatica que produz bB
        for (int i = 0; i < gramatica.length; i++) {
            System.out.println(gramatica[i]);
        }






        createFirstFollowTable(gramatica);  
      

        reader.close();
        bufferReader.close();
    }   
   
}

