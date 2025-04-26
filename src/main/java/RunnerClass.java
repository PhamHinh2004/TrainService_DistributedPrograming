import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.mindrot.jbcrypt.BCrypt;

public class RunnerClass {
    public static void main(String[] args) {
        EntityManager em = Persistence.createEntityManagerFactory("default")
                .createEntityManager();
        EntityTransaction tr = em.getTransaction();
    }
}
