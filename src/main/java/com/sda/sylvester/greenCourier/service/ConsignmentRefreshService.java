package com.sda.sylvester.greenCourier.service;

import com.sda.sylvester.greenCourier.model.Consignment;
import com.sda.sylvester.greenCourier.model.Status;
import com.sda.sylvester.greenCourier.view.ClientView;
import javafx.concurrent.Task;

import java.util.concurrent.TimeUnit;

import static com.sda.sylvester.greenCourier.view.ClientView.*;
import static com.sda.sylvester.greenCourier.view.CourierView.*;

public class ConsignmentRefreshService {

    private static ConsignmentDisplayService consignmentDisplayService = new ConsignmentDisplayService();
    public static Consignment selectedConsignmentBeforeRefresh = new Consignment();

    private static void waitSeconds(int unit) {
        try {
            TimeUnit.SECONDS.sleep(unit);
        } catch (InterruptedException s) {
            System.out.println("slept " + unit + " sec");
        }
    }

    public static void refreshConsignmentsObservableList() {
        selectedRow = courierOrdersTableView.getSelectionModel().getSelectedIndex();
        selectedConsignmentBeforeRefresh = getSelectedConsignment();
        consignmentsObservableList.setAll(consignmentDisplayService.getAllConsignments());
    }

    public static Task consignmentsAutoRefreshTask() {
        return new Task() {
            @Override
            public String call() throws Exception {
                while (courierAutoRefresh) {
                    refreshConsignmentsObservableList();
                    preserveCourierOrdersTableViewSelection();
                    waitSeconds(5);
                }
                return "auto-refreshed consignments";
            }
        };
    }

    private static void refreshPlacedOrderStatus() {
        Consignment refreshedConsignment = consignmentDisplayService.getConsignment(currentOrderNumber);
        ClientView.setObservationsTextField(refreshedConsignment.getObservations());
        if (refreshedConsignment.getStatus() == Status.COURIER_OFFERED) {
            ClientView.setConsignment(refreshedConsignment);
            setStatusInOrderTrackingTextBox(currentOrderNumber, " - Courier offered");
            clientAutoRefresh = false;
        }
    }

    public static Task placedOrderAutoRefreshTask() {
        return new Task() {
            @Override
            public String call() {
                while (clientAutoRefresh) {
                    refreshPlacedOrderStatus();
                    waitSeconds(5);
                }
                return "auto-refreshed placed order";
            }
        };
    }
}
