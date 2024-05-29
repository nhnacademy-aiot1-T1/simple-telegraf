create table motor_status (
                              id bigint not null auto_increment,
                              created_at datetime(6),
                              is_on bit,
                              motor_name varchar(255),
                              primary key (id)
)