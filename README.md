# Introduction to Flyway

## Flyway Command-line tool
1. Download CLI
2. Check JDBC driver to your database
3. Modify `<install-dir>/conf/flyway.conf
```
flyway.driver=org.hsqldb.jdbcDriver
flyway.url=jdbc:hsqldb:file:/db/flyway_sample
flyway.user=SA
flyway.password=mySecretPwd
flyway.connectRetries=10
flyway.initSql=SET ROLE 'myuser'
flyway.defaultSchema=schema1
flyway.schemas=schema1,schema2,schema3
flyway.createSchemas=true
flyway.table=schema_history
flyway.tablespace=my_tablespace
flyway.locations=classpath:com.mycomp.migration,database/migrations,filesystem:/sql-migrations,s3:migrationsBucket,gcs:migrationsBucket
flyway.sqlMigrationPrefix=Migration-
flyway.undoSqlMigrationPrefix=downgrade
flyway.repeatableSqlMigrationPrefix=RRR
flyway.sqlMigrationSeparator=__
flyway.sqlMigrationSuffixes=.sql,.pkg,.pkb
flyway.stream=true
flyway.batch=true
flyway.encoding=ISO-8859-1
flyway.placeholderReplacement=true
flyway.placeholders.aplaceholder=value
flyway.placeholders.otherplaceholder=value123
flyway.placeholderPrefix=#[
flyway.placeholderSuffix=]
flyway.resolvers=com.mycomp.project.CustomResolver,com.mycomp.project.AnotherResolver
flyway.skipDefaultCallResolvers=false
flyway.callbacks=com.mycomp.project.CustomCallback,com.mycomp.project.AnotherCallback
flyway.skipDefaultCallbacks=false
flyway.target=5.1
flyway.outOfOrder=false
flyway.outputQueryResults=false
flyway.validateOnMigrate=true
flyway.cleanOnValidationError=false
flyway.mixed=false
flyway.group=false
flyway.cleanDisabled=false
flyway.baselineOnMigrate=false
flyway.installedBy=my-user
flyway.errorOverrides=99999:17110:E,42001:42001:W
flyway.dryRunOutput=/my/sql/dryrun-outputfile.sql
flyway.lockRetryCount=10
flyway.oracle.sqlplus=true
flyway.oracle.sqlplusWarn=true
flyway.workingDirectory=C:/myProject
flyway.jdbcProperties.myProperty=value
```

4. Create migrations

Versioned Migrations:
```sql
-- V1__add_new_tables.sql

CREATE TABLE car (
    id INT NOT NULL PRIMARY KEY,
    license_plate VARCHAR NOT NULL,
    color VARCHAR NOT NULL
);

ALTER TABLE owner ADD driver_license_id VARCHAR;

INSERT INTO brand (name) VALUES ('DeLorean');
```

Undo Migrations: 
- Applied last and applied everytime checksum changes
- Typically used: 
  - (Re)creating views/procedures/functions/packages/... 
  - bulk reference data reinserts
```sql
-- U1__drop_tables.sql

DELETE FROM brand WHERE name='DeLorean';

ALTER TABLE owner DROP driver_license_id;

DROP TABLE car;
```

Repeatable Migrations:
```sql
-- R__blue_cars_view.sql

CREATE OR REPLACE VIEW blue_cars AS
    SELECT id, license_plate FROM cars WHERE color='blue';
```


5. Run `flyway migrate`

```
> flyway migrate

Flyway 9.8.1 by Redgate

Database: jdbc:h2:file:flyway.db (H2 1.3)
Successfully validated 5 migrations (execution time 00:00.010s)
Creating Schema History table: "PUBLIC"."flyway_schema_history"
Current version of schema "PUBLIC": << Empty Schema >>
Migrating schema "PUBLIC" to version 1 - First
Migrating schema "PUBLIC" to version 1.1 - View
Successfully applied 2 migrations to schema "PUBLIC" (execution time 00:00.030s).
```

```
> flyway migrate -outputType=json

{
  "initialSchemaVersion": null,
  "targetSchemaVersion": "1",
  "schemaName": "public",
  "migrations": [
    {
      "category": "Versioned",
      "version": "1",
      "description": "first",
      "type": "SQL",
      "filepath": "C:\\flyway\\sql\\V1__first.sql",
      "executionTime": 0
    },
    {
      "category": "Repeatable",
      "version": "",
      "description": "repeatable",
      "type": "SQL",
      "filepath": "C:\\flyway\\sql\\R__repeatable.sql",
      "executionTime": 0
    }
  ],
  "migrationsExecuted": 2,
  "flywayVersion": "9.8.1",
  "database": "testdb",
  "warnings": [],
  "operation": "migrate"
}
```


Other commands:
```
flyway clean
flyway info
flyway validate
flyway undo (available in teams edition)
... etc 
```


reference:
- https://flywaydb.org/documentation/concepts/migrations#versioned-migrations
- https://flywaydb.org/documentation/usage/commandline/

<br>

## Execute Flyway Database Migrations on Startup 

Spring Boot calls `Flyway.migrate()` to perform the database migration. 
If you would like more control, provide a @Bean that implements `FlywayMigrationStrategy`.

By default, Flyway autowires the (`@Primary`) `DataSource` in your context and uses that for migrations. If you like to use a different `DataSource`, you can create one and mark its `@Bean` as `@FlywayDataSource`. If you do so and want two data sources, remember to create another one and mark it as `@Primary`. Alternatively, you can use Flyway’s native `DataSource` by setting `spring.flyway.[url,user,password]` in external properties. Setting either `spring.flyway.url` or `spring.flyway.user` is sufficient to cause Flyway to use its own `DataSource`. If any of the three properties has not been set, the value of its equivalent `spring.datasource` property will be used.

reference: 
- https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/


### Thoughts/Discussions:
- Should flyway run in CI using command line or as a part of Spring Boot application startup?
- No point of writing undo scripts if using community edition?
- Should we run flyway for all unit tests and integration tests?
- How to use Test Containers easily (share).
