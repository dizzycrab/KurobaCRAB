package com.github.k1rakishou.chan.core.manager

import com.github.k1rakishou.chan.utils.BackgroundUtils
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.BitSet

class BottomNavBarVisibilityStateManager {
  private val state = BitSet()
  private val _replyViewStateFlow = MutableSharedFlow<Unit>(
    replay = 1,
    extraBufferCapacity = 64
  ).also { it.tryEmit(Unit) }

  fun listenForViewsStateUpdates(): SharedFlow<Unit> {
    BackgroundUtils.ensureMainThread()
    return _replyViewStateFlow.asSharedFlow()
  }

  fun replyViewStateChanged(isCatalogReplyView: Boolean, isVisible: Boolean) {
    BackgroundUtils.ensureMainThread()

    val bit = if (isCatalogReplyView) CatalogReplyViewBit else ThreadReplyViewBit

    if (isVisible) {
      if (state[bit]) return
      state.set(bit)
    } else {
      if (!state[bit]) return
      state.clear(bit)
    }

    _replyViewStateFlow.tryEmit(Unit)
  }

  fun anyOfViewsIsVisible(): Boolean {
    BackgroundUtils.ensureMainThread()
    return state.nextSetBit(0) >= 0
  }

  fun isThreadReplyLayoutVisible(): Boolean = state.get(ThreadReplyViewBit)
  fun isCatalogReplyLayoutVisible(): Boolean = state.get(CatalogReplyViewBit)

  companion object {
    private const val CatalogReplyViewBit = 1
    private const val ThreadReplyViewBit = 2
  }
}
