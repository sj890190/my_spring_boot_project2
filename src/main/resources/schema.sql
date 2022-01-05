--DROP TABLE IF EXISTS agri_fishery_products_trans_type_type;
--DROP TABLE IF EXISTS crop_market;
--DROP TABLE IF EXISTS crop_type;

CREATE TABLE crop_type (
    id bigint GENERATED ALWAYS AS IDENTITY,
    crop_code varchar(10) UNIQUE,
    crop_name varchar(100)
);
CREATE INDEX crop_type_crop_code ON crop_type USING HASH (crop_code);

CREATE TABLE crop_market (
    id bigint GENERATED ALWAYS AS IDENTITY,
    market_code varchar(10) UNIQUE,
    market_name varchar(100)
);
CREATE INDEX crop_market_market_code ON crop_market USING HASH (market_code);

CREATE TABLE agri_fishery_products_trans_type_type (
    tran_date Date,
    trans_date varchar(10),
    market_code varchar(10) REFERENCES crop_market(market_code),
    market_name varchar(100) REFERENCES crop_type(crop_code),
    category char(1),
    prod_code varchar(10),
    prod_name varchar(100),
    upper_price NUMERIC(5,2),
    middle_price NUMERIC(5,2),
    lower_price NUMERIC(5,2),
    avg_price NUMERIC(5,2),
    trans_quantity bigint,

	CONSTRAINT pk_agri_fishery_products_trans_type_type PRIMARY KEY (tran_date, trans_date, market_code, market_name)
);
CREATE INDEX agri_fishery_products_trans_type_type_market_code ON agri_fishery_products_trans_type_type USING HASH (market_code);
CREATE INDEX agri_fishery_products_trans_type_type_prod_code ON agri_fishery_products_trans_type_type USING HASH (prod_code);

CREATE TABLE agri_products_trans_type (
    tran_date Date,
    trans_date varchar(10),
    market_code varchar(10) REFERENCES crop_market(market_code),
    market_name varchar(100) REFERENCES crop_type(crop_code),
    crop_code varchar(10),
    crop_name varchar(100),
    upper_price NUMERIC(5,2),
    middle_price NUMERIC(5,2),
    lower_price NUMERIC(5,2),
    avg_price NUMERIC(5,2),
    trans_quantity bigint,

	CONSTRAINT pk_agri_products_trans_type PRIMARY KEY (tran_date, trans_date, market_code, market_name)
);
CREATE INDEX agri_products_trans_type_market_code ON agri_products_trans_type USING HASH (market_code);
CREATE INDEX agri_products_trans_type_crop_code ON agri_products_trans_type USING HASH (crop_code);