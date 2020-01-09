package com.sda.sylvester.greenCourier.view;

import com.sda.sylvester.greenCourier.model.Consignment;
import com.sda.sylvester.greenCourier.service.ConsignmentDisplayService;
import com.sda.sylvester.greenCourier.service.ConsignmentSaveService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.concurrent.TimeUnit;

public class CourierView extends Application {

    private static VBox courierVBox = new VBox();
    private static ConsignmentDisplayService consignmentDisplayService = new ConsignmentDisplayService();
    private static ObservableList<Consignment> consignmentsObservableList =
            FXCollections.observableArrayList(consignmentDisplayService.getAllConsignments());
    private static ConsignmentSaveService consignmentSaveService = new ConsignmentSaveService();
    private static Label courierOrdersTableViewLabel = new Label("Orders");
    private static Button refreshButton = new Button("Refresh");
    private static TableView<Consignment> courierOrdersTableView = new TableView<>();
    private static TableColumn fromColumn = new TableColumn("From");
    private static TableColumn toColumn = new TableColumn("To");
    private static TableColumn termColumn = new TableColumn("Term");
    private static HBox courierAddOrderHBox = new HBox();
    private static boolean autoRefresh = true;

    @Override
    public void start(Stage courierStage) throws Exception {
        courierStage.setTitle("BikeCourier");
        courierStage.setWidth(360);
        courierStage.setHeight(640);
        courierStage.setX(790);
        courierStage.setY(64);

        buildRefreshButton();
        buildCourierOrdersTable();
        buildCourierAddOrderHBox();
        buildCourierPhoneAppSimulation();

        Task autoRefreshTask = createAutoRefreshTask();

        Scene courierScene = new Scene(courierVBox);
        courierStage.setScene(courierScene);
        courierStage.show();

        new Thread(autoRefreshTask).start();
    }

    @Override
    public void stop() throws Exception {
        autoRefresh = false;
    }

    private static void buildCourierOrdersTable() {
        courierOrdersTableViewLabel.setFont(new Font("Arial", 20));
        courierOrdersTableView.setEditable(true);

        fromColumn.setMinWidth(100);
        fromColumn.setCellValueFactory(
                new PropertyValueFactory<Consignment, String>("consigner"));
        fromColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        fromColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Consignment, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Consignment, String> t) {
                        ((Consignment) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setConsigner(t.getNewValue());
                    }
                }
        );

        toColumn.setMinWidth(100);
        toColumn.setCellValueFactory(
                new PropertyValueFactory<Consignment, String>("consignee"));
        toColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        toColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Consignment, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Consignment, String> t) {
                        ((Consignment) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setConsignee(t.getNewValue());
                    }
                }
        );

        termColumn.setMinWidth(100);
        termColumn.setCellValueFactory(
                new PropertyValueFactory<Consignment, String>("terminus"));
        termColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        termColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Consignment, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Consignment, String> t) {
                        ((Consignment) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setTerminus(t.getNewValue());
                    }
                }
        );

        courierOrdersTableView.setItems(consignmentsObservableList);
        courierOrdersTableView.getColumns().addAll(fromColumn, toColumn, termColumn);
    }

    private static void buildCourierAddOrderHBox() {
        final TextField fromTextField = new TextField();
        fromTextField.setPromptText("From");
        fromTextField.setMaxWidth(fromColumn.getPrefWidth());
        final TextField toTextField = new TextField();
        toTextField.setMaxWidth(toColumn.getPrefWidth());
        toTextField.setPromptText("To");
        final TextField termTextField = new TextField();
        termTextField.setMaxWidth(termColumn.getPrefWidth());
        termTextField.setPromptText("Term");

        final Button addOrderButton = new Button("Add order ");
        addOrderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                consignmentSaveService.saveConsignment(new Consignment(
                        fromTextField.getText(),
                        toTextField.getText(),
                        termTextField.getText()));
                fromTextField.clear();
                toTextField.clear();
                termTextField.clear();
                refreshConsignmentsObservableList();
            }
        });

        courierAddOrderHBox.getChildren().addAll(fromTextField, toTextField, termTextField, addOrderButton);
        courierAddOrderHBox.setSpacing(3);
    }

    private static void buildRefreshButton() {
        refreshButton.setOnAction(event -> {
            refreshConsignmentsObservableList();
        });
    }

    protected static void refreshConsignmentsObservableList() {
        consignmentsObservableList.setAll(consignmentDisplayService.getAllConsignments());
    }

    private static void waitSeconds(int unit) {
        try {
//            Thread.sleep(unit*1000);
            TimeUnit.SECONDS.sleep(unit);
        } catch (InterruptedException s) {
            System.out.println("slept " + unit + " sec");
        }
    }

    private void buildCourierPhoneAppSimulation() {
        courierVBox.setSpacing(5);
        courierVBox.setPadding(new Insets(10, 11, 0, 11));
        courierVBox.getChildren().addAll(
                courierOrdersTableViewLabel,
                refreshButton,
                courierOrdersTableView,
                courierAddOrderHBox);
    }

    private Task createAutoRefreshTask() {
        return new Task() {
            @Override
            public String call() throws Exception {
                while (autoRefresh) {
                    waitSeconds(5);
                    refreshConsignmentsObservableList();
                }
                return "refreshing consignments";
            }
        };
    }
}