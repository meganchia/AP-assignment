package assignment;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.Scanner;

public class AdminLogin extends Stage {

    private File admin = new File("./data", "admin.txt");
    private MyResults r1 = new MyResults();

    private int WIDTH = 400;
    private int HEIGHT = 300;
    private Scene adminScene;
    private Label labUser, labPass, labError;
    private TextField textUser;
    private PasswordField passAdmin;
    private Button btnLogin, btnBack;
    private boolean verify;
    private String cornField = "-fx-background-color: #F1FAC0;";
    private BackgroundImage bg;

    public AdminLogin() throws IOException {
        this.setTitle("Admin Login");

        //labels and buttons
        labUser = new Label("Enter admin username: ");
        textUser = new TextField("Your username...");
        textUser.setMaxWidth(200);
        labPass = new Label("Enter password: ");
        passAdmin = new PasswordField();
        passAdmin.setMaxWidth(200);
        passAdmin.setPromptText("Your password...");
        labError = new Label();
        labError.setTextFill(Color.WHITE);
        //login button
        btnLogin = new Button("LOGIN");
        btnLogin.setOnAction(e -> {
            String username = textUser.getText();
            String password = passAdmin.getText();
            verify = readFile(username, password);

            if (verify) {
                this.hide();
                r1.showStage();
            }

            else {
                labError.setText("Incorrect username or password!");
            }
        });
        //back button
        btnBack = new Button("BACK");
        btnBack.setOnAction(e -> {
            this.hide();
            try {
                CandidateForm c1 = new CandidateForm();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        //set label and button styles
        try {
            r1.setTextFont(labUser, 30);
            r1.setTextFont(labPass, 30);
            r1.setTextFont(labError, 30);
            r1.setBtnStyle(btnLogin, 15);
            r1.setBtnStyle(btnBack, 15);
        }
        catch (FileNotFoundException e) {
            System.out.println("Font files not found!");
        }

        //button HBox
        HBox myHBox = new HBox(btnLogin, btnBack);
        myHBox.setSpacing(10);
        myHBox.setPadding(new Insets(10));
        myHBox.setAlignment(Pos.CENTER);

        //adding everything to VBox
        VBox myVBox = new VBox(labUser, textUser, labPass, passAdmin, myHBox, labError);
        myVBox.setSpacing(10);
        myVBox.setAlignment(Pos.CENTER);
        myVBox.setPadding(new Insets(10));

        //background image
        try {
            bg = new BackgroundImage(
                    new Image(new File("./data/bg5.jpg").toURI().toString(), WIDTH, HEIGHT, false, true),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    BackgroundSize.DEFAULT);
        }
        catch (Exception e) {
            System.out.println("Background image not found!");
        }
        myVBox.setBackground(new Background(bg));

        adminScene = new Scene(myVBox, WIDTH, HEIGHT);
        this.setResizable(false);
        this.setScene(adminScene);
    }

    //compares username and password
    public boolean verifyLogin(String user1, String user2, String pass1, String pass2) {
        boolean valid = false;

        if (user1.equals(user2) & pass1.equals(pass2)) {
            valid = true;
        }
        else {
            valid = false;
        }

        return valid;
    }

    //checks username and password against admin.txt
    public boolean readFile(String user, String pass) {
        Scanner scan;
        int totAdmin;
        String username = "";
        String password = "";
        boolean valid = false;

        try {
            scan = new Scanner(admin);
            String aLine = scan.nextLine();
            Scanner sline = new Scanner(aLine);
            totAdmin = Integer.parseInt(sline.next());

            for (int k = 1; k <= totAdmin; k++) {
                aLine = scan.nextLine();
                sline = new Scanner(aLine);
                sline.useDelimiter(":");
                username = sline.next();
                password = sline.next();

                valid = verifyLogin(user, username, pass, password);

                if (valid) {
                    break;
                }
            }
            sline.close();
            scan.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("File to read " + admin + " not found!");
        }

        return valid;
    }

    public void showStage() {
        this.show();
    }
}
