/*h2 crud test : create test table */
create table test_tb(
                        tb_idx varchar(50) primary key,
                        nm varchar(50)
);


/*파일 정보 테이블*/
create table com_file(
                         file_id varchar(50) ,
                         file_seq int ,
                         file_upld_nm varchar(255) ,
                         file_orign_nm varchar(255) ,
                         file_path varchar(500) ,
                         file_module varchar(50),
                         file_ord int ,
                         del_yn varchar(1) default 'N' ,
                         crt_dt date ,
                         crt_id varchar(50) ,
                         crt_ip_addr varchar(15) ,
                         upd_dt date ,
                         upd_id varchar(50) ,
                         upd_ip_addr varchar(15) ,
                         del_dt date ,
                         del_id varchar(50) ,
                         del_ip_addr varchar(15) ,
                         primary key (file_id, file_seq)
)

create table TB_SEQ(
    NAME varchar(50) primary key ,
    SEQ NUMBER
)