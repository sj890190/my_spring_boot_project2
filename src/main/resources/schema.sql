DROP TABLE IF EXISTS agri_fishery_products_trans_type_type;
DROP TABLE IF EXISTS agri_products_trans_type;
DROP TABLE IF EXISTS crop_market;
DROP TABLE IF EXISTS crop_type;

CREATE TABLE crop_type (
    id bigint GENERATED ALWAYS AS IDENTITY,
    crop_code varchar(10),
    crop_name varchar(100)
);
CREATE INDEX crop_type_crop_code ON crop_type USING HASH (crop_code);

CREATE TABLE crop_market (
    id bigint GENERATED ALWAYS AS IDENTITY,
    market_code varchar(10),
    market_name varchar(100)
);

CREATE TABLE agri_fishery_products_trans_type_type (
    tran_date Date,
    trans_date varchar(10),
    market_code varchar(10),
    market_name varchar(100),
    category char(1),
    prod_code varchar(10),
    prod_name varchar(100),
    upper_price NUMERIC(10,2),
    middle_price NUMERIC(10,2),
    lower_price NUMERIC(10,2),
    avg_price NUMERIC(10,2),
    trans_quantity bigint,

	CONSTRAINT pk_agri_fishery_products_trans_type_type PRIMARY KEY (trans_date, prod_code, prod_name, market_code, market_name)
);

CREATE TABLE agri_products_trans_type (
    tran_date Date,
    trans_date varchar(10),
    market_code varchar(10),
    market_name varchar(100),
    crop_code varchar(10),
    crop_name varchar(100),
    upper_price NUMERIC(10,2),
    middle_price NUMERIC(10,2),
    lower_price NUMERIC(10,2),
    avg_price NUMERIC(10,2),
    trans_quantity bigint,

	CONSTRAINT pk_agri_products_trans_type PRIMARY KEY (trans_date, crop_code, crop_name, market_code, market_name)
);