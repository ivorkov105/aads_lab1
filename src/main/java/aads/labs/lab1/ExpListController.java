package aads.labs.lab1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.stage.Stage;

import java.io.IOException;

public class ExpListController {

    public TextField elementInput;
    public ListView elementListView;
    public ComboBox<String> dataTypeComboBox;
    public Label errorLabel;
    public Label infoLabel;
    private ExpList<Object> expList = new ExpList<>();
    public TextField indexInput;

    @FXML
    private Button closeOrInitButton;

    @FXML
    public void initialize() {
        dataTypeComboBox.setItems(FXCollections.observableArrayList("Integer", "Double", "String"));
        dataTypeComboBox.getSelectionModel().selectFirst();
    }

    @FXML
    public void onAddButtonClick(ActionEvent actionEvent) {
        String selectedType = dataTypeComboBox.getValue();
        String input = elementInput.getText().trim();
        try {
            long startTime = System.nanoTime();
            Object element = parseInput(selectedType, input);
            expList.add(element);
            updateListView();
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            infoLabel.setText("Добавление элемента: " + duration + " наносекунд");
            errorLabel.setVisible(false);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void onDeleteButtonClick(ActionEvent actionEvent) {
        String input = elementInput.getText();
        Object element = parseInput(dataTypeComboBox.getValue(), input);
        try {
            long startTime = System.nanoTime();
            if (expList.has(element)) {
                expList.delete(element);
            }
            updateListView();
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            infoLabel.setText("Удаление элемента: " + duration + " наносекунд");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void onShowAllButtonClick(ActionEvent actionEvent) {
        try {
            if (expList.isNullOrEmpty()) {
                showError("Коллекция пуста. Нечего показывать.");
                return;
            }
            updateListView();
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void deleteAllElements(ActionEvent actionEvent) {
        try {
            long startTime = System.nanoTime();
            expList.clear();
            updateListView();
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            infoLabel.setText("Очистка коллекции: " + duration + " наносекунд");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private Object parseInput(String type, String input) {
        input = input.trim();
        try {
            return switch (type) {
                case "Integer" -> Integer.parseInt(input);
                case "Double" -> Double.parseDouble(input);
                case "String" -> input;
                default -> null;
            };
        } catch (NumberFormatException e) {
            errorLabel.setText("Неправильный формат данных: " + e.getMessage());
            return null;
        }
    }

    private void updateListView() {
        elementListView.getItems().clear();
        if (!expList.isNullOrEmpty()) {
            for (Object element : expList) {
                elementListView.getItems().add(element);
            }
        }
    }

    @FXML
    public void onGetElementClick(ActionEvent actionEvent) {
        try {
            int index = Integer.parseInt(indexInput.getText());
            long startTime = System.nanoTime();
            Object element = expList.get(index);
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            infoLabel.setText("Извлечение элемента на индексе " + index + ": " + duration + " наносекунд");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void onSetElementClick(ActionEvent actionEvent) {
        try {
            int index = Integer.parseInt(indexInput.getText());
            Object element = parseInput(dataTypeComboBox.getValue(), elementInput.getText());
            long startTime = System.nanoTime();
            Object oldElement = expList.set(index, element);
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            infoLabel.setText("Замена элемента " + oldElement + " на " + element + ": " + duration + " наносекунд");
            updateListView();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void onHasElementClick(ActionEvent actionEvent) {
        Object element = parseInput(dataTypeComboBox.getValue(), elementInput.getText());
        long startTime = System.nanoTime();
        boolean exists = expList.has(element);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        infoLabel.setText("Элемент " + element + (exists ? " найден." : " не найден.") + " Время: " + duration + " наносекунд");
    }

    @FXML
    public void onGetIndexClick(ActionEvent actionEvent) {
        Object element = parseInput(dataTypeComboBox.getValue(), elementInput.getText());
        long startTime = System.nanoTime();
        int index = expList.getId(element);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        infoLabel.setText("Индекс элемента " + element + ": " + (index >= 0 ? index : "не найден") + ". Время: " + duration + " наносекунд");
    }

    @FXML
    public void onAddByIndex(ActionEvent actionEvent) {
        Object element = parseInput(dataTypeComboBox.getValue(), elementInput.getText());
        int index = Integer.parseInt(indexInput.getText());
        long startTime = System.nanoTime();
        expList.add(element, index);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        infoLabel.setText("Вставка элемента " + element + " по индексу " + index + ": " + duration + " наносекунд");
    }

    @FXML
    public void getSize(ActionEvent actionEvent) {
        long startTime = System.nanoTime();
        int size = expList.size();
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        infoLabel.setText("Размер коллекции: " + size + ". Время: " + duration + " наносекунд");
    }

    @FXML
    public void onDeleteByIndex(ActionEvent actionEvent) {
        int index = Integer.parseInt(indexInput.getText());
        long startTime = System.nanoTime();
        expList.deleteByIndex(index);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        infoLabel.setText("Удаление элемента по индексу " + index + ": " + duration + " наносекунд");
    }

    @FXML
    public void onCloseOrInitColl(ActionEvent actionEvent) {
        try {
            long startTime = System.nanoTime();
            if (expList != null) {
                infoLabel.setText("Коллекция " + expList + " удалена");
                expList.close();
                expList = null;
                elementListView.getItems().clear();
            } else {
                expList = new ExpList<>();
                infoLabel.setText("Коллекция " + expList + " создана");
            }
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            infoLabel.setText(infoLabel.getText() + ". Время: " + duration + " наносекунд");
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    public void onOpenTestingWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ExpListApplication.class.getResource("testing_interface.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 640, 720);
        stage.setTitle("ExpListTesting");
        stage.setScene(scene);
        stage.show();
    }
}