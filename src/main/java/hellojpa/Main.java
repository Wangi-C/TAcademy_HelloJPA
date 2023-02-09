package hellojpa;

import hellojpa.entity.Member;
import hellojpa.entity.Team;

import javax.persistence.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");

        EntityManager em = emf.createEntityManager();
        //1. 트랜젹션을 얻기 - jpa의 모든 활동은 트랜잭션 안에서 이루어져야한다.
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        try {
            // 팀 저장
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            // 맴버 저장
            Member member = new Member();
            member.setName("wangi");
            member.setMemberType(MemberType.USER);
//            member.setTeamId(team.getId());
            member.setTeam(team);
            em.persist(member);

//            team.getMembers().add(member);

            em.flush();
            em.clear();

//            Thread.sleep(3000);

            // member 조회
            Member findMember = em.find(Member.class, member.getId());
            findMember.setName("helloWangi");
//            Team findTeam = findMember.getTeam();
//
//            List<Member> members = findTeam.getMembers();
//            for (Member m : members) {
//                System.out.println("m = " + m);
//            }

            Member memberA = new Member();
            memberA.setName("WanWan");
            memberA.setMemberType(MemberType.USER);
            memberA.setAge(12);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setName("Wangi");
            memberB.setMemberType(MemberType.ADMIN);
            memberB.setAge(13);
            memberB.setTeam(team);
            em.persist(memberB);
//
//            Thread.sleep(3000);
//
            TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
            List<Member> members = query.getResultList();

            for (Member m : members) {
                System.out.println("m = " + m);
            }
//
//            em.flush();
//            em.clear();
//
//            Member findMember = em.find(Member.class, memberB.getId());
//
////            Team teamB = findMember.getTeam();
////            System.out.println("teamB = " + teamB);
//
//            em.close();
//
//            Team teamB = findMember.getTeam();
//            System.out.println("teamB = " + teamB);
//
            em.flush();
            em.clear();
////
////            transaction.commit();
//
////            System.out.println(findTeam.getId());
////            System.out.println(findTeam.getName());

            /**fetch Join**/
            // fetch 사용전
            // 실행 sql :
            /* select
                    m
                from
                    Member m
                where
                    m.name like '%Wan%' */
            System.out.println("fetch Join 실습");
            String jpqlFetchbefore = "select m from Member m where m.name like '%Wan%'";
            List<Member> jpqlFindMember = em.createQuery(jpqlFetchbefore, Member.class).getResultList();

            // fetch Join 사용
            // 실행 sql :
            /*
            * select
                    member0_.id as id1_0_0_,
                    team1_.id as id1_2_1_,
                    member0_.age as age2_0_0_,
                    member0_.memberType as memberTy3_0_0_,
                    member0_.USERNAME as USERNAME4_0_0_,
                    member0_.TEAM_ID as TEAM_ID5_0_0_,
                    team1_.name as name2_2_1_
                from
                    Member member0_
                inner join
                    Team team1_
                        on member0_.TEAM_ID=team1_.id
                where
                    member0_.USERNAME like '%Wan%'
            * */
            System.out.println("fetch Join 적용");
            String jpqlFetchAfter = "select m from Member m join fetch m.team where m.name like '%Wan%'";
            jpqlFindMember = em.createQuery(jpqlFetchAfter, Member.class)
                    .setFirstResult(10)
                    .setMaxResults(20)
                    .getResultList();

            for (Member m :
                    jpqlFindMember) {
                System.out.println("m = " + m);
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
}
