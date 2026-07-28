package com.efit.hrms.repo;

import java.util.List;

public interface SequenceRepo {
    /** Returns a contiguous block of IDs [start, start+batchSize-1] from the given sequence table. */
    List<Long> getNextBatchIds(String sequenceTable, int batchSize);
}
