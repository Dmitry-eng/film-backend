docker-compose up -d
#####
docker cp init.sql kino-postgres:/init.sql
#####
docker exec -it kino-postgres psql -U postgres -d kino_db -f /init.sql
#####
------:8081/swagger-ui/index.html
#####



