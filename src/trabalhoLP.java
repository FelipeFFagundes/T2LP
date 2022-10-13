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
    ArrayList<String> terminalSimbol = new ArrayList<String>();
    ArrayList<String> nonTerminalSimbol = new ArrayList<String>();
    ArrayList<FirstFollowTable> FFtable = new ArrayList<FirstFollowTable>();

    public void createFirstFollowTable(Grammar [] grammar){
       
        for (int i = 0; i < nonTerminalSimbol.size(); i++) {
            String simbol = nonTerminalSimbol.get(i);
            List<String> follow = createFollow(nonTerminalSimbol.get(i), grammar);
            List<String> first = geraFirst(nonTerminalSimbol.get(i), grammar);
            FFtable.add(new FirstFollowTable(simbol, first, follow));
        }

        System.out.println("\n Tabela First/Follow: \n");
        for (int i = 0; i < FFtable.size(); i++) {
            System.out.println(" " + FFtable.get(i).getSimbol() + " ---> " + Arrays.toString(FFtable.get(i).getFirst()) + " --- " + Arrays.toString(FFtable.get(i).getFollow()));
        }

    }

    private void GLLEditor(String line) {
        
        for (int i = 0; i < line.length(); i++) {
            if(Character.isUpperCase(line.charAt(i)) && nonTerminalSimbol.contains(line.substring(i, i+1))==false && line.charAt(i) != 'E'){
                nonTerminalSimbol.add(line.substring(i, i+1));
            }
            else if (Character.isLowerCase(line.charAt(i)) && terminalSimbol.contains(line.substring(i, i+1))==false){
                terminalSimbol.add(line.substring(i, i+1));
            }
            else if (!Character.isLetter(line.charAt(i)) && line.charAt(i) != '-' && line.charAt(i) != 'E' && terminalSimbol.contains(line.substring(i, i+1))==false){
                terminalSimbol.add(line.substring(i, i+1));
            }
        }        
    }
    
    public  List<String> geraFirst(String simbol, Grammar[] grammar){
        List<String> first = new ArrayList<String>();
        for (int i = 0; i < grammar.length; i++) {

            if(grammar[i].getSimbol().equals(simbol) ){
                if(Character.isLowerCase(grammar[i].getProd().charAt(0))){
                    first.add(grammar[i].getProd().substring(0, 1));
                }
                else if(Character.isUpperCase(grammar[i].getProd().charAt(0))){
                    List<String> aux = geraFirst(Character.toString(grammar[i].getProd().charAt(0)), grammar);
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

    public class Grammar{
        private String simbol;
        private String prod;

        public Grammar(String simbol, String prod){
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

    public List<String> createFollow(String simbol, Grammar[] gramaticas){
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

    public class FirstFollowTable{
        private String simbol;
        private String[] first;
        private String[] follow;

        public FirstFollowTable(String simbol, List<String> first, List<String> follow){
            this.simbol = simbol;
            this.first = first.toArray(new String[first.size()]);
            this.follow = follow.toArray(new String[follow.size()]);
        }
        public String getSimbol(){
            return this.simbol;
        }
        public String[] getFirst(){
            return this.first;
        }
        public String[] getFollow(){
            return this.follow;
        }
        @Override
        public String toString() {
            return "FirstFollowTable:  Símbolo Não terminal: " + simbol + "    First: " + Arrays.toString(first) + "    Follow: " + Arrays.toString(follow) + "";
        }
    }


    public static void main(String args[]) throws IOException {
        System.out.println("<------------------------ ANALISADOR PREDITIVO TABULAR ------------------------>");
        System.out.println("INSTRUÇÕES: Deve ser criado um arquivo chamado 'exemploTeste' no mesmo diretório");
        System.out.println("do programa (nesse caso 'T2LP/src/exemploTeste'). O padrão que deve ser seguido é: Simbolo-producao");
        
        File file = new File("src/exemploTeste");
        trabalhoLP AP = new trabalhoLP();
        List<String> valores = new ArrayList<String>();     


        try{
            Reader reader = new FileReader(file);
            BufferedReader bufferReader = new BufferedReader(reader);
            
    
            String ggl;
        
            while((ggl = bufferReader.readLine()) != null){
        
                valores.add(ggl);            
            
            }                
        
            reader.close();
            bufferReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("O arquivo indicado não foi encontrado!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Erro na leitura do arquivo.");
            e.printStackTrace();
        }

        for (int i = 0; i < valores.size(); i++) {
            AP.GLLEditor(valores.get(i));                
        }
        System.out.println(" Simbolos Não Terminais: " + AP.nonTerminalSimbol);
        System.out.println(" Simbolos Terminais: " + AP.terminalSimbol);

        Grammar [] g = new Grammar[valores.size()]; 
        for (int i = 0; i < valores.size(); i++) {
            g[i] = AP.new Grammar(valores.get(i).substring(0, 1), valores.get(i).substring(2));
        }

        for (int i = 0; i < g.length; i++) {
            System.out.println(g[i]);
        }

        AP.createFirstFollowTable(g);  
      

    }   
   
}

