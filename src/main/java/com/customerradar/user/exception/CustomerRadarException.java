package com.customerradar.user.exception;

import com.customerradar.user.enums.StatusCode;

/**
 * Customer Radar Exception
 * an Exception with errorCode and Description
 */
public class CustomerRadarException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 6796227785803927652L;
    /** Error Code */
    protected final StatusCode errorCode;

    public CustomerRadarException() {
        super(StatusCode.FAIL.getCodeMsg());
        this.errorCode = StatusCode.FAIL;
    }

    /**
     * Customer Radar Exception
     * @param errorCode
     */
    public CustomerRadarException(final StatusCode errorCode) {
        super(errorCode.getCodeMsg());
        this.errorCode = errorCode;
    }
 
    /**
     * Description Exception
     * @param detailedMessage 
     */
    public CustomerRadarException(final String detailedMessage) {
        super(detailedMessage);
        this.errorCode = StatusCode.SYS_ERROR;
    }
 
    /**
     * Throw exception with Throwable
     * @param t Throwable
     */
    public CustomerRadarException(final Throwable t) {
        super(t);
        this.errorCode = StatusCode.SYS_ERROR;
    }
 
    /**
     * 
     * @param errorCode 
     * @param detailedMessage
     */
    public CustomerRadarException(final StatusCode errorCode, final String detailedMessage) {
        super(detailedMessage);
        this.errorCode = errorCode;
    }
 
    /**
     * 
     * @param errorCode 
     * @param t 
     */
    public CustomerRadarException(final StatusCode errorCode, final Throwable t) {
        super(errorCode.getCodeMsg(), t);
        this.errorCode = errorCode;
    }
 
    /**
     * 
     * @param detailedMessage 
     * @param t 
     */
    public CustomerRadarException(final String detailedMessage, final Throwable t) {
        super(detailedMessage, t);
        this.errorCode = StatusCode.SYS_ERROR;
    }
 
    /**
     * 
     * @param errorCode 
     * @param detailedMessage 
     * @param t 
     */
    public CustomerRadarException(final StatusCode errorCode, final String detailedMessage,
                        final Throwable t) {
        super(detailedMessage, t);
        this.errorCode = errorCode;
    }

    public StatusCode getStatusCode() {
        return this.errorCode;
    }
    
}