package com.hendisantika.service;

import com.hendisantika.entity.Sequence;
import com.hendisantika.repository.SequenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * Service for generating auto-increment IDs in MongoDB
 */
@Service
public class SequenceService {

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private SequenceRepository sequenceRepository;

    /**
     * Generate next sequence ID with prefix
     * @param seqName sequence name (e.g., "patient_seq", "doctor_seq", "appointment_seq")
     * @param prefix prefix for ID (e.g., "P", "D", "A")
     * @return formatted ID (e.g., "P00001", "D00001", "A00001")
     */
    public String getNextSequenceId(String seqName, String prefix) {
        Query query = new Query(Criteria.where("_id").is(seqName));
        Update update = new Update().inc("seq", 1);
        
        Sequence sequence = mongoOperations.findAndModify(query, update, Sequence.class);
        
        if (sequence == null) {
            // First time, create the sequence
            sequence = new Sequence();
            sequence.setId(seqName);
            sequence.setSeq(1);
            sequenceRepository.save(sequence);
            return prefix + String.format("%05d", 1);
        }
        
        return prefix + String.format("%05d", sequence.getSeq() + 1);
    }
}
