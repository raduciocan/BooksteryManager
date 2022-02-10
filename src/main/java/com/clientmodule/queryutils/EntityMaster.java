package com.clientmodule.queryutils;

import com.clientmodule.model.Book;
import com.clientmodule.model.Rent;
import com.clientmodule.model.Review;
import com.clientmodule.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EntityMaster implements Serializable {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    public EntityMaster() {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    private void closeManagers() {
        entityManager.close();
        entityManagerFactory.close();
    }

    public boolean doesUserExist(String email) {
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.userEmail=:email", User.class);
        query.setParameter("email", email);

        if(query.getResultList().isEmpty())
            return false;
        return true;
    }

    public boolean checkUserPassword(String email, String password) {
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.userEmail=:email", User.class);
        query.setParameter("email", email);

        User user = (User) query.getResultList().get(0);
        if(user.getUserPassword().equals(password))
            return true;
        return false;
    }

    public User logInUser(String email, String password) {
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.userEmail=:email AND u.userPassword=:password", User.class);
        query.setParameter("email", email);
        query.setParameter("password", password);

        User user = (User) query.getResultList().get(0);
        return user;
    }

    public void registerNewUser(User user) {
        try {
            entityManager.getTransaction().begin();

            entityManager.persist(user);

            entityManager.getTransaction().commit();
        } finally {
            if(entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }
    public void postBook(int id, String title, String author) {
        try {
            entityManager.getTransaction().begin();

            Book book = new Book();
            book.setId(id);
            book.setBookTitle(title);
            book.setBookAuthor(author);
            entityManager.persist(book);

            entityManager.getTransaction().commit();
        } finally {
            if(entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    public List getBooksRentedByUser(User user) {
        ArrayList<Rent> userRentList = new ArrayList<Rent>(getBookByQuery("SELECT r FROM Rent r WHERE r.userId=" + user.getId()));
        List result = new ArrayList<Book>();
        for (Rent rent : userRentList) {
            result.add(rent.getBooksByBookId());
        }
        return result;
    }

    public List getBooksNotRented() {
        //implemnt method
        return null;
    }

    public List getAllBooks(){
        return getBookByQuery("SELECT b FROM Book b");
    }
    public List getBookByQuery(String queryString) {
        Query query = entityManager.createQuery(queryString);
        return query.getResultList();
    }
    public void deleteBook(Book book){
        try {
            entityManager.getTransaction().begin();

            entityManager.remove(book);

            entityManager.getTransaction().commit();
        } finally {
            if(entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    public List getAllReviews() {
        return getReviewByQuery("SELECT r FROM Review r");
    }
    public List getReviewByQuery(String queryString) {
        Query query = entityManager.createQuery(queryString);
        return query.getResultList();
    }
    public void postReview(Review review) {
        try {
            entityManager.getTransaction().begin();

            entityManager.persist(review);

            entityManager.getTransaction().commit();
        } finally {
            if(entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    public void deleteReview(Review review) {
        try {
            entityManager.getTransaction().begin();

            entityManager.remove(review);

            entityManager.getTransaction().commit();
        } finally {
            if(entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    public void postRent(Rent rent) {
        try {
            entityManager.getTransaction().begin();

            entityManager.persist(rent);

            entityManager.getTransaction().commit();
        } finally {
            if(entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

    public void deleteRent(Rent rent) {
        try {
            entityManager.getTransaction().begin();

            entityManager.remove(rent);

            entityManager.getTransaction().commit();
        } finally {
            if(entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }
    }

}
