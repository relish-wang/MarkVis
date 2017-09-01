package wang.relish.markvis;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;

public class SpinnerApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    public Parent createContent() {

        HBox intBlock = new HBox(30);
        HBox stringBlock = new HBox(30);
        HBox doubleBlock = new HBox(30);

        String[] styles = {
                "spinner", //默认箭头按钮是垂直居右
                Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL,
                Spinner.STYLE_CLASS_ARROWS_ON_LEFT_VERTICAL,
                Spinner.STYLE_CLASS_ARROWS_ON_LEFT_HORIZONTAL,
                Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL,
                Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL
        };

        Spinner[] intSpinners = new Spinner[styles.length];
        Spinner[] stringSpinners = new Spinner[styles.length];
        Spinner[] doubleSpinners = new Spinner[styles.length];

        for (int i = 0; i < styles.length; i++) {
            //Integer Spinner  
            SpinnerValueFactory svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99);
            Spinner sp = new Spinner();
            sp.setValueFactory(svf);//设置Value Factory  
            sp.getStyleClass().add(styles[i]);//设置箭头按钮的位置样式  
            sp.setPrefWidth(80);
            sp.setEditable(true);//设置可编辑  
            intSpinners[i] = sp;

            //Double Spinner  
            svf = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 10.0, 0.5, 0.01);
            sp = new Spinner();
            sp.setValueFactory(svf);
            sp.getStyleClass().add(styles[i]);
            sp.setPrefWidth(90);
            doubleSpinners[i] = sp;

            //String Spinner  
            ObservableList<String> items = FXCollections.observableArrayList("Grace", "Matt", "Katie");
            svf = new SpinnerValueFactory.ListSpinnerValueFactory<String>(items);
            sp = new Spinner();
            sp.setValueFactory(svf);
            sp.setPrefWidth(100);
            sp.getStyleClass().add(styles[i]);
            if (i % 2 == 0) {
                sp.getValueFactory().setWrapAround(true);//循环切换：当前进到最后一个值时，再点前进是就显示第一个值  
            }
            stringSpinners[i] = sp;
        }

        intBlock.getChildren().addAll(Arrays.asList(intSpinners));
        doubleBlock.getChildren().addAll(Arrays.asList(doubleSpinners));
        stringBlock.getChildren().addAll(Arrays.asList(stringSpinners));

        //<editor-fold defaultstate="collapsed" desc="自定义Value Facroty，显示2的X次方，对X进行步进">  
        SpinnerValueFactory svf = new SpinnerValueFactory<Double>() {
            int power = 0;

            @Override
            public void decrement(int steps) {
                power--;
                updateValue();
            }

            @Override
            public void increment(int steps) {
                power++;
                updateValue();
            }

            public void updateValue() {
                setValue(Math.pow(2, power));
            }
        };
        svf.setValue(1);
        Spinner sp = new Spinner(svf);
        sp.setPrefWidth(100);
        //</editor-fold>  

        return new VBox(25, intBlock, doubleBlock, stringBlock, sp);
    }
}  