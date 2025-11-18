package com.hendisantika.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Sequence Entity for Auto-Increment IDs in MongoDB
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sequences")
public class Sequence {
    @Id
    private String id;
    private long seq;
}
