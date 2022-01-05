DROP TABLE IF EXISTS prod_type;
DROP TABLE IF EXISTS trans_type;
DROP TABLE IF EXISTS market_type;

CREATE TABLE market_type (
    market_type VARCHAR(1),
    market_code VARCHAR(10),
    market_name VARCHAR(100),
	CONSTRAINT pk_market_type PRIMARY KEY (market_type, market_code, market_name)
);

CREATE TABLE prod_type (
    prod_type VARCHAR(1),
    prod_code VARCHAR(10),
    prod_name VARCHAR(100),
	CONSTRAINT pk_prod_type PRIMARY KEY (prod_type, prod_code, prod_name)
);

CREATE TABLE trans_type (
	tran_date DATE,
    prod_type VARCHAR(1),
    prod_code VARCHAR(10),
    prod_name VARCHAR(100),
    market_code VARCHAR(10),
    market_name VARCHAR(100),
	upper_price NUMERIC(10,2),
	middle_rice NUMERIC(10,2),
	lower_price NUMERIC(10,2),
	avg_rice NUMERIC(10,2),
    trans_quantity BIGINT,
	trans_date VARCHAR(10),
	CONSTRAINT pk_agri_products_trans_type PRIMARY KEY (trans_DATE, prod_code, prod_name, market_code, market_name)
);