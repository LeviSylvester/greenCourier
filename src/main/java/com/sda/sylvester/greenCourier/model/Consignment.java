package com.sda.sylvester.greenCourier.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Consignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idConsignment;
    private String consigner;
    private String consignee;
    private String terminus;

    public Consignment() {
    }

    public Consignment(String consigner, String consignee, String terminus) {
        this.consigner = consigner;
        this.consignee = consignee;
        this.terminus = terminus;
    }

    public Consignment(int idConsignment, String consigner, String consignee, String terminus) {
        this.idConsignment = idConsignment;
        this.consigner = consigner;
        this.consignee = consignee;
        this.terminus = terminus;
    }

    public int getIdConsignment() {
        return idConsignment;
    }

    public void setIdConsignment(int idConsignment) {
        this.idConsignment = idConsignment;
    }

    public String getConsigner() {
        return consigner;
    }

    public void setConsigner(String consigner) {
        this.consigner = consigner;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getTerminus() {
        return terminus;
    }

    public void setTerminus(String terminus) {
        this.terminus = terminus;
    }

    @Override
    public String toString() {
        return "Consignment{" +
                "idConsignment=" + idConsignment +
                ", consigner='" + consigner + '\'' +
                ", consignee='" + consignee + '\'' +
                ", terminus='" + terminus + '\'' +
                '}';
    }

}