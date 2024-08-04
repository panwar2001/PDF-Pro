package com.panwar2001.pdfpro.usecase

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

enum class EventType{Success,Error,ShowDialog,ShowDialogError}
class UiEventUseCase @Inject constructor() {
    private val _uiEvent = MutableSharedFlow<EventType>()
    val uiEventFlow: SharedFlow<EventType> = _uiEvent

    suspend operator fun invoke(event:EventType){
        _uiEvent.emit(event)
     }
}