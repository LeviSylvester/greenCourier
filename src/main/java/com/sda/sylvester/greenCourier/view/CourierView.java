package com.sda.sylvester.greenCourier.view;

import com.sda.sylvester.greenCourier.model.Consignment;
import com.sda.sylvester.greenCourier.service.ConsignmentSaveService;
import com.sda.sylvester.greenCourier.service.CourierViewService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.text.Font;

public class CourierView extends Application {

    private static ConsignmentSaveService consignmentSaveService = new ConsignmentSaveService();

    private static VBox courierVBox = new VBox();
    private static Label courierOrdersTableViewLabel = new Label("Orders");
    private static TableView<Consignment> courierOrdersTableView = new TableView<>();
    private static TableColumn fromColumn = new TableColumn("From");
    private static TableColumn toColumn = new TableColumn("To");
    private static TableColumn termColumn = new TableColumn("Term");
    private static HBox courierAddOrderHBox = new HBox();
    private static Button refreshButton = new Button("Refresh");
    public static boolean autoRefresh = true;
    public static ObservableList<Consignment> consignmentsObservableList =
            FXCollections.observableArrayList();

    private static HBox observationsHBox = new HBox();
    private static Label observationsLabel = new Label("Observations: ");
    private static TextField observationsTextField = new TextField();
    private static Button sendButton = new Button("Send");

    private static Button statistics = new Button("Statistics");


    @Override
    public void start(Stage courierStage) throws Exception {
        courierStage.setTitle("Green Courier");
        courierStage.setWidth(360);
        courierStage.setHeight(640);
        courierStage.setX(790);
        courierStage.setY(64);

        buildRefreshButton();
        buildCourierOrdersTable();
        buildCourierAddOrderHBox();
        buildCourierPhoneAppSimulation();

        Scene courierScene = new Scene(courierVBox);
        courierStage.setScene(courierScene);
        courierStage.show();

        new Thread(CourierViewService.consignmentsAutoRefreshTask()).start();
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
                CourierViewService.refreshConsignmentsObservableList();
            }
        });

        courierAddOrderHBox.getChildren().addAll(
                fromTextField, toTextField, termTextField, addOrderButton
        );
        courierAddOrderHBox.setSpacing(3);
    }

    private static void buildRefreshButton() {
        refreshButton.setOnAction(event -> {
            CourierViewService.refreshConsignmentsObservableList();
        });
    }

    private void buildCourierPhoneAppSimulation() {
        observationsHBox.getChildren().addAll(observationsLabel, observationsTextField, sendButton);
        observationsHBox.setAlignment(Pos.CENTER);
        courierVBox.setBackground(new Background(new BackgroundFill(
                Paint.valueOf("#6B8779"), CornerRadii.EMPTY, Insets.EMPTY)));
        courierVBox.setSpacing(5);
        courierVBox.setPadding(new Insets(10, 11, 0, 11));
        courierVBox.getChildren().addAll(
                courierOrdersTableViewLabel,
                refreshButton,
                courierOrdersTableView,
                courierAddOrderHBox,
                observationsHBox,
                statistics);
    }
}