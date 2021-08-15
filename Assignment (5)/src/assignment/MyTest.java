package assignment;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MyTest extends Application {

	private Pane mainPane;
	private Scene mainScene;
	private CandidateForm winCandidate;
	private File qFile = new File("./data", "questions.txt"); 
	private File rFile = new File("./data", "result.txt"); 
	private int totQues = 0;
	private int activeQ = 1;
	private Label labCandidateName, labQuesNo, labQues, labName, labTimer, labCountdown, labCountdown2;
	private ImageView imgQues, imgA, imgB, imgC, imgD; 
	private ImageView candidateImg;
	private Label labA, labB, labC, labD;
	private RadioButton radChoice1, radChoice2, radChoice3, radChoice4;
	private ToggleGroup grpChoices;
	private Button btnPrev, btnNext, btnSubmit;
	private Pane paneC;
	private LinkedList<Question> quesList = new LinkedList<Question>();
	private Timer timer;
	private int interval = 300;

	public MyTest() throws FileNotFoundException {
	}

	public void start(Stage mainStage) throws IOException {

		mainStage.setTitle("Citizenship Test");

		labCandidateName = new Label("Candidate Name");
		labCandidateName.setStyle("-fx-font-family: Copperplate;");
		labTimer = new Label("Time Remaining"); 
		labTimer.setLayoutX(30); 
		labTimer.setLayoutY(40);
		labTimer.setStyle("-fx-font-family: Copperplate;");
		labCountdown = new Label("5:00");
		labCountdown.setLayoutX(175); 
		labCountdown.setLayoutY(40);
		labCountdown.setStyle("-fx-font-family: Copperplate;");
		labCountdown2 = new Label("0:00");
		labCountdown2.setLayoutX(175);
		labCountdown2.setLayoutY(40);
		labCountdown2.setStyle("-fx-font-family: Copperplate;");
		labCountdown2.setTextFill(Color.SALMON);
		labCandidateName.setLayoutX(30);
		labCandidateName.setLayoutY(15);
		labName = new Label(""); 
		labName.setLayoutX(175);
		labName.setLayoutY(15); 
		labName.setStyle("-fx-font-weight:bold;");
		labQuesNo = new Label(""); 
		labQuesNo.setLayoutX(25); 
		labQuesNo.setLayoutY(75); 
		labQuesNo.setStyle("-fx-font-size: 12pt;-fx-font-weight:bold;");

		labQuesNo = new Label(""); 
		labQuesNo.setLayoutX(25); 
		labQuesNo.setLayoutY(75); 
		labQuesNo.setStyle("-fx-font-size: 12pt;-fx-font-weight:bold;");

		labQues = new Label(""); 
		labQues.setLayoutX(25); 
		labQues.setLayoutY(100); 
		labQues.setStyle("-fx-font-size: 12pt;-fx-font-weight:bold;");

		imgQues = new ImageView();
		imgQues.setLayoutX(25);
		imgQues.setLayoutY(75);
		imgQues.setFitHeight(200);
		imgQues.setFitWidth(200);

		imgA = new ImageView();
		imgA.setLayoutX(100);
		imgA.setLayoutY(75);
		imgA.setFitHeight(140);
		imgA.setFitWidth(200);

		imgB = new ImageView();
		imgB.setLayoutX(100);
		imgB.setLayoutY(225);
		imgB.setFitHeight(140);
		imgB.setFitWidth(200);

		imgC = new ImageView();
		imgC.setLayoutX(100);
		imgC.setLayoutY(375);
		imgC.setFitHeight(140);
		imgC.setFitWidth(200);

		imgD = new ImageView();
		imgD.setLayoutX(100);
		imgD.setLayoutY(525);
		imgD.setFitHeight(140);
		imgD.setFitWidth(200);

		candidateImg = new ImageView(); 
		candidateImg.setImage(MyParam.getImage()); 
		candidateImg.setLayoutX(530); 
		candidateImg.setLayoutY(15); 
		candidateImg.setFitHeight(80); 
		candidateImg.setFitWidth(140);

		labA = new Label("A");
		labA.setLayoutX(25);
		radChoice1 = new RadioButton("");
		radChoice1.setLayoutX(50);
		labB = new Label("B");
		labB.setLayoutX(25);
		radChoice2 = new RadioButton("");
		radChoice2.setLayoutX(50);

		labC = new Label("C");
		labC.setLayoutX(25);
		radChoice3 = new RadioButton("");
		radChoice3.setLayoutX(50);

		labD = new Label("D");
		labD.setLayoutX(25);
		radChoice4 = new RadioButton("");
		radChoice4.setLayoutX(50);

		grpChoices = new ToggleGroup();

		radChoice1.setToggleGroup(grpChoices); 
		radChoice2.setToggleGroup(grpChoices); 
		radChoice3.setToggleGroup(grpChoices); 
		radChoice4.setToggleGroup(grpChoices);

		setFontStyle(labQues);
		setFontStyle(labA);
		setFontStyle(labA);
		setFontStyle(labB);
		setFontStyle(labC);
		setFontStyle(labD);
		setFontStyle(radChoice1);
		setFontStyle(radChoice2);
		setFontStyle(radChoice3);
		setFontStyle(radChoice4);

		paneC = new Pane(); 
		paneC.setLayoutX(25);
		paneC.setLayoutY(75); 
		paneC.getChildren().addAll(imgQues, labA, imgA, radChoice1, labB, imgB, radChoice2, labC, imgC, radChoice3, labD, imgD, radChoice4);

		btnPrev = new Button("Prev"); 
		btnPrev.setLayoutX(25); 
		btnPrev.setLayoutY(740); 
		btnPrev.setStyle("-fx-pref-width: 75px;"); 
		btnPrev.setDisable(true);
		btnNext = new Button("Next"); 
		btnNext.setLayoutX(125); 
		btnNext.setLayoutY(740); 
		btnNext.setStyle("-fx-pref-width: 75px;"); 
		btnSubmit = new Button("End"); 
		btnSubmit.setLayoutX(600); 
		btnSubmit.setLayoutY(740); 
		btnSubmit.setStyle("-fx-pref-width: 75px;");

		setBtnStyle(btnPrev);
		setBtnStyle(btnNext);
		setBtnStyle(btnSubmit);

		hoverBtn(btnPrev);
		hoverBtn(btnNext);
		hoverBtn(btnSubmit);

		readFromFile();

		radChoice1.setOnAction(e -> {
			quesList.get(activeQ-1).setSelected(0, true); 
			quesList.get(activeQ-1).setSelected(1, false); 
			quesList.get(activeQ-1).setSelected(2, false); 
			quesList.get(activeQ-1).setSelected(3, false);
		});

		radChoice2.setOnAction(e -> {
			quesList.get(activeQ-1).setSelected(0, false); 
			quesList.get(activeQ-1).setSelected(1, true); 
			quesList.get(activeQ-1).setSelected(2, false); 
			quesList.get(activeQ-1).setSelected(3, false);
		});

		radChoice3.setOnAction(e -> {
			quesList.get(activeQ-1).setSelected(0, false); 
			quesList.get(activeQ-1).setSelected(1, false); 
			quesList.get(activeQ-1).setSelected(2, true); 
			quesList.get(activeQ-1).setSelected(3, false);
		});

		radChoice4.setOnAction(e -> {
			quesList.get(activeQ-1).setSelected(0, false); 
			quesList.get(activeQ-1).setSelected(1, false); 
			quesList.get(activeQ-1).setSelected(2, false); 
			quesList.get(activeQ-1).setSelected(3, true);
		});

		if (totQues == 1)
			btnNext.setDisable(true);

		btnNext.setOnAction(e -> {
			activeQ++;
			btnPrev.setDisable(false);
			if (activeQ == totQues)
				btnNext.setDisable(true);
			reloadQues();
		});

		btnPrev.setOnAction(e -> {
			activeQ--;
			btnNext.setDisable(false);
			if (activeQ == 1)
				btnPrev.setDisable(true);
			reloadQues();
		});

		btnSubmit.setOnAction(e -> {
			appendToFile(); 
			mainStage.hide();
			System.exit(0);
		});

		mainPane = new Pane();
		mainPane.getChildren().addAll(labCandidateName, labName, candidateImg, labTimer, labCountdown, labQuesNo, labQues, paneC, btnNext, btnPrev, btnSubmit);

		//Set gif as background
		InputStream imgFile = new FileInputStream("data/TestBG.gif");
		Image image = new Image (imgFile);
		mainPane.setBackground(new Background(new BackgroundImage(image,BackgroundRepeat.REPEAT,
				BackgroundRepeat.REPEAT,
				BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT)));

		mainStage.hide();

		mainScene = new Scene(mainPane, 700, 800);

		//start
		mainStage.setScene(mainScene);
		reloadQues();
		winCandidate = new CandidateForm();
		winCandidate.show();
		winCandidate.setOnHiding(e ->{
			labName.setText(winCandidate.getName()); 
			mainStage.show();
			candidateImg.setImage(MyParam.getImage()); 
			setTimer();
		});

	}

	public void reloadQues() {
		labQuesNo.setText("Question " + Integer.toString(activeQ) + " / " +totQues); 
		labQues.setText(quesList.get(activeQ-1).getTheQues()); 
		radChoice1.setText(quesList.get(activeQ-1).getChoice(0)); 
		radChoice2.setText(quesList.get(activeQ-1).getChoice(1)); 
		radChoice3.setText(quesList.get(activeQ-1).getChoice(2)); 
		radChoice4.setText(quesList.get(activeQ-1).getChoice(3)); 
		imgQues.setImage(null);
		imgA.setImage(null);
		imgB.setImage(null);
		imgC.setImage(null);
		imgD.setImage(null);

		if (quesList.get(activeQ-1).getType() == 1) {
			labA.setLayoutY(100);
			radChoice1.setLayoutY(100);
			labB.setLayoutY(200);
			radChoice2.setLayoutY(200);
			labC.setLayoutY(300);
			radChoice3.setLayoutY(300);
			labD.setLayoutY(400);
			radChoice4.setLayoutY(400);
		}

		if (quesList.get(activeQ-1).getType() == 2) {
			File pFile = new File("data/" + quesList.get(activeQ- 1).getQuesPic());
			Image img = new Image(pFile.toURI().toString()); 
			imgQues.setImage(img);
			labA.setLayoutY(300); 
			radChoice1.setLayoutY(300); 
			labB.setLayoutY(400); 
			radChoice2.setLayoutY(400); 
			labC.setLayoutY(500); 
			radChoice3.setLayoutY(500); 
			labD.setLayoutY(600); 
			radChoice4.setLayoutY(600);
		}

		if (quesList.get(activeQ-1).getType() == 3) {
			File pFileA = new File("data/" + quesList.get(activeQ- 1).getChoice(0));
			Image img1 = new Image(pFileA.toURI().toString());
			File pFileB = new File("data/" + quesList.get(activeQ- 1).getChoice(1));
			Image img2 = new Image(pFileB.toURI().toString());
			File pFileC = new File("data/" + quesList.get(activeQ- 1).getChoice(2));
			Image img3 = new Image(pFileC.toURI().toString());
			File pFileD = new File("data/" + quesList.get(activeQ- 1).getChoice(3));
			Image img4 = new Image(pFileD.toURI().toString());

			imgA.setImage(img1);
			imgB.setImage(img2);
			imgC.setImage(img3);
			imgD.setImage(img4);

			labA.setLayoutY(75);
			radChoice1.setLayoutY(75);
			radChoice1.setText("");
			labB.setLayoutY(225);
			radChoice2.setLayoutY(225);
			radChoice2.setText("");
			labC.setLayoutY(375);
			radChoice3.setLayoutY(375);
			radChoice3.setText("");
			labD.setLayoutY(525);
			radChoice4.setLayoutY(525);
			radChoice4.setText("");
		}

		radChoice1.setSelected(quesList.get(activeQ- 1).getSelected(0));
		radChoice2.setSelected(quesList.get(activeQ- 1).getSelected(1));
		radChoice3.setSelected(quesList.get(activeQ- 1).getSelected(2));
		radChoice4.setSelected(quesList.get(activeQ- 1).getSelected(3));
	}

	public void setFontStyle(Label l) {
		l.setTextFill(Color.FLORALWHITE);
		l.setStyle("-fx-font-family: Papyrus; -fx-font-size: 15;");
	}

	public void setFontStyle(RadioButton r) {
		r.setTextFill(Color.FLORALWHITE);
		r.setStyle("-fx-font-family: Papyrus; -fx-font-size: 15;");
	}

	//Button style
	public void setBtnStyle(Button b) {
		b.setStyle("-fx-background-color: #F1FAC0;-fx-border-color:gold; -fx-pref-width:75px;");
		b.setTextFill(Color.BLACK);
	}

	public void hoverBtn(Button b) {
		//Change colour when hovered on
		b.addEventHandler(MouseEvent.MOUSE_ENTERED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						b.setStyle("-fx-background-color: #85dcdd;-fx-border-color:gold; -fx-pref-width:75px;");
						b.setTextFill(Color.WHITE);
					}
				});
		//revert back when mouse leaves
		b.addEventHandler(MouseEvent.MOUSE_EXITED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						setBtnStyle(b);
					}
				});
	}

	public void readFromFile() {
		Scanner sfile;
		int type;
		char answer;
		String theQues;
		String choices[] = new String[4];
		String quesPic;
		Question ques;

		try {
			sfile = new Scanner(qFile);
			String aLine = sfile.nextLine();
			Scanner sline = new Scanner(aLine); 
			totQues = Integer.parseInt(sline.next()); 

			for (int k = 1; k <= totQues; k++) {
				aLine = sfile.nextLine();
				sline = new Scanner(aLine);
				sline.useDelimiter(":");
				type = Integer.parseInt(sline.next());

				answer = sline.next().charAt(0);
				theQues = sline.next();
				quesPic = "";

				if (type == 2)
					quesPic = sline.next();

				choices[0] = sline.next();
				choices[1] = sline.next();
				choices[2] = sline.next();
				choices[3] = sline.next();
				sline.close();
				ques = new Question(type, answer, theQues, choices,quesPic);
				quesList.add(ques);
			}
			sfile.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("File to read " + qFile + " notfound!"); }
	}

	public void appendToFile() {
		try {
			String selected;
			PrintWriter pw = new PrintWriter(new FileWriter(rFile, true));
			pw.print(winCandidate.getName());
			pw.print(":");
			for(int i = 0; i < 25; i++) {
				selected = "null";
				for (int j = 0; j < 4; j++) {
					if (quesList.get(i).getSelected(j)) {
						switch(j){
						case 0:
							selected = "A";
							break;
						case 1:
							selected = "B";
							break;
						case 2:
							selected = "C";
							break;
						case 3: 
							selected = "D";
							break;
						}

					}
				}
				pw.print(selected + ":");
			}  
			pw.close();
		} 
		catch (IOException e) {
		}
	}

	public void setTimer() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				int minutes;
				int seconds;
				String secs = "";

				if(interval > 0)
				{
					minutes = (interval / 60) % 60;
					seconds = (interval % 60);

					//seconds formatting
					if (seconds < 10) {
						secs = "0" + seconds;
					}
					else {
						secs = String.valueOf(seconds);
					}

					String finalSecs = secs;
					Platform.runLater(() -> labCountdown.setText(minutes + ":" + finalSecs));
					interval--;
				}
				else
				{
					timer.cancel();
					interval = 0;
				}
				try {
					setInterval();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 1000,1000);
	}

	//actions during set intervals
	public void setInterval() throws InterruptedException {
		String soundFile;
		if (interval == 0) {
			labCountdown2.setVisible(true);
			labCountdown.setVisible(false);
			btnNext.setDisable(true);
			btnPrev.setDisable(true);
			appendToFile();

			soundFile = "./data/alarm2.wav";
			playSound(soundFile);
			Thread.sleep(5000);
			Platform.exit();
		}
		else if (interval == 60) {
			soundFile = "./data/alarm.mp3";
			playSound(soundFile);
			labCountdown.setTextFill(Color.CRIMSON);
		}
	}

	//warning sound
	public void playSound(String s) {
		try {
			Media sound = new Media(new File(s).toURI().toString());
		}
		catch (Exception e) {
			System.out.println("Sound file not found!");
		}
		Media sound = new Media(new File(s).toURI().toString());
		MediaPlayer mdPlayer = new MediaPlayer(sound);
		mdPlayer.play();
	}

	public static void main(String args[]) {
		Application.launch(args);
	}


}