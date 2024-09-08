package com.jobApplication.audit;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity // Marks this class as a JPA entity
@Table(name = "audit_log") // Maps this entity to the "audit_log" table in the database
@Data // Lombok annotation to generate getter, setter, toString, equals, and hashCode methods
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
public class AuditLog {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates values for this primary key
    private Long id; // Field for the primary key

    private String entityType; // Field to store the type of the entity being audited
    private Long entityId; // Field to store the ID of the entity being audited
    private String operation; // Field to store the type of operation performed (e.g., CREATE, UPDATE, DELETE)
    private String oldValue; // Field to store the previous value of the entity before the operation
    private String newValue; // Field to store the new value of the entity after the operation
    private LocalDateTime timestamp; // Field to store the timestamp of when the operation occurred
}
