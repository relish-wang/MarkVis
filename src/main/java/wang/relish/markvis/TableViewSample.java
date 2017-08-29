package wang.relish.markvis;

import com.sun.istack.internal.NotNull;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        TableView table = tableView("/Users/relish/IdeaProjects/MarkVis/src/main/resources/json.json");

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
    }

    private static TableView tableView(@NotNull String path) {
        TableView<Map<String, String>> table = new TableView<Map<String, String>>();
        List<Map<String, String>> maps = Util.jsonArrToList(path);
        ObservableList<Map<String, String>> items = FXCollections.observableArrayList();
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

    private static TableColumn[] generate(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        TableColumn[] tableColumns = new TableColumn[fields.length];
        for (int i = 0; i < fields.length; i++) {
            tableColumns[i] = new TableColumn(fields[i].getName());
            //noinspection unchecked
            tableColumns[i].setCellValueFactory(new PropertyValueFactory<UploadDataCompat, SimpleObjectProperty>(fields[i].getName()));
        }
        return tableColumns;
    }

    private static TableColumn[] generate(Set<String> set) {
        TableColumn[] tableColumns = new TableColumn[set.size()];
        int i = 0;
        for (String key : set) {
            int index = i++;
            tableColumns[index] = new TableColumn(key);
            //noinspection unchecked
            tableColumns[index].setCellValueFactory(new PropertyValueFactory<UploadDataCompat, SimpleObjectProperty>(key));
        }
        return tableColumns;
    }

    private static List<UploadDataCompat> compat(UploadData[] uploadData) {
        List<UploadDataCompat> uploadDataCompats = new ArrayList<UploadDataCompat>();
        for (UploadData uploadDatum : uploadData) {
            uploadDataCompats.add(new UploadDataCompat(uploadDatum));
        }
        return uploadDataCompats;
    }

    private static ObservableList<UploadDataCompat> getItems(UploadData[] uploadData) {
        return new ObservableListWrapper<UploadDataCompat>(compat(uploadData));
    }
}
