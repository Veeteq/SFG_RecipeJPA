create table cate_seq (next_val bigint) engine=MyISAM;
insert into cate_seq values ( 1 );
create table categories (cate_id bigint not null auto_increment, cate_name_tx varchar(255), cate_type_tx varchar(255), primary key (cate_id)) engine=MyISAM;
create table counterparties (cprt_id bigint not null auto_increment, cprt_name_tx varchar(255), primary key (cprt_id)) engine=MyISAM;
create table cprt_seq (next_val bigint) engine=MyISAM;
insert into cprt_seq values ( 1 );
create table evnt_seq (next_val bigint) engine=MyISAM;
insert into evnt_seq values ( 1 );
create table expe_seq (next_val bigint) engine=MyISAM;
insert into expe_seq values ( 1 );
create table expenses (expe_id bigint not null auto_increment, expe_comm_tx varchar(255), expe_item_cn decimal(19,2), oper_dt date, expe_pric_am decimal(19,2), item_id bigint not null, user_id bigint not null, primary key (expe_id)) engine=MyISAM;
create table fin_events (evnt_id bigint not null auto_increment, evnt_name_tx varchar(255), evnt_dt date, cprt_id bigint not null, primary key (evnt_id)) engine=MyISAM;
create table inco_seq (next_val bigint) engine=MyISAM;
insert into inco_seq values ( 1 );
create table incomes (inco_id bigint not null auto_increment, inco_pric_am decimal(19,2), inco_comm_tx varchar(255), oper_dt date, item_id bigint not null, user_id bigint not null, primary key (inco_id)) engine=MyISAM;
create table ingredient_seq (next_val bigint) engine=MyISAM;
insert into ingredient_seq values ( 1 );
create table ingredients (ingredient_id bigint not null auto_increment, ingredient_name varchar(255), amount decimal(19,2), recipe_id bigint, uom_id bigint, primary key (ingredient_id)) engine=MyISAM;
create table item_seq (next_val bigint) engine=MyISAM;
insert into item_seq values ( 1 );
create table items (item_id bigint not null auto_increment, item_name_tx varchar(255), cate_id bigint not null, primary key (item_id)) engine=MyISAM;
create table note_seq (next_val bigint) engine=MyISAM;
insert into note_seq values ( 1 );
create table notes (note_id bigint not null auto_increment, notes longtext, primary key (note_id)) engine=MyISAM;
create table recipe_categories (category_id bigint not null auto_increment, category_name varchar(255), primary key (category_id)) engine=MyISAM;
create table recipe_category (recipe_id bigint not null auto_increment, category_id bigint not null, primary key (recipe_id, category_id)) engine=MyISAM;
create table recipe_category_seq (next_val bigint) engine=MyISAM;
insert into recipe_category_seq values ( 1 );
create table recipe_seq (next_val bigint) engine=MyISAM;
insert into recipe_seq values ( 1 );
create table recipes (recipe_id bigint not null auto_increment, recipe_name varchar(255), cook_time integer, description varchar(255), difficulty varchar(255), directions longtext, image longblob, prep_time integer, servings integer, source varchar(255), title varchar(255), url varchar(255), notes_id bigint, primary key (recipe_id)) engine=MyISAM;
create table units_of_measure (uom_id bigint not null auto_increment, uom_name varchar(255), primary key (uom_id)) engine=MyISAM;
create table uom_seq (next_val bigint) engine=MyISAM;
insert into uom_seq values ( 1 );
create table user_seq (next_val bigint) engine=MyISAM;
insert into user_seq values ( 1 );
create table users (user_id bigint not null auto_increment, user_name_tx varchar(255), primary key (user_id)) engine=MyISAM;
alter table expenses add constraint FK7cu4417yclb0q4uj9rup4342e foreign key (item_id) references items (item_id);
alter table expenses add constraint FKhpk0n2cbnfiuu5nrgl0ika3hq foreign key (user_id) references users (user_id);
alter table fin_events add constraint FKlb5eamstni12p7cfavs9lyho0 foreign key (cprt_id) references counterparties (cprt_id);
alter table incomes add constraint FKlp8lqt6oj82j8g2rh3mj0k9dl foreign key (item_id) references items (item_id);
alter table incomes add constraint FKfq6qeso6vbt9wu7dyhnx8tpu9 foreign key (user_id) references users (user_id);
alter table ingredients add constraint FK7p08vcn6wf7fd6qp79yy2jrwg foreign key (recipe_id) references recipes (recipe_id);
alter table ingredients add constraint FKog551e34vp2grp2dly147mhug foreign key (uom_id) references units_of_measure (uom_id);
alter table items add constraint FKsoyc2gpobqin7cqb8bgkhi2wl foreign key (cate_id) references categories (cate_id);
alter table recipe_category add constraint FK9uunl4sfs3v2ehux2r8hm9rc5 foreign key (category_id) references recipe_categories (category_id);
alter table recipe_category add constraint FKm7277epjcd3or9ss6sbi03wh4 foreign key (recipe_id) references recipes (recipe_id);
alter table recipes add constraint FK74kcww8qjnl4ursb95gfk9or6 foreign key (notes_id) references notes (note_id);
