package unit7.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class HibernateInventoryApp {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .addAnnotatedClass(Product.class)
                .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
                .setProperty("hibernate.connection.url", "jdbc:h2:mem:inventory;DB_CLOSE_DELAY=-1")
                .setProperty("hibernate.connection.username", "sa")
                .setProperty("hibernate.connection.password", "")
                .setProperty("hibernate.hbm2ddl.auto", "create-drop")
                .setProperty("hibernate.show_sql", "true")
                .buildSessionFactory();

        try (factory) {
            Long productId;
            try (Session session = factory.openSession()) {
                session.beginTransaction();
                Product p = new Product("Wireless Mouse", 1200, 25);
                session.persist(p);
                session.getTransaction().commit();
                productId = p.getId();
            }

            try (Session session = factory.openSession()) {
                Product p = session.find(Product.class, productId);
                p.setStock(p.getStock() - 1);
                session.beginTransaction().commit();
            }

            try (Session session = factory.openSession()) {
                List<Product> products = session
                        .createQuery("from Product p where p.stock > 0", Product.class)
                        .getResultList();
                products.forEach(System.out::println);
            }
        }
    }
}
