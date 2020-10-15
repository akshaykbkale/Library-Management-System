package application;
	


import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;



public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			System.out.println(getClass().getClassLoader());
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gui2.fxml"));
			//Image image = new Image("/library.png");
			//primaryStage.getIcons().add(image);
			primaryStage.getIcons().add(new Image("/application/library.png"));
	        primaryStage.setTitle("Library Database");
	        primaryStage.setScene(new Scene(root, 1125.0, 855.0));
	        primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
