package com.sda.sylvester.greenCourier;

import com.sda.sylvester.greenCourier.dao.ConsignmentDao;
//import com.sda.sylvester.greenCourier.model.Consignment;

public class Main {

    public static void main(String[] args) {
//        Consignment consignment = new Consignment();
//        consignment.setConsigner("Client");
//        consignment.setConsignee("Destination");
//        consignment.setTerminus("Standard");
        ConsignmentDao consignmentDao = new ConsignmentDao();
//        consignmentDao.save(consignment);
        consignmentDao.deleteAllConsignments();
    }



}
