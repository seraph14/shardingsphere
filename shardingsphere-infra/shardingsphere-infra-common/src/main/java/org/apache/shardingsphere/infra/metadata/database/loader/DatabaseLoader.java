/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.infra.metadata.database.loader;

import org.apache.shardingsphere.infra.database.type.DatabaseType;
import org.apache.shardingsphere.infra.metadata.database.ShardingSphereDatabase;
import org.apache.shardingsphere.infra.metadata.schema.ShardingSphereSchema;
import org.apache.shardingsphere.infra.metadata.schema.builder.spi.DialectSystemSchemaBuilder;
import org.apache.shardingsphere.infra.metadata.schema.loader.SchemaLoader;
import org.apache.shardingsphere.infra.rule.ShardingSphereRule;
import org.apache.shardingsphere.spi.singleton.SingletonSPIRegistry;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * Database loader.
 */
public final class DatabaseLoader {
    
    private static final Map<String, DialectSystemSchemaBuilder> DIALECT_SYSTEM_SCHEMA_BUILDERS
            = SingletonSPIRegistry.getSingletonInstancesMap(DialectSystemSchemaBuilder.class, DialectSystemSchemaBuilder::getDatabaseType);
    
    /**
     * Load database.
     * 
     * @param schemaName schema name
     * @param databaseType database type
     * @param dataSourceMap data source map
     * @param rules rules
     * @param props properties
     * @return loaded database
     * @throws SQLException SQL exception
     */
    public static ShardingSphereDatabase load(final String schemaName, final DatabaseType databaseType, final Map<String, DataSource> dataSourceMap, 
                                              final Collection<ShardingSphereRule> rules, final Properties props) throws SQLException {
        Map<String, ShardingSphereSchema> schemas = new LinkedHashMap<>();
        schemas.put(schemaName, SchemaLoader.load(dataSourceMap, rules, props));
        findDialectSystemSchemaBuilder(databaseType).ifPresent(optional -> schemas.putAll(optional.build(schemaName)));
        return new ShardingSphereDatabase(schemas);
    }
    
    /**
     * Load database.
     * 
     * @param schemaName schema name
     * @param databaseType database type
     * @return loaded database
     */
    public static ShardingSphereDatabase load(final String schemaName, final DatabaseType databaseType) {
        Map<String, ShardingSphereSchema> schemas = new LinkedHashMap<>();
        findDialectSystemSchemaBuilder(databaseType).ifPresent(optional -> schemas.putAll(optional.build(schemaName)));
        return new ShardingSphereDatabase(schemas);
    }
    
    private static Optional<DialectSystemSchemaBuilder> findDialectSystemSchemaBuilder(final DatabaseType databaseType) {
        return Optional.ofNullable(DIALECT_SYSTEM_SCHEMA_BUILDERS.get(databaseType.getName()));
    }
}
