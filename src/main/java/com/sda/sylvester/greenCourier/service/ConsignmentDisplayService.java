package com.sda.sylvester.greenCourier.service;

import com.sda.sylvester.greenCourier.dao.ConsignmentDao;
import com.sda.sylvester.greenCourier.model.Consignment;

import java.util.List;

public class ConsignmentDisplayService {

    private ConsignmentDao consignmentDao = new ConsignmentDao();

    public Consignment getConsignment(int idConsignment) {
        return consignmentDao.get(idConsignment);
    }

    public List<Consignment> getAllConsignments() {
        return consignmentDao.getAllConsignments();
    }

}
