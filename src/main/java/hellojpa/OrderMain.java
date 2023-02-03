package hellojpa;

import hellojpa.order.entity.Grade;
import hellojpa.order.entity.Orders;
import hellojpa.order.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class OrderMain {
    // 실험
    // 1. FetchType.LAZY 설정 안했을 경우, QUERY 비교
    //    -> clear 주의 : EntityManager.clear를 하지 않으면 영속컨텍스트에 있는 order캐시를 가져오기 때문에 query문을 날리지 않느다.
    //    -> order의 item만 조회하고 싶은데 user테이블과 join을 한다. 필요없는 리소스가 소비된다.
    // 2. mappedBy 했을 때 vs 안했을때
    //    -> 누가 주인 역할인지 정해주지 않으면, 첫 실행에서는 h2 database에 테이블이 잘 생기지만 이후 재실행했을때 어떤 테이블을 먼저 drop 해야하는지 판단하지 못하여 warn 발생한다.
    //    -> 주인 역할 order 를 persist 하기 전, userC를 persist하지 않으면 ERROR 발생. (object references an unsaved transient instance - save the transient instance before flushing : hellojpa.order.entity.Orders.user -> hellojpa.order.entity.User)
    //    #예외발생 : Exception in thread "main" java.lang.StackOverflowError
    //               예외발생 로그를 확인했을 때, ' at hellojpa.order.entity.User$HibernateProxy$WHkj2SRh.toString(Unknown Source) ' 이런 내용이 있다
    //               엔티티 자체를 toString으로 출력하는 것은 불가능하다. -> 엔티티의 특정 필드를 조회한다.
    // 3. ManyToMany 에서 Item을 add 했을 때
    // 4. em.flush 이후에 persist에 넣었던 객체를 조회하면 어떻게 될까?
    //    -> flush로 db에 query를 날렸다고 해서 데이터가 들어가지는 않는다. / transaction.commit을 해줘야 실제 반영
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("order");
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            System.out.println("transaction 시작");

            User userA = new User();
            userA.setName("userA");
            userA.setGrade(Grade.USER);
            em.persist(userA);

            User userB = new User();
            userB.setName("userB");
            userB.setGrade(Grade.VIP);
            em.persist(userB);
            //4
            em.flush();
            em.clear();
//            transaction.commit();

            User findUser = em.find(User.class, userA.getId());
            System.out.println("findUser = " + findUser);

            /*****1******/

            Orders orders = new Orders();
            orders.setItem("사과");
            orders.setUser(userB);
            em.persist(orders);

            em.flush();
            em.clear();

            Orders findOrders = em.find(Orders.class, orders.getId());
            String orderItem = findOrders.getItem();
            System.out.println("orderItem = " + orderItem);

            em.clear();
            /*****2 mappedBy 했을 때 vs 안했을때******/

            User userC = new User();
            userC.setName("userC");
            em.persist(userC);

            Orders orders1 = new Orders();
            orders1.setItem("computer");
            orders1.setUser(userC);
            em.persist(orders1);

            Orders orders2 = new Orders();
            orders2.setItem("keyboard");
            orders2.setUser(userC);
            em.persist(orders2);

            em.flush();
            em.clear();

//            Orders findOrders2 = em.find(Orders.class, orders1.getId());
//            User userOfOrders = findOrders2.getUser();
//            System.out.println("userOfOrders = " + userOfOrders.getName());
//
//            List<Orders> ordersList = userOfOrders.getOrderList();
            User findUserC = em.find(User.class, userC.getId());

            List<Orders> orderList = findUserC.getOrderList();
            System.out.println("orderList.size() = " + orderList.size());
            for (Orders o : orderList) {
                System.out.println("o = " + o.getItem());
            }


            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            em.close();
            entityManagerFactory.close();
        }
    }
}
