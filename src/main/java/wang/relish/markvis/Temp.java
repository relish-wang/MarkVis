
package wang.relish.markvis;

import com.google.gson.Gson;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class Temp {
    public static List compat(Object[] been) {
        List<BeanCompat> data = new ArrayList<BeanCompat>();
        for (Object datum : been) {
            data.add(new BeanCompat((Bean) datum));
        }
        return data;
    }

    public static ObservableList getItems(Object[] been) {
        return new ObservableListWrapper<BeanCompat>(compat(been));
    }

    public static TableColumn[] generateTableColumn(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        TableColumn[] tableColumns = new TableColumn[fields.length];
        for (int i = 0; i < fields.length; i++) {
            tableColumns[i] = new TableColumn(fields[i].getName());
            tableColumns[i].setCellValueFactory(new PropertyValueFactory<BeanCompat, SimpleObjectProperty>(fields[i].getName()));
        }
        return tableColumns;
    }

    public static TableView tableView(String json) {
        Bean[] been = new Gson().fromJson(json, Bean[].class);
        ObservableList<BeanCompat> items = getItems(been);
        TableView<BeanCompat> table = new TableView<BeanCompat>();
        table.setItems(items);
        TableColumn[] generate = generateTableColumn(Bean.class);
        table.getColumns().addAll(generate);
        table.setMinWidth(876);
        return table;
    }
}