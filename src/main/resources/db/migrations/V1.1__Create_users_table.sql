create table firstscala_users (
     id uuid not null primary key default uuid_generate_v4(),
     email varchar(250) not null,
     password varchar(250) not null
);

ALTER TABLE firstscala
ADD COLUMN user_id uuid,
ADD CONSTRAINT fk_firstscala
FOREIGN KEY (user_id)
REFERENCES firstscala_users(id);
