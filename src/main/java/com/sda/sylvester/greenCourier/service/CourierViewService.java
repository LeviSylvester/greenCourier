package com.sda.sylvester.greenCourier.service;

import javafx.concurrent.Task;

import java.util.concurrent.TimeUnit;

import static com.sda.sylvester.greenCourier.view.CourierView.*;

public class CourierViewService {

    private static ConsignmentDisplayService consignmentDisplayService = new ConsignmentDisplayService();

    private static void waitSeconds(int unit) {
        try {
//            Thread.sleep(unit*1000);
            TimeUnit.SECONDS.sleep(unit);
        } catch (InterruptedException s) {
            System.out.println("slept " + unit + " sec");
        }
    }

    public static void refreshConsignmentsObservableList() {
        consignmentsObservableList.setAll(consignmentDisplayService.getAllConsignments());
    }

    public static Task consignmentsAutoRefreshTask() {
        return new Task() {
            @Override
            public String call() throws Exception {
                while (autoRefresh) {
                    refreshConsignmentsObservableList();
                    waitSeconds(5);
                }
                return "auto-refreshed consignments";
            }
        };
    }
}
