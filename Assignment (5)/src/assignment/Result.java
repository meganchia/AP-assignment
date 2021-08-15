package assignment;

import java.util.ArrayList;
import java.util.List;

public class Result {

    private int totQues = 25;
    private String name;
    private List<String> choices;
    private double correct;
    private List<String> check = new ArrayList<String>();

    public Result(int q, String n, List<String> ch, double cor, List<String> chk) {
        totQues = q;
        name = n;
        choices = ch;
        correct = cor;
        check = chk;
    }

    public int getTotQues() {
        return totQues;
    }

    public String getName() {
        return name;
    }

    public List<String> getChoices() { return choices; }

    public double getCorrect() {
        return correct;
    }

    public List<String> getCheck() { return check; }

    public double calculatePoints() {
        double score = (correct / totQues) * 100;
        return score;
    }

    //calculates candidate percentage score and returns as String
    public String getScore() {
        int score = (int) (this.calculatePoints());
        return (String.valueOf(score + "%"));
    }

}
