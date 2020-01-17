package com.sda.sylvester.greenCourier.service;

import com.sda.sylvester.greenCourier.dao.ConsignmentDao;
import com.sda.sylvester.greenCourier.model.Consignment;
import com.sda.sylvester.greenCourier.model.Status;

import static com.sda.sylvester.greenCourier.view.ClientView.*;

public class ConsignmentSaveService {

    private ConsignmentDao consignmentDao = new ConsignmentDao();

    public Consignment saveConsignment(Consignment consignment) throws IllegalOperationException {
        try {
            consignmentDao.save(consignment);
        } catch (Exception exception) {
            throw new IllegalOperationException("Failed to save consignment", exception);
        }
        return consignment;
    }

    public void closeDeal(Consignment consignment) throws IllegalOperationException {
        if (consignment.getStatus() == Status.COURIER_OFFERED) {
            consignment.setStatus(Status.AWAITING_DELIVERY);
            saveConsignment(consignment);
            setStatusInOrderTrackingTextBox(currentOrderNumber, " - Awaiting delivery.");
        }
    }
}