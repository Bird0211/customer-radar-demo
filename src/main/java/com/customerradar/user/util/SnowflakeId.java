package com.customerradar.user.util;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
/**
 * Get Id by SnowFlake
 * 
 */
public class SnowflakeId implements IdentifierGenerator {


    /**
     * start time (2015-01-01)
     */
    private static final long TWEPOCH = 1420041600000L;

    /**
     * worker id BITS
     */
    private static final long WORKER_ID_BITS = 5L;

    /**
     * data flag_id BITS
     */
    private static final long DATA_CENTER_ID_BITS = 5L;

    /**
     * max worker id
     */
    private static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);

    /**
     * max flag id
     */
    private static final long MAX_DATA_CENTER_ID = -1L ^ (-1L << DATA_CENTER_ID_BITS);

    /**
     * seq BITS
     */
    private static final long SEQUENCE_BITS = 12L;

    /**
     * Worker id SHIFT
     */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    /**
     * 
     */
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * 
     */
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    /**
     * 
     */
    private static final long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);

    /**
     * 
     */
    private static long workerId;

    /**
     * 
     */
    private static long datacenterId;

    /**
     * 
     */
    private static long sequence = 0L;

    /**
     * 
     */
    private static long lastTimestamp = -1L;


    public SnowflakeId() {
    }

    private static SnowflakeId snowflakeId=null;

    public static synchronized SnowflakeId getInstance(){
        if(snowflakeId==null){
            snowflakeId=new SnowflakeId();
        }
        return snowflakeId;
    }


    /**
     *
     * @param workerId     
     * @param datacenterId 
     */
    private SnowflakeId(long workerId, long datacenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        if (datacenterId > MAX_DATA_CENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATA_CENTER_ID));
        }
        SnowflakeId.workerId = workerId;
        SnowflakeId.datacenterId = datacenterId;
    }

    /**
     * get ID
     *
     * @return SnowflakeId
     */
    public static synchronized long getId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - TWEPOCH) << TIMESTAMP_LEFT_SHIFT)
                | (datacenterId << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     *
     * @param lastTimestamp 
     */
    protected static long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 
     *
     * @return current time
     */
    protected static long timeGen() {
        return System.currentTimeMillis();
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor arg0, Object arg1) throws HibernateException {
        return getId();
    }
    
}
