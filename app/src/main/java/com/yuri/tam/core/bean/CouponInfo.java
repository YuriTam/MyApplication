package com.yuri.tam.core.bean;

import java.io.Serializable;

/**
 * 优惠卷信息类
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年11月12日
 */
public class CouponInfo implements Serializable {
    private static final long serialVersionUID = -7424808758047337892L;

    private int couponType;    // 卡卷类型
    private String couponNo;   // 卡卷编号
    private String amount;     // 卡卷金额
    private String expTime;    // 有效期
    private boolean isUsed;    // 是否已使用

    public int getCouponType() {
        return couponType;
    }

    public void setCouponType(int couponType) {
        this.couponType = couponType;
    }

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExpTime() {
        return expTime;
    }

    public void setExpTime(String expTime) {
        this.expTime = expTime;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }
}
