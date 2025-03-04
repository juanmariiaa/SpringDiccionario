package org.example.dictionaryapp.exception;

public class RecordNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    private String errorMessage;
    private Long recordId;

    public RecordNotFoundException(String errorMessage, Long recordId) {
        super(errorMessage + recordId);
        this.errorMessage = errorMessage;
        this.recordId = recordId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Long getRecordId() {
        return recordId;
    }
}