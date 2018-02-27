package com.bio.stocktaking.Databases;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AllGoods_DB extends RealmObject {
    // primaryKey, unique task ID = 0, 1, 2, 3, 4... etc
    @PrimaryKey
    private long ID;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }


    private long barcode = 0;

    public long getBarcode() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }


    private String Stockname = "";

    public String getStockname() {
        return Stockname;
    }

    public void setStockname(String stockname) {
        Stockname = stockname;
    }



    private long artikul = 0;
    public long getArtikul() {
        return artikul;
    }

    public void setArtikul(long artikul) {
        this.artikul = artikul;
    }



    private int ammount;
    public int getAmmount() {
        return ammount;
    }

    public void setAmmount(int ammount) {
        this.ammount = ammount;
    }

    private double price = 0.0;
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    private String boxNumber = "";
    public String getBoxNumber() {
        return boxNumber;
    }

    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
    }




    public AllGoods_DB() {
    }

    public AllGoods_DB cloneObj(AllGoods_DB obj) {
        return obj;
    }



}
