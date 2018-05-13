package base;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utility.StringUtils;

public class Main extends Application {

    public static String Path = "";
    private static final Class refrence = Main.class;
    private static String PATH_TO_JAR;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("FraudTek Launcher");
        primaryStage.setScene(new Scene(root, 480, 140, new Color(0.18, 0.18, 0.18, 1.0)));
        primaryStage.setResizable(false);
        //Before Show load data into the screen

        initialize();
        //
        primaryStage.show();
    }

    public void initialize(){
        Gson gson = new Gson();
        JsonObject launchOptions = gson.fromJson(StringUtils.unify(StringUtils.loadData(Main.Path+"/Scripting/launch.json")), JsonObject.class);
        if(launchOptions.has("path")){
            PATH_TO_JAR = gson.fromJson(launchOptions.get("path"), String.class);
            System.out.println("Game Path:"+PATH_TO_JAR);
        }
    }

    public static String getPathToJAR() {
        return PATH_TO_JAR;
    }


    public static void main(String[] args) {
        //set reference
        Main.Path = StringUtils.removeEXEandJAR(StringUtils.getRelativePath(refrence));
        System.out.println(Path);
        launch(args);
    }
}
