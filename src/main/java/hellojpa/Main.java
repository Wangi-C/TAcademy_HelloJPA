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

            Thread.sleep(3000);

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
            memberA.setName("A");
            memberA.setMemberType(MemberType.USER);
            memberA.setAge(12);
            em.persist(memberA);

            Member memberB = new Member();
            memberB.setName("B");
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
