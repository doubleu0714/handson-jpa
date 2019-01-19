package io.doubleu0714;

import static org.junit.Assert.assertFalse;
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
public class AppTest {
    EntityManagerFactory entityManagerFactory;

    @Before
    public void setup() {
        // META-INF/persistence.xml 에 정의되어 있는 Unit으로 Factory를 생성한다.
        // EntityManagerFactory 생성비용이 크므로 Application에서는 가급적 한번만 생성하고 공유하도록 한다.
        entityManagerFactory = Persistence.createEntityManagerFactory("my-persistence-unit");
    }

    /**
     * 새로운 Entity 생성
     */
    @Test
    public void newEntity() {
        // Factory에서 EntityManager를 생성한다.
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        // 새로 생성한 Entity 아직은 Persistence Context에서 관리하지 않기때문에 비영속 상태이다.
        Member member = new Member();
        member.setAge(31);
        member.setName("Hong gil dong");
        assertFalse(entityManager.contains(member));

        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        // EntityManager.persist 메소드를 사용해 비영속 상태의 Member 엔티티를 영속 상태로 만든다.
        // 아직 Database에 Insert쿼리를 실행하진 않았고, 지연된 SQL를 실행하기 위해 SQL을 저장해 놓는다.
        entityManager.persist(member);
        assertTrue(entityManager.contains(member));

        // commit 과 동시에 EntityManager 의 flush 가 실행되면서 저장된 지연 SQL들이 실행된다.
        transaction.commit();

        // flush 이후에도 준영속으로 변경되지 않는다.(close, clear, detach)
        assertTrue(entityManager.contains(member));
    }

    @Test
    public void findEntity() {
        // Factory에서 EntityManager를 생성한다.
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        
        // entityManager 를 통해 Database에서 select 한다.
        // select 해온 결과는 Persistence Context에 의해 관리되는 영속상태가 된다.
        Member member = entityManager.find(Member.class, 1L);

        assertTrue(entityManager.contains(member));
    }

    @Test
    public void detachEntity() {
        // Factory에서 EntityManager를 생성한다.
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        
        // entityManager 를 통해 Database에서 select 한다.
        // select 해온 결과는 Persistence Context에 의해 관리되는 영속상태가 된다.
        Member member = entityManager.find(Member.class, 1L);

        assertTrue(entityManager.contains(member));
        
        // 영속 상태의 member entity를 준영속 상태로 만든다.
        entityManager.detach(member);
        assertFalse(entityManager.contains(member));

        // 준영속 상태의 변경은 Database에 반영되지 않는다.
        member.setAge(88);
        transaction.commit();
    }

    @Test
    public void mergeEntity() {
        // Factory에서 EntityManager를 생성한다.
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        // entityManager 를 통해 Database에서 select 한다.
        // select 해온 결과는 Persistence Context에 의해 관리되는 영속상태가 된다.
        Member member = entityManager.find(Member.class, 1L);

        assertTrue(entityManager.contains(member));
        
        // 영속 상태의 member entity를 준영속 상태로 만든다.
        entityManager.detach(member);
        assertFalse(entityManager.contains(member));

        // 준영속 상태의 변경은 Database에 반영되지 않는다.
        member.setAge(88);
        transaction.commit();

        transaction.begin();
        // EntityManager.merge 메소드를 통해 다시 영속상태로 만들어 준다.
        entityManager.merge(member);
        // merge를 통해 변경된 사항은 flush를 통해 Database에 반영된다.
        transaction.commit();
    }

    @Test
    public void removeEntity() {
        // Factory에서 EntityManager를 생성한다.
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        
        // entityManager 를 통해 Database에서 select 한다.
        // select 해온 결과는 Persistence Context에 의해 관리되는 영속상태가 된다.
        Member member = entityManager.find(Member.class, 1L);

        assertTrue(entityManager.contains(member));

        // 영속 상태의 member entity를 준영속 상태로 만든다.
        entityManager.remove(member);
        assertFalse(entityManager.contains(member));

        // 실제 sql은 flush시 실행된다.
        transaction.commit();
    }
}