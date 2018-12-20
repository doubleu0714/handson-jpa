package io.doubleu0714.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


// create table MEMBER (
//     id bigint not null,
//     AGE integer not null,
//     createDateTime timestamp,
//     NAME varchar(100) not null,
//     primary key (id)
// )
@Entity
@Table(name = "MEMBER")
public class Member {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "NAME", length = 100, nullable = false)
    private String name;
    @Column(name = "AGE", length = 100, nullable = false)
    private int age;
    @Column(name = "CREATE_TIME")
    private LocalDateTime createDateTime;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDateTime getCreateDateTime() {
        return this.createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }
}