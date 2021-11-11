package com.juul.indexeddb

import org.w3c.dom.events.Event

public abstract class EventException(
    message: String?,
    cause: Throwable?,
    public val event: Event,
) : Exception(message, cause)

public class EventHandlerException(cause: Throwable?, event: Event) : EventException("An inner exception was thrown: $cause", cause, event)

public class ErrorEventException(event: Event) : EventException("An error event was received.", cause = null, event)
public class OpenBlockedException(public val name: String, event: Event) : EventException("Resource in use: $name.", cause = null, event)
public class AbortTransactionException(event: Event) : EventException("Transaction aborted while waiting for completion.", cause = null, event)
