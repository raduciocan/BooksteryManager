package com.clientmodule.models;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "Rents")
public class Rent {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "user_id")
    private Integer userId;
    @Basic
    @Column(name = "book_id")
    private Integer bookId;
    @Basic
    @Column(name = "rent_date")
    private Date rentDate;
    @Basic
    @Column(name = "rent_due")
    private Date rentDue;
    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", insertable=false, updatable=false)
    private Book bookByBookId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable=false, updatable=false)
    private User userByUserId;

    public User getUsersByUserId() {
        return userByUserId;
    }

    public void setUsersByUserId(User userByUserId) {
        this.userByUserId = userByUserId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Date getRentDate() {
        return rentDate;
    }

    public void setRentDate(Date rentDate) {
        this.rentDate = rentDate;
    }

    public Date getRentDue() {
        return rentDue;
    }

    public void setRentDue(Date rentDue) {
        this.rentDue = rentDue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rent rent = (Rent) o;

        if (id != rent.id) return false;
        if (!Objects.equals(userId, rent.userId)) return false;
        if (!Objects.equals(bookId, rent.bookId)) return false;
        if (!Objects.equals(rentDate, rent.rentDate)) return false;
        if (!Objects.equals(rentDue, rent.rentDue)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (bookId != null ? bookId.hashCode() : 0);
        result = 31 * result + (rentDate != null ? rentDate.hashCode() : 0);
        result = 31 * result + (rentDue != null ? rentDue.hashCode() : 0);
        return result;
    }

    public Book getBooksByBookId() {
        return bookByBookId;
    }

    public void setBooksByBookId(Book bookByBookId) {
        this.bookByBookId = bookByBookId;
    }
}
