/*
 Source: https://github.com/santimattius/android-drag-and-drop
 */
package com.panwar2001.pdfpro.compose.images2pdf


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : Any> DraggableGrid(
    items: List<T>,
    onMove: (Int, Int) -> Unit,
    content: @Composable (T, Boolean,Int) -> Unit,
) {

    val gridState = rememberLazyGridState()
    val dragDropState = rememberGridDragDropState(gridState, onMove)
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.pointerInput(dragDropState) {
            detectDragGestures(
            onDrag = { change, offset ->
                change.consume()
                dragDropState.onDrag(offset = offset)
            },
            onDragStart = { offset -> dragDropState.onDragStart(offset) },
            onDragEnd = { dragDropState.onDragInterrupted() },
            onDragCancel = { dragDropState.onDragInterrupted() }
        )
    },
        state = gridState,
        contentPadding = PaddingValues(4.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        itemsIndexed(items, key = { index , _ -> index }) { index, item ->
            DraggableItem(dragDropState, index) { isDragging ->
                content(item, isDragging,index+1)
            }
        }
    }
}


@ExperimentalFoundationApi
@Composable
fun LazyGridItemScope.DraggableItem(
    dragDropState: GridDragDropState,
    index: Int,
    modifier: Modifier = Modifier,
    content: @Composable (isDragging: Boolean) -> Unit,
) {
    val dragging = index == dragDropState.draggingItemIndex
    val draggingModifier = if (dragging) {
        Modifier
            .zIndex(1f)
            .graphicsLayer {
                translationX = dragDropState.draggingItemOffset.x
                translationY = dragDropState.draggingItemOffset.y
            }
    } else if (index == dragDropState.previousIndexOfDraggedItem) {
        Modifier
            .zIndex(1f)
            .graphicsLayer {
                translationX = dragDropState.previousItemOffset.value.x
                translationY = dragDropState.previousItemOffset.value.y
            }
    } else {
        Modifier.animateItemPlacement()
    }
    Box(modifier = modifier.then(draggingModifier), propagateMinConstraints = true) {
        content(dragging)
    }
}