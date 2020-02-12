package com.sda.sylvester.greenCourier.view;

import com.sda.sylvester.greenCourier.model.Consignment;
import com.sda.sylvester.greenCourier.model.Status;
import com.sda.sylvester.greenCourier.service.ConsignmentRefreshService;
import com.sda.sylvester.greenCourier.service.ConsignmentSaveService;
import com.sda.sylvester.greenCourier.service.IllegalOperationException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClientView extends Application {

    private static ConsignmentSaveService consignmentSaveService = new ConsignmentSaveService();
    private static Consignment consignment = new Consignment();

    private static VBox clientVBox = new VBox();

    private static TextField fromTextField = new TextField();
    private static TextField toTextField = new TextField();
    private static TextField termTextField = new TextField();
    private static Button orderButton = new Button("Place order");

    private static HBox observationsHBox = new HBox();
    private static Label observationsLabel = new Label("Observations: ");

    private static TextField observationsTextField = new TextField();
    private static Button sendButton = new Button("Send");

    private static HBox trackDeliveryHBox = new HBox();
    private static Label trackDeliveryLabel = new Label("Order no.: ");
    private static TextField orderNumberAndStatusTextField = new TextField();
    private static Button trackButton = new Button("Track");
    private static Button acceptButton = new Button("Accept");

    private static Button historyButton = new Button("History");

    public static int currentOrderNumber;
    public static boolean clientAutoRefresh = false;

    @Override
    public void start(Stage clientStage) throws Exception {
        clientStage.setTitle("Green Courier");
        clientStage.setHeight(640);
        clientStage.setWidth(360);
        clientStage.setX(215);
        clientStage.setY(64);

        buildClientPhoneAppSimulation();
        Scene clientScene = new Scene(clientVBox);
        clientStage.setScene(clientScene);
        clientStage.show();
    }

    public static void setConsignment(Consignment consignment) {
        ClientView.consignment = consignment;
    }

    public static void setObservationsTextField(String observations) {
        ClientView.observationsTextField.setText(observations);
        ClientView.observationsTextField.positionCaret(observations.length());
    }

    private static void orderButtonPlacingOrder() {
        orderButton.setOnMouseClicked(event -> {
            consignment.setConsigner(fromTextField.getText());
            consignment.setConsignee(toTextField.getText());
            consignment.setTerminus(termTextField.getText());
            consignment.setStatus(Status.AWAITING_RESPONSE);
            consignment.setObservations(observationsTextField.getText());
            fromTextField.clear();
            toTextField.clear();
            termTextField.clear();
            try {
                consignment = consignmentSaveService.saveConsignment(consignment);
                currentOrderNumber = consignment.getIdConsignment();
                setStatusInOrderTrackingTextBox(currentOrderNumber, " - Awaiting response...");
                clientAutoRefresh = true;
                new Thread(ConsignmentRefreshService.placedOrderAutoRefreshTask()).start();
            } catch (IllegalOperationException exception) {
                clientVBox.getChildren().add(new Label("Unable to place order, try again!"));
            }
        });
    }

    public static void acceptButtonClosingDeal() {
        acceptButton.setOnAction(event -> {
            try {
                consignmentSaveService.closeDeal(consignment);
            } catch (IllegalOperationException e) {
                System.out.println("Database error occurred, please try again!");
            }
        });
    }



    public static void setStatusInOrderTrackingTextBox(int currentOrderNumber, String status) {
        orderNumberAndStatusTextField.setText(currentOrderNumber + status);
    }

    private void buildClientPhoneAppSimulation() {
        clientVBox.setAlignment(Pos.CENTER);
        clientVBox.setSpacing(10);

        Text clientWelcomeText = new Text("Order a courier");
        clientWelcomeText.setFont(Font.font("Veranda", 30));
        clientWelcomeText.setFill(Paint.valueOf("#2455d1"));

        Label fromLabel = new Label("From: ");
        Label toLabel = new Label("To: ");
        toLabel.setPadding(new Insets(0, 0, 0, 14));
        Label termLabel = new Label("Term: ");

        orderButtonPlacingOrder();
        acceptButtonClosingDeal();

        HBox fromHBox = new HBox();
        fromHBox.getChildren().addAll(fromLabel, fromTextField);
        fromHBox.setAlignment(Pos.CENTER);
        HBox toHBox = new HBox();
        toHBox.getChildren().addAll(toLabel, toTextField);
        toHBox.setAlignment(Pos.CENTER);
        HBox termHBox = new HBox();
        termHBox.getChildren().addAll(termLabel, termTextField);
        termHBox.setAlignment(Pos.CENTER);

        observationsHBox.getChildren().addAll(observationsLabel, observationsTextField, sendButton);
        observationsHBox.setAlignment(Pos.CENTER);
        trackDeliveryHBox.getChildren().addAll(trackDeliveryLabel, orderNumberAndStatusTextField, trackButton);
        trackDeliveryHBox.setAlignment(Pos.CENTER);
        clientVBox.setBackground(new Background(new BackgroundFill(
                Paint.valueOf("#6B8779"), CornerRadii.EMPTY, Insets.EMPTY
        )));
        clientVBox.getChildren().addAll(
                clientWelcomeText,
                fromHBox,
                toHBox,
                termHBox,
                orderButton,
                observationsHBox,
                trackDeliveryHBox,
                acceptButton,
                historyButton
        );
    }
}
