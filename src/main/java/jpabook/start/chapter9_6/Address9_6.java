package jpabook.start.chapter9_6;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Address9_6 {

    String street;
    String city;
    String state;
    @Embedded
    Zipcode zipcode;

}

@Embeddable
class Zipcode {

    String zip;
    String plusFour;
}