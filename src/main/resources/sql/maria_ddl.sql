/*maria crud test : create test table */
create table test_tb(
                        tb_idx varchar(50) primary key,
                        nm varchar(50)
) comment '테스트 CRUD 테이블'

/*파일 정보 테이블*/
create table com_file(
     file_id varchar(50) comment '파일ID',
     file_seq int comment '파일 SEQ',
     file_upld_nm varchar(255) comment '파일업로드명',
     file_orign_nm varchar(255) comment '실제파일명',
     file_path varchar(500) comment '파일경로',
     file_module varchar(50) comment '업로드한 파일 모듈(test_tb)',
     file_ord int comment '파일순서',
     del_yn varchar(1) default 'N' comment '삭제여부'
     crt_dt date comment '등록일',
     crt_id varchar(50) comment '등록자',
     crt_ip_addr varchar(15) comment '등록자IP',
     upd_dt date comment '수정일',
     upd_id varchar(50) comment '수정자',
     upd_ip_addr varchar(15) comment '수정자IP',
     del_dt date comment '삭제일',
     del_id varchar(50) comment '삭제자',
     del_ip_addr varchar(15) comment '삭제자IP',
     primary key (file_id, file_seq)
) comment '파일 정보 테이블';



create table TB_SEQ(
                       NAME varchar(50) primary key commit '시퀀스테이블명',
                       SEQ NUMBER comment '시퀀스'
) comment '시퀀스관리테이블'