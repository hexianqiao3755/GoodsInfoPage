package com.hq.hsmwan.bean;

/**
 * 商品信息中的规格参数bean
 */
public class GoodsConfigBean {
    private int keyPropId;
    /**
     * 参数key
     */
    private String keyProp;
    private int valueId;
    /**
     * 参数value
     */
    private String value;

    public GoodsConfigBean() {
    }

    public GoodsConfigBean(String value, String keyProp) {
        this.value = value;
        this.keyProp = keyProp;
    }

    public GoodsConfigBean(int keyPropId, String keyProp, int valueId, String value) {
        this.keyPropId = keyPropId;
        this.keyProp = keyProp;
        this.valueId = valueId;
        this.value = value;
    }

    public int getKeyPropId() {
        return keyPropId;
    }

    public void setKeyPropId(int keyPropId) {
        this.keyPropId = keyPropId;
    }

    public String getKeyProp() {
        return keyProp;
    }

    public void setKeyProp(String keyProp) {
        this.keyProp = keyProp;
    }

    public int getValueId() {
        return valueId;
    }

    public void setValueId(int valueId) {
        this.valueId = valueId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
