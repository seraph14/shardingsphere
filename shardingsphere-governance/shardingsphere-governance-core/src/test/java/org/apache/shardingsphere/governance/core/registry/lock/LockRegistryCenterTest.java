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

package org.apache.shardingsphere.governance.core.registry.lock;

import org.apache.shardingsphere.governance.core.lock.node.LockNode;
import org.apache.shardingsphere.governance.repository.spi.RegistryCenterRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public final class LockRegistryCenterTest {
    
    @Mock
    private RegistryCenterRepository registryCenterRepository;
    
    private LockRegistryCenter lockRegistryCenter;
    
    @Before
    public void setUp() throws ReflectiveOperationException {
        lockRegistryCenter = new LockRegistryCenter(registryCenterRepository);
        Field field = lockRegistryCenter.getClass().getDeclaredField("repository");
        field.setAccessible(true);
        field.set(lockRegistryCenter, registryCenterRepository);
    }
    
    @Test
    public void assertTryLock() {
        lockRegistryCenter.tryLock("test", 50L);
        verify(registryCenterRepository).tryLock(eq(new LockNode().getLockNodePath("test")), eq(50L), eq(TimeUnit.MILLISECONDS));
    }
    
    @Test
    public void assertReleaseLock() {
        lockRegistryCenter.releaseLock("test");
        verify(registryCenterRepository).releaseLock(eq(new LockNode().getLockNodePath("test")));
    }
    
    @Test
    public void assertDeleteLockAck() {
        lockRegistryCenter.deleteLockAck("test");
        verify(registryCenterRepository).delete(anyString());
    }
}
