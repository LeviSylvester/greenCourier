package com.sda.sylvester.greenCourier.view;

import com.sda.sylvester.greenCourier.model.Consignment;
import com.sda.sylvester.greenCourier.service.ConsignmentSaveService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClientView extends Application {

    private final VBox clientPlaceOrderVBox = new VBox();

    @Override
    public void start(Stage clientStage) throws Exception {
        clientStage.setTitle("BikeCourier");
        clientStage.setHeight(640);
        clientStage.setWidth(360);
        clientStage.setX(215);
        clientStage.setY(64);

        buildClientPhoneAppSimulation(clientPlaceOrderVBox);

        Scene clientScene = new Scene(clientPlaceOrderVBox);
        clientStage.setScene(clientScene);
        clientStage.show();
    }

    private void buildClientPhoneAppSimulation(VBox clientPlaceOrderVBox) {
        clientPlaceOrderVBox.setAlignment(Pos.CENTER);
        clientPlaceOrderVBox.setSpacing(10);

        Text clientWelcomeText = new Text("Order a courier");
        clientWelcomeText.setFont(Font.font("Veranda", 30));
        clientWelcomeText.setFill(Paint.valueOf("#2455d1"));

        Label fromLabel = new Label("From: ");
        TextField fromTextField = new TextField();
        Label toLabel = new Label("To: ");
        toLabel.setPadding(new Insets(0,0,0, 14));
        TextField toTextField = new TextField();
        Label termLabel = new Label("Term: ");
        TextField termTextField = new TextField();

        Button orderButton = new Button("Place order");
        orderButton.setOnMouseClicked(event -> {
            Consignment consignment = new Consignment();
            consignment.setConsigner(fromTextField.getText());
            consignment.setConsignee(toTextField.getText());
            consignment.setTerminus(termTextField.getText());
            fromTextField.clear();
            toTextField.clear();
            termTextField.clear();
            ConsignmentSaveService consignmentSaveService = new ConsignmentSaveService();
            consignmentSaveService.saveConsignment(consignment);
        });

        HBox fromHBox = new HBox();
        fromHBox.getChildren().addAll(fromLabel, fromTextField);
        fromHBox.setAlignment(Pos.CENTER);
        HBox toHBox = new HBox();
        toHBox.getChildren().addAll(toLabel, toTextField);
        toHBox.setAlignment(Pos.CENTER);
        HBox termHBox = new HBox();
        termHBox.getChildren().addAll(termLabel, termTextField);
        termHBox.setAlignment(Pos.CENTER);

        clientPlaceOrderVBox.getChildren().addAll(clientWelcomeText, fromHBox, toHBox, termHBox, orderButton);
    }

}
