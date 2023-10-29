package me.xtrm.delta.loader.event.bus.message.publish.impl;

import java.util.List;

import me.hippo.api.lwjeb.bus.AbstractAsynchronousPubSubMessageBus;
import me.hippo.api.lwjeb.message.handler.MessageHandler;
import me.hippo.api.lwjeb.message.publish.MessagePublisher;
import me.hippo.api.lwjeb.message.result.MessagePublicationResult;
import me.hippo.api.lwjeb.message.result.impl.DeadMessagePublicationResult;
import me.xtrm.delta.loader.event.bus.message.result.impl.DeltaMessagePublicationResult;

/**
 * @author xTrM_
 *
 * This is the standard implementation if a message publisher, it returns standard results.
 */
public final class DeltaMessagePublisher<T> implements MessagePublisher<T> {

    /**
     * @inheritDoc
     */
    @Override
    public MessagePublicationResult<T> publish(T topic, AbstractAsynchronousPubSubMessageBus<T> messageBus) {
        List<MessageHandler<T>> messageHandlers = messageBus.getSubscriber().subscriberMap().get(topic.getClass());
        return messageHandlers == null ? new DeadMessagePublicationResult<>() : new DeltaMessagePublicationResult<>(messageBus, messageHandlers, topic);
    }
}
