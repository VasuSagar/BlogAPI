package com.neon.blog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="chats")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="participant_a",nullable = false)
    private User participantA;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="participant_b",nullable = false)
    private User participantB;
}
