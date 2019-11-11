package com.eunovo.userservice.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "source_id", "target_id" }))
public class Friend implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "source_id", referencedColumnName = "id")
    private User source;

    @OneToOne(optional = false)
    @JoinColumn(name = "target_id", referencedColumnName = "id")
    private User target;

    @Column(columnDefinition = "boolean default false")
    private Boolean accepted = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Friend(User source, User target) {
        this.source = source;
        this.target = target;
    }
}