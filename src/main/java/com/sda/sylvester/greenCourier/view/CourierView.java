package com.sda.sylvester.greenCourier.view;

import com.sda.sylvester.greenCourier.model.Consignment;
import com.sda.sylvester.greenCourier.model.Status;
import com.sda.sylvester.greenCourier.service.ConsignmentDisplayService;
import com.sda.sylvester.greenCourier.service.ConsignmentSaveService;
import com.sda.sylvester.greenCourier.service.ConsignmentRefreshService;
import com.sda.sylvester.greenCourier.service.IllegalOperationException;
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
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.text.Font;

import static com.sda.sylvester.greenCourier.service.ConsignmentRefreshService.selectedConsignmentBeforeRefresh;

public class CourierView extends Application {

    private static Consignment selectedConsignment;
    private static ConsignmentDisplayService consignmentDisplayService = new ConsignmentDisplayService();
    private static ConsignmentSaveService consignmentSaveService = new ConsignmentSaveService();

    private static VBox courierVBox = new VBox();
    private static Label courierOrdersTableViewLabel = new Label("Orders");
    private static Button refreshButton = new Button("Refresh");
    private static TableColumn fromColumn = new TableColumn("From");
    private static TableColumn toColumn = new TableColumn("To");
    private static TableColumn termColumn = new TableColumn("Term");

    private static HBox selectedOrderHBox = new HBox();
    private static Label selectedOrderLabel = new Label("Selected: ");
    private static Label from = new Label();
    private static Label to = new Label();
    private static Label term = new Label();
    private static Button okButton = new Button("Ok");

    private static HBox courierAddOrderHBox = new HBox();

    private static HBox observationsHBox = new HBox();
    private static Label observationsLabel = new Label("Observations: ");
    private static TextField observationsTextField = new TextField();
    private static Button sendButton = new Button("Send");

    private static Button statistics = new Button("Statistics");

    public static int selectedRow = 0;
    public static boolean courierAutoRefresh = true;
    public static ObservableList<Consignment> consignmentsObservableList =
            FXCollections.observableArrayList();
    public static TableView<Consignment> courierOrdersTableView = new TableView<>();

    @Override
    public void start(Stage courierStage) throws Exception {
        courierStage.setTitle("Green Courier");
        courierStage.setWidth(360);
        courierStage.setHeight(640);
        courierStage.setX(790);
        courierStage.setY(64);

        buildRefreshButton();
        buildCourierOrdersTable();
        buildSelectedOrderHBox();
        buildCourierAddOrderHBox();
        buildObservationsHBox();
        buildCourierPhoneAppSimulation();

        Scene courierScene = new Scene(courierVBox);
        courierStage.setScene(courierScene);
        courierStage.show();

        new Thread(ConsignmentRefreshService.consignmentsAutoRefreshTask()).start();
    }

    @Override
    public void stop() throws Exception {
        courierAutoRefresh = false;
    }

    public static Consignment getSelectedConsignment() {
        return selectedConsignment;
    }

    public static void preserveCourierOrdersTableViewSelection() {
        if (selectedConsignmentBeforeRefresh != null) {
//            courierOrdersTableView.requestFocus();
            courierOrdersTableView.getSelectionModel().select(selectedRow);
//            courierOrdersTableView.getFocusModel().focus(selectedRow);
        }
    }

