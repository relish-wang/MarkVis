package wang.relish.markvis;

import com.google.gson.Gson;
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

public class TableViewSample extends Application {

    private final TableView<UploadDataCompat> table = new TableView<UploadDataCompat>();
    private final ObservableList<UploadDataCompat> data = FXCollections.emptyObservableList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Table View Sample");
        stage.setWidth(800);
        stage.setHeight(500);

        String str = "[\n" +
                "  {\n" +
                "    \"launchCount\": \"1\",\n" +
                "    \"startTime\": \"1503984545114\",\n" +
                "    \"endTime\": \"1503986652918\",\n" +
                "    \"filter\": \"*\",\n" +
                "    \"size\": \"16988\",\n" +
                "    \"networkType\": \"0\",\n" +
                "    \"reqCount\": \"32\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"launchCount\": \"1\",\n" +
                "    \"startTime\": \"1503984545114\",\n" +
                "    \"endTime\": \"1503986652918\",\n" +
                "    \"filter\": \"*.*\",\n" +
                "    \"size\": \"7923\",\n" +
                "    \"networkType\": \"2\",\n" +
                "    \"reqCount\": \"13\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"launchCount\": \"1\",\n" +
                "    \"startTime\": \"1503984545114\",\n" +
                "    \"endTime\": \"1503986652918\",\n" +
                "    \"filter\": \"*.*\",\n" +
                "    \"size\": \"7969\",\n" +
                "    \"networkType\": \"1\",\n" +
                "    \"reqCount\": \"19\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"launchCount\": \"1\",\n" +
                "    \"startTime\": \"1503984545114\",\n" +
                "    \"endTime\": \"1503986652918\",\n" +
                "    \"filter\": \"*.souche.com\",\n" +
                "    \"size\": \"4\",\n" +
                "    \"networkType\": \"2\",\n" +
                "    \"reqCount\": \"3\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"launchCount\": \"1\",\n" +
                "    \"startTime\": \"1503984545114\",\n" +
                "    \"endTime\": \"1503986652918\",\n" +
                "    \"filter\": \"*.souche.com\",\n" +
                "    \"size\": \"0\",\n" +
                "    \"networkType\": \"1\",\n" +
                "    \"reqCount\": \"0\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"launchCount\": \"1\",\n" +
                "    \"startTime\": \"1503984545114\",\n" +
                "    \"endTime\": \"1503986652918\",\n" +
                "    \"filter\": \"*.upload.souche.com\",\n" +
                "    \"size\": \"0\",\n" +
                "    \"networkType\": \"2\",\n" +
                "    \"reqCount\": \"0\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"launchCount\": \"1\",\n" +
                "    \"startTime\": \"1503984545114\",\n" +
                "    \"endTime\": \"1503986652918\",\n" +
                "    \"filter\": \"*.upload.souche.com\",\n" +
                "    \"size\": \"0\",\n" +
                "    \"networkType\": \"1\",\n" +
                "    \"reqCount\": \"0\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"launchCount\": \"1\",\n" +
                "    \"startTime\": \"1503984545114\",\n" +
                "    \"endTime\": \"1503986652918\",\n" +
                "    \"filter\": \"qxu1606440290.my3w.com\",\n" +
                "    \"size\": \"1\",\n" +
                "    \"networkType\": \"2\",\n" +
                "    \"reqCount\": \"2\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"launchCount\": \"1\",\n" +
                "    \"startTime\": \"1503984545114\",\n" +
                "    \"endTime\": \"1503986652918\",\n" +
                "    \"filter\": \"qxu1606440290.my3w.com\",\n" +
                "    \"size\": \"3\",\n" +
                "    \"networkType\": \"1\",\n" +
                "    \"reqCount\": \"4\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"launchCount\": \"1\",\n" +
                "    \"startTime\": \"1503984545114\",\n" +
                "    \"endTime\": \"1503986652918\",\n" +
                "    \"filter\": \"www.httpwatch.com\",\n" +
                "    \"size\": \"33\",\n" +
                "    \"networkType\": \"2\",\n" +
                "    \"reqCount\": \"1\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"launchCount\": \"1\",\n" +
                "    \"startTime\": \"1503984545114\",\n" +
                "    \"endTime\": \"1503986652918\",\n" +
                "    \"filter\": \"www.httpwatch.com\",\n" +
                "    \"size\": \"66\",\n" +
                "    \"networkType\": \"1\",\n" +
                "    \"reqCount\": \"2\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"launchCount\": \"1\",\n" +
                "    \"startTime\": \"1503984545114\",\n" +
                "    \"endTime\": \"1503986652918\",\n" +
                "    \"filter\": \"alissl.ucdl.pp.uc.cn\",\n" +
                "    \"size\": \"7867\",\n" +
                "    \"networkType\": \"2\",\n" +
                "    \"reqCount\": \"1\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"launchCount\": \"1\",\n" +
                "    \"startTime\": \"1503984545114\",\n" +
                "    \"endTime\": \"1503986652918\",\n" +
                "    \"filter\": \"alissl.ucdl.pp.uc.cn\",\n" +
                "    \"size\": \"7867\",\n" +
                "    \"networkType\": \"1\",\n" +
                "    \"reqCount\": \"1\"\n" +
                "  }\n" +
                "]";

        UploadData[] uploadData = new Gson().fromJson(str, UploadData[].class);


        ObservableList<UploadDataCompat> items = getItems(uploadData);


        table.setItems(items);

        TableColumn[] generate = generate(UploadData.class);
        table.getColumns().addAll(generate);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
    }

    private static TableColumn[] generate(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        TableColumn[] tableColumns = new TableColumn[fields.length];
        for (int i = 0; i < fields.length; i++) {
            tableColumns[i] = new TableColumn(fields[i].getName());
            tableColumns[i].setCellValueFactory(new PropertyValueFactory<UploadDataCompat, SimpleObjectProperty>(fields[i].getName()));
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
