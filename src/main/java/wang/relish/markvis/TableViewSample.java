package wang.relish.markvis;

import com.google.gson.Gson;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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

        String str = Util.readJSONFromFile(getClass().getResource("/json.json").getPath());

        TableView table = tableView(str);

        ((Group) scene.getRoot()).getChildren().addAll(table);

        stage.setScene(scene);
        stage.show();
    }

    private static TableView tableView(String jsonStr) {
        Bean[] uploadData = new Gson().fromJson(jsonStr, Bean[].class);

        ObservableList<BeanCompat> items = getItems(uploadData);
        TableView<BeanCompat> table = new TableView<BeanCompat>();
        table.setItems(items);
        TableColumn[] generate = generate(Bean.class);
        table.getColumns().addAll(generate);
        table.setMinWidth(876);

        return table;
    }

    private static TableColumn[] generate(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        TableColumn[] tableColumns = new TableColumn[fields.length];
        for (int i = 0; i < fields.length; i++) {
            tableColumns[i] = new TableColumn(fields[i].getName());
            tableColumns[i].setCellValueFactory(new PropertyValueFactory<BeanCompat, SimpleObjectProperty>(fields[i].getName()));
        }
        return tableColumns;
    }

    private static List<BeanCompat> compat(Bean[] uploadData) {
        List<BeanCompat> beanCompats = new ArrayList<BeanCompat>();
        for (Bean uploadDatum : uploadData) {
            beanCompats.add(new BeanCompat(uploadDatum));
        }
        return beanCompats;
    }

    private static ObservableList<BeanCompat> getItems(Bean[] uploadData) {
        return new ObservableListWrapper<BeanCompat>(compat(uploadData));
    }
}
