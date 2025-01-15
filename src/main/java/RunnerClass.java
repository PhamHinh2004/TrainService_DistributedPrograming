import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class RunnerClass {
    public static void main(String[] args) {
        EntityManager em = Persistence.createEntityManagerFactory("mysql-pu")
                .createEntityManager();
        EntityTransaction tr = em.getTransaction();
    }
}
