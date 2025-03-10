package aads.labs.lab1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

public class TestingController {
    @FXML private TextField elementsCountInput;
    @FXML private Label testResultLabel;
    @FXML private VBox container;

    private ExpList<Object> expList = new ExpList<>();

    private List<XYChart.Data<Number, Number>> insertData = new ArrayList<>();
    private List<XYChart.Data<Number, Number>> retrieveData = new ArrayList<>();
    private List<XYChart.Data<Number, Number>> deleteData = new ArrayList<>();

    private LineChart<Number, Number> insertChart;
    private LineChart<Number, Number> retrieveChart;
    private LineChart<Number, Number> deleteChart;

    public TestingController() {
        insertChart = createChart("Вставка");
        retrieveChart = createChart("Извлечение");
        deleteChart = createChart("Удаление");
    }

    private LineChart<Number, Number> createChart(String title) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(title);
        return chart;
    }

    @FXML
    public void onTestInsert(ActionEvent actionEvent) {
        performTest(insertData, "Вставка", i -> expList.add(i));
    }

    @FXML
    public void onTestRetrieve(ActionEvent actionEvent) {
        performTest(retrieveData, "Извлечение", expList::get);
    }

    @FXML
    public void onTestDelete(ActionEvent actionEvent) {
        performTest(deleteData, "Удаление", expList::deleteByIndex);
    }

    private void performTest(List<XYChart.Data<Number, Number>> dataList, String operation, Operation op) {
        try {
            int inputCount = Integer.parseInt(elementsCountInput.getText());
            long start = System.nanoTime();
            for (int i = 0; i < inputCount; i++) {
                op.execute(i);
            }
            long end = System.nanoTime();
            long opTime = end - start;
            testResultLabel.setText(operation + ": " + opTime + " наносекунд");

            dataList.add(new XYChart.Data<>(opTime, inputCount));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void onCreateInsert(ActionEvent actionEvent) {
        createChartFromData(insertChart, insertData);
    }

    @FXML
    public void onCreateRetrieve(ActionEvent actionEvent) {
        createChartFromData(retrieveChart, retrieveData);
    }

    @FXML
    public void onCreateDelete(ActionEvent actionEvent) {
        createChartFromData(deleteChart, deleteData);
    }

    private void createChartFromData(LineChart<Number, Number> chart, List<XYChart.Data<Number, Number>> dataList) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().addAll(dataList);
        chart.getData().clear();
        chart.getData().add(series);
        if (!container.getChildren().contains(chart)) {
            container.getChildren().add(chart);
        }
    }

    @FunctionalInterface
    private interface Operation {
        void execute(int i);
    }
}
