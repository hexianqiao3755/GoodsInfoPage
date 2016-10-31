package com.hq.hsmwan.bean;

import java.math.BigDecimal;

public class RecommendGoodsBean {
    private String title;
    private String imag;
    private BigDecimal price;
    private String currentPrice;

    public RecommendGoodsBean() {
    }

    public RecommendGoodsBean(String title, String imag, BigDecimal price, String currentPrice) {
        this.title = title;
        this.imag = imag;
        this.price = price;
        this.currentPrice = currentPrice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImag() {
        return imag;
    }

    public void setImag(String imag) {
        this.imag = imag;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }
}
