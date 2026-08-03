package com.hostel.complaint.model;

import java.time.LocalDateTime;

/**
 * Feedback model class
 * Represents feedback given by students for resolved complaints
 */
public class Feedback {
    private int feedbackId;
    private int complaintId;
    private int studentId;
    private Integer rating;
    private String comments;
    private LocalDateTime submittedAt;

    // For display purposes
    private String studentName;
    private String complaintTitle;

    public Feedback() {
    }

    public Feedback(int complaintId, int studentId, Integer rating, String comments) {
        this.complaintId = complaintId;
        this.studentId = studentId;
        this.rating = rating;
        this.comments = comments;
    }

    // Getters and Setters
    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getComplaintTitle() {
        return complaintTitle;
    }

    public void setComplaintTitle(String complaintTitle) {
        this.complaintTitle = complaintTitle;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackId=" + feedbackId +
                ", complaintId=" + complaintId +
                ", studentId=" + studentId +
                ", rating=" + rating +
                ", comments='" + comments + '\'' +
                ", submittedAt=" + submittedAt +
                '}';
    }
}
