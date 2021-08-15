package assignment;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class MyResults extends Stage {
    private File results = new File("./data", "result.txt");
    private File questions = new File("./data", "Questions.txt");

    private Pane p1;
    private Label labDesc, labResult, labScore;
    private Button btnNext;
    private ComboBox<String> options;
    int totQues = 25;
    private int WIDTH = 400;
    private int HEIGHT = 600;
    private int totResults;
    private char ans[] = new char[totQues];
    private LinkedList<Result> resList = new LinkedList<Result>();

    private String cornField = "-fx-background-color: #F1FAC0;";
    private BackgroundImage bg;

    public MyResults() throws IOException {
        //set window title
        this.setTitle("Results");

        //initialize variables
        getAnswers();
        totQues = ans.length;

        //results table
        TableView<Integer> table = new TableView<>();
        table.setLayoutX(50);
        table.setLayoutY(125);
        table.setPlaceholder(new Label("Nothing to display"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefWidth(300);
        table.setPrefHeight(400);
        table.setStyle(cornField);

        labResult = new Label("Results: ");
        labResult.setLayoutX(250);
        labResult.setLayoutY(20);

        labDesc = new Label("Candidate Name: ");
        labDesc.setLayoutX(50);
        labDesc.setLayoutY(20);

        labScore = new Label();
        labScore.setLayoutX(235);
        labScore.setLayoutY(40);

        //analysis button
        btnNext = new Button("ANALYSIS");
        btnNext.setLayoutX(300);
        btnNext.setLayoutY(540);
        btnNext.setOnAction(e -> {
            this.hide();
            try {
                MyAnalysis a1 = new MyAnalysis();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        //set label and button styles
        try {
            setTextFont(labResult, 20);
            setTextFont(labDesc, 20);
            setScoreFont(labScore, 100);
            setBtnStyle(btnNext, 15);
        }
        catch (FileNotFoundException e) {
            System.out.println("Font files not found!");
        }

        //drop down names
        options = new ComboBox<>();
        options.setLayoutX(50);
        options.setLayoutY(50);
        dropDown();
        options.setOnAction(e -> {
            displayResults(resList, options.getValue());
            displayChoices(resList, options.getValue(), table);
        });

        //add everything to pane
        p1 = new Pane();
        p1.getChildren().add(labDesc);
        p1.getChildren().add(labResult);
        p1.getChildren().add(labScore);
        p1.getChildren().add(options);
        p1.getChildren().add(btnNext);
        p1.getChildren().add(table);

        //background image
        try {
            bg = new BackgroundImage(
                    new Image(new File("./data/bg4.png").toURI().toString(), 400, 600, false, true),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    BackgroundSize.DEFAULT);
        }
        catch (Exception e) {
            System.out.println("Background image not found!");
        }

        p1.setBackground(new Background(bg));

        //set scene
        Scene resultScene = new Scene(p1, WIDTH, HEIGHT);
        this.setResizable(false);
        this.setScene(resultScene);
    }

    //get the total number of candidates from result.txt file
    public void getTotRes(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path), Charset.defaultCharset());
        int noOfLines = lines.size();
        totResults = noOfLines;
    }

    //gets answers from questions.txt
    public void getAnswers() {
        Scanner scan;
        int type;
        char answer;

        try {
            scan = new Scanner(questions);
            String aLine = scan.nextLine();
            Scanner sline = new Scanner(aLine);
            totQues = Integer.parseInt(sline.next());

            for (int k = 1; k <= totQues; k++) {
                aLine = scan.nextLine();
                sline = new Scanner(aLine);
                sline.useDelimiter(":");
                type = Integer.parseInt(sline.next());
                answer = sline.next().charAt(0);
                ans[k-1] = answer;

                sline.close();
            }
            scan.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File to read " + questions + " not found!");
        }
    }

    //determines if all candidate answers are correct or wrong - result.txt
    public void markAnswers() throws IOException {
        Scanner scan;
        String name;
        Result res;

        getTotRes("./data/result.txt");

        try {
            scan = new Scanner(results);

            for (int k = 0; k < totResults; k++) {
                String aLine = scan.nextLine();
                Scanner sline = new Scanner(aLine);
                double correct = 0;
                List<String> answer = new ArrayList<String>();
                List<String> check = new ArrayList<String>();
                sline.useDelimiter(":");
                name = sline.next();

                for (int i = 0; i < totQues; i++) {
                    answer.add(Character.toString(sline.next().charAt(0)));
                    if (Character.toString(ans[i]).equals(answer.get(i))) {
                        correct++;
                        check.add("\u2713");
                    }
                    else {
                        check.add("\u2717");
                    }
                }
                //creates Result for each candidate
                res = new Result(totQues, name, answer, correct, check);
                resList.add(res);
                sline.close();
            }
            scan.close();
        }

        catch (FileNotFoundException e) {
            System.out.println("File to read " + questions + " not found!");
        }
    }

    //adds candidate's scores to result.txt
    public void addScores() throws IOException {
        String name;
        String answers[] = new String[totQues];
        int score = 0;
        results.renameTo(new File("./data/oldresult.txt"));
        results = new File ("./data/oldresult.txt");
        PrintWriter newResults = new PrintWriter("./data/result.txt");

        for (int k = 0; k < resList.size(); k++) {
            name = resList.get(k).getName();
            answers = resList.get(k).getChoices().toArray(new String[totQues]);
            String a = String.join(":", answers);
            score = (int) resList.get(k).calculatePoints();

            //each candidate line
            newResults.println(name + ":" + a + ":" + score);
        }
        newResults.close();

        results = new File("./data", "result.txt");
        File oldResults = new File ("./data", "oldresult.txt");
        oldResults.delete();
    }

    //checking if candidate takes test more than once - takes most recent result
    public void checkResults() {
        String name;
        int key;
        Map<Integer, String> nameMap = new HashMap<Integer, String>();

        for (int i = 0; i < resList.size(); i++) {
            name = resList.get(i).getName();

            if (!nameMap.containsValue(name)) {
                nameMap.put(i, name);
            }
            else {
                //removes previous entry from resList
                for (Map.Entry<Integer, String> entry : nameMap.entrySet()) {
                    if (entry.getValue().equals(name)) {
                        key = entry.getKey();
                        resList.remove(key);
                    }
                }
            }
        }
    }

    //adds names to drop down
    public void dropDown() throws IOException {
        markAnswers();
        checkResults();
        addScores();
        String names[] = new String[resList.size()];
        for (int i = 0; i < resList.size(); i++) {
            names[i] = resList.get(i).getName();
        }
        options.getItems().addAll(FXCollections.observableArrayList(names));
        options.setStyle("-fx-font: 15px \"Times New Roman\";");
    }

    //displays candidate score
    public void displayResults(LinkedList<Result> resList, String name) {
        for (int i = 0; i < resList.size(); i++) {
            if (name.equals(resList.get(i).getName())) {
                String score = resList.get(i).getScore();
                double sc = resList.get(i).calculatePoints();
                labScore.setText(score);

                //add drop shadow to score
                DropShadow ds = new DropShadow();
                ds.setOffsetY(3.0f);
                ds.setRadius(2.0);
                labScore.setEffect(ds);

                //set score colour + hover
                if (sc < 50) {
                    labScore.setTextFill(Color.rgb(244, 100, 92));
                    hoverScore(labScore, "FAIL", ds);
                }
                else {
                    labScore.setTextFill(Color.AQUAMARINE);
                    hoverScore(labScore, "PASS", ds);
                }
            }
        }
    }

    //fills in table
    public void displayChoices(LinkedList<Result> resList, String name, TableView table) {
        table.getItems().clear();
        table.getColumns().clear();
        List<Integer> quesNo = new ArrayList<Integer>();

        //adding question no to a list
        for (int k = 0; k < totQues; k++) {
            quesNo.add(k+1);
            table.getItems().add(k);
        }

        //question no column
        TableColumn<Integer, Number> questionColumn = new TableColumn<>("No.");
        questionColumn.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            return new ReadOnlyIntegerWrapper(quesNo.get(rowIndex));
        });
        questionColumn.setStyle("-fx-alignment: CENTER-RIGHT");

        table.getColumns().add(questionColumn);

        for (int i = 0; i < resList.size(); i++) {
            if (name.equals(resList.get(i).getName())) {

                List<String> choices = resList.get(i).getChoices();
                List<String> checkList = resList.get(i).getCheck();

                //candidate choices column
                TableColumn<Integer, String> choicesColumn = new TableColumn<>("Choices");
                choicesColumn.setCellValueFactory(cellData -> {
                    Integer rowIndex = cellData.getValue();
                    return new ReadOnlyStringWrapper(choices.get(rowIndex));
                });
                choicesColumn.setStyle("-fx-alignment: CENTER");
                //candidate check mark column
                TableColumn<Integer, String> checkColumn = new TableColumn<>();
                checkColumn.setCellValueFactory(cellData -> {
                    Integer rowIndex = cellData.getValue();
                    return new ReadOnlyStringWrapper(checkList.get(rowIndex));
                });
                checkColumn.setStyle("-fx-alignment: CENTER");
                //sets column size
                questionColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
                choicesColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.4));
                checkColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.4));

                questionColumn.setResizable(false);
                choicesColumn.setResizable(false);
                checkColumn.setResizable(false);

                table.getColumns().add(choicesColumn);
                table.getColumns().add(checkColumn);
            }
        }
    }

    public void hoverScore(Label l, String desc, DropShadow ds) {
        Tooltip t = new Tooltip(desc);
        t.setFont(Font.font("Verdana", 15));
        l.setTooltip(t);

        l.setOnMouseEntered(e -> {
            ds.setColor(Color.WHITE);
        });

        l.setOnMouseExited(e -> {
            ds.setColor(Color.BLACK);
        });

    }

    public void setScoreFont(Label l, int s) throws FileNotFoundException {
        InputStream fontFile = new FileInputStream("./data/Holiday.otf");
        Font aFont = Font.loadFont(fontFile, s);
        l.setFont(aFont);
    }

    public void setTextFont(Label l, int s) throws FileNotFoundException {
        InputStream fontFile = new FileInputStream("./data/EnoltaseFont.ttf");
        Font aFont = Font.loadFont(fontFile, s);
        l.setFont(aFont);
    }

    public void setBtnStyle(Button b, int s) throws FileNotFoundException {
        InputStream fontFile = new FileInputStream("./data/EnoltaseFont.ttf");
        Font aFont = Font.loadFont(fontFile, s);
        b.setStyle(cornField);
        b.setFont(aFont);
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color: #FA8072; -fx-text-fill: #FFFFFF"));
        b.setOnMouseExited(e -> b.setStyle(cornField));
    }

    public void showStage() {
        this.show();
    }

}
