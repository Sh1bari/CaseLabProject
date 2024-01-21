insert into roles(name)
values ('ROLE_ADMIN'),
       ('ROLE_USER');

insert into subscription(amount_of_people, cost, description, subscription_name)
values (0, 1, 'Default subscription', 'DEFAULT'),
       (5, 20000, 'Basic subscription', 'BASIC'),
       (100, 100000, 'Standard subscription', 'STANDARD'),
       (-1, 1000000, 'Enterprise subscription', 'ENTERPRISE');