package com.sda.sylvester.greenCourier.service;

import com.sda.sylvester.greenCourier.dao.ConsignmentDao;
import com.sda.sylvester.greenCourier.model.Consignment;

public class ConsignmentSaveService {

    private ConsignmentDao consignmentDao = new ConsignmentDao();

    public void saveConsignment(Consignment consignment) throws IllegalOperationException {
        try {
            consignmentDao.save(consignment);
        } catch (Exception exception) {
            throw new IllegalOperationException("Failed to save consignment", exception);
        }
    }
}