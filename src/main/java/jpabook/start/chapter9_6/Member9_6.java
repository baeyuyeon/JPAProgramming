package jpabook.start.chapter9_6;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Member9_6 {


    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    Address9_6 address;
    @Embedded
    PhoneNumber phoneNumber;


}
