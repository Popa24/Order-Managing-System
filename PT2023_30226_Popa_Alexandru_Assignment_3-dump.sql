--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: client; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.client (
                               id integer NOT NULL,
                               name character varying(255),
                               surname character varying(255),
                               email character varying(255),
                               city character varying(255),
                               street character varying(255),
                               streetno character varying(255)
);


ALTER TABLE public.client OWNER TO postgres;

--
-- Name: client_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.client_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.client_id_seq OWNER TO postgres;

--
-- Name: client_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.client_id_seq OWNED BY public.client.id;


--
-- Name: log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.log (
                            id integer NOT NULL,
                            order_id bigint NOT NULL,
                            client_id bigint NOT NULL,
                            total_amount double precision NOT NULL,
                            "timestamp" timestamp without time zone NOT NULL
);


ALTER TABLE public.log OWNER TO postgres;

--
-- Name: log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.log_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.log_id_seq OWNER TO postgres;

--
-- Name: log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.log_id_seq OWNED BY public.log.id;


--
-- Name: order; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."order" (
                                id integer NOT NULL,
                                client_id bigint
);


ALTER TABLE public."order" OWNER TO postgres;

--
-- Name: order_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.order_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.order_id_seq OWNER TO postgres;

--
-- Name: order_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.order_id_seq OWNED BY public."order".id;


--
-- Name: order_product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.order_product (
                                      order_id bigint NOT NULL,
                                      product_id bigint NOT NULL
);


ALTER TABLE public.order_product OWNER TO postgres;

--
-- Name: order_product_quantity; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.order_product_quantity (
                                               order_id bigint NOT NULL,
                                               opq_quantity integer,
                                               product_id bigint NOT NULL
);


ALTER TABLE public.order_product_quantity OWNER TO postgres;

--
-- Name: product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product (
                                id integer NOT NULL,
                                productname character varying(255),
                                quantity integer,
                                price double precision
);


ALTER TABLE public.product OWNER TO postgres;

--
-- Name: product_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.product_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.product_id_seq OWNER TO postgres;

--
-- Name: product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.product_id_seq OWNED BY public.product.id;


--
-- Name: client id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client ALTER COLUMN id SET DEFAULT nextval('public.client_id_seq'::regclass);


--
-- Name: log id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.log ALTER COLUMN id SET DEFAULT nextval('public.log_id_seq'::regclass);


--
-- Name: order id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."order" ALTER COLUMN id SET DEFAULT nextval('public.order_id_seq'::regclass);


--
-- Name: product id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product ALTER COLUMN id SET DEFAULT nextval('public.product_id_seq'::regclass);


--
-- Data for Name: client; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.client (id, name, surname, email, city, street, streetno) FROM stdin;
2	Popa	Alexandru	alex24popa@gmail.com	Petrosani	1 decembrie 1918 bloc 74 	etaj 6 apartament 23
1	Pasare	Alina	alex24popa@gmail.com	Cluj-Napoca	ceva	23
\.


--
-- Data for Name: log; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.log (id, order_id, client_id, total_amount, "timestamp") FROM stdin;
1	27	2	1900	2023-05-01 15:49:06.726414
2	28	1	110	2023-05-01 16:13:10.13212
3	29	2	330	2023-05-01 16:26:24.589378
4	30	1	125	2023-05-01 17:06:04.082187
5	31	1	100	2023-05-01 17:08:45.660358
6	32	2	75	2023-05-01 17:13:51.877755
7	33	2	29100	2023-05-01 17:18:57.106704
8	34	1	850420	2023-05-01 17:45:00.2142
\.


--
-- Data for Name: order; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public."order" (id, client_id) FROM stdin;
26	1
27	2
28	1
30	1
31	1
32	2
33	2
34	1
\.


--
-- Data for Name: order_product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.order_product (order_id, product_id) FROM stdin;
26	2
26	1
27	3
27	2
28	1
30	1
30	2
31	1
31	2
32	3
32	2
33	5
33	2
34	4
34	1
34	3
34	5
34	2
\.


--
-- Data for Name: order_product_quantity; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.order_product_quantity (order_id, opq_quantity, product_id) FROM stdin;
26	100	2
26	200	1
27	90	3
27	100	2
28	11	1
30	5	1
30	5	2
31	4	1
31	4	2
32	3	3
32	3	2
33	10	5
33	10	2
34	100	4
34	100	1
34	100	3
34	100	5
34	100	2
\.


--
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.product (id, productname, quantity, price) FROM stdin;
4	mere	900	0.2
1	laptop asus tuf f15	400	5000
3	Armani Code (parfum barbati)	900	594
5	Iphone 12 pro max 256 gb	190	2800
2	Incarcator original Iphone 	9890	110
\.


--
-- Name: client_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.client_id_seq', 2, true);


--
-- Name: log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.log_id_seq', 8, true);


--
-- Name: order_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.order_id_seq', 34, true);


--
-- Name: product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.product_id_seq', 5, true);


--
-- Name: log log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.log
    ADD CONSTRAINT log_pkey PRIMARY KEY (id);


--
-- Name: client pk_client; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT pk_client PRIMARY KEY (id);


--
-- Name: order pk_order; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."order"
    ADD CONSTRAINT pk_order PRIMARY KEY (id);


--
-- Name: order_product pk_order_product; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_product
    ADD CONSTRAINT pk_order_product PRIMARY KEY (order_id, product_id);


--
-- Name: order_product_quantity pk_order_product_quantity; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_product_quantity
    ADD CONSTRAINT pk_order_product_quantity PRIMARY KEY (order_id, product_id);


--
-- Name: product pk_product; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT pk_product PRIMARY KEY (id);


--
-- Name: order fk_order_on_client; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."order"
    ADD CONSTRAINT fk_order_on_client FOREIGN KEY (client_id) REFERENCES public.client(id);


--
-- Name: order_product_quantity fk_order_product_quantity_on_order_entity; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_product_quantity
    ADD CONSTRAINT fk_order_product_quantity_on_order_entity FOREIGN KEY (order_id) REFERENCES public."order"(id);


--
-- Name: order_product_quantity fk_order_product_quantity_on_product_entity; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_product_quantity
    ADD CONSTRAINT fk_order_product_quantity_on_product_entity FOREIGN KEY (product_id) REFERENCES public.product(id);


--
-- Name: order_product fk_ordpro_on_order_entity; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_product
    ADD CONSTRAINT fk_ordpro_on_order_entity FOREIGN KEY (order_id) REFERENCES public."order"(id);


--
-- Name: order_product fk_ordpro_on_product_entity; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.order_product
    ADD CONSTRAINT fk_ordpro_on_product_entity FOREIGN KEY (product_id) REFERENCES public.product(id);


--
-- PostgreSQL database dump complete
--

