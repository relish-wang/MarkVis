package wang.relish.markvis;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class TableViewSample extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(876);
        stage.setHeight(500);

        String json = Util.readJSONFromFile(getClass().getResource("/json.json").getPath());

        TableView table = Temp.tableView(json);

        ((Group) scene.getRoot()).getChildren().addAll(table);

        stage.setScene(scene);
        stage.show();
    }
}
