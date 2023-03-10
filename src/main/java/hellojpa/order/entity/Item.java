package hellojpa.order.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

//@Entity
@Getter @Setter
@ToString
public class Item {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private int price;
}
