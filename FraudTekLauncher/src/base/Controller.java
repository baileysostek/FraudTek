package base;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import utility.StringUtils;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public ChoiceBox resolutions;
    @FXML
    public CheckBox fullscreen;
    @FXML
    public ImageView backgroundIMG;

    public boolean isFullscreen = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> entries = new LinkedList<>();

        Gson gson = new Gson();
        JsonObject launchOptions = gson.fromJson(StringUtils.unify(StringUtils.loadData(Main.Path+"/Scripting/launch.json")), JsonObject.class);

        int last_width = gson.fromJson(launchOptions.get("width"), Integer.class);
        int last_height = gson.fromJson(launchOptions.get("height"), Integer.class);

        System.out.println("Looking for resolution: "+last_width+"x"+last_height);

        int selectIndex = 0;

        if(launchOptions.has("aspectRatios")){
            System.out.println(launchOptions);
            JsonArray ratios =  gson.fromJson(launchOptions.get("aspectRatios"), JsonArray.class);
            for(int i = 0; i < ratios.size(); i++){
                JsonObject ratio = gson.fromJson(ratios.get(i), JsonObject.class);
                int width = gson.fromJson(ratio.get("width"), Integer.class);
                int height = gson.fromJson(ratio.get("height"), Integer.class);
                entries.add(width+" x "+height);
                //For setting selected on load.
                if(last_width == width && last_height == height){
                    selectIndex = i;
                }
            }
        }

        ObservableList list = FXCollections.observableList(entries);
        resolutions.setItems(list);

        resolutions.getSelectionModel().select(selectIndex);

        //Image
        try {
            FileInputStream input = new FileInputStream(Main.Path+"/Images/launcher_bg.png");
            Image image = new Image(input);
            backgroundIMG.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void launchGame(ActionEvent actionEvent) {
        String text = resolutions.getSelectionModel().getSelectedItem().toString().replaceAll(" x ", "x");
        String[] launchArgs = text.split("x");

        int WIDTH = Integer.parseInt(launchArgs[0]);
        int HEIGHT = Integer.parseInt(launchArgs[1]);

        try {
            Runtime.getRuntime().exec("cmd /c \"java -jar "+((Main.Path.replaceFirst("/", ""))+Main.getPathToJAR())+" "+WIDTH+" "+HEIGHT+" "+isFullscreen+"\"");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void setFullScreen(ActionEvent actionEvent) {
        isFullscreen = fullscreen.isSelected();
    }
}
