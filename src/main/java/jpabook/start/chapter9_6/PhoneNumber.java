package jpabook.start.chapter9_6;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Embeddable
public class PhoneNumber {

    String areaCode;
    String localNumber;
    @ManyToOne
    PhoneServiceProvider provider;
}

@Entity
class PhoneServiceProvider {

    @Id
    String name;
}
