/*
 * This file is part of NetMinecraft - https://github.com/RaphiMC/NetMinecraft
 * Copyright (C) 2022-2026 RK_01/RaphiMC and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.raphimc.netminecraft.netty.codec;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.ScheduledFuture;
import net.raphimc.netminecraft.constants.MCPipeline;

import java.util.concurrent.TimeUnit;

public class FlushConsolidationHandler extends ChannelDuplexHandler {

    private final int flushIntervalMs;
    private ScheduledFuture<?> resetTask;

    private int flushCount;
    private int lastFlushCount;
    private ScheduledFuture<?> flushTask;

    public FlushConsolidationHandler() {
        this(10);
    }

    public FlushConsolidationHandler(final int flushIntervalMs) {
        this.flushIntervalMs = flushIntervalMs;
    }

    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        this.resetTask = ctx.executor().scheduleAtFixedRate(() -> {
            this.lastFlushCount = this.flushCount;
            this.flushCount = 0;
            if (!this.shouldConsolidate(ctx)) {
                this.stopConsolidation(ctx);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        if (this.resetTask != null) {
            this.resetTask.cancel(false);
            this.resetTask = null;
        }
        this.resetAndStopConsolidation(ctx);
    }

    @Override
    public void flush(final ChannelHandlerContext ctx) throws Exception {
        this.flushCount++;
        if (this.shouldConsolidate(ctx)) {
            if (this.flushTask == null) {
                this.flushTask = ctx.executor().scheduleAtFixedRate(ctx::flush, this.flushIntervalMs, this.flushIntervalMs, TimeUnit.MILLISECONDS);
            }
        } else {
            super.flush(ctx);
        }
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        this.resetAndStopConsolidation(ctx);
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void disconnect(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        this.resetAndStopConsolidation(ctx);
        super.disconnect(ctx, promise);
    }

    @Override
    public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        this.resetAndStopConsolidation(ctx);
        super.close(ctx, promise);
    }

    @Override
    public void channelWritabilityChanged(final ChannelHandlerContext ctx) throws Exception {
        if (!ctx.channel().isWritable()) {
            if (this.flushTask != null) {
                ctx.flush();
            }
        }
        super.channelWritabilityChanged(ctx);
    }

    private boolean shouldConsolidate(final ChannelHandlerContext ctx) {
        final int threshold = ctx.channel().attr(MCPipeline.FLUSH_CONSOLIDATION_PPS_THRESHOLD_ATTRIBUTE_KEY).get();
        return this.flushCount >= threshold || this.lastFlushCount >= threshold;
    }

    private void stopConsolidation(final ChannelHandlerContext ctx) {
        if (this.flushTask != null) {
            this.flushTask.cancel(false);
            this.flushTask = null;
            ctx.flush();
        }
    }

    private void resetAndStopConsolidation(final ChannelHandlerContext ctx) {
        this.flushCount = 0;
        this.lastFlushCount = 0;
        this.stopConsolidation(ctx);
    }

}
