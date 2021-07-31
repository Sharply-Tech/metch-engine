package tech.sharply.metch.engine.building_blocks.domain.events

import org.springframework.context.ApplicationEvent

abstract class GenericApplicationEvent<T>(protected val data: T, source: Any) : ApplicationEvent(source)