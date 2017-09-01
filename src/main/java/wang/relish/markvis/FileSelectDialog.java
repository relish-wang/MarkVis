package wang.relish.markvis;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.io.File;
import java.util.List;

/**
 * @author relish
 * @since 2017/09/01
 */
public class FileSelectDialog extends Dialog {

    private OnItemClickListener mListener;


    public FileSelectDialog(List<File> fileList) {

        ObservableList<File> files = FXCollections.observableArrayList(fileList);
        ListView<File> fileListView = new ListView<File>(files);
        fileListView.setItems(files);
        fileListView.setPrefSize(400, 200);
        fileListView.setCellFactory((ListView<File> l) -> new FileCell());
        fileListView.setEditable(false);

    }

    class FileCell extends ListCell<File> {
        @Override
        protected void updateItem(File item, boolean empty) {
            super.updateItem(item, empty);
            Label label = new Label(item.getName());
            label.setOnMouseClicked(event -> {
                if (mListener != null) {
                    mListener.onItemClick(item.getAbsolutePath());
                }
                FileSelectDialog.this.close();
            });
            FileSelectDialog.this.setGraphic(label);
        }
    }

    public OnItemClickListener getOnItemClickListener() {
        return mListener;
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    interface OnItemClickListener {
        void onItemClick(String path);
    }
}
