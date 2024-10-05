package com.weather.feature.managelocations.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex
import timber.log.Timber


@OptIn(ExperimentalFoundationApi::class)
internal fun Modifier.locationsClickable(
    inSelectionMode: Boolean,
    onSelectionMode: () -> Unit,
    onItemSelected: () -> Unit,
    onLongClick: () -> Unit,
): Modifier {
    return this then if (inSelectionMode) {
        Modifier.clickable { onSelectionMode() }
    } else {
        Modifier.combinedClickable(
            onClick = { onItemSelected() },
            onLongClick = { onLongClick() }
        )
    }
}

internal fun Modifier.detectLongPressGesture(
    lazyListState: LazyListState,
    dragAndDropListItemState: DragAndDropListItemState,
): Modifier {
    return this then Modifier.pointerInput(lazyListState) {
        detectDragGesturesAfterLongPress(
            onDragStart = { offset ->
                dragAndDropListItemState.onDragStart(offset)
            },
            onDrag = { change, dragAmount ->
                change.consume()
                dragAndDropListItemState.onDrag(dragAmount.y)

            },
            onDragEnd = {
                dragAndDropListItemState.onDragEnd()
            },
            onDragCancel = {
                dragAndDropListItemState.onDragCancelOrInterrupted()
            }
        )
    }
}

internal fun Modifier.draggableItem(
    draggableState: DragAndDropListItemState,
    listIndex: Int,
): Modifier {
    return this then if (draggableState.draggableItemIndex == listIndex) {
        Modifier
            .zIndex(1f)
            .graphicsLayer {
                translationY = draggableState.draggableItemOffsetDelta
            }
    } else {
        Modifier
    }

}

@Composable
fun rememberDragAndDropListItem(
    lazyListState: LazyListState,
    onUpdateData: (fromIndex: Int, toIndex: Int) -> Unit,
    onReorderEnd: (fromIndex: Int, toIndex: Int) -> Unit,
): DragAndDropListItemState {
    return remember(lazyListState) {
        DragAndDropListItemState(
            lazyListState = lazyListState,
            onUpdateData = onUpdateData,
            onReorderEnd = onReorderEnd
        )
    }
}

class DragAndDropListItemState(
    private val lazyListState: LazyListState,
    private val onUpdateData: (fromIndex: Int, toIndex: Int) -> Unit,
    private val onReorderEnd: (fromIndex: Int, toIndex: Int) -> Unit,
) {
    private var draggingItem: LazyListItemInfo? by mutableStateOf(null)
    var draggableItemIndex: Int? by mutableStateOf(null)
    var draggableItemOffsetDelta: Float by mutableFloatStateOf(0f)
    private var currentIndex: Int by mutableIntStateOf(0)
    private var targetIndex: Int by mutableIntStateOf(0)
//    private var targetItem: LazyListItemInfo? by mutableStateOf(null)

    fun onDragStart(offset: Offset) {
        lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { item ->
            offset.y.toInt() in item.offset..(item.offset + item.size)
        }?.also {
            draggableItemIndex = it.index
            draggingItem = it
        }
    }

    fun onDrag(dragOffset: Float) {
        draggableItemOffsetDelta += dragOffset
        val currentDraggableItemIndex = draggableItemIndex
            ?: return
        val currentDraggableItem =
            draggingItem ?: return
        val startOffset =
            currentDraggableItem.offset + draggableItemOffsetDelta
        val endOffset =
            currentDraggableItem.offset + currentDraggableItem.size + draggableItemOffsetDelta
        val middleOffset =
            startOffset + (endOffset - startOffset) / 2
        val targetItem =
            lazyListState.layoutInfo.visibleItemsInfo.find { item ->
                middleOffset.toInt() in item.offset..item.offset + item.size && currentDraggableItemIndex != item.index
            }
        if (targetItem != null) {
            draggableItemIndex = targetItem.index
            draggingItem = targetItem
            draggableItemOffsetDelta += currentDraggableItem.offset - targetItem.offset
            setDataIndexes(currentDraggableItemIndex, targetItem.index)
            onUpdateData(currentDraggableItemIndex, targetItem.index)
        }
    }

    fun onDragCancelOrInterrupted() {
        draggableItemOffsetDelta = 0f
        draggableItemIndex = null
        draggingItem = null
        currentIndex = 0
        targetIndex = 0
    }

    fun onDragEnd() {
        onReorderEnd(currentIndex, targetIndex)
        onDragCancelOrInterrupted()
    }

    private fun setDataIndexes(from: Int, to: Int) {
        currentIndex = from
        targetIndex = to
    }
}