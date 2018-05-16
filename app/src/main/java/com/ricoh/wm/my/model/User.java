package com.ricoh.wm.my.model;

/**
 * Created by 2017063001 on 2018/3/9.
 */
public class User {

    /**
     * userId : 1
     * dept : 1G
     * num : 123
     * pwd : 123
     * name : 王淼
     * mail : miao_wang
     * grade : 系统管理员
     */

    private String dept;
    private String num;
    private String pwd;
    private String name;
    private String mail;
    private String grade;

    public User(String num, String pwd) {
        this.num = num;
        this.pwd = pwd;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "User{" +
                "dept='" + dept + '\'' +
                ", num='" + num + '\'' +
                ", pwd='" + pwd + '\'' +
                ", name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
}
