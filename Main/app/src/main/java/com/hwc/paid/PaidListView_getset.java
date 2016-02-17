package com.hwc.paid;

/**
 * Created by hyunwoo794 on 2016-01-27.
 */
public class PaidListView_getset {
    private String name;
    private String size;
    private String color;
    private String brand;
    private String image;
    private String price;

    PaidListView_getset(String _name, String _size, String _color, String _brand, String _image, String _price) {
        name = _name;
        size = _size;
        color = _color;
        brand = _brand;
        image = _image;
        price = _price;
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
}
