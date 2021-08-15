package assignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MyAnalysis extends Stage {
	private Label labUebia, labTitle, labMax, labMaxScore, labMin, labMinScore, labMean, labMeanScore, labMode, labModeScore, labMedian, labMedianScore, labSD, labSDScore;
	private File results = new File("./data", "result.txt");
	private Scanner sfile;
	private int totResults;
	private double[] score;
	private double[] sortedScore;
	private Button btnGraph;
	private Color aqua = Color.rgb(133,220,221);
	private Color cornField = Color.rgb(244,100,92);
	private String[] candidates; 

	public MyAnalysis() throws IOException {
		//set window title
		this.setTitle("Analysis Form");

		//initialize score array that stores scores of all candidates
		getTotRes("data/result.txt");
		score = new double[totResults];
		candidates = new String[totResults];
		//get the scores from result.txt
		readFile(results);

		//Uebia Flag
		Image uebia = new Image(new FileInputStream("data/Uebia.jpg"));  
		ImageView uebiaView = new ImageView(uebia);
		uebiaView.setFitWidth(90);
		uebiaView.setPreserveRatio(true);

		labUebia = new Label("❦ Uebia ❦");
		setCursiveFont(labUebia, 15);

		//Title text
		labTitle = new Label("Analysis of Scores");
		setCursiveFont(labTitle, 30);
		labTitle.setStyle("-fx-pref-width: 260px;-fx-border-color:white;-fx-border-width:5px;-fx-border-style:dotted");

		//Headings
		labMax = new Label("Maximum Score");
		setHeading(labMax);
		labMin = new Label("Minimum Score");
		setHeading(labMin);
		labMean = new Label("Mean of Score");
		setHeading(labMean);
		labMode = new Label("Mode of Score");
		setHeading(labMode);
		labMedian = new Label("Median of Score");
		setHeading(labMedian);
		labSD = new Label("Standard Deviation of Score");
		setHeading(labSD);

		//Show explanations when headings are hovered over
		showHint(labMax, "The highest score\n"
				+ "obtained by a candidate.");
		showHint(labMin, "The lowest score\n"
				+ "obtained by a candidate.");
		showHint(labMean, "The average of\n"
				+ "the scores.");
		showHint(labMode, "The most common\n"
				+ "score.");
		showHint(labMedian, "The middle of\n"
				+ "the set of scores.");
		showHint(labSD, "A quantity\n"
				+ "expressing by how much\n"
				+ "the members of a group\n"
				+ "differ from the mean\n"
				+ "value for the group.");

		//Calculate and display max, min, mode, median, standard deviation
		double max = maxScore(score);
		double min = minScore(score);
		double mean = meanScore();
		double mode = modeScore();

		sortedScore = score.clone();

		double med = medianScore(sortedScore);
		double sd = SDScore(score);

		labMaxScore = new Label(Double.toString(max));
		setNormFont(labMaxScore);
		labMinScore = new Label(Double.toString(min));
		setNormFont(labMinScore);
		labMeanScore = new Label(Double.toString(mean));
		setNormFont(labMeanScore);
		labModeScore = new Label(Double.toString(mode));
		setNormFont(labModeScore);
		labMedianScore = new Label(Double.toString(med));
		setNormFont(labMedianScore);
		labSDScore = new Label(Double.toString(sd));
		setNormFont(labSDScore);

		//Button to show the graph plotted
		btnGraph = new Button("SHOW GRAPH");
		setBtnStyle(btnGraph);
		//Change colour when hovered on
		btnGraph.addEventHandler(MouseEvent.MOUSE_ENTERED,
				new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				btnGraph.setStyle("-fx-background-color: #85dcdd;-fx-border-color:gold");
				btnGraph.setTextFill(Color.WHITE);
			}
		});
		//revert back when mouse leaves
		btnGraph.addEventHandler(MouseEvent.MOUSE_EXITED,
				new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				setBtnStyle(btnGraph);
			}
		});

		btnGraph.setOnAction((event) -> {
			try {

				start();
			}
			catch(Exception e) {
				System.out.println(e);
			}

		});

		//Add everything into a VBox
		VBox myVBox = new VBox(uebiaView, labUebia, labTitle, labMax, labMaxScore, labMin, labMinScore, labMean, labMeanScore, labMode, labModeScore, labMedian, labMedianScore, labSD, labSDScore, btnGraph);

		//VBox style
		myVBox.setSpacing(8);
		myVBox.setAlignment(Pos.CENTER);
		//Set gif as background
		InputStream imgFile = new FileInputStream("data/AnalysisBG.gif");
		Image image = new Image (imgFile);
		myVBox.setBackground(new Background(new BackgroundImage(image,BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT,
				BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT)));

		//Add Vbox to the scene and then show it
		Scene analysisScene = new Scene(myVBox, 400, 600);
		this.setScene(analysisScene);
		this.show();
	}

	//Title and country name font style
	public void setCursiveFont (Label l, int size) throws FileNotFoundException {
		InputStream titleFontFile = new FileInputStream("data/Precious.ttf");
		Font title = Font.loadFont(titleFontFile, size);

		l.setFont(title);
	}

	//Heading style
	public void setHeading(Label l) throws FileNotFoundException {
		InputStream h1FontFile = new FileInputStream("data/TheWesternDemoRegular.ttf");
		Font h1 = Font.loadFont(h1FontFile, 18);

		//Drop shadow
		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0f);
		ds.setColor(Color.WHITE);
		ds.setRadius(2.0);

		l.setFont(h1);
		l.setEffect(ds);

		//Change font colour when hovered over
		hoverTxt(l, ds);
	}

	//Use tooltip to show hints
	public void showHint(Label l, String desc) {
		Tooltip t = new Tooltip(desc);
		Tooltip.install(l, t);
	}

	//Change text colour when hovered over
	public void hoverTxt(Label l, DropShadow ds) {
		l.addEventHandler(MouseEvent.MOUSE_ENTERED,
				new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				l.setTextFill(aqua);
				ds.setColor(cornField);
			}
		});
		//revert back when mouse leaves
		l.addEventHandler(MouseEvent.MOUSE_EXITED,
				new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				l.setTextFill(Color.BLACK);
				ds.setColor(Color.WHITE);
			}
		});
	}

	//Score text style
	public void setNormFont(Label l) {
		l.setFont(Font.font("Monospace", 15));
		l.setTextFill(Color.CRIMSON);
		l.setStyle("-fx-font-weight: bold");
	}

	//Button style
	public void setBtnStyle(Button b) {
		b.setStyle("-fx-background-color: #F1FAC0;-fx-border-color:gold");
		b.setTextFill(Color.BLACK);
	}

	//get the total number of candidates from result.txt file
	public void getTotRes(String path) throws IOException {
		List<String> lines = Files.readAllLines(Paths.get(path), Charset.defaultCharset());
		totResults = lines.size();  
	}

	//get the candidate names and scores from result.txt file
	public void readFile(File myf) throws IOException{
		sfile = new Scanner(myf);
		//read the last item of each line
		for (int k = 1; k <= totResults; k++) {
			String aLine = sfile.nextLine();
			Scanner sline = new Scanner(aLine);
			String[] tokens = aLine.split(":");
			String last = tokens[tokens.length - 1];
			String first = tokens[0];
			candidates[k-1] = first;
			score[k-1] = Integer.parseInt(last);
			sline.close();
		}
		sfile.close();
	}

	//find the maximum score
	public double maxScore(double[] arr) {
		int i;
		double max = score[0];

		for (i = 1; i < score.length; i++)
			if (score[i] > max)
				max = score[i];

		return max;
	}

	//find the minimum score
	public double minScore(double[] arr) {
		double min = score[0];

		for (int i = 1; i < score.length; i++)
			if (score[i] < min)
				min = score[i];

		return min;
	}

	//add all the scores together
	public double sumScore() {
		double sum = 0;

		for (int i = 0; i < score.length; i++)
			sum += score[i];

		return sum;
	}

	//find the average of all the scores
	public double meanScore() {
		double avg;
		double sum = sumScore();

		avg = sum / totResults;
		return avg;
	}

	//find the mode score
	public double modeScore() {
		double mode = 0;
		int maxCount = 0;

		//compare a number in an index with all the other ones, if it is equal, increase the count
		for (int i = 0; i < score.length; ++i) {
			int count = 0;
			for (int j = 0; j < score.length; ++j) {
				if (score[j] == score[i])
					count++;
			}
			//if the count of this number is more than the current maximum, replace it
			if (count > maxCount) {
				maxCount = count;
				mode = score[i];
			}
		}
		return mode;
	}

	//sort the array in ascending order
	public double[] selSort(double[] list) {
		int size = list.length;

		//iterate through unsorted array
		for (int i = 0; i < size-1; i++){
			//nominate first element as minimum 
			int minPos = i;
			//find the minimum element in the unsorted array
			for (int j = i+1; j < size; j++)
				if (list[j] < list[minPos])
					minPos = j;

			//swap
			double temp = list[minPos];
			list[minPos] = list[i];
			list[i] = temp;
		}
		return list;
	}

	//sort the list into ascending order, then find the median
	public double medianScore(double[] list) {
		double median = 0;
		int size = totResults;

		//sort
		list = selSort(list);
		//calculate median
		if(size % 2 == 1){
			median = list[(size + 1) / 2 - 1];
		}
		else{
			median = (list[size/2-1]+list[size/2])/2;
		}
		return median;
	}

	//calculate standard deviation
	public double SDScore(double[] list){
		double mean = meanScore();
		double SD = 0;

		for (double num: list) {
			SD += Math.pow(num - mean, 2);
		}
		return Math.sqrt(SD/list.length);
	}

	//creating a barchart
	public void start() throws Exception {
		//setting the title name
		this.setTitle("2021 Candidates' Result");
		//defining the axes
		CategoryAxis xAxis  = new CategoryAxis();
		xAxis.setLabel("Candidates");

		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Marks");
		//creating the barchart
		BarChart barChart = new BarChart(xAxis, yAxis);

		XYChart.Series candidateData = new XYChart.Series();
		candidateData.setName("2021 Candidate Test");
		//setting the data
		for (int i = 0; i < totResults; i++) {
			candidateData.getData().add(new XYChart.Data(candidates[i], score[i]));
		}

		barChart.getData().add(candidateData);

		barChart.setStyle("-fx-font-family: Copperplate; -fx-font-size: 15;");

		VBox vbox = new VBox(barChart);

		//Set gif as background
		InputStream imgFile = new FileInputStream("data/GraphBG.gif");
		Image image = new Image (imgFile);
		vbox.setBackground(new Background(new BackgroundImage(image,BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT,
				BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT)));

		Scene scene = new Scene(vbox, 400, 300);

		this.setScene(scene);
		this.setHeight(400);
		this.setWidth(600);

		this.show();
		
		this.setOnHiding(e ->{
			Platform.exit();
		});
	}       
} 