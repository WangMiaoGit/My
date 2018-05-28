package com.ricoh.wm.my.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by 2017063001 on 2018/5/21.
 *  GreenDao 对象实体类
 *
 *  @Entity  表明这个实体类会在数据库中生成一个与之相对应的表
 *
 *
 *
 */
@Entity
public class User_test {
    @Id
    private Long id;
    private String name;
    private int age;

    @Generated(hash = 1848795300)
    public User_test(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
    @Generated(hash = 300403988)
    public User_test() {
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
