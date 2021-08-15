package assignment;

import javafx.scene.image.Image;

public class MyFlag {

	private static String name;
	private static Image flag;

	public static String getName() {
		return name;
	}

	public static void setName(String n) {
		name = n;
	}

	public static Image getImage() {
		return flag;
	}

	public static void setImage(Image f) {
		flag = f;
	}
}
