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

package org.apache.shardingsphere.infra.database.type.dialect;

import org.apache.shardingsphere.infra.database.metadata.dialect.MySQLDataSourceMetaData;
import org.apache.shardingsphere.sql.parser.sql.common.constant.QuoteCharacter;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class MySQLDatabaseTypeTest {
    
    @Test
    public void assertGetName() {
        assertThat(new MySQLDatabaseType().getName(), is("MySQL"));
    }
    
    @Test
    public void assertGetJdbcUrlPrefixes() {
        assertThat(new MySQLDatabaseType().getJdbcUrlPrefixes(), is(Arrays.asList("jdbc:mysql:", "jdbc:mysqlx:")));
    }
    
    @Test
    public void assertGetDataSourceMetaData() {
        assertThat(new MySQLDatabaseType().getDataSourceMetaData("jdbc:mysql://127.0.0.1/ds", "root"), instanceOf(MySQLDataSourceMetaData.class));
    }
    
    @Test
    public void assertGetSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.getSchema()).thenReturn("ds");
        assertThat(new MySQLDatabaseType().getSchema(connection), is("ds"));
    }
    
    @Test
    public void assertFormatTableNamePattern() {
        assertThat(new MySQLDatabaseType().formatTableNamePattern("tbl"), is("tbl"));
    }
    
    @Test
    public void assertGetQuoteCharacter() {
        QuoteCharacter actual = new MySQLDatabaseType().getQuoteCharacter();
        assertThat(actual.getStartDelimiter(), is("`"));
        assertThat(actual.getEndDelimiter(), is("`"));
    }
    
    @Test
    public void assertContainsSystemSchema() {
        assertTrue(new MySQLDatabaseType().containsSystemSchema("information_schema"));
        assertTrue(new MySQLDatabaseType().containsSystemSchema("performance_schema"));
        assertTrue(new MySQLDatabaseType().containsSystemSchema("mysql"));
        assertTrue(new MySQLDatabaseType().containsSystemSchema("sys"));
        assertFalse(new MySQLDatabaseType().containsSystemSchema("sharding_db"));
    }
}
