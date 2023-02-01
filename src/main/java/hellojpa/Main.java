package hellojpa;

import hellojpa.entity.Member;
import hellojpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

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

            em.flush();
            em.clear();

            // member 조회
            Member findMember = em.find(Member.class, member.getId());
            Team findTeam = findMember.getTeam();

            List<Member> members = findTeam.getMembers();
            for (Member m : members) {
                System.out.println("m = " + m);
            }

            transaction.commit();

            System.out.println(findTeam.getId());
            System.out.println(findTeam.getName());
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
}
