package com.clientmodule.model;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "Users")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "user_name")
    private String userName;
    @Basic
    @Column(name = "user_email")
    private String userEmail;
    @Basic
    @Column(name = "user_password")
    private String userPassword;
    @Basic
    @Column(name = "user_role")
    private String userRole;
    @OneToMany(mappedBy = "userByUserId")
    private Collection<Rent> rentById;
    @OneToMany(mappedBy = "userByUserId")
    private Collection<Review> reviewById;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (!Objects.equals(userName, user.userName)) return false;
        if (!Objects.equals(userEmail, user.userEmail)) return false;
        if (!Objects.equals(userPassword, user.userPassword)) return false;
        if (!Objects.equals(userRole, user.userRole)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (userEmail != null ? userEmail.hashCode() : 0);
        result = 31 * result + (userPassword != null ? userPassword.hashCode() : 0);
        result = 31 * result + (userRole != null ? userRole.hashCode() : 0);
        return result;
    }

    public Collection<Rent> getRentsById() {
        return rentById;
    }

    public void setRentsById(Collection<Rent> rentById) {
        this.rentById = rentById;
    }

    public Collection<Review> getReviewsById() {
        return reviewById;
    }

    public void setReviewsById(Collection<Review> reviewById) {
        this.reviewById = reviewById;
    }
}
