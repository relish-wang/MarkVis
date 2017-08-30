package wang.relish.markvis;

import com.sun.istack.internal.NotNull;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(876);
        stage.setHeight(500);

        TableView table = tableView(getClass().getResource("/json.json").getPath());

        ((Group) scene.getRoot()).getChildren().addAll(table);

        stage.setScene(scene);
        stage.show();
    }

    private static TableView tableView(@NotNull String path) {
        TableView<Map<String, String>> table = new TableView<Map<String, String>>();
        List<Map<String, String>> maps = Util.jsonArrToList(path);
        ObservableList<Map<String, String>> items = FXCollections.observableArrayList();

        @SuppressWarnings("AccessStaticViaInstance")
        Class<?> clazz = DynamicClassGenerator.getInstance().generate(maps.get(0)); //Bean

        for (Map<String, String> map : maps) {
            //noinspection UseBulkOperation
            items.add(map);
        }
        table.setItems(items);
        table.setMinWidth(876);

        TableColumn[] generate = generate(maps.get(0).keySet());
        //noinspection unchecked
        table.getColumns().addAll(generate);
        return table;
    }

    private static TableColumn[] generate(Set<String> set) {
        TableColumn[] tableColumns = new TableColumn[set.size()];
        int i = 0;
        for (String key : set) {
            int index = i++;
            tableColumns[index] = new TableColumn(key);
            //noinspection unchecked
            tableColumns[index].setCellValueFactory(new PropertyValueFactory<BeanCompat, SimpleObjectProperty>(key));
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
