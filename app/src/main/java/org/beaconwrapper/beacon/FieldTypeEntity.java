package org.beaconwrapper.beacon;

import java.util.List;

/**
 * <p>This is a pure model class which contains info about annotated field.</p>
 * <p>This class is helpful for filtering data at later on operation</p>
 */
public class FieldTypeEntity {

    private List<String> fieldValue;
    private Class<?> fieldType;
    private String fieldKey;

    /**
     * Instantiates a new Field type entity.
     *
     * @param fieldValue the field value
     * @param fieldType  the field type
     * @param fieldKey   the field key which is to be compare at later on time
     */
    public FieldTypeEntity(List<String> fieldValue, Class<?> fieldType, String fieldKey) {
        this.fieldValue = fieldValue;
        this.fieldType = fieldType;
        this.fieldKey = fieldKey;
    }

    /**
     * Gets field value.
     *
     * @return the field value
     */
    public List<String> getFieldValue() {
        return fieldValue;
    }

    /**
     * Sets field value.
     *
     * @param fieldValue the field value
     */
    public void setFieldValue(List<String> fieldValue) {
        this.fieldValue = fieldValue;
    }

    /**
     * Gets field type.
     *
     * @return the field type
     */
    public Class<?> getFieldType() {
        return fieldType;
    }

    /**
     * Sets field type.
     *
     * @param fieldType the field type
     */
    public void setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
    }

    /**
     * Gets field key.
     *
     * @return the field key
     */
    public String getFieldKey() {
        return fieldKey;
    }

    /**
     * Sets field key.
     *
     * @param fieldKey the field key
     */
    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }
}
