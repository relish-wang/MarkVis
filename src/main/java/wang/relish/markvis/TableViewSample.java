package wang.relish.markvis;

import com.google.gson.Gson;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TableViewSample extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    TableView table;

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(876);
        stage.setHeight(500);

        String pathResource = getClass().getResource("/").getPath();
        File[] files = new File(pathResource).listFiles();
        List<File> fileList = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    fileList.add(file);
                }
            }
        }

        ComboBox<File> comboBox = new ComboBox<File>();
        comboBox.getItems().addAll(fileList);

        comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            stage.close();

            String path = newValue.getAbsolutePath();
            String str = Util.readJSONFromFile(path);
            table = tableView(str);
            final VBox vbox = new VBox(8);
            vbox.getChildren().addAll(comboBox, table);

            ((Group) scene.getRoot()).getChildren().addAll(vbox);

            stage.setScene(scene);
            stage.show();
        });


        String str = Util.readJSONFromFile(getClass().getResource("/json1.json").getPath());

        table = tableView(str);


        final VBox vbox = new VBox(8);
        vbox.getChildren().addAll(comboBox, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

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


    private static void showChoiceDialog() {
        List<String> choices = new ArrayList<>();
        choices.add("a");
        choices.add("b");
        choices.add("c");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("b", choices);
        dialog.setTitle("Choice Dialog");
        dialog.setHeaderText("Look, a Choice Dialog");
        dialog.setContentText("Choose your letter:");

// Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            System.out.println("Your choice: " + result.get());
        }

// The Java 8 way to get the response value (with lambda expression).
        result.ifPresent(letter -> System.out.println("Your choice: " + letter));
    }

    interface OnFileSelectedListener {
        void onFileSelected(File file);
    }
}
