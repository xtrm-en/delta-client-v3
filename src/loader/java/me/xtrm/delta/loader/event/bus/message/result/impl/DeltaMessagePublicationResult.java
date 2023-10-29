package me.xtrm.delta.loader.event.bus.message.result.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import me.hippo.api.lwjeb.bus.publish.AsynchronousPublishMessageBus;
import me.hippo.api.lwjeb.message.handler.MessageHandler;
import me.hippo.api.lwjeb.message.result.MessagePublicationResult;
import me.xtrm.xeon.loader.api.XeonProvider;

/**
 * @author xTrM_
 *
 * This is a standard result, it allows filters to be used, this will be fine for most cases
 */
public final class DeltaMessagePublicationResult<T> implements MessagePublicationResult<T> {

    /**
     * The bus.
     */
    private final AsynchronousPublishMessageBus<T> publishBus;

    /**
     * The list of handlers.
     */
    private final List<MessageHandler<T>> handlers;

    /**
     * The topic.
     */
    private final T topic;

    /**
     * Creates a new {@link DeltaMessagePublicationResult} with the desired bus, handlers, and topic.
     *
     * @param publishBus  The bus.
     * @param handlers  The handlers.
     * @param topic  The topic.
     */
    public DeltaMessagePublicationResult(AsynchronousPublishMessageBus<T> publishBus, List<MessageHandler<T>> handlers, T topic) {
        this.publishBus = publishBus;
        this.handlers = handlers;
        this.topic = topic;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void async() {
        publishBus.addMessage(this);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void async(long timeout, TimeUnit timeUnit) {
        publishBus.addMessage(this, timeout, timeUnit);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void dispatch() {
    	boolean state = XeonProvider.getXeonLoader().isClassWrappingEnabled();
    	XeonProvider.getXeonLoader().setClassWrapping(true);
    	
        for (MessageHandler<T> handler : handlers) {
            if(handler.passesFilters(topic)) {
                handler.handle(topic);
            }
        }
        
        XeonProvider.getXeonLoader().setClassWrapping(state);
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<MessageHandler<T>> getHandlers() {
        return handlers;
    }
}
