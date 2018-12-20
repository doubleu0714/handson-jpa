package io.doubleu0714;

import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;

import io.doubleu0714.entities.Member;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    EntityManagerFactory entityManagerFactory;

    @Before
    public void setup() {
        entityManagerFactory = Persistence.createEntityManagerFactory("my-persistence-unit");
    }

    @Test
    public void newEntity() {
        // 새로 생성한 Entity 아직은 Persistence Context에서 관리하지 않기때문에 비영속 상태이다.
        Member member = new Member();
        member.setAge(31);
        member.setName("Hong gil dong");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        // EntityManager.persist 메소드를 사용해 비영속 상태의 Member 엔티티를 영속 상태로 만든다.
        entityManager.persist(member);
        // commit 과 동시에 EntityManager 의 flush 가 실행되면서 저장된 지연 SQL들이 실행된다.
        // 또한 commit과 함께 PersistenceContext내의 Entity는 clear 된다.
        transaction.commit();
    }
}
