package wang.relish.markvis;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(876);
        stage.setHeight(500);

        String path = getClass().getResource("/json.json").getPath();
        TableView table = DynamicClassGenerator.tableView(path);//tableView(getClass().getResource("/json.json").getPath());

        ((Group) scene.getRoot()).getChildren().addAll(table);

        stage.setScene(scene);
        stage.show();
    }
}
