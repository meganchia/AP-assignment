package assignment;

import javafx.scene.image.Image;

public class MyParam {

    private static String name;
    private static Image img;

    public static String getName() {
        return name;
    }

    public static void setName(String n) {
        name = n;
    }
    
    public static Image getImage() {
        return img;
    }
    
    public static void setImage(Image i) {
        img = i;
    }
}
