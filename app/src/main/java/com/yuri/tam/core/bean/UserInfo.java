package com.yuri.tam.core.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;

/**
 * 下载信息实体类
 *
 * @author 谭忠扬-YuriTam
 * @time 2018年02月02日
 */
@Entity(nameInDb = "User")
public class UserInfo implements Serializable {

    static final long serialVersionUID = 42L;

    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "USER_NAME")
    private String userName;          //用户名称
    @Property(nameInDb = "PASSWORD")
    private String password;          //用户密码
    @Property(nameInDb = "ID_CARD")
    private String IDcard;            //身份证号
    @Property(nameInDb = "AGE")
    private int age;                  //年龄
    @Property(nameInDb = "SEX")
    private int sex;                  //性别  0-男 1-女
    @Property(nameInDb = "PHONE")
    private String phone;             //电话号码
    @Property(nameInDb = "AVATAR")
    private String avatar;            //头像
    @Property(nameInDb = "ADDRESS")
    private String address;           //住址
    @Property(nameInDb = "IS_MARRIED")
    private boolean isMarried;        //是否已婚

    @Generated(hash = 1948933910)
    public UserInfo(Long id, String userName, String password, String IDcard,
            int age, int sex, String phone, String avatar, String address,
            boolean isMarried) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.IDcard = IDcard;
        this.age = age;
        this.sex = sex;
        this.phone = phone;
        this.avatar = avatar;
        this.address = address;
        this.isMarried = isMarried;
    }
    @Generated(hash = 1279772520)
    public UserInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getIDcard() {
        return this.IDcard;
    }
    public void setIDcard(String IDcard) {
        this.IDcard = IDcard;
    }
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public int getSex() {
        return this.sex;
    }
    public void setSex(int sex) {
        this.sex = sex;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getAddress() {
        return this.address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public boolean getIsMarried() {
        return this.isMarried;
    }
    public void setIsMarried(boolean isMarried) {
        this.isMarried = isMarried;
    }

}
