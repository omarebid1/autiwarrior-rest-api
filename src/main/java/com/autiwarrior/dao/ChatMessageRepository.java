package com.autiwarrior.dao;

import com.autiwarrior.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT m FROM ChatMessage m WHERE " +
            "(m.sender.userId = :user1 AND m.receiver.userId = :user2) OR " +
            "(m.sender.userId = :user2 AND m.receiver.userId = :user1) " +
            "ORDER BY m.timestamp ASC")
    List<ChatMessage> findChatHistory(@Param("user1") Integer user1, @Param("user2") Integer user2);

    @Query(value = """
            SELECT m.*
            FROM chat_message m
            INNER JOIN (
                SELECT 
                    CASE 
                        WHEN sender_id = :userId THEN receiver_id 
                        ELSE sender_id 
                    END as partner_id,
                    MAX(timestamp) as latest
                FROM chat_message
                WHERE sender_id = :userId OR receiver_id = :userId
                GROUP BY partner_id
            ) sub ON (
                (m.sender_id = :userId AND m.receiver_id = sub.partner_id OR
                 m.receiver_id = :userId AND m.sender_id = sub.partner_id)
                AND m.timestamp = sub.latest
            )
            """, nativeQuery = true)
    List<ChatMessage> findChatMessagesInvolvingUser(@Param("userId") Integer userId);

    @Query("SELECT m FROM ChatMessage m WHERE m.timestamp IN (" +
            "SELECT MAX(m2.timestamp) FROM ChatMessage m2 " +
            "WHERE m2.sender.userId = :userId OR m2.receiver.userId = :userId " +
            "GROUP BY CASE WHEN m2.sender.userId = :userId THEN m2.receiver.userId ELSE m2.sender.userId END" +
            ") ORDER BY m.timestamp DESC")
    List<ChatMessage> findRecentChats(@Param("userId") Integer userId);

    @Query("SELECT m FROM ChatMessage m WHERE m.timestamp IN (" +
            "SELECT MAX(m2.timestamp) FROM ChatMessage m2 " +
            "WHERE m2.sender.email = :email OR m2.receiver.email = :email " +
            "GROUP BY CASE WHEN m2.sender.email = :email THEN m2.receiver.email ELSE m2.sender.email END" +
            ") ORDER BY m.timestamp DESC")
    List<ChatMessage> findRecentChatsByEmail(@Param("email") String email);

    @Query("SELECT COUNT(m) FROM ChatMessage m " +
            "WHERE m.sender.email = :partnerEmail AND m.receiver.email = :currentUserEmail AND m.isRead = false")
    long countUnreadMessagesByEmail(@Param("partnerEmail") String partnerEmail,
                                   @Param("currentUserEmail") String currentUserEmail);

    @Query("SELECT COUNT(m) FROM ChatMessage m " +
            "WHERE m.sender.userId = :partnerId AND m.receiver.userId = :currentUserId AND m.isRead = false")
    long countUnreadMessages(@Param("partnerId") Integer partnerId,
                             @Param("currentUserId") Integer currentUserId);

    @Transactional
    @Modifying
    @Query("UPDATE ChatMessage m SET m.isRead = true " +
            "WHERE m.sender.userId = :senderId AND m.receiver.userId = :receiverId AND m.isRead = false")
    void markMessagesAsRead(@Param("senderId") Integer senderId, @Param("receiverId") Integer receiverId);

    @Query("SELECT m FROM ChatMessage m " +
            "WHERE (m.sender.userId = :user1 AND m.receiver.userId = :user2) " +
            "   OR (m.sender.userId = :user2 AND m.receiver.userId = :user1) " +
            "ORDER BY m.timestamp ASC")
    List<ChatMessage> findMessagesBetweenUsers(@Param("user1") Integer user1Id,
                                               @Param("user2") Integer user2Id);
}
