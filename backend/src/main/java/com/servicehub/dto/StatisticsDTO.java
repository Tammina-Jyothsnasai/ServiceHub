package com.servicehub.dto;

import java.util.Map;

public class StatisticsDTO {

    private long totalRequests;
    private long pendingRequests;
    private long assignedRequests;
    private long inProgressRequests;
    private long resolvedRequests;
    private long closedRequests;
    private long highPriorityRequests;
    private long urgentPriorityRequests;

    private long totalUsers;
    private long totalStaff;
    private double averageRating;
    private long totalFeedbackCount;

    private Map<String, Long> requestsByCategory;
    private Map<String, Long> requestsByStatus;
    private Map<String, Long> requestsByPriority;

    public StatisticsDTO() {}

    public long getTotalRequests() { return totalRequests; }
    public void setTotalRequests(long totalRequests) { this.totalRequests = totalRequests; }

    public long getPendingRequests() { return pendingRequests; }
    public void setPendingRequests(long pendingRequests) { this.pendingRequests = pendingRequests; }

    public long getAssignedRequests() { return assignedRequests; }
    public void setAssignedRequests(long assignedRequests) { this.assignedRequests = assignedRequests; }

    public long getInProgressRequests() { return inProgressRequests; }
    public void setInProgressRequests(long inProgressRequests) { this.inProgressRequests = inProgressRequests; }

    public long getResolvedRequests() { return resolvedRequests; }
    public void setResolvedRequests(long resolvedRequests) { this.resolvedRequests = resolvedRequests; }

    public long getClosedRequests() { return closedRequests; }
    public void setClosedRequests(long closedRequests) { this.closedRequests = closedRequests; }

    public long getHighPriorityRequests() { return highPriorityRequests; }
    public void setHighPriorityRequests(long highPriorityRequests) { this.highPriorityRequests = highPriorityRequests; }

    public long getUrgentPriorityRequests() { return urgentPriorityRequests; }
    public void setUrgentPriorityRequests(long urgentPriorityRequests) { this.urgentPriorityRequests = urgentPriorityRequests; }

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public long getTotalStaff() { return totalStaff; }
    public void setTotalStaff(long totalStaff) { this.totalStaff = totalStaff; }

    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }

    public long getTotalFeedbackCount() { return totalFeedbackCount; }
    public void setTotalFeedbackCount(long totalFeedbackCount) { this.totalFeedbackCount = totalFeedbackCount; }

    public Map<String, Long> getRequestsByCategory() { return requestsByCategory; }
    public void setRequestsByCategory(Map<String, Long> requestsByCategory) { this.requestsByCategory = requestsByCategory; }

    public Map<String, Long> getRequestsByStatus() { return requestsByStatus; }
    public void setRequestsByStatus(Map<String, Long> requestsByStatus) { this.requestsByStatus = requestsByStatus; }

    public Map<String, Long> getRequestsByPriority() { return requestsByPriority; }
    public void setRequestsByPriority(Map<String, Long> requestsByPriority) { this.requestsByPriority = requestsByPriority; }
}
