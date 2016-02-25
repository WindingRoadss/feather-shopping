package com.hwc.paid;

public class PaidListView_getset {
    private String name;
    private String size;
    private String color;
    private String brand;
    private String image;
    private String price;
    private String snum;
    private String brdel;
    private String prcnt;


    PaidListView_getset(String _name, String _size, String _color, String _brand, String _image, String _price, String _snum, String _brdel, String _prcnt) {
        name = _name;
        size = _size;
        color = _color;
        brand = _brand;
        image = _image;
        price = _price;
        snum = _snum;
        brdel = _brdel;
        prcnt = _prcnt;
    }


    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public String getBrand() {
        return brand;
    }

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public String getSnum() {
        return snum;
    }

    public String getBrdel() {
        return brdel;
    }

    public String getPrcnt() {
        return prcnt;
    }
}


