# Testing

- Open any REST Client.

- Get users: localhost:8080/users
- Add your users: 
	+ POST localhost:8080/users {"email":"admin@mail.com", "password":"my_pass", "role":"ADMIN"}
	+ POST localhost:8080/users {"email":"user@mail.com", "password":"my_pass", "role":"USER"}


- Authenticate against the authentication endpoint localhost:8080/auth/login with this your user data (POST request):
- 
```
	{"email":"admin@mail.com", "password":"my_pass"}
	{"email":"user@mail.com", "password":"my_pass"}
```

- Header: Content-Type application/json
	+ Example with curl:

```
curl -v -H "Content-Type: application/json" -d "{\"email\":\"admin@mail.com\", \"password\":\"my_pass\"}" localhost:8080/auth/login
```

- Response:

```
{
  "email": "admin@mail.com",
  "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxLG5hbUBjb2RlamF2YS5..."
}
```


- Then send a request to restricted endpoint using the token:
	+ Example with curl:

```
curl -v -H "Authorization: Bearer [accessToken]" localhost:8080/products
```

