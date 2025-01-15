
FROM postgres:15

ENV POSTGRES_DB=rentcar
    POSTGRES_USER=admin
    POSTGRES_PASSWORD=postgres

EXPOSE 5432

COPY ./initdb /docker-entrypoint-initdb.d/
