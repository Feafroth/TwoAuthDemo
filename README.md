# Test 2FA application

### Requirements

Java 11, Docker, Docker-compose

### Run application

Run docker-compose.yml to start Postgres DB

1. In rood directory run ``docker compose up``
2. Run the application with maven wrapper
``mvnw clean install spring-boot:run`` or via your favorite IDE

! DB structure creates automatically (hibernate DDL auto)

Fill the db with test user with the following SQL scripts:

Add roles:

``insert into role_table(name) values ('ROLE_USER');``

Add user:

````
insert into user_table (login, password, phone) values ('user', '$2a$12$ossqW92Ml7nBWKJnNbEqW.8JszgcI.id6DbvGfOs.aurSN7//WC7u', '+34555121314');
insert into user_role_table (user_id, role_id) values (1, 1);
````

### Test application

The application runs on the 8080 port

1. Call POST ``http://localhost:8080/sign-in``
with body:

```
{
    "login": "user",
    "password": "user"
}
```

You'll receive a temp token to enter a code from SMS

2. Call POST ``http://localhost:8080/two-auth`` use the token as Bearer authentication token

with body:

```
{
    "code": "54321"
}
```

You'll receive access JWT token

3. Call GET ``http://localhost:8080/api`` to test the access token, use the access token as Bearer authentication token