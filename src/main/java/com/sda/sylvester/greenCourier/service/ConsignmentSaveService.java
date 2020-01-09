package com.sda.sylvester.greenCourier.service;

import com.sda.sylvester.greenCourier.dao.ConsignmentDao;
import com.sda.sylvester.greenCourier.model.Consignment;

public class ConsignmentSaveService {

    private ConsignmentDao consignmentDao = new ConsignmentDao();

    public void saveConsignment(Consignment consignment){
        consignmentDao.save(consignment);
    }
}