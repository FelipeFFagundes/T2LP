package T2LP;
import java.util.Arrays;
import java.util.List;

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