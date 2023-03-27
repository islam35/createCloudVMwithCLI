CREATE TABLE employee
(
 employeeName varchar(100) NOT NULL,
  employeeId varchar(11) NOT NULL ,
 employeeAddress varchar(100) DEFAULT NULL,
 employeeEmail varchar(100) DEFAULT NULL,
 PRIMARY KEY (employeeId)
);

CREATE TABLE userInfo
(
    userEmail varchar(100) NOT NULL,
    password varchar(11) NOT NULL ,
    PRIMARY KEY (userEmail)
);
CREATE TABLE IF NOT EXISTS public.aws_map
(
    region text COLLATE pg_catalog."default" NOT NULL,
    vcpu text COLLATE pg_catalog."default" NOT NULL,
    memory text COLLATE pg_catalog."default" NOT NULL
);
CREATE TABLE IF NOT EXISTS public.azure_aws_data
(
    producthash text COLLATE pg_catalog."default" NOT NULL,
    sku text COLLATE pg_catalog."default" NOT NULL,
    "vendorName" text COLLATE pg_catalog."default" NOT NULL,
    region text COLLATE pg_catalog."default",
    service text COLLATE pg_catalog."default" NOT NULL,
    productfamily text COLLATE pg_catalog."default" NOT NULL DEFAULT ''::text,
    attributes json NOT NULL,
    prices jsonb NOT NULL,
    CONSTRAINT azuredata_pkey PRIMARY KEY (producthash)
    );
CREATE TABLE IF NOT EXISTS public.azure_map
(
    region text COLLATE pg_catalog."default" NOT NULL,
    vcpu text COLLATE pg_catalog."default" NOT NULL,
    skuname text COLLATE pg_catalog."default" NOT NULL,
    memory text COLLATE pg_catalog."default" NOT NULL
);
CREATE TABLE IF NOT EXISTS public.dropdown_map
(
    region text COLLATE pg_catalog."default" NOT NULL,
    vcpu text COLLATE pg_catalog."default" NOT NULL,
    memory text COLLATE pg_catalog."default" NOT NULL
);

CREATE TABLE IF NOT EXISTS public.createdvms
(
    username text COLLATE pg_catalog."default" NOT NULL,
    vminstanceid text COLLATE pg_catalog."default" NOT NULL,
    os text COLLATE pg_catalog."default" NOT NULL,
    region text COLLATE pg_catalog."default" NOT NULL,
    creationtime text COLLATE pg_catalog."default" NOT NULL,
    groupname text COLLATE pg_catalog."default" NOT NULL,
    vendorname text COLLATE pg_catalog."default" NOT NULL,
    skuorinstancename text COLLATE pg_catalog."default" NOT NULL,
    vmname text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "CreatedVMs_pkey" PRIMARY KEY (vminstanceid)
    )