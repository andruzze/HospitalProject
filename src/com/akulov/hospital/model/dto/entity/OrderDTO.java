package com.akulov.hospital.model.dto.entity;


import com.akulov.hospital.annotations.TableField;
import com.akulov.hospital.annotations.TableName;
import com.akulov.hospital.model.dto.DTOImpl;

import java.time.LocalDate;

@TableName(tableName = "orders")
public class OrderDTO extends DTOImpl {
    @TableField(collumnName = "id")
    private final Integer id;

    @TableField(collumnName = "vendor")
    private final String vendor;

    @TableField(collumnName = "order_date")
    private final LocalDate orderDate;

    @TableField(collumnName = "delivery_date")
    private final LocalDate deliveryDate;

    @TableField(collumnName = "status")
    private final String status;

    public OrderDTO(Integer id, String vendor, LocalDate orderDate, LocalDate deliveryDate, String status) {
        this.id = id;
        this.vendor = vendor;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getVendor() {
        return vendor;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public String getStatus() {
        return status;
    }
}
