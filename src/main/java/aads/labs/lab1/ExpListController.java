package aads.labs.lab1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;

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
            Object element = parseInput(selectedType, input);
            expList.add(element);
            updateListView();
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
            if (expList.has(element)) {
                expList.deleteByIndex(element);
            }
            updateListView();
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
            expList.clear();
            updateListView();
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
            System.out.println("Parsing input: " + input + " as " + type);
            return switch (type) {
                case "Integer" -> Integer.parseInt(input);
                case "Double" -> Double.parseDouble(input);
                case "String" -> input;
                default -> null;
            };
        } catch (NumberFormatException e) {
            System.out.println("Failed to parse input: " + input);
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
            Object element = expList.get(index);
            infoLabel.setText("Элемент на индексе " + index + ": " + element);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void onSetElementClick(ActionEvent actionEvent) {
        try {
            int index = Integer.parseInt(indexInput.getText());
            Object element = parseInput(dataTypeComboBox.getValue(), elementInput.getText());
            Object oldElement = expList.set(index, element);
            infoLabel.setText("Элемент " + oldElement + " заменён на " + element);
            updateListView();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void onHasElementClick(ActionEvent actionEvent) {
        Object element = parseInput(dataTypeComboBox.getValue(), elementInput.getText());
        boolean exists = expList.has(element);
        infoLabel.setText("Элемент " + element + (exists ? " найден." : " не найден."));
    }

    @FXML
    public void onGetIndexClick(ActionEvent actionEvent) {
        Object element = parseInput(dataTypeComboBox.getValue(), elementInput.getText());
        int index = expList.getId(element);
        infoLabel.setText("Индекс элемента " + element + ": " + (index >= 0 ? index : "не найден"));
    }

    @FXML
    public void onAddByIndex(ActionEvent actionEvent) {
        Object element = parseInput(dataTypeComboBox.getValue(), elementInput.getText());
        int index = Integer.parseInt(indexInput.getText());
        expList.add(element, index);
        infoLabel.setText("Вставка элемента " + element + " по индексу " + index);
    }

    @FXML
    public void getSize(ActionEvent actionEvent) {
        int size = expList.size();
        infoLabel.setText("Размер: " + size);
    }

    @FXML
    public void onDeleteByIndex(ActionEvent actionEvent) {
        int index = Integer.parseInt(indexInput.getText());
        expList.deleteByIndex(index);
    }

    @FXML
    public void onCloseOrInitColl(ActionEvent actionEvent) {
        try {
            if (expList != null) {
                infoLabel.setText("Коллекция " + expList + " удалена");
                expList.close();
                expList = null;
                closeOrInitButton.setText("Создать коллекцию");
            } else {
                expList = new ExpList<>();
                infoLabel.setText("Коллекция " + expList + " создана");
                closeOrInitButton.setText("Удалить коллекцию");
            }
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }
}