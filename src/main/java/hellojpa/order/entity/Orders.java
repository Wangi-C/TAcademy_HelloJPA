package hellojpa.order.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter @Setter
//@ToString
public class Orders {

    @Id @GeneratedValue
    private Long id;
    private String item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToOne
//    @JoinColumn(name = "item_id")
//    private Item item;
}
