package com.efit.hrms.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.efit.hrms.repo.SequenceRepo;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SequenceRepoImpl implements SequenceRepo {

    private final JdbcTemplate jdbcTemplate;

    public SequenceRepoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Uses MySQL's LAST_INSERT_ID() trick to atomically reserve a range without long locks:
     *
     * UPDATE <seq_table> SET next_val = LAST_INSERT_ID(next_val + :n);
     * SELECT LAST_INSERT_ID();
     *
     * This is connection-scoped and highly concurrent.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, timeout = 5)
    public List<Long> getNextBatchIds(String sequenceTable, int batchSize) {
        if (batchSize <= 0) return new ArrayList<>();

        // 1) Bump counter atomically and capture the end value in LAST_INSERT_ID
        jdbcTemplate.update("UPDATE " + sequenceTable + " SET next_val = LAST_INSERT_ID(next_val + ?)", batchSize);

        // 2) Fetch end of the reserved range
        Long end = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        long start = end - batchSize + 1;

        // 3) Materialize the range [start, end]
        List<Long> ids = new ArrayList<>(batchSize);
        for (long v = start; v <= end; v++) ids.add(v);
        return ids;
    }
}
