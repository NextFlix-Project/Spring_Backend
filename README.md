# Spring_Backend

### applications.properties
| Property Name                         | Property Values                           |
|---------------------------------------|-------------------------------------------|
| spring.datasource.url                 | `jdbc:postgresql://db-url/nextflix`       |
| spring.datasource.username            | `db username`                             |
| spring.datasource.password            | `db password`                             |
| spring.datasource.driverClassName     | `org.postgresql.Driver`                   |
| spring.jpa.database-platform          | `org.hibernate.dialect.PostgreSQLDialect` |
| spring.jpa.hibernate.ddl-auto         | `update`                                  |
| spring.session.store-type             | `jdbc`                                    | 
| spring.session.jdbc.initialize-schema | `always`                                  |
| server.servlet.session.timeout        | `31536000s`                               |
| stripe_apikey                         | `Stripe API key`                          |
| video_server_apikey                   | `API key shared with video servers`       |