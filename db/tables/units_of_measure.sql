CREATE TABLE UNITS_OF_MEASURE(
    UOM_ID BIGINT NOT NULL,
    UOM_NAME VARCHAR(255)
);
ALTER TABLE UNITS_OF_MEASURE ADD CONSTRAINT UNITS_OF_MEASURE_PK PRIMARY KEY(UOM_ID);
