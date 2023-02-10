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

            em.clear();

            System.out.println("//0208");

            Orders findOrders2 = em.find(Orders.class, orders1.getId());
            System.out.println("findOrders2.getItem() = " + findOrders2.getItem());

            em.detach(findOrders2);

            findOrders2.setItem("car");
            System.out.println("findOrders2.getItem() = " + findOrders2.getItem());

            /**지연로딩과 준영속성**/
            User findU = em.find(User.class, userC.getId());
            List<Orders> orderList1 = findU.getOrderList();
            em.close();
            for (Orders o : orderList1) {
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
