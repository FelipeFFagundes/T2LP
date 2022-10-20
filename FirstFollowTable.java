package T2LP;
import java.util.Arrays;
import java.util.List;

public class FirstFollowTable{
    private String simbolo;
    private String[] first;
    private String[] follow;        

    public FirstFollowTable(String simbolo, List<String> first, List<String> follow){
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