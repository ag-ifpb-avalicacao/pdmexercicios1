FROM postgres
MAINTAINER Natarajan Rodrigues
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD 12345
ENV POSTGRES_DB messagedb
COPY create.sql /docker-entrypoint-initdb.d/
