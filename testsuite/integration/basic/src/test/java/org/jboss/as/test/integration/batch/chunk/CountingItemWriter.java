/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2015, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.test.integration.batch.chunk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.ItemWriter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
@Named
public class CountingItemWriter implements ItemWriter {

    static final List<Object> WRITTEN_ITEMS = Collections.synchronizedList(new ArrayList<>());

    @Inject
    @BatchProperty(name = "writer.sleep.time")
    private long sleep;

    @Override
    public void open(final Serializable checkpoint) throws Exception {
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public void writeItems(final List<Object> items) throws Exception {
        WRITTEN_ITEMS.addAll(items);
        if (sleep > 0) {
            TimeUnit.MILLISECONDS.sleep(sleep);
        }
    }

    @Override
    public Serializable checkpointInfo() throws Exception {
        synchronized (WRITTEN_ITEMS) {
            return new ArrayList<>(WRITTEN_ITEMS);
        }
    }
}
