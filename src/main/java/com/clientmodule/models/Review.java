package com.clientmodule.models;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "Reviews")
public class Review {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "book_id")
    private Integer bookId;
    @Basic
    @Column(name = "user_id")
    private Integer userId;
    @Basic
    @Column(name = "review_text")
    private String reviewText;
    @Basic
    @Column(name = "review_date")
    private Date reviewDate;
    @Basic
    @Column(name = "review_rating")
    private Double reviewRating;
    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id", insertable=false, updatable=false)
    private Book bookByBookId;
    @ManyToOne()
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

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Double getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(Double reviewRating) {
        this.reviewRating = reviewRating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        if (id != review.id) return false;
        if (!Objects.equals(bookId, review.bookId)) return false;
        if (!Objects.equals(userId, review.userId)) return false;
        if (!Objects.equals(reviewText, review.reviewText)) return false;
        if (!Objects.equals(reviewDate, review.reviewDate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (bookId != null ? bookId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (reviewText != null ? reviewText.hashCode() : 0);
        result = 31 * result + (reviewDate != null ? reviewDate.hashCode() : 0);
        return result;
    }

    public Book getBooksByBookId() {
        return bookByBookId;
    }

    public void setBooksByBookId(Book bookByBookId) {
        this.bookByBookId = bookByBookId;
    }
}
