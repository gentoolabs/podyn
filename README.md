# DynamoDB to PostgreSQL / Citus replication

The `dynamodb-to-postgres` tool replicates DynamoDB tables to PostgreSQL tables, which can optionally be distributed using Citus.

## Building from source

To build a shaded JAR, run:

```
mvn package
```

The JAR file will be at `target/dynamodb-to-postgres-1.0.jar`.

## Running the JAR file

Once you've built the JAR, you can run it as follows.

```
java -jar target/dynamodb-to-postgres-1.0.jar --help
usage: dynamodb-to-postgres
 -c,--changes                   Continuously replicate changes
 -d,--data                      Replicate the current data
 -h,--help                      Show help
 -n,--num-connections <arg>     Database connection pool size (default 16)
 -r,--scan-rate <arg>           Maximum reads/sec during scan (default 25)
 -s,--schema                    Replicate the table schema
 -t,--table <arg>               DynamoDB table name(s) to replicate
 -u,--postgres-jdbc-url <arg>   PostgreSQL JDBC URL of the destination
 -x,--citus                     Create distributed tables using Citus
```

When the URL is omitted, the SQL statements that would otherwise be sent to the database are sent to stdout. When the table name is omitted, all DynamoDB tables in the region are replicated.

## Replicate schema and data from DynamoDB

After [setting up your AWS credentials](http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html#credentials-default), you can replicate the schema and do an initial data load by running:

```
export AWS_REGION=us-east-1

java -jar target/dynamodb-to-postgres-1.0.jar --postgres-jdbc-url jdbc:postgresql://host:5432/postgres?sslmode=require&user=citus&password=pw --schema --data --citus

Constructing table schema for table events
Moving data for table events
Adding new column to table events: name text
Adding new column to table events: payload jsonb
```

When `--schema` is specified, tables will be created in PostgreSQL as described in the *Schema conversion rules* section. When the `--data` argument is specified, all the data in the DynamoDB table is scanned in batches. Before a batch is sent to postgres, any fields that did not appear in the existing schema are added as new columns. After the new columns are added, `COPY` is used to load the batch into postgres.

## Replicate changes from DynamoDB

After the command completes, you can continuously stream changes using:

```
java -jar target/dynamodb-to-postgres-1.0.jar --postgres-jdbc-url jdbc:postgresql://host:5432/postgres?sslmode=require&user=citus&password=pw --changes --citus

Replicating changes for table events
...
```

The changes are processed in batches and new fields are added to the table as columns. The changes are translated into delete  or upsert statements that are sent to postgres using a connection pool in such a way that the ordering of changes to the same key is preserved.

After loading a batch of changes into the database, a checkpoint is made. If the tool is restarted, it will continue from its last checkpoint. The checkpoints are stored in DynamoDB tables prefixed with `d2p_migration_`.

## Schema conversion rules

Top-level keys in DynamoDB items are translated into columns. The initial schema is derived from the partition key, sort key and secondary indexes. When using Citus, the primary partition key becomes the distribution column.

DynamoDB types are mapped to PostgreSQL types according to the following table:

| DynamoDB type | PostgreSQL type |
| ------------- | --------------- |
| String        | text            |
| Binary        | bytea           |
| Numeric       | numeric         |
| StringSet     | jsonb           |
| NumberSet     | jsonb           |
| BinarySet     | jsonb           |
| Map           | jsonb           |
| List          | jsonb           |
| Boolean       | boolean         |
| Null          | text            |

For example, a DynamoDB table named `clicks` has primary partition key: `sitename` (String), primary sort key: `time` (String), and a secondary index named `pageid-index` on: `pageid` (Numeric). The following statements will be sent to PostgreSQL:

```
CREATE TABLE clicks (
   sitename text NOT NULL,
   "time" text NOT NULL,
   pageid numeric NOT NULL,
   PRIMARY KEY (site_name, "time")
);
CREATE INDEX "pageid-index" ON clicks (pageid);
SELECT create_distributed_table('clicks', 'siteid');
```

A new column is added to the PostgreSQL table whenever an item is processed during the initial data load or while replicating changes that contains a new key. For example, if a `payload` (String) key is encountered, the following command would be sent to the database:

```
ALTER TABLE clicks ADD COLUMN payload text;
```

In DynamoDB, the same key can appear with different types as long as it's it's not part of the primary key or a secondary index. If a key appears that maps to a different PostgreSQL type then the type name is added as a suffix. For example, if an item appears which contains `"clicks" -> true`, then a new column would be added:

```
ALTER TABLE clicks ADD COLUMN payload_boolean boolean;
```

There are currently no guarantees in terms of which type is used for the 'main' column. Using the same key for different (postgres) types is best avoided.
