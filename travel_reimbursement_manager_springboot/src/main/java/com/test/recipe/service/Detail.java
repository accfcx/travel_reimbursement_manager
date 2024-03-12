package com.test.recipe.service;

import java.math.BigDecimal;

public class Detail {
    private String name;
    private String model;
    private String unit;
    private BigDecimal count;
    private BigDecimal price;
    private BigDecimal amount;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model
     *            the model to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit
     *            the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @return the count
     */
    public BigDecimal getCount() {
        return count;
    }

    /**
     * @param count
     *            the count to set
     */
    public void setCount(BigDecimal count) {
        this.count = count;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price
     *            the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount
     *            the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the taxRate
     */
    public BigDecimal getTaxRate() {
        return taxRate;
    }

    /**
     * @param taxRate
     *            the taxRate to set
     */
    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    /**
     * @return the taxAmount
     */
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    /**
     * @param taxAmount
     *            the taxAmount to set
     */
    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    @Override
    public String toString() {
        return "Detail [name=" + name + ", model=" + model + ", unit=" + unit + ", count=" + count + ", price=" + price
                + ", amount=" + amount + ", taxRate=" + taxRate + ", taxAmount=" + taxAmount + "]";
    }
}