    private static void buildRefreshButton() {
        refreshButton.setOnAction(event -> {
            ConsignmentRefreshService.refreshConsignmentsObservableList();
            preserveCourierOrdersTableViewSelection();
        });
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

        courierOrdersTableView.getColumns().addAll(fromColumn, toColumn, termColumn);
        courierOrdersTableView.setItems(consignmentsObservableList);
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
                try {
                    consignmentSaveService.saveConsignment(new Consignment(
                            fromTextField.getText(),
                            toTextField.getText(),
                            termTextField.getText(),
                            observationsTextField.getText()
                    ));
                } catch (IllegalOperationException exception) {
                    System.out.println("Unable to save consignment");
                    courierVBox.getChildren().add(new Label("Unable to save order, try again!"));
                }
                fromTextField.clear();
                toTextField.clear();
                termTextField.clear();
                observationsTextField.clear();
                ConsignmentRefreshService.refreshConsignmentsObservableList();
            }
        });

        courierAddOrderHBox.getChildren().addAll(
                fromTextField, toTextField, termTextField, addOrderButton
        );
        courierAddOrderHBox.setSpacing(3);
    }

    private static void observeAndCompleteSelectedOrder() {
        courierOrdersTableView.getSelectionModel().selectedItemProperty().
                addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        selectedRow = courierOrdersTableView.getSelectionModel().getSelectedIndex();
                        selectedConsignment = newSelection;
                        from.setText(selectedConsignment.getConsigner());
                        to.setText(selectedConsignment.getConsignee());
                        term.setText(selectedConsignment.getTerminus());
                        observationsTextField.setText(selectedConsignment.getObservations());
                        String observationsTextFieldContent = observationsTextField.getText();
                        if (observationsTextFieldContent != null) {
                            observationsTextField.positionCaret(observationsTextFieldContent.length());
                        }
                        Status selectedConsignmentStatus = selectedConsignment.getStatus();
                        if (selectedConsignmentStatus == Status.AWAITING_RESPONSE) {
                            from.setTextFill(Color.RED);
                            to.setTextFill(Color.RED);
                            term.setTextFill(Color.RED);
                        } else if (selectedConsignmentStatus == Status.COURIER_OFFERED) {
                            from.setTextFill(Color.PURPLE);
                            to.setTextFill(Color.PURPLE);
                            term.setTextFill(Color.PURPLE);
                        } else if (selectedConsignmentStatus == Status.AWAITING_DELIVERY) {
                            from.setTextFill(Color.GREEN);
                            to.setTextFill(Color.GREEN);
                            term.setTextFill(Color.GREEN);
                        }
                    }
                });
    }

    private static void setOkButtonToAcceptOrder() {
        okButton.setOnAction(event -> {
            try {
                if (consignmentDisplayService.getConsignment(selectedConsignment.getIdConsignment()).
                        getStatus().equals(Status.AWAITING_RESPONSE)) {
                    selectedConsignment.setStatus(Status.COURIER_OFFERED);
                    consignmentSaveService.saveConsignment(selectedConsignment);
                } else {
                    System.out.println("Unable to set consignment status");
                    throw new IllegalOperationException("Unable to set consignment status");
                }
            } catch (IllegalOperationException exception) {
                from.setText("");
                to.setText("");
                term.setText("");
                courierVBox.getChildren().add(new Label("Someone took the order already."));
            } finally {
                preserveCourierOrdersTableViewSelection();
            }
        });
    }

    private static void buildSelectedOrderHBox() {
        from.setMinWidth(80);
        to.setMinWidth(80);
        term.setMinWidth(80);

        observeAndCompleteSelectedOrder();
        setOkButtonToAcceptOrder();

        selectedOrderHBox.getChildren().addAll(selectedOrderLabel, from, to, term, okButton);
    }

    private static void sendButtonUpdateObservationsInDB() {
        sendButton.setOnAction(event -> {
            if (selectedConsignment != null) {
                selectedConsignment.setObservations(observationsTextField.getText() + " | ");
                try {
                    consignmentSaveService.saveConsignment(selectedConsignment);
                } catch (IllegalOperationException e) {
                    System.out.println("Unable to save consignment.");
                }
            }
        });
    }

    private static void buildObservationsHBox() {
        sendButtonUpdateObservationsInDB();
        observationsHBox.getChildren().addAll(observationsLabel, observationsTextField, sendButton);
        observationsHBox.setAlignment(Pos.CENTER);
    }

    private void buildCourierPhoneAppSimulation() {
        courierVBox.setBackground(new Background(new BackgroundFill(
                Paint.valueOf("#6B8779"), CornerRadii.EMPTY, Insets.EMPTY)));
        courierVBox.setSpacing(5);
        courierVBox.setPadding(new Insets(10, 11, 0, 11));
        courierVBox.getChildren().addAll(
                courierOrdersTableViewLabel,
                refreshButton,
                courierOrdersTableView,
                selectedOrderHBox,
                courierAddOrderHBox,
                observationsHBox,
                statistics);
    }
}
