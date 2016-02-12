package com.hwc.cart;

/**
 * Created by hyunwoo794 on 2016-01-27.
 */
public class ListView_getset {
    private String name;
    private String size;
    private String color;
    private String brand;

    ListView_getset(String _name, String _size, String _color, String _brand) {
        name = _name;
        size = _size;
        color = _color;
        brand = _brand;
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
}
