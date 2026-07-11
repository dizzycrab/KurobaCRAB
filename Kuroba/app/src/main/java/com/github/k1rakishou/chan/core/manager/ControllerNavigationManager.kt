package com.github.k1rakishou.chan.core.manager

import com.github.k1rakishou.chan.ui.controller.base.Controller
import com.github.k1rakishou.chan.utils.BackgroundUtils
import com.github.k1rakishou.core_logger.Logger
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ControllerNavigationManager {
  private val _controllerNavigationFlow = MutableSharedFlow<ControllerNavigationChange>(
    extraBufferCapacity = 64
  )

  fun listenForControllerNavigationChanges(): SharedFlow<ControllerNavigationChange> {
    BackgroundUtils.ensureMainThread()
    return _controllerNavigationFlow.asSharedFlow()
  }

  fun onControllerPushed(controller: Controller) {
    BackgroundUtils.ensureMainThread()
    Logger.d(TAG, "onControllerPushed(${controller.javaClass.simpleName})")
    _controllerNavigationFlow.tryEmit(ControllerNavigationChange.Pushed(controller))
  }

  fun onControllerPopped(controller: Controller) {
    BackgroundUtils.ensureMainThread()
    Logger.d(TAG, "onControllerPopped(${controller.javaClass.simpleName})")
    _controllerNavigationFlow.tryEmit(ControllerNavigationChange.Popped(controller))
  }

  fun onControllerPresented(controller: Controller) {
    BackgroundUtils.ensureMainThread()
    Logger.d(TAG, "onControllerPresented(${controller.javaClass.simpleName})")
    _controllerNavigationFlow.tryEmit(ControllerNavigationChange.Presented(controller))
  }

  fun onControllerUnpresented(controller: Controller) {
    BackgroundUtils.ensureMainThread()
    Logger.d(TAG, "onControllerUnpresented(${controller.javaClass.simpleName})")
    _controllerNavigationFlow.tryEmit(ControllerNavigationChange.Unpresented(controller))
  }

  fun onControllerSwipedTo(controller: Controller) {
    BackgroundUtils.ensureMainThread()
    Logger.d(TAG, "onControllerSwipedTo(${controller.javaClass.simpleName})")
    _controllerNavigationFlow.tryEmit(ControllerNavigationChange.SwipedTo(controller))
  }

  fun onControllerSwipedFrom(controller: Controller) {
    BackgroundUtils.ensureMainThread()
    Logger.d(TAG, "onControllerSwipedFrom(${controller.javaClass.simpleName})")
    _controllerNavigationFlow.tryEmit(ControllerNavigationChange.SwipedFrom(controller))
  }

  fun onCloseAllNonMainControllers() {
    BackgroundUtils.ensureMainThread()
    Logger.d(TAG, "onCloseAllNonMainControllers()")
    // Do nothing here, other than logging
  }

  sealed class ControllerNavigationChange(val controller: Controller) {
    class Pushed(controller: Controller) : ControllerNavigationChange(controller)
    class Popped(controller: Controller) : ControllerNavigationChange(controller)
    class Presented(controller: Controller) : ControllerNavigationChange(controller)
    class Unpresented(controller: Controller) : ControllerNavigationChange(controller)
    class SwipedTo(controller: Controller) : ControllerNavigationChange(controller)
    class SwipedFrom(controller: Controller) : ControllerNavigationChange(controller)

    override fun toString(): String {
      return "CNC{${javaClass.simpleName}, controller=${controller.javaClass.simpleName}}"
    }
  }

  companion object {
    private const val TAG = "ControllerNavigationManager"
  }
}
