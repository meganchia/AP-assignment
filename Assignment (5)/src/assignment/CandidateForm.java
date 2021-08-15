package assignment;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CandidateForm extends Stage {

	private AdminLogin a1 = new AdminLogin();
	private String name;
	private LinkedList<Candidate> candidateList = new LinkedList<Candidate>();
	private String countryList[] = { "Afghanistan","Uruguay","Ecuador","Belgium","Iraq"};
	private File myf = new File("./data", "candidates.txt");
	private String[] names = new String[10];
	private int totCandidates = 0;
	private Button btnOk, btnLogin;
	private Image image, image2;
	private static MediaPlayer mediaPlayer;
	private static boolean found = false;
	private int WIDTH = 400;
	private int HEIGHT = 600;

	public CandidateForm() throws IOException {
		//title of window
		this.setTitle("Candidate Login Form");

		//welcome text
		Label welLabel = new Label("Welcome to our nation : UEBIA!");
		welLabel.setFont(new Font("Comic Sans MS", 24));

		//read from candidates.txt and initialise Candidate objects in linked list
		readFromFile();

		//candidate list
		Label labName1 = new Label("Select Candidate name");
		labName1.setFont(new Font("Comic Sans MS", 15));
		for (int i = 0; i < 10; i++) {
			names[i] = candidateList.get(i).getName();
		}
		ComboBox<String> nameBox = new ComboBox<String>(FXCollections.observableArrayList(names));
		nameBox.getSelectionModel().selectFirst();
		nameBox.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		name = nameBox.getValue().toString();
		MyParam.setName(name);

		//Get Candidate image
		image = getImage(name);
		ImageView imageView = new ImageView();
		MyParam.setImage(image);

		// Enter password
		Label labName2 = new Label("Enter password");
		labName2.setFont(new Font("Comic Sans MS", 15));
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Your password");
		passwordField.setBorder(new Border(new BorderStroke(Color.BLACK,
				BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		passwordField.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		passwordField.setMaxWidth(220);

		//Combo box for country
		Label labName3 = new Label("Select your country");
		labName3.setFont(new Font("Comic Sans MS", 15));
		ComboBox<String> comboBox = new ComboBox<String>(FXCollections.observableArrayList(countryList));
		comboBox.getSelectionModel().selectFirst();
		comboBox.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));

		//Get flag image
		String country = comboBox.getValue().toString();
		image2 = getImage(country);
		MyFlag.setImage(image2);
		ImageView imageView2 = new ImageView();

		//music playing
		playAnthem();

		EventHandler<ActionEvent> event = (ActionEvent e) -> {
			try {
				name = nameBox.getValue().toString();
				image = getImage(name); //change candidate image based on candidate combo box
				imageView.setImage(image);
				MyParam.setImage(image);

			}catch (FileNotFoundException ex) {
				Logger.getLogger(CandidateForm.class.getName()).log(Level.SEVERE, null, ex);
			}
		};

		EventHandler<ActionEvent> event2 = (ActionEvent e) -> {
			try {
				String country1 = comboBox.getValue().toString();
				image2 = getImage(country1);
				imageView2.setImage(image2); //change flag image based on country combo box
				MyFlag.setImage(image2);

				boolean playing = mediaPlayer.getStatus().equals(Status.PLAYING);
				if(playing == true){
					mediaPlayer.stop();
				}

			}catch (FileNotFoundException ex) {
				Logger.getLogger(CandidateForm.class.getName()).log(Level.SEVERE, null, ex);
			}
		};

		nameBox.setOnAction(event);
		comboBox.setOnAction(event2);

		Label labName4 = new Label();
		btnOk = new Button("Done");
		btnOk.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		btnOk.setOnAction(e -> {
			String password = passwordField.getText();
			MyFlag.setName(name);

			found = verifyCandidate(candidateList, name, password);

			if (found == true){
				mediaPlayer.stop(); //Stop anthem
				this.hide();
			}
			else{
				labName4.setText("Incorrect Password");
				labName4.setFont(new Font("Comic Sans MS", 15));
			}
		});

		btnLogin = new Button("LOGIN AS ADMIN");
		btnLogin.setOnAction(e -> {
			a1.showStage();
		});

		imageView.setImage(image);
		imageView.setFitHeight(140);
		imageView.setFitWidth(140);
		imageView2.setImage(image2);
		imageView2.setFitHeight(80);
		imageView2.setFitWidth(140);
		VBox myVBox = new VBox(welLabel,labName1, nameBox,imageView, labName2, passwordField, labName3,
				comboBox, imageView2, btnOk, labName4, btnLogin);
		myVBox.setSpacing(6);
		myVBox.setAlignment(Pos.CENTER);

		//background image
		InputStream imgFile = new FileInputStream("data/candidate_page_bg.jpg");
		Image image = new Image (imgFile);
		myVBox.setBackground(new Background(new BackgroundImage(image,BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT,BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

		this.setScene(new Scene(myVBox, WIDTH, HEIGHT));
		this.setResizable(false);
		this.show();
		
		this.setOnCloseRequest(e -> {
			System.exit(0);
		});
	}

	//read from candidates.txt file
	public void readFromFile() throws FileNotFoundException {
		Scanner sfile;
		String candidateName;
		String password;
		Candidate candidate;

		sfile = new Scanner(myf);
		String aLine = sfile.nextLine();
		Scanner sline = new Scanner(aLine);
		totCandidates = Integer.parseInt(sline.next());
		for (int k = 1; k <= totCandidates; k++) {
			aLine = sfile.nextLine();
			sline = new Scanner(aLine);
			sline.useDelimiter(",");
			candidateName = sline.next();
			password = sline.next();
			sline.close();
			candidate = new Candidate(candidateName, password);
			candidateList.add(candidate);
		}
		sfile.close();
	}

	public String getName() {
		return name;
	}

	public Image getImage(String file) throws FileNotFoundException {
		String path = "data/"+ file + ".jpg";
		InputStream imagefile = new FileInputStream(path);
		Image image = new Image(imagefile);
		return image;
	}

	public void playAnthem(){
		String musicpath = "data/anthem.mp4";
		String bip = musicpath;
		Media hit = new Media(new File(bip).toURI().toString());
		mediaPlayer = new MediaPlayer(hit);
		mediaPlayer.play();
	}

	// verify candidate name and password
	public static boolean verifyCandidate(LinkedList<Candidate> candidateList, String name, String password) {
		boolean loggedIn = false;
		Iterator<Candidate> spin = candidateList.iterator();

		while (spin.hasNext()){
			Candidate user = spin.next();
			if(user.getName().equals(name) && user.getPassword().equals(password)) {
				loggedIn = true;
				break;
			}
		}
		return loggedIn;
	}

}